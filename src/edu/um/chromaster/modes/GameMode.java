package edu.um.chromaster.modes;

import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.graph.Graph;
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

    public void setSelectedColour(Color selectedColour) {
        this.selectedColour = selectedColour;
    }

    public abstract void start();

    public abstract boolean gameWon();


}
