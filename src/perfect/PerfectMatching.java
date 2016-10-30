package perfect;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.fxml.BeanAdapter;

import sun.security.util.Length;

public class PerfectMatching {
	
	private BufferedReader reader;
	private final static String adapter = "TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCGTCTTCTGCTTG";
	
	//key -> the length of the string, value -> the number of matching corresponding.
	private HashMap<Integer, Integer> length_distribution;
	private int total_sequences = 0, total_matches = 0;
	private long beg_time, end_time;
	
	private String match;
	
	public static void main(String[] args) throws Exception {
		
		PerfectMatching pm = new PerfectMatching();
		
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
	
	public PerfectMatching(){
		length_distribution = new HashMap<Integer, Integer>();
	}
	
	public String test(String suff, String pref) throws Exception{
		
		SuffixTree tree = new SuffixTree(pref, suff);
		
		this.searchForPrefixSuffixMatch(tree);
		
		return this.match;
	}
	
	public void launch() throws Exception {
		
		String DNA;
		int i = 0;
		
		//We load each string
		while((DNA = reader.readLine()) != null){
			
			//For each line, we build a Generalized Suffix Tree with the adapter.
			//IMPORTANT: in our case, we consider that we search for the prefix in the first string and the suffix in the second.
			SuffixTree tree = new SuffixTree(adapter + "$", DNA + "$");
			
			//And then we search for the longest suffix/prefix match
			this.match = ""; //We init for this new strings
			this.searchForPrefixSuffixMatch(tree);
			
			if(i++ < 50){
				System.out.println(i + "  " + this.match);
			}
			
			//We memorize the length of the left fragment if a perfect match was found, in other case we update the data
			if(match != null && !match.equals("")){
				int left_length = DNA.length() - match.length();
				
				if(length_distribution.containsKey(left_length)){
					length_distribution.put(left_length, length_distribution.get(left_length) + 1);
				}else{
					length_distribution.put(left_length, 1);
				}
				
				total_matches++;
			}
			
			//Once this is done, we update the statistics
			total_sequences++;
		}
		
	}
	
	private String searchForPrefixSuffixMatch(SuffixTree tree){
		
		//We create the stack for the string we want the suffix
		ArrayList<Node> stack = new ArrayList<Node>();
		
		this.depthFirstSearch(tree.getRoot(), stack);
		
		return match;
	}
	
	private void depthFirstSearch(Node current, ArrayList<Node> stack){
		
		//If v is labeled with 1 (second string), we put it on the stack
		if(current.getTerminalEdgeIndexes().contains(new Integer(1)))
			stack.add(current);
		
		//If one of the child is a terminal edge (only $), it means that we now have a complete suffix.
		if(current.hasATerminalEdge()){
		
			//If that is the case, we search for all the other children if there is a prefix of the first string (0). It means the the position is 1.
			for (Node child : current.getChildren()) {
				
				//To have a complete prefix, we need it to be a leaf, to belongs to the first string (id == 0) and to be in first position (pos == 0)
				if(child.isLeaf() && child.getIdString() == 0 && child.getPos() == 0){
					//If that is the case, we have a suffix/prefix match, we record it with its length if it is better than a possible previous one.
					Node path;
					if((path = this.computePath(stack)).getEdge().length() > match.length()){
						match = path.getEdge();
					}
				}
			}
		}
		
		//Then we continue to go down for each node which isn't a leaf. 
		for (Node child : current.getChildren()) {
			this.depthFirstSearch(child, stack);
		}
		
		//When we go up, the element we put on the stack here, i.e. the last element
		if(stack.size() > 0)// && stack.get(stack.size()-1).equals(current))
			stack.remove(stack.size()-1);
	}
	
	public Node computePath(ArrayList<Node> path){
		Node n = new Node();
		
		for (Node c : path) {
			n.setEdge(n.getEdge() + c.getEdge());
		}
		
		return n;
	}

	public void loadFile(){
		try {
			this.reader = new BufferedReader(new FileReader("./s_3_sequence_1M.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
