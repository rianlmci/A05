package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.ST;

/**
 *
 * @author Rianna McIntyre + Penny Chanthavong
 *
 * @param <Value>
 */
public class KdTreeST<Value> {

    private class Node {
        private Point2D p; 		// the point
        private Value value; 	// the symbol table maps the point to this value
        private RectHV rect;	// the axis-aligned rectangle corresponding to this node
        private Node lb;		// the left/bottom subtree
        private Node rt;		// the right/top subtree

        public Node() {
            // TODO
        }
    }

    /**
     * Constructs an empty symbol table of points.
     */
    public KdTreeST() {
        // TODO
    }

    /**
     * Determines whether the symbol table is empty.
     *
     * @return
     */
    public boolean isEmpty() {
//		return false; // TODO
        return size() == 0;
    }

    /**
     * Returns the number of points in this symbol table.
     *
     * @return
     */
    public int size() {
        return 0; // TODO
    }

    /**
     * Associates the value with the point.
     *
     * @param p
     * @param val
     */
    /*
     * hints:
     * - splitting line can be thought of as left or not left therefore if the value
     * is less then it goes to the left everything else goes right
     * -  If the point already exists, the old value is replaced with
     * the new value.
     * - best implemented using private helper methods, see BST.java,
     * recommended: use orientation (vertical or horizontal) as an argument to 
     * the helper method
     */
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("Input cannot be null.");

        // TODO
    }

    /**
     * Returns the value associated with the point p.
     *
     * @param p
     * @return
     */
    /*
     * hints:
     * - splitting line can be thought of as left or not left therefore if the value
     * is less then it goes to the left everything else goes right
     * - best implemented using private helper methods, see BST.java,
     * recommended: use orientation (vertical or horizontal) as an argument to 
     * the helper method
     */
    public Value get(Point2D p) {
        if (p == null)
            throw new NullPointerException("Point cannot be null.");

        return null; // TODO
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

        return false; // TODO
    }

    /**
     * Returns all points in the symbol table.
     *
     * @return keys in the symbol table
     */
    /*
     * hints:
     * - returns iterable with zero points if there are no points in the data structure
     */
    public Iterable<Point2D> points() {
        return null; // TODO
    }

    /**
     * Returns all points that are inside the rectangle.
     *
     * @param rect
     * @return
     */
    /*
     * hints:
     * - do not search a subtree whose corresponding rectangle doesn't intersect the query rectangle
     * - points on the boundary of a rectangle are considered
     * - two rectangles intersect if they have just one point in common
     * - returns iterable with zero points if there are no points in the data structure
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException("Input cannot be null.");

        return null; // TODO
    }

    /**
     * Returns the nearest neighbor to point p. Returns null if the symbol table is
     * empty.
     *
     * @param p Point2D
     * @return the nearest point
     */
    /*
     * hints:
     * - when there are 2 subtrees to explore, choose the first subtree that is 
     * on the same side of the splitting line as the query point.
     * - do not search a subtree if no point in its corresponding rectangle could
     * be closer to the query point than the best candidate point found so far.
     * - if there are two (or more) nearest points return any one
     */
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException("Point cannot be null.");

        return null; // TODO
    }

    /*
     * = = = Test Client = = =
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
