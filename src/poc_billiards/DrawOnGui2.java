package poc_billiards;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	private JButton button;
	private JButton button2;
	private int count = 0;
	
	//TODO: make this shiftable:
	private int N = 5;
	
	
	public DrawOnGui2() {
		frame = new JFrame();
		frame.setSize(1100, 800);
		
		button = new JButton("Increase N");
		button.addActionListener(this);
		button2 = new JButton("Decrease N");
		button2.addActionListener(this);
		
		label = new JLabel("Number of clicks: " + count);
		
		contentPane = new JPanel();
		contentPane.setLayout(new GridLayout(5, 5));
		contentPane.add(button);
		contentPane.add(button2);
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
	
	public static final int DIM_X = 6 * SAFE_MULT;
	public static final int DIM_Y = 45 * SAFE_MULT;
	

	//public static final int DIM_X =18 * SAFE_MULT;
	//public static final int DIM_Y =15 * SAFE_MULT;
	
	//public static final int DIM_X =30 * SAFE_MULT;
	//public static final int DIM_Y = 9 * SAFE_MULT;
	

	//public static final int DIM_X = 90 * SAFE_MULT;
	//public static final int DIM_Y =  3 * SAFE_MULT;
	
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
			
			
			boolean ret[][] = null;

			
			
			g.setColor(Color.YELLOW);
			g.fillRect(TOP_LEFT_Y, TOP_LEFT_X, MULT * DIM_X, MULT * DIM_Y);
			
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
	        
	        boolean firstIteration = true;
	        
	        int maxYAtX0 = 0;
	        int minYAtXDim = DIM_Y;
	        
	        int it = 0;
	        while(true) {

	        	it++;
	        	
	        	if(it > 10000) {
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
		        

		        System.out.println("new X: " + newX + " goRight: " + goRight + "DIMX: " + DIM_X + "go up: " + goUp);
		        //TODO: put into a function

		        if(! goRight ^ !goUp) {
			        g.setColor(Color.GREEN);
		        	
		        } else {
			        g.setColor(Color.RED);
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
	        
	        System.out.println("maxYAtX0: " + maxYAtX0);
	        
	        paintBlueLineComponent(g, 0, DIM_Y, maxYAtX0);
	        paintBlueLineComponent(g, DIM_X, 0, minYAtXDim);
		}
		
		
		//Add blue line on corners:

	}
	
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
		if(buttonText.equals("Increase N")) {
			N++;
			
			if(N >= 10) {
				N = 9;
			}
			
		} else if(buttonText.equals("Decrease N")) {
			N--;
			
			if(N <= 0) {
				N = 1;
			}
			
		}
		System.out.println("N: " + N);
		panelDrawing.repaint();
		
	}

}
