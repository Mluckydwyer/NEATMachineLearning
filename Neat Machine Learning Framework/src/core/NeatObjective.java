package core;

import core.hierarchy.Genome;

public interface NeatObjective {
	public void calculateFitness(Genome genome);
}
