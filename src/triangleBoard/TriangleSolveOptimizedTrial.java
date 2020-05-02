package triangleBoard;

import java.util.ArrayList;
import java.util.HashMap;


//TODO:
//IDEA 1: keep track of spaces impossible to jump into
//    because it might lead us to realize which positions are impossible to finish

//BETTER IDEA: (DONE)
//Keep track of early states and keep a record of how many moves it took to get there and how many moves left
// Try to at least eliminate the need for the trial of a new move sometimes
// (TODO: Make it record all the starting points tried (not just one)

// IDEA3: (Also keep tract of the current best solution, so we know what were aiming for and know when to give up...)

//IDEA 4: make board moves reversable and save space
//IDEA 5: make board moves/processing more efficient
//IDEA 6: Record state 5 moves in and then compare to other attempts (DONE)

//IDEA 7: (Maybe take advantage of symmetries... but be careful to move the starting point appropriately)


//IDEA 8: add tricky isImpossible logic
//		It has to fail to find anything and check if it ever suceeded before
// (It could fail to find something because it was trying to be better than what's already found...)
//This is complicated...
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

				initRecordedTriangles(LENGTH);
				//TriangleBoard boardSol = getBestMoveListSlow(board);
				TriangleBoard boardSol = getBestMoveList(board).getBestSolution();
				
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

	public static int numFunctionCallForDEBUG = 0;
	public static TriangleBoard getBestMoveListSlow(TriangleBoard board) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 10000 == 0) {
			//board.draw();
		}
		
		if(board.getNumPiecesLeft() == 1) {
			return board;
		}
		
		TriangleBoard currentBestSol = null;
		
		ArrayList<String> moves = board.getMoves();
		
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard possibleBest = getBestMoveListSlow(board.move(moves.get(i)));
			
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
	
	

	//Optimized:
	
	public static final int NUM_EMPTY_PIECE_WHEN_RECORD = 8;
	
	public static HashMap<Long, triangleRecord>[] recordedTriangles;
	
	public static int bestGlobalSolution = Integer.MAX_VALUE;
	
	
	public static void initRecordedTriangles(int length) {
		recordedTriangles = new HashMap[getTriangleNumber(length)];
		for(int i=0; i<recordedTriangles.length; i++) {
			recordedTriangles[i] = new HashMap<Long, triangleRecord>();
		}
	}
	
	//TODO: this is looking for just 1 solution...
	// try finding more all optimal solutions later...
	//TODO:
	public static TriangleReturnPackage getBestMoveList(TriangleBoard board) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 1000000 == 0) {
			//board.draw();
		}
		
		if(board.getNumPiecesLeft() == 1) {
			
			return new TriangleReturnPackage(true, board);
		}

		//CHECKPOINT LOGIC
		//TODO: use
		boolean atCheckpointPosition = false;
		boolean disallowNewMoveAtCheckpointPosition = false;
		
		//TODO: all points are checkpoints because why not?
		//if(getTriangleNumber(board.length()) - board.getNumPiecesLeft() == NUM_EMPTY_PIECE_WHEN_RECORD) {
			//TODO: record it maybe?
			atCheckpointPosition = true;
			//System.out.println("Reached checkpoint");
			//board.draw();
			
			long lookup = board.getLookupNumber();
			
			if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup)) {
				triangleRecord prevRecordedCheckpoint = recordedTriangles[board.getNumPiecesLeft()].get(lookup);
				
				//If it's proven to be impossible, don't try:
				if(prevRecordedCheckpoint.isFindingSolImpossible()) {
					//System.out.println("Cutting short 0");
					return new TriangleReturnPackage(false, null);
				}
				
				//Check if number of moves to get there is ok:
				if(board.getNumMovesMade() > prevRecordedCheckpoint.getNumMovesToGetToPos()) {
					
					//System.out.println("Cutting short 1");
					return new TriangleReturnPackage(true, null);

				} else if(board.getNumMovesMade() == prevRecordedCheckpoint.getNumMovesToGetToPos()) {
					disallowNewMoveAtCheckpointPosition = true;
				}
				
				//Check if we are retracing our steps exactly:
				if(board.getLastJumpCode() == prevRecordedCheckpoint.getCurSelectedPieceCode()) {
					//If we are, keep going if it took less moves to get there:
					if(board.getNumMovesMade() >= prevRecordedCheckpoint.getNumMovesToGetToPos()) {
						

						//System.out.println("Cutting short 2");
						return new TriangleReturnPackage(true, null);
					}
				}
				
			} else {
				recordedTriangles[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board.getLastJumpCode()));
			}
			
			triangleRecord currentRecordedPos = recordedTriangles[board.getNumPiecesLeft()].get(lookup);
		//}
		//END CHECKPOINT LOGIC
		
		TriangleBoard currentBestSol = null;
		boolean confirmedSolutionExists = false;
		
		ArrayList<String> moves = board.getMoves();
		
		
		for(int i=0; i<moves.size(); i++) {

			TriangleBoard possibleBest = null;
			
			if(atCheckpointPosition && isNotANewMove(moves.get(i), board) && disallowNewMoveAtCheckpointPosition) {
				//System.out.println("Cutting short 3");
				possibleBest = null;
				confirmedSolutionExists = true;
				
				
			} else {
				
				TriangleReturnPackage tmp = getBestMoveList(board.move(moves.get(i)));
				possibleBest = tmp.getBestSolution();
				
				if(tmp.HasSolution()) {
					confirmedSolutionExists = true;
				}
			}
				
			if(possibleBest != null) {
				if(currentBestSol == null 
				|| possibleBest.getNumMovesMade() < currentBestSol.getNumMovesMade()) {
					currentBestSol = possibleBest;
					
				}
			}
			
		}
		
		if(confirmedSolutionExists == false) {
			currentRecordedPos.setImpossibleIfUncertain();
		} else {
			currentRecordedPos.setPossible();
		}
		
		return new TriangleReturnPackage(confirmedSolutionExists, currentBestSol);
		
	}
	
	public static boolean isNotANewMove(String jump, TriangleBoard board) {
		return Integer.parseInt(jump.split("-")[0]) == board.getLastJumpCode();
		
	}
	
	public static int getTriangleNumber(int n) {
		return n * (n+1) / 2;
	}
}
