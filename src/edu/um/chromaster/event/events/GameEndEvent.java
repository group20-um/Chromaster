package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;

public class GameEndEvent extends Event {

    private final String message;

    public GameEndEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
