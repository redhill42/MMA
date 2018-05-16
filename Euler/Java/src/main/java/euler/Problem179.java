package euler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

import euler.algo.PrimeSieve;
import static euler.algo.Library.exponent;

public final class Problem179 {
    private Problem179() {}

    private static class Solver {
        private final int[] accum;

        public Solver(int limit) {
            PrimeSieve sieve = new PrimeSieve(limit);
            int[] accum = new int[limit + 1];
            for (int p = 2; p > 0; p = sieve.nextPrime(p)) {
                for (int n = p; n <= limit; n += p) {
                    int a = exponent(n, p);
                    if (accum[n] == 0) {
                        accum[n] = a + 1;
                    } else {
                        accum[n] *= a + 1;
                    }
                }
            }

            int s = 0;
            for (int n = 2; n < limit; n++) {
                if (accum[n] == accum[n+1])
                    s++;
                accum[n] = s;
            }

            this.accum = accum;
        }

        public int solve(int n) {
            return accum[n - 1];
        }
    }

    public static int solve(int limit) {
        return new Solver(limit).solve(limit);
    }

    public static void main(String[] args)
        throws IOException
    {
        ArrayList<Integer> inputs = new ArrayList<>();
        int limit = 0;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = new PrintStream(new BufferedOutputStream(System.out));
        int t = nextInt(in);
        while (--t >= 0) {
            int n = nextInt(in);
            if (n > limit)
                limit = n;
            inputs.add(n);
        }

        Solver solver = new Solver(limit);
        for (int n : inputs)
            out.println(solver.solve(n));
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
