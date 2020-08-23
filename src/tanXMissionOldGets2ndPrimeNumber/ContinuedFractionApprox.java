package tanXMissionOldGets2ndPrimeNumber;

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

public class ContinuedFractionApprox {

	
	

	public static void attemptTanXCheckUsePiApproxNoDouble(Fraction piApprox) {
		if(piApprox.getNumerator().mod(new BigInteger("2")) == BigInteger.ZERO) {
			//System.out.println("Numerator even (good sign)");
			
			BigInteger X = piApprox.getNumerator().divide(new BigInteger("2"));
			
			//TODO:
			//Try the miller-robin test
			//https://www.youtube.com/watch?v=RNxr7km8lHo
			//WANT A PRIME:
			if(X.divideAndRemainder(new BigInteger("2"))[1] == BigInteger.ZERO
					|| X.divideAndRemainder(new BigInteger("3"))[1] == BigInteger.ZERO
							|| X.divideAndRemainder(new BigInteger("5"))[1] == BigInteger.ZERO
							|| X.divideAndRemainder(new BigInteger("7"))[1] == BigInteger.ZERO) {
				//System.out.println("Skip non-primes!");
				return;
			}
			
			if(MillerRobin.isMillerRabinPrime(X, 5) == false) {
				//System.out.println("Skip non-primes miller-robin test!");
				return;
				
			}
			
			Fraction XDividePi = Fraction.divide(new Fraction(X, BigInteger.ONE), PI.pi5000());
			BigInteger quotient = XDividePi.getNumerator().divideAndRemainder(XDividePi.getDenominator())[0];
			/*
			System.out.println(X);
			System.out.println(quotient);
			System.out.println(new Fraction(X, BigInteger.ONE).getDecimalFormat());
			
			System.out.println(Fraction.mult(PI.pi5000(), new Fraction(quotient, BigInteger.ONE)).getDecimalFormat());
			System.out.println("Boo! " + PI.pi5000().getDecimalFormat(15));
			System.out.println(new Fraction(quotient, BigInteger.ONE).getDecimalFormat(15));
			*/
			
			Fraction remainderAfterDivbyPi = Fraction.minus(new Fraction(X, BigInteger.ONE),
								Fraction.mult(PI.pi5000(), new Fraction(quotient, BigInteger.ONE)));
			
			Fraction cosGoalNumber = new Fraction(BigInteger.ONE, X);
			
			if(X.toString().length() > 10) {
				System.out.println("Try:");
				System.out.println(X.toString());
			}
			Fraction tanX = tanApproxAtPiOver2(remainderAfterDivbyPi, cosGoalNumber);
			
			if(Fraction.minus(tanX, new Fraction(X, BigInteger.ONE)).greaterThan0()) {
				System.out.println("Found X = " + X + " where tan X = " + tanX.getDecimalFormat(10));
			}
			
		}
	}
	
	
	
	public static Fraction piOn2 = Fraction.divide(PI.pi5000(), new Fraction(2, 1));
	
	public static int LIMIT_NUMERATOR_SIZE = 8000;

	//TODO: experiment with approximating the mediant or limiting the precision of each term
	

	//pre: x is near pi.
	//post: return 1 /cos x
	//if x is near pi, then tan x = sin x / cos x almost = 1 /cosx
	public static Fraction tanApproxAtPiOver2(Fraction x, Fraction goalNumber) {
		return Fraction.divide(Fraction.ONE, cosApprox(x, goalNumber));
	}
	
	
	//I made it figure out when it has enough info to just stop so it could go slightly faster.	
	public static Fraction cosApprox(Fraction x, Fraction goal) {
		
		//TODO: get relevant value of pi based on size of x
		
		if(Fraction.minus(x, piOn2).greaterThan0() == true) {
			System.out.println("X seems slightly too big, skipping");
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
			
			if(i %2 == 0) {
				boolean signIsPositive = false;
				if(i % 4 == 0) {
					signIsPositive = true;
				} else {
					signIsPositive = false;
				}
				
				xPowerN = Fraction.mult(xPowerN, xSquared);
				
				//TODO: approx based on size of factorial...
				xPowerN = approx(xPowerN);
				
				Fraction currentTerm =  Fraction.divide(xPowerN, factorial);
				
				if(signIsPositive) {
					output = Fraction.plus(output, currentTerm);
				} else {
					output = Fraction.minus(output,  currentTerm);
				}
				
				//Approximate a little bit:
				//truncate term to be "only" 1000+ digits in numerator
				
				currentTerm = approx(currentTerm);
				
				//Cut short but no guarantee that it's above 0.
				if(signIsPositive) {
					if( Fraction.minus(goal, output).greaterThan0()) {
						System.out.println("Early stop 1");
						return output;
						
					}
				} else {
					if( Fraction.minus(goal, output).greaterThan0() == false) {
						System.out.println("Early stop 2");
						return output;
					}
				}
				
				
				System.out.println("i = " + i + ": " + output.getDecimalFormat(40));
				
			}
			
		}
		
		
		
	}
	
	//TODO: limit denominator size based on goal
	//Maybe 2*#digits in goal
	public static Fraction approx(Fraction input) {
		BigInteger numeratorTerm = input.getNumerator();
		BigInteger denominatorTerm = input.getDenominator();
		
		if(numeratorTerm.toString().length() > LIMIT_NUMERATOR_SIZE) {
			
			int digitsCut = numeratorTerm.toString().length() - LIMIT_NUMERATOR_SIZE;
			
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
