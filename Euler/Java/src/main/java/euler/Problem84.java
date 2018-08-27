package euler;

import java.util.Arrays;
import java.util.Random;

import euler.algo.Pair;

@SuppressWarnings("unused")
public final class Problem84 {
    private Problem84() {}

    private static final int GO  = 0;
    private static final int A1  = 1;
    private static final int CC1 = 2;
    private static final int A2  = 3;
    private static final int T1  = 4;
    private static final int R1  = 5;
    private static final int B1  = 6;
    private static final int CH1 = 7;
    private static final int B2  = 8;
    private static final int B3  = 9;
    private static final int JAIL = 10;
    private static final int C1  = 11;
    private static final int U1  = 12;
    private static final int C2  = 13;
    private static final int C3  = 14;
    private static final int R2  = 15;
    private static final int D1  = 16;
    private static final int CC2 = 17;
    private static final int D2  = 18;
    private static final int D3  = 19;
    private static final int FP  = 20;
    private static final int E1  = 21;
    private static final int CH2 = 22;
    private static final int E2  = 23;
    private static final int E3  = 24;
    private static final int R3  = 25;
    private static final int F1  = 26;
    private static final int F2  = 27;
    private static final int U2  = 28;
    private static final int F3  = 29;
    private static final int G2J = 30;
    private static final int G1  = 31;
    private static final int G2  = 32;
    private static final int CC3 = 33;
    private static final int G3  = 34;
    private static final int R4  = 35;
    private static final int CH3 = 36;
    private static final int H1  = 37;
    private static final int T2  = 38;
    private static final int H2  = 39;

    private static int[] simulate(int dice, int iterations) {
        Random random = new Random();

        int[] board = new int[40];
        int cnt = 0;
        int pos = GO;
        int doubles = 0;

        while (cnt++ < iterations) {
            int d1 = random.nextInt(dice) + 1;
            int d2 = random.nextInt(dice) + 1;

            pos = (pos + d1 + d2) % 40;

            if (d1 == d2)
                doubles++;
            else
                doubles = 0;

            switch (pos) {
            case CC1: case CC2: case CC3:
                switch (random.nextInt(16) + 1) {
                case 1:
                    pos = GO;
                    break;

                case 2:
                    pos = JAIL;
                    break;
                }
                break;

            case CH1: case CH2: case CH3:
                switch (random.nextInt(16) + 1) {
                case 1:
                    pos = GO;
                    break;

                case 2:
                    pos = JAIL;
                    break;

                case 3:
                    pos = C1;
                    break;

                case 4:
                    pos = E3;
                    break;

                case 5:
                    pos = H2;
                    break;

                case 6:
                    pos = R1;
                    break;

                case 7: case 8: // go to next R
                    if (pos == CH1)
                        pos = R2;
                    else if (pos == CH2)
                        pos = R3;
                    else if (pos == CH3)
                        pos = R1;
                    break;

                case 9: // go to next U
                    if (pos == CH1)
                        pos = U1;
                    else if (pos == CH2)
                        pos = U2;
                    else if (pos == CH3)
                        pos = U1;
                    break;

                case 10:
                    pos -= 3;
                    break;
                }
            }

            if (pos == G2J || doubles == 3)
                pos = JAIL;

            board[pos]++;
        }

        return board;
    }

    public static String solve(int dice) {
        int[] board = simulate(dice, 5_000_000);
        Pair[] indexed = new Pair[board.length];

        for (int i = 0; i < board.length; i++)
            indexed[i] = new Pair(i, board[i]);

        Arrays.sort(indexed, (a, b) -> Long.compare(b.second(), a.second()));
        return String.format("%02d%02d%02d", indexed[0].first(), indexed[1].first(), indexed[2].first());
    }

    public static void main(String[] args) {
        int n = 4;
        if (args.length > 0)
            n = Integer.parseInt(args[0]);
        System.out.println(solve(n));
    }
}
