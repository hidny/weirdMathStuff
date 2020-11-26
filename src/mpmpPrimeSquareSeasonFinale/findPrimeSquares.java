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
				
				//if( n % 100 == 0) {
					System.out.println("Num blocks check so far: " + n);
				//}
				
				
				//System.out.println("Hello!");
					getBoolListOfPrimesFromStartWithSizeTrial2(n*BLOCK_SIZE, BLOCK_SIZE);
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
		
		
		for(int i=0; i<isPrime.length; i++) {
			isPrime[i] = true;
		}
		isPrime[0] = false;
		
		int limitSqrt = (int)Math.floor(Math.sqrt(start+size + 1));
		
		for(long i=2; i <= limitSqrt; i++) {
			if(primesToCheckBool[(int)i] == true) {
				
				long offset = start % i;
				
				long firstMult = 0;
				if(offset != 0) {
					firstMult = i - offset;
				}
				
				if(start + firstMult < i*i) {
					firstMult = i*i - start;
				}
				
				for(long j=0; firstMult + i * j < size; j++) {
					isPrime[(int)(firstMult + i*j)] = false;
				}
			}
		}
	}

	

}
