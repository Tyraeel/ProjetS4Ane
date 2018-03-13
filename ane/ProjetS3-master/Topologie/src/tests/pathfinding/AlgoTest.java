package tests.pathfinding;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AlgoTest {
	
	private pathfinding.Algo algo;
	
	@Before
	public void setUp() throws Exception {
		
		int[] depart = {2, 2};
		this.algo = new pathfinding.Algo(new pathfinding.TopoPathfinding(24, 24), depart, null, 5);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFaireRoute() {
		
		List<pathfinding.CasePathfinding> chemin = new ArrayList<pathfinding.CasePathfinding>();
		this.algo.setC(new pathfinding.CasePathfinding());
	    pathfinding.CasePathfinding c = this.algo.getC();
	    c.x = 2;
	    c.y = 2;
		chemin.add(0, c);
		
		assertEquals("Le chemin n'est pas bon", this.algo.faireRoute(), chemin);
	}

}
