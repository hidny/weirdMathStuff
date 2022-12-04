package figureOutNumbers;

public class figureOutMattNumbers {

	
	public static void main(String args[]) {
		
		double bestRatio = 0.0;
		
		double bestRatio2 = 0.0;
		
		
		
		int bestSum = 0;
		for(int i=2; i<10000; i++) {
			long div[] = UtilityFunctions.UtilityFunctions.getAllDivisors(i);

			long divPrime[] = UtilityFunctions.UtilityFunctions.getPrimeDivisors(i);
			
			int curSum = 0;
			for(int j=0; j<div.length; j++) {
				curSum += div[j];
			}
			
			
			
			if(curSum > bestSum) {
				bestSum=curSum;
				System.out.println("Sum: " + i + ": " + bestSum);
			}

			double curRatio = (1.0*curSum)/(1.0 *i);
			
			if(curRatio > bestRatio) {
				bestRatio=curRatio;
				//System.out.println("Ratio " + i + ": " + bestRatio);
			}
		

			double curRatio2 = (1.0*curSum - 1.0*i)/(1.0 *i);
			
			if(curRatio2 > bestRatio2) {
				bestRatio2=curRatio2;
				//System.out.println("Ratio2 " + i + ": " + bestRatio);
			}
			
			

			double resDiv = curSum;
			
			for(int j=0; j<divPrime.length; j++) {
				resDiv /= (1.0 *divPrime[j]);
			}
			
			//System.out.println(resDiv);
		}
	}
}
