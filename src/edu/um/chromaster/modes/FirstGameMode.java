package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

public class FirstGameMode extends GameMode {

    Color selectedColour;

    public FirstGameMode(Graph graph) {
        super(graph, true, true);
        Game.getEventHandler().registerListener(this);
    }

    @Override
    public void start() { }

    @Override
    public boolean gameWon() {

        if(getGraph().getNodes().values().stream().anyMatch(e -> e.getValue() == -1)) {
            return false;
        }
        return isValidColoured() && getGraph().getChromaticResult().getExact() == getGraph().getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).distinct().count();
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {
        event.getNode().getMeta().colour(selectedColour);
        event.getNode().setValue(selectedColour.hashCode());
    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        selectedColour = event.getColor();
    }
}
