package approximate;

public class DPApprox {
	
	private String adapter, DNA;
	private int matrix[][];
	private double error_rate;
	
	public DPApprox(String adapter, String DNA, double error_rate){
		this.adapter = adapter;
		this.DNA = DNA;
		this.error_rate = error_rate;
		
		matrix = new int[adapter.length()+1][DNA.length()+1];
		this.fill();
	}
	
	public String findMatch(){
		
		String best_match = null;
		
		//We browse the matrix line after line: adapter
		for (int i = 1; i < matrix.length; i++) {
			
			//Then we search a match in the current line: DNA
			for (int j = 1; j < matrix[i].length; j++) {
				
				//We have an initial number of errors: the number of lines we already explored. Because we aren't at the beginning anymore.
				int nb_errors = i-1;
				
				//If we have the beginning of a match, we follow it
				if(matrix[i][j] == 1){
					Tuple partial_solution = followMatch(i, j, nb_errors, 0, 0);
					
					//We check that the solution is valid
					if((partial_solution.errors * 1.0) / partial_solution.length < this.error_rate){
						//Then, if we have no solution, or if this solution is better, we keep it in mind
						if(best_match == null || best_match.length() < partial_solution.length){
							best_match = DNA.substring(j-1);
						}
					}
				}
			}
		}
		
		return best_match;
		
	}
	
	private Tuple followMatch(int i, int j, int nb_errors, int path_length, int prev) {
		
		//If we have an error in the path, the numbers in the matrix doesn't grow compared to the previous step
		if(matrix[i][j] != prev+1) nb_errors++;
		
		//There is 3 possible ends: we are at the end of the DNA, at the end of the adapter or we have too many errors 
		if(j == this.DNA.length()){

			//Everything is OK, we are at the end of the DNA so there are no additional errors, we just send the solution back
			return new Tuple(path_length+1, nb_errors);
			
		}else if(i == this.adapter.length() && j < this.DNA.length()){
			
			//Here, we have to compute the number of errors due to the fact we didn't reach the end of the DNA: the number of gaps
			nb_errors += this.DNA.length() - j;
			return new Tuple(path_length+this.DNA.length() - j, nb_errors);
			
		}
		
		//We explore a new cell, so we expand the length
		return followMatch(i+1, j+1, nb_errors, path_length+1, matrix[i][j]);
	}

	private double rate(int nb_nucl, int nb_mism){
		return nb_mism / (nb_nucl * 1.0);
	}
	
	private void fill(){
		
		//Init. of first column and line
		for (int i = 0; i < Math.max(adapter.length(), DNA.length()); i++) {
			if(i < adapter.length())
				matrix[i][0] = 0;
			
			if(i < DNA.length())
				matrix[0][i] = 0;
		}
		
		//Then, we fill line per line
		for (int i = 1; i < adapter.length()+1; i++) {
			
			//Then we cell by cell
			for (int j = 1; j < DNA.length()+1; j++) {
				
				matrix[i][j] = this.max(matrix[i-1][j] - 1, matrix[i][j-1] - 1, matrix[i-1][j-1] + f(adapter.charAt(i-1), DNA.charAt(j-1)), 0);
				
			}
		}
	}
	
	private int max(int... integers){
		int max = integers[0];
		for (int i = 1; i < integers.length; i++) {
			if(integers[i]>max)
				max=integers[i];
		}
		return max;
	}

	private int f(char charAt, char charAt2) {
		return (charAt == charAt2) ? 1 : -1;
	}
	
	@Override
	public String toString() {
		
		String s = "| ||e|";
		int k=0;
		
		for (int i = 0; i < DNA.length(); i++) {
			s+="|" + DNA.charAt(i) + "|";
		}
		
		s+="\n|e|";
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				s+="|" + matrix[i][j]+"|";
			}
			s+="\n";
			for (int j = 0; j < matrix[i].length; j++) {
				s+="--";
			}
			s+="\n";
			if(k < adapter.length()) s+="|" + adapter.charAt(k++) + "|";
		}
		
		return s;
	}
	
	public static void main(String[] args) {
		DPApprox dpa = new DPApprox("tgc", "gtgc", 0.1);
		System.out.println(dpa.findMatch());
		DPApprox dpa2 = new DPApprox("gctagga", "agcttagc", 0.42);
		System.out.println(dpa2.findMatch());
	}
	
}

class Tuple{
	public int errors, length;
	public Tuple(int length, int errors){
		this.length = length;
		this.errors = errors;
	}
}
