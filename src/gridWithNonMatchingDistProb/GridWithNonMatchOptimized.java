package gridWithNonMatchingDistProb;

public class GridWithNonMatchOptimized {

	//DONE: use burnside lemma to get unique without reflection or symmetry
	/* SUBMIT:
Found solution 3:
Board config:
---------------
|O__O_O|
|______|
|______|
|__O___|
|_O____|
|_O____|
|---------------
*/
	
	//DONE: get num symmetric solutions!
	
	//1: 1
	//2: 2
	//3: 5 (40)
	//4

	public static void main(String[] args) {
		for(int N=13; N<=13; N++) {
			getSolutionForLengthN(N);
		}
	}
	public static void getSolutionForLengthN(int N) { 
		//TODO: make this 10^6 X faster

		System.out.println("Trying with N = " + N);
		boolean array[][] = new boolean[N][N];
		
		boolean combo[]  = new boolean[N*N];
		
		boolean registeredDistances[];
		
		for(int i=0; i<combo.length; i++) {
			if(i < N) {
				combo[i] = true;
			} else {
				combo[i] = false;
			}
		}
		
		int numSolutionsFound = 0;
		
		//TODO sometimes a rotation matches a reflection, so for N > 3, each solution has 4 or 8 symmetric solutions.
		double numSolutionDiscountRotAndReflection = 0.0;
		
		
		while(combo != null) {
			
			registeredDistances = new boolean[2*N*N + 1];
			
			boolean curNoDupsFound = true;
			
			for(int i=0; i<registeredDistances.length; i++) {
				registeredDistances[i] = false;
			}
			
			int curNumContersFound = 0;
			int labelOfFirstBadCounter = -1;
			
			CHECK_NO_DUP_DIST:
			for(int i=0; i<combo.length; i++) {
				if(combo[i]) {
					curNumContersFound++;
					
					for(int j=0; j<i; j++) {
						if(combo[j]) {
							int x1 = j % N;
							int y1 = j / N;
							int x2 = i % N;
							int y2 = i / N;
							
							int distSquared = getSquareDist(y1, x1, y2, x2);
							
							if(registeredDistances[distSquared]) {
								curNoDupsFound = false;
								labelOfFirstBadCounter = curNumContersFound;
								break CHECK_NO_DUP_DIST;
							} else {
								registeredDistances[distSquared] = true;
							}
							
						}
					}
					
				}
				
			}
			
			
			
			if(curNoDupsFound) {
				System.out.println("Found solution " + numSolutionsFound + ":");
				print(combo);
				/*
				System.out.println("Check:");
				for(int i=0; i<combo.length; i++) {
					if(combo[i]) {
						
						for(int j=0; j<i; j++) {
							if(combo[j]) {
								int x1 = j % N;
								int y1 = j / N;
								int x2 = i % N;
								int y2 = i / N;
								
								int distSquared = getSquareDist(y1, x1, y2, x2);
								
								System.out.println(distSquared);
								
								
							}
						}
						
					}
					
				}*/
				
				int numOrbitals = getNumSymmetries(convertComboToTable(combo));
				System.out.println("Num orbitals: "  + numOrbitals);
				
				numSolutionsFound++;
				
				numSolutionDiscountRotAndReflection += 1.0 / (1.0 * numOrbitals);
			}
			
			

			if(labelOfFirstBadCounter != -1 && labelOfFirstBadCounter != N) {
				//move counter at index indexFirstBadCounter:
				
				boolean deleteRest = false;
				int numCountersRefound = 0;
				for(int i=0; i<combo.length; i++) {
					if(deleteRest) {
						combo[i] = false;
					}
					if(combo[i]) {
						numCountersRefound++;
						if(numCountersRefound == labelOfFirstBadCounter) {
							deleteRest = true;
						}
					}
				}
				
				for(int i=0; i<N-labelOfFirstBadCounter; i++) {
					combo[combo.length - 1 - i] = true;
				}
				
				
				//System.out.println("TEST" + labelOfFirstBadCounter);
				//System.out.println("TEST:");
				//print(combo);
			}
			combo = UtilityFunctions.UtilityFunctions.getNextCombination(combo);
		}
		
		System.out.println("Found " + numSolutionsFound + " in total");
		System.out.println("Found " + numSolutionDiscountRotAndReflection + " unique solutions");
		
	}
	
	public static int NUM_ROTATIONS = 4;
	public static int getNumSymmetries(boolean array[][]) {
		
		boolean tmpRot[][] = array;
		boolean tmpRotReflect[][];
		
		int numMatches = 0;
		
		for(int i=0; i<NUM_ROTATIONS; i++) {
			tmpRot = rotate90(tmpRot);
			
			if(matches(array, tmpRot)) {
				numMatches++;
			}
			
			tmpRotReflect = reflect(tmpRot);
			
			if(matches(array, tmpRotReflect)) {
				numMatches++;
			}
			
			
		}
		
		if( 8 % numMatches != 0) {
			System.out.println("ERROR: incorrect num symmetries");
			System.exit(1);
		}
		
		if(numMatches != 1 && array.length >2) {
			System.out.println("Symmetry when N > 2 !");
			System.exit(1);
		}
		
		return 8/numMatches;
	}
	
	public static boolean[][] rotate90(boolean array[][]) {
		boolean arrayRot[][] = new boolean[array.length][array[0].length];
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array.length; j++) {
				arrayRot[i][j] = array[j][array.length - 1 -i];
			}
		}
		return arrayRot;
	}
	

	public static boolean[][] reflect(boolean array[][]) {
		boolean arrayReflect[][] = new boolean[array.length][array[0].length];
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array.length; j++) {
				arrayReflect[i][j] = array[i][array.length - 1 - j];
			}
		}
		return arrayReflect;
	}
	
	public static boolean matches(boolean array1[][], boolean array2[][]) {
		for(int i=0; i<array1.length; i++) {
			for(int j=0; j<array2.length; j++) {
				if(array1[i][j] != array2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static void print(boolean array[]) {
		
		int N = (int)Math.round(Math.sqrt(array.length));
		System.out.println("Board config:");
		System.out.println("---------------");
		System.out.print("|");
		for(int i=0; i<array.length; i++) {
			if(array[i]) {
				System.out.print("O");
			} else {
				System.out.print("_");
			}
			if((i+1) % N == 0) {
				System.out.println("|");
				System.out.print("|");
			}
		}
		System.out.println("---------------");
	}

	
	public static void print(boolean array[][]) {
		System.out.println("Board config:");
		System.out.println("---------------");
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array.length; j++) {
				if(array[i][j]) {
					System.out.print("O");
				} else {
					System.out.print(" ");
				}
			}
			System.out.println("|");
			System.out.print("|");
		}
		System.out.println("---------------");
	}

	public static boolean[][] convertComboToTable(boolean combo[]) {
		int N = (int)Math.round(Math.sqrt(combo.length));
		boolean array[][] = new boolean[N][N];
		
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				array[i][j] = combo[i*N + j];
			}
		}
		return array;
	}
	
	public static int getSquareDist(int i, int j, int i2, int j2) {
		return (i-i2)*(i-i2) + (j-j2)*(j-j2);
	}
}
