package OneNet3Cuboids.FancyTricks;

import java.util.Scanner;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.OldReferenceFoldingAlgosNby1by1.FoldResolveOrderedRegionsNby1by1;
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
		
		this.threeBombActive = new boolean[cuboidNx1x1ToWorkWith.getDimensions()[0]];
		this.threeBombMinIndexToUse = new int[cuboidNx1x1ToWorkWith.getDimensions()[0]];
		
	}
	
	
	public static final int nugdeToGetRowNeighbourBasedOnRotation[][] = {{0, 1, 0 , 1}, {1, 0, 1, 0}};

	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	public static final int BOTTOM = 0;
	public static final int NUM_ROTATIONS = 4;
	public static final int ONE_EIGHTY_ROTATION = NUM_ROTATIONS / 2;
	
	private int TOP = -1;
	
	public boolean threeBombActive[] = null;
	public int threeBombMinIndexToUse[] = null;
	
	private static Scanner in = new Scanner(System.in);
	
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
		
		boolean debugAddedCellInTheMiddle = false;
		if(numInRow == 2) {
			debugAddedCellInTheMiddle = true;
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
		
		if(numInRow == 3) {
			
			//For debug:
			/* && rotationNeighbourPaperRelativeToMap % 2 == 1*/
			

			//Activate the 3-bomb:
			this.threeBombActive[indexNewCell % this.threeBombActive.length] = true;
			

			curi = new_i;
			curj = new_j;
			
			//TODO: make it faster?
			for(int i=1; i<NUM_ROTATIONS; i++) {

				curi = curi + nugdeBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
				curj = curj + nugdeBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];

				if(! paperUsed[curi][curj]) {
					break;
				}
			}
			
			int minIndex1 = indexCuboidonPaper.length;
			
			for(int r=0; r<NUM_ROTATIONS; r++) {
				int i2 = curi + nugdeBasedOnRotation[0][r];
				int j2 = curj + nugdeBasedOnRotation[1][r];
				
				if(indexCuboidonPaper[i2][j2] >= 0
						&& curRegion.getCellIndexToOrderOfDev().containsKey(indexCuboidonPaper[i2][j2])) {
					//TODO: check if indexCuboidonPaper[i2][j2]) is the neighbour of curi and curj...
					
					int curMin = curRegion.getCellIndexToOrderOfDev().get(indexCuboidonPaper[i2][j2]);
					
					if(curMin < minIndex1) {
						minIndex1 = curMin;
					}
				}
			}
			
			//TODO: get min index 1
			
			//TODO: copy/paste code
			curi = new_i;
			curj = new_j;
			
			//TODO: make it faster?
			for(int i=1; i<NUM_ROTATIONS; i++) {

				curi = curi - nugdeBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
				curj = curj - nugdeBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];

				if(! paperUsed[curi][curj]) {
					break;
				}
			}
			//TODO: get min index 2

			int minIndex2 = indexCuboidonPaper.length;
			
			for(int r=0; r<NUM_ROTATIONS; r++) {
				int i2 = curi + nugdeBasedOnRotation[0][r];
				int j2 = curj + nugdeBasedOnRotation[1][r];
				
				if(indexCuboidonPaper[i2][j2] >= 0
						&& curRegion.getCellIndexToOrderOfDev().containsKey(indexCuboidonPaper[i2][j2])) {
					//TODO: check if indexCuboidonPaper[i2][j2]) is the neighbour of curi and curj...
					int curMin = curRegion.getCellIndexToOrderOfDev().get(indexCuboidonPaper[i2][j2]);
					
					if(curMin < minIndex2) {
						minIndex2 = curMin;
					}
				}
			}
			
			

			//END TODO: copy/paste code
			
			threeBombMinIndexToUse[indexNewCell % this.threeBombActive.length] = Math.max(minIndex1, minIndex2);
			
			

			if(debugAddedCellInTheMiddle) {
				
				//TODO: if debugAddedCellInTheMiddle is true, maybe there's no hope?
				//TODO: it looks like there's no hope! Just take advantage of this!
				//TODO: PROVE IT!

				System.out.println("Added 3 in a row: (Cell index = " + indexNewCell + ")");
				System.out.println("Maybe there's no hope because we added it to the middle one?");
			}
			
			
			//paperUsed[new_i][new_j] = true;
			boolean wasMinus1 = indexCuboidonPaper[new_i][new_j] == -1;
			
			if(wasMinus1) {
				indexCuboidonPaper[new_i][new_j] = indexNewCell;
			}
			//Utils.printFoldWithIndex(indexCuboidonPaper);

			//paperUsed[new_i][new_j] = false;
			if(wasMinus1) {
				indexCuboidonPaper[new_i][new_j] = -1;
			}

			//System.out.println("Min index: to use: " + threeBombMinIndexToUse[indexNewCell % this.threeBombActive.length]);
			
			//in.next();

			//System.out.println("Debug");
			//in.nextLine();
			 

		} else {

			this.threeBombActive[indexNewCell % this.threeBombActive.length] = false;
		}
		
	}

	//TODO: maybe just hold state instead of recalculating?
	// Do the clever solution later...
	
	public void removeCell(boolean paperUsed[][], int indexCuboidonPaper[][], CuboidToFoldOn nx1x1cuboid,  Region curRegion,
			int removed_i, int removed_j, int indexRemovedCell, int rotationNeighbourPaperRelativeToMap) {
		// Maybe do nothing for now, but figure out how to make it faster later.
		
		
		//Check to see if we can reactivate 3-bomb
		
		if(indexRemovedCell == TOP || indexRemovedCell == BOTTOM) {
			return;
		}
		
		int numInRowTrial1 = 0;

		int curi = removed_i;
		int curj = removed_j;
		
		//check first way:
		for(int i=1; i<NUM_ROTATIONS; i++) {

			curi = curi + nugdeToGetRowNeighbourBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
			curj = curj + nugdeToGetRowNeighbourBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];

			if(paperUsed[curi][curj]) {
				numInRowTrial1++;
			} else {
				break;
			}
		}
		
		int numInRowTrial2 = numInRowTrial1;

		if(numInRowTrial2 == 0) {
			curi = removed_i;
			curj = removed_j;
	
			for(int i=1; i<NUM_ROTATIONS; i++) {
	
				curi = curi - nugdeToGetRowNeighbourBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
				curj = curj - nugdeToGetRowNeighbourBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];
	
				if(paperUsed[curi][curj]) {
					numInRowTrial2++;
				} else {
					break;
				}
			}
		}
		
		if(numInRowTrial2 == 3) {

			//For debug:
			/* && rotationNeighbourPaperRelativeToMap % 2 == 1*/
			
			curi = removed_i;
			curj = removed_j;
			
			int orderIndex1 = indexCuboidonPaper.length;
			
			for(int r=0; r<NUM_ROTATIONS; r++) {
				int i2 = curi + nugdeBasedOnRotation[0][r];
				int j2 = curj + nugdeBasedOnRotation[1][r];
				
				if(indexCuboidonPaper[i2][j2] >= 0
						&& curRegion.getCellIndexToOrderOfDev().containsKey(indexCuboidonPaper[i2][j2])) {
					//TODO: check if indexCuboidonPaper[i2][j2]) is the neighbour of curi and curj...
					int curMin = curRegion.getCellIndexToOrderOfDev().get(indexCuboidonPaper[i2][j2]);
					
					if(curMin < orderIndex1) {
						orderIndex1 = curMin;
					}
				}
			}
			
			//TODO: get min index 1
			
			//TODO: copy/paste code
			curi = removed_i;
			curj = removed_j;
			
			if(numInRowTrial1 == 0) {
				curi = curi - 4 * nugdeToGetRowNeighbourBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
				curj = curj - 4 * nugdeToGetRowNeighbourBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];
			} else {
				curi = curi + 4 * nugdeToGetRowNeighbourBasedOnRotation[0][rotationNeighbourPaperRelativeToMap];
				curj = curj + 4 * nugdeToGetRowNeighbourBasedOnRotation[1][rotationNeighbourPaperRelativeToMap];
			}
			
			//TODO: get min index 2

			int orderIndex2 = indexCuboidonPaper.length;
			
			for(int r=0; r<NUM_ROTATIONS; r++) {
				int i2 = curi + nugdeBasedOnRotation[0][r];
				int j2 = curj + nugdeBasedOnRotation[1][r];
				
				if(indexCuboidonPaper[i2][j2] >= 0
						&& curRegion.getCellIndexToOrderOfDev().containsKey(indexCuboidonPaper[i2][j2])) {
					//TODO: check if indexCuboidonPaper[i2][j2]) is the neighbour of curi and curj...
					
					int curMin = curRegion.getCellIndexToOrderOfDev().get(indexCuboidonPaper[i2][j2]);
					
					if(curMin < orderIndex2) {
						orderIndex2 = curMin;
					}
				}
			}
			
			

			//END TODO: copy/paste code
			
			threeBombMinIndexToUse[indexRemovedCell % this.threeBombActive.length] = Math.max(orderIndex1, orderIndex2);
			

			
			//System.out.println("3 in a row after removing: (Cell index = " + indexRemovedCell + ")");
			
			//paperUsed[new_i][new_j] = true;
			boolean wasMinus1 = indexCuboidonPaper[removed_i][removed_j] == -1;
			
			if(wasMinus1) {
				indexCuboidonPaper[removed_i][removed_j] = indexRemovedCell;
			}
			//Utils.printFoldWithIndex(indexCuboidonPaper);

			//paperUsed[new_i][new_j] = false;
			if(wasMinus1) {
				indexCuboidonPaper[removed_i][removed_j] = -1;
			}
			
			//System.out.println("Min index: to use after removal: " + threeBombMinIndexToUse[indexRemovedCell % this.threeBombActive.length]);
			
			
			//in.next();
			//System.out.println("Debug");
			
			
			//Activate the 3-bomb:
			this.threeBombActive[indexRemovedCell % this.threeBombActive.length] = true;
		} else {

			this.threeBombActive[indexRemovedCell % this.threeBombActive.length] = false;
		}
	}

	public static boolean checkThreeBombAfterRegionSplit() {

		//TODO: check if bottom cuboid has only 1 neighbour.
		return false;
	}
	
	
	//TODO: this returns true when it should not. Please debug!
	public boolean quickCheckThreeBombDetonatedForCurrentIteration(CuboidToFoldOn cuboid,
			Coord2D paperToDevelop[],
			int indexCuboidonPaper[][],
			int cellIndexToUse,
			Region curRegion
			) {
		

		if(FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0]) == 1) {
			
			for(int i=0; i<threeBombActive.length; i++) {
				if(threeBombActive[i] && threeBombMinIndexToUse[i] < curRegion.getCellIndexToOrderOfDev().get(cellIndexToUse)) {
					
					//Utils.printFoldWithIndex(indexCuboidonPaper);
					//System.out.println("BOMB TEST!");
					//System.out.println("BOMB TEST!");
					
					return true;
					
				}
			}
			
		}
		//TODO: Check if a 4-in-a-row line can still be added on paper... 
		
		//TODO: check if bottom cuboid has only 1 neighbour, for the easy case...
		
		return false;
	}
}
