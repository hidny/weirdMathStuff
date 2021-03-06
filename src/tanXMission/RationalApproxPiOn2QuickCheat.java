package tanXMission;

import java.math.BigInteger;

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
//Nah...

//For x near pi where x <pi:
//(1-cosx)/cos x < tanx < 1/cosx


//What if I did mediant between 1 and 3.5?
//what if I trial less ideal candidates?


class RationalApproxPiOn2QuickCheat {


	//LOL: apparently 355/113 is really really close to pi... 
	//TODO: fix this! (skip a bunch of mediant iterations!) (See other TODO for details)
	// That's slowing everything down!
	
	
	public static final int ONE_THOUSAND = 1000;
	public static final int HALF_ONE_THOUSAND = 500;

	public static final Fraction TWO = new Fraction(2, 1);
	
	//The numerator I use for PI should be at least 4 times the length of the numerator of the rational approx to pi: 
	public static final double PI_PRECISION_FACTOR = 4.0;
	
   public static void main(String[] args) {


      int prevSizeDebug = -1;
	 
      Fraction left  = new Fraction(0, 1);
      Fraction right = new Fraction(1, 0);

      int numDigitsApproxPi = ONE_THOUSAND;
      Fraction currentPrecisePiOver2 = Fraction.divide(PI.getPiForNDigits(numDigitsApproxPi), TWO);
    
      System.out.println("Length precise pi: " + currentPrecisePiOver2.getNumerator().toString().length());
      System.out.println("Using PI precision factor " + PI_PRECISION_FACTOR);
      System.out.println("cheating");
      
      ExtremeTanXCalculator.initializeListOfPrimes();
      
      int numIterDebug = 0;
      
      // do Stern-Brocot binary search
      for(int i=0; left.getNumerator().toString().length() < 45*ONE_THOUSAND; i++) {

    	  
    	  
         // compute next possible rational approximation
    	 // Fraction mediant = new Fraction(left.getNumerator().add(right.getNumerator()), left.getDenominator().add(right.getDenominator()));

    	  //TODO: this is probably the slowest part of the algo
    	  //what if we just need to update the decimal precision of the mediant based on decimal precision of
    	  //left and right?
    	  
    	  //What if we calc n= #times left will be picked in a row
    	  //or #times right will be picked in a row and use this knowledge to go faster?
    	  

    	 /*
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
      	 */
 
    	  
          BigInteger tmp1 = left.getDenominator().multiply(currentPrecisePiOver2.getNumerator())
          .subtract(left.getNumerator().multiply(currentPrecisePiOver2.getDenominator()));
          
          BigInteger tmp2 = right.getNumerator().multiply(currentPrecisePiOver2.getDenominator())
                  .subtract(right.getDenominator().multiply(currentPrecisePiOver2.getNumerator()));
          
          
    	  if (tmp2.compareTo(tmp1) > 0) {
            
            long n1 =tmp2.divide(tmp1).longValue();

            System.out.println("n1: " + n1);
            if(n1 <=0) {
            	System.out.println("doh!");
            	System.exit(1);
            }
            
            right = new Fraction((left.getNumerator().multiply(new BigInteger(n1 + ""))).add(right.getNumerator()),
            		(left.getDenominator().multiply(new BigInteger(n1 + ""))).add(right.getDenominator()));

            
    	  }else{

            long n2 = tmp1.divide(tmp2).longValue();
            System.out.println("n2: " + n2);

            if(n2 <=0) {
            	System.out.println("doh!");
            	System.exit(1);
            }
            
            //I'm cheating, but whatever...
            left =  new Fraction((right.getNumerator().multiply(new BigInteger((n2) + ""))).add(left.getNumerator()),
            		(right.getDenominator().multiply(new BigInteger((n2) + ""))).add(left.getDenominator()));
            
            if(left.getDenominator().mod(new BigInteger("2")).equals(BigInteger.ONE)) {
                	ExtremeTanXCalculator.attemptTanXCheckUsePiApproxNoDoublePiOn2(left, currentPrecisePiOver2);
              }

    	  }


    	  
     	 //System.out.println();
    	  if(prevSizeDebug < left.getNumerator().toString().length()) {

    		  	 int numeratorSize = left.getNumerator().toString().length();
    	    	 System.out.println("New numerator size: " + numeratorSize);
    	    	 prevSizeDebug =  left.getNumerator().toString().length();
    	    	 
    	    	 if(PI_PRECISION_FACTOR * numeratorSize > numDigitsApproxPi) {
    	    		 numDigitsApproxPi += ONE_THOUSAND;
    	    		 currentPrecisePiOver2 = Fraction.divide(PI.getPiForNDigits(numDigitsApproxPi), TWO);
    	    		 
    	    		 System.out.println("Upgrading size of pi guide...");
    	    	 }
    	  }
    	 //System.out.println("--------------------------------------------2");
    	 //System.out.println(mediant.getDenominator().toString());
    	 //System.out.println();

    	  
    	  
    	  numIterDebug++;
    	  if(numIterDebug % 1000 == 0) {
    		  System.out.println("Num iterations of mediants: " + numIterDebug);
    	  }
    	// System.out.println(i);
      }

   }

   //Found another prime!
   //(thanks to the people you gave me the hint)
   /*
    * 
Samuel Li
1 day ago
It has 1017 digits, the first 10 of which are 2308358707.

    */
   //What I found:
   //Found X = 230835870782558831561617186504559084198719501221763995608082253627620752053749345488376393822837250198036536001853828659466202612019525543362322174085744303421231446484541625047630462908919109308644634605051209877750956648014568322183373423523622941806761765245932401727973436579786298208782013178059220103271409347616696556052706562092799953175234183483071403726145726928572372071037042523626350312132351311366806233135093893271182587352730075523143635168510803804031460442796778933680674070124730971307185688425634077096234482442639666385695677866015904370207368846631450100939158029908242779848800640038255592227473300237596577845602369215568916732445980431078426390412264603773550384039765410088966381694110344811198325354315338629604946794192217817288101344643511450133142277670683067655250506551517767422160650566385017503208608678491109517443585115317845289832567015746473548492179557935154400719019569904865219030736244089287736334048402066257337090606092966121806567484954460809024219605952851728610326005069 where tan X = Infinity

   //TODO: You need to have the first 100 K digits of Pi to attempt it, but here it is:
   /*
    * Samuel Li
Samuel Li
1 day ago
@Stand-up Maths I've just found a third solution with 35085 digits, the first 10 of which are 4094619989.
*/
    
   
   /*
    * 
J L
1 day ago
@Σ5 For all integers n, we will always have |tan(n)| < 2 ( (2n+2)/pi )^41 (proof below).
 Since exp(n) is greater than this once n>200, there will be no solutions to |tan(n)| > exp(n) once n goes past 200. It's easy enough to check the first 200 values of n and see that there are no solutions, so there are no positive integers n such that |tan(n)| > exp(n).

Proof: This uses a result by Mahler (see equation (15) in https://carma.newcastle.edu.au/resources/mahler/docs/119.pdf ): if p/q is any rational number, then the distance of p/q from pi is at least 1/q^42.



Suppose n is very close to pi/2+k*pi (as the video explains). Using the bound |tan(pi/2 - x)|<1/|x| for x close to 0 (this follows from |tan(x)|>|x| and the identity tan(x)=1/tan(pi/2-x)), we have

|tan(n)| = |tan(pi/2 - (pi/2 + k*pi - n))|
< 1/|pi/2 + k*pi - n|
= (2/(2k+1)) / |pi - 2n/(2k+1)|
< (2/(2k+1)) (2k+1)^42
= 2 (2k+1)^41.

Now since we were using a value of n that's close to pi/2+k*pi, we must certainly have pi/2 + k*pi < n+1. This implies 2k+1 < (2/pi)(n+1), giving us the desired result.
*/
   
   /*
    * 
Samuel Li
1 day ago
In addition to the 1017-digit prime that several people have sent you by now, I've just found a 35,085 digit prime with tan(p) > p. The first 10 digits are 4094619989.
*/
   
  /*
   * 
Samuel Li
20 hours ago
@GEL It appears we got lucky: the next known solution has 43,176 digits, the first 10 of which are 1086855570. (This one wasn't found by me, my method skipped over it somehow.)
  */

}