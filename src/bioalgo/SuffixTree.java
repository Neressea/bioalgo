package bioalgo;

import java.util.ArrayList;

public class SuffixTree {
	
	/// DATA FOR INTERNAL NODES
	
	/**
	 * The value (nucleotids) of the edge leading to this node. The root node has an empty edge, and 
	 * the leaf have only an edge finishing with $.
	 */
	private String edge;
	
	/**
	 * The successors of this node in the tree. If the list is empty, it is a leaf. 
	 */
	private ArrayList<SuffixTree> children;
	
	/// DATA FOR LEAFS ///
	
	/**
	 * The id of the string this edge belongs to. 
	 */
	private int id_string;
	
	/**
	 * The position of the beginning of the suffix in this string.
	 */
	private int pos;
	
	
	
	
	public SuffixTree(){
		children = new ArrayList<SuffixTree>();
		this.edge="";
	}
	
	public SuffixTree(String edge) {
		this();
		this.edge = edge;
	}

	/**
	 * Creates a generalized suffix tree from the list of strings it receives in parameters.
	 * @param strings
	 * @return
	 */
	public static SuffixTree ukkonen(String... strings){
		
		SuffixTree st = new SuffixTree();
		
		//We add the first node
		st.children.add(new SuffixTree(strings[0].charAt(0)+""));
		
		//We expand the tree for each strings
		for (int i = 0; i < strings.length; i++) {
			
			//We expand it for each characters
			for (int j = 0; j < strings[i].length(); j++) {
				
				st.expands(strings[i].substring(0, j+1), i, j);
				
			}
			
		}
		
		return st;
	}
	
	public boolean isLeaf(){
		return children.isEmpty() && edge.charAt(edge.length() - 1) == '$';
	}
	
	public void expands(String beta, int string, int index){
		
		System.out.println(beta + " " + this+"\n\n");
			
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
		
	}
	
	public boolean edgeBeginWith(char c){
		return c == edge.charAt(0);
	}
	
	@Override
	public String toString() {
		
		String s = " [ " + edge + "";
		
		for (int i = 0; i < children.size(); i++) {
			s += "==>" + children.toString();
		}
		
		return s + "]";
	}
	
}
