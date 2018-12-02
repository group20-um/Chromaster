package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;


public class FirstGameMode extends GameMode {

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
        System.out.println("isValidColoured? " + isValidColoured());
        System.out.println("expected: " + getGraph().getChromaticResult().getExact() + " got: " + getGraph().getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).distinct().count());
        return isValidColoured() && getGraph().getChromaticResult().getExact() == getGraph().getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).distinct().count();
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {

        if(this.getSelectedColour() != null) {
            event.getNode().getMeta().colour(this.getSelectedColour());
            event.getNode().setValue(this.getSelectedColour().hashCode());

            if(this.gameWon()) {
                System.out.println("GAME WON - First Game Mode");
            }
        }
    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        this.setSelectedColour(event.getColor());
    }
}
