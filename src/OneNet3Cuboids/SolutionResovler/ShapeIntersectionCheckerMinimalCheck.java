package OneNet3Cuboids.SolutionResovler;

import java.util.ArrayList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheck;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.GraphUtils.PivotCellDescription;
import OneNet3Cuboids.Region.Region;

public class ShapeIntersectionCheckerMinimalCheck implements SolutionResolverInterface {

	//Find intersections in a slightly faster way way:
	// Basically, for all possibly duplicated solutions for shape1, check if there's an intersection with shape2,
	// but only check starting positions in shape 2 that are unique under rotations and reflections.
	// That "but" makes it up to 8X faster than ShapeIntersectionCheckerAtSolutionTime.
	
	// As mentioned in ShapeIntersectionCheckerAtSolutionTime, this isn't the best way to do the intersection search.
	// See ShapeIntersectionCheckerAtSolutionTime for more details.

	private MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1 memorylessUniqueCheckSkipSymmetriesMemManage2;

	public ShapeIntersectionCheckerMinimalCheck(CuboidToFoldOn otherShape) {
		
		memorylessUniqueCheckSkipSymmetriesMemManage2 = new MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1(otherShape);

		startingPointsAndRotationsToCheck = PivotCellDescription.getUniqueRotationListsWithCellInfo(otherShape);
		
		//Clear number of uniq solution because this program will be using it
		// uniqList should be empty though...
		BasicUniqueCheck.uniqList.clear();
	}

	//I got the wrong answer :( (only 2185, but should be 2263)
	
	private static int numTrials = 0;
	private static int numSolutions = 0;
	
	private static ArrayList<PivotCellDescription> startingPointsAndRotationsToCheck;
	
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] OrigPaperUsed) {
		
		long ret = 0L;
		
		numTrials++;
		if(numTrials % 1000000 == 0) {
			System.out.println("Num trials: " + numTrials);
			System.out.println("What's being tried:");
			Utils.printFold(OrigPaperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
		}
		
		int PAPER_TO_DEVELOP_INDEX_TO_USE = 0;

		for(int i=0; i<startingPointsAndRotationsToCheck.size(); i++) {
			
			int index =startingPointsAndRotationsToCheck.get(i).getCellIndex();
			int rotation = startingPointsAndRotationsToCheck.get(i).getRotationRelativeToCuboidMap();
		

			if(memorylessUniqueCheckSkipSymmetriesMemManage2.isValidSetupAtIndexedStartLocationWithRotation(paperToDevelop, OrigPaperUsed, PAPER_TO_DEVELOP_INDEX_TO_USE, rotation, index)) {
				
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
					MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
							paperToDevelop,
							memorylessUniqueCheckSkipSymmetriesMemManage2.getPaperUsed(),
							memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper(),
							MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.DEFAULT_ROTATION,
							MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.NO_REFLECTION);
					
					break;
				}
				//End if not dup

			}//End if intersection
			

			//Undo the changes, so we could reuse memManager2's paper used array and indexCuboidOnPaper array:
			MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
					paperToDevelop,
					memorylessUniqueCheckSkipSymmetriesMemManage2.getPaperUsed(),
					memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper(),
					MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.DEFAULT_ROTATION,
					MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.NO_REFLECTION);
			
			//TODO: delete
			//System.out.println("Print fold:");
			//Utils.printFoldWithIndex(memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper());
			//END TODO
			
		} //End loop for each start position
		

		
		return ret;
	}

	@Override
	public long getNumUniqueFound() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}
