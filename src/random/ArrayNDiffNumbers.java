package random;

public class ArrayNDiffNumbers {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int array[];
	public ArrayNDiffNumbers(int size) {
		array = new int[size];
		for(int i=0; i<size; i++) {
			array[i] = i + 1;
		}
	}
	
	private void shuffle() {
		for(int i=0; i<array.length; i++) {
			swap(i, i + (int)(Math.random() * (array.length-i)) );
		}
		
	}
	
	//pre: i and j are indexes of the deck:
	private void swap(int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
	public int[] getArray() {
		shuffle();
		return array;
	}
}
