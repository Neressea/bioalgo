package approximate;

public class KMP {
	
	private String adapter, DNA;
	private double authorized_error_rate;
	private int T[];
	private int nb_errors;
	
	public KMP(String adapter, String DNA, double authorized_error_rate, int str){
		this.adapter=adapter;
		this.DNA=DNA;
		this.authorized_error_rate = authorized_error_rate;
		
		/*int M = DNA.length();
        int N = adapter.length();
 
        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        this.T = new int[M];
        int j = 0;  // index for pat[]
 
        // Preprocess the pattern (calculate lps[]
        // array)
        this.buildTab();
 
        int i = 0;  // index for txt[]
        while (i < N)
        {
            if (DNA.charAt(j) == adapter.charAt(i)){
                j++;
                i++;
            }
            if (j == M){
                System.out.println("Found pattern at index " + (i-j) + " of string " + str);
                j = this.T[j-1];
            }
 
            // mismatch after j matches
            else if (i < N && DNA.charAt(j) != adapter.charAt(i))
            {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = this.T[j-1];
                else
                    i = i+1;
            }
        }*/
	}
	
	public String findMatch(){
		
		this.buildTab();
		int index = this.searchIndex();
		
		if(index == -1){
			return null;
		}else{
			return DNA.substring(index);
			//return adapter.substring(0, DNA.length() - index);
		}
	}
	
	public int searchIndex(){
		
		int m = 0, //beginning index in the array
				i = 0; //shift from the index
		
		while(m+i < adapter.length() && i < DNA.length()){
			if(adapter.charAt(m+i) == DNA.charAt(i)){
				i++;
			}else{
				nb_errors++;
				if(i != 0 && (nb_errors / (i*1.0)) <= authorized_error_rate){
					i++;
				}else{
					m+=i-this.T[i];
					
					if(i>0)
						i = this.T[i];
				}
			}	
		}

		if(i == DNA.length())
			return m;
		
		return -1;
	}
	
	public void buildTab(){
		
		int i=0, j=-1;
		T = new int[DNA.length()];
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
		
		/*this.T = new int[DNA.length()+1];
		
		// length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        this.T[0] = 0;  // lps[0] is always 0
 
        // the loop calculates lps[i] for i = 1 to M-1
        while (i < this.DNA.length())
        {
            if (this.DNA.charAt(i) == this.DNA.charAt(len))
            {
                len++;
                this.T[i] = len;
                i++;
            }
            else  // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar 
                // to search step.
                if (len != 0)
                {
                    len = this.T[len-1];
 
                    // Also, note that we do not increment
                    // i here
                }
                else  // if (len == 0)
                {
                    this.T[i] = len;
                    i++;
                }
            }
        }*/
		
	}
	
}
