package GameState;
import java.lang.Math;
/* spec พื้นที่ในแต่ละ cell*/
public class Region {
    private final long row;
    private final long col;
    private long deposit;
    private Player ownner;
    private Boolean isCityCenter;
    private long init_center_dep;

    public Region(long row, long col, long deposit) {
        this.row = row;
        this.col = col;
        this.deposit = deposit;
    }
    public long getDeposit(){
        return deposit;
    }

    public void relocate(){

    }
}
