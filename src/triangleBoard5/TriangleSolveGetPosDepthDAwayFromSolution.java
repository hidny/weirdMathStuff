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

public class TriangleSolveGetPosDepthDAwayFromSolution {


	public static int STANDARD_MEM_LIMIT = 19000000;
	
	public static void main(String args[]) {
		getPositionDepthNAwayFromGoal(7, 0, 0, 4);
	}

	//TODO: maybe make this a param
	public static boolean ALLOW_SAME_POS_SAME_DEPTH_TO_ACTIVATE_GET_NECESSARY_MOVES = true;

	//public static int MAX_DEPTH = 4;
	
	public static HashSet<Long>[] getPositionDepthNAwayFromGoal(int triangleLength, int endi, int endj, int maxDepth) {
		
		
		initSavedPosForCurrentDir(triangleLength);
		boolean memLimitReached = false;
		int saveDepth;
		for(saveDepth = 0; saveDepth<=maxDepth; saveDepth++) {
			
			System.out.println("getPositionDepthNAwayFromGoal TRYING saveDepth of " + saveDepth);

			numPosSavedForPreviousDepths = numPosSaveTotal;
			
			
			memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(triangleLength, saveDepth, endi, endj);
			
			
			System.out.println("Debug: End of search with depth " + saveDepth + " and triangle length " + triangleLength);
			System.out.println("Debug: Num records saved for prev depths: " + numPosSavedForPreviousDepths);
			System.out.println("Debug: Num records saved total: " + numPosSaveTotal);
			
			if(memLimitReached) {
				break;
			}
			
		}
		if(memLimitReached || saveDepth > maxDepth) {
			saveDepth--;
		}
		
		
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
			

			//TODO: put in function
			//DEBUG PRINT:
			/*Object array[] = savedPosAtDepthD[i].toArray();
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
				System.out.println(i +": " + arrayLong[j]);
			}*/
			//END DEBUG PRINT:
			
		}
		
		if(memLimitReached == false) {
			System.out.println("Debug: end of search with depth " + saveDepth + " and triangle length " + triangleLength);
			System.out.println("Num records saved for prev depths: " + numPosSavedForPreviousDepths);
			System.out.println("Num records saved total: " + numPosSaveTotal);
		}
		
		savedPosForCurrentSearchDir = null;
		
		System.out.println("Num records at depth " + saveDepth + " copied: " + numCopied);
		
		return savedPosAtDepthD;
		
		
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


		boolean memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(board, saveDepth);

		if(memLimitReached) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean doBackwardSearchAndSaveDeepAtDepthD(BackwardsTriangleBoard board, int curMaxDepth) {
	
		if(board.getNumMovesMade() > utilFunctions.getMaxDepthUsed(board, curMaxDepth)) {
			System.out.println("WHAT?");
			System.exit(1);
		}
		
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 10000000 == 0) {
			System.out.println("Current depth: " + curMaxDepth + " out of " + utilFunctions.getMaxDepthUsed(board, curMaxDepth));

			System.out.println("Debug: search with depth " + curMaxDepth + " and triangle length " + board.length());
			System.out.println("Num records saved for prev depths: " + numPosSavedForPreviousDepths);
			System.out.println("Num records saved total: " + numPosSaveTotal);
			System.out.println("Num million times recursive function called: " + (numFunctionCallForDEBUG/1000000));
			board.draw();
		}
		
		
		long lookup = board.getLookupNumber();
		
		if( savedPosForCurrentSearchDir[board.getNumPiecesLeft()].containsKey(lookup)) {
			
			//TODO: DEBUG 2 exceptions:
			if(board.getNumPiecesLeft() == 13 && lookup == 15649720) {
				System.out.println("looking at exceptional position that was already found 1: ");
				board.draw();
			} else if(board.getNumPiecesLeft() == 12 && lookup == 435021) {
				System.out.println("looking at exceptional position that was already found 2: ");
				board.draw();
			}
			
			triangleRecord previouslyFoundNode = savedPosForCurrentSearchDir[board.getNumPiecesLeft()].get(lookup);
		
			if(board.getNumMovesMade() > previouslyFoundNode.getNumMovesToGetToPos()) {
					
				//System.out.println("Cutting short 0");
				return false;
			} else if(board.getNumMovesMade() == previouslyFoundNode.getNumMovesToGetToPos()){
				//TODO: AHA! If you want to make getNecessaryFullBackwardsMovesToCheck,
				//you will need to let thru every previously found depth...
				//I advise only doing this for testing!
				if(previouslyFoundNode.getDepthUsedToFindRecord() == utilFunctions.getMaxDepthUsed(board, curMaxDepth)) {
					if(ALLOW_SAME_POS_SAME_DEPTH_TO_ACTIVATE_GET_NECESSARY_MOVES == false) {
						return false;
					}
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, curMaxDepth);
				}
				
			} else {
				
				System.err.println("ERROR: this case should not happen");
				System.out.println(board);
				System.exit(1);
			}
				
			
		} else {

			
			numPosSaveTotal++;
			savedPosForCurrentSearchDir[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, curMaxDepth));


			//Two boards that aren't found with necessary moves
			//12: 435021
			//13: 15649720
			if(board.getNumPiecesLeft() == 13 && lookup == 15649720) {
				System.out.println("looking at exceptional position 1: ");
				board.draw();
			} else if(board.getNumPiecesLeft() == 12 && lookup == 435021) {
				System.out.println("looking at exceptional position 2: ");
				board.draw();
			}
			
			
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
			System.out.println("ERROR: tried to save depth " + utilFunctions.getMaxDepthUsed(board, curMaxDepth) + "and found a solution");
			System.exit(1);
			//If found solution, let the forward search find it.
			return true;
		}

		//Two boards that aren't found with necessary moves
		//12: 435021
		//13: 15649720
		ArrayList<String> moves;
		if(ALLOW_SAME_POS_SAME_DEPTH_TO_ACTIVATE_GET_NECESSARY_MOVES == false) {
			moves = board.getFullBackwardsMoves();
		} else {
			moves = board.getNecessaryFullBackwardsMovesToCheck();
		}
		
		for(int i=0; i<moves.size(); i++) {

			BackwardsTriangleBoard tmp = board.doOneBackwardsMove(moves.get(i));
			boolean wentOverMemLimit = doBackwardSearchAndSaveDeepAtDepthD(board.doOneBackwardsMove(moves.get(i)), curMaxDepth - 1);
			
			if(wentOverMemLimit) {
				return true;
			}
		}
		
		return false;
	}
	
	
	
}
