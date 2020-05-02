package news;

import UtilityFunctions.UtilityFunctions;

public class primeStudyReplicate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int NUM = 10000000;
		int BASE = 6;
		boolean primes[] = ramarujan.UtilityFunctions.getBoolListOfPrimesUpToN(NUM);
		
		int lastPrime = -1;
		
		int numRepeatEndDigits = 0;
		int numNonrepeatEndDigits = 0;
		
		int tieCheckIndex = -1;
		
		for(int i=0; i<primes.length; i++) {
			if(primes[i] ) {
				if(lastPrime > 0) {
					if( i % BASE == lastPrime % BASE) {
						//System.out.println("Repeat: " + i + "  " + lastPrime);
						numRepeatEndDigits++;
					} else {
						tieCheckIndex = lastPrime;
						while(tieCheckIndex < i) {
							tieCheckIndex += BASE;
						}
						
						if(primes[tieCheckIndex] == false) {
							//System.out.println("Opposite win: " + i + " (" + lastPrime + ")");
							numNonrepeatEndDigits++;
						} else {
							//System.out.println("Tie: " + tieCheckIndex + " and " + i + " (" + lastPrime + ")");
							//tie
						}
					}
				}
				
				lastPrime = i;
				
			}
			
		
		}
		
		System.out.println("Studying repeating last digit for base " + BASE + " numbers up to " + NUM);
		System.out.println("Num repeats end digits: " + numRepeatEndDigits);
		System.out.println("Num non-repeats and no tie: " + numNonrepeatEndDigits);
		System.out.println("Expected if random: " + (1.0 / (UtilityFunctions.getTotient(BASE) * 1.0)));
		
		System.out.println("Fraction received: " + (1.0 * numRepeatEndDigits) / (1.0 * numNonrepeatEndDigits + 1.0 * numRepeatEndDigits));
		
	}

}
/*1000000000:
 * 
 *
Studying repeating last digit for base 10 numbers up to 1000000000
Num repeats end digits: 9114952
Num non-repeats and no tie: 37684036
Expected if random: 0.25
Fraction received: 0.19476814327694436

*/

/*
Studying repeating last digit for base 3 numbers up to 1000000000
Num repeats end digits: 22633034
Num non-repeats and no tie: 26375899
Expected if random: 0.5
Fraction received: 0.4618144614574653

*/