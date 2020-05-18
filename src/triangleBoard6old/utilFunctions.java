package triangleBoard6old;

public class utilFunctions {
	public static int getTriangleNumber(int n) {
		return n * (n+1) / 2;
	}
	
	public static int getMaxDepthUsed(TriangleBoard board, int curMaxDepth) {
		return board.getNumMovesMade() + curMaxDepth;
	}
	
	
}
