package millionDollarPuzzle;

import java.math.BigInteger;

public class sillyNegativeSolve {

	//Limit: 9007199254740991
	//  9 007 199 254 740 991

	//       39088169000000
	
	//TODO what if you add one
	//public static BigInteger LIMIT = new BigInteger("9007199254740991");
	
	//TODO add one
	public static BigInteger LIMIT = new BigInteger("9007199254740992");
/*
 * 
Answer: 
-4727250767562882
7648852415260772


 */
	
	public static BigInteger fib[] = new BigInteger[200];
	
	//9007199254740991
	
	
	//9007199254740991
	public static void main(String[] args) {

		System.out.println("TESTING!");
		for(int i=0; i<fib.length; i++) {
			fib[i] = null;
		}
		for(int i=0; i<fib.length; i++) {
			fib[i] = getFibonnaci(i);
			//System.out.println(fib[i]);
		}
		
		
		long TARGET = 1000000;
		BigInteger TARGET_BIG_INTEGER = new BigInteger("" + TARGET);
		BigInteger MINUS_ONE = new BigInteger("-1");
		
		int countWarning = 0;
		

		BigInteger lastBestDay1 = null;
		BigInteger lastBestDay2 = null;
		
		for(int i=5; countWarning < 10; i++) {
			
			
			BigInteger day1 = fib[i].multiply(TARGET_BIG_INTEGER);
			BigInteger day2 = fib[i+1].multiply(TARGET_BIG_INTEGER);

			System.out.println("Trying to hit target " + TARGET_BIG_INTEGER + " where i = " + i);
			
			if(i % 2 == 0) {
				day1 = day1.multiply(MINUS_ONE);
				System.out.println("day1  " + day1);
				System.out.println("day2   " + day2);
			} else {
				day2 = day2.multiply(MINUS_ONE);
				System.out.println("day1   " + day1);
				System.out.println("day2  " + day2);
			}
			
			System.out.println("limit  " + LIMIT);

			//hitsTarget(day1.longValue(), day2.longValue(), TARGET);
			
			if(day2.compareTo(BigInteger.ZERO) > 0) {
				while(day2.compareTo(LIMIT) > 0) {
					day1 = day1.add(fib[i-1]);
					day2 = day2.subtract(fib[i]);
				}

				if(day2.compareTo(LIMIT.multiply(MINUS_ONE)) < 0) {
					System.out.println("WARNING: OVER SHOOT!");
				}
				
			} else if(day2.compareTo(BigInteger.ZERO) < 0) {
				while(day2.compareTo(LIMIT.multiply(MINUS_ONE)) < 0) {
					day1 = day1.subtract(fib[i-1]);
					day2 = day2.add(fib[i]);
				}
				
				if(day2.compareTo(LIMIT) > 0) {
					System.out.println("WARNING: OVER SHOOT!");
				}
			}
			

			if(day1.abs().compareTo(LIMIT) > 0) {
				System.out.println("WARNING: Day 1 not within range!");
				countWarning++;
				//break;
			} else {
				if(day2.abs().compareTo(LIMIT) > 0) {
					System.out.println("WARNING: Day 2 not within range!");
					countWarning++;
					//break;
				} else {
					System.out.println("Looks good...");

					lastBestDay1 = day1;
					lastBestDay2 = day2;
				}
			}
			
			System.out.println("day1 factor " + fib[i-1]);
			System.out.println("day2 factor " + fib[i]);
			System.out.println("-----");

			System.out.println("day1 adjusted  " + day1);
			System.out.println("day2 adjusted  " + day2);
			System.out.println("limit          " + LIMIT);
			
			
			hitsTarget(day1.longValue(), day2.longValue(), TARGET);
			System.out.println();
			
			
		}
		
		//hitsTarget(-4807526976000000L, 7778742049000000L, TARGET);
		
		System.out.println();
		System.out.println("Answer: ");
		System.out.println(lastBestDay1);
		System.out.println(lastBestDay2);
		
	}
	
	//TODO:
	//Try to beat:
	//EZ...if x*Fn- y*Fn= TARGET
	//Then (x + cFn+1)Fn-(y+cFn)Fn+1=TARGET
	
	//WILL TRY TOMORROW!
	/*
	Trying to hit target 1 where i = 48
day1  -4807526976000000
day2   7778742049000000
limit  9007199254740991
-4807526976000000   0
2971215073000000   1
-1836311903000000   2
1134903170000000   3
-701408733000000   4
433494437000000   5
-267914296000000   6
165580141000000   7
-102334155000000   8
63245986000000   9
-39088169000000   10
24157817000000   11
-14930352000000   12
9227465000000   13
-5702887000000   14
3524578000000   15
-2178309000000   16
1346269000000   17
-832040000000   18
514229000000   19
-317811000000   20
196418000000   21
-121393000000   22
75025000000   23
-46368000000   24
28657000000   25
-17711000000   26
10946000000   27
-6765000000   28
4181000000   29
-2584000000   30
1597000000   31
-987000000   32
610000000   33
-377000000   34
233000000   35
-144000000   36
89000000   37
-55000000   38
34000000   39
-21000000   40
13000000   41
-8000000   42
5000000   43
-3000000   44
2000000   45
-1000000   46
1000000   47

i.e
last day number: 1000000   last day:47
	*/

	/*
	 * 
day1   20365011074000000
day2  -32951280099000000
limit  9007199254740991
last day number: 1000000   last day:50

	 */
	public static BigInteger getFibonnaci(int n) {
		
		if(n == 0) {
			return BigInteger.ZERO;
		} else if(n == 1) {
			return BigInteger.ONE;
		} else {
			return fib[n-1].add(fib[n-2]);
		}
	}
	

	public static boolean hitsTarget(long day1, long day2, long TARGET) {

		int count = 1;
		
		long daynMinus2 = day1;
		System.out.println(daynMinus2 + "   " + count);
		
		count++;
		long daynMinus1 = (day2 + day1);
		System.out.println(daynMinus1 + "   " + count);
		count++;
		
		long dayn = daynMinus1 + daynMinus2;
		System.out.println(dayn + "   " + count);
		
		
		while(dayn < Long.MAX_VALUE / 2) {
			daynMinus2 = daynMinus1;
			daynMinus1 = dayn;
			dayn = daynMinus1 + daynMinus2;

			count++;
			
			System.out.println(dayn + "   " + count);

			if(dayn == TARGET) {
				break;
			}
		}
		
		System.out.println("last day number: " + dayn + "   last day:" + count);
		
		if(dayn == TARGET) {
			return true;
		} else {
			return false;
		}
		
		
	}
}
	