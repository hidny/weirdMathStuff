package numberphile;

public class testingModscube {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int N = 247;
		int target = 33;
		for(int i=0; i<N; i++) {
			for(int j=0; j<N; j++) {
				System.out.print((i*i*i + j*j*j) % N + "    ");
			}
			System.out.println();
		}
		
		for(int k=0; k<N; k++) {
			System.out.println("Want " + (target + k*k*k)%N);
		}
	}

}
