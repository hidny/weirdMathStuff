package mpmpPascalsTriangleSingmaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner in = new Scanner(System.in);
		
		String a = in.nextLine();
		
		System.out.println(a);
		
		Scanner inFile[] = new Scanner[25];

		try {
			for(int i=0; i<inFile.length; i++) {
				inFile[i] = new Scanner(new File("C:\\Users\\Michael\\projectEuler2\\adventofcode\\in2019\\prob2019in" + (i+1) + ".txt"));
			}
			
			for(int i=0; i<inFile.length; i++) {
				while(inFile[i].hasNextLine()) {
					System.out.println(inFile[i].next());
				}
				
				//inFile[i].close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
