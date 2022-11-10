package a05;

import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

/**
 * Representation of a Kd Tree Symbol Table that contains points.
 * @author Rianna McIntyre + Penny Chanthavong
 * @author Natalie the java tutor, for help with recursion in the put method for bounds & linedirections.
 * @param <Value>
 */
public class KdTreeST<Value> {
	private final boolean VERTICAL = true; // | in a tree
	private final boolean HORIZONTAL = false; // - in a tree
	private Node root; // root of the tree
	private int size = 0; // size of the tree
	private double distToCompare;
	private Point2D championPoint;

	private class Node {
		private Point2D p; // the point
		private Value value; // the symbol table maps the point to this value
		private RectHV rect; // the axis-aligned rectangle corresponding to this node (Aka Bounding Box)
		private Node left; // the left/bottom subtree
		private Node right; // the right/top subtree
		private boolean lineDirection; // line direction of this node (| or -)

		public Node(Point2D p, Value val, boolean lineDirection, RectHV rect) {
			this.p = p;
			this.value = val;
			this.rect = rect;
			this.lineDirection = lineDirection;
			size += 1;
		}
	}

	/**
	 * Constructs an empty symbol table of points.
	 */
	public KdTreeST() {
	}

	/**
	 * @return whether the symbol table is empty.
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * @return the number of points in this symbol table.
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Associates the value with the point and puts it in the table.
	 * 
	 * @param p cartesian point
	 * @param val value associated with the point
	 * @throws NullPointerException if any argument is null
	 */
	public void put(Point2D p, Value val) {
		if (p == null || val == null) {
			throw new NullPointerException("Input cannot be null.");
		}

		double min = Double.NEGATIVE_INFINITY;
		double max = Double.POSITIVE_INFINITY;

		root = put(root, p, val, VERTICAL, new RectHV(min, min, max, max));
	}

	/**
	 * Helper method for put.
	 * Recursive bound and line direction setting with help from Natalie the Java tutor.
	 * @param parentNode the node we inspect to navigate to the next node.
	 * @param newPoint the point to be interested into the ST.
	 * @param val the value associated with the new point.
	 * @param lineDirection the line direction of a node.
	 * @param bounds the bounding rectangle for a node.
	 * @return the next point to navigate through.
	 */
	private Node put(Node parentNode, Point2D newPoint, Value val, boolean lineDirection, RectHV bounds) {
	    if (parentNode == null) {
	        return new Node(newPoint, val, lineDirection, bounds);
	    }

	    if (parentNode.p.compareTo(newPoint) == 0) { // if duplicate points
	        parentNode.value = val; // updates duplicate with new value
	        return parentNode;
	    }
	    else { //we start navigating the tree
	        int comparison = (lineDirection == VERTICAL)
					//ternary comparison help from Natalie/Java tutor.
	                ? Double.compare(newPoint.x(), parentNode.p.x())
	                : Double.compare(newPoint.y(), parentNode.p.y());

	        //update bounds
	        if (lineDirection == VERTICAL) { //compare x-values
	            if (comparison >= 0) { //new point goes right
					//recursive bound setting help from Natalie/Java tutor.
	                bounds = new RectHV(parentNode.p.x(), bounds.ymin(), bounds.xmax(), bounds.ymax());
	                parentNode.right = put(parentNode.right, newPoint, val, !lineDirection, bounds);
	            }
	            else { //new point goes left
	                bounds = new RectHV(bounds.xmin(), bounds.ymin(), parentNode.p.x(), bounds.ymax());
	                parentNode.left = put(parentNode.left, newPoint, val, !lineDirection, bounds);
	            }

	        }
	        else { //compare y-values
	            if (comparison >= 0) { //new point goes right
	                bounds = new RectHV(bounds.xmin(), parentNode.p.y(), bounds.xmax(), bounds.ymax());
	                parentNode.right = put(parentNode.right, newPoint, val, !lineDirection, bounds);
	            }
	            else { //new point goes left
	                bounds = new RectHV(bounds.xmin(), bounds.ymin(), bounds.xmax(), parentNode.p.y());
	                parentNode.left = put(parentNode.left, newPoint, val, !lineDirection, bounds);
	            }
	        }
	    }
	    return parentNode;
	}

	/**
	 * Gets the value associated with the point p, if it exists
	 * @param p the desired point we are getting from the ST
	 * @return value associated with point p, if it exists, otherwise returns null.
	 * @throws NullPointerException if point argument is null.
	 */
	public Value get(Point2D p) {
		if (p == null) {
			throw new NullPointerException("Point cannot be null.");
		}
		return get(p, root);
	}

	/**
	 * Helper method for get!
	 * 
	 * @param p is the desired point we are looking for.
	 * @param currentNode is the node we are looking inside of
	 * @return value associated with the given key rooted in tree at node
	 * <code>currentNode</code>.
	 */
	private Value get(Point2D p, Node currentNode) {
		if (currentNode == null) {
			return null;
		}
		if (currentNode.p.compareTo(p) == 0) {
			return currentNode.value;
		}
		// 0 if current point == desired point
		// less than 0 if current point is less than desired point
		// greater than 0 if current point is greater than desired point
		int compareX = Double.compare(p.x(), currentNode.p.x());
		int compareY = Double.compare(p.y(), currentNode.p.y());

		// Comparing Xs
		if (currentNode.lineDirection == VERTICAL) {
			if (compareX >= 0) { // if point is greater than the current node
				return get(p, currentNode.right);
			}
			else {
				return get(p, currentNode.left);
			}
		}
		else { // Comparing Ys
			if (compareY >= 0) {
				return get(p, currentNode.right);
			}
			else {
				return get(p, currentNode.left);
			}
		}
	}

	/**
	 * Determines whether the symbol table contains point p.
	 *
	 * @param p
	 * @return true if the point exists in the symbol table, otherwise false.
	 */
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new NullPointerException("Point cannot be null.");
		}
		return get(p) != null;
	}

	/**
	 * Returns all points in the symbol table.
	 *
	 * @return keys in the symbol table
	 */
	public Iterable<Point2D> points() {
		if (isEmpty()) {
			return new Queue<Point2D>();
		}

		Queue<Point2D> keys = new Queue<Point2D>();
		Queue<Node> queue = new Queue<Node>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node x = queue.dequeue();
			if (x == null)
				continue;
			keys.enqueue(x.p);
			queue.enqueue(x.left);
			queue.enqueue(x.right);
		}
		return keys;
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

		Queue<Point2D> q = new Queue<Point2D>();
		return range(root, rect, q);
	}

	/**
	 * Helper method for range. Determines the point(s) that intersects and is contained
	 * within a given rectangle.
	 * @param currentNode node we are currently inspecting.
	 * @param rect the bounding rectangle we want to find points inside of.
	 * @param q queue containing all points found so far that are inside the <code>rect</code>.
	 * @return queue containing all points in the KD tree that are inside the <code>rect</code>.
	 */
	private Iterable<Point2D> range(Node currentNode, RectHV rect, Queue<Point2D> q) {
		if (currentNode == null) {
			return q;
		}

		if (rect.intersects(currentNode.rect)) {
			if (rect.contains(currentNode.p)) {
				q.enqueue(currentNode.p);
			}
			range(currentNode.left, rect, q);
			range(currentNode.right, rect, q);
		}
		return q;
	}

	/**
	 * Returns the nearest neighbor of point p. Returns null if the symbol table is
	 * empty.
	 *
	 * Recursive call bug found with help from Natalie/Java Tutor.
	 * @param p
	 * @return
	 */
	public Point2D nearest(Point2D p) {
		if (p == null)
			throw new NullPointerException("Point cannot be null.");

		if (isEmpty()) {
			return null;
		}

		if (this.contains(p)) {
			return p;
		}

		distToCompare = root.p.distanceSquaredTo(p);
		championPoint = root.p;
		return nearest(root, p);
	}

	/**
	 * Helper method used to determine the nearest point to p.
	 *
	 * @param currentNode the node we are inspecting
	 * @param p the point we are comparing to see which of the KDTree's points are nearest to it.
	 * @return the nearest point to <code>p</code>
	 */
	private Point2D nearest(Node currentNode, Point2D p) {
		if (currentNode == null) {
			return championPoint;
		}

		if (championPoint.distanceSquaredTo(p) > currentNode.rect.distanceSquaredTo(p)) {
			if (currentNode.p.distanceSquaredTo(p) < distToCompare) {
				championPoint = currentNode.p;
				distToCompare = currentNode.p.distanceSquaredTo(p);
			}

			if (currentNode.lineDirection == VERTICAL) {
				if (currentNode.p.x() <= p.x()) {
					nearest(currentNode.right, p);
					nearest(currentNode.left, p);
				}
				else {
					nearest(currentNode.left, p);
					nearest(currentNode.right, p);
				}
			}

			if (currentNode.lineDirection == HORIZONTAL) {
				if (currentNode.p.y() <= p.y()) {
					nearest(currentNode.right, p);
					nearest(currentNode.left, p);
				}
				else {
					nearest(currentNode.left, p);
					nearest(currentNode.right, p);
				}
			}
		}
		return championPoint;
	}

	/*
	 * = = = = = = = = = = = = Test Client = = = = = = = = = = = =
	 * Testing methods expanded with help from Natalie/Java Tutor
	 */
	public static void main(String[] args) {
		KdTreeST<String> kdTree = new KdTreeST<>();
        kdTree.put(new Point2D(1.0, 5.0), "first");
        kdTree.put(new Point2D(7.5, -5.0), "second");
        kdTree.put(new Point2D(-1.0, -1.0), "third");
        kdTree.put(new Point2D(4.4, 7.4), "fourth");
        kdTree.put(new Point2D(3.2, 3.3), "fifth");

        for (Point2D p : kdTree.points()) {
            System.out.println(p.toString());
        }

        KdTreeST<Integer> kdTreeSt = new KdTreeST<>();

        System.out.println("Is empty: " + kdTreeSt.isEmpty());
        System.out.println("Size: " + kdTreeSt.size());

		// put test points into the PointST object
        kdTreeSt.put(new Point2D(2, 3), 1);

        System.out.println("Is empty: " + kdTreeSt.isEmpty());
        System.out.println("Size: " + kdTreeSt.size());

        kdTreeSt.put(new Point2D(4, 2), 2);
        kdTreeSt.put(new Point2D(4, 5), 3);
        kdTreeSt.put(new Point2D(3, 3), 4);
        kdTreeSt.put(new Point2D(1, 5), 5);
        kdTreeSt.put(new Point2D(4, 4), 6);
        kdTreeSt.put(new Point2D(4, 4), 7);

        System.out.println("Is empty: " + kdTreeSt.isEmpty());
        System.out.println("Size: " + kdTreeSt.size());

        System.out.println("Find point (4, 5) [should be 3]: " + kdTreeSt.get(new Point2D(4, 5)));
        System.out.println("Find point (4, 4) [should be 6]: " + kdTreeSt.get(new Point2D(4, 4)));
        System.out.println("Find point (1, 5) [should be 5]: " + kdTreeSt.get(new Point2D(1, 5)));
        System.out.println("Find point (2, 3) [should be 1]: " + kdTreeSt.get(new Point2D(2, 3)));
        System.out.println("Contains point (2, 3) [true]: " + kdTreeSt.contains(new Point2D(2, 3)));
        System.out.println("Find point (3, 3) [should be 4]: " + kdTreeSt.get(new Point2D(3, 3)));
        System.out.println("Find point (10, 1) [not found]: " + kdTreeSt.get(new Point2D(10, 1)));
        System.out.println("Contains point (10, 1) [false]: " + kdTreeSt.contains(new Point2D(10, 1)));


		KdTreeST<Integer> kd = new KdTreeST<Integer>();

		kd.put(new Point2D(2.0, 3.0), 1);
		kd.put(new Point2D(4.0, 2.0), 2);
		kd.put(new Point2D(4.0, 5.0), 3);
		kd.put(new Point2D(3.0, 3.0), 4);
		kd.put(new Point2D(1.0, 5.0), 5);
		kd.put(new Point2D(4.0, 4.0), 6);
		kd.put(new Point2D(2.0, -2.0), 7);
		kd.put(new Point2D(-3.0, -1.0), 8);
		//kd.put(new Point2D(4, 4), 7); // duplicate test
        //kd.put(new Point2D(5, 3), 8); // testing range

		System.out.println("Get 2.0, -2.0");
		System.out.println(kd.get(new Point2D(2.0, -2.0)));

        kd.range(new RectHV(1.3, 2.3, 3.6, 3.6));
		System.out.println("range: " + kd.range(new RectHV(1.3, 2.3, 3.6, 3.6))); // expected (2, 3), (3, 3)
		System.out.println("points in traveral level-order: " + kd.points());
		System.out.println();
		System.out.printf("Point (2,3) is at value %d\n", kd.get(new Point2D(2, 3)));
		System.out.printf("Point (4,2) is at value %d\n", kd.get(new Point2D(4, 2)));
		System.out.printf("Point (4,5) is at value %d\n", kd.get(new Point2D(4, 5)));
		System.out.printf("Point (3,3) is at value %d\n", kd.get(new Point2D(3, 3)));
		System.out.printf("Point (1,5) is at value %d\n", kd.get(new Point2D(1, 5)));
		System.out.printf("Point (4,4) is at value %d\n", kd.get(new Point2D(4, 4)));
		System.out.println();

		kd.nearest(new Point2D(4.2, 1.5));
		System.out.printf("The closest point to (4.2,1.5) is at point %s\n", kd.nearest(new Point2D(4.2, 1.5)).toString());
		System.out.printf("The closest point to (4, 3) is at point %s\n", kd.nearest(new Point2D(4, 3)).toString()); // can be any point (4, 4), or (4, 2)
		System.out.printf("The closest point to (0, 0) is at point %s\n", kd.nearest(new Point2D(0, 0)).toString());
		System.out.printf("The closest point to (0.1, 6) is at point %s\n", kd.nearest(new Point2D(0.1, 6)).toString());

		System.out.println("Information about points: ");
        for (Point2D p : kdTreeSt.points()) {
        	System.out.println(p.toString());
            System.out.println();
            System.out.println();
        }
        System.out.println();

        Iterable<Point2D> keys = new ArrayList<Point2D>();
        keys = kd.points();

	}
}
