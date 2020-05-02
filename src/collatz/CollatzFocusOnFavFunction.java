package collatz;

public class CollatzFocusOnFavFunction {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		long MAX = 2048;
		
		int numMatch = 0;
		int numTotal = 0;
		
		int numOnesTheSame = 0;
		
		for(int i=1; i<MAX; i++) {
			//if(i>=MAX/2) {
				if(i == TranslateToWeirdNums.getCollatzNum3(i, true)) {
					numMatch++;
				}
				numTotal++;
				
				if(getNumOnes(i) == getNumOnes(TranslateToWeirdNums.getCollatzNum3(i, true))) {
					numOnesTheSame++;
				}
				
				if((int)(Math.log(i)/Math.log(2)) > (int)(Math.log(i-1)/Math.log(2))) {

					System.out.println(i);
					System.out.println(numMatch + " out of " + numTotal);
					System.out.println("Num of ones the same: " + numOnesTheSame + " out of " + numTotal);
					numMatch = 0;
					numTotal = 0;
					numOnesTheSame = 0;
				}
			//}
			//System.out.println(i+ "|" + TranslateToWeirdNums.getCollatzNum3(i, true) );
			System.out.println(getBinary(i) + "|" + getBinary(TranslateToWeirdNums.getCollatzNum3(i, true)));
			
			
		}
		
		System.out.println();
		
		System.out.println(numMatch + " out of " + numTotal);
		
		System.out.println("Num of ones the same: " + numOnesTheSame + " out of " + numTotal);
		
		
		
		int shuffle[] = randomShuffle(1024, 2048);
		
		int count = 0;
		numOnesTheSame = 0;
		for(int i=0; i<shuffle.length; i++) {
			System.out.println(i+ "|" + shuffle[i]);
			
			if(shuffle[i] - 1024 == i ){
				count++;
			}

			if(getNumOnes(i) == getNumOnes(shuffle[i])) {
				numOnesTheSame++;
			}
		}
		System.out.println("Random shuffle of 1024:");
		System.out.println("Number of matches: " + count);
		System.out.println("Number of ones that are the same:" + numOnesTheSame);
	}

	public static int getNumOnes(int num) {
		String check = getBinary(num);
		
		int ret = 0;
		for(int i=0; i<check.length(); i++) {
			if(check.charAt(i) == '1') {
				ret++;
			}
		}
		return ret;
	}
	public static String getBinary(int num) {
		String output= "";
		while(num>=1) {
			if(num%2==0) {
				output = "0" + output;
			} else {
				output = "1" + output;
			}
			
			num /=2;
		}
		return output;
	}
	
	public static int[] randomShuffle(int min, int max) {
		int output[] = new int[max - min];
		
		for(int i=0; i<output.length; i++) {
			output[i] = min + i;
		}
		
		for(int i=0; i<output.length; i++) {
			int rand = (int)((output.length - i) * Math.random());
			
			int tmp = output[i];
			output[i] = output[i + rand];
			output[i + rand] = tmp;
		}
		
		return output;
		
	}
}
