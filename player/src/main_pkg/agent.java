package main_pkg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class agent {

    private String columnLetters = "ABCDEFGH";

    private String groupname;
    public cell_color player_color;
    private board curr_board;

    public agent(String group, cell_color color, String first_move) {
        groupname = group;
        player_color = color;
        curr_board = new board(player_color);
        ai_loop();
    }

    public void ai_loop() {
        //check for groupname.go
        File group_file = new File("../../../referee/" + groupname + ".go");
        while (!group_file.exists()) {/*languish*/}

        //if that exists, check for end_game
        File end_file = new File("../../../referee/end_game");
        if (end_file.exists()) return; //if that exists, game is done *crab rave*

        //if no end_game, czech move_file
        File move_file = new File("../../../referee/move_file");
        String opponent_move;
        try {
            Scanner move_scanner = new Scanner(move_file);
            opponent_move = move_scanner.nextLine();

            String our_move = choose_next_move(opponent_move);

            FileWriter move_writer = new FileWriter(move_file,false);
            move_writer.flush();
            move_writer.write(our_move);
            move_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (group_file.exists()) {/*wait until our group file gets removed*/}
        ai_loop(); //run ai_loop() again
    }

    public String choose_next_move(String opponent_move) {
        if(opponent_move == "" || opponent_move == null){
            cell ourCell = null;
            ourCell.setRow(4);
            ourCell.setCol(3);

            this.curr_board = apply_move(curr_board, ourCell, ourCell.getColor());
        }
        String[] opponentValueStrings = opponent_move.split(" ");
        int opponentCol = columnLetters.indexOf(opponentValueStrings[1]);
        int opponentRow = Integer.parseInt(opponentValueStrings[2]) - 1;
        cell opponentCell = new cell();
        opponentCell.setCol(opponentCol);
        opponentCell.setRow(opponentRow);
        if (player_color == cell_color.BLUE) opponentCell.setColor(cell_color.ORANGE);
        else opponentCell.setColor(cell_color.BLUE);
        this.curr_board = apply_move(curr_board, opponentCell, opponentCell.getColor());

        cell ourMove = randomMove();
        //miniMove ourMove = minimax(curr_board, 3, true, null, -10000, 10000);

        //print output

        char colString   = columnLetters.charAt(ourMove.getCol());
        String rowString = String.valueOf(ourMove.getRow());

        return "Saladin " + rowString + " " + colString + "\n";
    }

    public boolean isEnemyCell(cell possibleEnemyCell){
        boolean result = true;

        if(player_color == possibleEnemyCell.getColor()){
            result = false;
        }

        return result;
    }

    /**
     * Evaluation function for minmax
     * @return
     */
    public int eval_function(board b){
        return 0;
    }

    public boolean isPlayerCell(cell possiblePlayerCell){
        boolean result = false;

        if(player_color == possiblePlayerCell.getColor()){
            result = true;
        }

        return result;
    }

    /**
     * Min max function with alpha beta pruning
     * @return a minmaxmove data structure, that contains a chosen score and a chosen move
     */
    public miniMove minimax(board currentBoardState, int depth, boolean ourMove, cell currentMove, int alpha, int beta){
        board newBoardState = null;
        miniMove returnMove = new miniMove();
        List <cell> children = null;
        List <miniMove> childValues = null;

        if(depth == 0){
            returnMove.setValue(evaluation(currentMove));
            returnMove.setMove(currentMove);
            return returnMove;
        }else{
            children =  generate_moves(currentBoardState, player_color);

            //check if end state
            if (children.isEmpty()) {
                //if we have no moves, check if the enemy also has no moves
                cell_color color_to_check;
                if (player_color == cell_color.BLUE) color_to_check = cell_color.ORANGE;
                else color_to_check = cell_color.BLUE;
                List<cell> enemy_children = generate_moves(currentBoardState, color_to_check);
                if (enemy_children.isEmpty()) {
                    returnMove.setValue(evaluation(currentMove));
                    returnMove.setMove(currentMove);
                    return returnMove;
                }
            }

            //check valid moves
            //then run minimax on that

            miniMove currentChildVal = null;

            for (cell c: children){

                cell_color curr_col;
                if (ourMove) curr_col = cell_color.BLUE;
                else curr_col = cell_color.ORANGE;

                newBoardState = apply_move(currentBoardState, c, curr_col);
                currentChildVal = minimax(newBoardState, depth-1, !ourMove, c, alpha, beta);

                //alpha beta pruning
                if(ourMove){
                    if(alpha >= currentChildVal.getValue()){

                    }else{
                        childValues.add(currentChildVal);
                        alpha = currentChildVal.getValue();
                    }
                }else{
                    if(beta <= currentChildVal.getValue()){

                    }else{
                        childValues.add(currentChildVal);
                        beta = currentChildVal.getValue();
                    }
                }
            }

            if(ourMove){
                miniMove bestMove = new miniMove();
                bestMove.setValue(-106969690);

                for (miniMove childVal:childValues) {

                    if(childVal.getValue() > bestMove.getValue()){
                        bestMove = childVal;
                    }
                }
               // return childV;
            }else{
                miniMove worstMove = new miniMove();
                worstMove.setValue(1000000069);

                for (miniMove childVal:childValues) {

                    if (childVal.getValue() < worstMove.getValue()) {
                        worstMove = childVal;
                    }
                }
               // return minVal;
            }
        }

        returnMove.setValue(evaluation(currentMove));
        returnMove.setMove(currentMove);

        return returnMove;
    }


    private boolean better(int chosen_score, int best_score) {
        if(chosen_score > best_score){
            return true;
        }else{
            return false;
        }
    }

    private board apply_move(board board, cell cell, cell_color color) {

        board.place_piece(cell.getRow(), cell.getCol(), color);

        board.capture(cell);

        return board;
    }

    private List<cell> generate_moves(board currBoard, cell_color color) {
        List<cell> validMoves = new ArrayList<>();

        validMoves = currBoard.find_valid_moves(color);

        return validMoves;
    }



    private int evaluation(cell currentMove) {
        return 0;
    }

    /**
     * Generates a random move based on the current board state
     * @return
     */
    private cell randomMove(){
        Random rand = new Random();
        int upperbound = 8;
        cell move = null;
        List <cell> moves = generate_moves(curr_board, player_color);
        int int_random = rand.nextInt(upperbound);

        move = moves.get(int_random);

        return move;

    }

}
