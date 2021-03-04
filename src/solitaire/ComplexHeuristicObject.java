package solitaire;

import java.util.HashMap;
import java.util.HashSet;

public class ComplexHeuristicObject {

	public ComplexHeuristicObject(int[] numDone, int numMoves, String lines[][]) {
		this.numDone = numDone;
		this.numMoves = numMoves;
		this.lines = lines;
	}

	public String[][] getLines() {
		return lines;
	}

	public int[] getNumDone() {
		return numDone;
	}


	public int getNumMoves() {
		return numMoves;
	}

	private int numDone[];
	private int numMoves;
	private String lines[][];
	
	public boolean matches(ComplexHeuristicObject other) {
		
		for(int i=0; i<this.numDone.length; i++) {
			if(this.numDone[i] != other.numDone[i]) {
				return false;
			}
		}
		
		return true;
	}
	
	public String toString() {
		String ret = "";
		
		for(int i=0; i<numDone.length; i++) {
			ret += numDone[i] + "/";
		}
		return ret;
	}
	

	public int hashCode() {
		//System.out.println("GETTING HASHCODE");
		return this.toString().hashCode();
	}
		
	
	public boolean  equals (Object object) {
		if(this.toString().equals(object.toString())) {
			return true;
		} else {
			return false;
		}
	}


	
}
