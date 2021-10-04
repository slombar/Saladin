package mainPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Agent {

    public String COLUMN_LETTERS = "ABCDEFGH  P";

    public int PASS_INDEX = 10;

    private String groupName;
    public CellColor playerColor;
    private Board currentBoard;
    private CellColor currentTurn;

    private int MAX_DEPTH = 6;

    public int turnCounter = 1;

    public Agent(String group, CellColor color, String firstMove) {
        groupName = group;
        playerColor = color;
        if (firstMove.isEmpty()) {
            currentTurn = playerColor;
        }
        else {
            currentTurn = Board.getOppositeColor(playerColor);
        }
        currentBoard = new Board(playerColor, currentTurn);
        aiLoop();
    }

    private boolean goFileExists() {
        File goFile = new File("referee/" + groupName + ".go");
        return goFile.exists();
    }

    private boolean endGameFileExists() {
        File endFile = new File("referee/end_game");
        return endFile.exists();
    }

    private String readOpponentMove(File moveFile) {
        String opponentMove = null;

        try {
            Scanner moveScanner = new Scanner(moveFile);
            opponentMove = moveScanner.nextLine();
            System.out.println("Opponent's Move: " + opponentMove);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return opponentMove;
    }

    private void writeOurMoveToFile(File moveFile, String ourMove) {
        FileWriter moveWriter;
        try {
            moveWriter = new FileWriter(moveFile,false);
            moveWriter.flush();
            moveWriter.write(ourMove);
            moveWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("We printed the line: " + ourMove + " to the move_file.");
    }

    public void aiLoop() {
        //check for groupname.go
        System.out.println("Checking for groupname.go");
        while (!goFileExists() && !endGameFileExists()) {}

        //if groupname.go exists, check for end_game file
        if (endGameFileExists()) {
            return;
        }

        //if no end_game file exists, check for the move_file
        File moveFile = new File("referee/move_file");
        String opponentMove = "";
        if (currentTurn != playerColor) {
            opponentMove = readOpponentMove(moveFile);
            System.out.println("Opponent Move: " + opponentMove);
            applyMove(currentBoard, interpretMoveString(opponentMove), currentTurn);
            System.out.println(currentBoard.boardToString());
            updateTurn();
        }
        Cell ourMove = chooseNextMove(opponentMove);
        System.out.println("Our Move: " + moveToString(ourMove));

        writeOurMoveToFile(moveFile, moveToString(ourMove));

        applyMove(currentBoard, ourMove, currentTurn);
        System.out.println(currentBoard.boardToString());
        updateTurn();
        while (goFileExists() && !endGameFileExists()) {/*wait until our group file gets removed*/}
        aiLoop(); //run aiLoop() again
    }

    public Cell chooseNextMove(String opponentMove) {
        Cell ourMove = new Cell();

        /*
        if(opponentMove == null || opponentMove.isEmpty()){
            ourMove.setRow(5);
            ourMove.setCol(4);
            ourMove.setColor(playerColor);

            return ourMove;
        }
        */

        // ourMove = randomMove();

        MinimaxAgent minimaxAgent = new MinimaxAgent(currentBoard, currentTurn, MAX_DEPTH, this);
        ourMove = minimaxAgent.getMinimaxMove();

        return ourMove;
    }

    public String moveToString(Cell move) {
        if (move.getCol() == PASS_INDEX) {
            return groupName + " " + "P 1\n";
        }
        char colString   = COLUMN_LETTERS.charAt(move.getCol());
        String rowString = String.valueOf(8 - move.getRow());
        return groupName + " " + colString + " " + rowString + "\n";
    }

    public Cell interpretMoveString(String move) {

        String[] valueStrings = move.split(" ");
        int col = COLUMN_LETTERS.indexOf(valueStrings[1]);
        int row = 8 - Integer.parseInt(valueStrings[2]);

        Cell moveCell = new Cell();
        moveCell.setCol(col);
        moveCell.setRow(row);
        moveCell.setColor(currentTurn);

        System.out.println("Their move text is: " + move);
        System.out.println("Their move internally is col: " + moveCell.getCol() + ", row: " + moveCell.getRow());
        System.out.println("Their move is translated to: " + moveToString(moveCell));

        return moveCell;
    }

    public boolean isEnemyCell(Cell possibleEnemyCell){
        boolean result = true;

        if(playerColor == possibleEnemyCell.getColor()){
            result = false;
        }

        return result;
    }

    /**
     * Evaluation function for minmax
     * @return
     */
    public int evalFunction(Board b){
        return 0;
    }

    public boolean isPlayerCell(Cell possiblePlayerCell){
        return playerColor == possiblePlayerCell.getColor();
    }

    private boolean better(int chosenScore, int bestScore) {
        if(chosenScore > bestScore){
            return true;
        }else{
            return false;
        }
    }

    public Board applyMove(Board board, Cell cell, CellColor color) {

        if (cell.getCol() == PASS_INDEX) {
            // Pass
            board.currentColor = Board.getOppositeColor(color);
            return board;
        }

        board.placePiece(cell.getRow(), cell.getCol(), color);

        board.capture(cell);

        board.currentColor = Board.getOppositeColor(color);

        return board;
    }

    /**
     * Given a board state, evaluate the board.
     * @param currBoard
     * @return
     */
    private int evaluation(Board currBoard) {
        int sum = 0;
        for (int row = currBoard.boardMin; row < currBoard.boardMax; row++) {
            for (int col = currBoard.boardMin; col < currBoard.boardMax; col++) {

                //determine the current Cell
                Cell currentCell = currBoard.board[row][col];
                if (currBoard.isPlayerCell(currentCell)) {
                    sum++;
                }
                else if (currBoard.isEnemyCell(currentCell)) {
                    sum--;
                }

            }
        }
        return sum;
    }

    /**
     * Generates a random move based on the current Board state
     * @return
     */
    private Cell randomMove(){
        Random rand = new Random();
        Cell move = null;
        List <Cell> moves = currentBoard.findValidMoves();
        for (Cell testMove : moves) {
            System.out.println("Testmove: " + moveToString(testMove));
        }
        for (Cell testMove : moves) {
            System.out.println("Testmove internal: Col: " + testMove.getCol() + ", Row: " + testMove.getRow());
        }

        if (moves.size() == 0) {
            return null;
        }
        int intRandom = rand.nextInt(moves.size());

        move = moves.get(intRandom);

        return move;

    }

    private void updateTurn() {
        currentTurn = Board.getOppositeColor(currentTurn);
        turnCounter += 1;
    }

}
