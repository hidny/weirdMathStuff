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
		
		//Final answer: 14623
		//TODO: GetMinPerimeterRefined is all wrong!
		//There's prently of answers with 3 rectangles.
		//Smallest: 1469
		/*
		 * Actual found:
		 * At n = 4: Trying i= 27367:
At n = 4: Trying i= 44375:
At n = 4: Trying i= 59121:
At n = 4: Trying i= 72205:
At n = 4: Trying i= 84548:
At n = 4: Trying i= 97557:
At n = 4: Trying i= 109403:
At n = 4: Trying i= 120539:
At n = 4: Trying i= 131303:
At n = 4: Trying i= 142257:
At n = 4: Trying i= 152995:
At n = 4: Trying i= 164233:
At n = 4: Trying i= 174869:
At n = 4: Trying i= 185065:
At n = 4: Trying i= 194672:
At n = 4: Trying i= 204469:
At n = 4: Trying i= 214068:
At n = 4: Trying i= 223544:
At n = 4: Trying i= 232747:
At n = 4: Trying i= 241942:
At n = 4: Trying i= 251359:
At n = 4: Trying i= 260485:
At n = 4: Trying i= 269467:
At n = 4: Trying i= 279032:
At n = 4: Trying i= 288451:
At n = 4: Trying i= 297263:
At n = 4: Trying i= 306151:
At n = 4: Trying i= 314873:
At n = 4: Trying i= 323351:
At n = 4: Trying i= 331928:
At n = 4: Trying i= 341038:
At n = 4: Trying i= 350102:
At n = 4: Trying i= 358877:
At n = 4: Trying i= 367481:
At n = 4: Trying i= 375949:
At n = 4: Trying i= 384230:
At n = 4: Trying i= 392921:
At n = 4: Trying i= 401528:
At n = 4: Trying i= 409889:
At n = 4: Trying i= 418343:
At n = 4: Trying i= 426335:
At n = 4: Trying i= 434419:
At n = 4: Trying i= 442283:
At n = 4: Trying i= 450295:
At n = 4: Trying i= 457946:
At n = 4: Trying i= 466018:
At n = 4: Trying i= 474019:
At n = 4: Trying i= 482411:
At n = 4: Trying i= 490139:
At n = 4: Trying i= 498503:
At n = 4: Trying i= 506183:
At n = 4: Trying i= 513964:
At n = 4: Trying i= 521885:
At n = 4: Trying i= 529795:
At n = 4: Trying i= 537949:
At n = 4: Trying i= 545823:
At n = 4: Trying i= 553691:
At n = 4: Trying i= 561575:
At n = 4: Trying i= 569613:
At n = 4: Trying i= 577463:
At n = 4: Trying i= 584881:
At n = 4: Trying i= 592792:
At n = 4: Trying i= 600517:
At n = 4: Trying i= 608248:
At n = 4: Trying i= 615718:
At n = 4: Trying i= 623349:
At n = 4: Trying i= 630577:
At n = 4: Trying i= 637671:
At n = 4: Trying i= 645359:
At n = 4: Trying i= 652606:
At n = 4: Trying i= 660502:
At n = 4: Trying i= 667906:
At n = 4: Trying i= 675301:
At n = 4: Trying i= 682675:
At n = 4: Trying i= 689863:
At n = 4: Trying i= 697429:
At n = 4: Trying i= 704537:
At n = 4: Trying i= 711988:
At n = 4: Trying i= 719668:
At n = 4: Trying i= 727078:
At n = 4: Trying i= 734540:
At n = 4: Trying i= 741933:
At n = 4: Trying i= 749440:
At n = 4: Trying i= 756698:
At n = 4: Trying i= 764138:
At n = 4: Trying i= 771307:
At n = 4: Trying i= 778337:
At n = 4: Trying i= 785583:
At n = 4: Trying i= 792847:
At n = 4: Trying i= 800107:
At n = 4: Trying i= 807223:
At n = 4: Trying i= 814709:
At n = 4: Trying i= 821831:
At n = 4: Trying i= 828895:
At n = 4: Trying i= 836122:
At n = 4: Trying i= 843299:
At n = 4: Trying i= 850471:
At n = 4: Trying i= 857163:
At n = 4: Trying i= 864212:
At n = 4: Trying i= 871251:
At n = 4: Trying i= 878285:
At n = 4: Trying i= 885413:
At n = 4: Trying i= 891935:
At n = 4: Trying i= 898835:
At n = 4: Trying i= 905686:
At n = 4: Trying i= 912397:
At n = 4: Trying i= 919535:
At n = 4: Trying i= 926617:
At n = 4: Trying i= 933500:
At n = 4: Trying i= 940457:
At n = 4: Trying i= 947353:
At n = 4: Trying i= 954583:
At n = 4: Trying i= 961615:
At n = 4: Trying i= 968531:
At n = 4: Trying i= 975638:
At n = 4: Trying i= 982659:
At n = 4: Trying i= 990109:
At n = 4: Trying i= 996887:
,,,
		 */
		
	}

}
