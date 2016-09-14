package maze;

public class Movement 
{
	private int rowChange;
	private int columnChange;
	private int stayRow;
	private int stayColumn;
	
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