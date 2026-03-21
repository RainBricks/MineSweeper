package com.example.minesweeper.view;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.controller.GameController;
import com.example.minesweeper.controller.GameListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.minesweeper.consts.UIConsts.*;

public class Controller implements Initializable, GameListener {
    private GameController gameController;

    private ImageView restartButtonNormalUnpressedImageView;
    private ImageView restartButtonNormalPressedImageView;
    private ImageView restartButtonWinImageView;
    private ImageView restartButtonLoseImageView;

    @FXML private VBox root;
    @FXML private Label scoreLabel;
    @FXML private Button restartButton;
    @FXML private GridPane gameGridPane;
    @FXML private ComboBox<String> difficulty;
    @FXML private ImageView upperBoarder;
    @FXML private ImageView middleBoarder;
    @FXML private ImageView lowerBoarder;
    @FXML private ImageView middleLeftBoarder;
    @FXML private ImageView middleRightBoarder;

    public Controller() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameController = new GameController();
        gameController.setListener(this);

        showBoard();

        initRestartButtonImageViews();

        //difficulty.getSelectionModel().select("Easy");

        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (_, oldValue, newValue) -> {
                    if(oldValue == null || oldValue.equals(newValue)) return;
                    System.out.println("Change Difficulty: " + oldValue + " -> " + newValue);

                    restartButton.setGraphic(restartButtonNormalUnpressedImageView);

                    if (newValue.equals("Custom")) {
                        // show the custom inputs
                        updateCustom();
                        return;
                    }

                    int rows = 0, cols = 0, mines = 0;
                    switch(newValue) {
                        case "Easy":
                            rows = 8;
                            cols = 8;
                            mines = 10;
                            break;
                        case "Medium":
                            rows = 16;
                            cols = 16;
                            mines = 40;
                            break;
                        case "Hard":
                            rows = 16;
                            cols = 30;
                            mines = 99;
                            break;
                    }
                    gameController.setDifficulty(rows, cols, mines);
                }
        );
    }


    private void showBoard() {
        scoreLabel.setText("Score: 0");

        displayGamePlane();
        adjustWindowSize();
    }

    public void initRestartButtonImageViews() {
        InputStream win = this.getClass().getResourceAsStream("/images/win.png");
        InputStream lose = this.getClass().getResourceAsStream("/images/lose.png");
        InputStream smile_unpressed = this.getClass().getResourceAsStream("/images/face_unpressed.png");
        InputStream smile_pressed = this.getClass().getResourceAsStream("/images/face_pressed.png");
        if(win == null || lose == null || smile_unpressed == null || smile_pressed == null) {
            System.out.println("Init::setImage failed: path is not found");
            return;
        }

        restartButtonNormalUnpressedImageView = new ImageView(new Image(smile_unpressed));
        restartButtonNormalUnpressedImageView.setFitWidth(RESTART_BUTTON_SIZE);
        restartButtonNormalUnpressedImageView.setFitHeight(RESTART_BUTTON_SIZE);
        restartButtonNormalUnpressedImageView.setPreserveRatio(false);

        restartButtonNormalPressedImageView = new ImageView(new Image(smile_pressed));
        restartButtonNormalPressedImageView.setFitWidth(RESTART_BUTTON_SIZE);
        restartButtonNormalPressedImageView.setFitHeight(RESTART_BUTTON_SIZE);
        restartButtonNormalPressedImageView.setPreserveRatio(false);

        restartButtonWinImageView = new ImageView(new Image(win));
        restartButtonWinImageView.setFitWidth(RESTART_BUTTON_SIZE);
        restartButtonWinImageView.setFitHeight(RESTART_BUTTON_SIZE);
        restartButtonWinImageView.setPreserveRatio(false);

        restartButtonLoseImageView = new ImageView(new Image(lose));
        restartButtonLoseImageView.setFitWidth(RESTART_BUTTON_SIZE);
        restartButtonLoseImageView.setFitHeight(RESTART_BUTTON_SIZE);
        restartButtonLoseImageView.setPreserveRatio(false);

        restartButton.setGraphic(restartButtonNormalUnpressedImageView);
        restartButton.setOnMousePressed(event -> restartButton.setGraphic(restartButtonNormalPressedImageView));
        restartButton.setOnMouseReleased(event -> restartButton.setGraphic(restartButtonNormalUnpressedImageView));
    }


    private void updateCustom() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CustomDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Custom Difficulty");
            dialog.setDialogPane(dialogPane);

            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType);

            dialog.initStyle(StageStyle.UNDECORATED);

            Button confirmButton = (Button) dialog.getDialogPane().lookupButton(confirmButtonType);
            confirmButton.addEventFilter(ActionEvent.ACTION, actionEvent -> {
                try {
                    int rows = controller.getRows();
                    int cols = controller.getCols();
                    int mines = controller.getMines();

                    if (rows <= 0 || cols <= 0 || mines <= 0) {
                        controller.setWarning("Invalid number");
                        actionEvent.consume();
                    } else if (mines >= rows * cols) {
                        controller.setWarning("Too many mines");
                        actionEvent.consume();
                    } else if(rows > 30 || cols > 30) {
                        controller.setWarning("Size too big");
                        actionEvent.consume();
                    } else if(rows < 8 || cols < 8) {
                        controller.setWarning("Size too small");
                        actionEvent.consume();
                    }
                } catch (NumberFormatException e) {
                    controller.setWarning("Not a valid number!");
                    actionEvent.consume();
                }
            });

            dialog.showAndWait().ifPresent(result -> {
                if (result == confirmButtonType) {
                    try {
                        int rows = controller.getRows();
                        int cols = controller.getCols();
                        int mines = controller.getMines();
                        gameController.setDifficulty(rows, cols, mines);
                    } catch (NumberFormatException e) {
                        //
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        }
    }

    private void adjustWindowSize() {
        int rows = gameController.getRow();
        int cols = gameController.getColumn();
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            root.setPrefHeight(SETTINGS_BAR_HEIGHT + TILE_SIZE * rows  + BOARDER_HEIGHT * 3);
            root.setPrefWidth(TILE_SIZE * cols  + BOARDER_WIDTH * 2);

            upperBoarder.setFitWidth( (TILE_SIZE + 0.5)  * cols);
            middleBoarder.setFitWidth((TILE_SIZE + 0.5)  * cols);
            lowerBoarder.setFitWidth((TILE_SIZE + 0.5)  * cols);

            middleLeftBoarder.setFitHeight((TILE_SIZE + 0.5) * rows);
            middleRightBoarder.setFitHeight((TILE_SIZE + 0.5) * rows);

            stage.sizeToScene();
        } catch (NullPointerException e) {
            //we'll only reach here when game starts
        }
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
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void onGameOver(boolean win) {
        Board board = Board.getBoard();
        int rows = gameController.getRow();
        int cols = gameController.getColumn();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board.getTileAt(i, j).endgameReveal();
            }
        }
        restartButton.setGraphic(win ? restartButtonWinImageView : restartButtonLoseImageView);
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
    private void restart(ActionEvent event) {
        restartButton.setGraphic(restartButtonNormalUnpressedImageView);
        gameController.restart();
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
}