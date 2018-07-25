package euler;

public final class Problem558 {
    private Problem558() {}

    private static final int B = 200;

    private static void addbit(int[] x, int i) {
        if (x[i+2] != 0) {
            x[i] = 1;
            propagate(x, i);
            return;
        }
        if (x[i-1] != 0) {
            x[i] = 1;
            i--;
        }
        if (x[i+1] != 0) {
            // x^n + x^(n+1) = x^(n+2) + x^(n-3)
            x[i] = x[i+1] = 0;
            x[i+2] = 1;
            propagate(x, i+2);
            addbit(x, i-3);
        } else if (x[i-2] != 0) {
            x[i] = 1;
            propagate(x, i-2);
        } else if (x[i] != 0) {
            // x^n = x^(n-3) + x^(n-1)
            addbit(x, i-3);
            addbit(x, i-1);
        } else {
            x[i] = 1;
        }
    }

    private static void propagate(int[] x, int i) {
        // x^n + x^(n+2) = x^(n+3)
        while (x[i+2] != 0) {
            x[i] = x[i+2] = 0;
            x[i+3] = 1;
            i += 3;
        }
    }

    private static void add(int[] x, int[] y) {
        for (int i = 0; i < x.length; i++) {
            if (y[i] != 0)
                addbit(x, i);
        }
    }

    private static int bits(int[] x) {
        int s = 0;
        for (int b : x)
            s += b;
        return s;
    }

    public static int solve(int n) {
        int total = 0;
        int[] n2 = new int[B*2];
        int[] d  = new int[B*2];
        d[B] = 1;
        for (int i = 1; i <= n; i++) {
            add(n2, d);
            total += bits(n2);
            addbit(d, B-7);
            addbit(d, B-2);
            addbit(d, B+1);
        }
        return total;
    }

    public static void main(String[] args) {
        int n = 5_000_000;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
