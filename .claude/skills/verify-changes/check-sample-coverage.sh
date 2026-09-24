#!/usr/bin/env bash
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/../../.." && pwd)"
python3 - "$repo_root" <<'PY'
from pathlib import Path
import re
import sys

root = Path(sys.argv[1])
components = root / "components/src/main/kotlin/io/github/maniramezan/compose/components"
sample = root / "sample/src/main/java"
sample_text = "\n".join(path.read_text() for path in sample.rglob("*.kt"))
missing = {}

for path in components.rglob("*.kt"):
    if path.name.endswith(("Preview.kt", "Showkase.kt")):
        continue
    source = path.read_text()
    # The repository places @Composable immediately above its public functions.
    for match in re.finditer(
        r"@Composable\s+(?:@[A-Za-z][\w.]*(?:\([^)]*\))?\s+)*public\s+fun\s+"
        r"(?:<[^\n>]+>\s*)?([A-Za-z][A-Za-z0-9_]*)\s*\(",
        source,
    ):
        name = match.group(1)
        if not name[0].isupper():
            continue  # Lowercase composable helpers are not component entry points.
        if not re.search(r"\b" + re.escape(name) + r"\s*\(", sample_text):
            missing.setdefault(name, set()).add(str(path.relative_to(root)))

if missing:
    print("Public composables with no call in :sample (review each):")
    for name, paths in sorted(missing.items()):
        print(f"  {name}: {', '.join(sorted(paths))}")
else:
    print("Every detected public component composable has a call in :sample.")
PY
