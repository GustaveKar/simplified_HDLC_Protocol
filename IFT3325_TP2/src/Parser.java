import java.io.File;
import java.lang.Object;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.*;
public class Parser {
	LinkedList<Trame> trames = new LinkedList<Trame>();
	public Parser(String nomFichier){
	
		int cpt=0;
		FileInputStream fichier= null;
		try {
			fichier = new FileInputStream (new File(nomFichier));
			InputStream ips= new FileInputStream(nomFichier);
			InputStreamReader ipsr= new InputStreamReader(ips);
			BufferedReader br= new BufferedReader(ipsr);
			String ligne;
            while ((ligne=br.readLine())!=null){
            	System.out.println(ligne);
            	 String data=ligne;
            	 trames.add(new Trame("I",cpt,data));
            	 cpt++;
            	 
            }
			
			br.close();
		}catch(Exception e){
			System.out.println(e.toString());
		}
		
	}
	public LinkedList<Trame> getTrames (){
		return this.trames;
		
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
		  
		  }
		  catch (Exception e){
		   System.out.println(e.toString());
		  }
		  return binary.toString();
		}
	public LinkedList<Trame> getTrame(){
		return trames;
	}

}