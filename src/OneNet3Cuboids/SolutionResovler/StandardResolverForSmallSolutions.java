package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.OldReferenceDupRemovers.MemorylessUniqueCheckSkipSymmetriesForNx1x1NoMemManage;
import OneNet3Cuboids.OldReferenceDupRemovers.MemorylessUniqueCheckSkipSymmetriesForNx1x1MemManageOrig;
import OneNet3Cuboids.OldReferenceDupRemovers.MemorylessUniqueCheckSkipSymmetriesMemManage3ForNx1x1WithFancyValidTrick;

public class StandardResolverForSmallSolutions implements SolutionResolverInterface {

	private MemorylessUniqueCheckSkipSymmetriesMemManage3ForNx1x1WithFancyValidTrick memorylessUniqueCheckSkipSymmetriesMemManage3;
	public StandardResolverForSmallSolutions(CuboidToFoldOn exampleCuboid) {
		
		memorylessUniqueCheckSkipSymmetriesMemManage3 = new MemorylessUniqueCheckSkipSymmetriesMemManage3ForNx1x1WithFancyValidTrick(exampleCuboid);
	}
	
	@Override
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] paperUsed) {
		

		System.out.println(FoldResolveOrderedRegionsSkipSymmetries.numFound +
				" (num unique: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound + ")");
		
		
		//TODO: Maybe have global vars elsewhere? 
		FoldResolveOrderedRegionsSkipSymmetries.numFound++;
		
		
		//Utils.printFoldWithIndex(indexCuboidonPaper);

		//if(BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed)) {
		if(memorylessUniqueCheckSkipSymmetriesMemManage3.isUnique(paperToDevelop, paperUsed, indexCuboidonPaper)) {
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
