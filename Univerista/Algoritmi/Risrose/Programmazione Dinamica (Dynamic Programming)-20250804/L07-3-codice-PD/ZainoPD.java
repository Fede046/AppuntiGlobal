/*
 * Zaino.java - risolve il problema dello zaino, nella formulazione
 * seguente: dato un insieme di n oggetto x_0, x_1, ... x_{n-1} tali
 * che l'oggetto x_i abbia peso p[i] e valore v[i], determinare un
 * sottoinsieme di oggetti di peso minore o uguale a P il cui valore
 * sia massimo possibile. Tutti i pesi devono essere interi
 * positivi. I valori sono valori reali positivi.
 *
 * Compilare con
 *
 * javac ZainoPD.java
 *
 * Eseguire con
 *
 * java -ea ZainoPD <file input>
 *
 * Il file di input deve essere in formato testo, con la struttura
 * seguente:
 * - la prima riga contiene un intero positivo P (capacita' dello zaino);
 * - la seconda riga contiene un intero positivo n (numero di oggetti)
 * - seguono n righe, ciascuna delle quali contenente i due valori p[i] e v[i], separati da uno spazio
 *
 * (C) 2016, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * Ultimo aggiornamento 30 marzo 2021
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */
import java.io.*;
import java.util.Scanner;
import java.util.Locale;

public class ZainoPD {

    int[] p;	// p[i] e' il peso dell'oggetto i-esimo
    double[] v; // v[i] e' il valore dell'oggetto i-esimo
    int n;	// numero di oggetti
    int P;	// capacita' massima dello zaino.

    double[][] V;	// V[i][j] e' il valore massimo che si puo' ottenere
			// inserendo un sottoinsieme degli oggetti x_0, x_1,
			// ... x_i in uno zaino di capacita' massima j.

    boolean[][] use;	// use[i][j] = true sse l'oggetto x_i fa parte della
			// soluzione ottima che ha valore V[i][j]

    /**
     * Istanzia un nuovo oggetto di questa classe, leggendo i parametri
     * di input dal file "nomefile".
     *
     * @param nomefile nome del file contenente i parametri di input
     */
    public ZainoPD( String nomefile )
    {
        Locale.setDefault(Locale.US);
        try {
            Scanner s = new Scanner( new FileReader( nomefile ) );
            P = s.nextInt();
            n = s.nextInt();
            p = new int[n];
            v = new double[n];
            for ( int i=0; i<n; i++ ) {
                p[i] = s.nextInt();
                v[i] = s.nextDouble();
            }
        } catch ( IOException ex ) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    /**
     * Stampa la soluzione ottima. Questo metodo dovrebbe essere
     * invocato solo dopo l'esecuzione del metodo solve().
     */
    protected void stampaSoluzione( )
    {
        System.out.format("Valore massimo: %.2f\n", V[n-1][P]);
        System.out.println("Oggetti:");
        int j = P;
        int i = n-1;
        while ( i>=0 ) {
            if (use[i][j]) {
                System.out.println(p[i]+" "+v[i]);
                j = j-p[i];
            }
            i = i-1;
        }
    }

    /**
     * Stampa la matrice V. Questo metodo dovrebbe essere invocato
     * solo dopo l'esecuzione del metodo solve().
     */
    protected void stampaV( )
    {
        System.out.println("\n\nMatrice V[][]");
	for ( int i=0; i<n; i++ ) {
	    for ( int j=0; j<=P; j++ ) {
		System.out.format("%5.1f ", V[i][j]);
	    }
	    System.out.println();
	}
    }

    /**
     * Stampa la matrice use[][]. Questo metodo dovrebbe essere
     * invocato solo dopo l'esecuzione del metodo solve().
     */
    protected void stampaUse( )
    {
        System.out.println("\n\nMatrice use[][]");
	for ( int i=0; i<n; i++ ) {
	    for ( int j=0; j<=P; j++ ) {
		System.out.format("%c ", use[i][j] ? 'T' : 'F');
	    }
	    System.out.println();
	}
    }

    /**
     * Risolve il problema dello zaino utilizzando la programmazione
     * dinamica. Ritorna il valore massimo degli oggetti contenuti
     * nello zaino; in piu', al termine di questa procedura le matrici
     * V[][] e use[][] contengono i valori calcolati durante
     * l'esecuzione dell'algoritmo.
     */
    public double risolvi( )
    {
        int i,j;
        V = new double[n][1+P];
        use = new boolean[n][1+P];

        // Inizializza prima riga di V e K. In questa implementazione
        // non inizializziamo esplicitamente la prima colonna di V e
        // K, in quanto il calcolo di V[i][0] ricade nel caso "j <
        // p[i]" e viene correttamente settato a V[i-1][0].
	for ( j=0; j<=P; j++ ) {
	    if ( j < p[0] ) {
                V[0][j] = 0.0;
                use[0][j] = false;
	    } else {
                V[0][j] = v[0];
                use[0][j] = true;
            }
        }

	// Calcola gli altri elementi delle matrici V[i][j] e use[i][j]
	// per i=1..n-1, j=1..P
        for ( i=1; i<n; i++ ) {
            for ( j=0; j<=P; j++ ) {
                if ( j < p[i] ) { // non c'e' spazio per l'oggetto x_i
                    V[i][j] = V[i-1][j];
		    use[i][j] = false;
                } else {
                    if ( V[i-1][j-p[i]]+v[i] > V[i-1][j] ) {
                        V[i][j] = V[i-1][j-p[i]]+v[i];
                        use[i][j] = true;
                    } else {
                        V[i][j] = V[i-1][j];
			use[i][j] = false;
                    }
		}
		// Si puo' anche semplificare il blocco sopra come
		// segue:
		/*
		if ( j >= p[i] && V[i-1][j-p[i]]+v[i] > V[i-1][j] ) {
		    V[i][j] = V[i-1][j-p[i]]+v[i];
		    use[i][j] = true;
		} else {
		    V[i][j] = V[i-1][j];
		    use[i][j] = false;
		}
		*/
            }
        }

        stampaSoluzione( );
        stampaV();
        stampaUse();
        return V[n-1][P];
    }

    public static void main( String[] args )
    {
        if ( args.length != 1 ) {
            System.err.println("Specificare il nome del file di input");
            System.exit(1);
        }
        ZainoPD z = new ZainoPD(args[0]);
        final long start_t = System.currentTimeMillis();
	z.risolvi();
        final long end_t = System.currentTimeMillis();
        final double elapsed = (end_t - start_t)/1000.0;
        System.out.format("Elapsed time %.4f seconds\n", elapsed);
    }

}
