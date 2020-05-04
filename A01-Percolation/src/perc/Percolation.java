package perc;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
/*
 * Class for percolation assignment, 
 * prepared for CSIS 2420, summer 2019
 * 
 *	@author dBartz
 */
public class Percolation {
	private boolean[][] grid;
	private WeightedQuickUnionUF wquf;
	private WeightedQuickUnionUF wquf_BW;
	private int N;
	/**
	 * 
	 * @param N
	 */
	public Percolation(int N) {
		if ( N <= 0) throw new IllegalArgumentException("N must be greater than zero.");
		this.N = N;
		wquf = new WeightedQuickUnionUF(N*N+2); // append two virtual sites
		wquf_BW = new WeightedQuickUnionUF(N*N+1); // append one virtual site for backwash handling
		grid = new boolean[N][N];
	} // create N-by-N grid with all sites blocked
	
	public void open(int i, int j) {
		validateIndices(i,j,N);
		grid[i][j] = true;
		int k = convertIndices(i,j,N);
		int[][] as = {{i+1,j}, {i,j+1}, {i-1,j}, {i,j-1}};
		for (int[] a : as) {
			try {
				if (isOpen(a[0],a[1])) {
					wquf.union(k, convertIndices(a[0],a[1],N));
					wquf_BW.union(k, convertIndices(a[0],a[1],N));
				}
			} catch (IndexOutOfBoundsException e) {
				if (a[0] == -1) { // our site is at the top:
					wquf.union(k, N*N);
					wquf_BW.union(k, N*N);
				}
				if (a[0] == N) { // our site is at the base:
					wquf.union(k, N*N+1);
				}
				continue; // if it's neither at top nor base and at edge.
			}
		}
	} // open site (row i, column j) if it is not open already
	
	public boolean isOpen(int i, int j) {
		validateIndices(i,j,N);
		return grid[i][j];
	} // is site (row i, column j) open?
	
	public boolean isFull(int i, int j) {
		validateIndices(i,j,N);
		if (isOpen(i,j) && wquf.connected(N*N, convertIndices(i,j,N))
				&& wquf_BW.connected(N*N, convertIndices(i,j,N))) {
			return true;
		}	return false;
	} // is site (row i, column j) full?
	
	public boolean percolates() {
		return wquf.connected(N*N, N*N+1);
	} // does the system percolate?
	
	/*
	 * Method to convert indices ( row i, col j ) into a single index as:
	 *  _					_
	 * | 	0	4	8	12	 |
	 * |	1	5	9	13	 |
	 * |	2	6	10	14	 |
	 * |_	3	7	11	15	_|
	 * 
	 */
	private int convertIndices(int i, int j, int N) {
		validateIndices(i,j,N);
		return N*j + i;
	}
	private void validateIndices(int i, int j, int N) {
		if (i < 0 || i >= N) {
			throw new IndexOutOfBoundsException("row index " + i + " must be between 0 and " + (N-1));
		}
		if (j < 0 || j >= N) {
			throw new IndexOutOfBoundsException("col index " + j + " must be between 0 and " + (N-1));
		}
	}
}
