package euler;

import java.util.PriorityQueue;

import static euler.algo.Library.fromDigits;
import static euler.algo.Library.isPrime;
import static euler.algo.Library.nextPrime;

public final class Problem200 {
    private Problem200() {}

    private static class Sqube implements Comparable<Sqube> {
        final long p, q;
        final long value;

        Sqube(long p, long q) {
            this.p = p;
            this.q = q;
            this.value = p*p * q*q*q;
        }

        Sqube nextP() {
            long p1 = nextPrime(p);
            if (p1 == q)
                p1 = nextPrime(p1);
            return new Sqube(p1, q);
        }

        Sqube nextQ() {
            long q1 = nextPrime(q);
            if (q1 == p)
                q1 = nextPrime(q1);
            return new Sqube(p, q1);
        }

        @Override
        public int compareTo(Sqube x) {
            return Long.compare(value, x.value);
        }

        public boolean equals(Object x) {
            return (x instanceof Sqube) && value == ((Sqube)x).value;
        }

        public int hashCode() {
            return Long.hashCode(value);
        }
    }

    public static long solve(String patt, int count) {
        PriorityQueue<Sqube> squbes = new PriorityQueue<>();
        squbes.offer(new Sqube(3, 2));
        squbes.offer(new Sqube(2, 3));

        while (true) {
            Sqube sq = squbes.poll();

            long x = sq.value;
            if (Long.toString(x).contains(patt) && isPrimeProof(x)) {
                if (--count == 0)
                    return x;
            }

            Sqube next = sq.nextP();
            if (!squbes.contains(next))
                squbes.offer(next);
            next = sq.nextQ();
            if (!squbes.contains(next))
                squbes.offer(next);
        }
    }

    private static boolean isPrimeProof(long x) {
        if (x % 2 == 0 || x % 5 == 0) {
            long y = x / 10 * 10;
            return !isPrime(y + 1) &&
                   !isPrime(y + 3) &&
                   !isPrime(y + 7) &&
                   !isPrime(y + 9);
        }

        String str = Long.toString(x);
        int[] digits = new int[str.length()];
        for (int i = 0; i < digits.length; i++)
            digits[i] = str.charAt(i) - '0';

        for (int i = 0; i < digits.length - 1; i++) {
            int save = digits[i];
            for (int d = (i == 0) ? 1 : 0; d <= 9; d++) {
                if (d != save) {
                    digits[i] = d;
                    if (isPrime(fromDigits(digits)))
                        return false;
                }
            }
            digits[i] = save;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(solve("200", 200));
    }
}
