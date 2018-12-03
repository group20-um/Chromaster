package edu.um.chromaster.modes;

import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

public abstract class GameMode implements EventListener {

    private Graph graph;
    private boolean showColourSelector = false;
    private boolean showCreateColour = false;

    private Color selectedColour = null;

    public GameMode(Graph graph, boolean showColourSelector, boolean showCreateColour) {
        this.graph = graph;
        graph.reset();

        this.showColourSelector = showColourSelector;
        this.showCreateColour = showCreateColour;
    }

    public long getUsedColours() {
        return this.graph.getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).distinct().count();
    }

    public boolean isValidColoured() {
        System.out.println("A +" + graph.getNodes().values().stream()
                .noneMatch((node) -> graph.getEdges(node.getId()).stream().anyMatch(e -> e.getTo().getValue() == node.getValue() || node.getValue() == -1)));
        System.out.println("B + " + (getUsedColours() == graph.getChromaticResult().getExact()));
        return graph.getNodes().values().stream()
                .noneMatch((node) -> graph.getEdges(node.getId()).stream().anyMatch(e -> e.getTo().getValue() == node.getValue() || node.getValue() == -1)) &&
                getUsedColours() == graph.getChromaticResult().getExact();
    }

    public Color getSelectedColour() {
        return selectedColour;
    }

    public boolean isShowColourSelector() {
        return showColourSelector;
    }

    public boolean isShowCreateColour() {
        return showCreateColour;
    }

    public Graph getGraph() {
        return graph;
    }


    public double getScore() {
        return 1D;
    }

    public void setSelectedColour(Color selectedColour) {
        this.selectedColour = selectedColour;
    }

    public abstract void start();

    public abstract boolean gameWon();


}
