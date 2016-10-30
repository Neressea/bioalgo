package perfect;

public class Test {
	public static void main(String[] args) {
		
		try {
			
			SuffixTree tree = new SuffixTree("gctgca$", "tgc$");
			
			System.out.println(tree);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
