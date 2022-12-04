package collatz;

import java.math.BigInteger;

import UtilityFunctions.Fraction;

public class FindApproxToLog2Of3 {

	
	//3^a/2^b = 1
	//3^a=2^b
	//a*log(3)=b
	//log(3) = b/a
	
	public static void main(String []args) {
		int array[] ={1, 1, 2, 2, 3, 1, 5, 2, 23, 2, 2, 1, 1, 55, 1, 4, 3, 1, 1, 15, 1, 9, 2, 5, 7, 1, 1, 4, 8, 1, 11, 1, 20, 2, 1, 10, 1, 4, 1, 1, 1, 1, 1, 37, 4, 55, 1, 1, 49, 1, 1, 1, 4, 1, 3, 2, 3, 3, 1, 5, 16, 2, 3, 1, 1, 1, 1, 1, 5, 2, 1, 2, 8, 7, 1, 1, 2, 1, 1, 3, 3, 1, 1, 1, 1, 5, 4, 2, 2, 2, 16, 8, 10, 1, 25, 2, 1, 1, 1, 2, 18, 10, 1, 1, 1, 1, 9, 1, 5, 6, 2, 1, 1, 12, 1, 1, 1, 6, 2, 12, 1, 1, 12, 1, 1, 2, 12, 1, 12, 3, 1, 5, 1, 14, 1, 1, 14, 2, 3, 1, 2, 2, 1, 4, 1, 4, 8, 1, 1, 1, 3, 5, 1, 1, 1, 1, 2, 1, 4, 3, 7, 5, 3, 1, 32, 1, 1, 1, 18, 1, 3, 2, 5, 2, 1, 3, 1, 8, 1, 1, 1, 2, 6, 6, 5, 33, 2, 2, 3, 1, 1, 1, 1, 29, 1, 3, 2, 1, 21, 1, 6, 52, 1, 8, 1, 4, 14, 9, 7, 1, 4, 18, 2, 2, 1, 1, 2, 100, 39, 1};
		
		
		
		for(int i=0; i<array.length; i++) {
			Fraction curFraction = Fraction.ZERO;
			for(int j=i; j>=0; j--) {
				
				curFraction = Fraction.plus(curFraction, array[j]);
				curFraction = Fraction.divide(Fraction.ONE, curFraction);
			}
			
			curFraction = Fraction.plus(curFraction,Fraction.ONE);
			
			if(i % 2 == 0) {
				System.out.println("i = " + i);
				
				System.out.println(curFraction.getDenominator() + "           (" + curFraction.getNumerator() +")");
			}
		}
		
		
		
		
	}
	
	//Seems like my constraints are tighter than this:
	//http://go.helms-net.de/math/collatz/2hochS_3hochN_V2.htm
	public static void main2(String[] args) {
		// TODO Auto-generated method stub

		BigInteger TWO = new BigInteger("2");
		BigInteger THREE = new BigInteger("3");
		
		BigInteger curNum = new BigInteger("3");
		BigInteger curDenum = new BigInteger("2");
		
		Fraction curBest= new Fraction(0, 1);
		
		for(int i=0; i<1000000; i++) {
			Fraction curFraction = new Fraction(curNum, curDenum);
			
			if(curFraction.compareTo(Fraction.ONE) > 0) {
				curDenum = curDenum.multiply(TWO);
				i--;
			} else if (curFraction.compareTo(curBest) > 0) {
				curBest = new Fraction(curNum, curDenum);
				System.out.println("Found: " + (i+1));
				//System.out.println(curNum);
				//System.out.println(curDenum);
				System.out.println(curBest.getDecimalFormat(15));
				System.out.println("Factor: " + Fraction.divide(Fraction.ONE, Fraction.minus(Fraction.ONE, curBest)).getDecimalFormat(15));
				System.out.println();
				curNum = curNum.multiply(THREE);
			} else {
				curNum = curNum.multiply(THREE);
				
			}
		}
		
	}

}
/*
 * Found: 1
0.75
Factor: 4.0

Found: 3
0.84375
Factor: 6.4

Found: 5
0.94921875
Factor: 19.692307692307693

Found: 17
0.9621691927313805
Factor: 26.433482978553698

Found: 29
0.9752963217818404
Factor: 40.47980188087558

Found: 41
0.9886025477296102
Factor: 87.7389065798651

Found: 94
0.9906690375161856
Factor: 107.17008044288832

Found: 147
0.9927398469153751
Factor: 137.73814248044974

Found: 200
0.9948149849565275
Factor: 192.86347129482962

Found: 253
0.9968944606878649
Factor: 322.0052620466022

Found: 306
0.9989782831765223
Factor: 978.744772544671

Found: 971
0.9990218936368556
Factor: 1022.3836973946635

Found: 1636
0.9990655060010004
Factor: 1070.097829488984

Found: 2301
0.9991091202690493
Factor: 1122.4859711784322

Found: 2966
0.9991527364410779
Factor: 1180.2702824556827

Found: 3631
0.9991963545171816
Factor: 1244.329771493821

Found: 4296
0.9992399744974296
Factor: 1315.7453225137806

Found: 4961
0.9992835963819096
Factor: 1395.8611804138377

Found: 5626
0.9993272201707055
Factor: 1486.37036436111

Found: 6291
0.9993708458639055
Factor: 1589.4356289324905

Found: 6956
0.9994144734615841
Factor: 1707.8645191824717

Found: 7621
0.9994581029638316
Factor: 1845.3690152402496

Found: 8286
0.9995017343707219
Factor: 2006.9616309898618

Found: 8951
0.9995453676823484
Factor: 2199.579662892779

Found: 9616
0.9995890028987888
Factor: 2433.107185071717

Found: 10281
0.9996326400201271
Factor: 2722.125584685871

Found: 10946
0.9996762790464487
Factor: 3089.0802372434487

Found: 11611
0.9997199199778324
Factor: 3570.4081721354455

Found: 12276
0.999763562814364
Factor: 4229.453151842177

Found: 12941
0.9998072075561245
Factor: 5186.925275241118

Found: 13606
0.9998508542032039
Factor: 6704.84868820779

Found: 14271
0.9998945027556774
Factor: 9478.920576794626

Found: 14936
0.9999381532136303
Factor: 16168.988863107385

Found: 15601
0.999981805577151
Factor: 54961.897293757975

Found: 47468
0.9999890704052052
Factor: 91494.70028956732

*/
//TODO: use continuous fraction of log3/log2 to find better solutions.
