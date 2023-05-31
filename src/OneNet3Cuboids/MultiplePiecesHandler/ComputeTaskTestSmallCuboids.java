package OneNet3Cuboids.MultiplePiecesHandler;

import java.util.Scanner;

public class ComputeTaskTestSmallCuboids {

	public static void main(String args[]) {
		
		
		int maxDepth = 7;
		
		//10671
		for(int i=0; i<10672; i++) {
			
			System.out.println(i);
			
			ComputeTaskMain.runSubtask(maxDepth, i);
			
			
		}
	}
}
