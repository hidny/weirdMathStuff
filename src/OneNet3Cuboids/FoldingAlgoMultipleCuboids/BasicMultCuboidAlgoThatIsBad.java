package OneNet3Cuboids.FoldingAlgoMultipleCuboids;

import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.SolutionResovler.BadIntersectResolverForSmallSolutions;
import OneNet3Cuboids.SolutionResovler.SolutionResolverInterface;

public class BasicMultCuboidAlgoThatIsBad {

	public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered Regions start anywhere:");
		FoldResolveOrderedRegionsSkipSymmetries.solveFoldsForSingleCuboid(5, 1, 1);
		//FoldResolveOrderedRegionsSkipSymmetries.solveFoldsForSingleCuboid(1, 1, 1);
		
		System.out.println("Done getting all solutious for 1st cuboid.");
		for(int i=0; i<10; i++) {
			System.out.println();
		}
		
		//Find the number of intersections with a special solution resolver
		FoldResolveOrderedRegionsSkipSymmetries.numFound = 0;
		FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound = 0;
		
		SolutionResolverInterface solutionResolverIntersect = new BadIntersectResolverForSmallSolutions();

		//FoldResolveOrderedRegionsSkipSymmetries.solveFoldsForSingleCuboid(1, 1, 1, true, solutionResolverIntersect);
		FoldResolveOrderedRegionsSkipSymmetries.solveFoldsForSingleCuboid(3, 2, 1, true, solutionResolverIntersect);
		
		//TODO: print dimensions cuboids
		System.out.println("Final number of nets that fit both cuboids: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound);
	}
}
