package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;

public class Mine extends Tile{
    public Mine(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean click() {

        if(Board.getBoard().isShielded()){
            Board.getBoard().useShield();
            this.flag();
            System.out.println("Shield used!");

            return true;
        }

        if(status == TileStatus.flagged)
        {
            System.out.println("This tile is flagged!");
            return true;
        }
        if(status == TileStatus.closed)
        {
            status = TileStatus.exploded;
            this.tileView.update(this.status);
        }

        //If flagged, clicking does nothing
        if (status == TileStatus.flagged) {
            System.out.println("This tile is flagged!");
            return true;
        }

        return false;
    }

    @Override
    public void trigger() {
    }

    @Override
    public void endgameReveal(){
        if(status != TileStatus.exploded){
            status = TileStatus.minetriggered;
            tileView.update(status);
        }
    }
}
