package OneNet3Cuboids.FoldingAlgoMultipleCuboids;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.SolutionResovler.BadIntersectResolverForSmallSolutions;
import OneNet3Cuboids.SolutionResovler.ShapeIntersectionCheckerAtSolutionTime;
import OneNet3Cuboids.SolutionResovler.SolutionResolverInterface;

public class BasicCheckShapeIntersectionBForEachSolutionofA {

	public static void main(String args[]) {
		System.out.println("Intersection resolver by going through all solutions of shapeA, and checking them against shape B:");
		
		//It seems to work now!
/*
		SolutionResolverInterface intersectionCheckerAtSolutionTime = new ShapeIntersectionCheckerAtSolutionTime(new CuboidToFoldOn(3, 2, 1));
		
		FoldResolveOrderedRegionsSkipSymmetries.solveFoldsForSingleCuboid(5, 1, 1, true, intersectionCheckerAtSolutionTime);
*/
		SolutionResolverInterface intersectionCheckerAtSolutionTime = new ShapeIntersectionCheckerAtSolutionTime(new CuboidToFoldOn(3, 3, 1));
		
		FoldResolveOrderedRegionsSkipSymmetries.solveFoldsForSingleCuboid(7, 1, 1, true, intersectionCheckerAtSolutionTime);
		
		//TODO: print dimensions cuboids
		System.out.println("Final number of nets that fit both cuboids: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound);
	}
}
