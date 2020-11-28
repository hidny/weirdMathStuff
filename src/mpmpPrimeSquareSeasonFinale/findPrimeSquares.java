package mpmpPrimeSquareSeasonFinale;

import java.math.BigInteger;

public class findPrimeSquares {

	public static int BLOCK_SIZE = 1000000000;
	//public static int BLOCK_SIZE = 1000;
	public static boolean isPrime[];

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		isPrime= UtilityFunctions.UtilityFunctions.getBoolListOfPrimesUpToN(BLOCK_SIZE);
		 
		 
		//bruteForceSolveWithBigInteger();
		
		solveCleverWithBigInteger();
	}
	

	//Found the sequence:
	// https://oeis.org/A111441
	// I don't think I'll find a(12) :(
	public static void bruteForceSolveWithBigInteger() {
		
		int numPrimesFound = 0;
		
		BigInteger curSumSquarePrimes = BigInteger.ZERO;
		
		
		for(int i=0; i<BLOCK_SIZE; i++) {
			
			if(isPrime[i]) {
				numPrimesFound++;
				
				curSumSquarePrimes = curSumSquarePrimes.add(new BigInteger(i +"").multiply(new BigInteger(i +"")));
				
				if(/*isPrime[numPrimesFound] && */curSumSquarePrimes.divideAndRemainder(new BigInteger("" + numPrimesFound))[1].compareTo(BigInteger.ZERO) == 0) {
					System.out.println("Found that it works with " + numPrimesFound + " primes using big integers. (Sum = " + curSumSquarePrimes + ")");
				}
			}
		}
		
	}
	
	public static long NUM_BLOCKS = 1000000;
	
	public static void solveCleverWithBigInteger() {
		long numPrimesFound = 0;
		
		BigInteger curSumSquarePrimes = BigInteger.ZERO;
		
		
		for(long n=0; n<NUM_BLOCKS; n++) {
			
			if(n > 0) {
				
				if( n *(BLOCK_SIZE) % 1000000000 == 0) {
					System.out.println("Num blocks check so far: " + n + "B");
				}
				
				
				//System.out.println("Hello!");
					getBoolListOfPrimesFromStartWithSize(n*BLOCK_SIZE, BLOCK_SIZE);
				/*for(int i=0; i<isPrime.length; i++) {
					if(isPrime[i]) {
						System.out.println(n*BLOCK_SIZE + i);
					}
				}*/
			} else {
				/*for(int i=0; i<isPrime.length; i++) {
					if(isPrime[i]) {
						System.out.println(n*BLOCK_SIZE + i);
					}
				}*/
			}
			
			for(int i=0; i<BLOCK_SIZE; i++) {
				
				if(isPrime[i]) {
					numPrimesFound++;
					
					long curNum = n*BLOCK_SIZE + i;
					curSumSquarePrimes = curSumSquarePrimes.add(new BigInteger(curNum +"").multiply(new BigInteger(curNum +"")));
					
					if(/*isPrime[numPrimesFound] && */curSumSquarePrimes.divideAndRemainder(new BigInteger("" + numPrimesFound))[1].compareTo(BigInteger.ZERO) == 0) {
						System.out.println("Found that it works with " + numPrimesFound + " primes using big integers. (Sum = " + curSumSquarePrimes + ")");
					}
				}
			}
		}
	}
	

	public static int CHECK_LIMIT = (int)Math.max(10000000, Math.ceil(Math.sqrt(NUM_BLOCKS * BLOCK_SIZE)));
	public static boolean primesToCheckBool[] = UtilityFunctions.UtilityFunctions.getBoolListOfPrimesUpToN( CHECK_LIMIT);
	
	//TODO: test this
	public static void getBoolListOfPrimesFromStartWithSize(long start, int size) {
		
		if(start == 0) {
			isPrime=  UtilityFunctions.UtilityFunctions.getBoolListOfPrimesUpToN(size);
		
		} else if(start < size) {
			System.out.println("ERROR: start number should be at least just as big as the size!");
			System.exit(1);
		}
		
		if(start % 2 != 0 || size %2 != 0) {
			System.out.println("Error: size and start should both be mult 2");
		}

		isPrime[0] = false;
		isPrime[2] = false;
		for(int i=1; 2*i-1<isPrime.length; i++) {
			isPrime[2*i-1] = true;
		}
		
		int limitSqrt = (int)Math.floor(Math.sqrt(start+size + 1));
		
		for(long i=3; i <= limitSqrt; i++) {
			if(primesToCheckBool[(int)i] == true) {
				
				long twoI = 2*i;
				long offset = start % twoI;
				
				long firstMult = 0;
				if(offset != 0) {
					firstMult = twoI - offset;
				}
				
				if(firstMult >= i) {
					firstMult -=i;
				} else {
					firstMult += i;
				}
				/*
				if(start + firstMult < i*i) {
					firstMult = i*i - start;
				}*/
				
				for(long j=0; firstMult + twoI * j < size; j++) {
					isPrime[(int)(firstMult + twoI*j)] = false;
				}
			}
		}
	}
	/*
	Num blocks check so far: 1380B
	Found that it works with 51283502951 primes using big integers. (Sum = 31734804589156174948658730855096778)
	Num blocks check so far: 1381B
	*/

}
