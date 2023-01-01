package OneNet3Cuboids.DupRemover;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.Cuboid.SymmetryResolver.SymmetryResolver;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.Region.Region;
import OneNet3Cuboids.SolutionResovler.SolutionResolverInterface;

public class MemorylessUniqueCheckSkipSymmetries {

	public static boolean isUnique(CuboidToFoldOn orig, Coord2D paperToDevelop[], boolean array[][]) {
		
		int quickestAnswerToCompareTo[] = null;
		boolean isCurrentlyAloneInFirst = true;
		boolean isUniqueSoFar = true;
		
		int START_INDEX = 0;

		//TODO: reduce copy/paste code.
		//TODO: maybe make arrays based on paperToDevelop?
		
		//Loop other start locations and rotations to see if there's anything faster.
		// (i.e.: Anything that could be found first)

		SEARCH_UNREFLECTED:
		for(int i=0; i<paperToDevelop.length; i++) {

			boolean arrayRotated[][][] = new boolean[NUM_ROTATIONS][][];
			for(int rotation=0; rotation<NUM_ROTATIONS; rotation++) {
				arrayRotated[rotation] = getArrayRotated(array, rotation);
			}

			for(int rotation=0; rotation<NUM_ROTATIONS; rotation++) {
				
				boolean paperUsed[][] = new boolean[arrayRotated[rotation].length][arrayRotated[rotation][0].length];
				int indexCuboidOnPaper[][] = new int[arrayRotated[rotation].length][arrayRotated[rotation][0].length];
				
				//TODO: Eliminate this loop to make it faster, but that would mean changing index on paper 0...
				//Tough choice! I'll do it much later!
				for(int i2=0; i2<paperUsed.length; i2++) {
					for(int j2=0; j2<paperUsed[0].length; j2++) {
						paperUsed[i2][j2] = false;
						indexCuboidOnPaper[i2][j2] = -1;
					}
				}
				CuboidToFoldOn cuboid = new CuboidToFoldOn(orig);

				Coord2D newPaperToDevelop[] = new Coord2D[paperToDevelop.length];

				int startI = getIAfterRotation(arrayRotated[rotation], paperToDevelop[i].i, paperToDevelop[i].j, rotation);
				int startJ = getJAfterRotation(arrayRotated[rotation], paperToDevelop[i].i, paperToDevelop[i].j, rotation);
				
				if( ! arrayRotated[rotation][startI][startJ]) {
					
					System.out.println("DOH! BAD ROTATION 1");
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
				int numCellsUsedDepth = 0;

				paperUsed[startI][startJ] = true;
				newPaperToDevelop[numCellsUsedDepth] = new Coord2D(startI, startJ);
				
				cuboid.setCell(START_INDEX, 0);
				indexCuboidOnPaper[startI][startJ] = START_INDEX;
				numCellsUsedDepth += 1;
				
				Region regionsToHandleRevOrder[] = new Region[1];
				regionsToHandleRevOrder[0] = new Region(cuboid);
				
				
				
				int tmp[] = doDepthFirstSearch(arrayRotated[rotation], newPaperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth,
						regionsToHandleRevOrder, new int[cuboid.getNumCellsToFill()], quickestAnswerToCompareTo, isCurrentlyAloneInFirst);
				
				if(tmp != null) {

					Utils.printFold(arrayRotated[rotation]);
					if(i == 0 && rotation == 0) {
						quickestAnswerToCompareTo = tmp;
						isCurrentlyAloneInFirst = false;

						System.out.println("firstOrderingArray for current solution:");
						for(int k=0; k<tmp.length; k++) {
							System.out.println(tmp[k]);
						}
					} else {

						isUniqueSoFar = false;
						
						/*System.out.println("FasterOrderingArray for current solution:");
						for(int k=0; k<tmp.length; k++) {
							System.out.println(tmp[k]);
						}*/

						
						break SEARCH_UNREFLECTED;
					}
					
				}
			}
		}
		
		
		
		//TODO: also loop for mirrored solution (use getTranspose for this)
		// Don't forget to also transpose the start locations! 
		if(isUniqueSoFar) {
			
			boolean transposeArray[][] = getTranspose(array);
			
			boolean arrayRotatedAndReflected[][][] = new boolean[NUM_ROTATIONS][][];
			for(int rotation=0; rotation<NUM_ROTATIONS; rotation++) {
				arrayRotatedAndReflected[rotation] = getArrayRotated(transposeArray, rotation);
				
				//TODO: TEMP
				for(int i2=0; i2<arrayRotatedAndReflected[rotation].length; i2++) {
					for(int j2=0; j2<arrayRotatedAndReflected[rotation][0].length; j2++) {
						if(arrayRotatedAndReflected[rotation][i2][j2]) {
							System.out.print('#');
						} else {
							System.out.print(".");
						}
					}
					System.out.println();
				}
				System.out.println();
				//TODO: TEMP
			}
			
			//Search reflected solution (It's just the transpose)
			SEARCH_REFLECTED:
			for(int i=0; i<paperToDevelop.length; i++) {
				for(int rotation=0; rotation<NUM_ROTATIONS; rotation++) {
					
					boolean paperUsed[][] = new boolean[arrayRotatedAndReflected[rotation].length][arrayRotatedAndReflected[rotation][0].length];
					int indexCuboidOnPaper[][] = new int[arrayRotatedAndReflected[rotation].length][arrayRotatedAndReflected[rotation][0].length];
					
					//TODO: Eliminate this loop to make it faster, but that would mean changing index on paper 0...
					//Tough choice! I'll do it much later!
					for(int i2=0; i2<paperUsed.length; i2++) {
						for(int j2=0; j2<paperUsed[0].length; j2++) {
							paperUsed[i2][j2] = false;
							indexCuboidOnPaper[i2][j2] = -1;
						}
					}
					CuboidToFoldOn cuboid = new CuboidToFoldOn(orig);

					Coord2D newPaperToDevelop[] = new Coord2D[paperToDevelop.length];

					//Transpose it!
					int startI = getIAfterRotation(arrayRotatedAndReflected[rotation], paperToDevelop[i].j, paperToDevelop[i].i, rotation);
					int startJ = getJAfterRotation(arrayRotatedAndReflected[rotation], paperToDevelop[i].j, paperToDevelop[i].i, rotation);

					/*if(quickestAnswerToCompareTo[4] == 10 && quickestAnswerToCompareTo[5] == 7 && quickestAnswerToCompareTo[3] == 2
							&& rotation == 3 && startI == 5 && startJ == 6) {
						System.out.println("DEBUG");
					}*/
					
					if( ! arrayRotatedAndReflected[rotation][startI][startJ]) {
						System.out.println("DOH! BAD ROTATION 2");
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
						for(int i2=0; i2<arrayRotatedAndReflected[rotation].length; i2++) {
							for(int j2=0; j2<arrayRotatedAndReflected[rotation][0].length; j2++) {
								if(arrayRotatedAndReflected[rotation][i2][j2]) {
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
					
					int numCellsUsedDepth = 0;

					paperUsed[startI][startJ] = true;
					newPaperToDevelop[numCellsUsedDepth] = new Coord2D(startI, startJ);
					
					cuboid.setCell(START_INDEX, 0);
					indexCuboidOnPaper[startI][startJ] = START_INDEX;
					numCellsUsedDepth += 1;
					
					Region regionsToHandleRevOrder[] = new Region[1];
					regionsToHandleRevOrder[0] = new Region(cuboid);
					
					
					
					int tmp[] = doDepthFirstSearch(arrayRotatedAndReflected[rotation], newPaperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth,
							regionsToHandleRevOrder, new int[cuboid.getNumCellsToFill()], quickestAnswerToCompareTo, isCurrentlyAloneInFirst);
					
					if(tmp != null) {

						Utils.printFold(arrayRotatedAndReflected[rotation]);
						
						isUniqueSoFar = false;
						/*
						System.out.println("FasterOrderingArray for current solution is a reflect solution:");
						for(int k=0; k<tmp.length; k++) {
							System.out.println(tmp[k]);
						}*/


						break SEARCH_REFLECTED;
						
					}
				}
			}
		}
		
		//TODO:
		sanityCheck(isUniqueSoFar, array);
		//
		
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
	
	public static int getIAfterRotation(boolean array[][], int i, int j, int rotation) {
		
		if(rotation == 0) {
			return i;
		} else if(rotation == 1) {
			return j;
		} else if(rotation == 2) {
			return array.length - 1 - i;
		} else if(rotation == 3) {
			return array[0].length - 1 - j;
		} else {
			System.out.println("DOH!");
			System.exit(1);
			return -1;
		}
		
	}
	
	public static int getJAfterRotation(boolean array[][], int i, int j, int rotation) {
		
		if(rotation == 0) {
			return j;
		} else if(rotation == 1) {
			return array.length - 1 - i;
		} else if(rotation == 2) {
			return array[0].length - 1 - j;
		} else if(rotation == 3) {
			return i;
		} else {
			System.out.println("DOH!");
			System.exit(1);
			return -1;
		}
		
	}
	
	
	public static boolean[][] getArrayRotated(boolean array[][], int rotation) {
		
		boolean ret[][] = null;
		
		if(rotation == 0) {
			return array;

		} else if(rotation == 1) {
			ret = new boolean[array[0].length][array.length];
			
			for(int i=0; i<ret.length; i++) {
				for(int j=0; j<ret[0].length; j++) {
					ret[i][j] = array[ret[0].length - 1- j][i];
				}
			}

		} else if(rotation == 2) {
			ret = new boolean[array.length][array[0].length];
			
			for(int i=0; i<ret.length; i++) {
				for(int j=0; j<ret[0].length; j++) {
					ret[i][j] = array[ret.length - 1- i][ret[0].length - 1- j];
				}
			}

		} else if(rotation == 3) {
			ret = new boolean[array[0].length][array.length];
			
			for(int i=0; i<ret.length; i++) {
				for(int j=0; j<ret[0].length; j++) {
					ret[i][j] = array[j][ret.length - 1- i];
				}
			}
		}
		
		return ret;
	}

	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	
	//TODO: don't do recursion!
	//You don't need it!

	//Assumes we skip the symmetries
	
	//TODO: maybe it could be a loop instead?
	//TODO: return int[]
	//TODO: rotationRelativeNetToReplicate and mirrored could be done before the function starts.
	public static int[] doDepthFirstSearch(boolean netToReplicate[][], Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int curAnswer[], int quickestAnswerToCompareTo[], boolean isCurrentlyAloneInFirst) {

		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			
			if(isCurrentlyAloneInFirst) {
				//If you're tied for 1st, you're not faster than current fastest
				return curAnswer;

			} else {
				return null;
			}
		}
		
		int regionIndex = regions.length - 1;
		
		int numRotationIterationsSkipped = 0;
		
		//DEPTH-FIRST START:
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			
			if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse)) {

				numRotationIterationsSkipped += NUM_ROTATIONS;
				//TODO: maybe try adding a 'don't use index flag here?
				continue;
			}

			CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
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
				
				int lastRegionIndexBeforePotentailRegionSplit = regions.length - 1;

				if( !cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Split the regions if possible:
					regions = FoldResolveOrderedRegionsSkipSymmetries.splitRegionsIfNewCellSplitsRegions(paperToDevelop, indexCuboidonPaper,
							paperUsed, cuboid, numCellsUsedDepth,
							regions,
							indexToUse, j, regions[regionIndex].getMinOrderedCellCouldUsePerRegion(), regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion(),
							new_i, new_j, indexNewCell, rotationNeighbourPaperRelativeToMap,
							true, null);
					
					if(regions == null) {
						cantAddCellBecauseOfOtherPaperNeighbours = true;
					}
				}
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					//Add cell to new region(s):
					for(int r=lastRegionIndexBeforePotentailRegionSplit; r<regions.length; r++) {
						regions[r].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);						
					}
					
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

					return doDepthFirstSearch(netToReplicate, paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth,
							regions, curAnswer, quickestAnswerToCompareTo, isCurrentlyAloneInFirst);

					
				} // End recursive if cond
				
				//At this point, we can't add the cell.
				return null;

			} // End loop rotation
		} //End loop index

		return null;
	}
	
	
	public static void sanityCheck(boolean memorylessAnswer, boolean array[][]) {
		
		boolean basicCheckResultUniq = BasicUniqueCheck.isUnique(array);
		//System.out.println(max);
		
		if(basicCheckResultUniq && ! memorylessAnswer) {
			System.out.println("ERROR_1: Orig Basic unique check says this is a new solution but MemorylessUniqueCheck says it isn't");
			//System.exit(1);
		} else if(! basicCheckResultUniq && memorylessAnswer) {

			System.out.println("ERROR_2: Orig Basic unique check same this is a dup but MemorylessUniqueCheck says this is new");
			System.exit(1);
		}
		
	}
}
