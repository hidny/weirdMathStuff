package solitaire;

import java.util.ArrayList;

public class InitStateFaster {

	//TODO: make sure all cards including the dragon are unique.
	public static String state[][] =
			new String[][] { 
		{ "1R", "2R", "3R", "4R", "5R", "6R", "7R", "8R" },
		{ "1B", "2B", "3B", "4B", "5B", "6B", "7B", "8B" },
		{ "1G", "2G", "3G", "4G", "5G", "6G", "7G", "8G" },
		{ "F1", "X1", "X2", "X3", "X4", "Y1", "Y2", "Y3" },
		{ "Y4", "Z1", "Z2", "Z3", "Z4", "9R", "9B", "9G" }};
	
	/*
	public static String state2[][] =
			new String[][] { 
		{ "9R", "2R", "3R", "4R", "5R", "6R", "7R", "8R" },
		{ "9B", "2B", "3B", "4B", "5B", "6B", "7B", "8B" },
		{ "9G", "2G", "3G", "4G", "5G", "6G", "7G", "8G" },
		{ "2R", "X1", "X2", "X3", "X4", "Y1", "Y2", "Y3" },
		{ "1R", "1G", "1B", "Z1", "FF", "9R", "9B", "9G" }};
	*/
	
	public static String state3[][] =
			new String[][] { 
		{ "5R", "5B", "5G", "FF", "X1", "X2", "X3", "X4" },
		{ "4R", "4B", "4G", "9R", "9B", "9G", "Y1", "Y2" },
		{ "3R", "3B", "3G", "8R", "8B", "8G", "Y3", "Y4" },
		{ "2R", "2B", "2G", "7R", "7B", "7G", "Z1", "Z2" },
		{ "1R", "1B", "1G", "6R", "6B", "6G", "Z4", "Z3" }};
	
	public static String state4[][] =
			new String[][] { 
		{ "5R", "5B", "5G", "FF", "X1", "X2", "X3", "X4" },
		{ "4R", "4B", "4G", "9R", "9B", "9G", "Y1", "Y2" },
		{ "3R", "3B", "3G", "8R", "8B", "8G", "Y3", "Y4" }};
	
	public static String testSimple[][] =
			new String[][] { 
		{ "1R", "", "", "", "", "", "", "" },
		{ "2R", "", "", "", "", "", "", "" },
		{ "3R", "", "", "", "", "", "", "" },
		{ "4R", "", "", "", "", "", "", "" },
		{ "5R", "", "", "", "", "", "", "" }};
	
	
	public static String testSimple2[][] =
			new String[][] { 
		{ "1R", "1B", "", "", "", "", "", "" },
		{ "2R", "2B", "", "", "", "", "", "" },
		{ "3R", "3B", "", "", "", "", "", "" },
		{ "4R", "4B", "", "", "", "", "", "" },
		{ "5R", "5B", "", "", "", "", "", "" }};
	
	public static String testSimple3[][] =
			new String[][] { 
		{ "1R", "1B", "1G", "", "", "", "", "" },
		{ "2R", "2B", "2G", "", "", "", "", "" },
		{ "3R", "3B", "3G", "", "", "", "", "" },
		{ "4R", "4B", "4G", "", "", "", "", "" },
		{ "5R", "5B", "5G", "", "", "", "", "" }};
	
	public static String testSimple4[][] =
			new String[][] { 
		{ "1G", "1B", "1R", "", "", "", "", "" },
		{ "2B", "2G", "2R", "", "", "", "", "" },
		{ "3G", "3R", "3B", "", "", "", "", "" },
		{ "4B", "4R", "4G", "", "", "", "", "" },
		{ "5R", "5B", "5G", "", "", "", "", "" }};
	
	
	
	
	//public static String stateSmall[][] =
	//		new String[][] { 
	//	{ "3R", "3B", "3G", "8R", "8B", "8G", "Y3", "Y4" }};
	
	
	public static void main(String args[]) {
		
		//Questions:
		// 1: is there a faster way?
		// 2: Why is it finding the heuristic of the same position sometimes? (At the beginning)
			//Answer: AstarAlgo assumes the heuristic function is fast so doesn't mind calling it several times...
			//Workaround is easy... but misses the point.
		
		
		test(testSimple, "simple");

		test(testSimple2, "simple2");
		test(testSimple3, "simple3");
		test(testSimple4, "simple4");
	}
	
	public static void testSimple() {
		System.out.println("test simple:");
		FasterState s = new FasterState(testSimple);

		ArrayList<AstarNode> path = AstarAlgo.astar(s, null);
		
		
		System.out.println("Done");
		
		printResult(path);
	}
	
	public static void test(String cards[][], String name) {
		System.out.println("test " + name + ":");
		FasterState s = new FasterState(cards);

		ArrayList<AstarNode> path = AstarAlgo.astar(s, null);
		
		
		System.out.println("Done");
		
		printResult(path);
	}
	
	public static void printResult(ArrayList<AstarNode> path) {
		System.out.println("---------------------");
		System.out.println("Path of solution:");
		
		if(path == null ||path.size() ==  0) {
			System.out.println("No solution!");
		} else {
			AstarNode start = path.get(0);
			
			System.out.println(start);
			for(int i=1; i<path.size(); i++) {
				System.out.println(((FasterState)path.get(i)).getLastMoveDescription());
				System.out.println(path.get(i));
				System.out.println();
			}
			
		}
		System.out.println("Number of moves in total: " + (path.size() - 1));
		System.out.println("---------------------");
		
		
	}
	
}
