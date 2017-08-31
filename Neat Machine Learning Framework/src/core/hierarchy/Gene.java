package core.hierarchy;

public class Gene {

	public Gene() {
		in = Integer.MIN_VALUE;
		out = Integer.MIN_VALUE;
		innovationNumber = Integer.MIN_VALUE;
		weight = Double.MIN_VALUE;
		isEnabled = false;
		isRecurrent = false;
	}
	
	public Gene(Gene gene) {
		in = gene.in;
		out = gene.out;
		innovationNumber = gene.innovationNumber;
		weight = gene.weight;
		isEnabled = gene.isEnabled;
		isRecurrent = gene.isEnabled;
	}
	
	public int in;
	public int out;
	public int innovationNumber;
	public double weight;
	public boolean isEnabled;
	public boolean isRecurrent;
	
}
