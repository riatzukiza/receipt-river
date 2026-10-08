---
uuid: 7d593487-8163-41e4-97f2-63a9a4bb87fb
title: "Restore the compiled discovery test's Git boundary fixture"
status: incoming
priority: P1
points: 1
labels: receipt-river, tests, later-batch
---

# Restore the compiled discovery test's Git boundary fixture

## Context

First hosted run37734293522 on exact35237a3aec20b2c07bf655f9fa1093a292d88a22
compiled the existing test target, then discovery-test crashed with an arity2
property TypeError at the existing Git boundary call. Actual deterministic
artifact11531365779 retains test exit1. Neither producer SUCCESS nor build and
lint passes makes the test suite pass.

This newly discovered work is recorded for a later batch under the accepted
finite-inventory discipline. It is not admitted to the current milestone without
the human's scope decision. The existing containing-repository plan
4caf6f31-9e54-49e0-b6fc-34d2d8414ff0 remains blocked by the mandatory test result.

## Outcome

Run the complete existing compiled test suite without this fixture crash and
retain the bounded-concurrency assertions at their original semantic strength.
The one-point estimate is provisional until independent planning review.

## Scope

Identify the actual failing existing `with-redefs` seam and reproduce the
compiled Node22.20.0 call. The production `git/exec-at` is a multi-arity function;
the current delayed test replacement is a single-arity function. Compiled calls
to the original var may use its arity property. Verify this proposed cause
before repairing the replacement's shape. Keep production discovery, Git timeout,
identity and repository attribution semantics unchanged.

## Non-goals

Receipt compatibility implementation, historical receipt rewrites, unrelated
test suppression, narrowed namespace selection, compiler or package policy
migration, optional-test classification, runtime changes and character features.

## Acceptance criteria

1. The retained old-head hosted output remains inspectable as a real failure;
   its incomplete suite is never relabeled as successful tests.
2. A targeted reproduction establishes the causal fixture/call-shape mismatch,
   or records a different verified cause before any repair.
3. The existing complete `pnpm test` passes with its bounded-concurrency test
   and all original assertions still executed; no skip or selection workaround.
4. Existing build, lint and exact-clean-head hosted gates pass on the actual
   candidate. Publish complete review input and retain native review and gate
   evidence before qualification.

## Verification

Scope acceptance, qualified planning review and native Rheos admission precede
repair. Preserve the old compiled failure, test and Git-boundary source hashes,
then demonstrate failure before the minimal fixture change and success after it.
Run owning package commands, not a substitute local validator or smaller suite.

## Risks

The observed error identifies a call seam, not yet a proven root cause. Avoid
changing production behavior to accommodate an incorrectly shaped test double.
Unlocked dependency resolution is a separate observed limit; one successful
run would not prove all future dependency versions work.
