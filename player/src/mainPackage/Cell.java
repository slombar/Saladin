package mainPackage;

/**
 * Stores a specific cell on the board, including its color, row, and column.
 */
public class Cell {
    private CellColor color;
    private int row;
    private int col;

    /**
     * Constructor for the Cell class
     * @param cellColor, the color of the Cell
     * @param cellRow, the row of the Board the Cell is located in
     * @param cellCol, the column of the Board the Cell is located in
     */
    Cell(CellColor cellColor, int cellRow, int cellCol){
        this.col = cellCol;
        this.row = cellRow;
        this.color = cellColor;
    }

    /**
     * Default constructor for creating a Cell.
     */
    Cell(){
        this.col = 0;
        this.row = 0;
        this.color = CellColor.ORANGE;
    }

    /**
     * get a Cell's color
     */
    public CellColor getColor() {
        return color;
    }

    /**
     * set a Cell's color
     */
    public void setColor(CellColor color) {
        this.color = color;
    }

    /**
     * get the row of a Cell
     */
    public int getRow() {
        return row;
    }

    /**
     * set the row of a Cell
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * get the column of a Cell
     */
    public int getCol() {
        return col;
    }

    /**
     * set the column of a color
     */
    public void setCol(int col) {
        this.col = col;
    }
}
