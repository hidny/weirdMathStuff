package OneNet3Cuboids.DupRemover;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.Cuboid.SymmetryResolver.SymmetryResolver;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.Region.Region;
import number.IsNumber;

public class MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1 {
	
	//I tried to make it faster by only declaring memory at initialization of object,
	// and never again.

	//Loop other start locations and rotations to see if there's anything faster.
	// (i.e.: Anything that the algo would be found first)
	//Warning: This will only work if we model the algo correctly.
	
	public static long debugNumIsUniqueCalls = 0;
	public static long debugNumMoreThanTopValid = 0;
	
	public static final int DEFAULT_ROTATION = 0;
	public static final boolean NO_REFLECTION = false;
	
	public static final int ROTATIONS_TIMES_RELECTIONS = 8;
	
	public MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1(CuboidToFoldOn orig) {

		//TODO: get these dims from a utils location (don't just hope it matches what's set somewhere else)
		int totalArea = orig.getNumCellsToFill();
		
		//Variables to recycle/reuse for each iteration: 
		paperUsed = new boolean[2 * totalArea][2 * totalArea];
		indexCuboidOnPaper = new int[2 * totalArea][2 * totalArea];
		coord2DTable = new Coord2D[2 * totalArea][2 * totalArea];
		newPaperToDevelop = new Coord2D[totalArea];
		
		for(int i2=0; i2<paperUsed.length; i2++) {
			for(int j2=0; j2<paperUsed[0].length; j2++) {
				indexCuboidOnPaper[i2][j2] = -1;
				coord2DTable[i2][j2] = new Coord2D(i2, j2);
			}
		}
		
		//Copy dimensions and neighbours for cuboid to use:
		cuboidToUse = new CuboidToFoldOn(orig);
		//End copy dims and neighbours
		
		regionsToHandleRevOrder[0] = new Region(cuboidToUse);
		
		validSetup = new boolean[totalArea];
		isSimilarCellToStart = new boolean[totalArea];
		//TODO: Assuming it's a Nx1x1 where the top cell is similar to the bottom one:
		isSimilarCellToStart[isSimilarCellToStart.length - 1]  = true;
		

		originalQuickness = new int[totalArea];
		tmpQuickness = new int[totalArea];
	}
	
	private boolean paperUsed[][];
	private int indexCuboidOnPaper[][];
	private Coord2D newPaperToDevelop[];
	private Region regionsToHandleRevOrder[] = new Region[1];
	private CuboidToFoldOn cuboidToUse;
	
	private boolean validSetup[];
	private boolean isSimilarCellToStart[];
	
	private Coord2D coord2DTable[][];
	
	public int originalQuickness[];
	public int tmpQuickness[];
	
	
	public boolean isUnique(Coord2D paperToDevelop[], boolean array[][], int origIndexCuboidOnPaper[][]) {
		
		boolean isCurrentlyAloneInFirst = true;
		boolean isUniqueSoFar = true;
		
		int START_INDEX = 0;

		//Note that I'm also borrowing the boolean array[][] param... I'll put it back to the way it was.
	
		//if(array.length != array[0].length) {
		//	System.out.println("ERROR: dimensions have to be the same for MemManager To work.");
		//}
		

		//TODO: this is a hack for Nx1x1 that will be improved later:
		if(cuboidToUse.getDimensions()[1] != 1 || cuboidToUse.getDimensions()[2] != 1) {
			System.out.println("ERROR: MemorylessUniqueCheckSkipSymmetriesMemManage2 only handles Nx1x1 cuboids!");
			System.exit(1);
		}
		
		validSetup[0] = true;
		
		int minNeighbours = FoldResolveOrderedRegionsSkipSymmetries.getNumUsedNeighbourCellonPaper(
				origIndexCuboidOnPaper, paperToDevelop[0]);

		for(int index=1; index<validSetup.length; index++) {
			validSetup[index] = false;
		}
		
		int debugNumOtherValid = 0;

		
		//This algo is ineffient, but it works.
		// It basically brute forces matches every possible start pos with cell 0 of the cuboid
		//looking for a match. There is a better ways to do this.
		for(int index=1; index<validSetup.length; index++) {
			
			if(FoldResolveOrderedRegionsSkipSymmetries.getNumUsedNeighbourCellonPaper(
					origIndexCuboidOnPaper, paperToDevelop[index]) < minNeighbours) {
				
				validSetup[index] = false;

			} else if(isSimilarCellToStart[origIndexCuboidOnPaper[paperToDevelop[index].i][paperToDevelop[index].j]]) {
				
				validSetup[index] = true;
				
				
			} else {
				
				
				validSetup[index] = isValidSetupAtIndexedStartLocationWithRotation(paperToDevelop, array, index, DEFAULT_ROTATION, START_INDEX);
				
				if(validSetup[index]) {
					debugNumOtherValid++;
				}
				
				eraseChangesToPaperUsedAndIndexCuboidOnPaper(
						paperToDevelop,
						paperUsed,
						indexCuboidOnPaper,
						DEFAULT_ROTATION,
						NO_REFLECTION);
			}
			
		}
		
		

		debugNumIsUniqueCalls++;
		if(debugNumOtherValid > 0){
			debugNumMoreThanTopValid++;
		}

		//TODO: put this in the final summary...
		if(debugNumIsUniqueCalls > 0 && (debugNumIsUniqueCalls % 1000000 == 0 )) {
			System.out.println("--");
			System.out.println("debugNumIsUniqueCalls: " + debugNumIsUniqueCalls);
			System.out.println("debugNumMoreThanTopStartValid: " + debugNumMoreThanTopValid);
		}
		
		//Not needed because the 1st iteration reverses this completely:
		//switchOnOffPaperUsedForArrayRotatedOrReflected(false, paperToDevelop, array, DEFAULT_ROTATION, NO_REFLECTION);
		
		//Check to see if current solution is the 1st of every valid reflection/rotation:
		
		for(int symmetryIndex=0; symmetryIndex < ROTATIONS_TIMES_RELECTIONS && isUniqueSoFar; symmetryIndex++) {
			
			int rotation = symmetryIndex % NUM_ROTATIONS;
			boolean reflect = symmetryIndex >=  NUM_ROTATIONS;
			

			switchOnOffPaperUsedForArrayRotatedOrReflected(true, paperToDevelop, array, rotation, reflect);
			
			for(int i=0; i<paperToDevelop.length && isUniqueSoFar; i++) {

				if(! validSetup[i]) {
					continue;
				}
				
//Setup to run imitation algo:
				
				cuboidToUse.resetState();

				int startI = getIAfterRotation(array.length, paperToDevelop[i].i, paperToDevelop[i].j, rotation);
				int startJ = getJAfterRotation(array.length, paperToDevelop[i].i, paperToDevelop[i].j, rotation);
				

				if(reflect) {
					int tmp = startI;
					startI = startJ;
					startJ = tmp;
				}
				
				if( ! array[startI][startJ]) {
					System.out.println("DOH! Bad start location");
					printArrayDebug(array);
					System.exit(1);
				}
				
				int numCellsUsedDepth = 0;

				paperUsed[startI][startJ] = true;

				//Delete loop? Answer: No! It's used by symmetry resolver!
				for(int k=0; k<newPaperToDevelop.length; k++) {
					newPaperToDevelop[k] = null;
				}
				
				newPaperToDevelop[numCellsUsedDepth] = coord2DTable[startI][startJ];
				
				cuboidToUse.setCell(START_INDEX, 0);
				indexCuboidOnPaper[startI][startJ] = START_INDEX;
				numCellsUsedDepth += 1;
				
				
				regionsToHandleRevOrder[0].resetStateWithStartIndexOnly(0);
				
			//END Setup to run imitation algo.
				
				//How to debug:
				//if(debugArrayMatchesString(prevOrdering, "0, 0, 1, 2, 3, 6, 10, 9, 3, 14")) {
				//	System.out.println("DEBUG");
				//}

				if(i == 0 && rotation == DEFAULT_ROTATION && reflect == NO_REFLECTION) {
					if(originalQuickness == null) {
						System.out.println("DOH2! Orig quickness is null");
						System.exit(1);
					}

					
					originalQuickness = doDepthFirstSearch(array, newPaperToDevelop, indexCuboidOnPaper, paperUsed, cuboidToUse, numCellsUsedDepth,
						regionsToHandleRevOrder, originalQuickness, null, isCurrentlyAloneInFirst,
						coord2DTable);
				
					isCurrentlyAloneInFirst = false;

					if(originalQuickness == null) {
						System.out.println("DOH! Orig quickness is null");
						Utils.printFold(paperUsed);
						System.exit(1);
					}
				} else {

					int tmp[] = doDepthFirstSearch(array, newPaperToDevelop, indexCuboidOnPaper, paperUsed, cuboidToUse, numCellsUsedDepth,
							regionsToHandleRevOrder, tmpQuickness, originalQuickness, isCurrentlyAloneInFirst,
							coord2DTable);
					
					if(tmp != null) {

						//Sanity check:
						//if(! validSetup[i]) {
						//	System.out.println("Invalid setup got results! (unreflected!)");
						//	System.exit(1);
						//}
						
						//Print fold for debug:
						//Utils.printFold(arrayRotated[rotation]);
						
						isUniqueSoFar = false;
						//System.out.println("FasterOrderingArray for current solution:");
						//printOrderingSolution(tmp);
						
					}
						
				}
				
				
				//Erases changes to paper used, so we could reuse it:
				eraseChangesToPaperUsedAndIndexCuboidOnPaper(
						paperToDevelop,
						paperUsed,
						indexCuboidOnPaper, 
						rotation,
						reflect);
				
			}
		

			switchOnOffPaperUsedForArrayRotatedOrReflected(false, paperToDevelop, array, rotation, reflect);
			switchOnOffPaperUsedForArrayRotatedOrReflected(false, paperToDevelop, paperUsed, rotation, reflect);
		}

		//Get back orig array so calling function won't know I borrowed it:
		switchOnOffPaperUsedForArrayRotatedOrReflected(true, paperToDevelop, array, DEFAULT_ROTATION, NO_REFLECTION);
		
		//Sanity test:
		/*if(isUniqueSoFar && !MemorylessUniqueCheckSkipSymmetries.isUnique(orig, paperToDevelop, array)) {
			System.out.println("DOH! 123");
			Utils.printFold(array);
			System.exit(1);
		}*/
		
		return isUniqueSoFar;
	}
	
	public boolean[][] getPaperUsed() {
		return paperUsed;
	}

	public int[][] getIndexCuboidOnPaper() {
		return indexCuboidOnPaper;
	}

	public static boolean[][] getTranspose(boolean array[][]) {
		boolean transpose[][] = new boolean[array[0].length][array.length];
		
		for(int i=0; i<transpose.length; i++) {
			for(int j=0; j<transpose[0].length; j++) {
				transpose[i][j] = array[j][i];
			}
		}
		
		return transpose;
	}
	
	public static int getIAfterRotation(int arrayLength, int i, int j, int rotation) {
	
		if(rotation == 0) {
			return i;
		} else if(rotation == 1) {
			return j;
		} else if(rotation == 2) {
			return arrayLength - 1 - i;
		} else if(rotation == 3) {
			return arrayLength - 1 - j;
		} else {
			System.out.println("DOH!");
			System.exit(1);
			return -1;
		}
		
	}
	
	public static int getJAfterRotation(int arrayLength, int i, int j, int rotation) {
		
		if(rotation == 0) {
			return j;
		} else if(rotation == 1) {
			return arrayLength - 1 - i;
		} else if(rotation == 2) {
			return arrayLength - 1 - j;
		} else if(rotation == 3) {
			return i;
		} else {
			System.out.println("DOH!");
			System.exit(1);
			return -1;
		}
		
		
	}
	
	public static void eraseChangesToPaperUsedAndIndexCuboidOnPaper(
			Coord2D paperToDevelop[],
			boolean paperUsed[][],
			int indexCuboidOnPaper[][], 
			int rotation,
			boolean transpose) {
		
		if(!transpose) {
			if(rotation == 0) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].i][paperToDevelop[k].j] = false;
					indexCuboidOnPaper[paperToDevelop[k].i][paperToDevelop[k].j] = -1;
				}
			} else if(rotation == 1) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].j][paperUsed.length - 1 - paperToDevelop[k].i] = false;
					indexCuboidOnPaper[paperToDevelop[k].j][paperUsed.length - 1 - paperToDevelop[k].i] = -1;
				}
			} else if(rotation == 2) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].i][paperUsed.length - 1 - paperToDevelop[k].j] = false;
					indexCuboidOnPaper[paperUsed.length - 1 - paperToDevelop[k].i][paperUsed.length - 1 - paperToDevelop[k].j] = -1;
				}
			} else if(rotation == 3) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].j][paperToDevelop[k].i] = false;
					indexCuboidOnPaper[paperUsed.length - 1 - paperToDevelop[k].j][paperToDevelop[k].i] = -1;
				}
			}
		} else {
			if(rotation == 0) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].j][paperToDevelop[k].i] = false;
					indexCuboidOnPaper[paperToDevelop[k].j][paperToDevelop[k].i] = -1;
				}
			
			//Go counter-clockwise on rotation, so you can save an if condition:
			} else if(rotation == 3) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].i][paperUsed.length - 1 - paperToDevelop[k].j] = false;
					indexCuboidOnPaper[paperToDevelop[k].i][paperUsed.length - 1 - paperToDevelop[k].j] = -1;
				}
			} else if(rotation == 2) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].j][paperUsed.length - 1 - paperToDevelop[k].i] = false;
					indexCuboidOnPaper[paperUsed.length - 1 - paperToDevelop[k].j][paperUsed.length - 1 - paperToDevelop[k].i] = -1;
				}
			} else if(rotation == 1) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].i][paperToDevelop[k].j] = false;
					indexCuboidOnPaper[paperUsed.length - 1 - paperToDevelop[k].i][paperToDevelop[k].j] = -1;
				}
			}
		}
	
	}
	
	public static void switchOnOffPaperUsedForArrayRotatedOrReflected(
			boolean isTurnOn,
			Coord2D paperToDevelop[],
			boolean paperUsed[][],
			int rotation,
			boolean transpose) {
		
		if(!transpose) {
			if(rotation == 0) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].i][paperToDevelop[k].j] = isTurnOn;
				}
			} else if(rotation == 1) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].j][paperUsed.length - 1 - paperToDevelop[k].i] = isTurnOn;
				}
			} else if(rotation == 2) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].i][paperUsed.length - 1 - paperToDevelop[k].j] = isTurnOn;
				}
			} else if(rotation == 3) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].j][paperToDevelop[k].i] = isTurnOn;
				}
			}
		} else {
			if(rotation == 0) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].j][paperToDevelop[k].i] = isTurnOn;
				}
			
			//Go counter-clockwise on rotation, so you can save an if condition:
			} else if(rotation == 3) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperToDevelop[k].i][paperUsed.length - 1 - paperToDevelop[k].j] = isTurnOn;
				}
			} else if(rotation == 2) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].j][paperUsed.length - 1 - paperToDevelop[k].i] = isTurnOn;
				}
			} else if(rotation == 1) {
				
				for(int k=0; k<paperToDevelop.length; k++) {
					paperUsed[paperUsed.length - 1 - paperToDevelop[k].i][paperToDevelop[k].j] = isTurnOn;
				}
			}
		}
	
	}
	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	
	//I remove the recursive call because we don't really need it
	//You don't need it!

	//This algo Assumes we skip the symmetries


	public static int[] doDepthFirstSearch(boolean netToReplicate[][], Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int curAnswer[], int quickestAnswerToCompareTo[], boolean isCurrentlyAloneInFirst,
			Coord2D coord2DTable[][]) {


		ADD_NEXT_CELL:
		while(numCellsUsedDepth < cuboid.getNumCellsToFill()) {
			
			
			int regionIndex = 0;
			
			int numRotationIterationsSkipped = 0;
			
			//DEPTH-FIRST START:
			for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<numCellsUsedDepth; i++) {
				
				int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
				

				CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
				
				if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
						(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse)) {
	
					numRotationIterationsSkipped += NUM_ROTATIONS;
					
					/*
					 //This never happens for 4x1x1, so never mind!
					//Try to find an excuse not to continue:
					for(int j=0; j<neighbours.length; j++) {
						
						
						int new_i = paperToDevelop[i].i + nugdeBasedOnRotation[0][j];
						int new_j = paperToDevelop[i].j + nugdeBasedOnRotation[1][j];
						
						if(! paperUsed[new_i][new_j]
								&& netToReplicate[new_i][new_j]
							){
							System.out.println("BOOM");
							return null;
						}
		
					}
					 */
					continue;
				}
	
				
				int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
				
				//Try to attach a cell onto indexToUse using all 4 rotations:
				for(int j=0; j<neighbours.length; j++, numRotationIterationsSkipped++) {
					
					int rotationToAddCellOn = (j + curRotation) % NUM_ROTATIONS;
					
					int new_i = paperToDevelop[i].i + nugdeBasedOnRotation[0][rotationToAddCellOn];
					int new_j = paperToDevelop[i].j + nugdeBasedOnRotation[1][rotationToAddCellOn];
	
					int indexNewCell = neighbours[j].getIndex();
			
					if(paperUsed[new_i][new_j]) {
						//Cell we are considering to add is already there...
						continue;
	
					} else if(regions[regionIndex].getCellRegionsToHandleInRevOrder()[neighbours[j].getIndex()] == false) {
						continue;
	
					} else if(! netToReplicate[new_i][new_j]) {
						//Make sure to follow the netToRelplicate
						continue;
					
					
					} else if(cuboid.isCellIndexUsed(neighbours[j].getIndex())) {
	
						//Don't reuse a used cell:
						return null;
						
					} else if(regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse)
							&& regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) == regions[regionIndex].getMinOrderedCellCouldUsePerRegion() 
							&& j <  regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion()) {
	
						//The current rotation is out of order:
						return null;
	
					}
					
					int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[j].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
					
					if(SymmetryResolver.skipSearchBecauseOfASymmetryArg
							(cuboid, paperToDevelop, i, indexCuboidonPaper, rotationToAddCellOn, curRotation, paperUsed, indexToUse, indexNewCell)) {
						return null;
					}
					
					boolean cantAddCellBecauseOfOtherPaperNeighbours = FoldResolveOrderedRegionsSkipSymmetries.cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidonPaper,
							paperUsed, cuboid, numCellsUsedDepth,
							regions, regionIndex, indexToUse,
							indexNewCell, new_i, new_j, i
						);
					
					
					if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
						
						//Setup for adding new cell:
						cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
						
						paperUsed[new_i][new_j] = true;
						indexCuboidonPaper[new_i][new_j] = indexNewCell;
						
						paperToDevelop[numCellsUsedDepth] = coord2DTable[new_i][new_j];
	
						//Add cell to new region(s):
						
						regions[regionIndex].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);
						
						curAnswer[numCellsUsedDepth] = numRotationIterationsSkipped;
	
						if(isCurrentlyAloneInFirst) {
							//pass
		
						} else if(curAnswer[numCellsUsedDepth] < quickestAnswerToCompareTo[numCellsUsedDepth]) {
							isCurrentlyAloneInFirst = true;
							
						} else if(curAnswer[numCellsUsedDepth] > quickestAnswerToCompareTo[numCellsUsedDepth]) {
							return null;
						}
						numCellsUsedDepth += 1;
						//End setup
	
						// iterated again, but this time with a higher depth:
						// No need for recursion because we're just following 1 path.
						continue ADD_NEXT_CELL;
						
					} // End recursive if cond
					
					//At this point, we can't add the cell.
					return null;
	
				} // End loop rotation
			} //End loop index
			
			//At this point, we can't go any further because we used up all the indexes and all regions:
			return null;
		}

		//End of loop:
		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			
			if(isCurrentlyAloneInFirst) {
				//If you're tied for 1st, you're not faster than current fastest
				return curAnswer;

			} else {
				return null;
			}
		} else {
			
			//I don't think this is possible, but whatever.
			return null;
		}
		
	}
	
	public boolean isValidSetupAtIndexedStartLocationWithRotation(Coord2D paperToDevelop[], boolean netToReplicate[][], int indexCellSolutionCellList, int rotation, int cuboidStartIndex) {
		

		
		cuboidToUse.resetState();

		int startI = paperToDevelop[indexCellSolutionCellList].i;
		int startJ = paperToDevelop[indexCellSolutionCellList].j;
		
		int numCellsUsedDepth = 0;

		paperUsed[startI][startJ] = true;
		
		//Delete loop? Answer: No! It's used by symmetry resolver!
		for(int k=0; k<newPaperToDevelop.length; k++) {
			newPaperToDevelop[k] = null;
		}
		newPaperToDevelop[numCellsUsedDepth] = coord2DTable[startI][startJ];
		
		cuboidToUse.setCell(cuboidStartIndex, rotation);
		indexCuboidOnPaper[startI][startJ] = cuboidStartIndex;
		numCellsUsedDepth += 1;
		
		regionsToHandleRevOrder[0].resetStateWithStartIndexOnly(cuboidStartIndex);
	//END Setup to run imitation algo.
		
		

		return isValid(netToReplicate,
				newPaperToDevelop,
				indexCuboidOnPaper,
				paperUsed,
				cuboidToUse, 
				numCellsUsedDepth,
				regionsToHandleRevOrder,
				coord2DTable);
	}
	
	public static boolean isValid(boolean netToReplicate[][],
			Coord2D paperToDevelop[],
			int indexCuboidOnPaper[][],
			boolean paperUsed[][],
			CuboidToFoldOn cuboid, 
			int numCellsUsedDepth,
			Region defaultRegion[],
			Coord2D coord2DTable[][]) {

		int regionIndex = 0;
		
		ADD_NEXT_CELL:
		while(numCellsUsedDepth < cuboid.getNumCellsToFill()) {
			
			//DEPTH-FIRST START:
			for(int i=defaultRegion[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<numCellsUsedDepth; i++) {
				
				int indexToUse = indexCuboidOnPaper[paperToDevelop[i].i][paperToDevelop[i].j];
				
				CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
				
				int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
				
				//Try to attach a cell onto indexToUse using all 4 rotations:
				for(int j=0; j<neighbours.length; j++) {
					
					int rotationToAddCellOn = (j + curRotation) % NUM_ROTATIONS;
					
					int new_i = paperToDevelop[i].i + nugdeBasedOnRotation[0][rotationToAddCellOn];
					int new_j = paperToDevelop[i].j + nugdeBasedOnRotation[1][rotationToAddCellOn];
	
					int indexNewCell = neighbours[j].getIndex();
			
					if(paperUsed[new_i][new_j]) {
						//Cell we are considering to add is already there...
						continue;
	
					} else if(! netToReplicate[new_i][new_j]) {
						//Make sure to follow the netToRelplicate
						continue;
					
					} else if(cuboid.isCellIndexUsed(neighbours[j].getIndex())) {
	
						//Don't reuse a used cell:
						return false;
						
					}
					
					int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[j].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidOnPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = coord2DTable[new_i][new_j];

					//Add cell to new region::
					defaultRegion[regionIndex].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);
					
					numCellsUsedDepth += 1;
					//End setup

					// iterated again, but this time with a higher depth:
					// No need for recursion because we're just following 1 path.
					continue ADD_NEXT_CELL;
					
	
				} // End loop rotation
			} //End loop index
			
			//At this point, we can't go any further because we used up all the indexes and all regions:
			return false;
		}

		return true;
	
	}

	public static void printStateOfRotationBecauseOfError(boolean reflection, Coord2D paperToDevelop[], boolean array[][], boolean arrayRotated[][][], int rotation,
			int i, int startI, int startJ) {

		if(reflection) {
			System.out.println("DOH! BAD ROTATION for reflected array");
		} else {
			System.out.println("DOH! BAD ROTATION for unreflected array");
		}
		System.out.println("rotation: " + rotation);
		System.out.println("from: " + paperToDevelop[i].i + ", " +  paperToDevelop[i].j);
		System.out.println("To: " + startI + ", " + startJ);
		
		System.out.println("Before:");
		for(int i2=0; i2<array.length; i2++) {
			for(int j2=0; j2<array[0].length; j2++) {
				if(array[i2][j2]) {
					System.out.print('#');
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("After:");
		for(int i2=0; i2<arrayRotated[rotation].length; i2++) {
			for(int j2=0; j2<arrayRotated[rotation][0].length; j2++) {
				if(arrayRotated[rotation][i2][j2]) {
					System.out.print('#');
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.exit(1);
	}
	
	public static void printArrayDebug(boolean array[][]) {

		for(int i2=0; i2<array.length; i2++) {
			for(int j2=0; j2<array[0].length; j2++) {
				if(array[i2][j2]) {
					System.out.print('#');
				} else {
					System.out.print(".");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printOrderingSolution(int orderingReturned[]) {
		
		System.out.println("Ordering:");
		for(int k=0; k<orderingReturned.length; k++) {
			System.out.print(orderingReturned[k] + ", ");
		}
		System.out.println();

	}
	
	
	public static boolean debugArrayMatchesString(int array[], String debugOrdering) {
		
		if(array == null) {
			return false;
		}

		debugOrdering = debugOrdering.replace(" ", "");
		
		while(debugOrdering.endsWith(",")) {
			debugOrdering = debugOrdering.substring(0, debugOrdering.length() -1);
		}
		
		String tokens[] = debugOrdering.split(",");
		

		if(tokens.length != array.length) {
			return false;
		}
		
		for(int i=0; i<array.length; i++) {
			if(array[i] != pint(tokens[i])) {
				return false;
			}
		}
		
		return true;
		
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
