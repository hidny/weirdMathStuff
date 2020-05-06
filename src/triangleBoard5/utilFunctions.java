package triangleBoard5;

public class utilFunctions {
	public static int getTriangleNumber(int n) {
		return n * (n+1) / 2;
	}
	
	public static int getMaxDepthUsed(TriangleBoard board, int curMaxDepth) {
		return board.getNumMovesMade() + curMaxDepth;
	}
	
	public static int getMaxDepthUsed(BackwardsTriangleBoard board, int curMaxDepth) {
		return board.getNumMovesMade() + curMaxDepth;
	}
	
}
