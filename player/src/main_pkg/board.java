package main_pkg;

public class board {
    private cell[][] board;

    public board() {
        board = new cell[8][8];
    }

    public void place_piece(int row, int col, cell color) {
        board[row][col] = color;
    }

    private void capture(int row, int col, cell color) {

    }
}
