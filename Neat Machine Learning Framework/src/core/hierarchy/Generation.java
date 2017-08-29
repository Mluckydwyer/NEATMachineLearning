package core.hierarchy;

import java.util.ArrayList;
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
		nextGen.species = speciateGenomes(children);

		System.out.println("Running Fitness Tests");
		runFitnessTests(nextGen);

		return nextGen;
	}

	private void rankGlobally() {
		ArrayList<Genome> genomes = new ArrayList<>();

		for (Species s : species)
			for (Genome g : s.genomes)
				genomes.add(g);

		genomes.sort(new Comparator<Genome>() {

			@Override
			public int compare(Genome g1, Genome g2) {
				if (g1.fitness > g2.fitness) return -1;
				return 1;
			}
		});

		for (int i = 0; i < genomes.size(); i++)
			genomes.get(i).globalRank = i + 1;

		if (genomes.get(0).fitness > maxFitness) maxFitness = genomes.get(0).fitness;
	}

	private void cullSpeciesLowerHalf() {
		for (Species s : species) {
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

			if (s.staleness >= Neat.STALE_SPECIES && !(s.previousTopFitness >= maxFitness)) species.remove(i);
		}
	}

	private void removeWeakSpeceies() {
		int speciesAverageSum = 0;

		for (Species s : species)
			speciesAverageSum += s.averageFitness();

		for (int i = species.size() - 1; i >= 0; i--)
			if (Math.floor(species.get(i).averageFitness / speciesAverageSum * Neat.POPULATION_SIZE) < 1) species.remove(i);
	}

	// subtract one to leave space for top of species from last generation
	private ArrayList<Genome> breedChildren(ArrayList<Genome> children) {
		// TODO Auto-generated method stub
		return null;
	}

	private Genome breadChild(Species species) {
		// TODO Auto-generated method stub
		return null;
	}

	private void cullSpeciesToOne() {
		for (Species s : species) {
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

	private void runFitnessTests(Generation nextGen) {
		if (Neat.SIMULTANEOUS_FITNESS_TESTS) {
			ArrayList<Genome> genomes = new ArrayList<>();
			for (Species s : nextGen.species)
				for (Genome g : s.genomes) {
					g.genNetwork();
					genomes.add(g);
				}
			
			Neat.simultaneousNeatObjective.calculateFitness(genomes);
		}
		else {
			for (Species s : nextGen.species)
				for (Genome g : s.genomes) {
					g.genNetwork();
					Neat.neatObjective.calculateFitness(g);
				}
		}

	}

	private ArrayList<Species> speciateGenomes(ArrayList<Genome> children) {
		ArrayList<Species> species = new ArrayList<>();
		species.addAll(this.species);
		
		for (Genome g : children) {
			boolean foundSpecies = false;
			
			for (Species s : species) {
				double deltaDisjoint = Neat.DELTA_DISJOINT * disjointValue(g.genes, s.genomes.get(0).genes);
				double deltaWeights = Neat.DELTA_WEIGHTS * weightsValue(g.genes, s.genomes.get(0).genes);
				
				if (deltaDisjoint + deltaWeights < Neat.DELTA_THRESHOLD) {
					s.genomes.add(g);
					foundSpecies = true;
					break;
				}
			}
			
			if (!foundSpecies) {
				Species newSpecies = new Species();
				newSpecies.genomes.add(g);
				species.add(newSpecies);
			}
		}
		
		return species;
	}

	private int containsInovation(ArrayList<Gene> genes, Gene gene) {
		for (int i = 0; i < genes.size(); i++) {
			if (genes.get(i).innovationNumber == gene.innovationNumber) return i;
		}
		
		return -1;
	}
	
	private double disjointValue(ArrayList<Gene> genes1, ArrayList<Gene> genes2) {
		int numDisjointedGenes = 0;
		
		for (Gene g : genes1)
			if (containsInovation(genes2, g) == -1) numDisjointedGenes++;
		
		for (Gene g : genes2)
			if (containsInovation(genes1, g) == -1) numDisjointedGenes++;
		
		return numDisjointedGenes / Math.max(genes1.size(), genes2.size());
	}
	
	private double weightsValue(ArrayList<Gene> genes1, ArrayList<Gene> genes2) {
		int numShared = 0;
		double sum = 0;
		
		for (Gene g : genes1) {
			int i = containsInovation(genes2, g);
			
			if (i != -1){
				sum += Math.abs(g.weight - genes2.get(i).weight);
				numShared++;
			}
		}
		
		return sum / numShared;
	}

	public double getMaxFitness() {
		return maxFitness;
	}

}
