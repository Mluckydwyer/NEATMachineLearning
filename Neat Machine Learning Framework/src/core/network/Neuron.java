package core.network;

import java.util.ArrayList;

import core.hierarchy.Gene;

public class Neuron {

	public ArrayList<Gene> incoming;
	private double value;
	
	public Neuron() {
		incoming = new ArrayList<>();
	}

	public double getValue() {
		return value;
	}
	
}
