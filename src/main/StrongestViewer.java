package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class StrongestViewer {
	public HistorySimulator hs;
	public int chromosomeSize = 100;
	public int populationSize = 100;
	public int mutationRate;
	public Chromosome chrom;
	public ChromosomeComponent cc;
	public ChromosomeViewer cv;

	public StrongestViewer(HistorySimulator histSim) {
		hs = histSim;
		chrom = null;
	}
	public StrongestViewer(HistorySimulator histSim, Chromosome c) {
		hs = histSim;
		chrom = c;
	}

	public void createViewer() {
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setTitle("Strongest Viewer");
		
		JPanel chromPanel = new JPanel(new GridLayout(10,10,1,1));
		JButton[] geneBtns = new JButton[chromosomeSize];
//		for(int i=0; i<chromosomeSize; i++)
//			geneBtns[i] = new JButton();
		
		cc = new ChromosomeComponent(chrom, geneBtns);
		for(int i=0;i<chrom.getGenome().length;i++) {
			geneBtns[i] = new JButton();
			if(chrom.getGenome()[i]==1)
				geneBtns[i].setBackground(Color.green);
			else
				geneBtns[i].setBackground(Color.BLACK);
			geneBtns[i].setPreferredSize(new Dimension(30,30));
			chromPanel.add(geneBtns[i]);
		}
		frame.add(chromPanel);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);	
	}

	public void setStrongestChrom(Chromosome c) {
		chrom = c;
	}
	
	public void repaint() {
		cc.setChrom(chrom);
		cc.updateButtons();
	}
}
