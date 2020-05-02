package threeDiceProb;

public class ThreeIndistinguishableDiceProb2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int NUM_TRIALS = 100000;
		
		random.Die dice = new random.RandomDie();
		
		int tableOfResults[][] = new int[6][6];

		for(int i=0; i<6; i++) {
			for(int j=0; j<6; j++) {
				tableOfResults[i][j] = 0;
			}
		}
		
		
		for(int i=0; i<NUM_TRIALS; i++) {
			int roll1 = Integer.parseInt(dice.getRoll());
			int roll2 = Integer.parseInt(dice.getRoll());
					
			
			if(roll1 > roll2) {
				tableOfResults[roll1 - 1][roll2 - 1]++;
			} else {
				tableOfResults[roll2 - 1][roll1 - 1]++;
			}
			
		}
		
		for(int i=0; i<6; i++) {
			for(int j=0; j<6; j++) {
				System.out.print(tableOfResults[i][j] + "      ");
			}
			System.out.println();
		}
		
	}
	
	public static int sumRoll(int roll1, int roll2, int roll3) {
		return (roll1 + roll2 + roll3) % 6 + 1;
	}
	
	public static int multRoll(int roll1, int roll2, int roll3) {
		return (roll1 * roll2 * roll3) % 7;
	}
	

}
