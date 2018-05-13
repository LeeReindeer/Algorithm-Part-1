import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author leer
 * Created at 4/16/18 9:07 AM
 * Dos:
 * - use StdRandom, WeightedQuickUnionUF
 * - array indices bwtween 1 and n, not 0 and n -1
 * - virtual-top / virtual-bottom
 * <p>
 * Do nots:
 * - don't use package sentence
 * - don't use any data structures in java.util.*
 * - don't catch any exceptions, just throw
 */

public class Percolation {

  /**
   * Hint: At minimum, you'll need to store
   * the grid size -> n
   * which sites are open -> sites
   * and which sites are connected to which other sites.
   * The last of these is exactly what the union-find data structure is designed for.
   */
  private int n;
  private int openSize;
  private boolean[][] sites;

  private WeightedQuickUnionUF quickUnion;

  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    sites = new boolean[n + 1][n + 1];
    quickUnion = new WeightedQuickUnionUF((n * n) + 2);

    //union virtual top with first row and virtual bottom...
    for (int i = 1, j = n * (n - 1) + 1; i <= n; i++, j++) {
      quickUnion.union(0, i);
      quickUnion.union(n * n + 1, j);
    }
  }

  /**
   * valid row and col
   *
   * @throws IllegalArgumentException if invalid
   */
  private void checkRange(int row, int col) {
    if (row > n || col > n || row < 1 || col < 1) {
      throw new IllegalArgumentException();
    }
  }

  private boolean checkRangeNoThrow(int row, int col) {
    return row <= n && col <= n && row >= 1 && col >= 1;
  }

  /**
   * convert 2D [row, col] to 1D array index
   *
   * @return index in 1D array
   */
  private int xyTo1D(int row, int col) {
    return (row - 1) * n + (col);
  }

  /**
   * open a site (row, col) if it is not open already
   * <p>
   * check [right, top, left, below] sites' isOpen, if isOpen use
   * WeightedQuickUnionUF.union() to connect them.
   */
  public void open(int row, int col) {
    checkRange(row, col);
    if (isOpen(row, col)) {
      return;
    }
    sites[row][col] = true;
    openSize++;

    //top
    if (checkRangeNoThrow(row - 1, col) &&
        isOpenNoThrow(row - 1, col)) {
      quickUnion.union(xyTo1D(row, col), xyTo1D(row - 1, col));
    }
    //left
    if (checkRangeNoThrow(row, col - 1) &&
        isOpenNoThrow(row, col - 1)) {
      quickUnion.union(xyTo1D(row, col), xyTo1D(row, col - 1));
    }
    //bottom
    if (checkRangeNoThrow(row + 1, col) &&
        isOpenNoThrow(row + 1, col)) {
      quickUnion.union(xyTo1D(row, col), xyTo1D(row + 1, col));
    }
    //right
    if (checkRangeNoThrow(row, col + 1) &&
        isOpenNoThrow(row, col + 1)) {
      quickUnion.union(xyTo1D(row, col), xyTo1D(row, col + 1));
    }
  }

  /**
   * @return is this site set to OPEN
   */
  public boolean isOpen(int row, int col) {
    checkRange(row, col);
    return sites[row][col];
  }

  private boolean isOpenNoThrow(int row, int col) {
    return checkRangeNoThrow(row, col) && sites[row][col];
  }

  /**
   * is site open and connected to top row.
   * <p>
   * check the site is open(call isOpen()),
   * then check whether it is the child of the virtual top root.
   * <p>
   * use WeightedQuickUnionUF.find() to find the root, is root == top(0) ?
   */
  //todo has bugs..
  public boolean isFull(int row, int col) {
    checkRange(row, col);
//    return isOpen(row, col) && quickUnion.find(xyTo1D(row, col)) == 0;
    return isOpen(row, col) && quickUnion.connected(0, xyTo1D(row, col));
  }

  /**
   * number of open sites
   */
  public int numberOfOpenSites() {
    return openSize;
  }

  /**
   * @return is the system percolate
   */
  public boolean percolates() {
    if(n == 1) {
      return isOpen(1, 1);
    }
    return quickUnion.connected(0, n * n + 1);
  }
}
