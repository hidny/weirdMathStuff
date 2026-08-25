package cccgProblemBirthdayGuy.aboveN2;

import cccgProblemBirthdayGuy.GetMinPerimeterCornersRefined;

public class AttemptLucky7WithNMinus5 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//TODO: prep first 20...

		int LENGTH = 1000000;

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
		//TODO: Redo this, but find 4th rectangle...
		
		//There's prently of answers with 3 rectangles.
		//Smallest: 1469
		/*
		 * Actual found:
At n = 4 with debugIteration 1: Trying i= 1469:
At n = 4 with debugIteration 2: Trying i= 1547:
At n = 4 with debugIteration 3: Trying i= 1627:
At n = 4 with debugIteration 4: Trying i= 1663:
At n = 4 with debugIteration 5: Trying i= 1746:
At n = 4 with debugIteration 6: Trying i= 1869:
At n = 4 with debugIteration 7: Trying i= 1879:
At n = 4 with debugIteration 8: Trying i= 1957:
At n = 4 with debugIteration 9: Trying i= 2087:
At n = 4 with debugIteration 10: Trying i= 2180:
At n = 4 with debugIteration 11: Trying i= 2191:
At n = 4 with debugIteration 12: Trying i= 2317:
At n = 4 with debugIteration 13: Trying i= 2527:
At n = 4 with debugIteration 14: Trying i= 2572:
At n = 4 with debugIteration 15: Trying i= 2639:
At n = 4 with debugIteration 16: Trying i= 2743:
At n = 4 with debugIteration 17: Trying i= 2767:
At n = 4 with debugIteration 18: Trying i= 2827:
At n = 4 with debugIteration 19: Trying i= 2935:
At n = 4 with debugIteration 20: Trying i= 2957:
At n = 4 with debugIteration 21: Trying i= 3007:
At n = 4 with debugIteration 22: Trying i= 3118:
At n = 4 with debugIteration 23: Trying i= 3179:
At n = 4 with debugIteration 24: Trying i= 3231:
At n = 4 with debugIteration 25: Trying i= 3271:
At n = 4 with debugIteration 26: Trying i= 3399:
At n = 4 with debugIteration 27: Trying i= 3463:
At n = 4 with debugIteration 28: Trying i= 3517:
At n = 4 with debugIteration 29: Trying i= 3582:
At n = 4 with debugIteration 30: Trying i= 3637:
At n = 4 with debugIteration 31: Trying i= 3647:
At n = 4 with debugIteration 32: Trying i= 3671:
At n = 4 with debugIteration 33: Trying i= 3759:
At n = 4 with debugIteration 34: Trying i= 3815:
At n = 4 with debugIteration 35: Trying i= 3883:
At n = 4 with debugIteration 36: Trying i= 3940:
At n = 4 with debugIteration 37: Trying i= 4009:
At n = 4 with debugIteration 38: Trying i= 4196:
At n = 4 with debugIteration 39: Trying i= 4207:
At n = 4 with debugIteration 40: Trying i= 4255:
At n = 4 with debugIteration 41: Trying i= 4327:
At n = 4 with debugIteration 42: Trying i= 4460:
At n = 4 with debugIteration 43: Trying i= 4521:
At n = 4 with debugIteration 44: Trying i= 4657:
At n = 4 with debugIteration 45: Trying i= 4795:
At n = 4 with debugIteration 46: Trying i= 4799:
At n = 4 with debugIteration 47: Trying i= 4858:
At n = 4 with debugIteration 48: Trying i= 4871:
At n = 4 with debugIteration 49: Trying i= 4927:
At n = 4 with debugIteration 50: Trying i= 4935:
At n = 4 with debugIteration 51: Trying i= 4999:
At n = 4 with debugIteration 52: Trying i= 5012:
At n = 4 with debugIteration 53: Trying i= 5099:
At n = 4 with debugIteration 54: Trying i= 5142:
At n = 4 with debugIteration 55: Trying i= 5207:
At n = 4 with debugIteration 56: Trying i= 5243:
At n = 4 with debugIteration 57: Trying i= 5287:
At n = 4 with debugIteration 58: Trying i= 5291:
At n = 4 with debugIteration 59: Trying i= 5367:
At n = 4 with debugIteration 60: Trying i= 5389:
At n = 4 with debugIteration 61: Trying i= 5425:
At n = 4 with debugIteration 62: Trying i= 5434:
At n = 4 with debugIteration 63: Trying i= 5447:
At n = 4 with debugIteration 64: Trying i= 5501:
At n = 4 with debugIteration 65: Trying i= 5515:
At n = 4 with debugIteration 66: Trying i= 5537:
At n = 4 with debugIteration 67: Trying i= 5575:
At n = 4 with debugIteration 68: Trying i= 5651:
At n = 4 with debugIteration 69: Trying i= 5669:
At n = 4 with debugIteration 70: Trying i= 5687:
At n = 4 with debugIteration 71: Trying i= 5719:
At n = 4 with debugIteration 72: Trying i= 5758:
At n = 4 with debugIteration 73: Trying i= 5803:
At n = 4 with debugIteration 74: Trying i= 5807:
At n = 4 with debugIteration 75: Trying i= 5839:
At n = 4 with debugIteration 76: Trying i= 5872:
At n = 4 with debugIteration 77: Trying i= 5887:
At n = 4 with debugIteration 78: Trying i= 5911:
At n = 4 with debugIteration 79: Trying i= 5957:
At n = 4 with debugIteration 80: Trying i= 6027:
At n = 4 with debugIteration 81: Trying i= 6066:
At n = 4 with debugIteration 82: Trying i= 6127:
At n = 4 with debugIteration 83: Trying i= 6131:
At n = 4 with debugIteration 84: Trying i= 6184:
At n = 4 with debugIteration 85: Trying i= 6199:
At n = 4 with debugIteration 86: Trying i= 6297:
At n = 4 with debugIteration 87: Trying i= 6343:
At n = 4 with debugIteration 88: Trying i= 6358:
At n = 4 with debugIteration 89: Trying i= 6382:
At n = 4 with debugIteration 90: Trying i= 6457:
At n = 4 with debugIteration 91: Trying i= 6523:
At n = 4 with debugIteration 92: Trying i= 6543:
At n = 4 with debugIteration 93: Trying i= 6619:
At n = 4 with debugIteration 94: Trying i= 6706:
At n = 4 with debugIteration 95: Trying i= 6757:
At n = 4 with debugIteration 96: Trying i= 6775:
At n = 4 with debugIteration 97: Trying i= 6783:
At n = 4 with debugIteration 98: Trying i= 6847:
At n = 4 with debugIteration 99: Trying i= 6907:
At n = 4 with debugIteration 100: Trying i= 6911:
At n = 4 with debugIteration 101: Trying i= 6923:
At n = 4 with debugIteration 102: Trying i= 6929:
At n = 4 with debugIteration 103: Trying i= 6949:
At n = 4 with debugIteration 104: Trying i= 6999:
At n = 4 with debugIteration 105: Trying i= 7027:
At n = 4 with debugIteration 106: Trying i= 7091:
At n = 4 with debugIteration 107: Trying i= 7095:
At n = 4 with debugIteration 108: Trying i= 7117:
At n = 4 with debugIteration 109: Trying i= 7196:
At n = 4 with debugIteration 110: Trying i= 7207:
At n = 4 with debugIteration 111: Trying i= 7265:
At n = 4 with debugIteration 112: Trying i= 7287:
At n = 4 with debugIteration 113: Trying i= 7339:
At n = 4 with debugIteration 114: Trying i= 7343:
At n = 4 with debugIteration 115: Trying i= 7367:
At n = 4 with debugIteration 116: Trying i= 7512:
At n = 4 with debugIteration 117: Trying i= 7518:
At n = 4 with debugIteration 118: Trying i= 7531:
At n = 4 with debugIteration 119: Trying i= 7607:
At n = 4 with debugIteration 120: Trying i= 7611:
At n = 4 with debugIteration 121: Trying i= 7613:
At n = 4 with debugIteration 122: Trying i= 7621:
At n = 4 with debugIteration 123: Trying i= 7643:
At n = 4 with debugIteration 124: Trying i= 7687:
At n = 4 with debugIteration 125: Trying i= 7693:
At n = 4 with debugIteration 126: Trying i= 7715:
At n = 4 with debugIteration 127: Trying i= 7767:
At n = 4 with debugIteration 128: Trying i= 7819:
At n = 4 with debugIteration 129: Trying i= 7864:
At n = 4 with debugIteration 130: Trying i= 7892:
At n = 4 with debugIteration 131: Trying i= 7945:
At n = 4 with debugIteration 132: Trying i= 7949:
At n = 4 with debugIteration 133: Trying i= 7967:
At n = 4 with debugIteration 134: Trying i= 7975:
At n = 4 with debugIteration 135: Trying i= 7997:
At n = 4 with debugIteration 136: Trying i= 8047:
At n = 4 with debugIteration 137: Trying i= 8071:
At n = 4 with debugIteration 138: Trying i= 8125:
At n = 4 with debugIteration 139: Trying i= 8129:
At n = 4 with debugIteration 140: Trying i= 8131:
At n = 4 with debugIteration 141: Trying i= 8155:
At n = 4 with debugIteration 142: Trying i= 8159:
At n = 4 with debugIteration 143: Trying i= 8167:
At n = 4 with debugIteration 144: Trying i= 8207:
At n = 4 with debugIteration 145: Trying i= 8230:
At n = 4 with debugIteration 146: Trying i= 8313:
At n = 4 with debugIteration 147: Trying i= 8337:
At n = 4 with debugIteration 148: Trying i= 8341:
At n = 4 with debugIteration 149: Trying i= 8359:
At n = 4 with debugIteration 150: Trying i= 8390:
At n = 4 with debugIteration 151: Trying i= 8407:
At n = 4 with debugIteration 152: Trying i= 8411:
At n = 4 with debugIteration 153: Trying i= 8413:
At n = 4 with debugIteration 154: Trying i= 8422:
At n = 4 with debugIteration 155: Trying i= 8435:
At n = 4 with debugIteration 156: Trying i= 8491:
At n = 4 with debugIteration 157: Trying i= 8521:
At n = 4 with debugIteration 158: Trying i= 8525:
At n = 4 with debugIteration 159: Trying i= 8543:
At n = 4 with debugIteration 160: Trying i= 8567:
At n = 4 with debugIteration 161: Trying i= 8575:
At n = 4 with debugIteration 162: Trying i= 8607:
At n = 4 with debugIteration 163: Trying i= 8620:
At n = 4 with debugIteration 164: Trying i= 8631:
At n = 4 with debugIteration 165: Trying i= 8677:
At n = 4 with debugIteration 166: Trying i= 8707:
At n = 4 with debugIteration 167: Trying i= 8711:
At n = 4 with debugIteration 168: Trying i= 8729:
At n = 4 with debugIteration 169: Trying i= 8762:
At n = 4 with debugIteration 170: Trying i= 8768:
At n = 4 with debugIteration 171: Trying i= 8794:
At n = 4 with debugIteration 172: Trying i= 8869:
At n = 4 with debugIteration 173: Trying i= 8899:
At n = 4 with debugIteration 174: Trying i= 8917:
At n = 4 with debugIteration 175: Trying i= 8951:
At n = 4 with debugIteration 176: Trying i= 8983:
At n = 4 with debugIteration 177: Trying i= 8987:
At n = 4 with debugIteration 178: Trying i= 9061:
At n = 4 with debugIteration 179: Trying i= 9071:
At n = 4 with debugIteration 180: Trying i= 9107:
At n = 4 with debugIteration 181: Trying i= 9142:
At n = 4 with debugIteration 182: Trying i= 9148:
At n = 4 with debugIteration 183: Trying i= 9174:
At n = 4 with debugIteration 184: Trying i= 9229:
At n = 4 with debugIteration 185: Trying i= 9253:
At n = 4 with debugIteration 186: Trying i= 9263:
At n = 4 with debugIteration 187: Trying i= 9277:
At n = 4 with debugIteration 188: Trying i= 9335:
At n = 4 with debugIteration 189: Trying i= 9341:
At n = 4 with debugIteration 190: Trying i= 9367:
At n = 4 with debugIteration 191: Trying i= 9371:
At n = 4 with debugIteration 192: Trying i= 9391:
At n = 4 with debugIteration 193: Trying i= 9457:
At n = 4 with debugIteration 194: Trying i= 9530:
At n = 4 with debugIteration 195: Trying i= 9534:
At n = 4 with debugIteration 196: Trying i= 9586:
At n = 4 with debugIteration 197: Trying i= 9653:
At n = 4 with debugIteration 198: Trying i= 9667:
At n = 4 with debugIteration 199: Trying i= 9671:
At n = 4 with debugIteration 200: Trying i= 9731:
At n = 4 with debugIteration 201: Trying i= 9783:
At n = 4 with debugIteration 202: Trying i= 9823:
At n = 4 with debugIteration 203: Trying i= 9851:
At n = 4 with debugIteration 204: Trying i= 9911:
At n = 4 with debugIteration 205: Trying i= 9932:
At n = 4 with debugIteration 206: Trying i= 9943:
At n = 4 with debugIteration 207: Trying i= 9958:
,,,
		 */
		
	}

}
