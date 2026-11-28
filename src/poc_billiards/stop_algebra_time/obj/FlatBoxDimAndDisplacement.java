package poc_billiards.stop_algebra_time.obj;

import java.util.ArrayList;

import UtilityFunctions.UtilityFunctions;
import poc_billiards.stop_algebra_time.chineseRemainderTheorem.CRTTuple;
import poc_billiards.stop_algebra_time.chineseRemainderTheorem.ChineseRemainderTheoremSolverBigInt;

public class FlatBoxDimAndDisplacement {

	
	public static void main(String args[]) {
		
		System.out.println(getDistFrombottomLeftToTopLeftSlow(3, 2));
		
		

		System.out.println(getDistFrombottomLeftToTopLeftSlow(5, 6));
		
		
		
	}
	
	//TODO: make these private later 
	//Location of edge of strip along the trail from bottom left.
	
	public long LocationOfEdgeOfStrip;

	public long dimH;
	
	public long dimW;
	
	public long bottomLeftToTopRight;
	
	public FlatBoxDimAndDisplacement(long H, long W) {
		
		if(UtilityFunctions.getGCD(H, W) > 1 || H % 2 != 1 || W % 2 != 0) {
			System.out.println("ERROR setting FlatBoxDimAndDisplacement  " + H + " , " + W);
			System.exit(1);
		}
		
		this.dimH = H;
		this.dimW = W;
		
		this.bottomLeftToTopRight = getDistFrombottomLeftToTopLeftSlow(H, W);
		
		System.out.println("bottom left to top left: " + H + " x " + W + ": " + this.bottomLeftToTopRight);
		System.out.println();
		
		//
		this.LocationOfEdgeOfStrip = 0;
		
		
	}
	

	public long getC() {
		return LocationOfEdgeOfStrip;
	}


	public void setC(int c) {
		this.LocationOfEdgeOfStrip = c;
	}
	
	// Let Finv = input top index from top left
	 // output bottom index on same part of strip
	public long getF(int input) {
		return input - bottomLeftToTopRight;
	}

	// Let Finv = input bottom index from bottom left
	 // output top index on same part of strip
	public long getFinv(int input) {
		return input + bottomLeftToTopRight;
	}
	


	public long getTrailLength() {
		return (this.dimH * this.dimW);
	}



	// x displacement: 2
	// y displacement: 1
	// 2x = 0 (mod 2W)	
	// => x = 0 mod W (w is even)
	// x = H-1 mod 2H
	
	
	//PRE: gcd(W, H) = 1
	public static long getDistFrombottomLeftToTopLeftSlow(long H, long W) {
		
		if(UtilityFunctions.getGCD(W, H) != 1) {
			System.out.println("ERROR: gcd for getXFrombottomLeftToTopLeftSlow should be 1!");
			
		}
		
		//TODO: solve Chinese Remainder Theorem! (you already have the relevant code!)
		
		boolean testArray[][] = new boolean[(int)(2 * H)][(int)(2 * W)];
		
		if(H % 2 != 1 || W % 2 != 0) {
			System.out.println("Error: it should be odd x even 222 dim:" + H + " , " + W);
			System.exit(1);
		}
		
		long ret1 = 0;
		long xCoord = 0;
		long yCoord = 0;
		
		while(true) {
			
			testArray[(int)yCoord][(int)xCoord] = true;
			//System.out.println(xCoord + ", " + yCoord);
			if(xCoord == 0 && yCoord == H - 1) {
				break;
			}
			
			ret1++;
			
			xCoord = (xCoord + 2) % (2*W);
			yCoord = (yCoord + 1) % (2*H);
			
		}
		
		int tmp = 0;
		/*for(int i=testArray.length - 1; i >= 0; i--) {
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
		*/
		
		long ret2 = getXFrombottomLeftToTopLeftFast(H, W);
		
		System.out.println("Ret1: " + ret1 + "  vs  ret2: " + ret2);
		
		if(ret1 != ret2) {
			System.out.println("Doh!");
			System.exit(1);
		}
		
		return ret1;
	}
	
	
	//PRE: 1) gcd(W, H) = 1 (make it have a gcd > 1 later)
	//     2) W is even and H is odd.
	public static long getXFrombottomLeftToTopLeftFast(long H, long W) {
		

		if(UtilityFunctions.getGCD(W, H) != 1) {
			System.out.println("ERROR: gcd for getXFrombottomLeftToTopLeftFast should be 1!");
			
		}
		
		if(H % 2 != 1 || W % 2 != 0) {
			System.out.println("ERROR in getXFrombottomLeftToTopLeftFast (H W)");
		}
		ArrayList<CRTTuple> listEq = new ArrayList<CRTTuple>();
		
		//Solution by pen and paper:
		//listEq.add( new CRTTuple(0, W) );
		//listEq.add( new CRTTuple(H-1, 2*H) );
		
		listEq.add( new CRTTuple(0, W) );
		
		//By pen and paper, x = H-1 (mod 2H), but it's evenness is already covered by "x = 0 (mod W)",
		// so set it to "x = H-1 (mod H)" (Chinese Remainder theorem requires coprime mods)
		listEq.add( new CRTTuple((H-1), H) );
		
		return ChineseRemainderTheoremSolverBigInt.solve(listEq)[0].longValue();
	}
	
	
	// x = 5 mod 15
	// x = 7 mod 35
}