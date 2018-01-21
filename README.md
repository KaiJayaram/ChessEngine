# Chess Aplication with build in Minimax Based Engine
  
  ## The Application backend
    A fully implemented playable chess game that allows users to play against themselves. 
    I Built the entire game from scratch defining piece movements and special case moves such as
    en passant and castling. 
  
  ## The Application UI
    Implemented the UI using java.awt libraries. The UI is built to allow for click to move functionality.
    Pieces can viewed on the screen and can be moved around with the simple click of a mouse button.
    
  ## The Engine
    The Engine itself allows players to play against a computer opponent. The engine runs on a minimax tree
    that creates a list of all possible future moves moving down in a tree with each layer being the next turn. The engine
    is capable of quick (sub 10 second) evaluations up to a depth of 4 (4 moves into the future). Moves are selected based on a 
    built in position evaluation function that assigns a positive value to white favored positions and a negative value to black
    favored positions. Moves are selected by selecting the lowest value of the choices for each black turn node in the tree and
    the greatest of the values for each white turn node. The Algorithm is optimized using Alpha Beta pruning that cuts away certain
    areas of the tree that are no longer likely to have a move better than the current best move found by the algorithm. 
    
    
    
![2018-01-20](https://user-images.githubusercontent.com/23246165/35190280-44f5977a-fe12-11e7-8d1f-c87527a1799f.png)
![2018-01-20 1](https://user-images.githubusercontent.com/23246165/35190274-34c095da-fe12-11e7-8c31-f5ee023b5536.png)
![2018-01-20 3](https://user-images.githubusercontent.com/23246165/35190278-40abe336-fe12-11e7-8342-b0afd1704c88.png)
![2018-01-20 2](https://user-images.githubusercontent.com/23246165/35190276-3c41df76-fe12-11e7-9833-3ff59482b342.png)


