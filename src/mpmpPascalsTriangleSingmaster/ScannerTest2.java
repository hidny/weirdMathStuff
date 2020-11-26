package mpmpPascalsTriangleSingmaster;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ScannerTest2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		Scanner in;
		try {
			in = new Scanner(new File("C:\\Users\\Michael\\projectEuler2\\weirdMathStuff\\testDiffParser.txt"));

			String line = in.nextLine();
			String token = in.next();
			int number = in.nextInt();
			String line2 = in.nextLine();
			String line3 = in.nextLine();
			
			sopl(line);
			sopl(token);
			sopl(number);
			sopl(line2);
			sopl(line3);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}


	public static void sop(Object a) {
		System.out.print(a.toString());
	}
	public static void sopl(Object a) {
		System.out.println(a.toString());
	}

	public static void sopl() {
		System.out.println();
	}
	
}
