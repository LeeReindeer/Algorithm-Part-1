import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author leer
 * Created at 5/3/18 2:18 PM
 * Performance requirement. The order of growth of the running time of your program should be n^4 in the worst case
 * and it should use space proportional to n plus the number of line segments returned.
 */

public class BruteCollinearPoints {

  private Point[] points;

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
    checkArgument(points);
    this.points = copy(points);
  }

  private void checkArgument(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException();
    }

    for (int i = 0; i < points.length; i++) {
      Point point = points[i];
      if (point == null) {
        throw new IllegalArgumentException();
      }
      for (int j = i + 1; j < points.length; j++) {
        if (points[j] == null) {
          throw new IllegalArgumentException();
        }
        if (point.compareTo(points[j]) == 0) {
          throw new IllegalArgumentException();
        }
      }
    }
  }

  public int numberOfSegments() {
    return segments().length;
  }

  public LineSegment[] segments() {
    List<LineSegment> lineSegments = new ArrayList<>();
    Arrays.sort(points);

    int N = points.length;
    for (int i = 0; i < N; i++) {
      for (int j = i + 1; j < N; j++) {
        double k1 = points[i].slopeTo(points[j]);
        for (int k = j + 1; k < N; k++) {
          double k2 = points[j].slopeTo(points[k]);
          if (k1 != k2) {
            continue;
          }
          for (int l = k + 1; l < N; l++) {
            double k3 = points[k].slopeTo(points[l]);
            if (k1 == k2 && k2 == k3) {
                lineSegments.add(new LineSegment(points[i], points[l]));
            }
          }
        }
      }
    }
    return lineSegments.toArray(new LineSegment[lineSegments.size()]);
  }

  private static Point[] copy(Point[] points) {
    Point[] copy = new Point[points.length];
    System.arraycopy(points, 0, copy, 0, points.length);
    return copy;
  }

  public static void main(String[] args) {

    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
//    System.out.println(collinear.segments().length);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();

    System.out.println("number of segments: " + collinear.numberOfSegments());
    System.out.println("finish");
  }

}
