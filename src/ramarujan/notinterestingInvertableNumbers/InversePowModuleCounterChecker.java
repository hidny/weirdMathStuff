package ramarujan.notinterestingInvertableNumbers;

import java.math.BigInteger;

import ramarujan.Utility;
import ramarujan.UtilityFunctions;

public class InversePowModuleCounterChecker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//int POWER =31;
		//int MOD = 1000;
		
		int MAX_MOD = 10000;
		//int MIN_MOD = 10000;
		
		boolean is5Inversible;
		int count1 = 0;
		int count2 = 0;
		
		int mod;
		
		for(int power = 3; power < 100; power++) {
			for(mod=2; mod<MAX_MOD; mod++) {
				
				boolean taken[] = new boolean[mod];
				for(int i=0; i<mod; i++) {
					taken[i] = false;
				}
				
				is5Inversible = true;
				
				for(int i=0; i<mod; i++) {
					//System.out.println(getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + mod)));
					int temp = (int)Utility.getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + power), new BigInteger("" + mod)).longValue();
					if(taken[temp] == true) {
						is5Inversible = false;
						break;
					}
					taken[temp] = true;
				}
				
				
				if(is5Inversible) {
					//System.out.println(mod);
					count1++;
				} else {
					//System.out.println("   " + mod);
					count2++;
					
				}
				
				
			}
			
			if(UtilityFunctions.isPrime(power) == false && count2 < MAX_MOD/2) {
				System.out.println("Type 1:");
				System.out.println("For power of " + power + ": invertable " + count1);
				
				System.out.println("Not " + power + " invertable " + count2);
				
			} else if(UtilityFunctions.isPrime(power) && count1 < MAX_MOD/2) {
				System.out.println("Type 2:");
				System.out.println("For power of " + power + ": invertable " + count1);
				
				System.out.println("Not " + power + " invertable " + count2);
			}
			
			//System.out.print(".");
			
			count1 = 0;
			count2 = 0;
			
		}
		//For 3:
		//A074243		Numbers n such that every integer has a cube root mod n.
		
		//For 5:
		// There isn't any!
		
		//For my pattern:
		//3, 5, 49... (sloan doesn't have it)
		
		//If the power m is prime, then over half the modules have roots of that power
		//else less than half the modules have roots of that power
		//Except for:
		//3, 5, 7,...
		//It might degrade as MAX_MOD increses
	}

}
