package SeventhGuestMiniProblems;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//import utils.UtilityFunctions;

public class Word3by3 {

	public static void main(String args[]) {
		
		String lettersToUse = "aeoybdgtt";
		
		Scanner in = null;
		try {
			
			ArrayList <String> words = new ArrayList<String>();
			in = new Scanner(new File("words2.txt"));
			
			while(in.hasNext()) {
				String tmp = in.next();
				
				if(tmp.length() == 3) {
					words.add(tmp.toLowerCase());
				}
			}
			System.out.println("Number of words: " + words.size());
			
			for(int i=0; i<words.size(); i++) {
				System.out.println(words.get(i));
			}
			
			//Get passable words:
			ArrayList <String> passableWords = new ArrayList<String>();
			
			for(int i=0; i<words.size(); i++) {
				String tmp = words.get(i);
				
				boolean passableSoFar = true;
				for(int j=0; j<tmp.length(); j++) {
					if(! lettersToUse.contains(tmp.charAt(j) + "")) {
						passableSoFar = false;
						break;
					}
				}
				
				if(passableSoFar) {
					passableWords.add(tmp.toLowerCase());
				}
			}
			
			System.out.println();
			System.out.println();
			System.out.println("Number of passable words:");
			System.out.println(passableWords.size());
			for(int i=0; i<passableWords.size(); i++) {
				System.out.println(passableWords.get(i));
			}
			
			int finalLetterCount[] = new int[(int)('z' - 'a') + 1];
			for(int i=0; i<lettersToUse.length(); i++) {
				finalLetterCount[(int)(lettersToUse.charAt(i) - 'a')]++;
			}
			
			
			for(int i=0; i<passableWords.size(); i++) {
				int curLetterCount1[] = new int[finalLetterCount.length];
				
				String word1 = passableWords.get(i);
				
				curLetterCount1 = addWordToList(word1, curLetterCount1, finalLetterCount);
				
				
				if(curLetterCount1 == null) {
					continue;
				}
				
				for(int j=0; j<passableWords.size(); j++) {
					
					String word2 = passableWords.get(j);
					
					int curLetterCount2[] = addWordToList(word2, curLetterCount1, finalLetterCount);
					
					if(curLetterCount2 == null) {
						continue;
					}
					
					for(int k=0; k<passableWords.size(); k++) {
						String word3 = passableWords.get(k);
						
						int curLetterCount3[] = addWordToList(word3, curLetterCount2, finalLetterCount);
						
						if(curLetterCount3 == null) {
							continue;
						}
						
						//At this point, we found 3 words, and we're guessing it goes row by row:
						
						if(verticalAndDiagGood(word1, word2, word3, passableWords)) {

							System.out.println("Found potential combo:");
							System.out.println(word1);
							System.out.println(word2);
							System.out.println(word3);
							System.out.println();
							System.out.println();
							System.out.println();
						}
					}
					
				}
				
			}
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			
		} finally {
			in.close();
		}

	}
	
	public static int[] addWordToList(String newWord, int curLetterCount[], int finalLetterCount[]) {
		
		boolean firstWordGood = true;
		
		int retCurLetterCount[] = new int[curLetterCount.length];
		for(int i=0; i<retCurLetterCount.length; i++) {
			retCurLetterCount[i] = curLetterCount[i];
		}
		
		for(int i=0; i<newWord.length(); i++) {
			int indexToUse= (int)(newWord.charAt(i) - 'a');
			retCurLetterCount[indexToUse]++;
			
			if(retCurLetterCount[indexToUse] > finalLetterCount[indexToUse]) {
				firstWordGood = false;
				break;
			}
		}
		
		if(firstWordGood) {
			return retCurLetterCount;
		} else {
			return null;
		}
	}
	
	public static boolean verticalAndDiagGood(String word1, String word2, String word3, ArrayList <String> passableWords) {
		
		//Vertical:
		
		for(int i=0; i<3; i++) {
			String vert = "";
			
			vert += word1.charAt(i) + "";
			vert += word2.charAt(i) + "";
			vert += word3.charAt(i) + "";
			
			if(! isWordInDictionary(vert, passableWords)) {
				//return false;
			}
		}
		
		//Diag 1:
		
		String diag1 = "";

		diag1 += word1.charAt(0) + "";
		diag1 += word2.charAt(1) + "";
		diag1 += word3.charAt(2) + "";
		
		if(! isWordInDictionary(diag1, passableWords)) {
			//return false;
		}
		
		
		return true;
		
	}
	
	//Inefficient, but effective way to check dictionary:
	public static boolean isWordInDictionary(String target, ArrayList <String> passableWords) {
		
		boolean match = false;
		for(int i=0; i<passableWords.size(); i++) {
			if(passableWords.get(i).equals(target)) {
				match = true;
				break;
			}
		}
		
		return match;
	}
}
