package triangleBoard;

import java.util.ArrayList;

//TODO:
//IDEA 1: keep track of spaces impossible to jump into
//    because it might lead us to realize which positions are impossible to finish

//BETTER IDEA:
//Keep track of early states and keep a record of how many moves it took to get there and how many moves left
// Try to at least eliminate the need for the trial of a new move sometimes
// IDEA3: (Also keep tract of the current best solution, so we know what were aiming for and know when to give up...)

//IDEA 4: make board moves reversable and save space
//IDEA 5: make board moves/processing more efficient
//IDEA 6: Record state 5 moves in and then compare to other attempts (Maybe take advantage of symmetries...)

public class TriangleSolveOptimizedTrial {

	
	public int NO_SOLUTION = Integer.MAX_VALUE;
	
	public static void main(String args[]) {
		
		//int LENGTH = 4;
		int LENGTH = 6;
		System.out.println("Trying " + LENGTH);
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

	public static int numFunct = 0;
	public static TriangleBoard getBestMoveList(TriangleBoard board) {
		numFunct++;
		if(numFunct % 10000 == 0) {
			//board.draw();
		}
		
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
