package com.example.minesweeper.tileview;

import com.example.minesweeper.enums.TileStatus;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;


public class TileView extends Button {
    public TileView()
    {
        super();
        super.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(192,192,192),//color gray
                                new CornerRadii(1),
                                new Insets(1)
                        )
                )
        );

    }

    public void update(int minesAround)
    {
        super.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.rgb(255,255,255),//color white
                                new CornerRadii(1),//corner radius
                                new Insets(1)//padding
                        )
                )
        );
        if(minesAround != 0)
        {
            super.setText(String.valueOf(minesAround));
        }
        System.out.println("TileView::update complete");

    }

    public void update(TileStatus status)
    {

        if(status == TileStatus.flagged)
        {
            super.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.rgb(255,255,255), //color white
                                    new CornerRadii(1),
                                    new Insets(1)
                            )
                    )
            );
            super.setText("F");
            System.out.println("TileView::update complete");
            return;
        }

        if(status == TileStatus.closed)
        {
            super.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.rgb(192,192,192),//color gray
                                    new CornerRadii(1),
                                    new Insets(1)
                            )
                    )
            );
            super.setText("");
            System.out.println("TileView::update complete");
            return;
        }

        if(status == TileStatus.triggered)
        {
            super.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.rgb(255,192,192),//color pink
                                    new CornerRadii(1),
                                    new Insets(1)
                            )
                    )
            );
            super.setText("B");
            System.out.println("TileView::update complete");
            return;
        }

        if(status == TileStatus.exploded)
        {
            super.setBackground(
                    new Background(
                            new BackgroundFill(
                                    Color.rgb(255,0,0),//color red
                                    new CornerRadii(1),
                                    new Insets(1)
                            )
                    )
            );
            super.setText("B");
            System.out.println("TileView::update complete");
            return;
        }
    }


}
