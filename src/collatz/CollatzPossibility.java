package collatz;
//TODO: do your collatz experiment!
//I thought about it:
///it won't work :(

import java.math.BigInteger;
import java.util.ArrayList;

public class CollatzPossibility {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CollatzPossibility temp = new CollatzPossibility();
	}

	
	//false means go down
	//true means go up.
	private ArrayList<Boolean> trail = new ArrayList<Boolean>();
	
	//power we are moding
	private BigInteger mod = new BigInteger("2");
	
	//start of possible cycle
	private BigInteger start = new BigInteger("1");
	
	//end of possible cycle
	private BigInteger end = new BigInteger("1");
	
	private double logRatioEndtoStart = 0.0;
	
	public static double UP = Math.log(3.0)+ 0.0000001;
	private static double DOWN = Math.log(2.0);
	
	
	//TODO: turn a line 2^a into multiple possible line 2^a+1
	
	
	public CollatzPossibility() {
		//start with 1 mod 2 going to 0 mod 2 then back to 1 mod 2:
		
		this.trail.add(true);
		this.trail.add(false);
		
		this.logRatioEndtoStart += UP - DOWN;
		
		System.out.println(this);
	}
	
	public String toString() {
		String ret = "";
		ret += start + " mod " + mod + "\n";
		ret +=  "goes to " + "\n";
		ret +=  end + " mod " + mod + "\n";
		ret += "After " + trail.size() + " movements." + "\n";;
		
		ret += "It goes up by approx: " + Math.exp(logRatioEndtoStart) + "\n" + "\n";
		
		return ret;
	}
	
	
	//TODO: figure out how to implement:
	public static ArrayList<CollatzPossibility> getPossibilityNextPower(CollatzPossibility possibility) {
		return null;
	}
}
