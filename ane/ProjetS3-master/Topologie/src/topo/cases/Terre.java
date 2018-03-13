package topo.cases;

public class Terre extends Case {

	public Terre(int h) {
		super(h);
	}

	public int getCouleur() {
		int red = (0 << 16) & 0x00FF0000;
		int green = (this.hauteur << 8) & 0x0000FF00;
		int blue = 0 & 0x000000FF;
		return 0xFF000000 | red | green | blue;
	}

	public static int isSameCouleur(int rgb) {
		int r = 0;

		for (int i = 1; i < 256; i++) {
			if (rgb == new Terre(i).getCouleur()) {
				r = i;
			}
		}

		return r;
	}
}
