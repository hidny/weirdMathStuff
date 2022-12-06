package OneNet3Cuboids;

import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import number.IsNumber;

public class CuboidToFoldOn {

	public static final int SIDES_CUBOID = 6;

	public static final int NUM_NEIGHBOURS = 4;
	

	//TODO: use this later:
	private boolean cellsUsed[];
	//TODO: make sure this is an appropriate name:
	private int rotationRelativePaperUsed[];
	

	
	//TODO: allow function to insert paper at 1st cell with any rotation
	
	//TODO: for next cell to insert, make function to add cell relative to neighbour and record that
	// it's taken with the appropriate rotation
	
	//TODO: allow a cell to be removed no questions asked.
	
	//TODO: this should be able to handle a depth first search and 
	//for efficiency purposes, I'll make it mutable (it changes state when actions are done on it)
	
	//TODO: This should be simple!
	// Just use neighbours to help navigate and cellsUsed + rotationRelativePaperUsed to keep track of state.
	
	//TODO: if move invalid:
	// Current idea: return error number and stop changing state util :allow state change" function is run:
	
	//Maybe in another class:
	//TODO: have a function that checks for forced moves somewhere
	//TODO: also have a function that checks for less obvious impossibilities? (I haven't thought that hard yet)
	
	private CoordWithRotationAndIndex[][] neighbours;

	
	public CuboidToFoldOn(int a, int b, int c) {

		neighbours = NeighbourGraphCreator.initNeighbourhood(a, b, c);
		
		cellsUsed = new boolean[Utils.getTotalArea(a, b, c)];
		rotationRelativePaperUsed = new int[Utils.getTotalArea(a, b, c)];
		
		for(int i=0; i<cellsUsed.length; i++) {
			cellsUsed[i] = false;
			rotationRelativePaperUsed[i] = -1;
		}
		
	}

	public void setCell(int index, int rotation) {
		if(cellsUsed[index]) {
			System.out.println("Error: Setting cell when a cell is already activated!");
			System.exit(1);
		}
		

		cellsUsed[index] = true;
		rotationRelativePaperUsed[index] = rotation;
	}
	
	public void removeCell(int index) {
		if(!cellsUsed[index]) {
			System.out.println("Error: removing cell when a cell is not activated!");
			System.exit(1);
		}
		
		cellsUsed[index] = false;
		rotationRelativePaperUsed[index] = -1;
	}
	
	public int getNumCellsToFill() {
		return cellsUsed.length;
	}
	
	public CoordWithRotationAndIndex[] getNeighbours(int cellIndex) {
		return neighbours[cellIndex];
	}
	
	public int getRotationRelativeToPaper(int cellIndex) {
		return rotationRelativePaperUsed[cellIndex];
	}
	
	public boolean isCellIndexUsed(int cellIndex) {
		return cellsUsed[cellIndex];
	}

	public static int indexPaperAbove(int cellIndex) {
		return -1;
	}

	public static int indexPaperRight(int cellIndex) {
		return -1;
	}

	public static int indexPaperBelow(int cellIndex) {
		return -1;
	}

	public static int indexPaperLeft(int cellIndex) {
		return -1;
	}

	public static void main(String args[]) {
		CuboidToFoldOn c = new CuboidToFoldOn(3, 4, 5);
		//CuboidToFoldOn c = new CuboidToFoldOn(1, 1, 1);
	}

}
