package triangleBoard;

import java.util.ArrayList;

public class TriangleSolve {

	public static void main(String args[]) {
		
		//int LENGTH = 4;
		int LENGTH = 6;
		TriangleBoard board = new TriangleBoard(LENGTH);
		
		for(int i=0; i<LENGTH; i++) {
			for(int j=0; j<=i; j++) {
				board = new TriangleBoard(LENGTH);
				board.removePiece(i * LENGTH + j);
				
				TriangleBoard boardSol = getBestMoveList(board);
				
				if(boardSol != null) {
					System.out.println("Solution when removing piece " + (i * LENGTH + j));
					boardSol.draw();
				} else {
					System.out.println("No solution when removing piece " + (i * LENGTH + j));
				}
				//to do: depth first search
			}
		}
		
	}
	
	public static TriangleBoard getBestMoveList(TriangleBoard board) {
	
		if(board.getNumPiecesLeft() == 1) {
			return board;
		}
		
		TriangleBoard currentBestSol = null;
		
		ArrayList<String> moves = board.getMoves();
		
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard possibleBest = getBestMoveList(board.move(moves.get(i)));
			
			if(possibleBest != null) {
				if(currentBestSol == null) {
					currentBestSol = possibleBest;
				} else if(possibleBest.getNumMovesMade() < currentBestSol.getNumMovesMade()) {
					currentBestSol = possibleBest;
				}
			}
			
		}
		
		return currentBestSol;
		
	}
}
