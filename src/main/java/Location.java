public class Location {
    protected int row;
    protected int col;

    public Location(int r, int c) {
        row = r;
        col = c;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int dist(Location otherLoc) {
        return Math.abs(this.getCol() - otherLoc.getCol()) + Math.abs(this.getRow() - otherLoc.getRow());
    }

    public boolean equals(Location otherLoc) {
        return row == otherLoc.getRow() && col == otherLoc.getCol();
    }

    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}