package com.example.minesweeper.executor;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.controller.Controller;
import com.example.minesweeper.enums.TileStatus;


public class Executor{
    private Board board;
    private int score;
    private Controller controller;

    public Executor(Board board) {
        this.board = board;
        score = 0;
    }

    public void click(int x,int y)
    {
        if(!board.getTileAt(x,y).click())
        {
            controller.gameOver();
        }
        else {
            controller.setTileAt(x,y,board.getTileAt(x,y).getStatus(),board.getTileAt(x,y).getMinesAround());
        }
    }

    public void flag(int x,int y)
    {
        board.getTileAt(x,y).flag();
        controller.setTileAt(x,y,board.getTileAt(x,y).getStatus());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void startNewGame(String difficulty,int x,int y)
    {
        System.out.println("Start new game");
        board.createBoard(difficulty,x,y);
    }

    public void setTileAt(int x, int y, TileStatus status, int num)
    {
        controller.setTileAt(x,y,status,num);
    }

}
