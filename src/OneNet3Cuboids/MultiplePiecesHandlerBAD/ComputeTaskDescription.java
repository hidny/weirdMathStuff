package OneNet3Cuboids.MultiplePiecesHandlerBAD;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.FancyTricks.ThreeBombHandler;
import OneNet3Cuboids.Region.Region;
import OneNet3Cuboids.SolutionResovler.SolutionResolverIntersectInterface;

public class ComputeTaskDescription {
	Coord2D paperToDevelop[];
	int indexCuboidonPaper[][];
	boolean paperUsed[][];
	CuboidToFoldOn cuboid;
	int numCellsUsedDepth;
	Region regions[]; long limitDupSolutions; 
	boolean skipSymmetries; 
	SolutionResolverIntersectInterface solutionResolver; 
	CuboidToFoldOn cuboidToBringAlongStartRot; 
	int indexCuboidOnPaper2ndCuboid[][];
	int topBottombridgeUsedNx1x1[];
	ThreeBombHandler threeBombHandler;
	boolean debugNope; long debugIterations[];


	public ComputeTaskDescription(Coord2D[] paperToDevelop, int[][] indexCuboidonPaper, boolean[][] paperUsed,
			CuboidToFoldOn cuboid, int numCellsUsedDepth, Region[] regions, long limitDupSolutions,
			boolean skipSymmetries, SolutionResolverIntersectInterface solutionResolver,
			CuboidToFoldOn cuboidToBringAlongStartRot, int[][] indexCuboidOnPaper2ndCuboid,
			int[] topBottombridgeUsedNx1x1, ThreeBombHandler threeBombHandler, boolean debugNope,
			long[] debugIterations) {
		super();
		this.paperToDevelop = paperToDevelop;
		this.indexCuboidonPaper = indexCuboidonPaper;
		this.paperUsed = paperUsed;
		this.cuboid = cuboid;
		this.numCellsUsedDepth = numCellsUsedDepth;
		this.regions = regions;
		this.limitDupSolutions = limitDupSolutions;
		this.skipSymmetries = skipSymmetries;
		this.solutionResolver = solutionResolver;
		this.cuboidToBringAlongStartRot = cuboidToBringAlongStartRot;
		this.indexCuboidOnPaper2ndCuboid = indexCuboidOnPaper2ndCuboid;
		this.topBottombridgeUsedNx1x1 = topBottombridgeUsedNx1x1;
		this.threeBombHandler = threeBombHandler;
		this.debugNope = debugNope;
		this.debugIterations = debugIterations;
	}
	
	
}
