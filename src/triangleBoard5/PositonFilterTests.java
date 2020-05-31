package triangleBoard5;

import java.util.ArrayList;
import java.util.HashSet;

import utils.graph.GraphEdge;

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
	
	public static boolean[][] getStartsThatReduceNumMesonRegions(boolean triangle[][]) {
		
		boolean goodStarts[][] = new boolean[triangle.length][];
		
		//initialize: (optionalish)
		for(int i=0; i<goodStarts.length; i++) {
			goodStarts[i] = new boolean[triangle[i].length];
			for(int j=0; j<triangle[i].length; j++) {
				goodStarts[i][j] = false;
			}
		}
		
		
		int curNumMesonRegions = 0;
		
		//Handle corners
		//top:
		if(triangle[0][0]) {
			curNumMesonRegions++;
			goodStarts[0][0] = true;
		}
		//bottom left:
		if(triangle[triangle.length - 1][0]) {
			curNumMesonRegions++;
			goodStarts[triangle.length - 1][0] = true;
		}
		//bottom right:
		if(triangle[triangle.length - 1][triangle.length - 1]) {
			curNumMesonRegions++;
			goodStarts[triangle.length - 1][triangle.length - 1] = true;
		}
		
		//Handle sides:
		//left side
		for(int i=2; i<triangle.length - 1; i++) {
			if(triangle[i][0] && triangle[i-1][0]) {
				goodStarts[i][0] = true;
				goodStarts[i-1][0] = true;
				
				curNumMesonRegions++;
				i++;
			}
		}
		
		//right side
		for(int i=2; i<triangle.length - 1; i++) {
			if(triangle[i][i] && triangle[i-1][i-1]) {
				goodStarts[i][i] = true;
				goodStarts[i-1][i-1] = true;
				
				curNumMesonRegions++;
				i++;
			}
		}
		
		//bottom side
		for(int j=2; j<triangle.length - 1; j++) {
			if(triangle[triangle.length - 1][j] && triangle[triangle.length - 1][j-1]) {
				goodStarts[triangle.length - 1][j] = true;
				goodStarts[triangle.length - 1][j-1] = true;

				curNumMesonRegions++;
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
					
					goodStarts[i-1][j-1]  = true;
					goodStarts[i-1][j] = true;
					goodStarts[i][j-1] = true;
					goodStarts[i][j] = true;
					goodStarts[i][j+1] = true;
					goodStarts[i+1][j] = true;
					goodStarts[i+1][j+1] = true;
					

					curNumMesonRegions++;
					j += 3;
					rowFoundHex = true;
				}
			}
			
			if(rowFoundHex) {
				i+= 3;
			}
		}
		
		
		for(int i=1; i<goodStarts.length; i++) {
			for(int j=0; j<goodStarts[i].length; j++) {
				
				if(goodStarts[i][j]) {
					//SANITY TEST
					if(triangle[i][j] == false) {
						System.out.println("ERROR: empty space is part of meson region");
						System.exit(1);
					}
					//END SANITY TEST
					
					//WARNING: I don't like changing objects at some random spot: (oh well)
					triangle[i][j] = false;
					
					//If removing peg doesn't change # of meson regions,
					// don't start with that peg:
					if(getNumMesonRegionsSimple(triangle) == curNumMesonRegions) {
						goodStarts[i][j] = false;
					}

					//At least I change it back...
					triangle[i][j] = true;
				}
			}
		}
		
		return goodStarts;
		
	}
	
	//TODO: refine GetMinMovesBasedOnIsolation with min spanning tree
	//(Reuse Advent of code's GraphUtils and GraphEdge) 
	public static int getMinMovesBasedOnIsolationSpanningTree(boolean triangle[][], int countOfPegs) {
		int adjacencyMatrix[][] = createAdjacencyMatrix(triangle, countOfPegs);
		
		//TODO: don't use array list when you know the size of the Array!
		ArrayList <Comparable> edges = new ArrayList<Comparable>((countOfPegs * countOfPegs - 1)/2);
		//TODO: make sure to use insestion sort when sorting these small arrays!
		
		for(int i=0; i<adjacencyMatrix.length; i++) {
			for(int j=i+1; j<adjacencyMatrix.length; j++) {
				edges.add(new GraphEdge(i, j, adjacencyMatrix[i][j]));
			}
		}
		
		
		
		return getMinNumMovesBasedOnSpanningTree(countOfPegs, edges);
	}
	

	public static int getMinNumMovesBasedOnSpanningTree(int numVertices, ArrayList<Comparable> edges) {
		Object sortedEdges[] = utils.graph.Sort.sortList(edges);
		
		boolean taken[] = new boolean[numVertices];
		
		int numEdgesNeeded = taken.length - 1;
		
		int numEdgesUsed = 0;
		
		//System.out.println();
		//System.out.println();
		//System.out.println("TEST START FUNC");
		boolean connections[][] = new boolean[taken.length][taken.length];
		for(int i=0; i<connections.length; i++) {
			for(int j=0; j<connections[0].length; j++) {
				connections[i][j] = (i == j);
			}
		}
		
		int retNumEdgesGreaterThan1 = 0;
		int retWeightEdgesOver1 = 0;
		
		for(int i=0; numEdgesUsed < numEdgesNeeded; i++) {
			GraphEdge tmp = (GraphEdge)sortedEdges[i];
			if(connections[tmp.getI()][tmp.getJ()] == false) {

				if(tmp.getWeigth() >= 2) {
					retNumEdgesGreaterThan1++;
					retWeightEdgesOver1 += (tmp.getWeigth() - 1);
					//System.out.println("TEST found " + tmp.getWeigth());
					//System.out.println("From " + tmp.getI() + " to " + tmp.getJ());
				}

				//System.out.println(tmp.getI() + ", " + tmp.getJ());
				//System.out.println("Main: " + tmp.getI() + " connects to " + tmp.getJ());
				//System.out.println("Main: " + tmp.getJ() + " connects to " + tmp.getI());
				//System.out.println("---");
				connections[tmp.getI()][tmp.getJ()] = true;
				connections[tmp.getJ()][tmp.getI()] = true;
				
			
				for(int x=0; x<connections.length; x++) {
					if(connections[x][tmp.getI()] == true && connections[x][tmp.getJ()] == false) {
						//connections[tmp.getJ()][x] = true;
						//connections[x][tmp.getJ()] = true;

						for(int y=0; y<connections.length; y++) {
							if(connections[y][tmp.getJ()] == true && connections[y][x] == false) {
								connections[y][x] = true;
								connections[x][y] = true;
								//System.out.println(y + "connects to " + x);
								//System.out.println(x + "connects to " + y);
							}
						}
					} else if(connections[x][tmp.getJ()] == true && connections[x][tmp.getI()] == false) {
						//connections[tmp.getI()][x] = true;
						//connections[x][tmp.getI()] = true;

						for(int y=0; y<connections.length; y++) {
							if(connections[y][tmp.getI()] == true && connections[y][x] == false) {
								connections[y][x] = true;
								connections[x][y] = true;
								//System.out.println(y + "connects to " + x);
								//System.out.println(x + "connects to " + y);
							}
						}
					}
				}
					
				//System.out.println("---");
				
				if(taken[tmp.getI()] == false) {
					taken[tmp.getI()] = true;
					
				}
				
				if(taken[tmp.getJ()] == false) {
					taken[tmp.getJ()] = true;
				}
	
				
				numEdgesUsed++;
			}
			
		}
		
		if(numEdgesUsed < numEdgesNeeded) {
			System.out.println("ERROR! 2");
			System.exit(1);
		}
		
		//System.out.println("Test taken");
		for(int i=0; i<taken.length; i++) {
			if(taken[i] == false) {
				System.out.println("ERROR! node " + i + " not taken");
				System.exit(1);
			}
		}
		
		if(retWeightEdgesOver1 > 0) {
			return retWeightEdgesOver1 - retNumEdgesGreaterThan1 + 1;
		} else {
			return 1;
		}
	}
	
	
	//Initial basic algo
	//Will be refined later!
	public static int GetMinMovesBasedOnIsolationSimple(boolean triangle[][], int countOfPegs) {
		int adjacencyMatrix[][] = createAdjacencyMatrix(triangle, countOfPegs);
		
		int currentMaxIsolation = 0;
		int minIsolationDist;
		for(int i=0; i<adjacencyMatrix.length; i++) {
			
			minIsolationDist = triangle.length;
			
			for(int j=0; j<adjacencyMatrix.length; j++) {
				if(i == j) {
					continue;
				}
				if(adjacencyMatrix[i][j] < minIsolationDist) {
					minIsolationDist = adjacencyMatrix[i][j];
				}
			}
			
			if(minIsolationDist > currentMaxIsolation) {
				currentMaxIsolation = minIsolationDist;
			}
		}
		
		int ret = 1 + Math.max(0, currentMaxIsolation - 2);
		
		return ret;
		
	}
	
	private static int[][] createAdjacencyMatrix(boolean triangle[][], int countOfPegs) {
		
		int coords[][] = new int[countOfPegs][2];
		
		int numPegsFound = 0;
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					coords[numPegsFound][0] = i;
					coords[numPegsFound][1] = j;
					numPegsFound++;
					
				}
			}
		}
		
		if(numPegsFound != countOfPegs) {
			System.out.println("ERROR: incorrect number of pegs found in createAdjacencyMatrix");
			System.exit(1);
		}
		
		int matrix[][] = new int[countOfPegs][countOfPegs];
		
		for(int i=0; i<coords.length; i++) {
			for(int j=i+1; j<coords.length; j++) {
				
				
				//It's at least deltaX
				int distance = getDistanceWithinTriangle(coords[i][0], coords[i][1], coords[j][0], coords[j][1]);
				
				matrix[i][j] = distance;
				matrix[j][i] = distance;
				
				
				
			}
		}
			
		return matrix;
	}
	
	private static int getDistanceWithinTriangle(int i1, int j1, int i2, int j2) {
		int deltaX = j2 - j1;
		int deltaY = i2 - i1;
		
		int diagMoves = 0;
		if((deltaX < 0 && deltaY <0)  || (deltaX>0 && deltaY>0)) {
			diagMoves = Math.min( Math.abs(deltaX), Math.abs(deltaY));
		}
		
		//Distance = manhantan distance - #diag moves
		//(1 diag move is like 2 manhattan distance moves)
		return Math.abs(deltaX) + Math.abs(deltaY) - diagMoves;
	}
	
	
	//Pre: each move in ArrayList<String> moves is valid on the TriangleBoard in input
	public static ArrayList<String> excludeMovesThatLeadToSameOutcome(TriangleBoard triangle, ArrayList<String> moves) {
		
		//Initialize to avoid resize:
		HashSet<Long> setOfPos = new HashSet<Long>(moves.size() * 2);
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i=0; i<moves.size(); i++) {
			
			TriangleBoard tmpMovedPos = triangle.doOneMove(moves.get(i));
			long lookup = TriangleLookup.convertToNumberWithComboTricksAndSymmetry(tmpMovedPos.getTriangle(), tmpMovedPos.getNumPiecesLeft());
			
			if(setOfPos.contains(lookup) == false) {
				ret.add(moves.get(i));
				setOfPos.add(lookup);
			}
		}
		
		return ret;
	}
}
