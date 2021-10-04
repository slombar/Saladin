package mainPackage;

import java.util.ArrayList;
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
}
