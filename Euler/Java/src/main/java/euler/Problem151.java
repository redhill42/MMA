package euler;

public final class Problem151 {
    private Problem151() {}

    private static double cut(int[] sheets) {
        int numSheets = 0;
        for (int s : sheets)
            numSheets += s;

        double singles = 0;
        if (numSheets == 1) {
            if (sheets[sheets.length - 1] == 1)
                return 0;
            singles = 1;
        }

        for (int i = 0; i < sheets.length; i++) {
            if (sheets[i] == 0)
                continue;

            int[] next = sheets.clone();
            next[i]--;
            for (int j = i + 1; j < next.length; j++)
                next[j]++;
            singles += cut(next) * sheets[i] / numSheets;
        }
        return singles;
    }

    public static double solve() {
        int[] sheets = {1, 1, 1, 1};
        return cut(sheets);
    }

    public static void main(String[] args) {
        System.out.printf("%.6f%n", solve());
    }
}
