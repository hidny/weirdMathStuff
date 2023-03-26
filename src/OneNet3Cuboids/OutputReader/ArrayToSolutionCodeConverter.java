package OneNet3Cuboids.OutputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.Coord.Coord;
import OneNet3Cuboids.Coord.Coord2D;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;
import OneNet3Cuboids.SolutionResovler.ShapeIntersectionCheckerAtSolutionTime;

public class ArrayToSolutionCodeConverter {

	//Hacky way to find 3rd intersection by:
	// 1) looking at output of DFSIntersectFinderRegions.java
	// 2) Parsing the solutions found
	// 3) Running the solutions against a 3rd cuboid.
	
	//So far, I didn't find anything :(
	//I was really hoping to find a quick solution where the top and bottom of 11x1x1 had only 1 neighbour.
	// TODO: Maybe I'll try the same thing with 13x1x1 and 3x3x3
	
	public static void main(String[] args) {
		
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(1, 11, 1);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(1, 11, 1);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(5, 3, 1);
		CuboidToFoldOn cuboid = new  CuboidToFoldOn(7, 2, 1);
		
		ShapeIntersectionCheckerAtSolutionTime shapeCheck = new ShapeIntersectionCheckerAtSolutionTime(cuboid);
		
		// TODO Auto-generated method stub
		Scanner in = null;
		try {
			//in = new Scanner(new File("testSolutions.txt"));
			
			//in = new Scanner(new File("11x1x1AND5x3x1Only1BottomNeighbourSolutions.txt"));
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex0.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex1Finally.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex1and2Finally.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex1to4inclMissingEndOf4.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex5part1.txt"));
			
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex4.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex10to13incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex14to19incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to24incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to31incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to35incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex36to39incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex40.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex41.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex42.txt"));
			
			//got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/msttParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex43to45incl.txt"));
			
			//TODO: move to a special folder and change name to index0.txt
			
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex1TRIAL2.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex4.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex20.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex14to19incl.txt"));
			
			in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex6to9incl.txt"));
			//Intersect11x1x1and5x3x1AtIndex6to9incl.txt
		
			while(in.hasNextLine()) {
				
				boolean tmpArray[][] = getNextSolutionAsPaperUsedArray(in);
				
				if(tmpArray == null) {
					break;
				}
				
				boolean array[][] = addBorderAroundArray(tmpArray, 2);
	
				if(cuboid.getNumCellsToFill() != makeCoordList(array).length) {
					System.out.println("ERROR: the number of cells in test solution is not equal to the number of cells required!");
					Utils.printFold(array);
					System.exit(1);
				}
				//Utils.printFold(array);
				
				shapeCheck.resolveSolution(null, makeCoordList(array), makeFakeIndexArray(array), array);
			}
			System.out.println("Number of solutions found: " + shapeCheck.getNumUniqueFound());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
		

	}
	
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
	
	public static boolean checkIfArrayIsValidSolutionforCuboid(boolean array[][]) {
		
		return false;
	}

}
