package triangleBoard5;

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
		
		//private boolean triangle[][];
		//private int lastJumpCode;
		//private int numPiecesLeft;
		//private int numMovesMade;

		System.out.println("Num pieces left: " + numPiecesLeft);
		System.out.println("Num backwards moves Made: " + numBackwardsMovesMade);
		System.out.println("Move list: " + historicMoveList);
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
	}

	private BackwardsTriangleBoard prevLocation = null;
	private HashSet<String> moveList = null;
	
	private static int TEST_DEBUG_PRINT = 0;
	private static int TESTtotalFull = 0;
	private static int TESTtotalNeeded = 0;
	
	
	public ArrayList<String> getNecessaryFullBackwardsMovesToCheck() {
		
		
		ArrayList<String> fullList = getFullBackwardsMoves();

		ArrayList<String> neededList = new ArrayList<String>();
		
		
		//Get previous 1st jump locations to give some idea of an order:
		ArrayList<Integer> prevJumpLocations = new ArrayList<Integer>();
		BackwardsTriangleBoard tmpBoard = this;
		while(tmpBoard.prevLocation != null) {
			tmpBoard = tmpBoard.prevLocation;
			String moves[] = tmpBoard.historicMoveList.split(" ");
			prevJumpLocations.add(Integer.parseInt(moves[0].split("-")[0]));

		}
		
		//Filter out moves whose 1st jumps are "out of order"
		//TODO: does this even work?
		for(int i=0; i<fullList.size(); i++) {
			
			boolean dontNeedToCheck = false;
			tmpBoard = this;
			int j=0;
			while(tmpBoard.prevLocation != null &&  tmpBoard.prevLocation.moveList.contains(fullList.get(i))) {
				
				if(Integer.parseInt(fullList.get(i).split("-")[0]) < prevJumpLocations.get(j)) {

					//Should have done prev move(s) first because they are indep and prev move starts jump at a smaller numbered location
					dontNeedToCheck = true;
					
					break;
				}
				tmpBoard = tmpBoard.prevLocation;
				j++;
				
			}
					
				
			if(dontNeedToCheck == false) {
				neededList.add(fullList.get(i));
			} else {
				//System.out.println("TESTING!");
				//System.out.println("Move that was cancelled: " + fullList.get(i));
			}
		}
		
		
		TEST_DEBUG_PRINT++;
		TESTtotalFull += fullList.size();
		TESTtotalNeeded += neededList.size();
		
		if(TEST_DEBUG_PRINT % 10000000 == 0) {
			System.out.println("Testing branching improvement: " + fullList.size() + " vs " + neededList.size());
			System.out.println("Ratio: " + ((1.0*TESTtotalFull)/(1.0 * TESTtotalNeeded)));
			System.out.println("Perc: " + ((1.0*TESTtotalNeeded)/(1.0 * TESTtotalFull)));
		}
		
		return neededList;
	}
//TODO: END FIX ME

	public ArrayList<String> getFullBackwardsMoves() {
		
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
	
	public int getCode(int i, int j) {
		return i*triangle.length + j;
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
			//System.out.println(backwardsMove);
			//System.out.println(fromI + " " + fromJ);
			//this.draw();
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
	
	public long getLookupNumber() {
		return TriangleLookup.convertToNumberWithComboTricks(triangle);
	}
	
	public int length() {
		return triangle.length;
	}
}

/* From stackoverflow
This is possible with the menu items Window>Editor>Toggle Split Editor.

Current shortcut for splitting is:

Azerty keyboard:

Ctrl + _ for split horizontally, and
Ctrl + { for split vertically.
Qwerty US keyboard:

Ctrl + Shift + - (accessing _) for split horizontally, and
Ctrl + Shift + [ (accessing {) for split vertically.
MacOS - Qwerty US keyboard:

⌘ + Shift + - (accessing _) for split horizontally, and
⌘ + Shift + [ (accessing {) for split vertically.
On any other keyboard if a required key is unavailable (like { on a german Qwertz keyboard), the following generic approach may work:

Alt + ASCII code + Ctrl then release Alt
*/