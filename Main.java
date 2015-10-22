package z2.xseidl;


import java.io.File;
import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFileChooser;



public class Main {

	private static String contentOfAjFile;
	private static HashMap<String, Double> container; 
	
	private static final int NORMALIZACIA_BEZ_MEDZERY = 655616;
	private static final int NORMALIZACIA_S_MEDZERY = 655617;
	
	private static final String CHAR_SET_SK = "UTF-8";
	public static void main(String[] args)  {
		container = new HashMap<>();
		
		String[] path = vyberSubor();
		System.out.println("\n***+ "+path[1].toUpperCase()+" - s medzerami***");
		contentOfAjFile =normalize(path[0],path[1],NORMALIZACIA_S_MEDZERY,CHAR_SET_SK);
		System.out.println(contentOfAjFile);
	    getNGram(contentOfAjFile,1);
	    entropia("\nEntropia pre None-Gram",container);
		container.clear();
		getNGram(contentOfAjFile,2);
		entropia("\nEntropia pre Bi-Gram",container);
		container.clear();
		getNGram(contentOfAjFile,3);
		entropia("\nEntropia pre Tri-Gram",container );
		container.clear();
		
		System.out.println("\n\n*** "+path[1].toUpperCase()+" - bez medzier***");
		contentOfAjFile =normalize(path[0],path[1],NORMALIZACIA_BEZ_MEDZERY,CHAR_SET_SK);
		System.out.println(contentOfAjFile);
		getNGram(contentOfAjFile,1);
	    entropia("\nNone-Gram: ",container);
		container.clear();
		getNGram(contentOfAjFile,2);
		entropia("\nBi-Gram: ",container);
		container.clear();
		getNGram(contentOfAjFile,3);
		entropia("\nTri-Gram: ",container );
		container.clear();
		
		 path = vyberSubor();
		System.out.println("\n\n*** "+path[1].toUpperCase()+"- s medzerami***");
		contentOfAjFile =normalize(path[0],path[1],NORMALIZACIA_S_MEDZERY,CHAR_SET_SK);
		
		System.out.println(contentOfAjFile);
		
	    getNGram(contentOfAjFile,1);
	    entropia("\nEntropia pre None-Gram",container);
		container.clear();
		getNGram(contentOfAjFile,2);
		entropia("\nEntropia pre Bi-Gram",container);
		container.clear();
		getNGram(contentOfAjFile,3);
		entropia("\nEntropia pre Tri-Gram",container );
		container.clear();
		
		System.out.println("\n\n*** "+path[1].toUpperCase()+" - bez medzier***");
		contentOfAjFile =normalize(path[0],path[1],NORMALIZACIA_BEZ_MEDZERY,CHAR_SET_SK);
		getNGram(contentOfAjFile,1);
	    entropia("\nEntropia pre None-Gram",container);
		container.clear();
		getNGram(contentOfAjFile,2);
		entropia("\nEntropia pre Bi-Gram",container);
		container.clear();
		getNGram(contentOfAjFile,3);
		entropia("\nEntropia pre Tri-Gram",container );
		container.clear();    
	}
	private static void entropia(String text,HashMap<String, Double> map){
		Double vyslednaHodnota  = 0.0;
		for(String kluc: container.keySet()){
			Double  hodnota  = container.get(kluc);
			vyslednaHodnota += hodnota * log(hodnota, 2);		
		}
		
		System.out.print(text+vyslednaHodnota*(-1));
	}
	
	private static void getNGram( String text,int n){
		int counter = 0;
		for( int i = 0; i< text.length()-(n-1);i++){
			String tmp_subString = text.substring(i, i+n);
			if(!container.containsKey(tmp_subString)){
				container.put(tmp_subString, (double) 1);
			}else{
				container.put(tmp_subString, (double) (container.get(tmp_subString)+1));
			}
		}
		for(String kluc: container.keySet()){
			Double  hodnota  = container.get(kluc);
			container.put(kluc, hodnota/(text.length()-(n-1)));	
		}
		
	}
	 
	private static String[] vyberSubor(){
		JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("./"));
        fc.setMultiSelectionEnabled(false);
        int res = fc.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
             File f = fc.getSelectedFile();
              String[] path = 
              path = new String[2];
              path[0] = f.getPath();
              path[1] = f.getName();      
              return path;
         }
        return null;
       
	}
	
	private static String normalize( String path, String nameFile, int tag,String charsetS) {  				
		Path aj_patch = Paths.get(path.replace(nameFile, ""), nameFile);	
		Charset charset = Charset.forName(charsetS);
        StringBuffer mBuilder = new StringBuffer();
        try {
			List<String> lines = Files.readAllLines(aj_patch,charset);
			
			for (String line : lines) {
				mBuilder.append(line);
		      }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        switch(tag){
        	case NORMALIZACIA_BEZ_MEDZERY: return Normalizer.normalize(mBuilder, Form.NFKD).replaceAll("[^A-Za-z0-9]", "").replaceAll(" ", "").toLowerCase().trim();
        									
        	case NORMALIZACIA_S_MEDZERY: return Normalizer.normalize(mBuilder, Form.NFKD).replaceAll("[^A-Za-z0-9 ]", "").toLowerCase().trim();			
        	default:return null;	
        }
	}
	
	private static double log(Double x, int base)
	{
	    return  (Math.log(x) / Math.log(base));
	}
}
