package maze;

public class Tile 
{
	//Tile class has boolean to determine if it is a wall.  if it is a wall, then the pacman can't go there
	private boolean isWall;
	private int row;
	private int column;
	private int distanceToFood;

	public boolean solidWall()
	{
		return isWall;
	};
	
	//print visualization on the console
	public void printTileStatus()
	{
		if(solidWall())
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
		this.distanceToFood = 1000000000;
	}
	
	//convert tile to wall
	public void setWall()
	{
		this.isWall = true;
	}

	public int getRow() {
		return row;
	}


	public int getColumn() 
	{
		return column;
	}
	
	public void setDistance(int distance)
	{
		this.distanceToFood = distance;
	}

	public int getDistance()
	{
		return distanceToFood;
	}
	
	
}