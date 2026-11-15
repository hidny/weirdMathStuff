package cccg2026;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

import UtilityFunctions.Fraction;

//This formula test is inspired by the numbers given in this
// CCCG 2026 paper:
//Counting Polyominoes by Area and Perimeter*
//Gill Barequet and Zohar Nagel∗


// I'm trying to calc the odds that if I find a NxN binary 1/0 grid that's big (tends to inf), and I pick a random cell,
// what are the odds that the cell:
// a) is 1
// b) is part of a polyomino greater than area 40 (or infinite)

// For the record, it's a 21.862 % chance that a randomly selected cell is in a polyomino that's area greater than 40 (for when odds of 1 being 1/2)

// Area 40 is as high as the data in the paper goes...

//I don't know what to do with this new-found knowledge. :( Oh well!


//TODO: does something cool happen when you set the probability really low?
// TODO: Maybe you can somehow generate the number of polyominoes without bruteforcing it!

//For prob cell 1 equal 1/2
//public static final int NUM = 1;
//public static final int DENUM = 2;

//For prob cell 1 equal 1/10
//public static final int NUM = 1;
//public static final int DENUM = 10;


public class Test_polyomino_formula_with_fake_perim_3 {

	public static final BigInteger array[][] = new BigInteger[83][41];
	
	
	public static Fraction probableInf = Fraction.ONE;
	
	public static BigInteger powDenum[] = new BigInteger[(array.length + array[0].length + 1)];
	public static BigInteger powNum[] =   new BigInteger[(array.length + array[0].length + 1)];
	public static BigInteger powAntiNum[] = new BigInteger[(array.length + array[0].length + 1)];
	
	//This thought experiment actually worked out! I don't know what to do with this info
	// though!
	
	//Tried: Something is wrong at i = 62 maybe..
	//Wired: AHA! The data to bring it down to 0 is missing from the paper.
	// This is the best we could do...
	
	// approx 21.862 % chance that a randomly selected cell is in a polyomino that's area greater than 40.
	// (If prob on is 1/2) and 0 polyominoes don't count.
	
	public static final int NUM = 1;
	public static final int DENUM = 10000;
	public static final int ANITNUM = DENUM - NUM;
	
	public static void setupArray(BigInteger array[], int num) {
		
		BigInteger curPow = BigInteger.ONE;
		for(int i=0; i<array.length; i++) {
			array[i] = curPow;
			curPow = curPow.multiply(new BigInteger("" + num));
		}
	}
	
	static {
		setupArray(powDenum, DENUM);
		setupArray(powNum, NUM);
		setupArray(powAntiNum, ANITNUM);
		
		
		for(int i=0; i<array.length; i++) {

			for(int j=0; j<array[0].length; j++) {
				array[i][j] = BigInteger.ZERO;
			}
		}
		
		//They missed the 0 point!
		//For it to work, it has to have 1 'perimeter element'. Weird!
		array[1][0] = new BigInteger("1");
		readArray();
	}
	
	
	public static void listFiles(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;   // handle inaccessible directories

        for (File file : files) {
            System.out.println(file.getAbsolutePath());
            //if (file.isDirectory()) {
            //    listFiles(file);     // recursive call
            //}
            
            try {
            	Scanner in = new Scanner(file);
            	
            	String cur = in.nextLine();
            	
            	String afterEqual = cur.substring(cur.indexOf("=") + 1);
            	
            	int startIndex = 0;
            	for(int i=0; i<afterEqual.length(); i++) {
            		if(afterEqual.charAt(i) >= '0' && afterEqual.charAt(i) <= '9') {
            			startIndex = 10*startIndex + (int)(afterEqual.charAt(i) - '0');
            		} else {
            			break;
            		}
            	}
            	System.out.println("Start index: " + startIndex);
            	
            	while(in.hasNextLine()) {
            		String line = in.nextLine();
            		
            		line = line.replace(",", "");
            		
            		String tokens[] = line.split(" ");
            		
            		int rowNum = Integer.parseInt(tokens[0]);
            		
            		for(int i=1; i<tokens.length; i++) {
            			if(tokens[i].equals("")) {
            				continue;
            			}
            			
            			array[rowNum][startIndex + i - 1] = new BigInteger(tokens[i]);
            			//array[startIndex + i - 1][rowNum] = new BigInteger(tokens[i]);
            			System.out.println("array[" + rowNum + "][" + (startIndex + i - 1) + "] = new BigInteger(\"" + tokens[i] + "\");");
            			System.out.println(array[rowNum][startIndex + i - 1]);
            		}
            		System.out.println();
            		
            	}
            	
            	
            } catch(Exception e) {
    			e.printStackTrace();
    		} finally {
    		}
            
        }
    }

    public static void readArray() {
        File root = new File("perimPaperData");
        listFiles(root);
    }
    
	
	public static void main(String args[]) {
		
		Fraction origProb = new Fraction(new BigInteger("" + NUM), new BigInteger("" + DENUM));
		
		System.out.println("Original probability: " + origProb);

		for(int j=0; j<array[0].length; j++) {
			for(int i=0; i<array.length; i++) {
			
				
				Fraction probArrayElement = new Fraction(array[i][j], powDenum[i + j]);
				
				if(j == 0) {
					//Exception!
				} else {
					probArrayElement = Fraction.mult(probArrayElement, j);
				}
				
				probArrayElement = Fraction.mult(probArrayElement, new Fraction(powNum[j], BigInteger.ONE));
				probArrayElement = Fraction.mult(probArrayElement, new Fraction(powAntiNum[i], BigInteger.ONE));
				
				probableInf = Fraction.minus(probableInf, probArrayElement);
			}
			System.out.println("Cur prob to go to infinity after j = " + j + ":");
			//System.out.println(probableInf.getDecimalFormat(15));
			System.out.println("Approx prob:" + probableInf.getSmallNumber(15));
			System.out.println();
			
		}
		

		System.out.println("Final prob to go to infinity:");
		System.out.println(probableInf.getDecimalFormat(15));
	}
}
