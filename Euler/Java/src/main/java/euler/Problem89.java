package euler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static euler.algo.Library.fromRomanNumeral;
import static euler.algo.Library.toRomanNumeral;

public final class Problem89 {
    private Problem89() {}

    public static int solve(String[] data) {
        return solve(Arrays.asList(data));
    }

    private static int solve(List<String> data) {
        int res = 0;
        for (String roman : data)
            res += roman.length() - toRomanNumeral(fromRomanNumeral(roman)).length();
        return res;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(Problem89.class.getResourceAsStream("p089_roman.txt"));
        try {
            List<String> data = new ArrayList<>();
            while (in.hasNext())
                data.add(in.next());
            System.out.println(solve(data));
        } finally {
            in.close();
        }
    }
}
