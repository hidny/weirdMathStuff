package melissa;


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
public class DiceRaceProbAnalytical2 {

	
	public static int N = 10000;
	public static int SIDES = 6;
	
	public static double oddsP2WillGetMore[] = new double[N];
	
	//odds of Losing = odds of getting 2, 7, or 12.
	public static double oddsOfLosing = 1.0/36.0 + 1.0/6.0 + 1.0/36.0;
	
	public static void main(String args[]) {
		
		setupoddsP2Win();
		
		
		double oddsP1WinByStaying;
		double oddsP1WinByRolling;
		
		double oddsOfWinningPlayer1[] = new double[N];
		
		boolean player1Rolls[] = new boolean[N];
		
		
		for(int i=N-1; i>=0; i--) {
			//If P1 starts has i points, he has a decision to make:
			// roll or stay
			//odds of winning a stay is easy enough, but what about a roll?
			
			//STAY
			oddsP1WinByStaying = 1.0 - oddsP2WillGetMore[i];
			
			oddsP1WinByRolling = 0.0;
			//ROLL
			for(int roll = 2; roll<= 2*SIDES; roll++) {
				if(roll ==2 || roll == 12 || roll == 7) {
					 //If the player rolled 7, 2 or 12 return -1 to show that the player just lost.
				} else if(i + roll < N) {
					oddsP1WinByRolling += oddsOfWinningPlayer1[i + roll] * getOddsRoll2(roll);
				} else {
					oddsP1WinByRolling += getOddsRoll2(roll);
				}
			}
			
			if(oddsP1WinByStaying > oddsP1WinByRolling) {
				player1Rolls[i] = false;
				oddsOfWinningPlayer1[i] = oddsP1WinByStaying;
			} else {
				player1Rolls[i] = true;
				oddsOfWinningPlayer1[i] = oddsP1WinByRolling;
			}
		}
		
		for(int i=0; i<20; i++) {
			System.out.print(i + ": ");
			if(player1Rolls[i]) {
				System.out.print("(roll)");
			} else {
				System.out.print("(stay)");
			}
			System.out.println(" prob: " + oddsOfWinningPlayer1[i]);
		}
		
		System.out.println("ANSWER:");
		System.out.println("Prob player 1 wins: " + oddsOfWinningPlayer1[0]);
		
		for(int i=0; i<N; i++) {
			if(player1Rolls[i] == false) {
				System.out.println("Player 1 should stop rolling once his sum is at least " + i + ".");
				break;
			}
		}
		/*
		 * 
0: (roll) prob: 0.28964398571460476
1: (roll) prob: 0.2999719440836752
2: (roll) prob: 0.3106773948167959
3: (roll) prob: 0.3220752265234675
4: (roll) prob: 0.33340710727263956
5: (roll) prob: 0.34595529006422077
6: (roll) prob: 0.3588307556629745
7: (roll) prob: 0.3712074150736418
8: (roll) prob: 0.3845908995110364
9: (roll) prob: 0.3978914425110915
10: (roll) prob: 0.41110052264738894
11: (stay) prob: 0.42850746837372344
		 */
/*
Prob P1 winning is about 29%
and P1 should start staying at 11 points or more.
		 */
		
	}
	
	public static void setupoddsP2Win() {
		double oddsP2WillgetThere[] = new double[N];
		for(int i=0; i<N; i++) {
			oddsP2WillgetThere[i] = 0.0;
		}
		//P2 will get to 0 because she starts at 0:
		oddsP2WillgetThere[0] = 1.0;
		
		for(int i= 0; i<N; i++) {
			//supposing P2 is at i, calc the odds P2 will go further:
			//roll 3
			for(int roll = 2; roll<= 2*SIDES; roll++) {
				if(roll ==2 || roll == 12 || roll == 7) {
					 //If the player rolled 7, 2 or 12 return -1 to show that the player just lost.
				} else if(i + roll < N) {
					oddsP2WillgetThere[i + roll] += oddsP2WillgetThere[i] * getOddsRoll2(roll);
				}	
			}
		}
		
		double oddsP2WillLoseThere[] = new double[N];
		for(int i= 0; i<N; i++) {
			//supposing P2 is at i, calc the odds P2 will go further:
			//roll 3
			oddsP2WillLoseThere[i] =oddsP2WillgetThere[i] * oddsOfLosing;
			
		}
		
		
		for(int i=0; i<N; i++) {
			if( i == 0) {
				oddsP2WillGetMore[i] = 1.0 - oddsP2WillLoseThere[i];
			} else {
				oddsP2WillGetMore[i] = oddsP2WillGetMore[i - 1] - oddsP2WillLoseThere[i];
			}
			
		}
		
	}
	
	public static double getOddsRoll2(int n) {
		double ret = 0.0;
		int j;
		for(int i=1; i<=SIDES; i++) {
			j = n - i;
			if( j >= 1 && j <= SIDES) {
				ret += (1.0/(1.0*SIDES*SIDES));
			}
		}
		
		return ret;
	}
}
