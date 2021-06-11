package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class HistorySimulator {
	public ChromosomeViewer cv;
	public DataComponent dc;
	private ChromosomeMain cm;
	private Timer timer;
	public int mutationRate = 5;
	public PopulationViewer popView;
	public StrongestViewer strongView;
	private int numGens;
	private int startPauseContinueRestart = 0;
	private int trunRoulRank = 0;
	private int normDiseConsComp = 0;
	private int elite;
	private boolean crossover = false;

	public HistorySimulator(ChromosomeMain c){
		cm = c;
		cm.setPopulationSize(10);
		cm.createPopulation();
		cv = new ChromosomeViewer(this);
		dc = new DataComponent(cm);
		popView = new PopulationViewer(this, cm);
		strongView = new StrongestViewer(this);
		createViewer();
	}

	public void createViewer() {
		//Basic frame settings:
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(1400, 450));
		frame.setTitle("Population strength over time");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//mutation panel operations 
		JPanel popPanel = new JPanel(new FlowLayout());
		
		JLabel fitFuncLabel = new JLabel("Fitness Function");
		String[] fitFuncList = {"All 1's", "Disease Strength", "Longest 1's", "Custom"}; 
		@SuppressWarnings({ "rawtypes", "unchecked" })
		JComboBox fitFuncBox = new JComboBox(fitFuncList);
		fitFuncBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setNormDiseConsComp(fitFuncBox.getSelectedIndex());
			}
		});
		
		popPanel.add(fitFuncLabel);
		popPanel.add(fitFuncBox);
		
		JLabel mutateRateLabel = new JLabel("Mutation Rate (Percent)");
		JTextField mutationInput = new JTextField(mutationRate+"", 2);
		popPanel.add(mutateRateLabel);
		popPanel.add(mutationInput);
		// force the field to be numeric only

		mutationInput.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int r;
				try {
					r = Integer.parseInt(mutationInput.getText());
				} catch(NumberFormatException n) {
					r = 0;
					mutationInput.setText("0");
				}
				if(r>100) {
					r=100;
					mutationInput.setText("100");
				}
				if(r<0) {
					r=0;
					mutationInput.setText("0");
				}
				setMutationRate(r);
			}
		});
		
		JLabel popSizeLabel = new JLabel("Size of Population");
		int defaultPopSize = cm.getPopulationSize();
		JTextField popInput = new JTextField(""+defaultPopSize, 2);
		int n = Integer.parseInt(popInput.getText());
		cm.setPopulationSize(n);
		popView.populationSize = n;
		popView.setChromoList(cm.chromoList);
		popInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = Integer.parseInt(popInput.getText());
				cm.setPopulationSize(n);
				popView.populationSize = n;
				popView.setChromoList(cm.chromoList);
			}
		});
		popPanel.add(popSizeLabel);
		popPanel.add(popInput);
		

		//generations selector
		JLabel genLabel = new JLabel("Number of Generations");
		int defaultGen = 50;
		JTextField genInput = new JTextField(defaultGen+"", 2);
		numGens = defaultGen;
		genInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setNumGens(Integer.parseInt(genInput.getText()));
			}
		});
		popPanel.add(genLabel);
		popPanel.add(genInput);


		//genome length selector
		JLabel genomeLength = new JLabel("Genome Length");
		JTextField genLenInput = new JTextField("" + cm.getGenomeLength(), 2);
		genLenInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int n = Integer.parseInt(genLenInput.getText());
				cm.setGenomeLength(n);
			}

		});
		popPanel.add(genomeLength);
		popPanel.add(genLenInput);

		//choose evolution type
		String[] evoNames = {"Truncation", "Roulette", "Rank"};
		JComboBox<?> evoNamesBox = new JComboBox<Object>(evoNames);
		JLabel evoNamesLabel = new JLabel("Evolution Function");

		evoNamesBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTrunRoulRank(evoNamesBox.getSelectedIndex());
			}
		});
		popPanel.add(evoNamesLabel);
		popPanel.add(evoNamesBox);

		JCheckBox crossBox = new JCheckBox("Crossover?");
		crossBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCrossover(crossBox.isSelected());
			}
		});
		popPanel.add(crossBox);
		
		setElite(2);
		JTextField eliteInput = new JTextField(getElite()+"", 2);
		JLabel eliteLabel = new JLabel("Number of Elite");

		eliteInput.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setElite(Integer.parseInt(eliteInput.getText()));
			}
		});
		popPanel.add(eliteLabel);
		popPanel.add(eliteInput);

		JButton startButton = new JButton("Start");
		JPanel dataPanel = new JPanel();
		dataPanel.add(dc);

		popPanel.add(startButton);

		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(startPauseContinueRestart==0) {
					cm.createPopulation();
					cm.sortByFitness(normDiseConsComp);
					popView.createViewer();
					strongView.setStrongestChrom(cm.chromoList[cm.chromoList.length-1]);
					strongView.createViewer();
					timer.start();
					startButton.setText("Pause");
					startPauseContinueRestart = 1;
				}else if(startPauseContinueRestart==1) {
					timer.stop();
					startButton.setText("Continue");
					startPauseContinueRestart = 2;
				}else if(startPauseContinueRestart==2) {
					timer.start();
					startButton.setText("Pause");
					startPauseContinueRestart = 1;
				}else if(startPauseContinueRestart==3) {
					cm.createPopulation();
					startButton.setText("Pause");
					startPauseContinueRestart = 1;
					timer.start();
				}
			}
		});

		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cm.evolve(trunRoulRank, crossover, getElite(), normDiseConsComp);
//				System.out.println(pop);
				strongView.setStrongestChrom(cm.chromoList[cm.chromoList.length-1]);
				popView.setChromoList(cm.chromoList);
				
				popView.updatePopButtons();
				strongView.repaint();

				dc.repaint();
				if(cm.allIterations.size()>=numGens+1) {
					timer.stop();
					startButton.setText("Restart");
					startPauseContinueRestart = 3;
					cm.allIterations = new ArrayList<Chromosome[]>();
				}
			}
		});


		frame.add(popPanel, BorderLayout.NORTH);	
		frame.add(dataPanel,BorderLayout.CENTER);

		dc.repaint();
		cv.createViewer();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}


	public int getMutationRate() {
		return mutationRate;
	}

	public void setMutationRate(int rate) {
		mutationRate = rate;
	}

	public DataComponent getDataComponent() {
		return dc;
	}

	public void setNumGens(int n) {
		this.numGens = n;
	}

	public int getNumGens() {
		return numGens;
	}

	public int getTrunRoulRank() {
		return trunRoulRank;
	}

	public void setTrunRoulRank(int trunRoulRank) {
		this.trunRoulRank = trunRoulRank;
	}

	public boolean isCrossover() {
		return crossover;
	}

	public void setCrossover(boolean crossover) {
		this.crossover = crossover;
	}

	public void setElite(int n) {
		this.elite = n;
	}

	public int getElite() {
		return elite;
	}

	public int getNormDiseConsComp() {
		return normDiseConsComp;
	}

	public void setNormDiseConsComp(int normDiseConsComp) {
		this.normDiseConsComp = normDiseConsComp;
	}

}
