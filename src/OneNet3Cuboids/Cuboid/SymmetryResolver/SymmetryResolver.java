package OneNet3Cuboids.Cuboid.SymmetryResolver;

import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
import OneNet3Cuboids.GraphUtils.OldUtils;
import OneNet3Cuboids.OldReferenceFoldingAlgosNby1by1.FoldResolveOrderedRegionsNby1by1;
import OneNet3Cuboids.Region.Region;
import OneNet3Cuboids.Coord.Coord2D;

public class SymmetryResolver {

	public static boolean skipSearchBecauseOfASymmetryArgDontCareAboutRotation(CuboidToFoldOn cuboid,
			Coord2D paperToDevelop[],
			int indexCuboidonPaper[][], int cuboidOnPaperIndex,
			int cellIndexToUse) {
		
		//Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
		//this is for Nx1x1
		//for Nxkxk, you could do the same thing with corners.
		
		//TODO: this assumes the index cuboid.getNumCellsToFill() -1 is at a corner on the other side of the cuboid...
		//TODO: if bottom isn't just 1 cell, apply this rules to all 4 or corner cells
		
		if(cellIndexToUse == cuboid.getNumCellsToFill() -1 ) {
			if(FoldResolveOrderedRegionsSkipSymmetries.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[cuboidOnPaperIndex])  >=
					FoldResolveOrderedRegionsSkipSymmetries.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0])) {
				
				//Won't save much though...
				return true;
			}
		}
		//End Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
		
		return false;
		
	}
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

	public static boolean skipSearchBecauseCuboidCouldProvablyNotBeBuiltThisWay(CuboidToFoldOn cuboid,
			Coord2D paperToDevelop[],
			int indexCuboidonPaper[][], int cuboidOnPaperIndex,
			int cellIndexToUse,
			Region region,
			int topBottombridgeUsedNx1x1[]) {

		if(cuboid.getDimensions()[1] == 1 && cuboid.getDimensions()[2] == 1) {

			//This assumes we're dealing with a Nx1x1 and
			// top is the last cell index and bottom is cell index 0...
			
			int topCell = cuboid.getNumCellsToFill() -1;
			if(! cuboid.isCellIndexUsed(topCell) && 
					//Make sure the top cell is currently in the region:
					region.getCellRegionsToHandleInRevOrder()[topCell]) {
				
				
				int minCellOrderAllowed = region.getCellIndexToOrderOfDev().get(cellIndexToUse);
				
				if(minCellOrderAllowed > 0
						&& ! isTopCellNx1x1ReachableFromAppropriateCell(region, cuboid, minCellOrderAllowed,
								paperToDevelop, indexCuboidonPaper, cellIndexToUse, topBottombridgeUsedNx1x1)) {
					return true;
				}
				
			}	
		}
		
		//End Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
		
		return false;
		
	}

	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	public static int NUM_ROTATIONS = 4;
	

	//TODO: make sure I implemented BFS properly (i.e. get First, gets the first one...)
	//pre: Nx1x1
	private static boolean isTopCellNx1x1ReachableFromAppropriateCell(Region region, CuboidToFoldOn cuboid, int minCellOrderAllowed,
			Coord2D paperToDevelop[], int indexCuboidonPaper[][], int cellIndexToUseDebug,
			int topBottombridgeUsedNx1x1[]) {
		
		if(minCellOrderAllowed == 0) {
			return true;
		}
		
		
		LinkedList<Integer> queueCells = new LinkedList<Integer>();
		boolean explored[] = new boolean[cuboid.getCellsUsed().length];
		
		int topCellIndex = cuboid.getCellsUsed().length - 1;
		explored[topCellIndex] = true;
		queueCells.add(topCellIndex);
		
		boolean couldTopCouldBeOnRightOfBottom = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0])
				== 3;

		boolean couldGetToTopSoFar = false;
		
		
		BFS_LOOP:
		while(queueCells.isEmpty() == false) {
			
			
			int curCell = queueCells.getFirst();
			queueCells.remove();
			
			for(int n=0; n<NUM_ROTATIONS; n++) {
				
				int neighbour = cuboid.getNeighbours(curCell)[n].getIndex();
				
				if(! explored[neighbour]) {
					explored[neighbour] = true;
					
					if( ! cuboid.isCellIndexUsed(neighbour) ) {

						queueCells.add(neighbour);
					
					} else {
						
						if(region.getCellIndexToOrderOfDev().containsKey(neighbour)
							&& region.getCellIndexToOrderOfDev().get(neighbour)
									< minCellOrderAllowed) {
								//pass
							
		
						} else {
	
							if( ! region.getCellIndexToOrderOfDev().containsKey(neighbour)) {
								//Wrong region, so just assume everything is ok, and the other region will reach the top.
								System.out.println("ERROR: neighbour found is in the wrong region. This shouldn't be possible.");
								System.exit(1);
							}
							

							//Check if cell has valid path to bottom cell:
							
							if(topBottombridgeUsedNx1x1 != null) {
								if(topBottombridgeUsedNx1x1[neighbour] == 0
										|| (topBottombridgeUsedNx1x1[neighbour] == 1 && couldTopCouldBeOnRightOfBottom)) {
									

									//Check that cell found has cell that's allowed to be taken beside it:
									//This is the bare minimum...
									//TODO: maybe work your way up to top with a BFS?
									
									//TODO: maybe double check with 2nd cuboid...
									
									int curI=paperToDevelop[region.getCellIndexToOrderOfDev().get(neighbour)].i;
									int curJ=paperToDevelop[region.getCellIndexToOrderOfDev().get(neighbour)].j;
									//System.out.println(curI + ", " + curJ);
									
									boolean couldBeFree = false;
									
									for(int r=0; r<NUM_ROTATIONS; r++) {

										int new_i = curI + nugdeBasedOnRotation[0][r];
										int new_j = curJ + nugdeBasedOnRotation[1][r];
										//System.out.println(new_i + ", " + new_j);
										
										int rotationToUse = ( r - cuboid.getRotationPaperRelativeToMap(neighbour) + NUM_ROTATIONS) % NUM_ROTATIONS;
										
										int neighbour2 = cuboid.getNeighbours(neighbour)[rotationToUse].getIndex();
										
										if( cuboid.isCellIndexUsed(neighbour2) == false
												&& ! cantAddCellBecauseOfOtherPaperNeighbours(indexCuboidonPaper, cuboid,
												region, neighbour,
												neighbour2, new_i, new_j
											) ) {

											couldBeFree = true;
											break;
										}
									}
									
									if(couldBeFree) {
										couldGetToTopSoFar = true;
										break BFS_LOOP;
									}
								}
							} else {
								//Old way: delete after you prove that it's slower...
								if(OldUtils.couldReachTopNx1x1WithoutGoingThruBottom(region, cuboid, neighbour, minCellOrderAllowed, paperToDevelop, indexCuboidonPaper)) {
									
									couldGetToTopSoFar = true;
									
									break BFS_LOOP;
								}
							}
						}
						
					}
				}
				
				
			}
			
		}
		
		return couldGetToTopSoFar;
		
	}


	public static final int ONE_EIGHTY_ROTATION = 2;

	public static boolean cantAddCellBecauseOfOtherPaperNeighbours( int indexCuboidonPaper[][], CuboidToFoldOn cuboid,
			Region region, int indexToUse,
			int indexNewCell, int new_i, int new_j
		) {	
	boolean cantAddCellBecauseOfOtherPaperNeighbours = false;
	
	int neighboursBasedOnRotation[][] = {{new_i-1, new_j}, {new_i, new_j+1},{new_i+1, new_j},{new_i, new_j - 1}};

	
	for(int rotReq=0; rotReq<neighboursBasedOnRotation.length; rotReq++) {
		
		int i1 = neighboursBasedOnRotation[rotReq][0];
		int j1 = neighboursBasedOnRotation[rotReq][1];
	
		if(indexCuboidonPaper[i1][j1] == indexToUse) {
			continue;
		}
		
		//System.out.println("Paper neighbour:" + i1 + ", " + j1);
		
		if(indexCuboidonPaper[i1][j1] >= 0) {
			//System.out.println("Connected to another paper");
			
			int indexOtherCell = indexCuboidonPaper[i1][j1];
			int rotationOtherCell = cuboid.getRotationPaperRelativeToMap(indexOtherCell);

			if(region.getCellIndexToOrderOfDev().containsKey(indexOtherCell)
					&& region.getCellIndexToOrderOfDev().get(indexOtherCell) < region.getCellIndexToOrderOfDev().get(indexToUse) ) {
				cantAddCellBecauseOfOtherPaperNeighbours = true;
				break;
			}
			
			//There's a 180 rotation because the neighbour is attaching to the new cell (so it's flipped!)
			int neighbourIndexNeeded = (rotReq + ONE_EIGHTY_ROTATION - rotationOtherCell+ NUM_ROTATIONS) % NUM_ROTATIONS;


			if(cuboid.getNeighbours(indexOtherCell)[neighbourIndexNeeded].getIndex() != indexNewCell) {
				cantAddCellBecauseOfOtherPaperNeighbours = true;
				break;
			}
		}
	}
	return cantAddCellBecauseOfOtherPaperNeighbours;
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

		
		int new_j = paperToDevelop[cuboidOnPaperIndex].j + nugdeBasedOnRotation[1][rotationToAddCellOnPaper];
		
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
		int numNeighboursForIndexToAdd = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, new Coord2D(new_i, new_j) );
		

		if(cellIndexToUse != 0
				&& numNeighboursForIndexToAdd > numNeighboursOrig) {
			return true;
		}
		
		//Make sure no cell neighbour will have too many neighbours:
		if(numNeighboursOrig < 4) {
			for(int i2=new_i-1; i2<=new_i+1; i2++) {
				for(int j2=new_j-1; j2<=new_j+1; j2++) {
					
					if(i2 == paperToDevelop[0].i && j2 == paperToDevelop[0].j) {
						continue;
	
					} else if( 
						((i2 == new_i && j2 != new_j)
						|| (i2 != new_i && j2 == new_j))	
						&&	paperUsed[i2][j2]
						&& FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper, new Coord2D(i2, j2))
							>= numNeighboursOrig) {
						return true;
					}
				}
			}
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
