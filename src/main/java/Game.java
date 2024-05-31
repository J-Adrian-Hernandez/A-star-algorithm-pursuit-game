/**
 * J. Adrian Hernandez
 * 11/30/16
 * Description: A game in which agents pursue a player in a map with
 * obstacles using the A* star algorithm to calculate the shortest
 * path to the player. The goal of the game is to escape the enemies
 * as long as possible while you capture as many gems as you can. Big
 * score bonuses for capturing gems.
 *
 */

import javax.swing.*;
import java.util.*;
import java.lang.*;

public class Game {

    // Linking to actual file names
    private String user = "user.gif";
    private String gem = "red-gem.png";
    private String[] enemies = {"enemy.png", "enemy2.png", "enemy3.png"};
    private String obs = "obstacle.png";

    private Color[] rgb = {new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255)};
    private boolean isPaused = false;
    private boolean isOver = false;
    //TODO make the visuals look nicer
    private boolean nodeVisuals = false;
    private Grid grid;
    private int score;
    private int userRow;
    private int userCol;
    private Location userLoc;
    private int enemyRow;
    private int enemyCol;
    private Location gemLoc;
    private int gemRow;
    private int gemCol;
    private boolean gemExists;
    private Location enemyLoc;
    private int enemyRow2;
    private int enemyCol2;
    private Location enemyLoc2;
    private int enemyRow3;
    private int enemyCol3;
    private Location enemyLoc3;
    private int msElapsed;
    private int dimensions;
    private int cut = 15; //Used in the measurement of the minimum distance to enemies
    //for A* to execute per enemy
    private int nTimesKeyHandled;

    public Game() {
        dimensions = 20;
        grid = new Grid(dimensions, dimensions);
        score = 0;
        addObs();
        userRow = grid.getNumRows() - 1;
        userCol = 0;
        userLoc = new Location(userRow, userCol);
        gemRow = getFreeCellForGem().getRow();
        gemCol = getFreeCellForGem().getCol();
        gemLoc = new Location(gemRow, gemCol);
        enemyRow = 0;
        enemyCol = 0;
        enemyLoc = new Location(enemyRow, enemyCol);
        enemyRow2 = 0;
        enemyCol2 = grid.getNumCols() - 1;
        enemyLoc2 = new Location(enemyRow2, enemyCol2);
        enemyRow3 = grid.getNumRows() - 1;
        enemyCol3 = grid.getNumCols() - 1;
        enemyLoc3 = new Location(enemyRow3, enemyCol3);
        msElapsed = 0;
        updateTitle();
        grid.setImage(new Location(userRow, userCol), user);
        grid.setImage(new Location(gemRow, gemCol), gem);
        gemExists = true;
        grid.setImage(new Location(enemyRow, enemyCol), enemies[0]);
        grid.setImage(new Location(enemyRow2, enemyCol2), enemies[1]);
        grid.setImage(new Location(enemyRow3, enemyCol3), enemies[2]);
    }

    public void play() {
        Random r = new Random();
        Stack<Location> tempPath;
        Stack<Location> tempPath2;
        Stack<Location> tempPath3;
        Location tempUser;

        //This variable helps cap the number of times the key is handled while the
        //thread is sleeping (capping it to 1)

        while (!isOver) {
            //Allows user to unpause the game
            while(isPaused){
                grid.pause(50);
                handleKeyPress();
            }
            while (!isPaused && !isOver) {
                nTimesKeyHandled = 0;
                grid.pause(100);
                if(nTimesKeyHandled < 1){
                    handleKeyPress();
                }
                gemCaught();
                if (!gemExists) addGem();
                tempPath = (findPath(enemyLoc, userLoc));
                tempPath2 = (findPath(enemyLoc2, userLoc));
                tempPath3 = (findPath(enemyLoc3, userLoc));
                tempUser = userLoc;

                if (msElapsed % 200 == 0) {

                    int random = r.nextInt(3);
                    if (!tempPath.isEmpty()) {
                        //Randomly selects which enemy makes the first chasing move
                        if (tempPath.peek().equals(tempPath2.peek())
                                || tempPath.peek().equals(tempPath3.peek())) {
                            if (random == 0) {
                                enemyMove(tempPath.pop(), enemyLoc, enemies[0]);
                                isGameOver();
                            } else if (random == 1) {
                                enemyMove(tempPath2.pop(), enemyLoc2, enemies[1]);
                                isGameOver();
                            } else if (random == 2) {
                                enemyMove(tempPath3.pop(), enemyLoc3, enemies[2]);
                                isGameOver();
                            }

                        } else {
                            enemyMove(tempPath.pop(), enemyLoc, enemies[0]);
                            isGameOver(); //Returns true if enemy ocupies the same space as user
                            enemyMove(tempPath2.pop(), enemyLoc2, enemies[1]);
                            isGameOver();
                            enemyMove(tempPath3.pop(), enemyLoc3, enemies[2]);
                            isGameOver();
                        }
                    }
                }
                updateTitle();
                msElapsed += 100;
                //upCut();
            }
        }
    }

    public void handleKeyPress() {

        int key = grid.checkLastKeyPressed();
        if (!isPaused) {

            // Pausing
            if (key == 32) {
                isPaused = true;
                System.out.println("The game has been paused");
            }

            // The up arrow key and the 'W' key moves the player up one space when pressed
            if (key == 38 || key == 87) {
                userRow--;
                updateUserLocation();
                if (isWalkable(userLoc)) {
                    grid.setImage(new Location(userRow, userCol), user);
                    grid.setImage(new Location(userRow + 1, userCol), null);
                } else {
                    userRow++;
                    updateUserLocation();
                }
                nTimesKeyHandled++;
            }
            // The down arrow key and the 'S' key move the player down one space when pressed
            if (key == 40 || key == 83) {

                userRow++;
                updateUserLocation();
                if (isWalkable(userLoc)) {
                    grid.setImage(new Location(userRow, userCol), user);
                    grid.setImage(new Location(userRow - 1, userCol), null);
                } else {
                    userRow--;
                    updateUserLocation();
                }
                nTimesKeyHandled++;
            }
            // The left arrow key and the 'A' key moves the player left one space when pressed
            if (key == 37 || key == 65) {
                userCol--;
                updateUserLocation();
                if (isWalkable(userLoc)) {
                    grid.setImage(new Location(userRow, userCol), user);
                    grid.setImage(new Location(userRow, userCol + 1), null);
                } else {
                    userCol++;
                    updateUserLocation();
                }
                nTimesKeyHandled++;
            }
            // The right arrow and the 'D' key moves the player right one space when pressed
            if (key == 39 || key == 68) {
                userCol++;
                updateUserLocation();
                if (isWalkable(userLoc)) {
                    grid.setImage(new Location(userRow, userCol), user);
                    grid.setImage(new Location(userRow, userCol - 1), null);
                } else {
                    //failed to move so restore previous location
                    userCol--;
                    updateUserLocation();
                }
                nTimesKeyHandled++;
            }
        }
        else{
            if(key == 32){
                isPaused = false;
                System.out.println("The game has resumed");
            }
        }

    }

    public void updateUserLocation() {
        userLoc = (new Location(userRow, userCol));
    }

    public void updateEnemyLocation() {
        enemyLoc = (new Location(enemyRow, enemyCol));
    }

    //checks if a location is within boundary
    public boolean withinBound(Location loc) {
        return loc.getRow() >= 0 && loc.getRow() < grid.getNumRows()
                && loc.getCol() >= 0 && loc.getCol() <= grid.getNumCols() - 1;
    }

    // Returns false if that location is blocked, true otherwise
    public boolean isExpandable(Location loc) {
        return withinBound(loc) && (grid.getImage(loc) == null ||
                grid.getImage(loc) == user);
    }

    // Returns true if all surrouding blocks are empty and within boundaries
    public boolean isWalkable(Location loc) {
        return withinBound(loc) && (grid.getImage(loc) == null || grid.getImage(loc) == gem);
    }

    public boolean isGemSpawnable(Location loc) {
        return withinBound(loc) || grid.getImage(loc) == enemies[0] ||
                grid.getImage(loc) == enemies[1] || grid.getImage(loc) == enemies[2];
    }

    // Modifier method for the continuous updating of the enemy location
    public void enemyMove(Location closer, Location regi, String which) {
        grid.setImage(new Location(regi.getRow(), regi.getCol()), null);
        regi.row = closer.getRow();
        regi.col = closer.getCol();
        //updateEnemyLocation();
        grid.setImage(new Location(regi.getRow(), regi.getCol()), which);
    }

    // Uses the A* algorithm to find the shortest path from enemy to user
    public Stack<Location> findPath(Location enemyLoc, Location userLoc) {

        // Will contain all the moves necessary provided by the A* algorithm
        Stack<Location> path = new Stack<Location>();

        // Open list of nodes
        List<CellNode> open = new ArrayList<CellNode>();

        // Closed list of nodes
        List<CellNode> closed = new ArrayList<CellNode>();

        // Add starting location to open
        // Notice that the starting node does not have a parent
        open.add(new CellNode(enemyLoc, userLoc));

        // Make the starting node the current node
        CellNode current = (CellNode) open.get(0);
        //System.out.println(current); /*Tests toString function*/

        // Initial node keeps track of the initial location of enemy
        Location initial = new Location(enemyLoc.getRow(), enemyLoc.getCol());

        Random r = new Random();

        // While the user hasn't been caught and the distance
        // from enemy to user multiplied by an arbitrary
        // 'cut' isn't more than the number of nodes contained in open
        while (!current.getNodeLoc().equals(userLoc) &&
                !(open.size() > cut * current.getNodeLoc().dist(userLoc))) {

            Location myCurrent = new Location(current.getNodeLoc().getRow(),
                    current.getNodeLoc().getCol());
            // Determine which neighbors are non obstructions
            // Checking neighbors in this order Up, Down, Left, Right
            Location up = new Location(myCurrent.getRow() - 1, myCurrent.getCol());
            Location down = new Location(myCurrent.getRow() + 1, myCurrent.getCol());
            Location left = new Location(myCurrent.getRow(), myCurrent.getCol() - 1);
            Location right = new Location(myCurrent.getRow(), myCurrent.getCol() + 1);

            // Array containing current's neighbors
            Location[] neighbors = new Location[]{up, down, left, right};

            for (Location neighbor : neighbors) {
                if (isExpandable(neighbor)) {
                    open.add(new CellNode(current, neighbor, userLoc));
                    if (nodeVisuals) {
                        grid.setColor(neighbor, rgb[1]);
                    }
                }
            }

		  /*Testing to see that only the right locations(neighbors) were put in open */

            //System.out.println(open);

            /* Calculate costs of all CellNodes in open list
                remove the one with the lowest f cost from the open and add to close
                If two have lowest f add the one with lowest h, if tied again
                add one at random
                lowest is set to an Integer's mac value ( (2^31)-1 )
                so that any CellNode will be able to overwrite it
            */
            int lowestF = Integer.MAX_VALUE;
            int tempF;
            int tempH;
            int lowestH = Integer.MAX_VALUE;
            for (CellNode child : open) {
                if (child.parent != null) {
                    tempF = child.getFcost();
                    tempH = child.getHcost();
                    if (tempF < lowestF) {
                        lowestF = tempF;
                        lowestH = tempH;
                    } else if (tempF == lowestF) {
                        if (tempH < lowestH) {
                            lowestH = tempH;
                        } else {

                        }
                    }
                }
            }

            // We need a list of candidates, in case of a F cost ties
            List<CellNode> candidates = new ArrayList<CellNode>();

            for (CellNode child : open) {
                if (child.getFcost() == lowestF && child.getHcost() == lowestH) {
                    candidates.add(child);
                }
            }

            // If there was indeed a tie, we choose one at random to be current
            // Else if there is only one candidate, that will be our current
            int random = r.nextInt(candidates.size());
            current = candidates.get(random);

            // Add current to closed list
            closed.add(current);
            // nodeVisuals is used for testing and illustration purposes
            if (nodeVisuals) {
                grid.setColor(current.getNodeLoc(), rgb[0]);
            }
            //Remove current from open list
            open.remove(current);

            // Recalculate f costs of all nodes in open
            // Object to use cost
            for (CellNode node : open) {
                if (node.parent != null) {
                    for (CellNode explored : closed) {
                        if ((explored.getGcost()
                                + node.cost(explored.getNodeLoc(), node.getNodeLoc()))
                                < node.getGcost()) {
                            node.setParent(explored);
                        }
                    }
                }
            }

        }

        path.push(current.getNodeLoc());
        while (current.parent != null && !current.parent.getNodeLoc().equals(initial)) {
            current = current.parent;
            path.push(current.getNodeLoc());
        }

	  /*Show contents of stack
	   * System.out.println(path);
	  System.out.println(path.peek());
	  System.out.println(path);*/

		//temp path for redrawing
	  /*blackPath = path;

	  while(!blackPath.isEmpty()){
		  grid.setColor(blackPath.pop(), new Color(0,0,0));
	  }*/
        return path;
    }

    // Spawns obstacles
    public void addObs() {
        Random r = new Random();
        int random;
        for (int i = 2; i < grid.getNumCols() - 1; i++) {
            for (int j = 2; j < grid.getNumRows() - 2; j++) {
                random = r.nextInt(grid.getNumRows());
                if (random % j == 1 && (random % 3 == 0 || i % 3 == 1)) {
                    grid.setImage(new Location(i, j), obs);
                }
            }
        }
    }

    // Spawns gems the user collects for points
    public Location getFreeCellForGem() {

        Random r = new Random();

        int row;
        int col;
        do{
            row = r.nextInt(grid.getNumRows());
            col = r.nextInt(grid.getNumRows());
        }while (!isGemSpawnable(new Location(row, col)));

        return new Location(row, col);
    }

    public void gemCaught() {
        if (userLoc.equals(gemLoc)){
            gemLoc = null;
            gemExists = false;
            score += 50;
        }
    }

    public void addGem() {
        Location newGemLoc = getFreeCellForGem();
        gemRow = newGemLoc.getRow();
        gemCol = newGemLoc.getCol();
        updateGemLocation();
        grid.setImage(new Location(newGemLoc.getRow(), newGemLoc.getCol()), gem);
        gemExists = true;
    }

    public void updateGemLocation() {
        gemLoc = new Location(gemRow, gemCol);
    }

    public void updateTitle() {
        grid.setTitle("Pursuit " + "Score " + getScore());
    }

    public void isGameOver(){
        //The game ends once user is touched by enemy
        if (!isOver) {
            if (enemyLoc.equals(userLoc) || enemyLoc2.equals(userLoc)
                    || enemyLoc3.equals(userLoc)) {
                isOver = true;
                System.out.println("Game Over!");
                grid.showMessageDialog("Game Over!\n" + "Score " + getScore());
                int choice = grid.showConfirmDialog();
                if(choice == JOptionPane.YES_NO_OPTION){
                    grid.getFrame().dispose();
                    test();
                }
                else{
                    grid.getFrame().dispose();
                }

            }
        }
    }

    public int getScore() {
        return this.score + (msElapsed / 1000);
    }

    public void upCut() {
        if (cut > 1)
            cut--;
    }

    public static void test() {
        Game game = new Game();
        game.play();
    }

    public static void main(String[] args) {
        String audioFilePath = "./src/main/resources/HyouhakuKokuten.wav";
        Audio audioPlayer = new Audio(audioFilePath);
        audioPlayer.play();
        test();
        audioPlayer.pause(1000);
        System.exit(0);
    }
}
