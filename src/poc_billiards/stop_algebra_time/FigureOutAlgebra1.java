package poc_billiards.stop_algebra_time;

import java.util.ArrayList;

import poc_billiards.stop_algebra_time.obj.FlatBoxDimAndDisplacement;

public class FigureOutAlgebra1 {

	public static final int NUM_BOX_TARGET = 4;
	
	public static void main(String args[]) {
		
	
		System.out.println("Start");
		
		for(int area=2; area<900; area++) {
		
			findSolutions(area);
			
		}
		
		
		
	}
	
	
	public static void findSolutions(int area ) {
		long divs[] = UtilityFunctions.UtilityFunctions.getAllDivisors(area);
		
		
		ArrayList<Long> validEvenDims = new ArrayList<Long>();
		
		for(int k=0; k<divs.length; k++) {
			long quotient = area / divs[k];
			if(divs[k] % 2 == 0 && quotient % 2 == 1) {
				
				if(UtilityFunctions.UtilityFunctions.getGCD(quotient, divs[k]) == 1) {
					//TODO: Solve for when gcd(length, width) = 1, and widen the strip latter.
					// For the area 532 solution, the flat flat area is 540, and the gcd 1 area is just 90!
					validEvenDims.add(divs[k]);
				}
			}
		}
		
		if(validEvenDims.size() >= NUM_BOX_TARGET) {
			
			System.out.println("Try N = " + area);
			for(int j=0; j<validEvenDims.size(); j++) {
				
				System.out.println(validEvenDims.get(j) + " x " + (area / validEvenDims.get(j)));
				
				FlatBoxDimAndDisplacement trial = new FlatBoxDimAndDisplacement(area / validEvenDims.get(j), validEvenDims.get(j)  );
			}
			
			System.out.println();
			System.out.println();
			
			findSolutions(area, validEvenDims);
			//TODO: start search here!
		}
		
	}
	
	
	
	public static void findSolutions(int area, ArrayList<Long> validEvenDims) {
		
		
	}
	
	
}
