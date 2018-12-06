package edu.um.chromaster.modes;

import edu.um.chromaster.event.EventListener;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

/**
 * The overall GameMode class that manages some of the GameMode aspects.
 */
public abstract class GameMode implements EventListener {

    private Graph graph;
    private Color selectedColour = null;

    private long time;

    private boolean displayTimer;

    /**
     *
     * @param graph The graph that is going to be used in this game-mode.
     * @param time Usage of this value depends on each game mode. It needs to be specified in milli-seconds.
     */
    public GameMode(Graph graph, long time, boolean displayTimer) {
        this.graph = graph;
        this.time  = time;
        this.displayTimer = displayTimer;
        graph.reset();
    }

    /**
     * The associated the values.
     * @return
     */
    public long getTime() {
        return time;
    }

    /**
     * Whether or not {@link edu.um.chromaster.gui.GameBar} should show a timer displaying the value of {@link GameMode#getTime()}.
     * @return The time value in milliseconds.
     */
    public boolean isDisplayingTime() {
        return this.displayTimer;
    }

    /**
     * The amount of unique colours the user already used in the graph.
     * @return
     */
    public long getUsedColours() {
        return this.graph.getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).distinct().count();
    }

    /**
     * Checks: (1) if all nodes are coloured, (2) no node has a neighbour with the same colour as itself, and (3)
     * if the value of {@link GameMode#getUsedColours()} matches the exact chromatic number.
     * @param checkChromaticNumber True, to also check for the (3) condition, false if not.
     * @return
     */
    public boolean isValidColoured(boolean checkChromaticNumber) {
        boolean status = graph.getNodes().values().stream()
                .noneMatch((node) -> graph.getEdges(node.getId()).stream().anyMatch(e -> e.getTo().getValue() == node.getValue() || node.getValue() == -1));

        if(checkChromaticNumber) {
            status = status && (getUsedColours() == graph.getChromaticResult().getExact());
        }

        return status;
    }

    /**
     * Gets the currently selected colour that the user can use to change the colour of a node.
     * @return
     */
    public Color getSelectedColour() {
        return selectedColour;
    }

    /**
     * The associated graph.
     * @return
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Sets the new selected colour that is then available to the user to change the colour of a node.
     * @param selectedColour
     */
    public void setSelectedColour(Color selectedColour) {
        this.selectedColour = selectedColour;
    }

    /**
     * Called when the game starts.
     */
    public abstract void start();

    /**
     * Called to check if the user won.
     * @return True, if the user won, otherwise false.
     */
    public abstract boolean gameWon();

    public long getTimeLeft() {
        return this.getTime();
    }

}
