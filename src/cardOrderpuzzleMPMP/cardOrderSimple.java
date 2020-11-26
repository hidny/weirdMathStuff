package cardOrderpuzzleMPMP;

import UtilityFunctions.UtilityFunctions;
import number.IsNumber;

public class cardOrderSimple {
	
	//TODO: solve big question later...
	//Solve up to 10...
	//public static int MP_NUM_CARDS = 4;
	//public static int MP_NUM_IN_A_ROW_SEARCH = 3;

	public static int MP_NUM_CARDS = 10;
	public static int MP_NUM_IN_A_ROW_SEARCH = 5;

	public static void main(String args[]) {
		solve(MP_NUM_CARDS);
	}
	public static void solve(int numCards) {
		
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
				
				System.out.println("One answer:");
				for(int i=0; i<numbers.length; i++) {
					System.out.print(numbers[i] + "  ");
				}
				System.out.println();
				//System.exit(1);
			}
			
			
		}
		
		System.out.println("Answer: " + currentAnswer);
	}
	
	public static boolean isAscending(int numbers[]) {
		return isAscending(numbers, 0, 1, 1) 
				|| isAscending(numbers, -1, 0, 1);
	}
	
	public static boolean isAscending(int numbers[], int lastSelected, int currentInARow, int currentIndex) {
		
		if(currentInARow >= MP_NUM_IN_A_ROW_SEARCH) {
			return true;
		}
		if(currentIndex >= numbers.length) {
			return false;
		}
		
		
		if(lastSelected == -1) {
			//Idea: include currentIndex in subset if possible and try it
			// or exclude it and try it:
			//(Since it's the first element, it could be included without issues....)
			return isAscending(numbers, currentIndex, currentInARow + 1, currentIndex + 1) 
					|| isAscending(numbers, lastSelected, 0, currentIndex + 1);
		}
		
		//Idea: include currentIndex in subset if possible and try it:
		if(numbers[lastSelected] < numbers[currentIndex]) {
			
			if(isAscending(numbers, currentIndex, currentInARow + 1, currentIndex + 1)) {
				return true;
			}
			
		}

		//Idea: exclude currentIndex from subset possible and try it:
		return isAscending(numbers, lastSelected, currentInARow, currentIndex + 1);
		
		
	}
	

	public static boolean isDescending(int numbers[]) {
		return isDescending(numbers, 0, 1, 1) 
				|| isDescending(numbers, -1, 0, 1);
	}
	
	
	public static boolean isDescending(int numbers[], int lastSelected, int currentInARow, int currentIndex) {
		
		if(currentInARow >= MP_NUM_IN_A_ROW_SEARCH) {
			return true;
		}
		if(currentIndex >= numbers.length) {
			return false;
		}
		
		if(lastSelected == -1) {
			return isDescending(numbers, currentIndex, currentInARow + 1, currentIndex + 1) 
					|| isDescending(numbers, lastSelected, 0, currentIndex + 1);
		}
		
		
		if(numbers[lastSelected] > numbers[currentIndex]) {
			
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
