package tile;

import board.Board;
import enums.TileStatus;

public class Mine extends Tile{
    public Mine(Board board, int x, int y) {
        super(board, x, y);
    }

    @Override
    public boolean click() {
        if(status == TileStatus.closed)
        {
            status = TileStatus.opened;
        }
        return false;
    }

    @Override
    public void trigger() {
    }
}
