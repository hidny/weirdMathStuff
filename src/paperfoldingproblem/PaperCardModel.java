package paperfoldingproblem;

public class PaperCardModel {

	
	//private int iCoord = -1;
	//private int jCoord = -1;
	
	public static int getCardNumOtherSideOfRightFinal(int i, int j) {
		
		int curCardNum = i * PaperFolderModel.WIDTH + j;
		
		if(hasFoldRightFinal(i, j) == false) {
			System.out.println("ERROR: trying to get card that doesn't exist! (right)" + curCardNum);
			System.exit(1);
		}
		
		if(isVerticallyReflected(i, j) == false) {
			return curCardNum + 1;
		} else {
			return curCardNum - 1;
		}
	}
	
	public static int getCardNumOtherSideOfLeftFinal(int i, int j) {
		
		int curCardNum = i * PaperFolderModel.WIDTH + j;
		
		if(hasFoldLeftFinal(i, j) == false) {
			System.out.println("ERROR: trying to get card that doesn't exist! (left)" + curCardNum);
			System.exit(1);
		}
		
		if(isVerticallyReflected(i, j) == false) {
			return curCardNum - 1;
		} else {
			return curCardNum + 1;
		}
	}
	
	
	public static int getCardNumOtherSideOfBottomFinal(int i, int j) {
		
		int curCardNum = i * PaperFolderModel.WIDTH + j;
		
		if(hasFoldBottomFinal(i, j) == false) {
			System.out.println("ERROR: trying to get card that doesn't exist! (bottom)" + curCardNum);
			System.exit(1);
		}
		
		if(isHorizontallyRelected(i, j) == false) {
			return curCardNum + PaperFolderModel.WIDTH;
		} else {
			return curCardNum - PaperFolderModel.WIDTH;
		}
	}
	
	public static int getCardNumOtherSideOfTopFinal(int i, int j) {
		
		int curCardNum = i * PaperFolderModel.WIDTH + j;
		
		if(hasFoldTopFinal(i, j) == false) {
			System.out.println("ERROR: trying to get card that doesn't exist! (top)" + curCardNum);
			System.exit(1);
		}
		
		if(isHorizontallyRelected(i, j) == false) {
			return curCardNum - PaperFolderModel.WIDTH;
		} else {
			return curCardNum + PaperFolderModel.WIDTH;
		}
	}

	
	public static boolean hasFoldRightFinal(int i, int j) {
		if(isVerticallyReflected(i, j) == false && hasCardRight(i, j) ) {
			return true;
		} else if(isVerticallyReflected(i, j) && hasCardLeft(i, j)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean hasFoldLeftFinal(int i, int j) {
		if(isVerticallyReflected(i, j) == false && hasCardLeft(i, j) ) {
			return true;
		} else if(isVerticallyReflected(i, j) && hasCardRight(i, j)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean hasFoldBottomFinal(int i, int j) {
		if(isHorizontallyRelected(i, j) == false && hasCardBelow(i, j) ) {
			return true;
		} else if(isHorizontallyRelected(i, j) && hasCardAbove(i, j)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean hasFoldTopFinal(int i, int j) {
		if(isHorizontallyRelected(i, j) == false && hasCardAbove(i, j) ) {
			return true;
		} else if(isHorizontallyRelected(i, j) && hasCardBelow(i, j)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isHorizontallyRelected(int i, int j) {
		return i%2 != 0;
	}
	
	public static boolean isVerticallyReflected(int i, int j) {
		return j%2 != 0;
	}
	
	public static boolean hasCardAbove(int i, int j) {
		return i>0;
	}
	

	public static boolean hasCardBelow(int i, int j) {
		return i+1 < PaperFolderModel.HEIGHT;
	}
	

	public static boolean hasCardLeft(int i, int j) {
		return j>0;
	}
	

	public static boolean hasCardRight(int i, int j) {
		return j+1 < PaperFolderModel.WIDTH;
	}
}
