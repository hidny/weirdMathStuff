package MattInvestorProblem;

public class sloveInvestorsProblem {
	
	//Found:
	//A003111		Number of complete mappings of the cyclic group Z_{2n+1}.
	//(Formerly M3069)
	
	//Also found:
	//A006717		Number of ways of arranging 2n+1 nonattacking semi-queens on a (2n+1) X (2n+1) toroidal board.
	//(Formerly M3005)
	
	public static void main(String args[]) {
		
		for(int i=1; i<=20; i++) {
			idea0(i);
			System.out.println();
		}
		
	}
	
	public static void idea0(int numInvestors) {
		
		boolean allowed[][] = new boolean[numInvestors][numInvestors];
		boolean taken[] = new boolean[numInvestors];
		int currentSolution[] = new int[numInvestors];
		
		for(int i=0; i<allowed.length; i++) {
			for(int j=0; j<allowed[0].length; j++) {
				allowed[i][j] = true;
			}
			taken[i] = false;
			currentSolution[i] = -1;
		}
		
		
		//Start at the right of the first investor\
		
		long numberOfSolutions = -1;
		if(numInvestors > 1) {
			placeNextInvestor(0, 0, allowed, taken, currentSolution);
			numberOfSolutions = getNumSolutionsIdea0(allowed, taken, currentSolution, 1);
		} else {

			numberOfSolutions = getNumSolutionsIdea0(allowed, taken, currentSolution, 0);
		}
		
		
		
		System.out.println("There are " + numberOfSolutions + " solutions when there are " + numInvestors + " investors");
		System.out.println();
	}
	
	public static long getNumSolutionsIdea0(boolean allowed[][], boolean taken[], int currentSolution[], int indexSeat) {
		/*
		//System.out.println("test depth: " + indexSeat);
		int test = 0;
		for(int i=0; i<taken.length; i++) {
			if(taken[i]) {
				test++;
			}
		}
		*/
		
		boolean allowedStaticCopy[][] = hardCopyTable(allowed);
		boolean takenStaticCopy[] = hardCopyBoolArray(taken); 
		int currentSolutionStaticCopy[] = hardCopyIntArray(currentSolution); 
		
			
		long numAnswer = 0L;
		
		int currentIndexTrial = -1;
		while(true) {
			
			/*
			//TEST START
			test = 0;
			for(int i=0; i<taken.length; i++) {
				if(taken[i]) {
					test++;
				}
			}
			if(test != indexSeat) {
				System.out.println("ERROR: wrong num taken7!");
				
			}
			//TEST END
			*/
			
			currentIndexTrial = getNextPossibleInvestor(indexSeat, allowed, taken, currentIndexTrial + 1);
			if(currentIndexTrial == -1) {
				break;
			}
			
			placeNextInvestor(currentIndexTrial, indexSeat, allowed, taken, currentSolution);
			
			if(indexSeat < currentSolution.length - 1) {
				numAnswer += getNumSolutionsIdea0(allowed, taken, currentSolution, indexSeat + 1);
			
			} else {
				numAnswer++;
				/*System.out.print("Solution: ");
				for(int i=0; i<currentSolution.length; i++) {
					System.out.print((currentSolution[i]+1) + " ");
				}
				System.out.println();*/
			}
			

			allowed = hardCopyTable(allowedStaticCopy);
			taken = hardCopyBoolArray(takenStaticCopy); 
			currentSolution = hardCopyIntArray(currentSolutionStaticCopy); 
			
		}
		//System.out.println("test depth return: " + indexSeat);
		
		/*//TEST START
		test = 0;
		for(int i=0; i<taken.length; i++) {
			if(taken[i]) {
				test++;
			}
		}
		if(test != indexSeat) {
			System.out.println("ERROR: wrong num taken2!");
			
		}
		//TEST END
		 */
		return numAnswer;
	}
	
	public static boolean[][] hardCopyTable(boolean[][] table) {
		boolean newTable[][] = new boolean[table.length][table[0].length];
		
		for(int i=0; i<newTable.length; i++) {
			for(int j=0; j<newTable[i].length; j++) {
				newTable[i][j] = table[i][j];
			}
		}
		
		return newTable;
	}
	
	public static boolean[] hardCopyBoolArray(boolean[] array) {
		boolean newArray[] = new boolean[array.length];
		
		for(int i=0; i<newArray.length; i++) {
				newArray[i] = array[i];
		}
		return newArray;
	}
	
	public static int[] hardCopyIntArray(int[] array) {
		int newArray[] = new int[array.length];
		
		for(int i=0; i<newArray.length; i++) {
				newArray[i] = array[i];
		}
		return newArray;
	}
	
	
	public static int getNextPossibleInvestor(int indexSeat, boolean allowed[][], boolean taken[], int firstIndexToLookAt) {
		for(int i=firstIndexToLookAt; i<taken.length; i++) {
			if(taken[i] == false) {
				if(allowed[i][indexSeat]) {
					return i;
				}
			}
		}
		return -1;
	}
	
	public static void placeNextInvestor(int investorInd, int indexSeat, boolean allowed[][], boolean taken[], int currentSolution[]) {
		taken[investorInd] = true;
		currentSolution[indexSeat] = investorInd;
		
		for(int i=0; i<allowed.length - indexSeat; i++) {
			allowed[investorInd][indexSeat + i] = false;
			allowed[(investorInd + i) % allowed.length][indexSeat + i] = false;
			
		}
	}

	
	
}
