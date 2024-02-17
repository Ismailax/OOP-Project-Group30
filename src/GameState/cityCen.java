package GameState;

public class cityCen extends Region{
    private long col;
    private long row;
    public cityCen(long row, long col, long deposit) {
        super(row, col, deposit);
    }
    public void relocate(long newRow, long newCol){ // relocate ไปยังพื้นที่อื่น
        this.row = newRow;
        this.col = newCol;
    }
}
