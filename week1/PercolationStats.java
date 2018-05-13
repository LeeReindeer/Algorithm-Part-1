import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * @author leer
 * Created at 4/16/18 9:07 AM
 */
public class PercolationStats {

  private int n;
  private int trials;

  private double[] rateOfThreshold;

  /**
   * @param n      a n * n grid
   * @param trials trials times independent experiments
   */
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    this.trials = trials;
    this.rateOfThreshold = new double[trials];
  }

  /**
   * @return sample mean of percolation threshold
   */
  public double mean() {
    return StdStats.mean(rateOfThreshold);
  }

  /**
   * @return sample standard deviation of percolation threshold
   */
  public double stddev() {
    return StdStats.stddev(rateOfThreshold);
  }

  /**
   * @return low  endpoint of 95% confidence interval
   */
  public double confidenceLo() {
    return mean() - (0.025 / (Math.sqrt(trials)));
  }

  /**
   * @return high  endpoint of 95% confidence interval
   */
  public double confidenceHi() {
    return mean() + (0.025 / (Math.sqrt(trials)));
  }

  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int t = Integer.parseInt(args[1]);

    PercolationStats stats = new PercolationStats(n, t);
    int count = 0;
    while (count < t) {
      Percolation p = new Percolation(n);
      while (!p.percolates()) {
        p.open(StdRandom.uniform(1, n + 1), StdRandom.uniform(1, n + 1));
      }
      stats.rateOfThreshold[count] = (double)p.numberOfOpenSites() / (n * n);
      count++;
    }
    System.out.println("mean                     = " + stats.mean());
    System.out.println("stddev                   =" + stats.stddev());
    System.out.println("95%% confidence interval  = "+ "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
  }
}
