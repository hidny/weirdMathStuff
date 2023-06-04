package OneNet3Cuboids.MultiplePiecesHandler;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Utils;
import OneNet3Cuboids.DFSIntersectionNet.DFSIntersectFinderRegions;

public class ComputeTaskMain {

	// Length of depth 7 is 5396...
	
	public static int START_DEPTH = 13;
	public static int TARGET_TASK_INDEX = 1000000;
	
	
	//public static int TARGET_TASK_INDEX = 729;
	public static ComputeTaskDescription computeTask = null;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//TODO: given a compute task index and depth, run a search!
		
		if(TARGET_TASK_INDEX < 0) {
			System.out.println("ERROR: please specify the TARGET_TASK_INDEX. (It's currently less than 0)");
		}
		
		runSubtask(START_DEPTH, TARGET_TASK_INDEX);
		
	}
	
	public static void runSubtask(int start_depth, int targetIndex){
		

		updateComputeTask(start_depth, targetIndex);
		
		if(computeTask == null) {
			System.out.println("Target index too high.");
			System.out.println("Num pieces found: " + CuboidComputeTaskGetter.curNumPiecesCreated);
			System.exit(0);
		}
		
		ComputeTaskDescription taskDescriptionToUse = computeTask;
		
		
		System.out.println("Run compute task for index " + targetIndex + ": (Please wait)");
				
		DFSIntersectFinderRegions.doDepthFirstSearch(
				taskDescriptionToUse.paperToDevelop,
				taskDescriptionToUse.indexCuboidonPaper,
				taskDescriptionToUse.paperUsed,
				taskDescriptionToUse.cuboid,
				taskDescriptionToUse.numCellsUsedDepth,
				taskDescriptionToUse.regions,
				-1,
				taskDescriptionToUse.skipSymmetries,
				taskDescriptionToUse.solutionResolver,
				taskDescriptionToUse.cuboidToBringAlongStartRot,
				taskDescriptionToUse.indexCuboidOnPaper2ndCuboid,
				taskDescriptionToUse.topBottombridgeUsedNx1x1,
				taskDescriptionToUse.threeBombHandler,
				taskDescriptionToUse.debugNope,
				taskDescriptionToUse.debugIterations
			);
		
		
		System.out.println("Finished task.");
		
		System.out.println("Final number of unique solutions for Task: " + taskDescriptionToUse.solutionResolver.getNumUniqueFound());

		computeTask = null;
		taskDescriptionToUse = null;

	}
	
	public static void updateComputeTask(int startDepth, int targetIndex) {
		
		
		System.out.println("Fold Resolver Ordered Regions intersection skip symmetries Nx1x1:");

		computeTask = null;
		
		//CuboidComputeTaskGetter.getComputeTask(new CuboidToFoldOn(11, 1, 1), new CuboidToFoldOn(5, 3, 1), true, startDepth, targetIndex);
		CuboidComputeTaskGetter.getComputeTask(new CuboidToFoldOn(13, 1, 1), new CuboidToFoldOn(3, 3, 3), true, startDepth, targetIndex);
		
		//TODO: update the cuboids used:
		
		
		
		//CuboidComputeTaskGetter.getComputeTask(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(3, 2, 1), true, startDepth, targetIndex);
		//CuboidComputeTaskGetter.getComputeTask(new CuboidToFoldOn(5, 1, 1), new CuboidToFoldOn(5, 1, 1), true, startDepth, targetIndex);
		
		
	}

}
