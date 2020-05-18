package triangleBoardBckGoodToSolveTrig7;

import java.util.ArrayList;
import java.util.HashSet;

public class BackwardsTriangleBoard {
	//Only hard-copies allow
	
	public static void main(String args[]) {
		
		
		//TESTING code:
		BackwardsTriangleBoard backwardsBoard = new BackwardsTriangleBoard(4);
		
		backwardsBoard.addPiece(backwardsBoard.getCode(1, 1));
		backwardsBoard.draw();
		
		BackwardsTriangleBoard backwardsBoardTest = backwardsBoard.doOneBackwardsMove("15-5");
		
		backwardsBoardTest.draw();
		
		backwardsBoardTest = backwardsBoard.doOneBackwardsMove("5-13-15-5");
		
		backwardsBoardTest.draw();
		
		
		backwardsBoardTest = backwardsBoardTest.doOneBackwardsMove("12-14");
		backwardsBoardTest.draw();
		//END TESTING CODE
		
		System.out.println("new test");
		BackwardsTriangleBoard test2 = new BackwardsTriangleBoard(7);
		test2.addPiece(0);
		
		//TODO: TEST FAILS! Does it also fail going forward?
		//I think I'm taking incompatible shortcuts!
		//OMG: doOneBackwardsMove might not make an actual move
		test2 = test2.doOneBackwardsMove("14-0");
		test2 = test2.doOneBackwardsMove("28-14");
		test2 = test2.doOneBackwardsMove("23-7");
		
		test2.draw();
		
		
		System.out.println("TEST 3");
		//28-14 37-21-23-7 14-0
		//vs
		//37-21-23-7 28-14 14-0
		
		test2 = new BackwardsTriangleBoard(7);
		test2.addPiece(0);
		test2 = test2.doOneBackwardsMove("14-0");
		test2 = test2.doOneBackwardsMove("37-21-23-7");
		test2 = test2.doOneBackwardsMove("28-14");
		test2.draw();

		test2 = new BackwardsTriangleBoard(7);
		test2.addPiece(0);
		test2 = test2.doOneBackwardsMove("28-14-0");
		test2 = test2.doOneBackwardsMove("37-21-23-7");
		test2.draw();

		
	}
	
	private boolean triangle[][];
	private int numPiecesLeft;
	private int numBackwardsMovesMade;

	private String historicMoveList;
	private int internalLastJumpCodeForMultiJumpMoves = -1;
	
	public BackwardsTriangleBoard(int length) {
		triangle = new boolean[length][];
		for(int i=0; i<length; i++) {
			triangle[i] = new boolean[i+1];
		}

		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				triangle[i][j] = false;
			}
		}
		
		numBackwardsMovesMade = 0;
		
		numPiecesLeft = 0;
		
		historicMoveList ="";
		
	}
	
	public void draw() {
		for(int i=0; i<triangle.length; i++) {
			for(int k=i; k<triangle.length; k++) {
				System.out.print(" ");
			}
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					System.out.print("G ");
				} else {
					System.out.print("_ ");
				}
			}
			System.out.println();
		}

		System.out.println("Num pieces left: " + numPiecesLeft);
		System.out.println("Num backwards moves Made: " + numBackwardsMovesMade);
		System.out.println("Move list: " + historicMoveList);
		System.out.println("Lookup number: " + this.getLookupNumber());
		System.out.println();
	}
	
	public String toString() {
		String ret = "";
		for(int i=0; i<triangle.length; i++) {
			for(int k=i; k<triangle.length; k++) {
				ret += " ";
			}
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					ret += "G ";
				} else {
					ret += "_ ";
				}
			}
			ret += "\n";
		}

		ret += "Num pieces left: " + numPiecesLeft + "\n";
		ret += "Num backwards moves Made: " + numBackwardsMovesMade + "\n";
		ret += "Move list: " + historicMoveList + "\n";
		ret += "Lookup number: " + this.getLookupNumber() + "\n";
		ret += "\n";

		return ret;
	}
	
	public void removePiece(int code) {
		int i = code / triangle.length;
		int j = code % triangle.length;
		
		if(j <= i && triangle[i][j] == true) {
			triangle[i][j] = false;
		} else {
			if(j <= i && triangle[i][j] == false) {
				System.err.println("ERROR: trying to remove an illegal piece");
				System.exit(1);
			} else {
				System.err.println("WARNING: piece you're trying to remove is out of bounds");
			}
		}
		
		this.numPiecesLeft--;
		this.lastLookupNumberResult = -1;
	}
	
	public void addPiece(int code) {
		int i = code / triangle.length;
		int j = code % triangle.length;
		
		if(j <= i && triangle[i][j] == false) {
			triangle[i][j] = true;
		} else {
			if(j <= i && triangle[i][j] == true) {
				System.err.println("ERROR: trying to remove an illegal piece");
				System.exit(1);
			} else {
				System.err.println("WARNING: piece you're trying to remove is out of bounds");
			}
		}
		
		this.numPiecesLeft++;
		this.lastLookupNumberResult = -1;
	}

	private BackwardsTriangleBoard prevLocation = null;
	private HashSet<String> moveList = null;
	
	private static int TEST_DEBUG_PRINT = 0;
	private static int TEST_TOTAL_MOVES_FOUND = 0;
	private static int TEST_TOTAL_MOVES_NEEDED = 0;
	
	
	//This works, but isn't going to be used,
	//unless we do a forward save and backwards search:
	public ArrayList<String> getNecessaryFullBackwardsMovesToCheck() {
		
		ArrayList<String> fullList = getFullBackwardsMovesExcludingRepeatMoves();
		ArrayList<String> neededList = new ArrayList<String>();
		
		if(this.prevLocation == null) {
			neededList = fullList;
			
		} else {
			
			String moves[] = this.historicMoveList.split(" ");

			String prevJump = moves[0];

			for(int i=0; i<fullList.size(); i++) {
				
				boolean dontNeedToCheck = false;
				
				/*//TODO: put into TEST function
				//SANITY TEST
				if(this.prevLocation.moveList.contains(fullList.get(i))) {
					if(this.prevLocation.doOneBackwardsMove(fullList.get(i)).couldMoveBackwards(prevJump)
						!= this.prevLocation.doOneBackwardsMove(fullList.get(i)).getFullBackwardsMovesIncludingRepeatMoves().contains(prevJump)) {
						System.out.println("ERROR: couldMoveBackwards didn't work!");
						System.exit(1);
					}
				}
				//END SANITY TEST
				*/
				
				if(this.prevLocation.moveList.contains(fullList.get(i))
					&& this.prevLocation.doOneBackwardsMove(fullList.get(i)).couldMoveBackwards(prevJump) ) {

					//TESTcompareBoardsForTesting(this.prevLocation.doOneBackwardsMove(fullList.get(i)).doOneBackwardsMove(prevJump), this.doOneBackwardsMove(fullList.get(i)));
					
					if(this.getLookupNumber() > this.prevLocation.doOneBackwardsMove(fullList.get(i)).getLookupNumber()) {
						
						//Explanation:
						//Algo should have done current move (fullList.get(i)) first because
						//they are indep and current move would have gotten to a smaller lookup number had it gone first.
						//Unfortunately, I have to use an expensive lookupNumber because that's the only thing that uniquely identifies a position :(
						dontNeedToCheck = true;
					}
				}

				if(dontNeedToCheck == false) {
					neededList.add(fullList.get(i));
				}
			}
		}
		
		TEST_DEBUG_PRINT++;
		TEST_TOTAL_MOVES_FOUND += fullList.size();
		TEST_TOTAL_MOVES_NEEDED += neededList.size();
		
		if(TEST_DEBUG_PRINT % 100000 == 0) {
			System.out.println("Testing branching improvement: " + fullList.size() + " vs " + neededList.size());
			System.out.println("Ratio: " + ((1.0*TEST_TOTAL_MOVES_FOUND)/(1.0 * TEST_TOTAL_MOVES_NEEDED)));
			System.out.println("Perc: " + ((1.0*TEST_TOTAL_MOVES_NEEDED)/(1.0 * TEST_TOTAL_MOVES_FOUND)));
		}
		
		return neededList;
	}

	//WARNING: ONLY USE INTERNALLY
	private ArrayList<String> getFullBackwardsMovesIncludingRepeatMoves() {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					ret.addAll(getPossibleBackwardsMovesFromPosition(i * triangle.length + j));
				}
			}
		}
		
		moveList = new HashSet<String>();
		moveList.addAll(ret);
		return ret;
		
	}

	public ArrayList<String> getFullBackwardsMovesExcludingRepeatMoves() {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		
		int lastPegLocation;
		
		int lastPegLocationi;
		int lastPegLocationj;
		try {
			lastPegLocation = Integer.parseInt(this.historicMoveList.split("-")[0]);
			lastPegLocationi = lastPegLocation / triangle.length;
			lastPegLocationj = lastPegLocation % triangle.length;

		} catch(Exception e) {
			lastPegLocationi = -1;
			lastPegLocationj = -1;
		}
		
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					if(i != lastPegLocationi || j != lastPegLocationj ) {
						ret.addAll(getPossibleBackwardsMovesFromPosition(i * triangle.length + j));
					}
				}
			}
		}
		
		moveList = new HashSet<String>();
		moveList.addAll(ret);
		return ret;
		
	}
	
	public int getCode(int i, int j) {
		return i*triangle.length + j;
	}
	
	
	//Check if pos could do the move in input	
	//i.e: return true if there's no pegs in the way of the backwards move

	private boolean couldMoveBackwards(String backwardsMove) {
		
		String seriesOfJumps[] = backwardsMove.split("-");
		
		int finalLandingI = -1;
		int finalLandingJ = -1;
		
		
		for(int i=0; i<seriesOfJumps.length - 1; i++) {
			
			int from = Integer.parseInt(seriesOfJumps[seriesOfJumps.length - 2 - i]);
			int to = Integer.parseInt(seriesOfJumps[seriesOfJumps.length - 1 - i]);
			
			int fromi = from / triangle.length;
			int fromj = from % triangle.length;
			
			int toi = to / triangle.length;
			int toj = to % triangle.length;
			
			int betweeni = (fromi + toi)/2;
			int betweenj = (fromj + toj)/2;
			
			if(i == 0) {
				//Handle the fact that the final landing should have a peg after the move
				finalLandingI = toi;
				finalLandingJ = toj;
				
				if(triangle[finalLandingI][finalLandingJ] == false) {
					System.exit(1);
					//For some reason, this return statement never gets used, but I think it makes sense, so I'm keeping it:
					return false;
				}
			}
			
			if( triangle[betweeni][betweenj] == true 
				|| triangle[fromi][fromj] == true) {
				
				if(triangle[fromi][fromj] == true && finalLandingI == fromi && finalLandingJ == fromj) {
					//It's only ok for the from coord to have a peg if that is also the landing coord (i.e: the peg that doing the jumping)
				} else {
					//else not ok because that peg is in the way
					return false;
				}
			}
			
		}
		
		return true;
	}
	
	private ArrayList<String> getPossibleBackwardsMovesFromPosition(int code) {
		int iend = code / triangle.length;
		int jend = code % triangle.length;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		//There's 6 directions to check...
		//UP:
		if(iend >= 2 && jend <= iend-2) {
			if(triangle[iend-1][jend] == false && triangle[iend-2][jend] == false) {
				
				ret.addAll( getPossibleBackwardsMovesAfterBackJump(getCode(iend-2, jend) + "-" + getCode(iend, jend)) );
			}
		}
		
		//UP LEFT
		if(iend >=2 && jend >= 2) {
			if(triangle[iend-1][jend-1] == false && triangle[iend-2][jend-2] == false) {
				ret.addAll( getPossibleBackwardsMovesAfterBackJump(getCode(iend-2, jend-2) + "-" +  getCode(iend, jend)) );
			}
		}
		
		//RIGHT:
		if(jend + 2 < triangle[iend].length) {
			if(triangle[iend][jend+1] == false && triangle[iend][jend+2] == false) {
				ret.addAll( getPossibleBackwardsMovesAfterBackJump(getCode(iend, jend+2) + "-" + getCode(iend, jend)) );
			}
		}
		
		//LEFT:
		if(jend >=2) {
			if(triangle[iend][jend-1] == false && triangle[iend][jend-2] == false) {
				ret.addAll( getPossibleBackwardsMovesAfterBackJump(getCode(iend, jend-2) + "-" + getCode(iend, jend)) );
			}
		}
		
		//DOWN:
		if(iend + 2 < triangle.length) {
			if(triangle[iend+1][jend] == false && triangle[iend+2][jend] == false) {
				ret.addAll( getPossibleBackwardsMovesAfterBackJump(getCode(iend+2, jend) + "-" + getCode(iend, jend)) );
			}
		}
		
		//DOWN RIGHT
		if(iend + 2 < triangle.length) {
			if(triangle[iend+1][jend+1] == false && triangle[iend+2][jend+2] == false) {
				ret.addAll( getPossibleBackwardsMovesAfterBackJump(getCode(iend+2, jend+2) + "-" + getCode(iend, jend)) );
			}
		}
		
		return ret;
	}
	
	private ArrayList<String> getPossibleBackwardsMovesAfterBackJump(String jump) {
		ArrayList<String> ret = new ArrayList<String>();
		
		ret.add(jump);
		
		
		BackwardsTriangleBoard tmp = this.moveBackwardsInternal(jump);
		
		int jumpingPoint = Integer.parseInt(jump.split("-")[0]);
		ArrayList<String> newSeriesOfMoves = tmp.getPossibleBackwardsMovesFromPosition(jumpingPoint);
		for(int i=0; i<newSeriesOfMoves.size(); i++) {
			ret.add(newSeriesOfMoves.get(i) + "-" + jump.split("-")[1]);
		}
		
		return ret;
	}

	//WARNING: If you're moving the wrong peg, it won't count as an extra move
	public BackwardsTriangleBoard doOneBackwardsMove(String backwardsMove) {
		String seriesOfJumps[] = backwardsMove.split("-");
		
		BackwardsTriangleBoard newBoard = this;
		
		for(int i=0; i<seriesOfJumps.length - 1; i++) {
			
			int from = Integer.parseInt(seriesOfJumps[seriesOfJumps.length - 2 - i]);
			int to = Integer.parseInt(seriesOfJumps[seriesOfJumps.length - 1 - i]);
			
			newBoard = newBoard.moveBackwardsInternal(from + "-" + to);
		}
		
		if(newBoard == this) {
			System.out.println("ERROR blank move!");
			System.exit(1);
		}

		newBoard.prevLocation = this;
		
		return newBoard;
		
	}
	
	//pre: valid move
	private BackwardsTriangleBoard moveBackwardsInternal(String backwardsMove) {
		String fromTo[] = backwardsMove.split("-");
		
		int from = Integer.parseInt(fromTo[0]);
		int to = Integer.parseInt(fromTo[1]);
		
		int fromI = from / triangle.length;
		int fromJ = from % triangle.length;
		
		int toI = to / triangle.length;
		int toJ = to % triangle.length;
		
		BackwardsTriangleBoard newBoard = new BackwardsTriangleBoard(triangle.length);
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				newBoard.triangle[i][j] = triangle[i][j];
			}
		}
		
		if(newBoard.triangle[fromI][fromJ] == true) {
			
			System.out.println("ERROR backwards move 1");
			
			System.out.println(backwardsMove);
			System.out.println("From: " + fromI + " " + fromJ);
			System.out.println("To: " + toI + " " + toJ);
			this.draw();
			System.exit(1);
		}
		
		if(newBoard.triangle[(fromI+toI)/2][(fromJ+toJ)/2] == true) {
			System.out.println("ERROR backwards move 2");
			System.exit(1);
		}
		
		if(newBoard.triangle[toI][toJ] == false) {
			System.out.println("ERROR backwards move 3");
			System.exit(1);
		}
		
		newBoard.triangle[fromI][fromJ] = true;
		newBoard.triangle[(fromI+toI)/2][(fromJ+toJ)/2] = true;
		newBoard.triangle[toI][toJ] = false;
		
		newBoard.numPiecesLeft = this.numPiecesLeft + 1;
		
		newBoard.historicMoveList = this.historicMoveList;
		
		if(internalLastJumpCodeForMultiJumpMoves == to) {
			//not a new move
			newBoard.numBackwardsMovesMade = this.numBackwardsMovesMade;
			newBoard.historicMoveList  = from + "-" + newBoard.historicMoveList;
			
		} else {
			newBoard.numBackwardsMovesMade = this.numBackwardsMovesMade + 1;
			newBoard.historicMoveList =  backwardsMove + " " + newBoard.historicMoveList;
		}
		newBoard.internalLastJumpCodeForMultiJumpMoves = from;
		
		return newBoard;
	}


	public int getNumPiecesLeft() {
		return numPiecesLeft;
	}

	public int getNumMovesMade() {
		return numBackwardsMovesMade;
	}

	public String getHistoricMoveList() {
		return historicMoveList;
	}
	
	private long lastLookupNumberResult = -1;
	public long getLookupNumber() {
		if(lastLookupNumberResult == -1) {
			lastLookupNumberResult = TriangleLookup.convertToNumberWithComboTricksAndSymmetry(triangle, numPiecesLeft);
		}
		return lastLookupNumberResult;
	}
	
	public int length() {
		return triangle.length;
	}
	
	private static void TESTcompareBoardsForTesting(BackwardsTriangleBoard a, BackwardsTriangleBoard b) {
		if(a.length() != b.length()) {
			System.out.println("ERROR: not even the same length");
			System.exit(1);
		}
		if(a.getNumPiecesLeft() != b.getNumPiecesLeft()) {
			System.out.println("ERROR: num pieces left is wrong once!");
			System.exit(1);
		}
		
		for(int i=0; i<a.length(); i++) {
			for(int j=0; j<=i; j++) {
				if(a.triangle[i][j] != b.triangle[i][j]) {
					System.out.println("Huh. The triangles do not match...");
					System.out.println(a);
					System.out.println("vs");
					System.out.println(b);
					
					System.exit(1);
				}
			}
		}
	}
}