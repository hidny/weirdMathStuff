package triangleBoard;

public class triangleRecord {

	
	private int numMovesToGetToPos;
	private int numMovesToGetToEnd;
	
	//TODO: having this true is better than having it false.
	private boolean movedDifferentPieceStartingFromPos;
	private int curSelectedPieceCode;

	public triangleRecord(int numMovesToGetToPos, int curSelectedPieceCode) {

		this.numMovesToGetToPos = numMovesToGetToPos;
		this.curSelectedPieceCode = curSelectedPieceCode;

		this.movedDifferentPieceStartingFromPos = false;
		this.numMovesToGetToEnd = Integer.MAX_VALUE;
	}
	
	public void updateNumMovesToGetToPos(int numMovesToGetToPos, int curSelectedPieceCode) {
		if(numMovesToGetToPos >= this.numMovesToGetToPos) {
			System.err.println("ERROR: updating numMovesToGetThere when there wasn't an improvement");
			System.exit(1);
		}
		
		this.numMovesToGetToPos = numMovesToGetToPos;
		
		//Assume true until optimal way is false...
		this.movedDifferentPieceStartingFromPos = true;
		this.curSelectedPieceCode = curSelectedPieceCode;
	}
	
	public void updateNumMovesToGetToEnd(int numMovesToGetToEnd, boolean movedDifferentPieceStartingFromPos) {
		if(numMovesToGetToEnd >= this.numMovesToGetToEnd) {
			if(movedDifferentPieceStartingFromPos == true && this.movedDifferentPieceStartingFromPos == false) {
				//Still an improvement
			} else {
				System.err.println("ERROR: updating NumMovesToGetToEnd when there wasn't an improvement");
				System.exit(1);
			}
		}
		
		this.numMovesToGetToEnd = numMovesToGetToEnd;
		this.movedDifferentPieceStartingFromPos = movedDifferentPieceStartingFromPos;
	}
	
	public int getNumMovesToGetToPos() {
		return numMovesToGetToPos;
	}

	public int getNumMovesToGetToEnd() {
		return numMovesToGetToEnd;
	}

	public boolean isOptionalMovedDifferentPieceStartingFromPos() {
		return movedDifferentPieceStartingFromPos;
	}

	public int getCurSelectedPieceCode() {
		return curSelectedPieceCode;
	}
	
	
	
	
}
