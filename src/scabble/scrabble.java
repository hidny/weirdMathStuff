package scabble;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

import number.IsNumber;

public class scrabble {

	//Matt Parker problem:
	//How many ways, from the 100 standard scrabble tiles, can you choose seven which total 46 points?
	
	public static int NUM_DISTINCT_LETTERS = 26;
	public static int NUM_DISTINCT_OTHER = 1;
	public static int NUM_TILES = 100;
	
	public static int NUM_TILES_IN_HAND = 7;
	
	//public static int TARGET_VALUE = 46;
	//public static int TARGET_VALUE = 46;
	//public static int TARGET_VALUE = 26;
	
	public static char letters[] = new char[NUM_TILES];
	
	
	public static int NUM_DISTINCT_TILES = NUM_DISTINCT_LETTERS + NUM_DISTINCT_OTHER;
	
	public static char distinctLetterList[] = new char[NUM_DISTINCT_TILES];
	public static int values[] = new int[NUM_DISTINCT_TILES];
	public static int numTilesPerChar[] = new int[NUM_DISTINCT_TILES];
	
	public static ArrayList<String> finalList = new ArrayList<String>();
	
	public static long comboCount = 0;
	public static long combinations[][] = UtilityFunctions.UtilityFunctions.createPascalTriangle(NUM_TILES + 2);
	
	
	
	public static void main(String[] args) {
		Scanner in;
		try {
			 in = new Scanner(new File("scrabbleCounts.txt"));
			
			ArrayList <String>lines = new ArrayList<String>();
			
			String line;
			while(in.hasNextLine()) {
				line = in.nextLine();
				lines.add(line);
				
			}
			
			int currentIndexLetters = 0;
			
			for(int i=0; i<lines.size(); i++) {
				String token[] = lines.get(i).split(" ");
				
				int num_tiles = pint(token[0]);
				char c = token[2].charAt(0);

				int indexChar = getIndexChar(c);
				
				int value = pint(token[4]);
				
				for(int j=0; j<num_tiles; j++) {
					letters[currentIndexLetters + j] = c;
				}
				currentIndexLetters += num_tiles;
				
				distinctLetterList[indexChar] = c;
				values[indexChar] = value;
				numTilesPerChar[indexChar] = num_tiles;
				
			}
			
			sopl("SETUP:");
			for(int i=0; i<10; i++) {
				for(int j=0; j<10; j++) {
					sop(letters[10*i+j]+ " ");
				}
				sopl();
			}
			sopl();
			
			for(int i=0; i<10; i++) {
				for(int j=0; j<10; j++) {
					sop(letters[10*i+j]+ " " + values[getIndexChar(letters[10*i+j])] + "  ");
				}
				sopl();
			}
			sopl();
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 1);
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 2);
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 3);
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 4);
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 5);
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 6);
			//duplicateSolutionsWithNDistinctTilesInHand(NUM_TIMES_IN_HAND, 7);
			//distinctSolutions();
			
			//duplicateSolutionsWithNDistinctTilesInHand(3, 3);
			
			//Just find the number of distinct ways for all values from 1 to 70:
			for(int i=0; i<=70; i++) {
				int answer = getNumSolutions(i, NUM_TILES_IN_HAND);
				System.out.println(i + ":" + answer);
			}
			//sopl("Final answer:");
			in.close();
			
			//138 solutions found...
			/*java.util.Collections.sort(finalList);
			for(int i=0; i<finalList.size(); i++) {
				//sopl((i+1) + ":" + finalList.get(i));
				sopl(finalList.get(i));
			}*/
			
			sopl("Sanity check:");
			sopl("Total number of ways to get to 7 tiles out of 100 tiles:");
			sopl("Expected is (NUM_TILES choose num_tiles_in_hand) = (100 choose 7) = 16007560800. ");
			sopl("Actual number of ways found: " +  comboCount + ".");

			if(comboCount != combinations[NUM_TILES][NUM_TILES_IN_HAND]) {
				sopl("They don't match!");
			} else {
				sopl("They match!");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public static int getNumSolutions(int TARGET_VALUE, int numTilesinHand) {
		int ret = 0;
		for(int i=1; i<=numTilesinHand; i++) {
			ret += duplicateSolutionsWithNDistinctTilesInHand(TARGET_VALUE, numTilesinHand, i);	
		}
		return ret;
	}

	
	public static int duplicateSolutionsWithNDistinctTilesInHand(int TARGET_VALUE, int numTilesinHand, int numDistinctTilesInHand) {
		
		if(numDistinctTilesInHand == 0) {
			sopl("ERROR: impossible to have 0 distinct tiles in hand");
			System.exit(1);
		}
		if(numTilesinHand < numDistinctTilesInHand) {
			sopl("ERROR: you reversed the params in duplicateSolutionsWithNDistinctTilesInHand");
			System.exit(1);
		}

		
		HashSet listOfSortedSolutions = new HashSet();
	
		//Get distinct only
		boolean combo[] = new boolean[NUM_DISTINCT_TILES];
		for(int i=0; i<combo.length; i++) {
			combo[i] = false;
		}
		
		for(int i=0; i<numDistinctTilesInHand; i++) {
			combo[i] = true;
		}
		
		while(combo != null) {
			
				
			boolean dupCombo[] = new boolean[numTilesinHand - 1];
			
			for(int i=0; i<dupCombo.length; i++) {
				dupCombo[i] = false;
			}
			for(int i=0; i<numTilesinHand - numDistinctTilesInHand; i++) {
				dupCombo[i] = true;
			}
			
			while(dupCombo != null) {
			
				String sortedTiles = "";
				int value = 0;
				
				boolean notEnoughTiles = false;
				
				int dupIndex=0;
				
				for(int i=0; i<combo.length && value <= TARGET_VALUE; i++) {
					
					int numDupLetterUsed = 0;
					if(combo[i] == true) {
						value +=  values[i];
						sortedTiles += distinctLetterList[i];
						
						numDupLetterUsed++;
						
						if(numDupLetterUsed > numTilesPerChar[i]) {
							notEnoughTiles = true;
						}
						
						while(dupIndex < dupCombo.length && dupCombo[dupIndex] == true) {
							value +=  values[i];
							sortedTiles += distinctLetterList[i];
							dupIndex++;
							numDupLetterUsed++;
							
							if(numDupLetterUsed > numTilesPerChar[i]) {
								notEnoughTiles = true;
							}
						}
						dupIndex++;
					}
				}
				
				
				
				if(value == TARGET_VALUE) {
					
					//Adding duplicates to sets aren't allowed:
					if(listOfSortedSolutions.contains(sortedTiles) == false) {
						

						if(notEnoughTiles) {

							//Added if condition because of the println
							//sopl("FAKE SOLUTION: ");
							//sopl(sortedTiles);
							
						} else {
						
							//Testing combo count:
							// should add up to (NUM_TILES choose num_tiles_in_hand) = (100 choose 7) = 16007560800
							comboCount += numCombinations(combo, dupCombo);
							//END TESTING
							
							listOfSortedSolutions.add(sortedTiles);
							
							//TESTING
							//sopl(sortedTiles);
							//for(int i=0; i<sortedTiles.length(); i++)  {
							//	sopl(sortedTiles.charAt(i) + ": " + values[getIndexChar(sortedTiles.charAt(i))]);
							//}
							//sopl();
						}
					}
				}
				
				dupCombo = UtilityFunctions.UtilityFunctions.getNextCombination(dupCombo);
			}
			combo = UtilityFunctions.UtilityFunctions.getNextCombination(combo);
			
		}
		Iterator it = listOfSortedSolutions.iterator();
		while(it.hasNext()) {
			finalList.add((String)it.next());
		}
		//sopl();
		//sopl("Final size with " + numDistinctTilesInHand + " distinct letter:");
		//sopl(listOfSortedSolutions.size());
		
		return listOfSortedSolutions.size();
	}
	
	
	public static long numCombinations(boolean combo[], boolean dupCombo[]) {
		
		long ret = 1;
		
		int dupIndex = 0;
		for(int i=0; i<combo.length; i++) {
			
			int numDupLetterUsed = 0;
			
			if(combo[i] == true) {
				//sortedTiles += distinctLetterList[i];
				
				numDupLetterUsed++;
				
				if(numDupLetterUsed > numTilesPerChar[i]) {
					sopl("ERROR: solution does not have enough tiles in game... WHAT?");
					System.exit(1);
				}
				
				while(dupIndex < dupCombo.length && dupCombo[dupIndex] == true) {
					//sortedTiles += distinctLetterList[i];
					dupIndex++;
					numDupLetterUsed++;
					
					if(numDupLetterUsed > numTilesPerChar[i]) {
						sopl("ERROR: solution does not have enough tiles in game... WHAT?");
						System.exit(1);
						
					}
				}
				dupIndex++;
				
				//Count here:
				ret *= combinations[numTilesPerChar[i]][numDupLetterUsed];
			}
		}
		
		return ret;
	}
	
	public static int getIndexChar(char c) {
		int indexChar = -1;
		if(c == '_') {
			indexChar = 0;
		} else {
			indexChar = (int)(c - 'A' + 1);
		}
		return indexChar;
	}

	public static void sop(Object a) {
		System.out.print(a.toString());
	}
	public static void sopl(Object a) {
		System.out.println(a.toString());
	}

	public static void sopl() {
		System.out.println();
	}
	
	public static int pint(String s) {
		if (IsNumber.isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			sop("Error: (" + s + " is not a number");
			return -1;
		}
	}
	
	
}
