package euler.util;

public class TotientSieve extends PrimeSieve {
    private final int[] phi;

    public TotientSieve(int limit) {
        super(limit);

        this.phi = new int[limit + 1];
        phi[1] = phi[2] = 1;

        for (int n = 3; n <= limit; n += 2) {
            phi[n] = n;
            phi[n+1] = (n+1)/2;
        }

        for (int p = 3; p > 0; p = nextPrime(p)) {
            phi[p] = p - 1;
            for (int n = p + p; n <= limit; n += p) {
                phi[n] = phi[n] / p * (p - 1);
            }
        }
    }

    public int phi(int n) {
        return phi[n];
    }
}
