package pathfinding;

public class TopoPathfinding {

	private CasePathfinding[][] matrice;

	int largeur;
	int hauteur;

	public TopoPathfinding(int largeur, int hauteur) {
		this.matrice = new CasePathfinding[largeur][hauteur];
		this.largeur = largeur;
		this.hauteur = hauteur;

		/*
		 * for (int i = 0; i < this.largeur; i++) { for (int j = 0; j <
		 * this.hauteur; j++) { this.matrice[i][j] = new Case();
		 * this.matrice[i][j].niv = 2+i+j; } }
		 */
		for (int i = 0; i < this.largeur; i++) {
			for (int j = 0; j < this.hauteur; j++) {
				this.matrice[i][j] = new CasePathfinding();
				if ((i == 2 || i == 3)) {
					this.matrice[i][j].setNiv(15);
				} else {
					this.matrice[i][j].setNiv(0);
				}
			}
		}
	}

	public CasePathfinding find(int x, int y) {
		return this.matrice[x][y];
	}

	public CasePathfinding[][] getMatrice() {
		return matrice;
	}

	public void ToString() {
		String x = "";
		for (int i = 0; i < this.largeur; i++) {
			for (int j = 0; j < this.hauteur; j++) {
				x = x + this.matrice[i][j].getNiv() + " ";
			}
			System.out.println(x);
			x = "";
		}
	}

}
