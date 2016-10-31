package discovering;

public class PWM {
	
	private int matrix[][];
	public final static int A=0, C=1, G=2, T=3;
	
	public PWM(int length) {
		//One line for each possible amino acid, and a row for each nucleotide
		matrix = new int[4][length];
	}
	
	public void feed(String s){
		
		for (int i = 0; i < s.length(); i++) {
			int idx = -1;
			switch(s.charAt(i)) {
				case 'A':
					idx=PWM.A;
					break;
					
				case 'C':
					idx=PWM.C;
					break;
				
				case 'G':
					idx=PWM.G;
					break;
					
				case 'T':
					idx=PWM.T;
					break;
			}
			
			if(idx != -1)
				matrix[idx][i]++;
		}
	}
	
	@Override
	public String toString() {
		
		char acids[] = {'A', 'C', 'G', 'T'};
		String s="";
		
		for (int i = 0; i < acids.length; i++) {
			s+=acids[i]+" ";
			for (int j = 0; j < matrix[i].length; j++) {
				s+=matrix[i][j]+" ";
			}
			s+="\n";
		}
		
		return s+"\n";
	}
	
	public int getScore(){
		return this.consensusScore();
	}
	
	public String getConsensus(){
		
		String consensus = "";
		
		for (int i = 0; i < matrix[0].length; i++) {
			final int values[] = {matrix[0][i], matrix[1][i], matrix[2][i], matrix[3][i]};
			int max = this.max(values);
			
			if(values[PWM.A] == max){
				consensus+='A';
			}else if(values[PWM.C] == max){
				consensus+='C';
			}else if(values[PWM.G] == max){
				consensus+='G';
			}else{
				consensus+='T';
			}
		}
		
		return consensus;
	}

	private int consensusScore() {

		int score = 0;
		
		for (int i = 0; i < matrix[0].length; i++) {
			score+=this.max(matrix[0][i], matrix[1][i], matrix[2][i], matrix[3][i]);
		}

		return score;
	}
	
	private int max(int... integers){
		int max = integers[0];
		
		for (int i = 1; i < integers.length; i++) {
			if(integers[i] > max)
				max = integers[i];
		}
		
		return max;
	}
	
}
