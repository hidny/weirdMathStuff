package tanXMission;

import java.math.BigInteger;

import UtilityFunctions.Fraction;

public class oldPiByContinuedFractionCode {


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
	
	//TODO: remove need for double (remake own tan function)
	//With taylor series...
	//See wolfram: tan(x) power series
	
	//TOOD: more digits of pi
	
	//12055686754159438
	//

	//uses java's double type which stops working at some point:
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
	
	//Basic tan x check that worked for fractions with a low number of digits:
	public static void attemptTanXCheck(Fraction inputFraction) {
		if(inputFraction.getNumerator().mod(new BigInteger("2")) == BigInteger.ZERO) {
			//System.out.println("Numerator even (good sign)");
			
			BigInteger X = inputFraction.getNumerator().divide(new BigInteger("2")) ;
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
	
	
	
}
