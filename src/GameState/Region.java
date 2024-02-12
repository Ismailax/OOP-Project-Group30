package GameState;

public class Region {
    private final long row;
    private final long col;
    private long deposit;
    private Player ownner;

    public Region(long row, long col, long deposit) {
        this.row = row;
        this.col = col;
        this.deposit = deposit;
    }

}
