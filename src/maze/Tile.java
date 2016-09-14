package maze;

public class Tile 
{
	
	private boolean isWall;
	private int row;
	private int column;

	public boolean solidWall()
	{
		return isWall;
	};
	
	public void printTileStatus()
	{
		if(solidWall())
			System.out.print("%");
		else
			System.out.print(" ");
		
	};
	
	public Tile(int row, int col)
	{
		this.row = row;
		this.column = col;
		this.isWall = false;
	}
	
	public void setWall()
	{
		this.isWall = true;
	}

	public int getRow() {
		return row;
	}


	public int getColumn() {
		return column;
	}

	
	
	
}