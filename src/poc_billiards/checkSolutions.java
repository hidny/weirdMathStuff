package poc_billiards;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;


public class checkSolutions {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public static void testSolution(String solutionCodeString) {
		
		//System.out.println(solutionCodeString);
		

		int dimensionsToCheck[][] = null;
		int areaToCheck = -1;
		
		HashSet <String> codesThatCover3PlusCuboids = new HashSet <String>();
		
		HashSet<String> file1Solutions = new HashSet<String>();
		
		int numSoutionsFound = 0;
		int numSolutionsInFile = 0;
		
		boolean table[][] = convertSolutionCodeToTable(solutionCodeString);
		
		if(areaToCheck == -1 || areaToCheck != getNumCellsUsed(table) ) {
			
			if(areaToCheck != -1 && areaToCheck != getNumCellsUsed(table)) {
				System.out.println("Change of dimensions!");
				//System.exit(1);
			}
			areaToCheck = getNumCellsUsed(table);
			dimensionsToCheck = getSetOfDimensionsForSurfaceArea(areaToCheck);
		}

		
		/*
		BasicUniqueCheckImproved.isUnique(makeCoordList(table), table);
		
		if(BasicUniqueCheckImproved.debugLastScore.compareTo(new BigInteger(solutionCodeString)) != 0) {
			System.out.println("DOH! The solution code algo failed!");

			System.out.println(BasicUniqueCheckImproved.debugLastScore + "\nvs\n" + new BigInteger(solutionCodeString));
			System.out.println();
			System.out.println();
			System.exit(1);
		}
		*/
		
		System.out.println("Printing dimensions:");
		for(int i=0; i<dimensionsToCheck.length; i++) {
			System.out.println(dimensionsToCheck[i][0] + "x" + dimensionsToCheck[i][1] + "x" + dimensionsToCheck[i][2]);
			
		}
		System.out.println("Done printing dimensions.");
		
		boolean netToReplicate[][] = padBordersOfBoolTable(table);
		
		boolean solutionFound[] = new boolean[dimensionsToCheck.length];
		int numCuboidsCoveredByNet = 0;
		for(int i=0; i<dimensionsToCheck.length; i++) {
			
			if(ValidNetSolutionChecker.hasSolution(dimensionsToCheck[i], netToReplicate, false)) {
				System.out.println(dimensionsToCheck[i][0] + "x" + dimensionsToCheck[i][1] + "x" + dimensionsToCheck[i][2]);
				numCuboidsCoveredByNet++;
				solutionFound[i] = true;
			} else {
				solutionFound[i] = false;
			}
		}
		
		System.out.println("Num solutions (2): " + numCuboidsCoveredByNet);
		
		if(numCuboidsCoveredByNet >= 3) {
			
			codesThatCover3PlusCuboids.add(solutionCodeString);
			
			System.out.println("Found " + numCuboidsCoveredByNet + "-way solution!");

			numSoutionsFound++;
			
			//if(isRotationallySymmetric(table)) {
			//	System.out.println("This net is rotationally symmetric!");
			//}

			//System.out.println("Printing just the fold without indexes:");
			//Utils.printFold(table);
			
			boolean VERBOSE = true;
			
			/*for(int i=0; i<dimensionsToCheck.length; i++) {
				if(solutionFound[i]) {
					System.out.println("Solution for: " + getDimensionsString(dimensionsToCheck[i]));
					
					ValidNetSolutionChecker.hasSolution(dimensionsToCheck[i], netToReplicate, VERBOSE);
					
					System.out.println();
				}
			}*/
			
			System.out.println("Number of " + numCuboidsCoveredByNet + "-way solutions so far: " + numSoutionsFound);
			System.out.println();
		
			if(numCuboidsCoveredByNet > 3) {
				System.out.println("Goal of finding an " + numCuboidsCoveredByNet + "-way solution Achieved!");
				System.exit(1);
			}
		}
	}

	public static boolean[][] convertSolutionCodeToTable(String solutionCode) {
		
		boolean array[] = convertSolutionCodeTo1DBoolArray(solutionCode);
		
		
		int START_DIM_INDEX = 2;
		int BYTE_SIZE = 8;
		int height = convertBoolArrayToInt(array, START_DIM_INDEX, START_DIM_INDEX + BYTE_SIZE);
		int width = convertBoolArrayToInt(array, START_DIM_INDEX + BYTE_SIZE, START_DIM_INDEX + 2 * BYTE_SIZE);
		
		int startDataIndex = START_DIM_INDEX + 2*BYTE_SIZE;
		

		if(array[1] == false) {
			START_DIM_INDEX = 3;
			BYTE_SIZE = 8;
			height = convertBoolArrayToInt(array, START_DIM_INDEX, START_DIM_INDEX + 2 * BYTE_SIZE);
			width = convertBoolArrayToInt(array, START_DIM_INDEX + 2 * BYTE_SIZE, START_DIM_INDEX + 4 * BYTE_SIZE);
			
			startDataIndex = START_DIM_INDEX + 4*BYTE_SIZE;
		}
		
		boolean ret[][] = new boolean[height][width];
		
		for(int i=0; i + startDataIndex < array.length; i++) {
			
			int retI  = i / width;
			int retJ  = i % width;
			
			ret[retI][retJ] = array[i + startDataIndex];
		}
		
		
		return ret;
		
		}
	
	public static int convertBoolArrayToInt(boolean array[], int startIndex, int endIndex) {
		int ret = 0;
		
		for(int i=startIndex; i<endIndex; i++) {
			if(array[i]) {
				ret = 2*ret + 1;
			} else {
				ret *= 2;
			}
		}
		
		return ret;
	}
	

	public static boolean[] convertSolutionCodeTo1DBoolArray(String solutionCode) {
		
		BigInteger solutionCodeBool = new BigInteger(solutionCode);
		
		//System.out.println(solutionCodeBool);
		
		int lengthArray = 0;
		
		BigInteger cur = solutionCodeBool;
		BigInteger TWO = new BigInteger("2");

		lengthArray+=1;
		
		while(cur.divide(TWO).compareTo(BigInteger.ZERO) > 0) {
			lengthArray++;
			cur = cur.divide(TWO);
		}
		
		boolean ret[] = new boolean[lengthArray];
		cur = solutionCodeBool;
		
		for(int i=0; i<lengthArray; i++) {
			
			if(cur.divideAndRemainder(TWO)[1].compareTo(BigInteger.ONE) == 0) {
				ret[lengthArray - 1 - i] = true;
			} else {

				ret[lengthArray - 1 - i] = false;
			}
			
			cur = cur.divide(TWO);
		}
		return ret;
	}
	
	

	public static int[][] getSetOfDimensionsForSurfaceArea(int origSurfaceArea) {
		
		int ret[][] = null;
		
		ArrayList <String>list = new ArrayList <String>();
		
		int maxA = (int)Math.sqrt(origSurfaceArea);
		
		for(int a=1; a<maxA; a++) {
			int maxB = maxA + 20;
			for(int b=a; b<maxB; b++) {
				for(int c=b; true; c++) {
					
					int surfaceArea = 2 * (a*b + a*c + b*c);
					
					if(origSurfaceArea == surfaceArea) {
						list.add(c + "," + b + "," + a);
					} else if(surfaceArea > origSurfaceArea) {
						break;
					}
					
				}
			}
		}
		
		ret = new int[list.size()][3];
		
		for(int i=0; i<ret.length; i++) {
			
			String tokens[] = list.get(i).split(",");
			
			for(int j=0; j<tokens.length; j++) {
				ret[i][j] = pint(tokens[j]);
			}
		}
		
		return ret;
	}
	

	public static int pint(String s) {
		if (isNumber(s)) {
			return Integer.parseInt(s);
		} else {
			System.out.println("Error: (" + s + ") is not a number");
			return -1;
		}
	}

	public static boolean isNumber(String val) {
		try {
			int a = Integer.parseInt(val);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
	public static final int DEFAULT_BORDER_PADDING = 1;
	public static boolean[][] padBordersOfBoolTable(boolean table[][]) {
		
		boolean ret[][] = new boolean[table.length + 2 * DEFAULT_BORDER_PADDING][table[0].length + 2 * DEFAULT_BORDER_PADDING];
		
		for(int i=0; i<ret.length; i++) {
			for(int j=0; j<ret[0].length; j++) {
				ret[i][j] = false;
				
				if(i>=DEFAULT_BORDER_PADDING
						&& j>=DEFAULT_BORDER_PADDING
						&& i-DEFAULT_BORDER_PADDING < table.length
						&& j-DEFAULT_BORDER_PADDING<table[0].length
				) {
					ret[i][j] = table[i-1][j-1];
				}
			}
		}
		
		
		return ret;
	}
	


	public static String getDimensionsString(int dimensions[]) {
		return "("+ dimensions[0] +"," + dimensions[1] + "," + dimensions[2] +")";
	}
	

	public static int getNumCellsUsed(boolean table[][]) {
		int ret=0;
		for(int i=0; i<table.length; i++) {
			for(int j=0; j<table[0].length; j++) {
				if(table[i][j]) {
					ret++;
				}
			}
		}
		return ret;
	}
	
}
