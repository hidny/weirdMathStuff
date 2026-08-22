package cccgProblemBirthdayGuy.aboveN2;

import cccgProblemBirthdayGuy.GetMinPerimeterCornersRefined;

public class CandidatesAboveN2v1 {

	//Tryuign to find a 3 rect solutions where the first and biggest rect
	// is a big square...
	//I think I need pen & paper to think this through...
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int LENGTH = 500000;

		int trials[][] = GetMinPerimeterCornersRefined.dynamicProgrammingTrials(2, 1000000);
		
		
		/*for(int i=0; i<trials.length; i++) {
			for(int j=0; j<trials[i].length; j++) {
				System.out.println(i + ", " + j + ": " + trials[i][j]);
			}
				
		}*/
		
		
		//Get the best after 2:
		/*for(int j=0; j<trials[0].length; j++) {
			if(trials[0][j] > trials[1][j]) {
				System.out.println(j + ": " + trials[1][j] + " vs " + trials[0][j]);
				System.out.println("Diff: " + (trials[1][j]-trials[0][j] ));
				System.out.println();
			}
		}*/
		
		//Didn't work for K=100 and LENGTH = 500000
		//That's interesting...
		int MAX_K = 100;
		
		int effectiveMax = trials[0].length - MAX_K * MAX_K;
		System.out.println("effectiveMax: " + effectiveMax);
		
		for(int j=0; j<effectiveMax; j++) {
			if(trials[0][j] > trials[1][j]) {
				
				boolean bestAfterK = true;
				
				
				
				for(int k=0; k<=MAX_K; k++) {
					
					//N^2 perim same
					
					if(trials[1][j] < trials[1][j + k *k]) {
						bestAfterK = false;
					}
				}
				
				if(bestAfterK) {
					System.out.println("Best after " + MAX_K + " reductions");
					System.out.println("i.e: NxN, (N-1)x(N+1), ..., (N-" + MAX_K + ")x(N+" + MAX_K + ")");
					
					System.out.println(j + ": " + trials[1][j] + " vs " + trials[0][j]);
					System.out.println("Diff: " + (trials[1][j]-trials[0][j] ));
					
					int bestK2 = -1;
					int k2 = 0;
					boolean gotStopped = false;
					
					for(; k2*k2 + j < trials[0].length; k2++) {
						if(trials[1][j] < trials[1][k2 *k2 + j]) {
							gotStopped = true;
							bestK2 = k2;
							break;
						}
					}
					
					if(gotStopped) {
						System.out.println("(N-" + bestK2 + ")x(N+" + bestK2 + ") is more efficient...");
					} else {
						System.out.println("Good news! I couldn't find something better and checked up to k= " + (k2-1) + " inclusively.");
					}
					System.out.println();
					System.out.println();
					
				}
			}
		}
	}

}
