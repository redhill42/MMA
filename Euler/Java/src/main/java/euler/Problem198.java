package euler;

import euler.util.LongArray;

public final class Problem198 {
    private Problem198() {}

    private static class Stack extends LongArray {
        final void push(long a, long b) {
            add((a << 32) | b);
        }

        final int a() {
            return (int)(a[length - 1] >>> 32);
        }

        final int b() {
            return (int)a[length - 1];
        }

        final void pop() {
            length--;
        }
    }

    public static long solve(int frac, long limit) {
        Stack stack = new Stack();
        long a = 0, b = 1, c = 1, d = frac/2;
        long p, q;
        long count = 0;

        stack.push(c, d);
        while (!stack.isEmpty()) {
            c = stack.a();
            d = stack.b();
            p = a * d + b * c;
            q = 2 * b * d;

            if (q <= limit) {
                stack.push(a + c, b + d);
                if (p * frac < q)
                    count++;
            } else {
                a = c; b = d;
                stack.pop();
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int  k = 100;
        long n = 100_000_000;
        if (args.length > 0)
            k = Integer.parseInt(args[0]);
        if (args.length > 1)
            n = Long.parseLong(args[1]);
        System.out.println(solve(k, n));
    }
}
