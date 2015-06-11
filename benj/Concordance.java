package benj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Concordance {
	
	public static void compare(String path, List<String> dico, File out) throws IOException {
		
		FileWriter fw 		   = new FileWriter(out,true);
        BufferedWriter bw      = new BufferedWriter(fw);
        
        StringBuilder sb    = new StringBuilder();
        File          input = new File(path);
        for (String line : Files.readAllLines(Paths.get(input.toURI()))) sb.append(line);
        
		for(int i=0;i<dico.size();i++){
			Pattern p = Pattern.compile(dico.get(i));
			Matcher m = p.matcher(sb.toString());
			while (m.find()){
				bw.write(input.getName().replaceFirst("[.][^.]+$", "")+":"+m.group(0)+"\n");
			}
		}
		bw.close();
	}
	
	public static void make(String inPath, String outPath,int N) throws IOException {
		
		InputStream dico = Concordance.class.getClassLoader().getResourceAsStream("data/dico.txt");
		
		List<String> words = new ArrayList<>();
		
		BufferedReader brDico = new BufferedReader(new InputStreamReader(dico));
		
		String line;
		while((line=brDico.readLine())!=null)
			words.add(line);
		
		brDico.close();
		
		File out = new File(String.format(outPath+"/data/concordance.txt"));
    	
    	if(out.exists()){
    		out.delete();
    		out.createNewFile();
    	}
    	
    	else{
    		out.createNewFile();
    	}
		
		for(int i=1;i<=N;i++){
			String path = String.format(outPath+String.format("/data/abstract/biblio101_%s.txt",i));
			compare(path,words,out);
		}
		
	}
}
