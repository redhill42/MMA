package euler;

import static euler.algo.Library.factorize;

public final class Problem47 {
    private Problem47() {}

    public static int solve() {
        int a = 1, b = 1, c = 1, d = 1, n = 1;
        while (!(a == 4 && b == 4 && c == 4 && d == 4)) {
            a = b;
            b = c;
            c = d;
            d = factorize(n+3).nu();
            n++;
        }
        return n - 1;
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
