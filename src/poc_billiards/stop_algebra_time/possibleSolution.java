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
		
		System.out.println(getXFrombottomLeftToTopLeft(2, 3));
		
		

		System.out.println(getXFrombottomLeftToTopLeft(6, 5));
		
	}
	

	// x displacement: 2
	// y displacement: 1
	// 2x = 0 (mod 2W)	
	// => x = 0 mod W (w is even)
	// x = H-1 mod 2H
	
	public static int getXFrombottomLeftToTopLeft(int W, int H) {
		
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
		
		
		ArrayList<CRTTuple> listEq = new ArrayList<CRTTuple>();
		// TODO: gcd(W, 2H) > 1, and that's a problem!
		//listEq.add( new CRTTuple(0, W) );
		//listEq.add( new CRTTuple(H-1, 2*H) );
		
		//TODO I think gcd(H, W) = 1...
		//TODO: Also, this is wrong!
		//Alt:
		//listEq.add( new CRTTuple(0, 2) );
		System.out.println(" x = 0 mod " + W);
		listEq.add( new CRTTuple(0, W) );
		
		System.out.println(" x = " + (H-1)/2 + " mod " + H);
		listEq.add( new CRTTuple((H-1)/2, H) );
		
		long ret2 = ChineseRemainderTheoremSolverBigInt.solve(listEq)[0].longValue();
		
		System.out.println("Ret1: " + ret1 + "  vs  ret2: " + ret2);
		
		if(ret1 != ret2) {
			System.out.println("Doh!");
			System.exit(1);
		}
		
		return ret1;
	}
	
	
	// x = 5 mod 15
	// x = 7 mod 35
}
