package OneNet3Cuboids.SolutionResovler;

import OneNet3Cuboids.Coord.Coord2D;

public interface SolutionResolverInterface {

	public long resolveSolution(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][]);
}
