package edu.um.chromaster.event;

public enum Priority {

    LOW(-1),
    NORMAL(0),
    HIGH(1);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

}
