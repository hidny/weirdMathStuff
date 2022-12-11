package OneNet3Cuboids.FoldingAlgos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import number.IsNumber;

public class FoldResolverOrdered {

	

	public static final int NUM_ROTATIONS = 4;
	
	
	public static void solveFoldsForSingleCuboid(int a, int b, int c) {
		
		
		//cube.set start location 0 and rotation 0
		

		//TODO: LATER use hashes to help.. (record potential expansions, and no-nos...)
		Coord2D paperToDevelop[] = new Coord2D[Utils.getTotalArea(a, b, c)];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		int GRID_SIZE = 2*Utils.getTotalArea(a, b, c);
	
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
		paperToDevelop[numCellsUsedDepth] = new Coord2D(START_I, START_J);
		
		cuboid.setCell(START_INDEX, START_ROTATION);
		indexCuboidOnPaper[START_I][START_J] = START_INDEX;
		numCellsUsedDepth += 1;
		

		int minOrderedCellCouldUse = 0;
		int minCellRotationOfMinCellToDev = 0;
		HashMap <Integer, Integer> CellIndexToOrderOfDev = new HashMap <Integer, Integer>();
		CellIndexToOrderOfDev.put(START_INDEX, 0);
		
		doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, 0, CellIndexToOrderOfDev, minOrderedCellCouldUse, minCellRotationOfMinCellToDev);
		
		System.out.println("Final number of unique solutions: " + numUniqueFound);
	}
	
	private static int numFound = 0;
	private static int numUniqueFound = 0;
	//TODO: indexCuboid -> indexCuboidOnPaper
	//TODO: paper -> paperUsed
	
	//TODO:
	//TODO
	
	
	public static void doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth, int debugLastIndex,
			HashMap <Integer, Integer> CellIndexToOrderOfDev, int minOrderedCellCouldUse, int minCellRotationOfMinCellToDev) {

		
		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			//System.out.println("Done!");
			
			//TODO: do something more complicated than printing later:
			//Utils.printFold(paperUsed);
			//Utils.printFoldWithIndex(indexCuboidonPaper);
			
			numFound++;
			
			if((numCellsUsedDepth < 20 && numFound % 10000 == 0)
					|| numFound % 100000 == 0) {
				System.out.println(numFound + " (num unique: " + numUniqueFound + ")");
			}
			
			if(BasicUniqueCheckImproved.isUnique(paperUsed)) {
				numUniqueFound++;
				
				if((numCellsUsedDepth < 20 && numUniqueFound % 2000 == 0)
						|| numUniqueFound % 10000 == 0) {
					System.out.println("Found unique net:");
					Utils.printFold(paperUsed);
					Utils.printFoldWithIndex(indexCuboidonPaper);
				
					System.out.println("Num unique solutions found: " + numUniqueFound);
				}
				
			}

			return;
		}
		
		//DEPTH-FIRST START:
		
		for(int i=0; paperToDevelop[i] != null && i<paperToDevelop.length; i++) {
			
			
			//System.out.println("Coord i,j : " + coord_i + ", " + coord_j);
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			if(indexToUse < 0) {
				System.out.println("Doh!");
				System.exit(1);
			}
			
			if(! CellIndexToOrderOfDev.containsKey(indexToUse)) {
				System.out.println("Doh! Should contain key! Index: " + indexToUse);
				System.exit(1);
			}
			if(CellIndexToOrderOfDev.get(indexToUse) < minOrderedCellCouldUse) {
				continue;
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
				} else if(CellIndexToOrderOfDev.get(indexToUse) == minOrderedCellCouldUse && j < minCellRotationOfMinCellToDev) {
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
					new_i = paperToDevelop[i].i-1;
					new_j = paperToDevelop[i].j;
					
				} else if(rotationToAddCellOn == 1) {
					new_i = paperToDevelop[i].i;
					new_j = paperToDevelop[i].j+1;
					
				} else if(rotationToAddCellOn == 2) {
					new_i = paperToDevelop[i].i+1;
					new_j = paperToDevelop[i].j;
					
				} else if(rotationToAddCellOn == 3) {
					new_i = paperToDevelop[i].i;
					new_j = paperToDevelop[i].j-1;
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
				
				int rotationNeighbourRelativePaper = (curRotation - neighbours[j].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				
				
				
				//TODO: put in function A
				boolean couldAddCellBecauseOfOtherPaperNeighbours = true;
				
				SEARCH_FOR_BAD_SECOND_NEIGHBOURS:
				for(int i1=new_i-1; i1<=new_i+1; i1++) {
					for(int j1=new_j-1; j1<=new_j+1; j1++) {
						if((i1 == new_i && j1 != new_j)
								|| (i1 != new_i && j1 == new_j)) {
							
							if(paperToDevelop[i].i == i1 && paperToDevelop[i].j == j1) {
								continue;
							}
							
							//System.out.println("Paper neighbour:" + i1 + ", " + j1);
							
							if(paperUsed[i1][j1]) {
								//System.out.println("Connected to another paper");
								//TODO: make sure that it fits!
								
								int indexOtherCell = indexCuboidonPaper[i1][j1];
								int rotationOtherCell = cuboid.getRotationRelativeToPaper(indexOtherCell);

								if(CellIndexToOrderOfDev.get(indexOtherCell) < CellIndexToOrderOfDev.get(indexToUse) ) {
									couldAddCellBecauseOfOtherPaperNeighbours = false;
									break SEARCH_FOR_BAD_SECOND_NEIGHBOURS;
								}
								
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
									break SEARCH_FOR_BAD_SECOND_NEIGHBOURS;
								}
								
							}
							
						}
					}
				}
				//END TODO: put in function A
				
				if(couldAddCellBecauseOfOtherPaperNeighbours) {
					//TODO: go next level!
					

					//Setup:
					cuboid.setCell(indexNewCell, rotationNeighbourRelativePaper);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					numCellsUsedDepth += 1;

					CellIndexToOrderOfDev.put(indexNewCell, numCellsUsedDepth);
					//End setep
					
					int newMinOrderedCellCouldUse = CellIndexToOrderOfDev.get(indexToUse);
					int newMinCellRotationOfMinCellToDev = j;
	
					
					
					int debugLastIndex2 = indexNewCell;
					//TODO: remove choices based on ordering here
					
					//System.out.println("Doing a recursion depth " + numCellsUsedDepth);
					
					doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, debugLastIndex2, CellIndexToOrderOfDev, newMinOrderedCellCouldUse, newMinCellRotationOfMinCellToDev);
					
					//Tear down
					cuboid.removeCell(indexNewCell);
					paperUsed[new_i][new_j] = false;
					indexCuboidonPaper[new_i][new_j] = -1;
					
					numCellsUsedDepth -= 1;
					
					paperToDevelop[numCellsUsedDepth] = null;
					CellIndexToOrderOfDev.remove(indexNewCell);
					//End tear down
					
					
				//TODO: exit next level!
				} else {
					//System.out.println("can't add cell because of other paper neighbours");
				}
				
				
			}
		}

		//TODO: figure out how to record distinct answers
		
	}
	

	public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered:");
		solveFoldsForSingleCuboid(4, 1, 1);

		//Mission add to OEIS:
		//So far, the pattern is:
		//11, 349, ??
		
		//2nd mission:
		// get to 11.
		
		
		/* 5x1x1 on December 8th:
		 * 47800000
			47900000
			48000000
			48100000
			48200000 (duplicated solution)
			Final number of unique solutions: 3014430
		 */
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		
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
