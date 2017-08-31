package examples.xor;

import core.Neat;
import core.NeatObjective;
import core.NeatParameters;
import core.hierarchy.Genome;

public class NeatXOR implements NeatObjective {

	public static final double[][] INPUTS = { { 0.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 0.0 }, { 1.0, 1.0 } };
	public static final double[] DESIRED_OUTPUTS = { 0.0, 1.0, 1.0, 0.0 };
	public static final String TITLE = "Neat XOR Example";

	public static void main(String[] args) {
		NeatXOR xor = new NeatXOR();
		NeatParameters params = new NeatParameters();
		params.setNumberOfInputs(2);
		params.setNumberOfOutputs(1);
		params.setPopulationSize(25);
		params.setTargetFitness(4.0);
		params.setNeatObjective(xor);

		Neat neat = new Neat(TITLE, params);
		neat.learn();
	}

	@Override
	public void calculateFitness(Genome genome) {
		double fitness = 4.0;
		
		for (int i = 0; i < INPUTS.length; i++) {
			double[] output = genome.propagateNetwork(INPUTS[i]);
			fitness -= 0.5 * Math.pow((DESIRED_OUTPUTS[i] - output[0]), 2);
		}
		
		genome.setFitness(fitness);
	}
}
