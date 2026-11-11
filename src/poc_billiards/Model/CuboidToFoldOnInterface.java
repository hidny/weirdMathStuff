package poc_billiards.Model;

import poc_billiardsCoord.CoordWithRotationAndIndex;

public interface CuboidToFoldOnInterface {

	public int[] getDimensions();
	public CoordWithRotationAndIndex[] getNeighbours(int cellIndex);
}
