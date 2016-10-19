package bioalgo;

public class Main {
	public static void main(String[] args) {
		
		try {
			SuffixTree tree = new SuffixTree("gctgc$");
			
			System.out.println(tree);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
