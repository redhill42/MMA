package euler;

import euler.util.IntArray;

import static euler.algo.Library.gcd;

public final class Problem175 {
    private Problem175() {}

    public static String solve(int a, int b) {
        // must swap numerator and denominator according to the algorithm
        int d = gcd(a, b);
        IntArray sequence = f(b / d, a / d);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < sequence.length - 1; i++)
            buf.append(sequence.a[i]).append(",");
        buf.append(sequence.a[sequence.length - 1]);
        return buf.toString();
    }

    private static IntArray f(int a, int b) {
        IntArray sequence = new IntArray();
        int d = -1;
        int accum = 0;

        while (a != b) {
            int next;
            if (b > a) {
                b -= a;
                next = 0;
            } else {
                a -= b;
                next = 1;
            }

            if (next != d) {
                if (accum != 0)
                    sequence.add(0, accum);
                accum = 1;
            } else {
                accum++;
            }
            d = next;
        }

        if (d == 1) {
            sequence.add(0, accum + 1);
        } else {
            sequence.add(0, accum);
            sequence.add(0, 1);
        }
        return sequence;
    }

    public static void main(String[] args) {
        System.out.println(solve(123456789, 987654321));
    }
}
