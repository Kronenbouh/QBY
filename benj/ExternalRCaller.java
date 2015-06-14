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
	      
	      code.addRCode("inPath<-\""  + inPath + "\"");
	      code.addRCode("outPath<-\"" + outPath + "\"");
	      code.addRCode("source(paste(inPath,\"//lib//RApi//tools.R\",sep=\"\"))");
	      code.addRCode("source(paste(inPath,\"//lib//RApi//APImat.R\",sep=\"\"))");
	      code.addRCode("allMat(outPath)");
	      
	      caller.setRCode(code);
	      try {
		      caller.redirectROutputToFile(outPath + "/test.txt", true);
		      caller.runOnly();
	      } catch (IOException e) {
	    	  throw new RuntimeException(e);
	      }
	}
}
