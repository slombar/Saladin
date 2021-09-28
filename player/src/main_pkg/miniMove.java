package main_pkg;

public class miniMove {
    int value = 0;
    cell move = null;

    public miniMove() {
        this.value = 0;
        this.move = new cell();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public cell getMove() {
        return move;
    }

    public void setMove(cell move) {
        this.move = move;
    }
}
