package core;

import java.util.ArrayList;

import core.hierarchy.Generation;
import core.hierarchy.Species;

public class Neat {
	
	public static final int MAX_HIDDEN_NODES = 1000000; // 1,000,000
	
	//public static final double PERTURB_CONNECTION_WEIGHT_CHANCE = 0.90; TODO
	public static final double CONNECTION_MUTATION_CHANCE = 0.25;
	public static final double LINK_MUTATION_CHANCE = 2.00;
	public static final double BIAS_MUTATION_CHANCE = 0.40;
	public static final double NODE_MUTATION_CHANCE = 0.50;
	public static final double ENABLE_MUTATION_CHANCE = 0.20;
	public static final double DISABLE_MUTATION_CHANCE = 0.40;
	public static final double STEP_SIZE = 0.10;
	
	
	public static Objective obj;
	public ArrayList<Generation> generations;
	
	private static int innovationNumber;
	
	public static int POPULATION_SIZE;
	public static int NUMBER_OF_INPUTS;
	public static int NUMBER_OF_OUTPUTS;
	public static double TARGET_FITNESS;
	
	public Neat (Objective obj, int numInputs, int numOutputs, double targetFitness, int populationSize) {
		Neat.obj = obj;
		generations = new ArrayList<Generation>();
		innovationNumber = 1;
		
		POPULATION_SIZE = populationSize;
		NUMBER_OF_INPUTS = numInputs;
		NUMBER_OF_OUTPUTS = numOutputs;
		TARGET_FITNESS = targetFitness;
	}
	
	public void learn() {
		Generation firstGeneration = new Generation().firstGen();
		generations.add(firstGeneration);
		
		do {
			generations.add(generations.get(generations.size() - 1).genNextGen());
		} while (generations.get(generations.size() - 1).getMaxFitness() < TARGET_FITNESS);
	}
	
	public static int getNextInnovationNum() {
		return innovationNumber++;
	}

	public ArrayList<Species> onUpdate(double[] objData) {
		
		return null;
	}
	
	
}
