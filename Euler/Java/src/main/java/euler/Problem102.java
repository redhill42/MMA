package euler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public final class Problem102 {
    private Problem102() {}

    public static int solve(int[][] triangles) {
        int count = 0;
        for (int[] t : triangles)
            if (inside(t[0], t[1], t[2], t[3], t[4], t[5]))
                count++;
        return count;
    }

    private static boolean inside(int x1, int y1, int x2, int y2, int x3, int y3) {
        boolean b1 = x1 * y2 > x2 * y1;
        boolean b2 = x2 * y3 > x3 * y2;
        boolean b3 = x3 * y1 > x1 * y3;
        return b1 == b2 && b2 == b3;
    }

    private static int[][] readData(InputStream stream) {
        Scanner in = new Scanner(stream);
        ArrayList<int[]> data = new ArrayList<>();

        in.useDelimiter("[\\s,]");
        while (in.hasNextInt()) {
            int[] t = new int[6];
            for (int i = 0; i < 6; i++)
                t[i] = in.nextInt();
            data.add(t);
        }
        return data.toArray(new int[data.size()][]);
    }

    public static void main(String[] args) throws IOException {
        try (InputStream stream = Problem102.class.getResourceAsStream("p102_triangles.txt")) {
            System.out.println(solve(readData(stream)));
        }
    }
}
