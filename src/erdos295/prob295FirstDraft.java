package erdos295;

import java.math.BigInteger;

import UtilityFunctions.Fraction;

public class prob295FirstDraft {

	//Reference:
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
		
		b = new int[] {3, 4};
		sum = getSum(b);
		
		remainder = Fraction.minus(Fraction.ONE, sum);
		
		if(threeMoreToOne(remainder, 5, -1) != null) {
			System.out.println("last 3 fractions of (3, 4) = ");
			
			int array[] = threeMoreToOne(remainder, 5, -1);
			
			System.out.println("1/" + array[0] + " +  1/ " + array[1] + " + 1/ " + array[2]);
			
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
	
	
	public static Fraction THREE = new Fraction(3, 1);
	
	public static int[] threeMoreToOne(Fraction remainder, int minDeno, int maxDeno) {
		
		if(remainder.compareTo(Fraction.ONE) > 0) {
			return null;
		}
		if(minDeno < 0) {
			System.exit(1);
			return null;
		}
		
		long denom = remainder.getDenominator().longValue();
		
		long divs[] = UtilityFunctions.UtilityFunctions.getAllDivisors(denom);
		
		for(int k=0; k<divs.length; k++) {
			
			
			long aBase = divs[k];
			
			long divs2[] =  UtilityFunctions.UtilityFunctions.getAllDivisors(denom / divs[k]);
			
			
			for(int k2=0; k2<divs2.length; k2++) {
				
				long bBase = divs2[k2];
				
				long cBase = (denom / aBase) / bBase;
				
				
				//System.out.println("A " + aBase + "    B " + bBase + "    C : "  + cBase);
				
				Fraction checkOverLimit;
				
				int minaMult = (int)(minDeno/aBase);
				
				if(minaMult * aBase < minDeno) {
					minaMult++;
				}
				
				for(long aMult=minaMult; true; aMult++) {
					
					//TODO: if gcd(newA, denom) > gcd(divs[k], denom), do it later...
					long newA = aBase * aMult;
					
					//System.out.println("New a: " + newA);
					checkOverLimit = new Fraction(1, newA);
					
					if(newA < minDeno) {
						continue;
					}
					if(Fraction.mult(checkOverLimit, THREE).compareTo(remainder) < 0) {
						break;
					}
					
					if(checkOverLimit.compareTo(remainder) >= 0) {
						continue;
					}
					
					Fraction remainderAfterA = Fraction.minus(remainder, new Fraction(1, newA));
					
					
					for(int bMult=1; true; bMult++) {

						//TODO: if gcd(newB, denom) > gcd(divs2[k2], denom), do it later...
						long newB = bBase * bMult;
						//System.out.println("New b: " + newB);
						
						if(newA >= newB) {//TODO: skip to start...
							continue;
						}
						checkOverLimit = new Fraction(1, newB);
						
						if(Fraction.mult(checkOverLimit, TWO).compareTo(remainderAfterA) < 0) {
							break;
						}
						
						if(checkOverLimit.compareTo(remainderAfterA) >= 0) {
							continue;
						}
						
						for(int cMult=1; true; cMult++) {

							
							long newC = cBase * cMult;
							//System.out.println("New c: " + newC);
							
							if(newB >= newC) {
								continue;
							}
							
							//System.out.println("----------");
							//System.out.println(remainder);
							//System.out.println(newA);
							//System.out.println(newB);
							//System.out.println(newC);
							
							
							Fraction newFraction = new Fraction(newA * newB + newA * newC + newB*newC, newA*newB*newC);
							
							if(newFraction.compareTo(remainder) < 0) {
								break;
								
							} else if(newFraction.compareTo(remainder) == 0) {
								return new int[] {(int)newA, (int)newB, (int)newC};
							}
						}
					}
				}
			}
		}
		
		
		return null;
	}
}

//16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 32, 33, 34, 36, 38, 39, 40, 42, 43, 44, 50, 52, 54

//Missing                           31          35  37         41           45 46 47 48 49 51 53

//AHA large primes are no-go...
// TODO: maybe optimize in such a way that there are too many large primes somehow?
//Maybe the denom of the remainder has to be small enough and it's obvious when it's too big?
