import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * @author leer
 * Created at 5/17/18 12:36 PM
 * You must use either
 * @see edu.princeton.cs.algs4.SET
 * @see java.util.TreeSet;
 * do not implement your own red–black BST.
 * Performance requirements:
 * Your implementation should support insert() and contains() in time proportional to
 * the logarithm of the number of points in the set in the worst case;
 * it should support nearest() and range() in time proportional to the number of points in the set.
 */
public class PointSET {

  private SET<Point2D> pointSet;

  public PointSET() {
    pointSet = new SET<>();
  }                               // construct an empty set of points

  public boolean isEmpty() {
    return pointSet.isEmpty();
  }                      // is the set empty?

  public int size() {
    return pointSet.size();
  }                     // number of points in the set

  public void insert(Point2D p) {
    pointSet.add(p);
  }              // add the point to the set (if it is not already in the set)

  public boolean contains(Point2D p) {
    return pointSet.contains(p);
  }            // does the set contain point p?

  public void draw() {
    for (Point2D p : pointSet) {
      p.draw();
    }
  }           // draw all points to standard draw

  public Iterable<Point2D> range(RectHV rect) {
    Stack<Point2D> stack = new Stack<>();
    for (Point2D p : pointSet) {
      if (rect.contains(p)) {
        stack.push(p);
      }
    }
    return stack;
  }          // all points that are inside the rectangle (or on the boundary)

  public Point2D nearest(Point2D p) {
    Point2D min = null;
    double minDistance = Double.MAX_VALUE;
    for (Point2D point2D : pointSet) {
      double thisDistance = point2D.distanceSquaredTo(p);
      if (thisDistance < minDistance) {
        minDistance = thisDistance;
        min = point2D;
      }
    }
    return min;
  }         // a nearest neighbor in the set to point p; null if the set is empty

  public static void main(String[] args) {
    PointSET pointSET = new PointSET();
    for (int i = 0; i < 100; i++) {
      double x = StdRandom.uniform(0.0, 1.0);
      double y = StdRandom.uniform(0.0, 1.0);
      StdOut.printf("(%8.6f, %8.6f)\n", x, y);
      pointSET.insert(new Point2D(x, y));
    }
    Point2D testPonit = new Point2D(0.1, 0.9);
//    pointSET.insert(testPonit);
    Point2D near = pointSET.nearest(testPonit);
    System.out.printf("nearest point to (%8.6f, %8.6f) is (%8.6f, %8.6f)\n", testPonit.x(), testPonit.y(),
        near.x(), near.y());
    pointSET.draw();

  }        // unit testing of the methods (optional)
}
