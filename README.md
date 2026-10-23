# @eta-mu/receipt-river

Receipt River owns receipt event construction, schema metadata, validation,
historical receipt compatibility, local repository discovery, and
receipt-specific archaeology projections.

Schema facts live directly in `eta-mu.receipt-river.law.receipt`; package APIs
and the eta-mu composition manifest consume that CLJS authority without a
prebuild code-generation step.

The canonical application surface is:

```bash
eta-mu receipt ...
```

Historical records without an event envelope remain unversioned. The package
validator reads them for compatibility; it does not assign a schema version
retroactively.

Repository archaeology preserves clone and worktree identity separately. It
reports added worktrees distinctly from added clones, and emits an explicit
ambiguous location-change observation when repository evidence cannot establish
which new path corresponds to which removed path. Git probes have bounded
lifetimes so a stalled child process cannot block discovery indefinitely.

## Explicit completion of missing historical metadata (candidate API)

`validateLedger(lines)` keeps every raw UTF-8 source line and its validation
result. Its `raw-ok` flag never becomes true because of a later correction.
A valid versioned adjudication receipt may declare
`:decisions {:receipt-river/metadata-completion {:version 1 :line-number N
:sha256 "<64 hex source line hash>" :fields {:repo "..." :pi "..."}}}`.
Only absent `repo` and `pi` fields may be supplied, and exactly all missing
fields must be supplied as nonblank strings. The target must precede the
adjudication, be unversioned, and have no other validation errors. The SHA256
covers the exact source line without its line terminator. Multiple completions
for one line, invalid adjudications, changed semantic fields and wrong hashes
refuse admission. The derived completed record and correction location remain
explicit beside the unchanged invalid row.

`ok` describes admission with those explicit completions; it does not assert
raw historical validity, producer authentication, approval or deployment trust.
This candidate does not compose independent reflection ledgers or resolve Git
conflicts. Consumers must retain their stream and commit provenance, and adopt
the owner policy through review before using it as a release gate.
