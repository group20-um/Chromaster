package edu.um.chromaster.modes;


import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class SecondGameMode extends GameMode {

    private ScheduledThreadPoolExecutor schedule = new ScheduledThreadPoolExecutor(1);
    private int timeInSeconds;

    private boolean isPlayerOutOfTime = false;

    public SecondGameMode(Graph graph, int timeInSeconds) {
        super(graph, true, true);
        this.timeInSeconds = timeInSeconds;
        Game.getEventHandler().registerListener(this);
    }

    public long getUsedColours() {
        return getGraph().getNodes().values().stream().mapToInt(Node::getValue).distinct().sum();
    }

    @Override
    public void start() {
        this.schedule.schedule(() -> {
            this.isPlayerOutOfTime = true;
            getGraph().getNodes().forEach((id, node) -> node.getMeta().setAllowedToChangeColour(false));
        }, this.timeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean gameWon() {
        return (this.isPlayerOutOfTime && isValidColoured() && !isGraphFullyColoured());
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {
        if(this.getSelectedColour() != null && event.getNode().getMeta().isAllowedToChangeColour()) {
            event.getNode().getMeta().colour(this.getSelectedColour());
            event.getNode().setValue(this.getSelectedColour().hashCode());
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
