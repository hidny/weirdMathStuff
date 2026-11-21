package poc_billiards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//From youtube Java GUI Tutorial - Make a GUI in 13 Minutes #99
//From https://introcs.cs.princeton.edu/java/15inout/GUI.java.html
public class DrawOnGui2 implements ActionListener {

	private JFrame frame;
	private JLabel label;
	private JPanel contentPane;
	private JPanel panelDrawing;
	
	private JButton buttonInc;
	private JButton buttonInc2;
	
	private JButton buttonDec;
	private JButton buttonDec2;
	
	private JButton buttonBeep;
	private int count = 0;
	
	

	private static ArrayList <Integer> topNotches = new ArrayList <Integer>();
	private static ArrayList <Integer> topTabs = new ArrayList <Integer>();
	private static ArrayList <Integer> bottomNotches = new ArrayList <Integer>();
	private static ArrayList <Integer> bottomTabs = new ArrayList <Integer>();
	
	static {
		
		
		// Only 1 solution...
		bottomNotches.add(0); 
		bottomNotches.add(150); // 1/3 (1/3 + 2/3 = 1)
		bottomNotches.add(225); // 1/2
		bottomNotches.add(375); // 5/6 (5/6 + 1/6 = 1)
		
		bottomTabs.add(75);  // 1/6
		bottomTabs.add(300); // 2/3
		
		
		topNotches.add(12);  // 12 + 450 + 12 = 474
		topNotches.add(147); // 147 + 327 = 474
		topNotches.add(192); // 192 + 282 = 474
		topNotches.add(237); // 2* 237 = 474
		topNotches.add(372); // 372 + 102 = 474
		topNotches.add(417); // 417 + 57 = 474
		
		topTabs.add(57);
		topTabs.add(102);
		topTabs.add(282);
		topTabs.add(327);
	}
	
	public DrawOnGui2() {
		frame = new JFrame();
		frame.setSize(1100, 800);
		
		buttonInc2 = new JButton("Increase N2 by 5");
		buttonInc2.addActionListener(this);
		buttonDec2 = new JButton("Decrease N2 by 5");
		buttonDec2.addActionListener(this);
		
		buttonBeep = new JButton("beep");
		buttonBeep.addActionListener(this);
		
		buttonInc = new JButton("Increase N by 5");
		buttonInc.addActionListener(this);
		buttonDec = new JButton("Decrease N by 5");
		buttonDec.addActionListener(this);
		
		label = new JLabel("Number of clicks: " + count);
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(5, 5));
		contentPane.add(buttonInc2);
		contentPane.add(buttonDec2);
		//contentPane.add(buttonBeep);
		contentPane.add(buttonInc);
		contentPane.add(buttonDec);
		contentPane.add(label);
		
		panelDrawing = new GPanel();
		panelDrawing.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
		//panel.setLayout(new GridLayout(0, 1));
		panelDrawing.setLayout(null);
		//panel.add(button);
		//panel.add(label);
		
		//TODO: please paint grid or whatever.

		frame.add(contentPane, BorderLayout.NORTH);
		frame.add(panelDrawing, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Visualization on Matrix");
		
		// Source - https://stackoverflow.com/a/1235994
		// Posted by camickr, modified by community. See post 'Timeline' for change history
		// Retrieved 2026-10-18, License - CC BY-SA 3.0

		
		//frame.pack();
		frame.setVisible(true);
		
	}
	
	//TODO: make slow version of these table creation algos, so you can sanity check them.
	

	//public static final int DIM_X = 450;
	//public static final int DIM_Y = 6;
	
	public static final int SAFE_MULT = 2;
	
	//Start config options:

	//private static int N_ADJUST = 0;
	//private static int N2_ADJUST = 0;
	//public static final int DIM_X = 6 * SAFE_MULT;
	//public static final int DIM_Y = 45 * SAFE_MULT;
	
	private static int N_ADJUST = 0;
	private static int N2_ADJUST = 0;
	//public static final int DIM_X =18 * SAFE_MULT;
	//public static final int DIM_Y =15 * SAFE_MULT;
	
	//private static int N_ADJUST = 0;
	//private static int N2_ADJUST = 0;
	//public static final int DIM_X =30 * SAFE_MULT;
	//public static final int DIM_Y = 9 * SAFE_MULT;
	

	//private static int N_ADJUST = 0;
	//private static int N2_ADJUST = 0;
	public static final int DIM_X = 90 * SAFE_MULT;
	public static final int DIM_Y =  3 * SAFE_MULT;
	
	//END start config options:

	public static final int MULT = Math.min(1000 / DIM_X, 500 / DIM_Y);
	
	public static final int SLOPE_INV = 2;
	
	public static final int USUAL_STRETCH = SLOPE_INV * DIM_Y;

	public static final int TOP_LEFT_X= 10;
	public static final int TOP_LEFT_Y = 10;
	
	private class GPanel extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			
			g.setColor(Color.YELLOW);
			g.fillRect(TOP_LEFT_Y, TOP_LEFT_X, MULT * DIM_X, MULT * DIM_Y);
			
			//Notches and tabs on top border:
			paintBilliard(g, false, true);
			
			//Notches and tabs on bottom border:
			paintBilliard(g, false, false);
			
			//I don't the 2nd line isn't used!
			//paintBilliard(g, true);

	}
	
		
	public static int DistTotouchingRibbon = 0;
	public static int curRibbonHeight = 0;
	
	public static int DistToAdjCorner = 0;
	public static int curRibbonHeight2 = 0;
	
	
	public static void paintBilliard(Graphics g, boolean startTopLeft, boolean tabsAndNotchAbove) {
		boolean ret[][] = null;

		
		
		//g.drawRect(10,10, 190, 190);
		//g.drawRect(startX, startY, DIM -1, DIM -1);
		//g.setColor(Color.BLUE);
		//g.fillOval(50, 50, 100, 100);

		// Set the drawing color

        // Draw a line from (50, 50) to (200, 150)
        //g.drawLine(startX, startY + DIM, startX+100, startY);

        int curX=0;
        int curY=0;
        

        boolean goRight = true;
        boolean goUp = true;
        
        if(startTopLeft) {
        	curY = DIM_Y;
        	goUp = false;
        }
        
        boolean firstIteration = true;
        
        int maxYAtX0 = 0;
        int minYAtXDim = DIM_Y;
        
        int numSteps = 0;
        
        while(true) {

        	
        	if(numSteps > 10000) {
        		System.out.println("BREAK2!");
        		break;
        	}
        	
        	if(curX == 0 && curY > maxYAtX0) {
        		maxYAtX0 = curY;
        	
        	} else if(curX == DIM_X && curY < minYAtXDim) {
        		minYAtXDim = curY;
        	}

	        
	        if(firstIteration) {
	        	firstIteration = false;
	        } else {
		        if((curX == 0 || curX == DIM_X) && (curY == 0 || curY == DIM_Y)) {
		        	System.out.println("BREAK!");
		        	break;
		        }
	        }
	       
	        //For Now: slope is 1/2 or -1/2... because of the answer given.
	        
	        if(! (curX >0 && curX < DIM_X) ) {
	        	if(curX <=0) {
	        		goRight = true;
	        	} else {
	        		goRight = false;
	        	}
	        }
	        
	        if( !(curY >0 && curY < DIM_Y) ) {
	        	if(curY <=0) {
	        		goUp = true;
	        	} else {
	        		goUp = false;
	        	}
	        }
	        

	        if(!startTopLeft) {	
	        	if(curX == 0) {
	        		
	        		if(curY != 0 && (curRibbonHeight == 0 || curY < curRibbonHeight)) {
	        			curRibbonHeight = curY;
	        			DistTotouchingRibbon = numSteps;
	        		}
	        	}

	        	if(curX == 0) {
	        		
	        		if(curY != 0 && (curRibbonHeight2 == 0 || curY > DIM_Y - curRibbonHeight2)) {
	        			curRibbonHeight2 = DIM_Y - curY;
	        			DistToAdjCorner = numSteps;
	        			//System.exit(1);
	        			System.out.println("Debug 2 curRibbonHeight2 " + curRibbonHeight2);
	        			System.out.println("Debug 2 numSteps " + numSteps);
	        		}
	        	} else if(curX == DIM_X) {
	        		
	        		if(curY != 0 && (curRibbonHeight2 == 0 || curY < curRibbonHeight2)) {
	        			curRibbonHeight2 = curY;
	        			DistToAdjCorner = numSteps;
	        		}
	        	}
	        }
	        
	        
	        int dirX = 1;
	        
	        if(!goRight) {
	        	dirX = -1;
	        }
	        
	        int dirY = 1;
	        
	        if( ! goUp) {
	        	dirY = -1;
	        }

	        int newX = curX + dirX * SLOPE_INV;
	        int newY = curY + dirY;
	        

        	numSteps++;

	        System.out.println("new X: " + newX + " goRight: " + goRight + "DIMX: " + DIM_X + "go up: " + goUp);
	        //TODO: put into a function


	        int yAdjustment = 5;
        	if( tabsAndNotchAbove) {
        		yAdjustment *= -1;
        	} else {
        		//pass
        	}
	        
	        if(tabsAndNotchAbove) {
	        	
	        	//TODO: there's lots of copy/paste code here...
	        	for(int i=0; i<topNotches.size(); i++) {
	        		if(topNotches.get(i) >= (5*(numSteps)-N_ADJUST + 450) % 450
	        				&& topNotches.get(i) < (5*(numSteps+1)-N_ADJUST + 450) % 450
	        			) {
	        			g.setColor(Color.CYAN);
	        			g.drawOval(TOP_LEFT_X + MULT*curX-5, TOP_LEFT_Y + MULT*(DIM_Y - curY)-5 + yAdjustment, 10, 10);
	        		}
	        	}
	        	
	        	for(int i=0; i<topTabs.size(); i++) {
	        		if(topTabs.get(i) >= (5*(numSteps)-N_ADJUST + 450) % 450
	        				&& topTabs.get(i) < (5*(numSteps+1)-N_ADJUST + 450) % 450
	        			) {
	        			g.setColor(Color.BLACK);
	        			//g.drawOval(TOP_LEFT_X + MULT*curX-7, TOP_LEFT_Y + MULT*(DIM_Y - curY)-7 + yAdjustment, 14, 14);
	        			g.drawRect(TOP_LEFT_X + MULT*curX-7, TOP_LEFT_Y + MULT*(DIM_Y - curY)-7 + yAdjustment, 14, 14);
	        		}
	        	}
	        	
	        } else {
	        	for(int i=0; i<bottomNotches.size(); i++) {
	        		if(bottomNotches.get(i) >= (5*(numSteps)-N2_ADJUST + 450) % 450
	        				&& bottomNotches.get(i) < (5*(numSteps+1)-N2_ADJUST + 450) % 450
	        			) {
	        			g.setColor(Color.GRAY);
	        			g.drawOval(TOP_LEFT_X + MULT*curX-5, TOP_LEFT_Y + MULT*(DIM_Y - curY)-5 + yAdjustment, 10, 10);
	        		}
	        	}
	        	
	        	for(int i=0; i<bottomTabs.size(); i++) {
	        		if(bottomTabs.get(i) >= (5*(numSteps)-N2_ADJUST + 450) % 450
	        				&& bottomTabs.get(i) < (5*(numSteps+1)-N2_ADJUST + 450) % 450
	        			) {
	        			g.setColor(Color.DARK_GRAY);
	        			//g.drawOval(TOP_LEFT_X + MULT*curX-7, TOP_LEFT_Y + MULT*(DIM_Y - curY)-7 + yAdjustment, 14, 14);
	        			g.drawRect(TOP_LEFT_X + MULT*curX-7, TOP_LEFT_Y + MULT*(DIM_Y - curY)-7 + yAdjustment, 14, 14);
	        		}
	        	}
	        	//END TODO: there's lots of copy/paste code.
	        }
	        
	        if(! goRight ^ !goUp) {
		        g.setColor(Color.GREEN);
		        if(startTopLeft) {
			        g.setColor(Color.BLUE);
		        }
	        	
	        } else {
		        g.setColor(Color.RED);
		        if(startTopLeft) {
			        g.setColor(Color.MAGENTA);
		        }
	        }
	        
	        g.drawLine(TOP_LEFT_X + MULT*curX, TOP_LEFT_Y + MULT*(DIM_Y - curY), TOP_LEFT_X + MULT*newX, TOP_LEFT_Y + MULT*(DIM_Y - newY));
	        
	        //g.drawOval(TOP_LEFT_X + MULT*curX, TOP_LEFT_Y + MULT*(DIM_Y - curY), 2, 2);
	        
	        curX = newX;
	        curY = newY;
	        


	        if(!(newX >0 && newX < DIM_X) && !(newY >0 && newY < DIM_Y)) {
	        	System.out.println("THE END!");
	        	break;
	        }
	        
	        
	        
        }
        
        
        System.out.println("Number of iterations: " + numSteps);
        
        System.out.println("maxYAtX0: " + maxYAtX0);
        
        System.out.println("Debug ribbon up/down:");
		System.out.println("curRibbonHeight: " + curRibbonHeight);
		System.out.println("numSteps to get to other ribbon above: " + DistTotouchingRibbon + " out of " + numSteps);

        System.out.println("Debug ribbon corner to adj corner:");
		System.out.println("curRibbonHeight2: " + curRibbonHeight2);
		System.out.println("numSteps to get to adj corner: " + DistToAdjCorner + " out of " + numSteps + "( This is wrong! )");
        
        //paintBlueLineComponent(g, 0, DIM_Y, maxYAtX0);
        //paintBlueLineComponent(g, DIM_X, 0, minYAtXDim);
	}
	
	
	//Add blue line on corners:
	}
	
	//Mimicing what's in:
	//https://www.researchgate.net/publication/220991201_Common_Developments_of_Several_Different_Orthogonal_Boxes
	// The blue line overshoots, but whatever.
	public static void paintBlueLineComponent(Graphics g, int startX, int startY, int conditionNumber) {
		

        boolean firstIteration = true;
        
        boolean goRight = false;

        boolean goUp = false;
        if(startX == 0) {
        	goRight = true;
        }
        if(startY == 0) {
        	goUp = true;
        }
        
        int curX = startX;
        int curY = startY;
        
        
        boolean equation1 = false;
        if(startX == 0) {
        	equation1 = true;
        }
        
        int it = 0;
        while(true) {

        	it++;
        	
        	if(it > 10000) {
        		System.out.println("BREAK2!");
        		break;
        	}
        	

	        
	        if(firstIteration) {
	        	firstIteration = false;
	        } else {
	        	
	        	if(equation1) {

	        		if(2 * (curY - conditionNumber) - curX < 0) {
	        			break;
	        		}
	        	} else {
	        		
	        		if(2 * (curY - conditionNumber) - (curX - DIM_X) > 0) {
	        			break;
	        		}
	        	}
		        if((curX == 0 || curX == DIM_X) && (curY == 0 || curY == DIM_Y)) {
		        	System.out.println("BREAK!");
		        	break;
		        }
	        }
	       
	        //For Now: slope is 1/2 or -1/2... because of the answer given.
	        
	        if(! (curX >0 && curX < DIM_X) ) {
	        	if(curX <=0) {
	        		goRight = true;
	        	} else {
	        		goRight = false;
	        	}
	        }
	        
	        if( !(curY >0 && curY < DIM_Y) ) {
	        	if(curY <=0) {
	        		goUp = true;
	        	} else {
	        		goUp = false;
	        	}
	        }
	        
	        int dirX = 1;
	        
	        if(!goRight) {
	        	dirX = -1;
	        }
	        
	        int dirY = 1;
	        
	        if( ! goUp) {
	        	dirY = -1;
	        }

	        int newX = curX + dirX;
	        int newY = curY + dirY * SLOPE_INV;
	        

	        System.out.println("new X: " + newX + " new Y : " + newY + "   goRight: " + goRight + " DIMX: " + DIM_X + " go up: " + goUp);
	        //TODO: put into a function

	        if(! goRight ^ !goUp) {
		        g.setColor(Color.BLUE);
	        	
	        } else {
		        g.setColor(Color.BLUE);
	        }
	        
	        g.drawLine(TOP_LEFT_X + MULT*curX, TOP_LEFT_Y + MULT*(DIM_Y - curY), TOP_LEFT_X + MULT*newX, TOP_LEFT_Y + MULT*(DIM_Y - newY));
	        
	        g.drawOval(TOP_LEFT_X + MULT*curX, TOP_LEFT_Y + MULT*(DIM_Y - curY), 2, 2);
	        
	        curX = newX;
	        curY = newY;
	        


	        if(!(newX >0 && newX < DIM_X) && !(newY >0 && newY < DIM_Y)) {
	        	System.out.println("THE END!");
	        	break;
	        }
	        	
	        
        }
        
        
        System.out.println("Number of iterations: " + it);
        
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("?Hello world");
		
		new DrawOnGui2();
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		count++;
		label.setText("Number of clicks: " + count);
		
		String buttonText = event.getActionCommand();
		
		System.out.println("Button Text: " + buttonText);
		if(buttonText.equals("Increase N2 by 5")) {
			N2_ADJUST+=5;
			
		} else if(buttonText.equals("Increase N by 5")) {
			N_ADJUST+=5;
			
		}
		if(buttonText.equals("Decrease N2 by 5")) {
			N2_ADJUST-=5;
			
		} else if(buttonText.equals("Decrease N by 5")) {
			N_ADJUST-=5;
			
		}
		if(buttonText.equals("beep")) {
			System.out.println("boop");
		}
		
		if(N_ADJUST >= 450) {
			N_ADJUST -= 450;
		}
		if(N2_ADJUST >= 450) {
			N2_ADJUST -= 450;
		}
		

		if(N_ADJUST < 0) {
			N_ADJUST += 450;
		}
		if(N2_ADJUST < 0) {
			N2_ADJUST += 450;
		}
		
		System.out.println("N_ADJUST: " + N_ADJUST);
		System.out.println("N2_ADJUST: " + N2_ADJUST);
		panelDrawing.repaint();
		
	}

}
