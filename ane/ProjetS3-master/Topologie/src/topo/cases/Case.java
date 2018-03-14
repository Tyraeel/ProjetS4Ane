package topo.cases;

public abstract class Case {
	
	protected int hauteur;
	
	public Case(int h) {
		this.hauteur = h;
	}

	public void setHauteur(int h) {
		this.hauteur = h;
	}
	
	public int getHauteur() {
		return this.hauteur;
	}
	
	public abstract int getCouleur();
}
