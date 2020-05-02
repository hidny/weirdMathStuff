package ramarujan.bestSoFar;

import java.math.BigInteger;

import UtilityFunctions.*;

public class checkMTTheorem2 {
	public static void main(String[] args) {
		int currentTotient;
		
		//TODO: prove this never hits AAAH
		
		for(long i=1; i<20000; i++) {
		
			if(UtilityFunctions.isPrime(i) && i % 10 == 1) {
				if(modRootPow5Has5Answers((int)i) ) {
					System.out.println(" 1 mod 10 has 5 roots for each power: " + i);
				} else {
					System.out.println(" AAAH 1 mod 10 has 5 roots for each power: " + i);
					System.exit(1);
					
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
	
	public static boolean modRootPow5Has5Answers(int currentMod) {
		
		boolean ret = true;
		
		int checkTable[] = new int[currentMod];
		for(int i=0; i<checkTable.length; i++) {
			checkTable[i] = 0;
		}
		
		for(int i=0; i<checkTable.length; i++) {
			int powmod = (int)UtilityFunctions.getAPowerPmodMOD(new BigInteger(i + ""), new BigInteger(5 + ""), new BigInteger(currentMod + "")).longValue();
			checkTable[powmod]++;
			
			
		}
		for(int i=1; i<checkTable.length; i++) {
			if(checkTable[i] !=0 && checkTable[i] != 5) {
				return false;
			}
		}
		return true;
	}
	
}
