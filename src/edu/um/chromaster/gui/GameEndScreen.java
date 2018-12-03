package edu.um.chromaster.gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GameEndScreen extends StackPane {

    private Text text = new Text();

    public GameEndScreen() {
        StackPane.setAlignment(text, Pos.CENTER);
        text.setFont(new Font("Comic Sans", 40));
        this.setBackground(new Background(new BackgroundFill(Color.color(0, 1, 0, 0.6), null, null)));
        this.getChildren().add(text);
    }

    public void setText(String message) {
        Platform.runLater(() -> {
            text.setText(message);
        });
    }

}
