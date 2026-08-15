package cccgProblemBirthdayGuy;

public class GetMinPerimeterCutOffBigRectIntro {

	//Aug 15th (After CCCG 2026)
	
	//My understanding of the problem:
	//Idea:
	//Input n.
	
	//Simple Output:
	// Min perimeter of the rectangles added to the corners of a large NxN square such that
	// the area of the corners add up to input n.
	
	// More complex, but same problem:
	// min half of previous.
	
	//Even more complex:
	//What if you have the option of adding a rectangle into the edge not touching a corner and it costs even more.
	// Is it ever worth having an edge rectangle once all 4 corners are exhausted?
	
	//He tried to explain the usefulness, but I didn't get it. I'm working on it because it was the asker's birthday.
	
	// Corner cost: half perimeter. Edge cost: perimeter minus longest edge
	
	
	public static void main(String args[]) {
		
		//int solve1[] = setupSolve1();
		
		//int bestPerim = attemptImproveAreaIWith2(solve1, 101);
		//System.out.println("Best perimeter: " + bestPerim);
		
		//bruteForceTrial2();
		
		dynamicProgrammingTrials(2);
		
	}
	
	public static void dynamicProgrammingTrials(int numCornerTrials) {
		
		int trials[][] = new int[numCornerTrials][LENGTH];
		
		for(int i=0; i<trials.length; i++) {
			if(i == 0) {
				trials[i] = getTrialN(null, i + 1);
			} else {
				trials[i] = getTrialN(trials[i - 1], i + 1);
			}
		}
		
	}

	public static int solve1Static[] = null;
	public static final int LENGTH = 1000;
	
	public static int[] getTrialN(int prevTrial[], int n) {
		
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

		int solveN[] = new int[LENGTH];
		for(int i=1; i<solveN.length; i++) {
			solveN[i] = prevTrial[i];
		}
		
		for(int i=1; i<solveN.length; i++) {
			
			solveN[i] = attemptImproveAreaIWithN(n, solveN, i);
			
		}
	
		return solveN;
	
	}
	
	public static int attemptImproveAreaIWithN(int n, int prevTrial[], int i) {
		
		int currentRet = prevTrial[i];
		
		for(int trial=1; trial<=i/n; trial++) {
			int otherArea = i - trial;
			
			int res = solve1Static[trial] + prevTrial[otherArea];
			if(res < currentRet) {
				currentRet = res;
				//foundBetter = true;
				System.out.println("Trial " + n + " better i: " + i);
				
				//TODO: print prev...
			}
		}
		return currentRet;
	}
	
	
	public static void bruteForceTrial2() {
		
		
		int solve1[] = setupSolve1();
		//for(int i=1; i<solve1.length; i++) {
		//	System.out.println("i: " + i + ": " + solve1[i]);
		//	System.out.println();
		//}
		
		//49 -> 14
		//50 -> 15 (Less than I thought before...)

		System.out.println("-----------------");
		System.out.println("-----------------");
		System.out.println("-----------------");

		long solve2[] = new long[LENGTH];
		for(int i=1; i<solve2.length; i++) {
			solve2[i] = solve1[i];
		}
		
		for(int i=1; i<solve2.length; i++) {
			
			attemptImproveAreaIWith2(solve1, i);
			
		}
	}
	
	public static int attemptImproveAreaIWith2(int solve1[], int i) {
		
		int currentRet = solve1[i];
		
		for(int trial=1; trial<=i/2; trial++) {
			int otherArea = i - trial;
			
			if(solve1[trial] + solve1[otherArea] < currentRet) {
				currentRet = solve1[trial] + solve1[otherArea];
				//foundBetter = true;
				System.out.println("Trial 2 better i: " + i + ": solve1: " + solve1(i) + " solve 2: " + currentRet + " Used trial " + trial);
				
			}
		}
		return currentRet;
	}
	
	public static int[] setupSolve1() {
		int solve1[] = new int[LENGTH];
		
		//I'm just going to try to remember to watch out for 0s...
		solve1[0] = -1000000;
		
		for(int i=1; i<solve1.length; i++) {
			
			solve1[i] = solve1(i);
		}
		
		solve1Static = solve1;
		
		return solve1;
	}
	
	
	//TODO: Email Robert
	//TODO: Email the portrait guy that knows Jenny
	//TODO: work on the portrait problem in a polyomino because it seems like fun. 
	//TODO: Solve Joseph's open problem
	
	public static int solve1(int n) {
		
		long divisors[] = UtilityFunctions.UtilityFunctions.getAllDivisors(n);
		
		int div1 = (int)divisors[divisors.length / 2];
		
		return div1 + n/div1;
	}
	
	
}
