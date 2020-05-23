package triangleBoard5;

public class PositonFilterTests {

	public static void main(String[] args) {
		//Test inverse ratios
	
		System.out.println("formula 1: 1.0 = " + (getInverseGoldenRatio() + Math.pow(getInverseGoldenRatio(), 2)));
		TriangleBoard board = new TriangleBoard(4);

		board.removePiece(0);
		board.removePiece(4);
		board.removePiece(5);
		board.removePiece(8);
		board.removePiece(9);
		board.removePiece(10);
		board.removePiece(13);
		board.removePiece(15);
		board.draw();
		
		
		printDistFromEverySpace(getDistFromEverySpace(board.getTriangle()));
		
		BackwardsTriangleBoard board2 = new BackwardsTriangleBoard(4);
		board2.addPiece(9);
		board2.addPiece(10);
		board2.draw();
		
		
		printDistFromEverySpace(getDistFromEverySpace(board2.getTriangle()));
		
		

		BackwardsTriangleBoard board3 = new BackwardsTriangleBoard(7);
		board3.addPiece(24);
		board3.draw();
		
		
		printDistFromEverySpace(getDistFromEverySpace(board3.getTriangle()));
		

		BackwardsTriangleBoard board4 = new BackwardsTriangleBoard(7);
		board4.addPiece(30);
		board4.draw();
		
		
		printDistFromEverySpace(getDistFromEverySpace(board4.getTriangle()));
		
		

		BackwardsTriangleBoard board5 = new BackwardsTriangleBoard(4);
		board5.addPiece(9);
		board5.draw();
		
		
		printDistFromEverySpace(getDistFromEverySpace(board5.getTriangle()));
	}

	
	public static double DIST_FACTOR = getInverseGoldenRatio();
	

	public static double getInverseGoldenRatio() {
		
		return 2.0/(1.0 + Math.sqrt(5));
	}
	
	public static int neighbours[][] = new int [][] {{-1, 0}, {-1, -1}, {0, -1}, {0, 1}, {1, 0}, {1, 1}};
	
	
	/*
	 * Filtered:
       G 
      _ G 
     _ _ _ 
    G _ _ _ 
   G _ _ G _ 
  G _ _ _ _ _ 
 G _ _ _ _ _ G 
Num pieces left: 8
Num moves Made: 10
Move list:   14-0  30-14  44-30  23-37  46-44-30  21-7-23-37-21  32-30  48-46  43-29-31-45-47-31  16-32-48
Lookup number: 2270488

                  2.35  
               2.38  2.47  
            2.44  2.33  2.06  
         3.00  2.47  2.24  2.00  
      3.20  2.71  2.29  2.33  1.82  
   3.00  2.56  2.15  1.94  1.76  1.56  
2.49  2.24  1.91  1.71  1.49  1.51  1.60  
Num filtered so far: 39564
	 */
	
	public static boolean isAnyPegUnCapturableOrUnmoveable(boolean triangle[][]) {
		
		//TODO: maybe the weight of every space could be calculated earlier?
		double weightEverySpace[][] = getDistFromEverySpace(triangle);
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					if(isPegUnCapturableOrUnmoveable(i, j, weightEverySpace, triangle)) {
						return true;
					}
				}
			}
		}
		
		return false;
		
	}
	
	public static boolean isPegUnCapturableOrUnmoveable(int iTarget, int jTarget, double weightEverySpace[][], boolean triangle[][]) {
		
		if(triangle[iTarget][jTarget] == false) {
			System.out.println("ERROR: There's no peg there");
			System.exit(1);
		}
		
		//Get neighbours:
		for(int n=0; n<neighbours.length; n++) {
			int iN = iTarget + neighbours[n][0];
			int jN = jTarget + neighbours[n][1];
			
			if(iN >=0 && jN>=0 &&
					iN < triangle.length && jN < triangle[iN].length &&
					1.001 * weightEverySpace[iN][jN] > 1.0 + DIST_FACTOR) {
				return false;
			}
			
		}
		
		return true;
	}
	
	public static double[][] getDistFromEverySpace(boolean triangle[][]) {
		
		double ret[][] = new double[triangle.length][];
		
		for(int i=0; i<triangle.length; i++) {
			ret[i] = new double[triangle[i].length];
			for(int j=0; j<triangle[i].length; j++) {
				
				ret[i][j] = getDistFromSpace(i, j, triangle);
				
			}
		}
		
		return ret;
	}
	
	public static void printDistFromEverySpace(double dists[][]) {
		for(int i=0; i<dists.length; i++) {

			for(int k=i+1; k<dists.length; k++) {
				System.out.print("   ");
			}

			for(int j=0; j<dists[i].length; j++) {
				System.out.printf("%.2f  ", dists[i][j]);
			}
			System.out.println();
		}
	}
	

	
	//FROM advent of code 2017, prod 11:
	/*//Distance between 2 hex coordinates:
	 *  //TODO: why is this true? A: no!
	 *  // not for my current coord system!
	 * int temp = 0;
		if(x > 0 && y < 0 || x<0 && y>0 ){
			temp = Math.min( Math.abs(x), Math.abs(y));
		}
		
		int a1 = Math.abs(x) +Math.abs(y) - temp;
		
		if(a1 > count) {
			count = a1;
		}
	 */
	
	public static double getDistFromSpace(int Itarget, int Jtarget, boolean triangle[][]) {
		
		double ret = 0.0;

		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					
					//It's at least deltaX
					int deltaX = Jtarget - j;
					int deltaY = Itarget - i;
					
					int diagMoves = 0;
					if((deltaX < 0 && deltaY <0)  || (deltaX>0 && deltaY>0)) {
						diagMoves = Math.min( Math.abs(deltaX), Math.abs(deltaY));
					}
					
					//Distance = manhantan distance - #diag moves
					//(1 diag move is like 2 manhattan distance moves)
					int a1 = Math.abs(deltaX) + Math.abs(deltaY) - diagMoves;
					
					ret += Math.pow(DIST_FACTOR, a1);
					
				}
			}
		}

		return ret;
	}
	
	public static int getNumMesonRegionsSimple(boolean triangle[][]) {
		int ret = 0;
		
		//Handle corners
		//top:
		if(triangle[0][0]) {
			ret++;
		}
		//bottom left:
		if(triangle[triangle.length - 1][0]) {
			ret++;
		}
		//bottom right:
		if(triangle[triangle.length - 1][triangle.length - 1]) {
			ret++;
		}
		
		//Handle sides:
		//left side
		for(int i=2; i<triangle.length - 1; i++) {
			if(triangle[i][0] && triangle[i-1][0]) {
				ret++;
				i++;
			}
		}
		
		//right side
		for(int i=2; i<triangle.length - 1; i++) {
			if(triangle[i][i] && triangle[i-1][i-1]) {
				ret++;
				i++;
			}
		}
		
		//bottom side
		for(int j=2; j<triangle.length - 1; j++) {
			if(triangle[triangle.length - 1][j] && triangle[triangle.length - 1][j-1]) {
				ret++;
				j++;
			}
		}
		
		//Handle hexagones in a very naive way:
		//It optimal for triangles length 8...
		for(int i=4; i<triangle.length-2; i++) {
			boolean rowFoundHex = false;
			
			for(int j=2; j<triangle[i].length - 2; j++) {
				
				if(          triangle[i-1][j-1]   &&    triangle[i-1][j]
					   && triangle[i][j-1] && triangle[i][j] && triangle[i][j+1]
							&& triangle[i+1][j]    &&   triangle[i+1][j+1]) {
					ret++;
					j += 3;
					rowFoundHex = true;
				}
			}
			
			if(rowFoundHex) {
				i+= 3;
			}
		}
		
		return ret;
		
	}
	
	
	//TODO: also count jumbo regions and play around with the hexagons to get max number
	
	//TODO: make algo that returns productive starts? (There's some starting locations that won't help)
}
