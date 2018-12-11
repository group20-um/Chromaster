package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;

/**
 * The first game-mode (aka. until the bitter end).
 */
public class FirstGameMode extends GameMode {

    public FirstGameMode(Graph graph, long time) {
        super(graph, time, false);
        Game.getInstance().getEventHandler().registerListener(this);
    }

    @Override
    public void start() { }

    @Override
    public boolean gameWon() {
        return isValidColoured(true);
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {
        defaultNodeClickHandler(event);
    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        this.setSelectedColour(event.getColor());
    }
}
