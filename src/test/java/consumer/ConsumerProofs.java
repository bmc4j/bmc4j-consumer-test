package consumer;

import org.bmc4j.Bmc;
import org.bmc4j.BmcProof;
import org.bmc4j.Verdict;

/**
 * The consumer smoke: two proofs that together exercise the full installed product —
 * plugin resolution by coordinates, the runtime, the bundled engine for this platform
 * (extraction + a real jbmc run), the models (Math.min/max), and the refutation UX.
 * Both PASS when the product works, so a green {@code gradlew test} is the assertion.
 */
class ConsumerProofs {

    /** Expected verdict: VERIFIED — clamp's result is within bounds for every input. */
    @BmcProof
    void clamp_result_always_within_bounds() {
        int x = Bmc.anyInt(), lo = Bmc.anyInt(), hi = Bmc.anyInt();
        Bmc.assume(lo <= hi);
        int r = Clamp.clamp(x, lo, hi);
        Bmc.check(r >= lo && r <= hi);
    }

    /**
     * Expected verdict: REFUTED — the deliberate bug fires at exactly score = 100.
     * This proves the refutation pipeline end-to-end (counterexample + replay file);
     * if it ever came back VERIFIED, the installed product would be broken.
     */
    @BmcProof(expect = Verdict.REFUTED)
    void gradeBand_never_throws_for_valid_scores() {
        int score = Bmc.anyInt();
        Bmc.assume(score >= 1 && score <= 100);
        Clamp.gradeBand(score);
    }
}
