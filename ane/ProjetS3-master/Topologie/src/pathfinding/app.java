package pathfinding;


public class app {


	
	public static void main(String[] args) {

		TopoPathfinding topo = new TopoPathfinding(5,5);
		int[] d = {0,2};
		int[] a = {4,0};
		Algo algo = new Algo(topo,d,a, 10);
		System.out.println(topo.toString());
		algo.path(2,5);
	}

}
