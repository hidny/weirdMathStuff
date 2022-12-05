package OneNet3Cuboids;

public class NeighbourGraphCreator {

	
	public static int NUM_NEIGHBOURS =4;

	public static CoordWithRotation[][] initNeighbourhood(int a, int b, int c, int numbering[][][], Coord numberingInv[]) {
		String ret1 = DataModelViews.getFlatNumberingView(a, b, c, numbering);
		System.out.println(ret1);

		System.out.println("Printed flattened cuboid with bonus square at bottom...");
		
		System.out.println("This is the map I will use to encode neighbours and get somewhere... hopefully.");
		
		int flatArray[][] = makeTempFlatMapArray(a, b, c, numbering);
		
		//Sanity check:
		String ret2 = sanityGetFlatNumberingFromTempFlatArray(a, b, c, flatArray);
		if(ret1.equals(ret2)) {
		} else {
			System.out.println("sanityGetFlatNumberingFromTempFlatArray check failed!");
			System.exit(1);
		}
		//End sanity check
		

		CoordWithRotation[][] neighboursTranspose = new CoordWithRotation[4][];

		neighboursTranspose[0] = handleAboveNeighbours(a, b, c, flatArray, numberingInv);
		neighboursTranspose[1] = handleRightNeighbours(a, b, c, flatArray, numberingInv);
		neighboursTranspose[2] = handleBelowNeighbours(a, b, c, flatArray, numberingInv);
		neighboursTranspose[3] = handleLeftNeighbours(a, b, c, flatArray, numberingInv);
		
		CoordWithRotation neighbours[][] = new CoordWithRotation[CuboidToFoldOn.getTotalArea(a, b, c)][NUM_NEIGHBOURS];
		
		for(int i=0; i<neighbours.length; i++) {
			for(int j=0; j<NUM_NEIGHBOURS; j++) {
				neighbours[i][j]  = neighboursTranspose[j][i];
			}
		}
		
		for(int i=0; i<neighbours.length; i++) {
			
			System.out.println("Neighbours for " + i + ":");
			for(int j=0; j<NUM_NEIGHBOURS; j++) {

				
				int numNeighbour = numbering[neighbours[i][j].getA()][neighbours[i][j].getB()][neighbours[i][j].getC()];

				if(neighbours[i][j].getRot() == 0) {
					System.out.println(numNeighbour);
					
				} else if(neighbours[i][j].getRot() == 1) {
					System.out.println(numNeighbour + " with 90 clockwise");
					
				} else if(neighbours[i][j].getRot() == 2) {
					System.out.println(numNeighbour + " with 180 rotation");
					
				} else if(neighbours[i][j].getRot() == 3) {
					System.out.println(numNeighbour + " with 90 counter");
					
				}
				
			}
			System.out.println();
		}
		
		return neighbours;
	}
	
	
	public static CoordWithRotation[] handleAboveNeighbours(int a, int b, int c, int flatArray[][], Coord numberingInv[]) {
		
		//Only do easy ones for now:
		int size = CuboidToFoldOn.getTotalArea(a, b, c);
		CoordWithRotation ret[] = new CoordWithRotation[size];
		

		//System.out.println("Handle above neighbours:");
		
		//Above:
		for(int i=0; i<flatArray.length; i++) {
			for(int j=0; j<flatArray[0].length; j++) {

				int curIndex = flatArray[i][j];
				
				if(curIndex < 0) {
					continue;
				}
				
				if(i == 0) {

					//Weird opposite case:
					//With 180 rotation
					if(flatArray[flatArray.length - 1][j] >=0) {
						int tmp = flatArray[flatArray.length - 1][j];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 2);
						//System.out.println(tmp + " is above " + curIndex + ". (with 180 rotation)");

					
					}
					
				} else if(i>0 && i<2*c + a) {
					
					
					if(flatArray[i-1][j] >=0) {
						//No rotation (normal case)
						int tmp = flatArray[i-1][j];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 0);

						//System.out.println(tmp + " is above " + curIndex + ".");
						
						
					} else if(i == c) {
						//Diagonal tricks:
						if(j < c) {
							for(int d=1; d<=c; d++) {
								if(flatArray[i-d][j+d] >=0) {
									int tmp = flatArray[i-d][j+d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 1);
									
									//System.out.println(tmp + " is above " + curIndex + ". (with 90 clockwise rotation)");

									break;
								}
							}
						} else if(j >= c+b && j< 2*c + b) {
							for(int d=1; d<=c; d++) {
								if(flatArray[i-d][j-d] >=0) {
									
									int tmp = flatArray[i-d][j-d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 3);
									
									//System.out.println(tmp + " is above " + curIndex + ". (with 90 counter-clockwise rotation)");

									break;
								}
							}
						}
					}
					
					
				} else if(i == 2*c + 2*a - 1) {

					//Weird opposite case:
					//With 180 rotation
					
					int tmp = flatArray[0][j];
					
					ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 2);
					//System.out.println(tmp + " is above " + curIndex + ". (with 180 rotation)");
					
					
					
					
				}
			}
		}
		return ret;
		
	}
	
	//4th row is more trouble than it's worth
	//Scrap it? Nah! Just be careful!
	
	public static CoordWithRotation[] handleBelowNeighbours(int a, int b, int c, int flatArray[][], Coord numberingInv[]) {
		
		//Only do easy ones for now:
		int size = 2*(a*b + a*c + b*c);
		CoordWithRotation ret[] = new CoordWithRotation[size];
		
		//System.out.println("Handle below neighbours:");
		
		for(int i=0; i<flatArray.length; i++) {
			for(int j=0; j<flatArray[0].length; j++) {

				int curIndex = flatArray[i][j];
				

				if(curIndex < 0) {
					continue;
				}
				
				if(i < c + a + c - 1) {
					
					if(flatArray[i+1][j] >=0) {
						//No rotation: (Normal case)
						int tmp = flatArray[i+1][j];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 0);
						
						//System.out.println(tmp + " is below " + curIndex + ".");
						
						
					} else if(i == c + a - 1 && j < 2*c + b) {
						
						//Diagonal tricks:
						if(j < c) {
							for(int d=1; d<=c; d++) {
								if(flatArray[i+d][j+d] >=0) {
									int tmp = flatArray[i+d][j+d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 3);
									
									//System.out.println(tmp + " is below " + curIndex + ". (with 90 counter-clockwise rotation)");
									
									break;
								}
							}
						} else if(j >= c+b && j< 2*c + b) {
							for(int d=1; d<=c; d++) {
								if(flatArray[i+d][j-d] >=0) {
									
									int tmp = flatArray[i+d][j-d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 1);
									
									//System.out.println(tmp + " is below " + curIndex + ". (with 90 clockwise rotation)");
									
									break;
								}
							}
						}
					}
		
				} else if(i == 2*c + a - 1) {
					
					//opposite case:
					if(flatArray[i+1][j] >=0) {
						//180 rotation:
						int tmp = flatArray[i+1][j];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 2);
						
						//System.out.println(tmp + " is below " + curIndex + ". (180 degree rotation)");
						
						
					}
					
				} else if(i == 2*c + a) {
					
					//Weird opposite case:
					if(flatArray[i-1][j] >=0) {
						//180 rotation:
						int tmp = flatArray[i-1][j];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 2);
						
						//System.out.println(tmp + " is below " + curIndex + ". (180 degree rotation)");
						
						
					}
				}
			}
		}
		
		return ret;
		
	}
	

	public static CoordWithRotation[] handleLeftNeighbours(int a, int b, int c, int flatArray[][], Coord numberingInv[]) {
		
		//Only do easy ones for now:
		int size = 2*(a*b + a*c + b*c);
		CoordWithRotation ret[] = new CoordWithRotation[size];
		
		//System.out.println("Handle left neighbours:");
		
		for(int i=0; i<flatArray.length; i++) {
			for(int j=0; j<flatArray[0].length; j++) {

				int curIndex = flatArray[i][j];
				

				if(curIndex < 0 || i >= 2*c + a) {
					continue;
				}


				//All the way to the left case:
				if(j == 0) {
					
					int tmp = flatArray[i][flatArray[0].length - 1];
					
					ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 0);
					
					//System.out.println(tmp + " is left of " + curIndex + ".");
					
				} else {
					
					//Normal case:
					if(flatArray[i][j-1] >=0) {
						//No rotation:
						int tmp = flatArray[i][j-1];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 0);
						
						//System.out.println(tmp + " is left of " + curIndex + ".");
						
					} else {

						//Diagonal tricks:
						if(i < c) {
							for(int d=1; d<=c; d++) {
								if(flatArray[i+d][j-d] >=0) {
									int tmp = flatArray[i+d][j-d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 3);
									
									//System.out.println(tmp + " is left of " + curIndex + ". (with 90 counter-clockwise rotation)");
									
									break;
								}
							}
						} else {
							for(int d=1; d<=c; d++) {
								if(flatArray[i-d][j-d] >=0) {
									
									int tmp = flatArray[i-d][j-d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 1);
									
									//System.out.println(tmp + " is left of " + curIndex + ". (with 90 clockwise rotation)");
									
									break;
								}
							}
						}
					}
				}
			}
		}
		
		return ret;
		
	}



	public static CoordWithRotation[] handleRightNeighbours(int a, int b, int c, int flatArray[][], Coord numberingInv[]) {
		
		//Only do easy ones for now:
		int size = 2*(a*b + a*c + b*c);
		CoordWithRotation ret[] = new CoordWithRotation[size];
		
		//System.out.println("Handle right neighbours:");
		
		for(int i=0; i<flatArray.length; i++) {
			for(int j=0; j<flatArray[0].length; j++) {

				int curIndex = flatArray[i][j];
				

				if(curIndex < 0 || i >= 2*c + a) {
					continue;
				}

				//All the way to the right case:
				if(j == flatArray[0].length - 1) {
					
					int tmp = flatArray[i][0];
					
					ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 0);
					
					//System.out.println(tmp + " is right of " + curIndex + ".");
					
				//Normal case:
				} else {

					//No rotation:
					if(flatArray[i][j+1] >=0) {
						int tmp = flatArray[i][j+1];
						ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 0);
						
						//System.out.println(tmp + " is right of " + curIndex + ".");
						
						
					} else {

						//Diagonal tricks:
						if(i < c) {
							for(int d=1; d<=c; d++) {
								if(flatArray[i+d][j+d] >=0) {
									int tmp = flatArray[i+d][j+d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 1);
									
									//System.out.println(tmp + " is right of " + curIndex + ". (with 90 clockwise rotation)");
									
									break;
								}
							}
						} else {
							for(int d=1; d<=c; d++) {
								if(flatArray[i-d][j+d] >=0) {
									
									int tmp = flatArray[i-d][j+d];
									ret[curIndex] = new CoordWithRotation(numberingInv[tmp], 3);
									
									//System.out.println(tmp + " is right of " + curIndex + ". (with 90 counter-clockwise rotation)");
									
									break;
								}
							}
						}
					}
				}
			}
		}
		
		return ret;
		
	}
	
	
	public static int[][] makeTempFlatMapArray(int a, int b, int c, int numbering[][][]) {
		
		int ret2[][] = new int[2*a + 2*c][2*b + 2*c];
		for(int i=0; i<ret2.length; i++) {
			for(int j=0; j<ret2[0].length; j++) {
				ret2[i][j] = -1;
			}
		}
		
		String ret = "";
		
		for(int i=0; i<c; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {

				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(j >= c && j<c + b) {

					
					int num = numbering[0][i][j - c];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					ret2[i][j] = num;
					
				} else {
					ret += "    ";
				}
			}
			
			ret += "\n";
		}
		
		//2nd row has 4 sides:
		ret += "\n";

		for(int i=0; i<a; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {
				
				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				if( j < c) {
					
					int num = numbering[1][i][j];

					ret += " " + "  ".substring(((num) + "").length()) + num + " ";

					ret2[c + i][j] = num;
					
				} else if(j >= c && j<c + b) {
					
					int num = numbering[2][i][j - c];
					
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
					ret2[c + i][j] = num;
					
				} else if(j >= c + b && j<2*c + b) {
					
					int num = numbering[3][i][j - c - b];
					
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
					ret2[c + i][j] = num;
					
				} else if(j >= 2*c + b && j<2*c + 2*b) {
					
					int num = numbering[4][i][j - 2*c - b];
					
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";

					ret2[c + i][j] = num;
				} else {
					System.out.println("Doh!");
					System.exit(1);
				}
			}
			
			ret += "\n";
		}
		

		//3rd row has 1 side:
		ret += "\n";
		
		for(int i=0; i<c; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {

				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(j >= c && j<c + b) {

					int num = numbering[5][i][j - c];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					

					ret2[c + a + i][j] = num;
					
				} else {
					ret += "    ";
				}
			}
			
			ret += "\n";
		}
		
		//Bonus 4th row:
		ret += "\n";
		
		for(int i=0; i<a; i++) {
			
			for(int j=0; j < 2*b + 2 *c; j++) {

				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(j >= c && j<c + b) {

					//Mind bender: It gets flipped!
					// because it's a 3D cuboid or something!
					int num = numbering[4][(a-1) - (i)][(b-1) - (j - c)];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";

					ret2[2*c + a + i][j] = num;
					
				} else {
					ret += "    ";
				}
			}
			
			ret += "\n";
		}
		
		return ret2;
	}
	

	public static String sanityGetFlatNumberingFromTempFlatArray(int a, int b, int c, int flatArray[][]) {
		
		String ret ="";
		
		ret += "Map:\n";
		for(int i=0; i<flatArray.length; i++) {
			

			if( i == c || i == c + a || i == 2*c + a || i== 2*c + 2*a) {
				ret += "\n";
			}
			
			for(int j=0; j<flatArray[0].length; j++) {
				
				if( j == c || j == c + b || j == 2*c + b || j== 2*c + 2*b) {
					ret += "    ";
				}
				
				if(flatArray[i][j] >= 0) {

					int num = flatArray[i][j];
					ret += " " + "  ".substring(((num) + "").length()) + num + " ";
					
				} else {
					ret += "    ";
				}
			}

			ret += "\n";
			
		}
		
		return ret;
	}
	
	
}
