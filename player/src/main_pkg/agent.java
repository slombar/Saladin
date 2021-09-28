package main_pkg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


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
     * Min max function
     * @return
     */
    public MinMaxMove minimax(board new_board, int depth, int max_depth, int our_best_score, int enemy_best_score){
        int chosen_score = 0;
        cell chosen_move = null;

        minimax(new_board, depth+1, max_depth, our_best_score, enemy_best_score);

        //if we are considering our move
            if(isPlayerCell(chosen_move) && chosen_score > our_best_score){
                if(chosen_score > enemy_best_score){

                }else{
                    our_best_score = chosen_score;
                }

            }
            //if we are considering an enemy move
            if(isEnemyCell(chosen_move) && chosen_score < enemy_best_score){
                if(chosen_score < enemy_best_score){

                }else{
                    enemy_best_score = chosen_score;
                }
            }

        MinMaxMove returnMove = null;
        returnMove.setMove(chosen_move);
        returnMove.setScore(chosen_score);

        return returnMove;
    }

}
