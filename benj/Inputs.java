package benj;

import java.io.File;

import benj.utils.FileUtils;

public class Inputs {
	private Inputs() { }
	
	public static final String	BASE_PATH	= FileUtils.canonicalCurrentDirectory();
	public static final String	BIBLIO_PATH	= BASE_PATH + "/biblio/";

	public static final File	BIBLIO_DIR	= new File(BIBLIO_PATH);
}
