package horseyWorkSingMasterConj;

public class bruteForceTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//Brute force answers to x^2 = 20 * y^2 - 16
		
		for(long y=0; y<10000000; y++) {
			long x = (long)Math.round(Math.sqrt(20*y*y-16));
			
			if(x*x == 20*y*y-16) {
				System.out.println("(x , y) = ( " + x + ", " + y + ")");
			}
		}
	}
	
	/*
(x , y) = ( 2, 1)
(x , y) = ( 8, 2)
(x , y) = ( 22, 5)
(x , y) = ( 58, 13)
(x , y) = ( 152, 34)
(x , y) = ( 398, 89)
(x , y) = ( 1042, 233)
(x , y) = ( 2728, 610)
(x , y) = ( 7142, 1597)
(x , y) = ( 18698, 4181)
(x , y) = ( 48952, 10946)
(x , y) = ( 128158, 28657)
(x , y) = ( 335522, 75025)
(x , y) = ( 878408, 196418)
(x , y) = ( 2299702, 514229)
(x , y) = ( 6020698, 1346269)
(x , y) = ( 15762392, 3524578)
(x , y) = ( 41266478, 9227465)
	 */

}
