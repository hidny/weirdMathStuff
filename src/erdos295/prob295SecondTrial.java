package erdos295;

import java.math.BigInteger;
import java.util.ArrayList;

import UtilityFunctions.Fraction;

public class prob295SecondTrial {


	//Made it to:
	//Search minDenom: 5 and numTerms: 10
	//but then stalled...
	
	public static void main(String args[]) {
		
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
		solve(minDenom, numTerms, Fraction.ONE, new ArrayList<Long>());
		
		return foundSolution;
		
	}

	public static boolean foundSolution = false;
	
	public static long debug = 0;
	
	public static boolean solve(int minDenom, int numTerms, Fraction target, ArrayList<Long> cur) {
		
		if(debug % 100000000 == 0) {
			printCur(cur);
		}
		debug++;
		if(numTerms == 1) {
			if(target.getNumerator().equals(BigInteger.ONE)) {
				cur.add(target.getDenominator().longValue());
				
				if(target.getDenominator().longValue() >= minDenom) {
					String ret = "";
					for(int i=0; i<cur.size(); i++) {
						if(i < cur.size() - 1) {
						ret += "" + cur.get(i) + "^-1 +  ";
						} else {
							ret += "" + cur.get(i) + "^-1";
						}
						
					}
					ret += "\n";
					System.out.println("Solution: " + ret);
					foundSolution = true;
				}
				cur.remove(cur.size() - 1);
			}
			return foundSolution;
		}
		
		for(int i=minDenom; true; i++) {
			
			Fraction nextFraction = new Fraction(1, i);
			
			if(Fraction.mult(nextFraction, array[numTerms]).compareTo(target) < 0) {
				break;
			}

			if(nextFraction.compareTo(target) > 0) {
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
			
			solve(i+1, numTerms - 1, newTarget, cur);
			
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
