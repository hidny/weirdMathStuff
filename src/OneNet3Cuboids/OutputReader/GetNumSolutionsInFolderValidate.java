package OneNet3Cuboids.OutputReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.SolutionResovler.ShapeIntersectionCheckerAtSolutionTime;

public class GetNumSolutionsInFolderValidate {

	public static void main(String[] args) {
		
		//File folder = new File("C:/Users/Michael/projectEuler2/Cuboid/output/AttemptAt13x1x1and3x3x3byOnlySearchingSimpleSolutions");

		File folder = new File("D:\\7x2x1Intersects");
		//File folder = new File("D:\\10x1x1Intersect");
		
		//File folder = new File("D:\\area46AbundantSolutions");
		
		//File folder = new File("C:/Users/Michael/projectEuler2/weirdMathStuff/cuboidNetSearch/net_search_output");
		
		//File folder = new File("C:\\Users\\Michael\\Desktop\\mattParkerChallenge\\5X3X1and11x1x1Intersects\\JennySample");
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(1, 13, 1);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(3, 3, 3);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(6, 3, 1);

		boolean quiet = false;
		
		//File folder = new File("C:/Users/Michael/Desktop/mattParkerChallenge/5X3X1and11x1x1Intersects/");
		
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(5, 3, 1);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(11, 1, 1);
		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(7, 2, 1);
		

		//CuboidToFoldOn cuboid = new  CuboidToFoldOn(10, 1, 1);
		CuboidToFoldOn cuboid = new  CuboidToFoldOn(3, 3, 2);
	    //boolean quiet = true;
	    

		ShapeIntersectionCheckerAtSolutionTime shapeCheck = new ShapeIntersectionCheckerAtSolutionTime(cuboid, quiet);
	    
	    //boolean quiet = false;

		File[] listOfFiles = folder.listFiles();
		
		int numSlices = -1;
		
		
		boolean checkOutputArray[] = null;
		
		for (int i = 0; i < listOfFiles.length; i++) {
		  if (listOfFiles[i].isFile()) {
		    System.out.println("File " + listOfFiles[i].getName());
		    
		    Scanner in = null;;
			try {
				
				in = new Scanner(new File(listOfFiles[i].getAbsolutePath()));
				
				int count = 0;
				while(in.hasNextLine()) {
					
					String tmp = in.nextLine();
					if(tmp.contains("Final num pieces:")) {
						
						int numSlicesTmp = Integer.parseInt(in.nextLine());
						
						if(numSlices == -1 || numSlicesTmp == numSlices) {
							numSlices = numSlicesTmp;
							if(checkOutputArray == null) {
								System.out.println("Num slices to check: " + numSlices);
								checkOutputArray = new boolean[numSlices];
							}
						}
					}
					
					//Index pre-shuffle to post-shuffle: 3500 to 876
					
					if(tmp.contains("Index pre-shuffle to post-shuffle:")) {
						
						int sliceId = Integer.parseInt(tmp.split(" ")[tmp.split(" ").length - 1]);
						
						checkOutputArray[sliceId] = true;
					}
					
					
				}
				
			    
			    
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
		

		for(int i=0; i<checkOutputArray.length; i++) {
			if(checkOutputArray[i] == false) {
				System.out.println("DOH!");
				System.exit(1);
			}
		}

		System.out.println("Total num solutions found: " + shapeCheck.getNumUniqueFound());
		
		System.out.println("Done.");
	}

}
