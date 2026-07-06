package cordPuzzle3b1b;

public class cordPuzzle3b1b {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		solve(1);
		solve(2);
		solve(10);
		solve(100);
		solve(1000);
	}
	
	public static double solve(int n) {
		
		int SIMS = 10000;
		
		if(n*SIMS < 1000000) {
			SIMS = 1000000 / n;
		}
		
		int totalCollisions = 0;
		
		for(int iter=0; iter<SIMS; iter++) {
			double coords[] = new double[2*n];
			
			for(int i=0; i<coords.length; i++) {
				coords[i] = Math.random();
			}
			
			for(int i=0; i<n; i++) {
				if(coords[i] > coords[i+n]) {
					double tmp = coords[i];
					coords[i] = coords[i+n];
					coords[i+n] = tmp;
				}
			}
			
			int numCollisions = 0;
			
			for(int i=0; i<n; i++) {
				for(int j=i+1; j<n; j++) {

					boolean foundCollision = false;
					if( coords[i] < coords[j] && coords[i+n] > coords[j] && coords[i+n] < coords[j+n]) {
						numCollisions++;
						foundCollision = true;
					}
					if( coords[j] < coords[i] && coords[j+n] > coords[i] && coords[j+n] < coords[i+n]) {
						numCollisions++;

						if(foundCollision) {
							System.out.println("DOH!");
							System.exit(1);
						}
					}
				}
			}
			
			totalCollisions += numCollisions;
		}
		
		
		double tmp = (1.0 * totalCollisions) / (1.0 * SIMS);
		
		System.out.println("Solve for n = " + n + " and SIMS = " + SIMS + ": " + tmp);
		
		
		return tmp;
	}

}
