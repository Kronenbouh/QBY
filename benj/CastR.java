package benj;

import java.io.File;
import java.io.IOException;

import rcaller.RCaller;
import rcaller.RCode;

public class CastR {

	public static void castR(String inPath, String outPath) throws IOException {

		  File dist = new File(String.format(outPath+"/data/dist.txt"));
		  File rep = new File(String.format(outPath+"/data/rep.txt"));
		  
	    	if(dist.exists()){
	    		dist.delete();
	    		dist.createNewFile();
	    	}
	    	
	    	else{
	    		dist.createNewFile();
	    	}
	    	
	    	if(rep.exists()){
	    		rep.delete();
	    		rep.createNewFile();
	    	}
	    	
	    	else{
	    		rep.createNewFile();
	    	}
	    	
	    	
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
	      caller.redirectROutputToFile(String.format(outPath+"/test.txt"), true);
	      caller.runOnly();
	}
}
