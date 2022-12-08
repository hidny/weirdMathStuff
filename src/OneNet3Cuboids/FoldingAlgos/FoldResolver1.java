package OneNet3Cuboids.FoldingAlgos;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import number.IsNumber;

public class FoldResolver1 {

	//TODO: find the number of ways to fold a 1x1x1 cube (should be 11)
	

	public static final int NUM_ROTATIONS = 4;
	
	
	public static void solveFoldsForSingleCuboid(int a, int b, int c) {
		
		
		//cube.set start location 0 and rotation 0
		

		//TODO: LATER use hashes to help.. (record potential expansions, and no-nos...)
		HashSet <String>paperToDevelop = new HashSet<String>();
		
		int GRID_SIZE = 2 * Utils.getTotalArea(a, b, c);
	
		boolean paperUsed[][] = new boolean[GRID_SIZE][GRID_SIZE];
		int indexCuboidOnPaper[][] = new int[GRID_SIZE][GRID_SIZE];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				paperUsed[i][j] = false;
				indexCuboidOnPaper[i][j] = -1;
			}
		}

		//Default start location GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		
		CuboidToFoldOn cuboid = new CuboidToFoldOn(a, b, b);
		
		//Insert start cell:
		
		//Once this reaches the total area, we're done!
		int numCellsUsedDepth = 0;

		int START_INDEX = 0;
		int START_ROTATION = 0;
		paperUsed[START_I][START_J] = true;
		
		cuboid.setCell(START_INDEX, START_ROTATION);
		indexCuboidOnPaper[START_I][START_J] = START_INDEX;
		numCellsUsedDepth += 1;
		paperToDevelop.add(START_I +"," + START_J);
		
		doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, 0);
	}
	
	private static int numFound = 0;
	private static int numUniqueFound = 0;
	//TODO: indexCuboid -> indexCuboidOnPaper
	//TODO: paper -> paperUsed
	public static void doDepthFirstSearch(HashSet <String>paperToDevelop, int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth, int debugLastIndex) {

		
		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			//System.out.println("Done!");
			
			//TODO: do something more complicated than printing later:
			//printFold(paperUsed);
			//printFoldWithIndex(indexCuboidonPaper);
			
			numFound++;
			
			if(numFound % 100000 == 0) {
				System.out.println(numFound);
			}
			
			if(isUnique(paperUsed)) {
				numUniqueFound++;
				System.out.println("Found unique net:");
				printFold(paperUsed);
				printFoldWithIndex(indexCuboidonPaper);
				
				System.out.println("Num unique solutions found: " + numUniqueFound);
				
			}
		}
		
		//DEPTH-FIRST START:
		
		Iterator<String> it = paperToDevelop.iterator();
		
		ArrayList<String> toDev = new ArrayList<String>();
		
		//Get the keys in a weird way:
		while(it.hasNext()) {
			toDev.add(it.next());
		}
		
		for(int i=0; i<toDev.size(); i++) {
			
			String coordToDev = toDev.get(i);
			
			//TODO: assume all indexes before shouldn't be developed (enforce an ordering)
			//Do it later... and try to do it efficiently.
			
			//ew so inefficient:
			String token[] = coordToDev.split(",");
			int coord_i = pint(token[0]);
			int coord_j = pint(token[1]);

			//System.out.println("Coord i,j : " + coord_i + ", " + coord_j);
			
			int indexToUse = indexCuboidonPaper[coord_i][coord_j];
			if(indexToUse < 0) {
				System.out.println("Doh!");
				System.exit(1);
			}
			
			CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
			int curRotation = cuboid.getRotationRelativeToPaper(indexToUse);
			if(curRotation < 0) {
				System.out.println("Doh! 2");
				System.exit(1);
			}
			
			//System.out.println("Current rotation:");
			//System.out.println(curRotation);
			
			
			for(int j=0; j<neighbours.length; j++) {
				
				if(cuboid.isCellIndexUsed(neighbours[j].getIndex())) {
					//Don't reuse a used cell:
					continue;
				}
				//TODO: neighbours should have an index!
				
				//Try adding it or not
				//System.out.println(neighbours[j].getA() + "," + neighbours[j].getB() + "," + neighbours[j].getC());
				
				int rotationToAddCellOn = (j + curRotation) % NUM_ROTATIONS;
				
				//TODO: put in function
				int new_i = -1;
				int new_j = -1;
				if(rotationToAddCellOn == 0) {
					new_i = coord_i-1;
					new_j = coord_j;
					
				} else if(rotationToAddCellOn == 1) {
					new_i = coord_i;
					new_j = coord_j+1;
					
				} else if(rotationToAddCellOn == 2) {
					new_i = coord_i+1;
					new_j = coord_j;
					
				} else if(rotationToAddCellOn == 3) {
					new_i = coord_i;
					new_j = coord_j-1;
				} else {
					System.out.println("Doh! 3");
					System.out.println("Unknown rotation!");
					System.exit(1);
				}
				//END TODO: put in function

				int indexNewCell = neighbours[j].getIndex();
		
				if(paperUsed[new_i][new_j]) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				int rotationNeighbourRelativePaper = (curRotation + neighbours[j].getRot()) % NUM_ROTATIONS;
				
				
				cuboid.setCell(indexNewCell, rotationNeighbourRelativePaper);
				
				paperUsed[new_i][new_j] = true;
				indexCuboidonPaper[new_i][new_j] = indexNewCell;

				numCellsUsedDepth += 1;
				paperToDevelop.add(new_i +"," + new_j);
				
				if(cuboid.getNumCellsToFill() == 6 && indexCuboidonPaper[5][7] == 5) {
					System.out.println("Doh for 1x1x1!");
					printFold(paperUsed);
					printFoldWithIndex(indexCuboidonPaper);
				}
				
				//System.out.println("New i,j : " + new_i + ", " + new_j);
				
				//TODO: put in function A
				boolean couldAddCellBecauseOfOtherPaperNeighbours = true;
				
				for(int i1=new_i-1; i1<=new_i+1; i1++) {
					for(int j1=new_j-1; j1<=new_j+1; j1++) {
						if((i1 == new_i && j1 != new_j)
								|| (i1 != new_i && j1 == new_j)) {
							
							if(coord_i == i1 && coord_j == j1) {
								continue;
							}
							
							//System.out.println("Paper neighbour:" + i1 + ", " + j1);
							
							if(paperUsed[i1][j1]) {
								//System.out.println("Connected to another paper");
								//TODO: make sure that it fits!
								
								int indexOtherCell = indexCuboidonPaper[i1][j1];
								int rotationOtherCell = cuboid.getRotationRelativeToPaper(indexOtherCell);
								
								//TODO: put in function
								int rotReq = -1;
								
								if(i1 -1 == new_i) {
									rotReq = 0;
								} else if(j1 +1 == new_j) {
									rotReq = 1;
									
								} else if(i1 +1 == new_i) {
									rotReq = 2;
									
								} else if(j1 -1 == new_j) {
									rotReq = 3;
									
								} else {
									System.out.println("Oops! rotation 217");
								}
								
								int neighbourIndexNeeded = (rotReq - rotationOtherCell + NUM_ROTATIONS) % NUM_ROTATIONS;

								//End TODO: put in function

								if(cuboid.getNeighbours(indexOtherCell)[neighbourIndexNeeded].getIndex() != indexNewCell) {
									couldAddCellBecauseOfOtherPaperNeighbours = false;
									break;
								}
								
							}
							
						}
					}
				}
				//END TODO: put in function A
				
				if(couldAddCellBecauseOfOtherPaperNeighbours) {
				//TODO: go next level!
				
				int debugLastIndex2 = indexNewCell;
				//TODO: remove choices based on ordering here
					//System.out.println("Doing a recursion depth " + numCellsUsedDepth);
					doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, debugLastIndex2);
				
				//TODO: exit next level!
				} else {
					//System.out.println("can't add cell because of other paper neighbours");
				}
				
				cuboid.removeCell(indexNewCell);
				paperUsed[new_i][new_j] = false;
				indexCuboidonPaper[new_i][new_j] = -1;
				numCellsUsedDepth -= 1;
				paperToDevelop.remove(new_i +"," + new_j);
				
			}
		}

		//TODO: figure out how to record distinct answers
		
	}
	

	//TODO: remove empty border (reuse code)
	//TODO: improve and make it return a string
	public static void printFold(boolean array[][]) {
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(array[i][j]) {
					System.out.print("#");
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	

	//TODO: remove empty border (reuse code)
	//TODO: improve and make it return a string
	public static void printFoldWithIndex(int array[][]) {
		
		int maxLength = 0;
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(array[i][j] >= 0) {
					if((array[i][j] + "").length() > maxLength) {
						maxLength = (array[i][j] + "").length();
					}
				}
			}
		}
		
		String space ="";
		String points = "";
		
		for(int i=0; i<maxLength; i++) {
			space += " ";
			points += ".";
			
		}
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(array[i][j] >= 0) {
					int lengthNum = (array[i][j] + "").length();
					
					if(lengthNum < maxLength) {
						System.out.print("|" + space.substring(lengthNum) + array[i][j]);
					} else {
						System.out.print("|"+ array[i][j]);
					}
					
				} else {
					System.out.print("|" + points);
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String args[]) {
		solveFoldsForSingleCuboid(2, 1, 1);

		//Mission add to OEIS:
		//So far, the pattern is:
		//11, 348
		
		//2nd mission:
		// get to 11.
		
		
	}
	
	public static int NUM_REFLECTIONS = 2;
	public static HashSet<BigInteger> uniqList = new HashSet<BigInteger>();
	
	public static boolean isUnique(boolean array[][]) {
		
		//TODO: make function to get borders...
		int firsti = 0;
		
		int lasti = array.length - 1;
		
		
		int firstj = 0;
		int lastj = array[0].length - 1;
		
		TOP_BORDER:
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				
				if(array[i][j]) {
					firsti = i;
					break TOP_BORDER;
				}
			}
		}
		

		BOTTOM_BORDER:
		for(int i=array.length - 1; i>=0; i--) {
			for(int j=0; j<array[0].length; j++) {
				
				if(array[i][j]) {
					lasti = i;
					break BOTTOM_BORDER;
				}
			}
		}
		
		
		LEFT_BORDER:
		for(int j=0; j<array[0].length; j++) {
			for(int i=0; i<array.length; i++) {
				
				if(array[i][j]) {
					firstj = j;
					break LEFT_BORDER;
				}
			}
		}
		

		RIGHT_BORDER:
		for(int j=array[0].length - 1; j>=0; j--) {
			for(int i=0; i<array.length; i++) {
				
				if(array[i][j]) {
					lastj = j;
					break RIGHT_BORDER;
				}
			}
		}

		//END TODO: make function to get borders...
		

		// sideFactor and vertFactor is to make sure all nets that get converted to binary num
		// have the same background dimensions to work with.
		// If they don't, there could be a false match.
		BigInteger sideFactor = BigInteger.ONE;
		BigInteger vertFactor = BigInteger.ONE;

		BigInteger TWO = new BigInteger("2");
		
		for(int i = lasti - firsti; i<array.length; i++) {
			vertFactor = vertFactor.multiply(TWO);
		}
		

		for(int j = lastj - firstj; j<array[0].length; j++) {
			sideFactor = sideFactor.multiply(TWO);
		}
		
		//TODO: make function to get scores:
		//I could condense this and make it less repetitive, but I'm lazy.
		
		BigInteger scores[] = new BigInteger[NUM_REFLECTIONS * NUM_ROTATIONS];
		
		for(int i=0; i<scores.length; i++) {
			scores[i] = BigInteger.ZERO;
		}
		
		for(int i=firsti, irev = lasti; i<=lasti; i++, irev--) {
			for(int j=firstj, jrev = lastj; j<=lastj; j++, jrev--) {
				
				scores[0] = scores[0].multiply(TWO);
				scores[1] = scores[1].multiply(TWO);
				scores[2] = scores[2].multiply(TWO);
				scores[3] = scores[3].multiply(TWO);

				if(array[i][j]) {
					scores[0] = scores[0].add(BigInteger.ONE);
				}
				
				if(array[i][jrev]) {
					scores[1] = scores[1].add(BigInteger.ONE);
				}
				
				if(array[irev][j]) {
					scores[2] = scores[2].add(BigInteger.ONE);
				}
				
				if(array[irev][jrev]) {
					scores[3] = scores[3].add(BigInteger.ONE);
				}
				
			}
			scores[0] = scores[0].multiply(sideFactor);
			scores[1] = scores[1].multiply(sideFactor);
			scores[2] = scores[2].multiply(sideFactor);
			scores[3] = scores[3].multiply(sideFactor);
		}
		scores[0] = scores[0].multiply(vertFactor);
		scores[1] = scores[1].multiply(vertFactor);
		scores[2] = scores[2].multiply(vertFactor);
		scores[3] = scores[3].multiply(vertFactor);

		for(int j=firstj, jrev = lastj; j<=lastj; j++, jrev--) {
			for(int i=firsti, irev = lasti; i<=lasti; i++, irev--) {
				
				scores[4] = scores[4].multiply(TWO);
				scores[5] = scores[5].multiply(TWO);
				scores[6] = scores[6].multiply(TWO);
				scores[7] = scores[7].multiply(TWO);

				if(array[i][j]) {
					scores[4] = scores[4].add(BigInteger.ONE);
				}
				
				if(array[i][jrev]) {
					scores[5] = scores[5].add(BigInteger.ONE);
				}
				
				if(array[irev][j]) {
					scores[6] = scores[6].add(BigInteger.ONE);
				}
				
				if(array[irev][jrev]) {
					scores[7] = scores[7].add(BigInteger.ONE);
				}
				
			}
			scores[4] = scores[4].multiply(vertFactor);
			scores[5] = scores[5].multiply(vertFactor);
			scores[6] = scores[6].multiply(vertFactor);
			scores[7] = scores[7].multiply(vertFactor);
		}
		scores[4] = scores[4].multiply(sideFactor);
		scores[5] = scores[5].multiply(sideFactor);
		scores[6] = scores[6].multiply(sideFactor);
		scores[7] = scores[7].multiply(sideFactor);
		//END TODO: make function to get scores:
		
		//Deal with symmetries by getting max scores from 8 possible symmetries:
		BigInteger max = BigInteger.ZERO;
		
		for(int i=0; i<scores.length; i++) {
			if(max.compareTo(scores[i]) < 0) {
				max = scores[i];
			}
		}
		
		if(! uniqList.contains(max)) {
			uniqList.add(max);
			
			System.out.println("Max number: " + max);
			
			return true;
		} else {
			return false;
		}
	}
	
	
	
	public static void moveToListInNormalWay() {
		HashSet <String>toDevelop = new HashSet<String>();
		
		toDevelop.add("test");
		
		Iterator<String> it = toDevelop.iterator();
		
		ArrayList<String> toDev = new ArrayList<String>();
		
		//Get the keys in a weird way:
		while(it.hasNext()) {
			toDev.add(it.next());
		}
		
		for(int i=0; i<toDev.size(); i++) {
			for(int j=0; j<10; j++) {
				toDevelop.add("test" + (10*i + j));
			}
			
			System.out.println(toDev.get(i));
		}
	}
	
	public static void causeConcurrentModExcep() {
		HashSet <String>toDevelop = new HashSet<String>();
		
		toDevelop.add("test");
		
		Iterator<String> it = toDevelop.iterator();
		
		int i =0;
		while(it.hasNext()) {
			
			for(int j=0; j<10; j++) {
				toDevelop.add("test" + i);
				i++;
			}
			
			System.out.println(it.next());
		}
	}

	public static int pint(String s) {
		if (IsNumber.isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			System.out.println("Error: (" + s + " is not a number");
			return -1;
		}
	}
}
