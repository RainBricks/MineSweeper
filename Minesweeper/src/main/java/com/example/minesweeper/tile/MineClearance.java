package com.example.minesweeper.tile;

import com.example.minesweeper.enums.TileStatus;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MineClearance extends Tile{
    public MineClearance(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean click() {



        //if it's not opened, then do the operations
        if(status != TileStatus.opened)
        {

            tileView.playShieldAnime();

            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(e -> {
                this.board.makeShielded();
                System.out.println("Shield enabled!");
                super.click();
            });
            pause.play();
        }
        //if it is opened, we perform click method in the father class
        else
            super.click();

        return true;

    }

    @Override
    public void trigger()
    {
        if(status != TileStatus.closed)return;//if not closed then stop recursion

        status = TileStatus.triggered;//open the tile
        tileView.update(this.status);//update status
        System.out.println("Tile at " + this.x + " , " + this.y + "is triggered");

        if(this.minesAround != 0)return;//if this a numbered tile then stop recursion

        for(int i = x - 1; i <= x + 1;i++)
        {
            for(int j = y - 1;j <= y + 1;j++)
            {
                if(board.getTileAt(i,j) != null && !(i == x && j== y))board.getTileAt(i,j).trigger();
            }
        }
    }

    @Override
    public void endgameReveal(){
        status = TileStatus.triggered;
        tileView.update(status);
    }
}
