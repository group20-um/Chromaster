package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;
import edu.um.chromaster.graph.Node;

public class NodeClickedEvent extends Event {

    private Node node;

    public NodeClickedEvent(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }
}
