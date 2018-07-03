package euler;

import java.util.Arrays;

import static euler.algo.Library.gcd;

public final class Problem630 {
    private Problem630() {}

    private static class Point {
        final int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Line {
        // represent the line in the form Ax+By+C=0
        final int A, B, C;

        Line(int x1, int y1, int x2, int y2) {
            int a = y2 - y1;
            int b = x1 - x2;
            int c = x2 * y1 - x1 * y2;
            int g = gcd(a, b, c);

            if (a < 0 || (a == 0 && b < 0))
                g = -g;

            this.A = a / g;
            this.B = b / g;
            this.C = c / g;
        }

        final boolean parallelTo(Line other) {
            return A * other.B == B * other.A;
        }

        public boolean equals(Object o) {
            if (o instanceof Line) {
                Line other = (Line)o;
                return A == other.A && B == other.B && C == other.C;
            }
            return false;
        }

        public int hashCode() {
            return ((31 + A) * 31 + B) * 31 + C;
        }
    }

    private static class Generator {
        private long seed = 290797;

        public int next() {
            seed = seed * seed % 50515093;
            return (int)((seed % 2000) - 1000);
        }
    }

    public static long solve(int limit) {
        int i, j;

        // generate all points
        Generator gen = new Generator();
        Point[] points = new Point[limit];
        for (i = 0; i < limit; i++) {
            points[i] = new Point(gen.next(), gen.next());
        }

        // generate all lines
        Line[] lines = new Line[limit * (limit - 1) / 2];
        int nlines = 0;
        for (i = 0; i < limit; i++) {
            int x1 = points[i].x, y1 = points[i].y;
            for (j = i+1; j < limit; j++) {
                int x2 = points[j].x, y2 = points[j].y;
                lines[nlines++] = new Line(x1, y1, x2, y2);
            }
        }

        // sort lines by slope
        Arrays.parallelSort(lines, 0, nlines, (l1, l2) -> {
            int c = Integer.compare(l1.A * l2.B, l2.A * l1.B);
            if (c == 0)
                c = Integer.compare(l1.C, l2.C);
            return c;
        });

        // remove duplicate lines
        for (i = 0, j = 0; i < nlines; i++) {
            if (!lines[i].equals(lines[j])) {
                lines[++j] = lines[i];
            }
        }
        nlines = j + 1;

        // remove parallel crosses
        long result = (long)nlines * (nlines - 1);
        for (i = 0, j = 0; i < nlines; i = j) {
            while (j < nlines && lines[i].parallelTo(lines[j]))
                j++;
            result -= (j - i) * (j - i - 1);
        }

        return result;
    }

    public static void main(String[] args) {
        int n = 2500;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
