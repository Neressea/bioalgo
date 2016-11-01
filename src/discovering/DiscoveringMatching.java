package discovering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import approximate.ApproximateMatching;

public class DiscoveringMatching {
	
	private BufferedReader reader;
	
	//key -> the length of the string, value -> the number of matching corresponding.
	private long beg_time, end_time;
	private int total_sequences, read_sequences;
	private ArrayList<PWM> matrices;
	private PWM best = null;
	
	public DiscoveringMatching() {
		matrices = new ArrayList<PWM>();
		total_sequences = 1000000;
	}
	
	public static void main(String[] args) throws Exception {
		
		DiscoveringMatching pm = new DiscoveringMatching();
		String file = "./Seqset3.txt";
		
		//We load the file
		pm.loadFile(file);//"./s_1-1_1M.txt");
		
		//Then, we launch the algorithm
		pm.launch();
		
		//Then we print the info
		pm.printInfo();
		
		//Now we check if this seems correct
		ApproximateMatching am = new ApproximateMatching(pm.best.getConsensus());
		am.loadFile(file);
		am.launch();
		am.printInfo();
		am.printRepartition();
	}
	
	private void launch() throws IOException {
		
		System.out.println("###Match discovering launching...");
		
		String DNA = reader.readLine();
		this.beg_time = System.nanoTime();
		
		//We create matrices: one for each possible length (up to the total length of the string)
		for (int i = 0; i < DNA.length(); i++) {
			matrices.add(new PWM(i));
		}
		
		//variables to keep the best score in mind
		this.best = matrices.get(0);
		double best_score = this.best.getScore();
		
		//We load each string
		while(DNA!= null){
			
			//We feed each matrix
			for (int i = 0; i < matrices.size(); i++) {
				
				//We check we still continue to explore this matrix
				if(matrices.get(i) != null){
					//If that's the case, we feed it
					matrices.get(i).feed(DNA.substring(0, i));
					
					//Now we check if it is the new best value
					if(matrices.get(i).getScore() > best_score){
						this.best = matrices.get(i);
						best_score = this.best.getScore();
					}
				}
			}
			
			//Once this is done, we update the statistics
			read_sequences++;
			DNA = reader.readLine();
		}
		
		this.reader.close();
		
		this.end_time = System.nanoTime();
	}
	
	public void printInfo(){
		
		int total_time = (int) ((this.end_time - this.beg_time) / 1000000000.0);
		
		System.out.println("Statistics: ");
		System.out.println("Total time: " + total_time+"s");
		System.out.println("Number of sequences read: " + this.total_sequences);
		
		System.out.println("Consensus string:" + this.best.getConsensus()+", score value:"+this.best.getScore());
		System.out.println("###End of search");
	}

	public void loadFile(String file){
		try {
			this.reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
