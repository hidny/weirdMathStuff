package triangleBoard;

public class TriangleLookup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	public static long convertToNumberAssumeCloseToStart(boolean triangle[][]) {
		
		//TODO: maybe take advantage of the 6 fold symmetries...
			//Look up will take x6 longer, but it will save x6 the space.
		
		//(rotational and reflective)
		
		int triangleNumber = (triangle.length * (triangle.length + 1)) / 2;
		
		int numGoneWithoutEmptyFound = 0;
		
		long ret = 0;
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j] == true) {
					numGoneWithoutEmptyFound++;
				} else {
					ret = triangleNumber * ret + numGoneWithoutEmptyFound;
				}
			}
		}
		
		return ret;
	}
	
}
