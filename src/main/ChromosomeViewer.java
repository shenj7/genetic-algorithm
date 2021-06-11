package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ChromosomeViewer {
	public HistorySimulator hs;
	public int chromosomeSize = 100;
	public int populationSize = 100;
	public int mutationRate;
	public Chromosome chrom;
	public ChromosomeComponent cc;
	public ChromosomeViewer cv;


	public ChromosomeViewer(HistorySimulator histSim) {
		hs = histSim;
		updateMutationRate();
		chrom = new Chromosome(chromosomeSize);
	}

	public void createViewer() {
		JFrame frame = new JFrame();
		frame.setSize(600, 600);
		frame.setTitle("Chromosome Viewer");

		//mutate button


		//chromosome panel operations
		JPanel chromPanel = new JPanel(new GridLayout(10,10,1,1));
		JPanel buttonPanel = new JPanel(new FlowLayout());
		JButton loadButton = new JButton("load");
		JButton saveButton = new JButton("save");
		JButton mutatePress = new JButton("Mutate");
		JLabel chromLabel = new JLabel("filename: " + chrom.getFilename());
		
		
		JButton[] geneBtns = new JButton[chromosomeSize];
		for(int i=0;i<chrom.getGenome().length;i++) {
			geneBtns[i] = new JButton();
			if(chrom.getGenome()[i]==1)
				geneBtns[i].setBackground(Color.green);
			else
				geneBtns[i].setBackground(Color.BLACK);
			geneBtns[i].setPreferredSize(new Dimension(30,30));
			chromPanel.add(geneBtns[i]);
			
			int x = i;
			geneBtns[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(chrom.getGenome()[x] == 1) {
						chrom.getGenome()[x] = 0;
						geneBtns[x].setBackground(Color.BLACK);
					}
					else {
						chrom.getGenome()[x] = 1;
						geneBtns[x].setBackground(Color.green);
					}
				}
			});
		}
		
		cc = new ChromosomeComponent(chrom, geneBtns);
		
		mutatePress.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateMutationRate();
				cc.getChrom().mutate(mutationRate);
				cc.updateButtons();
				cc.repaint();
			}
		});
		
		buttonPanel.add(loadButton);
		buttonPanel.add(saveButton);
		buttonPanel.add(chromLabel);
		buttonPanel.add(mutatePress);
		chromPanel.setBackground(Color.gray);
//		frame.setLayout(new BorderLayout());
		frame.add(buttonPanel,BorderLayout.NORTH);
		frame.add(chromPanel, BorderLayout.SOUTH);
		frame.pack();
		frame.setVisible(true);
		loadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					chrom = new Chromosome(getLoadedFile());
					cc.setChrom(chrom);
					cc.updateButtons();
					cc.repaint();
					chromLabel.setText("filename: "+chrom.getFilename());
				}catch(NullPointerException c) {
					return;
				}
			}
		});
		saveButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					save();
					cc.updateButtons();
				}catch(NullPointerException c) {
					return;
				}
			}
		});
	}

	private String getLoadedFile() {
		JFileChooser loadSave = new JFileChooser(System.getProperty("user.dir")+"/filesToLoad");
		loadSave.showOpenDialog(null);
		return loadSave.getSelectedFile().getName();
	}

	private void save() {
		PrintWriter pw = null;
		JFileChooser loadSave = new JFileChooser(System.getProperty("user.dir")+"/filesToLoad");
		loadSave.showSaveDialog(null);
		try {
			pw = new PrintWriter(loadSave.getSelectedFile());
			for(int i=0;i<chrom.getGenome().length;i++) {
				pw.print(chrom.getGenome()[i]);
			}
			
		} catch (FileNotFoundException e) {
//			File file = new File("filesToLoad/"+loadSave.getSelectedFile().getName());
		}
		pw.close();

	}

	public void updateMutationRate() {
		mutationRate = hs.getMutationRate();
	}
	public Chromosome getChromosome() {
		return chrom;
	}
}
