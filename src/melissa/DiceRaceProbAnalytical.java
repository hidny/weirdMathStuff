package melissa;

import eulerBook.Fraction;

/*
 * Question 3. (20 marks) Write a simple press-your-luck dice game. The game works as follows:
 Player 1 starts with a score of zero and rolls two dice.
o If the player rolls 7, 2, or 12 their turn is over and they lose.
o If the player rolls any other sum it is added to their total score.
o The player can stop rolling at any time – remember if their last roll was a
7, 2 or 12 they lose and Player 2 wins.
 After Player 1 stops rolling, Player 2 rolls the two dice
o Player 2 continues to roll the dice until they roll a 7, 2 or 12 (Player 2
loses) or they have a total higher than Player 1 (Player 2 wins).
Your solution should make use of methods with the following headers:

Q: Player 1s optimal strat:

 public static int rollDie()
o return a random valid die value between 1 and 6
 public static int newScore(int oldScore, int rollValue)
o determine if the current roll should be added to the score. If the
player rolled 7, 2 or 12 return -1 to show that the player just lost.
Otherwise return the updated score.
 public static int whoGoesFirst()
o randomly return 1 if player 1 goes first or 2 if player 2 goes first
 public static String winner(int playerNumber)
o print a message indicating who won the game
 public static char keepRolling()
o A method that prompts the player to ask if they want to keep rolling.
Return y or n as needed. Note: Player 2 ~could~ stop rolling if they
wanted to but if they haven’t achieved a higher score they will lose.
 main()
o this is your main method that coordinates all of the other methods as
necessary. Collect all input and produce all output from this method. 
 */
public class DiceRaceProbAnalytical {

	
	public static int N = 1000;
	public static int SIDES = 6;
	
	public static Fraction oddsP2WillGetMore[] = new Fraction[N];
	
	//odds of Losing = odds of getting 2, 7, or 12.
	public static Fraction oddsOfLosing = Fraction.plus(new Fraction(1, 18), new Fraction(1, 6));
	
	public static void main(String args[]) {
		System.out.println("Odds of Losing: " +  oddsOfLosing.getNumerator() + "/" + oddsOfLosing.getDenominator());
		for(int i=0; i<=SIDES*2; i++) {
			System.out.println(i + ": " + getOddsRoll2(i).getNumerator() + "/" + getOddsRoll2(i).getDenominator());
		}
		
		setupoddsP2Win();
		
		
		Fraction oddsP1WinByStaying;
		Fraction oddsP1WinByRolling;
		
		Fraction oddsOfWinningPlayer1[] = new Fraction[N];
		
		boolean player1Rolls[] = new boolean[N];
		
		
		for(int i=N-1; i>=0; i--) {
			//If P1 starts has i points, he has a decision to make:
			// roll or stay
			//odds of winning a stay is easy enough, but what about a roll?
			
			//STAY
			oddsP1WinByStaying = Fraction.minus(Fraction.ONE, oddsP2WillGetMore[i]);
			
			oddsP1WinByRolling = Fraction.ZERO;
			//ROLL
			for(int roll = 2; roll<= 2*SIDES; roll++) {
				if(roll ==2 || roll == 12 || roll == 7) {
					 //If the player rolled 7, 2 or 12 return -1 to show that the player just lost.
				} else if(i + roll < N) {
					oddsP1WinByRolling = Fraction.plus(oddsP1WinByRolling, Fraction.mult(oddsOfWinningPlayer1[i + roll], getOddsRoll2(roll)));
				} else {
					oddsP1WinByRolling = Fraction.plus(oddsP1WinByRolling, Fraction.mult(Fraction.ONE, getOddsRoll2(roll)));
				}
			}
			
			if(oddsP1WinByStaying.compareTo(oddsP1WinByRolling) > 0) {
				player1Rolls[i] = false;
				oddsOfWinningPlayer1[i] = oddsP1WinByStaying;
			} else {
				player1Rolls[i] = true;
				oddsOfWinningPlayer1[i] = oddsP1WinByRolling;
			}
		}
		
		for(int i=0; i<100; i++) {
			System.out.print(i + ": ");
			if(player1Rolls[i]) {
				System.out.print("(roll)");
			} else {
				System.out.print("(stay)");
			}
			System.out.println(" prob: " + oddsOfWinningPlayer1[i]);
		}
		/*Possible answer:
0: (roll) prob: 0.2896439857146048
1: (roll) prob: 0.29997194408367533
2: (roll) prob: 0.310677394816796
3: (roll) prob: 0.32207522652346754
4: (roll) prob: 0.33340710727263967
5: (roll) prob: 0.3459552900642208
6: (roll) prob: 0.35883075566297457
7: (roll) prob: 0.37120741507364186
8: (roll) prob: 0.38459089951103653
9: (roll) prob: 0.3978914425110916
10: (roll) prob: 0.411100522647389
11: (stay) prob: 0.4285074683737235
12: (stay) prob: 0.44178404799403886
13: (stay) prob: 0.45665728886179274
14: (stay) prob: 0.4779790936341005

Prob P1 winning is about 30%
and P1 should start staying at 11 points or more.
		 */
		
	}
	//99:0.025768264335165835
	//99:0.025768264335165835
	
	public static void setupoddsP2Win() {
		Fraction oddsP2WillgetThere[] = new Fraction[N];
		for(int i=0; i<N; i++) {
			oddsP2WillgetThere[i] = Fraction.ZERO;
		}
		//P2 will get to 0 because she starts at 0:
		oddsP2WillgetThere[0] = Fraction.ONE;
		
		for(int i= 0; i<N; i++) {
			//supposing P2 is at i, calc the odds P2 will go further:
			//roll 3
			for(int roll = 2; roll<= 2*SIDES; roll++) {
				if(roll ==2 || roll == 12 || roll == 7) {
					 //If the player rolled 7, 2 or 12 return -1 to show that the player just lost.
				} else if(i + roll < N) {
					oddsP2WillgetThere[i + roll] = Fraction.plus(oddsP2WillgetThere[i + roll], Fraction.mult(oddsP2WillgetThere[i], getOddsRoll2(roll)));
				}	
			}
		}
		
		Fraction oddsP2WillLoseThere[] = new Fraction[N];
		for(int i= 0; i<N; i++) {
			//supposing P2 is at i, calc the odds P2 will go further:
			//roll 3
			oddsP2WillLoseThere[i] = Fraction.mult(oddsP2WillgetThere[i], oddsOfLosing);
			
		}
		
		System.out.println("Odds P2 will lose there: " );
		for(int i=0; i<N; i++) {
			System.out.println(i + ":" + oddsP2WillLoseThere[i].getDecimalFormat(15));
		}
		
		
		for(int i=0; i<N; i++) {
			if( i == 0) {
				oddsP2WillGetMore[i] = Fraction.minus(Fraction.ONE, oddsP2WillLoseThere[i]);
			} else {
				oddsP2WillGetMore[i] = Fraction.minus(oddsP2WillGetMore[i - 1], oddsP2WillLoseThere[i]);
			}
			
		}
		System.out.println("Odds P2 will get more:");
		for(int i=0; i<N; i++) {
			System.out.println(i + ":" + oddsP2WillGetMore[i].getDecimalFormat(15));
		}
	}
	
	public static Fraction getOddsRoll2(int n) {
		Fraction ret = Fraction.ZERO;
		int j;
		for(int i=1; i<=SIDES; i++) {
			j = n - i;
			if( j >= 1 && j <= SIDES) {
				ret = Fraction.plus(ret, new Fraction(1, SIDES*SIDES));
			}
		}
		
		return ret;
	}
}
