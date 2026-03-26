package com.example.minesweeper.tile;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.enums.TileStatus;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class RadarTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private Radar radar;
    private final int x = 1;
    private final int y = 1;
    private final int minesAround = 2;
    private final int mineX = 5;
    private final int mineY = 5;

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
        System.setOut(new PrintStream(outputStreamCaptor));
        radar = new Radar(x, y, minesAround, mineX, mineY);
    }

    @Test
    @DisplayName("test Radar constructor")
    void testConstructor() {
        assertEquals(minesAround, radar.getMinesAround());
        assertEquals(TileStatus.closed, radar.getStatus());
    }

    @Test
    @DisplayName("test click on Radar")
    void testClick() {

        Board.getBoard().createBoard(8,8,10,true,true,true);
        Board.getBoard().clickAt(0,0);
        radar.click();

        assertEquals(TileStatus.opened, radar.getStatus());

        String output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("Random Mine is Flagged!"));
        assertTrue(output.contains("is clicked"));

    }

    @Test
    @DisplayName("test triggered")
    void testTrigger() {
        radar.trigger();
        assertEquals(TileStatus.triggered, radar.getStatus());
    }

    @Test
    @DisplayName("test endGameReveal")
    void testEndgameReveal() {
        radar.endgameReveal();
        assertEquals(TileStatus.endRadar, radar.getStatus());
    }

    @Test
    @DisplayName("test updateView")
    void testUpdateViewWithAnimation() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            radar.click();
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));

    }
}