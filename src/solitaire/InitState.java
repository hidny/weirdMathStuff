package solitaire;

public class InitState {

	public static String state[][] =
			new String[][] { 
		{ "1R", "2R", "3R", "4R", "5R", "6R", "7R", "8R" },
		{ "1B", "2B", "3B", "4B", "5B", "6B", "7B", "8B" },
		{ "1G", "2G", "3G", "4G", "5G", "6G", "7G", "8G" },
		{ "F1", "XR", "XX", "XR", "XR", "YR", "YR", "YR" },
		{ "YR", "ZR", "ZR", "ZR", "ZR", "9R", "9B", "9G" }};
	
	public static String state2[][] =
			new String[][] { 
		{ "9R", "2R", "3R", "4R", "5R", "6R", "7R", "8R" },
		{ "9B", "2B", "3B", "4B", "5B", "6B", "7B", "8B" },
		{ "9G", "2G", "3G", "4G", "5G", "6G", "7G", "8G" },
		{ "2R", "XR", "XX", "XR", "XR", "YR", "YR", "YR" },
		{ "1R", "1G", "1B", "ZR", "FF", "9R", "9B", "9G" }};
	
	
	public static void main(String args[]) {
		
		State s = new State(state);

		State s2 = new State(state2);
		
	}
	
}
