package hatproblem;

//Main problem:
//https://www.youtube.com/watch?v=4YG4QnhVV7A
//Stacks of Hats (extra) - Numberphile

//Summary of 3 hat version
// 2 people have a stack of 3 hats on their heads and they can't see the colour of the hats on their head but they can 
// see each other's hats. (For now colour is blue or red)

// At some point, they have to simultaneously point to a blue hat on their head)

// If they had time to make an optimal plan, what's the probability that they both win


import UtilityFunctions.Fraction;

//
//N = 1:
// 0.25 (1/4)

//N = 2:
//  0.3125 (5/16)

//N = 3
// 0.34375 (11/32)

//For N =3:
// There's only 6 "real" solutions (I bet it's it's just 1 real one because 3! = 6)
//TODO: investigate

/* N=4:
 * New optimal probability: 0.34765625 (89/256)
Strat player1:
0:0
1:1
2:2
3:1
4:3
5:3
6:3
7:3
8:0
9:1
10:0
11:1
12:0
13:0
14:0
15:0
 */
//The added probability from N=3 to N=4 is very unimpressive...
//I easily create a strat to improve the N=3 solution to get the N=4 solution...
//So, look into the N=3 solution



//TODO: for N=4, is the optimal strat ever symmetric?

public class SolveNAndNHats {
	
	public static int N = 3;

	public static int DEBUG_N = 2;

	//For n = 1, there are 1 iterations to go thru.
	//For n = 2, there are 8 iterations to go thru.
	//For n = 3, there are 1094 iterations to go thru.
	//For n = 4, there are 178973355 iterations to go thru.
	
	//178973355 has 6 other sequences
	
	//Maybe add to oeis:
	//Num ways to colour every number from 1 to 2^N such that there's N colours and the order of appearance of the first N colours is fixed.
	
	//There's got to be a formula for it...

	public static void main(String[] args) {
		startFindingProbabilites();
		
		//countNumIterations();
		
	}
	
	//1, 8, 1094, 
	//Just to see how far to go...
	public static void countNumIterations() {
		int stratPlayer1[] = initStratPlayer1(N);
		int counter = 0;
		while(stratPlayer1 != null) {
			if(counter % 1000000 == 0) {
				System.out.println("Current counter: " + counter);
			}
			counter++;
			stratPlayer1 = orderedIncrement(stratPlayer1);
		}
		
		System.out.println("For n = " + N + ", there are " + counter + " iterations to go thru.");
	}

	public static void startFindingProbabilites() {

		int stratPlayer1[] = initStratPlayer1(N);
		
		int counter = 0;
		
		Fraction best = new Fraction(0, 1);
		
		while(stratPlayer1 != null) {
		
			if(N <= DEBUG_N) {
				System.out.println(counter);
			} else if(counter % 1000000 == 0) {
				System.out.println("Counter: " + counter);
			}
			counter++;
			
			//TODO: just for testing:
			//if(N <= DEBUG_N) {
				Fraction current = tryBestResponseToStrat(stratPlayer1);
			
				if(Fraction.minus(current, best).greaterThan0()) {
					best = current;
					
					System.out.println("New optimal probability: " +  best.getDecimalFormat(10));
					System.out.println("Strat player1:");
					for(int i=0; i<stratPlayer1.length; i++) {
						System.out.println(i + ":" + stratPlayer1[i]);
					}
					System.out.println();
				} else if(Fraction.minus(current, best).equals(Fraction.ZERO)) {
					System.out.println("Alternative optimal strategy with probability: " +  best.getDecimalFormat(10));
					System.out.println("Strat player1:");
					for(int i=0; i<stratPlayer1.length; i++) {
						System.out.println(i + ":" + stratPlayer1[i]);
					}
					System.out.println();
				}
			//}
				
			//stratPlayer1 = increment(stratPlayer1);
			
			//Forces strategy to be "ordered"
			//stratPlayer1 = orderedIncrement(stratPlayer1);
				
			//Forces strategy 1 to be 0 (because strat 0 is already a loss, so it doesn't matter)
			stratPlayer1 = orderedIncrementDebug(stratPlayer1);
		}
		
		System.out.println("Optimal probability: " + best.getDecimalFormat(10));
	}
	
	public static Fraction tryBestResponseToStrat(int stratPlayer1[]) {
		
		Fraction ODDS_OF_CURRECT_PLAYER1_CONFIG = new Fraction(1, (int)Math.pow(2, N));
		
		Fraction oddsOfBothWinningBasedOnPlayer1And2Hats[][] = new Fraction[(int)Math.pow(2, N)][N];
		
		for(int i=0; i<oddsOfBothWinningBasedOnPlayer1And2Hats.length; i++) {
			for(int k=0; k<N; k++) {
				oddsOfBothWinningBasedOnPlayer1And2Hats[i][k] = new Fraction(0, 1);
			}
		}
		
		
		for(int i=0; i<Math.pow(2, N); i++) {
			for(int j=0; j<Math.pow(2, N); j++) {
				boolean player1StackHats[] = BoolUtilFuntions.convertNumToBoolArray(i, N);
				boolean player2StackHats[] = BoolUtilFuntions.convertNumToBoolArray(j, N);
				
				int player1Choice = stratPlayer1[j];
				
				if(player1StackHats[player1Choice] == true) {
					//win:
					for(int k=0; k<N; k++) {
						if(player2StackHats[k] == true) {
							oddsOfBothWinningBasedOnPlayer1And2Hats[i][k] = Fraction.plus(oddsOfBothWinningBasedOnPlayer1And2Hats[i][k]
																		, ODDS_OF_CURRECT_PLAYER1_CONFIG);
						}
					}
					
				} else {
					//player 1 loses, player2 doesn't care about this case.
				}
			}
		}
		
		
		Fraction oddsPlayerBothWinning = new Fraction(0, 1);
		
		for(int i=0; i<Math.pow(2, N); i++) {
			
			Fraction currentBestProbPlayer2 = new Fraction(0, 1);
			
			for(int k=0; k<N; k++) {
				if(Fraction.minus(oddsOfBothWinningBasedOnPlayer1And2Hats[i][k], currentBestProbPlayer2)
						.greaterThan0()) {
					currentBestProbPlayer2 = oddsOfBothWinningBasedOnPlayer1And2Hats[i][k];
				}
			}
			
			oddsPlayerBothWinning = Fraction.plus(oddsPlayerBothWinning, Fraction.mult(currentBestProbPlayer2, ODDS_OF_CURRECT_PLAYER1_CONFIG));
		}
		
		if(N <= DEBUG_N) {
			System.out.println("Strat player1:");
			for(int i=0; i<stratPlayer1.length; i++) {
				System.out.println(i + ":" + stratPlayer1[i]);
			}
			System.out.println("Odds found for strat: " + oddsPlayerBothWinning.getDecimalFormat(10));
			
			System.out.println();
		}

		return oddsPlayerBothWinning;
	}
	
	
	public static int[] initStratPlayer1(int  N) {
		int stratPlayer1[] = new int[(int)Math.pow(2, N)];
		for(int i=0; i<stratPlayer1.length; i++) {
			stratPlayer1[i] = 0;
		}
		
		return stratPlayer1;
	}
	
	public static int[] orderedIncrementDebug(int stratPlayer1[]) {
		while(true) {
			
			stratPlayer1 = orderedIncrement(stratPlayer1);
			if(stratPlayer1 == null || stratPlayer1[1] == 0) {
				break;
			}
		}
		
		return stratPlayer1;
	}
	
	public static int[] orderedIncrement(int stratPlayer1[]) {
		
		while(true) {
			
			stratPlayer1 = increment(stratPlayer1);
			
			if(stratPlayer1 == null) {
				break;
			}
			
			boolean taken[] = new boolean[N];
			
			for(int i=0; i<taken.length; i++) {
				taken[i] = false;
			}
			
			
			boolean startNotOrdered = false;
			
			for(int i=0; i<stratPlayer1.length && startNotOrdered == false; i++) {
				taken[stratPlayer1[i]] = true;
				
				for(int j=0; j<stratPlayer1[i] && startNotOrdered == false; j++) {
					if(taken[j] == false) {
						startNotOrdered = true;
					}
				}
			}
			
			if(startNotOrdered) {
				if(N <= DEBUG_N) {
					System.out.println("Skip unordered player 1 strategy");
				}
				continue;
			}
			
			break;
		}
		
		return stratPlayer1;
	}
	
	public static int[] increment(int stratPlayer1[]) {
		
		for(int i=0; i<stratPlayer1.length; i++) {
			if(stratPlayer1[i] + 1 == N) {
				
				if(i==stratPlayer1.length - 1) {
					//done:
					return null;
				}
				
				stratPlayer1[i] = 0;
				continue;
			} else {
				stratPlayer1[i]++;
				break;
			}
		}
		
		return stratPlayer1;
		
	}
	
	
}
