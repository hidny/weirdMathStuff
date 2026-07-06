package test_pseudo_code;

import java.util.HashMap;

public class TryBFSIterator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		for(int n=1; n<20; n++) {
			
			numSolutions = 0;
			netSearchStart(n);
			
			if(numSolutions % n != 0 ) {
				System.out.println("Oops! I didn't get the right number!");
			}
			System.out.println("Number of solution for n = " + n + ": " + (numSolutions / n));
			
		}
	}

	public static Tile map[][] = null;

	public static Tile insert_initial_tile() {
	
		int zeroCoordI = map.length / 2;
		int zeroCoordJ = map[0].length / 2;
		
		Tile tile = new Tile();
		tile.i = zeroCoordI;
		tile.j = zeroCoordJ;
		tile.labelNumber = 0;
		
		map[zeroCoordI][zeroCoordJ] = tile;
		
		return tile;
	}
	
	
	public static void netSearchStart(int max_size) {
		

		/*net_search_start(max_size):
		    Define Mapping <Int to Int> tile_to_ordering
		    cur_size = 0, cur_order_index = 0, cur_rot_index = 0
		    tile_to_ordering.put(cur_order_index, cur_size)
		    insert_initial_tile(cur_order_index)
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
	
	public static Tile getNeiOnFlatPaper(Tile curTile, int rot) {
		
		Tile ret = new Tile();
		ret.i = curTile.i;
		ret.j = curTile.j;
		
		if(rot == 0) {
			ret.i -= 1;
		} else if(rot == 1) {
			ret.j += 1;
			
		} else if(rot == 2) {
			ret.i += 1;
		} else if(rot == 3) {

			ret.j -= 1;
			
		} else {
			System.out.println("Ooops!");
			System.exit(1);
			return null;
		}
		
		if(map[ret.i][ret.j] != null) {
			return map[ret.i][ret.j];
		} else {
			return ret;
		}
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
	
	public static int numSolutions = 0;
	
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
			
			//cur = ordering_to_tile(i)
			Tile curTile = orderingToTile.get(i);
			
			//foreach rot of cur:
			for(int rot = 0; rot<4; rot++) {
				
				//if i == cur_order_index and cur_rot_index > rot:
				if(i == cur_order_index && cur_rot_index > rot) {
	                //# Skip iteration if rot before BFS queue
	                //continue
					continue;
				}
				
				Tile nei = getNeiOnFlatPaper(curTile, rot);
				
				/*
				# If nei has a neighbour that was inserted
	            # before cur, it was already explored.
	            if neighbouring_location_non_empty() or
	               cur_tile_being_considered_was_already_explored
	                (cur, nei, tile_to_ordering, i, rot):
	                continue
				 */
				
				if(map[nei.i][nei.j] != null
						|| cur_tile_being_considered_was_already_explored
					    (curTile, nei, tileToOrdering, i, rot)) {
					continue;
				}
				
				
				
				/*

	            # Try inserting neighbour:
	            insert_tile(cur, nei, rot)
	            tile_to_ordering.put(nei, cur_size)
	            cur_size = cur_size + 1
	            */

				insertTile(curTile, nei, rot);
				tileToOrdering.put(nei, cur_size);
				orderingToTile.put(cur_size, nei);
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
	            remove_tile(nei)
	            tile_to_ordering.remove(nei)
	            */
				cur_size = cur_size - 1;
	            removeTile(nei);
	            tileToOrdering.remove(nei);
				orderingToTile.remove(cur_size);
	            
	            
				
			}
		}
	}
	
	
}
