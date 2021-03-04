package solitaire;

import java.util.HashMap;

//TODO: actually implement this!


///KEY to make it easy: solve it as if there's an infinite amount of free columns...
// That way all group of card moves move to a new column where it gets solved automatically
//And There's not that many states to keep track of
// just (NUM_SUITS*NUM_RANK + NUM_SUITS - 1) choose (NUM_SUITS - 1)
// =29 choose 2
// =406


//Why so little states?
//Answer:
//If you got 5R, 5G, and 5B done, we only really need to worry about the #moves to get there
// The order of moves beforehand doesn't matter!!!

//This is hard to prove...
//Maybe just do a simplified game with inf columns??


public class ComplexAdmissibleHeuristic {

	
	public static long getAdmissibleHeuristic(String lines[][], int done[], String cardsInReserve[]) {
		
		//Start Breadth first search here:
		//search sorted by # cards done...
		//(29 buckets:
		// 0 done
		// 1 card done
		//...
		// 20 cards done
		//...
		// 27 cards done
		
		//Enqueue orig lines
		
		//While a queue is not empty
		
			//Dequeue from lowest # bucket:
				
				//For every suit:
					//get # block moves for next lowest S
					//If next lowest won't go up automatically, charge 1 move to force it up.
					//If there's an auto-move to done before getting the the next lowest of S,
					// this is NOT the optimal path. Cancel it!
				

					//TODO: hard-copy lines here!
					//TODO: hard-copy done here!
		
					//Do auto-moves
					// update num done
					// record num done along with num moves if optimal (Record the current lines too!)
					// if combination of done is new, enqueue it to the correct bucket of #cards done.
		
					//Note about laziness:
					//For now, let's pretend they are flowers and do this as a proof of concept
					//(In other words, pretend they get done as soon as you see it...)
						//For fanciness: make every set of dragons that aren't done yet count as 1 move.
						//Note think about how to handle dragon in a better way LATER
				
				
		
		
		return AstarAlgo.GOAL_FOUND;
	}
	
	/*
	public static boolean[][] initCardsRdyToBeDone(String lines[][], int done[], String cardsInReserve[]) {
		boolean cardsRdyToBeDone[][] = new boolean[FasterState.NUM_SUITS][FasterState.NUM_RANKS];
		
		for(int i=0; i<done.length; i++) {
			for(int j=0; j<done[i]; j++) {
				cardsRdyToBeDone[i][j] = true;
			}
		}
		
		for(int i=0; i<cardsInReserve.length; i++) {
			
			String curCard = cardsInReserve[i];
			if(FasterState.isSuitedCard(curCard)) {
				cardsRdyToBeDone[FasterState.getSuitIndex(curCard)][FasterState.getCardRank(curCard)] = true;
			}
		}
		
		//Add cards in reserve and at bottom block of col to ready to be done:
		
		for(int i=0; i<FasterState.NUM_COLUMNS; i++) {
			int tmpIndex = getIndexBottomCardOfColumn(lines, i);
			
			if(getIndexBottomCardOfColumn(lines, i) != -1) {
				
				String curCard = lines[i][tmpIndex];
				if(FasterState.isSuitedCard(curCard)) {
					cardsRdyToBeDone[FasterState.getSuitIndex(curCard)][FasterState.getCardRank(curCard)] = true;
				}
			}
		}
		
		
		
		return cardsRdyToBeDone;
	}
*/
	
	//private static boolean isSuitedCard(String card) {
	
	//private static int getCardRank(String card) {
	
	//private static char getSuit(String card) {
	
	//Copy pasted from Faster State:
	private static int getIndexBottomCardOfColumn(String lines[][], int i) {
		for(int j=0; j<lines[i].length; j++) {
			if(lines[i][j].equals("")) {
				
				return j-1;
			}
		}
		
		return lines[i].length -1;
	}
	
	
	//TODO: move to main class...
	public static HashMap<String, ComplexHeuristicObject> setOfBestPaths = new HashMap<String, ComplexHeuristicObject>();
	
	public static ComplexHeuristicObject findMatching(ComplexHeuristicObject newObj) {
		
		if(setOfBestPaths.containsKey(newObj.toString())) {
			System.out.println("It contains it!");
			return setOfBestPaths.get(newObj.toString());
		}
		
		return null;
	}
	
	
	public static void replaceMatching(ComplexHeuristicObject newObj) {
		
		if(setOfBestPaths.containsKey(newObj.toString())) {
			setOfBestPaths.remove(newObj.toString());
		}
		
		setOfBestPaths.put(newObj.toString(), newObj);
	}
	
	public static boolean isMoreEfficientWay(ComplexHeuristicObject newObj) {

		int numDoneCurrent = newObj.getNumMoves();

		int numDoneBefore = -1;
		
		
		if(setOfBestPaths.containsKey(newObj.toString())) {
			numDoneBefore = setOfBestPaths.get(newObj.toString()).getNumMoves();
		}
		
		if(numDoneBefore == -1 || numDoneCurrent < numDoneBefore) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	//TODO: this is a little bit hacky...
	public int getNumMovesForGoal(int numRanks) {
		String completeString = numRanks + "/" + numRanks + "/" +numRanks;
		
		return setOfBestPaths.get(completeString).getNumMoves();
		
	}
	
	
	
	
	
//Copied from faster state:
	//(except I made it all static...)
	private static void doAutoMoves(String lines[][], int numDone[] , String cardsInReserve[]) {
		
		boolean autoRemovedCardInIteration;
		
		do {
			autoRemovedCardInIteration = false;
			
			int min = getMinCardNumNotAutoDealtWith(numDone);
			
			for(int i=0; i<cardsInReserve.length; i++) {
				
				String card = cardsInReserve[i];
				
				if(FasterState.isSuitedCard(card)) {
					if(FasterState.getCardRank(card) < min) {
						
						numDone = updateCardsDone(numDone, card);
						cardsInReserve[i] = "";
						autoRemovedCardInIteration = true;
					}
				} else if(FasterState.isFlower(card)) {
					
					cardsInReserve[i] = "";
					//not needed, but doesn't hurt too much:
					autoRemovedCardInIteration = true;
					
				}
			}
			
			for(int i=0; i<FasterState.NUM_COLUMNS; i++) {
	
				if(isColumnEmpty(lines, i) == false) {
					
					String card = getBottomOfColumn(lines, i);
					
					if(FasterState.isSuitedCard(card)) {
						if(FasterState.getCardRank(card) < min) {
							
							removeBottomOfColumn(lines, i);
							numDone = updateCardsDone(numDone, card);
							autoRemovedCardInIteration = true;
							
						}
						
					} else if(FasterState.isFlower(card)) {
						removeBottomOfColumn(lines, i);
						autoRemovedCardInIteration = true;
						
					}
					
				}
			}

		} while(autoRemovedCardInIteration);
		
	}

	private static int getMinCardNumNotAutoDealtWith(int numDone[]) {
		int minNum = Integer.MAX_VALUE;
		for(int i=0; i<FasterState.NUM_SUITS; i++) {
			if(minNum > numDone[i]) {
				minNum = numDone[i];
			}
		}
		
		return minNum + 2;
	}
	
	private static int[] updateCardsDone(int numDone[], String card) {
		if(FasterState.isSuitedCard(card) == false) {
			System.out.println("ERROR: card in updateCardsDone not even done");
			System.exit(1);
			
		}
		numDone[FasterState.getSuitIndex(card)]++;
		
		if(FasterState.getCardRank(card) != numDone[FasterState.getSuitIndex(card)]) {
			System.out.println("ERROR: setting card to done in the wrong order!");
			System.exit(1);
		}
	
		return numDone;
	}
	
	private static String[][] removeBottomOfColumn(String lines[][], int i) {
		
		
		if(isColumnEmpty(lines, i)) {
			System.out.println("ERROR: getting bottom of empty column");
			System.exit(1);
		}
		
		
		lines[i][getIndexBottomCardOfColumn(lines, i)] = "";
		
		return lines;
	}
	
	private static boolean isColumnEmpty(String lines[][], int i) {
		if(getIndexBottomCardOfColumn(lines, i) == -1) {
			return true;
		} else {
			return false;
		}
	}
	
	private static String getBottomOfColumn(String lines[][], int i) {
		
		if(isColumnEmpty(lines, i)) {
			System.out.println("ERROR: getting bottom of empty column");
			System.exit(1);
		}
		
		return lines[i][getIndexBottomCardOfColumn(lines, i)];
	}
//END copied from faster state
	
}
