package com.example.minesweeper.tile;

import com.example.minesweeper.enums.TileStatus;

public class MineClearance extends Tile{
    public MineClearance(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean click() {



        if(status != TileStatus.opened)
        {
            this.board.makeShielded();
            System.out.println("Shield enabled!");
        }

        return super.click();
    }

    @Override
    public void trigger()
    {
        if(status != TileStatus.closed)return;//if not closed then stop recursion

        status = TileStatus.triggered;//open the tile
        this.board.incScore();//increase the score
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
