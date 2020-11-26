package mpmpPascalsTriangleSingmaster;

import java.util.HashMap;

//https://aperiodical.com/2013/01/open-season-singmasters-conjecture/

//The bar: 3.5 x 10^204
public class solveMPMPPascalTrigRepeats {

	//Idea:
	//try mod a big prime 1st...
	//keep a list of up to 10^6 #s....
	//start with choose 2, then 3, then 4
	
	
	//Also: make a hash tree that keeps the lowest #. (and the choose associated with it...)
	//Only deal with the left half of triangle... (and center of it)
	
	
	//Idea: start with brute force
	
	public static long MOD = 10000000019L;
	
	public static HashMap<Long, Integer> tally = new HashMap<Long, Integer>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long pascalsTriangle[][] = new long[1000][1000];
		
		for(int i=0; i<pascalsTriangle.length; i++) {
			for(int j=0; j<pascalsTriangle[0].length; j++) {
				pascalsTriangle[i][j] = 0;
			}
		}
		pascalsTriangle[0][0] = 1;
		
		for(int i=1; i<pascalsTriangle.length; i++) {
			for(int j=1; j<=i; j++) {
				pascalsTriangle[i][j] = (pascalsTriangle[i-1][j] + pascalsTriangle[i-1][j-1]) % MOD;
				
				if(pascalsTriangle[i][j] != 1) {
					if(tally.get(pascalsTriangle[i][j]) == null) {
						tally.put(new Long(pascalsTriangle[i][j]), new Integer(1));
						
					} else {
						tally.put(new Long(pascalsTriangle[i][j]), new Integer(tally.get(pascalsTriangle[i][j]) + 1));
						
						int numTimes = tally.get(pascalsTriangle[i][j]);
						if(numTimes >= 6) {
							System.out.println("(" + i + " ," + j + ") --> " + pascalsTriangle[i][j] + " (happens " + numTimes + " times)");
							
						}
						
					}
				}
			}
			pascalsTriangle[i][0] = 1;
		}
		
		for(int i=0; i<10; i++) {
			for(int j=0; j<=i; j++) {
				sop(pascalsTriangle[i][j] + " ");
			}
			
			System.out.println();
			//System.out.println("For row " + i + ": " + row);
		}
		
	}
	
	public static void sop(String input) {
		System.out.print(input);
	}
	

}
