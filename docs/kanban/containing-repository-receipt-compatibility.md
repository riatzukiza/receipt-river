---
uuid: 4caf6f31-9e54-49e0-b6fc-34d2d8414ff0
title: "Read historical receipts in their containing repository context"
status: incoming
priority: P1
points: 3
labels: receipt-river, compatibility, cephalon
---

# Read historical receipts in their containing repository context

## Context

The user directs: append a correction or leave old receipt bytes unchanged;
the containing repository is the easiest place to imply attribution. Shared
receiver PR8 retains three older records without `:repo`. Its current owning
Receipt River compatibility reader rejects those records as missing a required
key, even though their file and checkout supply the repository context.
Do not rewrite them or ask again for rewrite permission.

This repository owns historical unversioned compatibility, repository discovery,
receipt validation and the exported `validateLine` API. Main154440f3 currently
has one-argument `record-errors` and two-argument `validate-line`; its strict
version1 envelope also calls the legacy payload validator and publishes
`legacy-required-keys` as its schema required fields. Simply removing `:repo`
from that shared constant would weaken the declared schema contract.

## Outcome

Let the existing reader use an explicit containing-repository context to
interpret a historical unversioned receipt that has no `:repo` field. Keep
original EDN bytes, parsed original record, source schema status and validation
errors separately inspectable. Inferred attribution is derived reader context,
never a field silently inserted into the historical receipt or a new attestation.

## Scope

- Add a small pure compatibility decision at the existing law boundary, with
  context-aware arities through the existing programmatic API. Keep the old
  arities' behavior unchanged when no containing context is available.
- Freeze the pure context as the closed CLJS map
  `{:repository/path "<actual containing checkout path>"}`: exactly that key,
  with a nonblank string value. The existing exported API gains
  `validateLine(line, lineNumber, containingRepositoryPath)` with a third
  **string** argument; the API edge constructs that pure map. A JS object is
  not implicitly converted into a CLJS map and remains an invalid context.
  Preserve the existing CLJS result representation; do not add a second API.
  The API edge owns checking that third argument is a nonblank string before
  constructing the map. Nil, blank and non-string arguments are refused as
  attribution input: construct no map and retain context-free validation/errors,
  without adding attribution or throwing a new protocol error. The pure law
  independently rejects malformed or extra-key context maps supplied directly.
- Context has a nonblank repository path supplied by an outer reader from its
  actual containing file/checkout. Document this as an attribution input, not
  authenticated repository identity, ancestry or ownership proof.
- Apply the exception only when the record is a map, has no `:event/schema`
  declaration and has no `:repo` key. An explicitly present nil value remains
  invalid; never replace an existing value with context. Retain all other
  existing kind, timestamp, required-field and parsing checks.
- Keep declared version1 payload requirements, registry/document bytes and new
  event construction strict. A versioned payload missing `:repo` is still
  refused even with containing context. No schema version is assigned to an
  unwrapped historical record.
- Expose derived containing attribution separately from unchanged `:event` and
  `:line`, with its basis and epistemic tier stated. Do not turn source context
  into validation success for another malformed field or into evidence approval.
- The context-aware result retains the exact context-free diagnostics at
  `:source/context-free-errors`, including the original missing-repo error;
  `:errors` contains effective compatibility errors in their original order.
  Only the specific permitted missing-repo error may disappear from the
  effective vector. When that exception is applied, separate attribution is
  `:source/repository {:path "<supplied path>" :basis :containing-repository
  :tier :derived}`. This attribution can coexist with remaining validation
  errors; it does not set `:ok` independently or attest receipt truth. No
  attribution is emitted when the exception does not apply. Context-free
  arities preserve their existing result and diagnostic behavior.
- Thread existing CLI repository discovery into its existing file-validation
  call. Consumer integration in eta-mu follows the qualified library source;
  the receiver must actually supply its ledger's containing checkout context.
  Passing the context-free old call must not acquire a hidden permissive default.
- Preserve existing newline/physical-line behavior. Absolute ordinal changes,
  `.ημ` path discovery improvements and archaeology changes are separate scope.

## Non-goals

Historical receipt edits, new receipt CLI or validator outside Receipt River,
result or approval promotion, schema-version retrofit, arbitrary metadata
corrections, ignoring another finding, credential installation, paid reviews,
new CI semantics, graph/mood/social implementation or runtime deployment.
This is separate from Foresight PR28's four-field bound documentary envelope
view; do not mix the two reader contracts or transfer approvals between them.

## Acceptance criteria

1. A frozen real receiver suffix retains its three original physical lines
   byte-for-byte: 10437bytes/SHA256
   `592b8c95c84c82d3c9df8d4291e7aa768f3ebe9242aa6971e81be21550ad6cfa`,
   source PR8head1c4c53a8af725322d9b6d508d4d165a3dc979685, lines256-258.
   Each is still unversioned and contains no newly persisted `:repo` field.
2. With valid containing-repository path context, otherwise valid unversioned
   records missing `:repo` validate and disclose derived attribution separately.
   Raw line and parsed original equality are preserved. With no, nil, blank or
   wrong-shaped context they retain the existing missing-repo failure.
3. Existing explicit `:repo` values are not overwritten. A present nil value is
   still rejected; other existing legacy validation behavior is unchanged,
   including explicit non-nil values such as false. Do not tighten a separate
   historical field contract as part of this missing-field repair.
4. Missing timestamp/kind/owner and other required fields, unsupported kind,
   invalid timestamp and invalid EDN remain invalid with containing context.
5. Declared version1 envelopes with missing payload repo remain invalid under
   all context cases. Existing envelope schema documents/registry and component
   versioning behavior stay intact. Historical records acquire no schema.
   Presence of nil/malformed `:event/schema` still takes the declared-envelope
   route and cannot activate unversioned compatibility.
6. The built public API and existing CLI route exercise the same owning law.
   A context-aware receiver integration is required before its own native
   findings can settle; a library-local fixture pass is insufficient.

## Verification

Planning is prospective, no behavioral tests or source repair yet. Read the
existing `event_test.cljs` seams, `law/receipt.cljs`, `api.cljs`, `infra/cli.cljs`
and ESM exports. Retain real receiver original suffix and source identities when
RED is authored; test the public boundary, strict envelope refusal and explicit
nil/malformed-context negatives. Native Rheos Ready precedes failing laws/tests;
then implement pure decision before adapters. Run owning `pnpm test`, `pnpm
build`, `pnpm lint:kondo` and diff hygiene with actual tool results visible.
The current owning law was separately applied read-only to the exact retained
receiver lines256-258: each has no repo/schema key and returns only
`missing required key: repo`. Initial wrong `.ημ/receipts.edn` source selection
was refused; actual source is `receipts.edn`, and only the corrected immutable
read/hash check is verification. This is baseline diagnosis, not a new test or
repair pass. Independent local planning assessment identified the two context
shape/diagnostic ambiguities above; its three-point estimate remains provisional
and supplies no native provider approval or readiness.

This personal fork currently has no CI/review workflow or GitHub Actions secret
names. Missing MiMo execution surface is not a quota exclusion or approval.
Qualify a supported canonical review route and mandatory gates before claiming
planning/code convergence; no fabricated provider identity or local waiver.
No existing board configuration/cards were found, so this first manual incoming
Markdown card is a proposal only. Installed Rheos admission is not claimed.

## Risks

A permissive default context could disguise a malformed new receipt. A caller
could misattribute a file by passing a guessed path; document what is derived
and verify its actual file/checkout binding at the consumer. Shared legacy and
version1 schema predicates must not accidentally weaken together. Consumer
copies of donor source are not the canonical library repair; integration must
preserve reviewed source/provenance and actually pass the receiver's native gate.

## Character goal relationship

This bounded prerequisite removes a provenance-reading obstruction for the
reviewed shared receiver used by the physical field work. It does not implement
or complete encounter -> physical graph/field -> separate persistent mood and
attention -> associative recall -> choice -> observed outcome -> memory, social
relationships or character evolution. Do not replace that goal with receipts.
