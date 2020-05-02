package ramarujan.bestSoFar;

import java.math.BigInteger;

import UtilityFunctions.*;

public class checkEulerTheorem {
	public static void main(String[] args) {
		int currentTotient;
		
		for(long i=1; i<20000; i++) {
			currentTotient = (int)UtilityFunctions.getTotient(i);
			

			boolean iThinkReversable = true;
			long array[] = UtilityFunctions.getPrimeDivisors(i);
			long prevDivisor = 1;
			for(int j=0; j<array.length; j++) {
				if( i % (array[j]*array[j]) == 0) {
					iThinkReversable = false;
					break;
				} else if ( array[j] % 10 == 1) {
					iThinkReversable = false;
					break;
				}
				
			}
			
			for(int j=1; j<i; j++) {
				int temp = (int)UtilityFunctions.getAPowerPmodMOD(new BigInteger(j + ""), new BigInteger(currentTotient + ""), new BigInteger(i + "")).longValue();
				if(UtilityFunctions.getGCD( i,  j) == 1 && temp != 1) {
					System.out.println("AHH!");
					System.exit(1);
					
				}
			}
			if(iThinkReversable) {
				if(modRootReversiblePow5((int)i)) {
					//whatever
				} else {
					System.out.println(i + ": I thought it was reversable!");
					System.exit(1);
				}
			}
	
			if(iThinkReversable == false) {
				if(modRootReversiblePow5((int)i)) {
					System.out.println(i + ": I thought it was NOT reversable!");
					System.exit(1);
				} else {
					
				}
			}
			
			if(i%100 == 0) {
				System.out.println(i);
			}
		}
			
			
		}
		
		
	
	
	public static boolean modRootReversiblePow5(int currentMod) {
					
		boolean ret = true;
		
		boolean checkTable[] = new boolean[currentMod];
		for(int i=0; i<checkTable.length; i++) {
			checkTable[i] = false;
		}
		
		for(int i=0; i<checkTable.length; i++) {
			int powmod = (int)UtilityFunctions.getAPowerPmodMOD(new BigInteger(i + ""), new BigInteger(5 + ""), new BigInteger(currentMod + "")).longValue();
			
			if(checkTable[powmod]) {
				ret = false;
				break;
			} else {
				checkTable[powmod] = true;
			}
			
		}
		return ret;
	}
	
			
}
