package OneNet3Cuboids.SolutionResovler;

import java.math.BigInteger;
import java.util.ArrayList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;

public class BadIntersectResolverForSmallSolutions implements SolutionResolverInterface {

	//Hack to find intersections
	//This assumes that BasicUniqueCheckImproved has all the solution for the 1st cuboid,
	// and we found a solution for the 2nd cuboid:
	@Override
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int[][] indexCuboidonPaper, boolean[][] paperUsed) {
		
		//TODO: Maybe have global vars elsewhere? 
		FoldResolveOrderedRegionsSkipSymmetries.numFound++;
		
		if(FoldResolveOrderedRegionsSkipSymmetries.numFound % 100000 == 0) {
			System.out.println(FoldResolveOrderedRegionsSkipSymmetries.numFound +
				" (num unique: " + FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound + ")");
		}
		boolean foundMatchInOtherCuboid = ! BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed);
		
		 BasicUniqueCheckImproved.uniqList.remove(BasicUniqueCheckImproved.debugLastScore);
		
		//System.out.println("Current list size: " + BasicUniqueCheckImproved.uniqList.size());
		
		if(! foundMatchInOtherCuboid) {
			
			return 0L;
		} else {

			answersBadIntersect.add(BasicUniqueCheckImproved.debugLastScore);
			
			FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound++;

			Utils.printFold(paperUsed);
			Utils.printFoldWithIndex(indexCuboidonPaper);
			System.out.println("Num solutions found that fit both cuboids: " + 
					FoldResolveOrderedRegionsSkipSymmetries.numUniqueFound);
			
			return 1L;
		}
	}

	public static ArrayList<BigInteger> answersBadIntersect = new ArrayList<BigInteger>();
	
}
