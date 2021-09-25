package main_pkg;

public class board {
    private cell_color[][] board;
    //row,col
    private int[][] valid_moves;
    private cell_color player_C;

    public board(cell_color player_color) {
        player_C = player_color;
        board = new cell_color[8][8];
    }

    public void place_piece(int row, int col, cell_color color) {
        board[row][col] = color;
    }

    private void capture(int row, int col, cell_color color) {

    }

    /**
     * First find out which empty cells adjacent to full cell_color]
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
     * @return
     */
    public int[][] find_valid_moves(){
        int[][] vMoves = {};
        int length = 8;



        //determine legal moves
        for(int row = 1; row < length; row++){

            for(int col = 1; col < length; col++){

               // cell topLeft  = board[ row-1 ][ col-1 ];
               // cell_color topLeft  = board[ row-1 ][ col-1 ];
               /* int top      = board[ x     ][ y - 1 ]
                int topRight = board[ x + 1 ][ y - 1 ]

                int midLeft  = board[ x - 1 ][ y     ]
                int midRight = board[ x + 1 ][ y     ]

                botLeft  = array[ x - 1 ][ y + 1 ]
                bot      = array[ x     ][ y + 1 ]
                botRight = array[ x + 1 ][ y + 1 ]*/

                //opponent
                if(board[row][col] != player_C && board[row][col] != cell_color.EMPTY){
                    //check adjacent to see if my color
                    //top left
                    //board[row-1]
                   // while()

                }
            }
        }



        return vMoves;
    }
}
