package OneNet3Cuboids.OldReferenceDupRemovers;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.Cuboid.SymmetryResolver.SymmetryResolver;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.Region.Region;
import number.IsNumber;

public class MemorylessUniqueCheckSkipSymmetriesForNx1x1MemManageOrig {

	//Loop other start locations and rotations to see if there's anything faster.
	// (i.e.: Anything that the algo would be found first)
	//Warning: This will only work if we model the algo correctly.
	
	public static long debugNumOnlyTopValid = 0;
	public static long debugNumMoreThanTopValid = 0;
	
	public static final int DEFAULT_ROTATION = 0;
	public static final boolean NO_REFLECTION = false;
	
	public static final int ROTATIONS_TIMES_RELECTIONS = 8;
	
	public static boolean isUnique(CuboidToFoldOn orig, Coord2D paperToDevelop[], boolean array[][]) {
		
		int quickestAnswerToCompareTo[] = null;
		boolean isCurrentlyAloneInFirst = true;
		boolean isUniqueSoFar = true;
		
		int START_INDEX = 0;

		//Variables to recycle/reuse for each iteration: 
		boolean paperUsed[][] = new boolean[array.length][array.length];
		int indexCuboidOnPaper[][] = new int[array.length][array.length];
		Coord2D newPaperToDevelop[] = new Coord2D[paperToDevelop.length];
		
		//Note that I'm also borrowing the boolean array[][] param... I'll put it back to the way it was.
		
		//TODO: Eliminate this loop to make it faster, but that would mean changing index on paper 0...
		//Tough choice! I'll do it much later!
		for(int i2=0; i2<paperUsed.length; i2++) {
			for(int j2=0; j2<paperUsed[0].length; j2++) {
				indexCuboidOnPaper[i2][j2] = -1;
			}
		}
		
		//if(array.length != array[0].length) {
		//	System.out.println("ERROR: dimensions have to be the same for MemManager To work.");
		//}
		
		// End variables to recycle/reuse for each iteration
				
	
		boolean validSetup[] = new boolean[paperToDevelop.length];
		validSetup[0] = true;
		
		int debugNumOtherValid = 0;
		for(int index=1; index<validSetup.length; index++) {
			validSetup[index] = isValidSetup(orig,
						paperToDevelop,
						array,
						index,
						paperUsed,
						indexCuboidOnPaper,
						newPaperToDevelop);
			
			if(validSetup[index]) {
				debugNumOtherValid++;
			}
			
			eraseChangesToPaperUsedAndIndexCuboidOnPaper(paperToDevelop, paperUsed, indexCuboidOnPaper, DEFAULT_ROTATION, false);
		}
		if(debugNumOtherValid == 1) {
			debugNumOnlyTopValid++;
		} else if(debugNumOtherValid >= 2){
			debugNumMoreThanTopValid++;
		} else {
			System.out.println("Top one should be valid.");
			System.exit(1);
		}
		if(debugNumOnlyTopValid > 0 && debugNumOnlyTopValid % 100000 == 0) {
			System.out.println("--");
			System.out.println("debugNumOnlyTopValid: " + debugNumOnlyTopValid);
			System.out.println("debugNumMoreThanTopValid: " + debugNumMoreThanTopValid);
		}
		
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
				
				CuboidToFoldOn cuboid = new CuboidToFoldOn(orig);

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
				newPaperToDevelop[numCellsUsedDepth] = new Coord2D(startI, startJ);
				
				cuboid.setCell(START_INDEX, 0);
				indexCuboidOnPaper[startI][startJ] = START_INDEX;
				numCellsUsedDepth += 1;
				
				Region regionsToHandleRevOrder[] = new Region[1];
				regionsToHandleRevOrder[0] = new Region(cuboid);
			//END Setup to run imitation algo.
				
				//How to debug:
				//if(debugArrayMatchesString(prevOrdering, "0, 0, 1, 2, 3, 6, 10, 9, 3, 14")) {
				//	System.out.println("DEBUG");
				//}

				int tmp[] = doDepthFirstSearch(array, newPaperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth,
						regionsToHandleRevOrder, new int[cuboid.getNumCellsToFill()], quickestAnswerToCompareTo, isCurrentlyAloneInFirst);
				
				if(tmp != null) {

					//Sanity check:
					//if(! validSetup[i]) {
					//	System.out.println("Invalid setup got results! (unreflected!)");
					//	System.exit(1);
					//}
					
					//Print fold for debug:
					//Utils.printFold(arrayRotated[rotation]);
					
					if(i == 0 && rotation == DEFAULT_ROTATION && reflect == NO_REFLECTION) {
						quickestAnswerToCompareTo = tmp;
						isCurrentlyAloneInFirst = false;
						
						//Can't do this because it breaks because of the fact we ignore regions in memoryless Algo:
						//sanityCheckOrderingComparedToPrevOrdering(tmp);

						//Debug:
						//System.out.println("firstOrderingArray for current solution:");
						//printOrderingSolution(tmp);
						

					} else {

						isUniqueSoFar = false;
						//System.out.println("FasterOrderingArray for current solution:");
						//printOrderingSolution(tmp);
						
					}
					
				} else if(tmp == null && i == 0  && rotation == DEFAULT_ROTATION && reflect == NO_REFLECTION) {
					System.out.println("ERROR: imitator algo rejected the solution that was found!");
					System.exit(1);
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
			Region regions[], int curAnswer[], int quickestAnswerToCompareTo[], boolean isCurrentlyAloneInFirst) {


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
						paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);
	
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
	
	public static boolean isValidSetup(CuboidToFoldOn origCuboidNeighboursAndDim,
			Coord2D paperToDevelop[],
			boolean netToReplicate[][],
			int indexCellInList,
			boolean paperUsed[][],
			int indexCuboidOnPaper[][],
			Coord2D newPaperToDevelop[]) {
		

		int START_INDEX = 0;
		
		CuboidToFoldOn newCuboid = new CuboidToFoldOn(origCuboidNeighboursAndDim);

		int startI = paperToDevelop[indexCellInList].i;
		int startJ = paperToDevelop[indexCellInList].j;
		
		int numCellsUsedDepth = 0;

		paperUsed[startI][startJ] = true;
		newPaperToDevelop[numCellsUsedDepth] = new Coord2D(startI, startJ);
		
		newCuboid.setCell(START_INDEX, 0);
		indexCuboidOnPaper[startI][startJ] = START_INDEX;
		numCellsUsedDepth += 1;
		
		Region defaultRegion[] = new Region[1];
		defaultRegion[0] = new Region(newCuboid);
	//END Setup to run imitation algo.
		

		return isValid(netToReplicate,
				newPaperToDevelop,
				indexCuboidOnPaper,
				paperUsed,
				newCuboid, 
				numCellsUsedDepth,
				defaultRegion);
	}
	
	public static boolean isValid(boolean netToReplicate[][],
			Coord2D paperToDevelop[],
			int indexCuboidOnPaper[][],
			boolean paperUsed[][],
			CuboidToFoldOn cuboid, 
			int numCellsUsedDepth,
			Region defaultRegion[]) {

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
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

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
