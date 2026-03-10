package tile;

import board.Board;
import enums.TileStatus;

public class Tile {

    protected TileStatus status;
    private int minesAround;
    protected Board board;
    protected int x;
    protected int y;

    public Tile(Board board, int x, int y) {
        this.board = board;
        status = TileStatus.closed;
        minesAround = 0;
        this.x = x;
        this.y = y;
    }

    public boolean click()
    {
        if(status == TileStatus.closed)
        {
            status = TileStatus.opened;
        }
        if(minesAround != 0)return true;
        if(board.getTileAt(x - 1,y) != null)board.getTileAt(x - 1,y).trigger();
        if(board.getTileAt(x + 1,y) != null)board.getTileAt(x + 1,y).trigger();
        if(board.getTileAt(x,y - 1) != null)board.getTileAt(x,y - 1).trigger();
        if(board.getTileAt(x,y + 1) != null)board.getTileAt(x,y + 1).trigger();
        return true;
    }

    public void trigger()
    {
        if(status == TileStatus.closed)
        {
            status = TileStatus.opened;
        }
        if(minesAround != 0)return;//if this a numbered tile then stop recursion
        if(board.getTileAt(x - 1,y) != null)board.getTileAt(x - 1,y).trigger();
        if(board.getTileAt(x + 1,y) != null)board.getTileAt(x + 1,y).trigger();
        if(board.getTileAt(x,y - 1) != null)board.getTileAt(x,y - 1).trigger();
        if(board.getTileAt(x,y + 1) != null)board.getTileAt(x,y + 1).trigger();

    }

    public void flag()
    {
        if(status != TileStatus.opened)
        {
            if(status == TileStatus.closed) status = TileStatus.flagged;
            else if(status == TileStatus.flagged) status = TileStatus.closed;
        }
    }

    public int getMinesAround() {
        return minesAround;
    }

    public void addMinesAround() {
        this.minesAround ++;
    }

    public TileStatus getStatus() {
        return status;
    }
}
