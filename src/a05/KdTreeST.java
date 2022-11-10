package a05;

import java.util.ArrayList;
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
	    
//		private Point2D p; // the point
//		private Value value; // the symbol table maps the point to this value
//		private RectHV rect; // the axis-aligned rectangle corresponding to this node (Aka Bounding Box)
//		private Node left; // the left/bottom subtree
//		private Node right; // the right/top subtree
//		private boolean lineDirection; // line direction of this node (| or -)
//
//		public Node(Point2D p, Value val) {
//			this.p = p;
//			this.value = val;
//			if (isEmpty()) {
//				this.rect = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 
//						Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
//				this.lineDirection = VERTICAL;
//				root = this;
//				size += 1;
//				return;
//			}
//			this.rect = createBoundsAndLineDirections(p);
//			size += 1;
//		}
	}

//	/**
//	 * Helper method for creating a node that sets the bound rectangle and also sets
//	 * the line direction of each node.
//	 * 
//	 * @param p point to insert into the tree
//	 * @return bounds for the rectangle of this node.
//	 */
//	private RectHV createBoundsAndLineDirections(Point2D p) {
//		Double currentXMin = root.rect.xmin();
//		Double currentYMin = root.rect.ymin();
//		Double currentXMax = root.rect.xmax();
//		Double currentYMax = root.rect.ymax();
//		Node current = root;
//
//		// Navigates to the bottom of tree and updates bounds.
//		while (current.left != null || current.right != null) {
//			if (current.p == p) {
//				return current.rect; // don't update bounds for duplicates
//			}
//			
//			// Compares x or y based on the parents bound-direction
//			if (current.lineDirection == VERTICAL) { // if we are comparing x, we update the x bounds
//				if (current.p.x() > p.x()) { // if the node we insert is less than the parent...
//					currentXMax = current.p.x(); // Update max bound (X)
//					if (current.left != null) {// we go to the left of the node,if it exists.
//						current.left.lineDirection = !current.lineDirection; // set line direction of nodes as we go down
//						current = current.left;
//					} 
//					else {
//						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
//					}
//				} 
//				else {// if the node we insert is greater or equal to parent...
//					currentXMin = current.p.x(); // Update min bound (X)
//					if (current.right != null) {
//						current.right.lineDirection = !current.lineDirection;
//						current = current.right;
//					} 
//					else {
//						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
//					}
//				}
//			} else {// line direction is horizontal, and if we are comparing y, we update the y
//					// bounds
//				if (current.p.y() > p.y()) {// if the node we insert is less than the parent...
//					currentYMax = current.p.y(); // Update max bound (Y)
//					if (current.left != null) {
//						current.left.lineDirection = !current.lineDirection;
//						current = current.left;
//					} 
//					else {
//						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
//					}
//				} 
//				else {// if the node we insert is greater or equal to parent...
//					currentYMin = current.p.y(); // Update min bound (Y)
//					if (current.right != null) {
//						current.right.lineDirection = !current.lineDirection;
//						current = current.right;
//					} 
//					else {
//						return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
//					}
//				}
//			}
//		}
//		// Performs one last update at end point if the bottom of the tree is reached.
//		if (current.lineDirection == VERTICAL) {
//			if (current.p.x() > p.x()) {
//				currentXMax = current.p.x(); // Update max bound (X) one final time...
//			} 
//			else {
//				currentXMin = current.p.x(); // Update min bound (X) one final time...
//			}
//		}
//		else {// line direction is horizontal, and if we are comparing y, we update the y
//				// bounds
//			if (current.p.y() > p.y()) {// if the node we insert is less than the parent
//				currentYMax = current.p.y(); // Update max bound (Y) one final time...
//			} 
//			else {// greater or equal to the parent
//				currentYMin = current.p.y(); // Update min bound (Y) one final time...
//			}
//		}
//		return new RectHV(currentXMin, currentYMin, currentXMax, currentYMax);
//	}

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
	 * 
	 * @param parentNode
	 * @param newPoint
	 * @param val
	 * @return
	 */
//	private Node put(Node parentNode, Point2D newPoint, Value val) {
//		if (parentNode == null) {
//			return new Node(newPoint, val);
//		}
//
//		if (parentNode.p.equals(newPoint)) { 
//			parentNode.value = val; // updates duplicate with new value
//			return parentNode;
//		}
//
//		// Quick Compare guide:
//		// 0 if parent == new child point
//		// less than 0 if parent is less than new child point
//		// greater than 0 if parent is greater than new child point
//		int compareX = Double.compare(parentNode.p.x(), newPoint.x());
//		int compareY = Double.compare(parentNode.p.y(), newPoint.y());
//
//		if (parentNode.lineDirection == VERTICAL) { // Comparing Xs
//			navigateTreePut(parentNode, newPoint, val, compareX);
//		}
//		else { 
//			navigateTreePut(parentNode, newPoint, val, compareY);
//		}
//		return parentNode;
//	}
	private Node put(Node parentNode, Point2D newPoint, Value val, boolean lineDirection, RectHV bounds) {
	    if (parentNode == null) {
	        return new Node(newPoint, val, lineDirection, bounds);
	    }

	    if (parentNode.p.compareTo(newPoint) == 0) { // if duplicate points
	        parentNode.value = val; // updates duplicate with new value
	        return parentNode;
	    }
	    else {
	        int comparison = (lineDirection == VERTICAL) 
	                ? Double.compare(newPoint.x(), parentNode.p.x())
	                : Double.compare(newPoint.y(), parentNode.p.y());
	        
	        //update bounds
	        if (lineDirection == VERTICAL) { //compare x-values
	            if (comparison >= 0) { //new point goes right
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
	 * Helper method for put that helps us navigate down the tree to the final
	 * destination of the new node.
	 * 
	 * @param parentNode  node we are traversing left or right from
	 * @param newPoint    point of the new node
	 * @param val         new node's associated value
	 * @param compareXorY result of Double.compare from either an X or a Y coord,
	 *                    used to navigate the tree
	 */
//	private void navigateTreePut(Node parentNode, Point2D newPoint, Value val, int compareXorY) {
//		if (compareXorY <= 0) {
//			parentNode.right = put(parentNode.right, newPoint, val);
//		}
//		else if (compareXorY > 0) {
//			parentNode.left = put(parentNode.left, newPoint, val);
//		}
//	}

	/**
	 * Returns the value associated with the point p.
	 *
	 * @param p
	 * @return
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
	 * 
	 * @param node
	 * @param r
	 * @param q
	 * @return
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
	 * @param x
	 * @param p
	 * @return
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
	 * = = = Test Client = = =
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
