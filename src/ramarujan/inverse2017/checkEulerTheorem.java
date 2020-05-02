package ramarujan.inverse2017;

import java.math.BigInteger;

import UtilityFunctions.*;

public class checkEulerTheorem {
	public static void main(String[] args) {
		int currentTotient;
		
		for(long i=1; i<1000; i++) {
			currentTotient = (int)UtilityFunctions.getTotient(i);
			
		
			
			for(int j=1; j<i; j++) {
				int temp = (int)UtilityFunctions.getAPowerPmodMOD(new BigInteger(j + ""), new BigInteger(currentTotient + ""), new BigInteger(i + "")).longValue();
				if(UtilityFunctions.getGCD( i,  j) == 1 && temp != 1) {
					System.out.println("AHH!");
					System.exit(1);
					
				}
			}
			if((currentTotient + 1 ) % 5 == 0) {
				if(modRootReversiblePow5((int)i)) {
					System.out.println(i + ": good");
					
				} else {
					System.out.println(i + ": bad");
				}
			}
			if(modRootReversiblePow5((int)i)) {
				System.out.println(i + ": mystery good");
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