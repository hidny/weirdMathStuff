package triangleBoard5;

public class triangleRecord {


	public static int FOUND_IN_PREV_SEARCH_SAME_DEPTH = -1;
	
	private int numMovesToGetToPos;
	private int depthUsedToFindRecord;
	


	public triangleRecord(int numMovesToGetToPos, TriangleBoard board, int curMaxDepth) {

		this.numMovesToGetToPos = numMovesToGetToPos;
		
		this.depthUsedToFindRecord = utilFunctions.getMaxDepthUsed(board, curMaxDepth);

	}

	public triangleRecord(int numMovesToGetToPos, BackwardsTriangleBoard board, int curMaxDepth) {

		this.numMovesToGetToPos = numMovesToGetToPos;
		
		this.depthUsedToFindRecord = utilFunctions.getMaxDepthUsed(board, curMaxDepth);

	}
	
	public void updateNumMovesToGetToPos(int numMovesToGetToPos, TriangleBoard board, int curMaxDepth) {
		if(numMovesToGetToPos >= this.numMovesToGetToPos && utilFunctions.getMaxDepthUsed(board, curMaxDepth) == depthUsedToFindRecord) {
			System.err.println("ERROR: updating numMovesToGetThere when there wasn't an improvement");
			System.exit(1);
		}
		
		this.numMovesToGetToPos = numMovesToGetToPos;
		this.depthUsedToFindRecord = utilFunctions.getMaxDepthUsed(board, curMaxDepth);
	}
	
	public void updateNumMovesToGetToPos(int numMovesToGetToPos, BackwardsTriangleBoard board, int curMaxDepth) {
		if(numMovesToGetToPos >= this.numMovesToGetToPos && utilFunctions.getMaxDepthUsed(board, curMaxDepth) == depthUsedToFindRecord) {
			System.err.println("ERROR: updating numMovesToGetThere when there wasn't an improvement");
			System.exit(1);
		}
		
		this.numMovesToGetToPos = numMovesToGetToPos;
		this.depthUsedToFindRecord = utilFunctions.getMaxDepthUsed(board, curMaxDepth);
	}
	
	public void scratchOutDepthUsedToFindRecord() {
		this.depthUsedToFindRecord = FOUND_IN_PREV_SEARCH_SAME_DEPTH;
	}
	
	public int getNumMovesToGetToPos() {
		return numMovesToGetToPos;
	}
	
	

	public int getDepthUsedToFindRecord() {
		return depthUsedToFindRecord;
	}
}
