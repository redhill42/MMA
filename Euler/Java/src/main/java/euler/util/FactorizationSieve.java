package euler.util;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class FactorizationSieve extends PrimeSieve {
    public static final class Factor implements Comparable<Factor> {
        private final int p, a;

        Factor(int p, int a) {
            this.p = p;
            this.a = a;
        }

        public int prime() {
            return p;
        }

        public int power() {
            return a;
        }

        @Override
        public int compareTo(Factor other) {
            return Integer.compare(p, other.p);
        }

        public boolean equals(Object obj) {
            if (obj instanceof Factor) {
                Factor other = (Factor)obj;
                return p == other.p && a == other.a;
            }
            return false;
        }

        public int hashCode() {
            return p * 31 + a;
        }

        public String toString() {
            return "{" + p + "," + a + "}";
        }
    }

    private final Set<Factor>[] factorizations;

    @SuppressWarnings("unchecked")
    public FactorizationSieve(int limit) {
        super(limit);

        factorizations = new Set[limit + 1];
        Arrays.setAll(factorizations, i -> new TreeSet<Factor>());
        factorizations[1].add(new Factor(1, 1));

        for (int p = 2; p > 0; p = nextPrime(p)) {
            factorizations[p].add(new Factor(p, 1));
            for (int n = p + p; n <= limit; n += p) {
                factorizations[n].add(new Factor(p, exponent(n, p)));
            }
        }
    }

    private static int exponent(int n, int k) {
        int a = 0;
        while (n % k == 0) {
            a++;
            n /= k;
        }
        return a;
    }

    private static int pow(int x, int n) {
        if (n == 0)
            return 1;
        if (n == 1)
            return x;

        int y = 1;
        while (n != 0) {
            if (n % 2 == 1)
                y *= x;
            n >>= 1;
            x *= x;
        }
        return y;
    }

    public Set<Factor> factors(int n) {
        return factorizations[n];
    }

    public int sigma(int n) {
        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int s = 1;
        for (Factor f : factors(n))
            s *= f.a + 1;
        return s;
    }

    public int sigma(int k, int n) {
        if (k == 0)
            return sigma(n);

        if (n < 0)
            n = -n;
        if (n <= 1)
            return n;

        int s = 1;
        for (Factor f : factors(n))
            s *= (pow(f.p, k * (f.a + 1)) - 1) / (pow(f.p, k) - 1);
        return s;
    }

    public int[] divisors(int n) {
        if (n < 0)
            n = -n;

        int[] d = new int[sigma(n)];
        int i = 0;

        if (n != 0)
            d[i++] = 1;
        if (n <= 1)
            return d;

        for (Factor f : factors(n)) {
            int k = i, p = f.p, a = f.a;
            while (--a >= 0) {
                for (int j = 0; j < i; j++)
                    d[k++] = d[j] * p;
                p *= f.p;
            }
            i = k;
        }

        Arrays.sort(d);
        return d;
    }
}
