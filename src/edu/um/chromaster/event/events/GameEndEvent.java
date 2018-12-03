package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;

public class GameEndEvent extends Event {

    private final String message;
    private final boolean win;

    public GameEndEvent(String message, boolean win) {
        this.message = message;
        this.win = win;
    }

    public String getMessage() {
        return message;
    }

    public boolean isWin() {
        return win;
    }
}
