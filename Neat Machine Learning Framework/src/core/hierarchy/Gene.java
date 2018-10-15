package core.hierarchy;

public class Gene {

	public int in;
	public int out;
	public int innovationNumber;
	public double weight;
	public boolean isEnabled;
	public boolean isRecurrent;
	
	public Gene() {
		in = Integer.MIN_VALUE;
		out = Integer.MIN_VALUE;
		innovationNumber = Integer.MIN_VALUE;
		weight = Double.MIN_VALUE;
		isEnabled = true;
		isRecurrent = false;
	}

}
