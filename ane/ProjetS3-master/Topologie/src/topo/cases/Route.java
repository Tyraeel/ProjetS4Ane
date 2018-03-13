package topo.cases;

public class Route extends Case {

	public Route(int h) {
		super(h);
	}

	public int getCouleur() {
		int red = (this.hauteur << 16) & 0x00FF0000;
		int green = (this.hauteur << 8) & 0x0000FF00;
		int blue = this.hauteur & 0x000000FF;
		return 0xFF000000 | red | green | blue;
	}

	public static int isSameCouleur(int rgb) {
		int r = 0;

		for (int i = 1; i < 256; i++) {
			if (rgb == new Route(i).getCouleur()) {
				r = i;
			}
		}

		return r;
	}

}
