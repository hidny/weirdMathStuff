package triangleBoard3;

import java.util.ArrayList;
import java.util.HashMap;


//Old ideas:
//****************************************************************************
//UNDONE: IDEA 1: keep track of spaces impossible to jump into
//    because it might lead us to realize which positions are impossible to finish

//DONE:
//Keep track of early states and keep a record of how many moves it took to get there and how many moves left
// Try to at least eliminate the need for the trial of a new move sometimes

//NO LONGER APPLIES: Make it record all the starting points tried (not just one))


//DONE: (Also keep tract of the current best solution, so we know what were aiming for and know when to give up...)


//DONE: IDEA 6: Record state 5 moves in and then compare to other attempts (DONE)


//IDEA (STILL IN THE RUNNING): (Maybe take advantage of symmetries... but be careful to move the starting point appropriately)
//Meh... just let it be 6x slower to KISS


//UNDONE: IDEA 8: add tricky isImpossible logic (DONE with the help of TriangleReturnPackage)

//****************************************************************************


//NEW IDEAS 1:
//IDEA (STILL IN THE RUNNING): use math/logic to figure out if a position is not going to produce a solution (Think Conway's soldiers)


//NO LONGER APPLIES: keep a record of all starting locations tried for position, so we could cut the search short more often (No longer applies)

//DONE: keep a record how many moves left from the position and stop searching if it's move than current best solution (FROM BEFORE)
//			(No longer applies: This could get complicated because the position could be in the middle of a move)

//AHA MOMENT for idea 2 and 3: It would be easier if I disallowed in-between moves
//I'm going to rewrite logic without worrying too much about position in the middle of a series of jumps.
//It's going to keep life simple.



//**************************************************************************

//NEW IDEAS 2:

//FORGET IT: Reintroduce concept of impossible to get there? (somehow... and very carefully)
	//Prove that there will be a return of inv first....
	//I don't actually think there's a huge return on inv... it might just go a max of x5 faster...
//It will probably go slower now that i think about it...
//

//STILL IN THE RUNNING: Add some Conway logic

//Bad old ideas:
//STILL IN THE RUNNING: take advantage of symmetries for the lookup table... (this could make it find more connections...)
//Won't be as complicated to implement
// might make it up to x6 times faster

//MEH IDEA 4: make board moves reversable and save space
//MEH IDEA 5: make board moves/processing more efficient

//STILL IN THE RUNNING: order move list from longest to shortest (longest are probably better)
//STILL IN THE RUNNING: use conway math to a) figure out min num moves left
											//b) figure out if position is impossible to complete

//FUNDAMENTAL IDEA: use breadth-first search... :(
//It might take too much memory though...
//Current solution takes about 1hr and 5 minutes...


//BETTER IDEA: Do Iterative deepening depth-first search

//IDEA: Improve how much gets recorded in the lookup table to not go over nay space limits

public class TriangleSolveOptimizedTrial {

	
	
	public static void main(String args[]) {
		
		//int LENGTH = 4;
		int LENGTH = 5;
		//int LENGTH = 6;
		//int LENGTH = 7;
		
		boolean SET_SLOW = true;
		if(SET_SLOW) {
			System.out.println("WARNING: is slow!");
		}
		
		System.out.println("Trying " + LENGTH + " in TriangleSolveOptimizedTrial3");

		TriangleBoard board = new TriangleBoard(LENGTH);
		
		for(int i=0; i<LENGTH; i++) {
			for(int j=0; j<=i; j++) {
				board = new TriangleBoard(LENGTH);
				board.removePiece(i * LENGTH + j);
				initRecordedTriangles(LENGTH);
				
				TriangleBoard boardSol = null;
				
				if(SET_SLOW) {
					boardSol = getBestMoveListSlow(board);
				} else {
					bestGlobalSolution = Integer.MAX_VALUE;
					boardSol = getBestMoveList(board);
				}
				
				//TriangleBoard boardSol = getBestMoveList(board).getBestSolution();
				
				if(boardSol != null) {
					System.out.println("Solution when removing piece " + (i * LENGTH + j));
					boardSol.draw();
					System.out.println();
				} else {
					System.out.println("No solution when removing piece " + (i * LENGTH + j));
					System.out.println();
				}
				//to do: depth first search
			}
		}
		
	}

	public static int numFunctionCallForDEBUG = 0;
	public static TriangleBoard getBestMoveListSlow(TriangleBoard board) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 10000 == 0) {
			//System.out.println("SLOW");
			//board.draw();
		}
		
		if(board.getNumPiecesLeft() == 1) {
			return board;
		}
		
		TriangleBoard currentBestSol = null;
		
		ArrayList<String> moves = board.getFullMoves();
		
		for(int i=0; i<moves.size(); i++) {
			TriangleBoard possibleBest = getBestMoveListSlow(board.doOneMove(moves.get(i)));
			
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
	public static TriangleBoard getBestMoveList(TriangleBoard board) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 1000000 == 0) {
			//System.out.println("FAST");
			board.draw();
			System.out.println("Best path so far: " + bestGlobalSolution);
		}
		
		if(board.getNumPiecesLeft() == 1) {
			return board;
		} else if(board.getNumMovesMade() >= bestGlobalSolution) {
			return null;
		}

		//Save progress:
		//TODO: use
		if(board.length() <= 6 || Math.min(getTriangleNumber(board.length()) - board.getNumPiecesLeft(), board.getNumPiecesLeft()) <= 8) {
			//System.out.println("Reached checkpoint");
			//board.draw();
			
			long lookup = board.getLookupNumber();
			
			triangleRecord checkpoint = null;
			
			if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup)) {
				checkpoint = recordedTriangles[board.getNumPiecesLeft()].get(lookup);
				
				
				if(board.getNumMovesMade() >= checkpoint.getNumMovesToGetToPos()) {
						
					//System.out.println("Cutting short 0");
					return null;
				} else {
					
					checkpoint.updateNumMovesToGetToPos(board.getNumMovesMade());
				}
				
				//TODO: if conway math says impossible: dont try.
				
				
			} else {
				recordedTriangles[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade()));
			}
			
		}
		//END CHECKPOINT LOGIC
		
		TriangleBoard currentBestSol = null;
		
		ArrayList<String> moves = board.getFullMoves();
		
		
		for(int i=0; i<moves.size(); i++) {

			TriangleBoard possibleBest = null;
			
			possibleBest = getBestMoveList(board.doOneMove(moves.get(i)));
			
			if(possibleBest != null) {
				if(currentBestSol == null 
				|| possibleBest.getNumMovesMade() < currentBestSol.getNumMovesMade()) {
					currentBestSol = possibleBest;
					
					if(currentBestSol.getNumMovesMade() < bestGlobalSolution) {
						bestGlobalSolution = currentBestSol.getNumMovesMade();
					}
					
				}
			}
			
		}
		
		return currentBestSol;
	}
	
	public static int getTriangleNumber(int n) {
		return n * (n+1) / 2;
	}
}
