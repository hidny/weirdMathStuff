package mpmpPascalsTriangleSingmaster;

public class solveMPMPPascalTrig {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long pascalsTriangle[][] = UtilityFunctions.UtilityFunctions.createPascalTriangle(127, 2);
		
		int hits = 0;
		int total = 0;
		
		for(int i=0; i<128; i++) {
			int row = 0;
			for(int j=0; j<=i; j++) {
				if(pascalsTriangle[i][j] % 2 == 1) {
					hits++;
					row++;
					sop("*");
				} else {
					sop(" ");
				}
				total++;
			}
			
			System.out.println();
			//System.out.println("For row " + i + ": " + row);
		}
		
		System.out.println("hits: " + hits);
		System.out.println("Total: " + total);
		
		System.out.println("Answer: " + ((100.0*hits) / (1.0* total)));
	}
	
	public static void sop(String input) {
		System.out.print(input);
	}
	

}
