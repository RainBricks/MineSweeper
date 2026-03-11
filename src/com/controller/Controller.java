package java.controller;

import javafx.fxml.Initializable;

import java.enums.TileStatus;
import java.executor.Executor;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Executor executor;


    public Controller(Executor executor) {
        this.executor = executor;
    }

    public void setTileAt(int x, int y, TileStatus status,int num)
    {

    }

    public void setTileAt(int x, int y, TileStatus status)
    {

    }

    public void gameOver()
    {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

    }
}
