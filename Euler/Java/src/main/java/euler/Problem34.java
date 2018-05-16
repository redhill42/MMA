package euler;

public final class Problem34 {
    private Problem34() {}

    public static int solve(int limit) {
        int[] factorials = new int[10];
        factorials[0] = 1;
        for (int i = 1; i < 10; i++)
            factorials[i] = i * factorials[i - 1];

        int sum = 0;
        for (int n = 3; n <= limit; n++) {
            int s = 0;
            for (int d = n; d != 0; d /= 10)
                s += factorials[d % 10];
            if (s == n)
                sum += n;
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(solve(50000));
    }
}
