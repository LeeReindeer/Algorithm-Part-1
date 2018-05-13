import java.util.ArrayList;

/**
 * @author leer
 * Created at 5/10/18 4:08 PM
 */
public class Board {

  private final int n;
  private final int[][] blocks;
  private int hamming;
  private int manhattan;

  /**
   * construct a board from an n-by-n array of blocks
   *
   * @param blocks where blocks[i][j] = block in row i, column j
   */
  public Board(int[][] blocks) {
    n = blocks.length;
    this.blocks = copy(blocks);
    hamming();
    manhattan();
  }

  public int dimension() {
    return n;
  }

  private int[][] gen_goals() {
    int[][] goals = new int[n][n];
    int k = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        goals[i][j] = ++k;
      }
    }
    goals[n - 1][n - 1] = 0;

    return goals;
  }

  /**
   * @return number of blocks out of place
   */
  public int hamming() {
    if (hamming != 0) {
      return hamming;
    }
    int[][] goals = gen_goals();
//    int count = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (blocks[i][j] != goals[i][j] && blocks[i][j] != 0) {
          hamming++;
        }
      }
    }
    return hamming;
  }

  /**
   * @return sum of Manhattan distances between blocks and goal
   */
  public int manhattan() {
    if (manhattan != 0) {
      return manhattan;
    }
//    int count = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (blocks[i][j] != 0) {
          int index = (blocks[i][j] - 1);
          Point point = oneD2xy(index);//the goal position
          manhattan += (Math.abs(point.x - i) + Math.abs(point.y - j));
        }
      }
    }

    return manhattan;
  }

  /**
   * is this board the goal board?
   *
   * @return
   */
  public boolean isGoal() {
    return hamming() == 0;
  }

  /**
   * a board that is obtained by exchanging any pair of blocks
   * @see #swap(int[][], Point, Point)
   * @return twin board
   */
  public Board twin() {

    int[][] nBlocks = copy(blocks);
    Point blank = blankSquare();
    Point p = new Point(0, 0);
    Point q = new Point(0, 1);

    if (p.equals(blank)) {
      p.x = 1;
      p.y = 0;
      q.x = 1;
    } else if (q.equals(blank)) {
      p.x = 1;
      q.x =  1;
    }
    swap(nBlocks, p, q);
    return new Board(nBlocks);
  }

  public boolean equals(Object y) {
    if (y instanceof Board) {
      Board that = (Board) y;
      //check size
      if (that.blocks.length != blocks.length) {
        return false;
      }
      //check every element
      for (int i = 0; i < blocks.length; i++) {
        for (int j = 0; j < blocks.length; j++) {
          if (blocks[i][j] != that.blocks[i][j]) {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }


  /**
   * @return all neighboring boards
   */
  public Iterable<Board> neighbors() {
    ArrayList<Board> boards = new ArrayList<>();
    Board board = null;
    int[][] nBlocks = copy(blocks);
    Point blank = blankSquare();
//    System.out.println("blank: " + Arrays.toString(blank));

    //3 situations (4 corner blocks, center, edge), has 2, 4, 3 neighbors, respectively.
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        //only one of i, j = 0
        if ((i == 0 || j == 0) && !(i == 0 && j == 0)) {
          //swap and check
          if (!swap(nBlocks,
              new Point(blank.x, blank.y),
              new Point(blank.x + i, blank.y + j))) {
            continue;
          }
          board = new Board(nBlocks);
          boards.add(board);
        }
        nBlocks = copy(blocks);//reset at most 4 times
      }
    }
    return boards;
  }

  /**
   * string representation of this board (in the output format specified below)
   *
   * @return string of board
   */
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append(dimension() + "\n");
    for (int i = 0; i < blocks.length; i++) {
      for (int j = 0; j < blocks.length; j++) {
        s.append(String.format("%2d ", blocks[i][j]));
      }
      s.append("\n");
    }
    return s.toString();
  }


  /***************************************************************************
   * Helper functions
   ***************************************************************************/

  private static class Point {
    int x;
    int y;

    Point() {
    }

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Point)) return false;
      Point point = (Point) o;
      return x == point.x &&
          y == point.y;
    }
  }

  private int[][] copy(int[][] src) {
    int[][] dest = new int[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        dest[i][j] = src[i][j];
      }
    }
    return dest;
  }

  /**
   * swap two <em>valid</em> element in 2d array
   *
   * @param src 2d array
   * @param a   the (x, y) of the block
   * @param b   same as above
   */
  private boolean swap(int[][] src, Point a, Point b) {
    if (a.x < 0 || a.y < 0 || a.x >= n || a.y >= n
        || b.x < 0 || b.y < 0 || b.x >= n || b.y >= n) {
      return false;
    }
    int t = src[a.x][a.y];
    src[a.x][a.y] = src[b.x][b.y];
    src[b.x][b.y] = t;
    return true;
  }

  /**
   * 1d array to 2d
   *
   * @param one 1d index
   * @return xy  2d index (xy[0]->x, xy[1]->y)
   */
  private Point oneD2xy(int one) {
    int[] xy = new int[2];
    Point point = new Point();
    point.x = one / (n);
    point.y = one % (n);
    return point;
  }

  /**
   * find blank square position
   */
  private Point blankSquare() {
//    int[] xy = new int[2];
    Point point = new Point();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (blocks[i][j] == 0) {
          point.x = i;
          point.y = j;
        }
      }
    }
    return point;
  }

  /***************************************************************************
   * Helper functions
   ***************************************************************************/


  public static void main(String[] args) {
    int a[][] = {{8, 1, 3}, {4, 2, 0}, {7, 6, 5}};
    int b[][] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};

    Board board1 = new Board(a);
    Board board2 = new Board(b);
    System.out.println(board1);
    System.out.println("Hamming: " + board1.hamming());
    System.out.println("Manhattan: " + board1.manhattan());
    Iterable<Board> neighbors = board1.neighbors();
    System.out.println("neighbors");

    for (Board bb : neighbors) {
      System.out.println(bb);
    }

    System.out.println("board1's twin:");
    System.out.println(board1.twin());
    System.out.println(board1.twin());


    System.out.println("*****************************");

    System.out.println(board2);
    System.out.println("board2 is goal? " + board2.isGoal());

    System.out.println(board1.equals(board2));
  }// unit tests (not graded)
}
