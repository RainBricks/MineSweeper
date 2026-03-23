package com.example.minesweeper.view;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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

    @FXML
    private CheckBox mineClearanceBox;

    @FXML
    private CheckBox radarBox;

    @FXML
    private CheckBox miniMineBox;


    public int getRows() {
        return Integer.parseInt(rowField.getText());
    }

    public int getCols() {
        return Integer.parseInt(colField.getText());
    }

    public int getMines() {
        return Integer.parseInt(mineField.getText());
    }

    public boolean isRadar() {
        return radarBox.isSelected();
    }

    public boolean isMineClearance() {
        return mineClearanceBox.isSelected();
    }

    public boolean isMiniMine() {
        return miniMineBox.isSelected();
    }

    public void setWarning(String warning) {
        warningLabel.setText(warning);
    }
}