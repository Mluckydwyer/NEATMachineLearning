package core;

public class NeatParameters {

	private int inputs;
	private int outputs;
	private int populationSize;
	private double targetFitness;
	private NeatObjective nObjective;
	private SimultaneousNeatObjective sObjective;

	public NeatParameters() {
		this((NeatObjective) null, 0, 0, 0, 0);
	}

	public NeatParameters(SimultaneousNeatObjective objective, int inputs, int outputs, int populationSize, double targetFitness) {
		this(inputs, outputs, populationSize, targetFitness);
		this.sObjective = objective;
		this.nObjective = null;
	}

	public NeatParameters(NeatObjective objective, int inputs, int outputs, int populationSize, double targetFitness) {
		this(inputs, outputs, populationSize, targetFitness);
		this.nObjective = objective;
		this.sObjective = null;
	}

	public NeatParameters(int inputs, int outputs, int populationSize, double targetFitness) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.populationSize = populationSize;
		this.targetFitness = targetFitness;
	}

	/**
	 * @return the inputs
	 */
	public int getInputs() {
		return inputs;
	}

	/**
	 * @param inputs the inputs to set
	 */
	public void setInputs(int inputs) {
		this.inputs = inputs;
	}

	/**
	 * @return the outputs
	 */
	public int getOutputs() {
		return outputs;
	}

	/**
	 * @param outputs the outputs to set
	 */
	public void setOutputs(int outputs) {
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

}
