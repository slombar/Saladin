package mainPackage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Agent {

    private String columnLetters = "ABCDEFGH";

    private String groupName;
    public CellColor playerColor;
    private Board currentBoard;

    public Agent(String group, CellColor color, String firstMove) {
        groupName = group;
        playerColor = color;
        currentBoard = new Board(playerColor);
        aiLoop();
    }

    public void aiLoop() {
        //check for groupname.go
        File groupFile = new File("../../../../referee/" + groupName + ".go");
        while (!groupFile.exists()) {/*languish*/}

        System.out.println("Found file: " + groupFile.getName());

        //if groupname.go exists, check for end_game file
        File endFile = new File("../../../../referee/end_game");
        if (endFile.exists()) return; //if that exists, game is done *crab rave*

        //if no end_game file exists, check for the move_file
        File moveFile = new File("../../../../referee/move_file");
        String opponentMove;
        try {
            Scanner moveScanner = new Scanner(moveFile);
            opponentMove = moveScanner.nextLine();
            System.out.println("Opponent's Move: " + opponentMove);

            String ourMove = chooseNextMove(opponentMove);
            System.out.println("Our Move: " + ourMove);

            FileWriter moveWriter = new FileWriter(moveFile,false);
            moveWriter.flush();
            moveWriter.write(ourMove);
            moveWriter.close();

            System.out.println("We printed the line: " + ourMove + " to the move_file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (groupFile.exists()) {/*wait until our group file gets removed*/}
        aiLoop(); //run aiLoop() again
    }

    public String chooseNextMove(String opponentMove) {
        if(opponentMove == "" || opponentMove == null){
            Cell ourCell = null;
            ourCell.setRow(4);
            ourCell.setCol(3);

            this.currentBoard = applyMove(currentBoard, ourCell, ourCell.getColor());
        }
        String[] opponentValueStrings = opponentMove.split(" ");
        int opponentCol = columnLetters.indexOf(opponentValueStrings[1]);
        int opponentRow = Integer.parseInt(opponentValueStrings[2]) - 1;
        Cell opponentCell = new Cell();
        opponentCell.setCol(opponentCol);
        opponentCell.setRow(opponentRow);
        if (playerColor == CellColor.BLUE) opponentCell.setColor(CellColor.ORANGE);
        else opponentCell.setColor(CellColor.BLUE);
        this.currentBoard = applyMove(currentBoard, opponentCell, opponentCell.getColor());

        Cell ourMove = randomMove();
        //MiniMove ourMove = minimax(currBoard, 3, true, null, -10000, 10000);

        //print output

        char colString   = columnLetters.charAt(ourMove.getCol());
        String rowString = String.valueOf(ourMove.getRow());

        return "Saladin " + rowString + " " + colString + "\n";
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
        boolean result = false;

        if(playerColor == possiblePlayerCell.getColor()){
            result = true;
        }

        return result;
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
            returnMove.setValue(evaluation(currentMove));
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
                    returnMove.setValue(evaluation(currentMove));
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

        returnMove.setValue(evaluation(currentMove));
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

        board.placePiece(cell.getRow(), cell.getCol(), color);

        board.capture(cell);

        return board;
    }

    private List<Cell> generateMoves(Board currBoard, CellColor color) {
        List<Cell> validMoves = new ArrayList<>();

        validMoves = currBoard.findValidMoves(color);

        return validMoves;
    }



    private int evaluation(Cell currentMove) {
        return 0;
    }

    /**
     * Generates a random move based on the current Board state
     * @return
     */
    private Cell randomMove(){
        Random rand = new Random();
        int upperbound = 8;
        Cell move = null;
        List <Cell> moves = generateMoves(currentBoard, playerColor);
        int intRandom = rand.nextInt(upperbound);

        move = moves.get(intRandom);

        return move;

    }

}
