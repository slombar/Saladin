package mainPackage;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Cell[][] board;
    private CellColor playerColor;

    /**
     * Constructor for the Board class
     * creates a blank Board of 8x8
     *
     * @param pc, the color of our Agent
     */
    public Board(CellColor pc) {
        playerColor = pc;
        board = new Cell[8][8];
    }

    /**
     * Place a single piece on the Board
     *
     * @param row, the row the piece gonna be in
     * @param col, the column the piece gonna be in
     */
    public void placePiece(int row, int col, CellColor color) {
        Cell placingCell = new Cell(color, row, col);
        board[row][col] = placingCell;
    }

    /**
     * Captures all pieces after placing a new piece
     *
     * @param capturingCell, the Cell where the newest piece was placed
     */
    public void capture(Cell capturingCell) {

        //up
        int[] up = {1, 0};
        captureLine(capturingCell, up);

        //down
        int[] down = {-1, 0};
        captureLine(capturingCell, down);

        //left
        int[] left = {0, -1};
        captureLine(capturingCell, left);

        //right
        int[] right = {0, 1};
        captureLine(capturingCell, right);

        //up left
        int[] upLeft = {1, -1};
        captureLine(capturingCell, upLeft);

        //up right
        int[] upRight = {1, 1};
        captureLine(capturingCell, upRight);

        //down left
        int[] downLeft = {-1, -1};
        captureLine(capturingCell, downLeft);

        //down right
        int[] downRight = {-1, 1};
        captureLine(capturingCell, downRight);

    }

    private void captureLine(Cell capturingCell, int[] vector) {
        if (vector.length == 2) {
            CellColor capturingColor = capturingCell.getColor();
            int currRow = capturingCell.getRow() + vector[0];
            int currCol = capturingCell.getCol() + vector[1];
            Cell currCell = board[currRow][currCol];

            if (currCell.getColor() == capturingColor
                    || currCell.getColor() == CellColor.EMPTY
                    || currRow > 7
                    || currCol > 7
                    || currRow < 0
                    || currCol < 0) return;
            else {
                ArrayList<Cell> cellsToChange= new ArrayList<>();
                cellsToChange.add(currCell);

                while (true) {
                    currCell = board[currRow + vector[0]][currCol + vector[1]];
                    currRow = currCell.getRow();
                    currCol = currCell.getCol();

                    if (currCell.getColor() == capturingColor) break;
                    else if (currCell.getColor() == CellColor.EMPTY
                            || currRow > 7
                            || currCol > 7
                            || currRow < 0
                            || currCol < 0) return;

                    else cellsToChange.add(currCell);
                }

                //loop through cellsToChange and update the Board
                for (Cell c : cellsToChange) {
                    placePiece(c.getRow(), c.getCol(), c.getColor());
                }
            }
        }
    }

    /**
     *For each Cell in the Board state:
     * Check if the current Cell is empty, therefore it has the possibility of being a move
     * Since this Cell is empty, we can look to see if enemy cells are adjacent to it.
     *      1. if we find that there are enemy adjacent cells (EAJ):
     *          a. we will continue to check each EAJ Cell to find connected enemy cells (CEC)*
     *             (*cells on diagonals, horizontals, and verticals of the EAJ Cell depending on its location in comparison to our empty Cell)
     *                  i. if we find CEC then we will continue to follow the path of connection until we:
     *                          A- reach an empty Cell
     *                          B- reach the end of the Board without finding a playercell
     *                             if we find an empty Cell or reach the end of the Board, this is not a valid move, therefore we exit out of this possible move
     *
     *                          C- find a player Cell
     *                             if we find a player Cell, this is a valid move! so we can add it to the list of valid moves.
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
    public List<Cell> findValidMoves(CellColor currColor) {

        List<Cell> validMoves = new ArrayList<>();
        int startOfBoardNum = 1;
        int endOfBoardNum = 8;
        Cell currentCell = null;
        Cell TLcurrentCell = null;
        Cell TcurrentCell = null;
        Cell TRcurrentCell = null;
        Cell LcurrentCell = null;
        Cell RcurrentCell = null;
        Cell BLcurrentCell = null;
        Cell BcurrentCell = null;
        Cell BRcurrentCell = null;

        //determine legal moves by using adjacent cells
        for (int row = startOfBoardNum; row < endOfBoardNum; row++) {
            for (int col = startOfBoardNum; col < endOfBoardNum; col++) {

                //determine the current Cell
                currentCell = board[row][col];

                if (currentCell.getColor() == CellColor.EMPTY) {

                    //for each Cell, find adjacent cells
                    TLcurrentCell = board[row - 1][col - 1];
                    TcurrentCell = board[row - 1][col];
                    TRcurrentCell = board[row - 1][col + 1];
                    LcurrentCell = board[row][col - 1];
                    RcurrentCell = board[row][col + 1];
                    BLcurrentCell = board[row + 1][col - 1];
                    BcurrentCell = board[row + 1][col];
                    BRcurrentCell = board[row + 1][col + 1];

                    /*check if we are on a bordering Cell (ie, it will not have all adjacent cells)*/
                    //check top and bottom walls
                    if (currentCell.getRow() == startOfBoardNum) { //is the current Cell on the top row of the Board?
                        //don't add any top adjacent cells
                        TLcurrentCell = null;
                        TcurrentCell = null;
                        TRcurrentCell = null;

                    } else if (currentCell.getRow() == endOfBoardNum) { //is the current Cell on the bottom row of the Board?
                        //don't add any bottom adjacent cells
                        BLcurrentCell = null;
                        BcurrentCell = null;
                        BRcurrentCell = null;
                    }

                    //check right and left walls
                    if (currentCell.getCol() == startOfBoardNum) { //is the current Cell on the left border of the Board?
                        //don't add any left adjacent cells
                        TLcurrentCell = null;
                        LcurrentCell = null;
                        BLcurrentCell = null;
                    } else if (currentCell.getCol() == endOfBoardNum) { // is the current Cell on the right border of the Board?
                        //don't add any right adjacent cells
                        TRcurrentCell = null;
                        RcurrentCell = null;
                        BRcurrentCell = null;
                    }

                    /* Check if any adjacent cells are enemies */
                    //top left Cell
                    if (isEnemyCell(TLcurrentCell)){
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        Cell nextCell = TLcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempRow--;
                            tempCol--;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempRow >= startOfBoardNum && tempCol >= startOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempRow][tempCol];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board without finding a player
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //top Cell
                    if (isEnemyCell(TcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        Cell nextCell = TcurrentCell;

                        do {
                            //iterate to the next Cell, in this case, the above column
                            tempRow--;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempRow >= startOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempRow][col];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //top right Cell
                    if (isEnemyCell(TRcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        Cell nextCell = TRcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempRow--;
                            tempCol++;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempRow >= startOfBoardNum && tempCol <= endOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempRow][tempCol];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                   //left Cell
                    if (isEnemyCell(LcurrentCell)) {
                        boolean exit = false;
                        int tempCol = col;
                        Cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempCol--;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempCol >= startOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempCol][col];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //right Cell
                    if (isEnemyCell(RcurrentCell)) {
                        boolean exit = false;
                        int tempCol = col;
                        Cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempCol++;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempCol <= endOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[row][tempCol];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    if (isEnemyCell(BLcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        Cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempRow++;
                            tempCol--;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempRow <= endOfBoardNum && tempCol >= startOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempRow][tempCol];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);
                    }
                    //bottom Cell
                    if (isEnemyCell(BcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        Cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempRow++;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempRow <= endOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempRow][col];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
                                exit = true;
                            }

                        } while (isEnemyCell(nextCell) && !exit);

                    }
                    if (isEnemyCell(BRcurrentCell)) {
                        boolean exit = false;
                        int tempRow = row;
                        int tempCol = col;
                        Cell nextCell = BcurrentCell;

                        do {
                            //iterate to the next Cell
                            tempRow++;
                            tempCol++;

                            //make sure we are in range of the Board ie not at a wall
                            if (tempRow <= endOfBoardNum && tempCol <= endOfBoardNum) {

                                //instantiate the next Cell
                                nextCell = board[tempRow][tempCol];

                                //is the next Cell a player color?
                                if (isPlayerCell(nextCell)) {
                                    // if a player Cell is found, then the move is valid and we can add it to the list
                                    validMoves.add(currentCell);

                                    //check if the Cell is empty, otherwise it's an enemy Cell
                                } else if (isEmptyCell(nextCell)) {
                                    //not a valid move because we reached an empty Cell instead of a player Cell
                                    exit = true;
                                }

                            } else {
                                //not a valid move because we reached the end of the Board
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
     * check to see if the given Cell and one of its adjacent cells
     *
     * @param possibleEnemyCell, the Cell to check if enemy
     * @return
     */
    public boolean isEnemyCell(Cell possibleEnemyCell) {
        boolean result = true;

        if(possibleEnemyCell == null){
            result = false;
        }

        if (playerColor == possibleEnemyCell.getColor()) {
            result = false;
        }

        if(possibleEnemyCell.getColor() == CellColor.EMPTY){
            result = false;
        }

        return result;
    }

    /**
     * check to see if the given Cell and one of its adjacent cells
     *
     * @param possibleEnemyCell, the Cell to check if enemy
     * @return
     */
    public boolean anotherIsEnemyCell(Cell possibleEnemyCell, CellColor currColor) {
        boolean result = true;

        if(possibleEnemyCell == null){
            result = false;
        }

        if (currColor == possibleEnemyCell.getColor()) {
            result = false;
        }

        if(possibleEnemyCell.getColor() == CellColor.EMPTY){
            result = false;
        }

        return result;
    }

    public boolean isEmptyCell(Cell possibleEmptyCell) {
        boolean result = false;

        if (possibleEmptyCell.getColor() == CellColor.EMPTY) {
            result = true;
        }

        return result;
    }

    public boolean isPlayerCell(Cell possiblePlayerCell) {
        boolean result = false;

        if (playerColor == possiblePlayerCell.getColor()) {
            result = true;
        }

        return result;
    }

    public boolean anotherIsPlayerCell(Cell possiblePlayerCell, CellColor currColor) {
        boolean result = false;

        if (currColor == possiblePlayerCell.getColor()) {
            result = true;
        }

        return result;
    }
}
