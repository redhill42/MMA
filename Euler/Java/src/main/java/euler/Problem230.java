package euler;

import euler.util.LongArray;

import static euler.algo.Library.pow;

public final class Problem230 {
    private Problem230() {}

    private static class Solver {
        private final long[] fibo;

        Solver(int blockSize) {
            LongArray fibs = new LongArray();
            fibs.add(0);
            fibs.add(blockSize);
            for (int i = 2; ; i++) {
                long a = fibs.get(i-1);
                long b = fibs.get(i-2);
                if (Long.MAX_VALUE - a < b)
                    break;
                fibs.add(a + b);
            }
            this.fibo = fibs.toArray();
        }

        public int solve(String A, String B, long index) {
            int cursor = 1;
            while (fibo[cursor] < index)
                cursor++;
            while (cursor > 2) {
                long a = fibo[cursor - 2];
                cursor--;
                if (index - a > 0)
                    index -= a;
                else
                    cursor--;
            }
            return (cursor == 1 ? A : B).charAt((int)index - 1) - '0';
        }
    }

    public static long solve(String A, String B, int limit) {
        if (A.length() != B.length() || A.isEmpty())
            throw new IllegalArgumentException("Invalid input");

        Solver solver = new Solver(A.length());
        long sum = 0;
        for (int n = 0; n <= limit; n++)
            sum += pow(10, n) * solver.solve(A, B, (127 + 19 * n) * pow(7, n));
        return sum;
    }

    public static void main(String[] args) {
        String A = "14159265358979323846264338327950288419716939937510" +
                   "58209749445923078164062862089986280348253421170679";
        String B = "82148086513282306647093844609550582231725359408128" +
                   "48111745028410270193852110555964462294895493038196";
        System.out.println(solve(A, B, 17));
    }
}
