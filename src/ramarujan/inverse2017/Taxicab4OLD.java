package ramarujan.inverse2017;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import ramarujan.UtilityFunctions;

/*
 * 10000005
10000006
10000009
10000015

 */; 
//TODO: make this the baseline and compare improvements to it.
 
 //TODO: make second third fourth mod list bigger than 1st. Might create a speed up
public class Taxicab4OLD {

	
	public static int NUM_POWER = 5;
	
	public static int NUM_MODS_LISTS = 4;
	//Start with 10 million.
	public static int RADIUS = 100000;
	
	public static int INITIAL_SPACE_FOR_ELEMENTS = 1;
	
	public static void main(String[] args) {
		
	
		int mods[] = new int[NUM_MODS_LISTS];
		for(int i=0; i<NUM_MODS_LISTS; i++) {
			mods[i] = 0;
		}
		int modsFound = 0;

		if(RADIUS == 10000000) {
			mods[0] = 10000005;
			mods[1] = 10000006;
			mods[2] = 10000009;
		}
		
		CHECK_NEXT_ROOT:
		for(int currentMod = RADIUS; mods[NUM_MODS_LISTS - 1] <= 0 && modsFound < NUM_MODS_LISTS; currentMod++) {
			boolean checkTable[] = new boolean[currentMod];
			for(int i=0; i<checkTable.length; i++) {
				checkTable[i] = false;
			}
			
			for(int i=0; i<checkTable.length; i++) {
				int powmod = (int)UtilityFunctions.getAPowerPmodMOD(new BigInteger(i + ""), new BigInteger(NUM_POWER + ""), new BigInteger(currentMod + "")).longValue();
				
				if(checkTable[powmod]) {
					continue CHECK_NEXT_ROOT;
				} else {
					checkTable[powmod] = true;
				}
				
			}
			
			System.out.println(currentMod);
			//At this point, we found a good one
			mods[modsFound] = currentMod;
			modsFound++;
		}
		
		//10000005
		//10000006
		//10000009
		System.out.println("End finding mods");
		int arrayForw[][] = new int[NUM_MODS_LISTS][1];
		int arrayBack[][] = new int[NUM_MODS_LISTS][1];
		
		for(int i=0; i<NUM_MODS_LISTS ;i++) {
			arrayForw[i] = new int[mods[i]];
			arrayBack[i] = new int[mods[i]];
			for(int j=0; j<arrayForw[i].length; j++) {
				
				int powmod = (int)UtilityFunctions.getAPowerPmodMOD(new BigInteger(j + ""), new BigInteger(NUM_POWER + ""), new BigInteger(mods[i] + "")).longValue();
				
				arrayForw[i][j] = powmod;
				arrayBack[i][powmod] = j;
			}
		}
		
		//TODO: remove pre cond later?
		//pre: modlist 1 < modlist2  < modlist3
		//I did it for my convenience... I might need to change it later...
		
		
		//test to see if we have enough space. Answer: yes!
		
		int a1;
		int b1;
		int b1pow;
		
		
		for(int modAnswer = 0; modAnswer<mods[0]; modAnswer++) {
			if(mods[0] > 1000) {
				System.out.println("modAnswer: " + modAnswer);
			}
			//Initialize scratch list:
			
			//templist[i][j][0] is the list size. (the mini arrays are scalable)
			int templist[][][] = new int[NUM_MODS_LISTS - 1][mods[NUM_MODS_LISTS - 1]][1 + INITIAL_SPACE_FOR_ELEMENTS];
			for(int i=0; i<templist.length; i++) {
				for(int j=0; j<templist[0].length; j++) {
					templist[i][j][0] = 0;
				}
			}
			
			
			//Trying every a1^5 mod mods[0]:
			for(int a1pow = 0; a1pow<mods[0]; a1pow++) {
				
				
				b1pow = (mods[0] + modAnswer - a1pow) % mods[0];
				
				a1 = arrayBack[0][a1pow];
				b1 = arrayBack[0][b1pow];
				
				//To save time, let's only search when a1 <= b1:
				if(b1 < a1) {
					continue;
				}
				
				//Look at the next NUM_MODS-1 and check if a^5 + b^5 could match something
				
				int indexTempList[][] = new int[NUM_MODS_LISTS-1][0];
				for(int i=0; i<NUM_MODS_LISTS-1; i++) {
					indexTempList[i] = templist[i][(arrayForw[i + 1][a1] + arrayForw[i + 1][b1]) % mods[i+1]];
				}
				
				//TODO: to make it faster, maybe swap the lists such that it goes from smallest size to biggest size.
				//TODO: if 1 of the lists are empty, skip!
				
				//For every potential answer in the first list, see if it's there in the second, third, fourth... list
				for(int i=1; i<=indexTempList[0][0]; i++) {
					int numSearch = indexTempList[0][i];

					boolean foundMatch = true;
					
					NEXT_LIST:
					for(int j=1; j<indexTempList.length; j++) {
						foundMatch = false;
						
						//If there's no potential answer for one of the modlists, give up!
						if(indexTempList[j][0] <= 0) {
							break;
						}
						
						for(int k=1; k<=indexTempList[j][0]; k++) {
							
							if(numSearch == indexTempList[j][k]) {
								foundMatch = true;
								continue NEXT_LIST;
							}
							
							if(k == indexTempList[j][0]) {
								foundMatch = false;
								break NEXT_LIST;
							}
						}
					}
					
					if(foundMatch) {
						//Check for match!
						
						int c1 = numSearch;
						int c1pow = arrayForw[0][c1];
						int d1pow = (mods[0] + modAnswer - c1pow) % mods[0];
						int d1 = arrayBack[0][d1pow];
						
						System.out.println("Checking if 0:");
						System.out.println(a1 +"^5  +  " + b1 + "^5 - " + c1 + "^5  - " + d1 + "^5");
						System.exit(1);
					}
					
				}
				
				
				//Adding element to the templist for search... and reimplement array lists :(
				//LATER: the first element of the list could be its length. (C style arrays)
				
				for(int i=0; i<NUM_MODS_LISTS-1; i++) {
					int indexPow5ModList = (arrayForw[i + 1][a1] + arrayForw[i + 1][b1]) % mods[i+1];
					int numElements = templist[i][indexPow5ModList][0];
					
					if(numElements >= templist[i][indexPow5ModList].length -1 ) {
						//GROW ARRAY:
						
						int newArray[] = new int[3*numElements];
						for(int j=0; j<templist[i][indexPow5ModList].length; j++) {
							newArray[j] = templist[i][indexPow5ModList][j];
						}
						templist[i][indexPow5ModList] = newArray;
					}
					
					//ADD ELEMENT TO ARRAY:
					templist[i][indexPow5ModList][numElements + 1] = a1;
					//Increase size of the list:
					templist[i][indexPow5ModList][0]++;
				}
				
				//TODO: to test, modify the forw and back functions and sneak an actual answer into it, then run the program
				
				
			}

		}
		
		for(int i=0; i<NUM_MODS_LISTS; i++) {
			System.out.println("mods[" + i + "]: " + mods[i]);
		}
	}
	
	//A030051		Numbers from the 290-theorem.
	
	//Older finds:
	//For 5:
			// There isn't any!
			//Related:
			//A257303, A052274
			
			//A052274 is cool. Look at the graph.
			//N. J. A. Sloane, Feb 05 2000
	
	/*
	//Testing sizes;
	int array[] = new int[RADIUS];
	int array2[] = new int[RADIUS];
	int array3[] = new int[RADIUS];

	for(int i=0; i<array.length; i++) {
		array[i] = i;
		array2[i] = i+ 1;
		array3[i] = i +2;
	}
	System.out.println(array[3443]);
	System.out.println(array2[3443]);
	System.out.println(array3[3443]);
	*/
	
	
}

//After running this for days, I conclude that:
/*If a solution to a^5+b^5 = c^5 + d^5 exists, one of the terms is over a million.
*/