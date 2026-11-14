package cccg2026;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

import UtilityFunctions.Fraction;

//This formula test is inspired by the numbers given in this
// CCCG 2026 paper:
//Counting Polyominoes by Area and Perimeter*
//Gill Barequet and Zohar Nagel∗

public class Test_polyomino_formula_with_fake_perim_2 {

	public static final BigInteger array[][] = new BigInteger[83][41];
	
	
	public static Fraction probableInf = Fraction.ONE;
	
	public static BigInteger pow4[] = new BigInteger[2*(array.length + array[0].length + 1)];
	public static BigInteger pow3[] = new BigInteger[(array.length + array[0].length + 1)];
	
	static {
		BigInteger curPow4 = BigInteger.ONE;
		
		for(int i=0; i<pow4.length; i++) {
			pow4[i] = curPow4;
			curPow4 = curPow4.multiply(new BigInteger("4"));
		}

		BigInteger curPow3 = BigInteger.ONE;
		
		for(int i=0; i<pow3.length; i++) {
			pow3[i] = curPow3;
			curPow3 = curPow3.multiply(new BigInteger("3"));
		}
		
		
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
			
			
			for(int j=0; j<array[0].length; j++) {
				
				Fraction probArrayElement = new Fraction(array[i][j], pow4[i + j]);
				probArrayElement = Fraction.mult(probArrayElement, new Fraction(pow3[i], BigInteger.ONE));
				
				probableInf = Fraction.minus(probableInf, probArrayElement);
			}
			System.out.println("Cur prob to go to infinity after i = " + i + ":");
			System.out.println(probableInf.getDecimalFormat(15));
			
		}
		

		System.out.println("Final prob to go to infinity:");
		System.out.println(probableInf.getDecimalFormat(15));
	}
}
