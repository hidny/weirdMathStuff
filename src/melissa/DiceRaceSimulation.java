package melissa;

public class DiceRaceSimulation {
/*
 *  * Question 3. (20 marks) Write a simple press-your-luck dice game. The game works as follows:
 Player 1 starts with a score of zero and rolls two dice.
o If the player rolls 7, 2, or 12 their turn is over and they lose.
o If the player rolls any other sum it is added to their total score.
o The player can stop rolling at any time – remember if their last roll was a
7, 2 or 12 they lose and Player 2 wins.
 After Player 1 stops rolling, Player 2 rolls the two dice
o Player 2 continues to roll the dice until they roll a 7, 2 or 12 (Player 2
loses) or they have a total higher than Player 1 (Player 2 wins).
Your solution should make use of methods with the following headers:
 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//From other prob:
		//0: (roll) prob: 0.2896439857146048
		
		//Prob P1 wins: 28.96413
		//Prob P1 wins(2): 25.581163
		//Prob P1 wins(3): 25.577452
		//Prob P1 wins(4): 25.981734
		//Prob P1 wins(5): 26.575913
		//Prob P1 wins(6): 27.279612
		//Prob P1 wins(7): 27.957809
		//Prob P1 wins(8): 28.014759
		//Prob P1 wins(9): 28.611666
		//Prob P1 wins(10): 28.902064
		
		//CLOSE ENOUGH:
		//Prob P1 wins(11): 28.96634
		//Prob P1 wins(11): 28.9656939
		//Prob P1 wins(11): 28.9666255
		//                  28.9675764
		
		//SUM: 1000000000
		//Prob P1 wins(11): 28.9636118
		//Prob P1 wins(11): 28.9637155
		//SUM: 1000000000
		//Prob P1 wins(11): 28.9670335
		//SUM: 1000000000
		//Prob P1 wins(11): 28.9665351
		//SUM: 1000000000
		//Prob P1 wins(11): 28.9644164
		//SUM: 1000000000
		//Prob P1 wins(11): 28.9646744
		//vs                28.96439857146048
		
		
		//Prob P1 wins(12): 28.919867
		
		
		int STAYP1 = 11;
		int NUM_SIDES = 6;
		
		int NUM_SIMULATIONS = 1000000000;
		int P1Wins = 0;
		int P2Wins = 0;
		
		int p1Points;
		int p2Points;
		
		int roll;
		boolean gameOver;
		
		for(int n=0; n<NUM_SIMULATIONS; n++) {
			p1Points = 0;
			p2Points = 0;
			gameOver = false;
			
			//P1Turn
			
			while(p1Points < STAYP1) {
				roll = getSum2Rolls(NUM_SIDES);
				
				if(roll == 7 || roll == 2 || roll ==12 ) {
					P2Wins++;
					gameOver = true;
					break;
				} else {
					p1Points += roll;
				}
			}
			
			if(gameOver == false) {
				//P2Turn
				while(p2Points <= p1Points) {
					roll = getSum2Rolls(NUM_SIDES);
					
					if(roll == 7 || roll == 2 || roll ==12 ) {
						P1Wins++;
						gameOver = true;
						break;
					} else {
						p2Points += roll;
					}
				}
				if(p2Points > p1Points && gameOver == false) {
					P2Wins++;
					gameOver = true;
				} else if(p2Points > p1Points && gameOver == true) {
					System.err.println("ERROR: something went wrong!");
				}
			}
			
		}
		
		System.out.println("//SUM: " + (P1Wins + P2Wins));
		double prob = (100.0* P1Wins )/(1.0 * NUM_SIMULATIONS);
		double probOpposite = (100.0* P2Wins )/(1.0 * NUM_SIMULATIONS);
		
		System.out.println("//Prob P1 wins(" +STAYP1 +"): " + prob);

	}

	
	public static int getSum2Rolls(int numSides) {
		return getRoll(numSides) + getRoll(numSides);
	}
	
	public static int getRoll(int numSides) {
		return 1 + (int)(numSides * Math.random());
	}
}

//Prob P1 wins: 28.96413
//Prob P1 wins(2): 25.581163


//Prob P1 wins (1): 25.577343