package edu.um.chromaster.modes;

import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

public abstract class GameMode implements EventListener {

    private Graph graph;
    private Color selectedColour = null;

    private long time;

    public GameMode(Graph graph, long time) {
        this.graph = graph;
        this.time  = time;
        graph.reset();

    }

    public long getTime() {
        return time;
    }

    public long getUsedColours() {
        return this.graph.getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).distinct().count();
    }

    public boolean isValidColoured() {
        return graph.getNodes().values().stream()
                .noneMatch((node) -> graph.getEdges(node.getId()).stream().anyMatch(e -> e.getTo().getValue() == node.getValue() || node.getValue() == -1)) &&
                getUsedColours() == graph.getChromaticResult().getExact();
    }

    public Color getSelectedColour() {
        return selectedColour;
    }

    public Graph getGraph() {
        return graph;
    }


    public void setSelectedColour(Color selectedColour) {
        this.selectedColour = selectedColour;
    }

    public abstract void start();

    public abstract boolean gameWon();

}
