package hatproblem;

import UtilityFunctions.Fraction;

public class SolveUsingSolutionOnPaper {

	public static int N = 7;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//for(int n=3; n<20; n = n + 2) {
			//N = n;;
			SolveNAndNHats.N = N;
			
			int stratPlayer1[] = getPaperStratForPlayer1(N);
			
			Fraction probBothWinning = SolveNAndNHats.tryBestResponseToStrat(stratPlayer1);
			
			printResults(probBothWinning, stratPlayer1);
		//}
	}
	
	

	
	//From paper: //linked in https://www.brand.site.co.il/riddles/201607a.html
	// ---> https://arxiv.org/pdf/1407.4711.pdf
	/*
	 * "Strategy S1:
(1) If the first three hats of the other player are not all of the same color, play
the 3-hat strategy.
(2) If the first three hats of the other player are BBB or WWW, repeat S1 on
hats 3 through infinity."
	 */

	public static int[] getPaperStratForPlayer1(int numHats) {
		int stratPlayer1[] = new int[(int)Math.pow(2,  numHats)];
		
		if(numHats % 2 == 1 && numHats > 1) {
			
		} else {
			System.out.println("ERROR: weird int getPaperStratForPlayer1. Number of hats I'm not yet ready to handle...");
			System.exit(1);
		}
		
		int NUM_CUBE_DIM = 3;
		
		for(int i=0; i<stratPlayer1.length; i++) {
			
			stratPlayer1[i] = 0;

			boolean array[] = BoolUtilFuntions.convertNumToBoolArray(i, numHats);
			
			boolean foundStart = false;
			
			for(int indexStartCube=0; indexStartCube + 3 <= N && foundStart == false; indexStartCube += 2) {
				
				for(int rotation=0; rotation < NUM_CUBE_DIM && foundStart == false; rotation++) {
					
					if(array[indexStartCube + rotation] && !array[indexStartCube + ((rotation+2) % NUM_CUBE_DIM)]) {
						
						stratPlayer1[i] = indexStartCube + rotation;
						foundStart = true;
					}
				}
				
			}
			
		}
		
		
		return stratPlayer1;
	}
	
	public static void printResults(Fraction probBothWinning, int stratPlayer1[]) {
		System.out.println("prob both winning for N = " + N +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
		System.out.println("");
		System.out.println("Player 1 strat:");
		for(int i=0; i<stratPlayer1.length; i++) {
			System.out.println(i + ": " + stratPlayer1[i]);
		}
		System.out.println();
		

		System.out.println("Again: prob both winning for N = " + N +":");
		System.out.println(probBothWinning.getDecimalFormat(10));
		System.out.println("(" + probBothWinning.getNumerator() + "/" + probBothWinning.getDenominator() + ")");
		
		//TODO: what's the probability distribution of which hat to pick for player1?
		
		//TODO: what's player 2's strat?
	}

	
	/*
	 * Again: prob both winning for N = 3:
0.34375
(11/32)

Again: prob both winning for N = 5:
0.349609375
(179/512)


	 * 
	 * Again: prob both winning for N = 7:
0.3499755859375
(2867/8192)

Again: prob both winning for N = 9:
0.34999847412109375
(45875/131072)

Again: prob both winning for N = 11:
0.34999990463256836
(734003/2097152)

Again: prob both winning for N = 13:
0.3499999940395355
(11744051/33554432)

Looks like it tends towards 0.35
It's just like they said it would!



	 */
}
