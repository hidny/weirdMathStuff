package triangleBoard5;

import java.util.ArrayList;
import java.util.HashMap;


//Bad ideas
//This could be more efficient if we could limit the length of the move... BUT IT WOULD BE MORE COMPLICATED!


//TODO: See what happens after implementing conway math... (and acknowledging that there's 4 position classes according to me...)
//TODO: Add your own logic to filter out impossible jump location, then impossible to finish location
//TODO: Look at paper to get even better filters
//(See loose paper for more clarification)


//TODO: DO A SIMPLE BFS!!!!
public class TriangleSolveLongBackSaveThenForward {


	public static long STANDARD_MEM_LIMIT = 200000000L;
	
	public static void main(String args[]) {
		getPositionDepthNAwayFromGoal(7);
	}
	

	private static int numFunctionCallForDEBUG = 0;
	private static int numPosSavedForPreviousDepths = 0;
	private static int numPosSaveTotal = 0;
	
	public static HashMap<Long, triangleRecord>[] savedPosForCurrentSearchDir;
	
	public static void initSavedPosForCurrentDir(int boardLength) {
		savedPosForCurrentSearchDir = new HashMap[utilFunctions.getTriangleNumber(boardLength)];
		for(int i=0; i<savedPosForCurrentSearchDir.length; i++) {
			savedPosForCurrentSearchDir[i] = new HashMap<Long, triangleRecord>();
		}
	}
	

	//TODO: try all start and end positions (filter out symmetric though)
	public static void getPositionDepthNAwayFromGoal(int triangleLength) {
		
		
		initSavedPosForCurrentDir(triangleLength);
		
		int endi=0;
		int endj=0;
		
		int saveDepth;
		for(saveDepth = 0; saveDepth<=3; saveDepth++) {
			
			System.out.println("TRYING saveDepth of " + saveDepth);

			numPosSavedForPreviousDepths = numPosSaveTotal;
			
			boolean memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(triangleLength, saveDepth, endi, endj);
			
			if(memLimitReached) {
				break;
			} else {
				
				//TODO: setup for opposite search
			}
			
		}
		saveDepth--;
		savedPosForCurrentSearchDir = null;
		
		
	}
	
	//Remove a peg and save all position depth D away for the answer:
	public static boolean doBackwardSearchAndSaveDeepAtDepthD(int triangleLength, int saveDepth, int endi, int endj) {
		
		BackwardsTriangleBoard board;

		board = new BackwardsTriangleBoard(triangleLength);
		board.addPiece(board.getCode(endi, endj));


		boolean memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(board, saveDepth);

		System.out.println("Debug: end of search with depth " + saveDepth + " and triangle length " + triangleLength);
		System.out.println("Num records saved for prev depths: " + numPosSavedForPreviousDepths);
		System.out.println("Num records saved total: " + numPosSaveTotal);
		
		if(memLimitReached) {
			return true;
		}
		
		System.out.println("DONE");
	
		
		return false;
	}
	
	public static boolean doBackwardSearchAndSaveDeepAtDepthD(BackwardsTriangleBoard board, int curMaxDepth) {
	
		if(board.getNumMovesMade() > utilFunctions.getMaxDepthUsed(board, curMaxDepth)) {
			System.out.println("WHAT?");
			System.exit(1);
		}
		
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 10000000 == 0) {
			//System.out.println("FAST");

			//System.out.println("Current depth: " + curMaxDepth + " out of " + utilFunctions.getMaxDepthUsed(board, curMaxDepth));

			//System.out.println("Debug: end of search with depth " + curMaxDepth + " and triangle length " + board.length());
			//System.out.println("Num records saved for prev depths: " + numPosSavedForPreviousDepths);
			//System.out.println("Num records saved total: " + numPosSaveTotal);
			//board.draw();
		}
		
		
		long lookup = board.getLookupNumber();
		
		if( savedPosForCurrentSearchDir[board.getNumPiecesLeft()].containsKey(lookup)) {
			
			triangleRecord previouslyFoundNode = savedPosForCurrentSearchDir[board.getNumPiecesLeft()].get(lookup);
		
			if(board.getNumMovesMade() > previouslyFoundNode.getNumMovesToGetToPos()) {
					
				//System.out.println("Cutting short 0");
				return false;
			} else if(board.getNumMovesMade() == previouslyFoundNode.getNumMovesToGetToPos()){
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() == utilFunctions.getMaxDepthUsed(board, curMaxDepth)) {
					return false;
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, curMaxDepth);
				}
				
			} else {
				
				System.err.println("ERROR: this case should not happen");
				System.exit(1);
			}
				
			
		} else {

			numPosSaveTotal++;
			savedPosForCurrentSearchDir[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, curMaxDepth));
			
			//board.draw();
			
			if(curMaxDepth > 0) {
				System.out.println("ERROR: backwardSavedTrianglesForOppositeDir should only add positions of the last possible depth!");
				System.exit(1);
			}
		}
		
	
		if(curMaxDepth == 0) {
			if(numPosSaveTotal > STANDARD_MEM_LIMIT) {
				return true;
			} else {	
				return false;
			}
		} else if(board.getNumPiecesLeft() == utilFunctions.getTriangleNumber(board.length()) - 1) {
			//If found solution, let the forward search find it.
			return true;
		}

		
		//TODO: why aren't they the SAME???
		//ArrayList<String> moves = board.getFullBackwardsMoves();
		ArrayList<String> moves = board.getNecessaryFullBackwardsMovesToCheck();
		
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
