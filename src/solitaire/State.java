package solitaire;


import java.util.ArrayList;

import number.IsNumber;

//TODO: implement AstarAlgo...
//Later
public class State implements AstarNode {

	public static final int HEIGHT = 5;
	public static final int EXTRA_SPACE = 10;
	public static final int NUM_COLUMNS = 8;
	public static final int NUM_SUITS = 3;
	public static final int NUM_RANKS = 9;
	
	public static final int NUM_GROUP_IDENTICAL = 4;

	public static final String IDENTICAL_CARDS[] = new String[] {"X", "Y", "Z"};
	public static final int RESERVE_SLOTS = IDENTICAL_CARDS.length;
	
	private int numDone[] = new int[NUM_SUITS];
	private String lines[][] = new String[HEIGHT + EXTRA_SPACE][NUM_COLUMNS];
	private String cardsInReserve[] = new String[RESERVE_SLOTS];
	private String lastMoveDescription = "";
	
	//Extra could figure out vars:
	//OPTIONAL
	/*private boolean flowerDone = false;
	private boolean XDone = false;
	private boolean YDone = false;
	private boolean ZDone = false;
	public int groupLength[] = new int[NUM_COLUMNS];
	public boolean emptyColumns[] = new boolean[NUM_COLUMNS];
	
	*/
	
	public State() {
		
	}
	
	public State(String deck[]) {
		
		String initLines[][] = new String[HEIGHT][NUM_COLUMNS];
		
		for(int i=0; i<initLines.length; i++) {
			for(int j=0; j<initLines[0].length; j++) {
				initLines[i][j] = deck[i * NUM_COLUMNS + j];
			}
		}
		initState(initLines);
	}
	
	public State(String initLines[][]) {
		initState(initLines);
		
		//System.out.println("Get moves:");
		getMoves();
	}
	
	private void initState(String initLines[][]) {
		for(int i=0; i<this.lines.length; i++) {
			for(int j=0; j<this.lines[0].length; j++) {
				this.lines[i][j] = "";
			}
		}
		
		for(int i=0; i<cardsInReserve.length; i++) {
			cardsInReserve[i] = "";
		}
		

		for(int i=0; i<NUM_SUITS; i++) {
			numDone[i] = 0;
		}
		
		
		for(int i=0; i<initLines.length; i++) {
			for(int j=0; j<initLines[0].length; j++) {
				this.lines[i][j] = initLines[i][j].toUpperCase();
				System.out.println(this.lines[i][j]);
			}
		}
		
		System.out.println(this.toString());

		System.out.println("Aftr auto movs");
		doAutoMoves();
		System.out.println(this.toString());
		
	}
	
	public void sortCardsInReserve() {
		for(int i=0; i<RESERVE_SLOTS; i++) {
			for(int j=i+1; j<RESERVE_SLOTS; j++) {
				if(cardsInReserve[i].compareTo(cardsInReserve[j]) < 0) {
					String tmp = cardsInReserve[i];
					 cardsInReserve[i] = cardsInReserve[j];
					 cardsInReserve[i] = tmp;
				}
			}
		}
	}
	
	//TODO: test
	public void sortCols() {
		
		//TODO: I might be able to make this faster...
		for(int i=0; i<NUM_COLUMNS; i++) {
			for(int j=i+1; j<NUM_COLUMNS; j++) {
				if(getColString(i).compareTo(getColString(j)) < 0) {
					swapColumns(i, j);
				}
			}
		}
	}
	
	public String getColString(int index) {
		String ret = "";
		for(int i=0; i<lines.length; i++) {
			ret += lines[i][index];
		}
		return ret;
	}
	
	public void swapColumns(int i, int j) {
		
		for(int k=0; k<lines.length; k++) {
			String tmp = lines[k][i];
			lines[k][i] = lines[k][j];
			lines[k][j] = tmp;
		}
	}
	
	
	///1R 1B 1G F1 YR /2R 2B 2G XR ZR /3R 3B 3G XX ZR /4R 4B 4G XR ZR /5R 5B 5G XR ZR /6R 6B 6G YR 9R /7R 7B 7G YR 9B /8R 8B 8G YR 9G /
	///8R 8B 8G YR 9G /7R 7B 7G YR 9B /6R 6B 6G YR 9R /5R 5B 5G XR ZR /4R 4B 4G XR ZR /3R 3B 3G XX ZR /2R 2B 2G XR ZR /1R 1B 1G F1 YR /
	public String toString() {
		String ret = "";
		sortCardsInReserve();
		sortCols();
		
		for(int i=0; i<cardsInReserve.length; i++) {
			ret += cardsInReserve[i] + " ";
		}
		ret += "/";
		
		for(int colIndex=0; colIndex<NUM_COLUMNS; colIndex++) {
			for(int rowIndex = 0; rowIndex<lines[colIndex].length; rowIndex++) {
				if(this.lines[rowIndex][colIndex].equals("") == false) {
					ret += lines[rowIndex][colIndex] + " ";
				}
			}
			ret += "/";
		}
		
		return ret;
	}
	
	//hashset will handle the rare case where there's a hash collision!!!
	/*
	 * Good news:
	 * https://www.javapedia.net/Set-and-its-implementations#qanda1242
	 * How HashSet checks for duplicates in Java?
	When you put an object into a HashSet, it uses hashcode value to determine where to put the object in the set. It also compares the objects hashcode value to other object's hashcode in the hashset. But two objects having same hashcode might not be equal. If the hashcode of two objects are equal then hashset uses equal() to see if the hashcode matched objects are really equal. And if they are equal the hashset knows that the new object is duplicate of something exist in the HashSet. And the add does not happen. The add() of hashcode returns false.
	 
	 https://stackoverflow.com/questions/12909325/hashset-collisions-in-java
	 
	 So, if I'm putting a bunch of strings of n-length words into a hash set, do I have to check the hashCode of that, and if that hashCode matches an already existing hashCode, then check to see if they are the the same string? Edit: Essentially, I'm asking what/how I am going to implement this check. � marcinx27 Oct 16 '12 at 7:25 
	no, you don't need to fiddle with hashCode of Strings in any way - see my answer � Christian Oct 16 '12 at 7:28

	His answer:

	I think you did not ask for hash collisions, right? The question is what happens when HashSet a and HashSet b are added into a single set e.g. by a.addAll(b).

	The answer is a will contain all elements and no duplicates. In case of Strings this means you can count the number of equal String from the sets with a.size() before add - a.size() after add + b.size().

	It does not even matter if some of the Strings have the same hash code but are not equal.

	share  improve this answer  follow 
	answered Oct 16 '12 at 7:27

	Christian
	 */

	public int hashCode() {
		//System.out.println("GETTING HASHCODE");
		return this.toString().hashCode();
	}
		
	
	public boolean  equals (Object object) {
		if(this.toString().equals(object.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//TODO: will need to test this. (OMG)
	public ArrayList<String> getMoves() {
		ArrayList<String> ret = new ArrayList<String>();
		
		//Move bottom of col to reserve
		if(hasEmptyReserveSlots()) {
			for(int j=0; j<NUM_COLUMNS; j++) {
				if(isColumnEmpty(j) == false) {
					
					String currentCard = lines[this.getIndexBottomCardOfColumn(j)][j];
					
					ret.add("move card " + currentCard + " of col " + j + " to reserve slot");
				}
			}
		}
		
		//Move bottom group (length n) to other col (if possible)
		for(int j=0; j<NUM_COLUMNS; j++) {
			
			int indexBottom = this.getIndexBottomCardOfColumn(j);
			int numGroup = getNumBottomGroupCards(j);
			for(int k=0; k<numGroup; k++) {
				String currentCard = lines[indexBottom-k][j];
				
				//j2 is the column move suggestion...
				for(int j2=0; j2<NUM_COLUMNS; j2++) {
					if(j == j2) {
						//can't move to the same column
						continue;
					}
					
					if(this.isColumnEmpty(j2)) {
						ret.add("move card " + currentCard + " of col " + j + " to empty column " + j2);
					} else {
						
						String cardBottomMove = lines[this.getIndexBottomCardOfColumn(j2)][j2];
						
						if(cardCouldLetCardMoveUnderIt(cardBottomMove, currentCard)) {
							ret.add("move card " + currentCard + " of col " + j + " to column " + j2);
						}
					}
					
				}
			}
			
		}
		
		//Move bottom CARD to done  (if possible)
		for(int j=0; j<NUM_COLUMNS; j++) {
			if(this.isColumnEmpty(j)) {
				continue;
			}
			String currentCard = lines[this.getIndexBottomCardOfColumn(j)][j];
			
			
			if(couldMoveSuitedCardToDone(currentCard)) {
					ret.add("move card " + currentCard + " of col " + j + " to done");
			}
		}
		
		//Move reserve to bottom of col (if possible)
		for(int i=0; i<this.RESERVE_SLOTS; i++) {
			if(this.cardsInReserve[i].equals("") == false) {
				
				String currentCard = this.cardsInReserve[i];
				
				for(int j=0; j<this.NUM_COLUMNS; j++) {
					
					if(this.isColumnEmpty(j)) {
						ret.add("move reserve card " + currentCard + " to empty column " + j);
					
					} else if(cardCouldLetCardMoveUnderIt(lines[this.getIndexBottomCardOfColumn(j)][j], currentCard)) {
						ret.add("move reserve card " + currentCard + " to non-empty column " + j);
						
					}
				}
			}
		}
		
		
		//Move reserve card to done (if possible)
		for(int j=0; j<RESERVE_SLOTS; j++) {
			if(this.cardsInReserve[j].equals("") == false) {
				
				String currentCard = this.cardsInReserve[j];
				
				if(couldMoveSuitedCardToDone(currentCard)) {
					ret.add("move reserve card " + currentCard + " to done");
					
				}
			}
		}
		
		//Move all GROUP of 4 X (or Y) (or Z) to reserve (if possible)
		for(int i=0; i<IDENTICAL_CARDS.length; i++) {
			
			boolean reserveSlotAvailableForDragon = false;
			
			String suitIDString = IDENTICAL_CARDS[i];
			char suitIDChar = suitIDString.charAt(0);
			
			for(int k=0; k<this.RESERVE_SLOTS; k++) {
				if(this.cardsInReserve[k].equals("") || cardIsPartofIdenticalGroup(this.cardsInReserve[k], suitIDChar)) {
					reserveSlotAvailableForDragon = true;
					break;
				}
			}
			
			if(reserveSlotAvailableForDragon == false) {
				continue;
			}
			
			int numFound = 0;
			
			for(int j=0; j<this.NUM_COLUMNS; j++) {
				if(this.isColumnEmpty(j) == false) {
					if(cardIsPartofIdenticalGroup(this.lines[this.getIndexBottomCardOfColumn(j)][j], suitIDChar)) {
						numFound++;
					}
				}
			}
			
			for(int j=0; j<RESERVE_SLOTS; j++) {
				if(cardIsPartofIdenticalGroup(this.cardsInReserve[j], suitIDChar)) {
					numFound++;
				}
			}
			if(numFound == NUM_GROUP_IDENTICAL) {
				ret.add("move all of group " + suitIDString + " to reserve slot");
			}
		}
		
	
		
		return ret;
	}
	
	

	
	
	public boolean couldMoveSuitedCardToDone(String card) {
		if(isSuitedCard(card)
			&& this.numDone[getSuitIndex(card)] == getCardRank(card) - 1) {
				return true;
		} else {
			return false;
		}
	}
	
	public static boolean cardIsPartofIdenticalGroup(String card, char groupLetter) {
		if(card.toUpperCase().contains((groupLetter + "").toUpperCase())) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean hasEmptyReserveSlots() {
		
		for(int i=0; i<this.cardsInReserve.length; i++) {
			if(this.cardsInReserve[i].equals("")) {
				return true;
			}
		}
		
		return false;
	}
	
	
	private String getBottomOfColumn(int j) {
		
		if(isColumnEmpty(j)) {
			System.out.println("ERROR: getting bottom of empty column");
			System.exit(1);
		}
		
		return lines[getIndexBottomCardOfColumn(j)][j];
	}


	private void removeBottomOfColumn(int j) {
		if(isColumnEmpty(j)) {
			System.out.println("ERROR: getting bottom of empty column");
			System.exit(1);
		}
		
		
		lines[getIndexBottomCardOfColumn(j)][j] = "";
	}
	

	private boolean isColumnEmpty(int j) {
		if(getIndexBottomCardOfColumn(j) == -1) {
			return true;
		} else {
			return false;
		}
	}
	

	
	//TODO: test!
	private int getNumBottomGroupCards(int j) {
		if(isColumnEmpty(j)) {
			return 0;
		}
		
		
		int ret = 1;
		
		int indexBottom = getIndexBottomCardOfColumn(j);
		String currentCard = lines[indexBottom][j];
		
		if(isSuitedCard(currentCard)) {
			
			for(int i2=indexBottom-1; i2>=0; i2--) {
				String nextCard = lines[i2][j];
				
				if(cardCouldLetCardMoveUnderIt(nextCard, currentCard)) {
					ret++;
					currentCard = nextCard;
				} else {
					break;
				}
				
			}
			
		} else {
			return 1;
		}
		
		return ret;
	}
	
	
	public static boolean cardCouldLetCardMoveUnderIt(String cardTop, String cardBottom) {
		if(isSuitedCard(cardTop) && isSuitedCard(cardBottom)
				&& getSuit(cardTop) != getSuit(cardBottom)
					&& getCardRank(cardTop) - 1 == getCardRank(cardBottom)) {
			return true;
		} else {
			return false;
		}
	}
	
	private int getIndexBottomCardOfColumn(int j) {
		for(int i=0; i<lines.length; i++) {
			if(lines[i][j].equals("")) {
				
				return i-1;
			}
		}
		
		return lines.length -1;
	}
	
	
	private int getMinCardNumNotAutoDealtWith() {
		int minNum = Integer.MAX_VALUE;
		for(int i=0; i<NUM_SUITS; i++) {
			if(minNum > this.numDone[i]) {
				minNum = this.numDone[i];
			}
		}
		
		return minNum + 2;
	}

	//Auto-move flower
	//Auto-move low cards to done when applicable
	//TODO: test
	private void doAutoMoves() {
		
		boolean autoRemovedCardInIteration;
		
		do {
			autoRemovedCardInIteration = false;
			
			int min = getMinCardNumNotAutoDealtWith();
			
			for(int i=0; i<this.cardsInReserve.length; i++) {
				
				String card = this.cardsInReserve[i];
				
				if(isSuitedCard(card)) {
					if(getCardRank(card) < min) {
						
						updateCardsDone(card);
						this.cardsInReserve[i] = "";
						autoRemovedCardInIteration = true;
					}
				} else if(isFlower(card)) {
					
					this.cardsInReserve[i] = "";
					autoRemovedCardInIteration = true;
					
				}
			}
			
			for(int j=0; j<NUM_COLUMNS; j++) {
	
				if(isColumnEmpty(j) == false) {
					
					String card = getBottomOfColumn(j);
					
					if(isSuitedCard(card)) {
						if(getCardRank(card) < min) {
							
							removeBottomOfColumn(j);
							updateCardsDone(card);
							autoRemovedCardInIteration = true;
							
						}
						
					} else if(isFlower(card)) {
						removeBottomOfColumn(j);
						autoRemovedCardInIteration = true;
						
					}
					
				}
			}

		} while(autoRemovedCardInIteration);
		
	}
	
	
	
	
	private void updateCardsDone(String card) {
		if(isSuitedCard(card) == false) {
			System.out.println("ERROR: card in updateCardsDone not even done");
			System.exit(1);
			
		}
		this.numDone[getSuitIndex(card)]++;
		
		if(this.getCardRank(card) != this.numDone[getSuitIndex(card)]) {
			System.out.println("ERROR: setting card to done in the wrong order!");
			System.exit(1);
		}
		
	}
	
	private static boolean isSuitedCard(String card) {
		if(card.length() == 2) {
			if(card.endsWith("B") || card.endsWith("G") || card.endsWith("R")) {
				return true;
			}
					
		}
		return false;
	}
	
	private static int getCardRank(String card) {
		return (int)(card.charAt(0) - '0');
	}
	
	//pr isSuitdCard
	private static char getSuit(String card) {
		return card.charAt(1);
	}
	
	private static int getSuitIndex(String card) {
		char suit = getSuit(card);
		if(suit =='B') {
			return 0;
			
		} else if(suit == 'G') {
			return 1;
			
		} else if(suit == 'R'){
			return 2;
		} else {
			System.out.println("ERROR: unknown suit");
			System.exit(1);
			
			return 3;
		}
		
	}
	
	private static boolean isFlower(String card) {
		if(card.contains("F")) {
			return true;
		} else {
			return false;
		}
	}
	
	

	//TODO:move based on move description
	/*
	 * 
ret.add("move card " + currentCard + " of col " + j + " to reserve slot");
ret.add("move card " + currentCard + " of col " + j + " to empty column " + j2);
ret.add("move card " + currentCard + " of col " + j + " to column " + j2);
ret.add("move card " + currentCard + " of col " + j + " to done");
ret.add("move reserve card " + currentCard + " to empty column " + j);
ret.add("move reserve card " + currentCard + " to non-empty column " + j);
ret.add("move reserve card " + currentCard + " to done");
ret.add("move all of group " + suitIDString + " to reserve slot");
	 */

	public State doMove(String moveDescription) {
		State newState = this.hardCopy();
		
		
		newState.lastMoveDescription = moveDescription;
		
		return newState.doMoveAndChangeState(moveDescription);
	}
	
	//This does the move and doesn't save the previous move...
	private State doMoveAndChangeState(String moveDescription) {
		//1st get card to use
		
		String desc = moveDescription.toUpperCase().trim();
		
		String token[] = desc.split(" ");
		
		//System.out.println();
		//System.out.println(this.toString());
		//System.out.println("Move description: " + desc);
		
		
		//TODO
		if(desc.startsWith("MOVE CARD ")) {
			//FIND card.
			String cardString = token[2];
			int cardI = getSuitedCardCoordI(cardString);
			int cardJ = getSuitedCardCoordJ(cardString);
			
			if(desc.contains("TO RESERVE SLOT")) {
				
				if(this.getIndexBottomCardOfColumn(cardJ) != cardI) {
					System.out.println("ERROR1: card not where it was expected!");
					System.out.println(this.getIndexBottomCardOfColumn(cardJ) + " vs " + cardI);
					System.out.println("CardJ: " + cardJ);
					System.exit(1);
				}
				

				this.lines[cardI][cardJ] = "";
				
				boolean foundReserveSpot = false;
				//TODO: constant EMPTY
				for(int k=0; k<RESERVE_SLOTS; k++) {
					if(this.cardsInReserve[k].equals("")) {
						foundReserveSpot = true;
						
						
						this.cardsInReserve[k] = cardString;
						break;
					}
				}
				
				if(foundReserveSpot == false) {
					System.out.println("ERROR2: no card in reserve slot!");
					System.exit(1);
					
				}
			
			} else if(desc.contains("TO DONE")) {

				if(this.getIndexBottomCardOfColumn(cardJ) != cardI) {
					System.out.println("ERROR3: card not where it was expected!");
					System.exit(1);
				}
				
				this.lines[cardI][cardJ] = "";
				//TODO: update done
				updateCardsDone(cardString);
			} else if(desc.contains("TO EMPTY COLUMN")) {
				
				int newColIndex = pint(token[token.length - 1]);
				
				if(this.isColumnEmpty(newColIndex) == false) {
					System.out.println("ERROR4: card column is not empty when I expected it to be empty!");
					System.exit(1);
				}

				//TODO: move group of cards.
				
				moveGroupOfCards(cardString, newColIndex);
				
			} else if(desc.contains("TO COLUMN") ) {
				
				int newColIndex = pint(token[token.length - 1]);
				
				if(this.isColumnEmpty(newColIndex) == true) {
					System.out.println("ERROR5: card column is empty when I expected it to have cards!");
					System.exit(1);
				}

				//TODO: move group of cards.
				
				moveGroupOfCards(cardString, newColIndex);
			} else {
				System.out.println("ERROR6L unknown command!");
				System.exit(1);
			}
			
		} else if(desc.startsWith("MOVE RESERVE CARD")) {

/*ret.add("move reserve card " + currentCard + " to empty column " + j);
ret.add("move reserve card " + currentCard + " to non-empty column " + j);
ret.add("move reserve card " + currentCard + " to done");
	*/
			String card = token[3];

			boolean foundCard = false;
			//Find card:
			for(int i=0; i<RESERVE_SLOTS; i++) {
				if(this.cardsInReserve[i].equals(card)) {
					this.cardsInReserve[i] = "";
					foundCard = true;
					break;
				}
			}
			
			if(foundCard == false) {
				System.out.println("ERROR7: could not find card in reserve slot");
			}
			
			if(desc.contains("TO EMPTY COLUMN")) {
				int newColIndex = pint(token[token.length - 1]);
				
				if(this.isColumnEmpty(newColIndex) == false) {
					System.out.println("ERROR8: card column is not empty when I expected it to be empty!");
					System.exit(1);
				}
				this.lines[0][newColIndex] = card;
				
			} else if(desc.contains("TO NON-EMPTY COLUMN")) {
				
				int newColIndex = pint(token[token.length - 1]);
				
				if(this.isColumnEmpty(newColIndex) == true) {
					System.out.println("ERROR9: card column is empty when I expected it to have cards!");
					System.exit(1);
				}
				this.lines[this.getIndexBottomCardOfColumn(newColIndex) + 1][newColIndex] = card;
				
				
			} else if(desc.contains("TO DONE")) {
				

				updateCardsDone(card);
				
			} else {
				System.out.println("ERROR 7-2: unknown reserve card command");
				System.exit(1);
			}
			
		} else if(desc.startsWith("MOVE ALL OF GROUP ")) {
			
			//ret.add("move all of group " + suitIDString + " to reserve slot");
			
			int numFound = 0;
			String dragonSuit = token[4];
			
			int indexToUse = -1;
			for(int i=0; i<RESERVE_SLOTS; i++) {
				if(this.cardsInReserve[i].contains(dragonSuit)) {
					indexToUse = i;
					break;
				} else if(this.cardsInReserve[i].equals("")) {
					indexToUse = i;
				}
			}
			
			if(indexToUse == -1) {
				System.out.println("ERROR 9: slot for dragon suit not found!");
				System.exit(1);
			}
			
			String cardToDelete = "";
			
			for(int i=0; i<RESERVE_SLOTS; i++) {
				if(this.cardsInReserve[i].contains(dragonSuit)) {
					if(cardToDelete.equals("")) {
						cardToDelete = this.cardsInReserve[i];
					} else if(cardToDelete.equals(this.cardsInReserve[i])) {
						System.out.println("ERROR 10: DRAGON aren't all the same!");
					}
					
					this.cardsInReserve[i] = "";
					numFound++;
				}
				
			}
			
			for(int j=0; j<NUM_COLUMNS; j++) {
				if(isColumnEmpty(j) == false) {
					
					String currentCard = lines[this.getIndexBottomCardOfColumn(j)][j];
					
					if(currentCard.contains(dragonSuit)) {
						
						if(cardToDelete.equals(currentCard)) {
							System.out.println("ERROR 10: DRAGON aren't all the same!");
						}
						
						lines[this.getIndexBottomCardOfColumn(j)][j] = "";
						numFound++;
					}
					
				}
			}

			this.cardsInReserve[indexToUse] = cardToDelete;

			if(numFound != this.NUM_GROUP_IDENTICAL) {
				System.out.println("ERROR 11: could not find all 4 dragons.");
				System.exit(1);
				
			}
			
		} else {
			System.out.println("ERROR: unknown move prefix!");
			System.exit(1);
		}

		
		//Rearrange cards: (TODO: don't do this in printed solution final path, so I can make sense of it)
		this.sortCardsInReserve();
		this.sortCols();
		
		//
		this.doAutoMoves();
		
		//System.out.println(this.toString());
		
		return this;
	}

	//ASTARNODE FUNCTIONS
	
	//Implements AstarNode
	public ArrayList<AstarNode> getNodeNeighbours() {
		ArrayList<String> moveDescriptions = this.getMoves();
		
		ArrayList<AstarNode> ret = new ArrayList<AstarNode>();
		
		
		
		//System.out.println("DEBUG: Get neighbours of:");
		//System.out.println(this.toString());
		//System.out.println();
		
		for(int i=0; i<moveDescriptions.size(); i++) {
			ret.add(this.doMove(moveDescriptions.get(i)));
		}
		
		//System.out.println();
		//System.out.println();
		//System.out.println();
		//System.out.println();
		
		return ret;
	}
	
	//Implements AstarNode
	//pos: nextPos is attainable by one move:
	public long getCostOfMove(AstarNode nextPos) {
		return 1;
	}
	
	//TODO: work on this (At least identify when done)
	public long getAdmissibleHeuristic(AstarNode goal) {
		
		for(int i=0; i<this.numDone.length; i++) {
			if(this.numDone[i] < NUM_RANKS) {
				return 1;
			}
		}
		
		for(int j=0; j<this.NUM_COLUMNS; j++) {
			if(this.isColumnEmpty(j) == false) {
				return 1;
			}
		}
		
		
		return AstarAlgo.GOAL_FOUND;
	}
	
	//END ASTAR NODE FUNCTIONS
	
	public void moveGroupOfCards(String cardString, int newColumnIndex) {
		int cardI = getSuitedCardCoordI(cardString);
		int cardJ = getSuitedCardCoordJ(cardString);
		
		
		int startNewColI = 0;
		
		if(this.isColumnEmpty(newColumnIndex) == false) {
			startNewColI = this.getIndexBottomCardOfColumn(newColumnIndex) + 1;
		}
		
		
		for(int k=0; lines[cardI + k][cardJ].equals("") == false; k++) {
			
			String currentCard = lines[cardI + k][cardJ];
			lines[cardI + k][cardJ] = "";
			
			if(k > 0 || startNewColI > 0) {
				if(cardCouldLetCardMoveUnderIt(this.getBottomOfColumn(newColumnIndex), currentCard) == false) {
					System.out.println("ERROR5: can't move top of group of cards under card!");
					System.exit(1);
				}
			}

			this.lines[startNewColI + k][newColumnIndex] = currentCard;
		}
	}
	
	//TODO: bug: will need to spcify the col because the dragons are identical!
	
	//TODO: maybe getCardDragon(String dragon, int column) {
	//
	public int getSuitedCardCoordI(String card) {
		return getCardCodeCoordInColumns(card) / NUM_COLUMNS;
	}
	
	public int getSuitedCardCoordJ(String card) {
		return getCardCodeCoordInColumns(card) %  NUM_COLUMNS;
	}
	
	public int getCardCodeCoordInColumns(String card) {
		for(int i=0; i<lines.length; i++) {
			for(int j=0; j<lines[0].length; j++) {
				if(lines[i][j].equals(card)) {
					return i * lines[0].length + j;
				}
			}
		}
		
		System.out.println("ERRROR: could not find Card");
		return -1;
	}
	
	
	
	
	public State hardCopy() {
		
		/*
		 * 
	private int numDone[] = new int[NUM_SUITS];
	private String lines[][] = new String[HEIGHT + EXTRA_SPACE][NUM_COLUMNS];
	private String cardsInReserve[] = new String[RESERVE_SLOTS];
	private String lastMoveDescription = "";
		 */
		
		State newState = new State();
		
		newState.numDone = new int[this.numDone.length];
		
		for(int i=0; i<this.numDone.length; i++) {
			newState.numDone[i] = this.numDone[i];
		}
		
		newState.lines = new String[this.lines.length][this.lines[0].length];
		
		for(int i=0; i<this.lines.length; i++) {
			for(int j=0; j<this.lines[0].length; j++) {
				newState.lines[i][j] = lines[i][j];
			}
		}
		newState.cardsInReserve = new String[this.cardsInReserve.length];
		
		for(int i=0; i<this.cardsInReserve.length; i++) {
			newState.cardsInReserve[i] = this.cardsInReserve[i];
		}

		//newState.lastMoveDescription = "";
		
		return newState;
	}
	
	public static int pint(String s) {
		if (IsNumber.isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			System.out.println("Error: (" + s + " is not a number");
			return -1;
		}
	}
	
	//PLAN:
	//SOLUTION:
	//STATE
	
	//PREV MOVE
	//STATE
	
	// PREV MOVE
	//STATE
	
	//...
	
	//PREV MOVE
	//STATE
}



//BRUTE FORCE solution already exists:
//https://www.reddit.com/r/shenzhenIO/comments/aqfn78/so_i_made_the_solver_for_shenzhen_solitaire_you/
//https://smankusors.com/projects/shenzhen_solitaire_solver/
/*
* in a nutshell, it just tries every combination moves, and then choose which results in less cards, and then do it again over and over until got nothing left.
* 
*/

//https://github.com/Smankusors/shenzhen_solitaire_solver

//Maybe be lazy and compare to his solutions?


//Another bot:
//https://github.com/oprypin/game-bots/blob/master/shenzhen-io/bot.cr

//Another:
// https://www.youtube.com/watch?v=VnkWLH7W8gc
// https://www.youtube.com/watch?v=kT7bEJQsVYk

