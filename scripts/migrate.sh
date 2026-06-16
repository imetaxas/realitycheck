#!/usr/bin/env bash
# migrate.sh — Migrate test imports from AssertJ or Google Truth to Reality Check.
#
# Usage:
#   ./scripts/migrate.sh [directory]    # defaults to src/test
#
# What it does:
#   1. Replaces static import of org.assertj.core.api.Assertions.* → RealityAssertions.*
#   2. Replaces static import of com.google.common.truth.Truth.* → RealityAssertions.*
#   3. Replaces Truth8 import
#   4. Adjusts isAtLeast/isAtMost to the RC equivalents (same names, just different import)
#
# What it does NOT do:
#   - Handle extracting(), usingRecursiveComparison(), or other deep AssertJ patterns
#   - Those need manual review — search for "TODO: manual migration" after running
#
# Always review the diff before committing.

set -euo pipefail

TARGET="${1:-src/test}"
RC_IMPORT="com.yanimetaxas.realitycheck.RealityAssertions"

if [ ! -d "$TARGET" ]; then
  echo "Error: directory '$TARGET' not found"
  exit 1
fi

echo "Migrating Java test files in: $TARGET"
echo "======================================="

count=0

find "$TARGET" -name '*.java' -type f | while read -r file; do
  changed=false

  # AssertJ imports
  if grep -q 'org\.assertj\.core\.api\.Assertions' "$file" 2>/dev/null; then
    sed -i.bak \
      's|import static org\.assertj\.core\.api\.Assertions\.\*;|import static '"$RC_IMPORT"'.*;|g' \
      "$file"
    sed -i.bak \
      's|import static org\.assertj\.core\.api\.Assertions\.|import static '"$RC_IMPORT"'.|g' \
      "$file"
    sed -i.bak \
      's|import org\.assertj\.core\.api\.Assertions;|import '"$RC_IMPORT"';|g' \
      "$file"
    changed=true
  fi

  # AssertJ SoftAssertions
  if grep -q 'org\.assertj\.core\.api\.SoftAssertions' "$file" 2>/dev/null; then
    sed -i.bak \
      's|import org\.assertj\.core\.api\.SoftAssertions;|import static com.yanimetaxas.realitycheck.Reality.checkAll;|g' \
      "$file"
    # Flag for manual review
    sed -i.bak \
      's|SoftAssertions\.assertSoftly|checkAll /* TODO: manual migration — change lambda body from softly.assertThat to softly.checkThat */|g' \
      "$file"
    changed=true
  fi

  # Google Truth imports
  if grep -q 'com\.google\.common\.truth\.Truth' "$file" 2>/dev/null; then
    sed -i.bak \
      's|import static com\.google\.common\.truth\.Truth\.\*;|import static '"$RC_IMPORT"'.*;|g' \
      "$file"
    sed -i.bak \
      's|import static com\.google\.common\.truth\.Truth\.|import static '"$RC_IMPORT"'.|g' \
      "$file"
    sed -i.bak \
      's|import static com\.google\.common\.truth\.Truth8\.\*;|import static '"$RC_IMPORT"'.*;|g' \
      "$file"
    changed=true
  fi

  # Clean up backup files
  rm -f "${file}.bak"

  if [ "$changed" = true ]; then
    echo "  Migrated: $file"
    count=$((count + 1))
  fi
done

echo ""
echo "Done. Review changes with: git diff"
echo "Search for remaining manual work: grep -r 'TODO: manual migration' $TARGET"
