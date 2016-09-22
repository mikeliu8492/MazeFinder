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
	
	public int dotsFound = 0;
	public int dotsTotal;
	public int tilesExplored = 0;
	
	private Tile [][] mazeForm;
	private int totalRows;
	private int totalColumns;
	
	PriorityQueue<Tile> open = new PriorityQueue<Tile>();
	ArrayList<Tile> closed = new ArrayList<Tile>();
	
	
	
	/**
	 * the spots where there is dots for pacman to eat
	 */
	private ArrayList<Coordinates> foodSpots;
	
	/**
	 * Copy of the foodSpots list that we will pop from.  Indicates when all dots
	 * have been consumed and the search ends.
	 */
	private ArrayList<Coordinates> accountingForFoods;
	
	
	/**
	 * coordinate for where pacman starts
	 */
	private Coordinates pacmanStart;
	
	/**
	 * actual tile where pacman starts
	 */
	private Tile pacmanOrigin;
	
	
	/**
	 * this is the final destination tile after the search is complete
	 */
	
	public Tile finalDestination;
	
	/**
	 * This is an arraylist of the tiles backtraced from end to start.
	 */
	
	public ArrayList<Tile> backTrace;
	
	/**
	 * initializes maze, sets up food, and erects walls
	 * @param fileToParse			String of file to parts for maze
	 * @throws IOException
	 */
	
	
	
	public Maze(String fileToParse) throws IOException
	{
		backTrace = new ArrayList<Tile>();
		foodSpots = new ArrayList<Coordinates>();
		accountingForFoods = new ArrayList<Coordinates>();
		setDimensions(fileToParse);
		mazeForm = new Tile[totalRows][totalColumns];
		instantiateMaze();
		erectWalls(fileToParse);
		distanceFill();
		dotsTotal = foodSpots.size();
		dotsFound = 0;
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
				else if(locatePacman(i, m))
					System.out.print("P");
				else
					mazeForm[i][m].printTileStatus();
			}
			
			System.out.println("");
		}
	}
	
	public void displaySpecialMaze()
	{
		for (int i = 0; i < totalRows; i++)
		{
			for (int m = 0; m < totalColumns; m++)
			{
				if(isFoodSpot(i, m))
					System.out.print(".");
				else if(locatePacman(i, m))
					System.out.print("P");
				else
					mazeForm[i][m].printTileStatus();
			}
			
			System.out.println("");
		}
	}
	
	
	/**
	 * Examines maze based on food locations.  Fills variable distanceToFood with Manhattan distance
	 * to the closest food dot.
	 */
	private void distanceFill()
	{
		for(int row = 0; row < totalRows; row++)
		{
			for(int col = 0; col < totalColumns; col++)
			{
				
				calculateClosestDistance(row, col, this.getTile(row, col));
			}
		}
		
	}
	
	/**
	 * Helper functions to compare Manhattan distance of one point to a food item to other food items.
	 * Returns distance to closest food item.
	 * 
	 * @param row			row of current tile
	 * @param col			column of current tile
	 * @param currentTile	
	 */
	private void calculateClosestDistance(int row, int col, Tile currentTile)
	{
		int min = currentTile.distanceToFood;
		if(!currentTile.isWall)
		{
			for(Coordinates coordinate : accountingForFoods)
			{
				int total = manhattan(coordinate, currentTile);
				if(min > total)
					min = total;
			}
		}
		
		currentTile.distanceToFood = min;
		currentTile.heuristicScore = currentTile.distanceToFood;
		currentTile.recalculateHeuristic();
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
	 * helper to erect walls in the maze
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
					mazeForm[lines][col].isWall = true;
				else if(strLine.charAt(col) == '.')
				{
					foodSpots.add(new Coordinates(lines, col));
					accountingForFoods.add(new Coordinates(lines, col));
				}
					
				else if(strLine.charAt(col) == 'P')
					pacmanStart = new Coordinates(lines, col);
				col++;
			}
			
			lines++;
		}
		
		in.close();
		fstream.close();
		
	}
	
	/**
	 * Display the distances on the board per non-wall tile to the nearest food items.
	 */
	
	public void displayDistances()
	{
		for(int row = 0; row < totalRows; row++)
		{
			for(int col = 0; col < totalColumns; col++)
			{
				if(!mazeForm[row][col].isWall)
				{
						System.out.print("Rows:  " + row +"   ");
						System.out.print("Col:  " + col +"   ");
						System.out.print("Distance to food:  " + mazeForm[row][col].heuristicScore + "  ");
						System.out.println();
				}

			}
		}
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
		for(Coordinates coordinates : accountingForFoods)
		{
			if(row == coordinates.getRow() && col == coordinates.getColumn())
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * Add it onto list
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean fromOrigInventory(int row, int col)
	{
		
		return false;
	}
	
	
	/**
	 * locates the pacman object, see if he's in a specific tile
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean locatePacman(int row, int col)
	{
		return (pacmanStart.getRow() == row && col == pacmanStart.getColumn());
	}
	
	/**
	 * determine Manhattan distance to a specific tile
	 * @param destination	coordinates of a tile with food on it
	 * @param currentTile	current tile you are comparing to the destination
	 * @return
	 */
	private int manhattan(Coordinates destination, Tile currentTile)
	{
		int destinationRow = destination.getRow();
		int destinationCol = destination.getColumn();
		
		int currentRow = currentTile.row;
		int currentCol = currentTile.column;
		
		return Math.abs(destinationRow-currentRow) + Math.abs(destinationCol-currentCol);
	}
	
	
	/**
	 * Depth first search
	 * @thePoint	coordinate for starting point
	 */
	
	public void DFS(Coordinates thePoint)
	{
		Stack<Tile> searchStore = new Stack<Tile>();
		
		
		Tile startTile = this.getTile(thePoint.getRow(), thePoint.getColumn());
		startTile.visited = true;
		
		System.out.println(startTile.row);
		System.out.println(startTile.column);
		
		
		searchStore.push(startTile);
		
		while(searchStore.size() > 0)
		{
			
			Tile currentTile = searchStore.pop();
			tilesExplored++;
			
			int currentRow = currentTile.row;
			int currentColumn = currentTile.column;
			
			if(isFoodSpot(currentRow, currentColumn))
			{
				System.out.println("FOUND FINAL TILE!");
				finalDestination = currentTile;
				break;
			}
			
			Tile right = mazeForm[currentRow][currentColumn+1];
			Tile up = mazeForm[currentRow-1][currentColumn];
			Tile left = mazeForm[currentRow][currentColumn-1];
			Tile down = mazeForm[currentRow+1][currentColumn];
			
			if(!right.isWall && !right.visited)
			{
				searchStore.push(right);
				right.visited = true;
				right.parent = currentTile;
			}
			if(!up.isWall && !up.visited)
			{
				searchStore.push(up);
				up.visited = true;
				up.parent = currentTile;
			}
			if(!left.isWall && !left.visited)
			{
				searchStore.push(left);
				left.visited = true;
				left.parent = currentTile;
			}
			if(!down.isWall && !down.visited)
			{
				searchStore.push(down);
				down.visited = true;
				down.parent = currentTile;
			}
			
			
		}
		
		System.out.println("\n\n\n");
		traceYourPath(finalDestination, null);
		showMazePath();
		
	}
	
	/**
	 * Breadth First Search
	 * @thePoint	coordinate for starting point
	 */
	
	public void BFS(Coordinates thePoint)
	{
		Queue<Tile> searchStore = new LinkedList<Tile>();
		
		Tile startTile = this.getTile(thePoint.getRow(), thePoint.getColumn());
		startTile.visited = true;
		
		
		searchStore.add(startTile);
		
		while(searchStore.size() > 0)
		{
			
			Tile currentTile = searchStore.poll();
			tilesExplored++;
			int currentRow = currentTile.row;
			int currentColumn = currentTile.column;
			
			if(isFoodSpot(currentRow, currentColumn))
			{
				System.out.println("FOUND FINAL TILE!");
				finalDestination = currentTile;
				System.out.println("dest row "+ finalDestination.row);
				System.out.println("dest col "+finalDestination.column);
				break;
			}
			
			Tile right = mazeForm[currentRow][currentColumn+1];
			Tile up = mazeForm[currentRow-1][currentColumn];
			Tile left = mazeForm[currentRow][currentColumn-1];
			Tile down = mazeForm[currentRow+1][currentColumn];
			
			if(!right.isWall && !right.visited)
			{
				searchStore.add(right);
				right.visited = true;
				right.parent = currentTile;
				
			}
			if(!up.isWall && !up.visited)
			{
				searchStore.add(up);
				up.visited = true;
				up.parent = currentTile;
			}
			if(!left.isWall && !left.visited)
			{
				searchStore.add(left);
				left.visited = true;
				left.parent = currentTile;
			}
			if(!down.isWall && !down.visited)
			{
				searchStore.add(down);
				down.visited = true;
				down.parent = currentTile;
			}
			
			
		}
		
		System.out.println("\n\n\n");
		traceYourPath(finalDestination, null);
		//showMazePath();
		System.out.println("\n\n\n");
	}
	
	/**
	 * Greedy best search first algorithm
	 * @thePoint	coordinate for starting point
	 */
	
	public void Greedy(Coordinates thePoint)
	{
		PriorityQueue<Tile> searchStore = new PriorityQueue<Tile>();
		Tile startTile = this.getTile(thePoint.getRow(), thePoint.getColumn());
		startTile.visited = true;
		
		System.out.println(startTile.row);
		System.out.println(startTile.column);
		
		
		searchStore.add(startTile);
		
		while(searchStore.size() > 0)
		{
			
			Tile currentTile = searchStore.poll();
			tilesExplored++;
			int currentRow = currentTile.row;
			int currentColumn = currentTile.column;
			
			if(isFoodSpot(currentRow, currentColumn))
			{
				System.out.println("FOUND FINAL TILE!");
				finalDestination = currentTile;
				break;
			}
			
			Tile right = mazeForm[currentRow][currentColumn+1];
			Tile up = mazeForm[currentRow-1][currentColumn];
			Tile left = mazeForm[currentRow][currentColumn-1];
			Tile down = mazeForm[currentRow+1][currentColumn];
			
			if(!right.isWall && !right.visited)
			{
				searchStore.add(right);
				right.visited = true;
				right.parent = currentTile;
			}
			if(!up.isWall && !up.visited)
			{
				searchStore.add(up);
				up.visited = true;
				up.parent = currentTile;
			}
			if(!left.isWall && !left.visited)
			{
				searchStore.add(left);
				left.visited = true;
				left.parent = currentTile;
			}
			if(!down.isWall && !down.visited)
			{
				searchStore.add(down);
				down.visited = true;
				down.parent = currentTile;
			}
			
			
		}
		
		System.out.println("\n");
		traceYourPath(finalDestination, null);
		//showMazePath();
	}
	
	
	/**
	 * Trace the walk path from end to start
	 */
	
	private void traceYourPath(Tile endTile, Tile onePastStart)
	{
		Tile tracer = finalDestination;
		int counter = 0;
		while(tracer != onePastStart)
		{
			backTrace.add(tracer);
			tracer = tracer.parent;
			counter++;
		}
		
		//System.out.println("tracer size" + backTrace.size());
	}
	
	
	
	/**
	 * Determine if a tile is in the traced path from start to end
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean inTracedPath(int row, int col)
	{
		for(Tile current : backTrace)
		{
			if(row == current.row && col == current.column)
				return true;
		}
		
		return false;
		
	}
	
	
	/**
	 * show the actual traced path from start to end
	 */
	public void showMazePath()
	{
		
		
		for (int i = 0; i < totalRows; i++)
		{
			for (int m = 0; m < totalColumns; m++)
			{
				if(locatePacman(i, m))
					System.out.print("P");
				else if(inTracedPath(i, m))
					System.out.print(".");
				else
					mazeForm[i][m].printTileStatus();
			}
			
			System.out.println("");
		}
		
		System.out.println("Explored squares:  "  + this.tilesExplored);
		System.out.println("Path length:  "  + this.backTrace.size());
	}
	
	
	/**
	 * TODO!!! Implement A*
	 */
	
	

	
	
	
	
	public void A_STAR(Coordinates thePoint)
	{
		
		Tile startTile = this.getTile(thePoint.getRow(), thePoint.getColumn());
		
		
		open.add(startTile);
		
		while (!open.isEmpty())
		{
			Tile minTile = open.poll();
			tilesExplored++;
				
			int minRow = minTile.row;
			int minCol = minTile.column;
			
			closed.add(minTile);
			
			if(isFoodSpot(minRow, minCol))
			{
				finalDestination = mazeForm[minRow][minCol];
				System.out.println("FOUND FINAL TILE!");
				System.out.println("dest row "+ finalDestination.row);
				System.out.println("dest col "+finalDestination.column);
				break;
			}
			
			ArrayList<Tile> neighbors = new ArrayList<Tile>();
			
			populateArrayList(neighbors, minTile);

			for (Tile neighbor : neighbors)
			{
				
				int newDistanceCost = minTile.distanceTraveled + 1;
				int newTotalCost = neighbor.distanceToFood + newDistanceCost;
					
				int emuRow = neighbor.column;
				int emuCol = neighbor.row;
				
				if(newTotalCost < neighbor.heuristicScore || !inOpen(neighbor))
				{
					neighbor.heuristicScore = newTotalCost;
					neighbor.distanceTraveled = newDistanceCost;
					neighbor.parent = minTile;
					
					if(!inOpen(neighbor))
					{
						open.add(neighbor);
						
					}
					else
					{
						open.remove(neighbor);
						open.add(neighbor);
					}
				}
				
				
				
				
			}
			
			neighbors.clear();
			
		}
		
		System.out.println("\n\n\n");
		traceYourPath(finalDestination, null);
		
	}

	
	/**
	 * helper method to populate an array with neighbors
	 * @param neighbors		populate this array
	 * @param minTile		the parent tile to get neighbors
	 */

	private void populateArrayList(ArrayList<Tile> neighbors, Tile minTile)
	{
		populateSuccessor(neighbors, minTile, 0,1);
		populateSuccessor(neighbors, minTile, 1,0);	
		populateSuccessor(neighbors, minTile, 0,-1);
		populateSuccessor(neighbors, minTile, -1,0);
		
	}
	
	
	/**
	 * What to do for each neighbor.  Calculate the heuristic and link it with parent
	 * @param neighbors			array to populate
	 * @param myParent			the parent node
	 * @param rowChange			offset row-wise from parent
	 * @param colChange			offset column-wise from parent
	 */
	private void populateSuccessor(ArrayList<Tile> neighbors, Tile myParent, int rowChange, int colChange)
	{
		
		int evalRow = myParent.row + rowChange;
		int evalCol = myParent.column + colChange;
		Tile evaluated = mazeForm[evalRow][evalCol];
		if(!evaluated.isWall && !inClosed(evaluated))
		{ 
			neighbors.add(evaluated);
		}
	}
	
	
	/**
	 * tile is in closed list
	 * @param emu		tile in question
	 * @return
	 */
	private boolean inClosed(Tile emu)
	{
		for(Tile tile : closed)
		{
			if(tile.row == emu.row && tile.column == emu.column)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Tile is in open list
	 * @param emu		tile in question
	 * @return
	 */
	private boolean inOpen(Tile emu)
	{
		for(Tile tile : open)
		{
			if(tile.row == emu.row && tile.column == emu.column)
				return true;
		}
		return false;
	}
	
	
	/**
	 * retrieves pacman's original starting position
	 * @return
	 */
	
	public Coordinates getOrigin()
	{
		return pacmanStart;
	}
	
	
	/**
	 * For now this uses BFS for debugging, the logic should be correct in decreasing the remaining dots to
	 * be eaten.
	 * @param thePoint
	 */
	
	public void A_STAR_MULTI(Coordinates thePoint)
	{
		Coordinates pacmanStart = thePoint;
		Tile origin = getTile(pacmanStart.getRow(), pacmanStart.getColumn());
		Tile briefStop;
		
		int oneLess = accountingForFoods.size()-1;
		
		while(!accountingForFoods.isEmpty())
		{
			A_STAR(pacmanStart);
			briefStop = finalDestination;
			Coordinates toEliminate = null;
			
			for(Coordinates coordinate : accountingForFoods)
			{
				if(coordinate.getRow() == briefStop.row && coordinate.getColumn() == briefStop.column)
				{
					toEliminate = coordinate;
					break;
				}
					
			}
			
			if(toEliminate != null)
			{
				accountingForFoods.remove(toEliminate);
				dotsFound++;
			}
				
			
			open.clear();
			closed.clear();
			
			//retool the distances
			distanceFill();
			clearMaze();
			
			System.out.println("accounting for food:  " + accountingForFoods.size());
			
			pacmanStart = new Coordinates(briefStop.row, briefStop.column);
		}
		
	}


	/**
	 * set all visited variables to false and parent variables to null
	 */
	private void clearMaze()
	{
		for(int row = 0; row < totalRows; row++)
		{
			for(int col = 0; col < totalColumns; col++)
			{
				mazeForm[row][col].visited= false;
				mazeForm[row][col].parent = null;
				mazeForm[row][col].distanceTraveled = 0;
				mazeForm[row][col].recalculateHeuristic();
			}
		}
	}
	
	
}
