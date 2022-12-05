package OneNet3Cuboids;

import OneNet3Cuboids.Coord.CoordWithRotation;

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
	
	private CoordWithRotation[][] neighbours;

	
	public CuboidToFoldOn(int a, int b, int c) {

		neighbours = NeighbourGraphCreator.initNeighbourhood(a, b, c);
		
	}
	
	public CoordWithRotation[] getNeighbours(int cellIndex) {
		return neighbours[cellIndex];
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
	}
	
}
