package euler.algo;

import java.util.Comparator;

public final class Permutations {
    private Permutations() {}

    public static boolean next(int[] a) {
        int n = a.length;

        // Find longest non-increasing suffix
        int i = n - 1;
        while (i > 0 && a[i - 1] >= a[i])
            i--;
        if (i == 0) {
            reverse(a, 0, n - 1);
            return false;
        }

        // Let a[i - 1] be the pivot, find rightmost element that exceeds the pivot
        int pivot = a[i - 1];
        int j = n - 1;
        while (a[j] <= pivot)
            j--;

        // Swap the pivot with j and reverse the suffix
        swap(a, i - 1, j);
        reverse(a, i, n - 1);
        return true;
    }

    public static boolean previous(int[] a) {
        int n = a.length;

        // Find longest non-increasing suffix
        int i = n - 1;
        while (i > 0 && a[i - 1] <= a[i])
            i--;
        if (i == 0) {
            reverse(a, 0, n - 1);
            return false;
        }

        // Let a[i - 1] be the pivot, find rightmost element that exceeds the pivot
        int pivot = a[i - 1];
        int j = n - 1;
        while (a[j] >= pivot)
            j--;

        // Swap the pivot with j and reverse the suffix
        swap(a, i - 1, j);
        reverse(a, i, n - 1);
        return true;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void reverse(int[] a, int from, int to) {
        while (from < to) {
            swap(a, from, to);
            from++;
            to--;
        }
    }

    public static <T> boolean next(T[] a, Comparator<T> c) {
        int n = a.length;

        // Find longest non-increasing suffix
        int i = n - 1;
        while (i > 0 && c.compare(a[i - 1], a[i]) >= 0)
            i--;
        if (i == 0) {
            reverse(a, 0, n - 1);
            return false;
        }

        // Let a[i - 1] be the pivot, find rightmost element that exceeds the pivot
        T pivot = a[i - 1];
        int j = n - 1;
        while (c.compare(a[j], pivot) <= 0)
            j--;

        // Swap the pivot with j and reverse the suffix
        swap(a, i - 1, j);
        reverse(a, i, n - 1);
        return true;
    }

    public static <T extends Comparable<T>> boolean next(T[] a) {
        return next(a, Comparator.naturalOrder());
    }

    public static <T> boolean previous(T[] a, Comparator<T> c) {
        return next(a, c.reversed());
    }

    public static <T extends Comparable<T>> boolean previous(T[] a) {
        return next(a, Comparator.reverseOrder());
    }

    private static <T> void swap(T[] a, int i, int j) {
        T t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static <T> void reverse(T[] a, int from, int to) {
        while (from < to) {
            swap(a, from, to);
            from++;
            to--;
        }
    }
}
