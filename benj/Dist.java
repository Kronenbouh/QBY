package benj;

import static benj.math.MathUtils.isZero;
import static benj.utils.FileUtils.canonicalCurrentDirectory;
import static benj.utils.FileUtils.ensureExists;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.util.Pair;

import javax.swing.JOptionPane;

import benj.math.MathUtils;
import benj.math.Matrix;
import benj.math.SimpleMatrix;
import benj.math.TriMatrix;
import benj.utils.FileUtils;

public class Dist {	
	private static final int INFINITY = 100000;
	
	public static TriMatrix<String> mat(String outPath) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(Dist.class.getResourceAsStream(outPath + "/data/dist.txt")))) {
			Pattern           separator = Pattern.compile(";");
			String            title     = br.readLine();		
			String[]          obj       = separator.split(title);
			TriMatrix<String> distMat   = initDistanceMatrix(obj);		
			populateDistanceMatrix(br,separator,obj,distMat);	
			return distMat;
		}
	}

	private static TriMatrix<String> initDistanceMatrix(String[] obj) {
		TriMatrix<String> distMat = new TriMatrix<String>(obj.length,String.class);
		for(int j=0 ; j<obj.length ; j++) 
			distMat.set(0,j,obj[j]);
		return distMat;
	}

	private static void populateDistanceMatrix(BufferedReader br, Pattern p, String[] obj, TriMatrix<String> distMat) throws IOException {
		int i = 1;
		String line;
		while((line = br.readLine()) != null){
			String[] tmp = p.split(line);
			for(int j=i ; j<obj.length ; j++)
				distMat.set(i,j,tmp[j-i]);
			i++;
		}
	}
		
	public static Matrix<String> find(String which, int scope, String outPath) throws IOException{
		TriMatrix<String> distMat  = mat(outPath);
		List<String>      firstRow = distMat.getRow(0);
		
		List<Float>                values          = fillWithNeighbours(which,distMat,firstRow);
		List<Pair<Integer, Float>> sortedWithIndex = sortWithIndex(values);
		 
		return closestNeighbours(scope,firstRow,sortedWithIndex);
	}

	private static Matrix<String> closestNeighbours(int max, List<String> firstRow, List<Pair<Integer, Float>> sortedWithIndex) {
		Matrix<String> res = new SimpleMatrix<>(2,max,String.class);
		for (int k=0 ; k<max ; k++) {
			Pair<Integer,Float> pair = sortedWithIndex.get(k);
			res.set(0,k,firstRow.get(pair.getKey()))
			   .set(1,k,String.valueOf(pair.getValue()));
		}
		return res;
	}

	private static List<Pair<Integer, Float>> sortWithIndex(List<Float> values) {
		return IntStream.range(0,values.size())
			.mapToObj(i -> new Pair<>(i,values.get(i)))
			.sorted((p0,p1) -> MathUtils.epsilonCompare(p0.getValue(),p1.getValue()))
			.collect(Collectors.toList());
	}

	private static List<Float> fillWithNeighbours(String which, TriMatrix<String> mat, List<String> row) {
		List<Float> values = new ArrayList<>();
		int         where  = row.indexOf(which);
		for(int i=1       ; i<=where     ; i++) values.add(Float.parseFloat(mat.get(i      ,where)));
		for(int j=where+1 ; j<row.size() ; j++) values.add(Float.parseFloat(mat.get(where+1,j    )));
		row.remove(which);
		return values;
	}
	
	public static float computeWeight(String toParse) throws IOException{
		float f = Float.parseFloat(toParse);
		return isZero(f) ? INFINITY : 1/f;
	}
	
	private static void writeGraph(String which, int max, String inPath, String outPath) throws IOException  {
		Matrix<String> toWrite = find(which, max, outPath);
		
		File out = new File(outPath + "graph.neato");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
	        bw.write("graph G{\n");
	        bw.write("\tnode[fontsize=5]\n");
	        bw.write("\t[style=filled]\n");
	        bw.write("\t"+"\""+which+"\""+"[fillcolor=\"red\"]\n");
	        bw.write("\t[pack=false]\n");
	        bw.write("\t[defaultdist=0]\n");
	        for (int k=0 ; k<toWrite.nRows() ; k++)
	        	bw.write(String.format("\t\"%s\"--\"%s\" [len=%f][weight=%f][color=%s]\n",
	        			which,
	        			toWrite.get(0,k),
	        			computeLen(toWrite.get(1,k)),
	        			computeWeight(toWrite.get(1,k)),
	        			decideColor(toWrite.get(1,k))));
	        bw.write("}");
        }
        
        ProcessBuilder pb = new ProcessBuilder("fdp","-Tpdf",out.getAbsolutePath(),"-o",FileUtils.toExtension(out,".pdf").getAbsolutePath());
        pb.start();
	}
	
	private static String decideColor(String toParse)throws IOException{
		return Float.parseFloat(toParse) <= 0.3 ? "red" : "blue";
	}
	
	private static float computeLen(String toParse)throws IOException{
		return 50*(0.01f + Float.parseFloat(toParse));
	}
	
	public static void main(String[] args) throws IOException {
		String inPath  = canonicalCurrentDirectory();
		String outPath = inPath + "/output";
		ensureExists(outPath + "/data/abstract");
		
		File biblioDir = new File(inPath + "/biblio/");
    	int  nBiblios  = biblioDir.list().length;
		
    	updateIfNecessary(new File(outPath + "/data/relation.txt"), "Avez-vous ajouté de nouveaux textes bruts ?", () -> {
    		Extract.doExtract(inPath,outPath,nBiblios);
			Concordance.make(inPath,outPath,nBiblios);
    	});
		updateIfNecessary(new File(outPath + "/data/dist.txt"), "Voulez-vous recalculer les distances ?", () -> ExternalRCaller.buildDistanceMatrix(inPath,outPath));
		
		String toFind = "Schneider, C";
		writeGraph(toFind,10,inPath,outPath);
	}

	private static void updateIfNecessary(File f, String msg, Runnable update) {
		if (f.exists()) {
			int ans = JOptionPane.showConfirmDialog(null,msg, "Loria",JOptionPane.YES_NO_OPTION);
			if( ans == JOptionPane.OK_OPTION) update.run();
		} else 
			update.run();
	}
}