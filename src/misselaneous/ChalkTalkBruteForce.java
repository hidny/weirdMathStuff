package misselaneous;

public class ChalkTalkBruteForce {

	//0.35245
	//0.35846
	public static double solve(double prob, int diff) {
		
		double ret = 0.0;
		
		int numTourneyWins = 0;
		int numTourneyLosses = 0;
		
		//0.028603
		for(int i=0; i<200000; i++) {

			int numWins =0;
			int numLosses = 0;

			while(true) {
				
				if(Math.random() < prob) {
					numWins++;
				} else {
					numLosses++;
				}
				
				if(Math.abs(numWins - numLosses) == diff) {
					if(numWins - numLosses >= diff) {
						numTourneyWins++;
						break;
		
					} else if(numLosses - numWins >= diff) {
						numTourneyLosses++;
						break;
					} else {
						System.exit(1);
					}
				}
			}
		}
		
		
		
		return (1.0 * numTourneyWins ) / (1.0 * numTourneyWins + 1.0 * numTourneyLosses);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//double prob = 5.0/14.0;
		//System.out.println(solve(prob, 6));
		
		
		double prob = 20.0/41.0;
		System.out.println(solve(prob, 10));

	}

}
