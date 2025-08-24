/**
 * FloydWarshall.java
 *
 *
 * Implementation of Floyd-Warshall algorithm for computing all-pair
 * shortest paths on a directed, weighted graph. Negative edge weights
 * are allowed.
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
 * To compile: javac FlydWarshall.java
 *
 * To execute: java FloydWarshall <fileIn>
o)
 * (C) 2020 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * (C) 2022 Jocelyne Elias (https://www.unibo.it/sitoweb/jocelyne.elias/)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.io.*;
import java.util.*;

public class FloydWarshall {

    int n;        // number of nodes in the graph
    int m;        // number of edges in the graph
    LinkedList< Edge > edges; // the graph is represented as a list of edges
    double[][] d; // matrix of distances 
    int[][] next; // next[u][v] is the successor of u on the shortest path to v
    
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
     * Constructor for the Floyd-Warshall algorithm
     *
     * @param inputf input file
     */
    public FloydWarshall(String inputf)
    {
        readGraph(inputf);
    }

    private void readGraph(String inputf)
    {
        Locale.setDefault(Locale.US);
        
        try {
            Scanner f = new Scanner(new FileReader(inputf));
            n = f.nextInt();
            m = f.nextInt();
            edges = new LinkedList<Edge>();
            
            for (int i=0; i<m; i++) {
                final int src = f.nextInt();
                final int dst = f.nextInt();
                final double weight = f.nextDouble();
                edges.add( new Edge(src, dst, weight) );
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }   
    }

    /**
     * Executes the Floyd-Warshall algorithm
     */
    public void shortestPaths( )
    {
        int u, v, k;
	  d = new double[n][n];
        next = new int[n][n];
        
        // Initialize distance matrix 
        for (u=0; u<n; u++) {
            for (v=0; v<n; v++) {
                d[u][v] = (u == v ? 0 : Double.POSITIVE_INFINITY);
                next[u][v] = (u == v ? u : -1);
            }
        }
        for (final Edge edge : edges) {
            final double w = edge.w;                        
            u = edge.src;
            v = edge.dst;
		// the distance between node u and node v without passing through any intermediate node            
            d[u][v] = w;
            next[u][v] = v;
        }
        // Main loop of the Floyd-Warshall algorithm
        for (k=0; k<n; k++) {
            for (u=0; u<n; u++) {
                for (v=0; v<n; v++) {
                    if (d[u][k] + d[k][v] < d[u][v]) {
                        d[u][v] = d[u][k] + d[k][v];
                        next[u][v] = next[u][k];
                    }
                }
            }
        }
        // Check for cycles of negative weight
        for (u=0; u<n; u++) {
            if (d[u][u] < 0.0) {
                System.out.println("Warning: negative-weight cycle(s) detected");
                break;
            }
        }        
    }

    /**
     * Print the shortest path from node u to node v, if such path
     * exists.
     */
    protected void printPath(int u, int v)
    {
	if ( (u != v) && (next[u][v] < 0) ) {
            System.out.print("Not reachable");
        } else {
            System.out.print(u);
            while ( u != v ) {
                u = next[u][v];
                System.out.printf("->%d", u);
            }
        }
    }

    /**
     * Print all distances and shortest paths
     */
    public void printPaths( )
    {
        int i, j;
        for (i=0; i<n; i++) {
            for (j=0; j<n; j++) {
                System.out.printf("%4d %4d %12.5f ", i, j, d[i][j]);
                printPath(i, j);
                System.out.println();
            }
        }
    }    

    /**
     * The main procedure creates a FloydWarshall object, invokes the
     * algorithm, and generates the output.
     */
    public static void main( String args[])
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
	
        FloydWarshall fw = new FloydWarshall(args[0]);
        fw.shortestPaths( );
        fw.printPaths( );
    }
}    
