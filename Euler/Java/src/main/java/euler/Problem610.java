package euler;

import java.util.HashSet;
import java.util.Set;

import euler.algo.Rational;

import static euler.algo.Library.fromRomanNumeral;
import static euler.algo.Library.toRomanNumeral;

public final class Problem610 {
    private Problem610() {}

    private static class Solver {
        private final Rational letterProb, termProb;
        private final Set<String> roman = new HashSet<>();
        private static final char[] letters = {'#', 'I', 'V', 'X', 'L', 'C', 'D', 'M'};

        Solver(int letterProb, int termProb) {
            this.letterProb = Rational.valueOf(letterProb, 100);
            this.termProb = Rational.valueOf(termProb, 100);

            for (int n = 0; n < 1000; n++) {
                roman.add(toRomanNumeral(n));
            }
        }

        Rational solve() {
            Rational res = compute("");
            return res.add(letterProb.multiply(1000).divide(Rational.ONE.subtract(letterProb)));
        }

        private Rational compute(String s) {
            Rational p = Rational.ZERO;
            StringBuilder v = new StringBuilder();

            for (char c : letters) {
                if (c == '#' || roman.contains(s + c)) {
                    p = p.add(prob(c));
                    v.append(c);
                }
            }

            Rational E = Rational.ZERO;
            for (int i = v.length() - 1; i >= 0; i--) {
                char c = v.charAt(i);
                Rational x = prob(c).divide(p);
                if (c == '#') {
                    x = x.multiply(fromRomanNumeral(s));
                } else {
                    x = x.multiply(compute(s + c));
                }
                E = E.add(x);
            }

            return E;
        }

        private Rational prob(char c) {
            return (c == '#') ? termProb : letterProb;
        }
    }

    public static double solve(int letterProb, int termProb) {
        return new Solver(letterProb, termProb).solve().doubleValue();
    }

    public static void main(String[] args) {
        System.out.printf("%.8f%n", solve(14, 2));
    }
}
