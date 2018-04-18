package euler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static euler.util.Utils.isqrt;

public final class Problem174 {
    private Problem174() {}

    private static class Solver {
        private final int[] table;
        private final int[] count;

        public Solver(int limit) {
            this.table = new int[limit + 1];
            this.count = new int[limit + 1];

            int n = limit / 4;
            for (int a = 1, m = isqrt(n); a < m; a++) {
                for (int b = 1; ; b++) {
                    int k = 4 * a * (a + b);
                    if (k > limit)
                        break;
                    ++table[k];
                }
            }

            int s = 0;
            for (int i = 0; i <= limit; i++) {
                if (table[i] >= 1 && table[i] <= 10)
                    s++;
                count[i] = s;
            }
        }

        public int solve(int k) {
            return count[k];
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args)
        throws IOException
    {
        List<Integer> inputs = new ArrayList<>();
        int limit = 0;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = new PrintStream(new BufferedOutputStream(System.out));
        int t = nextInt(in);
        while (--t >= 0) {
            int k = nextInt(in);
            if (k > limit)
                limit = k;
            inputs.add(k);
        }

        Solver solver = new Solver(limit);
        for (int k : inputs) {
            out.println(solver.solve(k));
        }
        out.flush();
    }

    private static int nextInt(BufferedReader in)
        throws IOException
    {
        String line = in.readLine();
        if (line == null)
            throw new EOFException();
        return Integer.valueOf(line);
    }
}
