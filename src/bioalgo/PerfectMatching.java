package bioalgo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.javafx.fxml.BeanAdapter;

public class PerfectMatching {
	
	private BufferedReader reader;
	private final static String adapter = "TGGAATTCTCGGGTGCCAAGGAACTCCAGTCACACAGTGATCTCGTATGCCGTCTTCTGCTTG";
	
	//key -> the length of the string, value -> the number of matching corresponding.
	private HashMap<Integer, Integer> length_distribution;
	private int total_sequences;
	private long beg_time, end_time;
	
	public static void main(String[] args) throws Exception {
		
		PerfectMatching pm = new PerfectMatching();
		
		//We load the file
		pm.loadFile();
		
		pm.beg_time = System.nanoTime();
		//Then, we launch the algorithm
		pm.launch();
		pm.beg_time = System.nanoTime();
	}
	
	private void launch() throws Exception {
		
		String DNA;
		
		//We load each string
		while((DNA = reader.readLine()) != null){
			
			//For each line, we build a Generalized Suffix Tree with the adapter
			SuffixTree tree = new SuffixTree(DNA, adapter);
			
			//And then we search for the longest suffix/prefix match
			String match = this.searchForPrefixSuffixMatch(tree);
			
			//We memorize the length of the left fragment if it isn't already, in other case we update the data
			int left_length = DNA.length() - match.length();
			
			if(length_distribution.containsKey(left_length)){
				length_distribution.put(left_length, length_distribution.get(left_length) + 1);
			}else{
				length_distribution.put(left_length, 1);
			}
			
			//Once this is done, we update the statistics
			total_sequences++;
		}
		
	}
	
	private String searchForPrefixSuffixMatch(SuffixTree tree){
		
		//We create the stacks fot the two strings
		ArrayList<ArrayList<Node>> stacks = new ArrayList<ArrayList<Node>>();
		stacks.add(new ArrayList<Node>());
		stacks.add(new ArrayList<Node>());
		
		this.depthFirstSearch(tree.getRoot(), stacks);
		
		return null;
	}
	
	private void depthFirstSearch(Node current, ArrayList<ArrayList<Node>> stacks){
		
		if(current.isLeaf()){
			stacks.get(0).add(e);
			return;
		}
		
		//First, we manage the stacks
		if(current.getTerminalEdgeIndexes().contains(1))
			stacks.get(1).add(current);
		
		if(current.getTerminalEdgeIndexes().contains(0))
			stacks.get(0).add(current);
		
		//Then we continue to the leaf, for each child
		for (int i = 0; i < current.getChildren().size(); i++) {
			Node child = current.getChildren().get(i);
			
			//We go down to the leaf.
			this.depthFirstSearch(child, stacks);
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
