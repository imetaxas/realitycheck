# Publishing to Maven Central

This guide covers how to release Reality Check artifacts to
[Maven Central](https://central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core).

The recommended path is **`scripts/publish.sh`** in CI mode: run preflight checks locally,
push a version tag, and let GitHub Actions deploy, create the GitHub Release, and bump
`master` to the next SNAPSHOT.

For GPG key generation and GitHub secret setup, see [gpg-setup.md](gpg-setup.md).

---

## Version numbering

Reality Check follows [Semantic Versioning](https://semver.org/):

| Change type | Example bump |
|---|---|
| Backward-compatible API additions | `1.0.0` → `1.1.0` |
| Backward-compatible bug fixes only | `1.1.0` → `1.1.1` |
| Breaking API changes | `1.x.x` → `2.0.0` |

Before tagging, update `CHANGELOG.md` with a `## [X.Y.Z]` section and release date.

The publish script refuses to release a version that already exists on Maven Central or as a
git tag.

---

## One-time setup

### 1. Sonatype Central Portal

1. Sign in at [central.sonatype.com](https://central.sonatype.com).
2. Verify the namespace `io.github.imetaxas` is registered.
3. Generate a user token (username + password).

### 2. GPG signing key

Follow [gpg-setup.md](gpg-setup.md) to:

- Generate a 4096-bit RSA key
- Upload the public key to a keyserver
- Store `GPG_PRIVATE_KEY` and `GPG_PASSPHRASE` in GitHub

### 3. GitHub environment secrets

In **Settings → Environments → `maven-central`**, configure:

| Secret | Purpose |
|---|---|
| `CENTRAL_USERNAME` | Central Portal token username |
| `CENTRAL_TOKEN` | Central Portal token password |
| `GPG_PRIVATE_KEY` | Armored private key block |
| `GPG_PASSPHRASE` | GPG key passphrase |

The `release` job in `.github/workflows/ci.yml` also requires manual approval on the
`maven-central` environment before secrets are exposed.

### 4. Local tooling (optional)

- `gh` CLI — for `--wait` and GitHub Release creation in local mode
- `~/.m2/settings.xml` — only required for `--local` publishes (see below)

---

## Release checklist

1. Merge all changes to `master`.
2. Add `## [X.Y.Z] - YYYY-MM-DD` to `CHANGELOG.md`.
3. Ensure CI is green on `master`.
4. Run a dry run:

   ```bash
   bash scripts/publish.sh X.Y.Z --dry-run
   ```

5. Publish:

   ```bash
   bash scripts/publish.sh X.Y.Z
   ```

6. Approve the `maven-central` environment in GitHub Actions if prompted.
7. Verify artifacts at
   [central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core/X.Y.Z](https://central.sonatype.com/artifact/io.github.imetaxas/realitycheck-core).

---

## CI release (recommended)

```bash
bash scripts/publish.sh 1.1.0              # preflight + tag + push
bash scripts/publish.sh 1.1.0 --dry-run    # preflight only
bash scripts/publish.sh 1.1.0 --wait        # also wait for Actions to finish
RELEASE_CONFIRM=yes bash scripts/publish.sh 1.1.0   # non-interactive
```

### What the script checks

- Clean working tree
- On `master` or `main` (CI mode)
- Version not already tagged or on Maven Central
- `mvn verify` (full reactor)
- Strict snapshot verification (`CommittedSnapshotTest`)
- No tracked test fixtures modified (`git diff --exit-code`)
- Aggregated Javadoc build

### What happens after the tag push

GitHub Actions (`.github/workflows/ci.yml`, `release` job):

1. Sets `pom.xml` version from the tag (`v1.1.0` → `1.1.0`)
2. Runs `mvn deploy -P release` (sources, Javadoc, GPG sign, Central Portal publish)
3. Creates a GitHub Release with auto-generated notes
4. Bumps `master` to `1.1.1-SNAPSHOT` and pushes the commit

Watch progress: [github.com/imetaxas/realitycheck/actions](https://github.com/imetaxas/realitycheck/actions)

---

## Local release (advanced)

Use only when CI is unavailable. You are responsible for version bumps and the GitHub Release.

### Maven `settings.xml`

Add a server entry with id `central` (matches `pom.xml` `publishingServerId`):

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_CENTRAL_PORTAL_USERNAME</username>
      <password>YOUR_CENTRAL_PORTAL_TOKEN</password>
    </server>
  </servers>
</settings>
```

### Publish

```bash
export GPG_PASSPHRASE='your-passphrase'
bash scripts/publish.sh 1.1.0 --local
```

The script runs `mvn versions:set`, `mvn deploy -P release`, and optionally creates the
GitHub Release and bumps to the next SNAPSHOT.

---

## Script options

| Flag | Description |
|---|---|
| `--dry-run` | Run preflight checks only; no tag or deploy |
| `--local` | Publish from this machine instead of pushing a tag |
| `--skip-verify` | Skip `mvn verify` and related checks (not recommended) |
| `--wait` | After tag push, wait for the GitHub Actions release run (`gh` required) |
| `--help` | Print usage |
| `RELEASE_CONFIRM=yes` | Skip interactive confirmation prompts |

---

## Troubleshooting

**`version is already on Maven Central`**

Pick the next semver version. Released coordinates cannot be overwritten.

**`git tag vX.Y.Z already exists`**

Delete the local tag (`git tag -d vX.Y.Z`) or choose a new version. Never force-push over a
tag that was already published.

**CI release job waiting for approval**

Open the workflow run → approve the `maven-central` environment.

**`gpg: signing failed`**

See [gpg-setup.md](gpg-setup.md). Local publishes need `-Dgpg.pinentry-mode=loopback` and
`GPG_PASSPHRASE` set (the script handles both).

**Central rejects signatures**

The public GPG key may not have propagated to keyservers yet. Wait up to 10 minutes and retry.

**`working tree is not clean`**

Commit or stash local changes before releasing.

---

## Modules published

All modules share the parent version. Consumers typically depend on:

- `io.github.imetaxas:realitycheck-core`
- `io.github.imetaxas:realitycheck-junit5`
- `io.github.imetaxas:realitycheck-bom` (version alignment)

Or use the BOM:

```xml
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.github.imetaxas</groupId>
      <artifactId>realitycheck-bom</artifactId>
      <version>X.Y.Z</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```
