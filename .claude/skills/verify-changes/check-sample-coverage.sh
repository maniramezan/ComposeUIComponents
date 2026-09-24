#!/usr/bin/env bash
# Lists public :components composables that no :sample page references.
# AGENTS.md requires every public component to have a configurable :sample demo.
#
# Usage: .claude/skills/verify-changes/check-sample-coverage.sh
# Exit code is 0 either way; review the output. Primitive overloads that are exercised
# through a data-driven counterpart (e.g. RowScope.TabBarItem via TabBar) may be listed.
set -euo pipefail

repo_root="$(git rev-parse --show-toplevel)"
components_dir="$repo_root/components/src/main/kotlin"
sample_dir="$repo_root/sample/src/main/java"

grep -rh "^public fun" "$components_dir" \
    | grep -v -E "(Preview|Showkase)\(\)" \
    | sed -E 's/public fun (<[^>]*> )?([A-Za-z.]*)\(.*/\2/' \
    | sed 's/.*\.//' \
    | sort -u \
    | while read -r name; do
        if ! grep -rqw "$name" "$sample_dir"; then
            echo "No :sample usage: $name"
        fi
    done
