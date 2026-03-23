package com.example.minesweeper.view;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.controller.GameController;
import com.example.minesweeper.controller.GameListener;
import com.example.minesweeper.controller.CounterController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, GameListener {
    private GameController gameController;
    private CounterController scoreCounterController;

    //used for resetbutton
    private String currentFaceClass = "face-normal";

    //used for window drag
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML private VBox root;
    @FXML private Button restartButton;
    @FXML private GridPane gameGridPane;
    @FXML private ComboBox<String> difficulty;
    @FXML private HBox scoreCounterContainer;
    @FXML private AnchorPane titleBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        scoreCounterController = new CounterController();
        scoreCounterContainer.getChildren().add(scoreCounterController);

        gameController = new GameController();
        gameController.setListener(this);

        setupTitleBarDraggable();

        applyFaceStyle("face-normal");

        setupDifficultyListener();

        showBoard();
    }


    private void setupTitleBarDraggable() {

        //store current relative position(relative to the titlebar)
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null) {
                //current window position + offset = current mouse position,follow this equation
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    //for combobox
    private void setupDifficultyListener() {
        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (_, oldValue, newValue) -> {
                    if(newValue == null || newValue.equals(oldValue)) return;

                    applyFaceStyle("face-normal");

                    if (newValue.equals("Custom")) {
                        updateCustom();
                    } else {
                        int rows = 8, cols = 8, mines = 10;
                        switch(newValue) {
                            case "Medium": rows = 16; cols = 16; mines = 40; break;
                            case "Hard":   rows = 16; cols = 30; mines = 99; break;
                            case "Easy":   default: break;
                        }
                        gameController.setDifficulty(rows, cols, mines);
                    }
                }
        );
    }

    //tool method for switch graph for reset btn
    private void applyFaceStyle(String newStyleClass) {
        restartButton.getStyleClass().remove(currentFaceClass);
        if (!restartButton.getStyleClass().contains(newStyleClass)) {
            restartButton.getStyleClass().add(newStyleClass);
        }
        currentFaceClass = newStyleClass;

    }

    private void showBoard() {
        scoreCounterController.setNum(0);
        displayGamePlane();
        adjustWindowSize();
    }



    private void adjustWindowSize() {
        //
        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();
            if (stage != null) {
                stage.sizeToScene();
            }
        });
    }

    public void displayGamePlane() {
        gameGridPane.getChildren().clear();
        Board board = Board.getBoard();
        int rows = gameController.getRow();
        int cols = gameController.getColumn();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board.getTileAt(i, j).getTileView().setOnMouseClicked(this::tileClick);
                gameGridPane.add(board.getTileAt(i, j).getTileView(), j, i);
            }
        }
    }

    @Override
    public void onScoreChange(int score) {
        scoreCounterController.setNum(score);
    }

    @Override
    public void onGameOver(boolean win) {
        applyFaceStyle(win ? "face-win" : "face-lose");
    }

    @Override
    public void onBoardCreated() {
        showBoard();
    }

    @Override
    public void onBoardChanged() {
        displayGamePlane();
    }


    @FXML
    public void restart(ActionEvent event) {
        applyFaceStyle("face-normal");
        gameController.restart();
    }

    @FXML
    public void closeGame(MouseEvent event) {
        Platform.exit();
    }

    public void tileClick(MouseEvent event) {
        Button btn = (Button) event.getSource();
        int[] pos = (int[]) btn.getUserData();

        if (event.getButton() == MouseButton.PRIMARY) {
            gameController.click(pos[0], pos[1]);
        } else if (event.getButton() == MouseButton.SECONDARY) {
            gameController.flag(pos[0], pos[1]);
        }
    }

    //activate custom dialog and get user input
    private void updateCustom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CustomDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CustomDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.initStyle(StageStyle.UNDECORATED);

            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(confirmButtonType);

            Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
            confirmButton.addEventFilter(ActionEvent.ACTION, actionEvent -> {
                if (!validateCustomInput(controller)) {
                    //if input is illegal then refuse to close
                    actionEvent.consume();
                }
            });

            //when input is legal, close,or confirm action is no longer consumed
            dialog.showAndWait().ifPresent(result -> {
                if (result == confirmButtonType) {
                    gameController.setDifficulty(controller.getRows(), controller.getCols(), controller.getMines());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check if input is legal
    private boolean validateCustomInput(CustomDialogController c) {
        try {
            int r = c.getRows(), col = c.getCols(), m = c.getMines();
            if (r < 8 || col < 8 || r > 30 || col > 30) {
                c.setWarning("Size must be 8-30");
                return false;
            }
            if (m >= r * col) {
                c.setWarning("Too many mines");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            c.setWarning("Invalid number");
            return false;
        }
    }
}