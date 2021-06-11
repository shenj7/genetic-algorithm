package main;

import java.util.Random;

/*
 * Allows us to set a seed for reproducible result
 */
public class SingletonRandom {
	
	static SingletonRandom thisSingle = null;
	static Random rnd = new Random();

	/*
	 * Insures we only have one instance of this class
	 */
	public static SingletonRandom getInstance() {
		if (thisSingle == null) {
			thisSingle = new SingletonRandom();
			rnd.setSeed(1);
		}
		return thisSingle;
	}
}
