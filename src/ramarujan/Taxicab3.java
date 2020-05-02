package ramarujan;

import java.math.BigInteger;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

public class Taxicab3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		complexGetFirstAnswer();
	}
	
	public static void complexGetFirstAnswer() {
		int POWER = 5;
		//int MOD = 1000;
		
		int MIN_MOD = 20000000;
		//int MIN_MOD = 10000;
		
		int inversableMod = -1;
		
		for(int mod=MIN_MOD; mod<10*MIN_MOD; mod++) {
			boolean taken[] = new boolean[mod];
			for(int i=0; i<mod; i++) {
				taken[i] = false;
			}
			
			for(int i=0; i<mod; i++) {
				//System.out.println(getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + mod)));
				int temp = (int)getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + mod)).longValue();
				if(taken[temp] == true) {
					break;
				}
				taken[temp] = true;
			}
			int numTaken = 0;
			for(int i=0; i<mod; i++) {
				if(taken[i] == true) {
					numTaken++;
				}
			}
			
			System.out.println(mod + " " + numTaken + " out of " + mod);
			if(numTaken == mod) {
				inversableMod = mod;
				break;
				//System.exit(1);
			}
		}
		//1002 is a good mod.
		//10001 is a good mod
		//1000002
		
		
		
		int inversePower5[] = new int[(int)inversableMod];
		for(int i=0; i<inversePower5.length; i++) {
			inversePower5[i] = -1;
		}
		for(int i=0; i<inversableMod; i++) {
			//System.out.println(getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + mod)));
			int tempNum = (int)getAPowerPmodMOD(new BigInteger("" + i), new BigInteger("" + POWER), new BigInteger("" + inversableMod)).longValue();
			inversePower5[tempNum] = i;
		}
		
		
		int M = inversableMod;
		powerArray = new BigInteger[M];
		
		// searched up to: Answer Mod M: 3107
		for(int answerModM=0; answerModM<M; answerModM++) {
			if(answerModM % 100 == 0) {
				System.out.println("Up to answer Mod M: " + answerModM + " where M = " + M + " and power is "+ POWER);
			}
			
			// put back to hashMap so I could find the original answer?
			Hashtable<BigInteger, Integer> hashTable = new Hashtable<BigInteger, Integer>();
			
			for(int i=0; i<M; i++) {
				int a = inversePower5[i];
				int b = inversePower5[(answerModM - i + M) % M];
				
				if(a <= b) {
					BigInteger sum = getSumWithPowers(POWER, a, b);
					
					if(sum.remainder(new BigInteger("" + M)).longValue() != answerModM) {
						System.out.println("ERROR!");
						System.exit(1);
					}
					
					if(hashTable.get(sum) != null) {
						
						System.out.println("---------------------- ");
						System.out.println("Answer!");
						
						System.out.println("a = " + a);
						System.out.println("b = " + b);
						
						
						System.out.println("c = " + hashTable.get(sum));
						
						System.out.println("d = " + getRoot(sum, POWER, hashTable.get(sum)));
						
						System.out.println("You get 2 solutions with: " + sum);
						System.out.println("---------------------- ");
						
						System.exit(1);
					} else{
						hashTable.put(sum, a);
					}
				}
				
			}
			
			
		}
		
		System.exit(1);
		
		
		
		Hashtable hashtable = new Hashtable<Long, Long>();
		BigInteger tempI;
		BigInteger tempBigI;
		BigInteger tempJ;
		BigInteger tempBigJ;
		
		//int POWER = 4;
		
		for(long i=1; true; i++) {
			
			Enumeration<BigInteger> e = hashtable.keys();
			
			tempBigI = new BigInteger("1");
			tempI = new BigInteger("" + i);
			
			for(int k=0; k<POWER; k++) {
				tempBigI = tempBigI.multiply(tempI);
			}
			
			BigInteger tempElement;
			
			while(e.hasMoreElements()) {
				tempElement = e.nextElement();
				if(tempElement.compareTo(tempBigI) <= 0) {
					hashtable.remove(tempElement);
				}
			}
			if(i % 100 == 0 ) {
				System.out.println("i = " + i + " hashtable length: " + hashtable.size());
				
			}
			
			
			for(long j=1; j<=i; j++) {
				tempJ = new BigInteger("" + j);
				
				tempBigI = new BigInteger("1");
				
				for(int k=0; k<POWER; k++) {
					tempBigI = tempBigI.multiply(tempI);
				}
				
				tempBigJ= new BigInteger("1");
				for(int k=0; k<POWER; k++) {
					tempBigJ = tempBigJ.multiply(tempJ);
				}
				
				tempBigI = tempBigI.add(tempBigJ);
				
				if(hashtable.get(tempBigI) != null) {
					System.out.println("Found: " + tempBigI);
					System.out.println(tempBigI + " = " + i + "^" + POWER + " + " + j + "^" + POWER);
					
					
					long fourthRoot = getRoot(tempBigI, POWER, (long)hashtable.get(tempBigI));
					System.out.println(tempBigI + " = " + hashtable.get(tempBigI) + "^" + POWER + " + " + fourthRoot + "^" + POWER );
					
					System.exit(1);
				} else {
					hashtable.put(tempBigI, i);
				}
			}
		}
	}
	
	
	
	
	public static long getRoot(BigInteger sum, int power, long firstNumber) {
		BigInteger firstNumberPower = new BigInteger("1");
		for(int k=0; k<power; k++) {
			firstNumberPower = firstNumberPower.multiply(new BigInteger("" + firstNumber));
		}
		
		BigInteger secondNumberPower = sum.subtract(firstNumberPower);
		
		//ystem.out.println("Second Number Power: " + secondNumberPower);
		
		BigInteger temp;
		for(int i=1; true; i++) {
			temp = new BigInteger("1");
			for(int k=0; k<power; k++) {
				temp = temp.multiply(new BigInteger("" + i));
			}
			if(temp.compareTo(secondNumberPower) == 0) {
				return i;
			} else if(temp.compareTo(secondNumberPower) > 0) {
				break;
			}
		}
		return -1;
	}
	
	
	
	public static BigInteger getAPowerPmodMOD(BigInteger a, BigInteger pow, BigInteger MOD) {
		//base case for power:
		if(pow.compareTo(BigInteger.ZERO) == 0) {
			if(a.compareTo(BigInteger.ZERO) == 0 ) {
				System.out.println("0^0 is I don't know!!!");
				System.exit(1);
			} else if(pow.compareTo(BigInteger.ZERO) < 0 ) {
				System.out.println("No negative powers!" +  a + " to the power of " + pow + "?" );
				System.exit(1);
			}
			return BigInteger.ONE;
		} else if(a.compareTo(BigInteger.ZERO) == 0) {
			return BigInteger.ZERO;
		}
		
		//System.out.println(a + " to the power of " + pow);
		
		int lengthPowTable = 0;
		BigInteger current = BigInteger.ONE;
		while(current.compareTo(pow) <= 0) {
			lengthPowTable++;
			current = current.multiply(new BigInteger("2"));
		}
		
		//System.out.println("Length: " + lengthPowTable);
		
		//Setup the power of 2 table
		BigInteger pow2Table[] = new BigInteger[lengthPowTable];
		pow2Table[0] = a;
		
		
		for(int i=1; i<lengthPowTable; i++) {
			pow2Table[i] = (pow2Table[i-1].multiply(pow2Table[i-1])).mod(MOD);
		}
		//End setup the power of 2 table.
	
		current = pow;
		BigInteger answer = BigInteger.ONE;
		
		
		for(int i=0; i<lengthPowTable && current.compareTo(BigInteger.ZERO) > 0; i++) {
			if(current.mod(new BigInteger("2")).compareTo(BigInteger.ONE) == 0) {
				answer = (answer.multiply(pow2Table[i])).mod(MOD);
				current = current.subtract(BigInteger.ONE);
			}
			current = current.divide(new BigInteger("2"));
		}
		
		return answer;
	}
	
	
	public static BigInteger powerArray[];
	
	//If the power was already found, resuse it.
	public static BigInteger getSumWithPowers(long power, long a, long b) {
		BigInteger temp1;
		BigInteger temp2;
		
		if(powerArray[(int)a] == null) {
			BigInteger aBig = new BigInteger("" + a);
			
			temp1 = BigInteger.ONE;
			
			for(int i = 0; i<power; i++) {
				temp1 = temp1.multiply(aBig);
			}
			powerArray[(int)a] = temp1;
		} else {
		
			temp1 = powerArray[(int)a];
			
		}
		if(powerArray[(int)b] == null) {
			BigInteger bBig = new BigInteger("" + b);
			
			temp2 = BigInteger.ONE;
			
			for(int i = 0; i<power; i++) {
				temp2 = temp2.multiply(bBig);
			}
			powerArray[(int)b] = temp2;
			
		} else {
			temp2 = powerArray[(int)b];
		}
			
		return temp1.add(temp2);
	}

}

//After running this for days, I conclude that:
/*If a solution to a^5+b^5 = c^5 + d^5 exists, one of the terms is over a million.
*/