package core.network;

import java.util.ArrayList;

import core.Neat;
import core.hierarchy.Gene;

public class Neuron {

	public ArrayList<Gene> incoming;
	public ArrayList<Double> history;
	public Gene recurrentConnection;
	protected double value;
	
	public Neuron() {
		incoming = new ArrayList<>();
		history = new ArrayList<>();
		recurrentConnection = null;
		value = 0;
	}

	protected double sumIncoming(Neuron[] neurons) {
		double sum = 0;

		for (Gene gene : incoming) {
			sum += neurons[gene.in].value * gene.weight;
		}
		
		if (recurrentConnection != null && Neat.generationNumber > 1)
			sum += neurons[recurrentConnection.in].history.get(neurons[recurrentConnection.in].history.size() - 1) * recurrentConnection.weight;
		
		return sum;
	}
	
}
