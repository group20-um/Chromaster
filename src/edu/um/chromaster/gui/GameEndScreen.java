package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.events.GameEndEvent;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.TimeUnit;

public class GameEndScreen extends StackPane {

    private ImageView background = new ImageView("res/game_end.png");

    private GameEndEvent gameEndEvent;
    private Text text = new Text();

    private long startTime;
    private double transitionTime = TimeUnit.SECONDS.toMillis(3);

    public GameEndScreen() {
        text.setFont(new Font("Times New Roman", 20));
        StackPane.setAlignment(text, Pos.CENTER);
        this.getChildren().addAll(background, text);
        this.setVisible(false);
    }

    public void execute(GameEndEvent gameEndEvent) {
        this.setVisible(true);

        this.text.setText(gameEndEvent.getMessage());

        this.startTime = System.currentTimeMillis();
        Game.getInstance().getSchedule().scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                final double opacity = Math.min((System.currentTimeMillis() - startTime) / transitionTime, 1);
                text.opacityProperty().setValue(opacity);
                background.setOpacity(opacity);

                //background.setFitHeight(this.getLayoutBounds().getHeight() * opacity);
                //background.setFitWidth(this.getLayoutBounds().getWidth() * opacity);
            });
        }, 0, (long) (1000 / 60D), TimeUnit.MILLISECONDS);
    }

}
