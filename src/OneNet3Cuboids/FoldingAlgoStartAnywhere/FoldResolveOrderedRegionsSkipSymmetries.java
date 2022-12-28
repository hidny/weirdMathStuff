package OneNet3Cuboids.FoldingAlgoStartAnywhere;

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
import OneNet3Cuboids.Cuboid.SymmetryResolver.SymmetryResolver;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.Region.Region;
import number.IsNumber;

public class FoldResolveOrderedRegionsSkipSymmetries {

	
	public static final int NUM_ROTATIONS = 4;
	public static final int NUM_NEIGHBOURS = NUM_ROTATIONS;
	
	
	public static void solveFoldsForSingleCuboid(int a, int b, int c) {
		solveFoldsForSingleCuboid(a, b, c, true);
	}

	public static void solveFoldsForSingleCuboid(int a, int b, int c, boolean skipSymmetries) {
		
		
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
		
		Region regionsToHandleRevOrder[] = new Region[1];
		regionsToHandleRevOrder[0] = new Region(cuboid);
		
		doDepthFirstSearch(paperToDevelop, indexCuboidOnPaper, paperUsed, cuboid, numCellsUsedDepth, regionsToHandleRevOrder, -1L, skipSymmetries);
		
		System.out.println("Final number of unique solutions: " + numUniqueFound);
	}
	
	private static int numFound = 0;
	private static int numUniqueFound = 0;
	
	
	public static long doDepthFirstSearch(Coord2D paperToDevelop[], int indexCuboidonPaper[][], boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], long limitDupSolutions, boolean skipSymmetries) {

		//Debug
		if(numCellsUsedDepth < regions[0].getCellIndexToOrderOfDev().size()) {
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
			if(numFound % 10000000L == 0) {
				System.out.println("Print possible duplicate solution:");
				Utils.printFold(paperUsed);
				Utils.printFoldWithIndex(indexCuboidonPaper);
				
			}
			
			if(BasicUniqueCheckImproved.isUnique(paperUsed)) {
				numUniqueFound++;

				//Utils.printFold(paperUsed);
				//Utils.printFoldWithIndex(indexCuboidonPaper);
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
		int regionIndex = regions.length - 1;
		
		
		//Check if last region is empty and make adjustments if so:
		if(regionIndex > 0 || limitDupSolutions >= 0) {

			
			if(regions[regionIndex].getNumCellsInRegion() == 0) {
				
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
				
				Region oneLessRegion[] = new Region[regions.length - 1];
				
				for(int i=0; i<oneLessRegion.length; i++) {
					oneLessRegion[i] = regions[i];
				}
				
				regions = oneLessRegion;
				
				
				if(regions[regionIndex].getNumCellsInRegion() == 0) {
					System.out.println("ERROR: 2nd last region is empty. This should not happen!");

					Utils.printFold(paperUsed);
					Utils.printFoldWithIndex(indexCuboidonPaper);
					System.exit(1);
				}
				
			}
		}
		//End check if last region is empty and make adjustments

		//DEPTH-FIRST START:
		
		for(int i=regions[regionIndex].getMinOrderedCellCouldUsePerRegion(); i<paperToDevelop.length && paperToDevelop[i] != null; i++) {
			
			
			//System.out.println("Coord i,j : " + coord_i + ", " + coord_j);
			
			int indexToUse = indexCuboidonPaper[paperToDevelop[i].i][paperToDevelop[i].j];
			if(indexToUse < 0) {
				System.out.println("Doh!");
				System.exit(1);
			}
			
			if(regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse)
					&& regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) < regions[regionIndex].getMinOrderedCellCouldUsePerRegion()) {
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
				} else if(regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexToUse)
						&& regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) == regions[regionIndex].getMinOrderedCellCouldUsePerRegion() 
						&& j <  regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion()) {
					continue;

				} else if(regions[regionIndex].getCellRegionsToHandleInRevOrder()[neighbours[j].getIndex()] == false) {
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
				
				if(SymmetryResolver.skipSearchBecauseOfASymmetryArg
						(cuboid, paperToDevelop, i, indexCuboidonPaper, rotationToAddCellOn,
							curRotation, paperUsed, indexToUse, indexNewCell)
					&& skipSymmetries == true) {
					continue;
				}
				
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

								if(regions[regionIndex].getCellIndexToOrderOfDev().containsKey(indexOtherCell)
										&& regions[regionIndex].getCellIndexToOrderOfDev().get(indexOtherCell) < regions[regionIndex].getCellIndexToOrderOfDev().get(indexToUse) ) {
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
				
				Region regionsBeforePotentailRegionSplit[] = regions;
				int prevNewMinOrderedCellCouldUse = regions[regionIndex].getMinOrderedCellCouldUsePerRegion();
				int prevMinCellRotationOfMinCellToDev = regions[regionIndex].getMinCellRotationOfMinCellToDevPerRegion();
				
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
					
					//TODO: rename to TRY_TO_DIVDE
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
										
									Region regionsSplit[] = new Region[regions.length + numNewWays];
									
									for(int k=0; k<regions.length - 1; k++) {
										regionsSplit[k] = regions[k];
									}
									
									for(int k=0; k<numNewWays + 1; k++) {
											
										int indexToAdd = regions.length - 1 + k;
										
										if(k==0) {
											regionsSplit[indexToAdd] = new Region(cuboid, regions[regionIndex], firstIndex, j);
										} else if(k == 1) {
											regionsSplit[indexToAdd] = new Region(cuboid, regions[regionIndex], indexNeighbourOfNewCell, j);	
										} else if(k == 2) {
											regionsSplit[indexToAdd] = new Region(cuboid, regions[regionIndex], indexNeighbourThirdWay, j);
											
										} else {
											System.out.println("ERROR: k is too high. k= " + k);
											System.exit(1);
										}
										
										//TODO: END COPY/PASTE FOR CREATION HERE
										
										//Quick check if each region has at least 1 solution:
										//TODO: put quick check in function
										//Mini setup
										//System.out.println("Check single solution:");
										
										//Don't set it! (It's supposed to be set for now)
										//cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
										
										paperUsed[new_i][new_j] = true;
										indexCuboidonPaper[new_i][new_j] = indexNewCell;
										paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

										
										regionsSplit[indexToAdd].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);
										
										//End TODO

										numCellsUsedDepth += 1;
										
										if(depthFirstAlgoWillFindAsolutionInRegionIndex(paperToDevelop, indexCuboidonPaper,
												paperUsed, cuboid, numCellsUsedDepth,
												regionsSplit, indexToAdd, skipSymmetries)
											== false) {
											
											
											//System.out.println("Region impossible!");
											
											cantAddCellBecauseOfOtherPaperNeighbours = true;
										}
										
										numCellsUsedDepth -= 1;
										
										
										//TODO: this is awkward: I'm tearing it down so I could build it up again later
										//Mini tear down
										regionsSplit[indexToAdd].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);


										paperUsed[new_i][new_j] = false;
										indexCuboidonPaper[new_i][new_j] = -1;
										paperToDevelop[numCellsUsedDepth] = null;
										
										//Don't remove this: (It's supposed to be set for now)
										//cuboid.removeCell(indexNewCell);
										
										//End mini-tear down
										//END TODO:  put quick check in function
										
										if(cantAddCellBecauseOfOtherPaperNeighbours) {
											//System.out.println("quick cut off");
											break STOP_DIVIDING;
										}
										
									}
									
									regionIndex = regions.length - 1;
									
									//Later:
									//TODO: check if region is the same as a hole in the net to go faster (Also useful for the 3Cuboid 1 net check)

									//reordering regions by whether or not they have a single solution and then by size:
									//TODO: afterwards, try incorporating algo that checks if there's less than or equal to N solutions? Probably bad...
									
									regions = regionsSplit;

									boolean regionHasOneSolution[] = new boolean[regions.length];
									for(int i2=regions.length - 1 - numNewWays; i2< regions.length; i2++) {
										
										paperUsed[new_i][new_j] = true;
										indexCuboidonPaper[new_i][new_j] = indexNewCell;
										paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

										
										regionsSplit[i2].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);
										
										//End TODO

										numCellsUsedDepth += 1;
										
										regionHasOneSolution[i2] = depthFirstAlgoWillFindOnly1solutionInRegionIndex(paperToDevelop, indexCuboidonPaper,
												paperUsed, cuboid, numCellsUsedDepth,
												regionsSplit, i2, skipSymmetries);

										numCellsUsedDepth -= 1;
										
										
										//TODO: this is awkward: I'm tearing it down so I could build it up again later
										//Mini tear down
										regionsSplit[i2].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);


										paperUsed[new_i][new_j] = false;
										indexCuboidonPaper[new_i][new_j] = -1;
										paperToDevelop[numCellsUsedDepth] = null;
										
										
										
									}
									
									//Sort new regions by whether it has only 1 solution, and then by size:
									for(int i2=regions.length - 1 - numNewWays; i2< regions.length; i2++) {
										
										int curBestIndex = i2;
										
										for(int j2=i2+1; j2<regions.length; j2++) {
											
											if(regionHasOneSolution[i2] && ! regionHasOneSolution[j2]) {
												curBestIndex = j2;

											} else if(regions[i2].getNumCellsInRegion() < regions[j2].getNumCellsInRegion()) {
												curBestIndex = j2;
											}
										}
										
										Region tmp = regions[i2];
										regions[i2] = regions[curBestIndex];
										regions[curBestIndex] = tmp;
									}
									//END sort
									
									 
									
									//End sort regions by size and if the region only has 1 solution
									
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
					
					//Setup:
					cuboid.setCell(indexNewCell, rotationNeighbourPaperRelativeToMap);
					
					paperUsed[new_i][new_j] = true;
					indexCuboidonPaper[new_i][new_j] = indexNewCell;
					paperToDevelop[numCellsUsedDepth] = new Coord2D(new_i, new_j);

					
					//Add cell to new region(s):
					for(int r=regionsBeforePotentailRegionSplit.length - 1; r<regions.length; r++) {
						regions[r].addCellToRegion(indexNewCell, numCellsUsedDepth, indexToUse, j);						
					}

					numCellsUsedDepth += 1;
					
					//End setup

					long newLimitDupSolutions = limitDupSolutions;
					if(limitDupSolutions >= 0) {
						newLimitDupSolutions -= retDuplicateSolutions;
					}
					
					retDuplicateSolutions += doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regions, newLimitDupSolutions, skipSymmetries);

	

					if(numCellsUsedDepth < regions[0].getCellIndexToOrderOfDev().size()) {
						System.out.println("WHAT???");
						System.exit(1);
					}
					
					//Tear down
					numCellsUsedDepth -= 1;

					regions = regionsBeforePotentailRegionSplit;
					
					//Remove cell from last region(s):
					regions[regions.length - 1].removeCellFromRegion(indexNewCell, numCellsUsedDepth, prevNewMinOrderedCellCouldUse, prevMinCellRotationOfMinCellToDev);
	

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

					regionIndex = regions.length - 1;
					
				} else {
					//System.out.println("can't add cell because of other paper neighbours or it creates an impossible region");
				}
				
				
			}
		}

		
		return retDuplicateSolutions;
	}
	
	
	public static boolean depthFirstAlgoWillFindAsolutionInRegionIndex(Coord2D paperToDevelop[], int indexCuboidonPaper[][],
			boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int regionIndex, boolean skipSymmetries) {
		
		
		Region regionArgToUse[] = new Region[1];
		regionArgToUse[0] = regions[regionIndex];
		
		if(doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regionArgToUse, 0L, skipSymmetries)
				> 0L) {
			return true;
		} else {
		
			return false;
		}
	}
 
	public static boolean depthFirstAlgoWillFindOnly1solutionInRegionIndex(Coord2D paperToDevelop[], int indexCuboidonPaper[][],
			boolean paperUsed[][], CuboidToFoldOn cuboid, int numCellsUsedDepth,
			Region regions[], int regionIndex, boolean skipSymmetries) {
		
		
		Region regionArgToUse[] = new Region[1];
		regionArgToUse[0] = regions[regionIndex];
		
		if(doDepthFirstSearch(paperToDevelop, indexCuboidonPaper, paperUsed, cuboid, numCellsUsedDepth, regionArgToUse, 1L, skipSymmetries)
				== 1L) {
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

	/*https://www.sciencedirect.com/science/article/pii/S0925772117300160
	 * 
	 *  "From the necessary condition, the smallest possible surface area that can fold into two boxes is 22,
	 *   and the smallest possible surface area for three different boxes is 46.
	 *   (...) However, the area 46 is too huge to search. "
	 *  
	 *  Challenge accepted!
	 */

	public static void main(String args[]) {
		System.out.println("Fold Resolver Ordered Regions start anywhere:");
		solveFoldsForSingleCuboid(2, 2, 2);

		//Best 5,1,1: 3 minute 45 seconds (3014430 solutions)
		
		
		System.out.println(System.currentTimeMillis());
		
		
		/* 5x1x1 on December 8th:
			Final number of unique solutions: 3014430
		//I hope it's right...
		//It took 6.5 hours.
		// Try to get it under 1 hour.
		 */
		
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
