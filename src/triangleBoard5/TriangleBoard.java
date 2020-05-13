package triangleBoard5;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

//Hard limit on number of records:
//20344823
public class TriangleBoard {
	//Only hard-copies allow
	
	public static void main(String args[]) {
		//TESTING code:
		TriangleBoard board = new TriangleBoard(4);

		board.removePiece(0);
		board.removePiece(13);
		board.removePiece(15);
		board.draw();
		
		
		ArrayList<String> moves = board.getFullMovesExcludingRepeatMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(14);
		board.removePiece(15);
		board.draw();
		
		moves = board.getFullMovesExcludingRepeatMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(12);
		board.removePiece(15);
		board.draw();
		

		System.out.println("***");
		System.out.println("***");
		System.out.println("TESTING AFTER 1 move:");
		moves = board.getFullMovesExcludingRepeatMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.doOneMove(moves.get(i));
			tmp.draw();
		}

		System.out.println("***");
		System.out.println("***");
		System.out.println("Try a double loop:");
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.doOneMove(moves.get(i));
			tmp.draw();
			
			ArrayList <String> moves2 = tmp.getFullMovesExcludingRepeatMoves();

			for(int j=0; j<moves2.size(); j++) {
				TriangleBoard tmp2 = tmp.doOneMove(moves2.get(j));
				tmp2.draw();
				
				System.out.println("Lookup number:");
				System.out.println(tmp2.getLookupNumber());
			}
			
			
		}
		
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(8);
		board.removePiece(15);
		
		moves = board.getFullMovesExcludingRepeatMoves();

		System.out.println("***");
		System.out.println("***");
		System.out.println("Try a double loop again:");
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.doOneMove(moves.get(i));
			tmp.draw();
			
			ArrayList <String> moves2 = tmp.getFullMovesExcludingRepeatMoves();

			for(int j=0; j<moves2.size(); j++) {
				TriangleBoard tmp2 = tmp.doOneMove(moves2.get(j));
				tmp2.draw();
			}
			
		}
		//END TESTING CODE
	}
	
	private boolean triangle[][];
	private int numPiecesLeft;
	private int numMovesMade;

	private String historicMoveList;
	private int internalLastJumpCodeForMultiJumpMoves = -1;
	
	public TriangleBoard(int length) {
		triangle = new boolean[length][];
		for(int i=0; i<length; i++) {
			triangle[i] = new boolean[i+1];
		}

		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				triangle[i][j] = true;
			}
		}
		
		numMovesMade = 0;
		
		numPiecesLeft = ((triangle.length + 1) * (triangle.length))/2;
		
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
		System.out.println("Num moves Made: " + numMovesMade);
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
		ret += "Num moves Made: " + this.numMovesMade + "\n";
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
	

	private TriangleBoard prevLocation = null;
	private HashSet<String> moveList = null;
	
	private static int TEST_DEBUG_PRINT = 0;
	private static int TEST_TOTAL_MOVES_FOUND = 0;
	private static int TEST_TOTAL_MOVES_NEEDED = 0;
	
//This works, but isn't going to be used,
	//unless we do a forward save and backwards search:
	public ArrayList<String> getNecessaryFullBackwardsMovesToCheck() {
		
		ArrayList<String> fullList = getFullMovesExcludingRepeatMoves();
		ArrayList<String> neededList = new ArrayList<String>();
		
		if(this.prevLocation == null) {
			neededList = fullList;
			
		} else {
			
			String moves[] = this.historicMoveList.split(" ");

			String prevJump = moves[moves.length - 1];

			for(int i=0; i<fullList.size(); i++) {
				
				boolean dontNeedToCheck = false;
				
				if(this.prevLocation.moveList.contains(fullList.get(i))
					&& this.prevLocation.doOneMove(fullList.get(i)).couldMoveForwards(prevJump) ) {

					TESTcompareBoardsForTesting(this.prevLocation.doOneMove(fullList.get(i)).doOneMove(prevJump), this.doOneMove(fullList.get(i)));
					
					if(this.getLookupNumber() > this.prevLocation.doOneMove(fullList.get(i)).getLookupNumber()) {
						
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
			System.out.println("Testing branching improvement for forwards jumps: " + fullList.size() + " vs " + neededList.size());
			System.out.println("Ratio: " + ((1.0*TEST_TOTAL_MOVES_FOUND)/(1.0 * TEST_TOTAL_MOVES_NEEDED)));
			System.out.println("Perc: " + ((1.0*TEST_TOTAL_MOVES_NEEDED)/(1.0 * TEST_TOTAL_MOVES_FOUND)));
		}
		
		return neededList;
	}

	//TODO: maybe use this internally?
	private ArrayList<String> getFullMovesIncludingRepeatMoves() {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					ret.addAll(getPossibleMovesFromPosition(i * triangle.length + j));
				}
			}
		}
		
		moveList = new HashSet<String>();
		moveList.addAll(ret);
		return ret;
		
	}
	
	
	public ArrayList<String> getFullMovesExcludingRepeatMoves() {
		
		String moves[] = this.historicMoveList.split(" ");
		
		int lastPegLocation;
		int lastPegLocationi;
		int lastPegLocationj;
		try {
			lastPegLocation = Integer.parseInt(this.historicMoveList.substring(this.historicMoveList.lastIndexOf("-") + 1));
			lastPegLocationi = lastPegLocation / triangle.length;
			lastPegLocationj = lastPegLocation % triangle.length;

		} catch(Exception e) {
			lastPegLocationi = -1;
			lastPegLocationj = -1;
		}
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					if(i != lastPegLocationi || j != lastPegLocationj ) {
						ret.addAll(getPossibleMovesFromPosition(i * triangle.length + j));
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

	private boolean couldMoveForwards(String forwardsMove) {
		
		String seriesOfJumps[] = forwardsMove.split("-");
		
		int startingI = -1;
		int startingJ = -1;
		
		
		for(int i=0; i<seriesOfJumps.length - 1; i++) {
			
			int from = Integer.parseInt(seriesOfJumps[i]);
			int to = Integer.parseInt(seriesOfJumps[i+1]);

			int fromi = from / triangle.length;
			int fromj = from % triangle.length;
			
			int toi = to / triangle.length;
			int toj = to % triangle.length;
			
			int betweeni = (fromi + toi)/2;
			int betweenj = (fromj + toj)/2;
			
			if(i == 0) {
				//Handle the fact that the peg should at the original position:
				startingI = fromi;
				startingJ = fromj;
				
				if(triangle[startingI][startingJ] == false) {
					return false;
				}
			}
			
			if( triangle[betweeni][betweenj] == false 
				|| triangle[toi][toj] == true) {
				
				if(triangle[toi][toj] == true && startingI == toi && startingJ == toj) {
					//It's only ok for the "to" coord to have a peg if that is also the starting coord (i.e: the peg that's doing the jumping)
				} else {
					//else not ok because that peg is in the way
					return false;
				}
			}
			
		}
		
		return true;
	}

	private ArrayList<String> getPossibleMovesFromPosition(int code) {
		int istart = code / triangle.length;
		int jstart = code % triangle.length;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		//There's 6 directions to check...
		//UP:
		if(istart >= 2 && jstart <= istart-2) {
			if(triangle[istart-1][jstart] && triangle[istart-2][jstart] == false) {
				
				ret.addAll( getPossibleMovesAfterJump(getCode(istart, jstart) +"-" + getCode(istart-2, jstart)) );
			}
		}
		
		//UP LEFT
		if(istart >=2 && jstart >= 2) {
			if(triangle[istart-1][jstart-1] && triangle[istart-2][jstart-2] == false) {
				ret.addAll( getPossibleMovesAfterJump(getCode(istart, jstart) +"-" + getCode(istart-2, jstart-2)) );
			}
		}
		
		//RIGHT:
		if(jstart + 2 < triangle[istart].length) {
			if(triangle[istart][jstart+1] && triangle[istart][jstart+2] == false) {
				ret.addAll( getPossibleMovesAfterJump(getCode(istart, jstart) +"-" + getCode(istart, jstart+2)) );
			}
		}
		
		//LEFT:
		if(jstart >=2) {
			if(triangle[istart][jstart-1] && triangle[istart][jstart-2] == false) {
				ret.addAll( getPossibleMovesAfterJump(getCode(istart, jstart) +"-" + getCode(istart, jstart-2)) );
			}
		}
		
		//DOWN:
		if(istart + 2 < triangle.length) {
			if(triangle[istart+1][jstart] && triangle[istart+2][jstart] == false) {
				ret.addAll( getPossibleMovesAfterJump(getCode(istart, jstart) +"-" + getCode(istart+2, jstart)) );
			}
		}
		
		//DOWN RIGHT
		if(istart + 2 < triangle.length) {
			if(triangle[istart+1][jstart+1] && triangle[istart+2][jstart+2] == false) {
				ret.addAll( getPossibleMovesAfterJump(getCode(istart, jstart) +"-" + getCode(istart+2, jstart+2)) );
			}
		}
		
		return ret;
	}
	
	private ArrayList<String> getPossibleMovesAfterJump(String jump) {
		ArrayList<String> ret = new ArrayList<String>();
		
		ret.add(jump);
		

		TriangleBoard tmp = this.moveInternal(jump);
		
		int landing = Integer.parseInt(jump.split("-")[1]);
		ArrayList<String> newSeriesOfMoves = tmp.getPossibleMovesFromPosition(landing);
		for(int i=0; i<newSeriesOfMoves.size(); i++) {
			ret.add(jump.split("-")[0] + "-" + newSeriesOfMoves.get(i));
		}
		
		return ret;
	}


	//WARNING: If you're moving the wrong peg, it won't count as an extra move
	public TriangleBoard doOneMove(String move) {
		String seriesOfJumps[] = move.split("-");
		
		TriangleBoard newBoard = this;
		
		for(int i=0; i<seriesOfJumps.length - 1; i++) {
			
			int from = Integer.parseInt(seriesOfJumps[i]);
			int to = Integer.parseInt(seriesOfJumps[i+1]);
			
			newBoard = newBoard.moveInternal(from + "-" + to);
		}
		
		if(newBoard == this) {
			System.out.println("ERROR blank move!");
			System.exit(1);
		}

		newBoard.prevLocation = this;
		
		return newBoard;
		
	}
	
	//pre: valid move
	private TriangleBoard moveInternal(String move) {
		String fromTo[] = move.split("-");
		
		int from = Integer.parseInt(fromTo[0]);
		int to = Integer.parseInt(fromTo[1]);
		
		int fromI = from / triangle.length;
		int fromJ = from % triangle.length;
		
		int toI = to / triangle.length;
		int toJ = to % triangle.length;
		
		TriangleBoard newBoard = new TriangleBoard(triangle.length);
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				newBoard.triangle[i][j] = triangle[i][j];
			}
		}
		
		if(newBoard.triangle[fromI][fromJ] == false) {
			System.out.println("ERROR move 1");
		}
		
		if(newBoard.triangle[(fromI+toI)/2][(fromJ+toJ)/2] == false) {
			System.out.println("ERROR move 2");
		}
		
		if(newBoard.triangle[toI][toJ] == true) {
			System.out.println("ERROR move 3");
		}
		
		newBoard.triangle[fromI][fromJ] = false;
		newBoard.triangle[(fromI+toI)/2][(fromJ+toJ)/2] = false;
		newBoard.triangle[toI][toJ] = true;
		
		newBoard.numPiecesLeft = this.numPiecesLeft - 1;
		
		newBoard.historicMoveList = this.historicMoveList;
		
		if(internalLastJumpCodeForMultiJumpMoves == from) {
			//not a new move
			newBoard.numMovesMade = this.numMovesMade;
			newBoard.historicMoveList += "-" + to;
			
		} else {
			newBoard.numMovesMade = this.numMovesMade + 1;
			newBoard.historicMoveList += "  " + move;
		}
		newBoard.internalLastJumpCodeForMultiJumpMoves = to;
		
		return newBoard;
	}


	public int getNumPiecesLeft() {
		return numPiecesLeft;
	}

	public int getNumMovesMade() {
		return numMovesMade;
	}

	public String getHistoricMoveList() {
		return historicMoveList;
	}
	

	private long lastLookupNumberResult = -1;
	public long getLookupNumber() {
		if(lastLookupNumberResult == -1) {
			lastLookupNumberResult = TriangleLookup.convertToNumberWithComboTricksAndSymmetry(triangle, numPiecesLeft);
		}
		return TriangleLookup.convertToNumberWithComboTricksAndSymmetry(triangle, numPiecesLeft);
	}
	
	public int length() {
		return triangle.length;
	}
	
	private static void TESTcompareBoardsForTesting(TriangleBoard a, TriangleBoard b) {
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