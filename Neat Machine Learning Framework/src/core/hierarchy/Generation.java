package core.hierarchy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import core.Neat;

public class Generation {

	public ArrayList<Species> species;
	
	private double maxFitness;
	
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
	
	public Generation genNextGen() {
		ArrayList<Genome> children = new ArrayList<>();
	
		System.out.println("Culling Bottom Half of Species");
		cullSpeciesLowerHalf();
		rankGlobally();
	
		System.out.println("Removing Stale Species");
		removeStaleSpecies();
		rankGlobally();
		
		System.out.println("Removing Weak Species");
		removeWeakSpeceies();
		rankGlobally();
		
		System.out.println("Breeding Children");
		children = breedChildren(children);
	
		System.out.println("Cull All But The Top Species Member");
		cullSpeciesToOne();

		
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
		ArrayList<Genome> genomes = new ArrayList<>();
		
		for (Species s:species)
			for (Genome g:s.genomes)
				genomes.add(g);
		
		Collections.sort(genomes, new Comparator<Genome>() {

			@Override
			public int compare(Genome g1, Genome g2) {
				if (g1.fitness > g2.fitness) return -1;
				else if (g1.fitness < g2.fitness) return 1;
				else return 0;
			}
		});
		
		for (int i = 0; i < genomes.size(); i++)
			genomes.get(i).globalRank = i + 1;
		
		if (genomes.get(0).fitness > maxFitness)
			maxFitness = genomes.get(0).fitness;
	}

	private void cullSpeciesLowerHalf() {
		for (Species s:species) {
			s.sort();
			
			for (int i = s.genomes.size(); i > Math.ceil(s.genomes.size() / 2) - 1; i--)
				s.genomes.remove(i);
		}
	}

	private void removeStaleSpecies() {
		for (int i = species.size(); i >= 0; i--) {
			Species s = species.get(i);
			s.sort();
			
			if (s.genomes.get(0).fitness > s.previousTopFitness) {
				s.previousTopFitness = s.genomes.get(0).fitness;
				s.staleness = 0;
			}
			else {
				s.staleness++;
			}
			
			if (s.staleness >= Neat.STALE_SPECIES && !(s.previousTopFitness >= maxFitness))
				species.remove(i);
		}
	}

	private void removeWeakSpeceies() {
		int speciesAverageSum = 0;
		
		for (Species s:species)
			speciesAverageSum += s.averageFitness();
		
		for (int i = species.size() - 1; i >= 0; i--)
			if (Math.floor(species.get(i).averageFitness / speciesAverageSum * Neat.POPULATION_SIZE) < 1)
				species.remove(i);
	}

	private ArrayList<Genome> breedChildren(ArrayList<Genome> children) {
		// TODO Auto-generated method stub
		return null;
	}

	private Genome breadChild(Species species2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void cullSpeciesToOne() {
		for (Species s:species) {
			s.sort();
			
			for (int i = s.genomes.size(); i > 0; i--)
				s.genomes.remove(i);
		}
	}
	
	private ArrayList<Genome> fillChildrenToPopSize(ArrayList<Genome> children) {
		while (children.size() < Neat.POPULATION_SIZE) {
			int randomSpecies = (int) Math.round(Math.random() * (species.size() - 1));
			children.add(breadChild(species.get(randomSpecies)));
		}
		return children;
	}

	private void runFitnessTests(ArrayList<Genome> children) {
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
