package benj;

import static benj.utils.FileUtils.getPathRelativeToClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import benj.utils.FileUtils;

public class Concordance {
	
	public static void compare(String path, List<String> dico, File out) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out,true))) {
	        String lines = FileUtils.readAllFile(path);
	        
			for(int i=0 ; i<dico.size() ; i++){
				Pattern p = Pattern.compile(dico.get(i));
				Matcher m = p.matcher(lines);
				while (m.find()) {
					String left  = new File(path).getName();
					left         = left.substring(0,left.lastIndexOf('.'));
					String right = m.group(0);
					bw.write(String.format("%s:%s\n",left,right));
				}
			}
        }
	}
	
	public static void make(String inPath, String outPath, int nBiblios) {
		
		List<String> words = new ArrayList<>();
		
		try (BufferedReader br = Files.newBufferedReader(getPathRelativeToClass(Concordance.class,"data/dico.txt"))) {
			String line;
			while((line = br.readLine()) != null) words.add(line);
			
			File out = new File(outPath + "/data/concordance.txt");
			for(int i=1 ; i<=nBiblios ; i++) {
				String path = String.format("%s/data/abstract/biblio101_%s.txt",outPath,i);
				compare(path,words,out);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
