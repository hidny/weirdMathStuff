package triangleBoard5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;


//Bad ideas
//This could be more efficient if we could limit the length of the move... BUT IT WOULD BE MORE COMPLICATED!


//TODO: See what happens after implementing conway math... (and acknowledging that there's 4 position classes according to me...)
//TODO: Add your own logic to filter out impossible jump location, then impossible to finish location
//TODO: Look at paper to get even better filters
//(See loose paper for more clarification)


//TODO: make it possible for triangle 9 to record backwards moves...
	// I think it involves making get back wards moves more efficient
//IDEAS to make it happen:
// 1) Remove counter-clockwise capture cycles from get backwards moves
// 2) Disallow boards with too few pieces left
// 3) Output straight to triangle records to save space on the last depth
// 4) Use get NecessaryMoveList logic to reduce the number of positions output (Won't really help until depth 3 or 4 if idea 3 is implemented...)
// NOTE: By now, I'm hoping there's significant progress!
// 5) Identify and elim repeat moves in getBackwardsMoves???

public class TriangleSolveGetPosDepthDAwayFromSolution {


	public static int STANDARD_MEM_LIMIT = 19000000;
	
	public static void main(String args[]) {
		int MAX_DEPTH = 2;
		getPositionsDepthNAwayFromAGoal(8, 0, 0, MAX_DEPTH);
		//getPositionsDepthNAwayFromAGoal(8, 0, 0, 3); -> 13559160
	}

	
	public static HashSet<Long>[] getPositionsDepthNAwayFromAnyGoal(int triangleLength, int maxDepth) {

		initSavedPosForCurrentDir(triangleLength);

		HashSet<Long> endingPositionSearched = new HashSet<Long>();
		BackwardsTriangleBoard backwardsBoardStart;

		for(int endi=0; endi<triangleLength; endi++) {
			for(int endj=0; endj<=endi; endj++) {
				
				//SKIP SYMMETRIC ENDS
				backwardsBoardStart = new BackwardsTriangleBoard(triangleLength);
				backwardsBoardStart.addPiece(endi * triangleLength + endj);
				
				long lookupBackwards = backwardsBoardStart.getLookupNumber();
				
				if(endingPositionSearched.contains(lookupBackwards)) {
					continue;
				} else {
					endingPositionSearched.add(lookupBackwards);
				}
				//END SKIP SYMMETRIC ENDS
				
				addPositionsDepthNAwayFromAGoal(triangleLength, endi, endj, maxDepth);
				
				scratchOutAllDepthUsedInfoForNextIteration();
				
				System.out.println();
			}
		}


		HashSet<Long>[] savedPosAtDepthD = packageResultsDepthDaway(maxDepth, triangleLength);

		
		savedPosForCurrentSearchDir = null;
		
		return savedPosAtDepthD;
	}
	
	public static void scratchOutAllDepthUsedInfoForNextIteration() {
		for(int i=0; i<savedPosForCurrentSearchDir.length; i++) {
			Iterator<Long> it = savedPosForCurrentSearchDir[i].keySet().iterator();
			while(it.hasNext()) {
				long key = it.next();
				savedPosForCurrentSearchDir[i].get(key).scratchOutDepthUsedToFindRecord();
			}
		}
	}
	
	public static HashSet<Long>[] getPositionsDepthNAwayFromAGoal(int triangleLength, int endi, int endj, int maxDepth) {

		initSavedPosForCurrentDir(triangleLength);
		
		addPositionsDepthNAwayFromAGoal(triangleLength, endi, endj, maxDepth);

		HashSet<Long>[] savedPosAtDepthD = packageResultsDepthDaway(maxDepth, triangleLength);
		
		savedPosForCurrentSearchDir = null;
		
		return savedPosAtDepthD;
	}
	
	
	private static void addPositionsDepthNAwayFromAGoal(int triangleLength, int endi, int endj, int maxDepth) {
		
		boolean memLimitReached = false;
		int saveDepth;
		
		for(saveDepth = 0; saveDepth<=maxDepth; saveDepth++) {
			
			System.out.println("getPositionDepthNAwayFromGoal saveDepth of " + saveDepth + " (endi = " + endi + ", endj = " + endj + ")");

			numPosSavedForPreviousDepths = numPosSaveTotal;
			
			
			memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(triangleLength, saveDepth, endi, endj);
			
			
			System.out.println("End of search with depth " + saveDepth + " and triangle length " + triangleLength);
			System.out.println("Num records saved for prev depths: " + numPosSavedForPreviousDepths);
			System.out.println("Num records saved total: " + numPosSaveTotal);
			
			if(memLimitReached) {
				break;
			}
			
		}
		
		if(memLimitReached || saveDepth > maxDepth) {
			saveDepth--;
		}
		
	}
	
	
	private static int numFunctionCallForDEBUG = 0;
	private static int numPosSavedForPreviousDepths = 0;
	private static int numPosSaveTotal = 0;
	
	private static HashMap<Long, triangleRecord>[] savedPosForCurrentSearchDir;
	
	private static void initSavedPosForCurrentDir(int boardLength) {
		savedPosForCurrentSearchDir = new HashMap[utilFunctions.getTriangleNumber(boardLength)];
		for(int i=0; i<savedPosForCurrentSearchDir.length; i++) {
			savedPosForCurrentSearchDir[i] = new HashMap<Long, triangleRecord>();
		}
		
		numFunctionCallForDEBUG = 0;
		numPosSavedForPreviousDepths = 0;
		numPosSaveTotal = 0;
	}
	
	
	//Remove a peg and save all position depth D away for the answer:
	private static boolean doBackwardSearchAndSaveDeepAtDepthD(int triangleLength, int saveDepth, int endi, int endj) {
		
		BackwardsTriangleBoard board;

		board = new BackwardsTriangleBoard(triangleLength);
		board.addPiece(board.getCode(endi, endj));

		DEPTH_USED_IN_SEARCH = saveDepth;
		boolean memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(board, saveDepth);

		if(memLimitReached) {
			return true;
		} else {
			return false;
		}
	}

	private static int DEPTH_USED_IN_SEARCH = -1;
	
	private static boolean doBackwardSearchAndSaveDeepAtDepthD(BackwardsTriangleBoard board, int curMaxDepth) {
	
		//SANITY CHECK
		if(DEPTH_USED_IN_SEARCH != curMaxDepth + board.getNumMovesMade()) {
			System.out.println("ERROR: incorrect number of moves made in TriangleSolveOptimizedTrial");
			System.exit(1);
		}
		//END SANITY CHECK
		
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 1000 == 0) {
			//
			System.out.println("Mid-way backwards Search: Current depth remaining: " + curMaxDepth + " out of " + DEPTH_USED_IN_SEARCH);

			System.out.println("Mid-way backwards Search:  search with depth " + curMaxDepth + " and triangle length " + board.length());
			System.out.println("Mid-way backwards Search: Num records saved for prev depths: " + numPosSavedForPreviousDepths);
			System.out.println("Mid-way backwards Search: Num records saved total: " + numPosSaveTotal);
			System.out.println("Mid-way backwards Search: Num million times recursive function called: " + (numFunctionCallForDEBUG/1000000));
			board.draw();
		}
		

		long lookup = board.getLookupNumber();
		
		if( savedPosForCurrentSearchDir[board.getNumPiecesLeft()].containsKey(lookup)) {
			
			triangleRecord previouslyFoundNode = savedPosForCurrentSearchDir[board.getNumPiecesLeft()].get(lookup);
		
			if(board.getNumMovesMade() > previouslyFoundNode.getNumMovesToGetToPos()) {
					
				//System.out.println("Cutting short 0");
				return false;

			} else if(board.getNumMovesMade() == previouslyFoundNode.getNumMovesToGetToPos()){
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() == DEPTH_USED_IN_SEARCH) {
					return false;
				} else if(previouslyFoundNode.getDepthUsedToFindRecord() == triangleRecord.FOUND_IN_PREV_SEARCH_SAME_DEPTH) {
					return false;
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, DEPTH_USED_IN_SEARCH);
				}
				
				
			} else {
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() != triangleRecord.FOUND_IN_PREV_SEARCH_SAME_DEPTH) {
					System.err.println("ERROR: this case should not happen");
					System.out.println(board);
					System.exit(1);
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, DEPTH_USED_IN_SEARCH);
				}
			}
				
			
		} else {

			//TODO debug to get ideas about how to be more efficient
			/*if(board.getNumMovesMade() >= 4 && numPosSaveTotal % 10000 == 2) {
				System.out.println("DEBUG: sample backwards 4 board");
				board.draw();
			}*/
			//END TODO debug
			savedPosForCurrentSearchDir[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, DEPTH_USED_IN_SEARCH));
			numPosSaveTotal++;
			
			if(numPosSaveTotal > STANDARD_MEM_LIMIT) {
				return true;
			}
			
			if(curMaxDepth > 0) {
				System.out.println("ERROR: backwardSavedTrianglesForOppositeDir should only add positions of the last possible depth!");
				System.exit(1);
			}
		}
		

		if(curMaxDepth == 0) {
			return false;
			
		} else if(board.getNumPiecesLeft() == utilFunctions.getTriangleNumber(board.length()) - 1) {
			//If found solution, let the forward search find it.
			return false;
		}

		ArrayList<String> moves = board.getFullBackwardsMovesExcludingRepeatMoves();
		//ArrayList<String> moves = board.getNecessaryFullBackwardsMovesToCheck();

		/*//TODO:
		 * put getNecessaryFullBackwardsMovesToCheck is a slow filter, put it where the positions aren't being saved anymore
		 * This isn't the right place!
		 *
		if(DO_GET_NECESSARY_MOVES && not_recording_positions_next_move) {
			moves = board.getNecessaryFullBackwardsMovesToCheck();
		} else {
			moves = board.getFullBackwardsMoves();
		}*/
		
		for(int i=0; i<moves.size(); i++) {

			BackwardsTriangleBoard tmp = board.doOneBackwardsMove(moves.get(i));
			boolean wentOverMemLimit = doBackwardSearchAndSaveDeepAtDepthD(board.doOneBackwardsMove(moves.get(i)), curMaxDepth - 1);
			
			if(wentOverMemLimit) {
				return true;
			}
		}
		
		return false;
	}
	
	private static HashSet<Long>[] packageResultsDepthDaway(int saveDepth, int triangleLength) {
		HashSet<Long>[] savedPosAtDepthD = new HashSet[utilFunctions.getTriangleNumber(triangleLength)];
		
		int numCopied = 0;
		
		for(int i=0; i<savedPosForCurrentSearchDir.length; i++) {
			
			savedPosAtDepthD[i] = new HashSet<Long>();
			
			Iterator<Long> it = savedPosForCurrentSearchDir[i].keySet().iterator();
			while(it.hasNext()) {
				long key = it.next();
				
				if(savedPosForCurrentSearchDir[i].get(key).getNumMovesToGetToPos() == saveDepth) {
					if(savedPosAtDepthD[i].contains(key)) {
						System.out.println("ERROR: not supposed to happen!");
						System.exit(1);
					}
					savedPosAtDepthD[i].add(key);
					numCopied++;
					
				}
				
			}
			//TESTprintSortedResultsOfGetPosDepthDAway(savedPosAtDepthD, i);

		}

		System.out.println("Num records at depth " + saveDepth + " copied: " + numCopied);
		
		return savedPosAtDepthD;
	}
	
	public static void TESTprintSortedResultsOfGetPosDepthDAway(HashSet<Long>[] savedPosAtDepthD, int D) {
		Object array[] = savedPosAtDepthD[D].toArray();
		long arrayLong[] = new long[array.length];
		for(int j=0; j<array.length; j++) {
			arrayLong[j] = (Long)array[j];
		}
		
		for(int j=0; j<arrayLong.length; j++) {
			for(int k=j+1; k<arrayLong.length; k++) {
				if((Long)(arrayLong[k]) < (Long)(arrayLong[j])) {
					long tmp = arrayLong[j];
					arrayLong[j] = arrayLong[k];
					arrayLong[k] = tmp;
				}
			}
		}
		
		for(int j=0; j<arrayLong.length; j++) {
			System.out.println(D +": " + arrayLong[j]);
		}
	}
	
}
