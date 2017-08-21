package core.hierarchy;

import java.util.ArrayList;

import core.Neat;

public class Generation {

	public ArrayList<Species> species;
	
	private int maxFitness;
	
	public Generation() {
		species = new ArrayList<Species>();
		maxFitness = 0;
	}
	
	public Generation firstGen() {
		ArrayList<Genome> children = new ArrayList<>();

		for (int i = 0; i < Neat.POPULATION_SIZE; i++)
			children.add(new Genome(true));
		
		System.out.println("Generated First Gen Children");
		return testNSpeciate(children);
	}

	private ArrayList<Genome> breedChildren(ArrayList<Genome> children) {
		// TODO Auto-generated method stub
		return null;
	}

	public Generation genNextGen() {
		ArrayList<Genome> children = new ArrayList<>();
	
		System.out.println("Culling Bottom Half of Species");
		cullSpeciesLowerHalf();
		rankGlobally();
	
		System.out.println("Removing Stale Species");
		removeStaleSpecies();
		rankGlobally();
		
		System.out.println("Removing Weak Species");
		calcAverageSpeciesFitness();
		removeWeakSpeceies();
		
		System.out.println("Breeding Children");
		children = breedChildren(children);
	
		System.out.println("Cull All But The Top Species Member");
		
		System.out.println("Filling Childs To Population Size");
		children = fillChildrenToPopSize(children);
	
		// TODO Backup/save
		
		return testNSpeciate(children);
	}

	public Generation testNSpeciate(ArrayList<Genome> children) {
		System.out.println("Speciating Children");
		Generation nextGen = new Generation();
		nextGen.species = speciateGenomes();
		
		System.out.println("Running Fitness Tests");
		runFitnessTests(children);
		
		return nextGen;
	}

	private void rankGlobally() {
		// TODO Auto-generated method stub
		
	}

	private void calcAverageSpeciesFitness() {
		// TODO Auto-generated method stub
		
	}

	private void cullSpeciesLowerHalf() {
		// TODO Auto-generated method stub
		
	}

	private void removeStaleSpecies() {
		// TODO Auto-generated method stub
		
	}

	private void removeWeakSpeceies() {
		// TODO Auto-generated method stub
		
	}

	private ArrayList<Genome> fillChildrenToPopSize(ArrayList<Genome> children) {
		// TODO Auto-generated method stub
		return null;
	}

	private void runFitnessTests(ArrayList<core.hierarchy.Genome> children) {
		// TODO Auto-generated method stub
		
	}
	
	private ArrayList<Species> speciateGenomes() {
		// TODO Auto-generated method stub
		return null;
	}

	public double getMaxFitness() {
		return maxFitness;
	}

}
