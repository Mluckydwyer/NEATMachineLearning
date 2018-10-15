package core.hierarchy;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import core.Neat;
import core.network.NeuralNet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Genome {

	enum Mutation {WEIGHT, CONNECTION, NODE, BIAS, ENABLE, DISABLE}
	
	public ArrayList<Gene> genes;
	protected HashMap<Mutation, Double> mutationRates;
	private NeuralNet latestNetwork;
	protected double fitness;
	protected int globalRank;
	protected int numNeurons;
	private Random random;

	public Genome() {
		random = new Random(Neat.RANDOM_SEED);
		genes = new ArrayList<Gene>();
		latestNetwork = null;
		fitness = 0;
		globalRank = 0;
		numNeurons = 0;

		mutationRates = new HashMap<>();
		mutationRates.put(Mutation.WEIGHT, Neat.WEIGHT_MUTATION_CHANCE);
		mutationRates.put(Mutation.CONNECTION, Neat.CONNECTION_MUTATION_CHANCE);
		mutationRates.put(Mutation.NODE, Neat.NODE_MUTATION_CHANCE);
		mutationRates.put(Mutation.BIAS, Neat.BIAS_MUTATION_CHANCE);
		mutationRates.put(Mutation.ENABLE, Neat.ENABLE_MUTATION_CHANCE);
		mutationRates.put(Mutation.DISABLE, Neat.DISABLE_MUTATION_CHANCE);
	}

	public Genome(boolean basicGenome) {
		this();
		if (basicGenome) {
			numNeurons = Neat.NUMBER_OF_INPUTS;
			mutate();
		}
	}

	protected void genNetwork() {
		latestNetwork = new NeuralNet(genes);
	}

	private boolean[] convertToBoolean(double[] values) { // Assumes values are normalizes from 0 - 1
		boolean[] bools = new boolean[values.length];

		for (int i = 0; i > 0; i++)
			if (values[i] > 0.5) bools[i] = true;

		return bools;
	}

	public boolean[] propagateNetworkBooleanOut(double[][] inputs) {
		return convertToBoolean(propagateNetwork(inputs));
	}

	public boolean[] propagateNetworkBooleanOut(double[] inputs) {
		return convertToBoolean(propagateNetwork(inputs));
	}

	public double[] propagateNetwork(double[][] inputs) {
		double[] inputs1D = new double[inputs.length * inputs[0].length];

		for (int x = 0; x < inputs.length; x++)
			for (int y = 0; y < inputs[x].length; y++)
				inputs1D[x * inputs.length + y] = inputs[x][y];

		return propagateNetwork(inputs1D);
	}

	public double[] propagateNetwork(double[] inputs) {
		if (inputs.length != Neat.NUMBER_OF_INPUTS) throw new InvalidParameterException(
				"Neat: Inputs Array Length Must Match The Number of Input Nodes (" + Neat.NUMBER_OF_INPUTS + "): " + inputs.length);
		return latestNetwork.propagate(inputs);
	}

	protected void mutate() { // TODO Has added ability to be over 100%. Not sure if needed
		for (Mutation key : mutationRates.keySet()) {
			if (random.nextBoolean()) mutationRates.put(key, mutationRates.get(key) * 0.95);
			else mutationRates.put(key, mutationRates.get(key) * 1.05263);
		}

		if (random.nextFloat() < mutationRates.get(Mutation.WEIGHT)) perturbWeightMutate();

		for (double p = mutationRates.get(Mutation.CONNECTION); p > 0; p--)
			if (random.nextFloat() < p) connectionBiasMutate(Mutation.CONNECTION);

		for (double p = mutationRates.get(Mutation.BIAS); p > 0; p--)
			if (random.nextFloat() < p) connectionBiasMutate(Mutation.BIAS);

		for (double p = mutationRates.get(Mutation.NODE); p > 0; p--)
			if (random.nextFloat() < p) nodeMutate();

		for (double p = mutationRates.get(Mutation.ENABLE); p > 0; p--)
			if (random.nextFloat() < p) enableDisableMutate(Mutation.ENABLE);

		for (double p = mutationRates.get(Mutation.DISABLE); p > 0; p--)
			if (random.nextFloat() < p) enableDisableMutate(Mutation.DISABLE);

	}

	private void perturbWeightMutate() {
		for (Gene gene : genes) {
		if (random.nextFloat() < Neat.WEIGHT_MUTATION_CHANCE)
			if (random.nextFloat() < Neat.PERTURB_WEIGHT_CHANCE)
				gene.weight += (random.nextFloat() * 2 - 1) * Neat.STEP_SIZE;
			else gene.weight = random.nextFloat() * 4 - 2;
		}
	}

	private void connectionBiasMutate(Mutation mode) {
		int neuron1;
		if (mode == Mutation.BIAS) neuron1 = 0;
		else neuron1 = randomNeuron(true);
		int neuron2 = randomNeuron(false);
		if (neuron1 == -1 || neuron2 == -1) return; // not enough neurons

		Gene newGene = new Gene();
		newGene.in = neuron1;
		newGene.out = neuron2;

		for (Gene gene : genes) {
			if (gene.in == newGene.in && gene.out == newGene.out) return;
		}

		if (newGene.in == newGene.out) newGene.isRecurrent = true;

		if (!Neat.ALLOW_RECURRENT_CONNECTIONS && newGene.isRecurrent) { // TODO Could possibly get stuck in infinite loop if recurrent aren't allowed
			connectionBiasMutate(mode);
			return;
		}

		newGene.innovationNumber = Neat.getNextInnovationNum();
		newGene.weight = random.nextFloat() * 4 - 2;
		genes.add(newGene);
	}

	private void nodeMutate() {
		if (genes.isEmpty()) return;
		Gene randomGene;
		boolean hasEnabledGenes = false;
		
		for (Gene g : genes) {
			if (g.isEnabled) {
				hasEnabledGenes = true;
				break;
			}
		}
		
		if (!hasEnabledGenes) return;	
		
		do {
			randomGene = genes.get(randomValue(genes));
		} while (!randomGene.isEnabled);
		
		numNeurons++;
		randomGene.isEnabled = false;

		Gene splitGene = new Gene();
		splitGene.in = randomGene.in;
		splitGene.out = numNeurons;
		splitGene.weight = 1.0;
		splitGene.innovationNumber = Neat.getNextInnovationNum();
		genes.add(splitGene);

		splitGene = new Gene();
		splitGene.in = numNeurons;
		splitGene.out = randomGene.out;
		splitGene.innovationNumber = Neat.getNextInnovationNum();
		genes.add(splitGene);
	}

	private void enableDisableMutate(Mutation mode) {
		ArrayList<Gene> possibleGenes = new ArrayList<>();

		for (Gene gene : genes) {
			if (gene.isEnabled && mode == Mutation.DISABLE) possibleGenes.add(gene);
			else if (!gene.isEnabled && mode == Mutation.ENABLE) possibleGenes.add(gene);
		}

		if (!possibleGenes.isEmpty()) {
			int randomIndex = randomValue(possibleGenes);
			possibleGenes.get(randomIndex).isEnabled = !possibleGenes.get(randomIndex).isEnabled;
		}
	}

	private int randomNeuron(boolean allowInputs) {
		HashSet<Integer> possibleNodes = new HashSet<>();
	
		if (allowInputs)
			for (int i = 0; i < Neat.NUMBER_OF_INPUTS; i++)
				possibleNodes.add(i);
		
		for (int i = 1; i <= Neat.NUMBER_OF_OUTPUTS; i++)
			possibleNodes.add(Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + i - 1);
	
		for (Gene gene : genes) {
			if (gene.in >= Neat.NUMBER_OF_INPUTS) possibleNodes.add(gene.in);
			if (gene.out >= Neat.NUMBER_OF_INPUTS) possibleNodes.add(gene.out);
		}
		
		if (possibleNodes.isEmpty()) return -1;
		
		int randomIndex = randomValue(possibleNodes.toArray());
		
		Iterator<Integer> values = possibleNodes.iterator();
	
		for (int i = 0; i < randomIndex - 1; i++)
			values.next();
	
		return values.next();
	}

	public Gene hasInnovation(Gene gene) {
		for (Gene g : genes)
			if (g.innovationNumber == gene.innovationNumber) return g;
		return null;
	}

	public static int lowestInnovation(ArrayList<Gene> genes) {
		int innovation = genes.get(0).innovationNumber;

		for (int i = 1; i < genes.size(); i++)
			if (genes.get(i).innovationNumber < innovation) innovation = genes.get(i).innovationNumber;

		return innovation;
	}

	private <E> int randomValue(List<E> values) {
		return randomValue(values.toArray());
	}

	private <E> int randomValue(E[] values) {
		return (int) Math.round(random.nextFloat() * (values.length - 1));
	}
	
	public String toString() {
		String output = "";
		output += "Genome:";
		output += "\nGen Rank:\t" + globalRank;
		output += "\nFitness:\t" + fitness;
		output += "\n# Neurons:\t" + numNeurons;
		output += "\n# Genes:\t" + genes.size();

		return output;
	}
	

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

}
