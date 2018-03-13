package topo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;

import pathfinding.Algo;
import pathfinding.CasePathfinding;
import pathfinding.TopoPathfinding;
import topo.cases.Case;
import topo.cases.DepartArrivee;
import topo.cases.Pont;
import topo.cases.Riviere;
import topo.cases.Route;
import topo.cases.Terre;
import topo.cases.Tunnel;

public class Topo {

	public Case[][] image;

	public Topo(int x, int y) {
		this.image = new Case[x][y];
		for (int i = 0; i < this.image.length; i++) {
			for (int j = 0; j < this.image.length; j++) {
				this.image[i][j] = new Terre(127);
			}
		}
	}

	public void ajoutPic(float hauteur, int x, int y, float pente) { //crée un pic d'hauteur hauteur, et si c'est possible crée des pics autour en prenant en compte pente
		if (hauteur > 0 && x >= 0 && x < this.image.length && y >= 0 && y < this.image.length
				&& this.image[x][y].getHauteur() <= hauteur) {

			int temp = (int) ((hauteur - (hauteur * pente)) / 2 + (hauteur * pente)); //calcule la prochaine hauteur 
			this.image[x][y].setHauteur((int) hauteur);

			ajoutPic(temp, x + 1, y, pente);
			ajoutPic(temp, x - 1, y, pente);
			ajoutPic(temp, x, y - 1, pente);
			ajoutPic(temp, x, y + 1, pente);
		}
	}

	public void ajoutChaineMontagne(int x1, int y1, int x2, int y2, int nb) {
		int x3 = (x2 - x1) / nb;
		int y3 = (y2 - y1) / nb;

		Random rand = new Random();

		for (int i = 0; i < nb; i++) {
			int x = x1 + (x3 * i) + (x3 / 2);
			int y = y1 + (y3 * i) + (x3 / 2);

			this.ajoutPic(rand.nextInt(55) + 200, x, y, (rand.nextInt(12) + 80) / 100f);
		}
	}

	public void ajoutCreux(float hauteur, int x, int y, float pente) {
		if (hauteur > 0 && x >= 0 && x < this.image.length && y >= 0 && y < this.image.length
				&& this.image[x][y].getHauteur() > hauteur) {

			int temp = (int) ((hauteur - (hauteur / pente)) / 2 + (hauteur / pente) + 1);
			this.image[x][y].setHauteur((int) hauteur - 1);

			ajoutCreux(temp, x + 1, y, pente);
			ajoutCreux(temp, x - 1, y, pente);
			ajoutCreux(temp, x, y - 1, pente);
			ajoutCreux(temp, x, y + 1, pente);
		}
	}

	public void ajoutFalaise(int x1, int y1, int x2, int y2, int nb) {
		int x3 = (x2 - x1) / nb;
		int y3 = (y2 - y1) / nb;

		Random rand = new Random();

		for (int i = 0; i < nb; i++) {
			int x = x1 + (x3 * i) + (x3 / 2);
			int y = y1 + (y3 * i) + (x3 / 2);
			this.ajoutCreux(rand.nextInt(55), x, y, (rand.nextInt(12) + 60) / 100f);
		}
	}

	public void ajoutPente(int x1, int y1, int x2, int y2, int nb) { //set la hauteur d'un rectangle ABCD à nb, on donne en paramètre A et C
		for (int x = x1; x < x2 + 1; x++) {
			for (int y = y1; y < y2 + 1; y++) {
				this.image[x][y].setHauteur(nb);
			}
		}
	}

	public void ajoutRiviere(int x, int y) { //crée une rivière, a chaque itération place une case rivière a la case la plus base en altitude
		// pas d'image vide					//si la rivière arrive au bord de TOPO ou dans un creux, elle sarrète
		this.image[x][y] = new Riviere(this.image[x][y].getHauteur());

		int[] caseVoisines = new int[4];

		// Si une case d'� c�t� est hors du tableau, la rivi�re
		// s'arr�te la.
		if (y > 0) {
			caseVoisines[0] = this.image[x][y - 1].getHauteur(); // haut
		} else {
			return;
		}

		if (x < this.image.length - 1) {
			caseVoisines[1] = this.image[x + 1][y].getHauteur(); // droite
		} else {
			return;
		}

		if (y < this.image[0].length - 1) {
			caseVoisines[2] = this.image[x][y + 1].getHauteur(); // bas
		} else {
			return;
		}

		if (x > 0) {
			caseVoisines[3] = this.image[x - 1][y].getHauteur(); // gauche
		} else {
			return;
		}

		if (!(this.image[x][y - 1] instanceof Riviere) && caseVoisines[0] <= this.image[x][y].getHauteur()
				&& caseVoisines[0] <= caseVoisines[1] && caseVoisines[0] <= caseVoisines[2]
				&& caseVoisines[0] <= caseVoisines[3]) {
			ajoutRiviere(x, y - 1);
		} else if (!(this.image[x + 1][y] instanceof Riviere) && caseVoisines[1] <= this.image[x][y].getHauteur()
				&& caseVoisines[1] <= caseVoisines[0] && caseVoisines[1] <= caseVoisines[2]
				&& caseVoisines[1] <= caseVoisines[3]) {
			ajoutRiviere(x + 1, y);
		} else if (!(this.image[x][y + 1] instanceof Riviere) && caseVoisines[2] <= this.image[x][y].getHauteur()
				&& caseVoisines[2] <= caseVoisines[1] && caseVoisines[2] <= caseVoisines[0]
				&& caseVoisines[2] <= caseVoisines[3]) {
			ajoutRiviere(x, y + 1);
		} else if (!(this.image[x - 1][y] instanceof Riviere) && caseVoisines[3] <= this.image[x][y].getHauteur()
				&& caseVoisines[3] <= caseVoisines[1] && caseVoisines[3] <= caseVoisines[2]
				&& caseVoisines[3] <= caseVoisines[0]) {
			ajoutRiviere(x - 1, y);
		}

	}

	public void toFile(String nom) {
		BufferedImage img = new BufferedImage(this.image.length, this.image[0].length, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < this.image.length; i++) {
			for (int j = 0; j < this.image.length; j++) {
				img.setRGB(i, j, this.image[i][j].getCouleur());
			}
		}

		try {
			File f = new File(nom + ".png");
			ImageIO.write(img, "PNG", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		String r = "";

		for (int i = 0; i < this.image.length; i++) {
			for (int j = 0; j < this.image.length; j++) {
				r += this.image[i][j] + " ";
			}

			r += "\n";
		}
		return r;
	}

	public static void main(String[] args) {
		Topo nouvelle = new Topo(128, 128);
		nouvelle.genererTopologie();
		nouvelle.toFile("test");
		nouvelle.importerTopologie("test.png");
		nouvelle.toFile("test2");
	}

	public static Topo topoAleatoire() {
		Topo topo = new Topo(320, 320);
		topo.genererTopologie();
		topo.toFile(FileSystemView.getFileSystemView().getRoots()[0] + "\\topo");
		return topo;
	}

	public void genererTopologie() {
		// 1 Chaine de montagne par 64 pixels
		// 1 Montagne par 16 pixels
		// 1 Falaise par 128 pixels
		// 1 Creux par 32 Pixels
		// 1 riviere par 32 pixels

		this.mettrePanchaison();

		this.genererAleatoirementChaines();

		this.genererAleatoirementPics();

		this.genererAleatoirementFalaises();

		this.genererAleatoirementCreux();

		this.genererAleatoirementRivieres();

		this.ajoutDepartArrivee();
		
		this.ajoutFakeRiviere();
	}

	private void ajoutFakeRiviere() {
		for (int i = 0; i < this.image.length; i++) {
			this.image[this.image.length/2][i] = new Riviere(this.image[this.image.length/2][i].getHauteur());
		}
		
	}

	private void mettrePanchaison() {
		Random rand = new Random();
		int angle = rand.nextInt(8);

		switch (angle) {
		case 0:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = -(i + j + 1 - this.image.length) / (this.image.length / 32);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 1:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = -2 * (i - (this.image.length) / 2) / (this.image.length / 32);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 2:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = (i - j) / (this.image.length);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 3:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = -2 * (j - (this.image.length) / 2) / (this.image.length / 32);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 4:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = (i + j + 1 - this.image.length) / (this.image.length / 32);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 5:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = 2 * (i - (this.image.length) / 2) / (this.image.length / 32);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 6:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = (i - j) / (this.image.length);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		case 7:
			for (int i = 0; i < this.image.length; i++) {
				for (int j = 0; j < this.image.length; j++) {
					int v = 2 * (j - (this.image.length) / 2) / (this.image.length / 32);
					this.image[i][j].setHauteur(127 + v);
				}
			}
			break;
		}
	}

	private void genererAleatoirementRivieres() {
		Random rand = new Random();
		if (this.image.length >= 32) {
			for (int i = 0; i < this.image.length / 32; i++) {
				this.ajoutRiviere(rand.nextInt(this.image.length), rand.nextInt(this.image.length));
			}
		}
	}

	private void genererAleatoirementCreux() {
		Random rand = new Random();
		if (this.image.length >= 32) {
			for (int i = 0; i < this.image.length / 32; i++) {
				this.ajoutCreux(rand.nextInt(55), rand.nextInt(this.image.length), rand.nextInt(this.image.length),
						(rand.nextInt(12) + 60) / 100f);
			}
		}
	}

	private void genererAleatoirementFalaises() {
		Random rand = new Random();
		if (this.image.length >= 128) {
			for (int i = 0; i < this.image.length / 128; i++) {
				int x1 = rand.nextInt(this.image.length);
				int y1 = rand.nextInt(this.image.length);
				int x2 = rand.nextInt(this.image.length);
				int y2 = rand.nextInt(this.image.length);

				this.ajoutFalaise(x1, y1, x2, y2,
						((int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) / 8) + 1);
			}
		}
	}

	private void genererAleatoirementPics() {
		Random rand = new Random();
		if (this.image.length >= 16) {
			for (int i = 0; i < this.image.length / 16; i++) {
				this.ajoutPic(rand.nextInt(64) + 191, rand.nextInt(this.image.length), rand.nextInt(this.image.length),
						(rand.nextInt(12) + 80) / 100f);
			}
		}
	}

	private void genererAleatoirementChaines() {
		Random rand = new Random();
		if (this.image.length >= 64) {
			for (int i = 0; i < this.image.length / 64; i++) {
				int x1 = rand.nextInt(this.image.length);
				int y1 = rand.nextInt(this.image.length);
				int x2 = rand.nextInt(this.image.length);
				int y2 = rand.nextInt(this.image.length);

				this.ajoutChaineMontagne(x1, y1, x2, y2,
						((int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) / 8) + 1);
			}
		}
	}

	private void ajoutDepartArrivee() {
		Random rand = new Random();
		int x1 = rand.nextInt(this.image.length);
		int y1 = rand.nextInt(this.image[0].length);
		int x2 = rand.nextInt(this.image.length);
		int y2 = rand.nextInt(this.image[0].length);

		this.image[x1][y1] = new DepartArrivee(this.image[x1][y1].getHauteur());
		this.image[x2][y2] = new DepartArrivee(this.image[x2][y2].getHauteur());
	}

	public int[] getDepartArrivee() {
		int[] r = new int[4];
		boolean firstFound = false;

		for (int i = 0; i < this.image.length; i++) {
			for (int j = 0; j < this.image.length; j++) {
				if (0 < DepartArrivee.isSameCouleur(this.image[i][j].getCouleur())) {
					if (!firstFound) {
						r[0] = i;
						r[1] = j;
					} else {
						r[2] = i;
						r[3] = j;
					}
					firstFound = true;
				}
			}
		}

		return r;
	}

	public void importerTopologie(String text) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(text));
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.image = new Case[img.getHeight()][img.getWidth()];

		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				if (Terre.isSameCouleur(img.getRGB(i, j)) != 0) {
					this.image[i][j] = new Terre(Terre.isSameCouleur(img.getRGB(i, j)));
				} else if (Riviere.isSameCouleur(img.getRGB(i, j)) != 0) {
					this.image[i][j] = new Riviere(Riviere.isSameCouleur(img.getRGB(i, j)));
				} else if (Route.isSameCouleur(img.getRGB(i, j)) != 0) {
					this.image[i][j] = new Route(Route.isSameCouleur(img.getRGB(i, j)));
				} else if (DepartArrivee.isSameCouleur(img.getRGB(i, j)) != 0) {
					this.image[i][j] = new DepartArrivee(DepartArrivee.isSameCouleur(img.getRGB(i, j)));
				} else if (Tunnel.isSameCouleur(img.getRGB(i, j)) != 0) {
					this.image[i][j] = new Tunnel(Tunnel.isSameCouleur(img.getRGB(i, j)));
				} else if (Pont.isSameCouleur(img.getRGB(i, j)) != 0) {
					this.image[i][j] = new Pont(Pont.isSameCouleur(img.getRGB(i, j)));
				}
			}
		}
	}

	public void executerPathfinding(int base, int pont, int tunnel, int diff) {
		TopoPathfinding topo = new TopoPathfinding(this.image.length, this.image[0].length);
		for (int i = 0; i < this.image.length; i++) {
			for (int j = 0; j < this.image.length; j++) {
				if (this.image[i][j] instanceof Riviere) {
					topo.getMatrice()[i][j].setNiv((this.image[i][j].getHauteur() * -1) - 1000);
				} else {
					topo.getMatrice()[i][j].setNiv(this.image[i][j].getHauteur());
				}
			}
		}

		int[] departArrivee = this.getDepartArrivee();
		int[] d = new int[2], a = new int[2];
		d[0] = departArrivee[0];
		d[1] = departArrivee[1];
		a[0] = departArrivee[2];
		a[1] = departArrivee[3];
		Algo algo = new Algo(topo, d, a, diff);
		algo.path((int) tunnel / base, (int) pont / base);
		this.dessinerChemin(algo, algo.faireRoute());
		this.toFile(FileSystemView.getFileSystemView().getRoots()[0] + "\\topo");
	}

	private void dessinerChemin(Algo algo, List<CasePathfinding> path) {
		if (path != null) {
			CasePathfinding previous = path.get(0);
			for (CasePathfinding n : path) {
				int diff = (Math.abs(n.x - previous.x) + Math.abs(n.y - previous.y));
				if (diff > 1) {
					// Si l'�cart est sup�rieur a 1 entre deux cases, donc qu'il
					// y a un pont ou une rivi�re

					boolean isDiffX = n.y - previous.y == 0;
					boolean isDiffPositive = n.x - previous.x + n.y - previous.y > 0;
					if (isDiffPositive) {
						for (int i = 1; i < diff; i++) {
							if (algo.topo.find((previous.x + n.x)/2, (previous.y + n.y)/2).getNiv() < 0 || algo.topo.find(isDiffX ? (previous.x + i) : (previous.x), isDiffX ? (previous.y)
									: (previous.y + i)).getNiv() < algo.topo.find((previous.x), (previous.y)).getNiv()) {
								this.image[isDiffX ? (previous.x + i) : (previous.x)][isDiffX ? (previous.y)
										: (previous.y + i)] = new Pont((n.getNiv() + 1000) * -1);
							} else {
								this.image[isDiffX ? (previous.x + i) : (previous.x)][isDiffX ? (previous.y)
										: (previous.y + i)] = new Tunnel(
												this.image[previous.x][previous.y].getHauteur());
							}
						}
					} else {
						for (int i = -1; i < -diff; i--) {
							if (algo.topo.find((previous.x + n.x)/2, (previous.y + n.y)/2).getNiv() < 0 || algo.topo.find(isDiffX ? (previous.x + i) : (previous.x), isDiffX ? (previous.y)
									: (previous.y + i)).getNiv() < algo.topo.find((previous.x + n.x), (previous.y + n.y)).getNiv()) {
								this.image[isDiffX ? (previous.x + i) : (previous.x)][isDiffX ? (previous.y)
										: (previous.y + i)] = new Pont((n.getNiv() + 1000) * -1);
							} else {
								this.image[isDiffX ? (previous.x + i) : (previous.x)][isDiffX ? (previous.y)
										: (previous.y + i)] = new Tunnel(
												this.image[previous.x][previous.y].getHauteur());
							}
						}
					}
				}
				this.image[n.x][n.y] = new Route(this.image[n.x][n.y].getHauteur());
				previous = n;
				System.out.println(n.x + " " + n.y + " " + diff);
			}
		}
	}
}
