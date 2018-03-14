package test.topo.cases;
import topo.cases.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestPont {
	Case c;
	
	@Before
	public void setUp() throws Exception {
		this.c = new topo.cases.Pont(0);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetCouleurHauteur127(){
		this.c.setHauteur(127);
		assertEquals("",0xFF7F3F00,this.c.getCouleur());
	}
	
	@Test
	public void testGetCouleurHauteurNulle(){
		assertEquals("",0xFF000000,this.c.getCouleur());
	}
	
	@Test
	public void testGetCouleurHauteurMax(){
		this.c.setHauteur(255);
		assertEquals("",0xFFFF7F00,this.c.getCouleur());
	}
	
	@Test
	public void testIsSameCouleur127(){
		assertEquals("",127,Pont.isSameCouleur(0xFF7F3F00));
	}
	
	@Test
	public void testIsSameCouleurNulle(){
		assertEquals("",0,Pont.isSameCouleur(0xFF000000));
	}
	
	@Test
	public void testIsSameCouleurMax(){
		assertEquals("",255,Pont.isSameCouleur(0xFFFF7F00));
	}

}
