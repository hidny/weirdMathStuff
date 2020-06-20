package paperfoldingproblem;

import java.util.ArrayList;

public class PaperFolderModel {


	public static int WIDTH = 4;
	public static int HEIGHT = 2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		int NUM_CARDS = WIDTH * HEIGHT;
		
		String cards[] = new String[NUM_CARDS];
		for(int i=0; i<cards.length; i++) {
			cards[i] = "" + i;
		}
		
		int numPermutations = 7*6*5*4*3*2;
		int numPermutations2 = factorial(NUM_CARDS - 1);
		
		
		//System.out.println(numPermutations + " vs " + numPermutations2);
		
		//Try all orderings with the top left card being first:
		
		int answer = 0;
		
		PERMUTAION:
		for(int m=0; m<numPermutations2; m++) {
			String perm[] = UtilityFunctions.UtilityFunctions.generatePermutation(cards, m);
			
			

			ArrayList<Integer> foldNumberRight = new ArrayList<Integer>();
			ArrayList<Integer> foldNumberTop = new ArrayList<Integer>();
			ArrayList<Integer> foldNumberBottom = new ArrayList<Integer>();
			ArrayList<Integer> foldNumberLeft = new ArrayList<Integer>();
		
			for(int indexCard=0; indexCard<perm.length; indexCard++) {
				
				int i = Integer.parseInt(perm[indexCard]) / WIDTH;
				int j = Integer.parseInt(perm[indexCard]) % WIDTH;
				
				
				//TODO TRY TO FIX UGLY COPY/PATE CODE
				if(PaperCardModel.hasFoldRightFinal(i, j)){
					
					int cardNumOtherSideOfRightFold = PaperCardModel.getCardNumOtherSideOfRightFinal(i, j);
					int indexCardOtherSideOfRightFold = getIndexCardNum(perm, cardNumOtherSideOfRightFold);
					
					if(indexCard < indexCardOtherSideOfRightFold) {
						foldNumberRight.add(indexCardOtherSideOfRightFold);
						
					} else if(indexCard > indexCardOtherSideOfRightFold) {

						//Check that indexCardOtherSideOfRightFold is correct
						//
						//and then remove the last fold
						if(foldNumberRight.get(foldNumberRight.size() - 1) != indexCard) {
							//System.out.println("NOPE RIGHT!");
							continue PERMUTAION;
						} else {
							foldNumberRight.remove(foldNumberRight.size() - 1);
						}
					} else {
						System.out.println("ERROR: same card should not be able to be on the other side of fold!");
						System.exit(1);
					}
				}
				
				if(PaperCardModel.hasFoldLeftFinal(i, j)){
					
					int cardNumOtherSideOfLeftFold = PaperCardModel.getCardNumOtherSideOfLeftFinal(i, j);
					int indexCardOtherSideOfLeftFold = getIndexCardNum(perm, cardNumOtherSideOfLeftFold);
					
					if(indexCard < indexCardOtherSideOfLeftFold) {
						foldNumberLeft.add(indexCardOtherSideOfLeftFold);
						
					} else if(indexCard > indexCardOtherSideOfLeftFold) {

						//Check that indexCardOtherSideOfLeftFold is correct
						//
						//and then remove the last fold
						if(foldNumberLeft.get(foldNumberLeft.size() - 1) != indexCard) {
							//System.out.println("NOPE LEFT!");
							continue PERMUTAION;
						} else {
							foldNumberLeft.remove(foldNumberLeft.size() - 1);
						}
					} else {
						System.out.println("ERROR: same card should not be able to be on the other side of fold! 2");
						System.exit(1);
					}
				}
				

				if(PaperCardModel.hasFoldTopFinal(i, j)){
					
					//System.out.println(i);
					//System.out.println(j);
					//System.out.println("ERROR: there shouldn't be any top fold folds!");
					//System.exit(1);

					int cardNumOtherSideOfTopFold = PaperCardModel.getCardNumOtherSideOfTopFinal(i, j);
					int indexCardOtherSideOfTopFold = getIndexCardNum(perm, cardNumOtherSideOfTopFold);
					
					if(indexCard < indexCardOtherSideOfTopFold) {
						foldNumberTop.add(indexCardOtherSideOfTopFold);
						
					} else if(indexCard > indexCardOtherSideOfTopFold) {

						//Check that indexCardOtherSideOfTopFold is correct
						//
						//and then remove the last fold
						if(foldNumberTop.get(foldNumberTop.size() - 1) != indexCard) {
							//System.out.println("NOPE TOP!");
							continue PERMUTAION;
						} else {
							foldNumberTop.remove(foldNumberTop.size() - 1);
						}
					} else {
						System.out.println("ERROR: same card should not be able to be on the other side of fold! 2");
						System.exit(1);
					}
				}
				
				

				if(PaperCardModel.hasFoldBottomFinal(i, j)){
					
					int cardNumOtherSideOfBottomFold = PaperCardModel.getCardNumOtherSideOfBottomFinal(i, j);
					int indexCardOtherSideOfBottomFold = getIndexCardNum(perm, cardNumOtherSideOfBottomFold);
					
					if(indexCard < indexCardOtherSideOfBottomFold) {
						foldNumberBottom.add(indexCardOtherSideOfBottomFold);
						
					} else if(indexCard > indexCardOtherSideOfBottomFold) {

						//Check that indexCardOtherSideOfBottomFold is correct
						//
						//and then remove the last fold
						if(foldNumberBottom.get(foldNumberBottom.size() - 1) != indexCard) {
							//System.out.println("NOPE BOTTOM!");
							continue PERMUTAION;
						} else {
							foldNumberBottom.remove(foldNumberBottom.size() - 1);
						}
					} else {
						System.out.println("ERROR: same card should not be able to be on the other side of fold! 2");
						System.exit(1);
					}
				}
				
			}
			
			if(foldNumberBottom.size() > 0 || 
					foldNumberTop.size() > 0 || foldNumberRight.size() > 0 || foldNumberLeft.size() > 0) {
				System.out.println("ERROR: size of fold Numbers didn't go down to 0");
				System.exit(1);
			}
			System.out.println("Possible answer:");
			for(int i=0; i<perm.length; i++) {
				System.out.print(perm[i] + " ");
			}
			System.out.println();
			
			
			
			answer++;
			//TODO: figure out sides of each fold
			//TODO: figure out if folds go into each other
		}
		
		
		System.out.println("Total number of ways found: " + answer);
		
		
		
	}
	
	public static int getIndexCardNum(String perm[], int cardNum) {
		int ret = -1;
		
		String tmp = "" + cardNum;
		for(int i=0; i<perm.length; i++) {
			if(tmp.equals(perm[i])) {
				return i;
			}
		}
		
		if(ret == -1) {
			System.out.println("ERROR: bad card num " + cardNum);
		}
		
		return ret;
	}
	
	public static void testFoldCount() {

		int numFoldsBottom = 0;  //EXPECT 8
		int numFoldsTop = 0;  //EXPECT 0
		int numFoldsRight = 0; //EXPECT 8
		int numFoldsLeft = 0; //EXPECT 4
		
		for(int i=0; i<HEIGHT; i++) {
			for(int j=0; j<WIDTH; j++) {
				
				if(PaperCardModel.hasFoldRightFinal(i, j)){
					numFoldsRight++;
				}

				if(PaperCardModel.hasFoldLeftFinal(i, j)){
					numFoldsLeft++;
				}

				if(PaperCardModel.hasFoldTopFinal(i, j)){
					numFoldsTop++;
				}

				if(PaperCardModel.hasFoldBottomFinal(i, j)){
					numFoldsBottom++;
				}
			}
		}
		
		System.out.println("Num folds bottom: " + numFoldsBottom);
		System.out.println("Num folds top: " + numFoldsTop);
		System.out.println("Num folds right: " + numFoldsRight);
		System.out.println("Num folds left: " + numFoldsLeft);
		//GOOD!
	}
	
	public static int factorial(int n) {
		if(n == 0) {
			return 1;
		} else return n* factorial(n - 1);
	}

	
	// public static String[] generatePermutation(String objects[], long permNumber) {
}

//Confirmed:
/*
Possible answer:
0 1 2 3 7 6 5 4  check!
Possible answer:
0 1 2 6 5 4 7 3  check!
Possible answer:
0 1 2 6 7 3 5 4  check!


Possible answer:
0 1 3 2 6 7 5 4 check!

 
Possible answer:
0 1 3 7 5 4 6 2 check!


Possible answer:
0 1 5 3 7 6 2 4 check!


Possible answer:
0 1 5 4 3 7 6 2 check!


Possible answer:
0 1 5 4 6 2 3 7 check!


Possible answer:
0 1 5 4 6 7 3 2 check!

 
Possible answer:
0 1 5 4 7 6 2 3 check! 


Possible answer:
0 1 5 6 2 3 7 4 check! 



Possible answer:
0 1 5 6 7 3 2 4 check!


Possible answer:
0 1 5 7 6 2 3 4 check!



Possible answer:
0 1 7 3 2 6 5 4 check!

 

Possible answer:
0 2 3 1 5 7 6 4 check!




Possible answer:
0 2 3 7 6 4 5 1 check!

Possible answer:
0 2 6 4 5 7 3 1 check!



Possible answer:
0 2 6 7 3 4 5 1 check!


Possible answer:
0 3 2 1 5 6 7 4 check!


Possible answer:
0 3 2 6 7 4 5 1 check!



Possible answer:
0 3 7 4 5 6 2 1 check!



Possible answer:
0 3 7 6 2 1 5 4 check! (hardest!)

 
Possible answer:
0 4 2 3 7 6 5 1 check!


Possible answer:
0 4 2 6 7 3 5 1 check!


Possible answer:
0 4 3 2 6 7 5 1 




Possible answer:
0 4 5 1 2 3 7 6 check!

 
 
 
 
 
Possible answer:
0 4 5 1 2 6 7 3 check!






Possible answer:
0 4 5 1 3 2 6 7 check!




Possible answer:
0 4 5 1 7 3 2 6 check!




Possible answer:
0 4 5 3 7 6 2 1





Possible answer:
0 4 5 6 2 3 7 1 check!






Possible answer:
0 4 5 6 7 3 2 1  check!





Possible answer:
0 4 5 7 6 2 3 1 check!

 
 
Possible answer:
0 4 6 7 5 1 3 2 check!

 
 
 

Possible answer:
0 4 7 3 2 6 5 1 check!


Possible answer:
0 4 7 6 5 1 2 3 check! 



Possible answer:
0 6 2 3 7 1 5 4 check!


Possible answer:
0 6 7 3 2 1 5 4 check!


Possible answer:
0 7 3 2 6 4 5 1 check! 



Possible answer:
0 7 6 2 3 1 5 4 check!

Total number of ways found: 40
*/



/*
OscarCunningham
OscarCunningham
1 day ago
Just to check my code, do people get 152 for the 3 by 3 case?



Metheth Propbut
Metheth Propbut
1 day ago
Yup I got 152 for 3 by 3!

1


OscarCunningham
OscarCunningham
1 day ago
@Metheth Propbut Yay!

(MT: I also got 152 for 3 by 3!... I don't know if it's right though...)


Hagen von Eitzen
Hagen von Eitzen
1 day ago
Dammit, I get 656 for 3x3.
Then again, I might be overlooking additional constraints not present when one dimension is 2



perldition
perldition
1 day ago
For the same purpose, what do you get for the 3x2 case?



OscarCunningham
OscarCunningham
1 day ago
@perldition 10

1

(Mtardibu: I got 10 for 3x2!!!)

Krzysztof Q
Krzysztof Q
1 day ago
guys, I want to learn to code, I am curious how long is you experience with coding so you were able to do this?



OscarCunningham
OscarCunningham
21 hours ago
@Krzysztof Q For this problem I found that the coding actually wasn't very hard. The tricky bit was working out how to represent the problem in code. If you want to get some experience using code to solve maths problems, I can recommend the Project Euler problems: https://projecteuler.net/.

*/