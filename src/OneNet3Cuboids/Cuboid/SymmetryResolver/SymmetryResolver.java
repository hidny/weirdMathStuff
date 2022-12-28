package OneNet3Cuboids.Cuboid.SymmetryResolver;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.FoldingAlgosNby1by1.FoldResolveOrderedRegionsNby1by1;
import OneNet3Cuboids.Coord.Coord2D;

public class SymmetryResolver {

	public static boolean skipSearchBecauseOfASymmetryArg(CuboidToFoldOn cuboid,
			Coord2D paperToDevelop[],
			int cuboidOnPaperIndex,
			int indexCuboidonPaper[][],
			int rotationToAddCellOnPaper,
			int curRotationOnPaperRelativeCuboid,
			boolean paperUsed[][],
			int cellIndexToUse,
			int cellIndexToAdd) {
		
		if(cuboid.getDimensions()[1] == 1 && cuboid.getDimensions()[2] == 1) {
			return skipSearchBecauseOfASymmetryArgNby1by1(cuboid,
					paperToDevelop,
					cuboidOnPaperIndex,
					indexCuboidonPaper,
					rotationToAddCellOnPaper,
					curRotationOnPaperRelativeCuboid,
					paperUsed,
					cellIndexToUse,
					cellIndexToAdd);

		} else if(cuboid.getDimensions()[0] == 2
				&& cuboid.getDimensions()[1] == 2
				&& cuboid.getDimensions()[2] == 2) {
			
			return skipSearchBecauseOfASymmetryArg2by2by2(cuboid,
					paperToDevelop,
					cuboidOnPaperIndex,
					indexCuboidonPaper,
					rotationToAddCellOnPaper,
					curRotationOnPaperRelativeCuboid,
					paperUsed,
					cellIndexToUse,
					cellIndexToAdd);
		}
		
		return false;
	}
	
	//getDimensions()
	private static boolean skipSearchBecauseOfASymmetryArgNby1by1(CuboidToFoldOn cuboid,
			Coord2D paperToDevelop[],
			int cuboidOnPaperIndex,
			int indexCuboidonPaper[][],
			int rotationToAddCellOnPaper,
			int curRotationOnPaperRelativeCuboid,
			boolean paperUsed[][],
			int cellIndexToUse,
			int cellIndexToAdd) {

		//TODO: put in function
		int new_i = -1;
		int new_j = -1;
		if(rotationToAddCellOnPaper == 0) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i-1;
			new_j = paperToDevelop[cuboidOnPaperIndex].j;
			
		} else if(rotationToAddCellOnPaper == 1) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i;
			new_j = paperToDevelop[cuboidOnPaperIndex].j+1;
			
		} else if(rotationToAddCellOnPaper == 2) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i+1;
			new_j = paperToDevelop[cuboidOnPaperIndex].j;
			
		} else if(rotationToAddCellOnPaper == 3) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i;
			new_j = paperToDevelop[cuboidOnPaperIndex].j-1;
		} else {
			System.out.println("Doh! 3");
			System.out.println("Unknown rotation!");
			System.exit(1);
		}
		//END TODO: put in function
		
		//Special rules for the 1st/bottom node:
		//These rules work because of the 4-way symmetry
		if(cellIndexToUse == 0) {
			
			if(FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0]) < 3 && rotationToAddCellOnPaper == 3) {
				//(Leave cell on left alone unless bottom is touching all 4 cells)
				//nope
				return true;
			} else if(rotationToAddCellOnPaper > 0 && indexCuboidonPaper[paperToDevelop[cuboidOnPaperIndex].i-1][paperToDevelop[cuboidOnPaperIndex].j] <0) {
				//If bottom is done with the cell on top, we're done!
				//nope
				return true;
			}

			//End special rules for the 1st/bottom node.
			
		//Special rules about where to put top in order to take advantage of symmetry:
		} else if(cellIndexToAdd == cuboid.getNumCellsToFill() -1 ) {
			
			
			
			if(curRotationOnPaperRelativeCuboid != 2) {
				//If curRotation is not 2, top isn't above bottom, and that's
				//probably going to mean a duplicate, unless it's a specific 3 bottom case.
				//In that case, it can be right of hub/bottom too.
				//we want top to be above except for (the T intersection case.)
				if(FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 3
						&& curRotationOnPaperRelativeCuboid == 3) {
						//The exception where top can be right of bottom:
						//(the T intersection case.)
						
				} else {
					return true;
				}
				
			} else if(new_j < paperToDevelop[0].j
					&& (FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 1 ||
							FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 4 ||
							(FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 2 
								&& paperUsed[paperToDevelop[0].i + 1][paperToDevelop[0].j]
								&& paperUsed[paperToDevelop[0].i - 1][paperToDevelop[0].j]))
					) {
				//If bottom has 1 or 4 neighbours, or 2 neighbours that are above and below, make top right left of bottom on paper (or directly above)
				
				//i.e.: Only go up and to the right in the 1 bottom and 1 top case.
				return true;
			}
		}
		//END special rules about where to put top in order to take advantage of symmetry
		
		return false;
	}
	
	private static boolean skipSearchBecauseOfASymmetryArg2by2by2(CuboidToFoldOn cuboid,
			Coord2D paperToDevelop[],
			int cuboidOnPaperIndex,
			int indexCuboidonPaper[][],
			int rotationToAddCellOnPaper,
			int curRotationOnPaperRelativeCuboid,
			boolean paperUsed[][],
			int cellIndexToUse,
			int cellIndexToAdd) {
		
		//TODO: put in function
		int new_i = -1;
		int new_j = -1;
		if(rotationToAddCellOnPaper == 0) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i-1;
			new_j = paperToDevelop[cuboidOnPaperIndex].j;
			
		} else if(rotationToAddCellOnPaper == 1) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i;
			new_j = paperToDevelop[cuboidOnPaperIndex].j+1;
			
		} else if(rotationToAddCellOnPaper == 2) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i+1;
			new_j = paperToDevelop[cuboidOnPaperIndex].j;
			
		} else if(rotationToAddCellOnPaper == 3) {
			new_i = paperToDevelop[cuboidOnPaperIndex].i;
			new_j = paperToDevelop[cuboidOnPaperIndex].j-1;
		} else {
			System.out.println("Doh! 3");
			System.out.println("Unknown rotation!");
			System.exit(1);
		}
		//END TODO: put in function
		
		int numNeighboursOrig = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0]);
		int numNeighboursForIndexToUse = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[cuboidOnPaperIndex]);
		int numNeighboursForIndexToAdd = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, new Coord2D(new_i, new_j) );
		
		
		if(cellIndexToUse != 0
				&& numNeighboursForIndexToUse >= numNeighboursOrig) {
				return true;
		}
		
		if(cellIndexToUse != 0
				&& numNeighboursForIndexToAdd > numNeighboursOrig) {
			return true;
		}
		
		/*if(numNeighboursOrig >=3) {
			//Final number of unique solutions: 180
			//180 solutions where all cells have 2 neighbours or less.
			// I thought there would be 0 while trying to figure it out while holding baby.
			return true;
		}*/
		/*if(numNeighboursOrig >=4) {
			//Final number of unique solutions: x
			//x solutions where all cells have 3 neighbours or less.
			return true;
		}*/
		
		return false;
	}
}
