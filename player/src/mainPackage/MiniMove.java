package mainPackage;

public class MiniMove {
    int value = 0;
    Cell move = null;

    public MiniMove() {
        this.value = 0;
        this.move = new Cell();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Cell getMove() {
        return move;
    }

    public void setMove(Cell move) {
        this.move = move;
    }
}
