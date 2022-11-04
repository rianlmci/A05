package a05;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

/**
 *
 * @author Rianna McIntyre + Penny Chanthavong
 *
 * @param <Value>
 */
public class KdTreeST<Value> {
	private final boolean VERTICAL = true; // | in a tree
	private final boolean HORIZONTAL = false; // - in a tree
	private Node root; // root of the tree
	int size = 0; // size of the tree

	private class Node {
		private Point2D p; // the point
		private Value value; // the symbol table maps the point to this value
		private RectHV rect; // the axis-aligned rectangle corresponding to this node (Aka Bounding Box)
		private Node left; // the left/bottom subtree
		private Node right; // the right/top subtree
		private boolean lineDirection; // line direction of this node (| or -)

		public Node(Point2D p, Value val) {
			this.p = p;
			this.value = val;
			if (isEmpty()) {
				this.rect = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
						Double.POSITIVE_INFINITY);
				this.lineDirection = VERTICAL;
				root = this;
				size += 1;
				return;
			}

			this.rect = createBoundsAndLineDirections(p);
			size += 1;
		}
	}

	/**
	 * Helper method for creating a node that sets the bound rectangle and also sets
	 * the line direction of each node.
	 * 
	 * @param p point to insert into the tree
	 * @return bounds for the rectangle of this node.
	 */
	private RectHV createBoundsAndLineDirections(Point2D p) {
		Double currentXMin = root.rect.xmin();
		Double currentYMin = root.rect.ymin();
		Double currentXMax = root.rect.xmax();
		Double currentYMax = root.rect.ymax();
		Node current = root;

		// NAVIGATE TO THE BOTTOM OF THE TREE, UPDATE BOUNDS...
		while (current.left != null || current.right != null) {
			if (current.p == p) {
				return current.rect; // don't update bounds for duplicates
			}
			// We look at comparing x or y based on the parent’s bound-direction
			// (horizontal or vertical)
			if (current.lineDirection == VERTICAL) { // if we are comparing x, we update the x bounds
				if (current.p.x() > p.x()) { // if the node we insert is less than the parent...
					currentXMax = current.p.x(); // Update max bound (X)
					if (current.left != null) {// we go to the left of the node,if it exists.
						current.left.lineDirection = !current.lineDirection; // set line direction of nodes as we go
																				// down
						current = current.left;
					} else {// reached leaf, return.
						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
					}
				} else {// if the node we insert is greater or equal to parent...
					currentXMin = current.p.x(); // Update min bound (X)
					if (current.right != null) {
						current.right.lineDirection = !current.lineDirection;
						current = current.right;
					} else {
						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
					}
				}
			} else {// line direction is horizontal, and if we are comparing y, we update the y
					// bounds
				if (current.p.y() > p.y()) {// if the node we insert is less than the parent...
					currentYMax = current.p.y(); // Update max bound (Y)
					if (current.left != null) {
						current.left.lineDirection = !current.lineDirection;
						current = current.left;
					} else {
						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
					}
				} else {// if the node we insert is greater or equal to parent...
					currentYMin = current.p.y(); // Update min bound (Y)
					if (current.right != null) {
						current.right.lineDirection = !current.lineDirection;
						current = current.right;
					} else {
						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
					}
				}
			}
		}
		// DO ONE LAST UPDATE AT END POINT IF WE REACH VERY BOTTOM OF TREE...
		if (current.lineDirection == VERTICAL) {
			if (current.p.x() > p.x()) {
				currentXMax = current.p.x(); // Update max bound (X) one final time...
			} else {
				currentXMin = current.p.x(); // Update min bound (X) one final time...
			}
		}

		else {// line direction is horizontal, and if we are comparing y, we update the y
				// bounds
			if (current.p.y() > p.y()) {// if the node we insert is less than the parent
				currentYMax = current.p.y(); // Update max bound (Y) one final time...
			} else {// greater or equal to the parent
				currentYMin = current.p.y(); // Update min bound (Y) one final time...
			}
		}
		return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
	}

	/**
	 * Constructs an empty symbol table of points.
	 */
	public KdTreeST() {
	}

	/**
	 * Determines whether the symbol table is empty.
	 *
	 * @return
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * Returns the number of points in this symbol table.
	 *
	 * @return
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Associates the value with the point and puts it in the table.
	 * 
	 * @param p
	 * @param val
	 */
	/*
	 * hints: - splitting line can be thought of as left or not left therefore if
	 * the value is less than, it goes to the left and everything else goes right -
	 * If the point already exists, the old value is replaced with the new value. -
	 * best implemented using private helper methods, see BST.java, recommended: use
	 * orientation (vertical or horizontal) as an argument to the helper method
	 */
	public void put(Point2D p, Value val) {
		if (p == null || val == null)
			throw new NullPointerException("Input cannot be null.");
		if (val == null) { // break statement
			return;
		}
		root = put(root, p, val); // starts recursion @ root
	}

	/**
	 * Helper method for put.
	 * 
	 * @param parentNode
	 * @param newPoint
	 * @param val
	 * @return
	 */
	private Node put(Node parentNode, Point2D newPoint, Value val) {
		if (parentNode == null) {
			return new Node(newPoint, val);
		}

		if (parentNode.p.compareTo(newPoint) == 0) { // if duplicate points
			parentNode.value = val; // updates duplicate with new value
			return parentNode;
		}

		// Quick Compare guide:
		// 0 if parent == new child point
		// less than 0 if parent is less than new child point
		// greater than 0 if parent is greater than new child point
		int compareX = Double.compare(parentNode.p.x(), newPoint.x());
		int compareY = Double.compare(parentNode.p.y(), newPoint.y());

		// Comparing Xs
		if (parentNode.lineDirection == VERTICAL) {
			navigateTree(parentNode, newPoint, val, compareX);
		}
		// Comparing Ys
		else if (parentNode.lineDirection == HORIZONTAL) {
			navigateTree(parentNode, newPoint, val, compareY);
		}
		return parentNode;
	}

	/**
	 * Helper method for put that helps us navigate down the tree to the final
	 * destination of the new node.
	 * 
	 * @param parentNode  node we are traversing left or right from
	 * @param newPoint    point of the new node
	 * @param val         new node's associated value
	 * @param compareXorY result of Double.compare from either an X or a Y coord,
	 *                    used to navigate the tree
	 */
	private void navigateTree(Node parentNode, Point2D newPoint, Value val, int compareXorY) {
		if (compareXorY <= 0) {
			parentNode.right = put(parentNode.right, newPoint, val);
		}

		else if (compareXorY > 0) {
			parentNode.left = put(parentNode.left, newPoint, val);
		}

	}

	/**
	 * Returns the value associated with the point p.
	 *
	 * @param p
	 * @return
	 */
	/*
	 * hints: - splitting line can be thought of as left or not left therefore if
	 * the value is less then it goes to the left everything else goes right - best
	 * implemented using private helper methods, see BST.java, recommended: use
	 * orientation (vertical or horizontal) as an argument to the helper method
	 */
	public Value get(Point2D p) {
		if (p == null)
			throw new NullPointerException("Point cannot be null.");
		return get(p, root); // start recursion @ root
	}

	/**
	 * Helper method for get!
	 * 
	 * @param p           is the desired point we are looking for
	 * @param currentNode is the node we are looking inside of
	 * @return value associated with the given key rooted in tree at node
	 *         <code>currentNode</code>.
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
		int compareX = Double.compare(currentNode.p.x(), p.x());
		int compareY = Double.compare(currentNode.p.y(), p.y());

		// Comparing Xs
		if (currentNode.lineDirection == VERTICAL) {
			if (compareX <= 0) {
				return get(p, currentNode.right);
			}

			else if (compareX > 0) {
				return get(p, currentNode.left);
			}

		}
		// Comparing Ys
		else if (currentNode.lineDirection == HORIZONTAL) {
			if (compareY <= 0) {
				return get(p, currentNode.right);
			}

			else if (compareY > 0) {
				return get(p, currentNode.left);
			}
		}
		return null;
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

		return get(p) != null;
	}

	/**
	 * Returns all points in the symbol table.
	 *
	 * @return keys in the symbol table
	 */
	/*
	 * hints: - returns iterable with zero points if there are no points in the data
	 * structure
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
	 * hints: - do not search a subtree whose corresponding rectangle doesn't
	 * intersect the query rectangle - points on the boundary of a rectangle are
	 * considered - two rectangles intersect if they have just one point in common -
	 * returns iterable with zero points if there are no points in the data
	 * structure
	 */
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null)
			throw new NullPointerException("Input cannot be null.");

		Queue<Point2D> q = new Queue<Point2D>();
		return intersectingPoints(root, rect, q);
	}

	// TODO:
	// traverse the kd tree
	// if the node intersects the rectangle add node to queue (using
	// rect.intersects)
	// loop through the queue of nodes to see if the points are in the rectangle
	// (using rect.contains p)
	// start at root, go left, go right
	private Iterable<Point2D> intersectingPoints(Node node, RectHV r, Queue<Point2D> q) {
		Node currentNode = node;
		RectHV rect = r;

		if (currentNode == null) {
			return q;
		}

		if (rect.intersects(currentNode.rect)) { // does the current node's rectangle intersect the given rectangle
			if (rect.contains(currentNode.p)) { // is the current node in the rectangle
				q.enqueue(currentNode.p); // add to queue if current node is in rectangle
			}
			intersectingPoints(currentNode.left, rect, q);
			intersectingPoints(currentNode.right, rect, q);
		} 
//		else {
//			return q;
//		}
		return q;
	}

	/**
	 * Returns the nearest neighbor to point p. Returns null if the symbol table is
	 * empty.
	 *
	 * @param p Point2D
	 * @return the nearest point
	 */
	/*
	 * hints: - when there are 2 subtrees to explore, choose the first subtree that
	 * is on the same side of the splitting line as the query point. - do not search
	 * a subtree if no point in its corresponding rectangle could be closer to the
	 * query point than the best candidate point found so far. - if there are two
	 * (or more) nearest points return any one
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
		KdTreeST<Integer> kd = new KdTreeST<Integer>();

		kd.put(new Point2D(2, 3), 1);
		kd.put(new Point2D(4, 2), 2);
		kd.put(new Point2D(4, 5), 3);
		kd.put(new Point2D(3, 3), 4);
		kd.put(new Point2D(1, 5), 5);
		kd.put(new Point2D(4, 4), 6);
		kd.put(new Point2D(4, 4), 7); // duplicate test

		System.out.println(kd.range(new RectHV(1.3, 2.3, 3.6, 3.6))); // expected (2, 3), (3, 3)

		System.out.printf("Point (2,3) is at value %d\n", kd.get(new Point2D(2, 3)));
		System.out.printf("Point (4,2) is at value %d\n", kd.get(new Point2D(4, 2)));
		System.out.printf("Point (4,5) is at value %d\n", kd.get(new Point2D(4, 5)));
		System.out.printf("Point (3,3) is at value %d\n", kd.get(new Point2D(3, 3)));
		System.out.printf("Point (1,5) is at value %d\n", kd.get(new Point2D(1, 5)));
		System.out.printf("Point (4,4) is at value %d\n", kd.get(new Point2D(4, 4)));
	}
}
