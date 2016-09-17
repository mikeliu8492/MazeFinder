package maze;

import java.util.*;

public class Tile implements Comparable<Tile>
{
	//Tile class has boolean to determine if it is a wall.  if it is a wall, then the pacman can't go there
	public boolean isWall;
	public boolean visited;
	
	public int row;
	public int column;
	
	public Tile parent;
	
	/**
	 * only distance to food is used for greedy
	 * the "heuristicScore" is used for A*
	 */
	public int heuristicScore;
	public int distanceTraveled;
	public int distanceToFood;
	
	//print visualization on the console
	public void printTileStatus()
	{
		if(isWall)
			System.out.print("%");
		else
			System.out.print(" ");
		
	};
	
	//create an empty tile
	public Tile(int row, int col)
	{
		this.row = row;
		this.column = col;
		this.isWall = false;
		this.visited = false;
		this.distanceToFood = 1000000000;
		this.distanceTraveled = 0;
	}
	
	
	
	/**
	 * Method for priority queue. Uses heuristic of manhattan distance to nearest food item
	 * as heuristic.  The lost the distance, the higher the priority.
	 */

	@Override
	public int compareTo(Tile other) 
	{
		if(this.distanceToFood < other.distanceToFood)
			return 1;
		else if (this.distanceToFood == other.distanceToFood)
			return 0;
		else
			return -1;

	}


	
	
}