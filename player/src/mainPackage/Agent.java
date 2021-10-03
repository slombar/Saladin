package mainPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Agent {

    private String columnLetters = "ABCDEFGH  P";

    private int passIndex = 10;

    private String groupName;
    public CellColor playerColor;
    private Board currentBoard;
    private CellColor currentTurn;

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
            System.out.println(opponentMove);
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
        if(opponentMove == null || opponentMove.isEmpty()){
            ourMove.setRow(5);
            ourMove.setCol(4);
            ourMove.setColor(playerColor);

            return ourMove;
        }

        ourMove = randomMove();

        //MiniMove ourMove = minimax(currBoard, 3, true, null, -10000, 10000);

        //print output

        return ourMove;
    }

    public String moveToString(Cell move) {
        if (move.getCol() == passIndex) {
            return groupName + " " + "P 1\n";
        }
        char colString   = columnLetters.charAt(move.getCol());
        String rowString = String.valueOf(8 - move.getRow());
        return groupName + " " + colString + " " + rowString + "\n";
    }

    public Cell interpretMoveString(String move) {

        String[] valueStrings = move.split(" ");
        int col = columnLetters.indexOf(valueStrings[1]);
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

    /**
     * Min max function with alpha beta pruning
     * @return a minmaxmove data structure, that contains a chosen score and a chosen move
     */
    public MiniMove minimax(Board currentBoardState, int depth, boolean ourMove, Cell currentMove, int alpha, int beta){
        Board newBoardState = null;
        MiniMove returnMove = new MiniMove();
        List <Cell> children = null;
        List <MiniMove> childValues = null;

        if(depth == 0){
            // TODO needs to take board in
            // returnMove.setValue(evaluation(currentMove));
            returnMove.setMove(currentMove);
            return returnMove;
        }else{
            children =  generateMoves(currentBoardState, playerColor);

            //check if end state
            if (children.isEmpty()) {
                //if we have no moves, check if the enemy also has no moves
                CellColor colorToCheck;
                if (playerColor == CellColor.BLUE) colorToCheck = CellColor.ORANGE;
                else colorToCheck = CellColor.BLUE;
                List<Cell> enemyChildren = generateMoves(currentBoardState, colorToCheck);
                if (enemyChildren.isEmpty()) {
                    // TODO needs to take board in
                    // returnMove.setValue(evaluation(currentMove));
                    returnMove.setMove(currentMove);
                    return returnMove;
                }
            }

            //check valid moves
            //then run minimax on that

            MiniMove currentChildVal = null;

            for (Cell c: children){

                CellColor currCol;
                if (ourMove) currCol = CellColor.BLUE;
                else currCol = CellColor.ORANGE;

                newBoardState = applyMove(currentBoardState, c, currCol);
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
                MiniMove bestMove = new MiniMove();
                bestMove.setValue(-106969690);

                for (MiniMove childVal:childValues) {

                    if(childVal.getValue() > bestMove.getValue()){
                        bestMove = childVal;
                    }
                }
               // return childV;
            }else{
                MiniMove worstMove = new MiniMove();
                worstMove.setValue(1000000069);

                for (MiniMove childVal:childValues) {

                    if (childVal.getValue() < worstMove.getValue()) {
                        worstMove = childVal;
                    }
                }
               // return minVal;
            }
        }

        // TODO returnMove.setValue(evaluation(currentMove));
        returnMove.setMove(currentMove);

        return returnMove;
    }


    private boolean better(int chosenScore, int bestScore) {
        if(chosenScore > bestScore){
            return true;
        }else{
            return false;
        }
    }

    private Board applyMove(Board board, Cell cell, CellColor color) {

        if (cell.getCol() == passIndex) {
            // Pass
            board.currentColor = Board.getOppositeColor(color);
            return board;
        }

        board.placePiece(cell.getRow(), cell.getCol(), color);

        board.capture(cell);

        board.currentColor = Board.getOppositeColor(color);

        return board;
    }

    private List<Cell> generateMoves(Board currBoard, CellColor color) {
        List<Cell> validMoves = new ArrayList<>();

        validMoves = currBoard.findValidMoves(color);

        return validMoves;
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
        List <Cell> moves = generateMoves(currentBoard, playerColor);
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
    }

}
