package triangleBoard;

import java.util.ArrayList;

public class TriangleBoard {
	//Only hard-copies allow
	
	public static void main(String args[]) {
		TriangleBoard board = new TriangleBoard(4);

		board.removePiece(0);
		board.removePiece(13);
		board.removePiece(15);
		board.draw();
		
		System.out.println(board.getNumPossibleMoves());
		
		ArrayList<String> moves = board.getMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(14);
		board.removePiece(15);
		board.draw();
		
		System.out.println(board.getNumPossibleMoves());
		
		moves = board.getMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(12);
		board.removePiece(15);
		board.draw();
		
		System.out.println(board.getNumPossibleMoves());
		
		moves = board.getMoves();
		for(int i=0; i<moves.size(); i++) {
			System.out.println(moves.get(i));
		}
		
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.move(moves.get(i));
			tmp.draw();
		}
		
		System.out.println("Try a double loop:");
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.move(moves.get(i));
			tmp.draw();
			
			ArrayList <String> moves2 = tmp.getMoves();

			for(int j=0; j<moves2.size(); j++) {
				TriangleBoard tmp2 = tmp.move(moves2.get(j));
				tmp2.draw();
				
				System.out.println("Lookup number:");
				System.out.println(tmp2.getLookupNumber());
			}
			
			
		}
		
		
		board = new TriangleBoard(4);
		board.removePiece(0);
		board.removePiece(8);
		board.removePiece(15);
		
		moves = board.getMoves();
		
		System.out.println("Try a double loop again:");
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard tmp = board.move(moves.get(i));
			tmp.draw();
			
			ArrayList <String> moves2 = tmp.getMoves();

			for(int j=0; j<moves2.size(); j++) {
				TriangleBoard tmp2 = tmp.move(moves2.get(j));
				tmp2.draw();
			}
			
		}
	}
	
	//TODO: add move list
	
	private boolean triangle[][];
	private int lastJumpCode;
	private int numPiecesLeft;
	private int numMovesMade;

	private String historicMoveList;
	
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
		
		lastJumpCode = -1;
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

		System.out.println("Last jump: " + lastJumpCode);
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
	
	
	//TODO:
	public int getNumPossibleMoves() {
		
		int ret = 0;
		
		for(int i=0; i<triangle.length; i++) {
			for(int j=0; j<triangle[i].length; j++) {
				if(triangle[i][j]) {
					ret += getNumPossibleMovesFromPosition(i * triangle.length + j);
				}
			}
		}
		
		return ret;
	}
	
	
	//TODO: delete later and replace with something that just gets an array list.
	private int getNumPossibleMovesFromPosition(int code) {
		int istart = code / triangle.length;
		int jstart = code % triangle.length;
		
		int ret = 0;
		
		//There's 6 directions to check...
		//UP:
		if(istart >= 2 && jstart <= istart-2) {
			if(triangle[istart-1][jstart] && triangle[istart-2][jstart] == false) {
				ret++;
			}
		}
		
		//UP LEFT
		if(istart >=2 && jstart >= 2) {
			if(triangle[istart-1][jstart-1] && triangle[istart-2][jstart-2] == false) {
				ret++;
			}
		}
		
		//RIGHT:
		if(jstart + 2 < triangle[istart].length) {
			if(triangle[istart][jstart+1] && triangle[istart][jstart+2] == false) {
				ret++;
			}
		}
		
		//LEFT:
		if(jstart >=2) {
			if(triangle[istart][jstart-1] && triangle[istart][jstart-2] == false) {
				ret++;
			}
		}
		
		//DOWN:
		if(istart + 2 < triangle.length) {
			if(triangle[istart+1][jstart] && triangle[istart+2][jstart] == false) {
				ret++;
			}
		}
		
		//DOWN RIGHT
		if(istart + 2 < triangle.length) {
			if(triangle[istart+1][jstart+1] && triangle[istart+2][jstart+2] == false) {
				ret++;
			}
		}
		
		return ret;
	}
	
	public ArrayList<String> getMoves() {
		
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
	
	public ArrayList<String> getPossibleMovesFromPosition(int code) {
		int istart = code / triangle.length;
		int jstart = code % triangle.length;
		
		ArrayList<String> ret = new ArrayList<String>();
		
		//There's 6 directions to check...
		//UP:
		if(istart >= 2 && jstart <= istart-2) {
			if(triangle[istart-1][jstart] && triangle[istart-2][jstart] == false) {
				ret.add(getCode(istart, jstart) +"-" + getCode(istart-2, jstart));
			}
		}
		
		//UP LEFT
		if(istart >=2 && jstart >= 2) {
			if(triangle[istart-1][jstart-1] && triangle[istart-2][jstart-2] == false) {
				ret.add(getCode(istart, jstart) +"-" + getCode(istart-2, jstart-2));
			}
		}
		
		//RIGHT:
		if(jstart + 2 < triangle[istart].length) {
			if(triangle[istart][jstart+1] && triangle[istart][jstart+2] == false) {
				ret.add(getCode(istart, jstart) +"-" + getCode(istart, jstart+2));
			}
		}
		
		//LEFT:
		if(jstart >=2) {
			if(triangle[istart][jstart-1] && triangle[istart][jstart-2] == false) {
				ret.add(getCode(istart, jstart) +"-" + getCode(istart, jstart-2));
			}
		}
		
		//DOWN:
		if(istart + 2 < triangle.length) {
			if(triangle[istart+1][jstart] && triangle[istart+2][jstart] == false) {
				ret.add(getCode(istart, jstart) +"-" + getCode(istart+2, jstart));
			}
		}
		
		//DOWN RIGHT
		if(istart + 2 < triangle.length) {
			if(triangle[istart+1][jstart+1] && triangle[istart+2][jstart+2] == false) {
				ret.add(getCode(istart, jstart) +"-" + getCode(istart+2, jstart+2));
			}
		}
		
		return ret;
	}
	
	//pre: valid move
	public TriangleBoard move(String move) {
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
		
		if(lastJumpCode == from) {
			//not a new move
			newBoard.numMovesMade = this.numMovesMade;
			newBoard.historicMoveList += "-" + to;
			
		} else {
			newBoard.numMovesMade = this.numMovesMade + 1;
			newBoard.historicMoveList += "  " + move;
		}
		newBoard.lastJumpCode = to;
		
		return newBoard;
	}

	public int getLastJumpCode() {
		return lastJumpCode;
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
		return TriangleLookup.convertToNumberAssumeCloseToStart(triangle);
	}
	
}
