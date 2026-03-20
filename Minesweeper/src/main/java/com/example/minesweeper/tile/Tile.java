package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;
import com.example.minesweeper.tileview.TileView;

public class Tile {

    private int minesAround;
    protected Board board;
    protected int x;
    protected int y;
    protected TileView tileView;
    protected TileStatus status;

    public Tile(int x, int y) {
        this.status = TileStatus.closed;
        this.tileView = new TileView();
        this.x = x;
        this.y = y;
        this.board = Board.getBoard();
        tileView.setUserData(new int[]{x,y});
    }

    //return value here means if player is alive
    public boolean click()
    {
        if(status == TileStatus.flagged)
        {
            System.out.println("This tile is flagged!");
            return true;
        }

        if(status == TileStatus.closed)
        {
            status = TileStatus.opened;
            this.board.incScore();//increase the score
            tileView.update(this.minesAround);//update current view
            System.out.println("Tile at " + this.x + " , " + this.y + "is clicked");
        }

        if(minesAround != 0)return true;

        for(int i = x - 1;i <= x + 1;i++)
        {
            for(int j = y - 1;j <= y + 1;j++)
            {
                if(board.getTileAt(i,j) != null && !(i == x && j == y))board.getTileAt(i,j).trigger();
            }
        }


        return true;

    }


    public void trigger()
    {
        if(status != TileStatus.closed)return;//if not closed then stop recursion

        status = TileStatus.opened;//open the tile
        this.board.incScore();//increase the score
        tileView.update(this.minesAround);//update status
        System.out.println("Tile at " + this.x + " , " + this.y + "is triggered");

        if(minesAround != 0)return;//if this a numbered tile then stop recursion

        for(int i = x - 1;i <= x + 1;i++)
        {
            for(int j = y - 1;j <= y + 1;j++)
            {
                if(board.getTileAt(i,j) != null && !(i == x && j== y))board.getTileAt(i,j).trigger();
            }
        }
    }

    public void flag()
    {
        if(status == TileStatus.closed)
        {
            status = TileStatus.flagged;
            tileView.update(status);
            System.out.println("Tile at " + this.x + " , " + this.y + "is flagged");
        }
        else if(status == TileStatus.flagged)
        {
            status = TileStatus.closed;
            tileView.update(status);
            System.out.println("Tile at " + this.x + " , " + this.y + "is unflagged");
        }



    }

    public void endgameReveal(){

    }


    public void addMinesAround() {
        this.minesAround ++;
    }

    public TileView getTileView() {
        return tileView;
    }

    public TileStatus getStatus() {
        return status;
    }



}
