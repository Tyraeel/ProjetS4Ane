package test;
import topo.cases.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestRoute {
	Case c;
	@Before
	public void setUp() throws Exception {
		this.c = new topo.cases.Route(0);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testGetCouleurHauteur127(){
		this.c.setHauteur(127);
		assertEquals("",0xFF7F7F7F,this.c.getCouleur());
	}
	
	@Test
	public void testGetCouleurHauteurNulle(){
		assertEquals("",0xFF000000,this.c.getCouleur());
	}
	
	@Test
	public void testGetCouleurHauteurMax(){
		this.c.setHauteur(255);
		assertEquals("",0xFFFFFFFF,this.c.getCouleur());
	}
	
	@Test
	public void testIsSameCouleur127(){
		assertEquals("",127,Route.isSameCouleur(0xFF7F7F7F));
	}
	
	@Test
	public void testIsSameCouleurNulle(){
		assertEquals("",0,Route.isSameCouleur(0xFF000000));
	}
	
	@Test
	public void testIsSameCouleurMax(){
		assertEquals("",255,Route.isSameCouleur(0xFFFFFFFF));
	}

}
