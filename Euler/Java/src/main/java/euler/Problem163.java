package euler;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public final class Problem163 {
    private Problem163() {}

    private static final double EPS = 1e-9;
    private static final double H = sqrt(3);

    private static class Point {
        final double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class Line {
        // represent the line in the form ax + by + c = 0
        final double a, b, c;
        final int slope;

        Line(double x1, double y1, double x2, double y2, int slope) {
            a = y1 - y2;
            b = x2 - x1;
            c = x2 * y1 - x1 * y2;
            this.slope = slope;
        }

        private Point intersect(Line other) {
            // no intersection point if the lines are parallel
            if (slope == other.slope)
                return null;

            // get the intersection point by solving the line equation
            double det = a * other.b - other.a * b;
            double x = (c * other.b - other.c * b) / det;
            double y = (a * other.c - other.a * c) / det;
            return new Point(x, y);
        }
    }

    // The point P is inside the triangle ABC if and only if the cross products
    // AB × AP, BC × BP and CA × CP all have the same sign. In our case the
    // size n triangle has the coordinate (0,0), (2n,0), (n,√3n), which can be
    // reduced to the following inequality:
    //     0 < y ≤ √3x ≤ 2√3n - y
    private static boolean inside(Point pt, int n) {
        double x = pt.x * H, y = pt.y;
        return y > -EPS && x - y > -EPS && 2*H*n - y - x > -EPS;
    }

    private static boolean isValidTriangle(Line a, Line b, Line c, int n) {
        Point p1 = a.intersect(b);
        Point p2 = b.intersect(c);
        Point p3 = c.intersect(a);

        // lines must not be parallel
        if (p1 == null || p2 == null || p3 == null)
            return false;

        // lines should not intersect at same point
        if (abs(p1.x - p2.x) < EPS && abs(p1.y - p2.y) < EPS)
            return false;

        // intersect points must inside the size n triangle
        return inside(p1, n) && inside(p2, n) && inside(p3, n);
    }

    public static int solve(int n) {
        List<Line> lines = generateLines(n);
        int total = 0;

        for (int i = 0; i < lines.size(); i++) {
            Line a = lines.get(i);
            for (int j = i+1; j < lines.size(); j++) {
                Line b = lines.get(j);
                Point p = a.intersect(b);
                if (p == null || !inside(p, n))
                    continue;
                for (int k = j+1; k < lines.size(); k++) {
                    Line c = lines.get(k);
                    if (isValidTriangle(a, b, c, n))
                        total++;
                }
            }
        }
        return total;
    }

    private static List<Line> generateLines(int n) {
        List<Line> lines = new ArrayList<>();

        // lines with 0 degree slope
        for (int i = 0; i < n; i++)
            lines.add(new Line(i, i*H, 2*n-i, i*H, 0));

        // lines with 90 degree slope
        for (int i = 1; i <= n; i++)
            lines.add(new Line(i, 0, i, i*H, 90));
        for (int i = n+1; i < 2*n; i++)
            lines.add(new Line(i, 0, i, (2*n-i)*H, 90));

        // lines with 60 degree slope
        for (int i = 0; i < n; i++)
            lines.add(new Line(2*i, 0, n+i, (n-i)*H, 60));

        // lines with 120 degree slope
        for (int i = 1; i <= n; i++)
            lines.add(new Line(2*i, 0, i, i*H, 120));

        // lines with 30 degree slope
        for (int i = 0; i < n; i++)
            lines.add(new Line(2*i, 0, (3*n+i)/2.0, (n-i)*H/2, 30));
        for (int i = 1; i < n; i++)
            lines.add(new Line(i, i*H, (3*n-i)/2.0, (n+i)*H/2, 30));

        // lines with 150 degreee slope
        for (int i = 1; i <= n; i++)
            lines.add(new Line(2*i, 0, i/2.0, i*H/2, 150));
        for (int i = 1; i < n; i++)
            lines.add(new Line(2*n-i, i*H, (n+i)/2.0, (n+i)*H/2, 150));

        return lines;
    }

    public static void main(String[] args) {
        int n = 36;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
