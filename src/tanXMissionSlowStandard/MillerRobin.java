package tanXMissionSlowStandard;

import java.math.BigInteger;

public class MillerRobin {

	public static void main(String[] args) {

		//TEST miller-rabin by comparing it to basic isPrime function: (It passes)
		for(int i=1; i<=1000000; i++) {
			
			boolean millerPrime = isMillerRabinPrime(new BigInteger(i + ""), 5);
			boolean utilCheckPrime = UtilityFunctions.UtilityFunctions.isPrime(i);
			
			if(millerPrime
					&& utilCheckPrime) {
				System.out.println(i);
				
			} else if(!millerPrime && !utilCheckPrime) {
				
			} else if(millerPrime && !utilCheckPrime) {
				System.out.println(i);
				System.out.println("AHH: miller prime not a prime!");
				System.exit(1);
			} else {
				System.out.println(i);
				System.out.println("AHH! Prime not a miller prime!");
				System.exit(1);
			}
		}
	}
	
	//Hard-code first few primes because I'm lazy...
	public static int primes[] = new int[]{2, 3, 5, 7, 11, 13, 17, 19, 23};

	
	public static final BigInteger TWO = new BigInteger("2"); 
	
	//TODO: for large numbers, this isn't the perfect test...
	//Maybe retest if we think we found one...
	
	//Input #1: n > 3, an odd integer to be tested for primality
	//Input #2: k, the number of rounds of testing to perform
	
	//https://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test
	public static boolean isMillerRabinPrime(BigInteger input, int k) {
		
		if(input.compareTo(new BigInteger("3")) <=0) {
			if(input.equals(BigInteger.ONE)) {
				return false;
			}
			System.out.println("input too small!");
			return true;
		}
		
		if(new BigInteger(primes[k] + "").compareTo(input) > 0) {
			System.out.println("k is two");
			k = 2;
		}
		
		BigInteger inputMinus1 = input.subtract(BigInteger.ONE);
		BigInteger d = inputMinus1;
		int r = 0;
		while(d.divideAndRemainder(TWO)[1].equals(BigInteger.ZERO)) {
			d = d.divide(TWO);
			r++;
		}
		
		//If r is 0 then input is even...
		if(r == 0) {
			return false;
		}
		
		WitnessLoop:
		for(int i=0; i<k; i++) {
			//They said pick random, but meh....
			BigInteger a = new BigInteger("" + primes[i]);
			
			
			BigInteger x = UtilityFunctions.UtilityFunctions.getAPowerPmodMOD(a, d, input);
			
			if(x.equals(BigInteger.ONE) || x.equals(input.subtract(BigInteger.ONE))) {
				continue WitnessLoop;	
			}
			
			for(int j=0; j<r-1; j++) {
				x = UtilityFunctions.UtilityFunctions.getAPowerPmodMOD(x, TWO, input);
				
				if( x.equals(input.subtract(BigInteger.ONE))) {
					continue WitnessLoop;
				}
			}
			
			return false;
		}
		
		//probably prime
		return true;
	}
}
