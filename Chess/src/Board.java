/*
 * Board holds a position. Holds engine methods.
 * Kailash Jayaram
 */
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

@SuppressWarnings("unchecked")
public class Board
{
	// instance vars
    private Tiles[][] grid;
    
    //piece values
    private double Kv;
    private double Qv;
    private double Pv;
    private double Rv;
    private double Bv;
    
    //minmax tree
    private DefaultMutableTreeNode root;
    private DefaultMutableTreeNode move;
    
    /**
     * creates a new chess board out of a 2d array of tiles and assigns tile colors 
     * and initial piece positions
     */
    public Board()
    {
    	// initializes board array
        grid = new  Tiles[8][8];
        int files = 0;
        
        // initial piece values
        Bv = 2.8;
        Kv = 3.2;
        Rv = 5;
        Qv = 9;
        Pv = 1;
         
        root = new DefaultMutableTreeNode();
        
        // initialize board
        for(int rank = 0; rank < 8; rank++)//rows
        {
            for(char file = 'a'; file <= 'h'; file++)//ascii representation columns
            {
                String temp = file + Integer.toString(rank);// piece location as string
                
                //initialize tiles with proper colors
                if((file + rank) %2 ==0)
                    grid[rank][files] = new Tiles(temp,true);
                else
                grid[rank][files] = new Tiles(temp,false);
    
            // adds the pieces
            if (rank == 1) {
            	
                // creates the row of white pawns     
                grid[rank][files].addPiece(new Piece("White" , "Pawn"));               
                } 
            
            else if (rank == 6) {
            	
            	// creates the row of black pawns
                grid[rank][files].addPiece(new Piece("Black","Pawn"));
                } 
            
            // initializes white back rank
            else if (rank == 0) {
            	
                switch (file) {
                    case 'a':
                    case 'h':
                       grid[rank][files].addPiece(new Piece("White","Rook"));
                        break;
                    case 'b':
                    case 'g':
                       grid[rank][files].addPiece(new Piece("White", "Knight"));
                        break;
                    case 'c':
                    case 'f':
                        grid[rank][files].addPiece(new Piece("White","Bishop"));
                        break;
                    case 'd':
                        grid[rank][files].addPiece(new Piece("White","Queen"));
                        break;
                    case 'e':
                      grid[rank][files].addPiece(new Piece("White","King"));
                       break;
 
                }
                
            } 
            
            // initializes black back rank
            else if (rank == 7) {
                switch (file) {
                
                   case 'a':
                    case 'h':
                       grid[rank][files].addPiece(new Piece("Black","Rook"));
                        break;
                    case 'b':
                    case 'g':
                       grid[rank][files].addPiece(new Piece("Black","Knight"));
                        break;
                    case 'c':
                    case 'f':
                        grid[rank][files].addPiece(new Piece("Black","Bishop"));
                        break;
                    case 'd':
                        grid[rank][files].addPiece(new Piece("Black","Queen"));
                        break;
                    case 'e':
                        grid[rank][files].addPiece(new Piece("Black","King"));
                        break;
                }
            }
            
            files++;// increments the file
            }
        files = 0;
        }                
    }
    
    /**
     * creates a board out of a 2d array of tiles
     * @param grid
     */
    public Board(Tiles[][] grid)
    {
        this.grid = grid;
        
        // initial piece values
        Bv = 2.8;
        Kv = 3.2;
        Rv = 5;
        Qv = 9;
        Pv = 1;
    }
    
    /**
     * given an initial and final location it moves the piece
     * @param intLoc
     * @param finLoc
     * @return boolean
     */
    public boolean movePiece(String intLoc, String finLoc)
    {
    	
        int file = intLoc.charAt(0)-97;//converts from ascii
        int rank = Integer.parseInt(intLoc.substring(1,2));
        
        boolean castled = false;// castle check
        
        if(!grid[rank][file].hasPiece())return false;//checks if there is a piece at loc
        if(!canMove(intLoc,finLoc))return false;// checks if move is legal
        else
        {
            int distance = finLoc.charAt(0)-intLoc.charAt(0);// castle check
            
            if(grid[rank][file].getPiece().getName().equals("King"))//for King
            {
                if(distance==-2||distance==2)// if king is moved 2 spaces(castling)
                {
                    Piece tempKing = grid[rank][file].removePiece();
                    
                    // parses location from string
                    file = finLoc.charAt(0)-97;
                    rank = Integer.parseInt(finLoc.substring(1,2));
                    
                    grid[rank][file].setPiece(tempKing);// moves the king
                    
                    // determines where to move the rook and moves it
                    if(distance==-2)
                    {
                        Piece tempRook = grid[rank][file-2].removePiece();
                        int rfile = finLoc.charAt(0)-96;
                        grid[rank][rfile].setPiece(tempRook);
                        tempRook.move();
                        tempKing.castle();
                        castled = true;
                    }
                    if(distance==2)
                    {
                        Piece tempRook = grid[rank][file+1].removePiece();
                        int rfile = finLoc.charAt(0)-98;
                        grid[rank][rfile].setPiece(tempRook);
                        tempKing.castle();
                        tempRook.move();
                        castled = true;
                    }
                }
            }
            
            // if the king was not castled
            if(!castled)
            {
            
                Piece temp = grid[rank][file].removePiece();
                
                // parse location from string
                file = finLoc.charAt(0)-97;
                rank = Integer.parseInt(finLoc.substring(1,2));
                
                grid[rank][file].setPiece(temp);// moves piece
                
                // special condition for queening
                if(temp.getName().equals("Pawn")&&(rank == 7 || rank == 0)) {
                	temp.setName("Queen");
                }
                
                //special pawn case check
                grid[rank][file].getPiece().twospacefalse();
                // distance pawn moved
                int vdist = Integer.parseInt(finLoc.substring(1,2))
                - Integer.parseInt(intLoc.substring(1,2));
                temp.move();
                
            // if pawn moved 2 squares
            if(grid[rank][file].getPiece().getName().equals("Pawn")&&vdist==2||vdist==-2)
            {
                grid[rank][file].getPiece().twospacetrue();
            }
            
            // checks for en passant
            if(temp.getName().equals("Pawn"))
            {
                if(temp.getColor().equals("Black"))
                {
                    if(grid[rank+1][file].hasPiece()&&grid[rank+1][file].getPiece().getColor().equals("White")&&grid[rank+1][file].getPiece().istwospace())
                    {
                        grid[rank+1][file].removePiece();
                    }
                }
                else
                {
                    if(grid[rank-1][file].hasPiece()&&grid[rank-1][file].getPiece().getColor().equals("Black")&&grid[rank-1][file].getPiece().istwospace())
                    {
                        grid[rank-1][file].removePiece();
                    }
                }
            }
        

        }
        } 
        return true;// returns true if piece was moved
        
    }
    
    /**
     * checks if a given move is valid
     * @param intLoc: initial piece location
     * @param finLoc: final piece location
     * @return boolean
     */
    public boolean canMove(String intLoc, String finLoc)//checks if a peice can move to a certain position
    {
    	// parses locations from string
        int file = finLoc.charAt(0)-97;
        int files = intLoc.charAt(0)-97;
        int ranks = Integer.parseInt(intLoc.substring(1,2));
        
        Tiles in = grid[ranks][files];// initial tile location
        if(in.hasPiece())
        {
        ArrayList<Tiles> moves = movementLines(in);// creates an array of possible moves
        
          if(in.getPiece().getName().equals("Pawn"))//checks if pawn and adds possible en passants
        {
            if(in.getPiece().getColor().equals("Black"))
            {
                if(files+1<=7 && grid[ranks][files+1].hasPiece()&&grid[ranks][files+1].getPiece().istwospace())
                {
                    moves.add(grid[ranks-1][files+1]);
                }
                if(files-1 >= 0 && grid[ranks][files-1].hasPiece()&&grid[ranks][files-1].getPiece().istwospace())
                {
                    moves.add(grid[ranks-1][files-1]);
                }
            }
            else
            {
                if(files+1<=7&&grid[ranks][files+1].hasPiece()&&grid[ranks][files+1].getPiece().istwospace())
                {
                    moves.add(grid[ranks+1][files+1]);
                }
                if(files-1>=0&&grid[ranks][files-1].hasPiece()&&grid[ranks][files-1].getPiece().istwospace())
                {
                    moves.add(grid[ranks+1][files-1]);
                }
            }
        }
        
        for(int i = 0; i < moves.size();i++)//returns true if the move you are trying to make is in the list of possible moves
        {
        	// makes sure the move does not result in a check for the moving player
            if(moves.get(i).getLoc().equals(finLoc) && checkCheck(intLoc,moves.get(i).getLoc()))
            {
                return true;
                
            }
            
        }   
       }
        return false;
    }
    
    /**
     * checks if a player is in check
     * @param color: color the player to check
     * @return boolean
     */
    public boolean inCheck(String color)
    {
        ArrayList<Tiles> locs = new ArrayList();
        Tiles king = new Tiles();
        // loops through the board
        for(int rank = 0; rank<8;rank++)
        {
            for(int file = 0; file<=7;file++)
            {
                if(grid[rank][file].hasPiece()&&!grid[rank][file].getPiece().getColor().equals(color))
                {
                    locs.add(grid[rank][file]);// adds all opposing pieces to the arraylist
                }
                if(grid[rank][file].hasPiece()&&grid[rank][file].getPiece().getName().equals("King")&&grid[rank][file].getPiece().getColor().equals(color))//finds the king
                {
                    king = grid[rank][file];// stores king position
                }
            }
        }
        
        for(int i = 0; i <locs.size();i++)// checks if king is in check
        {
            ArrayList<Tiles> checks = movementLines(locs.get(i));// gets possible moves of opponents piece
            for(int j = 0; j<checks.size();j++)
            {      
            if(checks.get(j).equals(king))// checks if your king is in the line of fire
            {
                return true;
            }        
        }
        }
        return false;
    }
    
    /**
     * takes in a tile and returns an array of all possible legal moves the piece on 
     * that tile has.
     * @param intLoc: tile to check
     * @return ArrayList<Tiles>
     */
    public ArrayList<Tiles> movementLines(Tiles intLoc)// returns movement lines for a given peice
    {
    	// parses tile location from string
        int file = intLoc.getLoc().charAt(0)-97;
        int rank = Integer.parseInt(intLoc.getLoc().substring(1,2));
        
        ArrayList<Tiles> moves = new ArrayList();
        
        if(intLoc.hasPiece())// null pointer check
        {
        // check what type of piece it is	
        switch(intLoc.getPiece().getName())
        {
            case("Pawn"):  // adds possible moves for pawn            
                if(intLoc.getPiece().getColor().equals("White"))
                {
                    if(rank+1<8&&!grid[rank+1][file].hasPiece())
                    {
                         moves.add(grid[rank+1][file]);       
                    }
                    if(!intLoc.getPiece().hasMoved())
                    { 
                    
                        if(rank+2<8&&!grid[rank+2][file].hasPiece()&&!grid[rank+1][file].hasPiece())
                        {
                            moves.add(grid[rank+2][file]);                
                        }
                    }
                    if(rank+1<8&&file+1<=7&&grid[rank+1][file+1].hasPiece()&&grid[rank+1][file+1].getPiece().getColor().equals("Black"))
                    {
                        moves.add(grid[rank+1][file+1]);                
                    }
                    if(rank+1<8&&file-1>=0&&grid[rank+1][file-1].hasPiece()&&grid[rank+1][file-1].getPiece().getColor().equals("Black"))
                    {
                        moves.add(grid[rank+1][file-1]);
                                         
                    }
                }
                else
                {
                    if(rank-1>=0&&!grid[rank-1][file].hasPiece())
                    {
                         moves.add(grid[rank-1][file]);       
                    }
                    if(!intLoc.getPiece().hasMoved())
                    {  
                        if(rank-2>=0&&!grid[rank-2][file].hasPiece()&&!grid[rank-1][file].hasPiece())
                        {
                            moves.add(grid[rank-2][file]);
                                          
                        }
                    }
                    if(rank-1>=0&&file+1<=7&&grid[rank-1][file+1].hasPiece()&&grid[rank-1][file+1].getPiece().getColor().equals("White"))
                    {
                        moves.add(grid[rank-1][file+1]);                         
                    }
                    if(rank-1>=0&&file-1>=0&&grid[rank-1][file-1].hasPiece()&&grid[rank-1][file-1].getPiece().getColor().equals("White"))
                    {
                        moves.add(grid[rank-1][file-1]);
                                         
                    }
                
                }                
                break;
            case("Bishop"): // adds in possible moves for a bishop
            	
                int tempra = rank;
                int tempf = file;
                
                while(tempra-1>=0&&tempf-1>=0&&!grid[tempra-1][tempf-1].hasPiece())
                {
                    moves.add(grid[tempra-1][tempf-1]);
                    tempra--;
                    tempf--;               
                }
                if(tempra-1>=0&&tempf-1>=0&&!grid[tempra-1][tempf-1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra-1][tempf-1]);
                }
                tempf = file;
                tempra = rank;
                           
                while(tempra-1>=0&&tempf+1<=7&&!grid[tempra-1][tempf+1].hasPiece())
                {
                    moves.add(grid[tempra-1][tempf+1]);
                    tempra--;
                    tempf++;
                 
                
                }
                if(tempra-1>=0&&tempf+1<=7&&!grid[tempra-1][tempf+1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra-1][tempf+1]);
                }
                
                tempf = file;
                tempra = rank;
                while(tempra+1<8&&tempf+1<=7&&!grid[tempra+1][tempf+1].hasPiece())
                {
                    moves.add(grid[tempra+1][tempf+1]);
                    tempra++;
                    tempf++;                
                }
                if(tempra+1<8&&tempf+1<=7&&!grid[tempra+1][tempf+1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra+1][tempf+1]);
                }
             
                tempf = file;
                tempra = rank;
                
                while(tempra+1<8&&tempf-1>=0&&!grid[tempra+1][tempf-1].hasPiece())
                {
                    moves.add(grid[tempra+1][tempf-1]);
                    tempra++;
                    tempf--;                                 
                }
                if(tempra+1<8&&tempf-1>=0&&!grid[tempra+1][tempf-1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra+1][tempf-1]);
                }
                
                tempf = file;
                tempra = rank;
                break;               
            case("Rook"): // adds in possible moves for a rook
                          
                tempf = file;
                tempra = rank;
                
                while(tempra-1>=0&&!grid[tempra-1][file].hasPiece())
                {
                    moves.add(grid[tempra-1][file]);
                    tempra--;                 
                }
                
                if(tempra-1>=0&&!grid[tempra-1][file].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra-1][file]);
                }
                
                tempf = file;
                tempra = rank;
                
                while(tempf+1<=7&&!grid[rank][tempf+1].hasPiece())
                {
                    moves.add(grid[rank][tempf+1]);
                    tempf++;
                }
                if(tempf+1<=7&&!grid[rank][tempf+1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[rank][tempf+1]);
                }
             
                tempf = file;
                tempra = rank;
            
                while(tempra+1<8&&!grid[tempra+1][file].hasPiece())
                {
                    moves.add(grid[tempra+1][file]);
                    tempra++;               
                }
                if(tempra+1<8&&!grid[tempra+1][file].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra+1][file]);
                }
             
                tempf = file;
                tempra = rank;
                 
                while(tempf-1>=0&&!grid[rank][tempf-1].hasPiece())
                { 
                    moves.add(grid[rank][tempf-1]);
                    tempf--;
                }
                if(tempf-1>=0&&!grid[rank][tempf-1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[rank][tempf-1]);
                }
                break;
            case("Queen"): // add in posssble moves for a  queen

                tempf = file;
                tempra = rank;
            
                while(tempra-1>=0&&!grid[tempra-1][file].hasPiece())
                {
                    moves.add(grid[tempra-1][file]);
                    tempra--;             
                }
                if(tempra-1>=0&&!grid[tempra-1][file].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra-1][file]);
                }
                tempf = file;
                tempra = rank;
              
                while(tempf+1<=7&&!grid[rank][tempf+1].hasPiece())
                {
                    moves.add(grid[rank][tempf+1]);
                    tempf++;
                }
                if(tempf+1<=7&&!grid[rank][tempf+1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[rank][tempf+1]);
                }
              
                tempf = file;
                tempra = rank;
                
                while(tempra+1<8&&!grid[tempra+1][file].hasPiece())
                {
                    moves.add(grid[tempra+1][file]);
                    tempra++;
                }
                if(tempra+1<8&&!grid[tempra+1][file].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra+1][file]);
                }
              
                tempf = file;
                tempra = rank;
                
                while(tempf-1>=0&&!grid[rank][tempf-1].hasPiece())
                {
                    moves.add(grid[rank][tempf-1]);
                    tempf--;
                }
                if(tempf-1>=0&&!grid[rank][tempf-1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[rank][tempf-1]);
                }
              
                tempf = file;
                tempra = rank;
                
                while(tempra-1>=0&&tempf-1>=0&&!grid[tempra-1][tempf-1].hasPiece())
                {
                    moves.add(grid[tempra-1][tempf-1]);
                    tempra--;
                    tempf--;
                }
                if(tempra-1>=0&&tempf-1>=0&&!grid[tempra-1][tempf-1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra-1][tempf-1]);
                }
                
                tempf = file;
                tempra = rank;
             
                while(tempra-1>=0&&tempf+1<=7&&!grid[tempra-1][tempf+1].hasPiece())
                {
                    moves.add(grid[tempra-1][tempf+1]);
                    tempra--;
                    tempf++;
                }
                if(tempra-1>=0&&tempf+1<=7&&!grid[tempra-1][tempf+1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra-1][tempf+1]);
                }
             
                tempf = file;
                tempra = rank;
                
                while(tempra+1<8&&tempf+1<=7&&!grid[tempra+1][tempf+1].hasPiece())
                {
                    moves.add(grid[tempra+1][tempf+1]);
                    tempra++;
                    tempf++;
                }
                if(tempra+1<8&&tempf+1<=7&&!grid[tempra+1][tempf+1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra+1][tempf+1]);
                }
              
                tempf = file;
                tempra = rank;
                
                while(tempra+1<8&&tempf-1>=0&&!grid[tempra+1][tempf-1].hasPiece())
                {
                    moves.add(grid[tempra+1][tempf-1]);
                    tempra++;
                    tempf--;
                }
                if(tempra+1<8&&tempf-1>=0&&!grid[tempra+1][tempf-1].getPiece().getColor().equals(intLoc.getPiece().getColor()))
                {
                    moves.add(grid[tempra+1][tempf-1]);
                }
                
                tempf = file;
                tempra = rank;
                break;
            case("King"): // adds in possible moves for a king
 
                if(rank-1>=0&&file+1<=7&&(grid[rank-1][file+1].hasPiece()&&!grid[rank-1][file+1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank-1][file+1].hasPiece()))   
                {
                    moves.add(grid[rank-1][file+1]);
                }
                if(rank+1<8&&file+1<=7&&(grid[rank+1][file+1].hasPiece()&&!grid[rank+1][file+1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+1][file+1].hasPiece()))
                {
                    moves.add(grid[rank+1][file+1]);
                }
                if(rank+1<8&&file-1>=0&&(grid[rank+1][file-1].hasPiece()&&!grid[rank+1][file-1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+1][file-1].hasPiece()))  
                {
                    moves.add(grid[rank+1][file-1]);
                }
                if(rank-1>=0&&file-1>=0&&(grid[rank-1][file-1].hasPiece()&&!grid[rank-1][file-1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank-1][file-1].hasPiece()))    
                {
                    moves.add(grid[rank-1][file-1]); 
                }
                if(rank-1>=0&&(grid[rank-1][file].hasPiece()&&!grid[rank-1][file].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank-1][file].hasPiece()))  
                {
                    moves.add(grid[rank-1][file]);
                }
                if(rank+1<8&&(grid[rank+1][file].hasPiece()&&!grid[rank+1][file].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+1][file].hasPiece()))  
                {
                    moves.add(grid[rank+1][file]);
                }
                if(file+1<=7&&(grid[rank][file+1].hasPiece()&&!grid[rank][file+1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank][file+1].hasPiece()))    
                {
                    moves.add(grid[rank][file+1]);
                }
                if(file-1>=0&&(grid[rank][file-1].hasPiece()&&!grid[rank][file-1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank][file-1].hasPiece()))  
                {
                    moves.add(grid[rank][file-1]);
                }
                
                //castling
                if(!intLoc.getPiece().hasMoved())
                {
                    if(intLoc.getPiece().getColor().equals("White"))
                    {
                        if(!grid[0][1].hasPiece()&&!grid[0][2].hasPiece()&&!grid[0][3].hasPiece()&&grid[0][0].hasPiece()&&!grid[0][0].getPiece().hasMoved())
                        {
                            moves.add(grid[0][2]);
                        }
                        if(!grid[0][5].hasPiece()&&!grid[0][6].hasPiece()&&grid[0][7].hasPiece()&&!grid[0][7].getPiece().hasMoved())
                        {
                            moves.add(grid[0][6]);
                        }
                    }
                    else
                    {
                        if(!grid[7][1].hasPiece()&&!grid[7][2].hasPiece()&&!grid[7][3].hasPiece()&&grid[7][0].hasPiece()&&!grid[7][0].getPiece().hasMoved())
                        {
                            moves.add(grid[7][2]);
                        }
                        if(!grid[7][5].hasPiece()&&!grid[7][6].hasPiece()&&grid[7][7].hasPiece()&&!grid[7][7].getPiece().hasMoved())
                        {
                            moves.add(grid[7][6]);
                        }
                    }
                }
                break;
            case("Knight"):// add possible moves for a knight
         
                if(rank-1>=0&&file+2<=7&&(grid[rank-1][file+2].hasPiece()&&!grid[rank-1][file+2].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank-1][file+2].hasPiece()))   
                {
                    moves.add(grid[rank-1][file+2]);
                }
                if(rank-1>=0&&file-2>=0&&(grid[rank-1][file-2].hasPiece()&&!grid[rank-1][file-2].getPiece().getColor().equals(intLoc.getPiece().getColor())|| !grid[rank-1][file-2].hasPiece()))
                {
                    moves.add(grid[rank-1][file-2]); 
                }
                if(rank+1<8&&file+2<=7&&(grid[rank+1][file+2].hasPiece()&&!grid[rank+1][file+2].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+1][file+2].hasPiece()))   
                {
                    moves.add(grid[rank+1][file+2]); 
                }
                if(rank+1<8&&file-2>=0&&(grid[rank+1][file-2].hasPiece()&&!grid[rank+1][file-2].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+1][file-2].hasPiece()))    
                {
                    moves.add(grid[rank+1][file-2]); 
                }
                if(rank+2<8&&file-1>=0&&(grid[rank+2][file-1].hasPiece()&&!grid[rank+2][file-1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+2][file-1].hasPiece())) 
                {
                    moves.add(grid[rank+2][file-1]);
                }
                if(rank+2<8&&file+1<=7&&(grid[rank+2][file+1].hasPiece()&&!grid[rank+2][file+1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank+2][file+1].hasPiece()))   
                {
                    moves.add(grid[rank+2][file+1]);
                }
                if(rank-2>=0&&file+1<=7&&(grid[rank-2][file+1].hasPiece()&&!grid[rank-2][file+1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank-2][file+1].hasPiece()))    
                {
                    moves.add(grid[rank-2][file+1]);
                }
                if(rank-2>=0&&file-1>=0&&(grid[rank-2][file-1].hasPiece()&&!grid[rank-2][file-1].getPiece().getColor().equals(intLoc.getPiece().getColor())||!grid[rank-2][file-1].hasPiece()))    
                {
                    moves.add(grid[rank-2][file-1]);
                }
            }
        }
        return moves;
    }
    
    /**
     * checks if a given move results in the moving player being in check
     * @param in: initial location
     * @param fin// final location
     * @return
     */
    public boolean checkCheck(String in, String fin)
    {
        Tiles[][] temp = grid;
        
        // parses location from string
        int file = in.charAt(0)-97;
        int rank = Integer.parseInt(in.substring(1,2));
        int files = fin.charAt(0)-97;
        int ranks = Integer.parseInt(fin.substring(1,2));
        
        Piece tempp = new Piece("White","Pawn");
        
        if(!grid[rank][file].hasPiece()) return false;// null pointer check
        
        // makes the move on a temporary board
        tempp = grid[ranks][files].getPiece();
        temp[ranks][files].setPiece(temp[rank][file].getPiece());
        temp[rank][file].removePiece();// makes the move
        Board tempb = new Board(temp);
        
        if(grid[ranks][files].hasPiece()&&tempb.inCheck(grid[ranks][files].getPiece().getColor()))
        {
            grid[rank][file].setPiece(grid[ranks][files].getPiece());// puts the piece back
            grid[ranks][files].setPiece(tempp);
            return false;// returns false if it results in a check
        }
        grid[rank][file].setPiece(grid[ranks][files].getPiece());// puts the piece back
        grid[ranks][files].setPiece(tempp);
        return true;
    }
    /**
     * returns a tile given the location
     * @param Location: location of desired tile
     * @return Tiles
     */
    public Tiles getTile(String Location)
    {
        int file = Location.charAt(0)-97;
        int rank = Integer.parseInt(Location.substring(1,2));
        return grid[rank][file];
    }
    /**
     * prints board for testing before GUI
     */
    public void print()
    {
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j <8; j++)
            {
                if(grid[i][j].hasPiece())
                System.out.print(grid[i][j].getPiece().getName());
                else System.out.print("_");
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    
    /**
     * returns the grid
     * @return Tiles[][]
     */
    public Tiles[][] getGrid()
    {
        return grid;
    }
    
    /**
     * checks if the game is over and returns a string 
     * @param color: color of player turn
     * @return String
     */
    public String gameOver(String color)
    {
        ArrayList<Tiles> pieces = new ArrayList();
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(grid[i][j].hasPiece() && grid[i][j].getPiece().getColor().equals(color))
                {
                    pieces.add(grid[i][j]);
                }                              
            }             
        }
        ArrayList<Tiles> temp = new ArrayList();
        for(int i = 0; i < pieces.size(); i++)
        {
            temp = movementLines(pieces.get(i));
            for(int j = 0; j <temp.size(); j++)// removes any moves from the array that resault in check
            { 
                if(!checkCheck(pieces.get(i).getLoc(),temp.get(j).getLoc()))
                {
                    temp.remove(j);
                    j--;
                }                         
            }
            if(temp.size() > 0) return "continue";
        }
        if(inCheck(color)) return "loses";
        return "draws";
    }
    
    // **Begin Position Evaluation Methods**
    
    /**
     * returns all possible moves for a piece on a given tile
     * @param loc: tile to be checked
     * @return ArrayList<Tiles>
     */
    public ArrayList<Tiles> possibleMoves(Tiles loc)
    {
        ArrayList<Tiles> moves = movementLines(loc);
        for(int i = 0; i <moves.size(); i++)// removes any moves from the array that result in check
        { 
            if(!checkCheck(loc.getLoc(),moves.get(i).getLoc()))
            {
            	moves.remove(i);
                i--;
            }                         
        }
        return moves;        
    }
    
    /**
     * returns the number of moves a given piece has
     * @param loc: tile of piece
     * @return
     */
    public int numMoves(Tiles loc)
    {
        return possibleMoves(loc).size();        
    }
   
    /**
     * returns an evaluation of a given position
     * @param b: position to be evaluated
     * @return double
     */
    public double positionEval(Board b)
    {
    	Rv = 5;
        Qv = 9;
        Pv = 1; 
        double sum = sumValue(b); // raw value of all pieces on board
        double value = 0;
        // changes the value of the bishop knight pawn and passed pawn based on number of
        // pieces left on board
        if(sum > 68) 
        {
            Bv = 2.8;
            Kv = 3.2;
            value += kingProtected(b);
            //value += passedPawns(b,.2);
            //value += canCastle(b);
        }
        else if(sum > 58)
        {
            Bv = 2.9;
            Kv = 3.1;
            value += kingProtected(b);
            //value += passedPawns(b,.3);
            //value += canCastle(b);
        }
        else if(sum > 48)
        {
            Bv = 3.0;
            Kv = 3.0;
            value += kingProtected(b);
            //value += passedPawns(b,.4);
            //value += canCastle(b);
        }
        else if(sum > 38)
        {
            Bv = 3.1;
            Kv = 2.9;
            Pv = 1.1;
            value += kingProtected(b);
            //value += passedPawns(b,.5);
            //value += canCastle(b);
        }
        else if(sum > 28)
        {
            Bv = 3.2;
            Kv = 2.8;
            Pv = 1.2;
            //value += passedPawns(b,.6);
            value += kingProtected(b);
            //value += canCastle(b);
        }
        else if(sum > 18)
        {
            Bv = 3.3;
            Kv = 2.7;
            Pv = 1.3;
            //value += passedPawns(b,.7);
        }
        else if(sum > 8)
        {
            Bv = 3.4;
            Kv = 2.7;
            Pv = 1.4;
            //value += passedPawns(b,.8);
        }
        else
        {
            Bv = 3.4;
            Kv = 2.7;
            Pv = 1.5;
            //value += passedPawns(b,.9);
        }
        
        // adds on values for all other position checks
        value += pieceValue(b);
        //value += moveValue(b);
        value += doubledPawns(b);
        //value += blockedPawns(b);
        //value += isolatedPawns(b);
        value += gameOverD(b);
        
        return value;
    }
    
    /**
     * checks the number of doubled pawns each player has and returns a value to this
     * specific part of the position.
     * @param b: board to be analyzed
     * @return double
     */
    public double doubledPawns(Board b)
    {
        double value = 0;
        Tiles[][] board = b.getGrid();
        for(int file = 0; file<8; file++)
        {
            int numW = 0;
            int numB = 0;
            for(int rank = 0; rank < 8; rank++)
            {
                if(board[rank][file].hasPiece()&&board[rank][file].getPiece().getName().equals("Pawn"))
                {
                    if(board[rank][file].getPiece().getColor().equals("White"))
                    {
                        numW++;
                    }
                    else
                    {
                        numB++;
                    }
                }
            }
            value -= numW * .25;
            value =+ numB * .25;
            
        }
        return value;
    }
    
    /**
     * returns a value based on the total number of moves each player has available
     * @param b: board to be analyzed
     * @return double
     */
    public double moveValue(Board b)
    {
        double value = 0;
        Tiles[][] board = b.getGrid();
        value -= allPossibleMoves(b, "Black").size()*.01;
        value += allPossibleMoves(b, "White").size() *.01;
        return value;
    }
    
    /**
     * compares the values of all the pieces on the board and assigns a material
     * value to the position
     * @param b: board to be analyzed
     * @return double
     */
    public double pieceValue(Board b)
    {
        double value = 0;
        Tiles[][] board = b.getGrid();
        for(int i = 0;i<8;i++)
        {
            for(int j = 0;j<8;j++)
            {
                if(board[i][j].hasPiece()&&board[i][j].getPiece().getColor().equals("Black"))
                {
                    Piece temp = board[i][j].getPiece();
                    if(temp.getName().equals("Pawn"))
                    {
                        value-= Pv;
                    }
                    if(temp.getName().equals("Rook"))
                    {
                        value-=Rv;
                    }
                    if(temp.getName().equals("Knight"))
                    {
                        value-=Kv;
                    }
                    if(temp.getName().equals("Bishop"))
                    {
                        value-=Bv;
                    }
                    if(temp.getName().equals("Queen"))
                    {
                        value-=Qv;
                    }
                }
                else
                {
                    if(board[i][j].hasPiece())
                    {
                        Piece temp = board[i][j].getPiece();
                        if(temp.getName().equals("Pawn"))
                    {
                        value+=Pv;
                    }
                    if(temp.getName().equals("Rook"))
                    {
                        value+=Rv;
                    }
                    if(temp.getName().equals("Knight"))
                    {
                        value+=Kv;
                    }
                    if(temp.getName().equals("Bishop"))
                    {
                        value+=Bv;
                    }
                    if(temp.getName().equals("Queen"))
                    {
                        value+=Qv;
                    }
                    }
                }
            }
        }
        return value;
    }
    
    /**
     * checks if any pawns are blocked and assigns a value based on this
     * specific part of the position.
     * @param b: board to be analyzed
     * @return double
     */
    public double blockedPawns(Board b)
    {
        Tiles[][] board = b.getGrid();
        double value = 0;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j<8; j++)
            {
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Pawn"))
                {
                    if(board[i][j].getPiece().getColor().equals("White"))
                    {
                        if(numMoves(board[i][j]) == 0)
                        {
                            value -= .25;
                        }
                    }
                    else
                    {
                        if(numMoves(board[i][j]) ==0)
                        {
                            value += .25;
                        }
                    }
                }
            }            
        }
        return value;
    }
    
    /**
     * returns a value based on the number of passed pawns
     * @param b: board to be analyzed
     * @param v: current value of a passed pawn
     * @return
     */
    public double passedPawns(Board b, double v)
    {
        Tiles[][] board = b.getGrid();
        double value = 0;
        for(int i = 0; i < 8; i++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Pawn"))
                {
                    if(board[i][j].getPiece().getColor().equals("White"))
                    {
                        if(i>4) value += Pv;
                    }
                    else
                    {
                        if(i<5) value -= Pv;                     
                    }
                }
            }    
        }
        return value;
    }
    
    /**
     * returns a massive value if a player has won the game
     * @param b: board to be analyzed
     * @return
     */
    public double gameOverD(Board b)
    {
        if(b.gameOver("White").equals("loses"))
        {
            return -100;
        }
        if(b.gameOver("Black").equals("loses"))
        {
            return 100;
        }
        return 0;
    }
    
    /**
     * Determines whether each sides king is protected and returns a value based
     * on this aspect of the position
     * @param b: position to be analyzed
     * @return double
     */
    public double kingProtected(Board b)
    {
        int wRank = 0;
        int wfile = 0;
        int bfile = 0;
        int bRank = 0;
        Tiles[][] board = b.getGrid();
        double value = 0;
        
        // find black and white king position
        for(int i = 'a'; i < 'i'; i++)
        {
            for(int j =0; j <8; j++)
            {
                if(board[i-97][j].hasPiece() && board[i-97][j].getPiece().getName().equals("King"))
                {
                    if(board[i-97][j].getPiece().getColor().equals("Black"))
                    {
                        bfile = j;
                        bRank = i-97;
                    }
                    else                    
                    {
                        wfile = j;
                        wRank = i-97;
                    }
                }                
            }
        }
        
        int bcount = 0;
        int wcount = 0;
        
        for(int i = 0; i < bRank; i++)//black pawns in front of king
        {
  
            if(board[i][bfile].hasPiece() && board[i][bfile].getPiece().getName().equals("Pawn") && board[i][bfile].getPiece().getColor().equals("Black"))
            {
             bcount++;
            }
        }
        for(int i = 7; i >wRank; i--)//white pawns in font of king
        {
            if(board[i][wfile].hasPiece() && board[i][wfile].getPiece().getName().equals("Pawn") && board[i][wfile].getPiece().getColor().equals("White"))
            {
            wcount++;
        }
        }
        
        if(bcount==0)
        {
            value+=.5;
        }
        if(wcount==0)
        {
            value-=.5;
        }
        return value;      
    }
    
    /**
     * checks the board for instances of isolated pawns and returns a value corresponding
     * to this aspect of the position
     * @param b: board to be analyzed
     * @return double
     */
    public double isolatedPawns(Board b)
    {
        int bl = 0;
        int wh = 0;
        Tiles[][] board = b.getGrid();
        int count = 0;
        
        for(int i =0;i<8;i++)//rank
        {
            for(int j =0;j<8;j++)//file
            {
                if(board[i][j].hasPiece())
                {
                    Piece temp = board[i][j].getPiece();
                    
                    // check for black isolated pawns
                    if(temp.getColor().equals("Black")&&temp.getName().equals("Pawn"))
                    {
                    	// if the pawn being examined is at the left edge
                        if(j==0)
                        {    
                            for(int a = 0;a<8;a++)
                            {
                                if(board[a][j+1].hasPiece()&&board[a][j+1].getPiece().getName().equals("Pawn")&&board[a][j+1].getPiece().getColor().equals("Black"))
                                {
                                    count++;
                                }
                            }
                            if(count==0)
                            {
                                bl++;
                            }
                            count=0;
                        }
                        
                        // if the pawn being examined is at the right edge
                        if(j==7)
                        {
                            for(int a = 0;a<8;a++)
                            {
                                if(board[a][j-1].hasPiece()&&board[a][j-1].getPiece().getName().equals("Pawn")&&board[a][j-1].getPiece().getColor().equals("Black"))
                                {
                                    count++;
                                }
                            }
                            if(count==0)
                            {
                                bl++;
                            }
                            count=0;
                        }
                        
                        // for pawns not on the edge
                        if(j!=7&&j!=0)
                        {
                            for(int a =0;a<8;a++)
                            {
                                if(board[a][j-1].hasPiece()&&board[a][j-1].getPiece().getName().equals("Pawn")&&board[a][j-1].getPiece().getColor().equals("Black"))
                                {
                                    count++;
                                }
                                if(board[a][j+1].hasPiece()&&board[a][j+1].getPiece().getName().equals("Pawn")&&board[a][j+1].getPiece().getColor().equals("Black"))
                                {
                                    count++;
                                }
                            }
                            if(count==0)
                            {
                                bl++;
                            }
                            count=0;
                        }
                    }
                    
                    //check white isolated pawns
                    if(temp.getName().equals("Pawn")&&temp.getColor().equals("White"))
                    {
                    	// for pawns on the left edge
                        if(j==0)
                        {
                            for(int a = 0;a<8;a++)
                            {
                                if(board[a][j+1].hasPiece()&&board[a][j+1].getPiece().getName().equals("Pawn")&&board[a][j+1].getPiece().getColor().equals("White"))
                                {
                                    count++;
                                }
                            }
                            if(count==0)
                            {
                                wh++;
                            }
                            count=0;
                        }
                        
                        // for pawns on the right edge
                        if(j==7)
                        {
                            for(int a = 0;a<8;a++)
                            {
                                if(board[a][j-1].hasPiece()&&board[a][j-1].getPiece().getName().equals("Pawn")&&board[a][j-1].getPiece().getColor().equals("White"))
                                {
                                    count++;
                                }
                            }
                            if(count==0)
                            {
                                wh++;
                            }
                            count=0;
                        }
                        
                        // for pawns not on the edges
                        if(j!=7&&j!=0)
                        {
                            for(int a =0;a<8;a++)
                            {
                                if(board[a][j-1].hasPiece()&&board[a][j-1].getPiece().getName().equals("Pawn")&&board[a][j-1].getPiece().getColor().equals("White"))
                                {
                                    count++;
                                }
                                if(board[a][j+1].hasPiece()&&board[a][j+1].getPiece().getName().equals("Pawn")&&board[a][j+1].getPiece().getColor().equals("White"))
                                {
                                    count++;
                                }
                            }
                            if(count==0)
                            {
                                wh++;
                            }
                            count=0;
                        }
                    }  
                }
            }
        }
        
        // assign value based on number of isolated pawns
        double out = 0;
        out+=bl*.25;
        out-=wh*.25;
        return out;
    }
    
    /**
     * sums the material value of all pieces on the board
     * @param b: position to be analyzed
     * @return double
     */
    public double sumValue(Board b)
    {
        Tiles[][] board= b.getGrid();
        double sum = 0;
        
        for(int i = 0; i < 8; i++)
        {
            for( int j = 0; j < 8; j++)
            {
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Pawn"))
                {
                    sum += Pv;                    
                }
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Queen"))
                {
                    sum += Qv;                    
                }
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Rook"))
                {
                    sum += Rv;                    
                }
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Knight"))
                {
                    sum += Kv;                    
                }
                if(board[i][j].hasPiece() && board[i][j].getPiece().getName().equals("Bishop"))
                {
                    sum += Bv;                    
                }
            }
        }
        return sum;
    }
    
    /**
     * checks if each side can castle and returns a value based on this aspect
     * of the position.
     * @param b: board to be analyzed
     * @return double
     */
    public double canCastle(Board b)
    {
        double out =0;
        Tiles[][] board = b.getGrid();
        // check if the white king can still castle
        if(board[0][4].hasPiece()&&board[0][4].getPiece().hasMoved()==false)
        {
            if(board[0][0].hasPiece()&&board[0][0].getPiece().hasMoved()==false)
            {
                out+=.25;
            }
            if(board[0][7].hasPiece()&&board[0][7].getPiece().hasMoved()==false)
            {
                out+=.25;
            }
        }
        // check if the black king can still castle
        if(board[7][4].hasPiece()&&board[7][4].getPiece().hasMoved()==false)
        {
            if(board[7][0].hasPiece()&&board[7][0].getPiece().hasMoved()==false)
            {
                out-=.25;
            }
            if(board[7][7].hasPiece()&&board[7][7].getPiece().hasMoved()==false)
            {
                out-=.25;
            }
        }
        return out;
    }
    
    /**
     * return all possible moves made on temporary boards in an array
     * @param b: current position
     * @param Color: current turn
     * @return ArrayList<Board>
     */
    public ArrayList<Board> allPossibleMoves(Board b,String Color)
    {
        Tiles[][] board = b.getGrid();
        ArrayList<Board> moves = new ArrayList();
        ArrayList<Tiles> tempmoves = new ArrayList();  
        
         for(int i = 0; i < 8; i ++)
        {
            for(int j = 0; j < 8; j++)
            {
                if(board[i][j].hasPiece() && board[i][j].getPiece().getColor().equals(Color))
                {
                	// find each piece of the right color and get all of its possible  moves
                    tempmoves = possibleMoves(board[i][j]);
                    
                    //create new board
                    for(int a = 0; a < tempmoves.size(); a++)
                    {
                        Tiles[][] temp = new Tiles[8][8];
                        for(int c = 0; c < 8; c++)
                        {
                            for(int d = 0; d <8; d++)// you must remake each tile in the array
                            {
                                temp[c][d] = new Tiles();
                                if(board[c][d].hasPiece())
                                {                   
                                    temp[c][d].setPiece(new Piece(board[c][d].getPiece()));
                                }
                                temp[c][d].setWhite(board[c][d].isWhite());
                                temp[c][d].setLoc(board[c][d].getLoc());
                            }
                        }
                        Board temps = new Board(temp);
                        //all moves are already possible no need to check again
                        temps.moveWithoutCheck(temp[i][j].getLoc(),tempmoves.get(a).getLoc());
                        // add the boards to the array
                        moves.add(temps);
                    }
                }
            }
        }
        return moves;
    }
    
    /**
     * moves without checking for ilegal moves
     * @param intLoc: initial location
     * @param finLoc: final location
     */
    public void moveWithoutCheck(String intLoc, String finLoc)
    {
    	//parse location from string
        int file = intLoc.charAt(0)-97;//converts from ascii
        int rank = Integer.parseInt(intLoc.substring(1,2));
        
        // for special case of castling
        boolean castled = false;
        if(grid[rank][file].hasPiece())
        {
            int distance = finLoc.charAt(0)-intLoc.charAt(0);
            if(grid[rank][file].getPiece().getName().equals("King"))
            {
                if(distance==-2||distance==2)
                {
                    Piece tempKing = grid[rank][file].removePiece();
                    file = finLoc.charAt(0)-97;
                    rank = Integer.parseInt(finLoc.substring(1,2));
                    grid[rank][file].setPiece(tempKing);
                    if(distance==-2)
                    {
                        Piece tempRook = grid[rank][file-2].removePiece();
                        int rfile = finLoc.charAt(0)-96;
                        grid[rank][rfile].setPiece(tempRook);
                        castled = true;
                    }
                    if(distance==2)
                    {
                        Piece tempRook = grid[rank][file+1].removePiece();
                        int rfile = finLoc.charAt(0)-98;
                        grid[rank][rfile].setPiece(tempRook);
                        castled = true;
                    }
                }
            }
            // for all other cases
            if(!castled)
            {
            Piece temp = grid[rank][file].removePiece();
            
            // parse final location from string
            file = finLoc.charAt(0)-97;
            rank = Integer.parseInt(finLoc.substring(1,2));
            
            grid[rank][file].removePiece();
            grid[rank][file].setPiece(temp);
            
            //special case for queening
            if(temp.getName().equals("Pawn")&&(rank == 7 || rank == 0)) temp.setName("Queen");
            
            // en passant special case
            grid[rank][file].getPiece().twospacefalse();
            int vdist = Integer.parseInt(finLoc.substring(1,2))-Integer.parseInt(intLoc.substring(1,2));
            if(grid[rank][file].getPiece().getName().equals("Pawn")&&vdist==2||vdist==-2)
            {
                grid[rank][file].getPiece().twospacetrue();
            }
            if(temp.getName().equals("Pawn"))
            {
                if(temp.getColor().equals("Black"))
                {
                    if(grid[rank+1][file].hasPiece()&&grid[rank+1][file].getPiece().getColor().equals("White")&&grid[rank+1][file].getPiece().istwospace())
                    {
                        grid[rank+1][file].removePiece();
                    }
                }
                else
                {
                    if(grid[rank-1][file].hasPiece()&&grid[rank-1][file].getPiece().getColor().equals("Black")&&grid[rank-1][file].getPiece().istwospace())
                    {
                        grid[rank-1][file].removePiece();
                    }
                }
            }
            }        
       }
    }
    
    //**Engine**
    
    /**
     * initializes board for engine 
     * @param b: board to be initialized
     * @param color: color the engine is playing
     */
    public void initialize(Board b, String color)
    {
    	// sets up the root of the minimax tree
        if(color.equals("White"))
        {
        GameState temp = new GameState(true, b, -200);
        root = new DefaultMutableTreeNode(temp);
       }
        else
        {
        GameState temp = new GameState(false, b, 200);
        root = new DefaultMutableTreeNode(temp);
        }   
    }
    
    /**
     * builds and evaluates down the tree bringing the numbers up to the parent nodes according
     * to the minimax algorithm. Keeps track of current best move for black and white on the path
     * to the root. This allows for the elimination of certain moves after better moves have already
     * been found. 
     * 
     * @param node: node to begin evaluation at
     * @param depth: ply of evaluation
     * @param alpha: best current move for white
     * @param beta:best current move for black
     * @param maximizingPlayer: determines which player is playing as the maximizer
     * @return double
     */
    public double AlphaBeta(DefaultMutableTreeNode node, int depth, double alpha, double beta, boolean maximizingPlayer)
    {
        if(depth == 0)
        {
            double x = ((GameState)node.getUserObject()).getBoard().positionEval(((GameState)node.getUserObject()).getBoard());
            ((GameState)node.getUserObject()).setValue(x);
            return x;
        }
        String color = "";
        
        // White is always the maximizing player
        if(maximizingPlayer) color = "White";
        else color = "Black";
        
        // list of all possible moves for the given node
        ArrayList<Board> x = ((GameState)node.getUserObject()).getBoard().allPossibleMoves(((GameState)node.getUserObject()).getBoard(), color);
        // create new nodes for each of the possible moves
        if(maximizingPlayer)
        {
        for(int i =0; i < x.size(); i++)
        {
            node.add(new DefaultMutableTreeNode( new GameState(!maximizingPlayer,x.get(i),200)));
        }
        }
        else
        {
            for(int i =0; i < x.size(); i++)
        {
            node.add(new DefaultMutableTreeNode( new GameState(maximizingPlayer,x.get(i),-200)));
        }
        }
        
        // if the node is at the bottom of the tree evaluate the position and return the evaluation
        if(node.isLeaf()) 
        {
            double t = ((GameState)node.getUserObject()).getBoard().positionEval(((GameState)node.getUserObject()).getBoard());
            ((GameState)node.getUserObject()).setValue(t);
            return t;
        }
        
        if(maximizingPlayer)
        {
        	// start evaluation at worst possible eval for node
            ((GameState)node.getUserObject()).setValue(-200);
            for( int i = 0; i < x.size(); i++)
            {
                ((GameState)node.getUserObject()).setValue(max(((GameState)node.getUserObject()).getValue(),AlphaBeta((DefaultMutableTreeNode)node.getChildAt(0), depth-1, alpha, beta, false)));                
                alpha = max(alpha,((GameState)node.getUserObject()).getValue());
                node.remove(0);// for memory efficiency
                if(beta <= alpha) break;// alpha beta pruning
            }
            return ((GameState)node.getUserObject()).getValue();
        }
        else
        {
            ((GameState)node.getUserObject()).setValue(200);
            for( int i = 0; i < x.size(); i++)
            { 
                ((GameState)node.getUserObject()).setValue(min(((GameState)node.getUserObject()).getValue(),AlphaBeta((DefaultMutableTreeNode)node.getChildAt(0), depth-1, alpha, beta, true)));
                beta = min(beta,((GameState)node.getUserObject()).getValue());
                node.remove(0);// for memory efficiency
                if(beta <= alpha) break;// alpha beta pruning
            }
            return ((GameState)node.getUserObject()).getValue();
        }
    }
    
    /**
     * creates the first row under the root of the tree. These nodes are not eliminated so
     * that the correct move is saved
     * @param b: current position
     * @param color: current turn
     * @return double
     */
    public double AlphaBetaHelper(Board b, String color)
    {
        ArrayList<Board> x = allPossibleMoves(b, color);
        double mavalue = -200;
        double mivalue = 200;
        //create first row of tree under root
        for(int i =0; i < x.size(); i++)
        {
            if(color.equals("Black"))
            root.add(new DefaultMutableTreeNode( new GameState(true,x.get(i),200)));
            else
            root.add(new DefaultMutableTreeNode( new GameState(false,x.get(i),200)));
            
            // run the evaluation for each node
            if(color.equals("White"))
            ((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).setValue(AlphaBeta(((DefaultMutableTreeNode)root.getChildAt(i)), 3, -200,200,false));
            else
             ((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).setValue(AlphaBeta(((DefaultMutableTreeNode)root.getChildAt(i)), 3, -200,200,true));
        }
        // get the highest evaluation for the engine player
        for(int i = 0; i < root.getChildCount(); i++)
        {
            if(color.equals("White"))
            {
                if(((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).getValue() > mavalue)
                {
                    mavalue = ((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).getValue();
                }           
            }
            else
            {
                if(((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).getValue() < mivalue)
                {
                    mivalue = ((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).getValue();
                }
            }
            ((DefaultMutableTreeNode)root.getChildAt(i)).removeAllChildren();// for memory efficiency
        }
        // return the evaluation
        if(((GameState)root.getUserObject()).isMax())
        {
            return mavalue;
        }
        else
        {
            return mivalue;
        }
    }
    
    /**
     * uses alphabeta helper to find the correct move and returns the string representation
     * of that move
     * @param b: current position
     * @param color: engine color
     * @return String
     */
    public String getMove(Board b ,String color)
    {
    	// runs the evaluation
        initialize(b,color);
        double value = 0;
        if(color.equals("White"))
        {
            value = AlphaBetaHelper(b, color);
        }
        else
        {
            value = AlphaBetaHelper(b,color);
        }
        
        // find the node that matches the evaluation
        double matest = -200;
        double mitest = 200;
        DefaultMutableTreeNode move = new DefaultMutableTreeNode();
        for(int i = 0; i < root.getChildCount(); i ++)
        {    
             if(((GameState)((DefaultMutableTreeNode)root.getChildAt(i)).getUserObject()).getValue() == value)
             {
                   move =((DefaultMutableTreeNode) root.getChildAt(i));
                   //if moves have the same value it selects a random move
                   Random rand = new Random();
                   double randD = rand.nextDouble();
                   if(randD >= .5) 
                   {
                	   break;
                   }           
             }            
        }
        
        // set up the board with the new move
        Board temp = ((GameState)move.getUserObject()).getBoard();
        Tiles[][] ogrid = b.getGrid();
        Tiles[][] ngrid = temp.getGrid();
        temp.print();
        String in = "";
        Piece p = null;
        String fin = "";
        String kloc = "";
        int count = 0;
        Piece isKing = null;
        Piece isRook = null;
        
        // gets the move as a string by comparing the 2 boards
        for(int i = 0;i<8;i++)
        {
            for(int j = 0;j<8;j++)
            {
                if(ogrid[i][j].hasPiece()&&ogrid[i][j].getPiece().getColor().equals(color)&&!ngrid[i][j].hasPiece())
                {
                    in = ogrid[i][j].getLoc();
                    p = ogrid[i][j].getPiece();
                    count++;
                    if(p.getName().equals("King"))
                    {
                        isKing = p;
                        kloc = ogrid[i][j].getLoc();
                    }
                    if(p.getName().equals("Rook"))
                    {
                        isRook = p;
                    }
                }
                
            }
        }
        for(int i = 0;i<8;i++)
        {
            for(int j = 0;j<8;j++)
            {
                if(ngrid[i][j].hasPiece()&&ngrid[i][j].getPiece().equals(p) && canMove(in,ngrid[i][j].getLoc()) )
                {
                    fin = ngrid[i][j].getLoc();
                    if((isKing!=null&&isRook!=null)&&ngrid[i][j].getPiece().getName().equals("King"))
                    {
                        fin = ngrid[i][j].getLoc();
                        return kloc+fin;
                    }
                }
            }
        }
        return in+fin;
    }
    
    //**Some Helpful Methods**
    
    public double max(double one, double two)
    {
        if(one > two) return one;
        else return two;
    }
    public double min(double one, double two)
    {
        if(one > two) return two;
        else return one;
    }
    public DefaultMutableTreeNode getRoot()
    {
        return root;
    }
    
}