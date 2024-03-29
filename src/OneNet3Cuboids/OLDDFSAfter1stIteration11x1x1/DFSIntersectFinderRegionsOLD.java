package OneNet3Cuboids.OLDDFSAfter1stIteration11x1x1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.Cuboid.SymmetryResolver.SymmetryResolver;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.GraphUtils.PivotCellDescription;
import OneNet3Cuboids.Region.Region;
import OneNet3Cuboids.SolutionResovler.*;

public class DFSIntersectFinderRegionsOLD {

	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	
	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToWrap, CuboidToFoldOn cuboidToBringAlong) {
		solveCuboidIntersections(cuboidToWrap, cuboidToBringAlong, true);
	}
	
	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, boolean skipSymmetries) {
		SolutionResolverIntersectInterface solutionResolver = null;
		
		
		if(Utils.getTotalArea(cuboidToBuild.getDimensions()) != Utils.getTotalArea(cuboidToBringAlong.getDimensions())) {
			System.out.println("ERROR: The two cuboid to intersect don't have the same area.");
			System.exit(1);
		}
		
		// Set the solution resolver to different things depending on the size of the cuboid:
		solutionResolver = new StandardResolverForSmallIntersectSolution(cuboidToBuild);
		
		solveCuboidIntersections(cuboidToBuild, cuboidToBringAlong, skipSymmetries, solutionResolver);
	}

	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver) {
		
		
		//cube.set start location 0 and rotation 0
		

		//TODO: LATER use hashes to help.. (record potential expansions, and no-nos...)
		Coord2D paperToDevelop[] = new Coord2D[Utils.getTotalArea(cuboidToBuild.getDimensions())];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		int GRID_SIZE = 2*Utils.getTotalArea(cuboidToBuild.getDimensions());
	
		boolean paperUsed[][] = new boolean[GRID_SIZE][GRID_SIZE];
		int indexCuboidOnPaper[][] = new int[GRID_SIZE][GRID_SIZE];

		int indexCuboidOnPaper2ndCuboid[][] = new int[GRID_SIZE][GRID_SIZE];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				paperUsed[i][j] = false;
				indexCuboidOnPaper[i][j] = -1;
				indexCuboidOnPaper2ndCuboid[i][j] = -1;
			}
		}

		//Default start location GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		
		CuboidToFoldOn cuboid = new CuboidToFoldOn(cuboidToBuild);
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
		
		Region regionsToHandleRevOrder[] = new Region[1];
		regionsToHandleRevOrder[0] = new Region(cuboid);
		
		
		//TODO: Later try intersecting with all of them at once, so it's easier to get distinct solutions,
		// and maybe it's faster?

		//TODO: 2nd one
		ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidToBringAlong);
		
		System.out.println("Num starting points and rotations to check: " + startingPointsAndRotationsToCheck.size());
		for(int i=0; i<startingPointsAndRotationsToCheck.size(); i++) {
			
			int startIndex2ndCuboid =startingPointsAndRotationsToCheck.get(i).getCellIndex();
			int startRotation2ndCuboid = startingPointsAndRotationsToCheck.get(i).getRotationRelativeToCuboidMap();
			
			CuboidToFoldOn cuboidToBringAlongStartRot = new CuboidToFoldOn(cuboidToBringAlong);

			cuboidToBringAlongStartRot.setCell(startIndex2ndCuboid, startRotation2ndCuboid);
			indexCuboidOnPaper2ndCuboid[START_I][START_J] = startIndex2ndCuboid;
			
			int topBottombridgeUsedNx1x1[] = new int[Utils.getTotalArea(cuboidToBuild.getDimensions())];
		
			doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, regionsToHandleRevOrder, -1L, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1);
			
			
			System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + startIndex2ndCuboid + " and a rotation index of " + startRotation2ndCuboid +".");
			System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
			
		}
		
		//TODO: end todo 2nd one
		
		
		System.out.println("Final number of unique solutions: " + solutionResolver.getNumUniqueFound());
	}
	
	
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	public static int numIterations = 0;
	
	public static long doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], long limitDupSolutions, boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver, CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][],
			int topBottombridgeUsedNx1x1[]) {

		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			
			int indexes[][][] = new int[2][][];
			indexes[0] = indexCuboidonPaper;
			indexes[1] = indexCuboidOnPaper2ndCuboid;
			return solutionResolver.resolveSolution(cuboid, paperToDevelop, indexes, paperUsed);
		}

		regions = FoldResolveOrderedRegionsSkipSymmetries.handleCompletedRegionIfApplicable(regions, limitDupSolutions, indexCuboidonPaper, paperUsed);
		
		if(regions == null) {
			
			return 1L;
		}
		
		numIterations++;
		
		if(numIterations % 10000000L == 0) {
			
			System.out.println("Num iterations: " + numIterations);
			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			Utils.printFoldWithIndex( indexCuboidOnPaper2ndCuboid);
			System.out.println();
		}
		
		int regionIndex = regions.length - 1;
		long retDuplicateSolutions = 0L;
		
		//DEPTH-FIRST START:
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			
			if( ! regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse)) {
				continue;
			} else if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse)
				&& skipSymmetries) {
				continue;

			} else	if( 2*numCellsUsedDepth < paperToDevelop.length && 
					SymmetryResolver.skipSearchBecauseCuboidCouldProvablyNotBeBuiltThisWay
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse, regions[regionIndex], topBottombridgeUsedNx1x1) && skipSymmetries) {
				
				break;
			}
			
			CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
			int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
			
			int indexToUse2 = indexCuboidOnPaper2ndCuboid[paperToDevelop[i].i][paperToDevelop[i].j];
			int curRotationCuboid2 = cuboidToBringAlongStartRot.getRotationPaperRelativeToMap(indexToUse2);
			
			//Try to attach a cell onto indexToUse using all 4 rotations:
			for(int j=0; j<neighbours.length; j++) {
				
				if(cuboid.isCellIndexUsed(neighbours[j].getIndex())) {
					
					//Don't reuse a used cell:
					continue;
					
				} else if(regions[regionIndex].getCellRegionsToHandleInRevOrder()[neighbours[j].getIndex()] == false) {
					continue;
	
				} else if(regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) == regions[regionIndex].getMinOrderedCellCouldUsePerRegion() 
						&& j <  regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion()) {
					continue;
				}

				int neighbourIndexCuboid2 = (j - curRotationCuboid2 + curRotation+ NUM_ROTATIONS) % NUM_ROTATIONS;
				
				int indexNewCell2 = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getIndex();
				
				if(cuboidToBringAlongStartRot.isCellIndexUsed(indexNewCell2)) {
					//no good!
					continue;
				}
				
				int rotationToAddCellOn = (j + curRotation) % NUM_ROTATIONS;
				
				int new_i = paperToDevelop[i].i + nugdeBasedOnRotation[0][rotationToAddCellOn];
				int new_j = paperToDevelop[i].j + nugdeBasedOnRotation[1][rotationToAddCellOn];

				int indexNewCell = neighbours[j].getIndex();
				
				
				if(paperUsed[new_i][new_j]) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				
				int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[j].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
				int rotationNeighbourPaperRelativeToMap2 = (curRotationCuboid2 - cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getRot() + NUM_ROTATIONS)  % NUM_ROTATIONS;
				
				if(SymmetryResolver.skipSearchBecauseOfASymmetryArg
						(cuboid, paperToDevelop, i, indexCuboidonPaper, rotationToAddCellOn, curRotation, paperUsed, indexToUse, indexNewCell)
					&& skipSymmetries == true) {
					continue;
				}
				
				boolean cantAddCellBecauseOfOtherPaperNeighbours = FoldResolveOrderedRegionsSkipSymmetries.cantAddCellBecauseOfOtherPaperNeighbours(paperToDevelop, indexCuboidonPaper,
						paperUsed, cuboid, numCellsUsedDepth,
						regions, regionIndex, indexToUse,
						indexNewCell, new_i, new_j, i
					);
				
				
				Region regionsBeforePotentailRegionSplit[] = regions;
				int prevNewMinOrderedCellCouldUse = regions[regionIndex].getMinOrderedCellCouldUsePerRegion();
				int prevMinCellRotationOfMinCellToDev = regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion();
				
				if( !cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Split the regions if possible:
					regions = splitRegionsIfNewCellSplitsRegions(paperToDevelop, indexCuboidonPaper,
							paperUsed, cuboid, numCellsUsedDepth,
							regions,
							indexToUse, j, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev,
							new_i, new_j, indexNewCell, rotationNeighbourPaperRelativeToMap,
							skipSymmetries,
							cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, indexNewCell2, rotationNeighbourPaperRelativeToMap2,
							topBottombridgeUsedNx1x1);
					
					if(regions == null) {
						cantAddCellBecauseOfOtherPaperNeighbours = true;
						regions = regionsBeforePotentailRegionSplit;
					}
				}
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					if(indexToUse == 0) {
						topBottombridgeUsedNx1x1[indexNewCell] = j;
					} else if(indexToUse == cuboid.getCellsUsed().length - 1) {
						topBottombridgeUsedNx1x1[indexNewCell] = NUM_ROTATIONS + j;
					} else {
						topBottombridgeUsedNx1x1[indexNewCell] = topBottombridgeUsedNx1x1[indexToUse];
					}

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;

					//Add cell to new region(s):
					for(int r=regionsBeforePotentailRegionSplit.length - 1; r<regions.length; r++) {
						regions[r].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);						
					}

					numCellsUsedDepth += 1;
					//End setup

					long newLimitDupSolutions = limitDupSolutions;
					if(limitDupSolutions >= 0) {
						newLimitDupSolutions -= retDuplicateSolutions;
					}
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regions, newLimitDupSolutions, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1);

					if(numCellsUsedDepth < regions[0].getCellIndexToOrderOfDev().size()) {
						System.out.println("WHAT???");
						System.exit(1);
					}
					
					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;

					regions = regionsBeforePotentailRegionSplit;
					
					//Remove cell from last region(s):
					regions[regions.length - 1].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);
	
					paperUsed[new_i][new_j] = false;
					indexCuboidonPaper[new_i][new_j] = -1;
					paperToDevelop[numCellsUsedDepth] = null;

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = -1;
					
					cuboid.removeCell(indexNewCell);
					cuboidToBringAlongStartRot.removeCell(indexNewCell2);
					//End tear down


					if(limitDupSolutions >= 0 && retDuplicateSolutions > limitDupSolutions) {
						//Handling option to only find 1 or 2 solutions:
						//This has to be done after tear-down because these objects are soft-copied...
						return retDuplicateSolutions;
					}

					regionIndex = regions.length - 1;
					
				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return retDuplicateSolutions;
	}
	


	public static boolean depthFirstAlgoWillFindAsolutionInRegionIndex(Coord2D paperToDevelop[], int indexCuboidonPaper[][],
			boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int regionIndex, boolean skipSymmetries,
			CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][],
			int topBottombridgeUsedNx1x1[]) {
		
		
		Region regionArgToUse[] = new Region[1];
		regionArgToUse[0] = regions[regionIndex];
		
		if(doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regionArgToUse, 0L, skipSymmetries, null, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1)
				> 0L) {
			return true;
		} else {
		
			return false;
		}
	}
 
	public static boolean depthFirstAlgoWillFindOnly1solutionInRegionIndex(Coord2D paperToDevelop[], int indexCuboidonPaper[][],
			boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int regionIndex, boolean skipSymmetries,
			CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][],
			int topBottombridgeUsedNx1x1[]) {
		
		
		Region regionArgToUse[] = new Region[1];
		regionArgToUse[0] = regions[regionIndex];
		
		if(doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regionArgToUse, 1L, skipSymmetries, null, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1)
				== 1L) {
			return true;
		} else {
		
			return false;
		}
	}
	
	

	//j = rotation relativeCuboidMap
	public static Region[] splitRegionsIfNewCellSplitsRegions(Coord2D paperToDevelop[], int indexCuboidonPaper[][],
			boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[],
			int indexToUse, int newMinRotationToUse, int prevNewMinOrderedCellCouldUse, int prevMinCellRotationOfMinCellToDev,
			int new_i, int new_j, int indexNewCell, int rotationNeighbourPaperRelativeToMap,
			boolean skipSymmetries,
			CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][], int indexNewCell2, int rotationNeighbourPaperRelativeToMap2,
			int topBottombridgeUsedNx1x1[]) {
		
		boolean cantAddCellBecauseOfOtherPaperNeighbours = false;
		
		//Trying to divide and conquer here:
		
		CoordWithRotationAndIndex neighboursOfNewCell[] = cuboid.getNeighbours(indexNewCell);
		
		int regionIndex = regions.length - 1;
		
		//areCellsSepartedCuboid(CuboidToFoldOn cuboid, int startIndex, int goalIndex)
		
		//flag is redundant, but expressive!
		boolean foundABlankNeighbour = false;
		int firstIndex = -1;

		//Add potentially new cell just for test:
		cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
				
		
		TRY_TO_DIVDE_REGIONS:
		for(int rotIndexToFill=0; rotIndexToFill<NUM_ROTATIONS; rotIndexToFill++) {
			
			

			int indexNeighbourOfNewCell = neighboursOfNewCell[rotIndexToFill].getIndex();
			
			if( ! cuboid.isCellIndexUsed(indexNeighbourOfNewCell)) {
				
				if( ! foundABlankNeighbour) {
					firstIndex = indexNeighbourOfNewCell;
					foundABlankNeighbour = true;
				} else {
					
					if(FoldResolveOrderedRegionsSkipSymmetries.areCellsSepartedCuboid(cuboid, firstIndex, indexNeighbourOfNewCell)) {
						
						//START DIVIDING THE REGION:
						
						//Look for a 3-three way:
						
						boolean found3Way = false;
						int indexNeighbourThirdWay = -1;
						for(int thirdRot=rotIndexToFill+1; thirdRot<NUM_ROTATIONS; thirdRot++) {
							
							int curThirdNeighbour = neighboursOfNewCell[thirdRot].getIndex();
							
							
							if(! cuboid.isCellIndexUsed(curThirdNeighbour)
									&& FoldResolveOrderedRegionsSkipSymmetries.areCellsSepartedCuboid(cuboid, curThirdNeighbour, firstIndex)
									&& FoldResolveOrderedRegionsSkipSymmetries.areCellsSepartedCuboid(cuboid, curThirdNeighbour, indexNeighbourOfNewCell)) {
								found3Way = true;
								indexNeighbourThirdWay = curThirdNeighbour;
							}
						}
						//End look for 3-three (cuboid separated into 3 regions)

						int numNewWays = 1;
						if(found3Way) {
							numNewWays = 2;
						}
							
						Region regionsSplit[] = new Region[regions.length + numNewWays];
						
						for(int k=0; k<regions.length - 1; k++) {
							regionsSplit[k] = regions[k];
						}
						
						for(int k=0; k<numNewWays + 1; k++) {
								
							int indexToAdd = regions.length - 1 + k;
							
							if(k==0) {
								regionsSplit[indexToAdd] = new Region(cuboid, regions[regionIndex], firstIndex, newMinRotationToUse);
							} else if(k == 1) {
								regionsSplit[indexToAdd] = new Region(cuboid, regions[regionIndex], indexNeighbourOfNewCell, newMinRotationToUse);	
							} else if(k == 2) {
								regionsSplit[indexToAdd] = new Region(cuboid, regions[regionIndex], indexNeighbourThirdWay, newMinRotationToUse);
								
							} else {
								System.out.println("ERROR: k is too high. k= " + k);
								System.exit(1);
							}
							
							
							//Quick check if each region has at least 1 solution:
							//TODO: put this quick check in function
							//Mini setup for checking a single solution:
							
							//Don't set it! (It's supposed to be set for now)
							//cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);

							//TODO: should I sort the regions before doing the quick check?
							// I remember it not being faster before.
							cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
		
							paperUsed[new_i][new_j] = true;
							indexCuboidonPaper[new_i][new_j] = indexNewCell;
							paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

							indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;
							
							regionsSplit[indexToAdd].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, newMinRotationToUse);
							
							if(indexToUse == 0) {
								topBottombridgeUsedNx1x1[indexNewCell] = newMinRotationToUse;
							} else if(indexToUse == cuboid.getCellsUsed().length - 1) {
								topBottombridgeUsedNx1x1[indexNewCell] = NUM_ROTATIONS + newMinRotationToUse;
							} else {
								topBottombridgeUsedNx1x1[indexNewCell] = topBottombridgeUsedNx1x1[indexToUse];
							}

							numCellsUsedDepth += 1;
							
							if(depthFirstAlgoWillFindAsolutionInRegionIndex(paperToDevelop, indexCuboidonPaper,
									paperUsed, cuboid, numCellsUsedDepth,
									regionsSplit, indexToAdd, skipSymmetries,
									cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid,
									topBottombridgeUsedNx1x1)
								== false) {
								
								
								//System.out.println("Region impossible!");
								
								cantAddCellBecauseOfOtherPaperNeighbours = true;
							}
							
							numCellsUsedDepth -= 1;
							
							
							//Mini tear down
							regionsSplit[indexToAdd].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);


							paperUsed[new_i][new_j] = false;
							indexCuboidonPaper[new_i][new_j] = -1;
							paperToDevelop[numCellsUsedDepth] = null;

							indexCuboidOnPaper2ndCuboid[new_i][new_j] = -1;

							//Don't remove this: (It's supposed to be set for now)
							//cuboid.removeCell(indexNewCell);

							cuboidToBringAlongStartRot.removeCell(indexNewCell2);
							
							//End mini-tear down
							//END TODO:  put quick check in function
							
							if(cantAddCellBecauseOfOtherPaperNeighbours) {
								//System.out.println("quick cut off");
								break TRY_TO_DIVDE_REGIONS;
							}
							
						}
						
						regionIndex = regions.length - 1;
						
						//Later:
						//TODO: check if region is the same as a hole in the net to go faster (Also useful for the 3Cuboid 1 net check)

						//reordering regions by whether or not they have a single solution and then by size:
						//TODO: afterwards, try incorporating algo that checks if there's less than or equal to N solutions? Probably bad...
						
						regions = regionsSplit;

						boolean regionHasOneSolution[] = new boolean[regions.length];
						for(int i2=regions.length - 1 - numNewWays; i2< regions.length; i2++) {
							
							paperUsed[new_i][new_j] = true;
							indexCuboidonPaper[new_i][new_j] = indexNewCell;
							paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

							indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;

							cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
	
							regionsSplit[i2].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, newMinRotationToUse);
							
							if(indexToUse == 0) {
								topBottombridgeUsedNx1x1[indexNewCell] = newMinRotationToUse;
							} else if(indexToUse == cuboid.getCellsUsed().length - 1) {
								topBottombridgeUsedNx1x1[indexNewCell] = NUM_ROTATIONS + newMinRotationToUse;
							} else {
								topBottombridgeUsedNx1x1[indexNewCell] = topBottombridgeUsedNx1x1[indexToUse];
							}

							numCellsUsedDepth += 1;
							
							regionHasOneSolution[i2] = depthFirstAlgoWillFindOnly1solutionInRegionIndex(paperToDevelop, indexCuboidonPaper,
									paperUsed, cuboid, numCellsUsedDepth,
									regionsSplit, i2, skipSymmetries,
									cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid,
									topBottombridgeUsedNx1x1);

							numCellsUsedDepth -= 1;
							
							
							regionsSplit[i2].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);

							cuboidToBringAlongStartRot.removeCell(indexNewCell2);

							paperUsed[new_i][new_j] = false;
							indexCuboidonPaper[new_i][new_j] = -1;
							paperToDevelop[numCellsUsedDepth] = null;
							

							indexCuboidOnPaper2ndCuboid[new_i][new_j] = -1;
							
						}
						
						//Sort new regions by whether it has only 1 solution, and then by size:
						for(int i2=regions.length - 1 - numNewWays; i2< regions.length; i2++) {
							
							int curBestIndex = i2;
							
							for(int j2=i2+1; j2<regions.length; j2++) {
								
								if(regionHasOneSolution[i2] && ! regionHasOneSolution[j2]) {
									curBestIndex = j2;

								} else if(regions[i2].getNumCellsInRegion() < regions[j2].getNumCellsInRegion()) {
									curBestIndex = j2;
								}
							}
							
							Region tmp = regions[i2];
							regions[i2] = regions[curBestIndex];
							regions[curBestIndex] = tmp;
						}
						//END sort
						
						 
						
						//End sort regions by size and if the region only has 1 solution
						
						break TRY_TO_DIVDE_REGIONS;
					}
				}
			}
		}//END LOOP

		
		//Remove potential new cell once test is done:
		cuboid.removeCell(indexNewCell);
		
		if(! cantAddCellBecauseOfOtherPaperNeighbours) {
			return regions;
		} else {
			return null;
		}
	}

	/*https://www.sciencedirect.com/science/article/pii/S0925772117300160
	 * 
	 *  "From the necessary condition, the smallest possible surface area that can fold into two boxes is 22,
	 *   and the smallest possible surface area for three different boxes is 46.
	 *   (...) However, the area 46 is too huge to search. "
	 *  
	 *  Challenge accepted!
	 */

	public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered Regions intersection skip symmetries Nx1x1:");

		//solveCuboidIntersections(new CuboidToFoldOn(9, 1, 1), new CuboidToFoldOn(4, 3, 1));

		//solveCuboidIntersections(new CuboidToFoldOn(8, 1, 1), new CuboidToFoldOn(5, 2, 1));
		//It got 35675 again, but this time it only took 3 hours! It took almost 2 days last time!
		
		solveCuboidIntersections(new CuboidToFoldOn(7, 1, 1), new CuboidToFoldOn(3, 3, 1));
		//It got 1070 (again) (They got 1080, but I think they were wrong)
		
		//solveCuboidIntersections(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1));
		//It got 2263!

		//solveCuboidIntersections(new CuboidToFoldOn(2, 1, 1), new CuboidToFoldOn(1, 2, 1));
		
		//Best 5,1,1: 3 minute 45 seconds (3014430 solutions) (December 27th)
		
		
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		
		/* 5x1x1 on December 8th:
			Final number of unique solutions: 3014430
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		 */
		
	}
	/*//TODO:
	46	1 � 1 � 11, 1 � 2 � 7, 1 � 3 � 5
54	1 � 1 � 13, 1 � 3 � 6, 3 � 3 � 3
58	1 � 1 � 14, 1 � 2 � 9, 1 � 4 � 5
62	1 � 1 � 15, 1 � 3 � 7, 2 � 3 � 5
64	1 � 2 � 10, 2 � 2 � 7, 2 � 4 � 4
70	1 � 1 � 17, 1 � 2 � 11, 1 � 3 � 8, 1 � 5 � 5
88	1 � 2 � 14, 1 � 4 � 8, 2 � 2 � 10, 2 � 4 � 6
*/
	
}
