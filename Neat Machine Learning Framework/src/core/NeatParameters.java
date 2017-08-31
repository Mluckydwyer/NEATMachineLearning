package core;

public class NeatParameters {

	private int inputs;
	private int outputs;
	private int populationSize;
	private int maxGenerations;
	private double targetFitness;
	private NeatObjective nObjective;
	private SimultaneousNeatObjective sObjective;
	
	public static final int DEFAULT_POPULATION_SIZE = 300;
	public static final int MAX_GENERATIONS = Integer.MAX_VALUE;
	public static final double MAX_FITNESS = Double.MAX_VALUE;

	public NeatParameters() {
		this((NeatObjective) null, 0, 0, DEFAULT_POPULATION_SIZE, 0, 0.0);
	}

	// Simultaneous Neat
	public NeatParameters(SimultaneousNeatObjective objective, int inputs, int outputs, double targetFitness) {
		this(inputs, outputs, DEFAULT_POPULATION_SIZE, MAX_GENERATIONS, targetFitness);
		this.sObjective = objective;
		this.nObjective = null;
	}

	public NeatParameters(SimultaneousNeatObjective objective, int inputs, int outputs, int maxGenerations) {
		this(inputs, outputs, DEFAULT_POPULATION_SIZE, maxGenerations, MAX_FITNESS);
		this.sObjective = objective;
		this.nObjective = null;
	}
	
	public NeatParameters(SimultaneousNeatObjective objective, int inputs, int outputs, int populationSize, int maxGenerations, double targetFitness) {
		this(inputs, outputs, populationSize, maxGenerations, targetFitness);
		this.sObjective = objective;
		this.nObjective = null;
	}
	
	// Neat
	public NeatParameters(NeatObjective objective, int inputs, int outputs, double targetFitness) {
		this(inputs, outputs, DEFAULT_POPULATION_SIZE, MAX_GENERATIONS, targetFitness);
		this.nObjective = objective;
		this.sObjective = null;
	}
	
	public NeatParameters(NeatObjective objective, int inputs, int outputs, int maxGenerations) {
		this(inputs, outputs, DEFAULT_POPULATION_SIZE, maxGenerations, MAX_FITNESS);
		this.nObjective = objective;
		this.sObjective = null;
	}
	
	public NeatParameters(NeatObjective objective, int inputs, int outputs, int populationSize, int maxGenerations, double targetFitness) {
		this(inputs, outputs, populationSize, maxGenerations, targetFitness);
		this.nObjective = objective;
		this.sObjective = null;
	}

	// Final Constructor
	public NeatParameters(int inputs, int outputs, int populationSize, int maxGenerations, double targetFitness) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.populationSize = populationSize;
		this.maxGenerations = maxGenerations;
		this.targetFitness = targetFitness;
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
	public int getPopulationSize() {
		return populationSize;
	}

	/**
	 * @param population the population to set
	 */
	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
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
	public SimultaneousNeatObjective getSimultaneousObjective() {
		return sObjective;
	}

	/**
	 * @param sObjective the sObjective to set
	 */
	public void setSimultaneousObjective(SimultaneousNeatObjective sObjective) {
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

}
