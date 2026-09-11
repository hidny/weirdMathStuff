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
	
	public static BigInteger DEBUG_MAX_LAST = null;
	
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
					//System.out.println("DEBUG_MAX_LAST: " + DEBUG_MAX_LAST);
					foundSolution = true;
					

					if(DEBUG_MAX_LAST != null && target.getDenominator().compareTo(DEBUG_MAX_LAST) > 0) {
						System.out.println("DOH! DEBUG_MAX_LAST is too big!");
						System.out.println(DEBUG_MAX_LAST);
						System.out.println(target.getDenominator());
						System.exit(1);
					}
					
					if(debugExpectedSol == false) {
						System.out.println("Unexpected!");
						System.exit(1);
					}
				}
			}
			return foundSolution;
		}
		

		Fraction tmpMaxDenom = Fraction.divide(array[numTerms], target);
		Fraction tmpMinFraction = Fraction.divide(target, array[numTerms]);
		
		long maxDenom = (long)Math.ceil(tmpMaxDenom.getDecimalFormat());
		
		Fraction fracDenomTooLow = new Fraction(1, maxDenom -1);
		Fraction fracDenomAtLimit = new Fraction(1, maxDenom);
		
		if(tmpMinFraction.compareTo(fracDenomTooLow) >= 0) {
			System.out.println("ERROR: Didn't find max Denom!");
			System.exit(1);
		}
		if(tmpMinFraction.compareTo(fracDenomAtLimit) < 0) {
			System.out.println("ERROR: max Denom found is too low.");
			System.exit(1);
		}
		

		boolean gotMinFraction = false;
		
		
		if(numTerms == 2) {
			

			long tmpMin2ndLastDenomPot = (long)Math.ceil(Fraction.divide(array[1], target).getDecimalFormat());
			
			// Handle case where the last term is really big:
			if(tmpMin2ndLastDenomPot >= minDenom) {
				
				Fraction min2ndLastDenomFraction = new Fraction(1, tmpMin2ndLastDenomPot);
				Fraction min2ndLastDenomFractionCheck = new Fraction(1, tmpMin2ndLastDenomPot - 1);
				
				if(min2ndLastDenomFraction.compareTo(target) > 0) {
					System.out.println("ERROR: Didn't find min Denom Fraction!");
					System.exit(1);
				}
	
				if(min2ndLastDenomFractionCheck.compareTo(target) < 0) {
					System.out.println("ERROR: min Denom Fraction is too high!");
					System.exit(1);
				}
				
				
				
				Fraction targetAfterFirstTry = Fraction.minus(target, min2ndLastDenomFraction);
				
				if(targetAfterFirstTry.getNumerator().compareTo(BigInteger.ONE) == 0) {
					
					if(targetAfterFirstTry.getDenominator().compareTo(new BigInteger("" + tmpMin2ndLastDenomPot)) > 0) {
						//System.out.println("AAAH ");
						//System.out.println(targetAfterFirstTry.getDenominator());
						//System.out.println("vs: " + new BigInteger("" + tmpMin2ndLastDenomPot));
						cur.add((long)tmpMin2ndLastDenomPot);
						
						DEBUG_MAX_LAST = null;
						//System.out.println("Really big last term");
						
						solve(tmpMin2ndLastDenomPot + 1, numTerms - 1, targetAfterFirstTry, cur, debugExpectedSol);
						
						cur.remove(cur.size() - 1);
						//System.out.println("AAAH2");
					}
				}
				
				
				minDenom = tmpMin2ndLastDenomPot + 1;
			}
			// END Handle case where the last term is really big.

			// Max last term after case where last term is really big is handled:
			
			//Now that the really big term possibility is handled, this is a good upper-bound for the last denominator:
			BigInteger maxLastDenom = new BigInteger("" + minDenom).pow(2);
			
			//System.out.println("min Denom: " + minDenom);
			//System.out.println("tmpMin2ndLastDenomPot: " + tmpMin2ndLastDenomPot);
			
			DEBUG_MAX_LAST = maxLastDenom;
			
			//minLastDenom = 1 + max2ndLastDenom  because the terms are defined to be monotonically increasing.
			long minLastDenom = maxDenom + 1;
			
			
			//Check if the second last and last can't do it:
			if(Fraction.plus(Fraction.divide(Fraction.ONE, minDenom), Fraction.divide(Fraction.ONE, minLastDenom)).compareTo(target) < 0) {

				//debugExpectedSol = false;
				return foundSolution;
			}

			//UP TO HERE

			//TODO: get allowed multiples for i...
			
			//TODO: minDenom and maxDenom should be BigInteger
			ArrayList<BigInteger> listToPayAttentionTo = new ArrayList<BigInteger>();
			
			//TODO: Something is wrong with the condition of this while loop:
			
			Fraction targetDenom = new Fraction(target.getDenominator(), BigInteger.ONE);
			
			//Let target = e/f
			// f*k = b*d
			// b > f*k / dmax
			// b <= maxDenom
			
			int debuglastK = -1;
			for(int k=1; (new Fraction(maxDenom, 1)).compareTo( //k*
					Fraction.divide(Fraction.mult(new Fraction(k, 1), targetDenom), 
							new Fraction(maxLastDenom, BigInteger.ONE))) >= 0;
					k++) {
				
				if(k % 100000 == 0) {
					//TODO: if k is allowed to be big, don't use this trick!
					System.out.println("k = " + k);
				}
				/*System.out.println();
				System.out.println();
				System.out.println("maxDenom: " + maxDenom);

				System.out.println("vs:");
				System.out.println("k: " + k);
				System.out.println("targetDenom: " + targetDenom);
				
				//System.out.println("tmpMaxDenomFractionIfRestEqual: " + tmpMaxDenom);
				System.out.println("maxLastDenom: " + maxLastDenom);
				*/
				ArrayList<BigInteger> list = prob295_get_gcd_numbers.getListOfNumbersToCheck(
						new BigInteger("" + minDenom),
						new BigInteger("" + maxDenom),
						cur,
						k
				);
				
				/*System.out.println("List length: " + list.size());
				for(int i=0; i<list.size(); i++) {
					System.out.println("element: " + list.get(i));
					System.out.println();
				}*/
				
				listToPayAttentionTo.addAll(list);
				debuglastK = k;
			}
			HashSet <BigInteger> listExpectedSet = new HashSet <BigInteger>();
			
			for(int i=0; i<listToPayAttentionTo.size(); i++) {
				listExpectedSet.add(listToPayAttentionTo.get(i));
				
			}
			
			if(debuglastK > 0) {
				System.out.println("debuglastK: " + debuglastK);
				if(debuglastK > 1000000) {
					System.out.println("Are you sure?");
					System.out.println();
					System.out.println();
					System.out.println("maxDenom: " + maxDenom);

					System.out.println("vs:");
					System.out.println("k: " + debuglastK);
					System.out.println("targetDenom: " + targetDenom);
					
					//System.out.println("tmpMaxDenomFractionIfRestEqual: " + tmpMaxDenom);
					System.out.println("maxLastDenom: " + maxLastDenom);
				}
	
				
				Object arrayDebug[] = listExpectedSet.toArray();
				/*System.out.println("------");
				System.out.println("allowed values:");
				for(int i=0; i<arrayDebug.length; i++) {
					System.out.println("arrayDebug: " + arrayDebug[i]);
				}*/
			}
			
			if(listToPayAttentionTo.isEmpty()) {
				return foundSolution;
			}
			
			
			// target e/f
			// f = (d1 d2 d3 d4 ... dn-3)/q
			// TODO: idea: 
			// Let M = f * q
			// Do algo to find all numbers x between min and max and has gcd(f, x) = x
			// Should be faster... but harder.
			
			boolean expectedSolutionBefore = debugExpectedSol;
			
			//TODO: copy/paste code except for one if condition.
			
			if(maxDenom > Math.pow(10, 6)) {
				System.out.println("Big num?");
			}
			for(long i=minDenom; i<=maxDenom; i++) {
				
				Fraction nextFraction = new Fraction(1, i);
				
	
				if(nextFraction.compareTo(target) > 0) {

					System.out.println("ERROR: nextFraction.compareTo(target) > 0");
					System.exit(1);
				}
				
				cur.add((long)i);
				
				if(listExpectedSet.contains(new BigInteger("" + i)) == false) {
					debugExpectedSol = false;
					
					//TODO: AHH!
					//System.out.println("Unexpected i: " + i);
					
				} else {
					debugExpectedSol = expectedSolutionBefore;
					if(expectedSolutionBefore) {
						//TODO: AH!
						//System.out.println("Expected i: " + i + " ( " + expectedSolutionBefore + ")");
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
				solve(i + 1, numTerms - 1, newTarget, cur, debugExpectedSol);
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