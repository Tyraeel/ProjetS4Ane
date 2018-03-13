package test;
import topo.cases.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestDepartArrivee {
	
	Case c;
	
	@Before
	public void setUp() throws Exception {
		this.c = new DepartArrivee(5);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetHauteur() {
		assertEquals("Le getter ne fonctionne pas",5,this.c.getHauteur());
	}
	
	@Test
	public void testSetHauteur(){	
		this.c.setHauteur(9);
		assertFalse("Le setteur ne fonctionne pas", this.c.getHauteur() == 5);
	}
	
	@Test
	public void testGetCouleurHauteurNulle(){
		this.c.setHauteur(0);
		assertEquals("",0xFF000000,this.c.getCouleur());
	}
	
	@Test
	public void testGetCouleurHauteurMax(){
		this.c.setHauteur(255);
		assertEquals("",0xFFFF0000,this.c.getCouleur());
	}
	
	@Test
	public void testGetCouleurHauteur127(){
		this.c.setHauteur(127);
		assertEquals("",0xFF7F0000,this.c.getCouleur());
	}
	
	@Test
	public void testIsSameCouleur127(){
		assertEquals("",127,DepartArrivee.isSameCouleur(0xFF7F0000));
	}
	
	@Test
	public void testIsSameCouleurNulle(){
		assertEquals("",0,DepartArrivee.isSameCouleur(0xFF000000));
	}
	
	@Test
	public void testIsSameCouleurMax(){
		assertEquals("",255,DepartArrivee.isSameCouleur(0xFFFF0000));
	}
}
