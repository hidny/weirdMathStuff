package tanXMission;

import java.math.BigInteger;

import UtilityFunctions.Fraction;

/* youtube comment: https://www.youtube.com/watch?v=A7eJb8n8zAw
 //What is the biggest tangent of a prime?
 * Moritz Ernst Jacob
19 hours ago (edited)
There's actually a way to generate those hits efficiently:
1. Get a rational approximation for pi = a/b, where a is even.
2. Your magic number x with tan(x) = large is calculated as: x = a*(k+1/2).
3. Cranking up k will make the approximation worse and worse, so at some point you'll have to find the next rational approximation for pi to generate more numbers.

4. Regarding primality: Since a has to be even, multiplying by (k+1/2) will always result in a composite number for k>0. So for each family of magic numbers, only the very first can be prime. Or in other words: only those apprimations of pi where the numerator is 2*prime will give us primes in this process.


 */

/*Update youtube comment:
 * Samuel Li
Samuel Li
1 day ago
It has 1017 digits, the first 10 of which are 2308358707.
 */

//TODO: rename class name:
public class ExtremeTanXCalculator {

	

	public static void initializeListOfPrimes() {
		BigIntegerPrimesList.initialize();
	}
	
	public static final BigInteger TWO = new BigInteger("2");
	
	public static void attemptTanXCheckUsePiApproxNoDoublePiOn2(Fraction piOn2ApproxToDeriveX, Fraction currentPrecisePiOn2) {

			BigInteger X = piOn2ApproxToDeriveX.getNumerator();

			//WANT A PRIME:
			//TODO: Test more primes before using miller-robin test:
			if(BigIntegerPrimesList.isProbPrime(X) == false) {
				//System.out.println("Skip non-primes!");
				return;
			}

			//TODO: FastCheck
			
			if(fastCheckSaysYes(piOn2ApproxToDeriveX, currentPrecisePiOn2) == false) {
				return;
			}
			/*
			if(BigIntegerPrimesList.isProbPrime(X) == false) {
				System.out.println("Stopped by extra prime check!");
				//System.out.println("Skip non-primes!");
				return;
			}
			*/
			
			System.out.println("Checking miller robin prime!");
			//Try the miller-robin test
			//https://www.youtube.com/watch?v=RNxr7km8lHo
			if(MillerRobin.isMillerRabinPrime(X, 1) == false) {
				//System.out.println("Skip non-primes miller-robin test!");
				return;
				
			}
			
			
			
			System.out.println("Found X = " + X);
			System.out.println("Please double triple check ifs primality!");
			if(MillerRobin.isMillerRabinPrime(X, 8) == false) {
				System.out.println("AHH! It is not a real prime!");
			}
			
	}
	
	public static boolean fastCheckSaysYes(Fraction piOn2ApproxToDeriveX, Fraction currentPrecisePiOn2) {
		
		Fraction A = new Fraction(piOn2ApproxToDeriveX.getNumerator(), BigInteger.ONE);
		Fraction B = new Fraction(piOn2ApproxToDeriveX.getDenominator(),  BigInteger.ONE);
		
		Fraction OneOverA = Fraction.divide( Fraction.ONE, A);
		
		Fraction Apart = Fraction.plus(A, OneOverA);
		
		
		Fraction BPiOver2 = Fraction.mult(B, currentPrecisePiOn2);
		
		//23083587078255883156161718650455908419871
		if(Fraction.minus(Apart, BPiOver2).greaterThan0()) {

			System.out.println(A.getNumerator() + " + " + OneOverA.getDecimalFormat(15));
			System.out.println("vs " + B.getNumerator() + " * (pi/2) = (" + BPiOver2.getDecimalFormat(15) +")");

			if(Fraction.minus(A, BPiOver2).greaterThan0()) {
				System.out.println("ERROR: A greater than B* (Pi/2)! This should not happen!");
				System.out.println(A.getNumerator());
				System.out.println(B.getNumerator());
				System.exit(1);
			}
			//System.out.println("Found X = " + X );
			//System.out.println("Please double triple check ifs primality!");
			
			System.out.println("Got passed fast Check!");
			System.out.println("Factor LHS/RHS: "  + Fraction.divide(A, BPiOver2).getDecimalFormat(15));
			return true;
		}
		
		return false;
	}

	public static boolean fastCheckSaysYes3(Fraction piApproxToDeriveX, Fraction currentPrecisePiOn2) {
	
		Fraction currentPrecisePi = Fraction.mult(currentPrecisePiOn2, new Fraction(2, 1));
		
		Fraction a = new Fraction(piApproxToDeriveX.getNumerator(), BigInteger.ONE);
		
		Fraction XDividePi = Fraction.divide(piApproxToDeriveX, currentPrecisePi); 
		
		BigInteger quotient = XDividePi.getNumerator().divideAndRemainder(XDividePi.getDenominator())[0];
		
		Fraction aHalfCirc = Fraction.minus(
						a,
						Fraction.mult(currentPrecisePi, new Fraction(quotient, BigInteger.ONE))
				);
		
		Fraction sumA = Fraction.plus(aHalfCirc, Fraction.divide(Fraction.ONE, a));
		
	
		if(Fraction.minus(sumA, currentPrecisePiOn2).greaterThan0()) {
			//System.out.println("Found X = " + X );
			//System.out.println("Please double triple check ifs primality!");
			
			System.out.println("Got passed fast Check!");
			System.out.println("Factor LHS/LHS: "  + Fraction.divide(sumA, currentPrecisePiOn2).getDecimalFormat(15));
			return true;
		}
		
		return false;
	}
	
	

	public static boolean fastCheckSaysYes2(Fraction piApproxToDeriveX, Fraction currentPrecisePi) {

		Fraction a = new Fraction(piApproxToDeriveX.getNumerator(), BigInteger.ONE);
		Fraction b = new Fraction(piApproxToDeriveX.getDenominator(), BigInteger.ONE);
		
		Fraction LHS = Fraction.minus(
				Fraction.mult(currentPrecisePi, b)
				, a);
		
		Fraction RHS = Fraction.divide(Fraction.ONE, a);
		
		//TODO: I bet if pi is more precise, I could lower the fudge factor...
		//Look into it!
		//Fudge factor of 2... I don't know...
		Fraction LHStest = Fraction.mult(new Fraction(20,  10), LHS);
		
		if(Fraction.minus(RHS, LHS).greaterThan0()) {
			//System.out.println("Found X = " + X );
			//System.out.println("Please double triple check ifs primality!");
			
			System.out.println("Got passed fast Check!");
			System.out.println("Factor LHS/LHS: "  + Fraction.divide(RHS, LHS).getDecimalFormat(15));
			return true;
		}
		
		return false;
	}


	//pre: x is near pi.
	//post: return 1 /cos x
	//if x is near pi, then tan x = sin x / cos x almost = 1 /cosx
	public static Fraction tanApproxAtPiOver2(Fraction x, Fraction cosGoalNumber, Fraction currentPrecisePiOn2) {
		
		//Make up some precision:
		int numDigitsPrecision = 3 * cosGoalNumber.getDenominator().toString().length();
		//End make up some precision
		
		return Fraction.divide(Fraction.ONE, cosApprox(x, cosGoalNumber, currentPrecisePiOn2, numDigitsPrecision));
	}
	
	
	//I made it figure out when it has enough info to just stop so it could go slightly faster.	
	public static Fraction cosApprox(Fraction x, Fraction cosGoalNumber, Fraction currentPrecisePiOn2, int numDigitsPrecision) {
		
		if(Fraction.minus(x, currentPrecisePiOn2).greaterThan0() == true) {
			
			System.out.println("in cosApprox: X seems slightly too big (i.e. tan x is negative), skipping");
			
			//TODO: maybe we don't need this check anymore??
			System.exit(1);
			return Fraction.ONE;
		}
		
		//xSquared should be just smaller than pi/2...
		Fraction xSquared = Fraction.mult(x, x);
		
		Fraction xPowerN = Fraction.ONE;
		
		Fraction output = Fraction.ONE;
		Fraction factorial = Fraction.ONE;
		
		System.out.println("Cos of : " + x.getDecimalFormat(20));
		
		for(int i=1; true; i++) {
			System.out.println(i);
			
			factorial = Fraction.mult(factorial, new Fraction(i, 1));
			factorial = approx(factorial, numDigitsPrecision);
			
			if(i %2 == 0) {
				boolean signIsPositive = false;
				if(i % 4 == 0) {
					signIsPositive = true;
				} else {
					signIsPositive = false;
				}

				//TODO: approx based on size of factorial... (nah)
				xPowerN = Fraction.mult(xPowerN, xSquared);
				xPowerN = approx(xPowerN, numDigitsPrecision);
				
				
				Fraction currentTerm =  Fraction.divide(xPowerN, factorial);

				//Approximate a little bit:
				//truncate term to be "only" 1000+ digits in numerator
				currentTerm = approx(currentTerm, numDigitsPrecision);
				
				if(signIsPositive) {
					output = Fraction.plus(output, currentTerm);
				} else {
					output = Fraction.minus(output,  currentTerm);
				}
				
				
				//Cut short but no guarantee that it's above 0.
				if(signIsPositive) {
					if( Fraction.minus(cosGoalNumber, output).greaterThan0()) {
						System.out.println("Early stop 1");
						return output;
						
					}
				} else {
					if( Fraction.minus(cosGoalNumber, output).greaterThan0() == false) {
						System.out.println("Early stop 2");
						return output;
					}
				}
				
				
				System.out.println("i = " + i + ": " + output.getDecimalFormat(40));
				
			}
			
		}
		
		
		
	}
	
	public static Fraction approx(Fraction input, int limitDenomSize) {
		BigInteger numeratorTerm = input.getNumerator();
		BigInteger denominatorTerm = input.getDenominator();
		
		if(numeratorTerm.toString().length() > limitDenomSize) {
			
			int digitsCut = numeratorTerm.toString().length() - limitDenomSize;
			
			System.out.println("Cutting " + digitsCut + " digits of the term's fraction");
			
			String numTermString = numeratorTerm.toString();
			String denomTermString = denominatorTerm.toString();
			
			numeratorTerm = new BigInteger(numTermString.substring(0, numTermString.length() - digitsCut));
			denominatorTerm = new BigInteger(denomTermString.substring(0, denomTermString.length() - digitsCut));
			
			input = new Fraction(numeratorTerm, denominatorTerm);
		}
		
		return input;
	}
	
	
	//Find close enough fractions:
	//(K1a+K2c)/(K1c+K2d)
	//(Not best approx... but passes tan x > x test
	
}
