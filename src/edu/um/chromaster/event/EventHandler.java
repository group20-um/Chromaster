package edu.um.chromaster.event;

import java.lang.reflect.Method;
import java.util.*;

public class EventHandler {

    private Map<Class<? extends Event>, List<EventHolder>> events = new HashMap<>();
    private List<EventListener> eventListeners = new ArrayList<>();


    public EventHandler() {}

    public boolean trigger(Event event) {

        if(!(this.events.containsKey(event.getClass()))) {
            return false;
        }

        this.events.get(event.getClass()).forEach(e -> e.execute(event));
        return true;

    }

    public boolean registerListener(EventListener listener) {

        if(this.eventListeners.contains(listener)) {
            return false;
        }

        this.eventListeners.add(listener);

        Method[] methods = listener.getClass().getMethods();
        for(Method method : methods) {

            Subscribe subscribe = method.getAnnotation(Subscribe.class);

            if (
                    (subscribe == null) ||
                            (!(method.getParameterTypes().length == 1)) ||
                            (!(method.getReturnType().equals(void.class))) ||
                            (!(Event.class.isAssignableFrom(method.getParameterTypes()[0])))
                    ) {
                continue;
            }

            Class<? extends Event> argument = (Class<? extends Event>) method.getParameterTypes()[0];

            if(!(this.events.containsKey(argument))) {
                this.events.put(argument, new LinkedList<>());
            }

            this.events.get(argument).add(new EventHolder(
                    listener,
                    method,
                    subscribe
            ));

        }

        return true;

    }

}
