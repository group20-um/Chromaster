package edu.um.chromaster.modes;

import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

public class FirstGameMode extends GameMode {

    public FirstGameMode(Graph graph) {
        super(graph);
    }

    @Override
    public void start() {
        getGraph().reset();
    }

    @Override
    public boolean isGameOver() {
        return getGraph().getChromaticNumber() != getGraph().getNodes().values().stream().mapToInt(Node::getValue).distinct().sum();
    }

    @Override
    public void onNodeClicked(NodeClickedEvent event) {

    }
}
