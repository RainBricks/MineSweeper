package com.example.minesweeper.board;

import com.example.minesweeper.enums.GameStatus;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

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
        board = Board.getBoard();
        board.createBoard(8, 8, 10, false, false, false);

        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    @DisplayName("test board initialize")
    void testBoardInitialization() {
        assertEquals(GameStatus.idle, board.getStatus(), "init gamestatus should be idle");
        assertEquals(0, board.getScore(), "init score should be 0");
    }

    @Test
    @DisplayName("test first click")
    void testFirstClickStartsGame() {
        board.clickAt(0, 0);

        assertEquals(GameStatus.gameRunning, board.getStatus(), "status should be gameRunning");
        assertTrue(outputStreamCaptor.toString().contains("Tile at 0 , 0is clicked"));
    }

    @Test
    @DisplayName("test flag at idle")
    void testFlagAtIdle() {
        board.flagAt(2, 2);

        assertFalse(outputStreamCaptor.toString().contains("is flagged"), "Idle state should not allow a flag operation");
    }

    @Test
    @DisplayName("test add score")
    void testScoreLogic() {
        board.incScore();
        board.incScore();
        assertEquals(2, board.getScore(), "score should be 2");

        board.makeGettingMinus();
        assertEquals(0, board.getScore(), "score should not be less than 0");
    }

    @Test
    @DisplayName("test shield logic")
    void testShieldLogic() {
        assertFalse(board.isShielded());
        board.makeShielded();
        assertTrue(board.isShielded());
        board.useShield();
        assertFalse(board.isShielded());
    }

    @Test
    @DisplayName("test click outbound")
    void testOutOfBoundsClick() {
        assertDoesNotThrow(() -> {
            board.clickAt(-1, 99);
            board.flagAt(99, -1);
        });
    }

    @Test
    @DisplayName("test recreate board")
    void testRecreateBoard() {
        board.createBoard(8, 8, 5, false, false, false);
        board.clickAt(0, 0);

    }

    @Test
    @DisplayName("test game end logic")
    void testClickAllTilesMustEndGame() {
        int rows = 8;
        int cols = 8;
        int mines = 20;

        board.createBoard(rows, cols, mines, false, false, false);
        assertEquals(GameStatus.idle, board.getStatus());

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board.getStatus() == GameStatus.gameWin || board.getStatus() == GameStatus.gameLose) {
                    break;
                }

                board.clickAt(i, j);

            }
        }

        GameStatus finalStatus = board.getStatus();

        assertTrue(
                finalStatus == GameStatus.gameWin || finalStatus == GameStatus.gameLose,
                "Error, all tiles clicked but game is still running, current status: " + finalStatus
        );

    }
}