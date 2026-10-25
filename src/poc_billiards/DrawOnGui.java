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
public class DrawOnGui implements ActionListener {

	private JFrame frame;
	private JLabel label;
	private JPanel contentPane;
	private JPanel panelDrawing;
	private JButton button;
	private JButton button2;
	private int count = 0;
	
	//TODO: make this shiftable:
	private int N = 5;
	
	
	public DrawOnGui() {
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
	
	//TODO: Doesn't look good!
	//public static final int DIM_X = 6 * SAFE_MULT;
	//public static final int DIM_Y = 45 * SAFE_MULT;
	

	//TODO: Doesn't look good!
	public static final int DIM_X =18 * SAFE_MULT;
	public static final int DIM_Y =15 * SAFE_MULT;
	
	//public static final int DIM_X =30 * SAFE_MULT;
	//public static final int DIM_Y = 9 * SAFE_MULT;
	

	//public static final int DIM_X = 90 * SAFE_MULT;
	//public static final int DIM_Y =  3 * SAFE_MULT;
	
	private class GPanel extends JPanel {
		
		@Override
		public void paintComponent(Graphics g) {

			super.paintComponent(g);
			
			int startX= 10;
			int startY = 10;
			
			boolean ret[][] = null;

			int MULT = Math.min(1000 / DIM_X, 500 / DIM_Y);
			
		
			int SLOPE_INV = 2;
			
			int USUAL_STRETCH = SLOPE_INV * DIM_Y;
			
			g.setColor(Color.YELLOW);
			g.fillRect(startY, startX, MULT * DIM_X, MULT * DIM_Y);
			
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
	        
	        int it = 0;
	        while(true) {

	        	it++;
	        	
	        	if(it > 10000) {
	        		System.out.println("BREAK2!");
	        		break;
	        	}
	        	
		        if(! goRight) {
			        g.setColor(Color.GREEN);
		        	
		        } else {
			        g.setColor(Color.RED);
		        }
		        
		        if(firstIteration) {
		        	firstIteration = false;
		        } else {
			        if((curX == 0 || curX == DIM_X) && (curY == 0 || curY == DIM_Y)) {
			        	System.out.println("BREAK!");
			        	break;
			        }
		        }
		       
		        int dirX = 1;
		        
		        if(!goRight) {
		        	dirX = -1;
		        }
		        //For Now: slope is 1/2 or -1/2... because of the answer given.
		        
		        int newX = curX + dirX * USUAL_STRETCH;
		        int newY = -1;
		        
		        if(newX >=0 && newX <= DIM_X) {
		        	if(curY == 0) {
		        		newY = DIM_Y;
		        		goUp = false;
		        	} else {
		        		newY = 0;
		        		goUp = true;
		        	}
		        } else {
		        	
		        	
		        	if(newX > DIM_X) {
		        		newX = DIM_X;
		        		
			        	if(curY == 0) {
			        		newY = (DIM_X - curX) / (SLOPE_INV);
			        	} else {
			        		newY = ((USUAL_STRETCH - (DIM_X - curX)) / (SLOPE_INV));
			        	}
		        		
		        	} else if(newX < 0) {
		        		newX = 0;
		        		
		        		if(curY == 0) {
			        		newY = (curX) / (SLOPE_INV);
			        	} else {
			        		newY = ((USUAL_STRETCH - (curX)) / (SLOPE_INV));
			        	}
		        		
		        	} else {
		        		System.out.println("ERROR: side collision!");
		        		System.exit(1);
		        	}
		        	
		        	
		        	//TODO: keep going!
		        	goRight = !goRight;
		        	

			        System.out.println(curX + " , " + curY);
			        System.out.println(newX + " , " + newY);
			        System.out.println();
		        }
		        
		        
		        //TODO: put into a function
		        g.drawLine(startX + MULT*curX, startY + MULT*(DIM_Y - curY), startX + MULT*newX, startY + MULT*(DIM_Y - newY));
		        
		        curX = newX;
		        curY = newY;
		        

		        if(curY >0 && curY < DIM_Y) {
		        	
		        	if(! goRight) {
				        g.setColor(Color.GREEN);
			        	
			        } else {
				        g.setColor(Color.RED);
			        }
		        	
		        	if(goUp) {
		        		newY = DIM_Y;
		        	} else {
		        		newY = 0;
		        	}
		        	
		        	if(curX == 0) {
		        		
		        		if(goUp) {
		        			newX = (USUAL_STRETCH  * (DIM_Y - curY)) / DIM_Y;
		        		} else {
		        			newX = (USUAL_STRETCH  * curY) / DIM_Y;
		        		}
		        	} else {
		        		
		        		if(goUp) {
		        			newX = DIM_X - (USUAL_STRETCH  * (DIM_Y - curY)) / DIM_Y;
		        		} else {
		        			newX = DIM_X - (USUAL_STRETCH  * curY) / DIM_Y;
		        		}
		        	}
		        	
		        	

			        g.drawLine(startX + MULT*curX, startY + MULT*(DIM_Y - curY), startX + MULT*newX, startY + MULT*(DIM_Y - newY));
			        System.out.println("After bounce:");
			        System.out.println(curX + " , " + curY);
			        System.out.println(newX + " , " + newY);
			        System.out.println();
			        curX = newX;
			        curY = newY;
		        }
		        
		        
	        }
	        
	        
	        
	        
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("?Hello world");
		
		new DrawOnGui();
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
