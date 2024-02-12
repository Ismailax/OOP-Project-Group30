package GameState;

public class Territory {
    private final long rows;
    private final long cols;
    private Region[][] region;

    public Territory(long rows, long cols) {
        this.rows = rows;
        this.cols = cols;
        region = new Region[(int) rows][(int) cols];
    }
}
