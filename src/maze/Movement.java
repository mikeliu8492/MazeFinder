package maze;

public class Movement 
{
	/**
	 * keeps track of path pacman takes
	 */
	private int rowChange;//difference in row change
	private int columnChange;//differnce in column change
	private int stayRow;//row where pacman stops before transition
	private int stayColumn;//column where pacman stops before transition
	
	public Movement(final int rowChange, final int columnChange, final int row, final int column)
	{
		this.rowChange = rowChange;
		this.columnChange = columnChange;
		this.stayRow = row;
		this.stayColumn = column;
	}
	
	public int getColumnChange() {
		return columnChange;
	}

	public int getRowChange() {
		return rowChange;
	}

	public int getStayRow() {
		return stayRow;
	}

	public int getStayColumn() {
		return stayColumn;
	}


	
	
	
	

}