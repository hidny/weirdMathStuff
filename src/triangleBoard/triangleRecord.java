package triangleBoard;

public class triangleRecord {

	
	private int numMovesToGetToPos;
	
	//TODO: make into a set of all starting points tried.
	//LATER
	private int curSelectedPieceCode;
	
	boolean isFindingSolUncertain = true;
	boolean isFindingSolImpossible = false;

	public triangleRecord(int numMovesToGetToPos, int curSelectedPieceCode) {

		this.numMovesToGetToPos = numMovesToGetToPos;
		this.curSelectedPieceCode = curSelectedPieceCode;
		this.isFindingSolImpossible = false;

	}
	
	public void updateNumMovesToGetToPos(int numMovesToGetToPos, int curSelectedPieceCode) {
		if(numMovesToGetToPos >= this.numMovesToGetToPos) {
			System.err.println("ERROR: updating numMovesToGetThere when there wasn't an improvement");
			System.exit(1);
		}
		
		this.numMovesToGetToPos = numMovesToGetToPos;
	}
	
	
	public boolean isFindingSolImpossible() {
		return isFindingSolImpossible;
	}

	public void setPossible() {
		this.isFindingSolUncertain = false;
		this.isFindingSolImpossible = false;
	}

	public void setImpossibleIfUncertain() {
		if(this.isFindingSolUncertain) {
			this.isFindingSolImpossible = true;
		} else {
			System.err.println("ERROR: this condition should not be possible to reach!");
			System.exit(1);
		}
	}

	public int getNumMovesToGetToPos() {
		return numMovesToGetToPos;
	}

	public int getCurSelectedPieceCode() {
		return curSelectedPieceCode;
	}
	
	
	
	
}
