package core;

import java.util.ArrayList;

import core.hierarchy.Species;

public interface Objective {

	public int[] calculateFitness(ArrayList<Species> s);
	
}
