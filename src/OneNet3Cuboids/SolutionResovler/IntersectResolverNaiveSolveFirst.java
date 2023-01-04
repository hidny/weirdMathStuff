package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetriesMemManage2;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;

public class IntersectResolverNaiveSolveFirst implements SolutionResolverInterface {

	//Hack to find intersections
	//This assumes that BasicUniqueCheckImproved has all the solution for the 1st cuboid,
	// and we found a solution for the 2nd cuboid:

	private MemorylessUniqueCheckSkipSymmetriesMemManage2 memorylessUniqueCheckSkipSymmetriesMemManage2;
	public IntersectResolverNaiveSolveFirst(CuboidToFoldOn otherShape) {
		
		memorylessUniqueCheckSkipSymmetriesMemManage2 = new MemorylessUniqueCheckSkipSymmetriesMemManage2(otherShape);
	}
	
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] paperUsed) {
		
		//TODO: this should be easy!
		//Maybe just copy/paste the code to get it done?
		
		/*
		for(int index=0; index<validSetup.length; index++) {
			
			
			if(isValidSetup(paperToDevelop, paperUsed, index)) {
				//Check dups with standard dup checker
				// If not dup, print solution
					System.out.println("Found intersection:");
					
					Utils.printFold(paperUsed);
	
					System.out.println("Solution 1:")
					Utils.printFoldWithIndex(indexCuboidonPaper);
	
					System.out.println("Solution 2:")
					Utils.printFoldWithIndex(indexCuboidonPaperUsed);
					
					return 1L
				//End if not dup
			}
			
			
			eraseChangesToPaperUsedAndIndexCuboidOnPaper(
					paperToDevelop,
					paperUsed,
					indexCuboidOnPaper,
					DEFAULT_ROTATION,
					NO_REFLECTION);
		}
		*/
		return 0L;
	}

	
}
