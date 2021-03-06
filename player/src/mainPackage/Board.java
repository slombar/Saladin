package mainPackage;

import java.util.ArrayList;
import java.util.List;

public class Board {
    public Cell[][] board;
    private final CellColor playerColor;
    public CellColor currentColor;
    private final Direction directions = new Direction();
    public int boardMin = 0;
    public int boardMax = 8;

    /**
     * Constructor for the Board class
     * creates a blank Board of 8x8, then fills in starting config.
     *
     * @param pc, the color of our Agent
     */
    public Board(CellColor pc, CellColor currentTurn) {
        playerColor = pc;
        currentColor = currentTurn;
        board = new Cell[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                placePiece(i, j, CellColor.EMPTY);
            }
        }

        placePiece(3, 3, CellColor.ORANGE);
        placePiece(3, 4, CellColor.BLUE);
        placePiece(4, 4, CellColor.ORANGE);
        placePiece(4, 3, CellColor.BLUE);
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
     * Checks if coordinates are out of bounds
     */
    public boolean outOfBounds(int row, int col) {
        return row < 0 || row > 7 || col < 0 || col > 7;
    }

    /**
     * Captures all pieces after placing a new piece
     *
     * @param capturingCell, the Cell where the newest piece was placed
     */
    public void capture(Cell capturingCell) {
        captureLine(capturingCell, directions.UP);
        captureLine(capturingCell, directions.DOWN);
        captureLine(capturingCell, directions.LEFT);
        captureLine(capturingCell, directions.RIGHT);
        captureLine(capturingCell, directions.UP_LEFT);
        captureLine(capturingCell, directions.UP_RIGHT);
        captureLine(capturingCell, directions.DOWN_LEFT);
        captureLine(capturingCell, directions.DOWN_RIGHT);
    }

    private void captureLine(Cell capturingCell, int[] vector) {
        // Update positions
        int currRow = capturingCell.getRow() + vector[0];
        int currCol = capturingCell.getCol() + vector[1];

        if (outOfBounds(currRow, currCol)) {
            return;
        }

        Cell currCell = board[currRow][currCol];

        if (!isEnemyCell(currCell)) {
            return;
        }

        ArrayList<Cell> cellsToChange = new ArrayList<>();

        // Keep going until we hit a non-enemy cell
        while (isEnemyCell(currCell)) {
            cellsToChange.add(currCell);

            currRow += vector[0];
            currCol += vector[1];

            if (outOfBounds(currRow, currCol)) {
                return;
            }
            currCell = board[currRow][currCol];
        }

        // If we find a player cell at the end of the line, capture all the pieces on the way.
        if (isPlayerCell(currCell)) {
            for (Cell c : cellsToChange) {
                placePiece(c.getRow(), c.getCol(), currentColor);
            }
        }
    }

    private boolean isValidMove(Cell possibleMove) {
        return captureLineTest(possibleMove, directions.UP)
                || captureLineTest(possibleMove, directions.DOWN)
                || captureLineTest(possibleMove, directions.LEFT)
                || captureLineTest(possibleMove, directions.RIGHT)
                || captureLineTest(possibleMove, directions.UP_LEFT)
                || captureLineTest(possibleMove, directions.UP_RIGHT)
                || captureLineTest(possibleMove, directions.DOWN_LEFT)
                || captureLineTest(possibleMove, directions.DOWN_RIGHT);
    }

    private boolean captureLineTest(Cell capturingCell, int[] vector) {
        int currRow = capturingCell.getRow() + vector[0];
        int currCol = capturingCell.getCol() + vector[1];

        if (outOfBounds(currRow, currCol)) {
            return false;
        }

        Cell currCell = board[currRow][currCol];

        if (!isEnemyCell(currCell)) {
            return false;
        }

        while (isEnemyCell(currCell)) {
            currRow += vector[0];
            currCol += vector[1];

            if (outOfBounds(currRow, currCol)) {
                return false;
            }
            currCell = board[currRow][currCol];
        }

        return isPlayerCell(currCell);
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
    public List<Cell> findValidMoves() {
        List<Cell> validMoves = new ArrayList<>();

        //determine legal moves by using adjacent cells
        for (int row = boardMin; row < boardMax; row++) {
            for (int col = boardMin; col < boardMax; col++) {

                //determine the current Cell
                Cell currentCell = board[row][col];

                if (isEmptyCell(currentCell)) {
                    if (isValidMove(currentCell)) {
                        validMoves.add(currentCell);
                    }
                }
            }
        }

        return validMoves;
    }

    public int findNumPotentialFutureMoves() {
        int result = 0;

        //determine legal moves by using adjacent cells
        for (int row = boardMin; row < boardMax; row++) {
            for (int col = boardMin; col < boardMax; col++) {

                //determine the current Cell
                Cell currentCell = board[row][col];

                if (isEnemyCell(currentCell)) {
                    if (isPotentialFutureMove(currentCell)) {
                        result++;
                    }
                }
            }
        }
        return result;
    }

    public boolean isPotentialFutureMove(Cell possibleFutureMove) {
        return isNextToEmptySpace(possibleFutureMove, directions.UP)
                || isNextToEmptySpace(possibleFutureMove, directions.DOWN)
                || isNextToEmptySpace(possibleFutureMove, directions.LEFT)
                || isNextToEmptySpace(possibleFutureMove, directions.RIGHT)
                || isNextToEmptySpace(possibleFutureMove, directions.UP_LEFT)
                || isNextToEmptySpace(possibleFutureMove, directions.UP_RIGHT)
                || isNextToEmptySpace(possibleFutureMove, directions.DOWN_LEFT)
                || isNextToEmptySpace(possibleFutureMove, directions.DOWN_RIGHT);
    }

    public boolean isNextToEmptySpace(Cell enemyCell, int[] vector) {
        int currRow = enemyCell.getRow() + vector[0];
        int currCol = enemyCell.getCol() + vector[1];

        if (outOfBounds(currRow, currCol)) {
            return false;
        }

        Cell currCell = board[currRow][currCol];

        return isEmptyCell(currCell);
    }

    /**
     * check to see if the given Cell and one of its adjacent cells
     *
     * @param possibleEnemyCell, the Cell to check if enemy
     */
    public boolean isEnemyCell(Cell possibleEnemyCell) {
        return possibleEnemyCell.getColor() == getOppositeColor(currentColor);
    }

    public boolean isEmptyCell(Cell possibleEmptyCell) {
        return possibleEmptyCell.getColor() == CellColor.EMPTY;
    }

    public boolean isPlayerCell(Cell possiblePlayerCell) {
        return possiblePlayerCell.getColor() == currentColor;
    }

    public static CellColor getOppositeColor(CellColor color) {
        if (color == CellColor.EMPTY) {
            return CellColor.EMPTY;
        }
        else if (color == CellColor.BLUE) {
            return CellColor.ORANGE;
        }
        else {
            return CellColor.BLUE;
        }
    }

    public String boardToString() {
        StringBuilder out = new StringBuilder();
        for (int row = boardMin; row < boardMax; row++) {
            out.append(8 - row).append("  ");
            for (int col = boardMin; col < boardMax; col++) {
                Cell currCel = board[row][col];
                CellColor color = currCel.getColor();
                if (color == CellColor.BLUE) {
                    out.append("B  ");
                } else if (color == CellColor.ORANGE) {
                    out.append("O  ");
                } else {
                    out.append("-  ");
                }
            }
            out.append("\n");
        }
        out.append("   A  B  C  D  E  F  G  H");
        return out.toString();
    }

    public Board deepCopy() {
        Board copyBoard = new Board(this.playerColor, this.currentColor);
        for (int row = boardMin; row < boardMax; row++) {
            if (boardMax - boardMin >= 0)
                System.arraycopy(board[row], boardMin, copyBoard.board[row], boardMin, boardMax - boardMin);
        }
        return copyBoard;
    }
}
