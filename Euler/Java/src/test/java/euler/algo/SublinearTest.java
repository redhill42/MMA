package euler.algo;

import org.junit.Test;

import static euler.algo.Library.pow;
import static euler.algo.Sublinear.sqfn;
import static org.junit.Assert.assertEquals;

public class SublinearTest {
    @Test
    public void sqfnSmallNumbers() {
        long [] results = {
            0,  1,  2,  3,  3,  4,  5,  6,  6,  6,  7,  8,
            8,  9, 10, 11, 11, 12, 12, 13, 13, 14, 15, 16,
            16, 16, 17, 17, 17, 18, 19, 20, 20, 21, 22, 23,
            23, 24, 25, 26, 26, 27, 28, 29, 29, 29, 30, 31,
            31, 31, 31, 32, 32, 33, 33, 34, 34, 35, 36, 37,
            37, 38, 39, 39, 39, 40, 41, 42, 42, 43, 44, 45,
            45, 46, 47, 47, 47, 48, 49, 50, 50, 50, 51, 52,
            52, 53, 54, 55, 55, 56, 56, 57, 57, 58, 59, 60,
            60, 61, 61, 61, 61
        };

        for (int n = 0; n < results.length; n++)
            assertEquals(results[n], sqfn(n));
    }

    @Test
    public void sqfnLargeNumbers() {
        long[] results = {
            1L,
            7L,
            61L,
            608L,
            6083L,
            60794L,
            607926L,
            6079291L,
            60792694L,
            607927124L,
            6079270942L,
            60792710280L,
            607927102274L,
            6079271018294L,
            60792710185947L,
            607927101854103L,
            6079271018540405L,
            60792710185403794L,
            607927101854022750L,
        };

        for (int n = 0; n < results.length; n++) {
            assertEquals(results[n], sqfn(pow(10, n)));
        }
    }

    @Test
    public void sqfnNegativeNumbers() {
        assertEquals(0, sqfn(-100));
    }
}
