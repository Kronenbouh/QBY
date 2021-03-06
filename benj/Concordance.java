package benj;

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
	private Concordance() { }
	
	public static void make(int nBiblios) {
		List<String> words = new ArrayList<>();
		
		try (BufferedReader br = Files.newBufferedReader(Inputs.DICO_FILE.toPath())) {
			String line;
			while((line = br.readLine()) != null) words.add(line);
			
			File out = Outputs.CONCORDANCE_FILE;
			for(int i=1 ; i<=nBiblios ; i++) {
				String path = Outputs.ABSTRACT_PATH + String.format("biblio101_%s.txt",i);
				compare(path,words,out);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void compare(String path, List<String> dico, File out) throws IOException {
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
}
