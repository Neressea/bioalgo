package approximate;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import perfect.PerfectMatching;
import perfect.SuffixTree;

public class ApproximateMatching {
	
	private BufferedReader reader;
	private String adapter;
	
	//key -> the length of the string, value -> the number of matching corresponding.
	private HashMap<Integer, Integer> length_distribution;
	private int total_sequences;
	private long beg_time, end_time;
	private double total_matches;
	
	public static void main(String[] args) throws IOException {
		ApproximateMatching pm = new ApproximateMatching("TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCGTCTTCTGCTTG");
		
		//We load the file
		pm.loadFile("s_3_sequence_1M.txt");
		
		//Then, we launch the algorithm
		pm.launch();
		
		pm.printInfo();
		pm.printRepartition();
	}
	
	public void printInfo(){
		int total_time = (int) ((this.end_time - this.beg_time) / 1000000000.0);
		double average_time = (total_time * 1.0) / this.total_sequences;
		double percentage_matching = (this.total_matches * 1.0 / this.total_sequences) * 100;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		
		System.out.println("Statistics: ");
		System.out.println("Total time: " + total_time+"s");
		System.out.println("Number of sequences read: " + this.total_sequences);
		System.out.println("Number of matching sequences: " + this.total_matches);
		System.out.println("Average time per DNA sequence: " + average_time+"s");
		System.out.println("Percentage of sequences with a match: " + numberFormat.format(percentage_matching)+"%");
		System.out.println("###End of approximate matching");
	}
	
	public void printRepartition(){
		System.out.println("\n\n\n");
		System.out.println("Length distribution: ");
		
		for (Integer length : this.length_distribution.keySet()) {
			int number_of_sequences = this.length_distribution.get(length);
			System.out.println("Number of sequences of length " + length + ": " + number_of_sequences);
		}
	}
	
	public ApproximateMatching(String adapter){
		length_distribution = new HashMap<Integer, Integer>();
		this.adapter = adapter;
	}
	
	public void launch() throws IOException{
		System.out.println("###Approximate matching launching...");
		this.beg_time = System.nanoTime();
		
		String DNA;
		int i = 0, k=0;
		
		//We load each string
		while((DNA = reader.readLine()) != null){
			//For each string, we create a KMP
			DPApprox kmp = new DPApprox(adapter, DNA, 0.25);
			String match = kmp.findMatch();
			
			//We memorize the length of the left fragment if a perfect match was found, in other case we update the data
			if(match != null && !match.equals("")){
				int left_length = DNA.length() - match.length();
				
				if(length_distribution.containsKey(left_length)){
					length_distribution.put(left_length, length_distribution.get(left_length) + 1);
				}else{
					length_distribution.put(left_length, 1);
				}
				
				//Once this is done, we update the statistics
				total_matches++;
			}
			total_sequences++;
		}
		
		this.end_time = System.nanoTime();
		this.reader.close();
	}
	
	public void setAdapter(String s){
		this.adapter=s;
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
