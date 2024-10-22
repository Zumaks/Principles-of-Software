	/**
	 * This is part of HW0: Environment Setup and Java Introduction.
	 */
	package hw0;
	import java.util.ArrayList;
	import java.util.List;
	/**
	 * Fibonacci calculates the <var>n</var>th term in the Fibonacci sequence.
	 *
	 * The first two terms of the Fibonacci sequence are 0 and 1,
	 * and each subsequent term is the sum of the previous two terms.
	 *
	 */
	public class Fibonacci {
		
		
		private List<Long> mylist;
		
		public Fibonacci() {
	        this.mylist = new ArrayList<>();
	        mylist.add(0L); 
	        mylist.add(1L); 
	    }
	
	    /**
	     * Calculates the desired term in the Fibonacci sequence.
	     *
	     * @param n the index of the desired term; the first index of the sequence is 0
	     * @return the <var>n</var>th term in the Fibonacci sequence
	     * @throws IllegalArgumentException if <code>n</code> is not a nonnegative number
	     */
		public long getFibTerm(int n) {
	        if (n < 0) {
	            throw new IllegalArgumentException(n + " is negative");
	        } 
	        else if (n < 2) {
	        	return n;
	    	} 
	        else if (n < mylist.size()) {
	            return mylist.get(n);
	        } 
	        else {
	            long res = getFibTerm(n - 1) + getFibTerm(n - 2);
	            mylist.add(res);
	            return res;
	        }
	}
	}
