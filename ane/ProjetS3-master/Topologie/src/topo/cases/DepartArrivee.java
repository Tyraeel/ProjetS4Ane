package topo.cases;

public class DepartArrivee extends Case {

	public DepartArrivee(int h) {
			super(h);
		}

	public int getCouleur() {
		int red = (this.hauteur << 16) & 0x00FF0000;
		int green = (0 << 8) & 0x0000FF00;
		int blue = 0 & 0x000000FF;
		return 0xFF000000 | red | green | blue;
	}

	public static int isSameCouleur(int rgb) {
			int r = 0;

			for (int i = 1; i < 256; i++) {
				if (rgb == new DepartArrivee(i).getCouleur()) {
					r = i;
				}
			}
			return r;
	}
	
	public static void main(String[] args)
	{
		System.out.println(new DepartArrivee(5).getCouleur());
		System.out.println(new DepartArrivee(5).getHauteur() << 16);
		System.out.println(new DepartArrivee(5).getHauteur() << 16 & 0x00FF0000);
		System.out.println(((0 << 8) & 0x0000FF00) + " " + (0 & 0x000000FF));
	}
}
