package euler;

import java.util.Random;

public final class Problem185 {
    private Problem185() {}

    private static class Guess {
        final char[] digits;
        final int hits;

        Guess(String digits, int hits) {
            this.digits = digits.toCharArray();
            this.hits = hits;
        }
    }

    private static class Solver {
        private static final int NUM_DIGITS = 16;

        private final Guess[] guesses = {
            new Guess("5616185650518293", 2),
            new Guess("3847439647293047", 1),
            new Guess("5855462940810587", 3),
            new Guess("9742855507068353", 3),
            new Guess("4296849643607543", 3),
            new Guess("3174248439465858", 1),
            new Guess("4513559094146117", 2),
            new Guess("7890971548908067", 3),
            new Guess("8157356344118483", 1),
            new Guess("2615250744386899", 2),
            new Guess("8690095851526254", 3),
            new Guess("6375711915077050", 1),
            new Guess("6913859173121360", 1),
            new Guess("6442889055042768", 2),
            new Guess("2321386104303845", 0),
            new Guess("2326509471271448", 2),
            new Guess("5251583379644322", 2),
            new Guess("1748270476758276", 3),
            new Guess("4895722652190306", 1),
            new Guess("3041631117224635", 3),
            new Guess("1841236454324589", 3),
            new Guess("2659862637316867", 2)
        };

        private final Random rand;

        public Solver() {
            rand = new Random();
            rand.setSeed(59433336635907L); // for consistency
        }

        // shuffle a single digit
        private void shuffle(char[] digits, int i) {
            char d = digits[i];
            do {
                digits[i] = (char)(rand.nextInt(10) + '0');
            } while (d == digits[i]);
        }

        // compute how many digits of the guesses don't match to the
        // currectly guessed number.
        private int distance(char[] current) {
            int errors = 0;

            for (Guess guess : guesses) {
                // count how many of matching digits
                int same = 0;
                for (int j = 0; j < current.length; j++)
                    if (current[j] == guess.digits[j])
                        same++;

                // too many identical digits?
                if (same > guess.hits)
                    errors += same - guess.hits;
                else // or too few?
                    errors += guess.hits - same;
            }

            return errors;
        }

        public String solve() {
            // initialize a purely random guess
            char[] secret = new char[NUM_DIGITS];
            for (int i = 0; i < secret.length; i++) {
                shuffle(secret, i);
            }

            final int RoundThreshold = 10;
            int rounds = 0;
            int errors = distance(secret);
            int previous = errors;

            while (errors != 0) {
                // replace every digit by a different random number, keep
                // those that minimize the error metric
                for (int i = 0; i < secret.length; i++) {
                    // replace by a new random digit
                    char previousDigit = secret[i];
                    shuffle(secret, i);

                    int modified = distance(secret);
                    if (modified <= errors) {
                        // we have improvement, keep the digit
                        errors = modified;
                    } else {
                        // restore previous digit if no improvement
                        secret[i] = previousDigit;
                    }
                }

                if (errors == previous) {
                    // we didn't improve on the previous guess
                    if (++rounds == RoundThreshold) {
                        // stuck too long? try to escape local optimum by
                        // changing a random number
                        shuffle(secret, rand.nextInt(secret.length));
                        errors = distance(secret);
                        rounds = 0;
                    }
                } else {
                    // we got closer to the goal
                    rounds = 0;
                    previous = errors;
                }
            }

            return new String(secret);
        }
    }

    public static String solve() {
        return new Solver().solve();
    }

    public static void main(String[] args) {
        System.out.println(solve());
    }
}
