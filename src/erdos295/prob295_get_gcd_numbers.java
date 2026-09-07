package erdos295;

import java.util.ArrayList;

public class prob295_get_gcd_numbers {

	
	//TODO: maybe move it to BigInteger in future.
	public static ArrayList<Long> getGCDNumbers(long min, long max, long multPrimesAvailable[], int numExpoPerPrime[]) {
		

		ArrayList<Long> ret = new ArrayList<Long>();
		
		
		return getGCDNumbers(min, max, multPrimesAvailable, numExpoPerPrime, 0, 0, 1, ret);
	}
	
	 private static ArrayList<Long> getGCDNumbers(long min, long max, long multPrimesAvailable[], int numExpoPerPrime[], int curIndex, int curIndex2, long curMult, ArrayList<Long> ret) {
		 
		 //TODO: think about this and then test it...
		 
		 //curMult*=multsAvailable[curIndex]
		 
		 if(mult < max) {
		 
			 if(curIndex2 < numExpoPerPrime[curIndex]) {
				 curIndex2++;
				 curMult *= multPrimesAvailable[curIndex];
			 } else {
				 curIndex++;
			 }
		 }
		 
		 return ret;
	 }
}
