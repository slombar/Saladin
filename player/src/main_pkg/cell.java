package main_pkg;

public class cell {
    private cell_color color;
    private int row;
    private int col;

    /**
     * Constructor for the cell class
     * @param cellColor, the color of the cell
     * @param cellRow, the row of the board the cell is located in
     * @param cellCol, the column of the board the cell is located in
     */
    cell(cell_color cellColor, int cellRow, int cellCol){
        this.col = cellCol;
        this.row = cellRow;
        this.color = cellColor;
    }

    cell(){
        this.col = 0;
        this.row = 0;
        this.color = cell_color.ORANGE;
    }
    /**
     * returns the opposite color of the given cell
     * @return blue if given cell is orange, orange if given cell is blue, and empty if cell is empty
     */
    public cell_color getOppositeColor(){

        //check for empty, if not proceed
        if(getColor() != cell_color.EMPTY) {

            //retrieve opposite color
            if (getColor() == cell_color.BLUE) {
                return cell_color.ORANGE;
            } else {
                return cell_color.BLUE;
            }
        }
        //default, return empty
        return cell_color.EMPTY;
    }

    /**
     * get a cell's color
     * @return
     */
    public cell_color getColor() {
        return color;
    }

    /**
     * set a cell's color
     * @param color
     */
    public void setColor(cell_color color) {
        this.color = color;
    }

    /**
     * get the row of a cell
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * set the row of a cell
     * @param row
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * get the column of a cell
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
