package approximate;

public class KMP {
	
	private String adapter, DNA;
	private double authorized_error_rate;
	private int T[];
	private int nb_errors;
	
	public KMP(String adapter, String DNA, double authorized_error_rate){
		this.adapter=adapter;
		this.DNA=DNA;
		this.authorized_error_rate = authorized_error_rate;
	}
	
	public String findMatch(){
		
		this.buildTab();
		int index = this.searchIndex();
		
		if(index == -1){
			return null;
		}
		
		if(this.DNA.equals(adapter.substring(0, DNA.length() - index))){
			return adapter.substring(0, DNA.length() - index);
		}
		
		return null;
	}
	
	public int searchIndex(){
		
		int m = 0, i = 0;
		
		while(m+i < adapter.length() && i < DNA.length()){
			if(adapter.charAt(m+i) == DNA.charAt(i)){
				i++;
			}else{
				nb_errors++;
				if(i != 0 && nb_errors / (m+i) < authorized_error_rate){
					i++;
				}else{
					m+= i - T[i];
					
					if(i>0)
						i = T[i];
				}
			}	
		}

		if(i == DNA.length())
			return m;
		
		return -1;
	}
	
	public void buildTab(){
		
		int i=0, j=-1;
		T = new int[DNA.length()+1];
		char c = '\0';
		
		T[0] = j;
		while(i < DNA.length()){ 
			if(DNA.charAt(i) == c && j < ((i+(i%2))/2)){
				T[i+1] = j+1;
				j++;
				i++;
			}else if(j > 0){
				j = T[j];
			}else{
				T[i+1] = 0;
				++i;
				j=0;
			}

			c = DNA.charAt(j);
		}
		
	}
	
}
