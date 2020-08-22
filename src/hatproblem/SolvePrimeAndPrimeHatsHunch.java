package hatproblem;

//Main problem:
//https://www.youtube.com/watch?v=4YG4QnhVV7A
//Stacks of Hats (extra) - Numberphile

//Summary of 3 hat version
// 2 people have a stack of 3 hats on their heads and they can't see the colour of the hats on their head but they can 
// see each other's hats. (For now colour is blue or red)

// At some point, they have to simultaneously point to a blue hat on their head)

// If they had time to make an optimal plan, what's the probability that they both win

/*
 * 
prob both winning for N = 5:
0.3427734375
(351/1024)
 */

/*
 * 
Again: prob both winning for N = 5:
0.34765625
(89/256)
 */

import UtilityFunctions.Fraction;


public class SolvePrimeAndPrimeHatsHunch {
	
	public static int P = 5;
	
	public static boolean debug = false;
	
	//TODO
	//From paper, try:
	/*
	 * Strategy S2:
(1) If the first three hats of the other player are not all of the same color, play
the 3-hat strategy.
(2) If the first three hats of the other player are BBB or WWW, repeat S2 on
hats 4 through inf.
	 */
	//I bet I'll get 0.35
	
	public static void main(String args[]) {
		getBestKnownForN5ThirdTry();
	}
	

	//TODO: generalize
	public static void getBestKnownForN5ThirdTry() {
		P = 5;
		SolveNAndNHats.N = 5;
		
		if(UtilityFunctions.UtilityFunctions.isPrime(P) == false) {
			System.out.println("WARNING: N=" + P + " is not prime.");
		}
		
		int stratPlayer1[] = null;
		
		if(P==5) {
			stratPlayer1 = new int[]{
			0,
			1,
			2,
			2,
			0,
			0,
			0,
			0,
			3,
			3,
			2,
			2,
			3,
			3,
			3,
			3,
			4,
			1,
			2,
			2,
			0,
			0,
			0,
			0,
			3,
			3,
			2,
			2,
			3,
			3,
			3,
			3
			};
		}
		checkViability(stratPlayer1);
		
		Fraction probBothWinning = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
		
		for(int i=stratPlayer1.length/2; i<stratPlayer1.length; i++) {
			
			int temp = stratPlayer1[i];
			stratPlayer1[i] = 4;
			
			for(int j=i+1; j<stratPlayer1.length; j++) {
				int temp2 = stratPlayer1[j];
				stratPlayer1[j] = 4;
				
				for(int k=j+1; k<stratPlayer1.length; k++) {
					int temp3 = stratPlayer1[k];
					stratPlayer1[k] = 4;
					Fraction current = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
					
					if(Fraction.minus(current, probBothWinning).greaterThan0()) {
						System.out.println("What's up 3!");
						System.out.println("i = " + i);
						
						
						probBothWinning = current;
					}
					stratPlayer1[k] = temp3;
				}
				stratPlayer1[j] = temp2;
				
			}
			
			stratPlayer1[i] = temp;
		}
		
		System.out.println("prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
		System.out.println("");
		System.out.println("Player 1 strat:");
		for(int i=0; i<stratPlayer1.length; i++) {
			System.out.println(i + ": " + stratPlayer1[i]);
		}
		System.out.println();
		

		System.out.println("Again: prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
	}
	
	
	/*
	 * 0.349609375
	(179/512)
	 */
	public static void getBestKnownForN5SecondTry() {
		P = 5;
		SolveNAndNHats.N = 5;
		
		if(UtilityFunctions.UtilityFunctions.isPrime(P) == false) {
			System.out.println("WARNING: N=" + P + " is not prime.");
		}
		
		int stratPlayer1[] = null;
		
		if(P==5) {
			stratPlayer1 = new int[]{
			0,
			1,
			2,
			2,
			0,
			0,
			0,
			0,
			3,
			3,
			2,
			2,
			3,
			3,
			3,
			3,
			4,
			1,
			2,
			2,
			0,
			0,
			0,
			0,
			3,
			3,
			2,
			2,
			3,
			3,
			3,
			3
			};
		}
		checkViability(stratPlayer1);
		
		Fraction probBothWinning = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
		
		for(int i=stratPlayer1.length/2; i<stratPlayer1.length; i++) {
			
			int temp = stratPlayer1[i];
			stratPlayer1[i] = 4;
			
			for(int j=i+1; j<stratPlayer1.length; j++) {
				int temp2 = stratPlayer1[j];
				stratPlayer1[j] = 4;
				
				Fraction current = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
				
				if(Fraction.minus(current, probBothWinning).greaterThan0()) {
					System.out.println("What's up 2!");
					System.out.println("i = " + i);
					
					
					probBothWinning = current;
				}
				stratPlayer1[j] = temp2;
				
			}
			
			stratPlayer1[i] = temp;
		}
		
		System.out.println("prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
		System.out.println("");
		System.out.println("Player 1 strat:");
		for(int i=0; i<stratPlayer1.length; i++) {
			System.out.println(i + ": " + stratPlayer1[i]);
		}
		System.out.println();
		

		System.out.println("Again: prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
	}
	
	/*
	 * 0.349609375
(179/512)

	 */
	public static void getBestKnownForN5() {
		P = 5;
		SolveNAndNHats.N = 5;
		
		if(UtilityFunctions.UtilityFunctions.isPrime(P) == false) {
			System.out.println("WARNING: N=" + P + " is not prime.");
		}
		
		int stratPlayer1[] = null;
		
		if(P==5) {
			
			//Took answer for N=4, doubled it and added 4 to the 32nd index:
			stratPlayer1 = new int[]{
			0,
			1,
			2,
			2,
			0,
			0,
			0,
			0,
			3,
			3,
			2,
			2,
			3,
			3,
			3,
			3,
			4,
			1,
			2,
			2,
			0,
			0,
			0,
			0,
			3,
			3,
			2,
			2,
			3,
			3,
			3,
			3
			};
		}
		checkViability(stratPlayer1);
		
		Fraction probBothWinning = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
		
		for(int i=stratPlayer1.length/2; i<stratPlayer1.length; i++) {
			
			int temp = stratPlayer1[i];
			stratPlayer1[i] = 4;
			
			Fraction current = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
			
			if(Fraction.minus(current, probBothWinning).greaterThan0()) {
				System.out.println("What's up!");
				System.out.println("i = " + i);
				
				
				probBothWinning = current;
			}
			
			stratPlayer1[i] = temp;
		}
		
		System.out.println("prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
		System.out.println("");
		System.out.println("Player 1 strat:");
		for(int i=0; i<stratPlayer1.length; i++) {
			System.out.println(i + ": " + stratPlayer1[i]);
		}
		System.out.println();
		

		System.out.println("Again: prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
	}
	
	public static void crazyStrats() {
		SolveNAndNHats.N = P;
		
		if(UtilityFunctions.UtilityFunctions.isPrime(P) == false) {
			System.out.println("WARNING: N=" + P + " is not prime.");
		}

		int stratPlayer1[] = new int[(int)Math.pow(2, P)];
		
		if(P==5) {
			/*
			stratPlayer1 = new int[]{
					0, //00000
					0, //00001
					1, //00010
					0, //00011
					2, //00100
					0, //00101
					1, //00110
					0, //00111
					3, //01000
					3, //01001
					1, //01010
					0, //01011
					2, //01100
					2, //01101
					1, //01110
					0, //01111
					4, //10000
					4, //10001
					4, //10010
					4, //10011
					2, //10100
					4, //10101
					1, //10110
					4, //10111
					3, //11000
					3, //11001
					3, //11010
					3, //11011
					2, //11100
					2, //11101
					1, //11110
					0};//11111
					*/
			//stratPlayer1 = prepStrat5();
		} else if(P == 7) {
			//stratPlayer1 = prepStrat7();
		}
		
		if(debug) {
			for(int num=0; num<P; num++) {
				System.out.println("Choose index " + num + ":");
				for(int i=0; i<stratPlayer1.length ; i++) {
					if(stratPlayer1[i] == num) {
						boolean array[] = BoolUtilFuntions.convertNumToBoolArray(i, P);
						for(int j=0; j<array.length; j++) {
							if(array[array.length - j - 1]) {
								System.out.print(1);
							} else{
								System.out.print(0);
							}
						}
						System.out.println();
					}
				}
				System.out.println();
			}
		}
			

		Fraction probBothWinning = Fraction.ZERO;
		
		if(P == 5) {
			int bestStrat[] = null;
			
			for(int num=0; true; num++) {
				System.out.println("num: " + num);
				stratPlayer1 = prepStrat5(num);
				
				if(stratPlayer1 == null) {
					break;
				}
				
				Fraction probBothWinningCurrent = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
				
				if(Fraction.minus(probBothWinningCurrent, probBothWinning).greaterThan0()) {
					probBothWinning = probBothWinningCurrent;
					bestStrat = prepStrat5(num);
				}
			}
			
			
			stratPlayer1 = bestStrat;
			
		} else if(P == 7) {
			int bestStrat[] = null;
			
			for(int num=0; true; num++) {
				System.out.println("num: " + num);
				stratPlayer1 = prepStrat7(num);
				
				if(stratPlayer1 == null) {
					break;
				}
				
				Fraction probBothWinningCurrent = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
				
				if(Fraction.minus(probBothWinningCurrent, probBothWinning).greaterThan0()) {
					probBothWinning = probBothWinningCurrent;
					bestStrat = prepStrat7(num);
				}
			}
			
			
			stratPlayer1 = bestStrat;
			
		} else {
			probBothWinning = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
		}
		
		//TODO: results function
		System.out.println("prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
		System.out.println("");
		System.out.println("Player 1 strat:");
		for(int i=0; i<stratPlayer1.length; i++) {
			System.out.println(i + ": " + stratPlayer1[i]);
		}
		System.out.println();
		

		System.out.println("Again: prob both winning for N = " + P +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		//END TODO RESULTS FUNCTION
		
	}
	
	public static int getNumTrueInArray(boolean array[]) {
		int ret = 0;
		for(int i=0; i<array.length; i++) {
			if(array[i]) {
				ret++;
			}
		}
		return ret;
	}
	
	
	
	
	public static int[] prepStrat5(int num) {
		boolean otherZeroStrats[][] = new boolean[][] {
			{false, false, false, false, true},//1
			
			{false, false, false, true, true},//2
			{false, false, true, false, true},
			

			{false, false, true, true, true},
			{false, true, true, false, true},

			{false, true, true, true, true}
		};
		
		int MULT = 5;

		for(int i=1; i<otherZeroStrats.length; i++) {
			
			otherZeroStrats[i] = rotateArray(otherZeroStrats[i], num % MULT);
			
			num /= MULT;
		}
		
		if(num > 1) {
			return null;
		}
		
		
		return createAndTestStrat(otherZeroStrats, false);
	}
	
	public static int[] prepStrat7(int num) {
		
		
		boolean otherZeroStrats[][] = new boolean[][] {
			{false, false, false, false, false, false, true},//1
			
			{false, false, false, false, false, true, true},//2
			{false, false, false, false, true, false, true},
			{false, false, false, true, false, false, true},
			
			{false, false, false, false, true, true, true},//3
			{false, false, false, true, false, true, true},
			{false, false, true, false, false, true, true},
			{false, false, false, true, true, false, true},
			{false, false, true, false, true, false, true},
			
			{false, false, false, true, true, true, true},//4
			{false, false, true, false, true, true, true},
			{false, false, true, true, true, false, true},
			{false, false, true, true, false, true, true},
			{false, true, false, true, false, true, true},

			{false, false, true, true, true, true, true},//5
			{false, true, false, true, true, true, true},
			{false, true, true, false, true, true, true},
	
			{false, true, true, true, true, true, true},//6
		};
		
		
		//TODO: improve:

		for(int i=1; i<otherZeroStrats.length; i++) {
			
			int amountToMove = num % i;
			int rotation = 0;
			if(amountToMove != 0) {

				rotation = 0;

			} else {
				int indexesFound = 1;
				
				for(int j=0; j<otherZeroStrats[i].length - 1; j++) {
					if(otherZeroStrats[i][j]) {
						if(indexesFound == amountToMove) {
							rotation = j+1;
							break;
						} else {
							indexesFound++;
						}
					}
				}

			}
			
			otherZeroStrats[i] = rotateArray(otherZeroStrats[i], rotation);
			
			num /= i;
		}
		
		if(num > 1) {
			return null;
		}
		
		return createAndTestStrat(otherZeroStrats, false);
	}
	
	public static int[] createAndTestStrat(boolean nonTrivialZeroStrats[][]) {
		return createAndTestStrat(nonTrivialZeroStrats, true);
	}
	
	public static int[] createAndTestStrat(boolean nonTrivialZeroStrats[][], boolean debug) {

		int stratPlayer1[] = new int[(int)Math.pow(2, P)];
		stratPlayer1[0] = 0;
		stratPlayer1[stratPlayer1.length - 1] = 0;
		
		
		int balanceCounter[] = new int[P];
		
		for(int i=0; i<balanceCounter.length; i++) {
			balanceCounter[i] = 0;
		}
		
		for(int i=0; i<nonTrivialZeroStrats.length; i++) {
			for(int j=0; j<nonTrivialZeroStrats[i].length; j++) {
				if(nonTrivialZeroStrats[i][j]) {
					balanceCounter[j]++;
				}
			}
		}
		if(debug) {
			System.out.println("Balance counter: ");
			for(int i=0; i<balanceCounter.length; i++) {
				System.out.println(i + ": " + balanceCounter[i]);
			}
			System.out.println("End balance counter");
			System.out.println();
		}
		
		for(int i=0; i<nonTrivialZeroStrats.length; i++) {
			for(int j=i+1; j<nonTrivialZeroStrats.length; j++) {
				for(int rot = 0; rot < P; rot++) {
					if(BoolUtilFuntions.convertToNumber(nonTrivialZeroStrats[i]) == BoolUtilFuntions.convertToNumber(rotateArray(nonTrivialZeroStrats[j], rot))) {
						System.out.println("ERROR: repeat entry for number: " + BoolUtilFuntions.convertToNumber(nonTrivialZeroStrats[i]));
						System.out.println("i = " + i + " and j = " + j);
						System.exit(1);
					}
				}
			}
		}
		
		boolean touched[] = new boolean[(int)Math.pow(2, P)];
		for(int i=0; i<touched.length; i++) {
			touched[i] = false;
		}
		
		//Set the one that are already true:
		touched[0] = true;
		touched[touched.length - 1] = true;

		for(int strat = 0; strat < P; strat++) {
			for(int i=0; i<nonTrivialZeroStrats.length; i++) {
				
				int indexToChange = BoolUtilFuntions.convertToNumber(rotateArray(nonTrivialZeroStrats[i], strat));
				
				touched[indexToChange] = true;
				
				stratPlayer1[indexToChange] = strat;
				
				if(BoolUtilFuntions.convertNumToBoolArray(indexToChange, P)[strat] == false) {
					
					if(debug) {
						System.out.println("WARN: strat doesn't give away a hint");
						System.exit(1);
					}
				}
			}
		}
		
		
		for(int i=0; i<touched.length; i++) {
			if(touched[i] == false) {
				System.out.println("ERROR: not all indexes were set");
				System.out.println(i);
				System.exit(1);
			}
			
			
			if(debug) {
				System.out.println(i + ": " + stratPlayer1[i]);
			}
		}
		
		
		return stratPlayer1;
	}
	
	public static boolean[] rotateArray(boolean array[], int rot) {
		boolean newArray[] = new boolean[array.length];
		
		for(int i=0; i<newArray.length; i++) {
			newArray[i] = array[(i+rot) % array.length];
		}
		
		return newArray;
	}
	

	
	
	public static void checkViability(int strat[]) {
		
		int histo[] = new int[P];
		for(int i=0; i<strat.length; i++) {
			histo[strat[i]]++;
		}
		
		System.out.println("Histogram:");
		for(int i=0; i<histo.length; i++) {
			System.out.println("strat " + i + " used: " + histo[i]);
		}
	}
	
	
}
