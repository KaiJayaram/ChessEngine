/*
 * object to be held in each tree node
 * Kailash Jayaram
 */
public class GameState
{
	//instance vars
    private final boolean isMax;// node type
    private Board b;// position
    private double value;// position eval
    
    public GameState(boolean isMax, Board b, double value)
    {
        this.isMax = isMax;
        this.b = b;
        this.value = value;
    }
    
    public double getValue()
    {
        return value;
    }
    
    public boolean isMax()
    {
        return isMax;
    }
    
    public Board getBoard()
    {
        return b;
    }
    
    public void setValue(double value)
    {
        this.value = value;
    } 
    
    public void setBoard(Board b)
    {
        this.b = b;
    }
}


