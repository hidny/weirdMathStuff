package erdos295;

import java.math.BigInteger;

import UtilityFunctions.Fraction;

public class prob295FirstDraft {

	//https://oeis.org/A192881
	
	
	public static void main(String args[]) {
		
		
		int a[] = new int[] {2, 3};
		
		Fraction sum = getSum(a);
		
		Fraction remainder = Fraction.minus(Fraction.ONE, sum);
		
		if(oneMoreToOne(remainder) >= 0) {
			System.out.println("last fraction = 1/" + oneMoreToOne(remainder));
		}
		
		System.out.println(sum.printNumDen());
		

		int b[] = new int[] {2};
		sum = getSum(b);
		
		remainder = Fraction.minus(Fraction.ONE, sum);
		
		if(twoMoreToOne(remainder, -1, -1) != null) {
			System.out.println("last 2 fractions = ");
			
			int array[] = twoMoreToOne(remainder, -1, -1);
			
			System.out.println("1/" + array[0] + " +  1/ " + array[1]);
			
		}
		
		b = new int[] {3, 4, 5};
		sum = getSum(b);
		
		remainder = Fraction.minus(Fraction.ONE, sum);
		
		if(twoMoreToOne(remainder, -1, -1) != null) {
			System.out.println("last 2 fractions of (3, 4, 5) = ");
			
			int array[] = twoMoreToOne(remainder, 6, -1);
			
			System.out.println("1/" + array[0] + " +  1/ " + array[1]);
			
		}
	}
	
	
	public static Fraction getSum(int a[]) {
		
		
		Fraction cur = Fraction.ZERO;
		
		for(int i=0; i<a.length; i++) {
			cur = Fraction.plus(cur, new Fraction(1, a[i]));
		}
		
		return cur;
		
	}
	
	public static int oneMoreToOne(Fraction remainder) {
		
		if(remainder.compareTo(Fraction.ONE) > 0) {
			return -1;
		}
		int ret = -1;
		
		
		if(remainder.getNumerator().equals(BigInteger.ONE)) {
			ret = (int)remainder.getDenominator().longValue();
		}
		
		return ret;
	}
	
	
	public static Fraction TWO = new Fraction(2, 1);
	
	public static int[] twoMoreToOne(Fraction remainder, int minDeno, int maxDeno) {
		
		if(remainder.compareTo(Fraction.ONE) > 0) {
			return null;
		}
		
		long denom = remainder.getDenominator().longValue();
		
		long divs[] = UtilityFunctions.UtilityFunctions.getAllDivisors(denom);
		
		for(int k=0; k<divs.length; k++) {
			
			
			long aBase = divs[k];
			long bBase = denom / divs[k];
			
			
			System.out.println("A " + aBase + "    B " + bBase);
			
			TRIAL_AB:
			for(int aMult=1; true; aMult++) {
				
				long newA = aBase * aMult;
				
				Fraction check = new Fraction(1, newA);
				
				if(newA < minDeno) {
					continue;
				}
				if(Fraction.mult(check, TWO).compareTo(remainder) < 0) {
					break;
				}
				
				if(check.compareTo(remainder) >= 0) {
					continue;
				}
				
				for(int bMult=1; true; bMult++) {
					
					long newB = bBase * bMult;
					
					if(newA >= newB) {
						System.out.println("Doh");
						continue;
					}

					System.out.println("----------");
					System.out.println(remainder);
					System.out.println(newA);
					System.out.println(newB);
					
					Fraction newFraction = new Fraction(newA + newB, newA*newB);
					
					if(newFraction.compareTo(remainder) < 0) {
						break;
						
					} else if(newFraction.compareTo(remainder) == 0) {
						return new int[] {(int)newA, (int)newB};
					}
				}
			}
		}
		
		
		return null;
	}
}
