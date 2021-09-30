package mainPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Got group name: " + args[0]);

        File groupNameFile = new File("../../../../referee/"+ args[0] + ".go");
        while(!groupNameFile.exists()) {

        }
        System.out.println("Found groupname.go");

        File moveFile = new File("../../../../referee/move_file");
        while(!moveFile.exists()) {}
        System.out.println("Found move file");

        String opponentMove;

        try {
            Scanner scanner = new Scanner(moveFile);
            opponentMove = scanner.nextLine();
            if (opponentMove.isEmpty()) {
                Agent agent = new Agent(args[0], CellColor.BLUE, opponentMove);
            }
            else {
                Agent agent = new Agent(args[0], CellColor.ORANGE, opponentMove);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
