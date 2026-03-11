package com.example.minesweeper;

import com.example.minesweeper.board.Board;
import com.example.minesweeper.controller.Controller;
import com.example.minesweeper.executor.Executor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private Controller controller;
    private Executor executor;
    private Board board;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        board = Board.getBoard();

        executor = new Executor(board);

        board.setExecutor(executor);

        controller = loader.getController();

        executor.setController(controller);

        controller.setExecutor(executor);

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
