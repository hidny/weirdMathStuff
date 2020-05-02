package threeDiceProb;

public class threeIndistinguishableDiceProbNoSolutionTest {

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
		
		int count[][][] = new int[6][6][6];
		
		for(int i=1; i<=6; i++) {
			for(int j=1; j<=6; j++) {
				for(int k=1; k<=6; k++) {
					roll1 = multRoll(i, j, k);
					roll2 = multRoll(i, j, k);
					
					int order[] = orderedRoll(i, j, k);
					count[order[0] - 1][order[1] - 1][order[2] - 1]++;
					
					
				}
			}
		}
		
		int numOnes = 0;
		int numThrees = 0;
		int numSixes = 0;
		
		for(int i=0; i<6; i++) {
			for(int j=0; j<6; j++) {
				for(int k=0; k<6; k++) {
					System.out.print(count[i][j][k] + "      ");
					if(count[i][j][k]== 1) {
						numOnes++;
					} else if(count[i][j][k] == 3) {
						numThrees++;
					} else if(count[i][j][k] == 6) {
						numSixes++;
					}
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
		}
		

		System.out.println();
		System.out.println("There's " + numOnes + " ones.");
		System.out.println("There's " + numThrees + " threes.");
		System.out.println("There's " + numSixes + " sixes.");
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


