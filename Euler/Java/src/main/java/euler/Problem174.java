package euler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import static java.lang.StrictMath.sqrt;

public class Problem174 {
    private final int limit;
    private final int[] table;
    private final int[] count;

    public Problem174() {
        this(1000000);
    }

    public Problem174(int limit) {
        this.limit = limit;
        this.table = new int[limit + 1];
        this.count = new int[limit + 1];

        int n = limit / 4;
        for (int a = 1, m = (int)sqrt(n); a < m; a++) {
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

    public int solve() {
        return solve(limit);
    }

    public int solve(int k) {
        return count[k];
    }

    public static void main(String[] args)
        throws IOException
    {
        Problem174 solver = new Problem174();
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = new PrintStream(new BufferedOutputStream(System.out));
        int t = nextInt(in);
        while (--t >= 0) {
            int k = nextInt(in);
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
