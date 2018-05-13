import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * @author leer
 * Created at 5/3/18 2:56 PM
 * The order of growth of the running time of your program should be n^2 log n
 * in the worst case and it should use space proportional to n plus the number of line segments returned. FastCollinearPoints should work properly even if the input has 5 or more collinear points.
 */
public class FastCollinearPoints {

  private Point[] points;

  public FastCollinearPoints(Point[] points) {
    checkArgument(points);
    this.points = copy(points);
  }     // finds all line segments containing 4 or more points

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
  }        // the number of line segments


  public LineSegment[] segments() {
    List<LineSegment> lineSegments = new ArrayList<>();

    Arrays.sort(points);
    Point[] copy = copy(points);

    for (Point pivot : points) { //n

      Arrays.sort(copy, pivot.slopeOrder()); //n lg n


      //i'm suck with find a new line, so i find some old code in github
      //https://github.com/keyvanakbary/princeton-algorithms/blob/master/week-3-collinear-points/Fast.java#L22
      int j = 0;
      double previous = 0.0;
      LinkedList<Point> collinear = new LinkedList<>();
      for(Point p : copy) { ///n
        if (Double.compare(p.slopeTo(pivot), previous) != 0) { //encounter a new line
          if (collinear.size() >= 3) { //this line is already has 3 points
            collinear.add(pivot); //add this pivot point
            Collections.sort(collinear);//sort by coordinates
            if (pivot == collinear.getFirst()) { //after sort the pivot is at first
              lineSegments.add(new LineSegment(collinear.getFirst(), collinear.getLast()));
            }
          }
          collinear.clear();
        }
        collinear.add(p);

        previous = p.slopeTo(pivot);
        j++;
      }
    }
    return lineSegments.toArray(new LineSegment[lineSegments.size()]); //n
  }       // the line segments

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
    FastCollinearPoints collinear = new FastCollinearPoints(points);
//    System.out.println(collinear.segments().length);
    for (LineSegment segment : collinear.segments()) {
      StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();

    System.out.println("finish");
  }

}
