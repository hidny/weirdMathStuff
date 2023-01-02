package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetries;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetriesMemManage;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;

public class StandardResolverForSmallSolutions implements SolutionResolverInterface {

	@Override
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] paperUsed) {
		

		System.out.println(FoldResolveOrderedRegionsSkipSymmetries.numFound +
				" (num unique: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound + ")");
		
		
		//TODO: Maybe have global vars elsewhere? 
		FoldResolveOrderedRegionsSkipSymmetries.numFound++;
		
		
		//Utils.printFoldWithIndex(indexCuboidonPaper);

		//if(BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed)) {
		if(MemorylessUniqueCheckSkipSymmetriesMemManage.isUnique(cuboidDimensionsAndNeighbours, paperToDevelop, paperUsed)) {
			FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound++;

			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			System.out.println("Num unique solutions found: " + 
					FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound);
			

			return 1L;
		} else {

			//System.out.println("Solution not found");
			return 0L;
		}
	}

	
}
