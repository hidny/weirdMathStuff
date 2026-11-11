package poc_billiards;

import java.util.ArrayList;

public class polyominize {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ArrayList <Integer> topNotches = new ArrayList <Integer>();
		ArrayList <Integer> topTabs = new ArrayList <Integer>();
		ArrayList <Integer> bottomNotches = new ArrayList <Integer>();
		ArrayList <Integer> bottomTabs = new ArrayList <Integer>();
		
		
		bottomNotches.add(0); 
		bottomNotches.add(150); // 1/3 (1/3 + 2/3 = 1)
		bottomNotches.add(225); // 1/2
		bottomNotches.add(375); // 5/6 (5/6 + 1/6 = 1)
		
		bottomTabs.add(75);  // 1/6
		bottomTabs.add(300); // 2/3
		
		
		topNotches.add(12);  // 12 + 450 + 12 = 474
		topNotches.add(147); // 147 + 327 = 474
		topNotches.add(192); // 192 + 282 = 474
		topNotches.add(237); // 2* 237 = 474
		topNotches.add(372); // 372 + 102 = 474
		topNotches.add(417); // 417 + 57 = 474
		
		topTabs.add(57);
		topTabs.add(102);
		topTabs.add(282);
		topTabs.add(327);
		
		
		
		makeStrip(450, 6, topNotches, topTabs, bottomNotches, bottomTabs);
	}
	
	public static final int GAP = 5;

	public static boolean topTab[][] = new boolean[][] {
		{true, false, false},
		{true, false, false},
		{true, true, true},
		{true, true, true},
		{true, false, false},
		{true, false, false}
	};
	public static boolean bottomTab[][];
	
	public static boolean topNotch[][];
	public static boolean bottomNotch[][];
	static {
		bottomNotch = topTab;
		
		bottomTab = new boolean[6][3];
		
		for(int i=0; i<bottomTab.length; i++) {
			for(int j=0; j<bottomTab[0].length; j++) {
				
				bottomTab[i][j] = topTab[i][bottomTab[0].length - 1 - j];
			}
		}
		
		topNotch = bottomTab;
		
	}
	
	public static void makeStrip(int length, int width, 
			ArrayList<Integer> topNotches,
			ArrayList<Integer> topTabs,
			ArrayList<Integer> bottomNotches,
			ArrayList<Integer> bottomTabs) {
		
		
		boolean strip[][] = new boolean[length][width + 2 * GAP];
		
		for(int i=0; i<length; i++) {
			for(int j=0; j<width; j++) {
				strip[i][GAP + j] = true;
			}
		}
		
		
		
		for(int i=0; i<topNotch.length; i++) {
			for(int j=0; j<topNotch[0].length; j++) {
				if(topNotch[i][j]) {
					System.out.print("#");
				} else {
					System.out.print("_");
				}
			}
			System.out.println();
		}
		System.out.println();
		
		

		//START Notches and Tabs
		//Left = bottom.
		//Right = top
		for(int ind=0; ind<bottomNotches.size(); ind++) {
			
			int curCenter = bottomNotches.get(ind);
			
			for(int i=0; i<bottomNotch.length; i++) {
				for(int j=0; j<bottomNotch[0].length; j++) {
					strip[(curCenter + i) % strip.length][GAP + j] = ! bottomNotch[i][j];
				}
			}
			
		}
		
		for(int ind=0; ind<bottomTabs.size(); ind++) {
			
			int curCenter = bottomTabs.get(ind);
			
			for(int i=0; i<bottomTab.length; i++) {
				for(int j=0; j<bottomTab[0].length; j++) {
					strip[(curCenter + i) % strip.length][GAP - topNotch[0].length + j] = bottomTab[i][j];
				}
			}
			
		}
		
		
		for(int ind=0; ind<topNotches.size(); ind++) {
			
			int curCenter = topNotches.get(ind);
			
			for(int i=0; i<topNotch.length; i++) {
				for(int j=0; j<topNotch[0].length; j++) {
					strip[(curCenter + i) % strip.length][GAP + width - topNotch[0].length  + j] = ! topNotch[i][j];
				}
			}
			
		}
		
		for(int ind=0; ind<topTabs.size(); ind++) {
			
			int curCenter = topTabs.get(ind);
			
			for(int i=0; i<topTab.length; i++) {
				for(int j=0; j<topTab[0].length; j++) {
					strip[(curCenter + i) % strip.length][GAP + width + j] = topTab[i][j];
				}
			}
			
		}
		//END Notches and Tabs
		
		for(int i=0; i<strip.length; i++) {
			for(int j=0; j<strip[0].length; j++) {
				if(strip[i][j]) {
					System.out.print("#");
				} else {
					System.out.print("_");
				}
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println("Number of cells: " + countCells(strip) + " (expected: 2660)");
	}
	
	
	public static int countCells(boolean net[][]) {
		int ret = 0;
		
		for(int i=0; i<net.length; i++) {
			for(int j=0; j<net[0].length; j++) {
				if(net[i][j]) {
					ret++;
				}
			}
		}
		return ret;
	}
}
