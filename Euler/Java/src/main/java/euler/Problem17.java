package euler;

public final class Problem17 {
    private Problem17() {}

    private static class Solver {
        private final String[] childhood = {
            "zero", "one", "two", "three", "four",
            "five", "six", "seven", "eight", "nine"
        };

        private final String[] teens = {
            "ten", "eleven", "twelve", "thirteen", "fourteen",
            "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"
        };

        private final String[] multiple_of_tens = {
            "", "",
            "twenty", "thirty", "forty", "fifty",
            "sixty", "seventy", "eighty", "ninety"
        };

        private String numerals(int n) {
            if (n < 10)
                return childhood[n];
            if (n <= 19)
                return teens[n - 10];
            if (n < 100 && n % 10 == 0)
                return multiple_of_tens[n / 10];
            if (n < 100)
                return multiple_of_tens[n / 10] + numerals(n % 10);
            if (n < 1000 && n % 100 == 0)
                return numerals(n / 100) + "hundred";
            if (n < 1000)
                return numerals(n / 100) + "hundredand" + numerals(n % 100);
            if (n < 1_000_000 && n % 1000 == 0)
                return numerals(n / 1000) + "thousand";
            if (n < 1_000_000)
                return numerals(n / 1000) + "thousand" + numerals(n % 1000);
            if (n < 1_000_000_000 && n % 1_000_000 == 0)
                return numerals(n / 1_000_000) + "million";
            if (n < 1_000_000_000)
                return numerals(n / 1_000_000) + "million" + numerals(n % 1_000_000);
            throw new IllegalArgumentException("The argument is out of range: " + n);
        }

        public int solve(int limit) {
            int sum = 0;
            for (int n = 1; n <= limit; n++)
                sum += numerals(n).length();
            return sum;
        }
    }

    public static int solve(int limit) {
        return new Solver().solve(limit);
    }

    public static void main(String[] args) {
        System.out.println(solve(1000));
    }
}
