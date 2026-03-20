package com.example.minesweeper.tileview;

import com.example.minesweeper.enums.TileStatus;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



public class TileView extends Button {


    private static final int CELL_SIZE = 24;


    public TileView()
    {
        super();

        super.setMinSize(CELL_SIZE, CELL_SIZE);
        super.setPrefSize(CELL_SIZE, CELL_SIZE);
        super.setMaxSize(CELL_SIZE, CELL_SIZE);


        super.setStyle(
                "-fx-padding: 0;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-width: 0;" +
                        "-fx-border-insets: 0;"
        );

        this.setImage(TileStatus.closed);
    }

    public void update(int minesAround)
    {

        this.setImage(minesAround);
        System.out.println("TileView::update complete");

    }

    public void update(TileStatus status)
    {

        this.setImage(status);

    }

    private void setImage(int minesAround)
    {
        Image image = new Image(getClass().getResourceAsStream("/images/" +  minesAround + ".gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);

        imageView.setPreserveRatio(false);

        super.setGraphic(imageView);
    }

    private void setImage(TileStatus status)
    {
        Image image = new Image(getClass().getResourceAsStream("/images/" +  status + ".gif"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);

        imageView.setPreserveRatio(false);

        super.setGraphic(imageView);
    }


}
