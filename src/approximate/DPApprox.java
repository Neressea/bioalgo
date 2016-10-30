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
		
		int nb_mismatch = 0;
		
		//We search for the first equality on the first column
		int i=1, j=1, eq = 0;
		while(i <= DNA.length() && eq == 0){ eq = matrix[1][i];}
		
		if(eq == 0) return null;
		
		//Now, we go through the diagonal
		while(i < DNA.length() && j < adapter.length() && rate(j, nb_mismatch) <= error_rate){
			
			int prev_eq = eq;
			eq = matrix[i+1][j+1];
			
			if(eq == prev_eq){
				nb_mismatch++;
			}
			
			i++;
			j++;
		}
		
		if(j==1) return null;
		
		return adapter.substring(0, j-1);
	}
	
	private double rate(int nb_nucl, int nb_mism){
		return nb_mism / (nb_nucl * 1.0);
	}
	
	private void fill(){
		//First column and line
		for (int i = 0; i < Math.max(adapter.length(), DNA.length()); i++) {
			if(i < adapter.length())
				matrix[i][0] = 0;
			
			if(i < DNA.length())
				matrix[0][i] = 0;
		}
		
		//Then, we fill column per column
		for (int i = 1; i < adapter.length(); i++) {
			for (int j = 1; j < DNA.length(); j++) {
				
				matrix[i][j] = Math.max(matrix[i-1][j], Math.max(matrix[i][j-1], matrix[i-1][j-1] + f(adapter.charAt(i), adapter.charAt(j))));
				
			}
		}
	}

	private int f(char charAt, char charAt2) {
		return (charAt == charAt2) ? 1 : 0;
	}
	
}
