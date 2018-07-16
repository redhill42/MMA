package euler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.max;

public final class Problem67 {
    private Problem67() {}

    public static int solve(int[][] triangle) {
        int n = triangle.length;
        int[] accum = new int[n];

        System.arraycopy(triangle[n-1], 0, accum, 0, n);
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j <= i; j++)
                accum[j] = max(accum[j], accum[j+1]) + triangle[i][j];
        }
        return accum[0];
    }

    private static int[][] readData(InputStream stream) {
        Scanner in = new Scanner(stream);
        ArrayList<int[]> data = new ArrayList<>();

        while (in.hasNextLine()) {
            data.add(Arrays.stream(in.nextLine().split(" "))
                           .mapToInt(Integer::parseInt)
                           .toArray());
        }
        return data.toArray(new int[data.size()][]);
    }

    public static void main(String[] args) throws IOException {
        try (InputStream stream = Problem67.class.getResourceAsStream("p067_triangle.txt")) {
            System.out.println(solve(readData(stream)));
        }
    }
}
