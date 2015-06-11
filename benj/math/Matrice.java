package benj.math;

import java.util.List;

public interface Matrice<T> {
	public         T  get   (int i, int j);
	public Matrice<T> set   (int i, int j, T value);
	public List   <T> getRow(int i);
	public List   <T> getCol(int j);
	public int        nRows ();
	public int        nCols ();
}
