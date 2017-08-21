package core.hierarchy;

import java.util.ArrayList;

import core.Neat;
import core.network.NeuralNet;

import java.util.HashMap;

public class Genome {
	
	public ArrayList<Gene> genes; // connections
	public HashMap<String, Double> mutationRates;
	private NeuralNet latestNetwork;
	public double fitness;
	public int globalRank;
	public int numNeurons; // maxneuron
	
	public Genome() {
		genes = new ArrayList<>();
		latestNetwork = null;
		fitness = 0;
		globalRank = 0;
		numNeurons = Neat.NUMBER_OF_INPUTS;
		
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
			
		}
	}
	
	public NeuralNet getNetwork() {
		latestNetwork = new NeuralNet(this);
		return latestNetwork;
	}

}
