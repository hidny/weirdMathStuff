package tanXMission;

import UtilityFunctions.Fraction;

public class testFunctionsTanX {

	public static void main(String args[]) { 
		
		Fraction pi = PI.pi5000();
		
		Fraction sixtyDeg = Fraction.divide(pi, new Fraction(3 , 1));
		Fraction ninetyDeg = Fraction.divide(pi, new Fraction(2 , 1));
		
		//System.out.println("cos 60 = About half: " + ContinuedFractionApprox.cosApprox(sixtyDeg).getDecimalFormat(15));
		

		//System.out.println("cos 90  = About 0: " + ContinuedFractionApprox.cosApprox(ninetyDeg).getDecimalFormat(15));
		
		System.out.println("badtad 90 = about inf: " + ContinuedFractionApprox.badTanApprox(ninetyDeg).getDecimalFormat(15));
	}
}
