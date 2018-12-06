package edu.um.chromaster.event.events;

import edu.um.chromaster.event.Event;
import edu.um.chromaster.graph.Node;

/**
 * Fired when one node was clicked.
 */
public class NodeClickedEvent extends Event {

    private Node node;

    /**
     *
     * @param node The node that was clicked.
     */
    public NodeClickedEvent(Node node) {
        this.node = node;
    }

    /**
     * The node that was clicked.
     * @return
     */
    public Node getNode() {
        return node;
    }
}
