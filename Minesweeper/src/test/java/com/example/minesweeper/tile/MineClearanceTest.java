package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class MineClearanceTest {

    private MineClearance clearance;
    private final int x = 2;
    private final int y = 2;
    private final int minesAround = 0;

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
        clearance = new MineClearance(x, y, minesAround);
    }

    @Test
    @DisplayName("test MineClearance constructor")
    void testConstructor() {
        assertEquals(minesAround, clearance.getMinesAround());
        assertEquals(TileStatus.closed, clearance.getStatus());
    }

    @Test
    @DisplayName("test click on MineClearance (shield activation + safe next click)")
    void testClick() {
        Board board = Board.getBoard();
        board.createBoard(8,8,10,true,true,true);
        board.clickAt(0,0);
        assertFalse(board.isShielded(), "Shield should be off before click");

        // First click → opens tile + activates shield
        clearance.click();

        assertEquals(TileStatus.opened, clearance.getStatus(), "MineClearance must open");
        assertTrue(board.isShielded(), "Shield must be activated after clicking MineClearance");

        Mine fakeMine = new Mine(2, 2);

        boolean alive = fakeMine.click();

        assertTrue(alive, "Player should survive the next mine due to shield");
        assertFalse(board.isShielded(), "Shield should be consumed after protecting player");
        assertEquals(TileStatus.flagged, fakeMine.getStatus(), "Mine should be flagged after shield use");
    }

    @Test
    @DisplayName("test trigger")
    void testTrigger() {
        clearance.trigger();
        assertEquals(TileStatus.triggered, clearance.getStatus());
    }

    @Test
    @DisplayName("test endGameReveal")
    void testEndgameReveal() {
        clearance.endgameReveal();

        TileStatus st = clearance.getStatus();

        assertNotEquals(TileStatus.closed, st);

        assertTrue(
                st == TileStatus.endShield ||
                        st == TileStatus.endMinus ||
                        st == TileStatus.endRadar ||
                        st == TileStatus.triggered ||
                        st == TileStatus.minetriggered,
                "MineClearance should reveal as special tile"
        );
    }

    @Test
    @DisplayName("test updateView animation-like behavior")
    void testUpdateViewWithAnimation() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            clearance.click();  // triggers TileView update
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }
}