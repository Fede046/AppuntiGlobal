/*
 * Resto.java - Risolve il problema del resto utilizzando la programmazione dinamica.
 * Date n monete di valori interi c[0], ... c[n-1], determinare il MINIMO numero di monete
 * necessarie per erogare un resto pari a R, se cio' e' possibile. I valori delle
 * monete possono essere arbitrari, nel senso che il programma
 * funziona anche con tagli di sistemi monetari NON canonici.
 * A reference for Canonical and non-canonical monetary systems: « Canonical Coin Systems for Change-Making Problems »
 *
 * Compilare con
 *
 * javac Resto.java
 *
 * Eseguire con
 *
 * java Resto <file input>
 *
 * Il file di input deve essere in formato testo, con la struttura
 * seguente:
 * - la prima riga contiene un intero positivo R (resto da erogare)
 * - la seconda riga contiene un intero positivo n (numero di monete)
 * - seguono n righe, ciascuna delle quali contenente il valore intero
 *   c[i] (valore della i-esima moneta a disposizione)
 *
 * Versione 0.2 del 30 aprile 2015
 * Autore: Moreno Marzolla <moreno.marzolla (at) unibo.it>
 * Licenza: CC0 1.0 universal
 *
 */


import java.io.*;
import java.util.Scanner;
import java.util.Locale;



public class Resto {

    int[] c;	// c[i] e' il valore della i-esima moneta
    int n;	// numero di monete a disposizione
    int R;	// resto da erogare

    /* N[i][j] e' il minimo numero di monete, scelte tra
       quelle di valore c[0], ... c[i], che e' necessario
       utilizzare per erogare un importo pari a j. Se
       l'importo j non e' erogabile, N[i][j] =
       Integer.MAX_VALUE */
    int[][] N;

    /* use[i][j] = true se e solo se la moneta i-esima, di valore
       c[i], viene usata per erogare il resto j; se il resto j non e'
       erogabile, use[i][j] = false. */
    boolean[][] use;

    /**
     * Crea una istanza della classe Resto. c[] e' l'array dei valori
     * delle n monete a disposizione, R e' il valore del resto da
     * erogare
     *
     * @param c[i] e' il valore della i-esima moneta a disposizione
     * @param R resto da erogare
     */
    public Resto( String inputf )
    {
        Locale.setDefault(Locale.US);
        try {
            Scanner s = new Scanner( new FileReader( inputf ) );
            R = s.nextInt();
            n = s.nextInt();
            c = new int[n];
            for ( int i=0; i<n; i++ ) {
                c[i] = s.nextInt();
            }
        } catch ( IOException ex ) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Stampa la soluzione del problema del resto. Ogni riga contiene
     * il valore di una delle monete da erogare; se il resto non e'
     * erogabile, stampa un messaggio opportuno
     */
    protected void stampaSoluzione( )
    {
        int i = n-1;
        int j = R;
        if ( N[i][j] == Integer.MAX_VALUE ) {
            System.out.println("Nessuna soluzione");
        } else {
            System.out.println("Soluzione:");
            while ( i >= 0 ) {
                if ( use[i][j] ) {
                    System.out.println(c[i]);
                    j -= c[i];
                }
                i--;
            }
        }
    }

    /**
     * Determina la soluzione del problema del resto usando la
     * programmazione dinamica. Questo metodo viene invocato dal
     * costruttore.  Si presti attenzione al fatto che questo metodo
     * utilizza il valore Integer.MAX_VALUE per rappresentare
     * "+infinito". Tecnicamente, Integer.MAX_VALUE e' il massimo
     * intero rappresentabile, e quindi _non_ e' il valore +infinito
     * (ne' ha proprieta' simili a +infinito, come avrebbe invece
     * Double.POSITIVE_INFINITY).
     *
     * @return minimo numero di monete necessarie per erogare il resto
     * R; se il resto non e' erogabile, ritorna Integer.MAX_VALUE
     */
    private int risolvi( )
    {
        int i,j;

        N = new int[n][R+1];
        use = new boolean[n][R+1];

        // Casi base
        N[0][0] = 0;
        use[0][0] = false;
        for ( j=1; j<=R; j++ ) {
            if ( j == c[0] ) {
                N[0][j] = 1;
                use[0][j] = true;
            } else {
                N[0][j] = Integer.MAX_VALUE;
                use[0][j] = false;
            }
        }


        // Riempimento delle tabelle di programmazione dinamica
        for ( i=1; i<n; i++ ) {
            for ( j=0; j<=R; j++ ) {
                /* Il singolo "if" che segue e' sufficiente per tener
                 * conto di tutti i casi che si possono presentare:
                 *
                 * - resto j non erogabile ne' usando ne' omettendo la
                 *   moneta i-esima;
                 * - resto j erogabile solo usando la moneta i-esima;
                 * - resto j erogabile solo omettendo la moneta
                 *   i-esima;
                 * - resto j erogabile sia usando che omettendo la
                 *   moneta i-esima (in questo caso si sceglie
                 *   l'alternativa che minimizza il numero di monete
                 *   usate);
                 */
                if ( j >= c[i] &&
                     N[i-1][j-c[i]] != Integer.MAX_VALUE &&
                     N[i-1][j] > N[i-1][j-c[i]] + 1 ) {
                    N[i][j] = N[i-1][j-c[i]] + 1;
                    use[i][j] = true;
                } else {
                    N[i][j] = N[i-1][j];
                    use[i][j] = false;
                }
            }
        }
        stampaSoluzione();
        return N[n-1][R];
    }

    public static void main( String[] args )
    {
        if ( args.length != 1 ) {
            System.err.println("Indicare sulla riga di comando il nome di un file di input");
            System.exit(1);
        } else {
            Resto problema = new Resto(args[0]);
            problema.risolvi();
        }
    }
}
