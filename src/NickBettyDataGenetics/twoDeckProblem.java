package NickBettyDataGenetics;

import java.math.BigInteger;

import UtilityFunctions.Fraction;

public class twoDeckProblem {

	/* Question:
	 * If you deal from two shuffled decks, in parallel, card by card, what is the probability that at least one pair of cards matches?
	 */

	//Solution: it's 1- 1/e^1 or 1- e^(-1)
	// Or about 63.21205588 %
	// Question: why???
	public static BigInteger counts[] = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int N = 1000;
		counts= new BigInteger[N + 1];
		
		for(int i=0; i<=N; i++) {
			
			double result = solve(i);
			System.out.println("N = " + i + ": " + result);
			
		}
	}

	
	public static double solve(int n) {
		Fraction result = new Fraction(solveNumerator(n), solveDenominator(n));
		
		System.out.println(solveNumerator(n));
		System.out.println(factorial(n));
		return result.getDecimalFormat(19);
	}
	
	public static BigInteger solveNumerator(int n) {
		
		if(n < 1) {
			return BigInteger.ZERO;
		}
		if(n == 1) {
			return BigInteger.ZERO;
			
		} else if(counts[n] != null) {
			return counts[n];
		}
		//First all mix in 1 big chain:
		
		
		BigInteger ret = BigInteger.ZERO;
		
		BigInteger allChained = factorial(n-1);
		
		ret = ret.add(allChained);
		
		//hard part
		
		for(int i=2; i<n; i++) {

			BigInteger nextTerm = factorial(n-1).divide(factorial(n-i)).multiply(solveNumerator(n-i));
			
			ret = ret.add(nextTerm);
		}
		
		counts[n] = ret;
		return counts[n];
	}
	public static BigInteger solveDenominator(int n) {
		return factorial(n);
	}
	
	public static BigInteger factorial(int n) {
		BigInteger ret = BigInteger.ONE;
		
		for(int i=2; i<=n; i++) {
			ret = ret.multiply(new BigInteger("" + i));
		}
		
		return ret;
		
	}
}
