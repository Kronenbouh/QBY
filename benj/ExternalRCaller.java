package benj;

import java.io.IOException;

import rcaller.RCaller;
import rcaller.RCode;

public class ExternalRCaller {

	public static void buildDistanceMatrix(String inPath, String outPath) {
	      RCaller caller = new RCaller();
	      caller.setRscriptExecutable("C://Program Files//R//R-3.1.0//bin//x64//Rscript.exe");
	      
	      RCode code = new RCode();
	      
	      code.clear();
	      
	      String code1 = String.format("inPath<-\""+inPath+"\"");
	      String code2 = String.format("outPath<-\""+outPath+"\"");
	      
	      
	      code.addRCode(code1);
	      code.addRCode(code2);
	      
	      code.addRCode("source(paste(inPath,\"//lib//RApi//tools.R\",sep=\"\"))");
	      code.addRCode("source(paste(inPath,\"//lib//RApi//APImat.R\",sep=\"\"))");
	      code.addRCode("allMat(outPath)");
	      
	      caller.setRCode(code);
	      try {
		      caller.redirectROutputToFile(String.format(outPath+"/test.txt"), true);
		      caller.runOnly();
	      } catch (IOException e) {
	    	  throw new RuntimeException(e);
	      }
	}
}
