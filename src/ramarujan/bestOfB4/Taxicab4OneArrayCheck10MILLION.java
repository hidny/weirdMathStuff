package ramarujan.bestOfB4;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import ramarujan.UtilityFunctions;

//Didn't check anything yet.
//checked up 1 million
//
/* * 10000005

10000006
10000009
10000015

//10M : up to 34K
 * 1CPU will take about 150 days
 */; 
//TODO: make this the baseline and compare improvements to it.
 
 //TODO: make second third fourth mod list bigger than 1st. Might create a speed up
public class Taxicab4OneArrayCheck10MILLION {

	//NUM_POWER should be 5 to tackle the challenge,
	//but it could also be a prime greater than 2
	public static int NUM_POWER = 5;
	
	//3 gets lots of answers.
	//4 gets almost no answers (2 checks is rigorous)
	//5+ will probably only show an answer if it's real.
	public static int NUM_MODS_LISTS = 4;
	//Start with 10 million.
	public static int RADIUS = 10000000;
	
	public static double CHECK_LIST_MULT = 2.0;
	
	public static int EXTRA_SPACE = 10000;
	//Trying:
	//10000005
	//20000011
	//20000018
	//20000021
	
	public static double HighTermFactor = Math.pow(2.0, 1.0/NUM_POWER);
	
	
	public static void main(String[] args) {
		
		if(NUM_MODS_LISTS < 2) {
			System.out.println("ERROR: this program needs at least 2 lists: one list to go through possibilities, and one check list!");
			System.exit(1);
		}
	
		System.out.println("root " + NUM_POWER + "  of 2:" + HighTermFactor);
		
		int mods[] = new int[NUM_MODS_LISTS];
		for(int i=0; i<NUM_MODS_LISTS; i++) {
			mods[i] = 0;
		}
		int modsFound = 0;

		CHECK_NEXT_ROOT:
		for(int currentMod = RADIUS; mods[NUM_MODS_LISTS - 1] <= 0 && modsFound < NUM_MODS_LISTS; currentMod++) {
			
			//Don't want to use a multiple of NUM_POWER, because we want to filter by mod NUM_POWER later
			if(currentMod % NUM_POWER ==0) {
				continue;
			}
			
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
			
			//I think the checklist should be bigger than the radius by a certain factor.
			//I'll first try 4:
			if(modsFound == 1) {
				currentMod = (int)(currentMod*CHECK_LIST_MULT);
				
			//For the double and triple, and fourth... checklist, the size shouldn't be too big.
				//So I put it back down the the same scale as the radius:
			} else if(modsFound == 2) {
				currentMod = (int)(currentMod/CHECK_LIST_MULT) + 1;
			}
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
			if(mods[0] > 1000 && modAnswer % 10 == 0) {
				System.out.println("modAnswer: " + modAnswer);
			}
			//Initialize scratch list:
			
			//Redeclaring seems to make it faster...
			//I think java
			//Adding EXTRA_SPACE because I don't feel like adding the logic to wrap around the array after a hash collision
			//hopefully, there won't be so much collisions at the end of the array such that the program tries to insert something
			//beyond the extra space
			int templist[] = new int[mods[1] + EXTRA_SPACE];

			

			//Trying every a1^5 mod (mods[0]) except a1^5 = 0 because 0^5 + 0^5 will get confused with null
			for(int a1pow = 1; a1pow<mods[0]; a1pow++) {
				
				// Find a1 and b1:
				b1pow = (mods[0] + modAnswer - a1pow) % mods[0];
				
				a1 = arrayBack[0][a1pow];
				b1 = arrayBack[0][b1pow];
					
				//To save time, let's only search when a1 <= b1:
				if(b1 < a1) {
					continue;
				}
				
				
				//Look at the the check list and check if a^5 + b^5 could match something:
				int firstIndex = (arrayForw[1][a1] + arrayForw[1][b1]) % mods[1];
				
				int i = firstIndex;
				for(; i<templist.length; i++) {
					if(templist[i] == 0) {
						break;
					}
					
					int d1 = templist[i];
					
					//if a1^5 + b1^5 = b1^5 + d1^5 and b1 > a1 and d1>b1
					//d1<b1 * 5th root of 2
					//and vice-versa
					if( (d1 > (HighTermFactor * b1 + 1)) || (HighTermFactor * d1 < (b1 - 1)) ) {
						continue;
					}
					
					int c1pow = (mods[1] + firstIndex - arrayForw[1][d1]) % mods[1];

					int c1 = arrayBack[1][c1pow];
					
					//If num_power is prime,
					//then a1^NUM_POWER = a (mod NUM_POWER)
					//That's an eazy way to weed out possibilities
					if((a1 + b1 - c1 - d1) % NUM_POWER != 0) {
						continue;
					}
					
					
					
					if(c1 >= mods[0]) {
						
						//Collision Sanity check: (this is a waste of time, but makes me feel better)
						//If c1 >= mods[0], we know that it's because of a hash collision:
						//and when we entered c1^5 + d1^5 into the table, it was not firstIndex % mods[1];
						//Sanity check:
						/*
						if( i >0 && i == firstIndex && templist[firstIndex-1] == 0) {
							System.out.println("AAHHH! Something is broken! Collision with no previous entry");
							System.exit(1);
						}
						
						boolean foundReasonableC1 = false;
						for(int j=i; j>=0 && templist[j] != 0; j--) {
							c1pow = (mods[1] + j - arrayForw[1][d1]) % mods[1];
							c1 = arrayBack[1][c1pow];
							
							if(c1 < mods[0]) {
								foundReasonableC1 = true;
							}
							
						}
						
						if(foundReasonableC1 == false) {
							System.out.println("AAHHH! Something is broken! Collision with no reasonable C1");
							System.exit(1);
						}
						*/
						//END Collision Sanity check:
						
						continue;
					}
					
					//Check with the other mods:
					boolean foundMatch = true;
					
					//At this double and triple check with other mods:
					for(int j=2; j<NUM_MODS_LISTS; j++) {
						if( (arrayForw[j][a1] + arrayForw[j][b1] - arrayForw[j][c1] - arrayForw[j][d1]) % mods[j] != 0) {
							foundMatch =false;
							break;
						}
						
					}
					
					if(foundMatch) {
						System.out.println("Checking if 0:");
						System.out.println("Note the mod number 0 (" + mods[0] + ")  need not be 0 because of the hacky way I handled hash collisions in the check list");
						System.out.println("The rest of the mods are there to double check that a1^5 + b^5 = c^5 +d^5  is possible (NUM_POWER = 5 in this example)");
						for(int mod=0; mod<NUM_MODS_LISTS; mod++) {
							System.out.println("module number " + mod + ": " + mods[mod]);
						}
						System.out.println(a1 +"^" + NUM_POWER +" +  " + b1 + "^" + NUM_POWER +  " - " + c1 + "^" + NUM_POWER +  "  - " + d1 + "^" + NUM_POWER);
						
						if(NUM_POWER==3) {
							System.out.println(Math.pow(a1, 3) + Math.pow(b1, 3));
						}
						boolean foundCollision = false;
						
						//Collision Sanity check:
						for(int j=0; j<2; j++) {
							if( (arrayForw[j][a1] + arrayForw[j][b1] - arrayForw[j][c1] - arrayForw[j][d1]) % mods[j] != 0) {
								System.out.println("mods[" + j + "] doesn\'t work, but that\'s ok!");
								foundCollision = true;
								if(j==1) {
									System.out.println("Error: equation for checklist doesn\'t work");
									System.exit(1);
								}
							}
							if(foundCollision && j==1) {
								System.out.println("Debug collision");
							}
						
						}
						//End collision Sanity check:
						if(NUM_POWER > 3) {
							System.exit(1);
						}
						System.out.println("Debug");
					}
				}
				
				
				//a1^5 + b1^5 = c1^5 +d1^5)
				//Add b1 to the list here: (b1 > a1 and is more useful for filtering:) (
				templist[i] = b1;
				
				//TODO: to test, modify the forw and back functions and sneak an actual answer into it, then run the program
				
				
			}

		}
		
		for(int i=0; i<NUM_MODS_LISTS; i++) {
			System.out.println("mods[" + i + "]: " + mods[i]);
		}
		System.out.println("End");
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

//{y1729, y4104, y13832, 20683, 32832, y39312, 40033, y46683, y64232, 65728, y110656,
//y110808, 134379, 149389, 165464, 171288, 195841, 216027, 216125, 262656, 314496, 320264, y327763, ...}
