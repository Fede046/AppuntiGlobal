/**
 * Implementation of Prim's algorithm to find a Minimum Spanning Tree
 * (MST) of a weighted, undirected graph.
 *
 * The input file must be formatted as follows: an initial line
 * contains two integers, the number n of nodes and the number m of
 * edges, respectively.  Then m lines follow: each line contains three
 * numbers, two integers between 0 and n-1 (the nodes connected by an
 * edge) and a double (the corresponding weight).
 *
 * To compile: javac Prim.java MinHeap.java
 *
 * To execute: java Prim <fileIn>
 *
 * (C) 2017 Gianluigi Zavattaro (https://www.unibo.it/sitoweb/gianluigi.zavattaro)
 * (C) 2020, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * (C) 2022 Jocelyne Elias (https://www.unibo.it/sitoweb/jocelyne.elias)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 * 
 */

import java.io.*;
import java.util.*;

/*****************************************************************************

Implementation of Prim's algorithm

******************************************************************************/
public class Prim {
    
    int n;       // number of nodes in the input graph
    int m;       // number of edges in the input graph
    Vector< LinkedList<Edge> > adjList; // the input graph is stored using adjacency lists
    LinkedList<Edge> mst; // edges belonging to the MST
    double wtot; // total weight of the MST
    
    /**
     * Edge of a weighted, undirected graph
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

        public int opposite(int v)
        {
            assert ((src == v) || (dst == v));
            
            return (v == src ? dst : src);
        }
    }

    /* The constructor reads the graph from the input file */
    public Prim(String inputf)
    {
        wtot = 0.0;
        mst = new LinkedList<Edge>();
        
        /* Read input data */
        readGraph(inputf);
        
        /* Run Prim's algorithm to compute the MST using node 0 as the
           root (if the graph is connected, the root can actually
           be any node) */
        MST(0);
    }

    public void dump( )
    {
        System.out.printf("%d %d\n", n, mst.size());
        for ( Edge e : mst ) {
            System.out.printf("%d %d %f\n", e.src, e.dst, e.w);
        }
        System.out.printf("# MST weight = %f\n", wtot);                
    }
    
    private void readGraph(String inputf)
    {                
        Locale.setDefault(Locale.US);
        
        try {
	     Scanner f = new Scanner(new FileReader(inputf));
	     n = f.nextInt();
	     m = f.nextInt();
             
             adjList = new Vector< LinkedList<Edge> >(n);

             for (int i=0; i<n; i++) {
                 adjList.add(i, new LinkedList<Edge>());
             }
             
	     for (int i=0; i<m; i++) {
                 final int src = f.nextInt();
                 final int dst = f.nextInt();
                 final double weight = f.nextDouble();
                 final Edge newEdge = new Edge(src, dst, weight);
                 adjList.get(src).add( newEdge );
                 adjList.get(dst).add( newEdge );
	     }
        } catch (IOException ex) {
	     System.err.println(ex);
	     System.exit(1);
        }
    }

    private void MST( int s )
    {
        /* mst_edges[v] is the edge connecting v with its parent in the MST */
        Edge[] mst_edges = new Edge[n];
        
        /* d[v] is the minimum weight among edges connecting
           v to a neighbor already in the MST */
	double[] d = new double[n];

        /* added[v] == true iff node v has already been added to the
           MST, and therefore should be ignored */
	boolean[] added = new boolean[n];

        /* Array of parents; this is actually not necessary in this
           implementation, but is included anyway to be consistent
           with the pseudocode */
        int[] p = new int[n];

        Arrays.fill(d, Double.POSITIVE_INFINITY);
        Arrays.fill(p, -1);
        Arrays.fill(added, false);
        
	  d[s] = 0.0;
        wtot = 0.0;

        MinHeap q = new MinHeap(n);

        for (int v=0; v<n; v++) {
            q.insert(v, d[v]);
        }       

	while ( !q.isEmpty() ) {
	    final int u = q.min();
            q.deleteMin();
	       added[u] = true;
            wtot += d[u];
            if (mst_edges[u] != null) {
                mst.add(mst_edges[u]);
            }            
            Iterator<Edge> iter = adjList.get(u).iterator();
	    while ( iter.hasNext() ) {
                final Edge edge = iter.next();
                final double w = edge.w;
		     final int v = edge.opposite(u);
	          if (!added[v] && (w < d[v])) {
                    d[v] = w;
                    q.changePrio(v, d[v]);
                    p[v] = u;
                    mst_edges[v] = edge;
		     }
	    }
	 }
    }

    /**
     * The main procedure creates a Prim object, invokes the Prim
     * algorithm, and dumps the MST.
     */    
    public static void main( String args[])
    {
        if (args.length != 1) {
            final int n = 100;
            System.out.printf("%d %d\n", n, n*(n-1)/2);
            for (int i=0; i<n-1; i++) {
                for (int j=i+1; j<n; j++) {
                    final double weight = Math.random() * 100;
                    System.out.printf("%d %d %f\n", i, j, weight);
                }
            }
            return;
        }
	
        Prim mst = new Prim(args[0]);
        mst.dump();
    }    
}    


