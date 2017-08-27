package core.hierarchy;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import core.Neat;
import core.network.NeuralNet;

import java.util.HashMap;

public class Genome {
	
	protected ArrayList<Gene> genes; // connections
	protected HashMap<String, Double> mutationRates;
	private NeuralNet latestNetwork;
	protected double fitness;
	protected int globalRank;
	protected int numNeurons; // maxneuron
	
	public Genome() {
		genes = new ArrayList<>();
		latestNetwork = null;
		fitness = 0;
		globalRank = 0;
		numNeurons = 0;
		
		mutationRates = new HashMap<>();
		mutationRates.put("connections", Neat.CONNECTION_MUTATION_CHANCE);
		mutationRates.put("link", Neat.LINK_MUTATION_CHANCE);
		mutationRates.put("bias", Neat.BIAS_MUTATION_CHANCE);
		mutationRates.put("node", Neat.NODE_MUTATION_CHANCE);
		mutationRates.put("enable", Neat.ENABLE_MUTATION_CHANCE);
		mutationRates.put("disable", Neat.DISABLE_MUTATION_CHANCE);
		mutationRates.put("step", Neat.STEP_SIZE);
		
	}
	
	public Genome(boolean basicGenome) {
		this();
		if (basicGenome) {
			numNeurons = Neat.NUMBER_OF_INPUTS;
			mutate();
		}
	}

	protected void genNetwork() {
		latestNetwork = new NeuralNet(this);
	}
	
	protected double getFitness() {
		return fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public double[] propagateNetwork(double[] inputs) {
		if (inputs.length != Neat.NUMBER_OF_INPUTS) throw new InvalidParameterException("Neat: Inputs Array Length Must Match The Number of Input Nodes(" + Neat.NUMBER_OF_INPUTS +"): " + inputs.length);
		return latestNetwork.propagate(inputs);
	}

	private void mutate() {
		// TODO Auto-generated method stub
	}
	
}
