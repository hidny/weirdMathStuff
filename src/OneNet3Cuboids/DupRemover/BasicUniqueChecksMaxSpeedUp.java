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
		
		//TODO: make function to get scores:
		//I could condense this and make it less repetitive, but I'm lazy.
		
		BigInteger scores[] = new BigInteger[NUM_REFLECTIONS * NUM_ROTATIONS];
		
		for(int i=0; i<scores.length; i++) {
			scores[i] = BigInteger.ZERO;
		}
		
		//boolean tooLow[] = new boolean[NUM_REFLECTIONS * NUM_ROTATIONS];
		//boolean maybeTooLow[] = new boolean[NUM_REFLECTIONS * NUM_ROTATIONS];
		//boolean onlyOneContender = false;
		
		for(int i=firsti, irev = lasti; i<=lasti; i++, irev--) {
			for(int j=firstj, jrev = lastj; j<=lastj; j++, jrev--) {
				
				for(int k=0; k<scores.length; k++) {
					//if(tooLow[k]) {
					//	continue;
					//}
					scores[k] = scores[k].multiply(TWO);
				}
				
				int tmp = (i-firsti) * (lastj - firstj + 1) + (j - firstj);
				int i2 = firsti + (tmp % (lasti - firsti + 1));
				int j2 = firstj + (tmp / (lasti - firsti + 1));

				int i2rev = lasti - (tmp % (lasti - firsti + 1));
				int j2rev = lastj - (tmp / (lasti - firsti + 1));

				if(array[i][j]) {
					scores[0] = scores[0].add(BigInteger.ONE);
				}
				
				if(array[i][jrev]) {
					scores[1] = scores[1].add(BigInteger.ONE);
				}
				
				if(array[irev][j]) {
					scores[2] = scores[2].add(BigInteger.ONE);
				}
				
				if(array[irev][jrev]) {
					scores[3] = scores[3].add(BigInteger.ONE);
				}

				
				if(array[i2][j2]) {
					scores[4] = scores[4].add(BigInteger.ONE);
				}
				
				if(array[i2][j2rev]) {
					scores[5] = scores[5].add(BigInteger.ONE);
				}
				
				if(array[i2rev][j2]) {
					scores[6] = scores[6].add(BigInteger.ONE);
				}
				
				if(array[i2rev][j2rev]) {
					scores[7] = scores[7].add(BigInteger.ONE);
				}

				if(tmp > 0 && /* tmp % (lasti - firsti + 1) == 0*/
						i2 == lasti) {
					scores[4] = scores[4].multiply(vertFactor2);
					scores[5] = scores[5].multiply(vertFactor2);
					scores[6] = scores[6].multiply(vertFactor2);
					scores[7] = scores[7].multiply(vertFactor2);
					System.out.println("Vert factor 2");
				}
			}
			
			scores[0] = scores[0].multiply(sideFactor);
			scores[1] = scores[1].multiply(sideFactor);
			scores[2] = scores[2].multiply(sideFactor);
			scores[3] = scores[3].multiply(sideFactor);
			
		
			
		}
		scores[0] = scores[0].multiply(vertFactor);
		scores[1] = scores[1].multiply(vertFactor);
		scores[2] = scores[2].multiply(vertFactor);
		scores[3] = scores[3].multiply(vertFactor);
		

		scores[4] = scores[4].multiply(sideFactor2);
		scores[5] = scores[5].multiply(sideFactor2);
		scores[6] = scores[6].multiply(sideFactor2);
		scores[7] = scores[7].multiply(sideFactor2);

		
		//End update the side Factor and vertFactor:

		
		//Deal with symmetries by getting max scores from 8 possible symmetries:
		BigInteger max = BigInteger.ZERO;
		
		for(int i=0; i<scores.length; i++) {
			if(max.compareTo(scores[i]) < 0) {
				max = scores[i];
			}
		}
		
		SanityCheckScoresForUniqueness.sanityCheckScoresWithSimplerMethod(scores, array);
		
		if(! uniqList.contains(max)) {
			uniqList.add(max);
			
			//System.out.println("Max number: " + max);
			
			return true;
		} else {
			return false;
		}
	}
	
	
}
