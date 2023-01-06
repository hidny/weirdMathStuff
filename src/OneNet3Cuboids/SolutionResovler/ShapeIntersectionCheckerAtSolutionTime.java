package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheck;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetriesMemManage2;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.Region.Region;

public class ShapeIntersectionCheckerAtSolutionTime implements SolutionResolverInterface {

	//Find intersections the naive way:
	// Basically, for all possibly duplicated solutions for shape1, check if there's an intersection with shape2.
	// This isn't the best way to do the intersection search.
	// Ideally, you do a DFS while making sure to satisfy both shapes at every step.
	// What this strategy has going for it is that it's really simple, and I can reuse logic I wrote
	// in MemorylessUniqueCheckSkipSymmetriesMemManage2 and BasicUniqueCheck.
	// This will be the slow and easy method to verify the faster methods.
	// I think this could only work for cuboids of dimensions (5x1x1 and 3x2x1), (7x1x1 and 1x3x3), and if I'm willing to wait over a week, it will work for
	// (8x1x1 and 5x2x1).

	private MemorylessUniqueCheckSkipSymmetriesMemManage2 memorylessUniqueCheckSkipSymmetriesMemManage2;
	public ShapeIntersectionCheckerAtSolutionTime(CuboidToFoldOn otherShape) {
		
		memorylessUniqueCheckSkipSymmetriesMemManage2 = new MemorylessUniqueCheckSkipSymmetriesMemManage2(otherShape);

		//Clear number of uniq solution because this program will be using it
		// uniqList should be empty though...
		BasicUniqueCheck.uniqList.clear();
	}

	private static int numTrials = 0;
	private static int numSolutions = 0;
	
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] OrigPaperUsed) {
		
		long ret = 0L;
		
		numTrials++;
		if(numTrials % 1000000 == 0) {
			System.out.println("Num trials: " + numTrials);
			System.out.println("What's being tried:");
			Utils.printFold(OrigPaperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
		}
		
		FIND_SOLUTION_LOOP:
		for(int index=0; index<paperToDevelop.length; index++) {
			
			for(int rotation=0; rotation<4; rotation++) {

				if(memorylessUniqueCheckSkipSymmetriesMemManage2.isValidSetupAtIndexedStartLocationInitialRotation(paperToDevelop, OrigPaperUsed, index, rotation)) {
					
					if(BasicUniqueCheck.isUnique(OrigPaperUsed)) {
						
						System.out.println("Found intersection:");
						
						numSolutions++;
						FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound++;
						
						System.out.println("Number of different intersections found: " + numSolutions);
						
						Utils.printFold(OrigPaperUsed);
		
						System.out.println();
						System.out.println("Solution 1:");
						Utils.printFoldWithIndex(indexCuboidonPaper);
	
						System.out.println();
						System.out.println("Solution 2:");
						Utils.printFoldWithIndex(memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper());
						
						ret = 1L;
						
						//Undo the changes, so we could reuse memManager2's paper used array and indexCuboidOnPaper array:
						MemorylessUniqueCheckSkipSymmetriesMemManage2.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
								paperToDevelop,
								memorylessUniqueCheckSkipSymmetriesMemManage2.getPaperUsed(),
								memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper(),
								MemorylessUniqueCheckSkipSymmetriesMemManage2.DEFAULT_ROTATION,
								MemorylessUniqueCheckSkipSymmetriesMemManage2.NO_REFLECTION);
						
						break FIND_SOLUTION_LOOP;
					}
					//End if not dup
	
				}//End if intersection
				

				//Undo the changes, so we could reuse memManager2's paper used array and indexCuboidOnPaper array:
				MemorylessUniqueCheckSkipSymmetriesMemManage2.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
						paperToDevelop,
						memorylessUniqueCheckSkipSymmetriesMemManage2.getPaperUsed(),
						memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper(),
						MemorylessUniqueCheckSkipSymmetriesMemManage2.DEFAULT_ROTATION,
						MemorylessUniqueCheckSkipSymmetriesMemManage2.NO_REFLECTION);
			} //End loop for each rotation
			
		} //End loop for each start position
		

		
		return ret;
	}

	
}
