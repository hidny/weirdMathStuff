
public class parkerSquare {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int square[][] = {{2, 5, 8}, {10, 11, 13}, {4, 1, 7}};
		int mod=27;

		printSquare(square);
		printActualSquare(square);
		analyzeSquare(square, mod);

		System.out.println("Sum to 0");
		findSumToTarget(square, mod, 0);
		
		System.out.println("Multiply by 5:");
		
		for(int i=0; i<square.length; i++) {
			for(int j=0; j<square.length; j++) {
				square[i][j] = (5*square[i][j]) %27;
			}
			
		}
		printSquare(square);
		analyzeSquare(square, mod);
		

		System.out.println("Sum to 2");
		findSumToTarget(square, mod, 2);
		
		System.out.println("---");
		
		for(int i=0; i<27; i++) {
			int cube = (i*i*i) % 27;
			int xplus2 = (i + 2) %27;
			
			if(cube == xplus2) {
				System.out.println("works for " + i);
			}
			
		}
		System.out.println("---");
		for(int i=0; i<27; i++) {
			int sum = i*i + (i*i + 2*i)* (i*i + 2*i);
			int moded = sum % mod;
			
			if(moded == 2) {
				System.out.println("hello case 1 for i = " + i);
				System.out.println((i));
			}
		}
		System.out.println("---");
		System.out.println("---");
		for(int i=0; i<27; i++) {
			int sum = (i*i + 2)*(i*i + 2) + (i + 2)*(i + 2);
			int moded = sum % mod;
			
			if(moded == 2) {
				System.out.println("hello case 2 for i = " + i);
				System.out.println((i + 2));
			}
		}
		System.out.println("---");
		for(int i=0; i<27; i++) {
			int sum = (i + 1)*(i + 1) + (i*i + i)*(i*i + i);
			int moded = sum % mod;
			
			if(moded == 2) {
				System.out.println("hello case 3 for i = " + i);
				System.out.println((i + 1));
			}
		}
		
		
		printActualSquare(square);
	}
	
	public static void printSquare(int square[][]) {
		for(int i=0; i<square.length; i++) {
			for(int j=0; j<square.length; j++) {
				System.out.print(square[i][j] + ", ");
			}
			System.out.println();
			
		}
	}
	
	public static void printActualSquare(int square[][]) {
		for(int i=0; i<square.length; i++) {
			for(int j=0; j<square.length; j++) {
				System.out.print(((square[i][j]*square[i][j])%27) + ", ");
			}
			System.out.println();
			
		}
	}
	
	
	public static void findSumToTarget(int square[][], int mod, int target) {
		System.out.println("Lemma check:");
		for(int i=0; i<square.length * square.length; i++) {
			for(int j=i+1; j<square.length * square.length; j++) {
				
				int sum = (square[i/3][i%3] * square[i/3][i%3] + square[j/3][j%3] * square[j/3][j%3]) % mod;
				
				System.out.println(sum);
				if(sum == target) {
					System.out.println("Found sum to 2: (" + i + ", " + j + ")");
				}
			}
		}
	}
	
	public static void analyzeSquare(int square[][], int mod) {
		for(int i=0; i<27; i++) {
			System.out.println(i + " x 11 = " + ((11*i)%27) + " mod 27");
		}
		
		for(int i=0; i<3; i++) {
			int sum =0;
			for(int j=0; j<3; j++) {
				sum = (sum + square[i][j]*square[i][j]) % mod;
			}
			
			System.out.println(sum);
		}
		System.out.println("---");
		
		for(int i=0; i<3; i++) {
			int sum =0;
			for(int j=0; j<3; j++) {
				sum = (sum + square[j][i]*square[j][i]) % mod;
			}
			
			System.out.println(sum);
		}
		System.out.println("---");
		int sum =0;
		for(int i=0; i<3; i++) {
			sum = (sum + square[i][i]*square[i][i]) % mod;
		}
		System.out.println(sum);
		System.out.println("---");
		sum =0;
		for(int i=0; i<3; i++) {
			sum = (sum + square[i][3 - 1 - i]*square[i][3 - 1 - i]) % mod;
		}
		System.out.println(sum);
	}

}
/*
 * 
 * Friend found mistake in your Magic Square of Squares Paper
 * 
 * Hi O.M.Cain,
I'm not sure if you know this, but there's a mistake in your paper. Your paper 'disproves' the existence of a non-parker square with mod 27, but my friend "ZacWut" pointed out it exists. Here's the solution he found:
4 25 1019 13 716 1 22

I double checked that it works, and believe that there's a mistake in Corollary 5.3. If you haven't already done so, can you make an errata, or fix this somehow?
Thanks,
Michael Tardibuono
*/
