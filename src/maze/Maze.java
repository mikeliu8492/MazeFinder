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
	private Tile [][] mazeForm;
	private int totalRows;
	private int totalColumns;
	
	ArrayList<Tile> open = new ArrayList<Tile>();
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
	
	
	private int squaresExplored;
	
	
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
		
		int squaresSearched = 0;
		while(searchStore.size() > 0)
		{
			
			Tile currentTile = searchStore.pop();
			squaresSearched++;
			
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
				//squaresSearched++;
			}
			if(!up.isWall && !up.visited)
			{
				searchStore.push(up);
				up.visited = true;
				up.parent = currentTile;
				//squaresSearched++;
			}
			if(!left.isWall && !left.visited)
			{
				searchStore.push(left);
				left.visited = true;
				left.parent = currentTile;
				//squaresSearched++;
			}
			if(!down.isWall && !down.visited)
			{
				searchStore.push(down);
				down.visited = true;
				down.parent = currentTile;
				//squaresSearched++;
			}
			
			
		}
		
		this.squaresExplored = squaresSearched;
		System.out.println("\n\n\n");
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
		
		int squaresSearched = 0;
		while(searchStore.size() > 0)
		{
			
			Tile currentTile = searchStore.poll();
			squaresSearched++;
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
		
		this.squaresExplored = squaresSearched;
		System.out.println("\n\n\n");
		showMazePath();
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
		
		int squaresSearched = 0;
		while(searchStore.size() > 0)
		{
			
			Tile currentTile = searchStore.poll();
			squaresSearched++;
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
		
		this.squaresExplored = squaresSearched;
		System.out.println("\n");
		showMazePath();
	}
	
	
	/**
	 * Trace the walk path from end to start
	 */
	
	private void traceYourPath()
	{
		Tile tracer = finalDestination;
		int counter = 0;
		while(tracer != null)
		{
			backTrace.add(tracer);
			tracer = tracer.parent;
			//System.out.println("trace counter" + counter);
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
		traceYourPath();
		
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
		
		System.out.println("Explored squares:  "  + this.squaresExplored);
		System.out.println("Path length:  "  + this.backTrace.size());
	}
	
	
	/**
	 * TODO!!! Implement A*
	 */
	
	
	
	/**
	 * Since the priority queue is already given a measurement comparable for the greedy algorithm,
	 * we use an unsorted list instead.  This finds the least cost item in a given list based on 
	 * the heuristic score.
	 * @param yourList			can be the open or closed list
	 * @return
	 */
	
	private Tile leastCost(ArrayList<Tile> yourList)
	{
		int minCost = yourList.get(0).heuristicScore;
		int counter = 0;
		int minIndex = 0;
		while (counter < yourList.size())
		{
			if(yourList.get(counter).heuristicScore < minCost)
			{
				minCost = yourList.get(counter).heuristicScore;
				minIndex = counter;
			}
			
			counter++;
		}
		return yourList.get(minIndex);

	}
	
	
	
	
	public void A_STAR(Coordinates thePoint)
	{
		
		Tile startTile = this.getTile(thePoint.getRow(), thePoint.getColumn());
		PriorityQueue<Tile> searchStore = new PriorityQueue<Tile>();
		
		
		int counter = 0;
		
		searchStore.add(startTile);
		
		while (!searchStore.isEmpty())
		{
			System.out.println("entered loop");
			Tile minTile = searchStore.poll();
			
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
<<<<<<< HEAD

				System.out.println("outside neighbor row " + neighbor.row + "  outside neighbor col " + neighbor.column);
				samePositionlowerCost(neighbor, minTile, newTotalCost, newDistanceCost, searchStore);
=======
				
				boolean lowerCostOpen = samePositionlowerCost(neighbor, newTotalCost, open);
				boolean lowerCostClosed = samePositionlowerCost(neighbor, newTotalCost, closed);
				
				if(!inOpen(neighbor))
				{
					neighbor.distanceTraveled = newDistanceCost;
					neighbor.heuristicScore = newTotalCost;
					open.add(neighbor);
					neighbor.parent = minTile;
					counter++;
				}
>>>>>>> 6d708725d63ff25e16ed95cd631202cba4b7bfe8
				
				
				
			}
			
			Tile other = searchStore.poll();
			if(other.heuristicScore < minTile.heuristicScore)
			{
				searchStore.add(other);
			}
			
		}
		
		this.squaresExplored = counter;
		System.out.println("\n\n\n");
		showMazePath();
		
	}

	
	/**
	 * helper method to populate an array with neighbors
	 * @param neighbors		populate this array
	 * @param minTile		the parent tile to get neighbors
	 */

	private void populateArrayList(ArrayList<Tile> neighbors, Tile minTile)
	{
		
		populateSuccessor(neighbors, minTile, 1,0);	
		populateSuccessor(neighbors, minTile, -1,0);
		populateSuccessor(neighbors, minTile, 0,-1);
		populateSuccessor(neighbors, minTile, 0,1);
		
		
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
			evaluated.parent = myParent;
			neighbors.add(evaluated);
		}
	}
	
	
	/**
	 * determine if a tile is the same position but lower cost in the terms of distance traveled,
	 * maybe deprecated b/c priority queue
	 * @param emu			imitation tile to pass int data
	 * @param cost			total cost of travel and heuristic
	 * @param inspection	list to inspect against(open or closed list)
	 * @return
	 */
	private boolean samePositionlowerCost(Tile emu, Tile curParent, int cost, int distanceTraveled, PriorityQueue<Tile> inspection)
	{
		int emuRow = emu.column;
		int emuCol = emu.row;
		
		PriorityQueue<Tile> temp = new PriorityQueue<Tile>();
		Tile compareTile = null;
		boolean existsInHeap = false;
		boolean lowerCostFound = false;
		
		
		if(inspection.isEmpty())
		{
			inspection.add(emu);
		}
		else
		{
			while(!inspection.isEmpty())
			{
				//System.out.println("entered loop");
				compareTile = inspection.poll();			
				if(emuRow == compareTile.row && emuCol == compareTile.column)
				{
					existsInHeap = true;
					if(cost < compareTile.heuristicScore)
					{
						compareTile.heuristicScore = cost;
						compareTile.distanceTraveled = distanceTraveled;
						compareTile.parent = curParent;
						temp.add(compareTile);
						lowerCostFound = true;
					}
					break;
					
					
					
				}
				else
				{
					temp.add(compareTile);
				}
				
				
			}
		}

		
		
		while(!temp.isEmpty())
		{
			inspection.add(temp.poll());
			
		}

		
		return false;
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
		Tile briefStop;
		
		while(!accountingForFoods.isEmpty())
		{
			BFS(pacmanStart);
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
				
			
			//open.clear();
			//closed.clear();
			
			//retool the distances
			distanceFill();
			clearMaze();
			
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
			}
		}
	}
	
}
