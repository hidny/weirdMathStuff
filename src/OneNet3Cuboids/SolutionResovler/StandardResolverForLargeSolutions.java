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

public class StandardResolverForLargeSolutions implements SolutionResolverInterface {
	
	
	private MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1 memorylessUniqueCheckSkipSymmetriesMemManage;
	public StandardResolverForLargeSolutions(CuboidToFoldOn exampleCuboid) {
		
		memorylessUniqueCheckSkipSymmetriesMemManage = new MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1(exampleCuboid);
	}
	
	
	@Override
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] paperUsed) {
		
		//TODO: Maybe have global vars elsewhere? 
		FoldResolveOrderedRegionsSkipSymmetries.numFound++;
		
		if(FoldResolveOrderedRegionsSkipSymmetries.numFound % 100000 == 0) {
			System.out.println(FoldResolveOrderedRegionsSkipSymmetries.numFound +
				" (num unique: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound + ")");
		}
		
		//if(BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed)) {
		if(memorylessUniqueCheckSkipSymmetriesMemManage.isUnique(paperToDevelop, paperUsed, indexCuboidonPaper)) {
			FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound++;

			if(FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound % 100000 == 0) {
				Utils.printFold(paperUsed);
				Utils.printFoldWithIndex(indexCuboidonPaper);
				System.out.println("Num unique solutions found: " + 
						FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound);
			}

			return 1L;
		} else {

			return 0L;
		}
	}
}
