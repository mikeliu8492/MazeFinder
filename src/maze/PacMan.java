package maze;

import java.util.ArrayList;

public class PacMan 
{
	//PACMAN objects, intends for him to keep track of where he goes and where his start position is
	
	private Maze assignedMaze;
	private ArrayList<Movement> movementList;
	private int currentRow;
	private int currentColumn;
	
	
	public PacMan(Maze assignedMaze, int startRow, int startColumn)
	{
		this.assignedMaze = assignedMaze;
		this.setCurrentRow(startRow);
		this.setCurrentColumn(startColumn);
		
	}


	public int getCurrentRow() {
		return currentRow;
	}


	public void setCurrentRow(int currentRow) {
		this.currentRow = currentRow;
	}


	public int getCurrentColumn() {
		return currentColumn;
	}


	public void setCurrentColumn(int currentColumn) {
		this.currentColumn = currentColumn;
	}
}
