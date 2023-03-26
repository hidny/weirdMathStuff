package OneNet3Cuboids.OutputReader;

import java.util.ArrayList;
import java.util.Scanner;

import OneNet3Cuboids.Coord.Coord2D;

public class OutputReaderUtilsFunctions {

	public static boolean[][] addBorderAroundArray(boolean array[][], int borderSize) {
		
		boolean ret[][] = new boolean[array.length+2*borderSize][array[0].length + 2 * borderSize];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				
				int i2 = i - borderSize;
				int j2 = j - borderSize;
				
				if(i2>=0 && i2<array.length && j2>=0 && j2<array[0].length
						&& array[i2][j2]) {
					ret[i][j] = true;
				} else {
					ret[i][j] = false;
				}
			}
		}
		
		return ret;
		
	}
	
	public static boolean[][] getNextSolutionAsPaperUsedArray(Scanner in) {
		
		ArrayList<String> net = new ArrayList<String>();
		
		boolean foundSolution = false;
		boolean readyToReadSolution = false;
		while(in.hasNextLine()) {
			
			String tmp = in.nextLine();
			
			if(readyToReadSolution) {
				if(tmp.contains("#") && tmp.endsWith("|")) {
					net.add(tmp);
					foundSolution = true;
				} else if(foundSolution == true){
					//Done with solution:
					break;
				} else {
					//Hacky, but whatever.
					//solution should come one line after "(num unique:", so we haven't actually found a solution
					readyToReadSolution = false;
				}
			}
			
			if(tmp.contains("(num unique:")) {
				readyToReadSolution = true;
			}
			
		}
		
		if(net.size() == 0) {
			return null;
		}
		boolean array[][] = new boolean[net.size()][net.get(0).replace("|", "").length()];
		
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(net.get(i).charAt(j) == '#') {
					array[i][j] = true;
				} else {
					array[i][j] = false;
				}
			}
		}
		
		//Utils.printFold(array);
		
		//System.out.println("Solution code:");
		
		//I'll need to remake paperToDevelop :(
		//BasicUniqueCheckImproved.isUnique(paperToDevelop, array)
		
		
		
		return array;
	}
	
	public static int[][] makeFakeIndexArray(boolean array[][]) {
		int ret[][] = new int[array.length][array[0].length];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				if(array[i][j]) {
					ret[i][j] = 33;
				} else {
					ret[i][j] = 0;
				}
			}
		}
		return ret;
	}
	

	public static Coord2D[] makeCoordList(boolean array[][]) {
		
		int numElements = 0;
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(array[i][j]) {
					numElements++;
				}
			}
		}

		Coord2D ret[] = new Coord2D[numElements];
		int index = 0;
		for(int i=0; i<array.length; i++) {
			for(int j=0; j<array[0].length; j++) {
				if(array[i][j]) {
					ret[index] = new Coord2D(i, j);
					index++;
				}
			}
		}
		
		return ret;
	}
}
