package com.example.minesweeper.tile;

import com.example.minesweeper.enums.TileStatus;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class MiniMine extends Tile{
    public MiniMine(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean click() {


        if(status != TileStatus.opened) {
            this.board.makeGettingMinus();
            this.board.decScore();
            System.out.println("You got minus point!");
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
        System.out.println("Mini Mine is at " + x + " , " + y);
        status = TileStatus.triggered;
        tileView.update(status);
    }

    @Override
    protected void updateView() {

        tileView.playMinusAnime();
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            tileView.update(this.minesAround);//update current view
        });
        pause.play();
    }
}
