package bioalgo;

import java.util.ArrayList;

public class SuffixTree {
	
	private Node root;
	
	public SuffixTree(){
		root = new Node();
	}

	/**
	 * Creates a generalized suffix tree from the list of strings it receives in parameters.
	 * @param strings
	 * @return
	 * @throws Exception 
	 */
	public SuffixTree(String... strings) throws Exception{
		
		this();

		//We create I1
		char c = strings[0].charAt(0);
		root.addChild(c);
		
		//We expand the tree for each strings
		for (int k = 0; k < strings.length; k++) {
			
			//We expand it for each characters, excpet the first one of the first string
			for (int i = 0; i < strings[k].length() - 1; i++) {
				
				char next_nucl = strings[k].charAt(i+1);
				
				for(int j = 0; j<i+1; j++){
					String label = strings[k].substring(j, i+1);
					ExtensionRule path=root.findEndPath(label, next_nucl);
					//If needed, we extand the current path with the nucl. i+1
					path.grow(next_nucl, k, j);
				}
				
				if(!isTerminal(next_nucl))
					this.addChild(next_nucl);
			}
		}
		
		//Now, we finally create the indexes of terminal nodes
		SuffixTree.generateTerminalEdgeIndexes(root);
	}
	
	private static ArrayList<Integer> generateTerminalEdgeIndexes(Node node){
		
		//If it is a terminal, we just return the ID of its string
		if(node.isTerminal()){
			ArrayList<Integer> l = new ArrayList<Integer>();
			l.add(node.getIdString());
			return l;
		}else if(node.isLeaf()){
			return new ArrayList<Integer>(); //This isn't a terminal edge, we don't care about it
		}
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		
		//If ot is an internal, we change the indexes of this node and we propagate the new indexes to the parent
		for (int i = 0; i < node.getChildren().size(); i++) {
			ArrayList<Integer> idx = SuffixTree.generateTerminalEdgeIndexes(node.getChildren().get(i));
			for (int j = 0; j < idx.size(); j++) {
				//If we already have this index, we ignore it.
				if(!indexes.contains(idx.get(j))){
					indexes.add(idx.get(j));
					
					//We add this index to the node
					node.addTerminalEdgeIndex(idx.get(j));
				}
			}
		}
		
		return indexes;
		
	}
	
	public static boolean isTerminal(char nucleotid){
		return nucleotid == '$' || nucleotid == '€';
	}
	
	private void addChild(char next_nucl) {

		int i=0;
		
		while(i<root.getChildren().size()){
			if(root.getChildren().get(i).edgeBeginsWith(next_nucl))
				return;
			
			i++;
		}
		
		root.addChild(next_nucl);
	}

	@Override
	public String toString() {
		return "Ukonnen tree: " + root + "";
	}
	
	public Node getRoot(){
		return root;
	}
	
}
