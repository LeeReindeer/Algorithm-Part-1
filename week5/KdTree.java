import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.Stack;

/**
 * @author leer
 * Created at 5/17/18 12:38 PM
 * Search and insert:
 * The algorithms for search and insert are similar to those for BSTs,
 * but at the root we use the x-coordinate
 * (if the point to be inserted has a smaller x-coordinate than the point at the root,
 * go left; otherwise go right);
 * then at the next level, we use the y-coordinate
 * (if the point to be inserted has a smaller y-coordinate than the point in the node,
 * go left; otherwise go right); then at the next level the x-coordinate, and so forth.
 */
public class KdTree {

  private Node root;
  private int size;

  /**
   * Odd -> vertical line(compare x-coordinate),
   * Even -> horizontal line(compare y-coordinate)
   */
  private static class Node {
    private Point2D p;      // the point
    private RectHV rect;    // the axis-aligned rectangle corresponding to this node
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree
    private boolean isNodeLineVertical; //the spilt line through the node is vertical or horizontal

    Node(Point2D p, RectHV rect, boolean isNodeLineVertical) {
      this.p = p;
      this.rect = rect;
      this.isNodeLineVertical = isNodeLineVertical;
    }
  }

  public KdTree() {
  }

  public boolean isEmpty() {
    return root == null;
  }

  public int size() {
    return size;
  }

  /**
   * add the point to the set (if it is not already in the set)
   *
   * @param p
   */
  public void insert(Point2D p) {
    checkValid(p);
    root = insert(root, p, 1, 0, 0, 1, 1);
    size++;
  }

  private Node insert(Node node, Point2D point2D, int level,
                      double xmin, double ymin, double xmax, double ymax) {
    if (node == null) {
      return new Node(point2D, new RectHV(xmin, ymin, xmax, ymax), level % 2 != 0);
    }
    boolean isNodeLineVertical = level % 2 != 0;
//    System.out.println("isVertical" + isNodeLineVertical);
    int cmp = (isNodeLineVertical) ?
        Double.compare(point2D.x(), node.p.x()) :
        Double.compare(point2D.y(), node.p.y());
    if (cmp < 0) {
//      System.out.println("lb");
      if (isNodeLineVertical) {
        node.lb = insert(node.lb, point2D, level + 1, node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
      } else {
        node.lb = insert(node.lb, point2D, level + 1, node.rect.xmin(), node.rect.ymin(), node.rect.xmax(), node.p.y());
      }
    } else { //(cmp >= 0)
//      System.out.println("rt");
      if (isNodeLineVertical) {
        node.rt = insert(node.rt, point2D, level + 1, node.p.x(), node.rect.ymin(), node.rect.xmax(), node.rect.ymax());
      } else {
        node.rt = insert(node.rt, point2D, level + 1, node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
      }
    }
    return node;
  }

  /**
   * does the set contain point p?
   */
  public boolean contains(Point2D p) {
    checkValid(p);
//    Node node = root;
//    while (node != null) {
//      int cmp = node == root ?
//          Double.compare(node.p.x(), p.x()) :
//          Double.compare(node.p.y(), p.y());
//      if (cmp < 0) {
//        node = node.lb;
//      } else if (cmp >= 0) {
//        if (node.p.equals(p)) {
//          return true;
//        }
//        node = node.rt;
//      }
//    }
    return contains(root, p);
  }

  private boolean contains(Node node, Point2D point2D) {
    if (node == null) {
      return false;
    }
    int cmp = (node.isNodeLineVertical) ?
        Double.compare(point2D.x(), node.p.x()) :
        Double.compare(point2D.y(), node.p.y());
    if (cmp < 0) {
      return contains(node.lb, point2D);
    } else { //(cmp >= 0)
      if (node.p.equals(point2D)) {
        return true;
      }
      return contains(node.rt, point2D);
    }
  }

  /**
   * A 2d-tree divides the unit square in a simple way:
   * all the points to the left of the root go in the left subtree;
   * all those to the right go in the right subtree; and so forth, recursively.
   * Your draw() method should draw all of the points to standard draw
   * in black and the subdivisions in red (for vertical splits) and blue
   * (for horizontal splits).
   * This method need not be efficient—it is primarily for debugging.
   * <p>
   * Use StdDraw.setPenColor(StdDraw.BLACK) and StdDraw.setPenRadius(0.01)
   * before before drawing the points;
   * use StdDraw.setPenColor(StdDraw.RED) or StdDraw.setPenColor(StdDraw.BLUE)
   * and StdDraw.setPenRadius() before drawing the splitting lines.
   */
  public void draw() {
    draw(root);
  }

  private void draw(Node node) {
    if (node == null) {
      return;
    }
//    boolean isNodeLineVertical = level % 2 != 0;
    draw(node.lb);
    drawPoint(node);
    drawLine(node, node.isNodeLineVertical ? StdDraw.RED : StdDraw.BLUE);
    draw(node.rt);
  }

  private void drawLine(Node node, Color color) {
    StdDraw.setPenColor(color);
    StdDraw.setPenRadius(0.0025);

//    System.out.println(node.rect);
    if (color == StdDraw.RED) {
      StdDraw.line(node.p.x(), node.rect.ymax(), node.p.x(), node.rect.ymin());
    } else {
      StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
    }
  }

  private void drawPoint(Node node) {
    StdDraw.setPenColor(StdDraw.BLACK);
    StdDraw.setPenRadius(0.01);

    StdDraw.point(node.p.x(), node.p.y());
  }

  /**
   * if the query rectangle does not intersect the rectangle corresponding to a node,
   * there is no need to explore that node (or its subtrees).
   * A subtree is searched only if it might contain a point contained in the query rectangle.
   * <p>
   * What should range() return if there are no points in the range?
   * It should return an Iterable<Point2D> object with zero points.
   * <p>
   * Optimization:
   * Instead of checking whether the query rectangle intersects the rectangle corresponding to a node,
   * it suffices to check only whether the query rectangle intersects the splitting line segment:
   * if it does, then recursively search both subtrees; otherwise, recursively search the one subtree
   * where points intersecting the query rectangle could be.
   *
   * @param rect the query rectangle
   * @return
   */
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException();
    }
    Stack<Point2D> points = new Stack<>();
    push(root, rect, points);
    return points;
  }          // all points that are inside the rectangle (or on the boundary)

  private void push(Node node, RectHV rectHV, Stack<Point2D> point2DStack) {
    if (node != null) {
      if (node.rect.intersects(rectHV)) {
        if (rectHV.contains(node.p)) {
          point2DStack.push(node.p);
        }
        if (node.lb != null && node.lb.rect.intersects(rectHV)) {
          push(node.lb, rectHV, point2DStack);
        }
        if (node.rt != null && node.rt.rect.intersects(rectHV)) {
          push(node.rt, rectHV, point2DStack);
        }
      }
    }
  }

  /**
   * a nearest neighbor in the set to point p; null if the set is empty
   */
  public Point2D nearest(Point2D p) {
    checkValid(p);
    return nearest(root, p, null);
  }

  private Point2D nearest(Node node, Point2D point, Point2D nearPoint) {
    Point2D nearestPoint = nearPoint;
    if (node != null) {
      if (nearestPoint == null ||
          nearestPoint.distanceSquaredTo(point) > node.p.distanceSquaredTo(point)) {
        nearestPoint = node.p;
      }
      if (node.isNodeLineVertical) {
        if (point.x() < node.p.x()) { //at left of the node, search left tree fist
          nearestPoint = nearest(node.lb, point, nearestPoint);
          nearestPoint = nearest(node.rt, point, nearestPoint);
        } else {
          nearestPoint = nearest(node.rt, point, nearestPoint);
          nearestPoint = nearest(node.lb, point, nearestPoint);
        }
      } else {
        if (point.y() < node.p.y()) {
          nearestPoint = nearest(node.lb, point, nearestPoint);
          nearestPoint = nearest(node.rt, point, nearestPoint);
        } else {
          nearestPoint = nearest(node.rt, point, nearestPoint);
          nearestPoint = nearest(node.lb, point, nearestPoint);
        }
      }
    }
    return nearestPoint;
  }

  private void checkValid(Point2D point2D) {
    if (point2D == null) {
      throw new IllegalArgumentException();
    }
  }

  /**
   * unit testing of the methods (optional)
   */
  private static void testInput() {
    KdTree kdTree = new KdTree();
    kdTree.insert(new Point2D(.7, .2));
    kdTree.insert(new Point2D(.5, .4));
    kdTree.insert(new Point2D(.2, .3));
    kdTree.insert(new Point2D(.4, .7));
    kdTree.insert(new Point2D(.9, .6));

    System.out.println("size: " + kdTree.size());
    System.out.println("isEmpty: " + kdTree.isEmpty());

    System.out.println("contain");
    System.out.println(kdTree.contains(new Point2D(.7, .2)));
    System.out.println(kdTree.contains(new Point2D(.5, .4)));
    System.out.println(kdTree.contains(new Point2D(.2, .3)));
    System.out.println(kdTree.contains(new Point2D(.1, .4)));

    kdTree.draw();

//    RectHV rectHV1 = new RectHV(.1, .1, .5, .5);
//    RectHV rectHV2 = new RectHV(.2, .2, .4, .4);
//    System.out.println(rectHV1.intersects(rectHV2));
//    System.out.println(rectHV2.intersects(rectHV1));
  }

  private static void testDraw(String[] args) {
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();
    while (!in.isEmpty()) {
      double x = in.readDouble();
      double y = in.readDouble();
      Point2D p = new Point2D(x, y);
      kdtree.insert(p);
    }
    kdtree.draw();
  }

  public static void main(String[] args) {
    testInput();
    testDraw(args);
  }
}
