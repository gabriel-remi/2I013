public class Life {
	 public static int delai = 10;
	public static void main(String[] args) {
		
		Ecosystem e = new Ecosystem();
		int it = 0, nb_pas_max = 200000;		
		
		while(it < nb_pas_max) {
			//System.out.println(it);
			try {
				Thread.sleep(delai);
			} catch (InterruptedException exception) 
			{
			}
			e.run();
			it++;
		}
	}
}
