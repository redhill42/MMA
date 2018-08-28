package euler;

import java.util.Arrays;

import euler.algo.Permutations;

public final class Problem336 {
    private Problem336() {}

    public static String solve(int length, int found) {
        int[] train = new int[length];
        int[] copy = new int[length];
        int cnt = 0;

        // optimize by start permutation from "CABDEFGH..." since a maximix
        // arrangement can't start with "A" or "B"
        Arrays.setAll(train, i -> i);
        train[0] = 2; train[1] = 0; train[2] = 1;

        do {
            System.arraycopy(train, 0, copy, 0, length);
            if (maximix(copy) && ++cnt == found) {
                return format(train);
            }
        } while (Permutations.next(train));
        return "NOT FOUND";
    }

    private static boolean maximix(int[] train) {
        for (int i = 0; i <= train.length - 2; i++)
            if (!rearrange(train, i))
                return false;
        return true;
    }

    private static boolean rearrange(int[] train, int id) {
        int n = train.length;

        // no need to rotate if carriage is already in place
        if (train[id] == id)
            return false;

        // only one rotation is needed for last two carriages
        if (id == n - 2) {
            rotate(train, id);
            return true;
        }

        // find the position of carriage
        int pos;
        for (pos = 0; pos < train.length; pos++) {
            if (train[pos] == id)
                break;
        }

        // it can't be maximix arragment if carriage is at end of train
        if (pos == n - 1)
            return false;

        // rotate the carriage into correct position
        rotate(train, pos);
        rotate(train, id);
        return true;
    }

    private static void rotate(int[] train, int pos) {
        int a = pos, b = train.length - 1;
        while (a < b) {
            int t = train[a];
            train[a] = train[b];
            train[b] = t;
            a++; b--;
        }
    }

    private static String format(int[] train) {
        StringBuilder b = new StringBuilder();
        for (int d : train)
            b.append((char)(d + 'A'));
        return b.toString();
    }

    public static void main(String[] args) {
        int n = 11, m = 2011;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            m = Integer.parseInt(args[1]);
        }
        System.out.println(solve(n, m));
    }
}
