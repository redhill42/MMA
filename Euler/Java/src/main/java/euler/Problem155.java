package euler;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import static euler.util.Utils.gcd;

public final class Problem155 {
    private Problem155() {}

    private static class Fraction implements Comparable<Fraction> {
        private final int num, den;

        Fraction(int num, int den) {
            this.num = num;
            this.den = den;
        }

        Fraction plus(Fraction that) {
            return new Fraction(this.num * that.den + that.num * this.den, this.den * that.den);
        }

        Fraction inversePlus(Fraction that) {
            return new Fraction(this.num * that.num, this.num * that.den + that.num * this.den);
        }

        @Override
        public int compareTo(Fraction that) {
            return this.num * that.den - that.num * this.den;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Fraction) {
                Fraction that = (Fraction)obj;
                return this.num * that.den == that.num * this.den;
            } else {
                return false;
            }
        }

        public int hashCode() {
            int g = gcd(num, den);
            return (num / g) * 31 + (den / g);
        }

        public String toString() {
            int g = gcd(num, den);
            int n = this.num / g;
            int d = this.den / g;
            if (d == 1) {
                return Integer.toString(n);
            } else {
                return n + "/" + d;
            }
        }
    }

    public static int solve(int limit) {
        @SuppressWarnings("unchecked")
        Set<Fraction>[] circuits = new Set[limit + 1];
        circuits[1] = Collections.singleton(new Fraction(1, 1));
        int count = 1;

        for (int n = 2; n <= limit; n++) {
            Set<Fraction> c = new TreeSet<>();
            for (int i = 1; i <= n / 2; i++) {
                for (Fraction a : circuits[i]) {
                    for (Fraction b : circuits[n - i]) {
                        c.add(a.plus(b));
                        c.add(a.inversePlus(b));
                    }
                }
            }

            for (int i = 1; i < n; i++)
                c.removeAll(circuits[i]);
            count += c.size();
            circuits[n] = c;
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println(solve(18));
    }
}
