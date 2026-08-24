package cccgProblemBirthdayGuy.aboveN2;

import cccgProblemBirthdayGuy.GetMinPerimeterCornersRefined;

public class CandidatesAboveN2v3 {

	//Tryuign to find a 3 rect solutions where the first and biggest rect
	// is almost a big square...
	//I think I need pen & paper to think this through...
	
	public static int INTERESTING_DIST = 10;
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int LENGTH = 100000;

		int trials[][] = GetMinPerimeterCornersRefined.dynamicProgrammingTrials(2, LENGTH);
		
		
		//TODO MAX_K = 100, C_MAX = 50...
		//That's interesting...
		int MAX_K = 200;
		
		//Looking to find potential Areas that start with the (N-C)x(N+C) rectangle
		// where C <= C_MAX
		int C_MAX = 100;
		
		int C_MAX_SQUARED = C_MAX*C_MAX;
		
		int effectiveMax = trials[0].length - MAX_K * MAX_K + C_MAX_SQUARED;
		System.out.println("effectiveMax: " + effectiveMax);
		
		
		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("------------------------");
		
		for(int target=0 - C_MAX_SQUARED; target < effectiveMax; target++) {
							
			boolean bestAfterKTrials = true;
			boolean oneShotWorks = false;
			
			int currentBestCValue = -1;
			
			for(int k=0; k<=MAX_K; k++) {
				
				int indexToCheck = target + k *k;
				
				if(indexToCheck < 0) {
					continue;
				} else if(currentBestCValue == -1) {
					currentBestCValue = k;
					
					if(trials[0][target + k *k] <= trials[1][target + k *k]) {
						oneShotWorks = true;
					} else {
						oneShotWorks = false;
					}
					//System.out.println("First best:");
					//System.out.println("k = " + k);
					//System.out.println("trials[1][target + k *k]: " + trials[1][target + k *k]);
					
				} else if(indexToCheck >= trials[0].length) {
					break;
				}
				//N^2 perim same
				
				//System.out.println("1: " + (j + currentBestCValue * currentBestCValue - C_MAX*C_MAX));
				//System.out.println("2: " + (j + k *k -C_MAX*C_MAX));
				
				/*if(k == 1) {
					System.out.println("Test k=1");
					System.out.println("k = " + k);
					System.out.println("trials[1][target + k *k]: " + trials[1][target + k *k]);
				}*/
				
				if(trials[1][target + k *k] < trials[1][target + currentBestCValue * currentBestCValue]) {
					
					if(trials[0][target + k *k] <= trials[1][target + k *k]) {
						oneShotWorks = true;
					} else {
						oneShotWorks = false;
					}
					
					//System.out.println("k = " + k);
					//System.out.println("trials[1][target + k *k]: " + trials[1][target + k *k]);
					
					if( k <= C_MAX) {
						currentBestCValue = k;
					} else {
						bestAfterKTrials = false;
					}
					
				} else if(trials[0][target + k *k] == trials[1][target + currentBestCValue * currentBestCValue]) {
					oneShotWorks = true;
				}
			}
			
			if(bestAfterKTrials && ! oneShotWorks) {
				
				int diffRelativeToCurrentBestC = target + currentBestCValue*currentBestCValue;
				
				if(diffRelativeToCurrentBestC < INTERESTING_DIST) {
				
					System.out.println("currentBestCValue = " + currentBestCValue);
					System.out.println("Best after " + MAX_K + " reductions");
					System.out.println("i.e: NxN, (N-1)x(N+1), ..., (N-" + MAX_K + ")x(N+" + MAX_K + ")");
					
					System.out.println("Target found: " + target);
					System.out.println("Target found relative to N^2-CMAX^2: " + (target + C_MAX_SQUARED));
					System.out.println("Target found relative to N^2-currentBestCValue^2: " + diffRelativeToCurrentBestC);
					
					int bestK2 = -1;
					int k2 = 0;
					boolean gotStopped = false;
					
					for(; k2*k2 + target < trials[0].length; k2++) {
						if(k2 *k2 + target < 0) {
							continue;
						}
						
						if(trials[1][target + k2 *k2] < trials[1][target + currentBestCValue*currentBestCValue]) {
							gotStopped = true;
							bestK2 = k2;
							break;
						}
						
					}
					
					if(gotStopped) {
						System.out.println("(N-" + bestK2 + ")x(N+" + bestK2 + ") is more efficient...");
						
						if(bestK2 <= MAX_K) {
							System.out.println("DEBUG ???");
							System.exit(1);
						}
					} else {
						System.out.println("Good news! I couldn't find something better and checked up to k= " + (k2-1) + " inclusively.");
						
						//TODO: get rid of this...
					}
					System.out.println();
					System.out.println();
				}
				
			}
		}
	}

}
