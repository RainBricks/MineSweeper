package com.example.minesweeper.controller;

import com.example.minesweeper.board.Board;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;


import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final Board board;

    private boolean clicked;

    private int row;
    private int column;
    private int mineNum;

    /*
    @FXML
    private AnchorPane topSection;

    @FXML
    private AnchorPane bottomSection;
    */


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


    public void gameOver()
    {
        gameGridPane.getChildren().clear();
        gameGridPane.add(new Label("Game Over!"), 0, 0);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        initTiles();


        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (_, oldValue, newValue) -> {
                    if(oldValue.equals(newValue))return;
                    System.out.println("Change Difficulty: " + oldValue + " -> " + newValue);
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

    public void initTiles()//Refresh the game plane
    {
        //clear existing  game status
        clicked = false;

        //create a new board
        board.createBoard(row,column,mineNum);

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


    public void click(int x,int y)
    {
        if(!board.getTileAt(x,y).click())
        {
            this.gameOver();
        }
    }

    public void flag(int x,int y)
    {
        board.getTileAt(x,y).flag();
    }



    public void tileClick(MouseEvent event)//Handle click and flag event !!flag event handling to be done
    {

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
