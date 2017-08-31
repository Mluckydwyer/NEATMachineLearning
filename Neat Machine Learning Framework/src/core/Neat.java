package core;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import core.hierarchy.Generation;

public class Neat {

	public static final int MAX_HIDDEN_NODES = 1000000; // 1,000,000
	public static final int STALE_SPECIES = 15;

	public static final double PERTURB_CONNECTION_WEIGHT_CHANCE = 0.90;
	public static final double CONNECTION_MUTATION_CHANCE = 0.25;
	public static final double LINK_MUTATION_CHANCE = 2.00;
	public static final double BIAS_MUTATION_CHANCE = 0.40;
	public static final double NODE_MUTATION_CHANCE = 0.50;
	public static final double ENABLE_MUTATION_CHANCE = 0.20;
	public static final double DISABLE_MUTATION_CHANCE = 0.40;
	public static final double STEP_SIZE = 0.10;

	public static final double DELTA_THRESHOLD = 1.0;
	public static final double DELTA_DISJOINT = 2.0;
	public static final double DELTA_WEIGHTS = 0.4;
	
	public static final double BIAS_NODE_VALUE = 1.0;
	
	public static final boolean ALLOW_RECURRENT_CONNECTIONS = false;

	public static NeatObjective neatObjective;
	public static SimultaneousNeatObjective simultaneousNeatObjective;
	public ArrayList<Generation> generations;

	private static boolean learning;
	private static int innovationNumber;

	public static int generationNumber;
	
	public static boolean SIMULTANEOUS_FITNESS_TESTS;
	public static int POPULATION_SIZE;
	public static int NUMBER_OF_INPUTS;
	public static int NUMBER_OF_OUTPUTS;
	public static double TARGET_FITNESS;
	public static String TITLE;

	public Neat() {
		this("Unspecified Objective");
	}

	public Neat(String title) {
		this(title, new NeatParameters());
	}

	public Neat(String title, NeatObjective obj, int numInputs, int numOutputs, int populationSize, double targetFitness) {
		this(title, new NeatParameters(obj, numInputs, numOutputs, populationSize, targetFitness));
	}

	public Neat(String title, SimultaneousNeatObjective obj, int numInputs, int numOutputs, int populationSize, double targetFitness) {
		this(title, new NeatParameters(obj, numInputs, numOutputs, populationSize, targetFitness));
	}

	public Neat(String title, NeatParameters params) {
		learning = false;
		innovationNumber = 1;
		generationNumber = 1;
		generations = new ArrayList<Generation>();

		TITLE = title;
		SIMULTANEOUS_FITNESS_TESTS = false;

		setParameters(params);
		System.out.println(this);
	}

	public void setParameters(NeatParameters params) {
		NUMBER_OF_INPUTS = params.getNumberOfInputs();
		NUMBER_OF_OUTPUTS = params.getNumberOfOutputs();
		POPULATION_SIZE = params.getPopulationSize();
		TARGET_FITNESS = params.getTargetFitness();

		if (params.getNeatObjective() != null) {
			neatObjective = params.getNeatObjective();
			setSimultaneousFitnessTests(false);
		}
		else if (params.getSimultaneousObjective() != null) {
			simultaneousNeatObjective = params.getSimultaneousObjective();
			setSimultaneousFitnessTests(true);
		}
	}

	private void verifyParams() {
		if (simultaneousNeatObjective == null && neatObjective == null) throw new InvalidParameterException("Neat: The Objective cannot be null");
		if (NUMBER_OF_INPUTS <= 0) throw new InvalidParameterException("Neat: The Number of inputs must be > 0, was: " + NUMBER_OF_INPUTS);
		if (NUMBER_OF_OUTPUTS <= 0) throw new InvalidParameterException("Neat: The Number of outputs must be > 0, was: " + NUMBER_OF_OUTPUTS);
		if (POPULATION_SIZE <= 0) throw new InvalidParameterException("Neat: The Population size must be > 0, was: " + POPULATION_SIZE);
		if (TARGET_FITNESS <= 0) throw new InvalidParameterException("Neat: The Target Fitness must be > 0, was: " + TARGET_FITNESS);
	}

	private void setSimultaneousFitnessTests(boolean simultaneous) {
		if (!learning) SIMULTANEOUS_FITNESS_TESTS = true;
	}

	public static int getNextInnovationNum() {
		return innovationNumber++;
	}

	public void learn() {
		verifyParams();
		learning = true;
		System.out.println("Learning initiated");
		Generation firstGeneration = new Generation().firstGen();
		generations.add(firstGeneration);

		do {
			generationNumber++;
			generations.add(generations.get(generations.size() - 1).genNextGen());
		} while (generations.get(generations.size() - 1).getMaxFitness() < TARGET_FITNESS);
	}

	public String toSTring() {
		String out = "";
		out += "NeuroEvolution of Augmenting Topologies\n";
		out += "----------------------------------------\n";
		out += "Objective:\t" + TITLE;
		out += "\nInputs:\t" + NUMBER_OF_INPUTS;
		out += "\nOutputs:\t" + NUMBER_OF_OUTPUTS;
		out += "\nPopulation:\t" + POPULATION_SIZE;
		out += "\nTarget Fitness:\t" + TARGET_FITNESS;
		out += "\n";

		return out;
	}

}
