package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheck;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.DupRemover.MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1;
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

	private MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1 memorylessUniqueCheckSkipSymmetriesMemManage2;
	
	private MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1 memorylessUniqueCheckSkipSymmetriesMemManage1;
	
	private boolean quiet = false;
	
	public ShapeIntersectionCheckerAtSolutionTime(CuboidToFoldOn otherShape, boolean quiet) {

		this(otherShape);
		this.quiet = quiet;
		
		numSolutions = 0;
		
	}

	public ShapeIntersectionCheckerAtSolutionTime(CuboidToFoldOn otherShape) {
		
		memorylessUniqueCheckSkipSymmetriesMemManage1 = new MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1(new CuboidToFoldOn(11, 1, 1));

		memorylessUniqueCheckSkipSymmetriesMemManage2 = new MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1(otherShape);

		//Clear number of uniq solution because this program will be using it
		// uniqList should be empty though...
		BasicUniqueCheck.uniqList.clear();
		
	}

	private static int numTrials = 0;
	private static int numSolutions = 0;
	
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] OrigPaperUsed) {
		
		long ret = 0L;
		
		numTrials++;
		if(numTrials % 1000000 == 0 && quiet == false) {
			System.out.println("Num trials: " + numTrials);
			System.out.println("What's being tried:");
			Utils.printFold(OrigPaperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
		}

		int START_INDEX = 0;

		FIND_SOLUTION_LOOP:
		for(int index=0; index<paperToDevelop.length; index++) {
			
			for(int rotation=0; rotation<4; rotation++) {

				if(memorylessUniqueCheckSkipSymmetriesMemManage2.isValidSetupAtIndexedStartLocationWithRotation(paperToDevelop, OrigPaperUsed, index, rotation, START_INDEX)) {
					
					
					if(BasicUniqueCheck.isUnique(OrigPaperUsed)) {
						

						numSolutions++;
						FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound++;
						
						if(! quiet && numSolutions % 10000 == 0) {
	 						System.out.println("Found intersection:");
							
							System.out.println("Number of different intersections found: " + numSolutions);
							
							Utils.printFold(OrigPaperUsed);
			

							System.out.println();
							System.out.println("Solution 1:");
							Utils.printFoldWithIndex(memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper());
							
							System.out.println();
							System.out.println("Solution 2:");
							outer:
							for(int index2=0; index2<paperToDevelop.length; index2++) {
								
								for(int rotation2=0; rotation2<4; rotation2++) {
									if(memorylessUniqueCheckSkipSymmetriesMemManage1
											.isValidSetupAtIndexedStartLocationWithRotation(paperToDevelop, OrigPaperUsed, index2, rotation2, START_INDEX)) {
										Utils.printFoldWithIndex(memorylessUniqueCheckSkipSymmetriesMemManage1.getIndexCuboidOnPaper());
										
										MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
												paperToDevelop,
												memorylessUniqueCheckSkipSymmetriesMemManage1.getPaperUsed(),
												memorylessUniqueCheckSkipSymmetriesMemManage1.getIndexCuboidOnPaper(),
												MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.DEFAULT_ROTATION,
												MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.NO_REFLECTION);
										
										break outer;
									} else {
									
										MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
												paperToDevelop,
												memorylessUniqueCheckSkipSymmetriesMemManage1.getPaperUsed(),
												memorylessUniqueCheckSkipSymmetriesMemManage1.getIndexCuboidOnPaper(),
												MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.DEFAULT_ROTATION,
												MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.NO_REFLECTION);
									}
								}
							}
							
							//Utils.printFoldWithIndex(indexCuboidonPaper);
		
						}
						
						ret = 1L;
						
						//Undo the changes, so we could reuse memManager2's paper used array and indexCuboidOnPaper array:
						MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.eraseChangesToPaperUsedAndIndexCuboidOnPaper(
								paperToDevelop,
								memorylessUniqueCheckSkipSymmetriesMemManage2.getPaperUsed(),
								memorylessUniqueCheckSkipSymmetriesMemManage2.getIndexCuboidOnPaper(),
								MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.DEFAULT_ROTATION,
								MemorylessUniqueCheckSkipSymmetriesMemManage2ForNx1x1.NO_REFLECTION);
						
						break FIND_SOLUTION_LOOP;
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
			} //End loop for each rotation
			
		} //End loop for each start position
		

		return ret;
	}

	@Override
	public long getNumUniqueFound() {
		// TODO Auto-generated method stub
		return numSolutions;
	}

	
}
