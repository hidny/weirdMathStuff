package poc_billiards.Model;

import poc_billiards.Coord.CoordWithRotationAndIndex;

public interface CuboidToFoldOnInterface {

	public int[] getDimensions();
	public CoordWithRotationAndIndex[] getNeighbours(int cellIndex);
}
