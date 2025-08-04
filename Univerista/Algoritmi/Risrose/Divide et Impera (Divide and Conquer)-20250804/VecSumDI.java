/**
 * Given an array with n>0 elements, find the nonempty subarray whose
 * sum of values is maximal. This program implements the divide and
 * conquer algorithm.
 *
 * The value of n (array length) is passed on the command line.  No
 * error checking is done. The vector is initialized
 * deterministically.
 *
 * To compile: javac vecsumdi.java
 *
 * To execute: java vecsumdi 10000
 *
 * (C) 2016, 2021 Moreno Marzolla (https://www.moreno.marzolla.name/)
 * (C) 2022 Jocelyne Elias (https://www.unibo.it/sitoweb/jocelyne.elias/)
 *
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 * 
 */


public class VecSumDI {

    /**
     * Return the maximum of a, b, and c
     */
    static double max3( double a, double b, double c) 
    {
	double m = a;
	if (b>m) m=b;
   	if (c>m) m=c;
	return m;
    }

    /**
     * Return the maximum sum of the values among all nonempty
     * subvectors of v[i..j].
     */
    static double vecsum( double v[], int i, int j )
    {
	if (i>j) {
	    // empty vector
	    return Double.NEGATIVE_INFINITY;
	} else if (i == j) {
	    // vector with one element
	    return v[i];
	} else {
	    // general case: vector with more than one element
	    int m = (i+j)/2;
	    double sleft = vecsum(v,i,m-1);
	    double sright = vecsum(v,m+1,j);
	    double sa, sb, s;
	    int k;
	    sa = s = 0.0;
	    for (k=m-1; k>=i; k--) {
		s += v[k];
		if (s > sa) sa = s;
	    }	
	    sb = s = 0.0;
	    for (k=m+1; k<=j; k++) {
		s += v[k];
		if (s > sb) sb = s;
	    }	
	    return max3(sleft, sright, v[m] + sa + sb);
	}
    }

    public static void main( String args[] )
    {
	if ( args.length != 1 ) {
	    System.err.println("Usage: java vecsumdi <array length>");
	    System.exit(1);
	}
	int n = Integer.parseInt(args[0]);
	double v[] = new double[n];
	int i,j,k;
	double sum, maxsum, val = 100;
	// Fill vector V[]
	for (i=0; i<v.length; ++i) {
	    v[i] = val;
	    val += 17;
	    if ( val > 100 ) val -= 200;
	}
	// Compute the maximum sum among all subvectors
	long start_t = System.currentTimeMillis();
	maxsum = vecsum(v, 0, n-1);
	long end_t = System.currentTimeMillis();
	System.out.println("Max sum = " + maxsum);
	long elapsed = (end_t - start_t);
	long min = elapsed / (60*1000);
	double sec = (elapsed -min*60)/1000.0;
	System.out.println("Elapsed time: "+min+" min "+sec+" sec");
    }
}
