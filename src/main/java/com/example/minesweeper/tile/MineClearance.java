package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MineClearance extends Tile{
    public MineClearance(int x, int y, int minesAround) {
        super(x, y);
        this.minesAround = minesAround;
    }

    @Override
    public boolean click() {


        //if it's not opened, then do the operations
        if (status != TileStatus.opened) {
            Board.getBoard().makeShielded();
            System.out.println("Shield enabled!");
        }

        return super.click();

    }

    @Override
    public void trigger()
    {
        if(status != TileStatus.closed)return;//if not closed then stop recursion

        status = TileStatus.triggered;//open the tile
        tileView.update(this.status);//update status
        System.out.println("Tile at " + this.x + " , " + this.y + "is triggered");

        Board.getBoard().incScore();//increase the score
        if(this.minesAround != 0)return;//if this a numbered tile then stop recursion

        Board.getBoard().triggerMinesAround(x,y);
    }

    @Override
    public void endgameReveal(){
        System.out.println("Mine Clearance is at " + x + " , " + y);
        status = TileStatus.endShield;
        tileView.update(status);
    }

    @Override
    protected void updateView() {

        tileView.playShieldAnime();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(_ -> {
            tileView.update(this.minesAround);//update current view
        });
        pause.play();
    }
}
