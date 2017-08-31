package core.hierarchy;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import core.Neat;
import core.network.NeuralNet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Genome {
	
	public ArrayList<Gene> genes; // connections
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

	private <E> int randomValue(ArrayList<E> values) {
		return (int) Math.round(Math.random() * values.size());
	}
	
	private int randomNeuron(boolean allowInputs) {
		HashSet<Integer> possibleNodes = new HashSet<>();
		
		for (int i = 0; i <= Neat.NUMBER_OF_INPUTS; i++)
			possibleNodes.add(i);
		
		for (Gene gene : genes) {
			possibleNodes.add(gene.in);
			possibleNodes.add(gene.out);
		}
		
		for (int i = 1; i <= Neat.NUMBER_OF_OUTPUTS; i++)
			possibleNodes.add(Neat.NUMBER_OF_INPUTS + Neat.MAX_HIDDEN_NODES + i);
		
		int randomIndex = (int) Math.round(Math.random() * possibleNodes.size());
		
		Iterator<Integer> values = possibleNodes.iterator();
		
		for (int i = 0; i < randomIndex - 1; i++)
			values.next();
		
		return values.next();
	}
	
	protected void genNetwork() {
		latestNetwork = new NeuralNet(genes);
	}
	
	protected double getFitness() {
		return fitness;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	private boolean[] convertToBoolean(double[] doubles) {
		boolean[] booleans = new boolean[doubles.length];
		
		for (int i = 0; i > 0; i++)
			if (doubles[i] > 0.5) booleans[i] = true;
		
		return booleans;
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
				inputs1D[y * inputs.length + x] = inputs[x][y];
		
		return propagateNetwork(inputs1D);
	}
	
	public double[] propagateNetwork(double[] inputs) {
		if (inputs.length != Neat.NUMBER_OF_INPUTS) throw new InvalidParameterException("Neat: Inputs Array Length Must Match The Number of Input Nodes(" + Neat.NUMBER_OF_INPUTS +"): " + inputs.length);
		return latestNetwork.propagate(inputs);
	}

	private void mutate() {
		perturbMutationChances();
		if (Math.random() < mutationRates.get("connections")) perturbWeightMutate();
		
		for (double p = mutationRates.get("link"); p > 0; p--)
			if (Math.random() < p) linkMutate(false);
		
		for (double p = mutationRates.get("bias"); p > 0; p--)
			if (Math.random() < p) linkMutate(true);
		
		for (double p = mutationRates.get("node"); p > 0; p--)
			if (Math.random() < p) nodeMutate();
		
		for (double p = mutationRates.get("enable"); p > 0; p--)
			if (Math.random() < p) enableDisableMutate("enable");
		
		for (double p = mutationRates.get("disable"); p > 0; p--)
			if (Math.random() < p) enableDisableMutate("disable");
		
	}
	
	private void perturbMutationChances() {	
		for (String key : mutationRates.keySet()) {
				if (Math.random() > 0.5) mutationRates.replace(key, mutationRates.get(key), mutationRates.get(key) * 0.95);
				else mutationRates.replace(key, mutationRates.get(key), mutationRates.get(key) * 1.05);
		}
	}
	
	// point mutate
	private void perturbWeightMutate() {
		for (Gene gene : genes) {
			if (Math.random() > Neat.PERTURB_CONNECTION_WEIGHT_CHANCE) gene.weight += Math.random() * mutationRates.get("step") * 2 - mutationRates.get("step");
			else gene.weight = Math.random() * 4 - 2;
		}
	}
	
	// Also is bias mutation
	private void linkMutate(boolean isBiasMutate) {
		int neuron1 = randomNeuron(true);
		int neuron2 = randomNeuron(false);
		if (neuron1 <= Neat.NUMBER_OF_INPUTS && neuron2 <= Neat.NUMBER_OF_INPUTS) return; // not enough neurons
		
		if (neuron2 <= Neat.NUMBER_OF_INPUTS) { // not sure if this is needed
			int temp = neuron1;
			neuron1 = neuron2;
			neuron2 = temp;
		}
		
		Gene newGene = new Gene();
		newGene.in = neuron1;
		newGene.out = neuron2;

		if (isBiasMutate) newGene.in = 0;
		
		for (Gene gene : genes) {
			if (gene.in == newGene.in && gene.out == newGene.out) return;
		}

		if (newGene.in == newGene.out) newGene.isRecurrent = true;
		//else if (Math.random() > 0.5) newGene.isRecurrent = true; // TODO Look at to see if recurrent can go to different node
		
		if (!Neat.ALLOW_RECURRENT_CONNECTIONS && newGene.isRecurrent) {
			linkMutate(isBiasMutate);
			return;
		}
		
		newGene.innovationNumber = Neat.getNextInnovationNum();
		newGene.weight = Math.random() * 4 - 2;
		genes.add(newGene);
	}
	
	private void nodeMutate() {
		if (genes.isEmpty()) return;
		
		numNeurons++;
		Gene randomGene = genes.get(randomValue(genes));
		
		if (!randomGene.isEnabled) return;
		randomGene.isEnabled = false;
		
		Gene splitGene = new Gene(randomGene);
		splitGene.out = numNeurons;
		splitGene.weight = 1.0;
		splitGene.innovationNumber = Neat.getNextInnovationNum();
		splitGene.isEnabled = true;
		genes.add(splitGene);
		
		splitGene = new Gene(randomGene);
		splitGene.in = numNeurons;
		splitGene.innovationNumber = Neat.getNextInnovationNum();
		splitGene.isEnabled = true;
		genes.add(splitGene);
	}
	
	private void enableDisableMutate(String mode) {
		ArrayList<Gene> possibleGenes = new ArrayList<>();
		
		for (Gene gene : genes) {
			if (gene.isEnabled && mode.equals("disable")) possibleGenes.add(gene);
			else if (!gene.isEnabled && mode.equals("enable")) possibleGenes.add(gene);
		}
		
		if (!possibleGenes.isEmpty()) {
			int randomIndex = randomValue(possibleGenes);
			possibleGenes.get(randomIndex).isEnabled = !possibleGenes.get(randomIndex).isEnabled;
		}
	}

	public Gene hasInnovation(Gene gene) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
