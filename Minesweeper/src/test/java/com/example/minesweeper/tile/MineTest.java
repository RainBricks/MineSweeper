package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MineTest {

    private Mine mine;
    private final int testX = 1;
    private final int testY = 1;

    @BeforeEach
    void setUp() {
        // Initialize JavaFX platform to avoid TileView errors
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already started
        }

        mine = new Mine(testX, testY);

        // Create a fresh board so Mine.click() can refer to Board.getBoard()
        Board.getBoard().createBoard(5, 5, 0, false, false, false);
    }

    @Test
    @DisplayName("Mine initial state should be closed with 0 minesAround")
    void testInitialState() {
        assertEquals(TileStatus.closed, mine.getStatus(), "Mine should start closed");
        assertEquals(0, mine.getMinesAround(), "minesAround should be 0");
    }

    @Test
    @DisplayName("Clicking a closed mine without shield should explode and return false")
    void testClickMineWithoutShield() {
        boolean alive = mine.click();

        assertFalse(alive, "Clicking a mine should return false (player dies)");
        assertEquals(TileStatus.exploded, mine.getStatus(), "Mine should become exploded");
    }

    @Test
    @DisplayName("Clicking a mine when shielded should consume shield and flag the mine")
    void testClickMineWithShield() {
        Board board = Board.getBoard();
        board.makeShielded(); // Activates shield

        boolean alive = mine.click();

        assertTrue(alive, "Should survive when shield is active");
        assertEquals(TileStatus.flagged, mine.getStatus(), "Mine should become flagged after shield use");
        assertFalse(board.isShielded(), "Shield should be consumed");
    }

    @Test
    @DisplayName("Clicking a flagged mine should do nothing and return true")
    void testClickFlaggedMine() {
        mine.flag();  // flag first
        assertEquals(TileStatus.flagged, mine.getStatus());

        boolean alive = mine.click();

        assertTrue(alive, "Clicking flagged mine should not kill");
        assertEquals(TileStatus.flagged, mine.getStatus(), "Status should remain flagged");
    }

    @Test
    @DisplayName("endgameReveal on a non-exploded mine should set minetriggered")
    void testEndgameReveal() {
        assertEquals(TileStatus.closed, mine.getStatus());

        mine.endgameReveal();

        assertEquals(TileStatus.minetriggered,
                mine.getStatus(),
                "Mine should be revealed as minetriggered at endgame");
    }

    @Test
    @DisplayName("endgameReveal should NOT override exploded mines")
    void testEndgameRevealOnExplodedMine() {
        mine.click(); // explode the mine first
        assertEquals(TileStatus.exploded, mine.getStatus());

        mine.endgameReveal(); // should not change exploded → minetriggered

        assertEquals(TileStatus.exploded,
                mine.getStatus(),
                "Exploded mines must remain exploded after endgameReveal");
    }

    @Test
    @DisplayName("Trigger on a mine should do nothing")
    void testTriggerDoesNothing() {
        mine.trigger(); // Mine override does nothing
        assertEquals(TileStatus.closed,
                mine.getStatus(),
                "Mine.trigger() should not change status");
    }
}