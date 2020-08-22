package collatz.justTryPseudoMersenneNumbers;

import java.math.BigInteger;


public class JustTryWithBoolArrayForwardTrial {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		getMersenne(6);
		
		for(int i=0; i<array.length; i++) {
			if(array[i]) {
				System.out.print(1);
			} else {
				System.out.print(0);
			}
		}
		System.out.println();
		printNumber();
		
		collatz();
		
	}
	
	
	public static void collatz() {
		
		while(indexNolongerPartOfNumber - leadingIndex > 1) {
			if(array[indexNolongerPartOfNumber - 1] == false) {
				divide2();
			} else {
				mult3Plus1Divide2();
			}

			printNumber();
		}
		
	}

	
	public static int BILLION = 100000000;;
	public static int ARRAY_LENGTH = 1000;
	
	public static boolean array[] = new boolean[ARRAY_LENGTH];
	
	public static int leadingIndex = array.length - 1;
	public static int indexNolongerPartOfNumber = array.length;

	public static void getMersenne(int n) {
		
		for(int i=array.length - n; i<array.length; i++) {
			array[i] = true;
		}
		indexNolongerPartOfNumber = array.length;
		leadingIndex = array.length - n;
		
	}
	
	
	public static void printNumber() {
		int num = 0;
		
		if(leadingIndex >= indexNolongerPartOfNumber) {
			System.out.println("ERROR: got 0!");
			System.out.println(0);
			System.exit(1);
		} else {
			for(int i=leadingIndex; i<indexNolongerPartOfNumber; i++) {
				if(array[i]) {
					System.out.print(1);
					num = 1 + 2*num;
				} else{
					System.out.print(0);
					num *= 2;
				}
			}
			System.out.print(": " + num);
			System.out.println();
		}
	}
	
	public static void divide2() {
		indexNolongerPartOfNumber--;
	}
	
	//public static int GAP = 100;
	//TODO:
	//AH!!! complex binary logic.
	public static void mult3Plus1Divide2() {
		
		int startOrigNumber = leadingIndex-2;
		
		
		int GAP = indexNolongerPartOfNumber - startOrigNumber;

		
		boolean carry = true;
		for(int i=indexNolongerPartOfNumber - 2; i>=startOrigNumber; i--) {
		
			//TODO: look up half adder and full adder...
			boolean nextDigit = (array[i] ^ array[i+1]) ^ carry;
			carry = (carry && !(!array[i] && !array[i+1])) || (!carry && array[i] && array[i+1]);			
			array[i + 1 - GAP] = nextDigit;

		}
		
		
		leadingIndex -= 1 + GAP;
		//DIDNT TEST
		while(array[leadingIndex] == false) {
			//TODO:
			System.out.println("Boo");
			leadingIndex++;
		}

		indexNolongerPartOfNumber -= GAP;
		
		//divide2();
		
	}
	
}
/*
111111: 63
1011111: 95
10001111: 143
Boo
11010111: 215
101000011: 323
Boo
111100101: 485
1011011000: 728
101101100: 364
10110110: 182
1011011: 91
10001001: 137
Boo
11001110: 206
1100111: 103
10011011: 155
Boo
11101001: 233
101011110: 350
10101111: 175
100000111: 263
Boo
110001011: 395
1001010001: 593
Boo
1101111010: 890
110111101: 445
1010011100: 668
101001110: 334
10100111: 167
Boo
11111011: 251
101111001: 377
1000110110: 566
100011011: 283
Boo
110101001: 425
1001111110: 638
100111111: 319
Boo
111011111: 479
1011001111: 719
10000110111: 1079
Boo
11001010011: 1619
100101111101: 2429
Boo
111000111100: 3644
11100011110: 1822
1110001111: 911
10101010111: 1367
100000000011: 2051
Boo
110000000101: 3077
1001000001000: 4616
100100000100: 2308
10010000010: 1154
1001000001: 577
Boo
1101100010: 866
110110001: 433
1010001010: 650
101000101: 325
Boo
111101000: 488
11110100: 244
1111010: 122
111101: 61
1011100: 92
101110: 46
10111: 23
100011: 35
Boo
110101: 53
1010000: 80
101000: 40
10100: 20
1010: 10
101: 5
1000: 8
100: 4
10: 2
1: 1

*/
