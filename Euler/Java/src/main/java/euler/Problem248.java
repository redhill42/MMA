package euler;

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import euler.algo.Library;
import static euler.algo.Library.factorial;
import static euler.algo.Library.factorize;

public final class Problem248 {
    private Problem248() {}

    private static class Solver {
        private final long factorial;
        private final long[] candidates;
        private final long[] results;

        Solver(long factorial) {
            this.factorial = factorial;

            this.candidates = Arrays.stream(factorize(factorial).divisors())
                .map(x->x+1).filter(Library::isPrime).toArray();

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

    public static void main(String[] args) {
        System.out.println(solve(factorial(13), 150000));
    }
}
