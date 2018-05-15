package euler;

import java.util.ArrayList;
import java.util.List;

import static euler.algo.Library.isPrime;
import static java.lang.Long.min;
import static java.lang.Math.abs;

public final class Problem273 {
    private Problem273() {}

    private static class Complex {
        final long real, imag;

        Complex(long real, long imag) {
            this.real = real;
            this.imag = imag;
        }

        Complex multiply(Complex c) {
            return new Complex(
                real * c.real - imag * c.imag,
                imag * c.real + real * c.imag
            );
        }

        Complex conjugate() {
            return new Complex(real, -imag);
        }
    }

    private static class Solver {
        private final Complex[] S;

        Solver(int limit) {
            List<Complex> primes = new ArrayList<>();
            for (int a = 1; 2*a*a < limit; a++) {
                for (int b = a+1; a*a + b*b < limit; b += 2) {
                    int n = a*a + b*b;
                    if ((n - 1) % 4 == 0 && isPrime(n)) {
                        primes.add(new Complex(a, b));
                    }
                }
            }
            this.S = primes.toArray(new Complex[primes.size()]);
        }

        public long solve() {
            long sum = 0;
            for (int i = 0; i < S.length; i++)
                sum += solve(i + 1, S[i]);
            return sum;
        }

        public long solve(int n, Complex c) {
            if (n == S.length) {
                return min(abs(c.real), abs(c.imag));
            }

            long sum = solve(n + 1, c);
            sum += solve(n + 1, c.multiply(S[n]));
            sum += solve(n + 1, c.multiply(S[n].conjugate()));
            return sum;
        }
    }

    public static long solve(int limit) {
        Solver solver = new Solver(limit);
        return solver.solve();
    }

    public static void main(String[] args) {
        int n = 150;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
