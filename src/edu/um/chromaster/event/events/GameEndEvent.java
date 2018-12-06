package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;

/**
 * The event is fired when one of the game mode ends.
 */
public class GameEndEvent extends Event {

    private final String message;
    private final boolean win;

    /**
     *
     * @param message A custom message that is associated with the event.
     * @param win True, if the user won otherwise false.
     */
    public GameEndEvent(String message, boolean win) {
        this.message = message;
        this.win = win;
    }

    /**
     * The associated message.
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @return True, if the user won, otherwise false.
     */
    public boolean isWin() {
        return win;
    }
}
