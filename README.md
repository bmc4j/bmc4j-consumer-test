# bmc4j-consumer-test

The consumer end-to-end test for [bmc4j](https://github.com/bmc4j/bmc4j): a fresh,
dependency-only project that installs bmc4j **by coordinates** — no `includeBuild`,
no source checkout — and proves the published product actually works the way a
user's build experiences it.

Two proofs, both of which **pass** when the product is healthy:

- `clamp_result_always_within_bounds` — VERIFIED: plugin resolution, the runtime,
  this platform's bundled engine (extraction + a real jbmc run), and the models.
- `gradeBand_never_throws_for_valid_scores` — declares `expect = Verdict.REFUTED`
  over a deliberate off-by-one: proves the refutation pipeline (counterexample +
  replay) end-to-end. If this ever came back VERIFIED, the install is broken.

So the whole assertion is: **`./gradlew test` is green.**

## Running

CI: dispatch the **Consumer test** workflow with the version to consume — it runs
the suite on linux, windows, and macOS. No secrets needed: GitHub Packages requires
*some* authenticated token for Maven reads even on public packages, and the
workflow's own `GITHUB_TOKEN` (with `packages: read`) satisfies that for a public
repo's packages.

Locally (any token with `read:packages` works, e.g. `gh auth token`):

```bash
# against GitHub Packages
GITHUB_ACTOR=<user> GITHUB_TOKEN=$(gh auth token) ./gradlew test -PbmcVersion=0.1.0

# against a local build of the main repo
(cd ../bmc4j && ./gradlew -p core publishToMavenLocal)
./gradlew test
```

`mavenLocal` is first in the repository order, so a locally published snapshot
wins during development; CI has no mavenLocal, so it always exercises the real
remote channel.
