package solitaire;

public class Deck {

	public static int NUM_CARDS= 40;
	public static int NUM_SUITS = 3;
	public static int NUM_RANKS = 9;
	
	public static int NUM_MISCELLANEOUS = 4;
	
	public static void main(String args[]) {
		
		String deck[] = initDeck();
		for(int i=0; i<deck.length; i++) {
			System.out.println(deck[i]);
		}
		
		String shuffledDeck[];
		
		int count[][] = new int[NUM_CARDS][NUM_CARDS];
		
		for(int i=0; i<100000; i++) {
			Deck tmp = new Deck(deck);
			tmp.shuffle();
			
			
			
			for(int j=0; j<NUM_CARDS; j++) {
				
				for(int k=0; k<NUM_CARDS; k++) {
					if(deck[j].equals(tmp.deck[k])) {
						count[j][k]++;
						break;
					}
				}
				
			}
		}
		System.out.println();
		System.out.println("count test:");
		
		for(int j=0; j<NUM_CARDS; j++) {
			
			for(int k=0; k<NUM_CARDS; k++) {
				System.out.print(count[j][k] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static String[] initDeck() {
		String deck[] = new String[NUM_CARDS];
		
		
		for(int i=0; i<NUM_SUITS; i++) {
			for(int j=0; j<NUM_RANKS; j++) {
				if(i == 0) {
					deck[NUM_RANKS * i + j] = (j+1) + "R";
				} else if(i == 1){
					deck[NUM_RANKS * i + j] = (j+1) + "G";
				} else {
					deck[NUM_RANKS * i + j] = (j+1) + "B";
				}
			}
		}
		
		int curIndex = NUM_SUITS * NUM_RANKS;
		
		for(int i=0; i<NUM_MISCELLANEOUS; i++) {
			deck[curIndex + i] = "Z" /*+ (i+1)*/; //TODO: i+1 only to test mix...
		}
		curIndex+=NUM_MISCELLANEOUS;
		

		for(int i=0; i<NUM_MISCELLANEOUS; i++) {
			deck[curIndex + i] = "Y" /*+ (i+1)*/;
		}
		curIndex+=NUM_MISCELLANEOUS;
		

		for(int i=0; i<NUM_MISCELLANEOUS; i++) {
			deck[curIndex + i] = "X" /*+ (i+1)*/;
		}
		curIndex+=NUM_MISCELLANEOUS;
		
		deck[curIndex] = "F";
		
		return deck;
	}
	
	private String deck[];
	private int curIndex = 0;
	
	public Deck(String array[]) {
		deck = new String[array.length];
		
		for(int i=0; i<deck.length; i++) {
			deck[i] = array[i];
		}
	}
	
	
	public void shuffle() {
		this.curIndex = 0;
		for(int i=0; i<deck.length; i++) {
			String tmp = deck[i];
			
			int newIndex = (int)(Math.random() * (deck.length - i)) + i;
			
			deck[i] = deck[newIndex];
			deck[newIndex] = tmp;
		}
	}
	
	public String getNextCard() {
		String ret = deck[curIndex];
		curIndex++;
		return ret;
	}
}
