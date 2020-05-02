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

public class scrabbleSequence {

	public static int NUM_DISTINCT_LETTERS = 26;
	public static int NUM_DISTINCT_OTHER = 1;
	public static int NUM_TILES = 100;
	
	public static int NUM_TIMES_IN_HAND = 7;
	
	public static int TARGET_VALUE = 48;
	//public static int TARGET_VALUE = 46;
	//public static int TARGET_VALUE = 26;
	
	public static char letters[] = new char[NUM_TILES];
	
	
	public static int NUM_DISTINCT_TILES = NUM_DISTINCT_LETTERS + NUM_DISTINCT_OTHER;
	
	public static char distinctLetterList[] = new char[NUM_DISTINCT_TILES];
	public static int values[] = new int[NUM_DISTINCT_TILES];
	public static int numTilesPerChar[] = new int[NUM_DISTINCT_TILES];
	
	public static ArrayList<String> finalList = new ArrayList<String>();
	
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
			
			int answer = duplicateSolutions(NUM_TIMES_IN_HAND);
			sopl("Final answer:");
			sopl(answer);
			in.close();
			
			//138 solutions found...
			java.util.Collections.sort(finalList);
			for(int i=0; i<finalList.size(); i++) {
				//sopl((i+1) + ":" + finalList.get(i));
				sopl(finalList.get(i));
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	
	public static int duplicateSolutions(int numTilesinHand) {
		int ret = 0;
		for(int i=1; i<=numTilesinHand; i++) {
			ret += duplicateSolutionsWithNDistinctTilesInHand(numTilesinHand, i);
			
		}
		return ret;
	}

	
	public static int duplicateSolutionsWithNDistinctTilesInHand(int numTilesinHand, int numDistinctTilesInHand) {
		
		if(numDistinctTilesInHand == 0) {
			sopl("ERROR: impossible to have 0 distinct tiles in hand");
			System.exit(1);
		}
		if(numTilesinHand < numDistinctTilesInHand) {
			sopl("ERROR: you reversed the params in duplicateSolutionsWithNDistinctTilesInHand");
			System.exit(1);
		}

		
		HashSet list = new HashSet();
		
	
		//Get distinct only
		boolean combo[] = new boolean[NUM_DISTINCT_TILES];
		for(int i=0; i<combo.length; i++) {
			combo[i] = false;
		}
		
		for(int i=0; i<numDistinctTilesInHand; i++) {
			combo[i] = true;
		}
		int count = 0;
		
		while(combo != null) {
			count++;
			
				
			boolean dupCombo[] = new boolean[numTilesinHand];
			
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
						
						//TODO: watch out!
						while(dupCombo[dupIndex] == true) {
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
				
				
				//sopl(value);
				
				//sopl(sortedTiles);
				if(value == TARGET_VALUE) {
					
					//Added if condition because of the println
					//(adding duplicates to sets aren't allowed)
					if(list.contains(sortedTiles) == false) {
						

						if(notEnoughTiles) {
							
							//sopl("FAKE SOLUTION: ");
							//sopl(sortedTiles);
							
						} else {
						
							list.add(sortedTiles);
							sopl(sortedTiles);
							for(int i=0; i<sortedTiles.length(); i++)  {
								sopl(sortedTiles.charAt(i) + ": " + values[getIndexChar(sortedTiles.charAt(i))]);
							}
							sopl();
						}
					}
				}
				
				dupCombo = UtilityFunctions.UtilityFunctions.getNextCombination(dupCombo);
			}
			combo = UtilityFunctions.UtilityFunctions.getNextCombination(combo);
			
		}
		Iterator it = list.iterator();
		while(it.hasNext()) {
			finalList.add((String)it.next());
		}
		sopl();
		sopl("Final size with " + numDistinctTilesInHand + " distinct letter:");
		sopl(list.size());
		
		return list.size();
	}
	
	
	public static void distinctSolutions() {
		

		HashSet list = new HashSet();
		
		//Get distinct only
		boolean combo[] = new boolean[NUM_DISTINCT_TILES];
		for(int i=0; i<combo.length; i++) {
			combo[i] = false;
		}
		
		for(int i=0; i<NUM_TIMES_IN_HAND; i++) {
			combo[i] = true;
		}
		int count = 0;
		
		while(combo != null) {
			count++;
			
			if(count %10000 == 0) {
				sopl(count);
			}
				
			String sortedTiles = "";
			int value = 0;
			
			for(int i=0; i<combo.length && value <= TARGET_VALUE; i++) {
				if(combo[i] == true) {
					value +=  values[i];
					sortedTiles += distinctLetterList[i];
				}
			}
			
			//sopl(value);
			
			//sopl(sortedTiles);
			if(value == TARGET_VALUE) {
				
				//Added if condition because of the println
				//(adding duplicates to sets aren't allowed)
				if(list.contains(sortedTiles) == false) {
					list.add(sortedTiles);
					sopl(sortedTiles);
					for(int i=0; i<sortedTiles.length(); i++)  {
						sopl(sortedTiles.charAt(i) + ": " + values[getIndexChar(sortedTiles.charAt(i))]);
					}
					sopl();
				}
			}
			combo = UtilityFunctions.UtilityFunctions.getNextCombination(combo);
			
		}
		
		sopl();
		sopl("Final size:");
		sopl(list.size());
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
	
	public static void exit() {
		exit(0);
	}
	public static void exit(int code) {
		sop("Exit with code " + code);
		
		System.exit(code);
	}
	
}
