package triangleBoard5;

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


	//This is looking for just 1 solution...
	// TODO: try finding all optimal solutions later...

	public static final int LENGTH = 6;

	public static int MAX_DEPTH_TOTAL = 13;
	public static int MEM_DEPTH_FORWARDS =7;
	public static int MEM_DEPTH_BACKWARDS = 3;

	
	public static void main(String args[]) {
		
		boolean SET_SLOW = false;
		if(SET_SLOW) {
			System.out.println("WARNING: is slow!");
		}
		
		System.out.println("Trying " + LENGTH + " in TriangleSolveOptimizedTrial5");
		System.out.println("Giving up after reaching a max depth of " + MAX_DEPTH_TOTAL);

		TriangleBoard boardStart;
		
		for(int i=0; i<LENGTH; i++) {
			for(int j=0; j<=i; j++) {
				boardStart = new TriangleBoard(LENGTH);
				boardStart.removePiece(i * LENGTH + j);
				
				long lookup = boardStart.getLookupNumber();
				
				if(startingPositionSearched.contains(lookup)) {
					System.out.println("SKIPING (" + i + ", " + j + ")");
					continue;
				} else {
					startingPositionSearched.add(lookup);
				}
				
				initRecordedTriangles(LENGTH);

				
				TriangleBoard boardSol = null;
				
				if(SET_SLOW) {
					boardSol = getBestMoveListSlow(boardStart);
				} else {
					boardSol = getBestMoveList(boardStart);
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
	public static int numBackwardsRecordsSavedForDEBUG = 0;
	
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
	

	public static BackwardsTriangleBoard getBestMoveListBackwardsSlow(BackwardsTriangleBoard board) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 10000 == 0) {
			//System.out.println("SLOW");
			//board.draw();
		}
		
		if(board.getNumPiecesLeft() == utilFunctions.getTriangleNumber(board.length()) - 1) {
			return board;
		}
		
		BackwardsTriangleBoard currentBestSol = null;
		
		ArrayList<String> backwardsMoves = board.getFullBackwardsMoves();
		
		for(int i=0; i<backwardsMoves.size(); i++) {
			BackwardsTriangleBoard possibleBest = getBestMoveListBackwardsSlow(board.doOneBackwardsMove(backwardsMoves.get(i)));
			
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
	
	
	public static void initRecordedTriangles(int boardLength) {
		numRecordsSavedForDEBUG = 0;
		recordedTriangles = new HashMap[utilFunctions.getTriangleNumber(boardLength)];
		for(int i=0; i<recordedTriangles.length; i++) {
			recordedTriangles[i] = new HashMap<Long, triangleRecord>();
		}
		
	}
	
	public static HashSet<Long> backwardsSolutionsCache[];
	
	public static void initBackwardsSolutionCache(int boardLength) {
		backwardsSolutionsCache= new HashSet[utilFunctions.getTriangleNumber(boardLength)];
		
		for(int i=0; i<backwardsSolutionsCache.length; i++) {
			backwardsSolutionsCache[i] = new HashSet<Long>();
		}
	}
	
	
	public static TriangleBoard getBestMoveList(TriangleBoard board) {

		initRecordedTriangles(board.length());
		initBackwardsSolutionCache(board.length());

		HashSet<Long> endingPositionSearched = new HashSet<Long>();
		
		int debugNumRecordSavedPrevDepth = 0;
		
		TriangleBoard answer = null;
		for(int i=1; true; i++) {
			
			if(i > MEM_DEPTH_BACKWARDS) {
				break;
			}
			
			//Instead of doing an init after every iteration, just save it and reuse it.
			// I don't know how much time it will save, but it's probably better than nothing.
			//TODO: testing
			//initRecordedTriangles(board.length());
			
			answer = getBestMoveList(board, i, false);
			
			System.out.println("End of search with depth " + i + " and triangle length " + board.length());
			System.out.println("Num records saved for prev depths: " + debugNumRecordSavedPrevDepth);
			System.out.println("Num records saved total: " + numRecordsSavedForDEBUG);
			
			debugNumRecordSavedPrevDepth = numRecordsSavedForDEBUG;
			if(answer != null) {
				break;
			}
		}

		
		
		BackwardsTriangleBoard backwardsBoardStart;
		
		for(int iend=0; iend<board.length(); iend++) {
			for(int jend=0; jend<=iend; jend++) {
				
				backwardsBoardStart = new BackwardsTriangleBoard(board.length());
				backwardsBoardStart.addPiece(iend * board.length() + jend);
				
				long lookupBackwards = backwardsBoardStart.getLookupNumber();
				
				if(endingPositionSearched.contains(lookupBackwards)) {
					continue;
				} else {
					endingPositionSearched.add(lookupBackwards);
				}
				
				System.out.println("***************************************");
				System.out.println("***************************************");
				System.out.println("Attempting to go from:");
				board.draw();
				System.out.println("To end goal (with symmetries):");
				backwardsBoardStart.draw();
				//int triangleLength, int endi, int endj, int maxDepth
				backwardsSolutionsCache = TriangleSolveGetPosDepthDAwayFromSolution.getPositionDepthNAwayFromGoal(board.length(), iend, jend, MEM_DEPTH_BACKWARDS);

				//Need to reinit recorded triangle because we're starting over from depth 1:
				initRecordedTriangles(board.length());
				
				debugNumRecordSavedPrevDepth = 0;
				
				for(int depth=MEM_DEPTH_BACKWARDS + 1; depth<= MAX_DEPTH_TOTAL; depth++) {
					System.out.println("DEBUG: trying depth " + depth + " with backwards saved pos");
					
					//TODO: testing init
					initRecordedTriangles(board.length());
					answer = getBestMoveList(board, depth - MEM_DEPTH_BACKWARDS, false);
					
					if(answer != null) {
						break;
					}
					

					System.out.println("End of search with depth " + depth + " and triangle length " + board.length() + " and  backwards saved pos depth " + MEM_DEPTH_BACKWARDS);
					System.out.println("Num records saved for prev depths: " + debugNumRecordSavedPrevDepth);
					System.out.println("Num records saved total: " + numRecordsSavedForDEBUG);
					debugNumRecordSavedPrevDepth = numRecordsSavedForDEBUG;
				}
			}
		}
		
		
		return answer;
		
	}
	
	public static TriangleBoard getBestMoveList(TriangleBoard board, int curMaxDepth, boolean currentlyFoundSolution) {
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 1000000 == 0) {
			//System.out.println("FAST");

			//System.out.println("Current depth: " + getMaxDepthUsed(board, curMaxDepth) + " out of " + MAX_DEPTH);

			//TODO: see what happens after implementing conway math...
			//System.out.println("Num records saved: " + numRecordsSavedForDEBUG);
			//board.draw();
		}

		
		if(board.getNumPiecesLeft() == 1) {
			System.out.println("FINAL SOLUTION");
			board.draw();
			//System.exit(1);
			return board;
		}

		long lookup = board.getLookupNumber();
		
		if(currentlyFoundSolution == false && backwardsSolutionsCache[board.getNumPiecesLeft()].contains(lookup)) {
			//Victory road
			currentlyFoundSolution = true;
			System.out.println("DEBUG: FOUND BRIDGE!");
			board.draw();
			
			curMaxDepth += MEM_DEPTH_BACKWARDS;
		} else if(curMaxDepth == 0){
			return null;
		}

		if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup)) {
			
			triangleRecord previouslyFoundNode = recordedTriangles[board.getNumPiecesLeft()].get(lookup);
			
			if(board.getNumMovesMade() > previouslyFoundNode.getNumMovesToGetToPos()) {
					
				//System.out.println("Cutting short 0");
				return null;
			} else if(board.getNumMovesMade() == previouslyFoundNode.getNumMovesToGetToPos()){
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() == utilFunctions.getMaxDepthUsed(board, curMaxDepth)) {
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
				|| board.getNumMovesMade() < MEM_DEPTH_FORWARDS) {
		
			if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup) == false) {
				recordedTriangles[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, curMaxDepth));
				
				numRecordsSavedForDEBUG++;
			}
		}
			
		//END CHECKPOINT LOGIC
		
		ArrayList<String> moves = board.getFullMoves();
		
		for(int i=0; i<moves.size(); i++) {

			TriangleBoard possibleBest = getBestMoveList(board.doOneMove(moves.get(i)), curMaxDepth - 1, currentlyFoundSolution);

			if(possibleBest != null) {
				return possibleBest;
			}
			
		}
		
		return null;
	}
	
}
