package triangleBoard3old;

public class TriangleLookup {

	//TODO: for larger triangles, this lookup could break, so be careful
	
	public static long convertToNumber(boolean triangle[][], int numPiecesLeft) {
		
		//TODO: maybe take advantage of the 6 fold symmetries...
			//Look up will take x6 longer, but it will save x6 the space.
		
		//(rotational and reflective)
		
		int triangleNumber = (triangle.length * (triangle.length + 1)) / 2;
		
		boolean resultThatHappensMore = true;
		if(numPiecesLeft < triangleNumber/2) {
			resultThatHappensMore = false;
		}
		
		int numGoneWithoutFocusedFound = 0;
		
		long ret = 0;
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j] == resultThatHappensMore) {
					numGoneWithoutFocusedFound++;
				} else {
					ret = triangleNumber * ret + numGoneWithoutFocusedFound;
				}
			}
		}
		
		return ret;
	}
	
	//TODO
	/*
	public static long convertToNumberWithSymmetries(boolean triangle[][], int numPiecesLeft) {
		
	}*/
	
}
