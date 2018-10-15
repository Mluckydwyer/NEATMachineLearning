package examples.xor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import core.Neat;
import core.NeatObjective;
import core.NeatParameters;
import core.SimultaneousNeatObjective;
import core.hierarchy.Genome;

public class NeatXOR implements NeatObjective, SimultaneousNeatObjective {

	public static final double[][] INPUTS = { { 0.0, 0.0 }, { 0.0, 1.0 }, { 1.0, 0.0 }, { 1.0, 1.0 } };
	public static final double[] DESIRED_OUTPUTS = { 0.0, 1.0, 1.0, 0.0 };
	public static final String TITLE = "Neat XOR Example";

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		NeatXOR xor = new NeatXOR();
		NeatParameters params = new NeatParameters();
		params.setNumberOfInputs(2);
		params.setNumberOfOutputs(1);
		params.setLoosePopulationSize(50);
		params.setTargetFitness(3.9);
		params.setMaxGenerations(50); // Testing
		//params.setSeed(625558350690744L); // Testing
		params.setNeatObjective(xor);
		
		Neat neat = new Neat(TITLE, params);
		neat.learn();
		System.out.println("\nLearning Complete\nTop " + neat.getTopGenome() + "\nEnter Some Test Data:\n");
		
		while(true) {
			System.out.println("> " + Arrays.toString(neat.getTopGenome().propagateNetwork(new double[] {scan.nextDouble(), scan.nextDouble()})) + "\n");
		}
	}

	@Override
	public void calculateFitness(Genome genome) {
		double fitness = 4.0;
		
		for (int i = 0; i < INPUTS.length; i++) {
			double[] output = genome.propagateNetwork(INPUTS[i]);
			fitness -= Math.pow((DESIRED_OUTPUTS[i] - output[0]), 2);
			//fitness -= Math.abs(DESIRED_OUTPUTS[i] - output[0]);
			//System.out.println("########## I: " + Arrays.toString(INPUTS[i]) + " D: " + DESIRED_OUTPUTS[i] + "\tA: " + output[0]);
		}
		
		//System.out.println("---  Fitness:\t" + fitness);
		genome.setFitness(fitness);
	}

	@Override
	public void calculateFitness(ArrayList<Genome> genomes) {
		
		for (int i = 0; i < INPUTS.length; i++) {
			for (Genome g:genomes) {
				switch (i){
					case 0:
						g.setFitness(4);
					default:
						g.setFitness(g.getFitness() - Math.pow((DESIRED_OUTPUTS[i] - g.propagateNetwork(INPUTS[i])[0]), 2));
				}
			}
		}
	
	}
}
