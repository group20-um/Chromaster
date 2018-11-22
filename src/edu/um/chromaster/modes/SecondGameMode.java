package edu.um.chromaster.modes;


import edu.um.chromaster.event.events.NodeClickedEvent;
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
        super(graph);
        this.timeInSeconds = timeInSeconds;
    }

    public int getUsedColours() {
        return getGraph().getNodes().values().stream().mapToInt(Node::getValue).distinct().sum();
    }

    @Override
    public void start() {
        this.schedule.schedule(() -> {
            this.isPlayerOutOfTime = true;
            getGraph().getNodes().forEach((id, node) -> node.getMeta().colour(Color.RED));
        }, this.timeInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean isGameOver() {
        return (this.isPlayerOutOfTime && !isGraphFullyColoured());
    }

    @Override
    public void onNodeClicked(NodeClickedEvent event) {

    }

    private boolean isGraphFullyColoured() {
        return this.getGraph().getNodes().values().stream().anyMatch(e -> e.getValue() != -1);
    }

}
