/**
 * Implementation of DFS (Depth-First Search) algorithm.
 *
 * The input file must be formatted as follows: an initial line
 * contains two integers, the number n of nodes and the number m of
 * edges, respectively.  Then m lines follow: each line contains three
 * numbers, two integers between 0 and n-1 (the nodes connected by an
 * edge) and a double (the corresponding weight, that is ignored here).
 *
 * To compile: javac DFS.java 
 *
 * To execute: java -ea DFS <fileIn>
 *
 * (C) 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.io.*;
import java.util.*;

public class DFS {

    enum Color {WHITE, GREY, BLACK};

    int n;      // number of nodes in the input graph
    int m;      // number of edges in the input graph
    Vector< LinkedList<Edge> > adjList; // the input graph is stored using adjacency lists
    int parent[];  // parent[v] is the predecessor of node v in the DFS tree
    Color color[]; // color[v] is the color of node v
    int discover[], finish[]; // discover[v], finish[v] are the discover and finish times of node v
    int t;         // Current time
    
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

    /* The constructor reads the graph from the input file and
       performs a full DFS visit of the whole graph */
    public DFS(String inputf)
    {
        readGraph(inputf);
    }

    public void dump( )
    {
        System.out.printf("v\tparent[v]\tdisc\tfinish\n");
        for (int v=0; v<n; v++)  {
            System.out.printf("%d\t%d\t%d\t%d\n", v, parent[v], discover[v], finish[v]);
        }
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
	     }
        } catch (IOException ex) {
	     System.err.println(ex);
	     System.exit(1);
        }
    }

    private void DFS_visit( int u )
    {
        color[u] = Color.GREY;
        t++;                
        discover[u] = t;

        Iterator<Edge> iter = adjList.get(u).iterator();
        while ( iter.hasNext() ) {
            final Edge edge = iter.next();
            final int v = edge.dst;
            if (color[v] == Color.WHITE) {
                parent[v] = u;
                DFS_visit(v);
            }
        }
        
        t++;
        finish[u] = t;
        color[u] = Color.BLACK;
    }
    
    public void visit( )
    {
        parent = new int[n];
        color = new Color[n];
        discover = new int[n];
        finish = new int[n];
        
        Arrays.fill(parent, -1);
        Arrays.fill(color, Color.WHITE);
        Arrays.fill(discover, -1);
        Arrays.fill(finish, -1);

        t = 0;

        for (int v=0; v<n; v++) {
            if (color[v] == Color.WHITE) {
                DFS_visit(v);
            }
        }
    }

    /**
     * The main procedure creates a DFS object, invokes the DFS
     * algorithm, and dumps the DFS trees/forest.
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

        DFS dfs = new DFS(args[0]);                
        final long tstart = System.currentTimeMillis();
        dfs.visit();
        final long tend = System.currentTimeMillis();
        final double elapsed = (tend - tstart) / 1000.0;
        System.err.println("Elapsed time " + elapsed + " sec");
        dfs.dump();
    }    
}    


