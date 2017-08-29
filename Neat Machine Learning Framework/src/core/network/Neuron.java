package core.network;

import java.util.ArrayList;

import core.hierarchy.Gene;

public class Neuron {

	public ArrayList<Gene> incoming;
	protected double value;
	
	public Neuron() {
		incoming = new ArrayList<>();
		value = 0;
	}

	protected double sumIncoming(Neuron[] neurons) {
		double sum = 0;

		for (Gene gene : incoming) {
			sum += neurons[gene.in].value * gene.weight;
		}
		
		return sum;
	}
	
}
