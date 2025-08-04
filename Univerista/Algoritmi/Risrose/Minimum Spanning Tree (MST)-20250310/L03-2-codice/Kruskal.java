/**
 * Implementation of the Kruskal's algorithm to find a Minimum Spanning
 * Tree (MST) of a weighted, undirected graph.
 *
 * The input file must be formatted as follows: a first line contains
 * two integers, the number n of nodes and the number m of edges,
 * respectively.  Then m lines follow: each line contains three
 * numbers: two integers between 0 and n-1 (the nodes connected by an
 * edge) and a double (the corresponding weight). The computed MST is
 * described in an output file.
 *
 * To compile: javac Kruskal.java
 *
 * To execute: java Kruskal <fileIn>
 *
 * (C) 2017 Gianluigi Zavattaro (https://www.unibo.it/sitoweb/gianluigi.zavattaro)
 * (C) 2020, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * (C) 2022, Jocelyne Elias (https://www.unibo.it/sitoweb/jocelyne.elias)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.io.*;
import java.util.*;


/* Implementation of the Kruskal's algorithm */
public class Kruskal {

    int n;      // number of nodes
    int m;      // number of edges
    Edge[] E;   // Array of edges of the input graph
    LinkedList<Edge> mst; // List of edges that belong to the MST
    double wtot; // total weight of the MST

    /**
     * Kruskal's algorithm is based on a greedy technique. 
     * It tries to add edges to the MST in order of
     * nondecreasing weight. If an edge does not produce a cycle on the
     * MST, it becomes part of the MST.
     *
     * Kruskal's algorithm uses a Union-Find data structure to
     * efficiently check whether an edge introduces a cycle. In this
     * file we implement the simplest variant of the QuickUnion data
     * structure without any optimization.
     */
    private class UnionFind {
        
        int[] p;        // array of parents
        final int n;    // number of nodes in the set
        
        public UnionFind( int n ) 
        {
            assert (n > 0);
            
            this.p = new int[n];
            this.n = n;
            for (int i=0; i<n; i++) {
                p[i] = i;
            }
        }
        
        public void union( int i, int j )
        {
            assert ((i>=0) && (i<n));
            assert ((j>=0) && (j<n));
            
            final int ri = find(i);
            final int rj = find(j);
            if ( ri != rj ) {
                p[rj] = ri;
            }
        }
        
        public int find( int i )
        {
            assert ((i>=0) && (i<n));
            
            while ( p[i] != i ) {
                i = p[i];
            }
            return i;
        }
    }

    
    /**
     * Edge is an (undirected) graph edge. Since Kruskal's algorithm
     * requires us to sort edges according to their weight, this class
     * must implement the Comparable interface.
     */
    private class Edge implements Comparable {
        
        public final int src;
        public final int dst;
        public final double w;
        
        public Edge( int src, int dst, double w ) {
            this.src = src;
            this.dst = dst;
            this.w = w;
        }
        
        public int compareTo( Object other ) {
            if ( other == this ) {
                return 0;
            } else {
                if ( this.w > ((Edge)other).w ) {
                    return 1;
                } else if (this.w == ((Edge)other).w) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
    }

    /**
     * The constructor reads the graph from the input file 
     *
     * @param inputf name of the input file
     */
    public Kruskal(String inputf)
    {
        mst = new LinkedList<Edge>();

        /* Read input data */
        readGraph(inputf);
        
        /* Run Kruskal's algorithm to compute the MST */
        MST();
    }

    /**
     * Dump the MST to standard output
     */
    public void dump( )
    {
        System.out.printf("%d %d\n", n, mst.size());
        for (Edge e : mst) {
            System.out.printf("%d %d %f\n", e.src, e.dst, e.w);
        }
        System.out.printf("# MST weight = %f\n", wtot);
    }
    
    /**
     * Read input data
     *
     * @input inputf Name of the input file
     */
    private void readGraph(String inputf)
    {
        Locale.setDefault(Locale.US);
        
        try {
            Scanner f = new Scanner(new FileReader(inputf));
            n = f.nextInt();
            m = f.nextInt();
            E = new Edge[m];
            
            for (int i=0; i<m; i++) {
		final int src = f.nextInt();
	     	final int dst = f.nextInt();
	     	final double weight = f.nextDouble();
                E[i] = new Edge(src, dst, weight);
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }   
    }
    
    /**
     * Compute the MST using Kruskal's algorithm
     */
    private void MST( )
    {
        UnionFind UF = new UnionFind(n);
        
	Arrays.sort( E );       // sort edges by weight
        wtot = 0.0;             // total weight of the MST
        
	for (int k=0; k<E.length; k++) {
            final int src = E[k].src;
            final int dst = E[k].dst;
            final double w = E[k].w;
            if ( UF.find(src) != UF.find(dst) ) {
		UF.union(src, dst);
                mst.add(E[k]);
		wtot += w;
	    }
	}
    }


    /**
     * The main procedure creates a Kruskal object, invokes Kruskal's
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

        Kruskal mst = new Kruskal(args[0]);
        mst.dump();
    }
}    


