package edu.um.chromaster.gui;

import edu.um.chromaster.Game;
import edu.um.chromaster.modes.SecondGameMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BackgroundElement extends ImageView {

    private ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(1);

    public BackgroundElement() {
        super("res/trianglify.png");
        pool.scheduleAtFixedRate(this::draw, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void draw() {

        if(Game.getInstance().getGameMode() instanceof SecondGameMode) {
            SecondGameMode gm = (SecondGameMode) Game.getInstance().getGameMode();
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-((double) gm.getTimeLeft() / gm.getTime()));
            this.setEffect(colorAdjust);
        }

    }

}
