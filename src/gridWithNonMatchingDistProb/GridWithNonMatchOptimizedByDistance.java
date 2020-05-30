package gridWithNonMatchingDistProb;

public class GridWithNonMatchOptimizedByDistance {

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
		getDistSquareAvailableDesc(16);
	}
	
	
	public static int getAmountOfDistancesWeCouldIgnore(int n) {
		return CountDistancesVsPossibleUniqueDist.bruteForceNumDistancesPossibleInNByNGrid(n)
				- CountDistancesVsPossibleUniqueDist.numUniqueDistancesNeeded(n);
	}
	
	
	public static int[] getDistSquareAvailableDesc(int n) {
		
		boolean distances[] = new boolean[2*n*n + 1];
		for(int i=1; i<n; i++) {
			for(int j=0; j<n; j++) {
				distances[i*i + j*j] = true;
			}
		}
		
		int numFound = 0;
		for(int i=0; i<distances.length; i++) {
			if(distances[i]) {
				numFound++;
			}
		}
		
		//Sanity check:
		if(numFound != CountDistancesVsPossibleUniqueDist.bruteForceNumDistancesPossibleInNByNGrid(n)) {
			System.out.println("ERROR: bruteForceNumDistancesPossibleInNByNGrid might be wrong");
			System.exit(1);
		}
		
		System.out.println("Number of distances found for n = " + n + ": " + numFound);
		
		int arrayDesc[] = new int[numFound];
		
		for(int i=0, j=0; i<arrayDesc.length; j++) {
			int tmpDistSquared = distances.length - 1 - j;

			if(distances[tmpDistSquared]) {
				arrayDesc[i] = tmpDistSquared;
				i++;
			}
		}
		
		if(arrayDesc[arrayDesc.length - 1 ] == 0) {
			System.out.println("ERROR: error setting up descending array");
		}
		
		System.out.println("Descending array of square distances available for n = " + n + ":");
		for(int i=0; i<arrayDesc.length; i++) {
			System.out.println(arrayDesc[i]);
		}
		
		
		return arrayDesc;
		
	}
	public static void getSolutionForLengthNByLength(int n) { 
		boolean board[][] = new boolean[n][n];
		//TODO: recursion
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				board[i][j] = false;
			}
		}
		
		
	}
	
}
