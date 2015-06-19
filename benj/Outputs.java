package benj;

import java.io.File;


public class Outputs {
	private Outputs() { }
	
	public static final String	BASE_PATH			= Inputs.BASE_PATH + "/output/";
	public static final String	DATA_PATH			= BASE_PATH + "data/";
	public static final String	ABSTRACT_PATH		= DATA_PATH + "abstract/";

	public static final File	BASE_DIR			= new File(BASE_PATH);
	public static final File	DATA_DIR			= new File(DATA_PATH);
	public static final File	ABSTRACT_DIR		= new File(ABSTRACT_PATH);

	public static final File	RELATION_FILE		= new File(DATA_PATH + "relation.txt");
	public static final File	DIST_FILE			= new File(DATA_PATH + "dist.txt");
	
	public static final File	CONCORDANCE_FILE	= new File(DATA_PATH + "concordance.txt");
	public static final File	GRAPH_FILE			= new File(BASE_PATH + "graph.neato");
	public static final File	R_OUTPUT_FILE		= new File(BASE_PATH + "test.txt");
}
