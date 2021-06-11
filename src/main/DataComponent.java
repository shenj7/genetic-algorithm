package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class DataComponent extends JComponent{
	
	public static final int LINE_WIDTH = 15;
	public static final int HEIGHT = 300;
	public static final int WIDTH = 1000;
	public static final int SIDE_OFFSET  = 35;
	public static final int TOP_OFFSET  = 20;
	public  double genOffset;
	
	private static final Color AVG_COLOR = Color.orange;
	private static final Color WEAK_COLOR = Color.red;
	private static final Color STRONG_COLOR = Color.green;
	private static final Color HAM_COLOR = Color.blue;
	
//	private int gen = 0;
	
	private ChromosomeMain cm;
	private ArrayList<Integer> avg;
	private ArrayList<Integer> weak;
	private ArrayList<Integer> strong;
	private ArrayList<Integer> ham;

	
	public DataComponent(ChromosomeMain main) {
		setPreferredSize(new Dimension(1175, 350));
		avg = new ArrayList<>();
		weak = new ArrayList<>();
		strong = new ArrayList<>();
		ham = new ArrayList<>();
		cm = main;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		genOffset = (double)WIDTH/(double)cm.getNumGens();
		int gen = cm.getNumGens();
		Graphics2D g2 = (Graphics2D) g.create();
		g2.drawRect(SIDE_OFFSET, TOP_OFFSET, WIDTH, HEIGHT);
		g2.setStroke(new BasicStroke(3));
		for(int i=1;i<avg.size();i++) {
			g2.setColor(AVG_COLOR);
			g2.drawLine((int)Math.ceil(SIDE_OFFSET+((i-1)*genOffset)), TOP_OFFSET+HEIGHT-(avg.get(i-1)*3), (int)Math.ceil(SIDE_OFFSET+((i)*genOffset)), TOP_OFFSET+HEIGHT-(avg.get(i)*3));		
			g2.setColor(WEAK_COLOR);
			g2.drawLine((int)Math.ceil(SIDE_OFFSET+((i-1)*genOffset)), TOP_OFFSET+HEIGHT-(weak.get(i-1)*3), (int)Math.ceil(SIDE_OFFSET+((i)*genOffset)), TOP_OFFSET+HEIGHT-(weak.get(i)*3));
			g2.setColor(STRONG_COLOR);
			g2.drawLine((int)Math.ceil(SIDE_OFFSET+((i-1)*genOffset)), TOP_OFFSET+HEIGHT-(strong.get(i-1)*3), (int)Math.ceil(SIDE_OFFSET+((i)*genOffset)), TOP_OFFSET+HEIGHT-(strong.get(i)*3));
			g2.setColor(HAM_COLOR);
			g2.drawLine((int)Math.ceil(SIDE_OFFSET+((i-1)*genOffset)), TOP_OFFSET+HEIGHT-(ham.get(i-1)*3), (int)Math.ceil(SIDE_OFFSET+((i)*genOffset)), TOP_OFFSET+HEIGHT-(ham.get(i)*3));

		}
		
		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.black);
		for(int i=0;i<11;i++) {
			//draw x axis - generations
			g2.drawLine(SIDE_OFFSET+(WIDTH/10*i), TOP_OFFSET+HEIGHT-5, SIDE_OFFSET+(WIDTH/10*i), TOP_OFFSET+HEIGHT+5);
			g2.drawString(""+(gen/10*i), SIDE_OFFSET+(i*WIDTH/10)-5, TOP_OFFSET+HEIGHT+20);
			//draw y axis - fitness
			g2.drawLine(SIDE_OFFSET-5, TOP_OFFSET+HEIGHT-(HEIGHT/10*i), SIDE_OFFSET+5,TOP_OFFSET+HEIGHT-(HEIGHT/10*i));
			g2.drawString(""+(10*i), SIDE_OFFSET-25, TOP_OFFSET+HEIGHT-(HEIGHT/10*i)+5);

		}
		//draw title
		g2.drawString("Fitness Over Generations", SIDE_OFFSET+(WIDTH/2)-50, TOP_OFFSET/2);
		//draw legend
		g2.setColor(Color.black);
		g2.drawString("Strongest Chromosome", 875, 215+12);
		g2.drawString("Average of Population", 875, 215+12+25);
		g2.drawString("Weakest Chromosome", 875, 215+12+50);
		g2.drawString("Hamming Distance", 875, 215+12+75);
		
		// Just for three-genome experiment
//		g2.drawString("Average numbers of 1s", 875, 215+12);
//		g2.drawString("Average numbers of undecideds", 875, 215+12+25);
//		g2.drawString("Average number of 0s", 875, 215+12+50);

		g2.setColor(STRONG_COLOR);
		g2.fillRect(850, 215, 15, 15);
		g2.setColor(AVG_COLOR);
		g2.fillRect(850, 215+25, 15, 15);
		g2.setColor(WEAK_COLOR);
		g2.fillRect(850, 215+50, 15, 15);
		g2.setColor(HAM_COLOR);
		g2.fillRect(850, 215+75, 15, 15);

		
	}

	public ArrayList<Integer> getAvg() {
		return avg;
	}

	public void setAvg(ArrayList<Integer> avg) {
		this.avg = avg;
	}

	public ArrayList<Integer> getStrong() {
		return strong;
	}

	public void setStrong(ArrayList<Integer> strong) {
		this.strong = strong;
	}

	public ArrayList<Integer> getWeak() {
		return weak;
	}

	public void setWeak(ArrayList<Integer> weak) {
		this.weak = weak;
	}
	
	public ArrayList<Integer> getHam() {
		return ham;
	}

	public void setHam(ArrayList<Integer> hamList) {
		this.ham = hamList;
	}

}
