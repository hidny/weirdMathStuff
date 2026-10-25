package random_studying;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GetAvgDisplacement {
	public static void main(String[] args) throws Exception {

		String[] toks = new String(Files.readAllBytes(Paths.get(args[0]))).trim().split(";");
        
        System.out.println("Hello world!");
        for(int i=0; i<toks.length; i++) {
        	System.out.print(toks[i]);
        }
        System.out.println();
        //System.exit(1);
        
        int n = toks.length, minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
        
        int[][] c = new int[n][2];
        
        for (int k = 0; k < n; k++) { String[] p = toks[k].split(","); c[k][0] = Integer.parseInt(p[0].trim()); c[k][1] = Integer.parseInt(p[1].trim());
            minx = Math.min(minx, c[k][0]); maxx = Math.max(maxx, c[k][0]); miny = Math.min(miny, c[k][1]); maxy = Math.max(maxy, c[k][1]); }
        
        int H = maxx - minx, W = maxy - miny;
        
        boolean[][] grid = new boolean[H + 1][W + 1];
        
        for (int k = 0; k < n; k++) grid[c[k][0] - minx][c[k][1] - miny] = true;
        
        System.out.println("net with " + n + " cells");
        
        for(int i=0; i<grid.length; i++) {
        	for(int j=0; j<grid[0].length; j++) {
        		if(grid[i][j]) {
        			System.out.print("#");
        		} else {
        			System.out.print("_");
        		}
        	}
        	System.out.println();
        }
        
        System.out.println("Width: " + W);
        System.out.println("Height: " + H);
        
        String grid2Str = "(0, 0), (0, −1), (−2, −1), (−2, −3), (−4, −3), (−4, −1), (−6, −1), (−6, 0)";
        
        grid2Str = grid2Str.replace("(", "").replace(")", "").replace("−", "");
        System.out.println(grid2Str);
        
        boolean table[][] = new boolean[4][7];
        
        String tokens[] = grid2Str.split(",");
        
        for(int i=0; i<tokens.length; i+=2) {

        	System.out.println(tokens[i].trim());
        	System.out.println(tokens[i + 1].trim());
        	
        	
        	int jCoord = Integer.parseInt(tokens[i].trim());
        	int iCoord = Integer.parseInt(tokens[i + 1].trim());
        	
        	table[iCoord][jCoord] = true;
        	System.out.println();
        	
        }
        
        
        for(int i=0; i<table.length; i++) {
        	for(int j=0; j<table[0].length; j++) {
        		if(table[i][j]) {
        			System.out.print("#");
        		} else {
        			System.out.print("_");
        		}
        	}
        	System.out.println();
        }
        
	}
}
