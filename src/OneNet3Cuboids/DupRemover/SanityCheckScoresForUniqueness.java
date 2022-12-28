package OneNet3Cuboids.DupRemover;

import java.math.BigInteger;

import OneNet3Cuboids.OldReferenceFoldingAlgosNby1by1.FoldResolver1;

public class SanityCheckScoresForUniqueness {


	public static int NUM_REFLECTIONS = 2;
	public static int NUM_ROTATIONS = 4;

	public static void sanityCheckScoresWithSimplerMethod(BigInteger scores[], boolean array[][]) {
		
		BigInteger simplerScores[] = new BigInteger[NUM_REFLECTIONS * NUM_ROTATIONS];
		
		for(int i=0; i<NUM_REFLECTIONS; i++) {
			for(int j=0; j<NUM_ROTATIONS; j++) {
				boolean tempArray[][] = hardCopy(array);
				if(i==1) {
					tempArray = reflectIt(tempArray);
				} else {
					//Do nothing
				}
				
				for(int k=0; k<=j; k++) {
					tempArray = rotate90clockwise(tempArray);
				}
				
				simplerScores[i*NUM_ROTATIONS + j] = getScore(tempArray);
			}
		}
		
		/*for(int i=0; i<scores.length; i++) {
			for(int j=i+1; j<scores.length; j++) {
				if(scores[i].compareTo(scores[j]) == 0) {
					System.out.println("There's a symmetry in scores! ");
				}
			}
		}
		
		for(int i=0; i<simplerScores.length; i++) {
			for(int j=i+1; j<simplerScores.length; j++) {
				if(simplerScores[i].compareTo(simplerScores[j]) == 0) {
					System.out.println("There's a symmetry in simpler scores! ");
				}
			}
		}*/
		
		if( ! sameScoresDiffOrder(scores, simplerScores)) {
			System.out.println("The scores might have a problem!");
			
			System.exit(1);
		}
		
		
	}
	
	private static BigInteger getScore(boolean arrayOrig[][]) {
		
		boolean curArray[][] = arrayOrig;
		
		int numCouldLift = 0;
		
		//Lift up:
		CHECK_LIFT:
		for(int i=0; i<curArray.length; i++) {
			for(int j=0; j<curArray[0].length; j++) {
				if(curArray[i][j]) {
					
					break CHECK_LIFT;
				}
			}
			
			numCouldLift++;
		}
		
		boolean curArray2[][] = new boolean[curArray.length][curArray[0].length];
		for(int i=numCouldLift; i<curArray.length; i++) {
			for(int j=0; j<curArray[0].length; j++) {
				curArray2[i - numCouldLift][j] = curArray[i][j];
			}
		}
		
		curArray = curArray2;
		
		//Adjust left:
		int numAdjustLeft = 0;
		CHECK_LEFT:
		for(int j=0; j<curArray[0].length; j++) {
			for(int i=0; i<curArray.length; i++) {
				if(curArray[i][j]) {
					
					break CHECK_LEFT;
				}
			}
			
			numAdjustLeft++;
		}

		curArray2 = new boolean[curArray.length][curArray[0].length];
		for(int j=numAdjustLeft; j<curArray[0].length; j++) {
			for(int i=0; i<curArray.length; i++) {
				curArray2[i][j - numAdjustLeft] = curArray[i][j];
			}
		}
		curArray = curArray2;
		
		//System.out.println("Print simple adjusted array");
		//FoldResolver1.printFold(curArray);
		
		BigInteger score = BigInteger.ZERO;
		BigInteger TWO = new BigInteger("2");
		
		for(int i=0; i<curArray.length; i++) {
			for(int j=0; j<curArray[0].length; j++) {
				
				score = score.multiply(TWO);

				if(curArray[i][j]) {
					score = score.add(BigInteger.ONE);
				}
				
				
			}
			
		}
		
		
		return score;
	}
	
	private static boolean[][] reflectIt(boolean array[][]) {
		
		boolean newArray[][] = new boolean[array.length][array[0].length];
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				newArray[i][j] = array[i][array[0].length - 1 - j];
			}
		}
		
		return newArray;
	}
	

	private static boolean[][] rotate90clockwise(boolean array[][]) {
		
		boolean newArray[][] = new boolean[array[0].length][array.length];
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				newArray[j][array.length - 1 - i] = array[i][j];
			}
		}
		
		return newArray;
	}
	
	private static boolean sameScoresDiffOrder(BigInteger a[], BigInteger b[]) {

		boolean taken[] = new boolean[a.length];
		
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<b.length; j++) {
				if(taken[j] == false && a[i].compareTo(b[j]) == 0) {
					//System.out.println("Test: " + i + " is  good");
					taken[j] = true;
					break;
				}
			}
		}
		
		for(int j=0; j<b.length; j++) {
			if(taken[j] == false) {
				System.out.println("ERROR: mismatch between ways of getting scores!");
				for(int k=0; k<b.length; k++) {
					System.out.println("a: " + a[k]);
					System.out.println("b: " + b[k]);
					System.out.println();
					System.out.println();
				}
				return false;
			}
		}
		
		return true;
	}
	

	
	private static boolean[][] hardCopy(boolean array[][]) {
		boolean newArray[][] = new boolean[array.length][array[0].length];
		
		for(int i=0; i<newArray.length; i++) {
			for(int j=0; j<newArray[0].length; j++) {
				newArray[i][j] = array[i][j];
			}
		}
		
		return newArray;
	}
}
