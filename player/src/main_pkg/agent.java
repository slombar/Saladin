package main_pkg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public int min_max(){
        int result = 0;

        return result;
    }
}
