package bioalgo;

public class ExtensionRule {
	
	private Node node;
	private int rule_to_apply, edge_indx;
	
	public ExtensionRule(Node n, int rule){
		node=n;
		rule_to_apply=rule;
	}
	
	public void setIndx(int index){
		this.edge_indx=index;
	}
	
	public void grow(char next_nucl) throws Exception{
		switch (rule_to_apply) {
			case 1:
				//We just add the child at the end
				node.addNucleotide(next_nucl);
				break;
			case 2:
				//We compute the new edge
				String prev = node.getEdge();
				node.setEdge(prev.substring(0, edge_indx));
				
				//We create the first child with the rest of the previous edge
				Node n = new Node();
				n.setEdge(prev.substring(edge_indx));
				
				//When we add a new child without splitting the edge (i.e., when beta is finished but we have children and we can't add the nucleotid),
				//we just don't add it.
				if(!n.getEdge().equals(""))
				node.addChild(n);
				
				//Then we create a new child with this nucleotide
				node.addChild(next_nucl);
				break;
			case 3:
				//We do nothing!
				break;

			default:
				throw new Exception("This rule doesn't exist");
			}
	}
	
	@Override
	public String toString() {
		return "RULE "+rule_to_apply;
	}
	
}
