
/**
 * Implementazione dell'algoritmo BFS (Breadth First Search).
 *
 * Il file di input deve essere formattato come segue: una riga iniziale
 * contiene due interi, il numero n di nodi e il numero m di archi, rispettivamente.
 * Seguono m righe: ogni riga contiene tre numeri, due interi tra 0 e n-1 (i nodi connessi da un arco)
 * e un double (il peso corrispondente, che qui viene ignorato).
 *
 * Per compilare: javac BFS.java 
 *
 * Per eseguire: java -ea BFS <fileIn> (ea: abilita le asserzioni)
 *
 * (C) 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 *
 * Distribuito sotto la licenza CC-zero 1.0
 * https://creativecommons.org/publicdomain/zero/1.0/
 * 
 */

import java.io.*;
import java.util.*;

public class BFS {

    int n; // numero di nodi nel grafo di input
    int m; // numero di archi nel grafo di input
    Vector<LinkedList<Edge>> adjList; // il grafo di input è memorizzato usando liste di adiacenza
    // adjList è un array di liste concatenate e ogni elemento di questo array
    // contiene una lista concatenata Java.
    int parent[]; // parent[v] è il predecessore del nodo v nell'albero BFS
    int dist[]; // dist[v] è la distanza (numero di archi) del nodo v dalla sorgente

    /**
     * Classe che rappresenta un arco di un grafo pesato e diretto
     */
    private class Edge {
        final int src; // nodo di partenza dell'arco
        final int dst; // nodo di arrivo dell'arco
        final double w; // peso dell'arco

        public Edge(int src, int dst, double w) {
            this.src = src;
            this.dst = dst;
            this.w = w;
        }
    }

    /* Il costruttore legge il grafo dal file di input */
    public BFS(String inputf) {
        readGraph(inputf);
    }

    /**
     * Stampa il contenuto degli array parent e dist
     */
    public void dump() {
        System.out.printf("v\tparent[v]\tdist[v]\n");
        for (int v = 0; v < n; v++) {
            System.out.printf("%d\t%d\t%d\n", v, parent[v], dist[v]);
        }
    }

    /**
     * Stampa il percorso più breve da src a dst, se esiste
     */
    public void print_path(int src, int dst) {
        if (dist[dst] < 0) {
            System.out.printf("Nessun percorso da %d a %d\n", src, dst);
        } else if (src == dst) {
            System.out.print(src);
        } else {
            print_path(src, parent[dst]);
            System.out.print(" " + dst);
        }
    }

    /**
     * Legge il grafo dal file di input e lo memorizza in una lista di adiacenza
     */
    private void readGraph(String inputf) {
        Locale.setDefault(Locale.US);

        try {
            Scanner f = new Scanner(new FileReader(inputf));
            n = f.nextInt();
            m = f.nextInt();

            adjList = new Vector<LinkedList<Edge>>(n);

            for (int i = 0; i < n; i++) {
                adjList.add(i, new LinkedList<Edge>());
            }

            for (int i = 0; i < m; i++) {
                final int src = f.nextInt();
                final int dst = f.nextInt();
                final double weight = f.nextDouble();
                final Edge newEdge = new Edge(src, dst, weight);
                adjList.get(src).add(newEdge);
            }
        } catch (IOException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Esegue la visita BFS a partire dal nodo s
     */
    public void visit(int s) {
        parent = new int[n];
        dist = new int[n];

        Arrays.fill(dist, -1); // Inizializza tutte le distanze a -1 (non visitato)
        Arrays.fill(parent, -1); // Inizializza tutti i predecessori a -1

        dist[s] = 0; // La distanza dalla sorgente a se stessa è 0
        Queue<Integer> q = new LinkedList<Integer>(); // Coda FIFO per la BFS
        q.add(s); // Aggiunge la sorgente alla coda

        while (!q.isEmpty()) {
            final int u = q.poll(); // Estrae il primo nodo dalla coda
            Iterator<Edge> iter = adjList.get(u).iterator(); // Ottiene un iteratore per la lista di adiacenza del nodo
                                                             // u
            while (iter.hasNext()) {
                final Edge edge = iter.next();
                final int v = edge.dst;
                assert (edge.src == u); // Verifica che l'arco parta dal nodo u
                if (dist[v] < 0) { // Se il nodo v non è stato visitato
                    dist[v] = dist[u] + 1; // Aggiorna la distanza di v
                    parent[v] = u; // Imposta u come predecessore di v
                    q.add(v); // Aggiunge v alla coda
                }
            }
        }
    }

    /**
     * La procedura principale crea un oggetto BFS, invoca l'algoritmo BFS
     * e stampa l'albero BFS.
     */
    public static void main(String args[]) {
        if (args.length != 1) {
            // Se non viene fornito un file di input, genera un grafo completo casuale
            final int n = 100;
            System.out.printf("%d %d\n", n, n * (n - 1) / 2);
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    final double weight = Math.random() * 100;
                    System.out.printf("%d %d %f\n", i, j, weight);
                }
            }
            return;
        }

        BFS bfs = new BFS(args[0]); // Crea un oggetto BFS e legge il grafo dal file di input
        final long tstart = System.currentTimeMillis();
        bfs.visit(0); // Esegue la BFS a partire dal nodo 0
        final long tend = System.currentTimeMillis();
        final double elapsed = (tend - tstart) / 1000.0;
        System.err.println("Tempo trascorso " + elapsed + " sec");
        bfs.dump(); // Stampa i risultati della BFS
        System.out.printf("Percorso più breve da %d a %d\n", 0, bfs.n - 1);
        bfs.print_path(0, bfs.n - 1); // Stampa il percorso più breve dal nodo 0 al nodo n-1
        System.out.println();
    }
}