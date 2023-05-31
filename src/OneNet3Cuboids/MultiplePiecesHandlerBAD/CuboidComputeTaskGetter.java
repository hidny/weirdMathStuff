package OneNet3Cuboids.MultiplePiecesHandlerBAD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.Cuboid.SymmetryResolver.SymmetryResolver;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.FancyTricks.ThreeBombHandler;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.GraphUtils.PivotCellDescription;
import OneNet3Cuboids.Region.Region;
import OneNet3Cuboids.SolutionResovler.*;

public class CuboidComputeTaskGetter {


	public static long curNumPiecesCreated = 0;
	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	
	
	public static void getComputeTask(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, boolean skipSymmetries
			, int maxDepth, int targetTaskIndex) {
		
		curNumPiecesCreated = 0;
		SolutionResolverIntersectInterface solutionResolver = null;
		

		if(maxDepth <= 0 || Utils.getTotalArea(cuboidToBuild.getDimensions())  <= maxDepth) {
			System.out.println("ERROR: invalid start depth of " + maxDepth);
			System.exit(1);
		}
		
		if(Utils.getTotalArea(cuboidToBuild.getDimensions()) != Utils.getTotalArea(cuboidToBringAlong.getDimensions())) {
			System.out.println("ERROR: The two cuboid to intersect don't have the same area.");
			System.exit(1);
		}
		
		// Set the solution resolver to different things depending on the size of the cuboid:
		solutionResolver = new StandardResolverForSmallIntersectSolution(cuboidToBuild);
		
		getComputeTaskInner(cuboidToBuild, cuboidToBringAlong, skipSymmetries, solutionResolver, maxDepth, targetTaskIndex);
	}
	
	
	private static void getComputeTaskInner(CuboidToFoldOn cuboidToBuild, CuboidToFoldOn cuboidToBringAlong, boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver,
			int maxDepth, int targetTaskIndex) {
		
		
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
		
		ThreeBombHandler threeBombHandler = new ThreeBombHandler(cuboid);
		

		ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck = PivotCellDescription.getUniqueRotationListsWithCellInfo(cuboidToBringAlong);
		
		System.out.println("Num starting points and rotations to check: " + startingPointsAndRotationsToCheck.size());
		
		for(int i=0; i<startingPointsAndRotationsToCheck.size() && (curNumPiecesCreated <= targetTaskIndex || targetTaskIndex < 0); i++) {
			
			int startIndex2ndCuboid =startingPointsAndRotationsToCheck.get(i).getCellIndex();
			int startRotation2ndCuboid = startingPointsAndRotationsToCheck.get(i).getRotationRelativeToCuboidMap();
			
			CuboidToFoldOn cuboidToBringAlongStartRot = new CuboidToFoldOn(cuboidToBringAlong);

			cuboidToBringAlongStartRot.setCell(startIndex2ndCuboid, startRotation2ndCuboid);
			indexCuboidOnPaper2ndCuboid[START_I][START_J] = startIndex2ndCuboid;
			
			int topBottombridgeUsedNx1x1[] = new int[Utils.getTotalArea(cuboidToBuild.getDimensions())];
			
			long debugIterations[] = new long[Utils.getTotalArea(cuboidToBuild.getDimensions())]; 
		
			getComputeTaskForStartingPointAndRotation(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, regionsToHandleRevOrder, -1L, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1, threeBombHandler, false, debugIterations, maxDepth, targetTaskIndex);
			

		}
		
		
		
	}
	
	
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	//TODO: return ArrayList and not a long...
	
	private static void getComputeTaskForStartingPointAndRotation(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], long limitDupSolutions, boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver, CuboidToFoldOn cuboidToBringAlongStartRot, int indexCuboidOnPaper2ndCuboid[][],
			int topBottombridgeUsedNx1x1[],
			ThreeBombHandler threeBombHandler,
			boolean debugNope, long debugIterations[],
			int maxDepth, int targetTaskIndex) {

		if(numCellsUsedDepth == maxDepth) {

			System.out.println();
			System.out.println("PIECE FOUND:");
			System.out.println("piece index: " + curNumPiecesCreated);
			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
			
			System.out.println("Last cell inserted: " + indexCuboidonPaper[paperToDevelop[numCellsUsedDepth - 1].i][paperToDevelop[numCellsUsedDepth - 1].j]);
			
			if( targetTaskIndex == curNumPiecesCreated) {
				System.out.println("USE THIS ONE!");
				ComputeTaskMain.computeTask = new ComputeTaskDescription(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth,
						regions, limitDupSolutions, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid,
						topBottombridgeUsedNx1x1,
						threeBombHandler,
						debugNope, debugIterations);
				
				
				System.out.println("Debug 1");
				System.out.println("Target task index: " + targetTaskIndex);
				System.out.println("Cell depth: " + numCellsUsedDepth);
				System.out.println("Max depth: " + maxDepth);
				Utils.printFold(paperUsed);
				Utils.printFoldWithIndex(indexCuboidonPaper);
				Utils.printFoldWithIndex(indexCuboidOnPaper2ndCuboid);
				System.out.println("----");
			}
			
			curNumPiecesCreated++;
			
			//TODO: fill an ArrayList with relevant info?
			return;
		}
		
		int regionIndex = regions.length - 1;
		long retDuplicateSolutions = 0L;
		

		debugIterations[numCellsUsedDepth] = -2;
		
		//TODO: use a cache, and compare results.
		int maxOrderBasedOn3Bomb = threeBombHandler.getMaxOrderIndex(cuboid,
				paperToDevelop,
				indexCuboidonPaper,
				regions[regionIndex],
				curNumPiecesCreated,
				numCellsUsedDepth,
				topBottombridgeUsedNx1x1
			);
		
		//DEPTH-FIRST START:
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<=maxOrderBasedOn3Bomb && i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
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
			for(int dirNewCellAdd=0; dirNewCellAdd<NUM_ROTATIONS; dirNewCellAdd++) {
				
				int neighbourArrayIndex = (dirNewCellAdd - curRotation + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				if(cuboid.isCellIndexUsed(neighbours[neighbourArrayIndex].getIndex())) {
					
					//Don't reuse a used cell:
					continue;
					
				} else if(regions[regionIndex].getCellRegionsToHandleInRevOrder()[neighbours[neighbourArrayIndex].getIndex()] == false) {
					continue;
	
				} else if(regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) == regions[regionIndex].getMinOrderedCellCouldUsePerRegion() 
						&& dirNewCellAdd <  regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion()) {
					continue;
				}

				int neighbourIndexCuboid2 = (dirNewCellAdd - curRotationCuboid2 + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				int indexNewCell2 = cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getIndex();
				
				if(cuboidToBringAlongStartRot.isCellIndexUsed(indexNewCell2)) {
					//no good!
					continue;
				}
				
				
				int new_i = paperToDevelop[i].i + nugdeBasedOnRotation[0][dirNewCellAdd];
				int new_j = paperToDevelop[i].j + nugdeBasedOnRotation[1][dirNewCellAdd];

				int indexNewCell = neighbours[neighbourArrayIndex].getIndex();
				
				
				if(paperUsed[new_i][new_j]) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				
				int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[neighbourArrayIndex].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
				int rotationNeighbourPaperRelativeToMap2 = (curRotationCuboid2 - cuboidToBringAlongStartRot.getNeighbours(indexToUse2)[neighbourIndexCuboid2].getRot() + NUM_ROTATIONS)  % NUM_ROTATIONS;
				
				if(SymmetryResolver.skipSearchBecauseOfASymmetryArg
						(cuboid, paperToDevelop, i, indexCuboidonPaper, dirNewCellAdd, curRotation, paperUsed, indexToUse, indexNewCell)
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
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					
					//Setup for adding new cell:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					cuboidToBringAlongStartRot.setCell(indexNewCell2, rotationNeighbourPaperRelativeToMap2);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					if(indexToUse == 0) {
						topBottombridgeUsedNx1x1[indexNewCell] = dirNewCellAdd;
					} else if(indexToUse == cuboid.getCellsUsed().length - 1) {
						topBottombridgeUsedNx1x1[indexNewCell] = NUM_ROTATIONS + dirNewCellAdd;
					} else {
						topBottombridgeUsedNx1x1[indexNewCell] = topBottombridgeUsedNx1x1[indexToUse];
					}

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = indexNewCell2;

					//Add cell to new region(s):
					for(int r=regionsBeforePotentailRegionSplit.length - 1; r<regions.length; r++) {
						regions[r].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, dirNewCellAdd);						
					}

					
					numCellsUsedDepth += 1;
					//End setup

					long newLimitDupSolutions = limitDupSolutions;
					if(limitDupSolutions >= 0) {
						newLimitDupSolutions -= retDuplicateSolutions;
					}
					
					threeBombHandler.addCell(paperUsed, indexCuboidonPaper, cuboid,  regions[regions.length - 1],
							new_i, new_j, indexNewCell, rotationNeighbourPaperRelativeToMap, topBottombridgeUsedNx1x1, curNumPiecesCreated);
					
					
					getComputeTaskForStartingPointAndRotation(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regions, newLimitDupSolutions, skipSymmetries, solutionResolver, cuboidToBringAlongStartRot, indexCuboidOnPaper2ndCuboid, topBottombridgeUsedNx1x1, threeBombHandler, debugNope, debugIterations, maxDepth, targetTaskIndex);

					if(curNumPiecesCreated > targetTaskIndex) {
						System.out.println("HELLO");
						return;
					}
					
					if(numCellsUsedDepth < regions[0].getCellIndexToOrderOfDev().size()) {
						System.out.println("WHAT???");
						System.exit(1);
					}
					
					
					//Tear down (undo add of new cell)
					numCellsUsedDepth -= 1;

					regions = regionsBeforePotentailRegionSplit;
					

					//Remember to keep this after the regions update

					
					//Remove cell from last region(s):
					regions[regions.length - 1].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);
	
					paperUsed[new_i][new_j] = false;
					indexCuboidonPaper[new_i][new_j] = -1;
					paperToDevelop[numCellsUsedDepth] = null;

					indexCuboidOnPaper2ndCuboid[new_i][new_j] = -1;
					
					cuboid.removeCell(indexNewCell);
					cuboidToBringAlongStartRot.removeCell(indexNewCell2);
					
					threeBombHandler.removeCell(paperToDevelop, paperUsed, indexCuboidonPaper, cuboid,  regions[regions.length - 1],
							new_i, new_j, indexNewCell, rotationNeighbourPaperRelativeToMap, topBottombridgeUsedNx1x1, curNumPiecesCreated);
					
					//End tear down


					if(limitDupSolutions >= 0 && retDuplicateSolutions > limitDupSolutions) {
						//Handling option to only find 1 or 2 solutions:
						//This has to be done after tear-down because these objects are soft-copied...
						return;
					}

					regionIndex = regions.length - 1;
					
				} // End recursive if cond
			} // End loop rotation
		} //End loop index

		return;
	}
	


	/*//TODO:
46	1 × 1 × 11, 1 × 2 × 7, 1 × 3 × 5
54	1 × 1 × 13, 1 × 3 × 6, 3 × 3 × 3
58	1 × 1 × 14, 1 × 2 × 9, 1 × 4 × 5
62	1 × 1 × 15, 1 × 3 × 7, 2 × 3 × 5
64	1 × 2 × 10, 2 × 2 × 7, 2 × 4 × 4
70	1 × 1 × 17, 1 × 2 × 11, 1 × 3 × 8, 1 × 5 × 5
88	1 × 2 × 14, 1 × 4 × 8, 2 × 2 × 10, 2 × 4 × 6
*/
	
}
