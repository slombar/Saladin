package mainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Waits for the correct .go file from the referee, then initializes the agent depending on the turn order.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Got group name: " + args[0]);

        File groupNameFile = new File("referee/"+ args[0] + ".go");
        System.out.println(groupNameFile.getAbsolutePath());
        while(!groupNameFile.exists()) {

        }
        System.out.println("Found groupname.go");

        File moveFile = new File("referee/move_file");
        while(!moveFile.exists()) {}
        System.out.println("Found move file");

        String opponentMove;

        try {
            Scanner scanner = new Scanner(moveFile);
            if (scanner.hasNext()) {
                // Opponent move exists
                opponentMove = scanner.nextLine();
                Agent agent = new Agent(args[0], CellColor.ORANGE, opponentMove);
            }
            else {
                Agent agent = new Agent(args[0], CellColor.BLUE, "");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
