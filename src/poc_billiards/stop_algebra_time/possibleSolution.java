package poc_billiards.stop_algebra_time;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import poc_billiards.stop_algebra_time.chineseRemainderTheorem.CRTTuple;
import poc_billiards.stop_algebra_time.chineseRemainderTheorem.ChineseRemainderTheoremSolverBigInt;

public class possibleSolution {

	private int N;
	
	private HashSet<Integer> tabTop = new HashSet<Integer>();
	private HashSet<Integer> notchTop = new HashSet<Integer>();
	

	private HashSet<Integer> tabBottom = new HashSet<Integer>();
	private HashSet<Integer> notchBottom = new HashSet<Integer>();
	
	
	//stripDisplacement[0] = 0
	
	int stripDisplacement[];
	
	
	public static void main(String args[]) {
		
		System.out.println(getXFrombottomLeftToTopLeftSlow(2, 3));
		
		

		System.out.println(getXFrombottomLeftToTopLeftSlow(6, 5));
		
	}
	

	// x displacement: 2
	// y displacement: 1
	// 2x = 0 (mod 2W)	
	// => x = 0 mod W (w is even)
	// x = H-1 mod 2H
	
	
	//PRE: gcd(W, H) = 1
	public static int getXFrombottomLeftToTopLeftSlow(int W, int H) {
		
		if(UtilityFunctions.UtilityFunctions.getGCD(W, H) != 1) {
			System.out.println("ERROR: gcd for getXFrombottomLeftToTopLeftSlow should be 1!");
			
		}
		
		//TODO: solve Chinese Remainder Theorem! (you already have the relevant code!)
		
		boolean testArray[][] = new boolean[2 * H][2 * W];
		
		if(H % 2 != 1 || W % 2 != 0) {
			System.out.println("Error: it should be even x odd.");
			System.exit(1);
		}
		
		int ret1 = 0;
		int xCoord = 0;
		int yCoord = 0;
		
		while(true) {
			
			testArray[yCoord][xCoord] = true;
			System.out.println(xCoord + ", " + yCoord);
			if(xCoord == 0 && yCoord == H - 1) {
				break;
			}
			
			ret1++;
			
			xCoord = (xCoord + 2) % (2*W);
			yCoord = (yCoord + 1) % (2*H);
			
		}
		
		int tmp = 0;
		for(int i=testArray.length - 1; i >= 0; i--) {
			for(int j=0; j<testArray[0].length; j++) {
				if(testArray[i][j]) {
					System.out.print("#");
					tmp++;
				} else {
					System.out.print("_");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		
		
		int ret2 = getXFrombottomLeftToTopLeftFast(W, H);
		
		System.out.println("Ret1: " + ret1 + "  vs  ret2: " + ret2);
		
		if(ret1 != ret2) {
			System.out.println("Doh!");
			System.exit(1);
		}
		
		return ret1;
	}
	
	//PRE: 1) gcd(W, H) = 1 (make it have a gcd > 1 later)
	//     2) W is even and H is odd.
	public static int getXFrombottomLeftToTopLeftFast(int W, int H) {
		

		if(UtilityFunctions.UtilityFunctions.getGCD(W, H) != 1) {
			System.out.println("ERROR: gcd for getXFrombottomLeftToTopLeftFast should be 1!");
			
		}
		
		ArrayList<CRTTuple> listEq = new ArrayList<CRTTuple>();
		
		//Solution by pen and paper:
		//listEq.add( new CRTTuple(0, W) );
		//listEq.add( new CRTTuple(H-1, 2*H) );
		
		listEq.add( new CRTTuple(0, W) );
		
		//By pen and paper, x = H-1 (mod 2H), but it's evenness is already covered by "x = 0 (mod W)",
		// so set it to "x = H-1 (mod H)" (Chinese Remainder theorem requires coprime mods)
		listEq.add( new CRTTuple((H-1), H) );
		
		return (int)ChineseRemainderTheoremSolverBigInt.solve(listEq)[0].longValue();
	}
	
	
	// x = 5 mod 15
	// x = 7 mod 35
}
