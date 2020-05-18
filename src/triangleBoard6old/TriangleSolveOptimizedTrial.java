package triangleBoard6old;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


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
// might make it up to x6 times less memory intensive, but might also slow it down.

//MEH IDEA 4: make board moves reversable and save space
//MEH IDEA 5: make board moves/processing more efficient

//STILL IN THE RUNNING: order move list from longest to shortest (longest are probably better)
				//Maybe only activate it for the final depth?

//STILL IN THE RUNNING: use conway math to a) figure out min num moves left
											//b) figure out if position is impossible to complete

//DONE: don't waste precious recordings... might save lots of time by not tossing them out...

//*********************
//DONE BETTER IDEA: Iterative deepening depth-first search
//Answer for length 6 takes less than 3 minutes vs 1 hour and 5 minutes from before
//************************


//DONE: Improve how much gets recorded in the lookup table to not go over nay space limits
// Then make recording only based on how many moves were done (Don't record complicated middle part if space is an issue)

//TODO: great idea:
// Do a backwards search that goes back 2-3 moves and save the results.
//That way, if the forwards search ever gets with 2-3 moves from goal, it will end.
// This could potentially make it 100x faster

public class TriangleSolveOptimizedTrial {

	public static HashSet<Long> startingPositionSearched = new HashSet<Long>();
	
	public static void main(String args[]) {
		
		//int LENGTH = 4;
		int LENGTH = 5;
		//int LENGTH = 6;
		//int LENGTH = 8;
		
		boolean SET_SLOW = false;
		if(SET_SLOW) {
			System.out.println("WARNING: is slow!");
		}
		
		System.out.println("Trying " + LENGTH + " in TriangleSolveOptimizedTrial6");
		System.out.println("Giving up after reaching a max depth of " + MAX_DEPTH);

		TriangleBoard board = new TriangleBoard(LENGTH);
		
		for(int i=0; i<LENGTH; i++) {
			for(int j=0; j<=i; j++) {
				board = new TriangleBoard(LENGTH);
				board.removePiece(i * LENGTH + j);
				initRecordedTriangles(LENGTH);
				
				long lookup = board.getLookupNumber();
				
				if(LENGTH >=7 && startingPositionSearched.contains(lookup)) {
					System.out.println("SKIPING (" + i + ", " + j + ")");
					continue;
				} else {
					startingPositionSearched.add(lookup);
				}
				
				
				TriangleBoard boardSol = null;
				
				if(SET_SLOW) {
					boardSol = getBestMoveListSlow(board);
				} else {
					boardSol = getBestMoveList(board);
				}
				
				if(boardSol != null) {
					System.out.println("Solution when removing piece " + (i * LENGTH + j));
					boardSol.draw();
					System.out.println();
				} else {
					System.out.println("No solution when removing piece " + (i * LENGTH + j));
					System.out.println();
				}
			}
		}
		
	}

	public static int numFunctionCallForDEBUG = 0;
	public static int numRecordsSavedForDEBUG = 0;
	
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
	
	
	public static void initRecordedTriangles(int length) {
		numRecordsSavedForDEBUG = 0;
		recordedTriangles = new HashMap[getTriangleNumber(length)];
		for(int i=0; i<recordedTriangles.length; i++) {
			recordedTriangles[i] = new HashMap<Long, triangleRecord>();
		}
	}
	
	//This is looking for just 1 solution...
	// TODO: try finding all optimal solutions later...
	//Invent a number that seems high enough:
	//public static int MAX_DEPTH = 14;
	
	public static int START_DEPTH = 1;
	public static int MAX_DEPTH = 13;
	
	public static TriangleBoard getBestMoveList(TriangleBoard board) {

		initRecordedTriangles(board.length());
		
		TriangleBoard answer = null;
		for(int i=START_DEPTH; i<=MAX_DEPTH; i++) {
			//System.out.println("i: " + i);
			
			//Instead of doing an init after every iteration, just save it and reuse it.
			// I don't know how much time it will save, but it's probably better than nothing.
			//initRecordedTriangles(board.length());
			
			answer = getBestMoveList(board, i);
			if(answer != null) {
				break;
			}
		}
		
		return answer;
		
	}

	//TODO: is 11 too high?
	public static final int MEMORIZING_DEPTH = 9;

	//Num memorized if MEMORIZING_DEPTH is 10 and board length is 7 (and 1st taken is top): 11773014 (without rotational symmetry)
	//                                                            with rotational symmetry: 4067860
	
	//For triangle length 8, I think I'm stuct with depth 9...
	//TODO: make MEM_DEPTH DYNAMIC... (i.e. give up once it reaches 19 M elements)
	
	//Note: num might change if I insist on doing the getNecessaryMovesToCheck only
	// if I insist on doing the getNecessaryMovesToCheck only: 11338817
	// I don't know where the 3 extra came from...
	//AHA: it came from positions found near the end (when there less than n pegs left)
	//Took less than 20 minutes to get there
	
	//Taking away piece 7 on length 7:
    /*G 
   _ _ 
  _ _ _ 
 _ G _ _ 
G _ _ _ _ 
G _ _ _ G _ 
G _ _ G G G _ 
Num pieces left: 9
Num moves Made: 12
Move list:   22-8  24-22  37-23  8-24  21-37  45-29  7-21-37  43-45  32-30-44  45-31  48-32-16-30  31-29-43-45
Current depth: 12 out of 13
Num records saved: 16675013
    G 
    */
	public static TriangleBoard getBestMoveList(TriangleBoard board, int curMaxDepth) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 1000000 == 0) {
			//System.out.println("FAST");

			//System.out.println("Current depth: " + getMaxDepthUsed(board, curMaxDepth) + " out of " + MAX_DEPTH);

			//For now, it's stuck at 6680931 and still running fast, so this is good
			//TODO: see what happens after implementing conway math...
			//System.out.println("Num records saved: " + numRecordsSavedForDEBUG);
			//board.draw();
		}
		
		if(board.getNumPiecesLeft() == 1) {
			return board;
		} else if(curMaxDepth == 0) {
			return null;
		}

		//Check if position was already found:	
		long lookup = board.getLookupNumber();
		if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup)) {
			
			triangleRecord previouslyFoundNode = recordedTriangles[board.getNumPiecesLeft()].get(lookup);
			
			if(board.getNumMovesMade() > previouslyFoundNode.getNumMovesToGetToPos()) {
					
				//System.out.println("Cutting short 0");
				return null;
			} else if(board.getNumMovesMade() == previouslyFoundNode.getNumMovesToGetToPos()){
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() == getMaxDepthUsed(board, curMaxDepth)) {
					return null;
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, curMaxDepth);
				}
				
			} else {
				
				previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, curMaxDepth);
			}
		}
		

		//TODO: if conway math says impossible: dont try.
		//And record impossible if recording is applicable...
		
		//Record position if worthwhile:
		//(Only record if it won't affect memory requirements too much)
		
		if(board.length() <= 6
				|| (getTriangleNumber(board.length()) - board.getNumPiecesLeft() <= 6 || board.getNumMovesMade() <= MEMORIZING_DEPTH))
			 {
		
			if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup) == false) {
				recordedTriangles[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, curMaxDepth));
				
				numRecordsSavedForDEBUG++;
			} else {
				//TODO: count the misses!
			}
		}
			
		//END CHECKPOINT LOGIC
		
		
		ArrayList<String> moves = board.getNecessaryMovesToCheck();
		
		
		for(int i=0; i<moves.size(); i++) {

			TriangleBoard possibleBest = getBestMoveList(board.doOneMove(moves.get(i)), curMaxDepth - 1);
			
			if(possibleBest != null) {
				return possibleBest;
			}
			
		}
		
		return null;
	}
	
	public static int getTriangleNumber(int n) {
		return n * (n+1) / 2;
	}
	
	public static int getMaxDepthUsed(TriangleBoard board, int curMaxDepth) {
		return board.getNumMovesMade() + curMaxDepth;
	}
}
