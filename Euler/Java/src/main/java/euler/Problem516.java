package euler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;

import static euler.util.Utils.isqrt;

public final class Problem516 {
    private Problem516() {}

    // wheel with size 30 (=2*3*5):
    // test against 30m+1, 30m+7, 30m+11, 30m+13, 30m+17, 30m+19, 30m+23, 30m+29
    // their deltas/increments are:
    private static final int[] delta = { 6, 4, 2, 4, 2, 4, 6, 2};

    public static boolean isPrime(long n) {
        if (n % 2 == 0 || n % 3 == 0 || n % 5 == 0)
            return n == 2 || n == 3 || n == 5;

        int i = 7, m = (int)isqrt(n), pos = 1;
        while(i <= m) {
            if (n % i == 0)
                return false;
            i += delta[pos];
            pos = (pos + 1) & 7;
        }
        return true;
    }

    private static class Pair {
        long number;
        long nextPrime;

        Pair(long number, long nextPrime) {
            this.number = number;
            this.nextPrime = nextPrime;
        }
    }

    public static long solve(long limit) {
        TreeSet<Long> hamming = new TreeSet<>();
        TreeSet<Long> primes = new TreeSet<>();

        for (long a = 1; a <= limit; a *= 2)
            for (long b = 1; a*b <= limit; b *= 3)
                for (long c = 1; a*b*c <= limit; c *= 5) {
                    long n = a * b * c;
                    hamming.add(n);
                    if (n > 5 && isPrime(n + 1))
                        primes.add(n + 1);
                }

        Queue<Pair> todo = new LinkedList<>();
        todo.offer(new Pair(1, 1));

        long sum = 0;
        while (!todo.isEmpty()) {
            Pair current = todo.poll();

            for (long h : hamming) {
                long next = multiplyExact(h, current.number);
                if (next < 0 || next > limit)
                    break;
                sum += next;
            }

            for (long prime : primes.tailSet(current.nextPrime, false)) {
                long next = multiplyExact(prime, current.number);
                if (next < 0 || next > limit)
                    break;
                todo.offer(new Pair(next, prime));
            }
        }

        return sum & 0xFFFFFFFFL;
    }

    private static long multiplyExact(long x, long y) {
        long r = x * y;
        if ((x | y) >>> 31 != 0 && r / y != x)
            return -1;
        return r;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long n = in.nextLong();
        System.out.println(solve(n));
    }
}
