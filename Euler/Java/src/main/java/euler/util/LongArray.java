package euler.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;

/**
 * Most of the time when you use an array is to place integers inside of it,
 * so why not have a super fast integer only array? This file contains an
 * implementation of an integer only array which can outperform Java's
 * ArrayList by about a factor of 10-15x.
 */
public class LongArray implements Iterable<Long> {
    // Default initial capacity.
    private static final int DEFAULT_CAP = 1 << 3;

    // Shared empty array instance used for empty instances.
    private static final long[] EMPTY_ARRAY = {};

    // Expose the array to gain performance boost
    public long[] a;
    public int length;

    /**
     * Initialize the array with a default capacity.
     */
    public LongArray() {
        a = EMPTY_ARRAY;
    }

    /**
     * Initialize the array with a certain capacity.
     */
    public LongArray(int capacity) {
        if (capacity > 0) {
            a = new long[capacity];
        } else if (capacity == 0) {
            a = EMPTY_ARRAY;
        } else {
            throw new IllegalArgumentException("Illegal capacity: " + capacity);
        }
    }

    private void ensureCapacity(int minCap) {
        if (a == EMPTY_ARRAY)
            minCap = Math.max(DEFAULT_CAP, minCap);
        if (minCap > a.length) {
            // overflow-conscious code
            int oldCap = a.length;
            int newCap = oldCap + (oldCap >> 1);
            if (newCap < minCap)
                newCap = minCap;
            if (newCap > Integer.MAX_VALUE - 8)
                throw new OutOfMemoryError();
            a = Arrays.copyOf(a, newCap);
        }
    }

    /**
     * Given an array make it a dynamic array.
     */
    public LongArray(long[] array) {
        a = array;
        length = array.length;
    }

    /**
     * Resize the array to the given length.
     */
    public void resize(int newsize) {
        resize(newsize, 0);
    }

    /**
     * Resize the array to the given length. Fill new element
     * with given value.
     */
    public void resize(int newsize, long fill) {
        if (newsize < 0)
            throw new IllegalArgumentException("Negative size");
        if (newsize > length) {
            ensureCapacity(newsize);
            Arrays.fill(a, length, newsize, fill);
        }
        length = newsize;
    }

    /**
     * Return the size of the array.
     */
    public int size() {
        return length;
    }

    /**
     * Return true or false on whether the array is empty.
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Get the element value in the given index.
     */
    public long get(int index) {
        if (index >= length)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+length);
        return a[index];
    }

    /**
     * Set the element value in the given index.
     */
    public void set(int index, long value) {
        ensureCapacity(index + 1);
        a[index] = value;
    }

    /**
     * Add an element to this dynamic array.
     */
    public void add(long value) {
        ensureCapacity(length + 1);
        a[length++] = value;
    }

    /**
     * Add an element at the given index.
     */
    public void add(int index, long value) {
        ensureCapacity(length + 1);
        System.arraycopy(a, index, a, index + 1, length - index);
        a[index] = value;
        length++;
    }

    /**
     * Removes the element at the specified index in this array.
     */
    public void removeAt(int index) {
        if (index >= length)
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+length);
        System.arraycopy(a, index + 1, a, index, length - index - 1);
        length--;
    }

    /**
     * Search and remove an element if it is found in the array.
     */
    public boolean remove(long value) {
        for (int i = 0; i < length; i++) {
            if (a[i] == value) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes all of the elements from this array.
     */
    public void clear() {
        length = 0;
    }

    /**
     * Reverse the contents of this array.
     */
    public void reverse() {
        for (int i = 0; i < length / 2; i++) {
            long t = a[i];
            a[i] = a[length - i - 1];
            a[length - i - 1] = t;
        }
    }

    /**
     * Sort the array.
     */
    public LongArray sort() {
        Arrays.sort(a, 0, length);
        return this;
    }

    /**
     * Return the Iterator for this array.
     */
    @Override
    public Iterator<Long> iterator() {
        return new Iterator<Long>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length;
            }

            @Override
            public Long next() {
                return a[index++];
            }
        };
    }

    /**
     * Performs the given action for each element of the array  until all
     * elements have been processed or the action throws an exception.
     *
     * @param action The action to be performed for each element
     */
    public void forEach(LongConsumer action) {
        long[] a = this.a;
        int len = this.length;
        for (int i = 0; i < len; i++) {
            action.accept(a[i]);
        }
    }

    /**
     * Returns the sum of integers in the array.
     */
    public long sum() {
        long[] a = this.a;
        int len = this.length;
        long sum = 0;
        for (int i = 0; i < len; i++)
            sum += a[i];
        return sum;
    }

    /**
     * Accumulate elements in this array.
     */
    public long accumulate(long z, LongBinaryOperator op) {
        long[] a = this.a;
        int len = this.length;
        for (int i = 0; i < len; i++)
            z = op.applyAsLong(z, a[i]);
        return z;
    }

    /**
     * Returns a copy of this array.
     */
    public long[] toArray() {
        return Arrays.copyOf(a, length);
    }

    public String toString() {
        int iMax = length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(a[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
