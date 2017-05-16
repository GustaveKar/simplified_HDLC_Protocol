
import java.lang.StringBuilder;
import java.util.Scanner;
public class Trame {
	final String flag="01111110" ;
	final int bit = 48;
	String type="";
	String Data="";
    String crc="";
    String typebin;
    String numeroBi;
    int numero;
	public  Trame(String type,int numero,String data,String crc ){
		this.typebin=toBin(type);
		this.type="0000000"+type;
		this.Data=data;
		String num= Integer.toString(this.numero);
	    num= toBin(num).toString();
		this.numeroBi=ajouteZero(numero,8);
		this.numero=numero;
		this.crc=crc;
		
		//System.out.println("------>"+this.type);
		
	}
	public Trame (String trame){
		System.out.println(trame);
		int taille= trame.length();
		this.type= trame.substring(8,16);
		this.typebin=this.type;
		this.numeroBi=trame.substring(16,24);
		this.numero = Integer.parseInt(this.numeroBi);
		if (this.type.compareTo("0000000D")!=0 && this.type.compareTo("0000000I")!=0){
			this.numeroBi=trame.substring(16,24);
			this.crc=trame.substring(24,40);
			this.numero = Integer.parseInt(this.numeroBi);
			//System.out.println(numeroBi);
			//System.out.println(type);
		}
		
			else {
				
				if (this.type.compareTo("0000000I")==0){
					
					this.numeroBi=trame.substring(16,24);
					this.Data=trame.substring(24,24+(taille-bit));
					this.crc=trame.substring(24+(taille-bit),24+(taille-bit)+16);
					//System.out.println(crc);
					this.numero = Integer.parseInt(this.numeroBi);
				}
				}
			
		
		}
	public String getnumeroBi(){
		return this.numeroBi;
	}
	public  Trame(String type,int numero,String data){
		//System.out.println("type1 "+type);
		this.typebin="0000000"+type;
		this.Data=data;
		this.type = typebin;
		this.typebin=toBin("0000000"+type);
		this.numeroBi= ajouteZero(numero,8);
		this.numero = Integer.parseInt(this.numeroBi);
		String data1= toBin(data);
		int cpt=0;
		boolean t= true;
		while (cpt<data1.length() && t){
			//System.out.println("+++++++++++++++++"+data1.substring(0,1)+" "+cpt);
			if(data1.substring(0,1)=="0"){
			   cpt++;
			  // System.out.println("+++++++++++++++++*****>"+data1.substring(0,1)+" "+cpt);
			   }
			   else
				   t=false;
		}
		
			//data1=data1.substring(2);
		    //System.out.println("+++++++++++++++++"+data1+" "+cpt);
		this.crc= crc(this.flag+toBin(this.type)+toBin(this.numeroBi)+toBin(this.Data)+this.flag,"10000101010111111");
					
		
		this.crc=ajouteZero(this.crc,16);
		//System.out.println(crc);
        
		//System.out.println("------>"+this.type);
		
	}
	/*public Trame (String type, String numero,String crc){
		this.typebin=toBin(type);
		this.type=type;
		this.numeroBi= numero;
		this.crc=crc;
	}*/
    

	public static void  main (String [] args){
		/*Trame v= new Trame ("I",7,"deodoeod");
		Trame vg= new Trame (v.toStringBi());
		System.out.println(v.toStringBi());
		System.out.println(crc(v.toStringBi(),"10000100100"));*/
	
		System.out.println(ajouteZero(11,8));
	}
	
	public String getData(){
		return this.Data;
		
	}
	public String getFlag(){
		return this.flag;
	}
	public String getCrc(){
		return this.crc;
	}
	
	public String getType(){
		return type;
	}
	public String getTypebin(){
		return typebin;
	}

	public int getNumero(){
		return numero;
	}
	public String getDataBi(){
		return toBin(this.Data);
	}
	public String toString(){
		
		return this.flag+this.type+this.numeroBi+ this.Data +this.crc+this.flag;
	}
public String toStringBi(){
		
		return this.flag+toBin(this.type)+toBin(this.numeroBi)+ toBin(this.Data )+this.flag;
	}
	public static String ajouteZero(int numero,int bit){
		String text= Integer.toString(numero);
		int taille=bit-text.length();
		String num="";
		for (int i=0;i<taille;i++)
			num+="0";
		return num+text;
	}
	public static String ajouteZero(String text,int bit){
		
		int taille=bit-text.length();
		String num="";
		for (int i=0;i<taille;i++)
			num+="0";
		return num+text;
	}
	public static String toBin(String info){
		  byte[] bytes = null;
		  StringBuilder binary = new StringBuilder();
		  try {
		   bytes = info.getBytes( "UTF-8" );
		   
		   
		   for (byte b : bytes)
		   {
		      int val = b;
		      for (int i = 0; i < 8; i++)
		      {
		         binary.append((val & 128) == 0 ? 0 : 1);
		         val <<= 1;
		      }
		      
		   }
		   //System.out.println("infoBin: "+binary);
		  }
		  catch (Exception e){
		   System.out.println(e.toString());
		  }
		  return binary.toString();
		}
	public static String crc(String texte1, String texte2){

		 int cpt=0,cpt1=0;
		    int taille1= texte1.length();
		    int taille2= texte2.length();
		   // System.out.println("taille1 : "+taille1);
		    //System.out.println("taille1 : "+taille2);
		    Scanner pauser = new Scanner (System.in);
	       
		    int reste;
		    cpt=taille2;
		    if (taille1< taille2)
		    	return texte1;
		    String  texte= texte1.substring(0,taille2);
		    //System.out.println("partie     texte1: "+texte1+ "texte "+ texte);
		    while((cpt <=taille1)&&(texte.length()==taille2)||cpt1==0){
		    	cpt1++;
		    	//System.out.println("partie reste***  texte1: "+texte1+ "texte "+ texte+" compare "+texte.compareTo(texte2)+"compter "+ cpt +"  "+ taille1);
		    	
		    	
		    	if ((texte.compareTo(texte2)>=0 && texte.length()==taille2) ||(texte.length()==taille2)){
		    		//System.out.println("partie div");
		    		reste=Integer.parseInt(texte,2)^Integer.parseInt(texte2,2);
		    		//System.out.println("partie div "+reste);
		    		if (reste!=0){
		    		texte= Integer.toBinaryString(reste);
		    		//System.out.println("partie div "+texte);
		    		}
		    		
		    		else
		    			texte="";
		    		//System.out.println(texte+"    "+(texte.compareTo(texte2)));
		    		if (cpt==taille1)
		    			if (texte=="")
		    				return "0";
		    			else
		    			return texte;
		    		
		    		while(((texte.compareTo(texte2)<0)&& cpt<=taille1-1&&texte.length()<taille2 && cpt<=taille1)|| (texte.length()<taille2 && cpt<=taille1)){
		    		//	System.out.println("partie reste");
		    			if (texte!="")
		    			if (Integer.parseInt(texte,2)==0 && texte.length()==taille2)
		    	    		texte="";
		    				
		    			if (cpt<taille1-1)
		    			texte+=texte1.substring(cpt,cpt+1);
		    			else
		    				texte+=texte1.substring(cpt);
		    			
		    			//System.out.println("texte--->"+texte+ " "+texte1.substring(cpt,cpt)+" taille = "+texte.length());
		    			cpt++;
		    			//System.out.println("texte--->"+ cpt);
		    			//pauser.nextLine();
		    		}
		    		
		    	}
		    	
		    	
		    }
		    if (cpt==taille1)
				if (texte=="")
					return "0";
				else
				return texte;
		    
		   //
		    return texte;
		    
		 
		 
		 
	 }
	 
}
