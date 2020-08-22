package collatz.justTryPseudoMersenneNumbers;

/*
 * up to 
 * Trying 2^(25928) - 1:
It took 349051 to get down to one
DEBUG: done getMersenee

Tried 1000000
// (4 and a half hours!)
//Trying 2^(1000000) - 1:
//It took 13420758 to get down to one
 */

import java.math.BigInteger;

public class JustTry {

	//FOUND OEIS: A193688
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int START = 10000;
		int NUM_TRIES = 90000;
		for(int i=START; i<START + NUM_TRIES; i++) {
			int numIter = colatz(getMersenne(i));
			
			System.out.println("Trying 2^(" + i + ") - 1:");
			System.out.println("It took " + numIter + " to get down to one");
		}
	}
	
	
	public static BigInteger getMersenne(int n) {
		if(n == 0) {
			System.out.println("ERROR: dont do 2^0 - 1");
			System.exit(1);
		}
		BigInteger ret = BigInteger.ONE;
		
		for(int i=0; i<n; i++) {
			ret = ret.multiply(TWO);
		}
		
		System.out.println("DEBUG: done getMersenee");
		return ret.subtract(BigInteger.ONE);
	}
	
	public static BigInteger TWO = new BigInteger("2");
	public static BigInteger THREE = new BigInteger("3");
	
	public static int colatz(BigInteger a) {
		
		int numIterations = 0;
		
		while(a.equals(BigInteger.ONE) == false) {
			numIterations++;
			//if(numIterations % 1000 == 2) {
			//	System.out.println(numIterations);
			//}
			if(a.divideAndRemainder(TWO)[1].equals(BigInteger.ONE)) {
				a = a.multiply(THREE).add(BigInteger.ONE);
			} else {
				a = a.divide(TWO);
			}
		}
		
		return numIterations;
	}

}
