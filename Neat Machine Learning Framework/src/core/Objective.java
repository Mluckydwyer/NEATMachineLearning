package core;

import java.util.ArrayList;

public interface Objective {

	public int[] calculateFitness(ArrayList<Species> s);

	// public double[] onUpdate(double[] objData);
	
}
