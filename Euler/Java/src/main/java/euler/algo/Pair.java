package euler.algo;

import java.io.Serializable;

/**
 * The Pair representing a pair of integers.
 */
public class Pair implements Serializable {
    private static final long serialVersionUID = -7524127358307327427L;

    /**
     * The first integer int the pair.
     */
    public long x;

    /**
     * The second integer in the pair.
     */
    public long y;

    /**
     * Constructs and initializes a pair of (0,0).
     */
    public Pair() {
        this(0, 0);
    }

    /**
     * Constructs and initializes a pair with the same pair as the specified
     * object.
     */
    public Pair(Pair p) {
        this(p.x, p.y);
    }

    /**
     * Constructs and initializes a pair of integers.
     */
    public Pair(long x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the first integer in the pair.
     */
    public long first() {
        return x;
    }

    /**
     * Returns the second integer in the pair.
     */
    public long second() {
        return y;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Pair) {
            Pair other = (Pair)obj;
            return x == other.x && y == other.y;
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return Long.hashCode(x) * 31 + Long.hashCode(y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
