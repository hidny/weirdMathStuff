package OneNet3Cuboids;

public class CuboidToFoldOn {

	public static final int SIDES_CUBOID = 6;

	public static final int NUM_NEIGHBOURS = 4;
	
	private boolean sidesUsed[][][] = new boolean[SIDES_CUBOID][][];
	
	//I don't like that the ground truth is a 3D array and not just a single array,
	// but whatever! numbering and numberingInv allows me to jump from coord to state and back.
	private int numbering[][][] = new int[SIDES_CUBOID][][];
	private Coord numberingInv[];
	
	//TODO: turn coord into number
	//TODO: get neighbour function
	

	private CoordWithRotation[][] neighbours;

	
	public CuboidToFoldOn(int a, int b, int c) {
		
		//b always "j" and a always "i"
		sidesUsed[0] = new boolean[c][b];
		sidesUsed[1] = new boolean[a][c];
		sidesUsed[2] = new boolean[a][b];
		sidesUsed[3] = new boolean[a][c];
		sidesUsed[4] = new boolean[a][b];
		sidesUsed[5] = new boolean[c][b];

		numbering[0] = new int[c][b];
		numbering[1] = new int[a][c];
		numbering[2] = new int[a][b];
		numbering[3] = new int[a][c];
		numbering[4] = new int[a][b];
		numbering[5] = new int[c][b];

		
		int currentNum = 0;
		numberingInv = new Coord[getTotalArea(a, b, c)];
		
		
		for(int i=0; i<numbering.length; i++) {
			for(int j=0; j<numbering[i].length; j++) {
				for(int k=0; k<numbering[i][j].length; k++) {
					numbering[i][j][k] = currentNum;
					numberingInv[currentNum] = new Coord(i, j, k);
					currentNum++;
				}
			}
		}
		if(currentNum != getTotalArea(a, b, c)) {
			System.out.println("Current num is not the total area. Something went wrong!");
			System.exit(1);
		}

		neighbours = NeighbourGraphCreator.initNeighbourhood(a, b, c, numbering, numberingInv);
	}
	
	public static int getTotalArea(int a, int b, int c) {
		return 2*(a*b + a*c + b*c);
	}
	
	public static  int indexLeft(int side) {
		return -1;
	}
	
	public static int indexRight(int side) {
		return -1;
	}
	
	public static int indexAbove(int side) {
		return -1;
	}
	

	public static int indexBelow(int side) {
		return -1;
	}
	


	public int getNumber(Coord coord) {
		return numbering[coord.a][coord.b][coord.c];
	}
	
	public CoordWithRotation[] getNeighbours(Coord start) {
		return neighbours[numbering[start.a][start.b][start.c]];
	}
	

	public static void main(String args[]) {
		CuboidToFoldOn c = new CuboidToFoldOn(3, 4, 5);
	}
	
}
