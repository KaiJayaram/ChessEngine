/*
 * Class Main
 * Runs the GUI and all interaction with the player.
 * Kailash Jayaram
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.util.*;

public class Main implements ActionListener, Runnable
{
	// Declare unicode vars for pieces
   static final char BLACK_PAWN = '\u265F';
   static final char BLACK_ROOK = '\u265C';
   static final char BLACK_KNIGHT = '\u265E';
   static final char BLACK_BISHOP = '\u265D';
   static final char BLACK_QUEEN = '\u265B';
   static final char BLACK_KING = '\u265A';
   static final char WHITE_PAWN = '\u2659';
   static final char WHITE_ROOK = '\u2656';
   static final char WHITE_KNIGHT = '\u2658';
   static final char WHITE_BISHOP = '\u2657';
   static final char WHITE_QUEEN = '\u2655';
   static final char WHITE_KING = '\u2654';
   
   // class variables
   static private Tile[][] board;
   static private Board b;
   private String turn;
   private String selectedTile;
   private String engineTurn;
   private ColorSelection c;
   
   /**
    * run method creates a new board
    * @param args
    */
   public static void main(String[] args) 
   {
	   new Main();   
	   
   }
   
   /**
    * initializes the board
    * @param args
    */
   public Main() 
   {
	   // initializes the gui with peices in starting position
       GUI gui = new GUI();
       board = new Tile[8][8];
       int count = 0;
       for (int rank = -1; rank < 8; rank++, count++){
           for (char file = 'a'-1; file <= 'h'; file++) {
        	   // set border colors and labels
               if(rank == -1)
               {
                   if(file == 'a' - 1)
                   gui.mainframe.add(new Tile(Character.toString('\u03A6'), Color.RED, false, this).lbl);
                   if(file>='a')
                   {
                       gui.mainframe.add(new Tile(Character.toString(file), Color.RED, false, this).lbl);
                   }                    
               }
               // more border colors and labels
               if(file == 'a' -1)
               {
                   if(rank > -1)
                   {
                       gui.mainframe.add(new Tile(Integer.toString(rank+1),Color.RED, false, this).lbl);                    
                   }  
               }
               if(file> 'a' -1 && rank > -1)
               {          
            	   // initialize inner board
                   int files = file-97;
                   if ((file + rank) % 2 == 0) 
                   {
                   board[rank][files] = new Tile(Character.toString(file) + rank, Color.WHITE, true, this);
                   }
               
                   else 
                   {
                       board[rank][files] = new Tile(Character.toString(file) + rank, Color.GRAY, true, this);
                   }                 
                   gui.mainframe.add(board[rank][files].btn);
                   // set white pawns
                   if (rank == 1) 
                   {
                       board[rank][files].btn.setText("" + WHITE_PAWN );
                   } 
                   //set black pawns
                   else if (rank == 6) 
                   {
                       board[rank][files].btn.setText("" + BLACK_PAWN);
                   } 
                   // set back rank
                   else if (rank == 0) 
                   {
                       switch (file) {
                           case 'a':
                           case 'h':
                               board[rank][files].btn.setText("" + WHITE_ROOK);
                               break;
                           case 'b':
                           case 'g':
                               board[rank][files].btn.setText("" + WHITE_KNIGHT);
                               break;
                           case 'c':
                           case 'f':
                               board[rank][files].btn.setText("" + WHITE_BISHOP);
                               break;
                           case 'd':
                               board[rank][files].btn.setText("" + WHITE_QUEEN);
                               break;
                           case 'e':
                               board[rank][files].btn.setText("" + WHITE_KING);
                               break;
                       }
                   }  
                   // set back rank
                   else if (rank == 7) 
                   {
                       switch (file) 
                       {
                           case 'a':
                           case 'h':
                               board[rank][files].btn.setText("" + BLACK_ROOK);
                               break;
                           case 'b':
                           case 'g':
                               board[rank][files].btn.setText("" + BLACK_KNIGHT);
                               break;
                           case 'c':
                           case 'f':
                               board[rank][files].btn.setText("" + BLACK_BISHOP);
                               break;
                           case 'd':
                               board[rank][files].btn.setText("" + BLACK_QUEEN);
                               break;
                           case 'e':
                               board[rank][files].btn.setText("" + BLACK_KING);
                               break;
                        }
                    }
                }
            }
        }
       // removes unnecesary stuff
        for(int i = 0; i < 8; i++) 
        {
        	for(int j = 0; j < 8; j++)
        	{
        		if(board[i][j].btn.getText().length() == 2) 
        		{
        			board[i][j].btn.setText("");
        		}
        	}
        }
        
        //make the board visible and set up the game in the back end
       
        gui.mainframe.setVisible(true);
        b = new Board();
        c = new ColorSelection(this);
    }
   
   /**
    * moves a piece on the GUI
    * @param inLoc: initial location
    * @param finLoc: final location
    */
    public static void move(String inLoc, String finLoc)
    {
    	// get the positions to be moved to and from
        int file = finLoc.charAt(0)-97;
        int rank = Integer.parseInt(finLoc.substring(1,2));
        int files = inLoc.charAt(0)-97;
        int ranks = Integer.parseInt(inLoc.substring(1,2));
        int distance = finLoc.charAt(0)-inLoc.charAt(0);
        
        // check if castling and get all the pieces as strings
        boolean castled = false;
        String x = board[ranks][files].btn.getText();
        String kw = Character.toString(WHITE_KING);
        String kb = Character.toString(BLACK_KING);
        String pw = Character.toString(WHITE_PAWN);
        String pb = Character.toString(BLACK_PAWN);
      
        // check king move for special case castle
        if(x.equals(kw) || x.equals(kb))
        {
            if(distance==-2||distance==2)
            {
                String tempk = board[ranks][files].btn.getText();
                board[ranks][files].btn.setText("");
                file = finLoc.charAt(0)-97;
                rank = Integer.parseInt(finLoc.substring(1,2));
                board[rank][file].btn.setText(tempk);
                if(distance== -2)
                {
                    String tempr = board[rank][file-2].btn.getText();
                    board[rank][file-2].btn.setText("");
                    int rfile = finLoc.charAt(0)-96;
                    board[rank][rfile].btn.setText(tempr);
                    castled = true;                
                }
                if(distance == 2)
                {
                    String tempr = board[rank][file+1].btn.getText();
                    board[rank][file+1].btn.setText("");
                    int rfile = finLoc.charAt(0)-98;
                    board[rank][rfile].btn.setText(tempr);
                    castled = true;            
                }       
            }
        }
        // move normal pieces
        if(!castled)
        {
            board[rank][file].btn.setText(board[ranks][files].btn.getText());
            board[ranks][files].btn.setText("");
        }
        // check pawn for en passant
        if(x.equals(pw) || x.equals(pb))
        {
            if(x.equals(pw))
            {
                if(b.getGrid()[rank][file-1].hasPiece() && b.getGrid()[rank][file-1].getPiece().getColor().equals("Black") && b.getGrid()[rank][file-1].getPiece().istwospace())
                {
                    board[rank-1][file].btn.setText("");                    
                }
            }
            if(x.equals(pb))
            {
                if(b.getGrid()[rank][file+1].hasPiece() && b.getGrid()[rank][file+1].getPiece().getColor().equals("White") && b.getGrid()[rank][file+1].getPiece().istwospace())
                {
                    board[rank+1][file].btn.setText("");                    
                }
            }
            // check back rank for queening
            if(rank == 0) 
            {
                board[rank][file].btn.setText(""+BLACK_QUEEN);
            }
            if(rank == 7)
            {
                board[rank][file].btn.setText(""+WHITE_QUEEN);
            }
        }      
    }  
    
    /**
     * synchronizes the gui wiht back end board after a move is made
     * @param temp
     */
    public void synchronize(Board temp)
    {
    	// get the board from back end
    	Tiles[][] x = temp.getGrid();
    	
    	//loop through the board and set the pieces
    	for(int i = 0; i < 8; i++)
    	{
    		for(int j = 0; j < 8; j++)
    		{
    			if(x[i][j].hasPiece())
    			{
    				if(x[i][j].getPiece().getColor().equals("White"))
    				{
    					if(x[i][j].getPiece().getName().equals("Pawn"))
    					{
    						board[i][j].btn.setText("" + WHITE_PAWN);
    					}
    					if(x[i][j].getPiece().getName().equals("Rook"))
    					{
    						board[i][j].btn.setText("" + WHITE_ROOK);
    					}
    					if(x[i][j].getPiece().getName().equals("Knight"))
    					{
    						board[i][j].btn.setText("" + WHITE_KNIGHT);
    					}
    					if(x[i][j].getPiece().getName().equals("Bishop"))
    					{
    						board[i][j].btn.setText("" + WHITE_BISHOP);
    					}
    					if(x[i][j].getPiece().getName().equals("King"))
    					{
    						board[i][j].btn.setText("" + WHITE_KING);
    					}
    					if(x[i][j].getPiece().getName().equals("Queen"))
    					{
    						board[i][j].btn.setText("" + WHITE_QUEEN);
    					}
    				}
    				else
    				{
    					if(x[i][j].getPiece().getName().equals("Pawn"))
    					{
    						board[i][j].btn.setText("" + BLACK_PAWN);
    					}
    					if(x[i][j].getPiece().getName().equals("Rook"))
    					{
    						board[i][j].btn.setText("" + BLACK_ROOK);
    					}
    					if(x[i][j].getPiece().getName().equals("Knight"))
    					{
    						board[i][j].btn.setText("" + BLACK_KNIGHT);
    					}
    					if(x[i][j].getPiece().getName().equals("Bishop"))
    					{
    						board[i][j].btn.setText("" + BLACK_BISHOP);
    					}
    					if(x[i][j].getPiece().getName().equals("King"))
    					{
    						board[i][j].btn.setText("" + BLACK_KING);
    					}
    					if(x[i][j].getPiece().getName().equals("Queen"))
    					{
    						board[i][j].btn.setText("" + BLACK_QUEEN);
    					}
    				}
    			}
    			else
    			{
    				board[i][j].btn.setText("");
    			}
    		}
    	}
    }
    
    /**
     * runs the ai
     * @param engineTurn: what color pieces the engine is playing with
     */
    public void playEngine(String engineTurn) {
    	// game starts with white
    	turn = "White";
        String inLoc = "";
        String finLoc = "";
        
        // check for game over
    	while(b.gameOver(turn).equals("continue"))
    	{
    		// makes engine move
    		if(turn.equals(engineTurn))
    		{
    			System.out.println("Thinking");
                String test = b.getMove(b,turn);
                System.out.println("done!");
                inLoc = test.substring(0,2);
                finLoc = test.substring(2,4);
                System.out.println(inLoc+finLoc);
                b.movePiece(inLoc, finLoc);
                synchronize(b);
                
                // change turn
                if(engineTurn.equals("White")) 
                {
                	turn = "Black";
                }
                else 
                {
                	turn = "White";
                }
                
    		}
    		// wait between execution
    		try 
    		{
    		    Thread.sleep(500);
    		}
    		catch (InterruptedException e)
    		{
    			System.out.println("Thread Interupted");
    		}
    	}
    }
    
    /**
     * checks to see if the user has made a move
     * ActionEvent e: the event from the button press
     */
    public void actionPerformed(ActionEvent e) 
    {
    	// check if a tile has been previously selected
    	if(selectedTile == null)
    	{
    		// get first tile clicked
    		selectedTile = e.getActionCommand();
    		Tiles temp = b.getTile(selectedTile);
    		Piece selectedPiece = null;
    		
    		// get the piece on the tile if there is one
    		if(temp.hasPiece())
    		{
    			selectedPiece = temp.getPiece();
    		}
    		// if there is no piece od nothing
    		else
    		{
    			selectedTile = null;
    		}
    		// if there is a piece get its moves and disply to the user
    		if(selectedPiece!= null && selectedPiece.getColor().equals(turn))
    		{
    			ArrayList<Tiles> possMoves = b.possibleMoves(temp);
    			for(int i = 0; i < possMoves.size(); i++)
    			{
    				String loc = possMoves.get(i).getLoc();
    				int file = loc.charAt(0)-97;
    		        int rank = Integer.parseInt(loc.substring(1,2));
    		        //displays moves with a x
    		        board[rank][file].btn.setText("X");
    			}
    		}
    		// if the piece is of the wrong color do nothing
    		else
    		{
    			selectedTile = null;
    		}
    	}
    	// if a piece has already been selected
    	else
    	{
    		// get the first tile
    		Tiles temp = b.getTile(selectedTile);
    		Piece selectedPiece = temp.getPiece();
    		// collect possible moves
    		ArrayList<Tiles> possMoves = b.possibleMoves(temp);
    		// get the new tile
    		String finLoc = e.getActionCommand();
    		
    		// check if the move is valid
    		boolean valid = false;
    		for(int i = 0; i < possMoves.size(); i++) 
    		{
    			if(finLoc.equals(possMoves.get(i).getLoc()))
    			{
    				valid = true;
    			}
    		}
    		
    		// reset the board to remove x's
    		synchronize(b);
    		
    		if(valid) 
    		{
    			// move the pieces on valid move
    			b.movePiece(selectedTile,finLoc);
    			synchronize(b);
    			if(turn.equals("White"))
    			{
    				turn = "Black";
    			}
    			else
    			{
    				turn = "White";
    			}
    			
    		}
    		selectedTile = null;
    		
    	}
    	
    	
    }
    // runs the engine
    public void run() {
        playEngine(engineTurn);
    }
    // sets up the engine
    public void begin()
    {
        engineTurn = c.getColor();
    	new Thread(this).start();   	 
    }

}

/*
 * creates the GUI
 */
class GUI 
{
    
    static JFrame mainframe = new JFrame("Flux Chess 2.0");
    GridLayout grid =  new GridLayout(9,9);
    
    GUI() 
    {
        mainframe.setSize(700,700);
        mainframe.setLayout(grid);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       
    }
    
}

/*
 * Defines each tile of the chess board GUI
 */
class Tile 
{
    JButton btn;
    JLabel lbl;
    static Font unicode = new Font("Arial Unicode MS", Font.BOLD, 50);
    static LineBorder border = new LineBorder(Color.BLACK, 2);
    
    Tile(String name, Color tc, boolean isBtn, Main x) 
    {
    	// set up each tile as a button on the board
        if(isBtn) 
        {
            btn = new JButton(name);
            btn.setActionCommand(name);
            btn.addActionListener(x);
            btn.setHorizontalAlignment(JButton.CENTER);
            btn.setBorder(border);
            btn.setBackground(tc);
            btn.setOpaque(true);
            btn.setFont(unicode);
        }
        // border tiles are just labels
        else
        {
            lbl = new JLabel(name);
            lbl.setHorizontalAlignment(JLabel.CENTER);
            lbl.setBorder(border);
            lbl.setBackground(tc);
            lbl.setOpaque(true);
            lbl.setFont(unicode);
        }      
    }
   
}
