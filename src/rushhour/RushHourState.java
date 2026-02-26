package rushhour;

import java.util.ArrayList;

public class RushHourState {

	public int map[][];
	
	//TODO:
	private String lastMoveDetails = "";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		RushHourState test = new RushHourState(6);
		
		test.insertCar(0, 0, 3, true);
		test.insertCar(2, 2, true, true);
		test.insertCar(4, 3, true);

		test.insertCar(0, 3, false);
		test.insertCar(0, 4, 3, false);
		test.insertCar(1, 1, false);
		test.insertCar(3, 2, false);
		test.insertCar(4, 5, false);
		
		//TODO: remove label
		//TODO: default to length 2.
		//TODO: warn when crash
		System.out.println(test);
	}
	
	//Goal 1 to the right
	
	public RushHourState(int size) {
		this.map = new int[size][size];
		
	}
	public RushHourState(int map[][]) {
		this.map = map;
		
	}
	private int curLabelIndex = 2;
	public void insertCar(int i, int j, boolean isHori) {
		insertCar(i, j, 2, isHori);
	}
	public void insertCar(int i, int j, boolean isHori, boolean isMain) {
		insertCar(i, j, 2, isHori, isMain);
	}
	
	public void insertCar(int i, int j, int length, boolean isHori) {
		insertCar(i, j, length, isHori, false);
	}
	
	
	public void insertCar(int i, int j, int length, boolean isHori, boolean isMain) {

		int labelToUse = -1;
		if(isMain) {
			labelToUse = 1;
		} else {
			labelToUse = curLabelIndex;
		}
		
		if(isHori) {
			for(int j1=j; j1<j+length; j1++) {
				insertLabel(labelToUse, i, j1);
			}
		} else {

			for(int i1=i; i1<i+length; i1++) {
				insertLabel(labelToUse, i1, j);
			}
		}
		
		curLabelIndex++;
	}
	
	private void insertLabel(int label, int i, int j) {

		if(this.map[i][j] != 0) {
			System.out.println("ERROR: overlap at i = " + i + " and j = " + j);
			System.exit(1);
		}
		this.map[i][j] = label;
	}
	
	public String toString() {
		String ret = "";
		for(int i=0; i<this.map.length; i++) {
			for(int j=0; j<this.map[0].length; j++) {
				ret += this.map[i][j];
			}
			ret += "\n";
		}
		return ret;
	}

	public ArrayList<RushHourState> getOptions() {
		ArrayList<RushHourState> ret = new ArrayList<RushHourState>();
		
		for(int i=0; i<this.map.length; i++) {
			for(int j=0; j<this.map[0].length; j++) {
				
				if(this.map[i][j] == 0) {
					//TODO: Look in the 4 cardinal dirs for a car that will fill the space.
					
					//UP
					// >0 because we're assuming that cars are 2 long...
					for(int i1=i-1;i1>0; i1--) {
						if(this.map[i1][j] != 0 && this.map[i1-1][j] == this.map[i1][j]) {
							//MOVE
							boolean move3 = false;
							if(i1-2 >=0 && this.map[i1-2][j] == this.map[i1-1][j]) {
								//TODO: move 3
								move3 = true;
							} else {
								//TODO move 2
							}
							
							int newmap[][] = hardCopyMap(this.map);
							
							//TODO:
							
						} else if(this.map[i1][j] != 0 && this.map[i1-1][j] != this.map[i1][j]) {
							break;
						}
					}
					//copy/paste 4 times...
				}
			}
		}
		
		
		return ret;
	}
	
	public static int[][] hardCopyMap(int map[][]) {
		int ret[][] = new int[map.length][map[0].length];
		for(int i=0; i<map.length; i++) {
			for(int j=0; j<map[0].length; j++) {
				ret[i][j]=map[i][j];
			}
		}
		return ret;
	}

	public String getLastMoveDetails() {
		return lastMoveDetails;
	}

	
}
