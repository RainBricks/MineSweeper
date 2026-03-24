package com.example.minesweeper.controller;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.GameStatus;
import com.example.minesweeper.view.GameListener;

import java.util.Random;

public class GameController {

    private int row;
    private int column;
    private int mineNum;
    private boolean hasMineClearance;
    private boolean hasRadar;
    private boolean hasMiniMine;

    private boolean isCustom;

    private GameListener listener;

    public GameController() {
        row = 8;
        column = 8;
        mineNum = 10;

        //at first start
        isCustom = false;
        hasMineClearance = true;
        hasRadar = true;
        hasMiniMine = true;
        Board.getBoard().createBoard(8, 8, 10, true, true, true);
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
        listener.onBoardCreated();
    }

    public void createBoard(int row, int col, int mines, boolean mineClearance, boolean radar, boolean miniMine) {
        this.row = row;
        this.column = col;
        this.mineNum = mines;
        this.hasMineClearance = mineClearance;
        this.hasRadar = radar;
        this.hasMiniMine = miniMine;
        //first create the board on model layer
        Board.getBoard().createBoard(row, col, mines, mineClearance, miniMine, radar);
        //then tell ui layer a board is created
        listener.onBoardCreated();
    }


    public void click(int x, int y) {
        boolean isFirstStarted = Board.getBoard().getStatus() == GameStatus.idle;

        Board.getBoard().clickAt(x, y);

        //if we click it for the first time, we update the ui layer's board
        if(isFirstStarted) {
            listener.onBoardChanged();
        }

        if(Board.getBoard().getStatus() == GameStatus.gameWin) {
            listener.onGameOver(true);
        }else if(Board.getBoard().getStatus() == GameStatus.gameLose)
        {
            listener.onGameOver(false);
        }
        listener.onScoreChange(Board.getBoard().getScore());

    }


    public void flag(int x, int y) {
        Board.getBoard().flagAt(x, y);
    }

    public void restart() {
        if (isCustom) {
            System.out.println("Game Restarted with previous settings");
            createBoard(row, column, mineNum, hasMineClearance, hasMiniMine, hasRadar);
        } else {
            System.out.println("Game Restarted");
            Random rand = new Random();
            createBoard(row, column, mineNum, rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean());
        }
        Board.getBoard().print();
    }

    //for regular dificulties
    public void setDifficulty(int row, int col, int mines) {
        isCustom = false;
        Random rand = new Random();
        createBoard(row, col, mines, rand.nextBoolean(), rand.nextBoolean(), rand.nextBoolean());
    }

    //for custom difficulty
    public void setDifficulty(int row, int col, int mines, boolean hasMineClearance, boolean hasRadar, boolean hasMiniMine) {
        isCustom = true;
        createBoard(row, col, mines, hasMineClearance, hasRadar, hasMiniMine);
    }

    public int getRow() { return row; }
    public int getColumn() { return column; }
}