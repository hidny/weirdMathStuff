package hatproblem;

//Main problem:

//https://www.youtube.com/watch?v=4YG4QnhVV7A
//Stacks of Hats (extra) - Numberphile

//Summary of 3 hat version
// 2 people have a stack of 3 hats on their heads and they can't see the colour of the hats on their head but they can 
// see each other's hats. (For now colour is blue or red)

// At some point, they have to simultaneously point to a blue hat on their head)

// If they had time to make an optimal plan, what's the probability that they both win

//Gah! I can't find anyone trying to solve the simplified problem!

//Found solution to simplified problem:
//From paper: //linked in https://www.brand.site.co.il/riddles/201607a.html
// ---> https://arxiv.org/pdf/1407.4711.pdf
// ---> 2.3. Constructing Strategies


// https://arxiv.org/pdf/2103.01541.pdf

import UtilityFunctions.Fraction;

/*
 * New optimal probability: 0.34765625
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
public class SolveNAndMHats {
	
	public static int PLAYER1_HAT_COUNT =4;
	public static int PLAYER2_HAT_COUNT = 4;

	
	// if p2 has 3 hats and p1 has 2
	// p1 has 2^(2^3) possible reactions = 2^8
	// p2 has 3^(2^2) possible reaction = 3^4
	
	//if p2 has 5 hats  and p1 has 2
	//p1` has 2^(2^5) possible reaction = 2^32
	//p2 has 5^(2^2) possible reactions = 5^4
	
	//try every strat for the person with the most hats
	
	public static int DEBUG_N = 2;

	
	
	public static void main(String[] args) {
		startFindingProbabilites();
		
		//countNumIterations();
		
	}
	
	//1, 8, 1094, 
	//Just to see how far to go...
	public static void countNumIterations() {
		int stratPlayer1[] = initStratPlayer1();
		int counter = 0;
		while(stratPlayer1 != null) {
			if(counter % 1000000 == 0) {
				System.out.println("Current counter: " + counter);
			}
			counter++;
			stratPlayer1 = orderedIncrement(stratPlayer1);
		}
		
		System.out.println("For player1HatCount = " + PLAYER1_HAT_COUNT + " and player2HatCount = " + PLAYER2_HAT_COUNT +", there are " + counter + " iterations to go thru.");
	}

	public static void startFindingProbabilites() {

		int stratPlayer1[] = initStratPlayer1();
		
		int counter = 0;
		
		Fraction best = new Fraction(0, 1);
		
		while(stratPlayer1 != null) {
		
			if(PLAYER1_HAT_COUNT <= DEBUG_N) {
				System.out.println(counter);
			} else if(counter % 100000 == 0) {
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
					System.out.println("=  " +  best.getNumerator() + "/" + best.getDenominator());
					System.out.println("Strat player1:");
					for(int i=0; i<stratPlayer1.length; i++) {
						System.out.println(i + ":" + stratPlayer1[i]);
					}
					System.out.println();
				}
			//}
			//TODO: move on to orderedIncrement later
			//stratPlayer1 = increment(stratPlayer1);
			stratPlayer1 = orderedIncrement(stratPlayer1);
		}
		
		System.out.println("Optimal probability: " + best.getDecimalFormat(10));
	}
	
	public static Fraction tryBestResponseToStrat(int stratPlayer1[]) {
		
		Fraction ODDS_OF_CURRECT_PLAYER1_CONFIG = new Fraction(1, (int)Math.pow(2, PLAYER1_HAT_COUNT));

		Fraction ODDS_OF_CURRECT_PLAYER2_CONFIG = new Fraction(1, (int)Math.pow(2, PLAYER2_HAT_COUNT));
		
		Fraction oddsOfBothWinningBasedOnPlayer1And2Hats[][] = new Fraction[(int)Math.pow(2, PLAYER1_HAT_COUNT)][PLAYER2_HAT_COUNT];
		
		for(int i=0; i<oddsOfBothWinningBasedOnPlayer1And2Hats.length; i++) {
			for(int k=0; k<PLAYER2_HAT_COUNT; k++) {
				oddsOfBothWinningBasedOnPlayer1And2Hats[i][k] = new Fraction(0, 1);
			}
		}
		
		
		for(int i=0; i<Math.pow(2, PLAYER1_HAT_COUNT); i++) {
			for(int j=0; j<Math.pow(2, PLAYER2_HAT_COUNT); j++) {
				boolean player1StackHats[] = convertNumToBoolArray(i, PLAYER1_HAT_COUNT);
				boolean player2StackHats[] = convertNumToBoolArray(j, PLAYER2_HAT_COUNT);
				
				int player1Choice = stratPlayer1[j];
				
				if(player1StackHats[player1Choice] == true) {
					//win:
					for(int k=0; k<PLAYER2_HAT_COUNT; k++) {
						if(player2StackHats[k] == true) {
							oddsOfBothWinningBasedOnPlayer1And2Hats[i][k] = Fraction.plus(oddsOfBothWinningBasedOnPlayer1And2Hats[i][k]
																		, ODDS_OF_CURRECT_PLAYER2_CONFIG);
						}
					}
					
				} else {
					//player 1 loses, player2 doesn't care about this case.
				}
			}
		}
		
		
		Fraction oddsPlayerBothWinning = new Fraction(0, 1);
		
		for(int i=0; i<Math.pow(2, PLAYER1_HAT_COUNT); i++) {
			
			Fraction currentBestProbPlayer2 = new Fraction(0, 1);
			
			for(int k=0; k<PLAYER2_HAT_COUNT; k++) {
				if(Fraction.minus(oddsOfBothWinningBasedOnPlayer1And2Hats[i][k], currentBestProbPlayer2)
						.greaterThan0()) {
					currentBestProbPlayer2 = oddsOfBothWinningBasedOnPlayer1And2Hats[i][k];
				}
			}
			
			oddsPlayerBothWinning = Fraction.plus(oddsPlayerBothWinning, Fraction.mult(currentBestProbPlayer2, ODDS_OF_CURRECT_PLAYER1_CONFIG));
		}
		
		if(PLAYER1_HAT_COUNT <= DEBUG_N) {
			System.out.println("Strat player1:");
			for(int i=0; i<stratPlayer1.length; i++) {
				System.out.println(i + ":" + stratPlayer1[i]);
			}
			System.out.println("Odds found for strat: " + oddsPlayerBothWinning.getDecimalFormat(10));
			
			System.out.println();
		}

		return oddsPlayerBothWinning;
	}
	
	
	public static int[] initStratPlayer1() {
		int stratPlayer1[] = new int[(int)Math.pow(2, PLAYER2_HAT_COUNT)];
		for(int i=0; i<stratPlayer1.length; i++) {
			stratPlayer1[i] = 0;
		}
		
		return stratPlayer1;
	}
	
	
	public static int[] orderedIncrement(int stratPlayer1[]) {
		
		while(true) {
			
			stratPlayer1 = increment(stratPlayer1);
			
			if(stratPlayer1 == null) {
				break;
			}
			
			boolean taken[] = new boolean[PLAYER1_HAT_COUNT];
			
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
				if(PLAYER1_HAT_COUNT <= DEBUG_N) {
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
			if(stratPlayer1[i] + 1 == PLAYER1_HAT_COUNT) {
				
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
	
	//returns boolean array in little endian format:
	public static boolean[] convertNumToBoolArray(int num, int size) {
		boolean ret[] = new boolean[size];
		
		for(int i= 0; i<ret.length; i++) {
			if(num % 2 == 1) {
				ret[i] = true;
			} else {
				ret[i] = false;
			}
			num /= 2;
		}
		
		return ret;
	}
}
