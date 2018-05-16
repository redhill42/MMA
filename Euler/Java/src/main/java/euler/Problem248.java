package euler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import euler.algo.Library;

public final class Problem248 {
    private Problem248() {}

    private static class Solver {
        private final long factorial;
        private final long[] candidates;
        private final long[] results;

        Solver(long factorial) {
            this.factorial = factorial;
            this.candidates = factorize(factorial);

            SortedSet<Long> results = new TreeSet<>();
            search(0, 1, 1, results);
            this.results = results.stream().mapToLong(x->x).toArray();
        }

        public long[] solve() {
            return results;
        }

        public long solve(int index) {
            return results[index - 1];
        }

        private static long[] factorize(long n) {
            List<Long> divisors = new ArrayList<>();
            divisors.add(1L);

            for (long p = 2; p * p <= n; p++) {
                if (n % p == 0) {
                    int a = 0;
                    while (n % p == 0) {
                        a++;
                        n /= p;
                    }

                    int k = divisors.size();
                    long q = p;
                    while (--a >= 0) {
                        for (int i = 0; i < k; i++)
                            divisors.add(divisors.get(i) * q);
                        q *= p;
                    }
                }
            }
            if (n != 0) {
                int k = divisors.size();
                for (int i = 0; i < k; i++)
                    divisors.add(divisors.get(i) * n);
            }

            return divisors.stream()
                           .mapToLong(x->x+1)
                           .filter(Library::isPrime)
                           .sorted()
                           .toArray();
        }

        private void search(int from, long number, long phi, Set<Long> results) {
            for (int i = from; i < candidates.length; i++) {
                long prime = candidates[i];
                long nextNumber = number * prime;
                long nextPhi = (number % prime == 0) ? phi * prime : phi * (prime - 1);

                if (nextPhi > factorial)
                    break;
                if (nextPhi == factorial) {
                    results.add(nextNumber);
                    break;
                }
                if (factorial % nextPhi == 0) {
                    search(i, nextNumber, nextPhi, results);
                }
            }
        }
    }

    public static long[] solve(long factorial) {
        Solver solver = new Solver(factorial);
        return solver.solve();
    }

    public static long solve(long factorial, int index) {
        Solver solver = new Solver(factorial);
        return solver.solve(index);
    }

    private static long factorial(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++)
            res *= i;
        return res;
    }

    public static void main(String[] args) {
        System.out.println(solve(factorial(13), 150000));
    }
}
