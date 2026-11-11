package poc_billiards;

import java.util.ArrayList;

public class polyominizeTest {

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
					strip[getIForStrip(curCenter, i, strip.length)][GAP + j] = ! bottomNotch[i][j];
				}
			}
			
		}
		
		for(int ind=0; ind<bottomTabs.size(); ind++) {
			
			int curCenter = bottomTabs.get(ind);
			
			for(int i=0; i<bottomTab.length; i++) {
				for(int j=0; j<bottomTab[0].length; j++) {
					strip[getIForStrip(curCenter, i, strip.length)][GAP - topNotch[0].length + j] = bottomTab[i][j];
				}
			}
			
		}
		
		
		for(int ind=0; ind<topNotches.size(); ind++) {
			
			int curCenter = topNotches.get(ind);
			
			for(int i=0; i<topNotch.length; i++) {
				for(int j=0; j<topNotch[0].length; j++) {
					strip[getIForStrip(curCenter, i, strip.length)][GAP + width - topNotch[0].length  + j] = ! topNotch[i][j];
				}
			}
			
		}
		
		for(int ind=0; ind<topTabs.size(); ind++) {
			
			int curCenter = topTabs.get(ind);
			
			for(int i=0; i<topTab.length; i++) {
				for(int j=0; j<topTab[0].length; j++) {
					strip[getIForStrip(curCenter, i, strip.length)][GAP + width + j] = topTab[i][j];
				}
			}
			
		}
		//END Notches and Tabs
		
		printBoolTable(strip);
		
		makeSmaller(strip);
		
		System.out.println("Number of cells: " + countCells(strip) + " (expected: 2660)");
		
		polyominize(strip);
	}
	
	private static int getIForStrip(int curCenter, int i, int stripLength) {
		return (stripLength - bottomTab.length/2 + curCenter + i) % stripLength;
	}
	
	public static void printBoolTable(boolean net[][]) {

		for(int i=0; i<net.length; i++) {
			for(int j=0; j<net[0].length; j++) {
				if(net[i][j]) {
					System.out.print("#");
				} else {
					System.out.print("_");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	
	public static boolean[][] polyominizeOld(boolean strip[][]) {
		
		boolean ret[][] = new boolean[1000][1000];
		
		for(int i=0; i<strip.length; i++) {
			for(int j=0; j<strip[0].length; j++) {
				if(strip[i][j]) {
					
					//double X = (2*(i + 0.5) + (j + 0.5)) / 5.0;
					//double Y = (0.0 -(i + 0.5) + 2*(j + 0.5)) / 5.0;
					
					//double X = (2.0*(i) + (j )) / 5.0;
					//double Y = (0.0 - (i) + 2*(j )) / 5.0;
					

					double Y = (0.0 - (j) + 2*(i)) / 5.0;
					double X = (2.0*(j) + (i)) / 5.0;
					//double Y = (0.0 - (j + 0.5) + 2*(i + 0.5)) / 5.0;
					//double X = (2.0*(j + 0.5) + (i + 0.5)) / 5.0;
					
					int newJ = (int)Y;
					int newI = (int)X;
					
					int adjustY = newI + 2 * newJ + 1;
					int adjustX = 2 * newI - newJ;
					
					System.out.println("----------");
					System.out.println(i + ", " + j);
					System.out.println(Y + " , " + X);
					System.out.println(adjustY + " , " + adjustX);
					System.out.println("----------");
					
					if(
							adjustX >= 0 
							&& adjustY >= 0 
							&& adjustY < strip.length 
							&& adjustX < strip[0].length) {
						
						if(strip[adjustY][adjustX] ) {

							if(ret[newI + 100][newJ + 100]) {
								System.out.println("Oh crap! (X, Y) lit up at least twice");
							}
							
							ret[newI + 100][newJ + 100] = true;
							
						}
						//System.exit(1);
					} else {
						
						if(2 * newJ - newI < 0) {
							System.out.println("Smaller than 0");
						}
						
						if(newJ + 2 * newI + 1 < 0) {
							System.out.println("Smaller than 0 (2)");
						}
					}
				}
			}
		}
		
		
		
		ret = makeSmaller(ret);

		printBoolTable(ret);
		
		System.out.println("Number of cells: " + countCells(ret) + " (expected: 532)");
		
		return ret;
	}
		
	public static boolean[][] polyominize(boolean strip[][]) {
	
		boolean ret[][] = new boolean[1000][1000];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				
				int xOrig = i - 500;
				int yOrig = j - 500;
				
				int x = 2*xOrig - yOrig;
				int y = xOrig + 2*yOrig + 1;
				
				if( x >= 0 && y >= 0 && x < strip.length && y < strip[0].length) {
					if(strip[x][y]) {
						ret[i][j] = true;
					} else {
						ret[i][j] = false;
					}
					System.out.println("good");
				} else {
					//System.out.println("coord: " + x + ", " + y);
				}
			}
		}
		ret = makeSmaller(ret);
	
		printBoolTable(ret);
		
		System.out.println("Number of cells: " + countCells(ret) + " (expected: 532)");
		
		return ret;
	}


	public static boolean[][] makeSmaller(boolean strip[][]) {
		
		int border[] = findBorders(strip);
		

		boolean ret[][] = new boolean
				[border[1] - border[0] + 1]
				[border[3] - border[2] + 1];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++ ) {
				ret[i][j] = strip[border[0] + i][border[2] + j];
			}
		}
		
		return ret;
	}
	
	public static int[] findBorders(boolean array[][]) {
		
		int minI = array.length - 1;
		int maxI = 0;

		int minJ = array[0].length - 1;
		int maxJ = 0;
		
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(array[i][j]) {
					
					if(i < minI) {
						minI = i;
					}
					
					if(i > maxI) {
						maxI = i;
					}
					
					if(j < minJ) {
						minJ = j;
					}
					
					if(j > maxJ) {
						maxJ = j;
					}
				}
			}
		}
		
		return new int[] {minI, maxI, minJ, maxJ};
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
