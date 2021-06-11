package main;

import java.util.ArrayList;
import java.util.Collections;

public class ChromosomeMain {
	public ArrayList<Chromosome[]> allIterations = new ArrayList<Chromosome[]>(); // List of previous generations
	public ArrayList<Integer> strengths = new ArrayList<Integer>();// Total strengths of previous generations
	public Chromosome[] chromoList; // Current Chromosome list
	public HistorySimulator histSim;
	public Chromosome compareChrom; // TODO: fix
	private int populationSize; // Population
	private int genomeLength = 100; // Genome length

	/*
	 * Constructor for ChromosomeMain
	 */
	public ChromosomeMain() {
		populationSize = 10;
		chromoList = new Chromosome[populationSize];
		for (Chromosome x : chromoList) {
			x = new Chromosome(genomeLength);
			x.initialize();
			x.getStrength();
			// System.out.println(x.strength);
		}
		histSim = new HistorySimulator(this);
	}

	/*
	 * Create a population of specified # of chromosomes
	 */
	public void createPopulation() {
//		if(compare == 3) {
//			compareChrom = histSim.cv.getChromosome();
//		} else {
		chromoList = new Chromosome[populationSize];
		for (Chromosome x : chromoList) {
			x = new Chromosome(genomeLength);
			x.initialize();
			x.getStrength();
		}
		for (int x = 0; x < chromoList.length; x++) {
			chromoList[x] = new Chromosome(genomeLength);
		}
//		}
	}

	/*
	 * Choose the elite chromosomes
	 */
	public void chooseElite(int n) {
		if (n > chromoList.length) {
			throw new IndexOutOfBoundsException();
		}
		sortByFitness(0);// normal getStrength function
		for (Chromosome x : chromoList) {
			x.isElite = false;
		}
		for (int x = 0; x < n; x++) {
			chromoList[chromoList.length - x - 1].isElite = true;
		}
	}

	/*
	 * Calculate the total fitness of the current Chromosome list
	 */
	public int totalFitness() { // most likely input chromolist here
		int totalFit = 0;
		for (Chromosome i : chromoList) {
			totalFit = totalFit + i.strength;
		}
		return totalFit;
	}

	/*
	 * Sort chromoList by strength
	 */
	public void sortByFitness(int fitNum) {
		if (fitNum == 0) {
			for (Chromosome x : chromoList)
				x.getStrength();
		} else if (fitNum == 1) {
			for (Chromosome x : chromoList)
				x.getDiseaseStrength();
		} else if (fitNum == 2) {
			for (Chromosome x : chromoList)
				x.getStrengthConsecutive();
		} else if (fitNum == 3) {
			System.out.println("In the right sort function");
			for (Chromosome x : chromoList)
				try {
					compareChrom = histSim.cv.getChromosome();
					System.out.println("COMPARING GENOME: " + compareChrom.genome);
					x.getStrengthCompare(compareChrom.getGenome());// TODO: fix!
				} catch (Exception e) {
					System.err.println("broken...");
					e.printStackTrace();
				}
		}
		for (int i = 0; i < chromoList.length - 1; i++) {
			int index = i;
			for (int j = i + 1; j < chromoList.length; j++) {
				if (chromoList[j].strength < chromoList[index].strength) {
					index = j;// searching for lowest index
				}
			}
			Chromosome smallerNumber = chromoList[index];
			chromoList[index] = chromoList[i];
			chromoList[i] = smallerNumber;
		}
	}

	/*
	 * Find the average Hamming Distance of the population
	 */
	public double getDiversity(Chromosome[] divChromoList) {
		double diversity = 0;
		for (Chromosome x : divChromoList) {
			for (Chromosome y : divChromoList) {
				for (int a = 0; a < x.genome.length; a++) {
					if (x.genome[a] == 1 && y.genome[a] == 0) {
						// if (x.genome[a] != y.genome[a]) {
						diversity++;
					}
				}
			}
		}
		// System.out.println("before dividing: " + diversity);
		diversity = diversity / (populationSize * (populationSize - 1) / 2);
		// System.out.println("after dividing: " + diversity);

		diversity = diversity / genomeLength;
		return diversity;
	}

	/*
	 * Apply crossover before mutation
	 */
	public void crossOverAll() {
		allIterations.add(new Chromosome[populationSize]); // add generation to allIterations
		for (int x = 0; x < chromoList.length; x++) {
			allIterations.get(allIterations.size() - 1)[x] = chromoList[x];
		}
//		sortByFitness();

		for (int k = 0; k < chromoList.length; k++) {
			int rand = SingletonRandom.rnd.nextInt(100);
			int randStrong = SingletonRandom.rnd.nextInt(50);
			for (int x = 0; x < rand; x++) {
				chromoList[k].genome[x] = allIterations.get(allIterations.size() - 1)[49+randStrong].genome[x];
			}
			for (int x = rand; x < genomeLength; x++) {
				chromoList[k].genome[x] = allIterations.get(allIterations.size() - 1)[49+randStrong].genome[x];
			}
		}
	}

	/*
	 * Truncation Selection
	 */
	public void truncatePop() {
		this.chooseElite(histSim.getElite());
		allIterations.add(new Chromosome[populationSize]); // add generation to allIterations
		for (int x = 0; x < chromoList.length; x++) {
			allIterations.get(allIterations.size() - 1)[x] = chromoList[x];
		}
		int counter = 0;
		for (int x = 0; x < chromoList.length; x++) {

			if (allIterations.get(allIterations.size() - 1)[x].isElite) {
				chromoList[counter] = allIterations.get(allIterations.size() - 1)[x].copyMutate(0);
				counter++;
			} else {
				chromoList[x] = null;
			}
		}
		for (int x = 0; x < chromoList.length / 2; x++) {
			if (chromoList[chromoList.length - 2 * x - 1] == null) {
				chromoList[chromoList.length - 2 * x
						- 1] = allIterations.get(allIterations.size() - 1)[populationSize - x - 1]
								.copyMutate(histSim.mutationRate);
			}
			if (chromoList[chromoList.length - 2 * x - 2] == null) {
				chromoList[chromoList.length - 2 * x
						- 2] = allIterations.get(allIterations.size() - 1)[populationSize - x - 1]
								.copyMutate(histSim.mutationRate);
			}
		}
	}

	/*
	 * Roulette Selection
	 */
	public void roulettePop() {
		this.chooseElite(histSim.getElite());
		allIterations.add(new Chromosome[populationSize]);
		for (int x = 0; x < chromoList.length; x++) { // Put chromolist into alliterations
			allIterations.get(allIterations.size() - 1)[x] = chromoList[x];
		}
		double totalStrength = 0;
		for (Chromosome x : chromoList) {
			totalStrength = totalStrength + x.strength;
		}
		int index = 0;
		for (int x = 0; x < populationSize; x++) {
			if (allIterations.get(allIterations.size() - 1)[x].isElite) {
				chromoList[index] = allIterations.get(allIterations.size() - 1)[x].copyMutate(0);
				index++;
			}
		}
		while (index < chromoList.length) {
			double rand = SingletonRandom.rnd.nextDouble();
			chromoList[index] = roulettePopHelper(0, rand, chromoList, 0, totalStrength)
					.copyMutate(histSim.mutationRate);
			index++;
		}
	}

	/*
	 * Helper function for roulettePop
	 */
	public Chromosome roulettePopHelper(double totalProb, double rand, Chromosome[] chromoListRou, int index,
			double totalStrength) {
		totalProb = totalProb + chromoListRou[index].strength / totalStrength;
		if (rand < totalProb) {
			return chromoListRou[index];
		}
		index++;
		return roulettePopHelper(totalProb, rand, chromoListRou, index, totalStrength);
	}

	/*
	 * Rank Selection
	 */
	public void rankPop() {
		allIterations.add(new Chromosome[populationSize]);
		for (int x = 0; x < chromoList.length; x++) {
			allIterations.get(allIterations.size() - 1)[x] = chromoList[x];
		}
		double totalStrength = 0; // Same idea as roulette
		for (int x = 0; x < chromoList.length; x++) {
			totalStrength = totalStrength + x;
		}
		int index = 0;
		for (int x = 0; x < populationSize; x++) {
			if (allIterations.get(allIterations.size() - 1)[x].isElite) {
				chromoList[index] = allIterations.get(allIterations.size() - 1)[x].copyMutate(0);
				index++;
			}
		}
		while (index < chromoList.length) {
			double rand = SingletonRandom.rnd.nextDouble();
			chromoList[index] = rankPopHelper(0, rand, chromoList, 0, totalStrength).copyMutate(histSim.mutationRate);
			index++; // Split doubles from 0 to 1 by strength
		}
	}

	/*
	 * Helper function for rankPop
	 */
	public Chromosome rankPopHelper(double totalProb, double rand, Chromosome[] chromoListRou, int index,
			double totalStrength) {
		totalProb = totalProb + index / totalStrength; // Cycle through in a recursive loop
		if (rand < totalProb) { // Check if the range of values for a certain population includes the
			// probability
			return chromoListRou[index];
		}
		index++;
		return rankPopHelper(totalProb, rand, chromoListRou, index, totalStrength);
	}

	/*
	 * Returns an arraylist of the average number of 1s
	 * For 3-genome experiment - use default settings, population size = 100, don't graph hamming
	 */
	
	public ArrayList<Integer> graphingAverageOnes() {
		ArrayList<Integer> dataGraphed = new ArrayList<Integer>();
		for (Chromosome[] x : allIterations) {
			int onesTemp = 0;
			for (Chromosome y : x) {
				for (int z: y.genome) {
					if (z == 1) {
						onesTemp++;
					}
				}
			}
			onesTemp = onesTemp/100;
			dataGraphed.add(onesTemp);
		}
		return dataGraphed;
	}
	/*
	 * Returns an arraylist of the average number of 0s
	 * For 3-genome experiment
	 */
	public ArrayList<Integer> graphingAverageZeroes() {
		ArrayList<Integer> dataGraphed = new ArrayList<Integer>();
		for (Chromosome[] x : allIterations) {
			int zeroesTemp = 0;
			for (Chromosome y : x) {
				for (int z: y.genome) {
					if (z == 0) {
						zeroesTemp++;
					}
				}
			}
			zeroesTemp = zeroesTemp/100;
			dataGraphed.add(zeroesTemp);
		}
		return dataGraphed;
	}
	/*
	 * Returns an arraylist of the average number of undecideds
	 * For 3-genome experiment
	 */
	
	public ArrayList<Integer> graphingAverageUndecideds() {
		ArrayList<Integer> dataGraphed = new ArrayList<Integer>();
		for (Chromosome[] x : allIterations) {
			int undecidedTemp = 0;
			for (Chromosome y : x) {
				for (int z: y.genome) {
					if (z == 2) {
						undecidedTemp++;
					}
				}
			}
			undecidedTemp = undecidedTemp/100;
			System.out.println("undecided" + undecidedTemp);
			dataGraphed.add(undecidedTemp);
		}
		return dataGraphed;
	}
	
	/*
	 * Returns an arraylist of the strongest chromosome's strength in the population
	 * after evolution
	 */
	public ArrayList<Integer> graphingStrongestData() {
		ArrayList<Integer> dataGraphed = new ArrayList<Integer>();
		for (Chromosome[] x : allIterations) {
			ArrayList<Integer> strengthsTemp = new ArrayList<Integer>();
			for (Chromosome y : x) {
				strengthsTemp.add(y.strength);
			}
			dataGraphed.add(Collections.max(strengthsTemp));
		}

		return dataGraphed;
	}

	/*
	 * Returns an arraylist of the weakest chromosome's strength in the population
	 * after evolution
	 */
	public ArrayList<Integer> graphingWeakestData() {
		ArrayList<Integer> dataGraphed = new ArrayList<Integer>();
		for (Chromosome[] x : allIterations) {
			ArrayList<Integer> strengthsTemp = new ArrayList<Integer>();
			for (Chromosome y : x) {
				strengthsTemp.add(y.strength);
			}
			dataGraphed.add(Collections.min(strengthsTemp));
		}

		return dataGraphed;
	}

	/*
	 * Returns an arraylist of the average chromosome strength in the population
	 * after evolution
	 */
	public ArrayList<Integer> graphingAverageData() {
		ArrayList<Integer> dataGraphed = new ArrayList<Integer>();
		for (Chromosome[] x : allIterations) {
			ArrayList<Integer> strengthsTemp = new ArrayList<Integer>();
			for (Chromosome y : x) {
				strengthsTemp.add(y.strength);
			}
			int average = 0;
			for (int z : strengthsTemp) {
				average = average + z;
			}
			average = average / strengthsTemp.size();
			dataGraphed.add(average);
		}
		return dataGraphed;
	}

	public ArrayList<Integer> graphingHammingDistance() {
		ArrayList<Double> dataGraphed = new ArrayList<Double>();
		for (Chromosome[] x : allIterations) {
			dataGraphed.add(getDiversity(x));
		}
		ArrayList<Integer> actualDataGraphed = new ArrayList<Integer>();
		for (Double d : dataGraphed) {
			actualDataGraphed.add((int) (d * 50));
		}
		return actualDataGraphed;
	}

	/*
	 * Evolve chromoList
	 */
	public void evolve(int evoFunc, boolean cross, int elite, int fitnessFuncIndex) {
		sortByFitness(fitnessFuncIndex);
		if (cross)
			crossOverAll();
		if (evoFunc == 0)
			truncatePop();
		else if (evoFunc == 1)
			roulettePop();
		else if (evoFunc == 2)
			rankPop();
		sortByFitness(fitnessFuncIndex);
		chooseElite(elite);
		passData();
	}

	/*
	 * Pass data into histSim
	 */
	public void passData() {
		DataComponent dc = histSim.getDataComponent();
		dc.setAvg(graphingAverageData());
		dc.setWeak(graphingWeakestData());
		dc.setStrong(graphingStrongestData());
		dc.setHam(graphingHammingDistance());
		// Just for three-genome experiment
//		dc.setAvg(graphingAverageUndecideds());
//		dc.setWeak(graphingAverageZeroes());
//		dc.setStrong(graphingAverageOnes());
	}

	/*
	 * Main
	 */
	public static void main(String args[]) {
		SingletonRandom.getInstance(); // Instantiate SingletonRandom
		new ChromosomeMain();
	}

	public void updateData() {

	}

	public int getGenomeLength() {
		return genomeLength;
	}

	public void setGenomeLength(int n) {
		genomeLength = n;
	}

	public int getNumGens() {
		return histSim.getNumGens();
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int n) {
		populationSize = n;
		createPopulation();
	}

}
