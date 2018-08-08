/* This class represents the would-be node, but is particularly
	a class that enhances the location class. It has the necessary
	attributes a node needs to use the A* algorithm, these are the
	Parent, g-cost, h-cost, and f-cost.
*/
public class CellNode {

    private Location loc;
    CellNode parent;
    private int g;
    private int h;
    private int f;

    //This constructor is for the current place holder
    public CellNode() {

    }

    //This constructor is for the root
    public CellNode(Location loc, Location user) {
        this.loc = loc;
        parent = null;
        this.g = 0;
        this.h = Math.abs(loc.getCol() - user.getCol()) + Math.abs(loc.getRow() - user.getRow());
        this.f = g + h;
    }

    //Constructs the children
    public CellNode(CellNode parent, Location loc, Location user) {
        this.loc = loc;
        this.parent = parent;
        this.g = parent.g + cost(parent.getNodeLoc(), loc);
        this.h = Math.abs(loc.getCol() - user.getCol()) + Math.abs(loc.getRow() - user.getRow());
        this.f = g + h;
    }

    public Location getNodeLoc() {
        return loc;
    }

    public int getGcost() {
        return g;
    }

    public int getHcost() {
        return h;
    }

    public int getFcost() {
        return g + h;
    }

    private void setG(int g) {
        g = g;
    }

    private void setH(int h) {
        h = h;
    }

    private void setF(int g, int h) {
        f = g + h;
    }

    public void setParent(CellNode parent) {
        this.parent = parent;
    }

    public int cost(Location loc, Location loc2) {
        return Math.abs(loc.getCol() - loc2.getCol()) + Math.abs(loc.getRow() - loc2.getRow());
    }

    public String toString() {
        return ("( " + this.loc + ", " + parent + ", "
                + g + ", " + h + ", " + f + ")");
    }
}

