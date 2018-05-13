/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

  private final int x;     // x-coordinate of this point
  private final int y;     // y-coordinate of this point

  /**
   * Initializes a new point.
   *
   * @param x the <em>x</em>-coordinate of the point
   * @param y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point
   * to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point.
   * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
   * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
   * +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical;
   * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
   *
   * @param that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    /* YOUR CODE HERE */
    if (this.y == that.y && this.x == that.x) { //degenerate line
      return Double.NEGATIVE_INFINITY;
    }

    if (this.x == that.x) { //vertical line
      return Double.POSITIVE_INFINITY;
    }

    if (this.y == that.y) { //horizon line
      return +0.0;
    }
    return (double) (that.y - this.y) / (double) (that.x - this.x);
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate.
   * Formally, the invoking point (x0, y0) is less than the argument point
   * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
   *
   * @param that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument
   * point (x0 = x1 and y0 = y1);
   * a negative integer if this point is less than the argument
   * point; and a positive integer if this point is greater than the
   * argument point
   */
  public int compareTo(Point that) {
    /* YOUR CODE HERE */
    if (this.x == that.x && this.y == that.y) {
      return 0;
    }
    if (this.y > that.y) {
      return 1;
    } else if (this.y == that.y) {
      return Integer.compare(this.x, that.x);
    } else {
      return -1;
    }
  }

  /**
   * Compares two points by the slope they make with this point.
   * The slope is defined as in the slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    /* YOUR CODE HERE */
    return new Comparator<Point>() {
      @Override
      public int compare(Point q, Point r) {
//        double slope1 = slopeTo(q);
//        double slope2 = slopeTo(r);
//        if (slope1 == slope2) return 0;
//        if (slope1 < slope2) return -1;
//        return 1;
        return Double.compare(slopeTo(q), slopeTo(r));
      }
    };
  }


  /**
   * Returns a string representation of this point.
   * This method is provide for debugging;
   * your program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }

  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    /* YOUR CODE HERE */
    Point point1 = new Point(2, 2);
    Point point2 = new Point(4, 2);

    double k1 = point1.slopeTo(point2);
    System.out.println("is infinity:" + Double.isInfinite(k1));
    System.out.println(k1);
    System.out.println(Double.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));

    System.out.println(point1.compareTo(point2));
  }
}
