package triangleBoard1BROKEN;

import java.util.ArrayList;
import java.util.HashMap;


//Old ideas:
//****************************************************************************
//DONE: IDEA 1: keep track of spaces impossible to jump into
//    because it might lead us to realize which positions are impossible to finish

//BETTER IDEA: (DONE)
//Keep track of early states and keep a record of how many moves it took to get there and how many moves left
// Try to at least eliminate the need for the trial of a new move sometimes
// (TODO: Make it record all the starting points tried (not just one)


//MAYBE IDEA3: (Also keep tract of the current best solution, so we know what were aiming for and know when to give up...)


//DONE: IDEA 6: Record state 5 moves in and then compare to other attempts (DONE)


//IDEA 7: (Maybe take advantage of symmetries... but be careful to move the starting point appropriately)
//Meh... just let it be 6x slower to KISS


//DONE: IDEA 8: add tricky isImpossible logic (DONE with the help of TriangleReturnPackage)

//****************************************************************************


//NEW IDEAS:
//IDEA 1: use math/logic to figure out if a position is not going to produce a solution (Think Conway's soldiers)


//IDEA 2: keep a record of all starting locations tried for position, so we could cut the search short more often

//IDEA 3: keep a record how many moves left from the position and stop searching if it's move than current best solution (FROM BEFORE)
//			(This could get complicated because the position could be in the middle of a move)

//AHA MOMENT for idea 2 adn 3: It would be easier if I disallowed in-between moves
//I'm going to rewrite logic without worrying too much about position in the middle of a series of jumps.
//It's going to keep life simple.


//MEH IDEA 4: make board moves reversable and save space
//MEH IDEA 5: make board moves/processing more efficient
//MEH IDEA 6:  Maybe take advantage of symmetries... but be careful to move the starting point appropriately (Meh... just let it be 6x slower to KISS)


public class TriangleSolveOptimizedTrial {

	
	
	public static void main(String args[]) {
		
		int LENGTH = 4;
		//int LENGTH = 6;
		
		
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
	
	public static final boolean HAS_SOLUTION = true;
	public static final boolean NO_SOLUTION = false;
	
	
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
			
			return new TriangleReturnPackage(HAS_SOLUTION, board);
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
					
					return new TriangleReturnPackage(NO_SOLUTION, null);
				}
				
				//Check if number of moves to get there is ok:
				if(board.getNumMovesMade() > prevRecordedCheckpoint.getNumMovesToGetToPos()) {
					
					//System.out.println("Cutting short 1");
					return new TriangleReturnPackage(HAS_SOLUTION, null);

				} else if(board.getNumMovesMade() == prevRecordedCheckpoint.getNumMovesToGetToPos()) {
					disallowNewMoveAtCheckpointPosition = true;
				}
				
				//Check if we are retracing our steps exactly:
				if(board.getLastJumpCode() == prevRecordedCheckpoint.getCurSelectedPieceCode()) {
					//If we are, keep going if it took less moves to get there:
					if(board.getNumMovesMade() >= prevRecordedCheckpoint.getNumMovesToGetToPos()) {
						

						//System.out.println("Cutting short 2");
						return new TriangleReturnPackage(HAS_SOLUTION, null);
					}
				}
				
			} else {
				recordedTriangles[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board.getLastJumpCode()));
			}
			
			triangleRecord currentRecordedPos = recordedTriangles[board.getNumPiecesLeft()].get(lookup);
		//}
		//END CHECKPOINT LOGIC
		
		TriangleBoard currentBestSol = null;
		boolean isConfirmedSolutionExists = false;
		
		ArrayList<String> moves = board.getMoves();
		
		
		for(int i=0; i<moves.size(); i++) {

			TriangleBoard possibleBest = null;
			
			if(atCheckpointPosition && isNotANewMove(moves.get(i), board) && disallowNewMoveAtCheckpointPosition) {
				//System.out.println("Cutting short 3");
				possibleBest = null;
				isConfirmedSolutionExists = true;
				
				
			} else {
				
				TriangleReturnPackage tmp = getBestMoveList(board.move(moves.get(i)));
				possibleBest = tmp.getBestSolution();
				
				if(tmp.HasSolution()) {
					isConfirmedSolutionExists = true;
				}
			}
				
			if(possibleBest != null) {
				if(currentBestSol == null 
				|| possibleBest.getNumMovesMade() < currentBestSol.getNumMovesMade()) {
					currentBestSol = possibleBest;
					
				}
			}
			
		}
		
		if(isConfirmedSolutionExists == false) {
			//TODO TEST take away
			//currentRecordedPos.setImpossibleIfUncertain();
		} else {
			currentRecordedPos.setPossible();
		}
		
		return new TriangleReturnPackage(isConfirmedSolutionExists, currentBestSol);
		
	}
	
	public static boolean isNotANewMove(String jump, TriangleBoard board) {
		return Integer.parseInt(jump.split("-")[0]) == board.getLastJumpCode();
		
	}
	
	public static int getTriangleNumber(int n) {
		return n * (n+1) / 2;
	}
}
