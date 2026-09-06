package erdos295;

import java.math.BigInteger;
import java.util.ArrayList;

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
	
	public static boolean solve(int minDenom, int numTerms, Fraction target, ArrayList<Long> cur, boolean debugExpectedSol) {
		
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
		
		//maxDenom if the value of the rest of the terms is equal to the current one:
		long maxDenom = (long)Math.ceil(tmpMaxDenomFractionIfRestEqual.getDecimalFormat()) + 1;
		
		//long maxDenomDEBUG = -1;
		//long minDenomDEBUG = -1;

		boolean gotMinFraction = false;
		
		
		if(numTerms == 2) {
			
			//TODO: This formula works, but it might be a bit naive... try to improve this on 4th trial.
			//Also, get stats on how much this section doesn't filter...
			Fraction minLast = Fraction.divide(new Fraction(target.getDenominator(), BigInteger.ONE), tmpMaxDenomFractionIfRestEqual);
			
			long possibleMinFor2ndLast = (long)Math.floor(Fraction.divide(Fraction.ONE, target).getDecimalFormat()) - 2;
			
			Fraction min2ndLast = new Fraction(1, possibleMinFor2ndLast);
			
			while(min2ndLast.compareTo(target) > 0) {
				possibleMinFor2ndLast++;
				min2ndLast = new Fraction(1, possibleMinFor2ndLast);
			}
			

			Fraction min2ndLastPlusOne = new Fraction(1, possibleMinFor2ndLast + 1);
			
			//Check if the second last and last can't do it:
			if(Fraction.plus(Fraction.divide(Fraction.ONE, minLast), min2ndLast).compareTo(target) < 0) {

				//debugExpectedSol = false;
				return foundSolution;
			}

			//Check if there's only one way to do second last and last:
			if(Fraction.plus(Fraction.divide(Fraction.ONE, minLast), min2ndLastPlusOne).compareTo(target) < 0) {

				
				//System.out.println("Test setup:");
				//System.out.println("Min 2nd one: " + possibleMin);
				//System.out.println("Min min2ndLastPlusOne: " + min2ndLastPlusOne.getDecimalFormat() );
				//System.out.println("Min last: " + minLast.getDecimalFormat() );
				//System.out.println("Min minDenom: " + minDenom );
				
				//maxDenom = minDenom;
				
				
				
				//TEST:
				//minDenomDEBUG = Math.max(minDenom, possibleMinFor2ndLast);
				//maxDenomDEBUG = possibleMinFor2ndLast;
				
				//REAL:
				minDenom = (int)Math.max(minDenom, possibleMinFor2ndLast);
				maxDenom = possibleMinFor2ndLast;
			}
			
		}
		
		for(int i=minDenom; i<=maxDenom; i++) {
			
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
