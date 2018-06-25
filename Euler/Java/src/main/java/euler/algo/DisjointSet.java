package euler.algo;

/**
 * <p>A disjoint-set data structure (also called a union-find data structure
 * or merge-find set) is a data structure that tracks a set of elements
 * partitioned into a number of disjoint (non-overlapping) subsets. It
 * provides near-constant-time operations (bounded by the inverse Ackermann
 * function) to add new sets, to merge existing sets, and to determine whether
 * elements are in the same set. In addition to many other uses, disjoint-set
 * play a key role in Kruskai's algorithm for finding the minimum spanning
 * tree of a graph.</p>
 *
 * <p>A disjoint-set forest consists of a number of elements each of which
 * stores an id, a parent pointer, and, in efficient algorithms, either a size
 * or a "rank" value.</p>
 *
 * <p>The parent pointers of elements are arranged to form one or more trees,
 * each representing a set. If an element's pointer points to no other element,
 * then the element is the root of a tree and is the representative member
 * of its set. A set may consist of only a single element. However, if the
 * element is part of whatever set is identified by following the chain of
 * parents upwards until a representative element (one without a parent) is
 * reached at the root of the tree.</p>
 */
public class DisjointSet {
    // The number of elements in this disjoint-set
    private final int size;

    // id[i] points to the parent of i, if id[i] = i then i is a root node
    private final int[] id;

    // Used to track the size of each of the component
    private final int[] sz;

    //  Tracks the number of components in the disjoint-set
    private int numComponents;

    /**
     * Construct a new disjoint-set data structure with the give size.
     *
     * @param size number of elements in this disjoint-set
     */
    public DisjointSet(int size) {
        if (size <= 0)
            throw new IllegalArgumentException("Size <= 0 is not allowed");

        this.size = this.numComponents = size;
        this.sz = new int[size];
        this.id = new int[size];

        // Makes a new set by creating a new element with a unique id, and
        // a parent pointer to itself. The parent pointer to itself indicates
        // that the element is the representative member of its own set.
        for (int i = 0; i < size; i++) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    /**
     * Follows the chain of parent pointers from x up the tree until it
     * reaches a root element, whose parent is itself. This root element
     * is the representative member of the set to which x belongs, and
     * may be x itself.
     */
    public int find(int x) {
        // Find the root of the component/set
        int root = x;
        while (root != id[root])
            root = id[root];

        // Compress the path leading back to the root.
        // Doing this operation is called "path compression"
        // and is what gives us amortized time complexity.
        while (x != root) {
            int next = id[x];
            id[x] = root;
            x = next;
        }

        return root;
    }

    /**
     * <p>Uses find to determine the roots of the tree x and y belong to. If the
     * roots are distinct, the trees are combined by attaching the root of one
     * to the root of the other. If this is done naively, such as by always
     * making x a child of y, the height of the tree can grow as O(n). To prevent
     * this merge by size is used.</p>
     *
     * <p>Merge by size always attaches the tree with fewer elements to the root
     * of the tree having more elements.</p>
     */
    public boolean merge(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);

        // these elements are already in the same set
        if (xRoot == yRoot)
            return false;

        // x and y are not in same set, so we merge them
        if (sz[xRoot] < sz[yRoot]) {
            sz[yRoot] += sz[xRoot];
            id[xRoot] = yRoot;
        } else {
            sz[xRoot] += sz[yRoot];
            id[yRoot] = xRoot;
        }

        // since the roots found are different we know that the
        // number of components/sets has decreased by one.
        numComponents--;
        return true;
    }

    /**
     * Returns whether or not the element 'x' and 'y' are in the same
     * components/set.
     */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * Return the number of elements in this Disjoint set.
     */
    public int size() {
        return size;
    }

    /**
     * Returns the number of remaining components/sets.
     */
    public int components() {
        return numComponents;
    }

    /**
     * Returns the size of components/set 'x' belongs to.
     */
    public int componentSize(int x) {
        return sz[find(x)];
    }
}
