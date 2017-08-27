package core;

import java.util.ArrayList;

import core.hierarchy.Genome;

public interface SimultaneousNeatObjective {
	public void calculateFitness(ArrayList<Genome> genomes);
}
