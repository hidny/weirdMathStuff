package OneNet3Cuboids.OldReferenceFoldingAlgosNby1by1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.DataModelViews;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.Coord.CoordWithRotationAndIndex;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import number.IsNumber;

public class FoldResolverDivideAndConquer {

	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	
	public static void solveFoldsForSingleCuboid(int a, int b, int c) {
		
		
		//cube.set start location 0 and rotation 0
		

		//TODO: LATER use hashes to help.. (record potential expansions, and no-nos...)
		Coord2D paperToDevelop[] = new Coord2D[Utils.getTotalArea(a, b, c)];
		for(int i=0; i<paperToDevelop.length; i++) {
			paperToDevelop[i] = null;
		}
		
		int GRID_SIZE = 2*Utils.getTotalArea(a, b, c);
	
		boolean paperUsed[][] = new boolean[GRID_SIZE][GRID_SIZE];
		int indexCuboidOnPaper[][] = new int[GRID_SIZE][GRID_SIZE];
		
		for(int i=0; i<paperUsed.length; i++) {
			for(int j=0; j<paperUsed[0].length; j++) {
				paperUsed[i][j] = false;
				indexCuboidOnPaper[i][j] = -1;
			}
		}

		//Default start location GRID_SIZE / 2, GRID_SIZE / 2
		int START_I = GRID_SIZE/2;
		int START_J = GRID_SIZE/2;
		
		CuboidToFoldOn cuboid = new CuboidToFoldOn(a, b, c);
		//Insert start cell:
		
		//Once this reaches the total area, we're done!
		int numCellsUsedDepth = 0;

		int START_INDEX = 0;
		int START_ROTATION = 0;
		paperUsed[START_I][START_J] = true;
		paperToDevelop[numCellsUsedDepth] = new Coord2D(START_I, START_J);
		
		cuboid.setCell(START_INDEX, START_ROTATION);
		indexCuboidOnPaper[START_I][START_J] = START_INDEX;
		numCellsUsedDepth += 1;
		

		int minOrderedCellCouldUsePerRegion[] = new int[1];
		minOrderedCellCouldUsePerRegion[0] = 0;
		int minCellRotationOfMinCellToDevPerRegion[] = new int[1];
		minCellRotationOfMinCellToDevPerRegion[0] = 0;

		HashMap <Integer, Integer> CellIndexToOrderOfDev[] = new HashMap[1];
		CellIndexToOrderOfDev[0] = new HashMap <Integer, Integer>();
		CellIndexToOrderOfDev[0].put(START_INDEX, 0);
		
		boolean CellRegionsToHandleInRevOrder[][] = new boolean[1][cuboid.getNumCellsToFill()];
		
		for(int j=0; j<CellRegionsToHandleInRevOrder[0].length; j++) {
			CellRegionsToHandleInRevOrder[0][j] = true;
		}
		
		doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, CellIndexToOrderOfDev, CellRegionsToHandleInRevOrder, minOrderedCellCouldUsePerRegion, minCellRotationOfMinCellToDevPerRegion, -1L);
		
		System.out.println("Final number of unique solutions: " + numUniqueFound);
	}
	
	private static int numFound = 0;
	private static int numUniqueFound = 0;
	
	
	public static long doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			HashMap <Integer, Integer> CellIndexToOrderOfDev[], boolean CellRegionsToHandleInRevOrder[][], int minOrderedCellCouldUsePerRegion[], int minCellRotationOfMinCellToDevPerRegion[], long limitDupSolutions) {

		//Debug
		if(numCellsUsedDepth < CellIndexToOrderOfDev[0].size()) {
			System.out.println("WHAT???");
			System.exit(1);
		}
		
		if(numCellsUsedDepth == cuboid.getNumCellsToFill()) {
			//System.out.println("Done!");
			
			//Utils.printFold(paperUsed);
			//Utils.printFoldWithIndex(indexCuboidonPaper);
			
			numFound++;
			
			
			if((numCellsUsedDepth < 12)
					|| (numCellsUsedDepth < 20 && numFound % 10000 == 0)
					|| (numCellsUsedDepth < 27 && numFound % 100000 == 0)
					|| numFound % 10000000 == 0) {
				System.out.println(numFound + " (num unique: " + numUniqueFound + ")");
			}
			
			if(BasicUniqueCheckImproved.isUnique(paperToDevelop, paperUsed)) {
				numUniqueFound++;
				
				if(numCellsUsedDepth < 12
						|| (numCellsUsedDepth < 20 && numUniqueFound % 2000 == 0)
						|| (numCellsUsedDepth < 27 && numUniqueFound % 100000 == 0)
						|| numUniqueFound % 1000000 == 0) {
					System.out.println("Found unique net:");
					Utils.printFold(paperUsed);
					Utils.printFoldWithIndex(indexCuboidonPaper);
				
					System.out.println("Num unique solutions found: " + numUniqueFound);
				}
				
			}

			return 1L;
		}
		

		long retDuplicateSolutions = 0L;
		int regionIndex = CellRegionsToHandleInRevOrder.length - 1;
		
		
		//Check if last region is empty and make adjustments if so:
		if(regionIndex > 0 || limitDupSolutions >= 0) {
			int numLeft = 0;
			for(int i=0; i<CellRegionsToHandleInRevOrder[regionIndex].length; i++) {
				if(CellRegionsToHandleInRevOrder[regionIndex][i]) {
					//TODO: loop might be slow, but whatever!
					numLeft++;
					break;
				}
			}
			
			if(numLeft == 0) {
				
				if(regionIndex == 0) {
					//Found solution to a specific region and not the whole thing
					// (sorry about the messy code. I didn't want to make a 
					// separate function for finding seperate regions.
					
					if(limitDupSolutions >=0) {
						return 1L;
					} else {
						System.out.println("ERROR: ran out of regions because completing the cuboid. This should not be possible!");
						System.exit(1);
					}
				}
				
				regionIndex--;
				
				boolean CellRegionsToHandleInRevOrderNew[][] = new boolean[CellRegionsToHandleInRevOrder.length - 1][cuboid.getNumCellsToFill()] ;
				
				//TODO: maybe I can be lazy and let this be a long array that doesn't need changing?
				// Nah...
				int minOrderedCellCouldUsePerRegionNew[] = new int[minOrderedCellCouldUsePerRegion.length -1];
				int minCellRotationOfMinCellToDevPerRegionNew[] = new int[minCellRotationOfMinCellToDevPerRegion.length -1];
				
				HashMap <Integer, Integer> CellIndexToOrderOfDevNew[] = new HashMap[minOrderedCellCouldUsePerRegion.length -1];
				
				for(int i=0; i<CellRegionsToHandleInRevOrderNew.length; i++) {
					CellRegionsToHandleInRevOrderNew[i] = CellRegionsToHandleInRevOrder[i];
					minOrderedCellCouldUsePerRegionNew[i] = minOrderedCellCouldUsePerRegion[i];
					minCellRotationOfMinCellToDevPerRegionNew[i] = minCellRotationOfMinCellToDevPerRegion[i];
					CellIndexToOrderOfDevNew[i] = CellIndexToOrderOfDev[i];
				}
				
				
				//I'm hoping this won't affect the functions calling this function:
				CellRegionsToHandleInRevOrder = CellRegionsToHandleInRevOrderNew;
				minOrderedCellCouldUsePerRegion = minOrderedCellCouldUsePerRegionNew;
				minCellRotationOfMinCellToDevPerRegion = minCellRotationOfMinCellToDevPerRegionNew;
				CellIndexToOrderOfDev = CellIndexToOrderOfDevNew;
				
				
				int numLeft2 = 0;
				for(int i=0; i<CellRegionsToHandleInRevOrder[regionIndex].length; i++) {
					if(CellRegionsToHandleInRevOrder[regionIndex][i]) {
						numLeft2++;
						break;
					}
				}
				
				if(numLeft2 == 0) {
					System.out.println("ERROR: 2nd last region is empty. This should not happen!");

					Utils.printFold(paperUsed);
					Utils.printFoldWithIndex(indexCuboidonPaper);
					System.exit(1);
				}
				
			}
		}
		//End check if last region is empty and make adjustments

		//DEPTH-FIRST START:
		
		for(int i=0; i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
			
			//System.out.println("Coord i,j : " + coord_i + ", " + coord_j);
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			if(indexToUse < 0) {
				System.out.println("Doh!");
				System.exit(1);
			}

			if(CellIndexToOrderOfDev[regionIndex].containsKey(indexToUse)
					&& CellIndexToOrderOfDev[regionIndex].get(indexToUse) < minOrderedCellCouldUsePerRegion[regionIndex]) {
				continue;
			}
			
			//Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
			//TODO: improve hack for Neighbours ==2 (seperate corner vs straight case)
			//i.e. if 0/bottom has straight, top has straight (at least enforces 1 rotation vs 3 rotations 
			if(indexToUse == cuboid.getNumCellsToFill() -1 ) {
				if(getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[i])  >=
						getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0])) {
					
					
					//TODO: have a flag that says to not bother getting neighbours for future depth...
					//Won't save much though...
					continue;
				}
			}
			//End Hack to enforce order where bottom of cuboid has just as many or more than top of cuboid.
			
			
			
			CoordWithRotationAndIndex neighbours[] = cuboid.getNeighbours(indexToUse);
			
			int curRotation = cuboid.getRotationPaperRelativeToMap(indexToUse);
			if(curRotation < 0) {
				System.out.println("Doh! 2");
				System.exit(1);
			}
			
			//System.out.println("Current rotation:");
			//System.out.println(curRotation);
			
			
			for(int j=0; j<neighbours.length; j++) {
				
				if(cuboid.isCellIndexUsed(neighbours[j].getIndex())) {
					
					//TODO2: have a flag that says to not bother getting neighbours for future depth...
					//Won't save much though... if no rotation works.
					
					//Don't reuse a used cell:
					continue;
				} else if(CellIndexToOrderOfDev[regionIndex].containsKey(indexToUse)
						&& CellIndexToOrderOfDev[regionIndex].get(indexToUse) == minOrderedCellCouldUsePerRegion[regionIndex] 
						&& j < minCellRotationOfMinCellToDevPerRegion[regionIndex]) {
					continue;

				} else if(CellRegionsToHandleInRevOrder[CellRegionsToHandleInRevOrder.length - 1][neighbours[j].getIndex()] == false) {
					continue;
				}
				
				//TODO: neighbours should have an index!
				
				//Try adding it or not
				//System.out.println(neighbours[j].getA() + "," + neighbours[j].getB() + "," + neighbours[j].getC());
				
				int rotationToAddCellOn = (j + curRotation) % NUM_ROTATIONS;
				
				//TODO: put in function
				int new_i = -1;
				int new_j = -1;
				if(rotationToAddCellOn == 0) {
					new_i = paperToDevelop[i].i-1;
					new_j = paperToDevelop[i].j;
					
				} else if(rotationToAddCellOn == 1) {
					new_i = paperToDevelop[i].i;
					new_j = paperToDevelop[i].j+1;
					
				} else if(rotationToAddCellOn == 2) {
					new_i = paperToDevelop[i].i+1;
					new_j = paperToDevelop[i].j;
					
				} else if(rotationToAddCellOn == 3) {
					new_i = paperToDevelop[i].i;
					new_j = paperToDevelop[i].j-1;
				} else {
					System.out.println("Doh! 3");
					System.out.println("Unknown rotation!");
					System.exit(1);
				}
				//END TODO: put in function

				int indexNewCell = neighbours[j].getIndex();
		
				if(paperUsed[new_i][new_j]) {
					//Cell we are considering to add is already there...
					continue;
				}
				
				int rotationNeighbourPaperRelativeToMap = (curRotation - neighbours[j].getRot() + NUM_ROTATIONS) % NUM_ROTATIONS;
				
				//Special rules for the 1st/bottom node:
				//These rules work because of the 4-way symmetry
				if(indexToUse == 0) {
					
					if(getNumUsedNeighbourCellonPaper(indexCuboidonPaper,paperToDevelop[0]) < 3 && rotationToAddCellOn == 3) {
						//(Leave cell on left alone unless bottom is touching all 4 cells)
						//nope
						continue;
					} else if(rotationToAddCellOn > 0 && indexCuboidonPaper[paperToDevelop[i].i-1][paperToDevelop[i].j] <0) {
						//If bottom is done with the cell on top, we're done!
						//nope
						continue;
					}

					//End special rules for the 1st/bottom node.
					
				//Special rules about where to put top in order to take advantage of symmetry:
				} else if(neighbours[j].getIndex() == cuboid.getNumCellsToFill() -1 ) {
					
					
					
					if(curRotation != 2) {
						//If curRotation is not 2, top isn't above bottom, and that's
						//probably going to mean a duplicate, unless it's a specific 3 bottom case.
						//In that case, it can be right of hub/bottom too.
						//we want top to be above except for (the T intersection case.)
						if(getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 3
								&& curRotation == 3) {
								//The exception where top can be right of bottom:
								//(the T intersection case.)
								
						} else {
							continue;
						}
						
					} else if(new_j < paperToDevelop[0].j
							&& (getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 1 ||
									getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 4 ||
									(getNumUsedNeighbourCellonPaper(indexCuboidonPaper, paperToDevelop[0]) == 2 
										&& paperUsed[paperToDevelop[0].i + 1][paperToDevelop[0].j]
										&& paperUsed[paperToDevelop[0].i - 1][paperToDevelop[0].j]))
							) {
						//If bottom has 1 or 4 neighbours, or 2 neighbours that are above and below, make top right left of bottom on paper (or directly above)
						
						//i.e.: Only go up and to the right in the 1 bottom and 1 top case.
						continue;
					}
				}
				//END special rules about where to put top in order to take advantage of symmetry
				
				
				
				//TODO: put in function A
				boolean cantAddCellBecauseOfOtherPaperNeighbours = false;
				
				SEARCH_FOR_BAD_SECOND_NEIGHBOURS:
				for(int i1=new_i-1; i1<=new_i+1; i1++) {
					for(int j1=new_j-1; j1<=new_j+1; j1++) {
						if((i1 == new_i && j1 != new_j)
								|| (i1 != new_i && j1 == new_j)) {
							
							if(paperToDevelop[i].i == i1 && paperToDevelop[i].j == j1) {
								continue;
							}
							
							//System.out.println("Paper neighbour:" + i1 + ", " + j1);
							
							if(paperUsed[i1][j1]) {
								//System.out.println("Connected to another paper");
								//TODO: make sure that it fits!
								
								int indexOtherCell = indexCuboidonPaper[i1][j1];
								int rotationOtherCell = cuboid.getRotationPaperRelativeToMap(indexOtherCell);

								if(CellIndexToOrderOfDev[regionIndex].containsKey(indexOtherCell)
										&& CellIndexToOrderOfDev[regionIndex].get(indexOtherCell) < CellIndexToOrderOfDev[regionIndex].get(indexToUse) ) {
									cantAddCellBecauseOfOtherPaperNeighbours = true;
									break SEARCH_FOR_BAD_SECOND_NEIGHBOURS;
								}
								
								//TODO: put in function
								int rotReq = -1;
								
								if(i1 -1 == new_i) {
									rotReq = 0;
								} else if(j1 +1 == new_j) {
									rotReq = 1;
									
								} else if(i1 +1 == new_i) {
									rotReq = 2;
									
								} else if(j1 -1 == new_j) {
									rotReq = 3;
									
								} else {
									System.out.println("Oops! rotation 217");
								}
								
								int neighbourIndexNeeded = (rotReq - rotationOtherCell + NUM_ROTATIONS) % NUM_ROTATIONS;

								//End TODO: put in function

								if(cuboid.getNeighbours(indexOtherCell)[neighbourIndexNeeded].getIndex() != indexNewCell) {
									cantAddCellBecauseOfOtherPaperNeighbours = true;
									break SEARCH_FOR_BAD_SECOND_NEIGHBOURS;
								}
								
								
								
							}
							
						}
					}
				}
				
				
				
				
				
				CoordWithRotationAndIndex neighboursOfNewCell[] = cuboid.getNeighbours(indexNewCell);
				
				
				
				//Let region specific variable default to using the given regions.
				//That might change if the new cell added, divides a region.
				boolean CellRegionsToHandleInRevOrderToUse[][] = CellRegionsToHandleInRevOrder;
				int minOrderedCellCouldUsePerRegionToUse[] = minOrderedCellCouldUsePerRegion;
				int minCellRotationOfMinCellToDevPerRegionToUse[] = minCellRotationOfMinCellToDevPerRegion;
				HashMap <Integer, Integer> CellIndexToOrderOfDevToUse[] = CellIndexToOrderOfDev;
				

				int prevNewMinOrderedCellCouldUse = minOrderedCellCouldUsePerRegionToUse[regionIndex];
				int prevMinCellRotationOfMinCellToDev = minCellRotationOfMinCellToDevPerRegionToUse[regionIndex];
				// End Let region specific variable default to using the given regions.
				
				//Trying to divide and conquer here:
				if( !cantAddCellBecauseOfOtherPaperNeighbours) {
					//areCellsSepartedCuboid(CuboidToFoldOn cuboid, int startIndex, int goalIndex)
					
					//flag is redundant, but expressive!
					boolean foundABlankNeighbour = false;
					int firstIndex = -1;

					//Add potentially new cell just for test:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
							
					//TODO: Don't forget to add cell to paper when the time comes...
					
					STOP_DIVIDING:
					for(int rotIndexToFill=0; rotIndexToFill<NUM_ROTATIONS; rotIndexToFill++) {
						
						
	
						int indexNeighbourOfNewCell = neighboursOfNewCell[rotIndexToFill].getIndex();
						
						if( ! cuboid.isCellIndexUsed(indexNeighbourOfNewCell)) {
							
							if( ! foundABlankNeighbour) {
								firstIndex = indexNeighbourOfNewCell;
								foundABlankNeighbour = true;
							} else {
								
								if(areCellsSepartedCuboid(cuboid, firstIndex, indexNeighbourOfNewCell)) {
									
									//START DIVIDING THE REGION:
									
									//Look for a 3-three way:
									
									boolean found3Way = false;
									int indexNeighbourThirdWay = -1;
									for(int thirdRot=rotIndexToFill+1; thirdRot<NUM_ROTATIONS; thirdRot++) {
										
										int curThirdNeighbour = neighboursOfNewCell[thirdRot].getIndex();
										
										
										if(! cuboid.isCellIndexUsed(curThirdNeighbour)
												&& areCellsSepartedCuboid(cuboid, curThirdNeighbour, firstIndex)
												&& areCellsSepartedCuboid(cuboid, curThirdNeighbour, indexNeighbourOfNewCell)) {
											found3Way = true;
											indexNeighbourThirdWay = curThirdNeighbour;
										}
									}
									//End look for 3-three (cuboid separated into 3 regions)

									int numNewWays = 1;
									if(found3Way) {
										numNewWays = 2;
									}
										
										
									CellRegionsToHandleInRevOrderToUse = new boolean[CellRegionsToHandleInRevOrder.length + numNewWays][cuboid.getNumCellsToFill()] ;
									
									//TODO: maybe I can be lazy and let this be a long array that doesn't need changing?
									
									//TODO: Reduce code duplication by combining 3way case with 2 way case (They aren't that different)
									// Nah...
									minOrderedCellCouldUsePerRegionToUse = new int[minOrderedCellCouldUsePerRegion.length + numNewWays];
									minCellRotationOfMinCellToDevPerRegionToUse = new int[minCellRotationOfMinCellToDevPerRegion.length + numNewWays];
									
									CellIndexToOrderOfDevToUse = new HashMap[CellRegionsToHandleInRevOrder.length + numNewWays];
									
									for(int k=0; k<CellRegionsToHandleInRevOrder.length - 1; k++) {
										CellRegionsToHandleInRevOrderToUse[k] = CellRegionsToHandleInRevOrder[k];
										minOrderedCellCouldUsePerRegionToUse[k] = minOrderedCellCouldUsePerRegion[k];
										minCellRotationOfMinCellToDevPerRegionToUse[k] = minCellRotationOfMinCellToDevPerRegion[k];
										CellIndexToOrderOfDevToUse[k] = CellIndexToOrderOfDev[k];
									}
									
									for(int k=0; k<numNewWays + 1; k++) {
											
										int indexToAdd = CellRegionsToHandleInRevOrder.length - 1 + k;
										if(k==0) {
											CellRegionsToHandleInRevOrderToUse[indexToAdd] = getCellsInUnfilledCuboidRegion(cuboid, firstIndex);
										} else if(k == 1) {
											CellRegionsToHandleInRevOrderToUse[indexToAdd] = getCellsInUnfilledCuboidRegion(cuboid, indexNeighbourOfNewCell);	
										} else if(k == 2) {
											CellRegionsToHandleInRevOrderToUse[indexToAdd] = getCellsInUnfilledCuboidRegion(cuboid, indexNeighbourThirdWay);
											
										} else {
											System.out.println("ERROR: k is too high. k= " + k);
											System.exit(1);
										}
										
										minOrderedCellCouldUsePerRegionToUse[indexToAdd] = CellIndexToOrderOfDev[regionIndex].get(indexToUse);
										minCellRotationOfMinCellToDevPerRegionToUse[indexToAdd] = j;
										
										CellIndexToOrderOfDevToUse[indexToAdd] = (HashMap <Integer, Integer>)CellIndexToOrderOfDev[regionIndex].clone();
										
										//Quick check if each region has at least 1 solution:
										//TODO: put quick check in function
										//Mini setup
										//System.out.println("Check single solution:");
										
										//Don't set it! (It's supposed to be set for now)
										//cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
										
										paperUsed[new_i][new_j] = true;
										indexCuboidonPaper[new_i][new_j] = indexNewCell;
										paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

										
										//TODO: explain this better
										CellIndexToOrderOfDevToUse[indexToAdd].put(indexNewCell, numCellsUsedDepth);
										CellRegionsToHandleInRevOrderToUse[indexToAdd][indexNewCell] = false;
										minOrderedCellCouldUsePerRegionToUse[indexToAdd] = CellIndexToOrderOfDevToUse[regionIndex].get(indexToUse);
										minCellRotationOfMinCellToDevPerRegionToUse[indexToAdd] = j;
										
										//End TODO

										numCellsUsedDepth += 1;
										
										if(depthFirstAlgoWillFindAsolutionInRegionIndex(paperToDevelop, indexCuboidonPaper,
												paperUsed, cuboid, numCellsUsedDepth,
												CellIndexToOrderOfDevToUse, CellRegionsToHandleInRevOrderToUse,
												minOrderedCellCouldUsePerRegionToUse, minCellRotationOfMinCellToDevPerRegionToUse, indexToAdd)
											== false) {
											
											
											//System.out.println("Region impossible!");
											
											cantAddCellBecauseOfOtherPaperNeighbours = true;
										}
										
										numCellsUsedDepth -= 1;
										
										
										//Mini tear down
										CellRegionsToHandleInRevOrderToUse[indexToAdd][indexNewCell] = true;
										CellIndexToOrderOfDevToUse[indexToAdd].remove(indexNewCell);
										minOrderedCellCouldUsePerRegionToUse[indexToAdd] = prevNewMinOrderedCellCouldUse;
										minCellRotationOfMinCellToDevPerRegionToUse[indexToAdd] = prevMinCellRotationOfMinCellToDev;
										

										paperUsed[new_i][new_j] = false;
										indexCuboidonPaper[new_i][new_j] = -1;
										paperToDevelop[numCellsUsedDepth] = null;
										
										//Don't remove it (It's supposed to be set for now)
										//cuboid.removeCell(indexNewCell);
										
										//End mini-tear down
										//END TODO:  put quick check in functiop
										
										if(cantAddCellBecauseOfOtherPaperNeighbours) {
											//System.out.println("quick cut off");
											break STOP_DIVIDING;
										}
										
									}
									
									regionIndex = CellRegionsToHandleInRevOrderToUse.length - 1;
									
									//Later:
									//TODO: check if each region has 1 solution
									//TODO: check if region is the same as a hole in the net to go faster (Also useful for the 3Cuboid 1 net check)
									
									
									/*
									System.out.println();
									System.out.println();
									System.out.println();
									
									System.out.println(DataModelViews.getFlatNumberingView(cuboid.getNumCellsToFill()/4, 1, 1));
									System.out.println("TODO:");
									if(found3Way){
										System.out.println("Found cell that will separate the cuboid in three ways!");
									} else {
										System.out.println("Found cell that will separate the cuboid!");
									}
									System.out.println("Index to used as bridge: " + indexToUse);
									System.out.println("Trying to add index: " + indexNewCell);
									if(found3Way) {
										System.out.println("Neighbours that are separated: " + firstIndex + ", " + indexNeighbourOfNewCell + ", and " + indexNeighbourThirdWay);
									} else {
										System.out.println("Neighbours that are separated: " + firstIndex + " and " + indexNeighbourOfNewCell);
									}
									Utils.printFold(paperUsed);
									Utils.printFoldWithIndex(indexCuboidonPaper);
									
									if(found3Way) {
										System.out.println("Stop at 3-way?");
									}
									 */

									//END DIVIDING THE REGION
									
									break STOP_DIVIDING;
								}
							}
						}
					}
					
					//Remove potential new cell once test is done:
					cuboid.removeCell(indexNewCell);
					//TODO: Don't forget to remove cell from paper when the time comes...
					
				}
				
				
				
				
				if( ! cantAddCellBecauseOfOtherPaperNeighbours) {
					//TODO: go next level!
					

					//Setup:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					
					//TODO: explain this better
					for(int r=CellRegionsToHandleInRevOrder.length - 1; r<CellRegionsToHandleInRevOrderToUse.length; r++) {

						CellIndexToOrderOfDevToUse[r].put(indexNewCell, numCellsUsedDepth);
						CellRegionsToHandleInRevOrderToUse[r][indexNewCell] = false;
						minOrderedCellCouldUsePerRegionToUse[r] = CellIndexToOrderOfDevToUse[regionIndex].get(indexToUse);
						minCellRotationOfMinCellToDevPerRegionToUse[r] = j;
						
					}
					//End TODO

					numCellsUsedDepth += 1;
					
					//End setup
					
					
					long newLimitDupSolutions = limitDupSolutions;
					if(limitDupSolutions >= 0) {
						newLimitDupSolutions -= retDuplicateSolutions;
					}
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, CellIndexToOrderOfDevToUse, CellRegionsToHandleInRevOrderToUse, minOrderedCellCouldUsePerRegionToUse, minCellRotationOfMinCellToDevPerRegionToUse, newLimitDupSolutions);

	

					if(numCellsUsedDepth < CellIndexToOrderOfDev[0].size()) {
						System.out.println("WHAT???");
						System.exit(1);
					}
					
					//Tear down
					numCellsUsedDepth -= 1;

					//New Region tear down

					for(int r=CellRegionsToHandleInRevOrder.length - 1; r<CellRegionsToHandleInRevOrderToUse.length; r++) {
						CellRegionsToHandleInRevOrderToUse[r][indexNewCell] = true;
						CellIndexToOrderOfDevToUse[r].remove(indexNewCell);
						minOrderedCellCouldUsePerRegionToUse[r] = prevNewMinOrderedCellCouldUse;
						minCellRotationOfMinCellToDevPerRegionToUse[r] = prevMinCellRotationOfMinCellToDev;
					}

					paperUsed[new_i][new_j] = false;
					indexCuboidonPaper[new_i][new_j] = -1;
					paperToDevelop[numCellsUsedDepth] = null;
					
					cuboid.removeCell(indexNewCell);
					//End tear down


					if(limitDupSolutions >= 0 && retDuplicateSolutions > limitDupSolutions) {
						//Handling option to only find 1 or 2 solutions:
						//This has to be done after tear-down because these objects are soft-copied...
						return retDuplicateSolutions;
					}
					
					
					regionIndex = CellRegionsToHandleInRevOrder.length - 1;
					
				//TODO: exit next level!
				} else {
					//System.out.println("can't add cell because of other paper neighbours");
				}
				
				
			}
		}

		
		return retDuplicateSolutions;
	}
	
	
	//TODO: make an function that checks if there's only 1 solution
	public static boolean depthFirstAlgoWillFindAsolutionInRegionIndex(Coord2D paperToDevelop[], int indexCuboidonPaper[][],
			boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			HashMap <Integer, Integer> CellIndexToOrderOfDev[], boolean CellRegionsToHandleInRevOrder[][],
			int minOrderedCellCouldUsePerRegion[], int minCellRotationOfMinCellToDevPerRegion[], int regionIndex) {
		
		//doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth, int debugLastIndex,
		
		boolean CellRegionsToHandleInRevOrderToUse[][] = new boolean[1][CellRegionsToHandleInRevOrder.length];
		int minOrderedCellCouldUsePerRegionToUse[] = new int[1];
		int minCellRotationOfMinCellToDevPerRegionToUse[] = new int[1];
		HashMap <Integer, Integer> CellIndexToOrderOfDevToUse[] = new HashMap[1];
		
		CellRegionsToHandleInRevOrderToUse[0] = CellRegionsToHandleInRevOrder[regionIndex];
		minOrderedCellCouldUsePerRegionToUse[0] = minOrderedCellCouldUsePerRegion[regionIndex];
		minCellRotationOfMinCellToDevPerRegionToUse[0] = minCellRotationOfMinCellToDevPerRegion[regionIndex];
		CellIndexToOrderOfDevToUse[0] = CellIndexToOrderOfDev[regionIndex];
		
		
		if(doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, CellIndexToOrderOfDevToUse, CellRegionsToHandleInRevOrderToUse, minOrderedCellCouldUsePerRegionToUse, minCellRotationOfMinCellToDevPerRegionToUse, 0L)
				> 0L) {
			return true;
		} else {
		
			return false;
		}
	}
 
			
	
	
	public static int getNumUsedNeighbourCellonPaper(
			int indexCuboidonPaper[][],
			Coord2D cellLocation) {

		
		int ret = 0;
		
		if(indexCuboidonPaper[cellLocation.i-1][cellLocation.j] >= 0) {
			ret++;
		}
		if(indexCuboidonPaper[cellLocation.i][cellLocation.j+1] >= 0) {
			ret++;
		}
		if(indexCuboidonPaper[cellLocation.i+1][cellLocation.j] >= 0) {
			ret++;
		}
		if(indexCuboidonPaper[cellLocation.i][cellLocation.j-1] >= 0) {
			ret++;
		}
		
		
		return ret;
	}
	

	//Don't use yet!
	public static int getNumUsedNeighbourCellonCuboidDoNotUse(
			CuboidToFoldOn cuboid, int indexCell) {
		int ret = 0;
		
		for(int r=0; r<NUM_ROTATIONS; r++) {
			if(cuboid.isCellIndexUsed(cuboid.getNeighbours(indexCell)[r].getIndex())) {
				ret++;
			}
		}
		
		return ret;
	}
	
	public static boolean cellHasNeighbourWithIndexUnderSomeMinIndex(
			CuboidToFoldOn cuboid, int index, HashMap <Integer, Integer> CellIndexToOrderOfDev, int minOrderedCellCouldUse) {
		
		for(int r=0; r<NUM_ROTATIONS; r++) {
			if(CellIndexToOrderOfDev.containsKey(cuboid.getNeighbours(index)[r].getIndex())
					&& CellIndexToOrderOfDev.get(cuboid.getNeighbours(index)[r].getIndex()) < minOrderedCellCouldUse) {
				return true;
			}
		}
		
		return false;
		
	}
	
	
	//Worry about optimizing this later...
	//Idea 0:
	// breadth-first search:
	
	//Don't do this: maybe combine with a region size function
	// and return more info?
	//NAH!
	public static boolean areCellsSepartedCuboid(CuboidToFoldOn cuboid, int startIndex, int goalIndex) {
		
		if(startIndex == goalIndex) {
			System.err.println("Warning: We're asking if the same cell is seperated from itself. Are you high?");
			return false;
		}
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		boolean explored[] = new boolean[cuboid.getNumCellsToFill()];
		
		explored[startIndex] = true;
		
		queue.add(startIndex);
		
		while( ! queue.isEmpty() ) {
			int v = queue.removeFirst();
			
			if(v == goalIndex) {
				return false;
			}
			
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				
				int neighbour = cuboid.getNeighbours(v)[j].getIndex();
				
				if(!explored[neighbour] && ! cuboid.isCellIndexUsed(neighbour)) {
					explored[neighbour] = true;
					queue.add(neighbour);
				}
			}
		}
		
		return true;
	}

	public static boolean[] getCellsInUnfilledCuboidRegion(CuboidToFoldOn cuboid, int startIndex) {
		
		LinkedList<Integer> queue = new LinkedList<Integer>();
		
		boolean explored[] = new boolean[cuboid.getNumCellsToFill()];
		
		explored[startIndex] = true;
		
		queue.add(startIndex);
		
		while( ! queue.isEmpty() ) {
			int v = queue.removeFirst();
			
			
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				
				int neighbour = cuboid.getNeighbours(v)[j].getIndex();
				
				if(!explored[neighbour] && ! cuboid.isCellIndexUsed(neighbour)) {
					explored[neighbour] = true;
					queue.add(neighbour);
				}
			}
		}
		
		return explored;
	}
	
	/*https://www.sciencedirect.com/science/article/pii/S0925772117300160
	 * 
	 *  "From the necessary condition, the smallest possible surface area that can fold into two boxes is 22,
	 *   and the smallest possible surface area for three different boxes is 46.
	 *   (...) However, the area 46 is too huge to search. "
	 *  
	 *  Challenge accepted!
	 */

	public static void main(String args[]) {
		System.out.println("Fold Resolver divide and Conquer:");
		solveFoldsForSingleCuboid(5, 1, 1);

		
		
		System.out.println(System.currentTimeMillis());
		//Mission add to OEIS:
		//So far, the pattern is:
		//11, 349, ??
		
		//2nd mission:
		// get to 11.
		
		
		/* 5x1x1 on December 8th:
		 * 47800000
			47900000
			48000000
			48100000
			48200000 (duplicated solution)
			Final number of unique solutions: 3014430
		 */
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		
	}
	
	public static int pint(String s) {
		if (IsNumber.isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			System.out.println("Error: (" + s + " is not a number");
			return -1;
		}
	}
}
