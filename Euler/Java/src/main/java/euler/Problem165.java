/**
 * Cloudway Platform
 * Copyright (c) 2012-2016 Cloudway Technology, Inc.
 * All rights reserved.
 */

package euler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem165 {
    private final int limit;

    public Problem165(int limit) {
        this.limit = limit;
    }

    public int solve() {
        List<Segment> segments = new ArrayList<>();
        Set<Point> intersections = Collections.synchronizedSet(new HashSet<>());

        for (int i = 0; i < limit; i++) {
            Segment a = new Segment();
            a.p1 = new Point(next(), next());
            a.p2 = new Point(next(), next());

            for (Segment b : segments) {
                Point x = intersect(a.p1, a.p2, b.p1, b.p2);
                if (x != null)
                    intersections.add(x);
            }
            segments.add(a);
        }

        return intersections.size();
    }

    private long seed = 290797;

    // generate "Blum Blum Shub" pseudo-random numbers.
    private int next() {
        seed *= seed;
        seed %= 50515093;
        return (int)(seed % 500);
    }

    static class Point {
        double x, y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                Point other = (Point)obj;
                return Double.compare(x, other.x) == 0 &&
                       Double.compare(y, other.y) == 0;
            }
            return false;
        }

        public int hashCode() {
            return Double.hashCode(x) ^ Double.hashCode(y);
        }

        public String toString() {
            return "{" + x + ',' + y + '}';
        }
    }

    static class Segment {
        Point p1, p2;
    }

    private static final double PRECISION = 0.00000001;

    // find intersection of two segments
    private static Point intersect(Point a, Point b, Point c, Point d) {
        double ab_x = b.x - a.x;
        double ab_y = b.y - a.y;
        double cd_x = d.x - c.x;
        double cd_y = d.y - c.y;

        double r = ab_x * cd_y - cd_x * ab_y;
        if (r == 0)
            return null;

        double s = (ab_x * (a.y - c.y) - ab_y * (a.x - c.x)) / r;
        double t = (cd_x * (a.y - c.y) - cd_y * (a.x - c.x)) / r;
        if (s <= 0 || s >= 1 || t <= 0 || t >= 1)
            return null;

        double c_x = a.x + t * ab_x;
        double c_y = a.y + t * ab_y;

        // cut off a few digits to avoid rounding issues
        c_x = Math.round(c_x / PRECISION) * PRECISION;
        c_y = Math.round(c_y / PRECISION) * PRECISION;
        return new Point(c_x, c_y);
    }

    public static void main(String[] args) {
        System.out.println(new Problem165(5000).solve());
    }
}
