package com.example.minesweeper.tile;

import com.example.minesweeper.enums.TileStatus;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    private Tile tile;
    private final int testX = 2;
    private final int testY = 3;

    @BeforeEach
    void setUp() {
        //enable javaFX platform, will have bug if don't do this
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            //do nothing
        }
        tile = new Tile(testX, testY);
    }

    @Test
    @DisplayName("test if init status is closed")
    void testInitialStatus() {
        assertEquals(TileStatus.closed, tile.getStatus(), "should be closed");
        assertEquals(0, tile.getMinesAround(), "minesAround should be 0");
    }

    @Test
    @DisplayName("test flag and deflag logic")
    void testFlagLogic() {
        tile.flag();
        assertEquals(TileStatus.flagged, tile.getStatus(), "status should be flagged");

        tile.flag();
        assertEquals(TileStatus.closed, tile.getStatus(), "status should be closed");
    }

    @Test
    @DisplayName("test addMinesAround")
    void testAddMinesAround() {
        tile.addMinesAround();
        tile.addMinesAround();
        assertEquals(2, tile.getMinesAround(), "minesAround should be 2");
    }

    @Test
    @DisplayName("test click on a flagged tile")
    void testClickFlaggedTile() {
        tile.flag();
        boolean alive = tile.click();

        assertTrue(alive, "should return true");
        assertEquals(TileStatus.flagged, tile.getStatus(), "status should not change");
    }


    @Test
    @DisplayName("test click on closed tile")
    void testClickClosedTile() {
        tile.click();
        assertEquals(TileStatus.opened, tile.getStatus(), "status should be opened");
    }

    @Test
    @DisplayName("test trigger")
    void testTrigger() {
        tile.trigger();
        assertEquals(TileStatus.opened, tile.getStatus(), "status should be opened");
    }
}