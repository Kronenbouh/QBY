package benj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {
	
    private static void extract1(String path, String sep, File out) throws IOException {
        StringBuilder sb    = new StringBuilder();
        File          input = new File(path);
        for (String line : Files.readAllLines(Paths.get(input.toURI()))) sb.append(line);
        
        String tag1="AU";
        String tag2="WC";
       
        
        Pattern p1 = Pattern.compile(String.format("<%s>(.*)</%s>",tag1,tag1));
        Pattern p2 = Pattern.compile(String.format("<%s>(.*)</%s>",tag2,tag2));
        Matcher m1 = p1.matcher(sb.toString());
        Matcher m2 = p2.matcher(sb.toString());
        
        FileWriter fw 		   = new FileWriter(out,true);
        BufferedWriter bw      = new BufferedWriter(fw);
        
        List<String> aut = new ArrayList<>();
        List<String> dom = new ArrayList<>();       
        
        
        while (m1.find()) 
        	for(String s : m1.group(1).split(sep))
        		aut.add(s);

        while (m2.find())
        	for(String s : m2.group(1).split(sep))
        		dom.add(s);
        		
        for(int i=0;i<aut.size();i++){
        	for(int j=0;j<dom.size();j++){
        		bw.write("\"" + path.substring(path.lastIndexOf("biblio"),path.lastIndexOf('.')) + "\""  + ":");
        		bw.write("\"" + aut.get(i).toString().trim() + "\""  + ":");
        		bw.write("\"" + dom.get(j).toString().trim() + "\"" + "\n");
        	}
        }
        
        bw.close();
    }
    
    private static void extract2(String path, File out) throws IOException {
        StringBuilder sb    = new StringBuilder();
        File          input = new File(path);
        for (String line : Files.readAllLines(Paths.get(input.toURI()))) sb.append(line);
        
        String tag1="AB";       
        
        Pattern p1 = Pattern.compile(String.format("<%s>(.*)</%s>",tag1,tag1));
        Matcher m1 = p1.matcher(sb.toString());
        
        FileWriter fw 		   = new FileWriter(out,true);
        BufferedWriter bw      = new BufferedWriter(fw);
        
        while (m1.find())
        	bw.write(m1.group(1));
           
        bw.close();
    }
        
    public static void doExtract(String inPath, String outPath, int N) {
    	
    	File out = new File(String.format(outPath+"/data/relation.txt"));
    	
    	for(int i=1 ; i<=N ; i++) {
    		try {
	    		File out2=new File(String.format(outPath+String.format("/data/abstract/biblio101_%s.txt",i)));
	        	
	    		extract1(String.format(inPath+String.format("/biblio/biblio101_%s.xml",i)),";",out);
	        	extract2(String.format(inPath+String.format("/biblio/biblio101_%s.xml",i)),out2);
    		} catch (IOException e) {
    			throw new RuntimeException(e);
    		}
    	}
    }
}