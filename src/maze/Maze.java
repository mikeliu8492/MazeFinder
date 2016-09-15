package maze;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.*;

public class Maze 
{
	/**
	 * create the maze form
	 */
	private Tile [][] mazeForm;
	private int totalRows;
	private int totalColumns;
	
	/**
	 * the spots where there is dots for pacman to eat
	 */
	private ArrayList<Coordinates> foodSpots;
	
	/**
	 * coordinate for where pacman starts
	 */
	private Coordinates pacmanStart;
	
	/**
	 * pacman tracks his own path
	 */
	
	private PacMan ourHero;
	
	/**
	 * initializes maze, sets up food, and erects walls
	 * @param fileToParse			String of file to parts for maze
	 * @throws IOException
	 */
	public Maze(String fileToParse) throws IOException
	{
		foodSpots = new ArrayList<Coordinates>();
		setDimensions(fileToParse);
		mazeForm = new Tile[totalRows][totalColumns];
		instantiateMaze();
		erectWalls(fileToParse);
		ourHero = new PacMan(this, pacmanStart.getRow(), pacmanStart.getColumn());
	}
	
	
	/**
	 * helper to determine dimensions of maze based on file
	 * @param fileToParse
	 * @throws IOException
	 */
	void setDimensions(String fileToParse) throws IOException
	{
		FileInputStream fstream = new FileInputStream(fileToParse);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		int lines = 0;
		
		while ((strLine = br.readLine()) != null)   
		{
			if (lines == 0)
				this.totalColumns = strLine.length();
			lines++;
		}
		
		in.close();
		fstream.close();
		
		this.totalRows = lines;
	}
	
	
	/**
	 * Displays maze to confirm accuracy
	 */
	public void displayMaze()
	{
		for (int i = 0; i < totalRows; i++)
		{
			for (int m = 0; m < totalColumns; m++)
			{
				if(isFoodSpot(i, m))
					System.out.print(".");
				else if(locatedPacman(i, m))
					System.out.print("P");
				else
					mazeForm[i][m].printTileStatus();
			}
			
			System.out.println("");
		}
	}
	
	
	/**
	 * Get the tile on the board.
	 * @param row
	 * @param col
	 * @return
	 */
	public Tile getTile(int row, int col)
	{
		
		if(row >= 0 && row <totalRows && col >= 0 && col <= totalColumns)
			return mazeForm[row][col];
		return null;
	}
	
	
	/**
	 * helper to build maze of empty tiles
	 */
	private void instantiateMaze()
	{
		for (int i = 0; i < totalRows; i++)
		{
			for (int m = 0; m < totalColumns; m++)
			{
				mazeForm[i][m] = new Tile(i, m);
			}
		}
	}
	
	/**
	 * helper to erect wass in the maze
	 * @param fileToParse
	 * @throws IOException
	 */
	private void erectWalls(String fileToParse) throws IOException
	{
		FileInputStream fstream = new FileInputStream(fileToParse);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		
		int lines = 0;

		while ((strLine = br.readLine()) != null)   
		{
			int col = 0;
			while(col < strLine.length())
			{
				if(strLine.charAt(col) == '%')
					mazeForm[lines][col].setWall();
				else if(strLine.charAt(col) == '.')
					foodSpots.add(new Coordinates(lines, col));
				else if(strLine.charAt(col) == 'P')
					pacmanStart = new Coordinates(lines, col);
				col++;
			}
			
			lines++;
		}
		
		in.close();
		fstream.close();
		
	}
	

	public int getRows()
	{
		return totalRows;
	}
	
	public int getColumns()
	{
		return totalColumns;
	}
	
	/**
	 * Determine if a specific space has a foodspot.
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isFoodSpot(int row, int col)
	{
		for(Coordinates coordinates : foodSpots)
		{
			if(row == coordinates.getRow() && col == coordinates.getColumn())
				return true;
		}
		
		return false;
	}
	/**
	 * locates the pacman object, see if he's in a specific tile
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean locatedPacman(int row, int col)
	{
		if(row == ourHero.getCurrentRow() && col == ourHero.getCurrentColumn())
			return true;
		return false;
	}
	
	/**
	 * determine manhattan distance to a specific tile
	 * @param destination
	 * @param currentTile
	 * @return
	 */
	private int manhattan(Coordinates destination, Tile currentTile)
	{
		int destinationRow = destination.getRow();
		int destinationCol = destination.getColumn();
		
		int currentRow = currentTile.getRow();
		int currentCol = currentTile.getColumn();
		
		return Math.abs(destinationRow-currentRow) + Math.abs(destinationCol-currentCol);
	}
	
	
	/**
	 * TODO:  not sure what this is yet, part of heuristic let me figure it out.
	 * @param destination
	 * @param currentTile
	 * @return
	 */
	private Tile evaluateNeighbors(Coordinates destination, Tile currentTile)
	{
		int currentRow = currentTile.getRow();
		int currentCol = currentTile.getColumn();
		
		ArrayList<Tile> myTiles = new ArrayList<Tile>();
		
		return null;
	}
	
}
