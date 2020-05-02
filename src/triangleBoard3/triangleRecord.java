package triangleBoard3;

public class triangleRecord {

	
	private int numMovesToGetToPos;
	

	public triangleRecord(int numMovesToGetToPos) {

		this.numMovesToGetToPos = numMovesToGetToPos;

	}
	
	public void updateNumMovesToGetToPos(int numMovesToGetToPos) {
		if(numMovesToGetToPos >= this.numMovesToGetToPos) {
			System.err.println("ERROR: updating numMovesToGetThere when there wasn't an improvement");
			System.exit(1);
		}
		
		this.numMovesToGetToPos = numMovesToGetToPos;
	}
	
	
	public int getNumMovesToGetToPos() {
		return numMovesToGetToPos;
	}

}
