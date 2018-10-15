package core.hierarchy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;

import core.Neat;

public class Generation {

	public ArrayList<Species> species;

	private double maxFitness;
	private double averageFitness;
	private int numSpecies;
	private int genPopulation;
	private Random random;
	public Date timestamp;

	public Generation() {
		species = new ArrayList<Species>();
		random = new Random(Neat.RANDOM_SEED);
		maxFitness = 0;
		averageFitness = 0;
	}

	public Generation firstGen() {
		ArrayList<Genome> children = new ArrayList<>();
		
		for (int i = 0; i < Neat.POPULATION_SIZE; i++)
			children.add(new Genome(true));

		if (Neat.VERBOSE_CONSOLE) System.out.println("> Initial Population Generated");
		return testNSpeciate(children);
	}

	public Generation genNextGen() {
		ArrayList<Genome> children = new ArrayList<>();
		genPopulation = getPopulationSize();
		numSpecies = species.size();

		//if (Neat.VERBOSE_CONSOLE) System.out.println("> Removing Empty Species");
		//removeEmptySpecies();
		
		if (Neat.VERBOSE_CONSOLE) System.out.println("> Removing Stale Species");
		removeStaleSpecies();
		rankGlobally(species);
		
		// if (Neat.VERBOSE_CONSOLE) System.out.println("> Removing Weak Species");
		// removeWeakSpeceies();
		// rankGlobally(species);

		if (Neat.VERBOSE_CONSOLE) System.out.println("> Culling Species To Top Surviving Members");
		cullSpeciesToSurvivalRate();
		rankGlobally(species);
				
		if (Neat.VERBOSE_CONSOLE) System.out.println("> Breeding Children");
		children = breedChildren();
		
		//if (Neat.VERBOSE_CONSOLE) System.out.println("> Culling All But The Elite Species Members");
		//cullSpeciesToElite();
		
		//if (Neat.VERBOSE_CONSOLE) System.out.println("> Removing Empty Species");
		//removeEmptySpecies();
		
		if (Neat.VERBOSE_CONSOLE) System.out.println("> Filling Children To Population Size");
		fillChildrenToPopSize(children);

		// TODO Backup/save

		return testNSpeciate(children);
	}

	public Generation testNSpeciate(ArrayList<Genome> children) {
		if (Neat.VERBOSE_CONSOLE) System.out.println("> Speciating Children");
		Generation nextGen = new Generation();
		nextGen.species = speciateGenomes(children);

		if (Neat.VERBOSE_CONSOLE) System.out.println("> Running Fitness Tests\n\n");
		runFitnessTests(nextGen);

		double speciesAverageSum = 0;

		for (Genome g:children)
			speciesAverageSum += g.fitness;

		averageFitness = speciesAverageSum / (double) children.size();
		timestamp = new Date(System.currentTimeMillis());
		
		if (Neat.generationNumber > 0) {
			rankGlobally(nextGen.species);

			System.out.println("> Timestamp:\t" + Neat.getTimestamp(timestamp.getTime()) + "\n> Up Time:\t"
					+ Neat.getComputeTime(Neat.startTimestamp) + "\n> Compute Time:\t"
					+ (Neat.generationNumber > 1 ? Neat.getComputeTime(Neat.generations.get(Neat.generations.size() - 2).timestamp)
							: Neat.getComputeTime(Neat.startTimestamp))
					+ "\n\nGeneration:\t" + Neat.generationNumber + "\n\nSpecies:\t" + numSpecies + "\nPopulation:\t" + genPopulation
					+ "\nTop Fitness:\t" + Neat.numberFormatter.format(maxFitness) + "\nAvg Fitness:\t" + Neat.numberFormatter.format(averageFitness)
					+ "\n------------------------------");
		}
		else {
			System.out.println("------------------------------");
		}

		return nextGen;
	}

	private int getPopulationSize() {
		int population = 0;
		for (Species s : species)
			population += s.genomes.size();

		return population;
	}

	public Genome rankGlobally(ArrayList<Species> species) {
		ArrayList<Genome> genomes = new ArrayList<>();

		for (Species s : species)
			for (Genome g : s.genomes)
				genomes.add(g);

		genomes.sort(new Comparator<Genome>() {

			@Override
			public int compare(Genome g1, Genome g2) {
				if (g1.fitness > g2.fitness) return -1;
				else if (g1.fitness < g2.fitness) return 1;
				return 0;
			}
		});

		for (int i = 0; i < genomes.size(); i++)
			genomes.get(i).globalRank = i + 1;

		if (genomes.get(0).fitness > maxFitness) maxFitness = genomes.get(0).fitness;
		
		return genomes.get(0);
	}

	private void cullSpeciesToSurvivalRate() {
		for (Species s : species) {
			s.sort();

			for (int i = s.genomes.size(); i > Math.floor(s.genomes.size() * Neat.SURVIVAL_THRESHOLD) && i > 1; i--)
				s.genomes.remove(i - 1);
		}
	}

	private void removeStaleSpecies() {
		species.sort(new Comparator<Species>() {

			@Override
			public int compare(Species s1, Species s2) {
				if (s1.averageFitness() > s2.averageFitness()) return -1;
				else if (s1.averageFitness() < s2.averageFitness()) return 1;
				return 0;
			}
		});
		
		for (int i = species.size() - 1; i >= 0; i--) {
			Species s = species.get(i);
			s.sort();

			if (s.genomes.get(0).fitness > s.previousTopFitness) {
				s.previousTopFitness = s.genomes.get(0).fitness;
				s.staleness = 0;
			}
			else {
				s.staleness++;
			}

			if (s.staleness >= Neat.STALE_SPECIES && !(s.previousTopFitness >= maxFitness) && i >= Neat.SPECIES_ELITE_MEMBERS) species.remove(i);
		}
	}

	// Species  going extinct
	private void removeWeakSpeceies() {
		int speciesAverageSum = 0;

		for (Species s : species)
			speciesAverageSum += s.averageFitness();

		for (int i = species.size() - 1; i >= 0; i--)
			if (Math.floor((species.get(i).averageFitness / speciesAverageSum) * genPopulation) < 1) species.remove(i);
	}

	private void removeEmptySpecies() {
		for (int i = species.size() - 1; i >= 0; i--)
			if (species.get(i).genomes.isEmpty()) species.remove(i);
	}

	// subtract one to leave space for top of species from last generation
	private ArrayList<Genome> breedChildren() {
		/*
		 * //assuming fitness is positive and that a higher fitness means it's better double maxFitness = 0; Genome best = new Genome(); for(Species s : species){ for(Genome g : s.genomes){
		 * if(g.fitness > maxFitness){ maxFitness = g.fitness; best = g; } } } //adds the best genome into children children.add(best); //gather all genomes ArrayList<Genome> allGenomes = new
		 * ArrayList<Genome>(); for(Species s : species){ for(Genome g : s.genomes){ allGenomes.add(g); } } for(int i = 1; i < Neat.POPULATION_SIZE; i++){ Genome mom; Genome dad; Genome g1 =
		 * allGenomes.get((int) (random.nextFloat() * allGenomes.size())); Genome g2 = allGenomes.get((int) (random.nextFloat() * allGenomes.size())); if(g1.fitness > g2.fitness) mom = g1; else mom =
		 * g2; g1 = allGenomes.get((int) (random.nextFloat() * allGenomes.size())); g2 = allGenomes.get((int) (random.nextFloat() * allGenomes.size())); if(g1.fitness > g2.fitness) dad = g1; else dad
		 * = g2; children.add(crossover(mom, dad)); }
		 */

		ArrayList<Genome> children = new ArrayList<>();
		double speciesAverageSum = 0;

		for (Species s : species)
			speciesAverageSum += s.averageFitness(); // use past/new average may cause different results

		for (int i = 0; i < species.size(); i++) {
			int childrenToBreed = (int) (Math.floor(species.get(i).averageFitness / speciesAverageSum * new Double(Neat.POPULATION_SIZE)) );// - species.get(i).genomes.size());

			for (int j = 0; j < childrenToBreed; j++)
				children.add(breedChild(species.get(i)));
		}

		return children;
	}

	private Genome breedChild(Species species) {
		Genome child;

		if (random.nextFloat() <= Neat.CROSSOVER_CHANCE) {
			Genome g1 = species.genomes.get((int) random.nextFloat() * species.genomes.size());
			Genome g2 = species.genomes.get((int) random.nextFloat() * species.genomes.size());
			child = crossover(g1, g2);
		}
		else child = species.genomes.get((int) random.nextFloat() * species.genomes.size());

		child.mutate();

		return child;
	}

	private Genome breedChild(Genome g1, Genome g2) {
		Genome child = crossover(g1, g2);
		child.mutate();
		
		return child;
	}
	
	private Genome crossover(Genome g1, Genome g2) {
		Genome child = new Genome();

		if (g2.fitness > g1.fitness) {
			Genome temp = g1;
			g1 = g2;
			g2 = temp;
		}

		for (Gene gene : g1.genes) {
			Gene possibleMatch = g2.hasInnovation(gene);

			/*
			 * if (possibleMatch != null) { if (random.nextFloat() > 0.5) child.genes.add(gene); else child.genes.add(possibleMatch); } else { if (g1.fitness == g2.fitness) if (random.nextFloat() >
			 * 0.5) child.genes.add(gene); else child.genes.add(gene); }
			 */

			if (possibleMatch != null && random.nextFloat() > 0.5 && possibleMatch.isEnabled) child.genes.add(possibleMatch);
			else child.genes.add(gene);

		}

		child.numNeurons = Math.max(g1.numNeurons, g2.numNeurons);
		child.mutationRates = g1.mutationRates;

		/*
		 * if (g1.fitness == g2.fitness) for (Gene gene : g2.genes) if (g1.hasInnovation(gene) == null && random.nextFloat() > 0.5) child.genes.add(gene);
		 */

		return child;
	}

	private void cullSpeciesToElite() {
		for (Species s : species) {
			s.sort();
			
			int remainingGenomes = 0;
			if (s.genomes.size() >= Neat.GENOME_ELITE_SPECIES_MIN_SIZE)
				remainingGenomes = Neat.GENOME_ELITE_MEMBERS;

			for (int i = s.genomes.size() - 1; i > remainingGenomes - 1; i--)
				s.genomes.remove(i);
		}
	}
	
	private void fillChildrenToPopSize(ArrayList<Genome> children) { // TODO Rewrite when historical genome saving has been implemented
		int eliteGenomes = 0;
		
		for (Species s:species)
			eliteGenomes += s.genomes.size();
		
		while (children.size() < Neat.POPULATION_SIZE - species.size() - eliteGenomes) {
			int randomSpecies = (int) Math.round(random.nextFloat() * (species.size() - 1));
			children.add(breedChild(species.get(randomSpecies)));
			
			//Genome g1 = children.get((int) Math.round(random.nextFloat() * (children.size() - 1)));
			//Genome g2 = children.get((int) Math.round(random.nextFloat() * (children.size() - 1)));
			//children.add(breedChild(g1, g2));
		}
	}

	private void runFitnessTests(Generation nextGen) { // TODO Move multithreading to framework and away from objective
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
					foundSpecies = inSpecies(s, g);
					
					if (foundSpecies) {
						s.genomes.add(g);
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

	private boolean inSpecies(Species species, Genome genome) {
		ArrayList<Gene> genes1 = genome.genes;
		ArrayList<Gene> genes2 = species.genomes.get(0).genes;
		int numDisjointGenes = 0;
		int numExcessGenes = 0;
		int numShared = 0;
		double weightDiff = 0;
		int innovationthreshold = Math.max(Genome.lowestInnovation(genes1), Genome.lowestInnovation(genes1));

		for (Gene g : genes1) {
			int n = containsInovation(genes2, g);
			if (n == -1) 
				if (g.innovationNumber < innovationthreshold) numDisjointGenes++;
				else numExcessGenes++;
			else {
				weightDiff += Math.abs(g.weight - genes2.get(n).weight);
				numShared++;
			}
		}
			
		for (Gene g : genes2)
			if (containsInovation(genes1, g) == -1) 
				if (g.innovationNumber < innovationthreshold) numDisjointGenes++;
				else numExcessGenes++;
		
		if (genes1.isEmpty() && genes2.isEmpty()) return true; // I believe this is correct..?

		double value = (numDisjointGenes / Math.max(genes1.size(), genes2.size())) * Neat.DELTA_DISJOINT;
		value += (numExcessGenes / Math.max(genes1.size(), genes2.size())) * Neat.DELTA_EXCESS;
		value += (weightDiff / numShared) * Neat.DELTA_WEIGHTS;

		if (value < Neat.DELTA_THRESHOLD) return true;
		return false;
	}

	private int containsInovation(ArrayList<Gene> genes, Gene gene) {
		for (int i = 0; i < genes.size(); i++) {
			if (genes.get(i).innovationNumber == gene.innovationNumber) return i;
		}
	
		return -1;
	}

	public double getMaxFitness() {
		return maxFitness;
	}

}
