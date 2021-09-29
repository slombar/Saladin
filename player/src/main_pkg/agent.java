package main_pkg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
int chosen_score = 0;
        cell chosen_move = null;
        MinMaxMove returnMove = null;

        //minmax was here before

        int best_score = 0;
        cell best_move = null;

        List<cell> moves_list = new ArrayList<>();

        if(depth == max_depth){
            chosen_score = evaluation();
        }else {
            moves_list = generate_moves();
            if (moves_list == null) {
                chosen_score = evaluation();
            } else {
                for (int i = 1; i < moves_list.size(); i++) {
                    best_score = 10000000;

                    apply_move(new_board, moves_list.get(i));

                    min_max(new_board, depth + 1, max_depth);

                    if (better(chosen_score, best_score)) {
                        best_score = chosen_score;
                        best_move = chosen_move;
                    }

                }
                chosen_score = best_score;
                chosen_move = best_move;
            }
        }

        //abpruning
        minimax(new_board, depth+1, max_depth, our_best_score, enemy_best_score);

                //if we are considering our move

                if (isPlayerCell(chosen_move) && chosen_score > our_best_score) {
                if (chosen_score > enemy_best_score) {

                } else {
                our_best_score = chosen_score;
                }
                }
                //if we are considering an enemy move
                if (isEnemyCell(chosen_move) && chosen_score < enemy_best_score) {
        if (chosen_score < enemy_best_score) {

        } else {
        enemy_best_score = chosen_score;
        }
        }

        chosen_score = evaluate(new_board);
        returnMove.setMove(chosen_move);
        returnMove.setScore(chosen_score);
        return returnMove;


        return minimax(new_board, depth+1, max_depth, our_best_score, enemy_best_score);
 */

public class agent {

    private String groupname;
    public cell_color player_color;
    private board curr_board = new board(player_color);

    public agent(String group, cell_color color, String first_move) {
        groupname = group;
        player_color = color;
        ai_loop();
    }

    public void ai_loop() {
        //check for groupname.go
        File group_file = new File(groupname + ".go");
        while (!group_file.exists()) {/*languish*/}

        //if that exists, check for end_game
        File end_file = new File("end_game");
        if (end_file.exists()) return; //if that exists, game is done *crab rave*

        //if no end_game, czech move_file
        File move_file = new File("move_file");
        String opponent_move;
        try {
            Scanner move_scanner = new Scanner(move_file);
            opponent_move = move_scanner.nextLine();

            String our_move = choose_next_move(opponent_move);

            FileWriter move_writer = new FileWriter(move_file,false);
            move_writer.flush();
            move_writer.write(our_move);
            move_writer.close();
            System.out.println("Got through test AI loop");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (group_file.exists()) {/*wait until our group file gets removed*/}
        ai_loop(); //run ai_loop() again
    }

    public String choose_next_move(String opponent_move) {
        return "test";
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
        miniMove returnMove = null;
        List <cell> children = null;
        List <miniMove> childValues = null;

        if(depth == 0){
            returnMove.setValue(evaluation(currentMove));
            returnMove.setMove(currentMove);
            return returnMove;
        }else{
            children =  generate_moves(currentBoardState);

            //check valid moves
            //then run minimax on that

            miniMove currentChildVal = null;

            for (cell c: children){

                newBoardState = apply_move(currentBoardState, c);
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

    private board apply_move(board new_board, cell cell) {
        return new_board;
    }

    private List<cell> generate_moves(board currBoard) {

        return null;
    }

    private int evaluation(cell currentMove) {
        return 0;
    }

}
