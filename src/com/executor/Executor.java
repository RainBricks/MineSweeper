package java.executor;

import javafx.fxml.Initializable;

import java.board.Board;
import java.controller.Controller;


public class Executor{
    private Board board;
    private int score;
    private Controller controller;

    public Executor(Board board) {
        this.board = board;
        score = 0;
    }

    public void click(int x,int y)
    {
        if(!board.getTileAt(x,y).click())
        {
            controller.gameOver();
        }
        else {
            controller.setTileAt(x,y,board.getTileAt(x,y).getStatus(),board.getTileAt(x,y).getMinesAround());
        }
    }

    public void flag(int x,int y)
    {
        board.getTileAt(x,y).flag();
        controller.setTileAt(x,y,board.getTileAt(x,y).getStatus());
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }




}
