package euler;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import euler.algo.PrimeSieve;

public final class Problem615 {
    private Problem615() {}

    static class Datum {
        Map<Integer,Integer> factors;
        double value;

        Datum(Map<Integer,Integer> factors, double value) {
            this.factors = factors;
            this.value = value;
        }

        Datum(int prime, int power) {
            this.factors = new TreeMap<>();
            this.factors.put(prime, power);
            this.value = power * Math.log(prime);
        }

        Datum replace(int p, int q) {
            Map<Integer,Integer> nmap = new TreeMap<>(factors);
            int k = nmap.get(p);
            if (k == 1) {
                nmap.remove(p);
            } else {
                nmap.replace(p, k - 1);
            }
            nmap.put(q, nmap.getOrDefault(q, 0) + 1);

            double nval = value - Math.log(p) + Math.log(q);
            return new Datum(nmap, nval);
        }

        Datum add2() {
            Map<Integer,Integer> nmap = new TreeMap<>(factors);
            nmap.put(2, nmap.getOrDefault(2, 0) + 1);
            return new Datum(nmap, value + Math.log(2));
        }

        long getValue(int modulo) {
            long ret = 1;
            for (Map.Entry<Integer,Integer> e : factors.entrySet()) {
                ret = ret * modpow(e.getKey(), e.getValue(), modulo) % modulo;
            }
            return ret;
        }

        private static long modpow(long a, long n, long m) {
            long ret = 1;
            while (n > 0) {
                if ((n & 1) == 1)
                    ret = ret * a % m;
                n >>= 1;
                a = a * a % m;
            }
            return ret;
        }

        public long code() {
            long ret = 0;
            for (Map.Entry<Integer,Integer> e : factors.entrySet()) {
                ret = ret * 1000000009 + e.getKey();
                ret = ret * 1000000009 + e.getValue();
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

            for (int p : current.factors.keySet()) {
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
