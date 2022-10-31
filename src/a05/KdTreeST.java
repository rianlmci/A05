package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.ST;

/**
 *
 * @author Pen
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

        Node() {
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
     * If the point exists, the old value is replaced with
     * the new value.
     *
     * @param p
     * @param val
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
    public Iterable<Point2D> points() {
        return null; // TODO
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

        return null; // TODO
    }

    /**
     * Returns the nearest neighbor to point p. Returns null if the symbol table is
     * empty.
     *
     * @param p Point2D
     * @return the nearest point
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
