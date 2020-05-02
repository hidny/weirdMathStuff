package threeDiceProb;

public class ThreeIndistinguishableDiceProbNoRand {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		int tableOfResults[][] = new int[6][6];

		for(int i=0; i<6; i++) {
			for(int j=0; j<6; j++) {
				tableOfResults[i][j] = 0;
			}
		}
		
		int roll1;
		int roll2;
		
		for(int i=1; i<=6; i++) {
			for(int j=1; j<=6; j++) {
				for(int k=1; k<=6; k++) {
					roll1 = multRoll(i, j, k);
					roll2 = multRoll(i, j, k);
					
					int order[] = orderedRoll(i, j, k);
					
					
					if(roll1 > roll2) {
						tableOfResults[roll1 - 1][roll2 - 1]++;
					} else {
						tableOfResults[roll2 - 1][roll1 - 1]++;
						
					}
					
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
	

	public static int[] orderedRoll(int roll1, int roll2, int roll3) {
		int temp;
		System.out.print(roll1 + " " + roll2 + " " + roll3 + ": ");
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
		
		System.out.println(roll1 + " " + roll2 + " " + roll3);
		
		return new int[]{roll1, roll2, roll3};
	}
	
	public static int sumRoll(int roll1, int roll2, int roll3) {
		return (roll1 + roll2 + roll3) % 6 + 1;
	}
	
	public static int multRoll(int roll1, int roll2, int roll3) {
		return (roll1 * roll2 * roll3) % 7;
	}
	

}
