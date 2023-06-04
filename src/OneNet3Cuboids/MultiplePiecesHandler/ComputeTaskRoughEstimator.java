package OneNet3Cuboids.MultiplePiecesHandler;

import java.util.Scanner;

import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;

//13x1x1 and 3x3x3 with max depth of 13:
// 2083716 pieces / 8 (piece/minute) / 60(minutes/hour) /24 (hours/day) = 181 days
public class ComputeTaskRoughEstimator {

	public static void main(String args[]) {
		
		//13x1x1 depth 13: 2083716
		int maxDepth = 13;
		
		int NUM_INDEX = 2083716;
		//10671
		for(int i=0; i<1000; i++) {

			ComputeTaskMain.runSubtask(maxDepth, (int)(Math.random() * NUM_INDEX));

			System.out.println("Test size after " + (i+1) + " iterations: " + BasicUniqueCheckImproved.uniqList.size());
		}
		
		
		System.out.println("Test size: " + BasicUniqueCheckImproved.uniqList.size());
	}
}
