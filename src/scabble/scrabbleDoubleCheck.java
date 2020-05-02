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

public class scrabbleDoubleCheck {

	public static int NUM_DISTINCT_LETTERS = 26;
	public static int NUM_DISTINCT_OTHER = 1;
	public static int NUM_TILES = 100;
	
	public static int NUM_TIMES_IN_HAND = 7;
	
	public static int TARGET_VALUE = 46;
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
			
			int answer =0;
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
			
			for(int d=0; d<6; d++) {
				boolean combo[] = new boolean[NUM_DISTINCT_TILES];
				
				for(int i=0; i<combo.length; i++) {
					if(i<6) {
						combo[i] = true;
					} else {
						combo[i] = false;
					}
				}
				
				while(combo != null) {
					
					
					int value = 0;
					String word = "";
					boolean notEnoughTiles = false;
					
					for(int i=0; i<NUM_DISTINCT_TILES; i++) {
						if(combo[i]) {
							word += distinctLetterList[i];
							value += values[i];
						}
						
						if(word.length() == d) {
							if(numTilesPerChar[i] < 2) {
								notEnoughTiles = true;
							}
							word += distinctLetterList[i];
							value += values[i];
						
						}
					}
					
					if(value == 46) {
						if(notEnoughTiles) {
							//sopl("Fake answer: " + word);
						} else {
							sopl("Answer: " + word);
							answer++;
						}
					
					}
					
					combo = UtilityFunctions.UtilityFunctions.getNextCombination(combo);
				}
			}
			
			sopl("Answer: " + answer);
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
		}
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
