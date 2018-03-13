package pathfinding;

public class CasePathfinding {

	private int niv; //Hauteur
	int G; //Le coût total pour arriver sur la case
	int H; //Distance au point d'arrivée
	int F; //G+H
	public int x;
	public int y;
	CasePathfinding parent;
	
	public CasePathfinding() {
	}

	public int getNiv() {
		return niv;
	}

	public void setNiv(int niv) {
		this.niv = niv;
	};
	
	
}
