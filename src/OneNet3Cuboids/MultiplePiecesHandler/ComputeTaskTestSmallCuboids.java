package OneNet3Cuboids.MultiplePiecesHandler;

import java.util.Scanner;

import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;

public class ComputeTaskTestSmallCuboids {

	public static void main(String args[]) {
		
		
		int maxDepth = 7;
		
		//10671
		for(int i=0; i<4353; i++) {
			
			System.out.println(i);
			
			ComputeTaskMain.runSubtask(maxDepth, i);
			
			
		}
		
		System.out.println("Test size: " + BasicUniqueCheckImproved.uniqList.size());
	}
}
