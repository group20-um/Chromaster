package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.modes.GameMode;
import edu.um.chromaster.modes.SecondGameMode;
import javafx.application.Platform;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.concurrent.TimeUnit;

/**
 * The GameBackgroundElement is responsible to display the correct background for every-gamemode.
 */
public class GameBackgroundElement extends StackPane {

    private ImageView bottom = new ImageView("res/trianglify_red.png");
    private ImageView top = new ImageView("res/trianglify_green.png");
    private ImageView defaultBackground = new ImageView("res/trianglify.png");

    public GameBackgroundElement(GameMode gameMode) {
        //--- The second game mode has a special-background, it blends in a linear fashion from the green-version to the
        // red-version of the background to indicate the time the user has left to colour the graph.
        if(gameMode instanceof SecondGameMode) {
            Game.getInstance().getSchedule().scheduleAtFixedRate(this::draw, 0, (long) (1000 / 60D), TimeUnit.MILLISECONDS);
            this.getChildren().addAll(bottom, top);
        }
        //--- No special treatment, just set the default background.
        else {
            this.getChildren().add(defaultBackground);
        }
    }

    private void draw() {

        if(Game.getInstance().getGameElement().getGameMode() instanceof SecondGameMode) {
            Platform.runLater(() -> {
                SecondGameMode gm = (SecondGameMode) Game.getInstance().getGameElement().getGameMode();
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
