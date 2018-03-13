package window;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import topo.Topo;

@SuppressWarnings("serial")
public class Fenetre extends JFrame {

	private JPanel container = new JPanel(); // JPanel principal qui contient
												// tout
	private JTextField cheminFichier = new JTextField(); // JLabel du chemin du
															// fichier
	
	//Les différentes configurations avec des valeurs par défault
	private JTextField tfConfigChoix1 = new JTextField("1"); //Base
	private JTextField tfConfigChoix2 = new JTextField("5"); //Creuser
	private JTextField tfConfigChoix3 = new JTextField("3"); //Pont
	private JTextField tfConfigChoix4 = new JTextField("10"); //Différence de niveau
	
	private int tabValeursConfig[] = new int[4];
	
	private Topo topologie = new Topo(128, 128);

	public Fenetre() {

		this.setTitle("L'âne");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		JButton buttonGenerate = new JButton("Topologie aléatoire");
		JButton buttonChange = new JButton("Donner une Topologie");

		JPanel b2 = new JPanel();
		b2.setLayout(new BoxLayout(b2, BoxLayout.LINE_AXIS));
		b2.add(buttonGenerate);
		b2.add(buttonChange);

		// On positionne maintenant ces trois lignes en colonne
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		container.add(b2);
		container.add(panelConfigGenerator());

		// Le listener des boutons
		buttonGenerate.addActionListener(new ItemActionGenerate());
		buttonChange.addActionListener(new ItemActionChange());

		this.getContentPane().add(container);
		this.setVisible(true);
	}

	private JPanel panelConfigGenerator() {
		JPanel panelConfig = new JPanel();
		
		JPanel panelConfigChoix = new JPanel();
		panelConfigChoix.setLayout(new GridLayout(2, 2));
		
		for (int i = 0; i < 4; i++) {
			JPanel panelConfigChoixTemp = new JPanel();
			
			JLabel labelConfigChoixTemp = new JLabel();
			
			panelConfigChoixTemp.setLayout(new BoxLayout(panelConfigChoixTemp, BoxLayout.PAGE_AXIS));
			
			this.tfConfigChoix1.setMaximumSize(new Dimension(300,24));
			this.tfConfigChoix2.setMaximumSize(new Dimension(300,24));
			this.tfConfigChoix3.setMaximumSize(new Dimension(300,24));
			this.tfConfigChoix4.setMaximumSize(new Dimension(300,24));
			
			switch (i) {
			case 0:
				labelConfigChoixTemp.setText("Prix du mètre");
				panelConfigChoixTemp.add(labelConfigChoixTemp);
				panelConfigChoixTemp.add(this.tfConfigChoix1);
				break;
			case 1:
				labelConfigChoixTemp.setText("Prix supplémentaire pour creuser");
				panelConfigChoixTemp.add(labelConfigChoixTemp);
				panelConfigChoixTemp.add(this.tfConfigChoix2);
				break;
			case 2:
				labelConfigChoixTemp.setText("Prix d'un mètre de pont");
				panelConfigChoixTemp.add(labelConfigChoixTemp);
				panelConfigChoixTemp.add(this.tfConfigChoix3);
				break;
			case 3:
				labelConfigChoixTemp.setText("Difference de niveau maximale en %");
				panelConfigChoixTemp.add(labelConfigChoixTemp);
				panelConfigChoixTemp.add(this.tfConfigChoix4);
				break;
			}
			panelConfigChoix.add(panelConfigChoixTemp);
		}
		
		panelConfig.setLayout(new BoxLayout(panelConfig, BoxLayout.PAGE_AXIS));
		
		panelConfig.add(panelConfigChoix);
		
		JButton buttonConfig = new JButton("Valider");
		buttonConfig.addActionListener(new ItemActionValiderConfig());
		
		panelConfig.add(buttonConfig);
		
		return panelConfig;
	}
	
	class ItemActionValiderConfig implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			tabValeursConfig[0] = Integer.parseInt(tfConfigChoix1.getText());
			tabValeursConfig[1] = Integer.parseInt(tfConfigChoix2.getText());
			tabValeursConfig[2] = Integer.parseInt(tfConfigChoix3.getText());
			tabValeursConfig[3] = Integer.parseInt(tfConfigChoix4.getText());
		}
	}

	class ItemActionChange implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			container.removeAll();
			container.validate();
			creerPanelChange();
			container.revalidate();
			container.repaint();
		}
	}

	class ItemActionGenerate implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			container.removeAll();
			container.validate();
			creerPanelGenerate();
			container.revalidate();
			container.repaint();
		}
	}

	class ItemActionExecute implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			container.removeAll();
			container.validate();
			creerPanelExecute();
			container.revalidate();
			container.repaint();
		}
	}

	public void creerPanelExecute() {

		this.topologie.executerPathfinding(Integer.parseInt(this.tfConfigChoix1.getText()), Integer.parseInt(this.tfConfigChoix2.getText()), Integer.parseInt(this.tfConfigChoix3.getText()), Integer.parseInt(this.tfConfigChoix4.getText()));

		// Pour l'ecran d'execution
		JPanel executePanel = new JPanel();
		JLabel executeLabel1 = new JLabel("");
		JLabel executeLabel2 = new JLabel("Résultat:");

		String stringCheminFichier = FileSystemView.getFileSystemView().getRoots()[0].toString() + "\\topo.png";

		if (this.cheminFichier.getText().isEmpty()) {
			executeLabel1.setText("L'image se trouve à '" + stringCheminFichier + "' ");
			executePanel.add(executeLabel1);
		}

		executePanel.setLayout(new BoxLayout(executePanel, BoxLayout.PAGE_AXIS));

		BufferedImage myPicture;

		executePanel.add(executeLabel2);

		try {
			myPicture = ImageIO.read(new File(stringCheminFichier));

			BufferedImage scaledImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);

			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(myPicture, 0, 0, 500, 500, null);

			graphics2D.dispose();

			myPicture = scaledImage;

			JLabel generatePicture = new JLabel(new ImageIcon(myPicture));

			executePanel.add(generatePicture);
		} catch (IOException e) {
			e.printStackTrace();
		}

		container.add(executePanel);
	}

	public void creerPanelGenerate() {

		// Pour l'ecran de generation
		JPanel generatePanel = new JPanel();
		JLabel generateLabel = new JLabel();

		String stringCheminFichier = FileSystemView.getFileSystemView().getRoots()[0].toString() + "\\topo.png";

		if (this.cheminFichier.getText().isEmpty()) {
			this.topologie = Topo.topoAleatoire();
			generateLabel.setText("L'image se trouve à '" + stringCheminFichier + "' ");
		} else {
			stringCheminFichier = this.cheminFichier.getText();
			generateLabel.setText("Votre image sera modifiée");
			this.topologie.importerTopologie(stringCheminFichier);
		}

		JButton generateBouton = new JButton("Executer");

		generatePanel.setLayout(new BoxLayout(generatePanel, BoxLayout.PAGE_AXIS));

		BufferedImage myPicture;

		generatePanel.add(generateLabel);

		try {
			myPicture = ImageIO.read(new File(stringCheminFichier));

			BufferedImage scaledImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);

			Graphics2D graphics2D = scaledImage.createGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			graphics2D.drawImage(myPicture, 0, 0, 500, 500, null);

			graphics2D.dispose();

			myPicture = scaledImage;

			JLabel generatePicture = new JLabel(new ImageIcon(myPicture));

			generatePanel.add(generatePicture);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JPanel boutons = new JPanel();

		if (this.cheminFichier.getText().isEmpty()) {

			JButton retry = new JButton("Re-générer");

			retry.addActionListener(new ItemActionGenerate());

			boutons.add(retry);
		}

		boutons.add(generateBouton);
		generatePanel.add(boutons);

		generateBouton.addActionListener(new ItemActionExecute());

		container.add(generatePanel);
	}

	public void creerPanelChange() {

		// Pour l'ecran d'importation
		JPanel importPanel = new JPanel();
		JLabel importLabel = new JLabel("Entrez le chemin vers le fichier de la topologie");
		JButton importBouton = new JButton("Importer");

		this.cheminFichier.setMaximumSize(new Dimension(750, 20));

		importPanel.setLayout(new BoxLayout(importPanel, BoxLayout.PAGE_AXIS));

		importPanel.add(importLabel);
		importPanel.add(this.cheminFichier);
		importPanel.add(importBouton);

		container.add(importPanel);

		importBouton.addActionListener(new ItemActionGenerate());
	}

	public static void main(String[] args) {
		Fenetre fen = new Fenetre();
	}
}