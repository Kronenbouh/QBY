package benj;

import static benj.utils.FileUtils.readAllFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiblioParser {
	public static void extract(int nBiblios) {
    	for(int i=1 ; i<=nBiblios ; i++) {
    		try {
	    		File   biblio  = new File(Outputs.BASE_PATH + String.format("biblio101_%s.txt",i));
	    		String xmlPath = Inputs.BIBLIO_PATH + String.format("biblio101_%s.xml",i);
	    		
				extractAUandWC(xmlPath,";",Outputs.RELATION_FILE);
	        	extractAB(xmlPath,biblio);
    		} catch (IOException e) {
    			throw new RuntimeException(e);
    		}
    	}
    }
	
    private static void extractAUandWC(String path, String sep, File out) throws IOException {
        String       lines = readAllFile(path);
        List<String> aut   = extractTag("AU",lines,sep);
        List<String> dom   = extractTag("WC",lines,sep);    
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out,true))) {
	        for (int i=0 ; i<aut.size() ; i++) {
	        	for (int j=0 ; j<dom.size() ; j++) {
	        		bw.write("\"" + path.substring(path.lastIndexOf("biblio"),path.lastIndexOf('.')) + "\""  + ":");
	        		bw.write("\"" + aut.get(i).toString().trim() + "\""  + ":");
	        		bw.write("\"" + dom.get(j).toString().trim() + "\"" + "\n");
	        	}
	        }
        }
    }

    private static void extractAB(String path, File out) throws IOException {
        Matcher m = getTagMatcher("AB",readAllFile(path));
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out,true))) {
	        while (m.find()) bw.write(m.group(1));
        }       
    }

    private static List<String> extractTag(String tag, String lines, String sep) {
    	List<String> aut = new ArrayList<>();
    	Matcher      m   = getTagMatcher(tag,lines);
    	
    	while (m.find()) aut.addAll(Arrays.asList(m.group(1).split(sep)));
    	return aut;
    }

    private static Matcher getTagMatcher(String tag, String input) {
    	Pattern p = Pattern.compile(String.format("<%s>(.*)</%s>",tag,tag));
    	Matcher m = p.matcher(input);
    	return m;
    }
}