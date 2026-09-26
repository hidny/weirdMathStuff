package sanderson_puzzlers;

import java.math.BigInteger;
import java.util.ArrayList;

public class MultMagicTrick {

	//Trying to solve Grant Sanderson's question:
	//https://www.youtube.com/shorts/ausLKMojXaY
	
	//Goal: given number N
	// Find M s.t M*N = P where P only has 1s and 0s in decimal.
	
	//I did a breadth first search starting with the right-most decimals and then going left until I find answers.
	//I could switch it to a depth-first search so that I could save on memory, but I don't care enough.
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//getMultFor1sAnd0s(3014630L);
		
		//getMultFor1sAnd0s(1234123435L);

		//getMultFor1sAnd0s(3141592653L);
		
		getMultFor1sAnd0s(2048L);
	}

	
	public static BigInteger getMultFor1sAnd0s(long input) {
		
		return getMultFor1sAnd0s(new BigInteger("" +  input));
	}
	
	public static BigInteger getMultFor1sAnd0s(BigInteger input) {
		
	
		BigInteger ten = new BigInteger("10");
		ArrayList<String> possibleNDigitNumbers = new ArrayList<String>();
		
		//TODO: ignore the 0 solution somehow!
		
		int numSolutions = 0;
		int NUM_SOLUTIONS_MAX = 1000;
		
		for(int i=0; i<10; i++) {
			possibleNDigitNumbers.add(new String(i + ""));
		}
		
		ArrayList<String> possibleNDigitNumbersFiltered = new ArrayList<String>();
		
		// + 20 to be lazy...
		for(int numDigits = 1; true; numDigits++) {

			possibleNDigitNumbersFiltered = new ArrayList<String>();
			
			for(int i=0; i<possibleNDigitNumbers.size(); i++) {
				
				BigInteger tmp = new BigInteger(possibleNDigitNumbers.get(i));
				
				BigInteger prodRem = input.multiply(tmp).divideAndRemainder(ten.pow(numDigits))[1];
				
				String prodRemS = prodRem.toString();
				
				boolean acceptable = true;
				
				for(int j=0; j<prodRemS.length(); j++) {
					if(prodRemS.charAt(j) == '0' || prodRemS.charAt(j) == '1') {
						//pass
					} else {
						acceptable = false;
						break;
					}
				}
				

				
				if(acceptable) {
					possibleNDigitNumbersFiltered.add(possibleNDigitNumbers.get(i));

					BigInteger prod = input.multiply(tmp);
					
					if(prod.compareTo(BigInteger.ZERO) > 0 && possibleNDigitNumbers.get(i).startsWith("0") == false) {
						String prodS = prod.toString();
						
						boolean acceptable2 = true;
						
						for(int j=0; j<prodS.length(); j++) {
							if(prodS.charAt(j) == '0' || prodS.charAt(j) == '1') {
								//pass
							} else {
								acceptable2 = false;
								break;
							}
						}
						
						if(acceptable2) {
							
							System.out.println("tmp: " + tmp + "    " + prod);
							numSolutions++;
							
							if(numSolutions >= NUM_SOLUTIONS_MAX) {
								System.out.println("Let us just stop here...");
								System.exit(0);
							}
						}
					}
				}
				
				
				
				
			}
			
			
			possibleNDigitNumbers = new ArrayList<String>();
	
			for(int i=0; i<10; i++) {
				for(int j=0; j<possibleNDigitNumbersFiltered.size(); j++) {
					possibleNDigitNumbers.add(new String(i + "" + possibleNDigitNumbersFiltered.get(j)));
				}
			}
			
		
		}
		
		
	}
}
