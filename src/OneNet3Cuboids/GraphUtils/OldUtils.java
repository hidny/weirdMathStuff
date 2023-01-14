package OneNet3Cuboids.GraphUtils;

import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.OldReferenceFoldingAlgosNby1by1.FoldResolveOrderedRegionsNby1by1;
import OneNet3Cuboids.Region.Region;

public class OldUtils {


	public static final int ONE_EIGHTY_ROTATION = 2;


	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	public static final int BOTTOM_Nx1x1 = 0;
	public static int NUM_ROTATIONS = 4;
	
	
	//(Replaced by caching) this info later... It's very wasteful to check each time.
	public static boolean couldReachTopNx1x1WithoutGoingThruBottom(Region region, CuboidToFoldOn cuboid, int startIndex, int minCellOrderAllowed,
			Coord2D paperToDevelop[], int indexCuboidonPaper[][]) {

		if(minCellOrderAllowed == 0) {
			return true;
		}
		
		
		boolean couldTopCouldBeOnRightOfBottom = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0])
				== 3;
		
		LinkedList<Integer> queueCells = new LinkedList<Integer>();
		boolean explored[] = new boolean[region.getCellRegionsToHandleInRevOrder().length];
		
		explored[startIndex] = true;
		queueCells.add(startIndex);
		
		int startI=paperToDevelop[region.getCellIndexToOrderOfDev().get(startIndex)].i;
		int bottomI=paperToDevelop[0].i;
		int bottomJ=paperToDevelop[0].j;
		
		//Shortcut: if the first cell is below bottom on paper, and it's not the 3 neighbours of bottom case,
		// we know it's a no-go:
		//Remember: higher i goes down!
		if(startI > bottomI && !couldTopCouldBeOnRightOfBottom) {
			return false;
		}
		
		while(queueCells.isEmpty() == false) {
			
			int curCellIndex = queueCells.getFirst();
			queueCells.remove();
			
			if( ! region.getCellIndexToOrderOfDev().containsKey(curCellIndex)) {
				//Don't worry about cells developed in other regions??
				continue;
			}

			//Weird, but it works:
			//TODO: make a function out of it!
			int curI=paperToDevelop[region.getCellIndexToOrderOfDev().get(curCellIndex)].i;
			int curJ=paperToDevelop[region.getCellIndexToOrderOfDev().get(curCellIndex)].j;
			
			//Sanity check to be deleted: (or hidden in the function)
			if(indexCuboidonPaper[curI][curJ] != curCellIndex) {
				System.out.println("oops geting curI curJ didn't work!");
				System.exit(1);
			}
			//End sanity check to be deleted.
			

			if(cuboid.getNeighbours(BOTTOM_Nx1x1)[0].getIndex() == curCellIndex && cellsAreNeighbours(curI, curJ, bottomI, bottomJ)) {
				return true;
			} else if(cuboid.getNeighbours(BOTTOM_Nx1x1)[1].getIndex() == curCellIndex
					&& couldTopCouldBeOnRightOfBottom
					&& cellsAreNeighbours(curI, curJ, bottomI, bottomJ)) {
				
				
				return true;
			} else {
				
				for(int i=1; i<NUM_ROTATIONS; i++) {
					if(cuboid.getNeighbours(BOTTOM_Nx1x1)[i].getIndex() == curCellIndex
							 && cellsAreNeighbours(curI, curJ, bottomI, bottomJ)) {
						return false;
					}
				}
			}
			
			
			for(int n=0; n<NUM_ROTATIONS; n++) {

				int new_i = curI + nugdeBasedOnRotation[0][n];
				int new_j = curJ + nugdeBasedOnRotation[1][n];
				
				int neighbour = indexCuboidonPaper[new_i][new_j];
				
				//Sanity test:
				if(neighbour == 0) {
					System.out.println("DOH! We shouldn't find the bottom in this search!");
					System.exit(1);
				}
				//End sanity test
				
				if( neighbour >= 0 && ! explored[neighbour]){
					
					explored[neighbour] = true;
					queueCells.add(neighbour);
					
					
					
				}
				
			}
		
		}
		
		if(queueCells.isEmpty() ) {
			System.out.println("DOH! The queue for this BFS shouldn't be empty!");
			System.exit(1);
		}
		
		return false;
	}

	//TODO: maybe make this 2D coords (clean this up)
	private static boolean cellsAreNeighbours(int curi, int curj, int curi2, int curj2) {
		
		if(Math.abs(curi - curi2) + Math.abs(curj - curj2) == 1) {
			return true;
		} else {
			return false;
		}
	}
	
}
