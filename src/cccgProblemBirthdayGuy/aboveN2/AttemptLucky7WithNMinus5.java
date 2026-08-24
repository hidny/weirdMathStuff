package cccgProblemBirthdayGuy.aboveN2;

import cccgProblemBirthdayGuy.GetMinPerimeterCornersRefined;

public class AttemptLucky7WithNMinus5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//TODO: prep first 20...

		int LENGTH = 100000;

		int trials[][] = GetMinPerimeterCornersRefined.dynamicProgrammingTrials(2, LENGTH);
		
		//Guess:
		// Solution of the form: (N-5)(N+5) + 3*2 + 1*1
		
		//perimeter extra: 3+2 + 1+1 = 7
		//best with 7 is only 12
		
		for(int i=0; i<20; i++) {
			System.out.println(i + " : " + trials[1][i]);
		}
		
		//System.exit(1);
		
		//Candidates that might get in way: (N-5-b + c1)(N+5+b+c2) + a * b is better
		// where c1 + c2 <=7 
		
		
		
		int debugNumTries = 0;
		int debugNumClose = 0;
		
		long finalN = -1;
		long finalTarget = -1;
		
		DONE:
		for(int N=100; true; N++) {
			
			System.out.println("N: " + N);
			
			long target = (N - 5)*(N+5)+7;
			
			for(int b=1; (N-5-b+7)*(N+5+b) >= target; b++) {
				for(int c1=0; c1<=7; c1++) {
					
					if((N-5-b + c1) * (N+5+b) > target) {
						//System.out.println("Continue");
						continue;
					}
					
					for(int c2=0; c2+c1<=7; c2++) {
						if((N-5-b + c1) * (N+5+b + c2) > target) {
							//System.out.println("break2");
							break;
						}
						//System.out.println("" + N + ", " + b + "," + c1 + "," + c2);
						
						int strike = (N-5-b + c1) * (N+5+b + c2);
						
						long closeness = target-strike;
						if(closeness <= 12) {
							System.out.println("***********");
							System.out.println("" + N + ", " + b + "," + c1 + "," + c2 + ": " + strike);
							System.out.println("" + N + ", " + b + "," + c1 + "," + c2 + ": " + (closeness));
							debugNumClose++;
							
							if(trials[1][(int)closeness] <= 7 || closeness == 0) {
								System.out.println("Nope!");
								System.out.println("***********");
								continue DONE;
							} else {

								System.out.println("***********");
							}
						}
						debugNumTries++;
						
						
					}
				}
			}
			
			//TODO: what if c1+ c2 < 0. Is that possible?
			finalTarget = target;
			finalN = N;
			
			//TODO:
			break;
		}
		
		System.out.println("debugNumTries: " + debugNumTries);
		System.out.println("debugNumClose: " + debugNumClose);
		
		System.out.println("Final target: " + finalTarget);
		
		System.out.println("(N-5)(N+5) + 7 solutions: " + (2*finalN + 7));
		System.out.println("trials[0][finalTarget]: " + trials[0][(int)finalTarget]);
		System.out.println("trials[1][finalTarget]: " + trials[1][(int)finalTarget]);
		
		int min = -1;
		int mini = -1;
		for(int i=0; i<finalTarget; i++) {
			if(trials[0][i] + trials[0][(int)(finalTarget - i)] <= trials[1][(int)finalTarget]) {
				System.out.println("2 box answer:");
				System.out.println("i : " + i);
				System.out.println("finalTarget : " + finalTarget);
			}
			
			if(trials[0][i] + trials[0][(int)(finalTarget - i)] < min || min == -1) {
				min = trials[0][i] + trials[0][(int)(finalTarget - i)];
				mini = i;
			}
		}
		
		System.out.println("Minimum index:" + mini);
		System.out.println(min);
		System.out.println(trials[0][0]);
		
	}

}
