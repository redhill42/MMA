package euler;

public final class Problem219 {
    private Problem219() {}

    public static long solve(int limit) {
        // The costs can be either of c,c+1,c+2,c+3,c+4 where c is the
        // lowest cost. We keep track of the counts of each of them.
        int n1 = 1, n2 = 0, n3 = 0, n4 = 1;

        int  remaining = limit - 2;
        long totalCost = 5;
        int  code = 1;

        while (remaining > 0) {
            int n = n1;
            if (n > remaining)
                n = remaining;

            // update and shift counters
            n1 = n2 + n;
            n2 = n3;
            n3 = n4;
            n4 = n;

            // update total cost
            remaining -= n;
            totalCost += (long)n * (code + 5);
            code++;
        }

        return totalCost;
    }
    public static void main(String[] args) {
        int n = (int)1e9;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
