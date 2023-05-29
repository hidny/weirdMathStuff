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
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(7, 2, 1);

		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(13, 1, 1);
		CuboidToFoldOn cuboid = new  CuboidToFoldOn(3, 3, 3);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(6, 3, 1);
		
		ShapeIntersectionCheckerAtSolutionTime shapeCheck = new ShapeIntersectionCheckerAtSolutionTime(cuboid);
		
		// TODO Auto-generated method stub
		Scanner in = null;
		try {
			//in = new Scanner(new File("testSolutions.txt"));
			
			//in = new Scanner(new File("11x1x1AND5x3x1Only1BottomNeighbourSolutions.txt"));
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex0.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex1Finally.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex1and2Finally.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex1to4inclMissingEndOf4.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex5part1.txt"));

			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to24incl.txt"));
			
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex6to7part1.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex7part2.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex8.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex9.txt"));

			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex10to13incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex14to19incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to24incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to31incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex20to35incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex36to39incl.txt"));
			
			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex40.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex41.txt"));

			//Got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex42.txt"));
			
			//got 0 :(
			//in = new Scanner(new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/Intersect11x1x1and5x3x1AtIndex43to45incl.txt"));
			
			//in = new Scanner(new File("D:/TEST13X1X1Index0again.TXT"));
			in = new Scanner(new File("D:/TEST13X1X1Index1AND2.TXT"));
			//in = new Scanner(new File("D:/TEST13X1X1Index3to6.TXT"));
			//in = new Scanner(new File("D:/TEST13X1X1Index6to9.TXT"));
			
			//TODO: move to a special folder and change name to index0.txt
			
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex1TRIAL2.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex4.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex20.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex14to19incl.txt"));
			
			//in = new Scanner(new File("C:/Users/Michael/Desktop/Intersect11x1x1and5x3x1AtIndex6to9incl.txt"));
			//Intersect11x1x1and5x3x1AtIndex6to9incl.txt
		
			int count = 0;
			while(in.hasNextLine()) {
				count++;
				if(count % 100000 == 0) {
					System.out.println("hello count of " + count);
				}
				boolean tmpArray[][] = OutputReaderUtilsFunctions.getNextSolutionAsPaperUsedArray(in);
				
				if(tmpArray == null) {
					break;
				}
				
				boolean array[][] = OutputReaderUtilsFunctions.addBorderAroundArray(tmpArray, 2);
	
				if(cuboid.getNumCellsToFill() != OutputReaderUtilsFunctions.makeCoordList(array).length) {
					System.out.println("ERROR: the number of cells in test solution is not equal to the number of cells required!");
					Utils.printFold(array);
					System.exit(1);
				}
				//Utils.printFold(array);
				
				shapeCheck.resolveSolution(null, OutputReaderUtilsFunctions.makeCoordList(array), OutputReaderUtilsFunctions.makeFakeIndexArray(array), array);
			}
			System.out.println("Number of solutions found: " + shapeCheck.getNumUniqueFound());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			in.close();
		}
		

	}
	

}
