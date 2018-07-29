package euler;

public final class Problem181 {
    private Problem181() {}

    public static long solve(int maxBlack, int maxWhite) {
        long[][] previous = new long[maxBlack + 1][maxWhite + 1];
        long[][] current  = new long[maxBlack + 1][maxWhite + 1];
        previous[0][0] = 1;

        // for all subsets
        for (int B = 0; B <= maxBlack; B++) {
            for (int W = 0; W <= maxWhite; W++) {
                if (B == 0 && W == 0)
                    continue;

                // put subset at every possible position
                for (int i = 0; i <= maxBlack; i++)
                    for (int j = 0; j <= maxWhite; j++) {
                        current[i][j] = 0;
                        for (int k = 0; k * B <= i && k * W <= j; k++)
                            current[i][j] += previous[i - k * B][j - k * W];
                    }

                // copy for next iteration
                for (int i = 0; i <= maxBlack; i++)
                    System.arraycopy(current[i], 0, previous[i], 0, maxWhite+1);
            }
        }

        return current[maxBlack][maxWhite];
    }

    public static void main(String[] args) {
        System.out.println(solve(60, 40));
    }
}
