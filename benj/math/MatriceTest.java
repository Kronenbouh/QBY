package benj.math;

public class MatriceTest {
	public static void main(String[] args) {
		int n = 4;
		Matrice<Integer> mat = new TriMatrice<Integer>(n,Integer.class);
		for (int i=0, k=0 ; i<n ; i++) 
			for (int j=i ; j<n ; j++)
				mat.set(i,j,k++);
		
		System.out.println(mat);
		System.out.println(mat.getRow(2));
		System.out.println(mat.getCol(3));
	}
}
