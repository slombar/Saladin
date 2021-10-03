package mainPackage;

import java.util.ArrayList;
import java.util.List;

public class MinimaxAgent {
    Board startingBoardState;
    CellColor startingTurn;
    boolean isOurTurn = true;
    int maxDepth;
    int POSITIVE_INFINITY = 10000000;
    int NEGATIVE_INFINITY = -10000000;

    Agent agent;

    public MinimaxAgent(Board startingBoardState, CellColor startingTurn, int maxDepth, Agent agent) {
        this.startingBoardState = startingBoardState;
        this.startingTurn = startingTurn;
        this.maxDepth = maxDepth;
        this.agent = agent;

    }

    public Cell getMinimaxMove() {
        return minimax(startingBoardState, new Cell(), startingTurn, isOurTurn, maxDepth, NEGATIVE_INFINITY, POSITIVE_INFINITY)
                .getMove();
    }

    private MiniMove minimax(Board currentBoardState, Cell currentMove, CellColor currentTurn, boolean isOurTurn,
                             int currentDepth, int alpha, int beta) {
        List<Cell> childrenMoves;
        List<MiniMove> childrenMiniMoves = new ArrayList<>();

        // If at end of depth, end recursion
        if (currentDepth == 0) {
            return copyCurrentMove(currentBoardState, currentMove);
        }

        childrenMoves = currentBoardState.findValidMoves(currentTurn);

        // If end state, end recursion
        if (isEndState(currentBoardState, currentTurn, childrenMoves)) {
            return copyCurrentMove(currentBoardState, currentMove);
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
                    childrenMiniMoves.add(currentChildMiniMove);
                }
            }
            else {
                if (beta > currentChildMiniMove.getValue()) {
                    beta = currentChildMiniMove.getValue();
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

    private boolean isEndState(Board currentBoardState, CellColor currentTurn, List<Cell> availableMoves) {
        if (availableMoves.isEmpty()) {
            List<Cell> enemyMoves = currentBoardState.findValidMoves(Board.getOppositeColor(currentTurn));
            return enemyMoves.isEmpty();
        }
        return false;
    }

    private int evaluateBoardState(Board currentBoardState) {
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

        // TODO improve eval function

        return sum;
    }
}
