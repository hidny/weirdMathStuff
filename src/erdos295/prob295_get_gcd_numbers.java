package erdos295;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class prob295_get_gcd_numbers {

	
	//TODO: use it and test it!
	public static HashMap<Long, Integer> getListPrimesAndExpos(ArrayList<Long> listDenomsUsed) {
		prob295Obj ret = new prob295Obj();
		
		HashMap<Long, Integer> factorAndExpo = new HashMap<Long, Integer>();
		
		int numPrimes = 0;
		
		for(int i=0; i<listDenomsUsed.size(); i++) {
			
			long divs[] = UtilityFunctions.UtilityFunctions.getPrimeDivisors(listDenomsUsed.get(i));
			
			for(int j=0; j<divs.length; j++) {
				
				int numExpo = 1;
				
				while(listDenomsUsed.get(i) % Math.pow(divs[j], numExpo + 1) == 0 ) {
					numExpo++;
				}
				
				if(factorAndExpo.containsKey(divs[j])) {
					
					int tmp = factorAndExpo.get(divs[j]);
					factorAndExpo.remove(divs[j]);
					
					factorAndExpo.put(divs[j], tmp + numExpo);
				} else {
					factorAndExpo.put(divs[j], numExpo);
					numPrimes++;
				}
			}
		}
			
		
		return factorAndExpo;
	}
	
	public static prob295Obj convertToLists(HashMap<Long, Integer> factorAndExpo) {
		prob295Obj ret = new prob295Obj();

		Iterator <Long>it = factorAndExpo.keySet().iterator();
		
		int numPrimes = factorAndExpo.size();
		
		ret.multPrimesAvailable = new BigInteger[numPrimes];
		ret.numExpoPerPrime = new int[numPrimes];
		
		for(int i=0; i<numPrimes; i++) {
			long next = it.next();
			ret.multPrimesAvailable[i] = new BigInteger(next + "");
			ret.numExpoPerPrime[i] = factorAndExpo.get(next);
		}
		
		if(it.hasNext()) {
			System.out.println("ERROR in getListPrimesAndExpos!");
			System.exit(1);
		}
		
		return ret;
	}
	
	
	public static ArrayList<BigInteger> getListOfNumbersToCheck(BigInteger min, BigInteger max, ArrayList<Long> listDenomsUsed, long k) {
		
		HashMap<Long, Integer> list1 = getListPrimesAndExpos(listDenomsUsed);

		BigInteger initMult = BigInteger.ONE;
		
		if(k > 1) {

			ArrayList<Long> listK = new ArrayList<Long>();
			listK.add(k);
			HashMap<Long, Integer> list2 = getListPrimesAndExpos(listK);
			
			Iterator <Long>it = list2.keySet().iterator();
			
			while(it.hasNext()) {
				
				long nextPrimeInK = it.next();
				
				int numExpos = -1;
				if(list1.containsKey(nextPrimeInK)) {
					numExpos = list1.get(nextPrimeInK) + list2.get(nextPrimeInK);
				} else {
					numExpos = list2.get(nextPrimeInK);
				}
				
				initMult = initMult.multiply(new BigInteger("" + nextPrimeInK).pow(numExpos));
				
				if(list1.containsKey(nextPrimeInK)) {
					list1.remove(nextPrimeInK);
				}
				
			}
		}
		
		prob295Obj obj = convertToLists(list1);
		
		return getGCDNumbers(
				 min,
				 max,
				 obj.multPrimesAvailable,
				 obj.numExpoPerPrime,
				 0,
				 initMult,
				 new ArrayList<BigInteger>()
		);
	}
	
	
	 private static ArrayList<BigInteger> getGCDNumbers(
			 BigInteger min,
			 BigInteger max,
			 BigInteger multPrimesAvailable[],
			 int numExpoPerPrime[],
			 int curIndex,
			 BigInteger curMult,
			 ArrayList<BigInteger> ret
	) {
		 
		 //TODO: think about this and then test it...
		 
		 if(curIndex >= multPrimesAvailable.length) {
			 if(curMult.compareTo(min) >= 0 && curMult.compareTo(max) <= 0) {
				 ret.add(curMult);
			 }
			 
			 return ret;
		 }
		 
		 BigInteger nextMult;
		 
		 //curMult*=multsAvailable[curIndex]
		 for(int i=0; i<=numExpoPerPrime[curIndex]; i++) {
			 nextMult = curMult.multiply(multPrimesAvailable[curIndex].pow(i));
			 
			 if(nextMult.compareTo(max) < 0) {
				 ret = getGCDNumbers(
						 min,
						 max,
						 multPrimesAvailable,
						 numExpoPerPrime,
						 curIndex + 1,
						 nextMult,
						 ret
				);
			 } else {
				 break;
			 }
		 }
		 
		 return ret;
	 }
	 
	 public static void main(String args[]) {
		 
		 ArrayList<Long> listDenomsUsed = new ArrayList<Long>();
		 
		 listDenomsUsed.add(20L);
		 listDenomsUsed.add(25L);
		 
		 prob295Obj obj = convertToLists(getListPrimesAndExpos(listDenomsUsed));
		 
		 System.out.println("20 and 25:");
		 for(int i=0; i<obj.multPrimesAvailable.length; i++) {
			 System.out.println(obj.multPrimesAvailable[i] + ": " + obj.numExpoPerPrime[i]);
		 }
	 }
}
