package euler.algo;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    private BitSet[] adj;

    /**
     * Initialize an empty graph with {@code V} vertices and 0 edges.
     *
     * @param V the number of vertices
     */
    public Graph(int V) {
        adj = new BitSet[V];
        Arrays.setAll(adj, i -> new BitSet(V));
    }

    /**
     * Initialize a new graph that is a deep copy of {@code G}.
     *
     * @param g the graph to copy
     */
    public Graph(Graph g) {
        adj = new BitSet[g.V()];
        Arrays.setAll(adj, i -> (BitSet)g.adj[i].clone());
    }

    /**
     * Returns the number of vertices in the graph.
     */
    public int V() {
        return adj.length;
    }

    /**
     * Returns the number of edges in the graph.
     */
    public int E() {
        int e = 0;
        for (BitSet row : adj)
            e += row.cardinality();
        return e / 2;
    }

    /**
     * Adds the undirected edge v-w to the graph.
     *
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     */
    public Graph addEdge(int v, int w) {
        adj[v].set(w);
        adj[w].set(v);
        return this;
    }

    /**
     * Remove an edge.
     */
    public void removeEdge(int v, int w) {
        adj[v].clear(w);
        adj[w].clear(v);
    }

    /**
     * Contract a graph between two vertices.
     */
    public void contract(int vToKeep, int vToRemove) {
        // iterate over each vertex
        for (int v = 0; v < adj.length; v++) {
            if (v == vToRemove) {
                for (int i = adj[v].nextSetBit(0); i >= 0; i = adj[v].nextSetBit(i+1)) {
                    if (i != vToKeep)
                        adj[vToKeep].set(i);
                }
            } else {
                if (adj[v].get(vToRemove)) {
                    adj[v].clear(vToRemove);
                    if (v != vToKeep)
                        adj[v].set(vToKeep);
                }
            }
        }

        // reorder graph
        int last = adj.length - 1;
        if (vToRemove != last) {
            adj[vToRemove] = adj[last];
            for (BitSet row : adj) {
                row.set(vToRemove, row.get(last));
                row.clear(last);
            }
        }

        adj = Arrays.copyOf(adj, adj.length - 1);
    }

    /**
     * Get neighbors for the given vertex.
     */
    public Set<Integer> getNeighbors(int v) {
        Set<Integer> res = new HashSet<>();
        BitSet row = adj[v];
        for (int i = row.nextSetBit(0); i >= 0; i = row.nextSetBit(i+1))
            res.add(i);
        return res;
    }

    /**
     * Returns true if the vertex has at least one neighbors.
     */
    public boolean hasNeighbors(int v) {
        return !adj[v].isEmpty();
    }

    /**
     * Find cycle in the graph.
     */
    public boolean findCycle(int[] cycle) {
        if (V() >= E() + 1)
            return false;

        DisjointSet set = new DisjointSet(V());
        for (int u = 0; u < adj.length; u++) {
            for (int v = u + 1; v < adj.length; v++) {
                if (adj[u].get(v) && !set.merge(u, v)) {
                    cycle[0] = u;
                    cycle[1] = v;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compute chromatic polynomial for this graph.
     */
    public Polynomial getChromaticPolynomial() {
        int[] cycle = new int[2];
        if (!findCycle(cycle)) {
            // returns x(x-1)^(n-1)
            Polynomial x = new Polynomial(1, 1);
            return x.mul(x.sub(1).pow(V() - 1));
        }

        Graph g1 = new Graph(this);
        Graph g2 = new Graph(this);

        g1.removeEdge(cycle[0], cycle[1]);
        g2.contract(cycle[0], cycle[1]);

        Polynomial p1 = g1.getChromaticPolynomial();
        Polynomial p2 = g2.getChromaticPolynomial();
        return p1.sub(p2);
    }

    /**
     * Returns a string representation of the graph.
     */
    public String toString() {
        StringBuilder b = new StringBuilder();

        b.append(V()).append(" vertices, ");
        b.append(E()).append(" edges\n");

        for (int v = 0; v < adj.length; v++) {
            b.append(v).append(": ");
            b.append(adj[v]);
            b.append("\n");
        }

        b.append("adjacency matrix\n");
        for (BitSet row : adj) {
            for (int i = 0; i < adj.length; i++)
                b.append(row.get(i) ? 1 : 0).append(" ");
            b.append("\n");
        }

        return b.toString();
    }
}
