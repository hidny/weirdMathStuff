package OneNet3Cuboids.MultiplePiecesHandler;

import java.math.BigInteger;

import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import UtilityFunctions.UtilityFunctions;

public class ComputeBatchMain {

	/*
	 * 
13x1x1 and 3x3x3 where max depth is 13:
Num pieces found: 2083716
Final num pieces:
2083716

secret key: 122572
Exp: 17
Mod: 2083723
	 */
	public static int BATCH_SIZE = 1000;
	public static int START_DEPTH = 13;
	public static int GET_ALL_PIECES = -1;
	
	public static int indexFromArgTODO = 2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		long numPieces = getNumPieces(START_DEPTH);

		System.out.println("Final num pieces:");
		System.out.println(numPieces);

		System.out.println();
		System.out.println("Setup shuffle for pseudo-random sampling of tasks:");
		BigInteger shuffleParams[] = getShuffleNumbers(numPieces);
		
		BigInteger exp = shuffleParams[0];
		BigInteger mod = shuffleParams[1];

		System.out.println("Exp: " + exp);
		System.out.println("Mod: " + mod);
		System.out.println();

		for(int i=0; i<BATCH_SIZE; i++) {
			long indexBeforeTranslation = indexFromArgTODO * BATCH_SIZE + i;
			
			int indexAfterTranslation = getAPowerPmodMOD(new BigInteger("" + indexBeforeTranslation), exp, mod).intValue();
			
			if(indexAfterTranslation >= numPieces) {
				continue;
			}
			
			ComputeTaskMain.runSubtask(START_DEPTH, indexAfterTranslation);

			System.out.println("Index pre-shuffle to post-shuffle: " + indexBeforeTranslation + " to " + indexAfterTranslation);
			System.out.println("Done piece index: " + indexAfterTranslation);
			System.out.println("Num solutions found after " + (i+1) + " iterations: " + BasicUniqueCheckImproved.uniqList.size());
			System.out.println("-----");
			System.out.println("");
			System.out.println("");

		}
		
	}
	
	//Shuffling the indexes based on RSA encryption:
	// This allows me to sample the search space in a way that isn't random, but can probably 
	// be treated as random enough for me to extrapolate the time it will take and how many solutions there are.

	public static BigInteger[] getShuffleNumbers(long numPieces) {
		
		long mod = numPieces;
		
		int defaultExp = 17;

		int carMichaelTotient = -1;
		
		for(; true; mod++) {
			
			if(mod % 2 == 1
					&& getPrimeDivisors(mod).length == 2) {
				

				long primes[] = getPrimeDivisors(mod);
				carMichaelTotient = (int)getLCM(primes[0] - 1, primes[1] - 1);
				
				if(carMichaelTotient % defaultExp != 0) {
					break;
				}
			}
		}
		
		
		int d = 1;
		//Compute the inverse the slow way:
		while( (d * defaultExp) % carMichaelTotient != 1) {
			d++;
		}
		
		System.out.println("secret key: " + d);
		
		sanityTestShuffle(new BigInteger("" + defaultExp), new BigInteger("" + d), new BigInteger("" + mod));
		
		return new BigInteger[] {new BigInteger("" + defaultExp), new BigInteger("" + mod)};
	}
	

	public static void sanityTestShuffle(BigInteger exp, BigInteger key, BigInteger mod) {
		//BigInteger exp = new BigInteger("" + 1);
		//BigInteger key = new BigInteger("" + 13);
		//int modInt= 21;
		
		System.out.println("Sanity testing RSA shuffle:");
		
		int modInt = mod.intValue();
		
		for(int i=0; i<modInt; i++) {
			
			BigInteger iBig = new BigInteger("" + i);
			BigInteger tmp = ComputeBatchMain.getAPowerPmodMOD(iBig, exp, mod);
			
			BigInteger maybeI = ComputeBatchMain.getAPowerPmodMOD(tmp, key, mod);
			
			if(iBig.compareTo(maybeI) != 0) {
				System.out.println("Error: There's a problem with the RSA shuffle for i = " + i);
				System.out.println(i + " encrypted and decrypted to " + maybeI);
				
				System.exit(1);
			}
			
		}
		
		System.out.println("Done sanity test for e = " + exp + ", d = " + key + ", and n = " + modInt);
	}
	
	public static long getLCM(long a, long b) {
		return (a*b)/getGCD(a, b);
	}
	
	public static long getGCD(long a, long b) {
		if(b>a) {
			return getGCD(b, a);
		}
		
		long ret = a % b;
		
		if(ret == 0) {
			return b;
		} else{
			return getGCD(b, ret);
		}
	}
	
	public static long getNumPieces(int startDepth) {
		

		ComputeTaskMain.updateComputeTask(startDepth, GET_ALL_PIECES);
		
		if(ComputeTaskMain.computeTask == null) {
			System.out.println("Target index too high.");
			System.out.println("Num pieces found: " + CuboidComputeTaskGetter.curNumPiecesCreated);
		}
		
		
		
		return CuboidComputeTaskGetter.curNumPiecesCreated;
	}
	
	
	public static BigInteger getAPowerPmodMOD(BigInteger a, BigInteger pow, BigInteger MOD) {
		//base case for power:
		if(pow.compareTo(BigInteger.ZERO) == 0) {
			if(a.compareTo(BigInteger.ZERO) == 0 ) {
				System.out.println("0^0 is I don't know!!!");
				System.exit(1);
			} else if(pow.compareTo(BigInteger.ZERO) < 0 ) {
				System.out.println("No negative powers!" +  a + " to the power of " + pow + "?" );
				System.exit(1);
			}
			return BigInteger.ONE;
		} else if(a.compareTo(BigInteger.ZERO) == 0) {
			return BigInteger.ZERO;
		}
		
		//System.out.println(a + " to the power of " + pow);
		
		int lengthPowTable = 0;
		BigInteger current = BigInteger.ONE;
		while(current.compareTo(pow) <= 0) {
			lengthPowTable++;
			current = current.multiply(new BigInteger("2"));
		}
		
		//System.out.println("Length: " + lengthPowTable);
		
		//Setup the power of 2 table
		BigInteger pow2Table[] = new BigInteger[lengthPowTable];
		pow2Table[0] = a;
		
		
		for(int i=1; i<lengthPowTable; i++) {
			pow2Table[i] = (pow2Table[i-1].multiply(pow2Table[i-1])).mod(MOD);
		}
		//End setup the power of 2 table.
	
		current = pow;
		BigInteger answer = BigInteger.ONE;
		
		
		for(int i=0; i<lengthPowTable && current.compareTo(BigInteger.ZERO) > 0; i++) {
			if(current.mod(new BigInteger("2")).compareTo(BigInteger.ONE) == 0) {
				answer = (answer.multiply(pow2Table[i])).mod(MOD);
				current = current.subtract(BigInteger.ONE);
			}
			current = current.divide(new BigInteger("2"));
		}
		
		return answer;
	}
	


	public static long[] getPrimeDivisors(long n) {
		long nTemp = n;
		long LIMIT = (long)Math.sqrt(n);
		
		int numPrimeDivisors = 0;
		for(long i=2; i<=LIMIT; i++) {
			if(nTemp % i == 0 && isPrime(i)) {
				numPrimeDivisors++;
				while(nTemp % i == 0) {
					nTemp /= i;
				}
			}
		}
		if(nTemp > 1) {
			numPrimeDivisors++;
		}
		
		long divisors[] = new long[numPrimeDivisors];
		
		nTemp = n;
		int currentIndex = 0;
		for(long i=1; i<=LIMIT; i++) {
			if(nTemp % i == 0 && isPrime(i)) {
				divisors[currentIndex] = i;
				currentIndex++;
				while(nTemp % i == 0) {
					nTemp /= i;
				}
			}
		}
		if(nTemp > 1) {
			divisors[currentIndex] = nTemp;
			currentIndex++;
		}
		
		
		return divisors;
	}
	
	public static boolean isPrime(long num) {
		if(num<=1) {
			return false;
		}
		
		int sqrt = (int)Math.sqrt(num);
		for(int i=2; i<=sqrt ; i++) {
			if(num%i == 0) {
				return false;
			}
		}
		
		return true;
	}
	
}
