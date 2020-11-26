package horseyWorkSingMasterConj;

import java.math.BigInteger;


//Found sequence I was looking for...
// https://oeis.org/A089508
// https://oeis.org/A081016

public class horseyEquationSolverAllBigInteger {

	

	public static BigInteger xFund= new BigInteger("9");
	public static BigInteger  yFund = new BigInteger("2");
		
	
	public static BigInteger prev3Solutions[][] = new BigInteger[3][2];
	public static int prevSolutionIndex = 0;
	
	public static BigInteger N = new BigInteger("20");
	public static BigInteger SIXTEEN = new BigInteger("16");
	
	public static void main(String args[]) {
	
		//Test x^2 = 20y^2 - 16
		setPrevThreeSolutions();
		
		for(int i=0; i<100; i++) {

			BigInteger curX = prev3Solutions[prevSolutionIndex][0];
			BigInteger curY = prev3Solutions[prevSolutionIndex][1];
			

			System.out.println("Claim: " + curX + "^2 = " + N + " * " + curY + "^2 - 16:");
			
			
			if(curX.multiply(curX).subtract(N.multiply(curY).multiply(curY).subtract(SIXTEEN)).compareTo(BigInteger.ZERO) == 0) {
				//System.out.println("We're good!");
			} else {
				System.out.println("Equation fails!");
				System.exit(1);
			}
			
			BigInteger curXAbs = curX.abs();
			BigInteger curYAbs =  curY.abs();
			
			BigInteger tmpFormula1[] = (curXAbs.subtract(new BigInteger("12"))).divideAndRemainder(new BigInteger("10"));

			//System.out.println("TEST");
			//System.out.println(curXAbs);
			//System.out.println(tmpFormula1[0]);
			//System.out.println(tmpFormula1[1]);
			//System.out.println("End test");
			
			if(tmpFormula1[1].compareTo(BigInteger.ZERO) == 0) {
				
				System.out.println("** (In curXAbs - 12) % 10 == 0");
				
				BigInteger nComb = tmpFormula1[0];

				BigInteger tmpFormula2[] = (nComb.multiply(new BigInteger("3")).add(new BigInteger("4")).subtract(curYAbs)).divideAndRemainder(new BigInteger("2"));
				
				if( tmpFormula2[1].compareTo(BigInteger.ZERO) != 0) {
					System.out.println("AAAH!2");
					System.exit(1);
				}
				
				BigInteger rComb = tmpFormula2[0];
				
				System.out.println("n comb = " + nComb);
				System.out.println("r comb = " + rComb);
				
			}
			
			System.out.println();
			System.out.println();
			

			BigInteger nextSol[] = solvePell(prev3Solutions[prevSolutionIndex][0], prev3Solutions[prevSolutionIndex][1], xFund, yFund, N);
			
			
			prev3Solutions[prevSolutionIndex][0] = nextSol[0];
			prev3Solutions[prevSolutionIndex][1] = nextSol[1];
			
			prevSolutionIndex = (prevSolutionIndex + 1) % prev3Solutions.length;
		}
		
	}
	
	
	public static void setPrevThreeSolutions() {
		prev3Solutions = new BigInteger[3][2];
		
		prev3Solutions[0][1] = new BigInteger("1");
		prev3Solutions[0][0] = new BigInteger("2");

		prev3Solutions[1][1] = new BigInteger("2");
		prev3Solutions[1][0] = new BigInteger("8");
		

		prev3Solutions[2][1] = new BigInteger("5");
		prev3Solutions[2][0] = new BigInteger("22");
		
	}
	
	/*
	 * Soln:
	 * n comb = 10803703
	   r comb = 4126648
	   
	   //(103 choose 40) - (104 choose 39) == 0
	    //(33551 choose 12816) - (33552 choose 12815) == 0
	   //(10803703 choose 4126648) - (10803704 choose 4126647) == 0
	    //But then it's messed up... :( (AHA: Integer overflow!!!)
	     * 
	     //Also: that's not the smallest solution... so what's going on?
	     
	     
	  I also only get a third of the solutions he mentions:
	  
	 */
	


	public static BigInteger[] getFirstSol() {
		return new BigInteger[] { new BigInteger("2"), BigInteger.ONE};
	}
	public static BigInteger[] solvePell(BigInteger x1, BigInteger y1, BigInteger xfun, BigInteger yfun, BigInteger n) {
		
		
		System.out.println("(x1 , y1) = ( " + x1 + ", " + y1 + ")");	
		BigInteger newSolution[] = new BigInteger[2];
		
		newSolution[0] = x1.multiply(xfun).add( n.multiply(y1).multiply(yfun) );
		newSolution[1] = x1.multiply(yfun).add( y1.multiply(xfun) );
		
		
		return newSolution;
		
	}
}
