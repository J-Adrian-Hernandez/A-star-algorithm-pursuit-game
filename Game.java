/*
 * Jose Adrian Hernandez
 * Dr. Koh
 * CS 4480
 * 11/30/16
 * Description: A game in which agents pursue a player in a map with
 * obstacles using the A* star algorithm to calculate the shortest
 * path to the player. The goal of the game is to escape the enemies
 * as long as possible.
 * 
*/
import java.util.*;
import java.lang.*;
public class Game{
	
//Easy linking to actual file names
private String user = "user.gif";
private String[] enemies = {"enemy.png", "enemy2.png", "enemy3.png"};
private String obs = "obstacle.png";

private Color[] rgb = {new Color(255, 0, 0), new Color(0, 255, 0), new Color(0, 0, 255)};
private boolean isPaused = false;
private boolean isOver = false;
private boolean nodeVisuals = false;
private Grid grid;
private Grid background;
private Grid gameOver;
private int userRow;
private int userCol;
private Location userLoc;
private int numEnemies;
private int enemyRow;
private int enemyCol;
private Location enemyLoc;
private int enemyRow2;
private int enemyCol2;
private Location enemyLoc2;
private int enemyRow3;
private int enemyCol3;
private Location enemyLoc3;
private int msElapsed;
private int timesAvoid;
private int dimensions = 15;
private int cut = 15;
  
  public Game()
  { 
	  
    grid = new Grid(dimensions, dimensions);
    userRow = grid.getNumRows()-1;
    userCol = 0;
    userLoc = new Location(userRow, userCol);
    enemyRow = 0;
    enemyCol = 0;
    enemyLoc = new Location(enemyRow, enemyCol);
    enemyRow2 = 0;
    enemyCol2 = grid.getNumCols()-1;
    enemyLoc2 = new Location(enemyRow2, enemyCol2);
    enemyRow3 = grid.getNumRows()-1;
    enemyCol3 = grid.getNumCols()-1;
    enemyLoc3 = new Location(enemyRow3, enemyCol3);
    msElapsed = 0;
    timesAvoid = 0;
    updateTitle();
    addObs();
    grid.setImage(new Location(userRow, userCol), user);
    grid.setImage(new Location(enemyRow, enemyCol), enemies[0]);
    grid.setImage(new Location(enemyRow2, enemyCol2), enemies[1]);
    grid.setImage(new Location(enemyRow3, enemyCol3), enemies[2]);
  }
  
  public void play()
  {
	  Random r = new Random();
	  Stack<Location> tempPath = (findPath(enemyLoc, userLoc));
	  Stack<Location> tempPath2 = (findPath(enemyLoc2, userLoc));
	  Stack<Location> tempPath3 = (findPath(enemyLoc3, userLoc));
	  Location tempUser = userLoc;
    while (!isOver)
    {
    	//Allows user to unpause the game
    	handleKeyPress();
    	while(!isPaused && !isOver){
	      grid.pause(48);
	      handleKeyPress();
	      
	      //if(!tempUser.equals(userLoc)){
	    	  tempPath = (findPath(enemyLoc, userLoc));
	    	  tempPath2 = (findPath(enemyLoc2, userLoc));
	    	  tempPath3 = (findPath(enemyLoc3, userLoc));
	    	  tempUser = userLoc;
	      //}
	      
	      if (msElapsed % 150 == 0)
	      {
	    	  //updateUserLocation();
	    	  //findPath(enemyLoc, userLoc);
	    	  int random = r.nextInt(3);
	    	  if(!tempPath.isEmpty()){
	    		  
	    		  if(tempPath.peek().equals(tempPath2.peek())
	    				  || tempPath.peek().equals(tempPath3.peek())){
	    			  if(random == 0){
				    	  enemyMove(tempPath.pop(), enemyLoc, enemies[0]);
				    	  isGameOver();
	    			  }
	    			  else if (random == 1){
				    	  enemyMove(tempPath2.pop(), enemyLoc2, enemies[1]);
				    	  isGameOver();
	    			  }
		    		  else if (random == 2){
		    			  enemyMove(tempPath3.pop(), enemyLoc3, enemies[2]);
		    			  isGameOver();
		    		  }
	    			  
	    		  }
	    		  else{
	    			  enemyMove(tempPath.pop(), enemyLoc, enemies[0]);
	    			  isGameOver();
	    			  enemyMove(tempPath2.pop(), enemyLoc2, enemies[1]);
	    			  isGameOver();
	    			  enemyMove(tempPath3.pop(), enemyLoc3, enemies[2]);
	    			  isGameOver();
	    		  }
	    	  }
	      }
	      updateTitle();
	      msElapsed += 50;
	      upCut();
	    }
    }
  }
  
  public void handleKeyPress()
  {
	  
	  int key = grid.checkLastKeyPressed();
	  if(!isPaused){
		  //Up
		  if(key == 38 || key == 87){
			  userRow--;
			  updateUserLocation();
			  if(isWalkable(userLoc)){
				  grid.setImage(new Location(userRow, userCol), user);
				  grid.setImage(new Location(userRow+1, userCol), null);
			  }else{
				  userRow++;
				  updateUserLocation();
			  }
		  }
		  //Down
		  if(key == 40 || key == 83){
			  
			  userRow++;
			  updateUserLocation();
			  if(isWalkable(userLoc)){
				  grid.setImage(new Location(userRow, userCol), user);
				  grid.setImage(new Location(userRow-1, userCol), null);
			  }else{
				  userRow--;
				  updateUserLocation();
			  }
			  
		  }
		  //Left
		  if(key == 37 || key == 65){
			  userCol--;
			  updateUserLocation();
			  if(isWalkable(userLoc)){
				  grid.setImage(new Location(userRow, userCol), user);
				  grid.setImage(new Location(userRow, userCol+1), null);
			  }else{
				  userCol++;
				  updateUserLocation();
			  }	  
		  }
		  //Right
		  if(key == 39 || key == 68){
			  userCol++;
			  updateUserLocation();
			  if(isWalkable(userLoc)){
				  grid.setImage(new Location(userRow, userCol), user);
				  grid.setImage(new Location(userRow, userCol-1), null);
			  }else{
				  //failed to move so restore previous location
				  userCol--;
				  updateUserLocation();
			  }	  
		  }
	  }
	  //The space bar should pause the game
	  if(key == 32){
		  if(isPaused){
			  isPaused = false;
			  System.out.println("The game has resumed");
		  }
		  else{
			  isPaused = true;
			  System.out.println("The game has been paused");
		  }
	  }
	  
  }
  
  public void updateUserLocation(){
	userLoc = (new Location(userRow, userCol));  
  }
  
  public void updateEnemyLocation(){
		enemyLoc = (new Location(enemyRow, enemyCol));  
  }
  
  //checks if a location is within boundary
  public boolean withinBound(Location loc){
	  return loc.getRow() >= 0 && loc.getRow() < grid.getNumRows()
			  && loc.getCol() >= 0 && loc.getCol() <= grid.getNumCols() -1;
  }
  
  //Returns false if that location is blocked, true otherwise
  public boolean isExpandable(Location loc){
	  return withinBound(loc) && (grid.getImage(loc) == null || 
			  grid.getImage(loc) == user);
  }
  public boolean isWalkable(Location loc){
	  return withinBound(loc) && grid.getImage(loc) == null;
  }
  
  
  public void enemyMove(Location closer, Location regi, String which){
	  grid.setImage(new Location(regi.getRow(), regi.getCol()), null);
	  regi.row = closer.getRow();
	  regi.col = closer.getCol();
	  //updateEnemyLocation();
	  grid.setImage(new Location(regi.getRow(), regi.getCol()), which);
  }
  
  //Uses A* to find the shortest path from enemy to user
  public Stack<Location> findPath(Location enemyLoc, Location userLoc){
	  
	  //will contain all the moves necessary provided by the A* algorithm
	  Stack<Location> path = new Stack<Location>();
	  
	  //Open list of nodes
	  List<CellNode> open = new ArrayList<CellNode>();
	  
	  //Closed list of nodes
	  List<CellNode> closed = new ArrayList<CellNode>();
	  
	  //Add starting location to open
	  //Notice that the starting node does not have a parent
	  open.add(new CellNode(enemyLoc, userLoc));
	  
	  //Make the starting node the current node
	  CellNode current = (CellNode) open.get(0);
	  //System.out.println(current); /*Tests toString function*/
	  //Initial node keeps track of the initial location of enemy
	  Location initial = new Location(enemyLoc.getRow(), enemyLoc.getCol());
	  
	  Random r = new Random();
	  
	  
	  while(!current.getNodeLoc().equals(userLoc) && 
			  !(open.size() > cut*current.getNodeLoc().dist(userLoc))){
		  
		  Location myCurrent = new Location(current.getNodeLoc().getRow(),
				  current.getNodeLoc().getCol());
		  //Determine which neighbors are non obstructions
		  //checking neighbors in this order Up, Down, Left, Right
		  Location up = new Location(myCurrent.getRow()-1, myCurrent.getCol());
		  Location down = new Location(myCurrent.getRow()+1, myCurrent.getCol());
		  Location left = new Location(myCurrent.getRow(), myCurrent.getCol()-1);
		  Location right = new Location(myCurrent.getRow(), myCurrent.getCol()+1);
		  
		  //Array containing current's neighbors
		  Location[] neighbors = new Location[] {up, down, left, right};
		  
		  for( Location neighbor : neighbors){
			  if(isExpandable(neighbor)){
				  open.add(new CellNode(current, neighbor, userLoc));
				  if(nodeVisuals){
				  grid.setColor(neighbor, rgb[1]);
				  }
			  }
		  }
		  
		  /*Testing to see that only the right locations(neighbors) were put in open
		  It works!*/
		  
		  //System.out.println(open);
		  
		  //Calculate costs of all CellNodes in open list
		  
		  //remove the one with the lowest f cost from the open and add to close
		  //If two have lowest f add the one with lowest h, if tied again
		  //add one at random
		  //lowest is set to a large number so that any CellNode will be able to overwrite it
		  int lowestF = 999;
		  int tempF;
		  int tempH;
		  int lowestH = 999;
		  for(CellNode child: open){
			  if(child.parent != null){
				  tempF = child.getFcost();
				  tempH = child.getHcost();
				  if(tempF < lowestF){
					  lowestF = tempF;
					  lowestH = tempH;
				  }
				  else if(tempF == lowestF){
					  if(tempH < lowestH){
						  lowestH = tempH;
					  }
					  else{
						  
					  }
				  }
			  }
		  }
		  
		  //We need a list of candidates, in case of a F cost tie
		  List<CellNode> candidates = new ArrayList<CellNode>();
		  
		  for(CellNode child : open){
			  if(child.getFcost() == lowestF && child.getHcost() == lowestH){
				  candidates.add(child);
			  }
		  }
		  
		  //If there was indeed a tie, we choose one at random to be current
		  //else if there is only one candidate, that will be our current
		  int random = r.nextInt(candidates.size());
		  current = candidates.get(random);
		 
		  //Add current to closed list
		  closed.add(current);
		  if(nodeVisuals){
			  grid.setColor(current.getNodeLoc(), rgb[0]);
		  }
		  //Remove current from open list
		  open.remove(current);
		  
		  //recalculate f costs of all nodes in open
		  //object to use cost
		  //May need to revise this one
		  for(CellNode node : open){
			  if(node.parent != null){
				  for(CellNode explored: closed){
					  if((explored.getGcost() 
							  + node.cost(explored.getNodeLoc(), node.getNodeLoc())) 
							  < node.getGcost()){
						  node.setParent(explored);
					  }
				  }
			  }
		  }
		  
	  }
	  
	  path.push(current.getNodeLoc());
	  while(current.parent != null && !current.parent.getNodeLoc().equals(initial)){
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
  
  //Spawns obstacles
  public void addObs(){
	  for(int i = 2; i < grid.getNumCols()-1; i++){
		  for(int j = 2; j < grid.getNumRows()-2; j++){
			  if(i % j == 1 && (i % 3 == 0 || i % 3 == 1)){
			  grid.setImage(new Location(i, j), obs);
			  }
		  }
	  }
  }
  
  public void updateTitle()
  {
    grid.setTitle("Pursuit " + "Score " + getScore());
  }
  
  public void isGameOver() {
	  //The game ends once user is touched by enemy
	  if(!isOver){
		  if(enemyLoc.equals(userLoc) || enemyLoc2.equals(userLoc)
				  || enemyLoc3.equals(userLoc)){
			  isOver = true;
			  System.out.println("Game Over!");
			  grid.showMessageDialog("Game Over!\n" + "Score " + getScore());
		  }
	  }
  }
  
  public int getScore()
  {
    return (msElapsed/900);
  }
  
  public void upCut(){
	  if(cut > 1)
	  cut--;
  }
  
  public static void test()
  {
    Game game = new Game();
    game.play();
  }
  
  public static void main(String[] args)
  {
    test();
  }
}