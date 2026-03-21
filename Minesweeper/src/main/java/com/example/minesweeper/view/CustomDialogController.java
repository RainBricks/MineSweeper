package com.example.minesweeper.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CustomDialogController {
    public Label warningLabel;
    @FXML
    private TextField rowField;

    @FXML
    private TextField colField;

    @FXML
    private TextField mineField;

    public int getRows() {
        return Integer.parseInt(rowField.getText());
    }

    public int getCols() {
        return Integer.parseInt(colField.getText());
    }

    public int getMines() {
        return Integer.parseInt(mineField.getText());
    }

    public  void setWarning(String warning) {
        warningLabel.setText(warning);
    }
}