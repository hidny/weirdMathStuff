package cardOrderpuzzleMPMP;

import UtilityFunctions.UtilityFunctions;
import number.IsNumber;

//FOUND:
//A079106		Number of permutations of length n containing the minimum number of monotone subsequences of length 5.

//AND:

//A079105		Number of permutations of length n, in which all monotone subsequences of length 4 are descending or all such subsequences are ascending, containing the minimum number of such subsequences subject to that constraint.


public class cardOrderBigQuestion {
	
	//TODO: solve big question later...
	//public static int MP_NUM_CARDS = 4;

	public static int curentNumberInARow = 1;

	//TODO: save the best 2 perms from prev and use in next...
	
	public static void main(String args[]) {
		
		for(int curNumCards=1; curNumCards<=20; curNumCards++) {

			int ret = 0;
			
			curentNumberInARow--;
			do {
				curentNumberInARow++;
				ret = solve(curNumCards);
			
			} while(ret == 0);
			
			System.out.println("Num cards: " + curNumCards);
			System.out.println("Num in a row ascending/descending that must be in a subsequence of every permutation: " + (curentNumberInARow-1));
			
		}
	}
	public static int solve(int numCards) {
		
		String cards[] = new String[numCards];
		for(int i=0; i<cards.length; i++) {
			cards[i] = "" + (i+1);
		}
		
		long numPerm = UtilityFunctions.getSmallFactorial(cards.length);
		
		String currentPerm[];
		int currentAnswer = 0;
		
		for(int permIndex=0; permIndex<numPerm; permIndex++) {
			currentPerm = UtilityFunctions.generatePermutation(cards, permIndex);
			
			int numbers[] = new int[currentPerm.length];
			
			for(int i=0; i<numbers.length; i++) {
				numbers[i] = (int)pint(currentPerm[i]);
			}
			
			
			//4
			if(isAscending(numbers) == false && isDescending(numbers) == false) {
				currentAnswer++;
				
				/*System.out.println("One answer:");
				for(int i=0; i<numbers.length; i++) {
					System.out.print(numbers[i] + "  ");
				}
				System.out.println();
				*/
			}
			
			
		}
		
		System.out.println("Answer: " + currentAnswer);
		
		return currentAnswer;
	}
	
	public static boolean isAscending(int numbers[]) {
		return isAscending(numbers, -1, 0, 0);
	}
	
	public static boolean isAscending(int numbers[], int lastSelected, int currentInARow, int currentIndex) {
		
		if(currentInARow >= curentNumberInARow) {
			return true;
	
		} else if(currentIndex >= numbers.length) {
			return false;
		}
		
		
		if(lastSelected == -1 || numbers[lastSelected] < numbers[currentIndex]) {
			
			if(isAscending(numbers, currentIndex, currentInARow + 1, currentIndex + 1)) {
				return true;
			}
			
		}
		
		return isAscending(numbers, lastSelected, currentInARow, currentIndex + 1);
		
		
	}
	

	public static boolean isDescending(int numbers[]) {
		return isDescending(numbers, -1, 0, 0);
	}
	
	
	public static boolean isDescending(int numbers[], int lastSelected, int currentInARow, int currentIndex) {
		
		if(currentInARow >= curentNumberInARow) {
			return true;

		} else if(currentIndex >= numbers.length) {
			return false;
		}
		
		
		if(lastSelected == -1 || numbers[lastSelected] > numbers[currentIndex]) {
			
			if(isDescending(numbers, currentIndex, currentInARow + 1, currentIndex + 1)) {
				return true;
			}
			
		}
		
		return isDescending(numbers, lastSelected, currentInARow, currentIndex + 1);
		
		
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
	
	public static long pint(String s) {
		if (IsNumber.isLong(s)) {
			return Long.parseLong(s);
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
