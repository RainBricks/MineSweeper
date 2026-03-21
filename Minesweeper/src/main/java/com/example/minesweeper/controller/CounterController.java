package com.example.minesweeper.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class CounterController extends StackPane {

    Image[] digits;

    @FXML
    ImageView digitOnes;

    @FXML
    ImageView digitTens;

    @FXML
    ImageView digitHundreds;

    public CounterController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Counter.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        initDigits();
    }

    public void initDigits()
    {
        digits = new Image[10];
        for(int i = 0; i <= 9; i++)
        {
            digits[i] = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/d" + i + ".png")));
        }

    }

    public void setNum(int num) {
        digitOnes.setImage(digits[num%10]);
        digitTens.setImage(digits[num/10%10]);
        digitHundreds.setImage(digits[num/100%10]);
    }
}
