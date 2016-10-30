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
	private final static String adapter = "TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCGTCTTCTGCTTG";
	
	//key -> the length of the string, value -> the number of matching corresponding.
	private HashMap<Integer, Integer> length_distribution;
	private int total_sequences;
	private long beg_time, end_time;
	private double total_matches;
	
	public static void main(String[] args) throws IOException {
		ApproximateMatching pm = new ApproximateMatching();
		
		//We load the file
		pm.loadFile();
		
		pm.beg_time = System.nanoTime();
		
		//Then, we launch the algorithm
		pm.launch();
		pm.end_time = System.nanoTime();
		
		pm.reader.close();
		
		int total_time = (int) ((pm.end_time - pm.beg_time) / 1000000000.0);
		double average_time = (total_time * 1.0) / pm.total_sequences;
		double percentage_matching = (pm.total_matches * 1.0 / pm.total_sequences) * 100;
		DecimalFormat numberFormat = new DecimalFormat("#.00");
		
		System.out.println("Statistics: ");
		System.out.println("Total time: " + total_time);
		System.out.println("Number of sequences read: " + pm.total_sequences);
		System.out.println("Number of matching sequences: " + pm.total_matches);
		System.out.println("Average time per DNA sequence: " + average_time);
		System.out.println("Percentage of sequences with a match: " + numberFormat.format(percentage_matching));
		
		System.out.println("\n\n\n");
		System.out.println("Length distribution: ");
		
		for (Integer length : pm.length_distribution.keySet()) {
			int number_of_sequences = pm.length_distribution.get(length);
			System.out.println("Number of sequences of length " + length + ": " + number_of_sequences);
		}
	}
	
	public ApproximateMatching(){
		length_distribution = new HashMap<Integer, Integer>();
	}
	
	public void launch() throws IOException{
		String DNA;
		int i = 0, k=0;
		
		//We load each string
		while((DNA = reader.readLine()) != null){
			//For each string, we create a KMP
			DPApprox kmp = new DPApprox(adapter, DNA, 0.10);
			String match = kmp.findMatch();
			
			if(i++ < 50){
				System.out.println(i + "  " + match);
			}
			
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
	}
	
	public void loadFile(){
		try {
			this.reader = new BufferedReader(new FileReader("s_3_sequence_1M.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
