package ramarujan;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Hashtable;

public class TaxicabOn4 {

	// number that's the sum of 2 4th powers in 2 different ways.
	
	//number that's the sum of 2 5th powers in 2 different ways
	// 5th power not found yet but not proven to not exist.
	// TODO: find the one for the 5th power.
	
	public static  Hashtable<BigInteger, Long> hashtable = new Hashtable<BigInteger, Long>();
	
	public static void main(String[] args) {
		long POWER = 3;
		
		int numSolutionsFound = 0;
		
		long kLimit;
		
		long JINITIAL = POWER*2+1;
		
		BigInteger lhs;

		BigInteger rhs;
		
		long f = 1;
		
		for(long i=3; true; i++) {
			if(i % 100 == 0 ) {
				System.out.println("i = " + i);
			}
			for(long j=1; j<=i; j++) {
				lhs = getSumWithPowers(POWER, i, j);
				
				if(i==10 && j ==9) {
					System.out.println("Debug 2");
				}
				if(lhs.compareTo(new BigInteger("" + 1729)) == 0) {
					System.out.println("debug");
				}
				
				//kLimit = j - POWER*2;
				f = 1;
				
				for(long k=1; j-k-2*POWER*f>0; k++) {
					rhs = getSumWithPowers(POWER, i + k, j - k - 2*POWER*f);
					
					while(rhs.compareTo(lhs) > 0 && j - k - 2*POWER*(f+1) >0) {
						f++;
						rhs = getSumWithPowers(POWER, i + k, j - k - 2*POWER*f);
					}
					
					if(rhs.compareTo(lhs) == 0) {
						numSolutionsFound++;
						System.out.println("FOUND: " + rhs + " solution #" + numSolutionsFound);
						System.out.println(rhs + " = " + i + "^" + POWER + " + " + j + "^" + POWER);
						System.out.println(rhs + " = " + (i + k) + "^" + POWER + " + " + (j - k - 2*POWER*f) + "^" + POWER);
						
						if(numSolutionsFound == 10) {
							System.exit(1);
						}
						
						if(POWER >4) {
							System.exit(1);
						}
						//System.exit(1);
					}
					
				}
			}
		}
	}
	
	public static BigInteger getSumWithPowers(long power, long a, long b) {
		BigInteger aBig = new BigInteger("" + a);
		BigInteger bBig = new BigInteger("" + b);
		BigInteger temp1 = BigInteger.ONE;
		BigInteger temp2 = BigInteger.ONE;
		for(int i = 0; i<power; i++) {
			temp1 = temp1.multiply(aBig);
		}
		
		for(int i = 0; i<power; i++) {
			temp2 = temp2.multiply(bBig);
		}
		
		return temp1.add(temp2);
	}
	
	public void getLower() {

		Hashtable<Long, Long> hashtable2 = new Hashtable<Long, Long>();
		long temp;
		
		double POWER = 4;
		
		for(long i=1; true; i++) {
			if(i % 100 == 0 ) {
				System.out.println("i = " + i);
			}
			for(long j=1; j<=i; j++) {
				temp = (long)Math.round(Math.pow(i, POWER) + Math.pow(j, POWER));
				
				if(hashtable2.get(temp) != null) {
					System.out.println("Found: " + temp);
					System.out.println(temp + " = " + i + "^" + POWER + " + " + j + "^" + POWER);
					long fourthRoot = (long)Math.round(Math.pow(temp - Math.pow((double)hashtable2.get(temp), POWER), 1.0/POWER));
					System.out.println(temp + " = " + hashtable2.get(temp) + "^" + POWER + " + " + fourthRoot + "^" + POWER );
					System.exit(1);
				} else {
					hashtable2.put(temp, i);
				}
			}
		}
	}
	
	
	public static void get5WithHashtable() {

		BigInteger tempI;
		BigInteger tempBigI;
		BigInteger tempJ;
		BigInteger tempBigJ;
		
		int POWER = 5;
		
		for(long i=1; true; i++) {
			
			Enumeration<BigInteger> e = hashtable.keys();
			
			tempBigI = new BigInteger("1");
			tempI = new BigInteger("" + i);
			
			for(int k=0; k<POWER; k++) {
				tempBigI = tempBigI.multiply(tempI);
			}
			
			BigInteger tempElement;
			
			while(e.hasMoreElements()) {
				tempElement = e.nextElement();
				if(tempElement.compareTo(tempBigI) <= 0) {
					hashtable.remove(tempElement);
				}
			}
			if(i % 100 == 0 ) {
				System.out.println("i = " + i + " hashtable length: " + hashtable.size());
				
			}
			
			
			for(long j=1; j<=i; j++) {
				tempJ = new BigInteger("" + j);
				
				tempBigI = new BigInteger("1");
				
				for(int k=0; k<POWER; k++) {
					tempBigI = tempBigI.multiply(tempI);
				}
				
				tempBigJ= new BigInteger("1");
				for(int k=0; k<POWER; k++) {
					tempBigJ = tempBigJ.multiply(tempJ);
				}
				
				tempBigI = tempBigI.add(tempBigJ);
				
				if(hashtable.get(tempBigI) != null) {
					System.out.println("Found: " + tempBigI);
					System.out.println(tempBigI + " = " + i + "^" + POWER + " + " + j + "^" + POWER);
					
					
					long fourthRoot = getRoot(tempBigI, POWER, hashtable.get(tempBigI));
					System.out.println(tempBigI + " = " + hashtable.get(tempBigI) + "^" + POWER + " + " + fourthRoot + "^" + POWER );
					
					System.exit(1);
				} else {
					hashtable.put(tempBigI, i);
				}
			}
		}
	}
	
	
	public static void get5WithHashtable2() {

		BigInteger tempI;
		BigInteger tempBigI;
		BigInteger tempJ;
		BigInteger tempBigJ;
		
		int POWER = 5;
		
		
		for(int j=0; j<1000; j++) {
			System.out.println(j + ": " + (((j*j*j)%1000) * (j*j)%1000)%1000);
		}
		
		
		for(long i=1; true; i++) {
			
			Enumeration<BigInteger> e = hashtable.keys();
			
			tempBigI = new BigInteger("1");
			tempI = new BigInteger("" + i);
			
			for(int k=0; k<POWER; k++) {
				tempBigI = tempBigI.multiply(tempI);
			}
			
			BigInteger tempElement;
			
			while(e.hasMoreElements()) {
				tempElement = e.nextElement();
				if(tempElement.compareTo(tempBigI) <= 0) {
					hashtable.remove(tempElement);
				}
			}
			if(i % 100 == 0 ) {
				System.out.println("i = " + i + " hashtable length: " + hashtable.size());
				
			}
			
			
			for(long j=1; j<=i; j++) {
				tempJ = new BigInteger("" + j);
				
				tempBigI = new BigInteger("1");
				
				for(int k=0; k<POWER; k++) {
					tempBigI = tempBigI.multiply(tempI);
				}
				
				tempBigJ= new BigInteger("1");
				for(int k=0; k<POWER; k++) {
					tempBigJ = tempBigJ.multiply(tempJ);
				}
				
				tempBigI = tempBigI.add(tempBigJ);
				
				if(hashtable.get(tempBigI) != null) {
					System.out.println("Found: " + tempBigI);
					System.out.println(tempBigI + " = " + i + "^" + POWER + " + " + j + "^" + POWER);
					
					
					long fourthRoot = getRoot(tempBigI, POWER, hashtable.get(tempBigI));
					System.out.println(tempBigI + " = " + hashtable.get(tempBigI) + "^" + POWER + " + " + fourthRoot + "^" + POWER );
					
					System.exit(1);
				} else {
					hashtable.put(tempBigI, i);
				}
			}
		}
	}
	
	
	public static long getRoot(BigInteger sum, int power, long firstNumber) {
		BigInteger firstNumberPower = new BigInteger("1");
		for(int k=0; k<power; k++) {
			firstNumberPower = firstNumberPower.multiply(new BigInteger("" + firstNumber));
		}
		
		BigInteger secondNumberPower = sum.subtract(firstNumberPower);
		
		BigInteger temp;
		for(int i=1; i<=firstNumber; i++) {
			temp = new BigInteger("1");
			for(int k=0; k<power; k++) {
				temp = temp.multiply(new BigInteger("" + i));
			}
			if(temp.compareTo(secondNumberPower) == 0) {
				return i;
			}
		}
		return -1;
	}

}



//double POWER = 3.0;

//Found: 1729
//1729 = 12^3.0 + 1^3.0
//1729 = 10^3.0 + 0^3.0

//double POWER = 4.0
//Found: 635318657
//635318657 = 158^4.0 + 59^4.0
//635318657 = 134^4.0 + 133^4.0

//FOUND: 138519003152
//138519003152 = 514^4 + 512^4
//138519003152 = 584^4 + 386^4

//power of 5: 
//i = 20000 hashtable length: 9962622