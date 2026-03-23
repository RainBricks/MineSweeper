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

    private GameStatus status;

    private GameListener listener;

    public GameController() {
        row = 8;
        column = 8;
        mineNum = 10;
        status = GameStatus.idle;

        //at first start
        isCustom = false;
        hasMineClearance = true;
        hasRadar = true;
        hasMiniMine = true;
        Board.getBoard().createBoard(8, 8, 8, true,true, true);
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
        if (listener != null) {
            listener.onBoardCreated();
        }
    }

    public void createBoard(int row, int col, int mines, boolean mineClearance, boolean radar, boolean miniMine) {
        this.row = row;
        this.column = col;
        this.mineNum = mines;
        this.hasMineClearance = mineClearance;
        this.hasRadar = radar;
        this.hasMiniMine = miniMine;

        Board.getBoard().createBoard(row, col, mines, mineClearance,miniMine, radar);

        status = GameStatus.idle;
        if (listener != null) {
            listener.onBoardCreated();
        }
    }

    public void click(int x, int y) {
        if (status == GameStatus.gameEnded) {
            return;
        }
        if (status == GameStatus.idle) {
            Board.getBoard().startGame(x, y);
            status = GameStatus.firstClicked;
            if (listener != null) {
                listener.onBoardChanged();
            }
        }
        if (!Board.getBoard().getTileAt(x, y).click()) {
            status = GameStatus.gameEnded;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    Board.getBoard().getTileAt(i, j).endgameReveal();
                }
            }
            System.out.println("You Lose!");
            Board.getBoard().print();
            if (listener != null) {
                listener.onGameOver(false);
            }
        } else {
            if (listener != null) {
                listener.onScoreChange(Board.getBoard().getScore());
            }
            if (Board.getBoard().win()) {
                status = GameStatus.gameEnded;
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column; j++) {
                        Board.getBoard().getTileAt(i, j).endgameReveal();
                    }
                }
                System.out.println("You Win!");
                Board.getBoard().print();
                if (listener != null) {
                    listener.onGameOver(true);
                }
            }
        }
        if (listener != null) {
            listener.onBoardChanged();
        }
    }

    public void flag(int x, int y) {
        if (status == GameStatus.gameEnded || status == GameStatus.idle) {
            return;
        }
        Board.getBoard().getTileAt(x, y).flag();
        if (listener != null) {
            listener.onBoardChanged();
        }
    }

    public void restart() {
        if(isCustom)
        {
            System.out.println("Game Restarted with previous settings");
            createBoard(row, column, mineNum, hasMineClearance, hasMiniMine, hasRadar);
        }
        else
        {
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}