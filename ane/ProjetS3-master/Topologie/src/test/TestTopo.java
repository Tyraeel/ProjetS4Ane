package test;
import topo.Topo;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTopo {
	Topo t;
	Topo topo2per2;
	@Before
	public void setUp() throws Exception {
		t = new Topo(20,20);
		topo2per2 = new Topo(2,2);

	}

	@After
	public void tearDown() throws Exception {
	}

	/*@Test
	public void testAjoutPic() {
		t.ajoutPic(143,10,10,0.8f);	
	}
	@Test
	public void testToString() {
		System.out.println(topo2per2.toString());
		assertEquals(topo2per2.image[0][0]+" "+topo2per2.image[1][0]+" \n"+topo2per2.image[0][1]+" "+topo2per2.image[1][1]+" \n", topo2per2.toString());
	}
*/
	@Test
	public void TestGetDepartArrivee(){
		topo2per2.image[0][0] = new topo.cases.DepartArrivee(127);
		topo2per2.image[1][1] = new topo.cases.DepartArrivee(127);
		int[] tab = {0,0,1,1};
		assertEquals(tab,topo2per2.getDepartArrivee());
	}
	
	/*@Test
	public void TestAjoutDepartArrivee(){
		topo2per2.ajoutDepartArrivee();
	}*/
	
}
