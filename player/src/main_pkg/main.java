package main_pkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        File groupname_file = new File(args[0] + ".go");

        while(!groupname_file.exists()) {}

        File move_file = new File("move_file");

        String opponent_move;

        try {
            Scanner scanner = new Scanner(move_file);
            opponent_move = scanner.nextLine();
            if (opponent_move.isEmpty()) {
                agent agent = new agent(args[0], cell.BLUE, opponent_move);
            }
            else {
                agent agent = new agent(args[0], cell.ORANGE, opponent_move);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
