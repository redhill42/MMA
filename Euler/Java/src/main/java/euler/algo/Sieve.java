package euler.algo;

/**
 * The base interface for prime sieves.
 */
public interface Sieve {
    /**
     * Get the limit of sieve.
     */
    int getLimit();

    /**
     * Primality test for the given number.
     *
     * @param n the number to be tested
     * @return {@code true} if the given number is a prime, {@code false} otherwise
     */
    boolean isPrime(int n);

    /**
     * Returns the next prime number in the sieve.
     *
     * @param n the start number, the returned prime number is greater than this number.
     * @return the next prime in the sieve, or -1 if no more primes in the sieve.
     */
    int nextPrime(int n);

    /**
     * Returns the previous prime number in the sieve.
     *
     * @param n the start number, the returned prime number is less than this number.
     * @return the previous prime in the sieve, or -1 if no more primes in the sieve.
     */
    int previousPrime(int n);

    /**
     * Returns the number of prime numbers in the sieve.
     *
     * @return the number of prime numbers in the sieve
     */
    int cardinality();

    /**
     * Returns all prime numbers in the sieve.
     *
     * @return all prime numbers in the sieve
     */
    int[] getPrimes();
}
