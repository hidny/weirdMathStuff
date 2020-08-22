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
public class SolveNAnd2PowerNHats {
	
	public static int N = 4;

	public static int DEBUG_N = 2;
	
	//178973355 has 6 other sequences
	
	//Maybe add to oeis:
	//Num ways to colour every number from 1 to 2^N such that there's N colours and the order of appearance of the first N colours is fixed.
	
	//There's got to be a formula for it...

	public static void main(String[] args) {
		
		Fraction probBothWinningFraction = findProbBothP1AndP2Win();
		double probBothWinning = probBothWinningFraction.getDecimalFormat(10);
		
		System.out.println("P1 n=" + (int)Math.pow(2, N) + " and P2 n=" + N + ":");
		System.out.println("Decimal representation of prob both P1 and P2 guessing blue hat: " + probBothWinning);
		System.out.println("Fractional representation: " + probBothWinningFraction.getNumerator() + "/" + probBothWinningFraction.getDenominator());
		
	}
	
		//P1 n=2 and P2 n=1:
		//Decimal representation of prob both P1 and P2 guessing blue hat: 0.25
		//Fractional representation: 1/4
	
		//P1 n=4 and P2 n=2:
		//Decimal representation of prob both P1 and P2 guessing blue hat: 0.3125
		//Fractional representation: 5/16
	
		//P1 n=8 and P2 n=3:
		//Decimal representation of prob both P1 and P2 guessing blue hat: 0.3203125
		//Fractional representation: 41/128

	//TODO: What happened???
	//P1 n=16 and P2 n=4: (crap)
	//Decimal representation of prob both P1 and P2 guessing blue hat: 0.312469482421875
	//Fractional representation: 10239/32768
	
	public static Fraction findProbBothP1AndP2Win() {
		
		long numPossiblePlayer1HatConfigs = (long)Math.pow(2, Math.pow(2, N));
		
		Fraction ODDS_OF_CURRECT_PLAYER1_CONFIG = new Fraction(1, numPossiblePlayer1HatConfigs);

		Fraction currentProb = new Fraction(0, 1);

		for(int i=0; i < numPossiblePlayer1HatConfigs; i++) {
			
			if(N <= DEBUG_N) {
				System.out.println(i);
			} else if(i % 1000000 == 0) {
				System.out.println("Counter: " + i);
			}
			
			if(i == 1) {
				System.out.println("DEBUG");
			}
			currentProb = Fraction.plus(currentProb, 
					Fraction.mult(getOddsP1AndP2Wins(i), ODDS_OF_CURRECT_PLAYER1_CONFIG));
			
		}
		
		return currentProb;
	}
	
	public static Fraction getOddsP1AndP2Wins(int player1StackNumber) {

		boolean player1Stack[] = convertNumToBoolArray(player1StackNumber, (int)Math.pow(2, N));
		
		Fraction ODDS_OF_CURRECT_PLAYER2_CONFIG = new Fraction(1, (int)Math.pow(2, N));
		
		Fraction oddsOfBothWinningIfPlayerChoosesIndexJ[] = new Fraction[N];
		
		
		for(int j=0; j<oddsOfBothWinningIfPlayerChoosesIndexJ.length; j++) {
			oddsOfBothWinningIfPlayerChoosesIndexJ[j] = new Fraction(0, 1);
		}
		

		for(int i=0; i<player1Stack.length; i++) {
			
			if(player1Stack[i]) {
				
				boolean player2StackAssumingIndexIPickedByPlayer1[] = convertNumToBoolArray(i, N);
				
				for(int j=0; j<N; j++) {
					if(player2StackAssumingIndexIPickedByPlayer1[j]) {
						oddsOfBothWinningIfPlayerChoosesIndexJ[j] =
								Fraction.plus(oddsOfBothWinningIfPlayerChoosesIndexJ[j], ODDS_OF_CURRECT_PLAYER2_CONFIG);
					}
				}
				
			}
		}
		
		
		Fraction ret = Fraction.ZERO;
		
		for(int j=0; j<N; j++) {
			if(Fraction.minus(oddsOfBothWinningIfPlayerChoosesIndexJ[j], ret).greaterThan0()) {
				ret = oddsOfBothWinningIfPlayerChoosesIndexJ[j];
			}
		}
		
		System.out.println("Odds both p1 and p2 win: " + ret);
		
		return ret;
		
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
