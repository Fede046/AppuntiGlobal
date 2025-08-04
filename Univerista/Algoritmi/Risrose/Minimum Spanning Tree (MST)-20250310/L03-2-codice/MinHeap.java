/****************************************************************************
 *
 * MinHeap.java 
 *
 * Written in 2020 by Moreno Marzolla <moreno.marzolla(at)unibo.it>
 *
 * To the extent possible under law, the author(s) have dedicated all 
 * copyright and related and neighboring rights to this software to the 
 * public domain worldwide. This software is distributed without any warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software. If not, see 
 * <http://creativecommons.org/publicdomain/zero/1.0/>. 
 *
 ****************************************************************************/
import java.util.*;

/**
 * This class implements a min-heap of a given maximum initial size n.
 * The heap contains pairs (data, priority) where data is an integer
 * in the range 0..n-1, and priority is any real value.
 */
class MinHeap {
    
    heapElem heap[];    
    /* pos[id] is the position of "id" inside the heap. Specifically,
       heap[pos[id]].key == id. This array is required to make
       decreaseKey() run in log(n) time. */
    int pos[]; 
    int size, maxSize;

    /**
     * An heap element is a pair (id, priority), where
     * id is an integer in 0..(maxSize-1)
     */
    private class heapElem {	
        public final int data;
        public double prio;
        
        public heapElem(int data, double prio)
        {
            this.data = data;
            this.prio = prio;
        }
    }

    /**
     * Build an empty heap with at most maxSize elements
     */
    public MinHeap(int maxSize)
    {
	this.heap = new heapElem[maxSize];
	this.maxSize = maxSize; 
        this.size = 0;
        this.pos = new int[maxSize];
        Arrays.fill(this.pos, -1);
    }

    
    /**
     * Return true iff index i is a valid index in the heap,
     * i.e., i>=0 and i<size
     */ 
    private boolean valid(int i)
    {
        return ((i >= 0) && (i < size));
    }

    /**
     * swap heap[i] with heap[j]
     */
    private void swap(int i, int j)
    {
        assert (pos[heap[i].data] == i);
        assert (pos[heap[j].data] == j);
        
        heapElem elemTmp = heap[i];
        heap[i] = heap[j];
        heap[j] = elemTmp;
        pos[heap[i].data] = i;
        pos[heap[j].data] = j;
    }

    /**
     * Return the index of the parent of heap[i]
     */
    private int parent(int i)
    {
        assert (valid(i));
        
        return (i+1)/2 - 1;
    }

    /**
     * Return the index of the left child of heap[i]
     */
    private int lchild(int i)
    {
        assert (valid(i));
        
        return (i+1)*2 - 1;
    }

    /**
     * Return the index of the right child of heap[i]
     */
    private int rchild(int i)
    {
        assert (valid(i));
        
        return lchild(i) + 1;
    }
    
    /**
     * Return true iff the heap is empty
     */
    public boolean isEmpty( )
    {
	return (size==0);
    }

    /**
     * Return true iff the heap is full, i.e., no more available slots
     * are available.
     */
    public boolean isFull( )
    {
        return (size > maxSize);
    }

    /**
     * Return the data of the element with lowest priority
     */
    public int min( )
    {
        assert ( !isEmpty() );
	return heap[0].data;
    } 

    /**
     * Return the position of the child of i (if any) with minimum
     * priority. If i has no childs, return -1.
     */
    private int minChild(int i)
    {
        assert (valid(i));
        
        final int l = lchild(i);
        final int r = rchild(i);
        int result = -1;
        if (valid(l)) {
            result = l;
            if (valid(r) && (heap[r].prio < heap[l].prio)) {
                result = r;
            }
        }
        return result;
    }
    
    /**
     * Exchange heap[i] with the parent element until it reaches the
     * correct position into the heap. This method requires time O(log n).
     */
    private void moveUp(int i)
    {
        assert (valid(i));
        
        int p = parent(i);
	while ( (p >= 0) && (heap[i].prio < heap[p].prio) ) {
            swap(i, p);
            i = p;
            p = parent(i);
        }        
    }

    /**
     * Exchange heap[i] with the child with lowest priority, if any
     * exists, until it reaches the correct position into the heap.
     * This method requires time O(log n).
     */
    private void moveDown(int i)
    {
        assert (valid(i));

        boolean done = false;
	do {
            int dst = minChild(i);
            if (valid(dst) && (heap[dst].prio < heap[i].prio)) {
                swap(i, dst);
                i = dst;
	    } else {
                done = true;
            }
 	} while (!done);
    }

    /**
     * Insert a new pair (data, prio) into the queue.
     * This method requires time O(log n).
     */
    public void insert(int data, double prio)
    {
        assert ((data >= 0) && (data < maxSize));
        assert (pos[data] == -1);
        assert ( !isFull() );        

        final int i = size++;
        pos[data] = i;
        heap[i] = new heapElem(data, prio);
        moveUp(i);
    } 

    /**
     * Delete the element with minimum priority. This method requires
     * time O(log n).
     */
    public void deleteMin( )
    {
        assert ( !isEmpty() );
        
        swap(0, size-1);
        pos[heap[size-1].data] = -1;
        size--;
        if (size>0) moveDown(0);
    } 

    /**
     * Chenage the priority associated to |data|. This method requires
     * time O(log n).
     */
    public void changePrio(int data, double newprio)
    {
	int j = pos[data];
        assert ( valid(j) );
        final double oldprio = heap[j].prio;
        heap[j].prio = newprio;
        if (newprio > oldprio) {
            moveDown(j);
        } else {
            moveUp(j);
        }
    } 
}
