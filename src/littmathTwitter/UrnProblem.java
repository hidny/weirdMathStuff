package littmathTwitter;

public class UrnProblem {

	/*"You are given an urn containning 100 balls; n of them are red, and 100-n are green, where n is chosen uniformly at random in [0, 100]. You take a random ball out of the urn-it's-red-and discard it. The next ball you pick (out of the 99 remaining) is:
	 * 
	 * More likely to be red
	 * More likely to be green
	 * Equally likely
	 * Don't know/see results
	 */
	
	public static int NUM_BALLS = 100;
	
	public static int FORGOTABLE = 0;
	public static int RED = 1;
	public static int GREEN = 2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int NUM_ITERATIONS = 990000;
		int numRed = 0;
		int numGreen = 0;
		int numForget = 0;

		for(int it=0; it<NUM_ITERATIONS; it++) {
			for(int r=0;r<=100; r++) {
			
				int result = getResultOfSimulationNonRandom(r, it, NUM_ITERATIONS);
				//int result = getResultOfSimulationRandomNoReplacement(r, it, NUM_ITERATIONS);
				
				if(result == FORGOTABLE) {
					numForget++;
				} else if(result == RED) {
					numRed++;
				} else if(result == GREEN) {
					numGreen++;
				} else {
					System.out.println("ERROR: doh!");
					System.exit(1);
				}
			}
			
			if(it % (NUM_ITERATIONS/5) == 0) {
				System.out.println("intermidiate results:");
				System.out.println("Num red: " + numRed);
				System.out.println("Num green: " + numGreen);
				System.out.println("Num numForget: " + numForget);
				System.out.println("Prob red: " + (10 * numRed) / (1.0 *(numRed + numGreen)) + "%");
				System.out.println();
			}
			
		}
		System.out.println("Final results:");
		System.out.println("Num red: " + numRed);
		System.out.println("Num green: " + numGreen);
		System.out.println("Num numForget: " + numForget);
		System.out.println("Prob red: " + (100. * numRed) / (1.0 *(numRed + numGreen)) + "%");
		System.out.println();
		
	}


	//TODO: unrandomize it
	public static int getResultOfSimulationRandomNoReplacement(int numRed, int it, int numIterations) {
		
		int index1 = (int)(NUM_BALLS * Math.random());
		if(index1 >= numRed) {
			return FORGOTABLE;
		}
		
		int index2 = (int)((NUM_BALLS-1) * Math.random());
		
		if(index2 >= numRed) {
			return GREEN;
		} else {
			return RED;
		}
	}
	
	//TODO: unrandomize it
	public static int getResultOfSimulationRandom(int numRed, int it, int numIterations) {
		
		int index1 = (int)(NUM_BALLS * Math.random());
		if(index1 > numRed) {
			return FORGOTABLE;
		}
		
		int index2 = (int)((NUM_BALLS-1) * Math.random());
		
		if(index2 > numRed -1) {
			return GREEN;
		} else {
			return RED;
		}
	}
	

	//TODO: unrandomize it
	public static int getResultOfSimulationNonRandom(int numRed, int it, int numIterations) {
		
		if(numIterations % (NUM_BALLS*(NUM_BALLS-1)) != 0) {
			System.out.println("WRONG NUM OF ITERATIONS");
		}
		int index1 = it%(NUM_BALLS);
		if(index1 > numRed) {
			return FORGOTABLE;
		}
		
		int index2 = it/NUM_BALLS;
		
		if(index2 > numRed - 1) {
			return GREEN;
		} else {
			return RED;
		}
	}
	
}
