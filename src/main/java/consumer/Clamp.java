package consumer;

public final class Clamp {
    private Clamp() {
    }

    public static int clamp(int x, int lo, int hi) {
        return Math.max(lo, Math.min(hi, x));
    }

    /** Deliberately buggy: index == length when score == 100. */
    public static char gradeBand(int score) {
        char[] bands = {'F', 'D', 'C', 'B', 'A'};
        return bands[score / 20];
    }
}
