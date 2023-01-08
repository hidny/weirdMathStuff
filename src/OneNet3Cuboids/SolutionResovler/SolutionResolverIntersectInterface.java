package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Coord.Coord2D;

public interface SolutionResolverIntersectInterface {

	
	public long resolveSolution(CuboidToFoldOn cuboidDimensionsAndNeighbours, Coord2D paperToDevelop[], int indexCuboidonPaper[][][], boolean paperUsed[][]);
	
	
	//TODO: actually use this for all classes instead of doing the weird spooky action at a distance:
	public long getNumUniqueFound();
}
