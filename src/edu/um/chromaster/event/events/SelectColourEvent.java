package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;
import javafx.scene.paint.Color;

/**
 * Fired when a colour got selected.
 */
public class SelectColourEvent extends Event {

    private final Color color;

    /**
     *
     * @param color The colour that got selected.
     */
    public SelectColourEvent(Color color) {
        this.color = color;
    }

    /**
     *
     * @return The colour that got selected.
     */
    public Color getColor() {
        return this.color;
    }

}
