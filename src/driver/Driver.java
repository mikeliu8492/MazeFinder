package driver;

import maze.*;

import java.io.*;
import java.util.*;

public class Driver 
{
	public static void main(String args[]) throws IOException
	{
		
		//change the name to any of the three mazes, and you can follow the basic heuristic
		String FILE_TO_READ = "tinySearch.txt";
		
		Maze myMaze = new Maze(FILE_TO_READ);
		
		System.out.println(myMaze.getRows());
		System.out.println(myMaze.getColumns());
		
		myMaze.displayMaze();
		
		//myMaze.displayDistances();
		
		/**
		 * call DFS for depth first
		 * call BFS for breadth first
		 * call Greedy for greedy best-first
		 * A* not implemented yet
		 */
		
		myMaze.A_STAR_MULTI(myMaze.getOrigin());
		myMaze.showMazePath();
		//System.out.println("Orig dots:  " + myMaze.dotsTotal);
		//System.out.println("found dots:  " + myMaze.dotsFound);
		
	}
	
	
}
