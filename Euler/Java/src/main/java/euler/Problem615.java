package euler;

import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import euler.algo.Factorization;
import euler.algo.PrimeFactor;
import euler.algo.PrimeSieve;

import static euler.algo.Library.modmul;
import static euler.algo.Library.modpow;

public final class Problem615 {
    private Problem615() {}

    static class Datum {
        Factorization factors;
        double value;

        Datum(Factorization factors, double value) {
            this.factors = factors;
            this.value = value;
        }

        Datum(int prime, int power) {
            this.factors = new Factorization(prime, power);
            this.value = power * Math.log(prime);
        }

        Datum replace(int p, int q) {
            Factorization nf = factors.remove(p, 1).multiply(q, 1);
            double nval = value - Math.log(p) + Math.log(q);
            return new Datum(nf, nval);
        }

        Datum add2() {
            Factorization nf = factors.multiply(2, 1);
            double nval = value + Math.log(2);
            return new Datum(nf, nval);
        }

        long getValue(int modulo) {
            long n = 1;
            for (PrimeFactor f : factors)
                n = modmul(n, modpow(f.prime(), f.power(), modulo), modulo);
            return n;
        }

        public long code() {
            long ret = 0;
            for (PrimeFactor f : factors) {
                ret = ret * 1000000009 + f.prime();
                ret = ret * 1000000009 + f.power();
            }
            return ret;
        }

        public String toString() {
            return factors.toString();
        }
    }

    public static long solve(int limit, int modulo) {
        PrimeSieve sieve = new PrimeSieve(limit);

        PriorityQueue<Datum> frontier = new PriorityQueue<>(
            (x, y) -> Double.compare(x.value, y.value));
        frontier.offer(new Datum(2, limit));

        Set<Long> seen = new TreeSet<>();

        for (int progress = 0; progress < limit - 1; progress++) {
            Datum current = frontier.poll();

            for (PrimeFactor f : current.factors) {
                int p = (int)f.prime();
                Datum next = current.replace(p, sieve.nextPrime(p));
                if (seen.add(next.code())) {
                    frontier.offer(next);
                }
            }

            Datum next = current.add2();
            if (seen.add(next.code())) {
                frontier.offer(next);
            }
        }

        return frontier.peek().getValue(modulo);
    }

    public static void main(String[] args) {
        System.out.println(solve(1_000_000, 123454321));
    }
}
