package benj;

import java.io.File;

import benj.utils.FileUtils;

public class Inputs {
	private Inputs() { }
	
	public static final String	BASE_PATH	= FileUtils.canonicalCurrentDirectory();
	public static final String	DATA_PATH 	= BASE_PATH + "/data/";
	public static final File	SEARCH_FILE = new File(BASE_PATH + "/in/search.txt");
	public static final String	BIBLIO_PATH	= BASE_PATH + "/biblio/";

	public static final File	BIBLIO_DIR	= new File(BIBLIO_PATH);
	
	public static final File	DICO_FILE	= new File(DATA_PATH + "dico.txt");
}
