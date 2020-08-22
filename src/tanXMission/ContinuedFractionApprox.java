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

public class ContinuedFractionApprox {

	//A001203
	//3,7,15,1,292,1,1,1,2,1,3,1,14,2,1,1,2,2,2,2,1,84,
	 //2,1,1,15,3,13,1,4,2,6,6,99,1,2,2,6,3,5,1,1,6,8,1,
	 //7,1,2,3,7,1,2,1,1,12,1,1,1,3,1,1,8,1,1,2,1,6,1,1,
	 //5,2,2,3,1,2,4,4,16,1,161,45,1,22,1,2,2,1,4,1,2,24,
	 //1,2,1,3,1,2,1
	 
	//TODO: build your own cont Frac???
	public static int contFrac[] = new int[] {3,7,15,1,292,1,1,1,2,1,3,1,14,2,1,1,2,2,2,2,1,84,
		 2,1,1,15,3,13,1,4,2,6,6,99,1,2,2,6,3,5,1,1,6,8,1,
		 7,1,2,3,7,1,2,1,1,12,1,1,1,3,1,1,8,1,1,2,1,6,1,1,
		 5,2,2,3,1,2,4,4,16,1,161,45,1,22,1,2,2,1,4,1,2,24,
		 1,2,1,3,1,2,1};
	
	public static void main(String args[]) {
		for(int i=0; i<contFrac.length; i++) {
			System.out.println(getPiApproxContinuedFraction(i).Approx(10));
			System.out.println(getPiApproxContinuedFraction(i).getNumerator());
			System.out.println("-------------------------------------------------------");
			System.out.println(getPiApproxContinuedFraction(i).getDenominator());
			
			attemptTanXCheck(getPiApproxContinuedFraction(i));
			
			
			
			System.out.println();
			
			
		}
		
		PI.pi5000();
		
	}
	
	

	public static void attemptTanXCheckUsePiApproxNoDouble(Fraction piApprox) {
		if(piApprox.getNumerator().mod(new BigInteger("2")) == BigInteger.ZERO) {
			//System.out.println("Numerator even (good sign)");
			
			BigInteger X = piApprox.getNumerator().divide(new BigInteger("2"));
			
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
			Fraction tanX = badTanApprox(remainderAfterDivbyPi, cosGoalNumber);
			
			if(Fraction.minus(tanX, new Fraction(X, BigInteger.ONE)).greaterThan0()) {
				System.out.println("Found X = " + X + " where tan X = " + tanX.getDecimalFormat(10));
			}
			
		}
	}
	
	//TODO: remove need for double (remake own tan function)
	//With taylor series...
	//See wolfram: tan(x) power series
	
	//TOOD: more digits of pi
	
	//12055686754159438
	//

	public static void attemptTanXCheckUsePiApprox(Fraction piApprox) {
		if(piApprox.getNumerator().mod(new BigInteger("2")) == BigInteger.ZERO) {
			//System.out.println("Numerator even (good sign)");
			
			BigInteger X = piApprox.getNumerator().divide(new BigInteger("2"));
			
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
			
			Fraction remainderAfterDivPi = Fraction.minus(new Fraction(X, BigInteger.ONE),
								Fraction.mult(PI.pi5000(), new Fraction(quotient, BigInteger.ONE)));
			
			//System.out.println("Debug: " + remainderAfterDivPi.getDecimalFormat(200));
			double ret = Math.floor(Math.tan(remainderAfterDivPi.getDecimalFormat(200)));
			
			//System.out.println("Ret: " + ret);
			
			double trialX = new Fraction(X, BigInteger.ONE).getDecimalFormat();
			
			if(ret >= trialX ) {
				System.out.println("Found: " + ret + "  vs  " + X);
			}
		}
	}
	
	public static void attemptTanXCheck(Fraction piApprox) {
		if(piApprox.getNumerator().mod(new BigInteger("2")) == BigInteger.ZERO) {
			//System.out.println("Numerator even (good sign)");
			
			BigInteger X = piApprox.getNumerator().divide(new BigInteger("2")) ;
			double trialX = new Fraction(X, BigInteger.ONE).getDecimalFormat();
			
			double ret = Math.floor(Math.tan(trialX));
			
			if(ret >= trialX ) {
				System.out.println("Found: " + ret + "  vs  " + X);
			}
		}
	}
	
	
	
	
	public static Fraction getPiApproxContinuedFraction(int n) {
		
		//Decimal part
		
		Fraction currentFraction = Fraction.ZERO;
		
		for(int i=n; i>0; i--) {
			currentFraction = Fraction.divide(Fraction.ONE, Fraction.plus(currentFraction, new Fraction(contFrac[i], 1)));
		}
		

		Fraction wholeNumberPart = new Fraction(contFrac[0], 1);
		Fraction answer = Fraction.plus(wholeNumberPart, currentFraction);
		
		return answer;
	}
	
	
	public static int STOP_POINT = 400;
	
	public static Fraction piOn2 = Fraction.divide(PI.pi5000(), new Fraction(2, 1));
	
	

	//TODO: experiment with approximating the mediant or limiting the precision of each term
	
	//I made it figure out when it has enough info to just stop so it could go slightly faster.	
	public static Fraction cosApprox(Fraction x, Fraction goal) {
		
		if(Fraction.minus(x, piOn2).greaterThan0() == true) {
			System.out.println("X seems slightly too big, skipping");
			return Fraction.ONE;
		}
		
		Fraction xSquared = Fraction.mult(x, x);
		Fraction xPowerN = Fraction.ONE;
		
		Fraction output = Fraction.ONE;
		Fraction factorial = Fraction.ONE;
		
		System.out.println("Cos of : " + x.getDecimalFormat(20));
		
		for(int i=1; i< STOP_POINT; i++) {
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
				
				Fraction currentTerm =  Fraction.divide(xPowerN, factorial);
				
				if(signIsPositive) {
					output = Fraction.plus(output, currentTerm);
				} else {
					output = Fraction.minus(output,  currentTerm);
				}
				
				
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
		
		
		return output;
		
	}
	
	//pre: x is near pi.
	public static Fraction badTanApprox(Fraction x, Fraction goalNumber) {
		return Fraction.divide(Fraction.ONE, cosApprox(x, goalNumber));
	}
	
	//TODO: make some kind of guarantee that tan x > x
	
	//Find close enough fractions:
	//(K1a+K2c)/(K1c+K2d)
	//(Not best approx... but passes tan x > x test
	
}
