package pathfinding;
import java.util.ArrayList;
import java.util.List;

public class Algo {
	public TopoPathfinding topo;
	int[] d; //depart
	int[] a; //arriv�e;
	CasePathfinding c; //current
	List<CasePathfinding> open = new ArrayList<CasePathfinding>(); // cases � traiter
	List<CasePathfinding> close = new ArrayList<CasePathfinding>(); // cases trait�es
	//variables d'informations
	int tourDeTest;
	private int diffHauteur;
	
	public Algo(TopoPathfinding topo, int[] d, int[] a, int diffHauteur) {
		this.topo = topo;
		this.d = d;
		this.a = a;
		this.diffHauteur = diffHauteur;
	};
	
	public void executerAlgo(int prixP, int prixT) { 
		if (this.path(prixP, prixT)) {
			this.affichageInformatif(this.faireRoute());
		} else {
			System.out.println("L'algo n'a pas pu trouver de solution");
		}
	}
	
	 
	public boolean path(int prixP, int prixT){ //INTESTABLE, beaucoup trop long, à redécouper en sous fonctions ?
		
		this.topo.find(d[0], d[1]).x = d[0];
		this.topo.find(d[0], d[1]).y = d[1];
		this.topo.find(d[0], d[1]).F = Math.abs(d[0] - a[0]) + Math.abs(d[1] - a[1]);
		this.open.add(this.topo.find(d[0], d[1]));
		this.c = this.topo.find(d[0], d[1]);
		
		//variable pour la selection de la case avec le F minimum
		int minF = 0;
		int IDminF = 0;		
		//variables d'informations
		boolean cheminTrouve = false;
		int tourDeTest = 0;
		//variable pour pont/tunel
		int longueurPont;
		int prixPont = prixP;
		int prixTunel = prixT;
		int prix = 0;
		// (variable qui simplifie une modification future de l'algorithme)
		int multiplieurH = 1;

		
		while(!this.open.isEmpty() && !cheminTrouve){ // tant qu'il reste des cases � traiter (open list) et que le chemin n'a pas d�ja �t� trouv�
			
			tourDeTest++; // pour compter le nombre de tour de l'algo (� titre informatif)
			
		    for(int i = 0; i <this.open.size() ; i++){  // on prend la case avec le F minimum
		    	this.c = this.open.get(i);

		    	if(minF == 0 || this.open.get(i).F < minF){
		    		minF = this.topo.find(this.c.x,this.c.y).F;
		    		IDminF = i;

		    	}
		    	
		    }
		    this.c = this.open.get(IDminF); // et on la stock dans c
			//System.out.println(this.c.F + " " + this.c.x + this.c.y); // affichage informatif
			minF = 0;
			IDminF = 0;
			
			//~~~~~ case � gauche ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

			if(this.c.x != 0 && !this.close.contains(this.topo.find(this.c.x-1,this.c.y)) && Math.abs(this.c.getNiv() - this.topo.find(this.c.x-1,this.c.y).getNiv())<this.diffHauteur ){ //si la case ne d�passe pas de la matrice et n'est pas dans la close list et est accessible
	
				//calcule case ==========================================================|
				if( this.open.contains(this.topo.find(this.c.x-1,this.c.y)) && this.topo.find(this.c.x-1,this.c.y).G < this.c.G + 1){ // si elle est deja dans l'open list et que son G est plus interessant que c.G +1 alors il existe deja un chemin pour cette case plus optimis�
				}else{
					this.open.remove(this.topo.find(this.c.x-1,this.c.y)); // on enl�ve la case de l'open list car on recalcule ses variables
					this.topo.find(this.c.x-1,this.c.y).x = this.c.x-1;
					this.topo.find(this.c.x-1,this.c.y).y = this.c.y;
					this.topo.find(this.c.x-1,this.c.y).parent = this.c;
					this.topo.find(this.c.x-1,this.c.y).G =  this.c.G + 1;
			    	this.topo.find(this.c.x-1,this.c.y).H = (Math.abs(this.c.x-1 - a[0]) + Math.abs(this.c.y - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x-1,this.c.y).F = this.topo.find(this.c.x-1,this.c.y).G + this.topo.find(this.c.x-1,this.c.y).H;
					this.open.add(this.topo.find(this.c.x-1,this.c.y));
				}// fin calcule case ===================================================|
				
			}else if (this.c.x != 0 && !this.close.contains(this.topo.find(this.c.x-1,this.c.y))){ //pont/tunel	
				
				longueurPont = 2; //remise � 2 longueurPont (variable aussi utilis� pour le tunel)
				
				if((this.c.getNiv() - this.topo.find(this.c.x-1,this.c.y).getNiv()) > this.diffHauteur){ // pont
					prix = prixPont;
					//System.out.println("pont gauche:" );
					while(this.c.getNiv() - this.topo.find(this.c.x-longueurPont,this.c.y).getNiv() > this.diffHauteur && this.c.x-longueurPont != 0){ //calcule de la longueur du pont dans longueurPont
						longueurPont++;
					}
				}else{ //tunel
					prix = prixTunel;
					//System.out.println("tunel gauche:");
					while((this.c.getNiv() - this.topo.find(this.c.x-longueurPont,this.c.y).getNiv() < -this.diffHauteur) && this.c.x-longueurPont != 0){ //calcule de la longueur du tunel dans longueurPont
						longueurPont++;
						//System.out.println(this.c.x +"/"+ this.c.y +"//"+ this.topo.find(this.c.x-longueurPont,this.c.y).getNiv());
					}
				}
				//calcule case (pont/tunel) eventuel =========================================|
				if(this.open.contains(this.topo.find(this.c.x-longueurPont,this.c.y)) && this.topo.find(this.c.x-longueurPont,this.c.y).G < this.c.G + (prix*longueurPont)){ // si elle est deja dans l'open list et que son G est plus interessant que c.G + prix du pont alors il existe deja un chemin pour cette case plus optimis�
				}else if(!this.close.contains(this.topo.find(this.c.x-longueurPont,this.c.y))){
					this.open.remove(this.topo.find(this.c.x-longueurPont,this.c.y)); // on enl�ve la case de l'open list car on recalcule ses variables
					this.topo.find(this.c.x-longueurPont,this.c.y).x = this.c.x-longueurPont;
					this.topo.find(this.c.x-longueurPont,this.c.y).y = this.c.y;
					this.topo.find(this.c.x-longueurPont,this.c.y).parent = this.c;
					this.topo.find(this.c.x-longueurPont,this.c.y).G =  this.c.G + (prix*longueurPont);
			    	this.topo.find(this.c.x-longueurPont,this.c.y).H = (Math.abs(this.c.x-longueurPont - a[0]) + Math.abs(this.c.y - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x-longueurPont,this.c.y).F = this.topo.find(this.c.x-longueurPont,this.c.y).G + this.topo.find(this.c.x-longueurPont,this.c.y).H;
					this.open.add(this.topo.find(this.c.x-longueurPont,this.c.y));
					//System.out.println("lg : "+longueurPont +" / F : "+this.topo.find(this.c.x-longueurPont,this.c.y).F);
				}// fin calcule case =========================================================|
			}
			
			//~~~~~ case en haut ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			if(this.c.y != 0 && !this.close.contains(this.topo.find(this.c.x,this.c.y-1)) && Math.abs(this.c.getNiv() - this.topo.find(this.c.x,this.c.y-1).getNiv())<this.diffHauteur ){
				
				//calcule case ========================================================|
				if( this.open.contains(this.topo.find(this.c.x,this.c.y-1)) && this.topo.find(this.c.x,this.c.y-1).G < this.c.G + 1){
				}else{
					this.open.remove(this.topo.find(this.c.x,this.c.y-1));
					this.topo.find(this.c.x,this.c.y-1).x = this.c.x;
					this.topo.find(this.c.x,this.c.y-1).y = this.c.y-1;
					this.topo.find(this.c.x,this.c.y-1).parent = this.c;
					this.topo.find(this.c.x,this.c.y-1).G =  this.c.G + 1;
			    	this.topo.find(this.c.x,this.c.y-1).H = (Math.abs(this.c.x - a[0]) + Math.abs(this.c.y-1 - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x,this.c.y-1).F = this.topo.find(this.c.x,this.c.y-1).G + this.topo.find(this.c.x,this.c.y-1).H;
					this.open.add(this.topo.find(this.c.x,this.c.y-1));
				}// fin calcule case ==================================================|
			
				
			}else if (this.c.y != 0 && !this.close.contains(this.topo.find(this.c.x,this.c.y-1))){ //pont/tunel	
				
				longueurPont = 2; //remise � 2 longueurPont (variable aussi utilis� pour le tunel)
				
				if((this.c.getNiv() - this.topo.find(this.c.x,this.c.y-1).getNiv()) > this.diffHauteur){ // pont
					prix = prixPont;
					//System.out.println("pont haut:");
					while(this.c.getNiv() - this.topo.find(this.c.x,this.c.y-longueurPont).getNiv() > this.diffHauteur && this.c.y-longueurPont != 0){ //calcule de la longueur du pont dans longueurPont
						longueurPont++;
					}
				}else{ //tunel
					prix = prixTunel;
					//System.out.println("tunel haut:");
					while(this.c.getNiv() - this.topo.find(this.c.x,this.c.y-longueurPont).getNiv() < -this.diffHauteur && this.c.y-longueurPont != 0){ //calcule de la longueur du tunel dans longueurPont
						longueurPont++;
					}
				}
				//calcule case (pont/tunel) eventuel ==========================================|
				if( this.open.contains(this.topo.find(this.c.x,this.c.y-longueurPont)) && this.topo.find(this.c.x,this.c.y-longueurPont).G < this.c.G + (prix*longueurPont)){ // si elle est deja dans l'open list et que son G est plus interessant que c.G + prix du pont alors il existe deja un chemin pour cette case plus optimis�
				}else if(!this.close.contains(this.topo.find(this.c.x,this.c.y-longueurPont))){
					this.open.remove(this.topo.find(this.c.x,this.c.y-longueurPont)); // on enl�ve la case de l'open list car on recalcule ses variables
					this.topo.find(this.c.x,this.c.y-longueurPont).x = this.c.x;
					this.topo.find(this.c.x,this.c.y-longueurPont).y = this.c.y-longueurPont;
					this.topo.find(this.c.x,this.c.y-longueurPont).G =  this.c.G + (prix*longueurPont);
					this.topo.find(this.c.x,this.c.y-longueurPont).parent = this.c;
			    	this.topo.find(this.c.x,this.c.y-longueurPont).H = (Math.abs(this.c.x - a[0]) + Math.abs(this.c.y-longueurPont - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x,this.c.y-longueurPont).F = this.topo.find(this.c.x,this.c.y-longueurPont).G + this.topo.find(this.c.x,this.c.y-longueurPont).H;
					this.open.add(this.topo.find(this.c.x,this.c.y-longueurPont));
					//System.out.println("lg "+longueurPont +" / "+this.topo.find(this.c.x,this.c.y-longueurPont).F);
				}// fin calcule case ==========================================================|
			}
			
			//~~~~~ case � droite ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			if(this.c.x != this.topo.largeur-1 && !this.close.contains(this.topo.find(this.c.x+1,this.c.y))&& Math.abs(this.c.getNiv() - this.topo.find(this.c.x+1,this.c.y).getNiv())<this.diffHauteur){
				
				//calcule case ========================================================|
				if( this.open.contains(this.topo.find(this.c.x+1,this.c.y)) && this.topo.find(this.c.x+1,this.c.y).G < this.c.G + 1){
				}else{
					this.open.remove(this.topo.find(this.c.x+1,this.c.y));
					this.topo.find(this.c.x+1,this.c.y).x = this.c.x+1;
					this.topo.find(this.c.x+1,this.c.y).y = this.c.y;
					this.topo.find(this.c.x+1,this.c.y).parent = this.c;
					this.topo.find(this.c.x+1,this.c.y).G =  this.c.G + 1;
			    	this.topo.find(this.c.x+1,this.c.y).H = (Math.abs(this.c.x+1 - a[0]) + Math.abs(this.c.y - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x+1,this.c.y).F = this.topo.find(this.c.x+1,this.c.y).G + this.topo.find(this.c.x+1,this.c.y).H;
					this.open.add(this.topo.find(this.c.x+1,this.c.y));
				}// fin calcule case ===================================================|
				
			}else if (this.c.x != this.topo.largeur-1 && !this.close.contains(this.topo.find(this.c.x+1,this.c.y))){ //pont/tunel	
				
				longueurPont = 2; //remise � 2 longueurPont (variable aussi utilis� pour le tunel)
				
				if((this.c.getNiv() - this.topo.find(this.c.x+1,this.c.y).getNiv()) > this.diffHauteur){ // pont
					prix = prixPont;
					//System.out.println("pont droite:" );
					while(this.c.getNiv() - this.topo.find(this.c.x+longueurPont,this.c.y).getNiv() > this.diffHauteur && this.c.x+longueurPont != this.topo.largeur-1){ //calcule de la longueur du pont dans longueurPont
						longueurPont++;
					}
				}else{ //tunel
					prix = prixTunel;
					//System.out.println("tunel droite:");
					while((this.c.getNiv() - this.topo.find(this.c.x+longueurPont,this.c.y).getNiv() < -this.diffHauteur) && this.c.x+longueurPont != this.topo.largeur-1){ //calcule de la longueur du tunel dans longueurPont
						longueurPont++;
						//System.out.println(this.c.x +"/"+ this.c.y +"//"+ this.topo.find(this.c.x+longueurPont,this.c.y).getNiv());
					}
				}
				//calcule case (pont/tunel) eventuel =========================================|
				if( this.open.contains(this.topo.find(this.c.x+longueurPont,this.c.y)) && this.topo.find(this.c.x+longueurPont,this.c.y).G < this.c.G + (prix*longueurPont)){ // si elle est deja dans l'open list et que son G est plus interessant que c.G + prix du pont alors il existe deja un chemin pour cette case plus optimis�
				}else if(!this.close.contains(this.topo.find(this.c.x+longueurPont,this.c.y))){
					this.open.remove(this.topo.find(this.c.x+longueurPont,this.c.y)); // on enl�ve la case de l'open list car on recalcule ses variables
					this.topo.find(this.c.x+longueurPont,this.c.y).x = this.c.x+longueurPont;
					this.topo.find(this.c.x+longueurPont,this.c.y).y = this.c.y;
					this.topo.find(this.c.x+longueurPont,this.c.y).parent = this.c;
					this.topo.find(this.c.x+longueurPont,this.c.y).G =  this.c.G + (prix*longueurPont);
			    	this.topo.find(this.c.x+longueurPont,this.c.y).H = (Math.abs(this.c.x+longueurPont - a[0]) + Math.abs(this.c.y - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x+longueurPont,this.c.y).F = this.topo.find(this.c.x+longueurPont,this.c.y).G + this.topo.find(this.c.x+longueurPont,this.c.y).H;
					this.open.add(this.topo.find(this.c.x+longueurPont,this.c.y));
					//System.out.println("lg : "+longueurPont +" / F : "+this.topo.find(this.c.x+longueurPont,this.c.y).F);
				}// fin calcule case =========================================================|
			}
			
			//~~~~~ case en bas ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

			
			if(this.c.y != this.topo.hauteur-1 && !this.close.contains(this.topo.find(this.c.x,this.c.y+1))&& Math.abs(this.c.getNiv() - this.topo.find(this.c.x,this.c.y+1).getNiv())<this.diffHauteur){
				
				if( this.open.contains(this.topo.find(this.c.x,this.c.y+1)) && this.topo.find(this.c.x,this.c.y+1).G < this.c.G + 1){
				}else{
					this.open.remove(this.topo.find(this.c.x,this.c.y+1));
					this.topo.find(this.c.x,this.c.y+1).x = this.c.x;
					this.topo.find(this.c.x,this.c.y+1).y = this.c.y+1;
					this.topo.find(this.c.x,this.c.y+1).parent = this.c;
					this.topo.find(this.c.x,this.c.y+1).G =  this.c.G + 1;
			    	this.topo.find(this.c.x,this.c.y+1).H = (Math.abs(this.c.x - a[0]) + Math.abs(this.c.y+1 - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x,this.c.y+1).F = this.topo.find(this.c.x,this.c.y+1).G + this.topo.find(this.c.x,this.c.y+1).H;
					this.open.add(this.topo.find(this.c.x,this.c.y+1));
				}
			}else if (this.c.y != this.topo.hauteur-1 && !this.close.contains(this.topo.find(this.c.x,this.c.y+1))){ //pont/tunel	
				
				longueurPont = 2; //remise � 2 longueurPont (variable aussi utilis� pour le tunnel)
				
				if((this.c.getNiv() - this.topo.find(this.c.x,this.c.y+1).getNiv()) > this.diffHauteur){ // pont
					prix = prixPont;
					//System.out.println("pont bas:");
					while(this.c.getNiv() - this.topo.find(this.c.x,this.c.y+longueurPont).getNiv() > this.diffHauteur && this.c.y+longueurPont != this.topo.hauteur-1){ //calcule de la longueur du pont dans longueurPont
						longueurPont++;
					}
				}else{ //tunel
					prix = prixTunel;
					//System.out.println("tunel bas:");
					while(this.c.getNiv() - this.topo.find(this.c.x,this.c.y+longueurPont ).getNiv() < -this.diffHauteur && this.c.y+longueurPont != this.topo.hauteur-1){ //calcule de la longueur du tunel dans longueurPont
						longueurPont++;
					}
				}
				//calcule case (pont/tunel) eventuel ==========================================|
				if( this.open.contains(this.topo.find(this.c.x,this.c.y+longueurPont )) && this.topo.find(this.c.x,this.c.y+longueurPont ).G < this.c.G + (prix*longueurPont)){ // si elle est deja dans l'open list et que son G est plus interessant que c.G + prix du pont alors il existe deja un chemin pour cette case plus optimis�
				}else if(!this.close.contains(this.topo.find(this.c.x,this.c.y+longueurPont))){
					this.open.remove(this.topo.find(this.c.x,this.c.y+longueurPont )); // on enl�ve la case de l'open list car on recalcule ses variables
					this.topo.find(this.c.x,this.c.y+longueurPont ).x = this.c.x;
					this.topo.find(this.c.x,this.c.y+longueurPont ).y = this.c.y+longueurPont ;
					this.topo.find(this.c.x,this.c.y+longueurPont ).G =  this.c.G + (prix*longueurPont);
					this.topo.find(this.c.x,this.c.y+longueurPont ).parent = this.c;
			    	this.topo.find(this.c.x,this.c.y+longueurPont ).H = (Math.abs(this.c.x - a[0]) + Math.abs(this.c.y+longueurPont - a[1])) * multiplieurH;
			    	this.topo.find(this.c.x,this.c.y+longueurPont ).F = this.topo.find(this.c.x,this.c.y+longueurPont ).G + this.topo.find(this.c.x,this.c.y+longueurPont ).H;
					this.open.add(this.topo.find(this.c.x,this.c.y+longueurPont ));
					//System.out.println("lg "+longueurPont +" / "+this.topo.find(this.c.x,this.c.y+longueurPont ).F);
				}// fin calcule case ==========================================================|
			}
			
			
			
			if(this.c.x == a[0] && this.c.y == a[1]){       // cas d'arr�t
				this.tourDeTest = tourDeTest;
				cheminTrouve = true;
			}else{
				this.open.remove(this.c);
				this.close.add(this.c);
			}
			
		}
		return cheminTrouve;
	}
	
	public List<CasePathfinding> faireRoute() {
			List<CasePathfinding> optimal = new ArrayList<CasePathfinding>();
			
			while(this.c.x != d[0] || this.c.y != d[1]){   // retourner et stocker le chemin dans la liste "optimal"
				optimal.add(0, this.c);
				this.c = this.c.parent;
			}
			
			optimal.add(0, this.topo.find(d[0], d[1]));
			
			return optimal;
	}

	public void affichageInformatif(List<CasePathfinding> optimal) {
			// affichage informatif
	    	System.out.println("Chemin optimal :  ");
	    	System.out.println("[" + d[0] + d[1] +"]" + " -> ");
			for (CasePathfinding n : optimal) {
		    	System.out.println("[" + n.x + n.y +"]" + " -> ");

			}
	    	System.out.println("arriv�e ! (longueur du chemin : " + optimal.size() + " co�t du chemin "+this.topo.find(a[0],a[1]).F +" )");
	    	System.out.println("Pour ce r�sultat l'algo � fait " + this.tourDeTest + " tests");
		
	}
	
	//ajout pour tests
	public void setC(CasePathfinding c)
	{
		this.c = c;
	}
	
	public CasePathfinding getC()
	{
		return this.c;
	}
	
}
