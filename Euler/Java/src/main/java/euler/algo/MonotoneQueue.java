package euler.algo;

/**
 * <p>Montone queue is a kind data structure like queue, which can add element at
 * the end, and pop elements at both the front and end of the queue.</p>
 *
 * <p>The head element in the queue is the maximum/minimum value, it can retrieive
 * the maximum/minimum value in O(1) times.</p>
 *
 * <p>And the elements in queue are monotonous, that is a[i]<=a[i+1], i=1,2,...,k.</p>
 *
 * <p>Here are the classical usage:</p>
 * <p>Given an array with size N and value k, calculate:</p>
 * <pre>
 *               i
 *     f(i) = Min/Max(array[j]), i = k,...,n
 *            j=i-k+1
 * </pre>
 *
 * <p>The above equation often appears in dynamic equation. Using monotonous-queue we
 * can calculate all the values in O(n) time.</p>
 *
 * <p>Details: Supposing calculate the maximum value, and the new add element is v,
 * When adding new elements, the monotonous-queue rules are follows:</p>
 *
 * <ol>
 * <li>Scan the elements from the end of the queue to front, pop all elements until
 *     first facing one element which queue[j]>v, and the add element v after position j.</li>
 * <li>If the head element satisfy v.index - queue[head].index >= k, pop it, and the
 *     following maximum will be queue[head+1].value.</li>
 * </ol>
 */
public class MonotoneQueue {
    private final int windowSize;
    private final long[] items;
    private final int[] indices;
    private int head, tail;
    private int current;

    public MonotoneQueue(int windowSize) {
        this.windowSize = windowSize;

        // Find the best power of two to hold elements.
        int capacity = windowSize;
        capacity |= (capacity >>> 1);
        capacity |= (capacity >>> 2);
        capacity |= (capacity >>> 4);
        capacity |= (capacity >>> 8);
        capacity |= (capacity >>> 16);
        capacity++;

        items = new long[capacity];
        indices = new int[capacity];
    }

    /**
     * Offer a new element into queue and return the minimum/maximum value in the queue.
     */
    public long offer(long item) {
        while (!isEmpty() && lastItem() <= item)
            removeLast();
        addLast(item, current);

        while (firstIndex() <= current - windowSize)
            removeFirst();

        current++;
        return firstItem();
    }

    public boolean isEmpty() {
        return head == tail;
    }

    public long firstItem() {
        return items[head];
    }

    public long lastItem() {
        return items[(tail - 1) & (items.length - 1)];
    }

    private int firstIndex() {
        return indices[head];
    }

    private void addLast(long e, int i) {
        items[tail] = e;
        indices[tail] = i;
        tail = (tail + 1) & (items.length - 1);
        assert tail != head;
    }

    private void removeFirst() {
        head = (head + 1) & (items.length - 1);
    }

    private void removeLast() {
        tail = (tail - 1) & (items.length - 1);
    }
}
