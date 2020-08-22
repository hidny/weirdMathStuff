package hatproblem;

public class BoolUtilFuntions {
	
	//warning: boolean gets reversed...
	public static int convertToNumber(boolean array[]) {
		int ret = 0;
	
		for(int i=0; i<array.length; i++) {
			if(array[i]) {
				ret = 2*ret + 1;
			} else {
				ret = 2*ret;
			}
		}
		
		return ret;
	}
	
	//returns boolean array in little endian format:
		public static boolean[] convertNumToBoolArray(int num, int size) {
			boolean ret[] = new boolean[size];
			
			for(int i= 0; i<ret.length; i++) {
				if(num % 2 == 1) {
					ret[i] = true;
				} else {
					ret[i] = false;
				}
				num /= 2;
			}
			
			return ret;
		}
		
}
