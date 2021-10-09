package mainPackage;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MinimaxAgent {
    Board startingBoardState;
    CellColor startingTurn;
    boolean isOurTurn = true;
    int maxDepth;

    double POSITIVE_INFINITY = 10000000;
    double NEGATIVE_INFINITY = -10000000;
    static double DISKS_ONLY_EVAL_WEIGHT = 1 / 100.0;
    static double NUM_MOVES_EVAL_WEIGHT = 1.0;
    static double CORNER_CELLS_EVAL_WEIGHT = 10.0;
    static double CORNER_ADJACENT_EVAL_WEIGHT = -3.0;

    long timeLimitMillis;
    long adjustedTimeLimit;
    long timerStart;

    Agent agent;

    public MinimaxAgent(Board startingBoardState, CellColor startingTurn, int maxDepth, Agent agent, long timeLimitMillis) {
        this.startingBoardState = startingBoardState;
        this.startingTurn = startingTurn;
        this.maxDepth = maxDepth;
        this.agent = agent;
        this.timeLimitMillis = timeLimitMillis;
        this.adjustedTimeLimit = timeLimitMillis - (timeLimitMillis / 4);
    }

    public Cell getMinimaxMove() {
        timerStart = System.currentTimeMillis();
        return minimax(startingBoardState, new Cell(), startingTurn, isOurTurn, maxDepth, NEGATIVE_INFINITY, POSITIVE_INFINITY)
                .getMove();
    }

    private MiniMove minimax(Board currentBoardState, Cell currentMove, CellColor currentTurn, boolean isOurTurn,
                             int currentDepth, double alpha, double beta) {
        List<Cell> childrenMoves;
        List<MiniMove> childrenMiniMoves = new ArrayList<>();

        long timeDiff = System.currentTimeMillis() - timerStart;
        if (timeDiff > adjustedTimeLimit) {
            return copyCurrentMove(currentBoardState, currentMove);
        }

        // If at end of depth, end recursion
        if (currentDepth == 0) {
            return copyCurrentMove(currentBoardState, currentMove);
        }

        childrenMoves = currentBoardState.findValidMoves();

        if (childrenMoves.isEmpty()) {
            // Create a new board state with us having passed, then check if enemy can move
            Cell passMove = new Cell();
            passMove.setCol(agent.PASS_INDEX);
            passMove.setRow(1);
            Board passedBoardState = agent.applyMove(currentBoardState.deepCopy(), passMove, currentTurn);
            List<Cell> enemyMoves = passedBoardState.findValidMoves();
            if (enemyMoves.isEmpty()) {
                return copyCurrentMove(currentBoardState, currentMove);
            }
            childrenMoves.add(passMove);
        }

        MiniMove currentChildMiniMove;

        for (Cell currentChild: childrenMoves) {
            Board newBoardState = agent.applyMove(currentBoardState.deepCopy(), currentChild, currentTurn);
            currentChildMiniMove =
                    minimax(newBoardState,
                    currentChild, Board.getOppositeColor(currentTurn),
                    !isOurTurn, currentDepth - 1, alpha, beta);

            // Alpha Beta pruning
            if (isOurTurn) {
                if (alpha < currentChildMiniMove.getValue()) {
                    alpha = currentChildMiniMove.getValue();
                    currentChildMiniMove.setMove(currentChild);
                    childrenMiniMoves.add(currentChildMiniMove);
                }
            }
            else {
                if (beta > currentChildMiniMove.getValue()) {
                    beta = currentChildMiniMove.getValue();
                    currentChildMiniMove.setMove(currentChild);
                    childrenMiniMoves.add(currentChildMiniMove);
                }
            }
        }

        if (isOurTurn) {
            MiniMove bestMove = new MiniMove();
            bestMove.setValue(NEGATIVE_INFINITY);
            for (MiniMove childMiniMove : childrenMiniMoves) {
                if (childMiniMove.getValue() > bestMove.getValue()) {
                    bestMove = childMiniMove;
                }
            }
            return bestMove;
        }
        else {
            MiniMove worstMove = new MiniMove();
            worstMove.setValue(POSITIVE_INFINITY);
            for (MiniMove childMiniMove : childrenMiniMoves) {
                if (childMiniMove.getValue() < worstMove.getValue()) {
                    worstMove = childMiniMove;
                }
            }
            return worstMove;
        }

    }

    private MiniMove copyCurrentMove(Board currentBoardState, Cell currentMove) {
        MiniMove move = new MiniMove();
        move.setMove(currentMove);
        move.setValue(evaluateBoardState(currentBoardState));
        return move;
    }

    public static double evaluateBoardState(Board currentBoardState) {
        double sum = 0;
        sum += (evaluateBoardStateDisksOnly(currentBoardState) * DISKS_ONLY_EVAL_WEIGHT);
        sum += (evaluateBoardNumMoves(currentBoardState) * NUM_MOVES_EVAL_WEIGHT);

        sum += (evaluateBoardNumCorners(currentBoardState) * CORNER_CELLS_EVAL_WEIGHT);
        sum += (evaluateBoardNumNextToCorner(currentBoardState) * CORNER_ADJACENT_EVAL_WEIGHT);
        return sum;
    }

    private static int evaluateBoardStateDisksOnly(Board currentBoardState) {
        int sum = 0;
        for (int row = currentBoardState.boardMin; row < currentBoardState.boardMax; row++) {
            for (int col = currentBoardState.boardMin; col < currentBoardState.boardMax; col++) {

                //determine the current Cell
                Cell currentCell = currentBoardState.board[row][col];
                if (currentBoardState.isPlayerCell(currentCell)) {
                    sum++;
                }
                else if (currentBoardState.isEnemyCell(currentCell)) {
                    sum--;
                }

            }
        }

        return sum;
    }

    private static int evaluateBoardNumMoves(Board currentBoardState) {
        return currentBoardState.findValidMoves().size();
    }

    /**
     * Evaluation function for board corners, if corner >0 if adjacent to corner <0
     * @param currentBoardState
     * @return a heuristic value for the board given that there are corner or corner adjacent cells
     *
     * corner cells
     * (0,0) (7,7) (0,7) (7,0)
     *
     * corner adjacent cells
     * (0,1) (1,0) (1,1) (7,6) (6,7) (6,6) (1,7) (0,6) (1,6) (7,1) (6,0) (6,1)
     */
    private static int evaluateBoardNumCorners(Board currentBoardState) {
        int sum = 0;;
        int[][] cornerCells = {{0, 0}, {7, 7}, {0, 7}, {7, 0}};
        for(int[] cornerCell : cornerCells) {
            Cell currentCell = currentBoardState.board[cornerCell[0]][cornerCell[1]];
            if (currentBoardState.isPlayerCell(currentCell)) {
                sum++;
            }
            else if (currentBoardState.isEnemyCell(currentCell)) {
                sum--;
            }
        }
        return sum;
    }

    /** Evaluates the board state if we are next to a corner
     * @param currentBoardState, the current board state
     * Corner adjacent cells
     * {{0, 1}, {1, 0}, {1, 1},
     * {7, 6}, {6, 7}, {6, 6},
     * {1, 7}, {0, 6}, {1, 6},
     * {7, 1}, {6, 0}, {6, 1}}

     * @return
     */
    private static int evaluateBoardNumNextToCorner(Board currentBoardState) {
        int sum =0;
        ArrayList<int[]> cornerAdjacentCells = new ArrayList<>();

        if (!currentBoardState.isPlayerCell(currentBoardState.board[0][0])) {
            cornerAdjacentCells.add(new int[]{0, 1});
            cornerAdjacentCells.add(new int[]{1, 0});
            cornerAdjacentCells.add(new int[]{1, 1});
        }
        if (!currentBoardState.isPlayerCell(currentBoardState.board[7][7])) {
            cornerAdjacentCells.add(new int[]{7, 6});
            cornerAdjacentCells.add(new int[]{6, 7});
            cornerAdjacentCells.add(new int[]{6, 6});
        }
        if (!currentBoardState.isPlayerCell(currentBoardState.board[0][7])) {
            cornerAdjacentCells.add(new int[]{1, 7});
            cornerAdjacentCells.add(new int[]{0, 6});
            cornerAdjacentCells.add(new int[]{1, 6});
        }
        if (!currentBoardState.isPlayerCell(currentBoardState.board[7][0])) {
            cornerAdjacentCells.add(new int[]{7, 1});
            cornerAdjacentCells.add(new int[]{6, 0});
            cornerAdjacentCells.add(new int[]{6, 1});
        }

        for (int[] cornerAdjacentCell : cornerAdjacentCells) {
            Cell currentCell = currentBoardState.board[cornerAdjacentCell[0]][cornerAdjacentCell[1]];
            if (currentBoardState.isPlayerCell(currentCell)) {
                sum++;
            } else if (currentBoardState.isEnemyCell(currentCell)) {
                sum--;
            }
        }

        return sum;
    }
}
