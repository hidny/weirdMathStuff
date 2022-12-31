package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetries;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;

public class StandardResolverForSmallSolutions implements SolutionResolverInterface {

	@Override
	public long resolveSolution(CuboidToFoldOn cuboidUsed, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] paperUsed) {
		
		//TODO: Maybe have global vars elsewhere? 
		FoldResolveOrderedRegionsSkipSymmetries.numFound++;
		
		
		System.out.println(FoldResolveOrderedRegionsSkipSymmetries.numFound +
				" (num unique: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound + ")");
		
		System.out.println("HELLO");
		Utils.printFoldWithIndex(indexCuboidonPaper);
		
		//TODO: don't hard-code dimensions:
		if(MemorylessUniqueCheckSkipSymmetries.isUnique(cuboidUsed, paperToDevelop, paperUsed)) {
			System.out.println("TODO");
		}
		
		if(BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed)) {
			FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound++;

			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			System.out.println("Num unique solutions found: " + 
					FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound);
			

			return 1L;
		} else {

			return 0L;
		}
	}

	
}
