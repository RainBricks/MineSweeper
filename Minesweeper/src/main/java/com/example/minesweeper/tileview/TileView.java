package com.example.minesweeper.tileview;

import com.example.minesweeper.enums.TileStatus;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;


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

        this.setImageWithStatus(TileStatus.closed);
    }

    public void update(int minesAround)
    {

        this.setImageWithNumber(minesAround);
        System.out.println("TileView::update complete");

    }

    public void update(TileStatus status)
    {

        this.setImageWithStatus(status);

    }

    private void setImageWithNumber(int minesAround)
    {
        InputStream input = this.getClass().getResourceAsStream("/images/" +  minesAround + ".gif");
        if(input == null)
        {
            System.out.println("TileView::setImage failed: path is not found");
            return ;
        }

        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);

        imageView.setPreserveRatio(false);

        super.setGraphic(imageView);
    }

    private void setImageWithStatus(TileStatus status)
    {

        InputStream input = this.getClass().getResourceAsStream("/images/" +  status + ".gif");
        if(input == null)
        {
            System.out.println("TileView::setImage failed: path is not found");
            return ;
        }

        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(CELL_SIZE);
        imageView.setFitHeight(CELL_SIZE);

        imageView.setPreserveRatio(false);

        super.setGraphic(imageView);
    }


}
