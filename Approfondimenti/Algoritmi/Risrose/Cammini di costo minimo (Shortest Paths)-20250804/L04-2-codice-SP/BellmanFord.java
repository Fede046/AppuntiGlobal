/**
 * BellmanFord.java
 *
 * Implementation of Bellman-Ford's Single-Source-Shortest-Path algorithm
 * for directed, weighted graphs. Negative edge weights are allowed.
 *
 * This program reads the input graph from a text file. The file
 * contains two integers n (number of nodes) and m (number of edges).
 * Then, m lines follow, where each line has the structure:
 *
 * src dst w
 *
 * where src, dst are the source and destination node of an edge
 * (integers), and w is the corresponding weight (double).
 *
 * To compile: javac BellmanFord.java
 *
 * To execute: java BellmanFord <fileIn>
o)
 * (C) 2020, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * (C) 2022 Jocelyne Elias (https://www.unibo.it/sitoweb/jocelyne.elias/)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.io.*;
import java.util.*;

public class BellmanFord {

    int n;      // number of nodes
    int m;      // number of edges
    LinkedList< Edge > edges; // the graph is represented as a list of edges
    int source; // the source node
    int[] p;    // array of parents
    double[] d; // array of distances from the source
    Edge[] sp;  // sp[v] is the edge connecting v and its parent p[v]

    /**
     * Edge of a weighted, directed graph
     */
    private class Edge {
        final int src;
        final int dst;
        final double w;

        public Edge(int src, int dst, double w)
        {
            this.src = src;
            this.dst = dst;
            this.w = w;
        }
    }

    /**
     * The constructor reads the graph from the input file
     *
     * @param inputf input file name
     */
    public BellmanFord(String inputf)
    {
        this.source = source;
        readGraph(inputf);
    }

        /**
     * Prints the shortest path from |source| to |dst|
     */
    protected void print_path(int dst)
    {
        if (dst == source)
            System.out.print(dst);
        else if (p[dst] < 0)
            System.out.print("Irraggiungibile");
        else {
            print_path(p[dst]);
            System.out.print("->" + dst);
        }
    }
    
    /**
     * Dump the shortest path tree to standard output.
     */
    public void print_paths( )
    {
        System.out.println("Source = " + source);
        System.out.println();
        System.out.println("   s    d         dist path");
        System.out.println("---- ---- ------------ -------------------");
        for (int dst=0; dst<n; dst++) {
            System.out.printf("%4d %4d %12.4f ", source, dst, d[dst]);
            print_path(dst);
            System.out.println();
        }
    }

    /**
     * Read the input graph from a file named inputf
     *
     * @param inputf input file name
     */
    private void readGraph(String inputf)
    {
        Locale.setDefault(Locale.US);

        try {
            Scanner f = new Scanner(new FileReader(inputf));
            n = f.nextInt();
            m = f.nextInt();
            edges = new LinkedList< Edge >();

            for (int i=0; i<m; i++) {
                final int src = f.nextInt();
                final int dst = f.nextInt();
                final double weight = f.nextDouble();
                edges.add(new Edge(src, dst, weight));
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Executes the Bellman-Ford shortest paths algorithm using s as the
     * source node.
     *
     * @param s source node
     * @result true if negative cycle detected
     */
    public boolean shortestPaths( int s )
    {
	d = new double[n];
        p = new int[n];
        sp = new Edge[n];

        Arrays.fill(d, Double.POSITIVE_INFINITY);
        Arrays.fill(p, -1);

	d[s] = 0.0;
        for (int i=0; i<n-1; i++) {
            for (Edge e: edges) {
                final int src = e.src;
                final int dst = e.dst;
                final double w = e.w;
                // relax
                if (d[src] + w < d[dst]) {
                    d[dst] = d[src] + w;
                    p[dst] = src;
                    sp[dst] = e;
                }
            }
        }
        // Check for the presence of negative-weights cycles
        for (Edge e: edges) {
            final int src = e.src;
            final int dst = e.dst;
            final double w = e.w;

            if (d[src] + w < d[dst]) {
                return true;
            }
        }
        return false;
    }

    /**
     * The main procedure creates a BellmanFord object, invokes the
     * algorithm, and generates the output containing the tree with
     * the shortest paths. If there is a negative-weight cycle, a
     * message is shown.
     */
    public static void main( String args[] )
    {
        if (args.length != 1) {
            final int n = 100;
            System.out.printf("%d %d\n", n, n*(n-1));
            for (int i=0; i<n; i++) {
                for (int j=0; j<n; j++) {
                    if (i != j) {
                        final double weight = 0.1 + Math.random() * 100;
                        System.out.printf("%d %d %f\n", i, j, weight);
                    }
                }
            }
            return;
        }

        BellmanFord sp = new BellmanFord(args[0]);

	  //the source is the node whose id is zero
        if (sp.shortestPaths(0)) {
            System.err.println("Negative cycles detected");
        } else {
            sp.print_paths();
        }
    }
}
