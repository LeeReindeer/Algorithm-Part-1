import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

/**
 * @author leer
 * Created at 5/10/18 4:08 PM
 */
public class Solver {

  private SearchNode finalNode;
  private boolean solvable;
  private int moves;

  private class SearchNode {

    Board board;
    //    Board prevBoard;
    SearchNode prevNode;
    int moves;

    SearchNode(Board board) {
      this.board = board;
    }

    SearchNode(SearchNode prev, Board curr, int moves) {
      this(curr);
      this.moves = moves;
      this.prevNode = prev;
    }
  }

  private class NodeComparator implements Comparator<SearchNode> {

    @Override
    public int compare(SearchNode b1, SearchNode b2) {
      return Integer.compare(b1.board.manhattan() + b1.moves, b2.board.manhattan() + b2.moves);
    }
  }

  /**
   * find a solution to the initial board (using the A* algorithm)
   *
   * @param initial initial board
   */
  public Solver(Board initial) {

    this.solvable = false;

    MinPQ<SearchNode> PQ = new MinPQ<>(10, new NodeComparator());
    MinPQ<SearchNode> twinPQ = new MinPQ<>(10, new NodeComparator());

    PQ.insert(new SearchNode(null, initial, 0));
    Board twin = initial.twin();
    twinPQ.insert(new SearchNode(null, twin, 0));

    SearchNode dequeued = PQ.delMin();
    SearchNode twinDequeued = twinPQ.delMin();

    while (!dequeued.board.isGoal() && !twinDequeued.board.isGoal()) {
      for (Board b1 : dequeued.board.neighbors()) {
        if (dequeued.prevNode == null //initial board
            || !b1.equals(dequeued.prevNode.board)) { //critical optimization.
//          System.out.println("insert: " + b1);
          SearchNode node = new SearchNode(dequeued, b1, numMoves(dequeued) + 1);
          PQ.insert(node);
//          System.out.println("moves: " + currentMove);
        }
      }

      for (Board b2 : twinDequeued.board.neighbors()) {
        if (dequeued.prevNode == null //initial board
            || !b2.equals(twinDequeued.prevNode.board)) { //critical optimization.
          SearchNode node = new SearchNode(dequeued, b2, numMoves(dequeued) + 1);
          twinPQ.insert(node);
        }
      }

      dequeued = PQ.delMin();
      twinDequeued = twinPQ.delMin();
    }
    if (dequeued.board.isGoal()) {
      solvable = true;
      finalNode = dequeued;
    }
  }

  /**
   * Run the A* algorithm on two puzzle instances—one
   * with the initial board and one with the initial board modified
   * by swapping a pair of blocks—in lockstep
   * (alternating back and forth between exploring search nodes in each of the two game trees).
   * Exactly one of the two will lead to the goal board.
   * <p>
   * I count moves here
   *
   * @return is the initial board solvable?
   * @see Board#twin()
   */
  public boolean isSolvable() {
    return solvable;
  }

  /**
   * @return min number of moves to solve initial board; -1 if unsolvable
   */
  public int moves() {
    if (!solvable) {
      return -1;
    }
    if (moves != 0) {
      return moves;
    }
    for (Board b : solution()) {
      moves++;
    }
    return moves -= 1;
  }

  private int numMoves(SearchNode node) {
    int moves = 0;
    SearchNode current = node;

    while (current.prevNode != null) {
      moves++;
      current = current.prevNode;
    }
    return moves;
  }

  /**
   * First, insert the initial search node (the initial board, 0 moves, and a null predecessor search node)
   * into a priority queue.
   * Then, delete from the priority queue the search node with the minimum priority,
   * and insert onto the priority queue all neighboring search nodes
   * (those that can be reached in one move from the dequeued search node).
   * Repeat this procedure until the search node dequeued corresponds to a goal board.
   *
   * @return sequence of boards in a shortest solution; null if unsolvable
   */
  public Iterable<Board> solution() {
    if (!solvable) {
      return null;
    }
    Stack<Board> boards = new Stack<>(); //FILO
    SearchNode t = finalNode;
    while (t != null) {
      boards.push(t.board);
      t = t.prevNode;
    }
    return boards;
  }

  public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable()) {
      StdOut.println("No solution possible");
    } else {
      Iterable<Board> solution = solver.solution();
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solution)
        StdOut.println(board);
    }
  }
}
