package triangleBoard4;

public class triangleRecord {

	
	private int numMovesToGetToPos;
	private int depthUsedToFindRecord;
	


	public triangleRecord(int numMovesToGetToPos, TriangleBoard board, int curMaxDepth) {

		this.numMovesToGetToPos = numMovesToGetToPos;
		
		this.depthUsedToFindRecord = TriangleSolveOptimizedTrial.getMaxDepthUsed(board, curMaxDepth);

	}
	
	public void updateNumMovesToGetToPos(int numMovesToGetToPos, TriangleBoard board, int curMaxDepth) {
		if(numMovesToGetToPos >= this.numMovesToGetToPos && TriangleSolveOptimizedTrial.getMaxDepthUsed(board, curMaxDepth) == depthUsedToFindRecord) {
			System.err.println("ERROR: updating numMovesToGetThere when there wasn't an improvement");
			System.exit(1);
		}
		
		this.numMovesToGetToPos = numMovesToGetToPos;
		this.depthUsedToFindRecord = TriangleSolveOptimizedTrial.getMaxDepthUsed(board, curMaxDepth);
	}
	
	
	public int getNumMovesToGetToPos() {
		return numMovesToGetToPos;
	}
	
	

	public int getDepthUsedToFindRecord() {
		return depthUsedToFindRecord;
	}
}
