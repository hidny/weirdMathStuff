package tanXMission;

import UtilityFunctions.Fraction;

//FROM: https://introcs.cs.princeton.edu/java/92symbolic/RationalApprox.java
// https://math.stackexchange.com/questions/2354993/rational-approximation-of-pi-where-denominator-lies-in-a-b

/******************************************************************************
 *  Compilation:  javac RationalApprox.java
 *  Execution:    java RationalApprox x
 *
 *  Compute the best rational approximation to x using Stern-Brocot
 *  tree.
 *
 *  % java RationalApprox 2.71828182845904523536028747135
 *  1 2 3 5/2 8/3 11/4 19/7 49/18 68/25 87/32 106/39 193/71 685/252 878/323 ...
 *
 *  % java RationalApprox 3.14159265358979323846264338328
 *  1 2 3 13/4 16/5 19/6 22/7 179/57 201/64 223/71 245/78 267/85 289/92 311/99 333/106

 *  % java RationalApprox 
 *  0.142857
 *  1/4 1/5 1/6 1/7 71429/500004 71430/500011 ...
 *
 *  Reference: Discrete Mathematics, 116-123.
 *
 ******************************************************************************/


//TODO: what if I do (2*a+c)/(2*b+d)
//Find tan.... maybe approx: 1/cos(x)
//https://blogs.ubc.ca/infiniteseriesmodule/units/unit-3-power-series/taylor-series/the-maclaurin-expansion-of-cosx/

//For x near pi where x <pi:
//(1-cosx)/cos x < tanx < 1/cosx


//What if I did mediant between 1 and 3.5?
//what if I trial less ideal candidates?

//TODO: don't even try it when x is not prime...

class RationalApprox {


	//LOL: apparently 355/113 is really really close to pi... 
	// That's slowing everything down!
	
	public static Fraction pi = PI.pi5000();

   public static void main(String[] args) {

	 
      Fraction left  = new Fraction(0, 1);
      Fraction right = new Fraction(1, 0);
      Fraction best = left;
      
      // do Stern-Brocot binary search
      for(int i=0; i<1000; i++) {

         // compute next possible rational approximation
    	  Fraction mediant = new Fraction(left.getNumerator().add(right.getNumerator()), left.getDenominator().add(right.getDenominator()));

    	  if (Fraction.minus(mediant, pi).greaterThan0()) {
            right = mediant;              // go left
    	  }else{
            left = mediant;              // go right
    	  }


      	 System.out.println("Left:");
     	 System.out.println(left.getNumerator().toString());
     	 System.out.println("--------------------------------------------2");
     	 System.out.println(left.getDenominator().toString());
     	 System.out.println();
     	 

      	 System.out.println("Right:");
     	 System.out.println(right.getNumerator().toString());
     	 System.out.println("--------------------------------------------2");
     	 System.out.println(right.getDenominator().toString());
     	 System.out.println();
     	 

     	 System.out.println();
    	 System.out.println(mediant.getNumerator().toString());
    	 System.out.println("--------------------------------------------2");
    	 System.out.println(mediant.getDenominator().toString());
    	 System.out.println();
    	 
    	 
     	 System.out.println();
    	 System.out.println(mediant.getNumerator().toString());
    	 System.out.println("--------------------------------------------2");
    	 System.out.println(mediant.getDenominator().toString());
    	 System.out.println();
         
    	 // ContinuedFractionApprox.attemptTanXCheck(mediant);
    	  ContinuedFractionApprox.attemptTanXCheckUsePiApproxNoDouble(mediant);
    	  
    	// System.out.println(i);
      }
      System.out.println();

   }

}