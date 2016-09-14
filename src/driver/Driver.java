package driver;

import maze.*;

import java.io.*;
import java.util.*;

public class Driver 
{
	public static void main(String args[]) throws IOException
	{
		
		String FILE_TO_READ = "mediumMaze.txt";
		Maze myMaze = new Maze(FILE_TO_READ);
		
		System.out.println(myMaze.getRows());
		System.out.println(myMaze.getColumns());
		
		System.out.println("\n\n");
		myMaze.displayMaze();
	
		
	}
	
	
}
