/**
 * Calcola la distanza di Levenshtein (Edit/Levenshtein Distance) tra due stringhe passate
 * sulla riga di comando.
 *
 * Per compilare: javac Levenshtein.java
 *
 * Per eseguire: java -ea Levenshtein stringaS stringaT (esempio: java -ea Levenshtein libro albero
 * stampa la sequenza di comandi di editing necessari per trasformare "libro" in "albero").
 *
 * (C) 2016, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * 2022 Jocelyne Elias (https://www.unibo.it/sitoweb/jocelyne.elias/)
 * Ultimo aggiornamento 4 maggio 2022 
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */


import java.util.ArrayList;

class Levenshtein {
    /* Comandi che indico nella tabella cmd[] */
    final static char CMD_DELETE = '|';
    final static char CMD_INSERT = '-';
    final static char CMD_CHANGE = '\\';

    int[][] L;          /* tabella delle distanze di Lavenshtein */
    char[][] cmd;       /* tabella dei comandi */
    final String S, T;
    final int n, m;     /* Lunghezze di S e T, rispettivamente */

    /* Una istanza di questa classe determina il minimo numero di
       operazioni di editing necessarie per trasformare la stringa S
       nella stringa T */
    Levenshtein(String S, String T)
    {
        this.S = S;
        this.T = T;
        this.n = S.length();
        this.m = T.length();
        this.L = new int[n+1][m+1];
        this.cmd = new char[n+1][m+1];
    }

    /**
     * Restituisce la distanza di Levenshtein tra le due stringhe |S|
     * e |T|.  La distanza e' un intero compreso tra 0 (la stessa stringa) e la massima
     * lunghezza delle due stringhe (una delle 2 stringhe è vuota). Si presti attenzione al fatto che
     * le posizioni dei caratteri delle stringhe in Java iniziano da 0
     * e non da 1 come nello pseudocodice.
     */
    int distance( )
    {
        int i, j;
        for ( i=0; i<n+1; i++ ) {
            for ( j=0; j<m+1; j++ ) {
                if ( i==0 && j==0 ) {
                    L[i][j] = 0;
                    cmd[i][j] = '.'; /* valore di default, che viene mantenuto solo nella cella (0,0) */
                } else if (i == 0) {
                    /* Questo è il caso i==0, j>i */
                    L[i][j] = j;
                    cmd[i][j] = CMD_INSERT;
                } else if (j == 0) {
                    /* Questo è il caso i>j, j==0 */
                    L[i][j] = i;
                    cmd[i][j] = CMD_DELETE;
                } else {
                    /* Questo è il caso generale */
                    int dist = L[i-1][j] + 1;
                    char c = CMD_DELETE;
                    if (L[i][j-1] + 1 < dist) {
                        dist = L[i][j-1] + 1;
                        c = CMD_INSERT;
                    }
                    if (L[i-1][j-1] + (S.charAt(i-1) != T.charAt(j-1) ? 1 : 0) < dist) {
                        dist = L[i-1][j-1] + (S.charAt(i-1) != T.charAt(j-1) ? 1 : 0);
                        c = CMD_CHANGE;
                    }
                    L[i][j] = dist;
                    cmd[i][j] = c;
                }
            }
        }
        return L[n][m];
    }

    /**
     * Stampa la matrice L[][] delle distanze
     */
    public void printL( )
    {
        int i, j;

        /* intestazione colonne */
        System.out.print("    ");
        for (j=1; j<m+1; j++) {
            System.out.print(T.charAt(j-1) + " ");
        }
        System.out.println();
        for (i=0; i<n+1; i++) {
            /* intestazione right */
            if (i>0) {
                System.out.print(S.charAt(i-1) + " ");
            } else {
                System.out.print("  ");
            }

            for (j=0; j<m+1; j++) {
                System.out.print(L[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Stampa la matrice cmd[][] dei comandi di editing
     */
    public void printCmd( )
    {
        int i, j;

        /* intestazione colonne */
        System.out.print("  ");
        for (j=1; j<m+1; j++) {
            System.out.print(T.charAt(j-1));
        }
        System.out.println();
        for (i=0; i<n+1; i++) {
            /* intestazione righe */
            if (i>0) {
                System.out.print(S.charAt(i-1));
            } else {
                System.out.print(" ");
            }

            for (j=0; j<m+1; j++) {
                System.out.print(cmd[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /* Stampa le operazioni di editing necessarie per trasformare i
       primi i caratteri di S nei primi j caratteri di T */
    protected void printEdits_rec(StringEdit str, int i, int j)
    {
        assert(i>=0);
        assert(j>=0);

        if (i==0 && j==0)
            return;

        switch(cmd[i][j]) {
        case CMD_INSERT:
            printEdits_rec(str, i, j-1);
            str.insert(T.charAt(j-1));
            break;
        case CMD_DELETE:
            printEdits_rec(str, i-1, j);
            str.delete();
            break;
        default:
            printEdits_rec(str, i-1, j-1);
            str.change(T.charAt(j-1));
        }
    }

    /* Stampa la sequenza di operazioni di editing necessarie per
       trasformare S in T */
    void printEdits( )
    {
        StringEdit str = new StringEdit(S);
        printEdits_rec(str, n, m);
    }

    public static void main( String[] args )
    {
        if (args.length != 2) {
            System.err.println("Uso: java Levenshtein S T");
            System.exit(1);
        }
        Levenshtein prob = new Levenshtein(args[0], args[1]);
        prob.distance();
        prob.printL();
        prob.printCmd();
        prob.printEdits( );
    }
}

/**
 * Questa classe consente di visualizzare le operazioni di editing da
 * applicare ad una stringa s. La stringa è rappresentata mediante un
 * ArrayList, in modo da supportare inserimento e cancellazione in
 * posizioni arbitrarie. Inizialmente il "cursore", rappresentato
 * dall'indice idx, è posizionato sul primo carattere.
 */
class StringEdit
{
    ArrayList<Character> s;
    int idx;

    StringEdit(String str)
    {
        this.s = new ArrayList<Character>();
        for (char c : str.toCharArray()) {
            this.s.add(c);
        }
        this.idx = 0;
    }

    /**
     * Inserisce il carattere c nella posizione corrente del cursore.
     * Il carattere viene inserito tra la posizione idx e (idx+1)
     * (quindi i rimanenti caratteri vengono traslati a destra). Dopo
     * l'inserimento, il cursore si sposta di una posizione a destra.
     */
    public void insert(char c)
    {
        print( );
        System.out.format("  INSERT '%c'  ", c);
        s.add(idx, c);
        idx++;
        print( );
        System.out.println( );
    }

    /**
     * Cancella il carattere nella posizione corrente del cursore.
     * Tutti i caratteri a destra del cursore vengono traslati di una
     * posizione verso sinistra. La posizione del cursore non viene
     * modificata.
     */
    public void delete( )
    {
        print( );
        System.out.format("  DELETE '%c'  ", s.get(idx));
        s.remove(idx);
        print( );
        System.out.println( );
    }

    /**
     * Sostituisce il carattere nella posizione corrente del cursore
     * con il carattere c. Al termine dell'operazione, il cursore si
     * sposta di una posizione verso destra.
     */
    public void change(char c)
    {
        print( );
        if (s.get(idx) != c)
            System.out.format("  REPLACE '%c'->'%c'  ", s.get(idx), c);
        else            
            System.out.format("  KEEP '%c'  ", c);
        s.set(idx, c);
        idx++;
        print( );
        System.out.println( );
    }

    /**
     * Stampa la stringa soggetta a editing; la posizione del cursore
     * è indicata tra parentesi quadre []
     */
    protected void print( )
    {
        int i;
        for (i=0; i<idx; i++) {
            System.out.print(s.get(i));
        }
        if (i < s.size()) {
            System.out.print("[" + s.get(i) + "]");
            for (i++; i<s.size(); i++) {
                System.out.print(s.get(i));
            }
        } else
            System.out.print("[]");
    }
}
