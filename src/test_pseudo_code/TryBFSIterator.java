package test_pseudo_code;

import java.util.HashMap;

// Turning the pseudocode in the paper into acutal working code
// just to make sure I didn't oversimplify and mess it up.

//The code itself isn't that efficient and counts the number of fixed polyominos in a 2D plane.
//I didn't bother stopping it from overcounting by a factor of n, because that's not the point.
// See Redelmeier's paper for how to not overcount:
/*
 * @article{Redelmeier-Counting-Polyominoes,
title = {Counting polyominoes: Yet another attack},
journal = {Discrete Mathematics},
volume = {36},
number = {3},
pages = {191-203},
year = {1981},
issn = {0012-365X},
doi = {https://doi.org/10.1016/S0012-365X(81)80015-5},
url = {https://www.sciencedirect.com/science/article/pii/S0012365X81800155},
author = {D. Hugh Redelmeier},
abstract = {A polyomino is a connected collection of squares on an unbounded chessboard. There is no known formula yielding the number of distinct polyominoes of a given number of squares. A polyomino enumeration method, faster than any previous, is presented. This method includes the calculation of the number of symmetric polyominoes. All polyominoes containing up to 24 squares have been enumerated (using ten months of computer time). Previously, only polyominoes up to size 18 were enumerated.}

 */

public class TryBFSIterator {

	public static void main(String[] args) {

		for(int n=1; n<20; n++) {
			
			numSolutions = 0;
			netSearchStart(n);
			
			//TODO: Acknowledge that it gives: n*#solutions
			
			if(numSolutions % n != 0 ) {
				System.out.println("Oops! I didn't get the right number!");
			}
			System.out.println("Number of solution for n = " + n + ": " + (numSolutions / n));
			
		}
	}

	public static Tile memMap[][] = null;
	
	public static Tile map[][] = null;

	public static Tile insert_initial_tile() {
	
		memMap = new Tile[map.length][map[0].length];
		for(int i=0; i<memMap.length; i++) {
			for(int j=0; j<memMap[0].length; j++) {
				memMap[i][j] = new Tile();
				memMap[i][j].i = i;
				memMap[i][j].j = j;
				memMap[i][j].labelNumber = i * memMap[0].length + j;
				
			}
		}
		
		int zeroCoordI = map.length / 2;
		int zeroCoordJ = map[0].length / 2;
		
		
		map[zeroCoordI][zeroCoordJ] = memMap[zeroCoordI][zeroCoordJ];
		
		return map[zeroCoordI][zeroCoordJ];
	}
	
	
	
	public static Tile getNeiOnFlatPaper(Tile curTile, int rot) {
		
		
		int iNei = curTile.i;
		int jNei = curTile.j;
		
		if(rot == 0) {
			iNei -= 1;
		} else if(rot == 1) {
			jNei += 1;
			
		} else if(rot == 2) {
			iNei += 1;
		} else if(rot == 3) {

			jNei -= 1;
			
		} else {
			System.out.println("Ooops!");
			System.exit(1);
			return null;
		}
		
		return memMap[iNei][jNei];
	}
	
	
	public static boolean cur_tile_being_considered_was_already_explored
    (Tile cur, Tile nei, HashMap<Tile, Integer>  tileToOrdering, int cur_order_index, int rot) {
		
		for(int rot2 = 0; rot2<4; rot2++) {
			
			Tile neiToNeigh = getNeiOnFlatPaper(nei, rot2);
			
			if(map[neiToNeigh.i][neiToNeigh.j] == null) {
				continue;
			} else if(neiToNeigh == cur) {
				continue;
			} else {
				
				if(tileToOrdering.get(neiToNeigh) < tileToOrdering.get(cur)) {
					return true;
				} else {
					continue;
				}
			}
		}
		
		return false;
	}
	

	public static void insertTile(Tile cur, Tile nei, int rot) {
		
		if(map[nei.i][nei.j] != null) {
			System.out.println("Oops! Assumption broken! (insert Tile)");
			System.exit(1);
		}
		map[nei.i][nei.j] = nei;
	}
	public static void removeTile(Tile nei) {
		
		if(map[nei.i][nei.j] == null) {
			System.out.println("Oops! Assumption broken! (remove Tile)");
			System.exit(1);
		}
		map[nei.i][nei.j] = null;
	}
	
	//net_search(Mapping <Int to Int> tile_to_ordering,
	//    max_size, cur_size, cur_order_index, cur_rot_index):
	
	public static long numSolutions = 0;
	

	public static void netSearchStart(int max_size) {
		

		/*net_search_start(max_size):
    Define Mapping <Int to Int> tile_to_ordering
    cur_size = 0, cur_order_index = 0, cur_rot_index = 0
    tile_to_ordering.put(cur_order_index, cur_size)
    insert_initial_tile()
    cur_size = cur_size + 1
    
    net_search(tile_to_ordering, max_size, cur_size,
    cur_order_index, cur_rot_index)
		    
		   */
		HashMap<Tile, Integer>  tileToOrdering = new HashMap<Tile, Integer>();
		HashMap<Integer, Tile>  orderingToTile = new HashMap<Integer, Tile>();
		int cur_size = 0;
		int cur_order_index = 0;
		int cur_rot_index = 0;
		
		map = new Tile[3 * max_size][3 * max_size];
		
		Tile init = insert_initial_tile();
		
		orderingToTile.put(cur_order_index, init);
		tileToOrdering.put(init, cur_order_index);
		
		cur_size+=1;
		
		net_search(tileToOrdering, orderingToTile, max_size, cur_size, cur_order_index, cur_rot_index);
		
	}
	
	/*
	 * net_search(Mapping <Int to Int> tile_to_ordering,
    max_size, cur_size, cur_order_index, cur_rot_index):
	 */
	public static void net_search(HashMap<Tile, Integer>  tileToOrdering,
			HashMap<Integer, Tile>  orderingToTile,
			int max_size, int cur_size, int cur_order_index, int cur_rot_index) {
		
		/*
	    if max_size == cur_size:
	        process_solution()
	        return
		 */
		if(max_size == cur_size) {
			numSolutions++;
			return;
		}
		
		//for i = cur_order_index to cur_size:
		for(int i=cur_order_index; i<cur_size; i++) {
			
			// curTile = ordering_to_tile(i)
			Tile curTile = orderingToTile.get(i);
			
			//foreach rot from 0 to 3 inclusive:
			for(int rot = 0; rot<4; rot++) {
				
				//if i == cur_order_index and cur_rot_index > rot:
				if(i == cur_order_index && cur_rot_index > rot) {
	                //# Skip iteration if rot before BFS queue
	                //continue
					continue;
				}
				
				//neiTile = getNeiOnFlatPaper(curTile, rot)
				Tile neiTile = getNeiOnFlatPaper(curTile, rot);
				
				/*
				# If neiTile has a neighbour that was inserted
	            # before curTile, it was already explored.
	            if neighbouring_location_non_empty() or
	               cur_tile_being_considered_was_already_explored
	                (curTile, neiTile, tile_to_ordering, i, rot):
	                continue
				 */
				
				if(map[neiTile.i][neiTile.j] != null
						|| cur_tile_being_considered_was_already_explored
					    (curTile, neiTile, tileToOrdering, i, rot)) {
					continue;
				}
				
				
				
				/*

	            # Try inserting neighbour:
	            insert_tile(curTile, neiTile, rot)
	            tile_to_ordering.put(neiTile, cur_size)
	            cur_size = cur_size + 1
	            */

				insertTile(curTile, neiTile, rot);
				tileToOrdering.put(neiTile, cur_size);
				orderingToTile.put(cur_size, neiTile);
	            cur_size = cur_size + 1;
				
				/*
	            # Recursive call:
	             Net_search(tile_to_ordering, max_size,
            	cur_size, i, rot)
	            */
	            net_search(tileToOrdering,
	        			orderingToTile,
	        			max_size, cur_size, i, rot);
	            
	            /*
	            # Undo inserting neighbour:
	            cur_size = cur_size - 1
	            remove_tile(neiTile)
	            tile_to_ordering.remove(neiTile)
	            */
				cur_size = cur_size - 1;
	            removeTile(neiTile);
	            tileToOrdering.remove(neiTile);
				orderingToTile.remove(cur_size);
	            
	            
				
			}
		}
	}
	
	
}
