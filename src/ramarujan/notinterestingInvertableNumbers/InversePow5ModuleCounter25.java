package ramarujan.notinterestingInvertableNumbers;

import java.math.BigInteger;

import ramarujan.Utility;

public class InversePow5ModuleCounter25 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int POWER =5;
		
		int MAX_MOD = 25;
		
		boolean is5Inversible;
		int count1 = 0;
		int count2 = 0;
		
		int mod;
		
		for(mod=25; mod<=MAX_MOD; mod++) {
			System.out.println("Mod " + mod + ":");
			boolean taken[] = new boolean[mod];
			for(int i=0; i<mod; i++) {
				taken[i] = false;
			}
			
			is5Inversible = true;
			
			for(int i=0; i<mod; i++) {
				//System.out.println(getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + mod)));
				int temp = (int)Utility.getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + mod)).longValue();
				if(taken[temp] == true) {
					is5Inversible = false;
					//break;
				}
				System.out.println("i = " + i + ": " + temp);
				taken[temp] = true;
			}
			
			
			if(is5Inversible) {
				System.out.println(mod);
				count1++;
			} else {
				//System.out.println("   " + mod);
				count2++;
				
			}
			
			
		}
		

		System.out.println("For power of " + POWER + ": invertable " + count1);
		
		System.out.println("Not " + POWER + " invertable " + count2);

	
		//For 3:
		//A074243		Numbers n such that every integer has a cube root mod n.
		
		//For 5:
		// There isn't any!
		//Related:
		//A257303, A052274
		
		//A052274 is cool. Look at the graph.
		//N. J. A. Sloane, Feb 05 2000
		

//99987
//99989
//For power of 3: invertable 20004
//Not 3 invertable 79994

		
	}

}
