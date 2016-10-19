package bioalgo;

public class Main {
	public static void main(String[] args) {
		
		try {
			
			SuffixTree tree = new SuffixTree("gctgca$", "tgc$");
			
			System.out.println(tree);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
