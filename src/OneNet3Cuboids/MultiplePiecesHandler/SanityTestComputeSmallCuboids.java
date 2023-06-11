package OneNet3Cuboids.MultiplePiecesHandler;

import java.util.Scanner;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;

public class SanityTestComputeSmallCuboids {

	public static void main(String args[]) {
		
		
		int maxDepth = 7;
		
		//10671
		for(int i=0; i<4353; i++) {
			
			System.out.println(i);
			
			ComputeTaskMain.runSubtask(maxDepth, i, new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1));
			
			
		}
		
		System.out.println("Test size: " + BasicUniqueCheckImproved.uniqList.size());
	}
}
