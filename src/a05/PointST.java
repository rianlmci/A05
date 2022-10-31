package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;

/**
 * Represents a mutable data type that is a symbol table with Point2D. The data
 * structure is implemented using a Red-Black Binary Search Tree.
 *
 * @author Penny C. + Natalie Mueller (help from tutor)
 *
 * @param <Value>
 */
public class PointST<Value> {
    private RedBlackBST<Point2D, Value> rbTree;

    /**
     * Constructs an empty symbol table of points.
     */
    public PointST() {
        rbTree = new RedBlackBST<Point2D, Value>();
    }

    /**
     * Determines whether the symbol table is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return rbTree.isEmpty();
    }

    /**
     * Returns the number of points.
     *
     * @return
     */
    public int size() {
        return rbTree.size();
    }

    /**
     * Associates the value with the point.
     *
     * @param p
     * @param val
     */
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("Input cannot be null.");

        rbTree.put(p, val);
    }

    /**
     * Returns the value associated with point p.
     *
     * @param p
     * @return
     */
    public Value get(Point2D p) {
        if (p == null)
            throw new NullPointerException("Point cannot be null.");

        return rbTree.get(p);
    }

    /**
     * Determines whether the symbol table contains point p.
     *
     * @param p
     * @return true if the point exists in the symbol table, otherwise false.
     */
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException("Point cannot be null.");

        return rbTree.contains(p);
    }

    /**
     * Returns all points in the symbol table.
     *
     * @return keys in the symbol table
     */
    public Iterable<Point2D> points() {
        return rbTree.keys();
    }

    /**
     * Returns all points that are inside the rectangle.
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException("Input cannot be null.");

        Queue<Point2D> points = new Queue<Point2D>();

        for (Point2D p : rbTree.keys()) {
            if (rect.contains(p))
                points.enqueue(p);
        }
        return points;
    }

    /**
     * Returns the nearest neighbor of point p. Returns null if the symbol table is
     * empty.
     *
     * @param p Point2D
     * @return the nearest point
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException("Point cannot be null.");

        Point2D nearestPoint = rbTree.select(0);
        for (Point2D point : rbTree.keys()) {
            if (p.distanceSquaredTo(point) < p.distanceSquaredTo(nearestPoint)) {
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    /*
     * = = = Test Client = = =
     */
    public static void main(String[] args) {
        PointST<Integer> pointST = new PointST<>();

        // put test points into the PointST object
        pointST.put(new Point2D(2, 3), 1);
        pointST.put(new Point2D(4, 2), 2);
        pointST.put(new Point2D(4, 5), 3);
        pointST.put(new Point2D(3, 3), 4);
        pointST.put(new Point2D(1, 5), 5);
        pointST.put(new Point2D(4, 4), 6);

        // print out all the points
        System.out.println("Points entered into the PointST: ");
        for (Point2D point : pointST.points()) {
            System.out.print(point.toString() + " ");
        }
        System.out.println("\n");

        // print out the nearest neighbor to a specific point
        System.out.println("Nearest point to (3, 4): " + pointST.nearest(new Point2D(3, 4)));
        System.out.println();

        // print the points out that exist within a specific range:
        System.out.println("Points within the range [(3.0, 2.0), (4.0, 3.0)]: ");
        RectHV range1 = new RectHV(3.0, 2.0, 4.0, 3.0);
        for (Point2D point : pointST.range(range1)) {
            System.out.print(point.toString() + " ");
        }
    }
}
