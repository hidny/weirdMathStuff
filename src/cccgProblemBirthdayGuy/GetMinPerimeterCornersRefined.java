package cccgProblemBirthdayGuy;

public class GetMinPerimeterCornersRefined {

	//See intro for more details...

	public static final int LENGTH = 10000000;
	
	public static void main(String[] args) {

		System.out.println("Trying with LENGTH: " + LENGTH);

		dynamicProgrammingTrials(4);
	}

	
	public static void dynamicProgrammingTrials(int numCornerTrials) {
		
		int trials[][] = new int[numCornerTrials][LENGTH];
		boolean stopTrying[] = new boolean[LENGTH];
		
		for(int i=0; i<trials.length; i++) {
			if(i == 0) {
				trials[i] = getTrialN(i + 1);
			} else {
				trials[i] = getTrialN(trials[i - 1], stopTrying, i + 1);
				
				for(int j=1; j<LENGTH; j++) {
					if(trials[i][j] == trials[i-1][j]) {
						stopTrying[j] = true;
					}
				}
				
			}
		}
		
	}
	
	public static int solve1Static[] = null;

	public static int[] getTrialN(int n) {
		return getTrialN(null, null, n);
	}

	public static int[] getTrialN(int prevTrial[], boolean stopTrying[], int n) {
		if(n < 0 ) {
			System.out.println("ERROR: start n at 1");
			System.exit(1);
		}
		
		if( n == 1) {
			return setupSolve1();
		}
		
		System.out.println("-----------------");
		System.out.println("-----------------");
		System.out.println("-----------------");
		System.out.println("Trying with " + n + " Rectangles in the corners:");

		int solveN[] = new int[LENGTH];
		for(int i=1; i<solveN.length; i++) {
			solveN[i] = prevTrial[i];
		}
		
		int debug = 0;
		for(int i=1; i<solveN.length; i++) {
			
			if( ! stopTrying[i]) {
				debug++;
				if(n >=3 && debug % 1000 == 0) {
					System.out.println("At n = " + n + ": Trying i= " + i + ":");
				}
				solveN[i] = attemptImproveAreaIWithN(n, solveN, i);
			}
			
		}
	
		return solveN;
	
	}
	
	
	
	public static int[] setupSolve1() {
		System.out.println("Setting up solve1:");
		int solve1[] = new int[LENGTH];
		
		//I'm just going to try to remember to watch out for 0s...
		solve1[0] = -1000000;
		
		for(int i=1; i<solve1.length; i++) {
			
			solve1[i] = solve1(i);
		}
		
		solve1Static = solve1;
		System.out.println("Done setting up solve1!");
		
		return solve1;
	}

	public static int solve1(int n) {
		
		long divisors[] = UtilityFunctions.UtilityFunctions.getAllDivisors(n);
		
		int div1 = (int)divisors[divisors.length / 2];
		
		return div1 + n/div1;
	}

	public static int numSolutions = 0;
	
	public static int attemptImproveAreaIWithN(int n, int prevTrial[], int i) {
		
		int currentRet = prevTrial[i];
		
		for(int trial=1; trial<=i/n; trial++) {
			int otherArea = i - trial;
			
			int res = solve1Static[trial] + prevTrial[otherArea];
			if(res < currentRet) {
				currentRet = res;

				numSolutions++;
				
				//foundBetter = true;
				if(n > 2 || numSolutions % 100000 == 0 || trial > 60) {
					System.out.println("Trial " + n + " better i: " + i);
					System.out.println("Split off rectangle area: (" + trial + ") plus (" + otherArea + ") = " + i);
					//TODO: record all of the solutions???
					//Maybe later
					if(n >=3) {
						System.out.println("Successful exit!");
						System.exit(0);
					}
				}
				//TODO: print prev...
				
			}
		}
		return currentRet;
	}
}
