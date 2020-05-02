package numberphile;

import java.math.BigInteger;
import java.util.ArrayList;

public class Finding33 {
	/* The challenge:
	 * find three number a, b, c s.t.
	 * a ^3 + b^3 - c^3 == 33
	 * or:
	 * a^3 - b^3 - c^3 == 33
	 * 
	 * I'm going to try case 1 first.
	 * 
	 * 
	 //33, 42, 114, 165, 390, 579,
	 //627, 633, 732, 795, 906, 921, and 975
	 */
	
	public static int LIST_LENGTH = 20;
	
	public static ArrayList<Long> seeds = new ArrayList<Long>();
	public static BigInteger listOfMods[] = new BigInteger[LIST_LENGTH];
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int TARGET = 33;
		
		int found = 0;
		
		
		
		boolean skipSearch;
		
		searchSeeds:
		for(int i=1; i<1000000; i++) {
			if(i%10000 == 0) {
				System.out.println(i);
			}
			skipSearch = false;
			for(int j=0; j<seeds.size(); j++) {
				if(UtilityFunctions.UtilityFunctions.getGCD(i, seeds.get(j)) != 1) {
					skipSearch = true;
				}
			}
			
			if(skipSearch == false && checkIfRoot3Covers(i) == false) {
				//System.out.println("Cube root doesn\'t cover: " + i);
				if(checkIfElligibleRootNumber(i, TARGET, 1.0, true)) {
					
					seeds.add((long)i);
					
					System.out.println("AHA! " + i);
					found++;
					if(found >= LIST_LENGTH) {
						break searchSeeds;
					}
				}
			}
		}
		
		BigInteger current = BigInteger.ONE;
		
		for(int i=0; i<LIST_LENGTH; i++) {
			listOfMods[i] = current;
			current = current.multiply(new BigInteger("" + seeds.get(i)));
		}
		
		//At this point, we have 20 seeds.
	}
	
	public static void findSolution() {
		findSolution(0, 0, 0, 0);
	}
	
	public static void findSolution(int index, long currentSolutionA, long currentSolutionB, long currentSolutionC) {
		//TODO
		
	}
	
	public static boolean checkIfRoot3Covers(int n) {
		boolean temp[] = new boolean[n];
		for(int i=0; i<n; i++) {
			temp[i] =false;
			
		}
		
		for(long i=0; i<n; i++) {
			if(temp[(int)((i*i*i) % n)] == true) {
				return false;
			} else{
				temp[(int)((i*i*i) % n)] =true;
			}
		}
		
		return true;
		
	}
	
	//pre: maxRatio <= 1.00
	public static boolean checkIfElligibleRootNumber(int n, int target, double maxRatio, boolean twoPositiveCubes) {
		if(maxRatio > 1.0) {
			System.err.println("Error: ratio should be smaller than 1!");
			System.exit(1);
		}
		
		long numWays = 0;
		
		int table[] = new int[n];
		for(long i=0; i<n; i++) {
			table[(int)i] = 0;
		}
		
		for(long i=0; i<n; i++) {
			for(long j=0; j<n; j++) {
				table[(int)((i*i*i + j*j*j) % n)]++;
			}
		}
		
		for(long k=0; k<n; k++) {
			if(twoPositiveCubes) {
				numWays += table[(int)((k*k*k + target)%n)];
			} else {
				numWays += table[(int)((k*k*k - target)%n)];
				
			}
		}
		
		//System.out.println("For n = " + n + ": " + (1.0 *numWays)/(maxRatio * n*n) + "   " + numWays + "/" + (n*n));
		if(numWays < maxRatio * n*n) {
			System.out.println("Seed number: " + n);
			System.out.println("For n = " + n + ": " + (1.0 *numWays)/(n*n) + "   " + numWays + "/" + (n*n));
			return true;
		} else {
			return false;
		}
		
	}
}

/*
 * After 3 hours:
 *
Seed number: 7
For n = 7: 0.5510204081632653   27/49
AHA! 7
Seed number: 9
For n = 9: 0.3333333333333333   27/81
AHA! 9
Seed number: 247
For n = 247: 0.6571981183104132   40095/61009
AHA! 247
*/

/*After 2 seconds:
Seed number: 7
For n = 7: 0.5510204081632653   27/49
AHA! 7
Seed number: 9
For n = 9: 0.3333333333333333   27/81
AHA! 9
Seed number: 13
For n = 13: 0.7988165680473372   135/169
AHA! 13
Seed number: 19
For n = 19: 0.8227146814404432   297/361
AHA! 19
Seed number: 37
For n = 37: 0.926953981008035   1269/1369
AHA! 37
Seed number: 43
For n = 43: 0.934559221200649   1728/1849
AHA! 43
Seed number: 67
For n = 67: 0.9563377144130096   4293/4489
AHA! 67
Seed number: 73
For n = 73: 0.9575905423156315   5103/5329
AHA! 73
Seed number: 103
For n = 103: 0.9696484117258931   10287/10609
AHA! 103
Seed number: 121
For n = 121: 0.9917355371900827   14520/14641
AHA! 121
Seed number: 151
For n = 151: 0.9792991535458971   22329/22801
AHA! 151
Seed number: 157
For n = 157: 0.9814596941052376   24192/24649
AHA! 157
Seed number: 163
For n = 163: 0.9806541458090255   26055/26569
AHA! 163
Seed number: 181
For n = 181: 0.983211745673209   32211/32761
AHA! 181
Seed number: 199
For n = 199: 0.9852023938789425   39015/39601
AHA! 199
Seed number: 211
For n = 211: 0.98548999348622   43875/44521
AHA! 211
Seed number: 229
For n = 229: 0.9864800442401938   51732/52441
AHA! 229
Seed number: 271
For n = 271: 0.9893247640963494   72657/73441
AHA! 271
Seed number: 307
For n = 307: 0.9900582499549067   93312/94249
AHA! 307
Seed number: 337
For n = 337: 0.991141948947336   112563/113569
AHA! 337
*/