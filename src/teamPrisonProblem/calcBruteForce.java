package teamPrisonProblem;

public class calcBruteForce {

	//Problem is already solved... It's 1 - ln(2) for large n.
	public static void main(String args[] ) {
		int i = 50;
		System.out.println((2*i) + " people: " + (1 - getAnswer(2*i)));
	}
	
	public static void initial() {

		System.out.println("10 people: " +  (1 - getAnswer(10)));
		
		for(int i=1; i<10000; i++) {
			System.out.println((2*i) + " people: " + (1 - getAnswer(2*i)));
		}
	}
	
	public static double memory[];
	
	public static double getAnswer(int n) {
		
		memory = new double[n/2 + 1];
		
		int limit = n/2;
		
		for(int i=n/2+1; i<n; i++) {
			getOddsLoss(i, limit);
		}
		
		return getOddsLoss(n, n/2);
	}
	
	
	
	
	public static double getOddsLoss(int n, int limit) {
		
		
		if(n<= limit) {
			return 0.0;
		}

		if(memory[n - limit] > 0.0) {
			return memory[n - limit];
		}
		
		double ret = (1.0*n - 1.0*limit)/(1.0* n);
		
		for(int i = 1; i<limit; i++) {
			ret += 1.0 * getOddsLoss(n-i, limit) / (1.0 * n);
		}
		
		memory[n - limit] = ret;
		return ret;
	}
	/*
	10 people: 0.3543650793650793
	2 people: 0.5
	4 people: 0.41666666666666663
	6 people: 0.3833333333333334
	8 people: 0.3654761904761905
	10 people: 0.3543650793650793
	12 people: 0.34678932178932176
	14 people: 0.3412948162948163
	16 people: 0.33712814962814963
	18 people: 0.3338601757719404
	20 people: 0.33122859682457206
	22 people: 0.3290640946600699
	24 people: 0.32725250045717125
	26 people: 0.32571403891870976
	28 people: 0.32439128759595826
	30 people: 0.3232418623086021
	32 people: 0.3222337977924732
	34 people: 0.32134253219532527
	36 people: 0.32054888140167426
	38 people: 0.3198376438483316
	40 people: 0.31919661820730605
	42 people: 0.3186158981143905
	44 people: 0.318087356888175
	46 people: 0.31760426510073536
	48 people: 0.3171610026893876
	50 people: 0.3167528394240815
	*/
	/*
6004 people: 0.30693609031964975
6006 people: 0.306936062592731
6008 people: 0.3069360348842638

*/
	
	//20000 people: 0.30687781881505394
	//1 - ln(2) = 0.30685
	//Answer: https://en.wikipedia.org/wiki/100_prisoners_problem#Asymptotics
}
