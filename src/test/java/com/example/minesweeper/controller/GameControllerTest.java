package com.example.minesweeper.controller;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController controller;
    private MockGameListener mockListener;


    private static class MockGameListener implements com.example.minesweeper.view.GameListener {
        boolean boardCreatedCalled = false;
        boolean boardChangedCalled = false;
        boolean scoreChangedCalled = false;
        Boolean gameOverResult = null;

        @Override public void onBoardCreated() { boardCreatedCalled = true; }
        @Override public void onBoardChanged() { boardChangedCalled = true; }
        @Override public void onScoreChange(int score) { scoreChangedCalled = true; }
        @Override public void onGameOver(boolean win) { gameOverResult = win; }
    }

    @BeforeAll
    static void initJavaFX() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            //
        }
    }

    @BeforeEach
    void setUp() {
        controller = new GameController();
        mockListener = new MockGameListener();
        controller.setListener(mockListener);
    }

    @Test
    @DisplayName("test init, onBoardCreated should be called")
    void testSetListener() {
        assertTrue(mockListener.boardCreatedCalled);
    }

    @Test
    @DisplayName("test create board")
    void testCreateBoard() {
        controller.createBoard(10, 10, 5, true, true, true);

        assertEquals(10, controller.getRow());
        assertEquals(10, controller.getColumn());
        assertTrue(mockListener.boardCreatedCalled);
    }

    @Test
    @DisplayName("test first click")
    void testFirstClick() {
        controller.click(0, 0);

        assertTrue(mockListener.boardChangedCalled);
        assertTrue(mockListener.scoreChangedCalled);
    }

    @Test
    @DisplayName("test set difficulty")
    void testSetDifficultyCustom() {

        controller.setDifficulty(12, 16, 20, true, false, true);


        controller.restart();
        assertEquals(12, controller.getRow());
        assertEquals(16, controller.getColumn());
    }

    @Test
    @DisplayName("test flag")
    void testFlag() {

        controller.click(0, 0);

        // 插旗
        controller.flag(1, 1);


        assertDoesNotThrow(() -> controller.flag(1,1));
    }

    @Test
    @DisplayName("test game end logic")
    void testGameOverScenario() {

        for (int i = 0; i < controller.getRow(); i++) {
            for (int j = 0; j < controller.getColumn(); j++) {
                controller.click(i, j);
                if (mockListener.gameOverResult != null) break;
            }
            if (mockListener.gameOverResult != null) break;
        }


        assertNotNull(mockListener.gameOverResult, "onGameOver should be called");
        System.out.println("result: " + (mockListener.gameOverResult ? "win" : "lose"));
    }
}