#!/usr/bin/env bash
# publish.sh — Release Reality Check to Maven Central.
#
# Full documentation: docs/PUBLISH.md
# GPG / GitHub secrets setup: docs/gpg-setup.md
#
# ── Quick start (recommended) ────────────────────────────────────────────────
#
#   bash scripts/publish.sh 1.1.0 --dry-run    # preflight only
#   bash scripts/publish.sh 1.1.0              # tag + push → CI publishes
#
# ── Version numbering (semver) ─────────────────────────────────────────────
#
#   1.0.0 → 1.1.0   backward-compatible API additions (new assertion methods, etc.)
#   1.1.0 → 1.1.1   bug fixes only
#   1.x.x → 2.0.0   breaking API changes
#
# Before releasing: add ## [X.Y.Z] - YYYY-MM-DD to CHANGELOG.md.
# The script refuses versions already on Maven Central or already tagged.
#
# ── One-time setup ───────────────────────────────────────────────────────────
#
# 1. Sonatype Central Portal — namespace io.github.imetaxas, user token
# 2. GPG key — generate, upload public key to keyserver (see docs/gpg-setup.md)
# 3. GitHub environment "maven-central" secrets:
#      CENTRAL_USERNAME, CENTRAL_TOKEN, GPG_PRIVATE_KEY, GPG_PASSPHRASE
#
# ── CI mode (default) ────────────────────────────────────────────────────────
#
# Preflight locally, then create annotated tag vX.Y.Z and push to origin.
# GitHub Actions (.github/workflows/ci.yml release job) will:
#   1. mvn versions:set from tag
#   2. mvn deploy -P release  (sign + publish to Central Portal)
#   3. gh release create
#   4. bump master to next patch SNAPSHOT (e.g. 1.1.1-SNAPSHOT)
#
# Approve the maven-central environment in Actions if prompted.
#
# ── Local mode (--local) ─────────────────────────────────────────────────────
#
# Publish from this machine. Requires:
#   ~/.m2/settings.xml  →  <server id="central"> with Central Portal token
#   GPG key in local keyring
#   export GPG_PASSPHRASE=...   (or MAVEN_GPG_PASSPHRASE)
#
# ── Options ──────────────────────────────────────────────────────────────────
#
#   bash scripts/publish.sh X.Y.Z              CI release via tag push
#   bash scripts/publish.sh X.Y.Z --dry-run    preflight only, no tag/push
#   bash scripts/publish.sh X.Y.Z --local      publish from this machine
#   bash scripts/publish.sh X.Y.Z --skip-verify skip mvn verify (not recommended)
#   bash scripts/publish.sh X.Y.Z --wait       wait for GitHub Actions after push
#   RELEASE_CONFIRM=yes bash scripts/publish.sh X.Y.Z   non-interactive
#
# ── Preflight checks (unless --skip-verify) ──────────────────────────────────
#
#   mvn verify
#   strict CommittedSnapshotTest (realitycheck-snapshot)
#   git diff --exit-code  (no modified golden files)
#   mvn javadoc:aggregate

set -euo pipefail

VERSION=""
MODE="ci"
DRY_RUN=false
SKIP_VERIFY=false
WAIT_FOR_CI=false

usage() {
  sed -n '2,/^set -euo pipefail$/p' "$0" | sed '$d' | sed 's/^# \{0,1\}//'
  exit "${1:-0}"
}

log() {
  printf '==> %s\n' "$*"
}

die() {
  printf 'error: %s\n' "$*" >&2
  exit 1
}

require_cmd() {
  command -v "$1" >/dev/null 2>&1 || die "missing required command: $1"
}

repo_root() {
  git rev-parse --show-toplevel
}

semver_ok() {
  [[ "$1" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]
}

next_snapshot() {
  local version="$1"
  echo "$version" | awk -F. '{printf "%s.%s.%d-SNAPSHOT\n", $1, $2, $3+1}'
}

central_version_exists() {
  local version="$1"
  local url="https://search.maven.org/solrsearch/select?q=g:io.github.imetaxas+AND+a:realitycheck-core+AND+v:${version}&rows=1&wt=json"
  local count
  count="$(curl -fsSL "$url" | python3 -c 'import json,sys; print(json.load(sys.stdin)["response"]["numFound"])' 2>/dev/null || echo 0)"
  [[ "$count" != "0" ]]
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    -h|--help)
      usage 0
      ;;
    --dry-run)
      DRY_RUN=true
      ;;
    --local)
      MODE="local"
      ;;
    --skip-verify)
      SKIP_VERIFY=true
      ;;
    --wait)
      WAIT_FOR_CI=true
      ;;
    --*)
      die "unknown option: $1 (try --help)"
      ;;
    *)
      if [[ -z "$VERSION" ]]; then
        VERSION="${1#v}"
      else
        die "unexpected argument: $1"
      fi
      ;;
  esac
  shift
done

[[ -n "$VERSION" ]] || usage 1
semver_ok "$VERSION" || die "version must look like X.Y.Z (got: $VERSION)"

ROOT="$(repo_root)"
cd "$ROOT"

TAG="v${VERSION}"
NEXT_SNAPSHOT="$(next_snapshot "$VERSION")"

require_cmd git
require_cmd mvn
require_cmd curl
require_cmd python3

log "Reality Check release ${VERSION} (${MODE} mode)"
log "Repository: $ROOT"
log "Documentation: docs/PUBLISH.md"

if [[ -n "$(git status --porcelain)" ]]; then
  die "working tree is not clean — commit or stash changes before releasing"
fi

BRANCH="$(git branch --show-current)"
if [[ "$MODE" == "ci" && "$BRANCH" != "master" && "$BRANCH" != "main" ]]; then
  die "CI releases must be cut from master/main (current: $BRANCH)"
fi

if git rev-parse "$TAG" >/dev/null 2>&1; then
  die "git tag $TAG already exists locally"
fi

if git ls-remote --exit-code --tags origin "$TAG" >/dev/null 2>&1; then
  die "git tag $TAG already exists on origin"
fi

if central_version_exists "$VERSION"; then
  die "${VERSION} is already on Maven Central — pick a new version"
fi

if ! grep -q "## \\[${VERSION}\\]" CHANGELOG.md 2>/dev/null; then
  log "warning: CHANGELOG.md has no '## [${VERSION}]' section yet"
fi

CURRENT_POM_VERSION="$(mvn -q -DforceStdout help:evaluate -Dexpression=project.version)"
log "Current pom version: ${CURRENT_POM_VERSION}"
log "Tag to create: ${TAG}"
log "Post-release SNAPSHOT (CI will set): ${NEXT_SNAPSHOT}"

if [[ "$SKIP_VERIFY" == false ]]; then
  log "Running full verify (same as CI build job)"
  mvn -B -ntp verify

  log "Verifying committed snapshots in strict mode"
  mvn -B -ntp -pl realitycheck-snapshot -am \
    -Dtest=CommittedSnapshotTest \
    -Dsurefire.failIfNoSpecifiedTests=false \
    -Drealitycheck.strict-ci=true test

  log "Checking tracked fixtures were not modified by tests"
  git diff --exit-code

  log "Validating aggregated Javadoc"
  mvn -B -ntp javadoc:aggregate -DskipTests
else
  log "Skipping mvn verify (--skip-verify)"
fi

if [[ "$DRY_RUN" == true ]]; then
  log "Dry run complete — no tag created, nothing pushed"
  exit 0
fi

confirm() {
  local prompt="$1"
  if [[ "${RELEASE_CONFIRM:-}" == "yes" ]]; then
    return 0
  fi
  printf '%s [y/N] ' "$prompt"
  read -r reply
  [[ "$reply" == "y" || "$reply" == "Y" ]]
}

release_via_ci() {
  if ! confirm "Create tag ${TAG} and push to origin to trigger CI publish?"; then
    die "aborted"
  fi

  require_cmd git
  git tag -a "$TAG" -m "Release ${TAG}"
  log "Pushing branch ${BRANCH}"
  git push origin "HEAD:refs/heads/${BRANCH}"
  log "Pushing tag ${TAG}"
  git push origin "$TAG"

  log "Tag pushed. GitHub Actions will:"
  log "  1. run build + release jobs"
  log "  2. deploy ${VERSION} to Maven Central (approve the maven-central environment if prompted)"
  log "  3. create GitHub Release ${TAG}"
  log "  4. bump master to ${NEXT_SNAPSHOT}"
  log ""
  log "Watch: https://github.com/imetaxas/realitycheck/actions"
  log "Verify: https://central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core/${VERSION}"

  if [[ "$WAIT_FOR_CI" == true ]]; then
    require_cmd gh
    log "Waiting for release workflow on tag ${TAG}"
    gh run watch --repo imetaxas/realitycheck "$(gh run list --repo imetaxas/realitycheck --workflow CI --limit 20 --json databaseId,headBranch -q ".[] | select(.headBranch==\"${TAG}\") | .databaseId" | head -n1)"
  fi
}

release_locally() {
  if ! confirm "Publish ${VERSION} to Maven Central from this machine?"; then
    die "aborted"
  fi

  if [[ ! -f "${HOME}/.m2/settings.xml" ]]; then
    die "~/.m2/settings.xml not found — configure <server id=\"central\"> (see docs/PUBLISH.md)"
  fi
  if ! gpg --list-secret-keys >/dev/null 2>&1; then
    die "no GPG secret keys found — see docs/gpg-setup.md"
  fi
  if [[ -z "${GPG_PASSPHRASE:-${MAVEN_GPG_PASSPHRASE:-}}" ]]; then
    die "set GPG_PASSPHRASE or MAVEN_GPG_PASSPHRASE for artifact signing"
  fi

  log "Setting release version in pom.xml"
  mvn -B -ntp versions:set -DnewVersion="${VERSION}" -DgenerateBackupPoms=false

  log "Deploying to Maven Central"
  MAVEN_GPG_PASSPHRASE="${GPG_PASSPHRASE:-${MAVEN_GPG_PASSPHRASE}}" \
    mvn -B -ntp deploy -P release -DskipTests -Dgpg.pinentry-mode=loopback

  if command -v gh >/dev/null 2>&1; then
    if confirm "Create GitHub Release ${TAG}?"; then
      git tag -a "$TAG" -m "Release ${TAG}"
      git push origin "$TAG"
      gh release create "$TAG" --generate-notes --title "Reality Check ${TAG}"
    fi
  else
    log "gh not installed — create the GitHub release manually after tagging"
  fi

  if confirm "Bump workspace to ${NEXT_SNAPSHOT}?"; then
    mvn -B -ntp versions:set -DnewVersion="${NEXT_SNAPSHOT}" -DgenerateBackupPoms=false
    git add -A
    git commit -m "chore: bump version to ${NEXT_SNAPSHOT}"
    log "Committed ${NEXT_SNAPSHOT} — push master when ready"
  fi

  log "Published ${VERSION} locally"
  log "Verify: https://central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core/${VERSION}"
}

case "$MODE" in
  ci) release_via_ci ;;
  local) release_locally ;;
  *) die "unknown mode: $MODE" ;;
esac
