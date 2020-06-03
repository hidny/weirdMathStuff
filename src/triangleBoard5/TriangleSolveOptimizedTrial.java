package triangleBoard5;


//So cool: https://pepkin88.me/triangle-peg-solitaire/

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

//DONE: AHA MOMENT for idea 2 and 3: It would be easier if I disallowed in-between moves
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

//BAD IDEA 4: make board moves reversable and save space
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

//DONE: great idea:
// Do a backwards search that goes back 2-3 moves and save the results.
//That way, if the forwards search ever gets with 2-3 moves from goal, it will end.
// This could potentially make it 100x faster

//TODO: IMPORTANT: make get forward moves filter out bad start locations according to meson region if the position is
// right on the edge of being discarded

public class TriangleSolveOptimizedTrial {


	public static HashSet<Long> startingPositionSearched = new HashSet<Long>();


	//This is looking for just 1 solution...
	// TODO: try finding all optimal solutions later...

	//TODO: use pen & paper to figure out which layer actually needs getNecessaryFilter
	public static final int LENGTH = 7;

	public static int MAX_DEPTH_TOTAL = 14;

	//TODO: for triangle 9, I should filter out board positions with few pieces left
	public static int MEM_DEPTH_BACKWARDS = 0;

	public static boolean SEARCH_SINGLE_GOAL = false;
	public static int GOAL_I = 0;
	public static int GOAL_J = 0;
	
	
	//public static int MEM_DEPTH_FORWARDS = Math.min(14, MAX_DEPTH_TOTAL - 1 - MEM_DEPTH_BACKWARDS);
	public static int MEM_DEPTH_FORWARDS = Math.min(13, MAX_DEPTH_TOTAL - 1 - MEM_DEPTH_BACKWARDS);

	
	public static void main(String args[]) {
		

		//DEBUG STATS:
		for(int i=0; i<MAX_DEPTH_TOTAL + 1; i++) {

			debugVisitsPerNumMoves[i] = 0;
			debugIsolationFilterPerNumMoves[i] = 0;
			debugIsolationFilterTriedPerNumMoves[i] = 0;
			debugIsolationFilterClosePerNumMoves[i] = 0;
			debugIsolationFilterClosePerNumMoves2[i] = 0;
		}
		//END DEBUG STATS
		
		getForwardSolutions();
	}
	
	public static void getForwardSolutions() {
		boolean SET_SLOW = false;
		if(SET_SLOW) {
			System.out.println("WARNING: is slow!");
		}
		
		if(SET_SLOW == false) {
			System.out.println("Setting backwards cache");
			initBackwardsSolutionCache(LENGTH);
			
			if(SEARCH_SINGLE_GOAL == false) {
				backwardsSolutionsCache = TriangleSolveGetPosDepthDAwayFromSolution.getPositionsDepthNAwayFromAnyGoal(LENGTH, MEM_DEPTH_BACKWARDS);
			} else {
				backwardsSolutionsCache = TriangleSolveGetPosDepthDAwayFromSolution.getPositionsDepthNAwayFromAGoal(LENGTH, GOAL_I, GOAL_J, MEM_DEPTH_BACKWARDS);
			}

		}
		
		System.out.println("Trying " + LENGTH + " in TriangleSolveOptimizedTrial5");
		System.out.println("Giving up after reaching a max depth of " + MAX_DEPTH_TOTAL);
		System.out.println();

		TriangleBoard boardStart;
		
		for(int i=0; i<LENGTH; i++) {
			for(int j=0; j<=i; j++) {
				boardStart = new TriangleBoard(LENGTH);
				boardStart.removePiece(i * LENGTH + j);
				
				long lookup = boardStart.getLookupNumber();
				
				if(startingPositionSearched.contains(lookup)) {
					System.out.println("SKIPING (" + i + ", " + j + ")");
					System.out.println();
					System.out.println();
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
					System.out.println();
				} else {
					System.out.println("No solution when removing piece " + (i * LENGTH + j));
					System.out.println();
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
		
		ArrayList<String> moves = board.getFullMovesExcludingRepeatMoves();
		
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
		
		ArrayList<String> backwardsMoves = board.getFullBackwardsMovesExcludingRepeatMoves();
		
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

		int debugNumRecordSavedPrevDepth = 0;
		
		System.out.println("Start search for solution starting with:\n"  + board);
		
		TriangleBoard answer = null;
		for(int i=1; true; i++) {
			
			if(i > MEM_DEPTH_BACKWARDS) {
				break;
			}
			
			//Instead of doing an init after every iteration, just save it and reuse it.
			// I don't know how much time it will save, but it's probably better than nothing.
			//TODO: testing
			//initRecordedTriangles(board.length());

			DEPTH_USED_IN_SEARCH = i;
			answer = getBestMoveList(board, i, false);
			
			System.out.println("End of search with depth " + i + " and triangle length " + board.length());
			System.out.println("Num records saved for prev depths: " + debugNumRecordSavedPrevDepth);
			System.out.println("Num records saved total: " + numRecordsSavedForDEBUG);
			
			debugNumRecordSavedPrevDepth = numRecordsSavedForDEBUG;
			if(answer != null) {
				break;
			}
		}

		System.out.println();
		System.out.println("Start searching with the backwards positions Depth " + MEM_DEPTH_BACKWARDS + " away from the answer:");
		System.out.println();
		
		
		//Need to reinit recorded triangle because we're starting over from depth 1:
		initRecordedTriangles(board.length());
		
		debugNumRecordSavedPrevDepth = 0;
		
		for(int depth=MEM_DEPTH_BACKWARDS + 1; depth<= MAX_DEPTH_TOTAL; depth++) {
			System.out.println("DEBUG: trying depth " + depth + " with backwards saved pos");
			
			DEPTH_USED_IN_SEARCH = depth - MEM_DEPTH_BACKWARDS;
			answer = getBestMoveList(board, depth - MEM_DEPTH_BACKWARDS, false);
			
			
			System.out.println("End of search with depth " + depth + " and triangle length " + board.length() + " and backwards saved pos depth " + MEM_DEPTH_BACKWARDS);
			System.out.println("Num records saved for prev depths: " + debugNumRecordSavedPrevDepth);
			System.out.println("Num records saved total: " + numRecordsSavedForDEBUG);
			debugNumRecordSavedPrevDepth = numRecordsSavedForDEBUG;
			

			if(answer != null) {
				break;
			}
		}
		

		return answer;
		
	}
	
	
	public static int debugNumFilteredOut = 0;
	public static int debugNumFiltered = 0;
	public static int debugVisitsPerNumMoves[] = new int[MAX_DEPTH_TOTAL+1];
	public static int debugIsolationFilterPerNumMoves[] = new int[MAX_DEPTH_TOTAL+1];
	public static int debugIsolationFilterTriedPerNumMoves[] = new int[MAX_DEPTH_TOTAL+1];
	public static int debugIsolationFilterClosePerNumMoves[] = new int[MAX_DEPTH_TOTAL+1];
	public static int debugIsolationFilterClosePerNumMoves2[] = new int[MAX_DEPTH_TOTAL+1];
	

	//TODO: keep experimenting and changing this after every optimizaion made:
	//For Trig 9
	public static boolean isolationFilterIsWorthwhile(TriangleBoard board) {
		if(LENGTH == 9) {
			return board.getNumMovesMade() >= 14 || (board.getNumMovesMade() == 13 && board.getNumPiecesLeft() <= utilFunctions.getTriangleNumber(LENGTH)/2 );
		} else {
			return false;
		}
	}
	
	
	private static int DEPTH_USED_IN_SEARCH = -1;
	
	
	public static TriangleBoard getBestMoveList(TriangleBoard board, int curMaxDepth, boolean currentlyFoundSolution) {
		numFunctionCallForDEBUG++;
		debugVisitsPerNumMoves[board.getNumMovesMade()]++;
		
		if(numFunctionCallForDEBUG % 5000000 == 0) {
			
			System.out.println("Current depth: " + DEPTH_USED_IN_SEARCH + " out of (" + MAX_DEPTH_TOTAL + " minus a memorized backwards depth of " + MEM_DEPTH_BACKWARDS +")");

			//TODO: see what happens after implementing conway math...
			System.out.println("Num records saved: " + numRecordsSavedForDEBUG);
			board.draw();
			System.out.println("Min num moves: " +( board.getNumMovesMade() + PositonFilterTests.getNumMesonRegionsSimple(board.getTriangle())));
			
			System.out.println("Debug stats");
			for(int i=0; i<=MAX_DEPTH_TOTAL; i++) {

				System.out.println("Num moves = " + i);
				System.out.println("visits to getBestMoveList:           " + debugVisitsPerNumMoves[i]);
				System.out.println("Visits to Isolation filter:          " + debugIsolationFilterPerNumMoves[i]);
				System.out.println("Visits to Isolation filter function: " + debugIsolationFilterTriedPerNumMoves[i]);
				System.out.println("Close to Isolation filter:(off by 1) " + debugIsolationFilterClosePerNumMoves[i]);
				System.out.println("Close to Isolation filter:(off by 2) " + debugIsolationFilterClosePerNumMoves2[i]);
			}
			System.out.println("End DebugStats");
			
		}

		//SANITY CHECK
		if(currentlyFoundSolution == false &&
				DEPTH_USED_IN_SEARCH != curMaxDepth + board.getNumMovesMade()) {
			System.out.println("ERROR: incorrect number of moves made in TriangleSolveOptimizedTrial");
			System.exit(1);
		}
		//END SANITY CHECK
		
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
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() == DEPTH_USED_IN_SEARCH) {
					return null;
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, DEPTH_USED_IN_SEARCH);
				}
				
			} else {
				
				previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, DEPTH_USED_IN_SEARCH);
			}
		}
		
		

		//APPLY FILTER AFTER WE NOTICE POSITION IS NOT FOUND:
		boolean mustBe100percentMesonEfficient = false;
		
		if(curMaxDepth > 0) {
			int numMesonRegions = PositonFilterTests.getNumMesonRegionsSimple(board.getTriangle());
			//Implemented A* filter
			//Found heuristic function with merson regions:
			if(board.getNumMovesMade() + numMesonRegions > DEPTH_USED_IN_SEARCH + MEM_DEPTH_BACKWARDS) {
				return null;
				
			} else if(board.getNumMovesMade() + numMesonRegions == DEPTH_USED_IN_SEARCH + MEM_DEPTH_BACKWARDS) {
				mustBe100percentMesonEfficient = true;
			}
			
			int numMovesLeftBasedOnPegIsolationSpanningTree = 0;
			
			//Only bother doing this calc when there's not many pegs left:
			if(isolationFilterIsWorthwhile(board)) {
				numMovesLeftBasedOnPegIsolationSpanningTree = PositonFilterTests.getMinMovesBasedOnIsolationSpanningTree(board.getTriangle(), board.getNumPiecesLeft());
				
				debugIsolationFilterTriedPerNumMoves[board.getNumMovesMade()]++;
			}
			
			//TODO: put into function
			//SANITY:
			/*int numMovesLeftBasedOnPegIsolation = PositonFilterTests.GetMinMovesBasedOnIsolationSimple(board.getTriangle(), board.getNumPiecesLeft());
			if(numMovesLeftBasedOnPegIsolation > numMovesLeftBasedOnPegIsolationSpanningTree) {
				System.out.println("ERROR: spanning Tree algo slower than isolation algo");
				System.out.println("span: " + numMovesLeftBasedOnPegIsolationSpanningTree);
				System.out.println("isolation: " + numMovesLeftBasedOnPegIsolation);
				
				board.draw();
				int numMovesLeftBasedOnPegIsolationSpanningTree2 = PositonFilterTests.getMinMovesBasedOnIsolationSpanningTree(board.getTriangle(), board.getNumPiecesLeft());
				int numMovesLeftBasedOnPegIsolationSpanningTree3 = PositonFilterTests.getMinMovesBasedOnIsolationSpanningTree(board.getTriangle(), board.getNumPiecesLeft());
				
				System.out.println("First calc: " + numMovesLeftBasedOnPegIsolationSpanningTree);
				System.out.println("Second calc: " + numMovesLeftBasedOnPegIsolationSpanningTree2);
				System.out.println("Third calc: " + numMovesLeftBasedOnPegIsolationSpanningTree3);
				System.exit(1);
			}*/
			//END TODO
			
	
			if(board.getNumMovesMade() + numMovesLeftBasedOnPegIsolationSpanningTree > DEPTH_USED_IN_SEARCH + MEM_DEPTH_BACKWARDS) {
				debugIsolationFilterPerNumMoves[board.getNumMovesMade()]++;
				//System.out.println("TEST");
				//TODO: count how many times this happens...
				return null;
			} else {
				if(isolationFilterIsWorthwhile(board)) {
					if(board.getNumMovesMade() + numMovesLeftBasedOnPegIsolationSpanningTree + 1 > DEPTH_USED_IN_SEARCH + MEM_DEPTH_BACKWARDS) {
						debugIsolationFilterClosePerNumMoves[board.getNumMovesMade()]++;
					} else if(board.getNumMovesMade() + numMovesLeftBasedOnPegIsolationSpanningTree + 2 > DEPTH_USED_IN_SEARCH + MEM_DEPTH_BACKWARDS) {
						debugIsolationFilterClosePerNumMoves2[board.getNumMovesMade()]++;
					}
				}
			}
		}

		//END APPLY FILTER AFTER WE NOTICE POSITION IS NOT FOUND:
		

		//TODO: if conway math says impossible: dont try.
		//And record impossible if recording is applicable...
		
		//Record position if worthwhile:
		//(Only record if it won't affect memory requirements too much)
		if(board.getNumMovesMade() <= MEM_DEPTH_FORWARDS) {
		
			if(recordedTriangles[board.getNumPiecesLeft()].containsKey(lookup) == false) {
				recordedTriangles[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, DEPTH_USED_IN_SEARCH));
				
				numRecordsSavedForDEBUG++;
			}
		}
			
		//END CHECKPOINT LOGIC
		
		
		//Basic filtering... TODO test if it makes it go faster
		/*
		if(board.getNumMovesMade() > 9 && LENGTH <= 8 && PositonFilterTests.isAnyPegUnCapturableOrUnmoveable(board.getTriangle())) {
			if(debugNumFilteredOut % 10 == 0) {
				System.out.println("Filtered:");
				board.draw();
				System.out.println();
				PositonFilterTests.printDistFromEverySpace(PositonFilterTests.getDistFromEverySpace(board.getTriangle()));
				debugNumFilteredOut++;
				System.out.println("Num filtered out so far: "  + debugNumFilteredOut);
				System.out.println("Num filtered so far: "  + debugNumFiltered);
			}

			return null;
		} else if(board.getNumMovesMade() > 9) {
			
			if(debugNumFiltered % 1000000 == 0) {
				System.out.println("Sample filter check has to work with:");
				board.draw();
			}
			debugNumFiltered++;
		}*/
		
		
		//TODO: does getFullMovesExcludingRepeatMoves make it faster?
		ArrayList<String> moves;
		if(curMaxDepth == 2) {
			//moves = board.getNecessaryFullBackwardsMovesToCheck(mustBe100percentMesonEfficient);
			//TODO: also only get Necessary moves here...
			moves = board.getFullMovesWith2MovesAwayFilters(mustBe100percentMesonEfficient);
			
		} else if(board.getNumMovesMade() + 1 <= MEM_DEPTH_FORWARDS
				|| board.getNumMovesMade() + 1 >= MAX_DEPTH_TOTAL - MEM_DEPTH_BACKWARDS) {
			moves = board.getFullMovesExcludingRepeatMoves(mustBe100percentMesonEfficient);
		} else {

			//TODO: Maybe this trick is only worth-while if there's 3 moves that aren't being recorded...
			//1 only gets reduces by around 30%, which is prob more trouble than it's worth.
			
			//Getting only the necessary moves
			//TODO: This trick doesn't mix well with prioritizing moves, so BE CAREFUL
			
			//Only use getNecessaryFullBackwardsMovesToCheck when
			// 1) position isn't saved
			// 2) next move isn't going to be checked against the backwards saved positions
			moves = board.getNecessaryFullBackwardsMovesToCheck(mustBe100percentMesonEfficient);
		}
		
		if(curMaxDepth > 1) {
			moves = PositonFilterTests.excludeMovesThatLeadToSameOutcome(board, moves);
		}
		
		for(int i=0; i<moves.size(); i++) {

			TriangleBoard possibleBest = getBestMoveList(board.doOneMove(moves.get(i)), curMaxDepth - 1, currentlyFoundSolution);

			if(possibleBest != null) {
				return possibleBest;
			}
			
		}
		
		return null;
	}
	
}
