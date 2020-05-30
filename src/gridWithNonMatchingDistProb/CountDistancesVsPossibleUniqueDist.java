package gridWithNonMatchingDistProb;

public class CountDistancesVsPossibleUniqueDist {

	/*
	 * 
	 * 
	 * //Looks like it's provably impossible when n>16...
	//But that doesn't rule out grids where length = 12, 13, 14, 15
	//I guess for those values of n, there's not that many unused distances,
	//and we could attack the search problem with that fact...
	 * 
	 * For example, 15 needs to use up all possible distances...
	 * I think I could prove that's impossible by pen and paper 
	 * 
Output 12 to 15:

Consider 12 by 12 grid:
There needs to be 66 unique distances
And there are 70 possible distances


Consider 13 by 13 grid:
There needs to be 78 unique distances
And there are 82 possible distances


Consider 14 by 14 grid:
There needs to be 91 unique distances
And there are 93 possible distances


Consider 15 by 15 grid:
There needs to be 105 unique distances
And there are 105 possible distances

The rest are impossible!


	 */
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for(int n=2; n<20; n++) {
			System.out.println("Consider " + n + " by " + n + " grid:");
			System.out.println("There needs to be " + numUniqueDistancesNeeded(n) + " unique distances");
			System.out.println("And there are " + bruteForceNumDistancesPossibleInNByNGrid(n) + " possible distances");
			if(bruteForceNumDistancesPossibleInNByNGrid(n) < numUniqueDistancesNeeded(n)) {
				System.out.println("That's impossible!");
				System.out.println("*****************");
			} else {
				if(n > 15) {
					System.out.println("Cool! I found an exception!");
					System.exit(1);
				}
				
			}
			
			System.out.println();
			System.out.println();
		}

	}
	
	//TODO: use 3 blue 1 brown video to do better and come up with a proof that n > 16 is impossible.
	//( https://www.youtube.com/watch?v=NaL_Cb42WyY )
	public static int bruteForceNumDistancesPossibleInNByNGrid(int n) {
		
		boolean taken[] = new boolean[2*n*n + 1];
		for(int i=1; i<n; i++) {
			for(int j=0; j<=i; j++) {
				taken[i*i + j*j] = true;
			}
		}

		int ret = 0;
		
		for(int i=0; i<taken.length; i++) {
			if(taken[i]) {
				ret++;
			}
		}
		
		return ret;
	}
	
	public static int numUniqueDistancesNeeded(int n) {
		return (n*(n-1))/2;
	}

}
