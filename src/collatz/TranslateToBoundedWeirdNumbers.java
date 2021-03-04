package collatz;

public class TranslateToBoundedWeirdNumbers {
	
	
	public static void main(String args[]) {
		
		for(long i=0; i<1000; i++) {
			long newNumber = translate(i, false);
			
			//System.out.println(i + ": " + newNumber);
			System.out.println(newNumber);
			
		}
	}
	
	
	
	//Idea: build binary number right to left
	//1) if number is even: divide num by 2 and add 0 to binary ouput
	//2) if number is odd: (mult by 3 add 1)/2 and add 1 to binary output
	//3) repeat until number is 1 or 64th step....
	
	//4)XOR output by
	//10101010...
	//or 010101010...
	//I think this just changes the sign and adds 1 or something, so no big deal.
	
	//5) If binary starts with 1, then make it a negative number (2s complement way?)
	
	
	//TODOs: do the function for a 1000 bit number, so it won't mess up at 27
	//Search numbers on the OEIS (regular and other way)
	//Find the inverse function
		//--> I've convinced myself that the function is one-to-one and goes from positive to integer.
		//Given there's enough bits to eventually get stuck in the 1, 2, 4 loop.
		//AH: It's not one-to-one! f(x) = -3 doesn't exist! (Assuming f is the orig way)
	
	//TODO: Make a log(2) plot of it
	//TODO: maybe make output hex in the hopes that it'll be easier to spot patterns.
	
	public static int NUM_BITS_IN_LONG = 64;
	
	//start with long, and then move onto big int
	public static long translate(long input, boolean otherWay) {
		
		boolean array[] = new boolean[NUM_BITS_IN_LONG];
		
		long curNumber = input;
		
		for(int i=array.length - 1; i>=0; i--) {
			
			//if(input == 27) {
			//	System.out.println("27-" + i + ":" + curNumber);
			//}
			if(curNumber % 2 == 0) {
				curNumber /= 2;
				array[i] = false;
			} else {
				curNumber = (3 * curNumber + 1) / 2;
				array[i] = true;
				
			}
		}
		
		if(curNumber != 1 && curNumber != 2) {
			System.out.println("Warning: number " +input + " didn't end on 1 or 2 after " + NUM_BITS_IN_LONG + " iterations!");
		}
		
		//Mix it up
		int mixIndex = 0;
		if(otherWay) {
			mixIndex=1;
		}
		for(; mixIndex<array.length; mixIndex+=2) {
			array[mixIndex] = !array[mixIndex];
		}
		
		long ret = 0;
		
		long current = 1;
		
		for(int i=array.length - 1; i>=0; i--) {
			
			if(i==0) {
				//negative
				if(array[i]) {
					//Note long becomes negative by itself... so I don't even need to do anything...
					ret += current;
				}
				
			} else {
				if(array[i]) {
					ret += current;
				}
			}
			
			current*= 2;
		}
		
		return ret;
	}

	
}

/*
 * Regular
 *(It's not on the OEIS... probably too niche...)
1: -1
2: 0
3: 9
4: -2
5: -5
6: -20
7: 573
8: 2
9: 2295
10: 8
11: -287
12: 38
13: 35
14: -1148
15: -1115
16: -6
17: 143
18: -4592
19: -4583
20: -18
21: -21
22: 572
23: 557
24: -78
25: -18329
26: -72
*/
/*
 * other way: if x is answer in orig way, and y is answer in other way, then:
 *  y = -x - 1
 
1: 0
2: -1
3: -10
4: 1
5: 4
6: 19
7: -574
8: -3
9: -2296
10: -9
11: 286
12: -39
13: -36
14: 1147
15: 1114
16: 5
17: -144
18: 4591
19: 4582
20: 17
21: 20
22: -573
23: -558
24: 77
25: 18328
26: 71
*/

//Should I bother adding it to the OEIS?