package euler;

import static java.lang.Math.atan2;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.round;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

public final class Problem177{
    private Problem177() {}

    private static final double EPS = 1e-9;

    public static int solve() {
        double[] sin = new double[180];
        double[] cos = new double[180];
        for (int i = 0; i < 180; i++) {
            sin[i] = Math.sin(toRadians(i));
            cos[i] = Math.cos(toRadians(i));
        }

        int count = 0;
        for (int alpha = 2; alpha <= 90; alpha++) {
            for (int diagB = 1; diagB < 180 - alpha; diagB++) {
                double AD = sin[diagB] / sin[alpha + diagB];
                for (int beta = max(alpha, diagB + 1); beta <= 180 - alpha; beta++) {
                    for (int diagA = beta == alpha ? diagB : 1; diagA < min(alpha, 180-beta); diagA++) {
                        double AC = sin[beta] / sin[beta + diagA];
                        double x = toDegrees(atan2(sin[diagA] * AC - sin[alpha] * AD,
                                                   cos[diagA] * AC - cos[alpha] * AD));
                        int rx = (int)round(x);
                        int delta = 180 - alpha + rx;
                        int gamma = 180 - beta - rx;
                        if (gamma < alpha)
                            break;
                        if (delta < beta)
                            continue;
                        if (beta == delta && diagA > alpha / 2)
                            continue;
                        if (gamma == alpha && diagB > beta / 2)
                            continue;
                        if (Math.abs(x - rx) < EPS)
                            count++;
                    }
                }
            }
        }
        return count;
    }

    public static void main(String[] args){
        System.out.println(solve());
    }
}
