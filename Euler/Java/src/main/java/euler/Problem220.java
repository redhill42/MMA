package euler;

import java.util.ArrayList;

import euler.algo.Matrix;
import euler.algo.Vector;

public final class Problem220 {
    private Problem220() {}

    @SuppressWarnings("unused")
    public static String bruteForce(long steps) {
        int x = 0, y = 0, dx = 0, dy = 1;
        for (long n = 1; n <= steps; n++) {
            x += dx; y += dy;
            if ((((n & -n) << 1) & n) != 0) {
                // turn left
                int t = dx;
                dx = -dy;
                dy = t;
            } else {
                // turn right
                int t = dx;
                dx = dy;
                dy = -t;
            }
        }
        return String.format("%d,%d", x, y);
    }

    private static class Rule {
        long x;
        Matrix r, l;

        Rule(long x, Matrix r, Matrix l) {
            this.x = x;
            this.r = r;
            this.l = l;
        }
    }

    public static String solve(long steps) {
        // matrices transform the vector (x, y, dx, dy)
        Matrix F = Matrix.valueOf(new long[][] {
            {1, 0, 1, 0}, {0, 1, 0, 1}, {0, 0, 1, 0}, {0, 0, 0, 1}
        });

        Matrix R = Matrix.valueOf(new long[][] {
            {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}, {0, 0, -1, 0}
        });

        Matrix L = Matrix.valueOf(new long[][] {
            {1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, -1}, {0, 0, 1, 0}
        });

        Matrix R1 = F.mul(R).mul(F);
        Matrix L1 = F.mul(L).mul(F);
        Vector v = Vector.valueOf(0, 0, 0, 1);

        long x = 0;
        Matrix r = R, l = L;
        ArrayList<Rule> rules = new ArrayList<>();
        while (x < steps) {
            Matrix r1 = l.mul(R1).mul(r);
            Matrix l1 = l.mul(L1).mul(r);
            r = r1; l = l1;
            x = 2 * x + 2;
            rules.add(new Rule(x, r, l));
        }

        v = F.mul(v);
        steps--;

        boolean in_r = true;
        for (int i = rules.size() - 1; i >= 0 && steps > 1; i--) {
            Rule rule = rules.get(i);

            if (rule.x > steps) {
                in_r = true;
                continue;
            }

            v = rule.r.mul(v);
            steps -= rule.x;
            if (steps >= 2) {
                v = in_r ? R1.mul(v) : L1.mul(v);
                steps -= 2;
            }

            in_r = false;
        }

        if (steps == 1)
            v = F.mul(v);

        return String.format("%d,%d", v.a(0), v.a(1));
    }

    public static void main(String[] args) {
        long n = (long)1e12;
        if (args.length > 0)
            n = Long.parseLong(args[0]);
        System.out.println(solve(n));
    }
}
