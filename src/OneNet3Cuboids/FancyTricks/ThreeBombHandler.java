package OneNet3Cuboids.FancyTricks;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Region.Region;

public class ThreeBombHandler {

	//I'll start by making this stateless...
	//But then I'll add state info if it makes it faster.
	
	public ThreeBombHandler(CuboidToFoldOn cuboidNx1x1ToWorkWith) {
		
		if(cuboidNx1x1ToWorkWith.getDimensions()[1] != 1 || cuboidNx1x1ToWorkWith.getDimensions()[2] != 1) {
			System.out.println("Invalid dimensions for ThreeBombHandler");
			System.exit(1);
		}
		
		this.TOP = cuboidNx1x1ToWorkWith.getDimensions()[0] * 4 + 1;
		
	}
	
	
	public static final int nugdeToGetRowNeighbourBasedOnRotation[][] = {{0, 1, 0 , 1}, {1, 0, 1, 0}};
	public static final int BOTTOM = 0;
	public static final int NUM_ROTATIONS = 4;
	public static final int ONE_EIGHTY_ROTATION = NUM_ROTATIONS / 2;
	
	private int TOP = -1;
	
	//TODO: optional:
	// maybe cache the num cells used per row?
	
	//1st step:
	// Detect when there's 3 in a row...
	//TODO: will we even need indexCuboidOnPaper?
	public void addCell(boolean paperUsed[][], int indexCuboidonPaper[][], CuboidToFoldOn nx1x1cuboid,  Region curRegion,
			int new_i, int new_j, int indexNewCell, int rotationNeighbourPaperRelativeToMap) {
		
		
		if(indexNewCell == TOP || indexNewCell == BOTTOM) {
			return;
		}
		
		//Added cell counts as 1 by default:
		int numInRow = 1;

		int curi = new_i;
		int curj = new_j;
		
		//check first way:
		for(int i=1; i<NUM_ROTATIONS; i++) {

			curi = curi + nugdeToGetRowNeighbourBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
			curj = curj + nugdeToGetRowNeighbourBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];

			if(paperUsed[curi][curj]) {
				numInRow++;
			} else {
				break;
			}
		}

		curi = new_i;
		curj = new_j;

		for(int i=1; i<NUM_ROTATIONS; i++) {

			curi = curi - nugdeToGetRowNeighbourBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
			curj = curj - nugdeToGetRowNeighbourBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];

			if(paperUsed[curi][curj]) {
				numInRow++;
			} else {
				break;
			}
		}
		
		if(numInRow == 3/* && rotationNeighbourPaperRelativeToMap % 2 == 1*/) {
			System.out.println("Added 3 in a row: (Cell index = " + indexNewCell + ")");
			
			//paperUsed[new_i][new_j] = true;
			boolean wasMinus1 = indexCuboidonPaper[new_i][new_j] == -1;
			
			if(wasMinus1) {
				indexCuboidonPaper[new_i][new_j] = indexNewCell;
			}
			Utils.printFoldWithIndex(indexCuboidonPaper);

			//paperUsed[new_i][new_j] = false;
			if(wasMinus1) {
				indexCuboidonPaper[new_i][new_j] = -1;
			}
			
			//TODO: activate the 3-bomb!
			
		}
	}

	public static void removeCell() {
		// Maybe do nothing for now, but figure out how to make it faster later.
	}

	public static boolean checkThreeBombAfterRegionSplit() {

		//TODO: check if bottom cuboid has only 1 neighbour.
		return false;
	}
	
	public static boolean quickCheckThreeBombForCurrentIteration() {
		

		//TODO: Check if a 4-in-a-row line can still be added on paper... 
		
		//TODO: check if bottom cuboid has only 1 neighbour, for the easy case...
		
		return false;
	}
}
