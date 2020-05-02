package Polydivisable2;

import java.math.BigInteger;
import java.util.ArrayList;

import UtilityFunctions.UtilityFunctions;

public class PolydivisableNumberBaseLong {
	//TODO: This is currently broken... :(

	//TODO: make sure it's small enough
	//or be smart about it
	//public static int BLOCK_SIZE = 12;
	
	//For 100
	public static int BLOCK_SIZE = 10;
	
	public static long remainders[][];
	public static long dividers[];
	
	public static boolean isCorrectGCDWithBase[][];
	
	public static boolean used[];
	public static int list[];
	
	
	public static void main(String[] args) {

		
		for(int base= 16; base<=84; base = base + 2) {
			int numBuckets = (int)Math.ceil((1.0*base)/(1.0*BLOCK_SIZE));
			
			remainders = new long[base][numBuckets];
			dividers = new long[numBuckets];
			
			for(int i=0; i<remainders.length; i++) {
				for(int j=0; j<remainders[0].length; j++) {
					remainders[i][j] = 0;
				}
			}
			for(int i=0; i<dividers.length; i++) {
				dividers[i] = 1;
			}
			
			for(int i=1; i<base; i++) {
				
				int currentBucket = getBucketNumber(i);
				dividers[currentBucket] = (dividers[currentBucket]*i) / UtilityFunctions.getGCD(i, dividers[currentBucket]);
				System.out.println(currentBucket + ": " + dividers[currentBucket]);
			}
			

			getContraintsForPolydivisableNumbersBaseN(base);
			
			guessNumber(base);
		}
		
	}
	
	
	public static void getContraintsForPolydivisableNumbersBaseN(int base) {
			System.out.println("Getting constraints for base " + base);
			
			//[index to add] [number to add]
			isCorrectGCDWithBase = new boolean[base][base];
			for(int i=1; i<base; i++) {
				System.out.print(i + ":");
				if(i < 10 ) {
					System.out.print(" ");
				}
				for(int j=1; j<base; j++) {
					if(UtilityFunctions.getGCD(base, i) == UtilityFunctions.getGCD(base, j)) {
						isCorrectGCDWithBase[i][j] = true;
						System.out.print("* ");
					} else {
						isCorrectGCDWithBase[i][j] = false;
						System.out.print("  ");
					}
				}
				System.out.println();
			}
			
			System.out.println("BoostN:");
			isCorrectGCDWithBase =  ImproveConstraintsWithPowN(base, isCorrectGCDWithBase);
			for(int i=1; i<base; i++) {
				System.out.print(i + ":");
				if(i < 10 ) {
					System.out.print(" ");
				}
	
				for(int j=1; j<base; j++) {
					if(isCorrectGCDWithBase[i][j]) {
						System.out.print("*" + " ");
					} else {
						System.out.print(" " + " ");
					}
				}
				System.out.println();
			}
	
			boolean trueTable[][] = new boolean[base][base];
			for(int i=0; i<trueTable.length; i++) {
				for(int j=0; j<trueTable.length; j++) {
					trueTable[i][j] = true;
				}
			}
			System.out.println("OnlyBoostN:");
			trueTable =  ImproveConstraintsWithPowN(base, trueTable);
			for(int i=1; i<base; i++) {
				System.out.print(i + ":");
				if(i < 10 ) {
					System.out.print(" ");
				}
	
				for(int j=1; j<base; j++) {
					if(trueTable[i][j] == false && UtilityFunctions.getGCD(base, i) == UtilityFunctions.getGCD(base, j)) {
						System.out.print((j%10) + " ");
					} else {
						System.out.print(" " + " ");
					}
				}
				System.out.println();
			}
			
			//no zeros allowed:
			for(int i=0; i<base - 1; i++) {
				isCorrectGCDWithBase[i][0] = false;
			}
			
			//list is one based.
			list = new int[base];
			
			//used is one based.
			used = new boolean[base];
			//used[1] for 1
			//...
			//used[9] for 9.
			for(int i=0; i<base; i++) {
				list[i] = 0;
				used[i] = false;
			}
			
			//0 is not allowed to be used.
			used[0] = true;
	
	}
	
	
	public static boolean[][] ImproveConstraintsWithPowN(int base, boolean current[][]) {
		for(int n = 2; n<base; n++) {
			if(UtilityFunctions.isPrime(n)) {
				int currentMult = 1;
				
				while(base % currentMult == 0) {
					currentMult *= n;
				}
				
				//it doesn't work if it's odd or a power of 2:
				//(I'm not sure about the odd thing, but I know odd numbers can't have a solution)
				if(currentMult == n || currentMult >= base) {
					continue;
				}
				
				
				System.out.println("Bonus time N for n = " + n + "!");
				
				if(n == 2) {
					int offset = currentMult/2;
					for(int i=offset; i<base; i+=currentMult) {
						for(int j=offset; j<base; j+=currentMult) {
							current[i][j] = false;
						}
					}
				}
				
				for(int i=currentMult; i<base; i+=currentMult) {
					for(int j=currentMult; j<base; j+=currentMult) {
						current[i][j] = false;
					}
				}
			}
		}
		return current;
		
	}
	
	
	public static void guessNumber(int base) {
		guessNumber(base, 1);
	}
	
	public static void guessNumber(int base, int indexToAdd) {
		int targetLengthNumber = base;
		/*
		if(indexToAdd >= targetLengthNumber) {
			System.out.println("ERROR: index to add is too big!");
			System.exit(1);
		}
		*/
		multRemaindersByBase(base, indexToAdd);
		
		//BigInteger firstTryBig = new BigInteger("" + indexToAdd).subtract(remainder);
		//int firstTry = firstTryBig.intValue();
		int currentBucket = getBucketNumber(indexToAdd);
		int firstTry = (int)((0 - remainders[indexToAdd][currentBucket])) % indexToAdd;
		
		if(firstTry < 1) {
			firstTry += indexToAdd;
		}
		/*
		if(firstTry <1 || firstTry > indexToAdd) {
			System.out.println("ERROR: something went wrong!");
			System.exit(1);
		}
		*/
		int jumpAmount = indexToAdd;
		
		
		for(int i=firstTry; i < base; i += jumpAmount) {
			if(indexToAdd == 1) {
				if(base == 84 && i==1) {
					i = 67;//24*12*14
					//3, 21, 37, 55
				}
				System.out.println("Trying to start with " + i);
				
			}
			
			if(used[i] == false && isCorrectGCDWithBase[indexToAdd][i]) {
					
				used[i] = true;
				list[indexToAdd] = i;
				
				//TRACKING
				
				if(base >= 84 && indexToAdd == 3) {
					for(int j=0; j<indexToAdd; j++) {
						System.out.print(list[j] +  ", " );
					}
					System.out.println(list[indexToAdd]);
				}
				
				//nextNumber = currentNumber.add(new BigInteger("" + i));
				addToRemainders(indexToAdd, i);
				
				indexToAdd++;
				
				if(indexToAdd == targetLengthNumber) {
					System.out.println("Answer: " + PrintNumber(list, base));
					
					//TRACKING
					System.exit(1);
					
					PrintNumber(list, base);
				} else {
					guessNumber(base, indexToAdd);
				}
				
				indexToAdd--;

				removeFromRemainders(indexToAdd, i);
				
				used[i] = false;
				
				
			}
		}

	}
	
	//TODO
	public static void multRemaindersByBase(int base, int index) {
		int bucketNumber = getBucketNumber(index);
		
		for(int i=bucketNumber; i<dividers.length; i++) {
			remainders[index][i] = (remainders[index - 1][i] * base) % dividers[i];
		}
	}
	
	public static void addToRemainders(int index, int number) {
		int bucketNumber = getBucketNumber(index);
		
		//TODO: figure out If I have to update every bucket
		for(int i=bucketNumber; i<dividers.length; i++) {
			remainders[index][i] += number;
			remainders[index][i] %= dividers[i];
		}
	}
	
	public static void removeFromRemainders(int index, int number) {
		int bucketNumber = getBucketNumber(index);
		//TODO: figure out If I have to update every bucket
		for(int i=bucketNumber; i<dividers.length; i++) {
			remainders[index][i] += (dividers[i] - number);
			remainders[index][i] %= dividers[i];
		}
	}
	
	public static int getBucketNumber(int index) {
		return (index-1)/BLOCK_SIZE;
	}
	
	//UTIL FUNCTIONS:
	public static String PrintNumber(int list[], int base) {
		return convertStringArrayToCommaDelimited(convertListToStringArray(list), base) + " = " + convertStringArrayToBase10(list, base);
	}
	
	public static String[] convertListToStringArray(int list[]) {
		String ret[] = new String[list.length];
		for(int i=0; i<list.length; i++) {
			ret[i] = "" + list[i];
		}
		return ret;
	}
	
	public static String convertStringArrayToCommaDelimited(String list[], int base) {
		String ret = "";
		
		//One-base list:
		for(int i=1; i<list.length; i++) {
			ret += list[i] + ", ";
			if(Integer.parseInt(list[i]) > base) {
				System.out.println("ERROR: digit is too damn high!");
				System.exit(1);
			}
		}
		//take away the last ", ":
		ret = ret.substring(0, ret.length() - 2);
		//Write down the base.
		ret += "(base " + base + ")";
		return ret;
	}
	
	public static String convertStringArrayToBase10(int list[], int base) {
		return convertBigIntegerArrayToBase10(list, base, list.length).toString();
	}
	
	public static BigInteger convertBigIntegerArrayToBase10(int list[], int base, int length) {
		BigInteger mult = BigInteger.ONE;
		BigInteger ret = BigInteger.ZERO;
		
		for(int i=0; i<length; i++) {
			ret = ret.add(new BigInteger("" + list[length - 1 - i]).multiply(mult));
			mult = mult.multiply(new BigInteger("" + base));
		}
		return ret;
	}
}

//base 68:
//checked

//base 74:
//hard

//base 78:
//checked

//base 80:
//1, 3, 5, 7
//checked

//base 84
//checked

//base 90
//checked