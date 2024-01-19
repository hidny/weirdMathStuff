package OneNet3Cuboids.OutputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.SolutionResovler.ShapeIntersectionCheckerAtSolutionTime;

public class GetNumSolutionsInFolderMagnifier {

	public static int MAGNIFICATION_FACTOR = 3;
	
	public static void main(String[] args) {
		
		//File folder = new File("C:/Users/Michael/projectEuler2/Cuboid/output/AttemptAt13x1x1and3x3x3byOnlySearchingSimpleSolutions");

		//File folder = new File("D:/output13x1x1DoneSoFar");
		
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(1, 13, 1);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(3, 3, 3);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(12, 6, 2);

		boolean quiet = false;
		
		File folder = new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/");
		
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(1, 1, 11);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(5, 3, 1);
		CuboidToFoldOn cuboid = new  CuboidToFoldOn(MAGNIFICATION_FACTOR * 7, MAGNIFICATION_FACTOR * 2, MAGNIFICATION_FACTOR * 1);
	    //boolean quiet = true;
	    

		ShapeIntersectionCheckerAtSolutionTime shapeCheck = new ShapeIntersectionCheckerAtSolutionTime(cuboid, quiet);
	    
	    //boolean quiet = false;

		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    Scanner in = null;;
			try {
				in = new Scanner(new File(listOfFiles[i].getAbsolutePath()));
			
	
			    int numSolutionsFound = 0;
			    
			    while(in.hasNextLine()) {
	
			    	String tmp = in.nextLine();
			    	
			    	if(tmp.startsWith("Num unique solutions found:")) {
			    		
			    		String tokens[] = tmp.split(" ");
			    		numSolutionsFound = Integer.parseInt(tokens[tokens.length -1]);
			    		
			    		//if(numSolutionsFound > 9999) {
			    		//	break;
			    		//}
			    	} else {
			    		
			    	}
			    
			    }
			    

			    in.close();
			    
				//ShapeIntersectionCheckerAtSolutionTime shapeCheck = new ShapeIntersectionCheckerAtSolutionTime(cuboid, quiet);
				
				in = new Scanner(new File(listOfFiles[i].getAbsolutePath()));
				
				int count = 0;
				while(in.hasNextLine()) {
					count++;
					
					if(count %100000 == 0) {
						System.out.println("Hello count in file of " + count);
					}
					boolean tmpArray[][] = OutputReaderUtilsFunctions.getNextSolutionAsPaperUsedArray(in);
					
					
					if(tmpArray == null) {
						break;
					}
					
					tmpArray = magnifyBoolArray(tmpArray);
					
					boolean array[][] = OutputReaderUtilsFunctions.addBorderAroundArray(tmpArray, 2);
		
					if(cuboid.getNumCellsToFill() != OutputReaderUtilsFunctions.makeCoordList(array).length) {
						System.out.println("ERROR: the number of cells in test solution is not equal to the number of cells required!");
						Utils.printFold(array);
						System.exit(1);
					}
					//Utils.printFold(array);
					
					shapeCheck.resolveSolution(null, OutputReaderUtilsFunctions.makeCoordList(array), OutputReaderUtilsFunctions.makeFakeIndexArray(array), array);
				}
				System.out.println("Number uniq of solutions found through the shape checker: " + shapeCheck.getNumUniqueFound());
			    
			    System.out.println("Num uniq solutions written in output file: " + numSolutionsFound);
			    System.out.println();
			    
			    
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} finally {
				if(in != null) {
					in.close();
				}
			}
		    
		  } else if (listOfFiles[i].isDirectory()) {
		    System.out.println("Directory " + listOfFiles[i].getName());
		  }
		}

		System.out.println("Total num solutions found: " + shapeCheck.getNumUniqueFound());
		
		System.out.println("Done.");
	}

	public static boolean[][] magnifyBoolArray(boolean orig[][]) {
		boolean ret[][] = new boolean[MAGNIFICATION_FACTOR * orig.length][MAGNIFICATION_FACTOR * orig[0].length];
		
		for(int i=0; i<orig.length; i++) {
			for(int j=0; j<orig[0].length; j++) {
				if(orig[i][j]) {
					for(int i2=0; i2<MAGNIFICATION_FACTOR; i2++) {
						for(int j2=0; j2<MAGNIFICATION_FACTOR; j2++) {
						
							ret[MAGNIFICATION_FACTOR*i + i2][MAGNIFICATION_FACTOR*j + j2] = true;
						}
					}
				}
			}
		}
		
		return ret;
	}
}
