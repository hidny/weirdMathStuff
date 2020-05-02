package tests;

import java.math.BigInteger;
import java.util.ArrayList;

//https://oeis.org/A163574/internal
//search 559922224824157 on google to get relevant results
public class PolydivisableNumbersBaseN {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(convertStringArrayToBase10(new int[] {1, 3}, 10) + " = 13");

		System.out.println(convertStringArrayToBase10(new int[] {1, 3}, 16) + " = 19");
		
		for(int base = 34; base < 100; base = base +2) {
			System.out.println("Base " + base + ":");
			getPolydivisableNumbersBaseN(base);
			System.out.println();
		}
	}
	
	//Find PolydivisableNumbers that use each digit except 0 only once:
	
	public static int list[];
	public static boolean used[];
	
	public static ArrayList<String> getPolydivisableNumbersBaseN(int base) {
		list = new int[base - 1];
		//used is one based.
		used = new boolean[base];
		//used[1] for 1
		//...
		//used[9] for 9.
		for(int i=0; i<base - 1; i++) {
			list[i] = 0;
			used[i] = false;
		}
		used[base - 1] = false;
		used[0] = true;
		
		return guessNumber(base);
	}
	
	public static ArrayList<String> guessNumber(int base) {
		return guessNumber(base, 0);
	}

	public static ArrayList<String> guessNumber(int base, int indexToAdd) {
		ArrayList<String> ret = new ArrayList<String>();
		int targetLengthNumber = base -1;
		if(indexToAdd >= targetLengthNumber) {
			System.out.println("ERROR: index to add is too big!");
			System.exit(1);
		}
		
		for(int i=1; i < base; i++) {
			if(used[i] == false) {
				used[i] = true;
				list[indexToAdd] = i;
				
				if( convertBigIntegerArrayToBase10(list, base, indexToAdd + 1).divideAndRemainder(new BigInteger("" + (indexToAdd + 1)))[1].compareTo(BigInteger.ZERO) == 0) {
					indexToAdd++;
					if(indexToAdd == targetLengthNumber) {
						System.out.println(PrintNumber(list, base));
						
						ret.add(PrintNumber(list, base));
					} else {
						guessNumber(base, indexToAdd);
					}
					
					indexToAdd--;
				}
				
				used[i] = false;
				
				
			}
		}
		//stub

		return ret;
	}

	
	public static String PrintNumber(int list[], int base) {
		return convertStringArrayToCommaDelimited(convertListToStringArray(list), base) + " = " + convertStringArrayToBase10(list, base);
	}
	
	public static String[] convertListToStringArray(int list[]) {
		String ret[] = new String[list.length];
		for(int i=0; i<list.length; i++) {
			ret[i] = "" + list[i];
		}
		return ret;
	}
	
	public static String convertStringArrayToCommaDelimited(String list[], int base) {
		String ret = "";
		for(int i=0; i<list.length; i++) {
			ret += list[i] + ", ";
			if(Integer.parseInt(list[i]) > base) {
				System.out.println("ERROR: digit is too damn high!");
				System.exit(1);
			}
		}
		//take away the last ", ":
		ret = ret.substring(0, ret.length() - 2);
		//Write down the base.
		ret += "(base " + base + ")";
		return ret;
	}
	
	public static String convertStringArrayToBase10(int list[], int base) {
		return convertBigIntegerArrayToBase10(list, base, list.length).toString();
	}
	
	public static BigInteger convertBigIntegerArrayToBase10(int list[], int base, int length) {
		BigInteger mult = BigInteger.ONE;
		BigInteger ret = BigInteger.ZERO;
		
		for(int i=0; i<length; i++) {
			ret = ret.add(new BigInteger("" + list[length - 1 - i]).multiply(mult));
			mult = mult.multiply(new BigInteger("" + base));
		}
		return ret;
	}
}

/*
 * https://oeis.org/A163574/internal

  Search		Hints
(Greetings from The On-Line Encyclopedia of Integer Sequences!)
A163574		Decimal expansion of smallest zeroless pandigital number in base n such that each k-digit substring (1 <= k <= n-1 = number of base-n digits) starting from the left, is divisible by k (or 0 if none exists).		5
%I
%S 1,0,27,0,2285,0,874615,0,381654729,0,0,0,559922224824157,0,0,0,0,0,0,
%T 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
%N Decimal expansion of smallest zeroless pandigital number in base n such that each k-digit substring (1 <= k <= n-1 = number of base-n digits) starting from the left, is divisible by k (or 0 if none exists).
%C Sequence gives smallest term with desired property.
%C For n=2 and 10, there is only one such number.
%C For n=4, there are 2 solutions: 27 and 57, the latter 321(4).
%C For n=6, there are 2 solutions: 2285 and 7465, the latter 54321(6).
%C For n=8, there are 3 solutions: 874615, 1391089 and 1538257, these last two being 5234761(8) and 5674321(8).
%C There are no solutions for a number system of base n, if n is odd. For a solution the sum of the digits is always (n-1)*n/2. A solution is always divisible by n-1. This is only possible if the sum of the digits is divisible by n-1. As a consequence, n/2 has to be an integer and therefore n has to be even (translated from 2nd link from German by web page author, Werner Brefeld). - _Michel Marcus_, Dec 09 2013
%C Is it true that a(n) = 0 for n > 14? - _Chai Wah Wu_, Jun 07 2015
%H Blaine, <a href="http://puzzles.blainesville.com/2008/01/how-about-math-puzzle.html#comments">How about a math puzzle?</a>
%H Werner Brefeld, <a href="http://www.brefeld.homepage.t-online.de/neunstellig-1.html">Neunstellige Zahl und Teilbarkeit</a> (in German).
%H Albert Franck, <a href="http://www.paulcooijmans.com/others/puzfrank.html">Puzzles</a>, see item 7.
%F a(2n+1) = 0 (see proof in comment). - _Michel Marcus_, Dec 09 2013
%e a(3) = 0, since the 2 possible zeroless numbers, 12 and 21 in base 3, are both odd numbers, so do not satisfy the condition for k=2.
%e a(4) = 27, that is 123 in base 4, such that 1, 12, and 123 are respectively divisible by 1, 2 and 3.
%e Expansion of each term in the corresponding base : 27 = 123 (4); 2285 = 14325 (6); 874615 = 3254167 (8); 381654729 = 381654729 (10); 559922224824157 = 9C3A5476B812D (14).
%o (PARI) a(n) = {n--; for (j=0, n!-1, perm = numtoperm(n, j); ok = 1; for (i=1, n, v = sum(k=1, i, perm[k]*(n+1)^(i-k)); if ((v % i), ok=0; break;);); if (ok, return(v)););} \\ _Michel Marcus_, Dec 01 2013
%o (PARI) chka(n, b) = {digs = digits(n, b); for (i=1, #digs, v = sum(k=1, i, digs[k]*b^(i-k)); print(v, ": ", v/i); if (v % i, return (0));); return (1);} \\ _Michel Marcus_, Dec 02 2013
%o (PARI) okdigits(v, i) = {for (j=1, i-1, if (v[i] == v[j], return (0));); return (1);}
%o a(n) = {b = n; n--; v = vector(n, i, 0); i = 1; while (1, v[i]++; while (v[i] > n, v[i] = 0; i --; if (i==0, return (0)); v[i]++); curv = sum (j=1, i, v[j]*(b^(i-j))); if (! (curv % i), if (okdigits(v, i), if (i == n, return (sum (j=1, n, v[j]*(b^(n-j))))); i++;);););} \\ _Michel Marcus_, Dec 08 2013
%o (Python)
%o def vgen(n,b):
%o ....if n == 1:
%o ........t = list(range(1,b))
%o ........for i in range(1,b):
%o ............u = list(t)
%o ............u.remove(i)
%o ............yield i, u
%o ....else:
%o ........for d, v in vgen(n-1,b):
%o ............for g in v:
%o ................k = d*b+g
%o ................if not k % n:
%o ....................u = list(v)
%o ....................u.remove(g)
%o ....................yield k, u
%o def A163574(n):
%o ....for a, b in vgen(n-1,n):
%o ........return a
%o ....return 0 # _Chai Wah Wu_, Jun 07 2015
%K nonn,base,more
%O 2,3
%A _Gaurav Kumar_, Jul 31 2009
%E Corrected and edited by _Michel Marcus_, Dec 02 2013
%E More terms from _Michel Marcus_, Dec 09 2013
%E a(31)-a(41) from _Chai Wah Wu_, Jun 07 2015
*/

/* OUTPUT:
13 = 13
19 = 19
Base 2:
1(base 2) = 1

Base 3:

Base 4:
1, 2, 3(base 4) = 27
3, 2, 1(base 4) = 57

Base 5:

Base 6:
1, 4, 3, 2, 5(base 6) = 2285
5, 4, 3, 2, 1(base 6) = 7465

Base 7:

Base 8:
3, 2, 5, 4, 1, 6, 7(base 8) = 874615
5, 2, 3, 4, 7, 6, 1(base 8) = 1391089
5, 6, 7, 4, 3, 2, 1(base 8) = 1538257

Base 9:

Base 10:
3, 8, 1, 6, 5, 4, 7, 2, 9(base 10) = 381654729

Base 11:

Base 12:

Base 13:

Base 14:
9, 12, 3, 10, 5, 4, 7, 6, 11, 8, 1, 2, 13(base 14) = 559922224824157

Base 15:

Base 16:

Base 17:

Base 18:

Base 19:

Base 20:

Base 21:

Base 22:

Base 23:

Base 24:

Base 25:

Base 26:

Base 27:

Base 28:

Base 29:

Base 30:

Base 31:

Base 32:
(checked)

*/