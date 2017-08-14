/*
 * Defines each piece. Pieces are identified by String name
 * Kailash Jayaram
 */
public class Piece
{
	//instance vars
    private String name;// identifier
    private String color;
    
    //special case checks
    private boolean hasMoved;
    private boolean canCastle;
    private boolean twoSpace;// en passant check
    private boolean hasCastled;
    
    public Piece(String color, String name)
    {
        this.name = name;
        twoSpace = false;
        this.color = color;
        hasMoved = false;
        canCastle = true;
        hasCastled = false;
    }
    /**
     * copy constructor
     * @param clone
     */
    public Piece(Piece clone)
    {
    	this.name = new String(clone.getName());
    	this.twoSpace = clone.istwospace();
    	this.color = new String(clone.getColor());
    	this.hasMoved = clone.hasMoved();
    	this.canCastle = clone.canCastle();
    	this.hasCastled = clone.hasCastled();
    }
    public String getName()
    {
        return name;
    }
    
    public String getColor()
    {
        return color;
    }
    
    public boolean hasMoved()
    {
        return hasMoved;
    }
    
    public void move()
    {
        hasMoved = true;
        canCastle = false;
    }   
    
    public boolean istwospace()
    {
        return twoSpace;
    }
    
    public void twospacetrue()
    {
        twoSpace=true;
    }
    
    public void twospacefalse()
    {
        twoSpace= false;
    }
    
    public boolean canCastle()
    {
        return canCastle;
    }
    
    public void setCastle(boolean in)
    {
        canCastle = in;
    }
    
    public void setName(String name)
    {
        this.name = name;
        
    }
    
    public boolean hasCastled()
    {
        return hasCastled;
    }
    
    public void castle()
    {
        hasCastled = true;
    }
    public boolean equals(Piece temp)
    {
    	if(temp.getName().equals(name) && temp.getColor().equals(color))
    	{
    		return true;
    	}
    	return false;
    }
}




