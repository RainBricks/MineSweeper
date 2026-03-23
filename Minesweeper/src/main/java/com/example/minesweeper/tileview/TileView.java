package com.example.minesweeper.tileview;

import com.example.minesweeper.enums.TileStatus;
import javafx.scene.control.Button;
import java.util.ArrayList;
import java.util.List;

import static com.example.minesweeper.consts.UIConsts.TILE_SIZE;

public class TileView extends Button {

    private String currentStatusClass;

    public TileView() {
        super();

        //this is not implemented in css because it might cause a conflict
        this.setTileSize();

        //current styleclass is null, so anything added will become father styleclass
        this.getStyleClass().add("tile");

        this.update(TileStatus.closed);
    }

    public void update(int minesAround) {
        applyStatusStyle("tile-num-" + minesAround);
    }

    public void update(TileStatus status) {
        applyStatusStyle("tile-" + status.name());
    }

    public void playRadarAnime()  { applyStatusStyle("tile-radar"); }
    public void playMinusAnime()  { applyStatusStyle("tile-minus"); }
    public void playShieldAnime() { applyStatusStyle("tile-shield"); }

    private void applyStatusStyle(String newStyleClass) {
        if (currentStatusClass != null) {
            this.getStyleClass().remove(currentStatusClass);
        }

        if (!this.getStyleClass().contains(newStyleClass)) {
            this.getStyleClass().add(newStyleClass);
        }

        this.currentStatusClass = newStyleClass;

    }

    private void setTileSize() {
        this.setMinSize(TILE_SIZE, TILE_SIZE);
        this.setPrefSize(TILE_SIZE, TILE_SIZE);
        this.setMaxSize(TILE_SIZE, TILE_SIZE);
    }
}