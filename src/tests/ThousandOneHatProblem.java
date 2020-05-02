package tests;

import random.ArrayNDiffNumbers;

public class ThousandOneHatProblem {

	public static void main(String[] args) {
		int N = 1000;
		int NUM_TRIALS = 10000;
		int min = N;
		
		int temp;
		for(int i=0; i<NUM_TRIALS; i++) {
			temp = trial(N);
			if(temp<min) {
				min = temp;
			}
		}
		
		System.out.println("Done " + NUM_TRIALS + " trials.");
		System.out.println("Min: " + min + " out of " + N);
	
	
	}
	
	public static int trial(int numPeople) {
		int numHats = numPeople + 1;
		
		ArrayNDiffNumbers trial = new ArrayNDiffNumbers(numHats);
		
		int randShuffleArray[] = trial.getArray();
		/*System.out.println("The order:");
		for(int i=0; i<numHats; i++) {
			System.out.println(randShuffleArray[i]);
			if(i == 0) {
				System.out.println("------------");
			}
		}
		System.out.println("End of shuffle");*/
		
		boolean hatsTaken[] = new boolean[numHats];
		int hatSolution[] = new int[numPeople];
		int guesses[] = new int[numPeople];
		
		int hatRemoved = randShuffleArray[0];
		hatsTaken[numPeople] = false;
		
		for(int i=0; i<numPeople; i++) {
			hatsTaken[i] = false;
			hatSolution[i] = randShuffleArray[i + 1];
			guesses[i] = -1;
		}
		
		int numRight = 0;
		for(int i=0; i<numPeople; i++) {
			//System.out.println("i = " + i);
			int solutionKnowToPlayer[] = getSolutionKnown(i, hatSolution);
			int pick = getPick(i, solutionKnowToPlayer,  guesses);
			
			//System.out.println("Picking " + pick);
			
			if(pick == -1 || pick == 0) {
				System.out.println("ERROR!");
			}
			
			if(pick!= -1 && hatsTaken[pick - 1] == true) {
				System.out.println("ERROR: picked " + pick);
				System.out.println("ERROR: hat already taken");
				System.exit(1);
			}
			if(pick != -1) {
				hatsTaken[pick - 1] = true;
			}
			guesses[i] = pick;
			
			if(hatSolution[i] == pick) {
				numRight++;
			} else {
				System.out.println("Person " + i + " is wrong. Picked " + pick + " actual: " + hatSolution[i]);
			}
		}
		
		int count = 0;
		for(int i=0; i<numPeople+1; i++) {
			if(hatsTaken[i] == false && count < 1) {
				count++;
			} else if(hatsTaken[i] == false){
				System.out.println("ERROR: not enough hats!");
			}
		}
		
		System.out.println("Got " + numRight + " out of " + numPeople);
		
		return numRight;
	}
	
	public static int[] getSolutionKnown(int index, int hatSolution[]) {
		int ret[]=new int[hatSolution.length];
	
		for(int i=0; i<ret.length; i++) {
			if(i<=index) {
				ret[i] = -1;
			} else {
				ret[i] = hatSolution[i]; 
			}
		}
		return ret;
	}
	
//index starts at 0.
public static int getPick(int index, int hatSolutionKnown[], int guesses[]) {
	int numHats = hatSolutionKnown.length + 1;
	boolean hatsPicked[] = new boolean[numHats];
	for(int i=0; i<index; i++) {
		hatsPicked[guesses[i] - 1] = true;
	}
	
	boolean potentialAnswers[] = new boolean[hatSolutionKnown.length + 1];
	for(int i=0; i<potentialAnswers.length; i++) {
		potentialAnswers[i] = true;
	}
	//ignore the first guess because she probably got it wrong:
	for(int i=1; i<index; i++) {
		if(guesses[i] >= 1 && guesses[i] <= hatSolutionKnown.length + 1){
			potentialAnswers[guesses[i] - 1] = false;
		}
	}
	for(int i=index + 1; i<hatSolutionKnown.length; i++) {
		potentialAnswers[hatSolutionKnown[i] - 1] = false;
	}
	
	
	int a = -1;
	int b = -1;
	int c = -1;
	int d = -1;
	
	for(int i=0; i<hatSolutionKnown.length + 1; i++) {
		if(potentialAnswers[i] && a == -1) {
			a = i + 1;
		} else if(potentialAnswers[i] && b == -1) {
			b = i + 1;
		} else if(potentialAnswers[i] && c == -1) {
			c = i + 1;
		} else if(potentialAnswers[i] && d == -1) {
			d = i + 1;
		
		} else if(potentialAnswers[i]) {
			System.out.println("ERROR: too much!");
		}
	}
	
	//System.out.println(a + " " + b + " " + c + " " + d);
	if(index == 0) {
		//System.out.println("First pick");
		
		if(c != -1) {
			System.out.println("ERROR: c shouldn\'t be defined here.");
			System.exit(1);
			return -2;
		} else {
			if( a == -1 || b == -1) {
				System.out.println("ERROR: a or b is -1");
				System.out.println("A = " + a);
				System.out.println("B = " + b);
				
				System.exit(1);
				return -2;
			}
			
			return getModAdd(a, b, numHats);
			
		}
	} else if(index == guesses.length - 1) {
		//System.out.println("LAST PICK");
		for(int i=0; i<hatsPicked.length; i++) {
			if(hatsPicked[i] == false) {
				return i + 1;
			}
		}
		System.out.println("ERROR: expected to find a hat.");
		System.exit(1);
		return -3;
		
	} else {
		int first = guesses[0];
		int lastPersonsHat = hatSolutionKnown[hatSolutionKnown.length -1];
		
		if(first == lastPersonsHat || hatsPicked[hatSolutionKnown[hatSolutionKnown.length - 1] - 1] == false) {
			if(getModAdd(b, c, numHats) == first) {
				if(a != first) {
					return a;
				} else {
					return lastPersonsHat;
				}
			} else if(getModAdd(a, c, numHats) == first) {
				if(b != first) {
					return b;
				} else {
					return lastPersonsHat;
				}
			} else if(getModAdd(a, b, numHats) == first) {
				if(c != first) {
					return c;
				} else {
					return lastPersonsHat;
				}
			} else {
				System.out.println("ERROR: can\'t find hat with hint from first picker.");
				System.exit(1);
				return -3;
			}
		} else {
			//System.out.println("Last hat picked by someone other than the first guesser:");
			
			if(a == first) {
				a = d;
			} else if(b == first) {
				b = d;
			} else if(c == first) {
				c = d;
			} else if(d == first) {
				//do nothing
			} else {
				System.out.println("ERROR: expected one of the unknowns to be the first pick.");
				System.exit(1);
			}
			
			if(getModAdd(b, c, numHats) == first) {
				return a;
				
			} else if(getModAdd(a, c, numHats) == first) {
				return b;
				
			} else if(getModAdd(a, b, numHats) == first) {
				return c;
				
			} else {

				System.out.println("ERROR: expected one of the unknowns to be the first pick.(2)");
				System.exit(1);
				return -3;
			}
			
			
		}
	}
}

public static int getModAdd(int a, int b, int n) {
	return (a + b)%(n) + 1;
}

//TODO 1: use the sample excel sheet to find and immitate it here.
}
