import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Sender extends TimerTask  {
   int cpt ;
   static PrintWriter writer;
   boolean quit = true;
   Socket s ;
	Scanner cs ;
	Map<Integer,Trame> plage;
	Map<Integer,Long> plageTimer;
	Parser pa;
	LinkedList<Trame> trame;
	Trame [] tab;
	Trame D;
	Timer time ;

   public Sender(String hote, int port,String nomFichier) throws UnknownHostException, IOException{
	   this.s =  new Socket(hote, port);
	    this. cs = new Scanner(s.getInputStream());
	    this.plage = new HashMap<Integer,Trame>() ;
		this.plageTimer = new HashMap<Integer,Long>() ;
	    this.pa = new Parser(nomFichier);
		this.trame = pa.getTrame();
	    this.time  = new Timer();
		this.tab = new Trame [trame.size()];
        System.out.println("affichage de la liste chainee"+this.trame);
		
		for(int i =0; i <trame.size(); i++){
				this.tab[i]=this.trame.get(i);	
		}
		System.out.println(trame);
				
		D = new Trame("000000000000000D0000000000000000000000000000");
   };
   public boolean cancel(){
	   this.time.cancel();
	return true;
   }
   //envoyer une chiane de caractere via un socket
	public static void send(String T,Socket s) throws IOException{

		Scanner sc = new Scanner(System.in);
		PrintStream p = new PrintStream(s.getOutputStream());
		
		p.println(T);
		
	}

	public static void affiche(Integer [] tab){
		for (int i =0; i<tab.length;i++)
			System.out.println(tab[i]);
	}
	public boolean getQuit(){
		return this.quit;
	}
	public static void  ecrire(String fichier,String texte){
		try{
		    if(writer==null)
		    	writer = new PrintWriter(fichier, "UTF-8");
		   
		    writer.println(texte);
		   
		} catch (IOException e) {
		  
		}
	}
	//en attente d'une trame 
	public  void ecouter_sender(BufferedReader br) throws IOException, InterruptedException{
	    String fenetre,trameAenvoyer;
	    Trame trame;
	    int taillePlage=0,tailleTrame;
	  
	    fenetre=br.readLine();
	    System.out.println("ecouter" + fenetre);
	    if (fenetre!=null)
	    {
	    	trame=new Trame(fenetre);
	    	
	    	tailleTrame=tab.length;
	    	System.out.println("======>trame recu pour traitement "+ fenetre);
	    	// gestion des acquitement et envoie des trames d'information;
	    	if(trame.getType().compareTo("0000000A")==0)
	    	{
	    		if (this.cpt==0)
	    		{
	    		  System.out.println("======>demande connexion accepte par le serveur");
	    		  taillePlage=plage.size();
				  for (int i=0;i<4-taillePlage;i++){
					  System.out.println("ajout dans la plage  de la trame"+ tab[this.cpt].toString());
					  plage.put(this.cpt, tab[cpt]);
					  plageTimer.put(this.cpt, System.currentTimeMillis()); 
					  trameAenvoyer=tab[this.cpt].toString();
					  send(trameAenvoyer,s);
					  this.cpt+=1;
				  }
	    		  
	    		}
	    		else
	    		{   System.out.println("======>gestion  d' acquitement simple");
	    			if(this.cpt<=tailleTrame-1)
	    			{
	    			  if (plage.containsKey(trame.getNumero()))
	    			  {   
	    				  System.out.println("======>la plage avant ajout "+ plage);
	    				  System.out.println("======>taille de la plage avant ajout "+ plage.size());
	    				  plage.remove(trame.getNumero());
	    				  plageTimer.remove(trame.getNumero());
	    				  System.out.println("======>taille de la plage apres ajout "+ plage.size());
	    				  System.out.println("======>la plage avant ajout "+ plage);
	    				  taillePlage=plage.size();
	    				  for (int i=0;i<4-taillePlage;i++){
	    					  System.out.println("======>ajout dans la plage  de la trame"+ tab[cpt].toString());
	    					  plage.put(this.cpt, tab[cpt]);
	    					  plageTimer.put(this.cpt, System.currentTimeMillis()); 
	    					  trameAenvoyer=tab[this.cpt].toString();
	    					  send(trameAenvoyer,s);
	    					  this.cpt+=1;
	    				  }
	    			  }
	    			  
	    			  
	    			}
	    			else if(this.cpt==tailleTrame)
	    			{
	    				if(plage.size()!=0)
	    				{
	    					System.out.println("======>gestion des dernieres acquitement ");
	    					plage.remove(trame.getNumero());
	    					plageTimer.remove(trame.getNumero());
	    					if (plage.size()==0)
	    					{
	   	    				 System.out.println("======>gestion de toutes les trames par le serveur fin de communication");
	   	    				 trameAenvoyer=new Trame("000000000000000F000000000000000000000000").toString();
	   	    				 send(trameAenvoyer,s);
	   	    				 this.quit=false;
	   	    				 this.cancel();
	   	    				}
	    				}
	    				
	    				
	    			}
	    		  	
	    		}
	    	}
	    	// Gestion des acquitements multiples
	    	else if (trame.getType().compareTo("0000000N")==0)
	    	{
	    	System.out.println("======>gestion  d' acquitements multiples");
	    	 int numero=trame.getNumero();
	    	 Set<Integer> set = new HashSet<Integer>();
				set = plage.keySet();
				Integer[] tabs ;
				
				tabs = set.toArray(new Integer[set.size()]);
				affiche(tabs);
				System.out.println("la plage avant MAJ "+plage);
				System.out.println("la set avant " +
						"" +
						" "+set);
				taillePlage=plage.size();
				for(int i = 0; i<taillePlage;i++)
				{
						if(tabs[i] <= numero)
						{
							System.out.println("retrait dans la plage de la trame" + tab[i]);
							plage.remove(tabs[i]);
							plageTimer.remove(tabs[i]);
						}
				}
				System.out.println("la plage apres MAJ "+plage);
				if (this.cpt<tailleTrame)
				{
					taillePlage=plage.size();
  				  for (int i=0;i<4-taillePlage;i++){
  					  if (this.cpt<tailleTrame){
  					  System.out.println("ajout dans la plage  de la trame"+ tab[cpt].toString());
  					  plage.put(this.cpt, tab[cpt]);
  					  plageTimer.put(this.cpt, System.currentTimeMillis()); 
  					  trameAenvoyer=tab[this.cpt].toString();
  					  send(trameAenvoyer,s);
  					  this.cpt+=1;}
  				  }
				}
				else
				{   if(plage.size()==0){
					System.out.println("fin de la connexion ");
					trameAenvoyer=new Trame("000000000000000F000000000000000000000000").toString();
					send(trameAenvoyer,s);
					this.quit=false;
					this.cancel();
					
					}
				}
	    }
	    	else if(trame.getType().compareTo("0000000S") == 0)
	    	{
	    		trameAenvoyer=tab[trame.getNumero()].toString();
	    		send(trameAenvoyer,s);
	    	}
	    
	    }
		
	}
	
	
	public static void main(String args []) throws IOException, InterruptedException{
		
		   
		Sender sender= new Sender(args[0],Integer.parseInt(args[1]),args[2]);
		
		String result,fenetre;

		BufferedReader br= new BufferedReader(new InputStreamReader(sender.s.getInputStream()));
		
		
		
		send(sender.D.toString(),sender.s);
		
		sender.time.scheduleAtFixedRate(sender, 5000, 5000);
		while(sender.getQuit()){
			
			
			if (br.ready())
				sender.ecouter_sender(br);
		
			}

	}
	@Override
	public void run() {
		Long time = System.currentTimeMillis();
		System.out.println("on est dans le run "+plageTimer +" "+time);
		
	
	
		int timerTaille = plageTimer.size();
		Set<Integer> set = new HashSet<Integer>();
			set = plage.keySet();
			Integer[] tabs ;
			
			tabs = set.toArray(new Integer[set.size()]);
		for(int i = 0; i<plageTimer.size();i++){
			System.out.println(plageTimer.size());
			if(plageTimer.get(i) !=null){
			
			
			if((time-plageTimer.get(i)) > 5000){
				System.out.println("retramission");
				try {
					send(plage.get(i).toString(),s);
				} catch (IOException e) {
		
					e.printStackTrace();
				}
			}
			}
		}
		
	}
		
}
	
	
	
	
	

	
