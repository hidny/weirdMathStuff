package OneNet3Cuboids.OLDDFSWithCoolDebugTricks;

import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.FoldingAlgoStartAnywhere.FoldResolveOrderedRegionsSkipSymmetries;
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
			Region region) {
		
		//Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
		//this is for Nx1x1
		//for Nxkxk, you could do the same thing with corners.
		
		//TODO: this assumes the index cuboid.getNumCellsToFill() -1 is at a corner on the other side of the cuboid...
		//TODO: if bottom isn't just 1 cell, apply this rules to all 4 or corner cells
		
		if(cuboid.getDimensions()[1] == 1 && cuboid.getDimensions()[2] == 1) {
			
			int topCell = cuboid.getNumCellsToFill() -1;
			if(! cuboid.isCellIndexUsed(topCell)) {
				
				
				int minCellOrderAllowed = region.getCellIndexToOrderOfDev().get(cellIndexToUse);
				
				if(minCellOrderAllowed > 0
						&& ! isTopCellNx1x1ReachableFromAppropriateCell(region, cuboid, minCellOrderAllowed,
								paperToDevelop, indexCuboidonPaper, cellIndexToUse)) {
					return true;
				}
				
			}	
		}
		
		//End Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
		
		return false;
		
	}
	
	public static int NUM_ROTATIONS = 4;
	
	//pre: Nx1x1
	//TODO: copy/paste code:
	public static boolean isTopCellNx1x1ReachableFromAppropriateCell(Region region, CuboidToFoldOn cuboid, int minCellOrderAllowed,
			Coord2D paperToDevelop[], int indexCuboidonPaper[][], int cellIndexToUseDebug) {
		
		if(minCellOrderAllowed == 0) {
			return true;
		}
		
		LinkedList<Integer> queueCells = new LinkedList<Integer>();
		boolean explored[] = new boolean[cuboid.getCellsUsed().length];
		
		int topCellIndex = cuboid.getCellsUsed().length - 1;
		explored[topCellIndex] = true;
		queueCells.add(topCellIndex);
		

		boolean couldGetToTopSoFar = false;
		
		//System.out.println("START LOOP");
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
							//Check appropriate
	
							//if(neighbour == 14 && indexCuboidonPaper[cuboid.getCellsUsed().length - 4][cuboid.getCellsUsed().length + 1] == 14) {
							//	System.out.println("DEBUG123");
							//}
							
							if( ! region.getCellIndexToOrderOfDev().containsKey(neighbour)) {
								//Wrong region:
								couldGetToTopSoFar = true;
								break BFS_LOOP;
							}
							
						
							
							if(couldReachTopNx1x1WithoutGoingThruBottom(region, cuboid, neighbour, minCellOrderAllowed, paperToDevelop, indexCuboidonPaper)) {
								
								
								couldGetToTopSoFar = true;
								
								break BFS_LOOP;
							}
						}
						
					}
				}
				
				
			}
			
		}
		//System.out.println("...");
		/*if(couldGetToTopSoFar == false) {
			System.out.println("Test true");
			System.out.println(minCellOrderAllowed);
			System.out.println("Cell to use: " + cellIndexToUseDebug);
			
			Utils.printFoldWithIndex(indexCuboidonPaper);

			for(int i=0; i< cuboid.getNumCellsToFill(); i++) {
				if(cuboid.isCellIndexUsed(i)) {
					System.out.println(i + " -> " + region.getCellIndexToOrderOfDev().get(i));
				}
			}
			
			System.out.println("What");
			//System.exit(1);
			
		}*/
		return couldGetToTopSoFar;
		
		
	}
	

	public static final int nugdeBasedOnRotation[][] = {{-1, 0, 1, 0}, {0, 1, 0 , -1}};
	
	public static final int BOTTOM_Nx1x1 = 0;
	
	//TODO: cache/memorise this info later...
	//TODO: It's very wasteful to check each time.
	public static boolean couldReachTopNx1x1WithoutGoingThruBottom(Region region, CuboidToFoldOn cuboid, int startIndex, int minCellOrderAllowed,
			Coord2D paperToDevelop[], int indexCuboidonPaper[][]) {

		if(minCellOrderAllowed == 0) {
			return true;
		}
		
		/*if(startIndex == 16) {
			Utils.printFoldWithIndex(indexCuboidonPaper);
			System.out.println("Debug TODO");
		}*/
		
		
		boolean couldTopCouldBeOnRightOfBottom = FoldResolveOrderedRegionsNby1by1.getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0])
				== 3;
		
		LinkedList<Integer> queueCells = new LinkedList<Integer>();
		boolean explored[] = new boolean[region.getCellRegionsToHandleInRevOrder().length];
		
		explored[startIndex] = true;
		queueCells.add(startIndex);
		
		int startI=paperToDevelop[region.getCellIndexToOrderOfDev().get(startIndex)].i;
		int bottomI=paperToDevelop[0].i;
		int bottomJ=paperToDevelop[0].j;
		
		//Shortcut: if the first cell is below bottom on paper, and it's not the 3 neighbours of bottom case,
		// we know it's a no-go:
		//TODO: cache/let algo memorise the answer to this function.
		//Remember: higher i goes down!
		if(startI > bottomI && !couldTopCouldBeOnRightOfBottom) {
			return false;
		}
		
		while(queueCells.isEmpty() == false) {
			
			int curCellIndex = queueCells.getFirst();
			queueCells.remove();
			
			if( ! region.getCellIndexToOrderOfDev().containsKey(curCellIndex)) {
				//Don't worry about cells developed in other regions??
				continue;
			}

			//Weird, but it works:
			//TODO: make a function out of it!
			int curI=paperToDevelop[region.getCellIndexToOrderOfDev().get(curCellIndex)].i;
			int curJ=paperToDevelop[region.getCellIndexToOrderOfDev().get(curCellIndex)].j;
			
			//Sanity check to be deleted:
			if(indexCuboidonPaper[curI][curJ] != curCellIndex) {
				System.out.println("oops geting curI curJ didn't work!");
				System.exit(1);
			}
			//End sanity check to be deleted.
			

			if(cuboid.getNeighbours(BOTTOM_Nx1x1)[0].getIndex() == curCellIndex && cellsAreNeighbours(curI, curJ, bottomI, bottomJ)) {
				return true;
			} else if(cuboid.getNeighbours(BOTTOM_Nx1x1)[1].getIndex() == curCellIndex
					&& couldTopCouldBeOnRightOfBottom
					&& cellsAreNeighbours(curI, curJ, bottomI, bottomJ)) {
				
				/*
				System.out.println(minCellOrderAllowed);
				Utils.printFoldWithIndex(indexCuboidonPaper);

				for(int i=0; i< cuboid.getNumCellsToFill(); i++) {
					if(cuboid.isCellIndexUsed(i)) {
						System.out.println(i + " -> " + region.getCellIndexToOrderOfDev().get(i));
					}
				}*/
				return true;
			} else {
				
				for(int i=1; i<NUM_ROTATIONS; i++) {
					if(cuboid.getNeighbours(BOTTOM_Nx1x1)[i].getIndex() == curCellIndex
							 && cellsAreNeighbours(curI, curJ, bottomI, bottomJ)) {
						return false;
					}
				}
			}
			
			
			for(int n=0; n<NUM_ROTATIONS; n++) {

				int new_i = curI + nugdeBasedOnRotation[0][n];
				int new_j = curJ + nugdeBasedOnRotation[1][n];
				
				int neighbour = indexCuboidonPaper[new_i][new_j];
				
				//Sanity test:
				if(neighbour == 0) {
					System.out.println("DOH! We shouldn't find the bottom in this search!");
					System.exit(1);
				}
				//End sanity test
				
				if( neighbour >= 0 && ! explored[neighbour]){
					
					explored[neighbour] = true;
					queueCells.add(neighbour);
					
					
					
				}
				
			}
		
		}
		
		if(queueCells.isEmpty() ) {
			System.out.println("DOH!");
			System.exit(1);
		}
		
		return false;
	}
	
	//TODO: maybe make this 2D coords
	private static boolean cellsAreNeighbours(int curi, int curj, int curi2, int curj2) {
		
		if(Math.abs(curi - curi2) + Math.abs(curj - curj2) == 1) {
			return true;
		} else {
			return false;
		}
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
