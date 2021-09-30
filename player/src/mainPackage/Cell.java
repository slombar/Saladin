package mainPackage;

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

    Cell(){
        this.col = 0;
        this.row = 0;
        this.color = CellColor.ORANGE;
    }
    /**
     * returns the opposite color of the given Cell
     * @return blue if given Cell is orange, orange if given Cell is blue, and empty if Cell is empty
     */
    public CellColor getOppositeColor(){

        //check for empty, if not proceed
        if(getColor() != CellColor.EMPTY) {

            //retrieve opposite color
            if (getColor() == CellColor.BLUE) {
                return CellColor.ORANGE;
            } else {
                return CellColor.BLUE;
            }
        }
        //default, return empty
        return CellColor.EMPTY;
    }

    /**
     * get a Cell's color
     * @return
     */
    public CellColor getColor() {
        return color;
    }

    /**
     * set a Cell's color
     * @param color
     */
    public void setColor(CellColor color) {
        this.color = color;
    }

    /**
     * get the row of a Cell
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * set the row of a Cell
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * get the column of a Cell
     * @return
     */
    public int getCol() {
        return col;
    }

    /**
     * set the column of a color
     * @param col
     */
    public void setCol(int col) {
        this.col = col;
    }
}
