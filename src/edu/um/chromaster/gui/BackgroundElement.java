package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.SecondGameMode;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

import java.util.concurrent.TimeUnit;

public class BackgroundElement extends Group {

    private ImageView bottom = new ImageView("res/trianglify_red.png");
    private ImageView top = new ImageView("res/trianglify_green.png");
    private ImageView defaultBackground = new ImageView("res/trianglify.png");

    public BackgroundElement(GameMode gameMode) {
        if(gameMode instanceof SecondGameMode) {
            Game.getInstance().getSchedule().scheduleAtFixedRate(this::draw, 0, (long) (1000 / 60D), TimeUnit.MILLISECONDS);
            this.getChildren().addAll(bottom, top);
        } else {
            this.getChildren().add(defaultBackground);
        }
    }

    public void draw() {

        if(Game.getInstance().getGameMode() instanceof SecondGameMode) {
            Platform.runLater(() -> {
                SecondGameMode gm = (SecondGameMode) Game.getInstance().getGameMode();
                ColorAdjust colorAdjust = new ColorAdjust();
                double r = ((double) gm.getTimeLeft() / (double) gm.getTime());
                top.setBlendMode(BlendMode.OVERLAY);
                top.setOpacity(r);
                bottom.setOpacity(1 - r);
                this.setEffect(colorAdjust);
            });
        }

    }

}
