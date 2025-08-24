/**
 * Given a list of 2n points, n black and n white,
 * computes the minimal number of wire needed to connect
 * each point with a correspoding unique point of different color. 
 *
 * To compile: javac connectPoints.java
 *
 * To execute: java connectPoints <fileIn>
 * 
 * The input file is a text file having in the first line
 * the number n, followed by 2n lines containing the string
 * "black" or the string "white".
 *
 * (C) 2017 Gianluigi Zavattaro (https://www.unibo.it/sitoweb/gianluigi.zavattaro)
 * Distributed under the CC-zero 1.0 license
 * https://creativecommons.org/publicdomain/zero/1.0/
 * 
 */

import java.io.*;
import java.util.Scanner;

/**
 * Node class for the implementation of a queue of integers.
 * It contains an int and a pointer to the next node in the queue.
 */
class Node {
    private int j;
    private Node next;
    public Node(int i) {j=i; next=null;}
    public int getValue() {return j;}
    public Node getNext() {return next;}    
    public void setNext(Node n) {next=n;}    
}

/**
 * Implementation of a queue of integers
 * It includes a head and a tail pointer
 * + the usual empty, dequeue and enqueue
 * operations.
 */
class Queue {

    Node h,t;

    public Queue() {
       	h=null;
	t=null;
    }

    public boolean empty(){
	return (h==null);
    }	

    public void enqueue(int i){
	Node n=new Node(i);
	if (t==null) {h=n; t=n;}
	else {t.setNext(n); t=n;}
    }

    public int dequeue(){
	int i=h.getValue();
	h=h.getNext();
	if (h==null) t=null;
	return i;	
    }

}

public class connectPoints {

    // The input file is formatted as follows: the first line
    // contains an integer n, while the subsequent 2*n lines
    // contain either the string "black" or the string "white".
    // The method returns a vector of boolen where true stands for
    // white while false represents black.
    public static Boolean[] readInput( String inputf ) 
    {
	int n;
	Boolean v[]=null;       
        try {
            Scanner s = new Scanner ( new FileReader( inputf ) ) ;
            n = s.nextInt();s.nextLine();
	    v = new Boolean[2*n];         
            for ( int i=0; i<2*n; i++ ) {
                if ( (s.nextLine()).equals("black") )
		    v[i]=false;
		else v[i]=true;
            }
            s.close();
        } catch ( IOException ex ) {
            System.err.println(ex);
            System.exit(1);
        }
 	return v;
    }

    // Computes the minimal amount of wire to connect black and white points.
    // It uses a greedy approach and exploits a queue
    // to store points that have been already visited but not yet
    // connected.
    public static int findMin( Boolean v[] ) {

        int wire=0;
	Queue black=new Queue(), white=new Queue();

	for (int i=0; i<v.length; i++) {
	    if (v[i]) 
		if (black.empty()) white.enqueue(i);
		else wire = wire + (i - black.dequeue());
	    else 
		if (white.empty()) black.enqueue(i);
		else wire = wire + (i - white.dequeue());
        }

	return wire;

    }

    public static void main( String args[] ) {

	if ( args.length != 1 ) {
	    System.err.println("Usage: java connectPoints <input file>");
	    System.exit(1);
	}

        System.out.println( findMin( readInput(args[0]) ) );	

    }

}