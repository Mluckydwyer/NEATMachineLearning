package core;

import java.util.ArrayList;

public class Neat {

	public Objective obj;
	
	public final int populationSize = 100;
	
	public Neat (Objective obj) {
		this.obj = obj;
	}

	public ArrayList<Species> onUpdate(double[] objData) {
		
		return null;
	}
	
	public int getPopulationSize() {
		return populationSize;
	}
	
	
}
