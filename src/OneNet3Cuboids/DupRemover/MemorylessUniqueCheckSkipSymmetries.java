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
		
		
		//TODO
		//COPY/PASTE CODE FOR SETUP:
		
		boolean paperUsed[][] = new boolean[array.length][array[0].length];
		int indexCuboidOnPaper[][] = new int[array.length][array[0].length];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				paperUsed[i][j] = false;
				indexCuboidOnPaper[i][j] = -1;
			}
		}

		//Default start location GRID_SIZE / 2, GRID_SIZE / 2
		

		//TODO: loop on every possible start position:
		int START_I = array.length/2;
		int START_J = array.length/2;
		//END TODO
		
		//TODO: don't let this be verbose:
		CuboidToFoldOn cuboid = new CuboidToFoldOn(orig);
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
		//END COPY/PASTE CODE FOR SETUP:
		
		
		
		//TODO: loop 0 to 4
		//int rotationRelativeNetToReplicate = 0;
		
		
		//TODO: later:
		int altAnswer[] = null;
		 
		boolean isCurrentlyAloneInFirst = true;
		
		int firstOrderingArray[] = doDepthFirstSearch(array, paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth,
				regionsToHandleRevOrder, new int[cuboid.getNumCellsToFill()], altAnswer, isCurrentlyAloneInFirst);
		
		
		Utils.printFold(array);
		System.out.println("firstOrderingArray for current solution:");
		for(int i=0; i<firstOrderingArray.length; i++) {
			System.out.println(firstOrderingArray[i]);
		}
		//LOL: the numbers actually look correct even though it's 1:20 AM and I'm tired!
		//TODO: compare to other possible answers to make sure this is the 1st one.
		

		System.out.println("End firstOrderingArray for current solution:");


		//TODO: loop other start locations and rotations to see if there's anything faster.
		
		//TODO: also loop for mirrored solution (use getTranspose for this)
		// Don't forget to also transpose the start locations! 
		
		
		
		//TODO:
		sanityCheck(false, array);
		//
		
		return false;
	}
	
	public boolean[][] getTranspose(boolean array[][]) {
		boolean transpose[][] = new boolean[array[0].length][array.length];
		
		for(int i=0; i<transpose.length; i++) {
			for(int j=0; j<transpose[0].length; j++) {
				transpose[i][j] = array[j][i];
			}
		}
		
		return transpose;
	}
	

	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	//Assumes we skip the symmetries
	
	//TODO: maybe it could be a loop instead?
	//TODO: return int[]
	//TODO: rotationRelativeNetToReplicate and mirrored could be done before the function starts.
	public static int[] doDepthFirstSearch(boolean netToReplicate[][], Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int curAnswer[], int altAnswer[], boolean isCurrentlyAloneInFirst) {

		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			return curAnswer;
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

					numCellsUsedDepth += 1;
					//End setup

					return doDepthFirstSearch(netToReplicate, paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth,
							regions, curAnswer, altAnswer, isCurrentlyAloneInFirst);

					
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
			System.out.println("Orig Basic unique check says this is a new solution but MemorylessUniqueCheck says it isn't");
			System.out.println("HERE");
			System.exit(1);
		} else if(! basicCheckResultUniq && memorylessAnswer) {

			System.out.println("Orig Basic unique check same this is a dup but MemorylessUniqueCheck says this is new");
			System.out.println("HERE 2");
			System.exit(1);
		}
		
	}
}
