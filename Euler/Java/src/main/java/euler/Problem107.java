package euler;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import euler.algo.DisjointSet;

public final class Problem107 {
    private Problem107() {}

    private static class Edge {
        int u, v, w;
        Edge(int u, int v, int w) {
            this.u = u; this.v = v; this.w = w;
        }
    }

    private static int solve(int vertices, List<Edge> edges) {
        DisjointSet set = new DisjointSet(vertices);
        int totalWeight = 0, optimumWeight = 0;

        edges.sort((a, b) -> Integer.compare(a.w, b.w));
        for (Edge edge : edges) {
            if (set.merge(edge.u, edge.v))
                optimumWeight += edge.w;
            totalWeight += edge.w;
        }
        return totalWeight - optimumWeight;
    }

    public static int solve(int[][] matrix) {
        int vertices = matrix.length;
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            for (int j = i + 1; j < vertices; j++) {
                if (matrix[i][j] > 0)
                    edges.add(new Edge(i, j, matrix[i][j]));
            }
        }
        return solve(matrix.length, edges);
    }

    private static List<Edge> readGraph(int vertices, InputStream stream) {
        Scanner in = new Scanner(stream).useDelimiter(",|\\s+");
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                String tok = in.next();
                if (j > i && !"-".equals(tok))
                    edges.add(new Edge(i, j, Integer.parseInt(tok)));
            }
        }
        return edges;
    }

    public static void main(String[] args) throws IOException {
        int n;
        InputStream in;

        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            in = new FileInputStream(args[1]);
        } else {
            n = 40;
            in = Problem107.class.getResourceAsStream("p107_network.txt");
        }
        System.out.println(solve(n, readGraph(n, in)));
    }
}
