package benj.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
	private FileUtils() { }
	
	public static String currentDirectory() {
		String path = new File(".").getAbsolutePath();
		return path.substring(0,path.length() - 2);
	}
	
	public static String canonicalCurrentDirectory() { return toCanonicalPath(currentDirectory()); }
	
	public static String toCanonicalPath(String path) { return path.replace(File.separatorChar,'/'); }
	
	public static void ensureExists(File dir) {
		if(!dir.exists()) dir.mkdirs();
	}

	public static boolean hasExtension(String path, List<String> extensions) {
		return hasExtension(new File(path),extensions);
	}
	
	public static boolean hasExtension(File file, List<String> extensions) {
		return extensions.stream().filter(file.getName()::endsWith).findAny().isPresent();
	}
	
	public static File toExtension(String path, String extension) {
		return toExtension(new File(path),extension);
	}
	
	public static File toExtension(File file, String extension) {
		if (hasExtension(file,Arrays.asList(extension))) return file;
		
		if (!extension.startsWith(".")) extension = "." + extension;
		
		String path = file.getAbsolutePath();
		int lastDot = path.lastIndexOf('.');
		return new File((lastDot < 0 ? path : path.substring(0,lastDot)) + extension);
	}
	
	public static String readAllFile(String path) throws IOException {
    	StringBuilder sb    = new StringBuilder();
    	File          input = new File(path);
    	for (String line : Files.readAllLines(Paths.get(input.toURI()))) sb.append(line);
    	String lines = sb.toString();
    	return lines;
    }
}