package erdos295;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import UtilityFunctions.Fraction;

public class prob295ThirdTrial {


	//Made it to:
	//Search minDenom: 5 and numTerms: 10
	//but then stalled...
	
	public static void main(String args[]) {
		
		System.out.println("Third Trial!");
		solve(1, 1);
		
		System.out.println("Next:");
		solve(2, 3);
		

		System.out.println("Next:");
		solve(3, 5);
		
		for(int i=4; i<16; i++) {
			for(int j=i+1; true; j++) {
				System.out.println("Search minDenom: " + i + " and numTerms: " + j);
				boolean foundSolution = solve(i, j);
				
				if(foundSolution) {
					break;
				}
			}
			
			System.out.println();
		}
	}
	
	public static Fraction array[] = new Fraction[1000];

	//setupBigIntegerNums
	static  {
		for(int i=0; i<array.length; i++) {
			array[i] = new Fraction(new BigInteger("" + i), BigInteger.ONE);
		}
	}
	
	public static boolean solve(int minDenom, int numTerms) {
		
		foundSolution = false;
		solve(minDenom, numTerms, Fraction.ONE, new ArrayList<Long>(), true);
		
		return foundSolution;
		
	}

	public static boolean foundSolution = false;
	
	public static long debug = 0;
	
	public static boolean solve(long minDenom, int numTerms, Fraction target, ArrayList<Long> cur, boolean debugExpectedSol) {
		
		if(debug % 100000000 == 0) {
			printCur(cur);
		}
		debug++;
		if(numTerms == 1) {
			if(target.getNumerator().equals(BigInteger.ONE)) {
				
				if(target.getDenominator().longValue() >= minDenom) {
					
					String ret = "";
					for(int i=0; i<cur.size(); i++) {
						ret += "" + cur.get(i) + "^-1 +  ";
					}
					//I think the final denominator could be very big, so don't convert to 64 bit long:
					ret += "" + target.getDenominator() + "^-1";
					ret += "\n";
					System.out.println("Solution: " + ret);
					foundSolution = true;
					

					if(debugExpectedSol == false) {
						System.out.println("Unexpected!");
						System.exit(1);
					}
				}
			}
			return foundSolution;
		}
		

		Fraction tmpMaxDenomFractionIfRestEqual = Fraction.divide(array[numTerms], target);
		Fraction tmpMaxDenomSquared = Fraction.mult(tmpMaxDenomFractionIfRestEqual, tmpMaxDenomFractionIfRestEqual);
		
		//maxDenom if the value of the rest of the terms is equal to the current one:
		long maxDenom = (long)Math.ceil(tmpMaxDenomFractionIfRestEqual.getDecimalFormat()) + 1;
		
		//long maxDenomDEBUG = -1;
		//long minDenomDEBUG = -1;

		boolean gotMinFraction = false;
		
		
		if(numTerms == 2) {
			System.out.println("-------------------------------------");
			
			System.out.println(debug);
			if(debug == 514) {
				System.out.println("Debug");
			}
			//TODO: This formula works, but it might be a bit naive... try to improve this on 4th trial.
			//Also, get stats on how much this section doesn't filter...
			Fraction minLast = Fraction.divide(new Fraction(target.getDenominator(), BigInteger.ONE), tmpMaxDenomFractionIfRestEqual);
			
			long possibleMinFor2ndLast = (long)Math.floor(Fraction.divide(Fraction.ONE, target).getDecimalFormat()) - 2;

			//TODO: max last Denom please!
			
			Fraction min2ndLast = new Fraction(1, possibleMinFor2ndLast);
			
			
			//TOOD: why did you do this????
			//Slow while loop...
			//while(min2ndLast.compareTo(target) > 0) {
			//	possibleMinFor2ndLast++;
			//	min2ndLast = new Fraction(1, possibleMinFor2ndLast);
			//}
			
			if(min2ndLast.compareTo(target) > 0) {
				possibleMinFor2ndLast = (long)Math.floor(Fraction.divide(Fraction.ONE, target).getDecimalFormat());
			}
			
			Fraction maxLastDenom = Fraction.divide(Fraction.ONE, Fraction.minus(target, new Fraction(BigInteger.ONE, new BigInteger("" + possibleMinFor2ndLast))));
			
			System.out.println("???" + possibleMinFor2ndLast);
			System.exit(1);
			//
			
			//Check if the second last and last can't do it:
			if(Fraction.plus(Fraction.divide(Fraction.ONE, minLast), min2ndLast).compareTo(target) < 0) {

				//debugExpectedSol = false;
				return foundSolution;
			}
			minDenom = Math.max(possibleMinFor2ndLast, minDenom);
			//BigInteger minDenomBigInt = new BigInteger("" + minDenom);
			
			Fraction minLastFraction = Fraction.divide(Fraction.ONE, minLast);
			
			//TODO: get allowed multiples for i...
			
			//TODO: minDenom and maxDenom should be BigInteger
			ArrayList<BigInteger> listToPayAttentionTo = new ArrayList<BigInteger>();
			
			//TODO: Something is wrong with the condition of this while loop:
			
			Fraction targetDenom = new Fraction(target.getDenominator(), BigInteger.ONE);
			
			for(int k=1; tmpMaxDenomFractionIfRestEqual.compareTo(
					Fraction.divide(Fraction.mult(new Fraction(k, 1), targetDenom), maxLastDenom)) >= 0;
					k++) {
				System.out.println("k: " + k);
				System.out.println("targetDenom: " + targetDenom);
				System.out.println("tmpMaxDenomFractionIfRestEqual: " + tmpMaxDenomFractionIfRestEqual);
				System.out.println("maxLastDenom: " + maxLastDenom);
				ArrayList<BigInteger> list = prob295_get_gcd_numbers.getListOfNumbersToCheck(
						new BigInteger("" + minDenom),
						new BigInteger("" + maxDenom),
						cur,
						k
				);
				
				System.out.println("List length: " + list.size());
				for(int i=0; i<list.size(); i++) {
					System.out.println("element: " + list.get(i));
					System.out.println();
				}
				
				listToPayAttentionTo.addAll(list);
			}
			
			HashSet <BigInteger> listExpectedSet = new HashSet <BigInteger>();
			
			for(int i=0; i<listToPayAttentionTo.size(); i++) {
				listExpectedSet.add(listToPayAttentionTo.get(i));
				
			}
			Object arrayDebug[] = listExpectedSet.toArray();
			System.out.println("------");
			System.out.println("allowed values:");
			for(int i=0; i<arrayDebug.length; i++) {
				System.out.println("arrayDebug: " + arrayDebug[i]);
			}
			
			
			
			// target e/f
			// f = (d1 d2 d3 d4 ... dn-3)/q
			// TODO: idea: 
			// Let M = f * q
			// Do algo to find all numbers x between min and max and has gcd(f, x) = x
			// Should be faster... but harder.
			
			boolean expectedSolutionBefore = debugExpectedSol;
			
			//TODO: copy/paste code except for one if condition.
			for(long i=minDenom; i<=maxDenom; i++) {
				
				Fraction nextFraction = new Fraction(1, i);
				
				if(Fraction.plus(nextFraction, minLastFraction).compareTo(target) < 0) {
					break;
				}
				
	
				if(nextFraction.compareTo(target) > 0) {
					
					if(gotMinFraction == false) {
						i = (int)Math.floor(Fraction.divide(Fraction.ONE, target).getDecimalFormat()) - 2;
						gotMinFraction = true;
					}
					
					continue;
				}
				
				//if(Fraction.mult(nextFraction, array[numTerms - 1]).compareTo(target) < 0) {
					//System.out.println("TODO");
				//}
				
				cur.add((long)i);
				if(listExpectedSet.contains(new BigInteger("" + i)) == false) {
					debugExpectedSol = false;
					System.out.println("Unexpected i: " + i);
					if(i == 42) {
						System.out.println("Debug");
					}
				} else {
					debugExpectedSol = expectedSolutionBefore;
					if(expectedSolutionBefore) {
						System.out.println("Expected i: " + i + " ( " + expectedSolutionBefore + ")");
					}
				}
				Fraction newTarget = Fraction.minus(target, nextFraction);
				
				if(newTarget.compareTo(Fraction.ZERO) < 0) {
					
					//TODO: remove later:
					System.out.println(array[numTerms]);
					System.out.println(nextFraction);
					System.out.println("Target change:");
					System.out.println(target);
					System.out.println(newTarget);
					System.exit(1);
				}
				
				//Testing code:
				//if( (maxDenomDEBUG < i && maxDenomDEBUG >= 0) || (minDenomDEBUG > i && minDenomDEBUG>=0)) {
					//System.out.println("test");
					//System.out.println(maxDenomDEBUG + " vs " + i);
				//	solve(i, numTerms - 1, newTarget, cur, false);
				//} else {
				
					solve(i, numTerms - 1, newTarget, cur, debugExpectedSol);
				//}
				cur.remove(cur.size() - 1);
				
				
			}
			//END TODO: copy/paste code except for one if condition.
			
			
		} else {
		
			for(long i=minDenom; i<=maxDenom; i++) {
				
				Fraction nextFraction = new Fraction(1, i);
				
				
	
				if(nextFraction.compareTo(target) > 0) {
					
					if(gotMinFraction == false) {
						i = (int)Math.floor(Fraction.divide(Fraction.ONE, target).getDecimalFormat()) - 2;
						gotMinFraction = true;
					}
					
					continue;
				}
				
				//if(Fraction.mult(nextFraction, array[numTerms - 1]).compareTo(target) < 0) {
					//System.out.println("TODO");
				//}
				
				cur.add((long)i);
				Fraction newTarget = Fraction.minus(target, nextFraction);
				
				if(newTarget.compareTo(Fraction.ZERO) < 0) {
					
					//TODO: remove later:
					System.out.println(array[numTerms]);
					System.out.println(nextFraction);
					System.out.println("Target change:");
					System.out.println(target);
					System.out.println(newTarget);
					System.exit(1);
				}
				
				//Testing code:
				//if( (maxDenomDEBUG < i && maxDenomDEBUG >= 0) || (minDenomDEBUG > i && minDenomDEBUG>=0)) {
					//System.out.println("test");
					//System.out.println(maxDenomDEBUG + " vs " + i);
				//	solve(i+1, numTerms - 1, newTarget, cur, false);
				//} else {
					solve(i+1, numTerms - 1, newTarget, cur, debugExpectedSol);
				//}
				cur.remove(cur.size() - 1);
				
				
			}
		}
		
		return foundSolution;
		
	}
	
	public static void printCur(ArrayList<Long> cur) {
		
		System.out.println("Debug:");
		String ret = "";
		for(int i=0; i<cur.size(); i++) {
			if(i < cur.size() - 1) {
			ret += "" + cur.get(i) + "^-1 +  ";
			} else {
				ret += "" + cur.get(i) + "^-1";
			}
			
		}
		ret += "\n";
		System.out.println(ret);
	}
}


/*
TODO:

second last term logic is not good yet:

Look what fell through the cracks!
Debug:
12^-1 +  13^-1 +  14^-1 +  15^-1 +  16^-1 +  17^-1 +  18^-1 +  19^-1 +  20^-1 +  21^-1 +  22^-1 +  23^-1 +  24^-1 +  25^-1 +  26^-1 +  27^-1 +  28^-1 +  29^-1 +  33^-1 +  36^-1 +  7006^-1 +  247048166^-1

Debug:
12^-1 +  13^-1 +  14^-1 +  15^-1 +  16^-1 +  17^-1 +  18^-1 +  19^-1 +  20^-1 +  21^-1 +  22^-1 +  23^-1 +  24^-1 +  25^-1 +  26^-1 +  27^-1 +  28^-1 +  29^-1 +  33^-1 +  36^-1 +  7006^-1 +  347048166^-1
Debug:
12^-1 +  13^-1 +  14^-1 +  15^-1 +  16^-1 +  17^-1 +  18^-1 +  19^-1 +  20^-1 +  21^-1 +  22^-1 +  23^-1 +  24^-1 +  25^-1 +  26^-1 +  27^-1 +  28^-1 +  29^-1 +  33^-1 +  36^-1 +  7006^-1 +  447048166^-1

*/