package OneNet3Cuboids;

public class DataModelViews {


	
	public static String getFlatNumberingView(int a, int b, int c, int numbering[][][]) {
		//1st row only has side 0:

		String ret ="";
		ret += "Map:\n";
		
		for(int i=0; i<c; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {

				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(j >= c && j<c + b) {

					int num = numbering[0][i][j - c];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else {
					ret += "    ";
				}
			}
			
			ret += "\n";
		}
		
		//2nd row has 4 sides:
		ret += "\n";

		for(int i=0; i<a; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {
				
				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				if( j < c) {
					
					int num = numbering[1][i][j];

					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else if(j >= c && j<c + b) {
					
					int num = numbering[2][i][j - c];
					
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else if(j >= c + b && j<2*c + b) {
					
					int num = numbering[3][i][j - c - b];
					
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else if(j >= 2*c + b && j<2*c + 2*b) {
					
					int num = numbering[4][i][j - 2*c - b];
					
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
				} else {
					System.out.println("Doh!");
					System.exit(1);
				}
			}
			
			ret += "\n";
		}
		

		//3rd row has 1 side:
		ret += "\n";
		
		for(int i=0; i<c; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {

				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(j >= c && j<c + b) {

					int num = numbering[5][i][j - c];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else {
					ret += "    ";
				}
			}
			
			ret += "\n";
		}
		
		//Bonus 4th row:
		ret += "\n";
		
		for(int i=0; i<a; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {

				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(j >= c && j<c + b) {

					//Mind bender: It gets flipped!
					// because it's a 3D cuboid or something!
					int num = numbering[4][(a-1) - (i)][(b-1) - (j - c)];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else {
					ret += "    ";
				}
			}
			
			ret += "\n";
		}
				
		
		return ret;
		
	}
	
}
