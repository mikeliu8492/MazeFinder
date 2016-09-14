package maze;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.*;

public class Maze 
{

	private Tile [][] mazeForm;
	private int totalRows;
	private int totalColumns;
	
	private ArrayList<Coordinates> foodSpots;
	private Coordinates pacmanStart;
	
	private PacMan ourHero;
	
	public Maze(String fileToParse) throws IOException
	{
		foodSpots = new ArrayList<Coordinates>();
		setDimensions(fileToParse);
		mazeForm = new Tile[totalRows][totalColumns];
		instantiateMaze();
		erectWalls(fileToParse);
		ourHero = new PacMan(this, pacmanStart.getRow(), pacmanStart.getColumn());
	}
	
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
	
	public Tile getTile(int row, int col)
	{
		
		if(row >= 0 && row <totalRows && col >= 0 && col <= totalColumns)
			return mazeForm[row][col];
		return null;
	}
	
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
	
	private boolean isFoodSpot(int row, int col)
	{
		for(Coordinates coordinates : foodSpots)
		{
			if(row == coordinates.getRow() && col == coordinates.getColumn())
				return true;
		}
		
		return false;
	}
	
	private boolean locatedPacman(int row, int col)
	{
		if(row == ourHero.getCurrentRow() && col == ourHero.getCurrentColumn())
			return true;
		return false;
	}
	
	private int manhattan(Coordinates destination, Tile currentTile)
	{
		int destinationRow = destination.getRow();
		int destinationCol = destination.getColumn();
		
		int currentRow = currentTile.getRow();
		int currentCol = currentTile.getColumn();
		
		return Math.abs(destinationRow-currentRow) + Math.abs(destinationCol-currentCol);
	}
	
	private Tile evaluateNeighbors(Coordinates destination, Tile currentTile)
	{
		int currentRow = currentTile.getRow();
		int currentCol = currentTile.getColumn();
		
		ArrayList<Tile> myTiles = new ArrayList<Tile>();
		
		return null;
	}
	
}
