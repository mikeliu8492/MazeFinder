package maze;

public class FoodRepresentation 
{
	public Coordinates location;
	public int symbol;
	
	FoodRepresentation(int row, int column, int symbol)
	{
		location = new Coordinates(row, column);
		this.symbol = symbol;
	}
}
