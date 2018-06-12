package euler.algo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static euler.algo.Library.factorize;
import static euler.algo.Library.gcd;
import static euler.algo.Library.modpow;
import static euler.algo.Library.pow;

@SuppressWarnings("unused")
public class Factorization implements Iterable<PrimeFactor> {
    private final long n;
    private final List<PrimeFactor> factors;

    public Factorization(long n, List<PrimeFactor> factors) {
        this.n = n;
        this.factors = factors;
    }

    public long phi() {
        long r = this.n;
        for (PrimeFactor f : factors) {
            long p = f.prime();
            r = r / p * (p - 1);
        }
        return r;
    }

    public long sigma() {
        long r = 1;
        for (PrimeFactor f : factors)
            r *= f.power() + 1;
        return r;
    }

    public long sigma(int k) {
        long r = 1;
        for (PrimeFactor f : factors) {
            long p = f.prime();
            int  a = f.power();
            long s = 1;
            for (int i = 1; i <= a; i++)
                s += pow(p, i * k);
            r *= s;
        }
        return r;
    }

    public long unitarySigma() {
        return 1L << factors.size();
    }

    public long unitarySigma(int k) {
        long r = 1;
        for (PrimeFactor f : factors)
            r *= 1 + pow(f.prime(), k * f.power());
        return r;
    }

    public long rad() {
        long r = 1;
        for (PrimeFactor f : factors)
            r *= f.prime();
        return r;
    }

    public int moebius() {
        for (PrimeFactor f : factors)
            if (f.power() > 1)
                return 0;
        return factors.size() % 2 == 0 ? 1 : -1;
    }

    public int omega() {
        int r = 0;
        for (PrimeFactor f : factors)
            r += f.power();
        return r;
    }

    public int nu() {
        return factors.size();
    }

    public long[] divisors() {
        long[] divisors = new long[(int)sigma()];
        divisors[0] = 1;

        int i = 1;
        for (PrimeFactor f : factors) {
            int  k = i;
            long p = f.prime();
            int  a = f.power();
            long q = p;

            while (--a >= 0) {
                for (int j = 0; j < i; j++)
                    divisors[k++] = divisors[j] * q;
                q *= p;
            }
            i = k;
        }

        Arrays.sort(divisors);
        return divisors;
    }

    public long[] unitaryDivisors(int n) {
        long[] divisors = new long[(int)unitarySigma()];
        divisors[0] = 1;

        int i = 1;
        for (PrimeFactor f : factors) {
            int  k = i;
            long u = f.value();

            for (int j = 0; j < i; j++)
                divisors[k++] = divisors[j] * u;
            i = k;
        }

        Arrays.sort(divisors);
        return divisors;
    }

    public long ord(int a) {
        if (gcd(a, n) != 1)
            throw new IllegalArgumentException(
                String.format("Multiplicative order: %d and %d must coprime", a, n));

        long n1 = phi();
        long t = 1;
        for (PrimeFactor f : factorize(n1)) {
            long p = f.prime();
            long x = modpow(a, n1 / f.value(), n);
            while (x > 1) {
                x = modpow(x, p, n);
                t *= p;
            }
        }
        return t;
    }

    public boolean isPrimitiveRoot(int a) {
        return ord(a) == phi();
    }

    @Override
    public Iterator<PrimeFactor> iterator() {
        return factors.iterator();
    }

    public String toString() {
        return factors.toString();
    }
}
