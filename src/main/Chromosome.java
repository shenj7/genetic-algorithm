package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Chromosome {

	public boolean isElite;
	public int[] genome;
	public int strength;
	public String filename;
	public static final int WIDTH = 5;

	/*
	 * Constructor for a Chromosome of a certain size
	 */
	public Chromosome(int size) {
		isElite = false;
		genome = new int[size];
		strength = 0;
		filename = "newRandomChromosome.txt";

		initialize();
	}

	/*
	 * Constructor for a Chromosome from a file
	 */
	public Chromosome(String startingfile) {
		isElite = false;
		strength = 0;
		filename = startingfile;

		loadInitial();
	}
	
	/*
	 * Constructor for a given Chromosome
	 */
	public Chromosome(int[] genome) {
		isElite = false;
		this.genome = genome;
		strength = 0;
		filename = "newFile";
	}

	/*
	 * Create a random 100-bit bitstring chromosome.
	 */
	public void initialize() {
		for (int x = 0; x < genome.length; x++) {
			genome[x] = SingletonRandom.rnd.nextInt(2);
		}
	}

	/*
	 * Load files
	 */
	private void loadInitial() {
		Scanner scanner = null;
		String path = "";
		try {
			scanner = new Scanner(new File("filesToLoad//" + filename));
			path = scanner.next();
			genome = new int[path.length()];
			for (int i = 0; i < path.length(); i++) {
				if (path.charAt(i) == ('1'))
					genome[i] = 1;
				else
					genome[i] = 0;
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found...");
			e.printStackTrace();
			return;
		} catch (NoSuchElementException e) {
			System.err.println("No such element...");
			e.printStackTrace();
			return;
		}

		scanner.close();
	}

	 /*
     * Determine strength of chromosome. Each true increases its strength by 1.
     * Strength can vary from 0 (all zeros) to 100 (all 1s).
     */
    public void getStrength() {
        this.strength = 0; // reset strength
        for (int x : genome) {
            if (x == 1) {
                strength++;
            }
        }
    }
    
    /*
     * Fitness function: "Disease genome"
     */
    public void getDiseaseStrength() {
        this.strength = 0; // reset strength
        for (int x : genome) {
            if (x == 1) {
                strength++;
            }
        }
            if (genome[0] == 1) {
                strength = strength - 25;
            }
    }
   
    /*
     * Fitness function: longest string of 1s and zeroes
     *
     */
    public void getStrengthConsecutive() {
    	System.out.println("genome length " + genome.length);
        this.strength = getStrengthConsecutiveHelper(genome[0], 0, genome);
    }
   
    public int getStrengthConsecutiveHelper(int currentBit, int currentChain, int[] genome) {
    	if (genome.length == 0) {
    		return currentChain;
    	}
    	int[] newGenome = new int[genome.length-1];
    	for (int x = 0; x < newGenome.length; x++) {
    		newGenome[x] = genome[x + 1];
    	}
    	if (genome[0] == currentBit) {
    		return getStrengthConsecutiveHelper(currentBit, currentChain + 1, newGenome);
    	} else {
    		int otherChain = getStrengthConsecutiveHelper(genome[0], 1, newGenome);
    		return Math.max(currentChain, otherChain);
    	}
    }
    /*
	 * Fitness function: similarity to a given genome
	 */
	public void getStrengthCompare(int[] idealGenome) throws Exception {
		this.strength = 0; // reset strength
		if (genome.length != idealGenome.length) {
			throw new Exception();
		}
		for (int x = 0; x < genome.length; x++) {
			if (genome[x] == idealGenome[x]) {
				strength++;
			}
		}
	}
	
	/*
	 * Mutates the chromosome at a 1% mutation rate.
	 */
	public void mutate(int rate) {
		if (this.isElite)
			return;
		// Random rnd = new Random();
		for (int x = 0; x < genome.length; x++) {
			if (SingletonRandom.rnd.nextDouble() < ((double) rate / (double) genome.length)) {
				if (genome[x] == 1) {
					genome[x] = 0;
				} else if (genome[x] == 0) {
					genome[x] = 1;
				}
			}
		}
	}

	
	/*
	 * Copies and mutates the Chromosome at a given rate
	 */
    public Chromosome copyMutate(int rate) {
        Chromosome newOne = new Chromosome(genome.length);
        if (this.isElite) {
            for (int x = 0; x < genome.length; x++) {
                newOne.genome[x] = this.genome[x];
            }
        } else {
            for (int x = 0; x < genome.length; x++) {
                if (SingletonRandom.rnd.nextDouble() < ((double) rate / (double) 100)) {
                    if (genome[x] == 1) {
                        newOne.genome[x] = 0;
                    } else if (genome[x] == 0) {
                        newOne.genome[x] = 1;
                    }
                } else {
                    newOne.genome[x] = this.genome[x];
                }
            }
        }
        newOne.getStrength();
        return newOne;
    }

	/*
	 * Get file name
	 */
	public String getFilename() {
		return this.filename;
	}

	/*
	 * Get genome
	 */
	public int[] getGenome() {
		return this.genome;
	}
	
	/*
	 * ToString function for easy testing
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Arrays.toString(genome);		
	}
	
	/*
	 * Creates a mutated copy of a chromosome
	 */
	public Chromosome mutateCopy(int rate) {
		Chromosome newChromo = new Chromosome(100);
		for (int x = 0; x < genome.length; x++) {
			newChromo.genome[x] = this.genome[x];
			if (SingletonRandom.rnd.nextDouble() < ((double) rate / (double) genome.length)) {
				if (this.genome[x] == 1) {
					newChromo.genome[x] = 0;
				} else if (this.genome[x] == 0) {
					newChromo.genome[x] = 1;
				}
			}
		}
		
		return newChromo;
	}

	public void drawOn(Graphics2D g2d) {
		g2d = (Graphics2D) g2d.create();
		int x=0;
		int y=0;
		int spacing = 1;
		g2d.setColor(Color.black);
		g2d.fillRect(10, 10, 500, 500);
		for(int i=0;i<genome.length;i++) {
			if(this.genome[i]==1) 
				g2d.setColor(Color.green);
			else
				g2d.setColor(Color.black);			
			g2d.fillRect(x*(WIDTH + spacing), y*(WIDTH + spacing), WIDTH, WIDTH);
			if(x<9) x++;
			else {x=0; y++;}
		}
	}
}
