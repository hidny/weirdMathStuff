package misselaneous;

import java.math.BigInteger;

public class ChalkTalkQuestion {



	public static double pow2[] = null;
	public static double pascalsTriangle[][] = null;
	public static final double TWO = 2.0;
	
	public static void createPascalsTriangle(int n) {
		
		pascalsTriangle = new double[n+1][n+1];
		
		for(int i=0; i<pascalsTriangle.length; i++) {
			for(int j=0; j<pascalsTriangle[0].length; j++) {
				pascalsTriangle[i][j] = 0.0;
			}
		}
		
		pascalsTriangle[0][0] = 1.0;
		
		for(int i=1; i<pascalsTriangle.length; i++) {
			for(int j=0; j<pascalsTriangle[0].length; j++) {
				
				if(j == 0) {
					pascalsTriangle[i][j] = 1.0;
				} else {
					pascalsTriangle[i][j] = pascalsTriangle[i - 1][j] + pascalsTriangle[i - 1][j - 1];
				}
				
				//System.out.print(pascalsTriangle[i][j] + "    ");
			}
			//System.out.println();
		}
	}
	
	public static double f(int n, double prob) {
		double p = prob;
		double q = 1.0 - prob;
		
		double pq = p *  q;
		//System.out.println("Solve f:");
		//System.out.println("n: " + n);
		//System.out.println("pq: " + pq);
		
		double ret = 0.0;
		
		int startN = n;
		for(int i=0; i<=startN; i++) {
			
			int sign = (int)Math.pow(-1, i);
			ret += sign * pascalsTriangle[startN-i][i] * Math.pow(pq, i);
			//System.out.println(" + " + sign * pascalsTriangle[startN-i][i]);
			
		}
		
		
		
		System.out.println("f( " + n + ", " + prob + ") = ");
		System.out.println(ret);
		System.out.println("----------------------");
		return ret;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int chalkTalkN = 12;
		
		if(chalkTalkN % 2 != 0) {
			System.exit(1);
		}
		createPascalsTriangle(chalkTalkN);
		
		int convenientN = chalkTalkN/2;
		
		double prob = 5.0/14.0;
		//double prob = 0.5;
		
		System.out.println("Guess: ");
		
		double result = Math.pow(prob, convenientN) * f(convenientN - 1, prob)/f(2 * convenientN - 1, prob);
		
		System.out.println("Final answer:");
		System.out.println(result);
		


		prob = 20.0/41.0;
		convenientN = 10;
		createPascalsTriangle(2 * convenientN);
		
		result = Math.pow(prob, convenientN) * f(convenientN - 1, prob)/f(2 * convenientN - 1, prob);
		System.out.println("test answer:");
		System.out.println(result);
	}

}
