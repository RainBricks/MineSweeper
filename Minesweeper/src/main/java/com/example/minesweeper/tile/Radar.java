package com.example.minesweeper.tile;

import com.example.minesweeper.enums.TileStatus;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Radar extends Tile{
    public Radar(int x, int y) {
        super(x, y);
    }
    @Override
    public boolean click() {
        //if it's not opened, then do the operations
        if (status != TileStatus.opened) {
            this.board.makeFlagRandom();
            System.out.println("Random Mine is Flagged!");
        }

        return super.click();
    }

    @Override
    protected void trigger()
    {
        if(status != TileStatus.closed)return;//if not closed then stop recursion

        status = TileStatus.triggered;//open the tile
        tileView.update(this.status);//update status
        System.out.println("Tile at " + this.x + " , " + this.y + "is triggered");

        this.board.incScore();//increase the score
        if(this.minesAround != 0)return;//if this a numbered tile then stop recursion

        super.triggerMinesAround();
    }

    @Override
    public void endgameReveal(){
        System.out.println("Radar is at " + x + " , " + y);
        status = TileStatus.triggered;
        tileView.update(status);
    }

    @Override
    protected void updateView() {

        tileView.playRadarAnime();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            tileView.update(this.minesAround);//update current view
        });
        pause.play();
    }
}
