package triangleBoard5;

import eulerBook.UtilityFunctions;

public class TriangleLookup {

	//TODO: for larger triangles, this lookup could break, so be careful
	
	public static long convertToNumberBad(boolean triangle[][], int numPiecesLeft) {
		
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

	
	public static long pascalsTriangle[][] = UtilityFunctions.createPascalTriangle(100);

	//this is an algo that get the lookup number based on which combination is being used:
	public static long convertToNumberWithComboTricks(boolean triangle[][]) {
		long ret = 0;
		
		int curIndex=0;
		int numFocusedFound = 0;
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					
					numFocusedFound++;
					if(curIndex >= numFocusedFound) {
						ret += pascalsTriangle[curIndex][numFocusedFound];
					}
				}
				curIndex++;
			}
		}
		
		//I don't want to take advantage of symmetries all of the time, but I could do it at the beginning at least:
		if(numFocusedFound > TriangleSolveOptimizedTrial.getTriangleNumber(triangle.length) - 7) {
			ret = Math.min(ret, getComboNumAfterReflection(triangle));
		}
		
		return ret;
	}
	
	public static int getComboNumAfterReflection(boolean triangle[][]) {
		//TODO: reflection!
		int ret2 = 0;
		int curIndex=0;
		int numFocusedFound = 0;
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][triangle[i].length - j - 1]) {
					
					numFocusedFound++;
					if(curIndex >= numFocusedFound) {
						ret2 += pascalsTriangle[curIndex][numFocusedFound];
					}
				}
				curIndex++;
			}
		}
		
		return ret2;
	}
	
}
