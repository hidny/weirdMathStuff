package OneNet3Cuboids.OldReferenceDFSIntersectionNet;

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

public class DFSIntersectFinderWithSeveredLogic {

	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	public static int debugNumUnreachable = 0;
	
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
			

			boolean isSevered[][] = new boolean[paperToDevelop.length][paperToDevelop.length];
		
			doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, regionsToHandleRevOrder, -1L, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, isSevered);
			
		}
		
		//TODO: end todo 2nd one
		
		
		System.out.println("Final number of unique solutions: " + solutionResolver.getNumUniqueFound());
	}
	
	
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	
	public static long doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], long limitDupSolutions, boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver, CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][],
			boolean isSevered[][]) {

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
		
		//TODO: only create coord2Ds once...
		ArrayList<Coord2D> newlySeveredCells2ndCuboid = new ArrayList<Coord2D>();
		
		int regionIndex = regions.length - 1;
		long retDuplicateSolutions = 0L;
		
		
		//DEPTH-FIRST START:
		MAIN_LOOP:
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			


			//Add previous index to severed:
			if(i > 0) {
				int indexToUse2 = indexCuboidOnPaper2ndCuboid[paperToDevelop[i-1].i][paperToDevelop[i-1].j];
				for(int n=0; n<cuboidToBringAlongStartRot.getNeighbours(indexToUse2).length; n++) {
					
					int neighbourIndex = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[n].getIndex();
					
					if(cuboidToBringAlongStartRot.isCellIndexUsed(neighbourIndex)
							&& ! isSevered[indexToUse2][neighbourIndex]) {
						if((indexToUse2 == 10 || neighbourIndex == 10)
								&& (indexToUse2 == 21 || neighbourIndex == 21)) {
							/*Utils.printFold(paperUsed);
							System.out.println("1st cuboid:");
							Utils.printFoldWithIndex(indexCuboidonPaper);
							System.out.println("2nd cuboid:");
							Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
							System.out.println("What");
							System.out.println("Debug3");*/
						}
						newlySeveredCells2ndCuboid.add(
								new Coord2D(indexToUse2, neighbourIndex)
							);
					}
						
				}
			}
			//End to severed
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			
			if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse)
				&& skipSymmetries) {
				
				continue;
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
					
				} else if(regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse)
						&& regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) == regions[regionIndex].getMinOrderedCellCouldUsePerRegion() 
						&& j <  regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion()) {
					continue;

				} else if(regions[regionIndex].getCellRegionsToHandleInRevOrder()[neighbours[j].getIndex()] == false) {
					continue;
				}

				int neighbourIndexCuboid2 = (j - curRotationCuboid2 + curRotation+ NUM_ROTATIONS) % NUM_ROTATIONS;
				
				int indexNewCell2 = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getIndex();
				
				if(cuboidToBringAlongStartRot.isCellIndexUsed(indexNewCell2)) {
					//no good!
					continue;
				}
				
				//Add to severed:
				if(j > 0) {
					for(int n=0; n<j; n++) {
						
						int neighbourIndexCuboid2tmp = (n - curRotationCuboid2 + curRotation+ NUM_ROTATIONS) % NUM_ROTATIONS;
						
						int neighbourIndex = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2tmp].getIndex();
						
						if(cuboidToBringAlongStartRot.isCellIndexUsed(neighbourIndex)
								&& ! isSevered[indexToUse2][neighbourIndex]) {
							newlySeveredCells2ndCuboid.add(
									new Coord2D(indexToUse2, neighbourIndex)
								);
							
							if((indexToUse2 == 10 || neighbourIndex == 10)
									&& (indexToUse2 == 21 || neighbourIndex == 21)) {
								
								/*Utils.printFold(paperUsed);
								System.out.println("1st cuboid:");
								Utils.printFoldWithIndex(indexCuboidonPaper);
								System.out.println("2nd cuboid:");
								Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
								System.out.println("What");
								System.out.println("Debug2");*/
							}
						}
							
					}
				}
				//End add to severed
				
				//END TODO is this right?
				
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
					regions = FoldResolveOrderedRegionsSkipSymmetries.splitRegionsIfNewCellSplitsRegions(paperToDevelop, indexCuboidonPaper,
							paperUsed, cuboid, numCellsUsedDepth,
							regions,
							indexToUse, j, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev,
							new_i, new_j, indexNewCell, rotationNeighbourPaperRelativeToMap,
							skipSymmetries);
					
					if(regions == null) {
						cantAddCellBecauseOfOtherPaperNeighbours = true;
						regions = regionsBeforePotentailRegionSplit;
					}
				}
				
				//TODO:
				//Check severed 1:
				
				//TODO: bookmark how far along you got:
				for(int indexSev=0; indexSev<newlySeveredCells2ndCuboid.size(); indexSev++) {
					
					isSevered[newlySeveredCells2ndCuboid.get(indexSev).i][newlySeveredCells2ndCuboid.get(indexSev).j] = true;
					isSevered[newlySeveredCells2ndCuboid.get(indexSev).j][newlySeveredCells2ndCuboid.get(indexSev).i] = true;
					
				}
				
				
				for(int s=0; s<isSevered.length; s++) {
					//cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[n].getIndex();
					if( ! cuboidToBringAlongStartRot.isCellIndexUsed(s)) {
						
						//TODO: make this path to an active cell later.
						/*for(int n=0; n<NUM_ROTATIONS; n++) {
							if(isSevered[s][cuboidToBringAlongStartRot.getNeighbours(s)[n].getIndex()] == false) {
								severedFromCuboidSoFar = false;
								break;
							}
						}*/
						boolean cellReachable = isCellReachable(isSevered, s, cuboidToBringAlongStartRot);
						
						if( ! cellReachable) {
							cantAddCellBecauseOfOtherPaperNeighbours = true;
							//TODO:
							System.out.println("Index " + s + " is unreachable. (good)");
							Utils.printFold(paperUsed);
							System.out.println("1st cuboid:");
							Utils.printFoldWithIndex(indexCuboidonPaper);
							System.out.println("2nd cuboid:");
							Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
							//System.out.println("What");
							//System.exit(1);
							
							debugNumUnreachable++;
							break MAIN_LOOP;
						}
					}
				}
				
				//END TODO
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
					
					ArrayList<Coord2D> severedAfterAddingCell = new ArrayList<Coord2D>();
					
				
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;

					//Add cell to new region(s):
					for(int r=regionsBeforePotentailRegionSplit.length - 1; r<regions.length; r++) {
						regions[r].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);						
					}

					boolean sanityTestGoodLoop = false;
					
					
					//Add severed if the first cuboid won't connect to neighbour
					for(int n=0; n<NUM_ROTATIONS; n++) {
						
						int tmpNeighbourNewIndexCuboid2 = (n - rotationNeighbourPaperRelativeToMap2 + rotationNeighbourPaperRelativeToMap + NUM_ROTATIONS) % NUM_ROTATIONS;
						
						/*System.out.println("...");
						System.out.println(cuboid.getNeighbours(indexNewCell)[n].getIndex());
						System.out.println(cuboidToBringAlongStartRot.getNeighbours(indexNewCell2)[tmpNeighbourNewIndexCuboid2].getIndex());
						System.out.println("...");
						*/
						
						if(cuboid.getNeighbours(indexNewCell)[n].getIndex() == indexToUse
								&& cuboidToBringAlongStartRot.getNeighbours(indexNewCell2)[tmpNeighbourNewIndexCuboid2].getIndex() == indexToUse2) {
							sanityTestGoodLoop = true;
						}
						
						if(cuboid.isCellIndexUsed(cuboid.getNeighbours(indexNewCell)[n].getIndex())
							&& ! cuboidToBringAlongStartRot
									.isCellIndexUsed(cuboidToBringAlongStartRot.getNeighbours(indexNewCell2)[tmpNeighbourNewIndexCuboid2].getIndex())) {
							
							if( ! isSevered[indexNewCell2][tmpNeighbourNewIndexCuboid2]) {
								isSevered[indexNewCell2][tmpNeighbourNewIndexCuboid2] = true;
								isSevered[tmpNeighbourNewIndexCuboid2][indexNewCell2] = true;
								
								severedAfterAddingCell.add(new Coord2D(indexNewCell2, tmpNeighbourNewIndexCuboid2));
								
								/*System.out.println("Test Add severed after adding cell");
								Utils.printFold(paperUsed);
								System.out.println("1st cuboid:");
								Utils.printFoldWithIndex(indexCuboidonPaper);
								System.out.println("2nd cuboid:");
								Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
								System.out.println("What");
								*/
								//System.exit(1);
								
								
							}
								//TODO: undo this!

						}
					}
					if( ! sanityTestGoodLoop) {

						Utils.printFold(paperUsed);
						Utils.printFoldWithIndex(indexCuboidonPaper);
						Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
						System.out.println("DOH! Bad rotation");
						System.exit(1);
					} else {
						//System.out.println("Good rotation");
					}
					
					
					numCellsUsedDepth += 1;
					//End setup

					
					long newLimitDupSolutions = limitDupSolutions;
					if(limitDupSolutions >= 0) {
						newLimitDupSolutions -= retDuplicateSolutions;
					}
					
					
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regions, newLimitDupSolutions, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, isSevered);

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
					
					//Undo severed links because of the cell that was added:
					for(int indexSev=0; indexSev<severedAfterAddingCell.size(); indexSev++) {
						isSevered[severedAfterAddingCell.get(indexSev).i][severedAfterAddingCell.get(indexSev).j] = false;
						isSevered[severedAfterAddingCell.get(indexSev).j][severedAfterAddingCell.get(indexSev).i] = false;
					}
					//End undo severed links
					
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

		for(int indexSev=0; indexSev<newlySeveredCells2ndCuboid.size(); indexSev++) {
			
			isSevered[newlySeveredCells2ndCuboid.get(indexSev).i][newlySeveredCells2ndCuboid.get(indexSev).j] = false;
			isSevered[newlySeveredCells2ndCuboid.get(indexSev).j][newlySeveredCells2ndCuboid.get(indexSev).i] = false;
				
		}

		return retDuplicateSolutions;
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
		System.out.println("Fold Resolver Ordered Regions skip symmetries Nx1x1:");
		

		//solveCuboidIntersections(new CuboidToFoldOn(8, 1, 1), new CuboidToFoldOn(5, 2, 1));
		
		solveCuboidIntersections(new CuboidToFoldOn(7, 1, 1), new CuboidToFoldOn(3, 3, 1));
		//It got 1070 (again) (They got 1080, but I think they were wrong)
		
		//solveCuboidIntersections(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1));
		//It got 2263!

		//solveCuboidIntersections(new CuboidToFoldOn(2, 1, 1), new CuboidToFoldOn(1, 2, 1));
		
		//Best 5,1,1: 3 minute 45 seconds (3014430 solutions) (December 27th)
		
		System.out.println("Debug num unreachable: " + debugNumUnreachable);
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		
		/* 5x1x1 on December 8th:
			Final number of unique solutions: 3014430
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		 */
		
	}
	
	
	public static boolean isCellReachable(boolean isSevered[][], int startIndex, CuboidToFoldOn cuboidToBringAlongStartRot) {
		
		LinkedList<Integer> queueCells = new LinkedList<Integer>();
		boolean explored[] = new boolean[isSevered.length];
		
		explored[startIndex] = true;
		queueCells.add(startIndex);
		
		
		while(queueCells.isEmpty() == false) {
			
			int curCell = queueCells.getFirst();
			queueCells.remove();
			
			for(int n=0; n<NUM_ROTATIONS; n++) {
				
				int neighbour = cuboidToBringAlongStartRot.getNeighbours(curCell)[n].getIndex();
				
				if( ! isSevered[curCell][neighbour] ){
					if(cuboidToBringAlongStartRot.isCellIndexUsed(neighbour)) {
						return true;
					} else if(! explored[neighbour]) {
						explored[neighbour] = true;
						queueCells.add(neighbour);
					}
				}
				
			}
		
		}
		
		return false;
		
	}
	
}
