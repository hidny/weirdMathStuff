package collatz;

public class TranslateToWeirdNums {

	public static void main(String args[]) {
		
		int MAX = 256;
		
		//4 first functions: all y = f(x) is one-to-one
		//4 next functions: all y = f(x) is one-to-one and all x = f'(y) (y can always be reversed)
		//I didn't find these numbers anywhere, and I'm curious about them.
		//I think I'll experiment for fun.
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum1(i, false));
		}
		
		System.out.println();
		
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum2(i, false));
		}

		
		System.out.println();
		
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum3(i, false));
		}
		
		System.out.println();
		
		
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum4(i, false));
		}
		
		System.out.println();

		System.out.println();
		System.out.println();
		
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum1(i, true));
		}
		
		System.out.println();
		
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum2(i, true));
		}

		
		System.out.println();
		
		//TODO: this one is the most interesting because it's the least random with y = f(x) = x happening the most often.
		//Look into this!
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum3(i, true));
		}
		
		System.out.println();
		
		
		for(int i=1; i<MAX; i++) {
			System.out.println(i+ "|" + getCollatzNum4(i, true));
		}
		
		System.out.println();
		
		//TODO: take away leading 1 for input... leading 1 only show how many itrations to do...
		
	}
	
	
	public static int getCollatzNum1(int num, boolean messWithLeadingOne) {
		
		int limit = (int)(Math.log(num)/Math.log(2));
		
		if(messWithLeadingOne) {
			num -= (int)Math.pow(2.0, limit);
			limit --;
		}
		
		if(num >= 0) {
			
			int ret = 1;

			for(int i=0; i<=limit; i++) {
				if(num % 2 == 0) {
					ret = 2*ret;
					num /= 2;
					
				} else {
					ret = 2*ret + 1;
					num = (3*num + 1)/2;
				}
			}
			
			return ret;
		} else {
			System.out.println("I\'m not ready for this");
			return -1;
		}
		
	}
	

	public static int getCollatzNum2(int num, boolean messWithLeadingOne) {
		
		int limit = (int)(Math.log(num)/Math.log(2));

		if(messWithLeadingOne) {
			num -= (int)Math.pow(2.0, limit);
			limit --;
		}
		
		if(num >= 0) {
			
			int ret = 1;
			for(int i=0; i<=limit; i++) {
				if(num % 2 == 0) {
					ret = 2*ret + 1;
					num /= 2;
					
				} else {
					ret = 2*ret;
					num = (3*num + 1)/2;
				}
			}
			
			return ret;
		} else {
			System.out.println("I\'m not ready for this");
			return -1;
		}
		
	}
	
	
	public static int getCollatzNum3(int num, boolean messWithLeadingOne) {
		
		int limit = (int)(Math.log(num)/Math.log(2));

		if(messWithLeadingOne) {
			num -= (int)Math.pow(2.0, limit);
			limit --;
		}
		
		if(num >= 0) {
			
			int mult = 1;
			
			int ret = 0;

			for(int i=0; i<=limit; i++) {
				if(num % 2 == 0) {
					num /= 2;
					
				} else {
					ret += mult;
					num = (3*num + 1)/2;
				}
				
				mult *= 2;
			}
			
			ret += mult;
			
			return ret;
		} else {
			System.out.println("I\'m not ready for this");
			return -1;
		}
		
	}
	

	public static int getCollatzNum4(int num, boolean messWithLeadingOne) {

		
		int limit = (int)(Math.log(num)/Math.log(2));

		if(messWithLeadingOne) {
			num -= (int)Math.pow(2.0, limit);
			limit --;
		}
		
		if(num >= 0) {
			
			int mult = 1;
			
			int ret = 0;

			for(int i=0; i<=limit; i++) {
				if(num % 2 == 0) {
					ret += mult;
					num /= 2;
					
				} else {
					num = (3*num + 1)/2;
				}
				
				mult *= 2;
			}
			
			ret += mult;
			
			return ret;
		} else {
			System.out.println("I\'m not ready for this");
			return -1;
		}
		
	}
}
