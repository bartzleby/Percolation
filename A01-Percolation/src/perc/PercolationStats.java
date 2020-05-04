package perc;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
/*
 * Class for percolationStats, 
 * prepared for CSIS 2420, summer 2019
 * 
 *	@author dBartz
 */
public class PercolationStats {
	
	private int N;
	private int T;
	
	private double[] tholds;
	
	private double mean;
	private double stddev;
	private double conLo;
	private double conHi;
	
	public PercolationStats(int N, int T) {
		if ( N <= 0) throw new IllegalArgumentException("N must be greater than zero.");
		if ( T <= 0) throw new IllegalArgumentException("T must be greater than zero.");
		
		this.N = N;
		this.T = T;
		
		tholds = new double[T];
		
		for (int ti = 0; ti < T; ti++) {
		
			Percolation p = new Percolation(N);
			
			int q1; int q2;
			int count = 0;
			while (!p.percolates()) {
				q1 = StdRandom.uniform(N);
				q2 = StdRandom.uniform(N);
				if (p.isOpen(q1,q2)) continue;
				p.open(q1, q2);
				count += 1;
			}
			tholds[ti] = (double)count/N/N;
		}
		
		mean = mean();
		stddev = stddev();
		conLo = confidenceLow();
		conHi = confidenceHigh();

	} // perform T independent experiments on an N-by-N grid
	
	public static void main(String args[]) {
		PercolationStats tps = new PercolationStats(200,100);
		printStats(tps);
	}
	
	private static void printStats(PercolationStats tps) {
		System.out.printf("PercolationStats(%d, %d):\n\n", tps.N, tps.T);
		System.out.printf("%25s: %f\n", "Mean", tps.mean);
		System.out.printf("%25s: %f\n", "Std. deviation", tps.stddev);
		System.out.printf("%25s: %f\n", "95pct confidence low", tps.conLo);
		System.out.printf("%25s: %f\n", "95pct confidence high", tps.conHi);
	}

	public double mean() {
		return StdStats.mean(tholds);
		} // sample mean of percolation threshold
	
	public double stddev() {
		return StdStats.stddev(tholds);
		} // sample standard deviation of percolation threshold
	
	public double confidenceLow() {
		double mean = StdStats.mean(tholds);
		double stddev = StdStats.stddev(tholds);
		double rootee = Math.sqrt(T);
		return mean - ((1.96*stddev)/rootee);
		} // low endpoint of 95% confidence interval
	
	public double confidenceHigh() {
		double mean = StdStats.mean(tholds);
		double stddev = StdStats.stddev(tholds);
		double rootee = Math.sqrt(T);
		return mean + ((1.96*stddev)/rootee);
	} // high endpoint of 95% confidence interval
	
}
