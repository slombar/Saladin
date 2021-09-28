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

    /**
     * Evaluation function for minmax
     * @return
     */
    public int eval_function(board b){
        return 0;
    }

    /**
     * Min max function
     * @return
     */
    public MinMaxMove min_max(board the_board, int depth, int max_depth){
        /*
        minimax(in game board, in int depth, in int max_depth,
        out score chosen_score, out score chosen_move)
  begin
    if (depth = max_depth) then
        chosen_score = evaluation(board);
   ` else
        moves_list = generate_moves(board);
       ` if (moves_list = NULL) then
        `    chosen_score = evaluation(board);
       ` else
            for (i = 1 to moves_list.length) do
                best_score = infinity;
                new_board = board;
                `apply_move(new_board, moves_list[i]);
                minimax(new_board, depth+1, max_depth, current_score, current_move);
                if (better(current_score, best_score)) then
                    best_score = current_score;
                    best_move = current_move;
                endif
            enddo
            chosen_score = best_score;
            chosen_move = best_move;
        endif
    endif
end.
         */
        int chosen_score = 0;
        cell chosen_move = null;
        int best_score = 0;
        cell best_move = null;

        List<cell> moves_list = new ArrayList<>();

        if(depth == max_depth){
            chosen_score = evaluation();
        }else {
            moves_list = generate_moves();
            if (moves_list == null) {
                chosen_score = evaluation();
            }else{
                for(int i = 1; i <moves_list.size();i++){
                    best_score = 10000000;
                    board new_board = null;

                    apply_move(new_board, moves_list.get(i));

                    min_max(new_board, depth+1, max_depth);

                    if (better(chosen_score, best_score)){
                        best_score = chosen_score;
                        best_move = chosen_move;
                    }

                }
                chosen_score = best_score;
                chosen_move = best_move;
            }
        }

        MinMaxMove returnMove = null;
        returnMove.setMove(chosen_move);
        returnMove.setScore(chosen_score);

        return returnMove;
    }

    public int evaluation(){
        return 0;
    }

    public List<cell> generate_moves(){
        List<cell> moves_list = new ArrayList<>();
        return moves_list;
    }
    public void apply_move(board new_board, cell cell){


    }
    public boolean better(int the_score, int best_score){

        return true;

    }
}
