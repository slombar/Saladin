package main_pkg;

public class MinMaxMove {
    int score = 0;
    cell move = null;

    public MinMaxMove(int score, cell move) {
        this.score = score;
        this.move = move;
    }

    public int getScore() {
        return score;
    }

    public cell getMove() {
        return move;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMove(cell move) {
        this.move = move;
    }
}
