package euler;

import static java.lang.Math.min;

public final class Problem308 {
    private Problem308() {}

    private static final int S01 = 1;
    private static final int S11 = 2;
    private static final int S17 = 3;

    /**
     * Simulate a FRACTRAN.
     *
     * @param N the number of primes to inspect
     * @return the iterations
     */
    private static long fractran(int N) {
        if (N <= 0)
            return 0;

        int  r2, r3, r5, r7;        // FRACTRAN registers
        int  st;                    // the state register
        long pc;                    // the program counter

        // initialize the FRACTRAN
        r2 = 1;
        r3 = r5 = r7 = 0;
        st = S01;
        pc = 0;

        // FRACTRAN state transition
        while (true) {
            pc++;
            switch (st) {
            case S01:
                r3 += r2;
                r5 += r2 + 1;
                pc += r2 + r7;
                r2 = r7 = 0;
                st = S11;
                break;

            case S11:
                r7 += r3 - 1;
                pc += r3 * 2 + 1;       // loop on 11<->29, skip state 13
                r3 = 0;
                st = S17;
                break;

            case S17:
                // terminate state
                if (r3 + r5 + r7 == 0) {
                    System.out.printf("%d: %d%n", r2, pc);
                    if (--N == 0)
                        return pc;
                }

                // loop on state 13<->17
                if (r5 > 0 && r7 > 0) {
                    int r = min(r5, r7);
                    r2 += r;
                    r3 += r;
                    r5 -= r;
                    r7 -= r;
                    pc += r * 2;
                }

                if (r5 > 0) {
                    r5--;
                    r2++;
                    r3++;
                    pc++;               // skip state 13
                    st = S11;
                    break;
                }

                if (r3 > 0) {
                    r3--;
                    r5 += r2;
                    pc += r2 * 2 + 1;   // loop on state 19<->23
                    r2 = 0;
                    r7++;
                    st = S11;
                    break;
                }

                st = S01;
                break;
            }
        }
    }

    /**
     * Use the Richard Guy's formula to compute the iteration count.
     */
    public static long solve(int N) {
        long n = 1, b;
        long steps = 0;

        while (--N >= 0) {
            do {
                long s = 0, t = ++n;
                for (long d = 2, c = n - 1; ; d++, c = b) {
                    b = n / d;
                    s += (c - b) * (d - 1);
                    if (b >= d) {
                        t += b;
                        if (b * d == n) {
                            s += d;
                            break;
                        }
                    }
                    if (b <= d) {
                        b = 1;
                        s += t;
                        break;
                    }
                }
                steps += n + b - 2 + (6 * n + 2) * (n - b) + 2 * s;
            } while (b > 1); // b == 1 if n is prime
        }
        return steps;
    }

    public static void main(String[] args) {
        int n = 10001;
        boolean simulate = false;

        if (args.length > 0) {
            if ("-simulate".equals(args[0])) {
                simulate = true;
                if (args.length > 1)
                    n = Integer.parseInt(args[1]);
            } else {
                n = Integer.parseInt(args[0]);
            }
        }

        if (simulate) {
            System.out.println(fractran(n));
        } else {
            System.out.println(solve(n));
        }
    }
}
