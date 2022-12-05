package OneNet3Cuboids;

import OneNet3Cuboids.Coord.Coord;

public class Utils {

	public static final int SIDES_CUBOID = 6;

	public static int[][][] getFlatNumberingOfCuboid(int a, int b, int c) {

		int temp_numbering[][][] = new int[SIDES_CUBOID][][];
		temp_numbering[0] = new int[c][b];
		temp_numbering[1] = new int[a][c];
		temp_numbering[2] = new int[a][b];
		temp_numbering[3] = new int[a][c];
		temp_numbering[4] = new int[a][b];
		temp_numbering[5] = new int[c][b];
		
		int currentNum = 0;
		
		
		for(int i=0; i<temp_numbering.length; i++) {
			for(int j=0; j<temp_numbering[i].length; j++) {
				for(int k=0; k<temp_numbering[i][j].length; k++) {
					temp_numbering[i][j][k] = currentNum;
					currentNum++;
				}
			}
		}
		if(currentNum != getTotalArea(a, b, c)) {
			System.out.println("Current num is not the total area. Something went wrong!");
			System.exit(1);
		}
		
		return temp_numbering;
	}
	
	public static Coord[] getFlatInverseNumberingOfCuboid(int temp_numbering[][][], int a, int b, int c) {
		
		Coord numberingInv[] = new Coord[getTotalArea(a, b, c)];
				
		for(int i=0; i<temp_numbering.length; i++) {
			for(int j=0; j<temp_numbering[i].length; j++) {
				for(int k=0; k<temp_numbering[i][j].length; k++) {

					int currentNum = temp_numbering[i][j][k];
					numberingInv[currentNum] = new Coord(i, j, k);
				}
			}
		}
		
		return numberingInv;
	}
	

	public static int getTotalArea(int a, int b, int c) {
		return 2*(a*b + a*c + b*c);
	}
}
