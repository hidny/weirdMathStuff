package cardFlipPuzzle;


//Form matt parker's maths problem (#4)

//Basically a 2 player game where player 1 closes their eyes and the player 2 has N cards arranged in a row flipped however they want
// 
// Player 1 says which card to flip every turn and the game ends when all cards are fliped down

//I know that you can guarantee to win in 2^N - 1 turns, but I don't know how many winning
//trails there are or how they scale. I'm going to write a program to find out...

//I got the idea that it's like traversing a hyper-cube, but it doesn't help with counting the number of ways...

//Found it:
//A003043		Number of Hamiltonian paths (or Gray codes) on n-cube with a marked starting node.

//They got it up until n=5, and I got it until n=5... could I do better?

//for n=6, the answer doesn't even fit into a long (answer > 2^64), so I don't know...
/*
 * From site:
n		a(n)
1		1
2		2
3		18
4		5712
5		5859364320


 */

/*
 * What I thought about sending the puzzle site:
 * "Hi MATT PARKER'S MATHS PUZZLES,

CARD PUZZLE (MPMP PUZZLE 4)

I decided to challenge myself with answering a slightly different question to this week's puzzle.
The slightly different question is: given there's N cards, how many different optimal sequences of moves are there?

I wrote up a program that does it and then found the answer on OEIS:
A003043	"Number of Hamiltonian paths (or Gray codes) on n-cube with a marked starting node"

What's really surprising to me initially is that we still don't know the answer to N=6.
It goes:
n		a(n)
1		1
2		2
3		18
4		5712
5		5859364320
6               ????   > 2^64

I hope you find this interesting.

Michael
"
 */
public class cardFlipPuzzle {

	
	
	public static void main(String[] args) {
		

		for(int i=1; i<6; i++) {
			System.out.println(getFactorial(i));
			
			long tmp = getNumSolutions(i);
			System.out.println("Num cards: "  + i);
			System.out.println("Num solutions (should be a multiple of " + i + "!): " + tmp);
		}
		
		
	}
	

	private static boolean flipStatesFound[];
	private static long maxDepth;
	
	
	
	public static long getFactorial(long n) {
		if(n==0) {
			return 1;
		} else {
			return n * getFactorial(n-1);
		}
	}
	
	
	
	
	
	//Brute force:
	//TODO: make it a rule to flip card 1 then 2 then 3... then multiply answer by N!
	//I think this is limited, but I'll find the pattern by then...
	
	public static long getNumSolutions(int numCards) {
		
		flipStatesFound = new boolean[(int)Math.pow(2,  numCards)];
		
		maxDepth = (int)Math.pow(2,  numCards) - 1;
		
		for(int i=0; i<flipStatesFound.length; i++) {
			flipStatesFound[i] = false;
		}
		
		int startLocation= 0;
		flipStatesFound[startLocation] = true;
		
		
			
		return getFactorial(numCards) * getNumSolutionDepthFirstAndOrdered(numCards, startLocation, 0, 0);
	}
	
	
	public static long getNumSolutionDepthFirstAndOrdered(int numCards, int location, int depth, int numCardsActivated) {
		
		if(depth == maxDepth) {
			return 1;
		}
		
		long sum = 0;
		
		for(int i=0; i<numCards && i <= numCardsActivated; i++) {
			
			int newLocation = location ^ (1 << i);
			
			if(flipStatesFound[newLocation] == false) {
				
				flipStatesFound[newLocation] = true;
				if(i == numCardsActivated) {
					sum += getNumSolutionDepthFirstAndOrdered(numCards, newLocation, depth + 1, numCardsActivated + 1);	
				} else {
					sum += getNumSolutionDepthFirstAndOrdered(numCards, newLocation, depth + 1, numCardsActivated);	
				}
				flipStatesFound[newLocation] = false;
				
			}
			
			
		}
		
		return sum;
	}

	
	//BRUTE FORCE
	public static long getNumSolutionDepthFirst(int numCards, int location, int depth) {
		
		if(depth == maxDepth) {
			return 1;
		}
		
		long sum = 0;
		
		for(int i=0; i<numCards; i++) {
			
			int newLocation = location ^ (1 << i);
			
			if(flipStatesFound[newLocation] == false) {
				
				flipStatesFound[newLocation] = true;
				sum += getNumSolutionDepthFirst(numCards, newLocation, depth + 1);
				flipStatesFound[newLocation] = false;
				
			}
			
		}
		
		
		return sum;
	}
	

}

/*
 * 
1,2,18,5712

A003043		Number of Hamiltonian paths (or Gray codes) on n-cube with a marked starting node.
(Formerly M2112)		+30

COMMENTS	
More precisely, this is the number of ways of making a list of the 2^n nodes of the n-cube, with a distinguished starting position and a direction, such that each node is adjacent to the previous one. The final node may or may not be adjacent to the first. Finally, divide by 2^n since the starting node really doesn't matter.

Also, the number of strings s of length 2^n - 1 over the alphabet {1,2,...,n} with the property that every contiguous subblock has some letter that appears an odd number of times.

REFERENCES	
M. Gardner, Mathematical Games, Sci. Amer. Vol. 228 (No. 4, Apr. 1973), p. 111.

N. J. A. Sloane and Simon Plouffe, The Encyclopedia of Integer Sequences, Academic Press, 1995 (includes this sequence).

LINKS	
Table of n, a(n) for n=1..5.

Vladimir Shevelev, Combinatorial minors of matrix functions and their applications, arXiv:1105.3154 [math.CO], 2011-2014.

Vladimir Shevelev, Combinatorial minors of matrix functions and their applications, Zesz. Nauk. PS., Mat. Stosow., Zeszyt 4, pp. 5-16. (2014).

FORMULA	
a(n) = A091299(n)/2^n.

CROSSREFS	
Cf. A091299, A006069, A006070, A003042, A066037, A091302, A179926.

KEYWORD	
nonn,hard,more

AUTHOR	
N. J. A. Sloane

EXTENSIONS	
a(5) (from A091299) from Max Alekseyev, Jul 09 2006

Alternative description added by Jeffrey Shallit, Feb 02 2013

STATUS	
approved
*/

//OMG: I was taught by a Jeffrey Shallit from Waterloo! It's the same guy!
