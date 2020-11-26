package mpmpPascalsTriangleSingmaster;

public class singMasterWithLogs {

	//https://aperiodical.com/2013/01/open-season-singmasters-conjecture/
	public static double logs[] = new double[100000];
	public static double sumLogs[] = new double[100000];
	
	public static int LIMIT = 100;
	
	public static double log2 = Math.log(2.0);
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		logs[0] = 0.0;
		sumLogs[0] = 0.0;
		for(int i=1; i<logs.length; i++) {
			logs[i] = Math.log(i) /log2;
			sumLogs[i] = sumLogs[i-1] + logs[i];
		}
		
		for(int i=1; i<logs.length; i++) {
			
			System.out.println(Math.round(Math.exp(log2 * logs[i])));
		}
		
		//What's 10000! ?
		
		double sum =0.0;
		for(int i=1; i<LIMIT; i++) {
			
			sum += logs[i];
		}
		System.out.println(sum);
		System.out.println(Math.exp(log2 * sum));
		
		
		for(int i=0; i<100; i++) {
			for(int j=0; j<i; j++) {
				findMatch(i, j, 2);
			}
			System.out.println();
		}
		
	}
	
	
	public static void testing() {

		System.out.println("Second sum method: " + sumLogs[9999]);
		
		//(2^118444.85529050218 - 9999!)/9999!
		
		// = -6.92773... × 10^-11
		//Small enough...
		
		//TODO: (n choose m)
		for(int m = LIMIT/2; m > 2; m--) {
			
			for(int n = LIMIT; 2*m <= n; n--) {
				
				System.out.println("Try " + n + " choose " + m);
				
				double approxComb = sumLogs[n] - sumLogs[n-m] - sumLogs[m];
				
				System.out.println(Math.round(Math.exp(log2 * approxComb)));
				
				//TODO: find repeats
			}
		}
	}
	
	
	public static void findMatch(int n, int m, int trial) {
		
		double approxComb = sumLogs[n] - sumLogs[n-m] - sumLogs[m];
		
		
		double trialUpApprox = (approxComb + sumLogs[trial])/trial;

		
		double approxNumber = Math.round(Math.exp(log2 * trialUpApprox));
		
		//System.out.println(approxNumber);
		
		System.out.print("( " + n + " choose " + m + ") = " + (approxNumber*approxNumber/2) + "    ");
		
		
		
	}
	

}
