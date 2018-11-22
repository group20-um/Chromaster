package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;
import javafx.scene.paint.Color;

public class SelectColourEvent extends Event {

    private final Color color;

    public SelectColourEvent(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

}
