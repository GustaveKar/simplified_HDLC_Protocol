import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


public class Receiver {
	   int cpt;
	   boolean b = false;
	   int num;
	   int cptr =0;
	   boolean quit  = true;
	   boolean srej = false;
	   int trameErrone;
	   int test =0;
	   static PrintWriter writer;
		Map<Integer,Trame> plage = new HashMap<Integer,Trame>() ;

	   public Receiver(){};

	private static int getRandomNumberInRange(int min, int max) {

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	public boolean getQuit(){
		return this.quit;
	}
	public static String conversion(String binaire){
		int taille= binaire.length(),numero;
		int taille4= taille/4;
		int cpt=0;
		String texte="";
		
		for(int i=0;i<taille4-1&& cpt<taille;i++){
			
			numero=Integer.parseInt(binaire.substring(cpt, cpt+8),2);
			texte=texte+((char) numero);
			
			cpt+=8;
		}
		return texte;
	}
	public static void send(String T,Socket s) throws IOException{
		
		
		Scanner sc = new Scanner(System.in);
		PrintStream p = new PrintStream(s.getOutputStream());
		
		p.println(T);
		
	}
	public static void  ecrire(String fichier,String texte){
		try{
		    if(writer==null)
		    	writer = new PrintWriter(fichier, "UTF-8");
		   
		    writer.println(texte);
		   
		} catch (IOException e) {
		   // do something
		}
	}
	//mettre la donné sur 8 bit
	public static String ajouteZero(int numero){
		String text= Integer.toString(numero);
		int taille=8-text.length();
		String num="";
		for (int i=0;i<taille;i++)
			num+="0";
		return num+text;
	}
	//mettre la donné sur 8 bit
	public static String ajouteZero(String text,int bit){
		
		int taille=bit-text.length();
		String num="";
		for (int i=0;i<taille;i++)
			num+="0";
		return num+text;
	}

	//calcul du crc
	public static String crc(String texte1, String texte2){

		 int cpt=0,cpt1=0;
		    int taille1= texte1.length();
		    int taille2= texte2.length();
		
		    Scanner pauser = new Scanner (System.in);
	       
		    int reste;
		    cpt=taille2;
		    if (taille1< taille2)
		    	return texte1;
		    String  texte= texte1.substring(0,taille2);

		    while((cpt <=taille1)&&(texte.length()==taille2)||cpt1==0){
		    	cpt1++;
		    
		    	
		    	
		    	if ((texte.compareTo(texte2)>=0 && texte.length()==taille2) ||(texte.length()==taille2)){

		    		reste=Integer.parseInt(texte,2)^Integer.parseInt(texte2,2);

		    		if (reste!=0){
		    		texte= Integer.toBinaryString(reste);

		    		}
		    		
		    		else
		    			texte="";
	
		    		if (cpt==taille1)
		    			if (texte=="")
		    				return "0";
		    			else
		    			return texte;
		    		
		    		while(((texte.compareTo(texte2)<0)&& cpt<=taille1-1&&texte.length()<taille2 && cpt<=taille1)|| (texte.length()<taille2 && cpt<=taille1)){
		    	
		    			if (texte!="")
		    			if (Integer.parseInt(texte,2)==0 && texte.length()==taille2)
		    	    		texte="";
		    				
		    			if (cpt<taille1-1)
		    			texte+=texte1.substring(cpt,cpt+1);
		    			else
		    				texte+=texte1.substring(cpt);
		    			
		    			
		    			cpt++;
	
		    		}
		    		
		    	}
		    	
		    	
		    }
		    if (cpt==taille1)
				if (texte=="")
					return "0";
				else
				return texte;

		    return texte;
		    
		 
		 
		 
	 }
	 //Traitement de la trame recu
	public void gestionTrame(String fenetre , Socket s,Map<Integer,Trame> data) throws IOException, InterruptedException{

		  String trameAenvoyer,crc;
		  Trame trame, trameSend;
		
		  
		  if (fenetre!=null)
		  {
			trame= new Trame(fenetre);
			System.out.println("\n\n================================================");
    		System.out.println("        Reception de trame");
    		System.out.println("================================================\n\n");
			
			System.out.println("======> Trame recu "+ trame.toStringBi()+"<=======");
			
			System.out.println("======>Type de la trame "+trame.getType()+"  test "+trame.getType().compareTo("0000000F"));
			// gesstion de la demande de connexion
		    if (trame.getType().compareTo("0000000D")==0)
		    {
		    	trameAenvoyer=(new Trame("000000000000000A0000000000000000000000000000")).toString();
		    	System.out.println("=============>acquitement de la demande de connexion "+ trameAenvoyer+"<===============");
		    	send(trameAenvoyer,s);
		    }
		    // gestion des trames d'information et emissions des acquitement.
		    else if (trame.getType().compareTo("0000000I")==0)
		    {
		    	
		    		crc = ajouteZero(crc(trame.toStringBi(),"10000101010111111"),16);
		    		this.test =0;

		      if(crc.compareTo(trame.getCrc())==0){
		    	  
		    	int random=1;
		    	if (!this.b)
		    	{
		    		random=getRandomNumberInRange(0, 1);
		    	}
		    	if (random==0)
		    	{   System.out.println("================================================");
		    		System.out.println("        gestion d'un seul acquitement");
		    		System.out.println("================================================");
		    		data.put(trame.getNumero(), trame);
		    		ecrire("test.txt",trame.getData());
		    		trameAenvoyer="000000000000000A"+ajouteZero(trame.getNumero())+"0000000000000000000000000000";
		    		System.out.println("======>envoie de l'acquitement de la trame "+ trame.getNumero()+" <=======");
		    		 
		    		Thread.sleep(1000);
		    		send(trameAenvoyer,s);
		    		
		    	}
		    	//gestion d'acquictement multiple
		    	else if(random==1)
		    	{   
		    		this.b=true;
		    		this.num= trame.getNumero();
		    		this.cptr+=1;
		    		data.put(this.num, trame);
		    		ecrire("test.txt",trame.getData());
		    		if (this.cptr==4){
		    			this.b=false;
		    			this.cptr=0;
		    			trameAenvoyer=new Trame ("000000000000000N"+ajouteZero(trame.getNumero())+"000000000000000000000000").toString();
		    			System.out.println("======>Envoie d'un acquitement de type N "+ trameAenvoyer);
		    			send(trameAenvoyer,s);
		    			
		    		}
		    	}
		    
		    
		   }
		      else{
		    	  System.out.println("================================================");
		    		System.out.println("        gestion d'un Rejet Selectif");
		    		System.out.println("================================================");
		    	  this.srej = true;
		    	  this.trameErrone = trame.getNumero();
		    	  trameAenvoyer = "000000000000000S"+ajouteZero(trameErrone)+"000000000000000000000000";
		    	  System.out.println("=========> envoi au d'un srej pour la trame numero"+ trame.getNumero()+" <==========");
		    	  plage.put(this.trameErrone, trame);
		    	  send(trameAenvoyer,s);
		      }
		  }
		 // gestion de la fin de connexion
		    else if(trame.getType().compareTo("0000000F")==0)
		    {
		    	System.out.println("================================================");
	    		System.out.println("        gestion de la fin de connexion");
	    		System.out.println("================================================");
		    	
		    	this.quit=false;
		    }
		 }
		 
	
		
	}
	public  void ecouter_receiver(String fenetre , Socket s,Map<Integer,Trame> data) throws IOException, InterruptedException{
	   //gestion d'un rejet selectif
		if(this.srej){
			Trame trame = new Trame(fenetre);
			int numero = trame.getNumero();
			if(this.trameErrone != numero){
				plage.put(numero, trame);
			}
			else {
				Set<Integer> set = new HashSet<Integer>();
				Integer[] tabs ;
				this.srej = false;
				while(plage.size() != 0){
						
					
					
					set = plage.keySet();
					
					
					this.gestionTrame(fenetre, s, data);
					if(this.srej){
						
						break;
					}
					plage.remove(numero);
					set.remove(numero);
					if(!set.isEmpty()){
						tabs = set.toArray(new Integer[set.size()]);
						Arrays.sort(tabs);
						
						System.out.println("++++=======> "+Arrays.toString(tabs));
						data.put(numero, trame);
						fenetre =plage.get(tabs[0]).toString();
						numero = tabs[0];
					}
					
				}
				System.out.println("======>J'y suis avec taille de la plage suivante "+plage.size());
				
			}
		}
		//gestion d'une trame non erronnée
		else{
			this.gestionTrame(fenetre, s, data);
		}
	}

	public static void main(String args[]) throws IOException, InterruptedException
	{
		
		

		Map<Integer,Trame> data = new HashMap<Integer,Trame>() ;
	
	
		ServerSocket s1 = new ServerSocket(Integer.parseInt(args[0]));
		Socket ss = s1.accept();
		Scanner sc = new Scanner(ss.getInputStream());
		BufferedReader br= new BufferedReader(new InputStreamReader(ss.getInputStream()));
	
		Receiver receiver = new Receiver();
		
		boolean ready=br.ready(),pret;
		Thread.sleep(5);
		pret=true;
		String fenetre=br.readLine();
		
		System.out.println("============>"+fenetre+" ==========> "+receiver.getQuit()+" ==========> "+pret);
		while(receiver.getQuit()&&  pret){
		  System.out.println("============><============");
		  receiver.ecouter_receiver(fenetre,ss,data);
		  Thread.sleep(5);
		  ready=br.ready();
		  if(ready)
		  fenetre=br.readLine();
		  if (!ready && receiver.cptr!=0 && receiver.b){
			String  trameAenvoyer=new Trame ("000000000000000N"+ajouteZero(receiver.num)+"000000000000000000000000").toString();
  			  System.out.println("======>Envoie d'un acquitement de type N "+ trameAenvoyer);
  			  
  			  send(trameAenvoyer,ss);
  			  
		  }
		  Thread.sleep(100);
			  if(!ready&&receiver.b){
				  receiver.b=false;
				  ready = br.ready();
				  if(!ready){
					  System.out.println("======>fin de la connexion");
					  receiver.quit=false;
				  }else{
					  ready=true;
					  fenetre=br.readLine();
					  System.out.println("======><=======");

			  }
		     }
			  
		}
		writer.close();
	
	}
}	
	








































