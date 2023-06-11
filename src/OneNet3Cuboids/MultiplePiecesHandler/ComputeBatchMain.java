package OneNet3Cuboids.MultiplePiecesHandler;

import java.math.BigInteger;

import OneNet3Cuboids.CuboidToFoldOn;
import OneNet3Cuboids.Coord.Coord;
import OneNet3Cuboids.DupRemover.BasicUniqueCheckImproved;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;

public class ComputeBatchMain {

	/*
	 * 
13x1x1 and 3x3x3 where max depth is 13:
Num pieces found: 2083716
Final num pieces:
2083716

secret key: 122572
Exp: 17
Mod: 2083723
	 */
	
	//How to test it:
	//Test Works:
	//public static int BATCH_SIZE = 20000;
	//public static int START_DEPTH = 6;
	
	//Test Works:
	//public static int BATCH_SIZE = 29270;
	//public static int START_DEPTH = 8;
	
	//What to run:
	//public static int BATCH_SIZE = 4000;
	//public static int START_DEPTH = 13;
	
	public static int BATCH_SIZE = -1;
	public static int START_DEPTH = -1;
	public static int BATCH_INDEX_TO_USE = -1;
	
	public static int GET_ALL_PIECES_INDEX = -1;
	
	
	public static void main(String[] args) {
		
	 	CuboidToFoldOn cuboids[] = parseConfigFileAndSetOutputFile();
	 	CuboidToFoldOn cuboid1 = cuboids[0];
	 	CuboidToFoldOn cuboid2 = cuboids[1];
	 	
		long numPieces = getNumPieces(START_DEPTH, cuboid1, cuboid2);

		System.out.println("Final num pieces:");
		System.out.println(numPieces);

		System.out.println();
		System.out.println("Setup shuffle for pseudo-random sampling of tasks:");
		BigInteger shuffleParams[] = getShuffleNumbers(numPieces);
		
		BigInteger exp = shuffleParams[0];
		BigInteger mod = shuffleParams[1];

		System.out.println("Exp: " + exp);
		System.out.println("Mod: " + mod);
		System.out.println();

		for(int i=0; i<BATCH_SIZE; i++) {
			long indexBeforeTranslation = BATCH_INDEX_TO_USE * BATCH_SIZE + i;
			
			int indexAfterTranslation = getAPowerPmodMOD(new BigInteger("" + indexBeforeTranslation), exp, mod).intValue();
			
			if(indexAfterTranslation >= numPieces) {
				continue;
			}
			
			ComputeTaskMain.runSubtask(START_DEPTH, indexAfterTranslation, cuboid1, cuboid2);

			System.out.println("Index pre-shuffle to post-shuffle: " + indexBeforeTranslation + " to " + indexAfterTranslation);
			System.out.println("Done piece index: " + indexAfterTranslation);
			System.out.println("Num solutions found after " + (i+1) + " iterations: " + BasicUniqueCheckImproved.uniqList.size());
			System.out.println("-----");
			System.out.println("");
			System.out.println("");

		}
		
	}
	
	public static CuboidToFoldOn[] parseConfigFileAndSetOutputFile() {
		CuboidToFoldOn ret[] = new CuboidToFoldOn[2];
		
		 try (InputStream input = new FileInputStream("net_search.properties")) {
		
		        Properties prop = new Properties();
		
		        // load a properties file
		        prop.load(input);
		
		        String cuboid1String = prop.getProperty("cuboid1");
		        String cuboid2String = prop.getProperty("cuboid2");
	
		        String depthString = prop.getProperty("search_start_depth");
		        String batchSizeString = prop.getProperty("batch_size");
		        String indexToUseString = prop.getProperty("batch_index_to_search");
		        
		        Coord cuboid1Coord = parseCuboidConfig(cuboid1String);
		        Coord cuboid2Coord = parseCuboidConfig(cuboid2String);
		        
		        Coord cuboidCoordsRearranged[] = centerCuboidAroundNx1x1CuboidIfPossible(cuboid1Coord, cuboid2Coord);
	
		        cuboid1Coord = cuboidCoordsRearranged[0];
		        cuboid2Coord = cuboidCoordsRearranged[1];
		        ret[0] = new CuboidToFoldOn(cuboid1Coord.a, cuboid1Coord.b, cuboid1Coord.c);
		        ret[1] = new CuboidToFoldOn(cuboid2Coord.a, cuboid2Coord.b, cuboid2Coord.c);
		        
		        
		        if(! isNumber(batchSizeString)|| ! isNumber(indexToUseString) || ! isNumber(depthString)) {
		        	System.out.println("ERROR: one of the settings is not a number: batch_size, batch_index_to_search, and/or search_start_depth");
		        }
		        START_DEPTH = Integer.parseInt(depthString);
		        BATCH_SIZE = Integer.parseInt(batchSizeString);
		        BATCH_INDEX_TO_USE = Integer.parseInt(indexToUseString);
		        
		        
		        String cuboid1StringRearranged = getCuboidDimensionsString(ret[0]);
		        String cuboid2StringRearranged = getCuboidDimensionsString(ret[1]);
		        
		        PrintStream o;
		        
		        String filenameString = "net_search_" + cuboid1StringRearranged + "_and_" + cuboid2StringRearranged + "_SD_" + START_DEPTH + "_BS_" + batchSizeString + "_IND_"+ indexToUseString +".txt";
		        String path = filenameString;
		        
		        if(prop.getProperty("output_folder") == null) {
		        	path = "net_search_output" + File.separator + filenameString;
		        	o = new PrintStream(new File(path));
			        
		        } else {
		        	String prefix = prop.getProperty("output_folder");
		        	
		        	while(prefix.startsWith("\"") && prefix.endsWith("\"")) {
		        		prefix = prefix.substring(1, prefix.length() - 1);
		        	}
		        	
		        	if(! prefix.endsWith(File.separator)) {
		        		prefix = prefix + File.separator;
		        	}
		        	
		        	path = prefix + filenameString;
		        	o = new PrintStream(new File(path));
		
		        }
		        
	        	System.out.println("The program will output to this path:\n" + path);
		        System.setOut(o);
		        
		        System.out.println("Cuboids to compare:");
		        System.out.println(cuboid1Coord.a + "x" + cuboid1Coord.b + "x" + cuboid1Coord.c);
		        System.out.println("and");
		        System.out.println(cuboid2Coord.a + "x" + cuboid2Coord.b + "x" + cuboid2Coord.c);
		 
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
		 
		 	return ret;
	}
	
	private static String getCuboidDimensionsString(CuboidToFoldOn cuboid) {
		
		return cuboid.getDimensions()[0] + "x" + cuboid.getDimensions()[1] + "x" + cuboid.getDimensions()[2];
	}
	
	//Shuffling the indexes based on RSA encryption:
	// This allows me to sample the search space in a way that isn't random, but can probably 
	// be treated as random enough for me to extrapolate the time it will take and how many solutions there are.

	public static BigInteger[] getShuffleNumbers(long numPieces) {
		
		long mod = numPieces;
		
		int defaultExp = 17;

		int carMichaelTotient = -1;
		
		for(; true; mod++) {
			
			if(mod % 2 == 1
					&& getPrimeDivisors(mod).length == 2) {
				

				long primes[] = getPrimeDivisors(mod);
				
				if(primes[0] * primes[1] < mod) {
					continue;
				}
				
				carMichaelTotient = (int)getLCM(primes[0] - 1, primes[1] - 1);
				
				if(carMichaelTotient % defaultExp != 0) {
					break;
				}
			}
		}
		
		
		int d = 1;
		//Compute the inverse the slow way:
		while( (d * defaultExp) % carMichaelTotient != 1) {
			d++;
		}
		
		System.out.println(defaultExp);
		System.out.println(mod);
		System.out.println("secret key: " + d);
		
		sanityTestShuffle(new BigInteger("" + defaultExp), new BigInteger("" + d), new BigInteger("" + mod));
		
		return new BigInteger[] {new BigInteger("" + defaultExp), new BigInteger("" + mod)};
	}
	

	public static void sanityTestShuffle(BigInteger exp, BigInteger key, BigInteger mod) {
		//BigInteger exp = new BigInteger("" + 1);
		//BigInteger key = new BigInteger("" + 13);
		//int modInt= 21;
		
		System.out.println("Sanity testing RSA shuffle:");

		int modInt = mod.intValue();
		
		for(int i=0; i<modInt; i++) {
			
			BigInteger iBig = new BigInteger("" + i);
			BigInteger tmp = ComputeBatchMain.getAPowerPmodMOD(iBig, exp, mod);
			
			BigInteger maybeI = ComputeBatchMain.getAPowerPmodMOD(tmp, key, mod);
			
			if(iBig.compareTo(maybeI) != 0) {
				System.out.println("Error: There's a problem with the RSA shuffle for i = " + i);
				System.out.println(i + " encrypted and decrypted to " + maybeI);
				
				System.exit(1);
			}
			
		}
		
		System.out.println("Done sanity test for e = " + exp + ", d = " + key + ", and n = " + modInt);
	}
	
	public static long getLCM(long a, long b) {
		return (a*b)/getGCD(a, b);
	}
	
	public static long getGCD(long a, long b) {
		if(b>a) {
			return getGCD(b, a);
		}
		
		long ret = a % b;
		
		if(ret == 0) {
			return b;
		} else{
			return getGCD(b, ret);
		}
	}
	
	public static long getNumPieces(int startDepth, CuboidToFoldOn cuboid1, CuboidToFoldOn cuboid2) {
		

		ComputeTaskMain.updateComputeTask(startDepth, GET_ALL_PIECES_INDEX, cuboid1, cuboid2);
		
		if(ComputeTaskMain.computeTask == null) {
			System.out.println("Target index too high.");
			System.out.println("Num pieces found: " + CuboidComputeTaskGetter.curNumPiecesCreated);
		}
		
		
		
		return CuboidComputeTaskGetter.curNumPiecesCreated;
	}
	
	
	public static BigInteger getAPowerPmodMOD(BigInteger a, BigInteger pow, BigInteger MOD) {
		//base case for power:
		if(pow.compareTo(BigInteger.ZERO) == 0) {
			if(a.compareTo(BigInteger.ZERO) == 0 ) {
				System.out.println("0^0 is I don't know!!!");
				System.exit(1);
			} else if(pow.compareTo(BigInteger.ZERO) < 0 ) {
				System.out.println("No negative powers!" +  a + " to the power of " + pow + "?" );
				System.exit(1);
			}
			return BigInteger.ONE;
		} else if(a.compareTo(BigInteger.ZERO) == 0) {
			return BigInteger.ZERO;
		}
		
		//System.out.println(a + " to the power of " + pow);
		
		int lengthPowTable = 0;
		BigInteger current = BigInteger.ONE;
		while(current.compareTo(pow) <= 0) {
			lengthPowTable++;
			current = current.multiply(new BigInteger("2"));
		}
		
		//System.out.println("Length: " + lengthPowTable);
		
		//Setup the power of 2 table
		BigInteger pow2Table[] = new BigInteger[lengthPowTable];
		pow2Table[0] = a;
		
		
		for(int i=1; i<lengthPowTable; i++) {
			pow2Table[i] = (pow2Table[i-1].multiply(pow2Table[i-1])).mod(MOD);
		}
		//End setup the power of 2 table.
	
		current = pow;
		BigInteger answer = BigInteger.ONE;
		
		
		for(int i=0; i<lengthPowTable && current.compareTo(BigInteger.ZERO) > 0; i++) {
			if(current.mod(new BigInteger("2")).compareTo(BigInteger.ONE) == 0) {
				answer = (answer.multiply(pow2Table[i])).mod(MOD);
				current = current.subtract(BigInteger.ONE);
			}
			current = current.divide(new BigInteger("2"));
		}
		
		return answer;
	}
	


	public static long[] getPrimeDivisors(long n) {
		long nTemp = n;
		long LIMIT = (long)Math.sqrt(n);
		
		int numPrimeDivisors = 0;
		for(long i=2; i<=LIMIT; i++) {
			if(nTemp % i == 0 && isPrime(i)) {
				numPrimeDivisors++;
				while(nTemp % i == 0) {
					nTemp /= i;
				}
			}
		}
		if(nTemp > 1) {
			numPrimeDivisors++;
		}
		
		long divisors[] = new long[numPrimeDivisors];
		
		nTemp = n;
		int currentIndex = 0;
		for(long i=1; i<=LIMIT; i++) {
			if(nTemp % i == 0 && isPrime(i)) {
				divisors[currentIndex] = i;
				currentIndex++;
				while(nTemp % i == 0) {
					nTemp /= i;
				}
			}
		}
		if(nTemp > 1) {
			divisors[currentIndex] = nTemp;
			currentIndex++;
		}
		
		
		return divisors;
	}
	
	public static boolean isPrime(long num) {
		if(num<=1) {
			return false;
		}
		
		int sqrt = (int)Math.sqrt(num);
		for(int i=2; i<=sqrt ; i++) {
			if(num%i == 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public static boolean isNumber(String val) {
		try {
			int a = Integer.parseInt(val);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	public static boolean isLong(String val) {
		try {
			long a = Long.parseLong(val);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	public static Coord parseCuboidConfig(String cuboidDesc) {
		
		Pattern p = Pattern.compile("[^\\d]*(\\d+)[^\\d]+(\\d+)[^\\d]+(\\d+)[^\\d]*");
		int array[] = new int[3];
		
	    Matcher m = p.matcher(cuboidDesc);
	    
	    if (m.matches()) {
			array[0] = Integer.parseInt(m.group(1));
			array[1] = Integer.parseInt(m.group(2));
			array[2] = Integer.parseInt(m.group(3));
	    } else {
	    	System.out.println("** Bad input. Cuboid description should be 3 numbers on one line, separated by space");
	    	System.exit(1);
	    }
		
		
		return new Coord(array[0], array[1], array[2]);
	}
	
	
	public static Coord[] centerCuboidAroundNx1x1CuboidIfPossible(Coord cuboid1, Coord cuboid2) {
		
		//Swap the cuboids to make Nx1x1 cuboid first:
		if(isNx1x1Cuboid(cuboid2) && !isNx1x1Cuboid(cuboid1)) {
			Coord tmp = cuboid1;
			cuboid1 = cuboid2;
			cuboid2 = tmp;
		}
		
		
		
		//Make sure the Nx1x1 cuboid starts with N
		if(isNx1x1Cuboid(cuboid1) && !isNx1x1Cuboid(cuboid2)) {
			if(cuboid1.b > 1) {
				cuboid1.a = cuboid1.b;
			} else if(cuboid1.c > 1) {
				cuboid1.a = cuboid1.c;
			}
			cuboid1.b = 1;
			cuboid1.c = 1;
		}
		
		//Make sure the 1st Nx1x1 cuboid starts with N: (Special case when both cuboids are Nx1x1)
		if(isNx1x1Cuboid(cuboid1) && isNx1x1Cuboid(cuboid2)) {
			
			if(cuboid1.a == cuboid2.a && cuboid1.b == cuboid2.b && cuboid1.c == cuboid2.c) {
				//If both cuboids have same dimensions, just make both cuboids start with N:
				if(cuboid1.b > 1) {
					cuboid1.a = cuboid1.b;
					cuboid2.a = cuboid2.b;
					
				} else if(cuboid1.c > 1) {
					cuboid1.a = cuboid1.c;
					cuboid2.a = cuboid2.c;
				}
				cuboid1.b = 1;
				cuboid1.c = 1;
				cuboid2.b = 1;
				cuboid2.c = 1;
				
			} else if(cuboid1.a == 1) {
				//Make sure the first cuboid start with N:
				if(cuboid2.a != 1) {
					//Swap cuboid 1 and 2:
					Coord tmp = cuboid1;
					cuboid1 = cuboid2;
					cuboid2 = tmp;
				} else {
					//Make the first cuboid start with Nx1x1
					if(cuboid1.b > 1) {
						cuboid1.a = cuboid1.b;
					} else if(cuboid1.c > 1) {
						cuboid1.a = cuboid1.c;
					}
					cuboid1.b = 1;
					cuboid1.c = 1;
				}
			}
		}
		
		return new Coord[] {cuboid1, cuboid2};
	}
	
	
	private static boolean isNx1x1Cuboid(Coord cuboid) {
		
		int numOnes = 0;
		
		if(cuboid.a == 1) {
			numOnes++;
		}

		if(cuboid.b == 1) {
			numOnes++;
		}
		if(cuboid.c == 1) {
			numOnes++;
		}
		
		if(numOnes == 2) {
			return true;
		} else {
			return false;
		}
	}
	
}
