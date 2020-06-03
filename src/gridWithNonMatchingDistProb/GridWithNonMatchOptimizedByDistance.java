package gridWithNonMatchingDistProb;

import java.util.Scanner;

public class GridWithNonMatchOptimizedByDistance {

	//DONE: use burnside lemma to get unique without reflection or symmetry
	/* SUBMIT:
Found solution 3:
Board config:
---------------
|O__O_O|
|______|
|______|
|__O___|
|_O____|
|_O____|
|---------------
*/
	
	//DONE: get num symmetric solutions!
	
	//1: 1
	//2: 2
	//3: 5 (40)
	//4
	
	//public static int N =5;

	//I didn't find any solutions, but how do I prove I haven't messed up?

	public static void main(String[] args) {
		
		for(int i=2; i<=20; i++) {
				solve(i);
		}

		//for(int i=0; i<GridWithNonMatchOptimized.labels.size(); i++) {
		//	System.out.println(GridWithNonMatchOptimized.labels.get(i));
		//}
	}

	//pre: n>1
	public static void solve(int n) {
		int distSquares[] = getDistSquareAvailableDesc(n);
		int distOffsets[][][] = getDistOffsets(distSquares, false);
		int distOffsetsLaterInPage[][][] = getDistOffsets(distSquares, true);
		//System.out.println("Debug Check");
		
		boolean board[][] = new boolean[n][n];
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[0].length; j++) {
				board[i][j] = false;
			}
		}
		
		//for(int i=1; i<=20; i++) {
		//	System.out.println("Distances we could ignore for a " + i + " by "+ i +" grid: " + getNumDistancesWeCouldIgnore(i));
		//}
		
		int intialNumDistancesWeCouldIgnore = getNumDistancesWeCouldIgnore(n);
		
		solve(board, 0, distSquares, 0, distOffsets, distOffsetsLaterInPage, intialNumDistancesWeCouldIgnore);
		
		
		System.out.println("Done trying to find solution for an " + n + " by " + n + " board.");
		System.out.println("(Found " + numSolutionsFound + " solutions.)");
		System.out.println("(Found " + numSolutionDiscountRotAndReflection + " solutions by taking symmetries into account.)");
		
		numSolutionsFound=0;
		numSolutionDiscountRotAndReflection=0.0;
		
	}
	
	public static Scanner in = new Scanner(System.in);
	public static int numSolutionsFound = 0;
	public static double numSolutionDiscountRotAndReflection = 0.0;
	
	public static void solve(boolean curBoard[][], int numCountersOnBoard, int distSquares[], int curDistSquareIndex, int distOffsets[][][], int distOffsetsLaterInPage[][][], int intialNumDistancesWeCouldIgnore) {
		
		/*
		if(curBoard.length == 5 && numCountersOnBoard == 2 && curBoard[0][0] && curBoard[4][2]) {
			System.out.println("debug1!");
		}
		if(curBoard.length == 5 && numCountersOnBoard == 3 && curBoard[0][0] && curBoard[4][2] && curBoard[3][3] ) {
			System.out.println("debug2!");
		}

		if(curBoard.length == 5 && numCountersOnBoard == 4 && curBoard[0][0] && curBoard[4][2] && curBoard[3][3] && curBoard[0][1]) {

			System.out.println(distSquares[curDistSquareIndex]);
			GridWithNonMatchOptimized.print(curBoard);
			System.out.println("debug3!");
		}

		if(curBoard.length == 5 && numCountersOnBoard == 5 && curBoard[0][0] && curBoard[4][2] && curBoard[3][3] && curBoard[0][1] && curBoard[2][0]) {
			System.out.println("debug4!");
		}
*/
		int curNumDistancesIgnored = getNumDistancesIgnored(distSquares, curDistSquareIndex, curBoard);

		//End condition:
		if(numCountersOnBoard == curBoard.length) {
			
			System.out.println("Solution number " + numSolutionsFound + ":");
			numSolutionsFound++;
			GridWithNonMatchOptimized.print(curBoard);
			

			int numOrbitals = GridWithNonMatchOptimized.getNumSymmetries(curBoard);
			System.out.println("Num orbitals: "  + numOrbitals);
			
			numSolutionDiscountRotAndReflection += 1.0 / (1.0 * numOrbitals);
			
			System.out.println("Current number of unique solutions " + numSolutionDiscountRotAndReflection);
			
			//if(curBoard.length > 6) {
			//	System.out.println("Continue?");
			//	in.next();
			//}
			System.out.println();
			
			return;
		} else if(intialNumDistancesWeCouldIgnore < curNumDistancesIgnored ) {
			//No more solutions
			//Because there's too many ignored distances
			return;
		}
		
		//Sanity check
		if(getRegisteredDistances(curBoard)[distSquares[curDistSquareIndex]] == true) {
			System.out.println("AAAH!");
			System.exit(1);
		}
		
		//Add 1 counter on board
		if(numCountersOnBoard > 0) {
			
			int distanceOffsets[][] = distOffsets[curDistSquareIndex];

			for(int i=0; i<curBoard.length; i++) {
				for(int j=0; j<curBoard.length; j++) {

					if(curBoard[i][j]) {
						
						for(int trial=0; trial<distanceOffsets.length; trial++) {
							
							int newCounterI = i + distanceOffsets[trial][0];
							int newCounterJ = j + distanceOffsets[trial][1];
							
							
							if(isAnEmptySpace(curBoard, newCounterI, newCounterJ)) {
								
								curBoard[newCounterI][newCounterJ] = true;
								
								if(boardHasNoDuplicateDist(curBoard)
										&& noNewBiggerDistancesAdded(distSquares, curDistSquareIndex, curBoard, curNumDistancesIgnored)) {
									int nextDistSquareIndex = getNextDistSquareIndex(distSquares, curDistSquareIndex, curBoard);

									//Add counter and try to solve
									solve(curBoard, numCountersOnBoard + 1, distSquares, nextDistSquareIndex, distOffsets, distOffsetsLaterInPage, intialNumDistancesWeCouldIgnore);
								}
								
								curBoard[newCounterI][newCounterJ] = false;
							}
						}
					}
				}
			}
		}
		
		//Add 2 counters on board
		if(numCountersOnBoard <= curBoard.length - 2) {
			

			int distanceOffsetsPos[][] = distOffsetsLaterInPage[curDistSquareIndex];

			for(int trial=0; trial<distanceOffsetsPos.length; trial++) {
				for(int i=0; i<curBoard.length - distanceOffsetsPos[trial][0]; i++) {
					for(int j=0; j<curBoard.length; j++) {
						
						if(curBoard[i][j] == false) {
							
							int newCounterI = i + distanceOffsetsPos[trial][0];
							int newCounterJ = j + distanceOffsetsPos[trial][1];
							
							if(isAnEmptySpace(curBoard, i, j)
									&& isAnEmptySpace(curBoard, newCounterI, newCounterJ)) {
								
								curBoard[i][j] = true;
								curBoard[newCounterI][newCounterJ] = true;
								
								//solve
								if(boardHasNoDuplicateDist(curBoard)
										&& noNewBiggerDistancesAdded(distSquares, curDistSquareIndex, curBoard, curNumDistancesIgnored)) {

									int nextDistSquareIndex = getNextDistSquareIndex(distSquares, curDistSquareIndex, curBoard);

									//Add two counters and try to solve
									solve(curBoard, numCountersOnBoard + 2, distSquares, nextDistSquareIndex, distOffsets, distOffsetsLaterInPage, intialNumDistancesWeCouldIgnore);
									
								}

								curBoard[i][j] = false;
								curBoard[newCounterI][newCounterJ] = false;
							}
						}
					}
				}
			}
			
		}
		
		//Try ignoring current Dist to add:

		int nextDistSquareIndex = getNextDistSquareIndex(distSquares, curDistSquareIndex, curBoard);
		solve(curBoard, numCountersOnBoard, distSquares, nextDistSquareIndex, distOffsets, distOffsetsLaterInPage, intialNumDistancesWeCouldIgnore);

		
	}
	
	public static int getNumDistancesIgnored(int distSquares[], int curDistSquareIndex, boolean curBoard[][]) {
		boolean registeredDistances[]  = getRegisteredDistances(curBoard);
		int numIgnored = 0;
		
		for(int i=0; i<curDistSquareIndex; i++) {
			if(registeredDistances[distSquares[i]] == false) {
				numIgnored++;
			}
		}
		return numIgnored;
	}
	
	public static boolean noNewBiggerDistancesAdded(int distSquares[], int curDistSquareIndex, boolean curBoard[][], int curNumDistancesIgnored) {
		
		boolean registeredDistances[]  = getRegisteredDistances(curBoard);

		int newCountNumDistancesIgnored = 0;
		for(int i=0; i<curDistSquareIndex; i++) {
			if(registeredDistances[distSquares[i]] == false) {
				newCountNumDistancesIgnored++;
			}
		}
		
		if(newCountNumDistancesIgnored > curNumDistancesIgnored) {
			System.out.println("ERROR: something impossible happened in noNewBiggerDistancesAdded");
			System.exit(1);
		}
		
		if(newCountNumDistancesIgnored == curNumDistancesIgnored) {
			return true;
		} else {
			return false;
		}
	}
	
	//pre: the input board is valid
	public static boolean[] getRegisteredDistances(boolean curBoard[][]) {
		boolean registeredDistances[] = new boolean[2*curBoard.length*curBoard.length + 1];
		
		for(int i=0; i<registeredDistances.length; i++) {
			registeredDistances[i] = false;
		}
		
		for(int i=0; i<curBoard.length * curBoard.length; i++) {
			if(curBoard[i / curBoard.length][i % curBoard.length]) {
				
				for(int j=0; j<i; j++) {
					if(curBoard[j / curBoard.length][j % curBoard.length]) {
						int x1 = j % curBoard.length;
						int y1 = j / curBoard.length;
						int x2 = i % curBoard.length;
						int y2 = i / curBoard.length;
						
						int distSquared = getSquareDist(y1, x1, y2, x2);
						
						if(registeredDistances[distSquared]) {
							System.out.println("ERROR: duplicate distance in getNextDistSquareIndex");
							System.exit(1);
						} else {
							registeredDistances[distSquared] = true;
						}
						
					}
				}
				
			}
		}
		
		return registeredDistances;
		
	}
	
	//might return index out of bounds... deal with it
	public static int getNextDistSquareIndex(int distSquares[], int curDistSquareIndex, boolean curBoard[][]) {
		boolean registeredDistances[]  = getRegisteredDistances(curBoard);

		int curIndexToReturn = curDistSquareIndex + 1;
		while(curIndexToReturn < distSquares.length &&
				registeredDistances[distSquares[curIndexToReturn]] == true) {
			curIndexToReturn++;
		}
		
		return curIndexToReturn;
	}
	
	public static boolean boardHasNoDuplicateDist(boolean curBoard[][]) {
		
		boolean registeredDistances[] = new boolean[2*curBoard.length*curBoard.length + 1];
		
		for(int i=0; i<registeredDistances.length; i++) {
			registeredDistances[i] = false;
		}
		
		for(int i=0; i<curBoard.length * curBoard.length; i++) {
			if(curBoard[i / curBoard.length][i % curBoard.length]) {
				
				for(int j=0; j<i; j++) {
					if(curBoard[j / curBoard.length][j % curBoard.length]) {
						int x1 = j % curBoard.length;
						int y1 = j / curBoard.length;
						int x2 = i % curBoard.length;
						int y2 = i / curBoard.length;
						
						int distSquared = getSquareDist(y1, x1, y2, x2);
						
						if(registeredDistances[distSquared]) {
							return false;
						} else {
							registeredDistances[distSquared] = true;
						}
						
					}
				}
				
			}
			
		}
		
		return true;
	}


	public static int getSquareDist(int i, int j, int i2, int j2) {
		return (i-i2)*(i-i2) + (j-j2)*(j-j2);
	}
	
	public static boolean isAnEmptySpace(boolean curBoard[][], int i, int j) {
		return i >= 0 && i < curBoard.length
				&& j >= 0 && j  < curBoard[0].length
					&& curBoard[i][j] == false;
	}
	
	public static int getNumDistancesWeCouldIgnore(int n) {
		return CountDistancesVsPossibleUniqueDist.bruteForceNumDistancesPossibleInNByNGrid(n) - CountDistancesVsPossibleUniqueDist.numUniqueDistancesNeeded(n);
	}
	
	public static int[][][] getDistOffsets(int distancesAvailable[], boolean offsetIsLaterInPage) {
		int ret[][][] = new int[distancesAvailable.length][][];
		
		
		for(int n=0; n<distancesAvailable.length; n++) {
			
			int curDistanceLimit = (int)Math.floor(Math.sqrt(distancesAvailable[n]) + 1.0);
			
			//get number of coord:
			//Inefficient, but it doesn't matter
			
			int numOffsets = 0;
			for(int i=0-curDistanceLimit; i<=curDistanceLimit; i++) {
				for(int j=0-curDistanceLimit; j<=curDistanceLimit; j++) {
					if(i*i + j*j == distancesAvailable[n]
							&& (offsetIsLaterInPage == false || (i>0 || (i==0 && j> 0)))
							) {
						numOffsets++;
					}
				}
			}
			 
			ret[n] = new int[numOffsets][2];
			
			int curOffsetIndex = 0;
			for(int i=0-curDistanceLimit; i<=curDistanceLimit; i++) {
				for(int j=0-curDistanceLimit; j<=curDistanceLimit; j++) {
					if(i*i + j*j == distancesAvailable[n]
							&& (offsetIsLaterInPage == false || (i>0 || (i==0 && j> 0)))
							) {
						
						ret[n][curOffsetIndex][0] = i;
						ret[n][curOffsetIndex][1] = j;
						
						curOffsetIndex++;
					}
				}
			}
		}
		
		
		return ret;
	}
	
	public static int[] getDistSquareAvailableDesc(int n) {
		
		boolean distances[] = new boolean[2*n*n + 1];
		for(int i=1; i<n; i++) {
			for(int j=0; j<n; j++) {
				distances[i*i + j*j] = true;
			}
		}
		
		int numFound = 0;
		for(int i=0; i<distances.length; i++) {
			if(distances[i]) {
				numFound++;
			}
		}
		
		//Sanity check:
		if(numFound != CountDistancesVsPossibleUniqueDist.bruteForceNumDistancesPossibleInNByNGrid(n)) {
			System.out.println("ERROR: bruteForceNumDistancesPossibleInNByNGrid might be wrong");
			System.exit(1);
		}
		
		System.out.println("Number of distances found for n = " + n + ": " + numFound);
		System.out.println("Num of distinct distances needed: " + (n * (n-1))/2);
		
		int arrayDesc[] = new int[numFound];
		
		for(int i=0, j=0; i<arrayDesc.length; j++) {
			int tmpDistSquared = distances.length - 1 - j;

			if(distances[tmpDistSquared]) {
				arrayDesc[i] = tmpDistSquared;
				i++;
			}
		}
		
		if(arrayDesc[arrayDesc.length - 1 ] == 0) {
			System.out.println("ERROR: error setting up descending array");
		}
		
		//System.out.println("Descending array of square distances available for n = " + n + ":");
		//for(int i=0; i<arrayDesc.length; i++) {
		//	System.out.println(arrayDesc[i]);
		//}
		
		
		return arrayDesc;
		
	}
	
	
}
