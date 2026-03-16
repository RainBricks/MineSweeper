module com.example.minesweeper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports com.example.minesweeper;

    opens com.example.minesweeper to javafx.fxml;
    opens com.example.minesweeper.controller to javafx.fxml;
}