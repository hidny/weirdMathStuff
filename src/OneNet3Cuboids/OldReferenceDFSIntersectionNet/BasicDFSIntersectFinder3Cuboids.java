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

public class BasicDFSIntersectFinder3Cuboids {

	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	
	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToWrap, CuboidToFoldOn cuboidsToBringAlong[]) {
		solveCuboidIntersections(cuboidToWrap, cuboidsToBringAlong, true);
	}
	
	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidsToBringAlong[], boolean skipSymmetries) {
		SolutionResolverIntersectInterface solutionResolver = null;
		
		
		for(int i=0; i<cuboidsToBringAlong.length; i++) {
			if(Utils.getTotalArea(cuboidToBuild.getDimensions()) != Utils.getTotalArea(cuboidsToBringAlong[i].getDimensions())) {
				System.out.println("ERROR: The two cuboid to intersect don't have the same area. (index = " + i + ")");
				System.exit(1);
			}
		}
		
		// Set the solution resolver to different things depending on the size of the cuboid:
		solutionResolver = new StandardResolverForSmallIntersectSolution(cuboidToBuild);
		
		solveCuboidIntersections(cuboidToBuild, cuboidsToBringAlong, skipSymmetries, solutionResolver);
	}
	
	public static long numIterations = 0L;

	public static void solveCuboidIntersections(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidsToBringAlong[], boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver) {
		
		
		//cube.set start location 0 and rotation 0
		

		//TODO: LATER use hashes to help.. (record potential expansions, and no-nos...)
		Coord2D paperToDevelop[] = new Coord2D[Utils.getTotalArea(cuboidToBuild.getDimensions())];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		int GRID_SIZE = 2*Utils.getTotalArea(cuboidToBuild.getDimensions());
	
		boolean paperUsed[][] = new boolean[GRID_SIZE][GRID_SIZE];
		int indexCuboidOnPaper[][] = new int[GRID_SIZE][GRID_SIZE];

		int indexCuboidOnPaperOtherCuboids[][][] = new int[cuboidsToBringAlong.length][GRID_SIZE][GRID_SIZE];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				paperUsed[i][j] = false;
				indexCuboidOnPaper[i][j] = -1;
				for(int k=0; k<cuboidsToBringAlong.length; k++) {
					indexCuboidOnPaperOtherCuboids[k][i][j] = -1;
				}
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
		ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck[] = new ArrayList[cuboidsToBringAlong.length];
				
		//PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidToBringAlong);
				
		//indexCuboidOnPaperOtherCuboids
		
		//TODO: don't avoid recursion in future:
		if(cuboidsToBringAlong.length != 2) {
			System.out.println("ERROR: this currently only accepts 2 cuboids to bring along");
			System.exit(1);
		}
		
		startingPointsAndRotationsToCheck[0] = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidsToBringAlong[0]);
		startingPointsAndRotationsToCheck[1] = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidsToBringAlong[1]);
		
		int numStartingToCheck = startingPointsAndRotationsToCheck[0].size() * startingPointsAndRotationsToCheck[1].size();
		
		System.out.println("Num starting points and rotations to check: " + numStartingToCheck);
		System.out.println("It's " + startingPointsAndRotationsToCheck[0].size() + " by " + startingPointsAndRotationsToCheck[1].size());
		
		CuboidToFoldOn cuboidsToBringAlongStartRot[] = new CuboidToFoldOn[2];
		
		for(int i=0; i<startingPointsAndRotationsToCheck[0].size(); i++) {
			
			int startIndex2ndCuboid = startingPointsAndRotationsToCheck[0].get(i).getCellIndex();
			int startRotation2ndCuboid = startingPointsAndRotationsToCheck[0].get(i).getRotationRelativeToCuboidMap();
			
			cuboidsToBringAlongStartRot[0] = new CuboidToFoldOn(cuboidsToBringAlong[0]);

			cuboidsToBringAlongStartRot[0].setCell(startIndex2ndCuboid, startRotation2ndCuboid);
			indexCuboidOnPaperOtherCuboids[0][START_I][START_J] = startIndex2ndCuboid;
			
			for(int j=0; j<startingPointsAndRotationsToCheck[1].size(); j++) {
				

				int startIndex3rdCuboid = startingPointsAndRotationsToCheck[1].get(j).getCellIndex();
				int startRotation3rdCuboid = startingPointsAndRotationsToCheck[1].get(j).getRotationRelativeToCuboidMap();
				
				cuboidsToBringAlongStartRot[1] = new CuboidToFoldOn(cuboidsToBringAlong[1]);

				cuboidsToBringAlongStartRot[1].setCell(startIndex3rdCuboid, startRotation3rdCuboid);
				indexCuboidOnPaperOtherCuboids[1][START_I][START_J] = startIndex3rdCuboid;
				
				doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, regionsToHandleRevOrder, -1L, skipSymmetries, solutionResolver, cuboidsToBringAlongStartRot, indexCuboidOnPaperOtherCuboids);
				
				
				System.out.println("Done with trying to intersect 2nd cuboid that has a start index of " + startIndex2ndCuboid + " and a rotation index of " + startRotation2ndCuboid +".");
				System.out.println("                          and 3rd cuboid that has a start index of " + startIndex3rdCuboid + " and a rotation index of " + startRotation3rdCuboid +".");
				
				System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
			}
		}
		
		//TODO: end todo 2nd one
		
		
		System.out.println("Final number of unique solutions: " + solutionResolver.getNumUniqueFound());
	}
	
	
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	
	public static long doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], long limitDupSolutions, boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver, CuboidToFoldOn cuboidsToBringAlongStartRot[], int indexOtherCuboidsOnPaper[][][]) {

		numIterations++;
		
		if(numIterations % 1000000L == 0) {
			
			System.out.println("Num iterations: " + numIterations);
			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			Utils.printFoldWithIndex( indexOtherCuboidsOnPaper[0]);
			Utils.printFoldWithIndex(indexOtherCuboidsOnPaper[1]);
			System.out.println("Solutions: " + solutionResolver.getNumUniqueFound());
			System.out.println();
		}
		
		
		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			int indexes[][][] = new int[3][][];
			indexes[0] = indexCuboidonPaper;
			indexes[1] = indexOtherCuboidsOnPaper[0];
			indexes[2] = indexOtherCuboidsOnPaper[1];
			return solutionResolver.resolveSolution(cuboid, paperToDevelop, indexes, paperUsed);
		}

		regions = FoldResolveOrderedRegionsSkipSymmetries.handleCompletedRegionIfApplicable(regions, limitDupSolutions, indexCuboidonPaper, paperUsed);
		
		if(regions == null) {
			return 1L;
		}
		
		
		int regionIndex = regions.length - 1;
		long retDuplicateSolutions = 0L;
		
		//DEPTH-FIRST START:
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			
			if(SymmetryResolver.skipSearchBecauseOfASymmetryArgDontCareAboutRotation
					(cuboid, paperToDevelop, indexCuboidonPaper, i,indexToUse)
				&& skipSymmetries) {
				continue;
			}
			
			CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
			int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
			
			int indexToUse2 = indexOtherCuboidsOnPaper[0][paperToDevelop[i].i][paperToDevelop[i].j];
			int curRotationCuboid2 = cuboidsToBringAlongStartRot[0].getRotationPaperRelativeToMap(indexToUse2);
			

			int indexToUse3 = indexOtherCuboidsOnPaper[1][paperToDevelop[i].i][paperToDevelop[i].j];
			int curRotationCuboid3 = cuboidsToBringAlongStartRot[1].getRotationPaperRelativeToMap(indexToUse3);
			
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
				
				int indexNewCell2 = cuboidsToBringAlongStartRot[0].getNeighbours(indexToUse2)[neighbourIndexCuboid2].getIndex();
				
				if(cuboidsToBringAlongStartRot[0].isCellIndexUsed(indexNewCell2)) {
					//no good!
					continue;
				}

				int neighbourIndexCuboid3 = (j - curRotationCuboid3 + curRotation+ NUM_ROTATIONS) % NUM_ROTATIONS;
				int indexNewCell3 = cuboidsToBringAlongStartRot[1].getNeighbours(indexToUse3)[neighbourIndexCuboid3].getIndex();
				
				if(cuboidsToBringAlongStartRot[1].isCellIndexUsed(indexNewCell3)) {
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
				int rotationNeighbourPaperRelativeToMap2 = (curRotationCuboid2 - cuboidsToBringAlongStartRot[0].getNeighbours(indexToUse2)[neighbourIndexCuboid2].getRot() + NUM_ROTATIONS)  % NUM_ROTATIONS;
				int rotationNeighbourPaperRelativeToMap3 = (curRotationCuboid3 - cuboidsToBringAlongStartRot[1].getNeighbours(indexToUse3)[neighbourIndexCuboid3].getRot() + NUM_ROTATIONS)  % NUM_ROTATIONS;
				
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
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					cuboidsToBringAlongStartRot[0].setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
					cuboidsToBringAlongStartRot[1].setCell(indexNewCell3, rotationNeighbourPaperRelativeToMap3);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					
					indexOtherCuboidsOnPaper[0][new_i][new_j] = indexNewCell2;
					indexOtherCuboidsOnPaper[1][new_i][new_j] = indexNewCell3;
					

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
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regions, newLimitDupSolutions, skipSymmetries, solutionResolver, cuboidsToBringAlongStartRot, indexOtherCuboidsOnPaper);

					if(numCellsUsedDepth < regions[0].getCellIndexToOrderOfDev().size()) {
						System.out.println("WHAT???");
						System.exit(1);
					}
					
					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;

					regions = regionsBeforePotentailRegionSplit;
					
					//Remove cell from last region:
					regions[regions.length - 1].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);
	
					paperUsed[new_i][new_j] = false;
					indexCuboidonPaper[new_i][new_j] = -1;
					paperToDevelop[numCellsUsedDepth] = null;

					indexOtherCuboidsOnPaper[0][new_i][new_j] = -1;
					indexOtherCuboidsOnPaper[1][new_i][new_j] = -1;
					
					cuboid.removeCell(indexNewCell);
					cuboidsToBringAlongStartRot[0].removeCell(indexNewCell2);
					cuboidsToBringAlongStartRot[1].removeCell(indexNewCell3);
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

		CuboidToFoldOn main = new CuboidToFoldOn(11, 1, 1);
		CuboidToFoldOn others[] = new CuboidToFoldOn[] { new CuboidToFoldOn(7, 2, 1), new CuboidToFoldOn(5, 3, 1)};
		/*
		CuboidToFoldOn main = new CuboidToFoldOn(2, 1, 1);
		CuboidToFoldOn others[] = new CuboidToFoldOn[] { new CuboidToFoldOn(2, 1, 1), new CuboidToFoldOn(1, 1, 2)};
		*/
		
		//solveCuboidIntersections(new CuboidToFoldOn(8, 1, 1), new CuboidToFoldOn(5, 2, 1));
		
		//solveCuboidIntersections(new CuboidToFoldOn(7, 1, 1), new CuboidToFoldOn(3, 3, 1));
		//It got 1070 (again) (They got 1080, but I think they were wrong)
		
		//solveCuboidIntersections(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1));
		//It got 2263!

		//solveCuboidIntersections(new CuboidToFoldOn(2, 1, 1), new CuboidToFoldOn(1, 2, 1));
		
		//Best 5,1,1: 3 minute 45 seconds (3014430 solutions) (December 27th)
		
		solveCuboidIntersections(main, others);
		System.out.println("Current UTC timestamp in milliseconds: " + System.currentTimeMillis());
		
		
		/* 5x1x1 on December 8th:
			Final number of unique solutions: 3014430
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		 */
		
	}
	
}
