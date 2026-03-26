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

class MiniMineTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MiniMine mini;
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
        System.setOut(new PrintStream(outputStreamCaptor));
        mini = new MiniMine(x, y, minesAround);
    }

    @Test
    @DisplayName("test MiniMine constructor")
    void testConstructor() {
        assertEquals(minesAround, mini.getMinesAround());
        assertEquals(TileStatus.closed, mini.getStatus());
    }

    @Test
    @DisplayName("test click on MiniMine")
    void testClick() {

        Board.getBoard().createBoard(8,8,10,true,true,true);
        Board.getBoard().clickAt(0,0);
        mini.click();

        assertEquals(TileStatus.opened, mini.getStatus());

        String output = outputStreamCaptor.toString().trim();

        assertTrue(output.contains("You got minus point!"));
        assertTrue(output.contains("is clicked"));

    }

    @Test
    @DisplayName("test triggered")
    void testTrigger() {
        mini.trigger();
        assertEquals(TileStatus.triggered, mini.getStatus());
    }

    @Test
    @DisplayName("test flag/unflag")
    void testFlag() {
        mini.flag();
        assertEquals(TileStatus.flagged, mini.getStatus());

        mini.flag();
        assertEquals(TileStatus.closed, mini.getStatus());
    }

    @Test
    @DisplayName("test endGameReveal")
    void testEndgameReveal() {

        mini.endgameReveal();
        TileStatus st = mini.getStatus();

        assertNotEquals(TileStatus.closed, st);

        // acceptable MiniMine end statuses
        assertTrue(
                st == TileStatus.endMinus ||
                        st == TileStatus.triggered ||
                        st == TileStatus.minetriggered,
                "MiniMine should reveal as special tile"
        );
    }

    @Test
    @DisplayName("test updateView with JavaFX thread")
    void testUpdateViewWithAnimation() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            mini.click();    // triggers TileView update
            latch.countDown();
        });

        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }
}