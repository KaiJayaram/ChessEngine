/*
 * Defines each tile on the board
 * Kailash Jayaram
 */
import java.util.*;
public class Tiles
{
	//instance vars
    private boolean isWhite;
    private String location;
    private Piece unit;
    
    public Tiles(String loc, boolean isWhite)
    {
        location = loc;
        this.isWhite = isWhite;
        unit = null;
    }
    
    public Tiles()
    {
        location = null;
        isWhite= false;
        unit = null;
    }
    
    public boolean isWhite()
    {
        return isWhite; 
    }   
    
    public boolean hasPiece()
    {
        return (unit!=null);
    }
    
    public String toString()
    {
          return "Location: " + location; 
    }
    
    public void setLoc(String loc)
    {
        location = loc;        
    }
    
    public String getLoc()
    {
        return location;        
    }
    
    public boolean addPiece(Piece temp)
    {
        if(!hasPiece()){
            unit = temp;
            return true;
        }
        else {
        	return false;
        }
    }
    
    public Piece removePiece()
    {
        //if(!hasPiece()) throw new NoSuchElementException();
        Piece temp = unit;
        unit = null;
        return temp;
    }
    
    public Piece getPiece()
    {   
        return unit;
    }
    
    public void setWhite(boolean temp)
    {
        isWhite = temp;
        
    }
    
    /**
     * captures piece
     * @param temp
     */
    public void setPiece(Piece temp)
    {
        unit = temp;
    }   
}