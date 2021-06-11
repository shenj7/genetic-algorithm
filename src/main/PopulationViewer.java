package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PopulationViewer {
	public HistorySimulator hs;
	public int chromosomeSize = 100;
	public int populationSize;
	public int mutationRate;
	public Chromosome[] chromoList;
	public Chromosome chrom;
	public ChromosomeMain cm;
	public PopulationViewer cv;
	ChromosomeComponent[] CCList;


	public PopulationViewer(HistorySimulator histSim, ChromosomeMain chromoMain) {
		cm = chromoMain;
		this.chromoList = chromoMain.chromoList;
		hs = histSim;
		updateMutationRate();
	}

	public void createViewer() {
		JFrame frame = new JFrame();
//		frame.setPreferredSize(new Dimension(450, 1000));
		frame.setTitle("Current Population");
		System.out.println(populationSize);
		
		JPanel other = new JPanel(new GridLayout(10, 10, 3, 3));
		frame.add(other);
		
		CCList = new ChromosomeComponent[populationSize];
		JPanel[] panList = new JPanel[populationSize];
		
		for(int i=0; i<CCList.length; i++) {
			JPanel tempPanel = new JPanel(new GridLayout(10, 10, 1, 1));
			JButton[] tempButtons = new JButton[chromoList[i].genome.length]; //NOT A MESSAGE CHAIN NOT EVEN CLOSE BABY
 			
			CCList[i] = new ChromosomeComponent(chromoList[i], tempButtons);
			int[] tempGenome = chromoList[i].getGenome();
 			for(int k=0; k<tempGenome.length; k++) {
 				tempButtons[k] = new JButton();
 				if(tempGenome[k]==1)
 					tempButtons[k].setBackground(Color.green);
 				else
 					tempButtons[k].setBackground(Color.BLACK);
 				tempButtons[k].setPreferredSize(new Dimension(8,8));
 				tempPanel.add(tempButtons[k]);
 			}
 			
			CCList[i].updateButtons();
			panList[i] = tempPanel;
		}
		
		for(int i=0; i<panList.length; i++)
			other.add(panList[i]);

		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void updatePopButtons() {
		for(int i = 0; i < CCList.length; i++) {
			CCList[i].setChrom(chromoList[i]);
			CCList[i].updateButtons();
			System.out.print(CCList[i].getChrom().strength +" ");
		}
		System.out.println();
	}

	public void updateMutationRate() {
		mutationRate = hs.getMutationRate();
	}

	public Chromosome getChromosome() {
		return chrom;
	}
	
	public void setChromoList(Chromosome[] list) {
		chromoList = list;
	}
}
