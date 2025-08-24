/**
 * Ordinamento topologico
 *
 * The input file must be formatted as follows: an initial line
 * contains two integers, the number n of nodes and the number m of
 * edges, respectively.  Then m lines follow: each line contains three
 * numbers, two integers between 0 and n-1 (the nodes connected by an
 * edge) and a double (the corresponding weight, that is ignored here).
 *
 * To compile: javac Toposort.java 
 *
 * To execute: java -ea Toposort <fileIn>
 *
 * (C) 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */

import java.io.*;
import java.util.*;

public class Toposort {

    enum Color {WHITE, GREY, BLACK};

    int n;      // number of nodes in the input graph
    int m;      // number of edges in the input graph
    Vector< LinkedList<Edge> > adjList; // the input graph is stored using adjacency lists
    Color color[]; // color[v] is the color of node v (used during the DFS visit)
    LinkedList<Integer> result; // nodes in topological order
    
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

    /* The constructor reads the graph from the input file */
    public Toposort(String inputf)
    {
        readGraph(inputf);
    }

    public void dump( )
    {
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            final int v = iter.next();
            System.out.println(v);
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
                 adjList.get(src).add( newEdge ); // get(src): returns the element at the specified position (src) in this Vector.
	     }
        } catch (IOException ex) {
	     System.err.println(ex);
	     System.exit(1);
        }
    }

    /* Restituisce "true" se viene scoperto un ciclo, "false" altrimenti */
    private boolean sort_rec( int u )
    {
        if (color[u] == Color.BLACK)
            return false;

        if (color[u] == Color.GREY)
            return true;
		
        color[u] = Color.GREY;

        Iterator<Edge> iter = adjList.get(u).iterator();
        while ( iter.hasNext() ) {
            final Edge edge = iter.next();
            final int v = edge.dst;
            if (sort_rec(v)) return true;
        }
        color[u] = Color.BLACK;
        result.addFirst(u);
        return false;
    }

    
    /**
     * Calcola un possibile ordinamento topologico del grafo.
     * Restituisce "true" se e solo se è stato trovato un ciclo (in
     * tal caso l'ordinamento topologico non esiste). Se esiste un
     * ordinamento topologico, al termine la lista "result" conterrà i
     * nodi nell'ordine corretto.
     */
    public boolean sort( )
    {
        boolean cycle = false;
        color = new Color[n];
        Arrays.fill(color, Color.WHITE);
        
        result = new LinkedList<Integer>();
        
        for (int v=0; v<n && !cycle; v++) {
            if (sort_rec(v)) cycle = true;
        }

        return cycle;
    }

    /**
     * The main procedure creates a Topsort object and invokes the sort function.
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
	
        Toposort topo = new Toposort(args[0]);
        final long tstart = System.currentTimeMillis();
        final boolean hasCycles = topo.sort();
        final long tend = System.currentTimeMillis();
        final double elapsed = (tend - tstart) / 1000.0;
        System.err.println("Elapsed time " + elapsed + " sec");
        
        if (hasCycles) {
            System.out.println("Cycles detected");
        } else {
            topo.dump();
        }
    }    
}    


