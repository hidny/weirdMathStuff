package threeDiceProb;

public class threeIndistinguishableDiceProbNoRandDesire {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		int tableOfResults[][] = new int[6][6];

		for(int i=0; i<6; i++) {
			for(int j=0; j<6; j++) {
				tableOfResults[i][j] = 0;
			}
		}
		
		for(int i=1; i<=6; i++) {
			for(int j=1; j<=6; j++) {	
				if(i > j) {
					tableOfResults[i - 1][j - 1] += 6;
				} else {
					tableOfResults[j - 1][i - 1] += 6;
					
				}
			}
		}
		
		for(int i=0; i<6; i++) {
			for(int j=0; j<6; j++) {
				System.out.print(tableOfResults[i][j] + "      ");
			}
			System.out.println();
		}
		
	}
	
	public static int orderedRoll(int roll1, int roll2, int roll3) {
		int temp;
		if(roll1 > roll2) {
			temp = roll1;
			roll1 = roll2;
			roll2 = temp;
		}
		
		if(roll2 > roll3) {
			temp = roll2;
			roll2 = roll3;
			roll3 = temp;
		}
		
		if(roll1 > roll2) {
			temp = roll1;
			roll1 = roll2;
			roll2 = temp;
		}
		
		if(roll1 >roll2 || roll1 > roll3 || roll2 > roll3) {
			System.out.println("ERROR: I messed up");
		}
		
		return -1;
	}
	
	public static int sumRoll(int roll1, int roll2, int roll3) {
		return (roll1 + roll2 + roll3) % 6 + 1;
	}
	
	public static int multRoll(int roll1, int roll2, int roll3) {
		return (roll1 * roll2 * roll3) % 7;
	}
	

}
