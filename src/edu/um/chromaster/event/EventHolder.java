package edu.um.chromaster.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An EventHolder is a class to structure all registered events.
 */
public class EventHolder {

    private final EventListener listener;
    private final Method method;
    private final Subscribe subscribe;

    /**
     *
     * @param listener The associated listener.
     * @param method The associated method.
     * @param subscribe The Subscribe annotation.
     */
    EventHolder(EventListener listener, Method method, Subscribe subscribe) {
        this.listener = listener;
        this.method = method;
        this.subscribe = subscribe;
    }

    void execute(Event event) {
        try {
            this.method.setAccessible(true);
            this.method.invoke(this.listener, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            //@TODO Handle... or do whatever is appropriate.
            e.printStackTrace();
        }
    }

}
