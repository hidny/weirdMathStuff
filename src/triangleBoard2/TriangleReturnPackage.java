package triangleBoard2;

public class TriangleReturnPackage {
	
	private boolean hasSolution;	
	private TriangleBoard bestSolution = null;

	public TriangleReturnPackage(boolean hasSolution, TriangleBoard bestSolution) {
		this.hasSolution = hasSolution;
		this.bestSolution = bestSolution;
	}
	
	public boolean HasSolution() {
		return hasSolution;
	}

	
	public TriangleBoard getBestSolution() {
		return bestSolution;
	}
	
	
}
