package com.example.minesweeper.tileview;

import com.example.minesweeper.enums.TileStatus;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

import static com.example.minesweeper.consts.UIConsts.TILE_SIZE;

public class TileView extends Button {

    public TileView() {
        super();
        this.setTileStyle();
        this.setImageWithStatus(TileStatus.closed);
    }

    public void update(int minesAround) {
        this.setImageWithNumber(minesAround);
        //System.out.println("TileView::update complete");
    }

    public void update(TileStatus status) {
        this.setImageWithStatus(status);
    }

    public void playRadarAnime() {
        this.setImage("radar.gif");
    }

    public void playMinusAnime() {this.setImage("minus.gif");}

    public void playShieldAnime() {this.setImage("shield.gif");}

    private void setImageWithNumber(int minesAround) {
        this.setImage(minesAround + ".png");
    }

    private void setImageWithStatus(TileStatus status) {
        this.setImage(status + ".png");
    }

    private void setImage(String imageName) {
        InputStream input = this.getClass().getResourceAsStream("/images/" + imageName);
        if (input == null) {
            System.out.println("TileView::setImage failed: path is not found for " + imageName);
            return;
        }

        Image image = new Image(input);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(TILE_SIZE);
        imageView.setFitHeight(TILE_SIZE);
        imageView.setPreserveRatio(false);

        super.setGraphic(imageView);
    }


    private void setTileStyle() {
        super.setMinSize(TILE_SIZE, TILE_SIZE);
        super.setPrefSize(TILE_SIZE, TILE_SIZE);
        super.setMaxSize(TILE_SIZE, TILE_SIZE);
        super.setStyle(
                "-fx-padding: 0;" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-insets: 0;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-width: 0;" +
                        "-fx-border-insets: 0;"
        );
    }
}