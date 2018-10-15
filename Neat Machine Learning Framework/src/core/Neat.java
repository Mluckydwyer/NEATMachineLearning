package core;

import java.security.InvalidParameterException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import core.hierarchy.Generation;
import core.hierarchy.Genome;

public class Neat {

	public static final int MAX_HIDDEN_NODES = 1000000; // 1,000,000
	public static final int STALE_SPECIES = 20;

	public static final double CROSSOVER_CHANCE = 0.75;
	public static final double PERTURB_WEIGHT_CHANCE = 0.50; // TODO Tune
	public static final double WEIGHT_MUTATION_CHANCE = 0.80; // 
	public static final double CONNECTION_MUTATION_CHANCE = 0.30; // 0.05
	public static final double BIAS_MUTATION_CHANCE = 0.40; // TODO Tune 0.15
	public static final double NODE_MUTATION_CHANCE = 0.03;
	public static final double ENABLE_MUTATION_CHANCE = 0.01; // TODO Tune 0.2
	public static final double DISABLE_MUTATION_CHANCE = 0.05; // TODO Tune 0.1
	public static final double STEP_SIZE = 0.50; // 0.1

	public static final double DELTA_THRESHOLD = 4.0; // 1
	public static final double DELTA_DISJOINT = 1.0; // 2
	public static final double DELTA_EXCESS = 1.0; // 2
	public static final double DELTA_WEIGHTS = 3.0; // 0.4
	
	public static final double BIAS_NODE_VALUE = 1.0;
	
	public static final boolean ALLOW_RECURRENT_CONNECTIONS = false; // TODO
	public static final boolean VERBOSE_CONSOLE = false;

	//BETA PARAMS
	public static final int SPECIES_ELITE_MEMBERS = 2;
	public static final double SURVIVAL_THRESHOLD = 0.2;

	
	public static NeatObjective neatObjective;
	public static SimultaneousNeatObjective simultaneousNeatObjective;
	public static DecimalFormat numberFormatter;
	public static NeatParameters parameters;

	public static ArrayList<Generation> generations;

	private static boolean learning;
	private static int innovationNumber;
	public static Date startTimestamp;
	private static Genome topGenome;

	public static int generationNumber;
	private static int staleGenerationCount; // TODO include this every 20 to focus on only top two species
	
	public static boolean SIMULTANEOUS_FITNESS_TESTS;
	public static int POPULATION_SIZE;
	public static int NUMBER_OF_INPUTS;
	public static int NUMBER_OF_OUTPUTS;
	public static int MAX_GENERATIONS;
	public static long RANDOM_SEED;
	public static double TARGET_FITNESS;
	public static String TITLE;

	public Neat() {
		this("Untitled Experiment");
	}

	public Neat(String title) {		this(title, new NeatParameters());
	}

	public Neat(String title, NeatParameters params) {
		learning = false;
		innovationNumber = 1;
		generationNumber = 0;
		
		generations = new ArrayList<Generation>();
		numberFormatter = new DecimalFormat("##############0.0######");

		TITLE = title;
		SIMULTANEOUS_FITNESS_TESTS = false;

		setParameters(params);
		System.out.println(this);
	}

	public void updateParameters() {
		if (!learning)
			setParameters(Neat.parameters);
		else
			throw new RuntimeException("Cannot update Parameters while learning is in progress");
	}
	
	private void setParameters(NeatParameters params) {
		Neat.parameters = params;

		NUMBER_OF_INPUTS = params.getNumberOfInputs();
		NUMBER_OF_OUTPUTS = params.getNumberOfOutputs();
		POPULATION_SIZE = params.getLoosePopulationSize();
		MAX_GENERATIONS = params.getMaxGenerations();
		TARGET_FITNESS = params.getTargetFitness();

		if (params.getSimultaneousNeatObjective() != null) {
			simultaneousNeatObjective = params.getSimultaneousNeatObjective();
			setSimultaneousFitnessTests(true);
		}
		else if (params.getNeatObjective() != null) {
			neatObjective = params.getNeatObjective();
			setSimultaneousFitnessTests(false);
		}
		
		if (params.getSeed() == Long.MIN_VALUE)
			RANDOM_SEED = System.nanoTime();
		else
			RANDOM_SEED = params.getSeed();
		
	}

	private void verifyParams() {
		if (simultaneousNeatObjective == null && neatObjective == null) throw new InvalidParameterException("Neat: An Objective must be specified, was: null");
		if (NUMBER_OF_INPUTS <= 0) throw new InvalidParameterException("Neat: The number of Inputs must be > 0, was: " + NUMBER_OF_INPUTS);
		if (NUMBER_OF_OUTPUTS <= 0) throw new InvalidParameterException("Neat: The number of Outputs must be > 0, was: " + NUMBER_OF_OUTPUTS);
		if (POPULATION_SIZE <= 0) throw new InvalidParameterException("Neat: The Population size must be > 0, was: " + POPULATION_SIZE);
		if (MAX_GENERATIONS <= 0) throw new InvalidParameterException("Neat: The Maximum number of Generations must be > 0, was: " + MAX_GENERATIONS);
		if (TARGET_FITNESS <= 0) throw new InvalidParameterException("Neat: The Target Fitness must be > 0, was: " + TARGET_FITNESS);
	}

	private void setSimultaneousFitnessTests(boolean simultaneous) {
		if (!learning) SIMULTANEOUS_FITNESS_TESTS = simultaneous;
	}

	public static int getNextInnovationNum() {
		return innovationNumber++;
	}

	public void learn() {
		verifyParams();
		learning = true;
		startTimestamp = new Date(System.currentTimeMillis());
		System.out.println("\n> Timestamp:\t" + getTimestamp(startTimestamp.getTime()) + "\n> Learning initiated");
		Generation firstGeneration = new Generation().firstGen();
		generations.add(firstGeneration);

		do {
			generationNumber++;
			generations.add(generations.get(generations.size() - 1).genNextGen());
		} while (generations.get(generations.size() - 1).getMaxFitness() < TARGET_FITNESS && generationNumber < MAX_GENERATIONS);
		
		topGenome = generations.get(generations.size() - 1).rankGlobally(generations.get(generations.size() - 1).species);
	}
	
	public static String getTimestamp(long time) {
		return new SimpleDateFormat("HH:mm:ss.SSS").format(time);
	}
	
	public static String getComputeTime(Date date) {
		long millis = System.currentTimeMillis() - date.getTime();
		return String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)), millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)));
	}

	public String toString() {
		String out = "";
		out += "NeuroEvolution of Augmenting Topologies\n";
		out += "----------------------------------------\n";
		out += "Objective:\t" + TITLE;
		out += "\nSeed:\t\t" + RANDOM_SEED;
		out += "\nInputs:\t\t" + NUMBER_OF_INPUTS;
		out += "\nOutputs:\t" + NUMBER_OF_OUTPUTS;
		out += "\nPopulation:\t" + POPULATION_SIZE + ((POPULATION_SIZE == NeatParameters.DEFAULT_POPULATION_SIZE)? " [Default]" : "");
		out += "\nTarget Fitness:\t" + TARGET_FITNESS;
		out += "\n";

		return out;
	}

	/**
	 * @return the topGenome
	 */
	public static Genome getTopGenome() {
		return topGenome;
	}

}
