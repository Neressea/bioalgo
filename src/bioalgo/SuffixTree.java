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
					path.grow(next_nucl);
				}
				
				this.addChild(next_nucl);
			}
		}
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
	
	/*public void expands(String beta, int string, int index){
			
		//We search the edge beginning with the current suffixe
		
		SuffixTree child = children.get(0);
		for (SuffixTree st : children) {
			if(st.edgeBeginWith(beta.charAt(0))){
				child = st;
				break;
			}
		}
		
		//When we get it, we see how many nucleotids we matched
		int i=0, j=0;
		while(i < child.edge.length() && j < beta.length() - 1) j = ++i;
		
		//If we are at the end of both beta and edge, we just add this new char
		if(i == child.edge.length() - 1 && j == beta.length() - 2){
			
			//We remove all unnecessary characters
			beta = beta.charAt(beta.length() - 1) + "";
			
			//And we add it to the edge
			child.edge+=beta;
			
			
		//If we are at the end of beta but not of the edge, we have to create a new intermediate node
		}else if(j == beta.length() - 2){
			
			//We create a new node containing the nucleotid left
			String new_edge = child.edge.substring(0, i);
			SuffixTree new_child = new SuffixTree(new_edge);
			
			//We moves this node's children to this new intermediate node
			new_child.children = this.children;
			
			//And we create the new list of children, with just the intermediate node and the new leaf wwith le last characters of beta
			this.children = new ArrayList<SuffixTree>();
			this.children.add(new_child);
			this.children.add(new SuffixTree(beta.charAt(beta.length()-1) + ""));
			
			//We update beta
			beta = beta.substring(beta.length()-1);
		
		//If we are at the end of edge but not beta, we must continue our search
		}else{
			
			//We remove all unnecessary characters
			beta = beta.substring(i+1);
			
			//We recurse
			child.expands(beta, string, index);
			
		}
		
	}*/
	
	

	@Override
	public String toString() {
		return "Ukonnen tree: " + root + "";
	}
	
}
