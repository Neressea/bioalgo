package bioalgo;

import java.util.ArrayList;

public class Node {
	/// DATA FOR INTERNAL NODES
	
		/**
		 * The value (nucleotids) of the edge leading to this node. The root node has an empty edge, and 
		 * the leaf have only an edge finishing with $.
		 */
		private String edge;
		
		/**
		 * The successors of this node in the tree. If the list is empty, it is a leaf. 
		 */
		private ArrayList<Node> children;
		
		/// DATA FOR LEAFS ///
		
		/**
		 * The id of the string this edge belongs to. 
		 */
		private int id_string;
		
		/**
		 * The position of the beginning of the suffix in this string.
		 */
		private int pos;
		
		
		public Node(){
			children = new ArrayList<Node>();
			edge="";
		}
		
		public Node(char beg_edge){
			this();
			this.edge = beg_edge+"";
		}
		
		public Node(char beg_edge, int id_string, int pos){
			this(beg_edge);
			this.id_string = id_string;
			this.pos = pos;
		}
		
		public ExtensionRule findEndPath(String label, char next_nucl){
			
			for (int i = 0; i < children.size(); i++) {
				ExtensionRule er = children.get(i).recursiveFindEndPath(label, next_nucl);
				if(er != null)
					return er;
			}
			
			return null;
		}
		
		public ExtensionRule recursiveFindEndPath(String label, char next_nucl){
			
			ExtensionRule end = null;
						
			//The indexes in both strings
			int idx_label=0, idx_edge=0;
			while(idx_label < label.length() && idx_edge < edge.length()){
				
				//If we don't find the path, then there is no match
				if(label.charAt(idx_label) != edge.charAt(idx_edge)){
					return null;
				}
				
				//We increase the two values
				idx_label = ++idx_edge;
			}
			
			//If we are at the end of the edge, but not of the label, we continue our search
			if(idx_label < label.length() && idx_edge == edge.length()){
				
				//We search for the child that begins with this path
				Node child = null;
				
				for (int i = 0; i < children.size(); i++) {
					if(children.get(i).edgeBeginsWith(label.charAt(idx_label))){
						child = children.get(i);
						break;
					}
				}
				
				if(child == null) return null;
				
				end = child.recursiveFindEndPath(label.substring(idx_label), next_nucl);
			}
						
			//If we are at the end of both the label and the edge, and there is no child, beta ends at leaf and we apply the first rule
			if(idx_label == label.length() && idx_edge == edge.length()){
				if(this.children.isEmpty())
					end = new ExtensionRule(this, 1);
				else{
					end = new ExtensionRule(this, 2);
					end.setIndx(edge.length());
				}
			}
			
			//If we are at the end of the label but not the edge, we have to check if the nucl already exists or if we have to create a new child
			if(idx_label == label.length() && idx_edge < edge.length()){
				
				//If the next nucl is the nucl we want to add, we apply the rule three: we do nothing.
				if(edge.charAt(idx_edge) == next_nucl)
					end = new ExtensionRule(this, 3);
				else{
					end = new ExtensionRule(this, 2); //Otherwise, we apply the rule 2: we create a new child
					end.setIndx(idx_edge); //We precise to the rule were it must split the tree
				}
				
			}
			
			return end;
		}

		public boolean edgeBeginsWith(char nucl){
			return edge.charAt(0) == nucl;
		}
		
		public boolean isLeaf(){
			return this.children.isEmpty() && (edge.charAt(edge.length() - 1) == '$' || edge.charAt(edge.length() - 1) == '€');
		}
		
		public boolean isTerminal(){
			return children.isEmpty() && edge.equals("$");
		}
		
		public boolean isRoot(){
			return edge.isEmpty();
		}
		
		public void addChild(char nucleotid){
			children.add(new Node(nucleotid));
		}
		
		public void addNucleotide(char nucl){
			this.edge+=nucl;
		}
		
		//// GETTERS AND SETTERS
		
		
		@Override
		public String toString() {
			
			String s="";
			if(isRoot()){
				s = "ROOT [{";
			}else if(isLeaf()){
				s = "Leaf [edge=" + edge + " (" + id_string + ", " + pos + ") {";
			}else{
				s = "Node [edge=" + edge + ", children={";
			}
			
			for (int i = 0; i < children.size(); i++) {
				s+=children.get(i)+", ";
			}
			
			return s + "}]";
		}

		public String getEdge() {
			return edge;
		}

		public void setEdge(String edge) {
			this.edge = edge;
		}

		public ArrayList<Node> getChildren() {
			return children;
		}

		public void setChildren(ArrayList<Node> children) {
			this.children = children;
		}

		public int getIdString() {
			return id_string;
		}

		public void setIdString(int id_string) {
			this.id_string = id_string;
		}

		public int getPos() {
			return pos;
		}

		public void setPos(int pos) {
			this.pos = pos;
		}

		public void addChild(Node n) {
			children.add(n);
		}
}
