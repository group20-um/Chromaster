package edu.um.chromaster.modes;

import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.graph.Graph;

public abstract class GameMode implements EventListener {

    private Graph graph;

    public GameMode(Graph graph) {
        this.graph = graph;
        graph.reset();
    }

    public Graph getGraph() {
        return graph;
    }

    public abstract void start();

    public abstract boolean isGameOver();

    @Subscribe
    public abstract void onNodeClicked(NodeClickedEvent event);

}
