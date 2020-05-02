package ramarujan.inverse2017;

public class check25 {

	public static void main(String args[]) {
		
		for(int i=0; i<25; i++) {
			System.out.println(i + "^5 = "  + ((i*i*i*i*i)%25));
		}
		
		for(int i=0; i<25; i++) {
			for(int j=0; j<25; j++) {
				
				for(int k=0; k<25; k++) {
					int numM = 0;
					for(int m=0; m<25; m++) {
						if( (powerOf5(i) + powerOf5(j) - powerOf5(k) - powerOf5(m)) %25 == 0) {
							numM++;
							int tempList[] = new int[] {i, j, k, m};
							getNumMod5Used(tempList);
							System.out.println(i + " " + j + " " + k + " " + m + " gives solutions");
							if(getNumMod5Used(tempList) > 2 ) {
								System.out.println("AHHH");
								System.exit(1);
							}
						}
					}
					if(numM > 0) {
						System.out.println(i + " " + j + " " + k + " gives solutions");
					}
				}
			}
		}
		
		
	}

	public static int powerOf5(int i) {
		return i*i*i*i*i;
	}
	
	public static int getNumMod5Used(int list[]) {
		boolean check[] = new boolean[5];
		for(int i=0; i<check.length; i++) {
			check[i] = false;
		}
		
		for(int i=0; i<list.length; i++) {
			check[list[i]%5] = true;
		}
		
		int ret = 0;
		for(int i=0; i<check.length; i++) {
			if(check[i]) {
				ret++;
			}
		}
		
		return ret;
	}
}

/*Quote
Cabtaxi(5,2) - x15 ± x25 ± x35 ± x45 = 0

No solutions to Cabtaxi(5,2) have been found.

The algorithm can be used to search for solutions to x15 ± x25 ± x35 ± x45 = 0. Inspection of this equation modulo 25 shows that the terms sum in pairs to 0 (mod 5). We can re-arrange any solution of this equation into Cabtaxi(5,2) form, with the two terms of a tuple summing to 0 (mod 5). Since a, b = x±y, this means we can force a in the search algorithm to be divisible by 5, significantly reducing the queue size and speeding up the search. Furthermore, the tuples are generated in order, and instead of looking for equality of tuples, we can look for their difference being a 5th power. This gives solutions of x15 ± x25 ± x35 ± x45 ± x55 = 0, albeit where x5 is relatively small (and divisible by 5 because of the imposed modulo constraint).

The search has been taken to a radius of 5,270,658, with this constraint on a. As a search for Cabtaxi(5,2) it is messed up a bit, but still exhaustive (after rearrangement of terms). As a search for four 5th powers summing to zero it is exhaustive, and as a search for five 5th powers summing to zero is quite effective, although not exhaustive. It finds 141325 + 2205 = 140685 + 62375 + 50275 (Bob Scher & Ed Seidl, 1997) and 853595 = 852825 + 289695 + 31835 + 555 (Jim Frye, 2004) in about a minute and an hour respectively on a 1.8GHz PC, which is much faster than the original exhaustive searches. It does not find the only other known solution of five 5th powers summing to zero, 1445 = 1335 + 1105 + 845 + 275 ( L.J. Lander & T.R. Parkin, 1966), since the term divisible by 5 is relatively big in this case. I find it (a little) curious that in all 3 known solutions, it is the same term that is divisible by 5 and 11, and it is also small (in absolute terms).

*/

// 0 0   1  4
//LHS 0 mod 5^5
//RHS: why not?