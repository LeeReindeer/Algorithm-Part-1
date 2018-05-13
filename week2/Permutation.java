import edu.princeton.cs.algs4.StdIn;

/**
 * @author leer
 * Created at 4/20/18 3:24 PM
 */
public class Permutation {
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);

    int i = n;
    RandomizedQueue<String> queue = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      queue.enqueue(StdIn.readString());
    }

    while (i > 0) {
      System.out.println(queue.dequeue());
      i--;
    }
  }
}
