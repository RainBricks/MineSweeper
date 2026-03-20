package com.example.minesweeper.controller;

import com.example.minesweeper.board.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.minesweeper.consts.UIConsts.*;

public class Controller implements Initializable {
    private final Board board;

    private boolean clicked;

    private int row;
    private int column;
    private int mineNum;

    private boolean gameEnded;

    private ImageView restartButtonNormalUnpressedImageView;
    private ImageView restartButtonNormalPressedImageView;
    private ImageView restartButtonWinImageView;
    private ImageView restartButtonLoseImageView;

    @FXML private VBox root;

    @FXML private Label scoreLabel;

    @FXML private Button restartButton;


    @FXML private HBox customPane;
    @FXML private TextField customRow;
    @FXML private TextField customColumn;
    @FXML private TextField customMineNum;
    @FXML private Button updateCustomButton;


    @FXML
    private GridPane gameGridPane;

    @FXML
    private ComboBox<String> difficulty;


    public Controller()
    {
        this.board = Board.getBoard();
        this.row = 8;
        this.column = 8;
        this.mineNum = 10;

    }

    public void initRestartButtonImageViews()
    {
        InputStream win = this.getClass().getResourceAsStream("/images/win.png");
        InputStream lose = this.getClass().getResourceAsStream("/images/lose.png");
        InputStream smile_unpressed = this.getClass().getResourceAsStream("/images/face_unpressed.png");
        InputStream smile_pressed = this.getClass().getResourceAsStream("/images/face_pressed.png");
        if(win == null || lose == null || smile_unpressed == null || smile_pressed == null)
        {
            System.out.println("Init::setImage failed: path is not found");
            return ;
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
        restartButton.setOnMousePressed(event -> {restartButton.setGraphic(restartButtonNormalPressedImageView);});
        restartButton.setOnMouseReleased(event -> {restartButton.setGraphic(restartButtonNormalUnpressedImageView);});
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        initTiles();
        initRestartButtonImageViews();

        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (_, oldValue, newValue) -> {
                    if(oldValue.equals(newValue))return;
                    System.out.println("Change Difficulty: " + oldValue + " -> " + newValue);

                    if (newValue.equals("Custom")) {
                        // show the custom inputs
                        customPane.setVisible(true);
                        customPane.setManaged(true);
                        // do not start a new game until user presses "Start"
                        return;
                    } else {
                        // hide custom inputs
                        customPane.setVisible(false);
                        customPane.setManaged(false);
                    }

                    switch(newValue)
                        {
                        case "Easy":
                            this.row = 8;
                            this.column = 8;
                            this.mineNum = 10;
                            initTiles();
                            break;
                        case "Medium":
                            this.row = 16;
                            this.column = 16;
                            this.mineNum = 40;
                            initTiles();
                            break;
                        case "Hard":
                            this.row = 16;
                            this.column = 30;
                            this.mineNum = 99;
                            initTiles();
                            break;

                        }
                }
        );


    }

    //for custom mode
    @FXML
    private void updateCustom(ActionEvent event) {
        this.row = Integer.parseInt(customRow.getText());
        this.column = Integer.parseInt(customColumn.getText());
        this.mineNum = Integer.parseInt(customMineNum.getText());
        initTiles();
    }

    public void initTiles()//Refresh the game plane
    {
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setResizable(false);
            stage.setHeight(SETTINGS_BAR_HEIGHT + TILE_SIZE * (this.row +1));
            stage.setWidth(TILE_SIZE * (this.column + 1));
        }catch (NullPointerException e){

        }
        //clear existing  game status
        clicked = false;
        gameEnded = false;

        //create a new board
        board.createBoard(row,column,mineNum);

        scoreLabel.setText("Score: 0");

        //display game plane
        this.displayGamePlane();
    }

    public void displayGamePlane()
    {
        gameGridPane.getChildren().clear();
        for(int i = 0;i < row;i++)
        {
            for(int j = 0;j < column;j++)
            {
                board.getTileAt(i,j).getTileView().setOnMouseClicked(this::tileClick);
                gameGridPane.add(board.getTileAt(i,j).getTileView(),j,i);
            }
        }
    }

    public void gameOver(boolean result)
    {
        /*gameGridPane.getChildren().clear();
        gameGridPane.add(new Label("Game Over!"), 0, 0);*/
        gameEnded = true;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                board.getTileAt(i, j).endgameReveal();
            }
        }

        restartButton.setGraphic(result?restartButtonWinImageView:restartButtonLoseImageView);

    }

    @FXML
    private void restart(ActionEvent event) {
        initTiles();
    }


    public void click(int x,int y)
    {
        scoreLabel.setText("Score: " + board.getScore());

        if(!board.getTileAt(x,y).click())
        {
            this.gameOver(false);//lose
        }

        if(board.win()){
            this.gameOver(true);//win
        }
    }

    public void flag(int x,int y)
    {
        board.getTileAt(x,y).flag();
    }



    public void tileClick(MouseEvent event)//Handle click and flag event !!flag event handling to be done
    {

        //disable the board when game over
        if(gameEnded) return;

        Button btn = (Button) event.getSource();
        int[] pos = (int[]) btn.getUserData();

        if(event.getButton() == MouseButton.PRIMARY)
        {
            if(!clicked)
            {
                board.startGame(pos[0],pos[1]);
                clicked = true;
                this.displayGamePlane();
            }
            System.out.println("Left click event at " + pos[0] + " , " + pos[1]);
            this.click(pos[0],pos[1]); //left_click
        }
        else if(event.getButton() == MouseButton.SECONDARY)
        {
            if(!clicked)
            {
                return;
            }
            System.out.println("Right click event at " + pos[0] + " , " + pos[1]);
            this.flag(pos[0],pos[1]);
        }



    }

}
