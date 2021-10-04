package mainPackage;

public class MiniMove {
    double value = 0;
    Cell move = null;

    public MiniMove() {
        this.value = 0;
        this.move = new Cell();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Cell getMove() {
        return move;
    }

    public void setMove(Cell move) {
        this.move = move;
    }
}
