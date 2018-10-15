package core;

public class NeatParameters {

	private int inputs;
	private int outputs;
	private int loosePopulationSize;
	private int maxGenerations;
	private long randomGenerationSeed = Long.MIN_VALUE;
	private double targetFitness;
	private NeatObjective nObjective;
	private SimultaneousNeatObjective sObjective;
	
	public static final int DEFAULT_POPULATION_SIZE = 300;
	public static final int MAX_GENERATIONS = Integer.MAX_VALUE;
	public static final double MAX_FITNESS = Double.MAX_VALUE;

	public NeatParameters() {
		this(0, 0);
	}

	// Final Constructor
	public NeatParameters(int inputs, int outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
	}

	/**
	 * @return the inputs
	 */
	public int getNumberOfInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setNumberOfInputs(int inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the outputs
	 */
	public int getNumberOfOutputs() {
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setNumberOfOutputs(int outputs) {
		this.outputs = outputs;
	}

	/**
	 * @return the population
	 */
	public int getLoosePopulationSize() {
		return loosePopulationSize;
	}

	/**
	 * @param population the population to set
	 */
	public void setLoosePopulationSize(int populationSize) {
		this.loosePopulationSize = populationSize;
	}

	/**
	 * @return the targetFitness
	 */
	public double getTargetFitness() {
		return targetFitness;
	}

	/**
	 * @param targetFitness the targetFitness to set
	 */
	public void setTargetFitness(double targetFitness) {
		this.targetFitness = targetFitness;
	}

	/**
	 * @return the nObjective
	 */
	public NeatObjective getNeatObjective() {
		return nObjective;
	}

	/**
	 * @param nObjective the nObjective to set
	 */
	public void setNeatObjective(NeatObjective nObjective) {
		this.nObjective = nObjective;
	}

	/**
	 * @return the sObjective
	 */
	public SimultaneousNeatObjective getSimultaneousNeatObjective() {
		return sObjective;
	}

	/**
	 * @param sObjective the sObjective to set
	 */
	public void setSimultaneousNeatObjective(SimultaneousNeatObjective sObjective) {
		this.sObjective = sObjective;
	}

	/**
	 * @return the maxGenerations
	 */
	public int getMaxGenerations() {
		return maxGenerations;
	}

	/**
	 * @param maxGenerations the maxGenerations to set
	 */
	public void setMaxGenerations(int maxGenerations) {
		this.maxGenerations = maxGenerations;
	}

	/**
	 * @return the randomGenerationSeed
	 */
	public long getSeed() {
		return randomGenerationSeed;
	}

	/**
	 * @param randomGenerationSeed the randomGenerationSeed to set
	 */
	public void setSeed(long randomGenerationSeed) {
		this.randomGenerationSeed = randomGenerationSeed;
	}

}
