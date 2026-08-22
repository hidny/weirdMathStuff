package cccgProblemBirthdayGuy.aboveN2;

import cccgProblemBirthdayGuy.GetMinPerimeterCornersRefined;

public class CandidatesAboveN2v2 {

	//Tryuign to find a 3 rect solutions where the first and biggest rect
	// is almost a big square...
	//I think I need pen & paper to think this through...
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int LENGTH = 10000;

		int trials[][] = GetMinPerimeterCornersRefined.dynamicProgrammingTrials(2, LENGTH);
		
		
		//TODO MAX_K = 100, C_MAX = 50...
		//That's interesting...
		int MAX_K = 50;
		
		//Looking to find potential Areas that start with the (N-C)x(N+C) rectangle
		// where C <= C_MAX
		int C_MAX = 10;
		
		int C_MAX_SQUARED = C_MAX*C_MAX;
		
		int effectiveMax = trials[0].length - MAX_K * MAX_K + C_MAX_SQUARED;
		System.out.println("effectiveMax: " + effectiveMax);
		
		
		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("------------------------");
		System.out.println("------------------------");
		
		for(int target=0-C_MAX_SQUARED; target < effectiveMax; target++) {
			
			if(trials[0][target + C_MAX_SQUARED] > trials[1][target + C_MAX_SQUARED]) {
				
				boolean bestAfterK = true;
				
				int currentBestCValue = -1;
				
				for(int k=0; k<=MAX_K; k++) {
					
					int indexToCheck = target + k *k;
					
					if(indexToCheck < 0) {
						continue;
					} else if(currentBestCValue == -1) {
						currentBestCValue = k;
					} else if(indexToCheck >= trials.length) {
						break;
					}
					//N^2 perim same
					
					//System.out.println("1: " + (j + currentBestCValue * currentBestCValue - C_MAX*C_MAX));
					//System.out.println("2: " + (j + k *k -C_MAX*C_MAX));
					if(trials[1][target + k *k] < trials[1][target + currentBestCValue * currentBestCValue]) {
						
						if( k <= C_MAX) {
							currentBestCValue = k;
						} else {
							bestAfterK = false;
						}
					}
				}
				
				if(bestAfterK) {
					System.out.println("currentBestCValue = " + currentBestCValue);
					System.out.println("Best after " + MAX_K + " reductions");
					System.out.println("i.e: NxN, (N-1)x(N+1), ..., (N-" + MAX_K + ")x(N+" + MAX_K + ")");
					
					System.out.println("Target found: " + target);
					System.out.println("Target found relative to N^2-CMAX^2: " + (target + C_MAX_SQUARED));
					
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
