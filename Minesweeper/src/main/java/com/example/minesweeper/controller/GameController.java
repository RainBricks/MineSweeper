package com.example.minesweeper.controller;

import com.example.minesweeper.board.Board;

public class GameController {
    private final Board board;
    private int row, column, mineNum;
    private boolean clicked;
    private boolean gameEnded;
    private GameListener listener;

    public GameController() {
        board = Board.getBoard();
        row = 8;
        column = 8;
        mineNum = 10;
        board.createBoard(row, column, mineNum);
    }

    public void setListener(GameListener listener) {
        this.listener = listener;
        if (listener != null) {
            listener.onBoardCreated();
        }
    }

    public void createBoard(int row, int col, int mines) {
        this.row = row;
        this.column = col;
        this.mineNum = mines;
        board.createBoard(row, col, mines);
        clicked = false;
        gameEnded = false;
        if (listener != null) {
            listener.onBoardCreated();
        }
    }

    public void click(int x, int y) {
        if (gameEnded) {
            return;
        }
        if (!clicked) {
            board.startGame(x, y);
            clicked = true;
            if (listener != null) {
                listener.onBoardChanged();
            }
        }
        if (!board.getTileAt(x, y).click()) {
            gameEnded = true;
            Board board = Board.getBoard();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {
                    board.getTileAt(i, j).endgameReveal();
                }
            }
            System.out.println("You Lose!");
            board.print();
            if (listener != null) {
                listener.onGameOver(false);
            }
        } else {
            if (listener != null) {
                listener.onScoreChange(board.getScore());
            }
            if (board.win()) {
                gameEnded = true;
                Board board = Board.getBoard();
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < column; j++) {
                        board.getTileAt(i, j).endgameReveal();
                    }
                }
                System.out.println("You Win!");
                board.print();
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
        if (gameEnded || !clicked) {
            return;
        }
        board.getTileAt(x, y).flag();
        if (listener != null) {
            listener.onBoardChanged();
        }
    }

    public void restart() {
        System.out.println("Game Restarted");
        createBoard(row, column, mineNum);
        this.board.print();
    }

    public void setDifficulty(int row, int col, int mines) {
        createBoard(row, col, mines);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

}