package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.GameEndEvent;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.gui.ColorList;

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

        if(this.getSelectedColour() != null) {

            System.out.println(event.getNode().getMeta().colour() + " - " + this.getSelectedColour());
            if(event.getNode().getMeta().colour().equals(this.getSelectedColour())) {
                event.getNode().getMeta().colour(ColorList.NODE_INNER_DEFAULT);
                return;
            }

            event.getNode().getMeta().colour(this.getSelectedColour());
            event.getNode().setValue(this.getSelectedColour().hashCode());

            if(this.gameWon()) {
                Game.getInstance().getEventHandler().trigger(new GameEndEvent("YOU! ARE! SO! SMART!", true));
                System.out.println("GAME WON - First Game Mode");
            }
        }
    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        this.setSelectedColour(event.getColor());
    }
}
