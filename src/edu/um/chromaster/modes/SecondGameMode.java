package edu.um.chromaster.modes;


import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.GameEndEvent;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;

import java.util.concurrent.TimeUnit;


public class SecondGameMode extends GameMode {

    private long startTime = 0;

    private boolean isPlayerOutOfTime = false;

    public SecondGameMode(Graph graph, long time) {
        super(graph, time);
        Game.getInstance().getEventHandler().registerListener(this);
    }


    public long getTimeLeft() {
        return Math.max(0, this.getTime() + startTime - System.currentTimeMillis());
    }

    public long getUsedColours() {
        return getGraph().getNodes().values().stream().mapToInt(Node::getValue).distinct().sum();
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
        getGraph().getNodes().forEach((id, node) -> node.getMeta().setAllowedToChangeColour(true));
        Game.getInstance().getSchedule().schedule(() -> {
            this.isPlayerOutOfTime = true;
            getGraph().getNodes().forEach((id, node) -> node.getMeta().setAllowedToChangeColour(false));

            if(!gameWon()) {
                Game.getInstance().getEventHandler().trigger(new GameEndEvent("You lost", false));
            }
        }, this.getTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean gameWon() {
        return (isValidColoured() && isGraphFullyColoured());
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {
        if(this.getSelectedColour() != null && event.getNode().getMeta().isAllowedToChangeColour()) {
            event.getNode().getMeta().colour(this.getSelectedColour());
            event.getNode().setValue(this.getSelectedColour().hashCode());

            if(gameWon()) {
                System.out.println("YEAA - Second Game Mode");
            }
        }
    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        this.setSelectedColour(event.getColor());
    }

    private boolean isGraphFullyColoured() {
        return this.getGraph().getNodes().values().stream().anyMatch(e -> e.getValue() != -1);
    }

}
