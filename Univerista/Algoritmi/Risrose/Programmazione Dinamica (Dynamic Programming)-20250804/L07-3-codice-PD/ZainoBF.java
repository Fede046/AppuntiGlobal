/*
 * ZainoBF.java - risolve il problema dello zaino, nella formulazione
 * seguente: dato un insieme di n oggetto x_0, x_1, ... x_{n-1} tali
 * che l'oggetto x_i abbia peso p[i] e valore v[i], determinare un
 * sottoinsieme di oggetti di peso minore o uguale a P il cui valore
 * sia massimo possibile. Tutti i pesi devono essere interi
 * positivi. I valori sono valori reali positivi.
 *
 * Questo programma risolve il problema con un approccio Brute-Force,
 * cioè esaminando tutte le possibili combinazioni valide di oggetti;
 * in effetti, questo programma non richiede che i pesi siano interi
 * (funziona anche se i pesi sono reali arbitrari), ma ho lasciato i
 * pesi interi per poter usare gli stessi file di input di Zaino.java
 *
 * Compilare con
 *
 * javac ZainoBF.java
 *
 * Eseguire con
 *
 * java -ea ZainoBF <file input>
 *
 * Il file di input deve essere in formato testo, con la struttura
 * seguente:
 * - la prima riga contiene un intero positivo P (capacita' dello zaino);
 * - la seconda riga contiene un intero positivo n (numero di oggetti)
 * - seguono n righe, ciascuna delle quali contenente i due valori p[i] e v[i], separati da uno spazio
 *
 * (C) 2016, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * Ultimo aggiornamento 31 marzo 2021
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 */


import java.io.*;
import java.util.Scanner;
import java.util.Locale;

public class ZainoBF {

    int[] p;	// p[i] e' il peso dell'oggetto i-esimo
    double[] v; // v[i] e' il valore dell'oggetto i-esimo
    int n;	// numero di oggetti
    int P;	// capacita' massima dello zaino.

    /**
     * Istanzia un nuovo oggetto di questa classe, leggendo i parametri
     * di input dal file "nomefile".
     *
     * @param nomefile nome del file contenente i parametri di input
     */
    public ZainoBF( String nomefile )
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
     * Risolve il problema P(i,j) nella formulazione mostrata sui
     * lucidi: determina il sottoinsieme dei primi "i" oggetti che
     * abbia valore massimo e peso minore o uguale a j.
     */
    protected double V(int i, int j)
    {
        if (j == 0)
            return 0.0;
        else if (i == 0) {
            return (j >= p[i] ? v[i] : 0);
        } else {
            if (j >= p[i]) {
                final double s0 = V(i-1, j);
                final double s1 = V(i-1, j-p[i]) + v[i];
                return s0 > s1 ? s0 : s1;
            } else {
                return V(i-1, j);
            }
        }
    }

    public double risolvi( )
    {
        final double result = V(n-1, P);
        System.out.format("Valore massimo: %.2f\n", result);
        return result;
    }
    


    public static void main( String[] args )
    {
        if ( args.length != 1 ) {
            System.err.println("Specificare il nome del file di input");
            System.exit(1);
        }
        ZainoBF z = new ZainoBF(args[0]);
        final long start_t = System.currentTimeMillis();
	   z.risolvi();
        final long end_t = System.currentTimeMillis();
        final double elapsed = (end_t - start_t)/1000.0;
        System.out.format("Elapsed time %.4f seconds\n", elapsed);
    }

}
