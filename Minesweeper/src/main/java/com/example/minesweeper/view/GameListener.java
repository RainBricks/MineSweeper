package com.example.minesweeper.view;

public interface GameListener {
    void onScoreChange(int score);
    void onGameOver(boolean win);
    void onBoardCreated();   //when board is created
    void onBoardChanged();   //when the state changes,e.g. a click event or a flag event
}