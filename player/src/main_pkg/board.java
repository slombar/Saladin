package main_pkg;

import java.util.ArrayList;
import java.util.List;

public class board {
    private cell[][] board;
    private cell_color playerColor;

    /**
     * Constructor for the board class
     * creates a blank board of 8x8
     *
     * @param player_color, the color of our agent
     */
    public board(cell_color player_color) {
        playerColor = player_color;
        board = new cell[8][8];
    }

    /**
     * Place a single piece on the board
     *
     * @param row, the row the piece gonna be in
     * @param col, the column the piece gonna be in
     */
    public void place_piece(int row, int col) {
        cell placingCell = new cell(playerColor, row, col);
        board[row][col] = placingCell;
    }

    /**
     * Capture a piece on the board
     *
     * @param capturingCell, the cell you are trying to capture
     */
    private void capture(cell capturingCell) {
        int cellRow = capturingCell.getRow();
        int cellCol = capturingCell.getCol();
    }

    /**
     *For each cell in the board state:
     * Check if the current cell is empty, therefore it has the possibility of being a move
     * Since this cell is empty, we can look to see if enemy cells are adjacent to it.
     *      1. if we find that there are enemy adjacent cells (EAJ):
     *          a. we will continue to check each EAJ cell to find connected enemy cells (CEC)*
     *             (*cells on diagonals, horizontals, and verticals of the EAJ cell depending on its location in comparison to our empty cell)
     *                  i. if we find CEC then we will continue to follow the path of connection until we:
     *                          A- reach an empty cell
     *                          B- reach the end of the board without finding a playercell
     *                             if we find an empty cell or reach the end of the board, this is not a valid move, therefore we exit out of this possible move
     *
     *                          C- find a player cell
     *                             if we find a player cell, this is a valid move! so we can add it to the list of valid moves.
     *
     *                              ADJACENT CELL STRUCTURE
     *
     *                               -1COL              COL              +1COL
     *                 -1ROW      [TLcurrentCell]   [TcurrentCell]   [TRcurrentCell]
     *
     *                 ROW        [LcurrentCell]    *[currentCell]*  [RcurrentCell]
     *
     *                 +1ROW      [BLcurrentCell]   [BcurrentCell]   [BRcurrentCell]
     *
     * @return a list of valid moves in the form of cells
     */
    public List<cell> find_valid_moves() {

        List<cell> validMoves = new ArrayList<>();
        int startOfBoardNum = 1;
        int endOfBoardNum = 8;
        cell currentCell = null;
        cell TLcurrentCell = null;
        cell TcurrentCell = null;
        cell TRcurrentCell = null;
        cell LcurrentCell = null;
        cell RcurrentCell = null;
        cell BLcurrentCell = null;
        cell BcurrentCell = null;
        cell BRcurrentCell = null;

        //determine legal moves by using adjacent cells
        for (int row = startOfBoardNum; row < endOfBoardNum; row++) {
            for (int col = startOfBoardNum; col < endOfBoardNum; col++) {

                //determine the current cell
                currentCell = board[row][col];

                if (currentCell.getColor() == cell_color.EMPTY) {

                    //for each cell, find adjacent cells
                    TLcurrentCell = board[row - 1][col - 1];
                    TcurrentCell = board[row - 1][col];
                    TRcurrentCell = board[row - 1][col + 1];
                    LcurrentCell = board[row][col - 1];
                    RcurrentCell = board[row][col + 1];
                    BLcurrentCell = board[row + 1][col - 1];
                    BcurrentCell = board[row + 1][col];
                    BRcurrentCell = board[row + 1][col + 1];

                    /*check if we are on a bordering cell (ie, it will not have all adjacent cells)*/
                    //check top and bottom walls
                    if (currentCell.getRow() == startOfBoardNum) { //is the current cell on the top row of the board?
                        //don't add any top adjacent cells
                        TLcurrentCell = null;
                        TcurrentCell = null;
                        TRcurrentCell = null;

                    } else if (currentCell.getRow() == endOfBoardNum) { //is the current cell on the bottom row of the board?
                        //don't add any bottom adjacent cells
                        BLcurrentCell = null;
                        BcurrentCell = null;
                        BRcurrentCell = null;
                    }

                    //check right and left walls
                    if (currentCell.getCol() == startOfBoardNum) { //is the current cell on the left border of the board?
                        //don't add any left adjacent cells
                        TLcurrentCell = null;
                        LcurrentCell = null;
                        BLcurrentCell = null;
                    } else if (currentCell.getCol() == endOfBoardNum) { // is the current cell on the right border of the board?
                        //don't add any right adjacent cells
                        TRcurrentCell = null;
                        RcurrentCell = null;
                        BRcurrentCell = null;
                    }

                    /* Check if any adjacent cells are enemies */
                    //top left cell
                    if (isEnemyCell(TLcurrentCell)){
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        cell nextCell = TLcurrentCell;

                        do {
                            //iterate to the next cell
                            tempRow--;
                            tempCol--;

                            //make sure we are in range of the board ie not at a wall
                            if (tempRow >= startOfBoardNum && tempCol >= startOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempRow][tempCol];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board without finding a player
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //top cell
                    if (isEnemyCell(TcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        cell nextCell = TcurrentCell;

                        do {
                            //iterate to the next cell, in this case, the above column
                            tempRow--;

                            //make sure we are in range of the board ie not at a wall
                            if (tempRow >= startOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempRow][col];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //top right cell
                    if (isEnemyCell(TRcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        cell nextCell = TRcurrentCell;

                        do {
                            //iterate to the next cell
                            tempRow--;
                            tempCol++;

                            //make sure we are in range of the board ie not at a wall
                            if (tempRow >= startOfBoardNum && tempCol <= endOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempRow][tempCol];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                   //left cell
                    if (isEnemyCell(LcurrentCell)) {
                        boolean exit = false;
                        int tempCol = col;
                        cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next cell
                            tempCol--;

                            //make sure we are in range of the board ie not at a wall
                            if (tempCol >= startOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempCol][col];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //right cell
                    if (isEnemyCell(RcurrentCell)) {
                        boolean exit = false;
                        int tempCol = col;
                        cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next cell
                            tempCol++;

                            //make sure we are in range of the board ie not at a wall
                            if (tempCol <= endOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[row][tempCol];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    if (isEnemyCell(BLcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next cell
                            tempRow++;
                            tempCol--;

                            //make sure we are in range of the board ie not at a wall
                            if (tempRow <= endOfBoardNum && tempCol >= startOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempRow][tempCol];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //bottom cell
                    if (isEnemyCell(BcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next cell
                            tempRow++;

                            //make sure we are in range of the board ie not at a wall
                            if (tempRow <= endOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempRow][col];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);

                    }
                    if (isEnemyCell(BRcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next cell
                            tempRow++;
                            tempCol++;

                            //make sure we are in range of the board ie not at a wall
                            if (tempRow <= endOfBoardNum && tempCol <= endOfBoardNum) {

                                //instantiate the next cell
                                nextCell = board[tempRow][tempCol];

                                //is the next cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the cell is empty, otherwise it's an enemy cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty cell instead of a player cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }

                }


            }
        }

        return validMoves;
    }

    /**
     * check to see if the given cell and one of its adjacent cells
     *
     * @param possibleEnemyCell, the cell to check if enemy
     * @return
     */
    public boolean isEnemyCell(cell possibleEnemyCell) {
        boolean result = true;

        if(possibleEnemyCell == null){
            result = false;
        }

        if (playerColor == possibleEnemyCell.getColor()) {
            result = false;
        }

        if(possibleEnemyCell.getColor() == cell_color.EMPTY){
            result = false;
        }

        return result;
    }

    public boolean isEmptyCell(cell possibleEmptyCell) {
        boolean result = false;

        if (possibleEmptyCell.getColor() == cell_color.EMPTY) {
            result = true;
        }

        return result;
    }

    public boolean isPlayerCell(cell possiblePlayerCell) {
        boolean result = false;

        if (playerColor == possiblePlayerCell.getColor()) {
            result = true;
        }

        return result;
    }
}
