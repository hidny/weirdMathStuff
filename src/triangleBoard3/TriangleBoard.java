package triangleBoard3;

import java.util.ArrayList;

public class TriangleBoard {
	//Only hard-copies allow
	
	public static void main(String args[]) {
		TriangleBoard board = new TriangleBoard(4);

		board.removePiece(0);
		board.removePiece(13);
		board.removePiece(15);
		board.draw();
		
		
		ArrayList<String> moves = board.getFullMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(14);
		board.removePiece(15);
		board.draw();
		
		moves = board.getFullMoves();
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
		moves = board.getFullMoves();
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
			
			ArrayList <String> moves2 = tmp.getFullMoves();

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
		
		moves = board.getFullMoves();

		System.out.println("***");
		System.out.println("***");
		System.out.println("Try a double loop again:");
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.doOneMove(moves.get(i));
			tmp.draw();
			
			ArrayList <String> moves2 = tmp.getFullMoves();

			for(int j=0; j<moves2.size(); j++) {
				TriangleBoard tmp2 = tmp.doOneMove(moves2.get(j));
				tmp2.draw();
			}
			
		}
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
		
		//private boolean triangle[][];
		//private int lastJumpCode;
		//private int numPiecesLeft;
		//private int numMovesMade;

		System.out.println("Num pieces left: " + numPiecesLeft);
		System.out.println("Num moves Made: " + numMovesMade);
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
	
	public ArrayList<String> getFullMoves() {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					ret.addAll(getPossibleMovesFromPosition(i * triangle.length + j));
				}
			}
		}
		
		return ret;
		
	}
	
	public int getCode(int i, int j) {
		return i*triangle.length + j;
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


	public TriangleBoard doOneMove(String move) {
		
		String seriesOfJumps[] = move.split("-");
		
		TriangleBoard newBoard = this;
		
		for(int i=0; i<seriesOfJumps.length - 1; i++) {
			
			int from = Integer.parseInt(seriesOfJumps[i]);
			int to = Integer.parseInt(seriesOfJumps[i+1]);
			
			newBoard = newBoard.moveInternal(from + "-" + to);
		}
		
		newBoard.numMovesMade = this.numMovesMade + 1;
		
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
	
	public long getLookupNumber() {
		return TriangleLookup.convertToNumber(triangle, numPiecesLeft);
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