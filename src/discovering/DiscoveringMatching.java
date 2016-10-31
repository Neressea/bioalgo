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
	private int total_sequences;
	private ArrayList<PWM> matrices;
	private PWM best = null;
	
	public DiscoveringMatching() {
		matrices = new ArrayList<PWM>();
	}
	
	public static void main(String[] args) throws Exception {
		
		DiscoveringMatching pm = new DiscoveringMatching();
		
		//We load the file
		pm.loadFile("./s_1-1_1M.txt");
		
		//Then, we launch the algorithm
		pm.launch();
		
		//Then we print the info
		pm.printInfo();
		
		//Now we check if this seems correct
		ApproximateMatching am = new ApproximateMatching(pm.best.getConsensus());
		am.loadFile("./s_1-1_1M.txt");
		am.launch();
		am.printInfo();
	}
	
	private void launch() throws IOException {
		
		String DNA = reader.readLine();
		this.beg_time = System.nanoTime();
		
		//We create matrices: one for each possible length (up to the total length of the string)
		for (int i = 0; i < DNA.length(); i++) {
			matrices.add(new PWM(i));
		}
		
		//We load each string
		while(DNA!= null){
			
			//We feed each matrix
			for (int i = 0; i < matrices.size(); i++) {
				matrices.get(i).feed(DNA.substring(0, i));
			}
			
			//Once this is done, we update the statistics
			total_sequences++;
			DNA = reader.readLine();
		}
		
		this.reader.close();
		
		findBest();
		
		this.end_time = System.nanoTime();
	}
	
	private void findBest(){
		//Now we search for the matrix with the best score
		this.best = matrices.get(0);
		int best_score = this.best.getScore();
		
		for (int i = 0; i < matrices.size(); i++) {
			if(matrices.get(i).getScore() > best_score){
				this.best = matrices.get(i);
				best_score = this.best.getScore();
			}
		}
	}
	
	public void printInfo(){
		
		int total_time = (int) ((this.end_time - this.beg_time) / 1000000000.0);
		
		System.out.println("Statistics: ");
		System.out.println("Total time: " + total_time+"s");
		System.out.println("Number of sequences read: " + this.total_sequences);
		
		System.out.println(this.best.getConsensus()+"  "+this.best.getScore());
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
