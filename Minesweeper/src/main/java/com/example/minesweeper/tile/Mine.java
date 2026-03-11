package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;

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
