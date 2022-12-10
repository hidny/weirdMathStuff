package OneNet3Cuboids.DupRemover;

import java.math.BigInteger;
import java.util.HashSet;

public class BasicUniqueChecksMaxSpeedUp {


	public static int NUM_REFLECTIONS = 2;
	public static int NUM_ROTATIONS = 4;

	public static HashSet<BigInteger> uniqList = new HashSet<BigInteger>();
	
	public static boolean isUnique(boolean array[][]) {
		
		//TODO: make function to get borders...
		int firsti = 0;
		
		int lasti = array.length - 1;
		
		
		int firstj = 0;
		int lastj = array[0].length - 1;
		
		TOP_BORDER:
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				
				if(array[i][j]) {
					firsti = i;
					break TOP_BORDER;
				}
			}
		}
		

		BOTTOM_BORDER:
		for(int i=array.length - 1; i>=0; i--) {
			for(int j=0; j<array[0].length; j++) {
				
				if(array[i][j]) {
					lasti = i;
					break BOTTOM_BORDER;
				}
			}
		}
		
		
		LEFT_BORDER:
		for(int j=0; j<array[0].length; j++) {
			for(int i=0; i<array.length; i++) {
				
				if(array[i][j]) {
					firstj = j;
					break LEFT_BORDER;
				}
			}
		}
		

		RIGHT_BORDER:
		for(int j=array[0].length - 1; j>=0; j--) {
			for(int i=0; i<array.length; i++) {
				
				if(array[i][j]) {
					lastj = j;
					break RIGHT_BORDER;
				}
			}
		}

		//END TODO: make function to get borders...
		

		// sideFactor and vertFactor is to make sure all nets that get converted to binary num
		// have the same background dimensions to work with.
		// If they don't, there could be a false match.
		BigInteger sideFactor = BigInteger.ONE;
		BigInteger vertFactor = BigInteger.ONE;

		BigInteger TWO = new BigInteger("2");
		
		BigInteger intermediateFactor = BigInteger.ONE;
		for(int j=0; j<array[0].length; j++) {
			intermediateFactor = intermediateFactor.multiply(TWO);
		}
		
		for(int i = lasti - firsti; i<array.length - 1; i++) {
				vertFactor = vertFactor.multiply(intermediateFactor);
		}
		

		for(int j = lastj - firstj; j<array[0].length - 1; j++) {
			sideFactor = sideFactor.multiply(TWO);
		}
		

		//Need to update the side Factor and vertFactor:
		BigInteger sideFactor2 = BigInteger.ONE;
		BigInteger vertFactor2 = BigInteger.ONE;

		
		for(int i = lasti - firsti; i<array.length - 1; i++) {
			vertFactor2 = vertFactor2.multiply(TWO);
		}
		
		intermediateFactor = BigInteger.ONE;
		for(int i = 0; i<array.length; i++) {
			intermediateFactor = intermediateFactor.multiply(TWO);
		}
		
		for(int j = lastj - firstj; j<array[0].length - 1; j++) {
			sideFactor2 = sideFactor2.multiply(intermediateFactor);
		}
		
		BigInteger scores[] = new BigInteger[NUM_REFLECTIONS * NUM_ROTATIONS];
		
		for(int i=0; i<scores.length; i++) {
			scores[i] = BigInteger.ZERO;
		}
		
		boolean tooLow[] = new boolean[NUM_REFLECTIONS * NUM_ROTATIONS];
		boolean onlyOneContender = false;
		
		for(int i=firsti, irev = lasti; i<=lasti; i++, irev--) {
			for(int j=firstj, jrev = lastj; j<=lastj; j++, jrev--) {
				
				for(int k=0; k<scores.length; k++) {
					if(tooLow[k]) {
						continue;
					}
					scores[k] = scores[k].multiply(TWO);
				}
				
				int tmp = (i-firsti) * (lastj - firstj + 1) + (j - firstj);
				int i2 = firsti + (tmp % (lasti - firsti + 1));
				int j2 = firstj + (tmp / (lasti - firsti + 1));

				int i2rev = lasti - (tmp % (lasti - firsti + 1));
				int j2rev = lastj - (tmp / (lasti - firsti + 1));

				if(!tooLow[0] && array[i][j]) {
					scores[0] = scores[0].add(BigInteger.ONE);
				}
				
				if(!tooLow[1] && array[i][jrev]) {
					scores[1] = scores[1].add(BigInteger.ONE);
				}
				
				if(!tooLow[2] && array[irev][j]) {
					scores[2] = scores[2].add(BigInteger.ONE);
				}
				
				if(!tooLow[3] && array[irev][jrev]) {
					scores[3] = scores[3].add(BigInteger.ONE);
				}

				
				if(!tooLow[4] && array[i2][j2]) {
					scores[4] = scores[4].add(BigInteger.ONE);
				}
				
				if(!tooLow[5] && array[i2][j2rev]) {
					scores[5] = scores[5].add(BigInteger.ONE);
				}
				
				if(!tooLow[6] && array[i2rev][j2]) {
					scores[6] = scores[6].add(BigInteger.ONE);
				}
				
				if(!tooLow[7] && array[i2rev][j2rev]) {
					scores[7] = scores[7].add(BigInteger.ONE);
				}

				if(! onlyOneContender  ) {
					int numContender = 0;

					BigInteger tmpScores[] = new BigInteger[NUM_REFLECTIONS * NUM_ROTATIONS];
					
					int numExtra1 = (i - firsti)* ((array[0].length - 1) - (lastj - firstj));
					int numExtra2 = (j2 - firstj)* ((array.length - 1) - (lasti - firsti));
					
					if(i == firsti && j2 == firstj) {
						tmpScores = scores;
					} else {
						
						
						BigInteger factorToUse = BigInteger.ONE;
						for(int n=0; n < Math.abs(numExtra1 - numExtra2); n++) {
							factorToUse = factorToUse.multiply(TWO);
						}
						
						for(int m=0; m<scores.length; m++) {
							if(numExtra1 < numExtra2 && m < 4) {
								tmpScores[m] = scores[m].multiply(factorToUse);
							} else if(numExtra1 > numExtra2 && m >= 4) {
								tmpScores[m] = scores[m].multiply(factorToUse);
							} else {
								tmpScores[m] = scores[m];
							}
							
						}
						
					}
					
					for(int k=0; k<tmpScores.length; k++) {
						if(! tooLow[k]) {
							numContender++;
						}
					}
					for(int k=0; k<tmpScores.length; k++) {
						if(tooLow[k]) {
							continue;
						}
						for(int m=k+1; m<tmpScores.length; m++) {
							if(tooLow[m]) {
								continue;
							}
							
							if(tmpScores[k].compareTo(tmpScores[m]) < 0) {
								
								//System.out.println(tmpScores[k] + " <");
								//System.out.println(tmpScores[m]);
								
								if( (m <4 && k<4) || (m >= 4 && k >= 4)
										|| (numExtra2 > numExtra1 && k >= 4)
										|| (numExtra2 < numExtra1 && k < 4)) {
									tooLow[k] = true;
									numContender--;
								}
								break;
							} else if(tmpScores[k].compareTo(tmpScores[m]) > 0) {
								
								
								if( (m <4 && k<4) || (m >= 4 && k >= 4)
										|| (numExtra2 > numExtra1 && m >= 4)
										|| (numExtra2 < numExtra1 && m < 4)) {
									numContender--;
									tooLow[m] = true;
									
								
								}
							}
						}
					}
					
					if(numContender == 1) {
						//System.out.println("Only 1 contender");
						onlyOneContender = true;
					}
				}
				
				if(tmp > 0 &&
						i2 == lasti) {
					
					for(int k=0; k<scores.length/2; k++) {
						if(tooLow[4 + k]) {
							continue;
						}
						scores[4 + k] = scores[4 + k].multiply(vertFactor2);
					}
				}
				
				
				
			}
			
			for(int k=0; k<scores.length/2; k++) {
				if(tooLow[k]) {
					continue;
				}
				scores[k] = scores[k].multiply(sideFactor);
			}
		
			
		}
		for(int k=0; k<scores.length/2; k++) {
			if(tooLow[k]) {
				continue;
			}
			scores[k] = scores[k].multiply(vertFactor);
		}
		
		for(int k=0; k<scores.length/2; k++) {
			if(tooLow[4 + k]) {
				continue;
			}
			scores[4 + k] = scores[4 + k].multiply(sideFactor2);
		}
		

		
		//End update the side Factor and vertFactor:

		
		//Deal with symmetries by getting max scores from 8 possible symmetries:
		BigInteger max = BigInteger.ZERO;
		
		for(int i=0; i<scores.length; i++) {
			if(max.compareTo(scores[i]) < 0) {
				max = scores[i];
			}
		}
		
		//System.out.println("Max: " + max);
		
		//Sanity check:
		
		/*BasicUniqueCheck.isUnique(array);
		
		if(! BasicUniqueCheck.uniqList.contains(max)) {
			System.out.println("DOH!");
			System.exit(1);
		}*/
		
		if(! uniqList.contains(max)) {
			uniqList.add(max);
			
			//System.out.println("Max number: " + max);
			
			return true;
		} else {
			return false;
		}
	}
	
	
	//public static boolean[] refreshNumContenders(BigInteger scores[]) {
		
	//}
	
}
