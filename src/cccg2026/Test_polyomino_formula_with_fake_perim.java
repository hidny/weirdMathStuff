package cccg2026;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

import UtilityFunctions.Fraction;

//This formula test is inspired by the numbers given in this
// CCCG 2026 paper:
//Counting Polyominoes by Area and Perimeter*
//Gill Barequet and Zohar Nagel∗

public class Test_polyomino_formula_with_fake_perim {

	public static final BigInteger array[][] = new BigInteger[83][41];
	
	
	public static Fraction probableInf = Fraction.ONE;
	
	public static BigInteger pow2[] = new BigInteger[array.length + array[0].length + 1];
	
	static {
		BigInteger curPow2 = BigInteger.ONE;
		
		for(int i=0; i<pow2.length; i++) {
			pow2[i] = curPow2;
			curPow2 = curPow2.multiply(new BigInteger("2"));
		}
		
		
		for(int i=0; i<array.length; i++) {

			for(int j=0; j<array[0].length; j++) {
				array[i][j] = BigInteger.ZERO;
			}
		}
		
		//They missed the 0 point!
		//For it to work, it has to have 1 'perimeter element'. Weird!
		array[0][1] = new BigInteger("1");
		/*
		array[1][4] = new BigInteger("1");
		
		array[2][6] = new BigInteger("2");

		array[3][7] = new BigInteger("4");
		array[3][8] = new BigInteger("2");

		array[4][8] = new BigInteger("9");
		array[4][9] = new BigInteger("8");
		array[4][10] = new BigInteger("2");

		array[5][8] = new BigInteger("1");
		array[5][9] = new BigInteger("20");
		array[5][10] = new BigInteger("28");
		array[5][11] = new BigInteger("12");
		array[5][12] = new BigInteger("2");

		array[6][9] = new BigInteger("4");
		array[6][10] = new BigInteger("54");
		array[6][11] = new BigInteger("80");
		array[6][12] = new BigInteger("60");
		array[6][13] = new BigInteger("16");
		array[6][14] = new BigInteger("2");

		array[7][10] = new BigInteger("22");
		array[7][11] = new BigInteger("136");
		array[7][12] = new BigInteger("252");
		array[7][13] = new BigInteger("228");
		array[7][14] = new BigInteger("100");
		array[7][15] = new BigInteger("20");
		array[7][16] = new BigInteger("2");

		array[8][10] = new BigInteger("4");
		array[8][11] = new BigInteger("80");
		array[8][12] = new BigInteger("388");
		array[8][13] = new BigInteger("777");
		array[8][14] = new BigInteger("818");
		array[8][15] = new BigInteger("480");
		array[8][16] = new BigInteger("152");
		array[8][17] = new BigInteger("24");
		array[8][18] = new BigInteger("2");

		array[9][11] = new BigInteger("28");
		*/
		
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
		
		
		for(int i=0; i<array.length; i++) {
			
			System.out.println("Cur prob to go to infinity before i = " + i + ":");
			System.out.println(probableInf.getDecimalFormat(15));
			
			for(int j=0; j<array[0].length; j++) {
				
				Fraction probArrayElement = new Fraction(array[i][j], pow2[i + j]);
				probableInf = Fraction.minus(probableInf, probArrayElement);
			}
			
		}
		

		System.out.println("Final prob to go to infinity:");
		System.out.println(probableInf.getDecimalFormat(15));
	}
}
