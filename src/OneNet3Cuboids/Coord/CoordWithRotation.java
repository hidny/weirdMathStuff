package OneNet3Cuboids.Coord;

public class CoordWithRotation {

	private int a;
	private int b;
	private int c;
	
	//0: none
	//1: clockwise 90
	//2: opposite dir
	//3: counter-clockwise 90 
	private int rot;
	
	public CoordWithRotation(int a, int b, int c, int rot) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.rot = rot;
	}
	
	public CoordWithRotation(Coord c, int rot) {
		this.a = c.a;
		this.b = c.b;
		this.c = c.c;
		this.rot = rot;
	}

	public int getA() {
		return a;
	}

	public int getB() {
		return b;
	}

	public int getC() {
		return c;
	}

	public int getRot() {
		return rot;
	}
	
	
}
