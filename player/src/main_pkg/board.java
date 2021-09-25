package main_pkg;

public class board {
    private cell[][] board;
    private cell_color playerColor;

    /**
     * Constructor for the board class
     * creates a blank board of 8x8
     * @param player_color, the color of our agent
     */
    public board(cell_color player_color) {
        playerColor = player_color;
        board = new cell[8][8];
    }

    /**
     * Place a single piece on the board
     * @param row, the row the piece gonna be in
     * @param col, the column the piece gonna be in
     */
    public void place_piece(int row, int col) {
        cell placingCell = new cell(playerColor, row, col);
        board[row][col] = placingCell;
    }

    /**
     * Capture a piece on the board
     * @param capturingCell, the cell you are trying to capture
     */
    private void capture(cell capturingCell) {
        int cellRow = capturingCell.getRow();
        int cellCol = capturingCell.getCol();
    }

    /**
     *
     * which cells involve capturing opponents (which have allied piece on the same hor, ver, dia, line)
     * has to be between two of the player's cells already on board
     * check diagonal and check if it hits allied piece
     * if it hits enemy piece then wait until hits allied piece then we have a color change
     * if it never hits another of the player's current pieces / wall then it doesn't get anything
     *
     * rows 1 at bottom 8 at top
     * cols a-h
     *
     * to find wall check if greater than the range / less than 0
     *
     * loop through all opponent pieces and then check empty spaces next to it
     *
     * @return a list of valid moves in the form of cells
     */
    public cell[] find_valid_moves(){
        cell[] validMoves = {};
        int startOfBoardNum = 1;
        int length = 8;
        cell currentCell = null;
        cell TLcurrentCell = null;
        cell TcurrentCell = null;
        cell TRcurrentCell = null;
        cell LcurrentCell = null;
        cell RcurrentCell = null;
        cell BLcurrentCell = null;
        cell BcurrentCell = null;
        cell BRcurrentCell = null;

        //determine legal moves
        for(int row = startOfBoardNum; row < length; row++){
            for(int col = startOfBoardNum; col < length; col++){

                /* Determining position of adjacent cells:
                          -1COL              COL              +1COL
                -1ROW      [TLcurrentCell]   [TcurrentCell]   [TRcurrentCell]

                ROW        [LcurrentCell]    *[currentCell]*  [RcurrentCell]

                +1ROW      [BLcurrentCell]   [BcurrentCell]   [BRcurrentCell]
                 */

                //determine the current cell
                currentCell = board[row][col];

                /*
                check if the current cell is empty, therefore it has the possibility of being a move
                    1. if current cell is empty, then we can look to see if enemy cells are adjacent to it.
                    2. we find if there are enemy adjacent cells (EAJ), if yes:
                        a. we will continue to check each EAJ cell to find connected enemy cells (CEC)*
                            (*cells on diagonals, horizontals, and verticals of the EAJ cell)
                            i. if we find CEC then we will continue to follow the path of connection until we either:
                                - reach an empty cell
                                - reach the end of the board without finding a playercell
                                - find a player cell
                               if we find an empty cell or reach the end of the board, this is not a valid move, therefore we remove it from the possibile moves

                    */
                if(currentCell.getColor() == cell_color.EMPTY) {

                    //for each cell, find adjacent cells
                    TLcurrentCell = board[row-1][col-1];
                    TcurrentCell = board[row-1][col];
                    TRcurrentCell = board[row-1][col+1];
                    LcurrentCell = board[row][col-1];
                    RcurrentCell = board[row][col+1];
                    BLcurrentCell = board[row+1][col-1];
                    BcurrentCell = board[row+1][col];
                    BRcurrentCell = board[row+1][col+1];

                    /*check if we are on a bordering cell (ie, it will not have all adjacent cells)*/
                    if(currentCell.getRow() == startOfBoardNum){ //is the current cell on the top row of the board?
                        //don't add any top adjacent cells
                        TLcurrentCell = null;
                        TcurrentCell = null;
                        TRcurrentCell = null;
                    }else if(currentCell.getRow() == length){ //is the current cell on the bottom row of the board?
                        //don't add any bottom adjacent cells
                        BLcurrentCell = null;
                        BcurrentCell = null;
                        BRcurrentCell = null;
                    }

                    if(currentCell.getCol() == startOfBoardNum) { //is the current cell on the left border of the board?
                        //don't add any left adjacent cells
                        TLcurrentCell = null;
                        LcurrentCell = null;
                        BLcurrentCell = null;
                    }else if (currentCell.getCol() == length){ // is the current cell on the right border of the board?
                        //don't add any right adjacent cells
                        TRcurrentCell = null;
                        RcurrentCell = null;
                        TRcurrentCell = null;
                    }

                    /* Check if any adjacent cells are enemies */
                    for(int x = 0; x < 9; x++){

                        if(isEnemyCell(currentCell, TLcurrentCell)){
                            //if the cell to the top left is an enemy cell, do the following checks:
                            //  1. see if the BRcurrentCell (cell adjacent to bottom right of currentCell) is an enemy cell (
                        }
                        if(isEnemyCell(currentCell, TcurrentCell)){

                        }
                        if(isEnemyCell(currentCell, TRcurrentCell)){

                        }
                        do{
                            //looking at current empty cell (1st parameter for movement), check the left to see if there is an enemy cell
                            //if there is, continue to check the left cell until either a wall/empty space is met, or:
                            // if a player cell is found, then the move is valid and we can add it to the list





                        }while(isEnemyCell(currentCell, LcurrentCell));
                        if(isEnemyCell(currentCell, RcurrentCell)){

                        }
                        if(isEnemyCell(currentCell, BLcurrentCell)){

                        }
                        if(isEnemyCell(currentCell, BcurrentCell)){

                        }
                        if(isEnemyCell(currentCell, BRcurrentCell)){

                        }

                    }
                }

            }
        }

        return validMoves;
    }

    /**
     * check to see if the given cell and one of its adjacent cells
     * @param currentCell, the cell you want to compare with the possibleEnemyCell
     * @param possibleEnemyCell, the cell to check if enemy
     * @return
     */
    public boolean isEnemyCell(cell currentCell, cell possibleEnemyCell){
        boolean result = false;

        if(currentCell.getOppositeColor() == possibleEnemyCell.getColor()){
            result = true;
        }

        return result;
    }
}
