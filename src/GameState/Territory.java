package GameState;
/* spec แผนที่สร้าง region ขึ้นมา */
public class Territory {
    private final long rows;
    private final long cols;
    private Region[][] region;

    public Territory(long rows, long cols) {
        this.rows = rows;
        this.cols = cols;
        region = new Region[(int) rows][(int) cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                region[i][j] = new Region(i, j, 0);
            }
        }
    }

    public Region getRegion(long rows, long cols) {
        return region[((int) rows) - 1][((int) cols) - 1];
    }

    public Region getUp(long row, long col){
        if(row > 1) {
            return getRegion(row - 1, col);
        }
        return null;
    }

    public Region getUpRight(long row, long col){
        if(col < cols){
            if(col % 2 != 0){
                return getRegion(row, col + 1);
            }else{
                return getRegion(row-1, col + 1);
            }
        }
        return null;
    }

    public Region getDownRight(long row, long col){
        if (row < rows){
            if(col % 2 != 0){
                return getRegion(row + 1, col + 1);
            }else{
                return getRegion(row, col + 1);
            }
        }
        return null;
    }

    public Region getDown(long row, long col){
        if(row < rows) {
            return getRegion(row + 1, col);
        }
        return null;
    }

    public Region getDownLeft(long row, long col){
        if (col > 1){
            if(col % 2 != 0){
                return getRegion(row+1, col - 1);
            }else{
                return getRegion(row, col - 1);
            }
        }
        return null;
    }

    public Region getUpLeft(long row, long col){
        if (row > 1) {
            if(col % 2 != 0){
                return getRegion(row, col - 1);
            }else{
                return getRegion(row - 1, col - 1);
            }
        }
        return null;
    }
}
