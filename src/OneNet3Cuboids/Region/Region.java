package OneNet3Cuboids.Region;

import java.util.HashMap;
import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;

//TODO: maybe rename to CuboidRegion
public class Region {

	//TODO: rename later:
	private HashMap <Integer, Integer> CellIndexToOrderOfDev;
	
	//TODO: don't have rev order in name
	private boolean CellRegionsToHandleInRevOrder[];
	
	//Remove the Per region vars because it's obvious
	private int minOrderedCellCouldUsePerRegion;
	private int minCellRotationOfMinCellToDevPerRegion;
	
	//TODO: this is the cur number of unfilled cells in region:
	//Rename variable
	private int numCellsInRegion;
	
	public HashMap<Integer, Integer> getCellIndexToOrderOfDev() {
		return CellIndexToOrderOfDev;
	}

	public boolean[] getCellRegionsToHandleInRevOrder() {
		return CellRegionsToHandleInRevOrder;
	}

	public int getMinOrderedCellCouldUsePerRegion() {
		return minOrderedCellCouldUsePerRegion;
	}

	public int getMinCellRotationOfMinCellToDevPerRegion() {
		return minCellRotationOfMinCellToDevPerRegion;
	}

	public Region(CuboidToFoldOn cuboid) {
		this(cuboid, 0);
	}
	
	public Region(CuboidToFoldOn cuboid, int startCellIndex) {
	
		minOrderedCellCouldUsePerRegion = 0;
		minCellRotationOfMinCellToDevPerRegion = 0;
		
		CellIndexToOrderOfDev = new HashMap <Integer, Integer>();
		CellIndexToOrderOfDev.put(startCellIndex, 0);
		
		CellRegionsToHandleInRevOrder = new boolean[cuboid.getNumCellsToFill()];
		
		for(int j=0; j<CellRegionsToHandleInRevOrder.length; j++) {
			CellRegionsToHandleInRevOrder[j] = true;
		}
		
		//TODO: Minus 1 because the first cell has already been filled?
		numCellsInRegion = cuboid.getNumCellsToFill() - 1;
	}
	
	
	public Region(CuboidToFoldOn cuboid, Region origRegionThatWasSplit, int cellIndexInNewRegion, int minCellRotation) {
		
		
		numCellsInRegion = 0;

		CellRegionsToHandleInRevOrder = getCellsInUnfilledCuboidRegion(cuboid, cellIndexInNewRegion);
		
		for(int i=0; i<CellRegionsToHandleInRevOrder.length; i++) {
			if(CellRegionsToHandleInRevOrder[i] == true) {
				numCellsInRegion++;
			}
		}
		
		minOrderedCellCouldUsePerRegion = origRegionThatWasSplit.getMinOrderedCellCouldUsePerRegion();
		minCellRotationOfMinCellToDevPerRegion = minCellRotation;
		
		CellIndexToOrderOfDev = (HashMap <Integer, Integer>)origRegionThatWasSplit.getCellIndexToOrderOfDev().clone();
		
		
	}

	//TODO: add logic for this var:
	public int getNumCellsInRegion() {
		return numCellsInRegion;
	}
	
	
	public void addCellToRegion(int indexNewCell, int numCellsUsedDepth, int indexCellNewCellAttachedTo, int rotationCellNewCellIsAttachedTo) {
		CellIndexToOrderOfDev.put(indexNewCell, numCellsUsedDepth);
		
		if(CellRegionsToHandleInRevOrder[indexNewCell]) {
			CellRegionsToHandleInRevOrder[indexNewCell] = false;
			numCellsInRegion--;
		}
		
		minOrderedCellCouldUsePerRegion = CellIndexToOrderOfDev.get(indexCellNewCellAttachedTo);
		minCellRotationOfMinCellToDevPerRegion = rotationCellNewCellIsAttachedTo;
	}
	
	public void removeCellFromRegion(int indexNewCell, int numCellsUsedDepth, int prevNewMinOrderedCellCouldUse, int prevMinCellRotationOfMinCellToDev) {
		CellIndexToOrderOfDev.remove(indexNewCell);
		
		if(CellRegionsToHandleInRevOrder[indexNewCell] == false) {
			CellRegionsToHandleInRevOrder[indexNewCell] = true;
			numCellsInRegion++;
		}
		
		minOrderedCellCouldUsePerRegion = prevNewMinOrderedCellCouldUse;
		minCellRotationOfMinCellToDevPerRegion = prevMinCellRotationOfMinCellToDev;
	}
	
	public static int NUM_NEIGHBOURS = 4;
	

	public static boolean[] getCellsInUnfilledCuboidRegion(CuboidToFoldOn cuboid, int startIndex) {
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		boolean explored[] = new boolean[cuboid.getNumCellsToFill()];
		
		explored[startIndex] = true;
		
		queue.add(startIndex);
		
		while( ! queue.isEmpty() ) {
			int v = queue.removeFirst();
			
			
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				
				int neighbour = cuboid.getNeighbours(v)[j].getIndex();
				
				if(!explored[neighbour] && ! cuboid.isCellIndexUsed(neighbour)) {
					explored[neighbour] = true;
					queue.add(neighbour);
				}
			}
		}
		
		return explored;
	}
	
}
