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

    private static final boolean VERTICAL   = true; // | in a tree
    private static final boolean HORIZONTAL = false; // - in a tree
    private Node root; //root of the tree
    int size = 0; // size of the tree
    int height = 0; //height of the tree
    private class Node {
        private Point2D p; 		// the point
        private Value value; 	// the symbol table maps the point to this value
        private RectHV rect;	// the axis-aligned rectangle corresponding to this node (Aka Bounding Box)
        private Node lb;		// the left/bottom subtree
        private Node rt;		// the right/top subtree
        private boolean lineDirection;

        public Node(Point2D p, Value val)  {
            this.p = p;
            this.value = val;
            if(isEmpty()){
                this.rect = new RectHV(
                        Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                        Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                this.lineDirection = VERTICAL;
                root = this; //what I'm trying to do is assign /this/ node to the KD tree.
                size += 1;
                return;
            }

            this.rect = createBounds(p);
            size += 1;
        }
    }

    /**
     * Helper method for creating a node
     * @param p point to insert into the tree
     * @return bounds for the rectangle of this node.
     */
    private RectHV createBounds(Point2D p) {
        Double currentXMin = root.rect.xmin();
        Double currentYMin = root.rect.ymin();
        Double currentXMax = root.rect.xmax();
        Double currentYMax = root.rect.ymax();
        Node current = root;


        //NAVIGATE TO THE BOTTOM OF THE TREE, UPDATE BOUNDS...
        while (current.lb != null || current.rt != null) {

            //We look at comparing x or y based on the parent’s bound-direction.
            if (current.lineDirection == VERTICAL){
                //if we are comparing x, we update the x bounds
                if(current.p.x() > p.x()){ //if the node we insert is less than the parent
                    currentXMax = current.p.x(); //The bound will be the comparison coordinate (x/y) of the parent.
                    if(current.lb != null) {
                        current = current.lb; //we go to the left of the tree
                    }
                    else {
                        //reached end of tree, return.
                        return new RectHV(currentXMin,currentYMin,currentXMax,currentYMax);
                    }
                }
                else/*greater or equal to the parent*/{
                    currentXMin = current.p.x();
                    if(current.rt != null) {
                        current = current.rt;
                    }
                    else {
                        //reached end of tree, return.
                        return new RectHV(currentXMin,currentYMin,currentXMax,currentYMax);
                    }
                }
            }

            else/*line direction is horizontal*/{
                //if we are comparing y, we update the y bounds
                if(current.p.y() > p.y()){ //if the node we insert is less than the parent
                    currentYMax = current.p.y(); //The bound will be the comparison coordinate (x/y) of the parent.
                    if(current.lb != null) {
                        current = current.lb; //we go to the left of the tree
                    }
                    else {
                        //reached end of tree, return.
                        return new RectHV(currentXMin,currentYMin,currentXMax,currentYMax);
                    }
                }
                else/*greater or equal to the parent*/{
                    currentYMin = current.p.y();
                    if(current.rt != null) {
                        current = current.rt;
                    }
                    else {
                        //reached end of tree, return.
                        return new RectHV(currentXMin,currentYMin,currentXMax,currentYMax);
                    }
                }
            }
        }

        //Do one last compare to end point
        if (current.lineDirection == VERTICAL) {
            //if we are comparing x, we update the x bounds
            if (current.p.x() > p.x()) { //if the node we insert is less than the parent
                currentXMax = current.p.x(); //The bound will be the comparison coordinate (x/y) of the parent.
            }
            else/*greater or equal to the parent*/ {
                currentXMin = current.p.x();
            }
        }

        else/*line direction is horizontal*/{
                //if we are comparing y, we update the y bounds
                if(current.p.y() > p.y()){ //if the node we insert is less than the parent
                    currentYMax = current.p.y(); //The bound will be the comparison coordinate (x/y) of the parent.
                }
                else/*greater or equal to the parent*/{
                    currentYMin = current.p.y();
                }
            }

        return new RectHV(currentXMin,currentYMin,currentXMax,currentYMax);
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
     * Associates the value with the point.
     *
     * @param p
     * @param val
     */
    /*
     * hints:
     * - splitting line can be thought of as left or not left therefore if the value
     * is less than, it goes to the left and everything else goes right
     * -  If the point already exists, the old value is replaced with
     * the new value.
     * - best implemented using private helper methods, see BST.java,
     * recommended: use orientation (vertical or horizontal) as an argument to 
     * the helper method
     */
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("Input cannot be null.");

        Node newNode = new Node(p,val);

        if(isEmpty()){
            root = newNode;
        }

        Node current = root;

        //NAVIGATE TO THE BOTTOM OF THE TREE...
        while (current.lb != null || current.rt != null) /*current has children*/ {
            //We look at comparing x or y based on the parent’s bound-direction.
            if (current.lineDirection == VERTICAL) {
                if (current.p.x() > p.x()) { //if the node we insert is less than the parent
                    current = current.lb; //we go to the left of the tree
                }
                else/*greater or equal to the parent i.e 'not left'*/ {
                    current = current.rt; //we go to the right of the tree
                }
            }
            else/*line direction is horizontal*/ {
                if (current.p.y() > p.y()) { //if the node we insert is less than the parent
                    current = current.lb; //we go to the left of the tree
                }
                else/*greater or equal to the parent i.e 'not left'*/ {
                    current = current.rt; //we go to the right of the tree
                }
            }
        }

        //Do one last compare to end point
        newNode.lineDirection = !current.lineDirection; //opposite direction
        if (current.lineDirection == VERTICAL) {
            if (current.p.x() > p.x()) { //if the node we insert is less than the parent
                current.lb = newNode; //we go to the left of the tree
            }
            else/*greater or equal to the parent i.e 'not left'*/ {
                current.rt =newNode; //we go to the right of the tree
            }
        }
        else/*line direction is horizontal*/ {
            if (current.p.y() > p.y()) { //if the node we insert is less than the parent
               current.lb = newNode; //we go to the left of the tree
            }
            else/*greater or equal to the parent i.e 'not left'*/ {
                current.rt = newNode; //we go to the right of the tree
            }
        }
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
