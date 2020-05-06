package triangleBoard5;

import java.util.ArrayList;
import java.util.HashMap;

public class TriangleSolveLongBackSaveThenForward {


	public static long STANDARD_MEM_LIMIT = 200000000L;
	
	public static void main(String args[]) {
		doOptimizedSearchBackSaveForwardSearch(7);
	}
	
	private static int numFunctionCallForDEBUG = 0;
	private static int numBackwardsRecordsSavedForCurrentDirForDEBUG = 0;
	
	
	public static HashMap<Long, triangleRecord>[] recordedBackwardsTrianglesForCurrentDir;
	
	public static void initRecordedBackwardsTriangles(int boardLength) {
		numBackwardsRecordsSavedForCurrentDirForDEBUG = 0;
		recordedBackwardsTrianglesForCurrentDir = new HashMap[utilFunctions.getTriangleNumber(boardLength)];
		for(int i=0; i<recordedBackwardsTrianglesForCurrentDir.length; i++) {
			recordedBackwardsTrianglesForCurrentDir[i] = new HashMap<Long, triangleRecord>();
		}
	}
	
	
	private static int numPrevBackwardsSavedTrianglesForOppositeDir;
	private static int numBackwardsSavedTrianglesForOppositeDir;
	
	public static HashMap<Long, triangleRecord>[] prevBackwardSavedTrianglesForOppositeDir;
	public static HashMap<Long, triangleRecord>[] backwardSavedTrianglesForOppositeDir;
	
	public static void initBackwardSavedTrianglesForOppositeDir(int boardLength) {
		numBackwardsSavedTrianglesForOppositeDir = 0;
		backwardSavedTrianglesForOppositeDir = new HashMap[utilFunctions.getTriangleNumber(boardLength)];
		for(int i=0; i<backwardSavedTrianglesForOppositeDir.length; i++) {
			backwardSavedTrianglesForOppositeDir[i] = new HashMap<Long, triangleRecord>();
		}
	}
	

	
	public static void doOptimizedSearchBackSaveForwardSearch(int triangleLength) {
		
		int saveDepth;
		for(saveDepth = 4; true; saveDepth++) {
			
			System.out.println("TRYING saveDepth of " + saveDepth);
			boolean memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(triangleLength, saveDepth);
			
			if(memLimitReached) {
				break;
			} else {
				
				numPrevBackwardsSavedTrianglesForOppositeDir = numBackwardsSavedTrianglesForOppositeDir;
				prevBackwardSavedTrianglesForOppositeDir = backwardSavedTrianglesForOppositeDir;
			}
			
		}
		saveDepth--;
		backwardSavedTrianglesForOppositeDir = prevBackwardSavedTrianglesForOppositeDir;
		prevBackwardSavedTrianglesForOppositeDir = null;
		
		
		System.out.println("Save depth: " + saveDepth);
		
		
		
		for(int curDepth=1; true; curDepth++) {
			
			if(curDepth > saveDepth) {
				//must increase saveDepth by 1... unfortunately
			}
			//TODO:
			break;
		}
		
	}
	
	//TODO: later: only go backwards on single end node...
	public static boolean doBackwardSearchAndSaveDeepAtDepthD(int triangleLength, int saveDepth) {
		

		initBackwardSavedTrianglesForOppositeDir(triangleLength);
		
		BackwardsTriangleBoard board;

		
		
		//for(int i=0; i<triangleLength; i++) {
		//	for(int j=0; j<=i; j++) {
		//What if the end goal is top:
				board = new BackwardsTriangleBoard(triangleLength);
				board.addPiece(board.getCode(0, 0));
				
				//TODO: Just initialize once... could save time (WARN: logic might be messed up though)
				initRecordedBackwardsTriangles(triangleLength);
				//END TODO
				
				boolean memLimitReached = doBackwardSearchAndSaveDeepAtDepthD(board, saveDepth);
				
				if(memLimitReached) {
					return true;
				}
				
				System.out.println("DONE");
				
				//TODO:
				System.exit(1);
		//	}
	//	}
		
		return false;
	}
	
	public static boolean doBackwardSearchAndSaveDeepAtDepthD(BackwardsTriangleBoard board, int saveDepthRemaining) {
	
		if(board.getNumMovesMade() > utilFunctions.getMaxDepthUsed(board, saveDepthRemaining)) {
			System.out.println("WHAT?");
			System.exit(1);
		}
		
		numFunctionCallForDEBUG++;
		if(numFunctionCallForDEBUG % 100000 == 0) {
			//System.out.println("FAST");

			System.out.println("Current depth: " + saveDepthRemaining + " out of " + utilFunctions.getMaxDepthUsed(board, saveDepthRemaining));

			//TODO: see what happens after implementing conway math...
			System.out.println("Num records saved for search: " + numBackwardsRecordsSavedForCurrentDirForDEBUG);
			System.out.println("Num records saved for search other direction: " + numBackwardsSavedTrianglesForOppositeDir);
			board.draw();
		}
		
		if(board.getNumPiecesLeft() == utilFunctions.getTriangleNumber(board.length()) - 1) {
			//If found solution, let the forward search find it.
			return true;
		}
		//TODO: SAVE HERE
		
		
		long lookup = board.getLookupNumber();
		
		if(  board.getNumPiecesLeft() <= utilFunctions.getTriangleNumber(board.length())  
				&& backwardSavedTrianglesForOppositeDir[board.getNumPiecesLeft()].containsKey(lookup) == false
				) {

			//IF: 1)possible and 2)IF 2*(pegs remaining) >= NumPegs
			// 3) not already saved
			//Save the position for a backwards search:
			
			
			//TODO: SAVING WONT WORK...
			backwardSavedTrianglesForOppositeDir[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, saveDepthRemaining));
			
			numBackwardsSavedTrianglesForOppositeDir++;
		}
			
	
		if(saveDepthRemaining == 0) {
			if(numBackwardsSavedTrianglesForOppositeDir > STANDARD_MEM_LIMIT) {
				return true;
			} else {	
				return false;
			}
		}

		/*
		if(recordedBackwardsTrianglesForCurrentDir[board.getNumPiecesLeft()].containsKey(lookup)) {
			
			triangleRecord previouslyFoundNode = recordedBackwardsTrianglesForCurrentDir[board.getNumPiecesLeft()].get(lookup);
			
			//TODO: figure this out maybe only look for records by the numMovesMade.
			//if(board.getNumMovesMade() > previouslyFoundNode.getNumMovesToGetToPos()) {
					
				//System.out.println("Cutting short 0");
				//return false;
			} else if(board.getNumMovesMade() == previouslyFoundNode.getNumMovesToGetToPos()){
				
				if(previouslyFoundNode.getDepthUsedToFindRecord() == utilFunctions.getMaxDepthUsed(board, saveDepthRemaining)) {
					return false;
				} else {
					previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, saveDepthRemaining);
				}
				
			} else {
				
				previouslyFoundNode.updateNumMovesToGetToPos(board.getNumMovesMade(), board, saveDepthRemaining);
			}
		}*/
		

		//TODO: if conway math says impossible: dont try.
		//And record impossible if recording is applicable...
		
		//Record position if worthwhile:
		//(Only record if it won't affect memory requirements too much)
		
		/*
		if(board.length() <= 6
				|| (utilFunctions.getTriangleNumber(board.length()) - board.getNumPiecesLeft() <= 6 || board.getNumMovesMade() < 10)
			) {
		
			if(recordedBackwardsTrianglesForCurrentDir[board.getNumPiecesLeft()].containsKey(lookup) == false) {

				recordedBackwardsTrianglesForCurrentDir[board.getNumPiecesLeft()].put(lookup, new triangleRecord(board.getNumMovesMade(), board, saveDepthRemaining));
				
				numBackwardsRecordsSavedForCurrentDirForDEBUG++;
			}
		}
		*/
		//END CHECKPOINT LOGIC
		
		
		
		//TODO: this could be more efficient if we could limit the length of the move...
		ArrayList<String> moves = board.getFullBackwardsMoves();
		//END TODO

		//System.out.println(moves.size());

		//for(int i=0; i<moves.size(); i++) {
		//	System.out.println(moves.get(i));
		//}
		//System.out.println(moves.size());
		
		for(int i=0; i<moves.size(); i++) {

			BackwardsTriangleBoard tmp = board.doOneBackwardsMove(moves.get(i));
			if(2 * (tmp.getNumPiecesLeft() + saveDepthRemaining - 1) <= utilFunctions.getTriangleNumber(board.length())){
				
				boolean wentOverMemLimit = doBackwardSearchAndSaveDeepAtDepthD(board.doOneBackwardsMove(moves.get(i)), saveDepthRemaining - 1);
				
				if(wentOverMemLimit) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	
}
