package com.example.minesweeper.controller;

import com.example.minesweeper.board.Board;
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

    @FXML
    private GridPane gameGridPane;

    @FXML private ComboBox<String> difficulty;



    @FXML private ImageView upperBoarder;

    @FXML private ImageView middleBoarder;

    @FXML private ImageView lowerBoarder;

    @FXML private ImageView middleLeftBoarder;

    @FXML private ImageView middleRightBoarder;



/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/

    public Controller()
    {
        this.board = Board.getBoard();
        this.row = 8;
        this.column = 8;
        this.mineNum = 10;

    }

/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/

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
                        updateCustom();
                        // do not start a new game until user presses "Start"
                        return;
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

/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/

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


/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------*/


    //for custom mode
    private void updateCustom() {
        try {

            //a new custom dialog
            //load fxml file here
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customDialog.fxml"));
            DialogPane dialogPane = loader.load();
            CustomDialogController controller = loader.getController();

            //create new dialog here
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Custom Difficulty");
            dialog.setDialogPane(dialogPane);

            //buttons
            ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType);

            dialog.initStyle(StageStyle.UNDECORATED);

            //check if input is valid
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
                    }else if(rows < 8 || cols < 8) {
                        controller.setWarning("Size too small");
                        actionEvent.consume();
                    }
                } catch (NumberFormatException e) {
                    controller.setWarning("Not a valid number!");
                    actionEvent.consume();
                }
            });

            dialog.showAndWait().ifPresent(result -> {

                //if the result of the dialog is confirmed
                if (result == confirmButtonType) {
                    try {
                        this.row = controller.getRows();
                        this.column = controller.getCols();
                        this.mineNum = controller.getMines();
                    } catch (NumberFormatException e) {
                        //leave it empty, cause default value already exists
                    }
                }
            });
            initTiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initTiles()//Refresh the game plane
    {

        //clear existing  game status
        clicked = false;
        gameEnded = false;

        //create a new board
        board.createBoard(row,column,mineNum);

        scoreLabel.setText("Score: 0");

        //display game plane
        this.displayGamePlane();

        try {

            //set the size of the window
            Stage stage = (Stage) root.getScene().getWindow();
            //System.out.println(stage.getWidth());
            root.setPrefHeight(SETTINGS_BAR_HEIGHT + TILE_SIZE * this.row  + BOARDER_HEIGHT * 3 );
            root.setPrefWidth(TILE_SIZE * this.column  + BOARDER_WIDTH * 2 );

            upperBoarder.setFitWidth( (TILE_SIZE + 0.5)  * this.column );//This is because there are gaps between the buttons, and the gap size between two buttons is between 0-1, here I picked 0.4
            middleBoarder.setFitWidth((TILE_SIZE + 0.5)  * this.column );
            lowerBoarder.setFitWidth((TILE_SIZE + 0.5)  * this.column );

            middleLeftBoarder.setFitHeight((TILE_SIZE + 0.5) * this.row );//different coefficient for width and height
            middleRightBoarder.setFitHeight((TILE_SIZE + 0.5) * this.row );

            stage.sizeToScene();
        }catch (NullPointerException e){
            //should only be executed when app starts
        }
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
        restartButton.setGraphic(restartButtonNormalUnpressedImageView);
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
