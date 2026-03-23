package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class Radar extends Tile{
    private final int mineX;
    private final int mineY;

    public Radar(int x, int y, int minesAround, int mineX, int mineY) {
        super(x, y);
        this.minesAround = minesAround;
        this.mineX = mineX;
        this.mineY = mineY;
    }
    @Override
    public boolean click() {
        //if it's not opened, then do the operations
        if (status != TileStatus.opened) {
            Board.getBoard().getTileAt(mineX,mineY).flag();
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

        Board.getBoard().incScore();//increase the score
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
        pause.setOnFinished(_ -> {
            tileView.update(this.minesAround);//update current view
        });
        pause.play();
    }
}
