package millionDollarPuzzle;

public class MillionDollarPuzzleSolve {

	public static int fib[] = new int[50];
	
	//9007199254740991
	
	
	//9007199254740991
	public static void main(String[] args) {

		int TARGET = 1000000;
		
		for(int i=0; i<fib.length; i++) {
			fib[i] = -1;
		}
		for(int i=0; i<fib.length; i++) {
			fib[i] = getFibonnaci(i);
		}
		int lastFibIndexUsed = 0;
		for(lastFibIndexUsed = 0; fib[lastFibIndexUsed] < TARGET; lastFibIndexUsed++) {
			
		}
		lastFibIndexUsed--;
		System.out.println(fib[lastFibIndexUsed]);
		//832040
		
		int a = -1;
		int b = -1;
		
		FOUND_SOLUTION:
		while(lastFibIndexUsed > 0) {
			System.out.println("Trying " + fib[lastFibIndexUsed]);
			
			b = 0;
			for(a = 0; a*fib[lastFibIndexUsed] <= TARGET; a++) {
				for(b = 0; a*fib[lastFibIndexUsed] + b * fib[lastFibIndexUsed - 1] <= TARGET; b++) {
					
					if(a*fib[lastFibIndexUsed] + b * fib[lastFibIndexUsed - 1] == TARGET) {
						break FOUND_SOLUTION;
					}
				}
				
			}
			
			lastFibIndexUsed--;
			
		}
		
		System.out.println( a + " * " + fib[lastFibIndexUsed] + " + " + b + " * " + fib[lastFibIndexUsed - 1] + " = " + TARGET);
		
		if(hitsTarget(a, b, TARGET) ) {
			System.out.println("FOUND IT!");
			System.out.println();
		}
		
		//day1: 154
		//day2: 144
		
		
		int daynMINUS2 = a;
		int daynMINUS1 = b + a;
		int dayn = daynMINUS1 + daynMINUS2;
		for(int i=0; i<20; i++) {
			System.out.println( daynMINUS2 + " + " + daynMINUS1 + " = " + dayn);
			
			if(dayn == TARGET) {
				break;
			}
			
			daynMINUS2 = daynMINUS1;
			daynMINUS1 = dayn;
			dayn = daynMINUS1 + daynMINUS2;
			
			
		}
		
		System.out.println("hits target:");
		hitsTarget(1, 5, 20);
		System.out.println("Trial2");
		hitsTarget(144, 154, 1000000);
		
	}


	public static int getFibonnaci(int n) {
		if(fib[n] != -1) {
			return fib[n];
		}
		
		if(n == 0) {
			return 0;
		} else if(n == 1) {
			return 1;
		} else {
			return getFibonnaci(n-1) + getFibonnaci(n-2);
		}
	}
	
	public static boolean hitsTarget(long day1, long day2, long TARGET) {
		
		long daynMinus2 = day1;
		long daynMinus1 = (day2 + day1);
		long dayn = daynMinus1 + daynMinus2;
		System.out.println(dayn);
		
		while(dayn < TARGET) {
			daynMinus2 = daynMinus1;
			daynMinus1 = dayn;
			dayn = daynMinus1 + daynMinus2;
			System.out.println(dayn);
		}
		
		if(dayn == TARGET) {
			return true;
		} else {
			return false;
		}
		
		
	}
}
