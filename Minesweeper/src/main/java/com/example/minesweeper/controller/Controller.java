package com.example.minesweeper.controller;

import com.example.minesweeper.board.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import com.example.minesweeper.enums.TileStatus;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Board board;

    private boolean clicked;

    @FXML
    private AnchorPane topSection;

    @FXML
    private AnchorPane bottomSection;

    @FXML
    private GridPane gameGridPane;

    @FXML
    private ComboBox<String> difficulty;


    public Controller()
    {
    }

    public void setBoard(Board board) {
        this.board = board;
    }


    public void setTileAt(int x, int y, TileStatus status, int num)
    {
        if(status == TileStatus.flagged)return;
        Button but = new Button();
        if(num != 0) but.setText(String.valueOf(num));
        else but.setText("O");
        but.setOnAction(this::tileClick);
        gameGridPane.add(but,y,x);

    }

    public void setTileAt(int x, int y, TileStatus status)
    {
        if(status == TileStatus.flagged)return;
        Button but = new Button("o");
        but.setOnAction(this::tileClick);
        gameGridPane.add(but,y,x);
    }

    public void gameOver()
    {
        gameGridPane.getChildren().clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        initTiles("Easy");


        difficulty.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if(oldValue.equals(newValue))return;
                    System.out.println("Change Difficulty: " + oldValue + " -> " + newValue);
                    initTiles(newValue);
                }
        );
    }

    public void initTiles(String difficulty)//Refresh the game plane
    {
        int row,column;
        column = switch (difficulty) {
            case "Easy" -> {
                row = 8;
                yield 8;
            }
            case "Medium" -> {
                row = 16;
                yield 16;
            }
            case "Hard" -> {
                row = 16;
                yield 30;
            }
            default -> {
                row = 8;
                yield 8;
            }
        };
        gameGridPane.getChildren().clear();
        clicked = false;
        for(int i = 0;i < row;i++)
        {
            for(int j = 0;j < column;j++)
            {
                Button btn = new Button(" ");
                btn.setUserData(new int[]{i,j});
                btn.setOnAction(this::tileClick);
                gameGridPane.add(btn,j,i);
            }
        }
    }


    public void click(int x,int y)
    {
        if(!board.getTileAt(x,y).click())
        {
            this.gameOver();
        }
        else {
            this.setTileAt(x,y,board.getTileAt(x,y).getStatus(),board.getTileAt(x,y).getMinesAround());
        }
    }

    public void flag(int x,int y)
    {
        board.getTileAt(x,y).flag();
        this.setTileAt(x,y,board.getTileAt(x,y).getStatus());
    }


    public void startNewGame(String difficulty,int x,int y)
    {
        System.out.println("Start new game");
        board.createBoard(difficulty,x,y);
    }

    public void tileClick(ActionEvent event)//Handle click and flag event !!flag event handling to be done
    {
        Button btn = (Button) event.getSource();
        int[] pos = (int[]) btn.getUserData();
        System.out.println("Click at " + pos[0] +" , " + pos[1]);
        if(!clicked)
        {
            this.startNewGame(difficulty.getValue(),pos[0],pos[1]);
            clicked = true;
        }
        this.click(pos[0],pos[1]);
    }

}
