package benj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import benj.math.ArrayIndexComparator;
import benj.math.TriMatrice;
import benj.utils.FileUtils;

public class Dist {	
	public static TriMatrice<String> mat(String outPath) throws IOException {
	
//		InputStreamReader isr = new InputStreamReader(Dist.class.getResourceAsStream("dist.txt"));
		
		FileReader dist = new FileReader(String.format(outPath+"/data/dist.txt"));
		
		BufferedReader brDist = new BufferedReader(dist);
		Pattern p = Pattern.compile(";");
		
		String title=brDist.readLine();
		
		String[] obj = p.split(title);
			
		int n=obj.length;
				
		TriMatrice<String> distMat = new TriMatrice<String>(n,String.class);
		
		for(int j=0;j<n;j++)
			distMat.set(0,j,obj[j]);
		
		int i=1;
		String line;
		while((line=brDist.readLine())!=null){
			String[] tmp = p.split(line);
			for(int j=i;j<n;j++)
				distMat.set(i,j,tmp[j-i]);
			i++;
		}
				
		brDist.close();	
		return distMat;
	}
		
	public static String[][] find(String which, int max, String outPath) throws IOException{
		TriMatrice<String> distMat = mat(outPath);
		List<String> obj = distMat.getRow(0);
		List<Float> obj2 = new ArrayList<>();
		int where = obj.indexOf(which);
		int N = obj.size();
		for(int i=1;i<=where;i++){
			obj2.add(Float.parseFloat(distMat.get(i,where)));
		}
		for(int j=where+1;j<N;j++){
			System.out.println("le j "+j);
			System.out.println(distMat.get(where+1,j));
			obj2.add(Float.parseFloat(distMat.get(where+1,j)));
		}
		obj.remove(which);
		
		Float [] length = new Float[N-1];
		
		for(int k=0;k<N-1;k++){
			length[k]=obj2.get(k);
		}
		
		ArrayIndexComparator comparator = new ArrayIndexComparator(length);
		Integer[] indexes = comparator.createIndexArray();
		Arrays.sort(indexes, comparator);
		Arrays.sort(length);
		
		String[][] res = new String[max][max];
		
		for(int k=0;k<max;k++){
			res[0][k]=obj.get(indexes[k]);
			res[1][k]=Float.toString(length[k]);
		}
		
		return res;
	}
	
	public static String calculWeight(String toParse)throws IOException{
		String res;
		float tmp=Float.parseFloat(toParse);
		if(tmp==0.0)
			res="1";
		else if(tmp<0.4)
			res="0.7";
		else if(tmp<0.7)
			res="0.7";
		else
			res="0.7";
		return res;
	}
	
	public static String calculLen(String toParse)throws IOException{
		String res;
		float tmp=Float.parseFloat(toParse);
		res=Float.toString(50*(0.01f+tmp));
		return res;
	}
	
	public static String decideColor(String toParse)throws IOException{
		String res;
		float tmp=Float.parseFloat(toParse);
		if(tmp<=0.3)
			res="red";
		else
			res="blue";
		return res;			
	}
	
	public static void writeGraph(String which, int max, String inPath, String outPath) throws IOException{
		String[][] toWrite = find(which, max, outPath);
		int N=toWrite.length;
		File out = new File(String.format(outPath+"/graph.neato"));
		
    	if(out.exists()){
    		out.delete();
    		out.createNewFile();
    	}
		
        FileWriter fw 		   = new FileWriter(out,true);
        BufferedWriter bw      = new BufferedWriter(fw);
        
        bw.write("graph G{\n");
        bw.write("\tnode[fontsize=5]\n");
        bw.write("\t[style=filled]\n");
        bw.write("\t"+"\""+which+"\""+"[fillcolor=\"red\"]\n");
        bw.write("\t[pack=false]\n");
        bw.write("\t[defaultdist=0]\n");
        for(int k=0;k<N;k++){
        	bw.write("\t"+"\""+which+"\""+"--"+"\""+toWrite[0][k]+"\""+" [len="+calculLen(toWrite[1][k])+"]"+"[weight="+calculWeight(toWrite[1][k])+"][color="+decideColor(toWrite[1][k])+"]\n");
        }
        
        bw.write("}");
        bw.close();
        ProcessBuilder pb = new ProcessBuilder("neato","-Tpdf",out.getAbsolutePath(),"-o",FileUtils.toExtension(out,".pdf").getAbsolutePath());
        pb.start();
	}
	
	public static void main(String[] args) throws IOException {
		
		final File f2 = new File(Dist.class.getProtectionDomain().getCodeSource().getLocation().getPath());
    	
		String inPath = f2.getParent().replaceAll("\\\\", "/");
		
		String outPath = String.format(inPath+"/output");

		File dir1 = new File(outPath);
		if(dir1.exists()==false)
			dir1.mkdirs();
		
		File dir2 = new File(String.format(outPath+"/data"));
		if(dir2.exists()==false)
			dir2.mkdirs();
		
		File dir3 = new File(String.format(outPath+"/data/abstract"));
		if(dir3.exists()==false)
			dir3.mkdirs();		
		
		File raw = new File(String.format(inPath+"/biblio/"));
    	
    	int N=raw.list().length;
		
		File dist = new File(String.format(outPath+"/data/dist.txt"));
		
		File relation = new File(String.format(outPath+"/data/relation.txt"));
			
		boolean update1=dist.exists();
		
		boolean update2=relation.exists();
		
		if(update2){
			int ans2 = JOptionPane.showConfirmDialog(null,"Avez-vous ajouté de nouveaux textes bruts ?", "Loria", JOptionPane.YES_NO_OPTION);
			update2=ans2 == JOptionPane.OK_OPTION;
			if(update2==true)
				Extract.doExtract(inPath,outPath,N);
				Concordance.make(inPath,outPath,N);
		}
		
		else{
			Extract.doExtract(inPath,outPath,N);
			Concordance.make(inPath,outPath,N);
		}
		
		if(update1){
			int ans1 = JOptionPane.showConfirmDialog(null,"Voulez-vous recalculer les distances ?", "Loria", JOptionPane.YES_NO_OPTION);
			update1=ans1 == JOptionPane.OK_OPTION;
			if(update1==true)
				CastR.castR(inPath,outPath);
		}
		
		else{
			CastR.castR(inPath,outPath);
		}
		
		String toFind = "Schneider, C";
		writeGraph(toFind,10,inPath,outPath);
	}	
}
	
