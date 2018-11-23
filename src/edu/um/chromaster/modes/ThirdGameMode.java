package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import edu.um.chromaster.gui.ColorList;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.Stack;

public class ThirdGameMode extends GameMode {

    private Stack<Node> path = new Stack<>();

    public ThirdGameMode(Graph graph) {
        super(graph, true, true);
        Game.getEventHandler().registerListener(this);
    }

    @Override
    public void start() {
        getGraph().getNodes().values().forEach(e -> {
            e.getMeta().setAllowedToChangeColour(false);
            e.getMeta().visible(false);
        });

        path.addAll(getGraph().getNodes().values());
        Collections.shuffle(path, Game.random);

        Node node = path.peek();
        node.getMeta().visible(true);
        node.getMeta().setAllowedToChangeColour(true);
        getGraph().getEdges(node.getId()).forEach(e -> {
            e.getTo().getMeta().visible(true);
            e.getTo().getMeta().setAllowedToChangeColour(false);
        });
    }

    @Override
    public double getScore() {
        return (getGraph().getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).count());
    }

    @Override
    public boolean gameWon() {
        return isValidColoured() && getGraph().getNodes().values().stream().filter(e -> e.getValue() != -1).mapToInt(Node::getValue).count() == getGraph().getNodes().size();
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {

        Node n = event.getNode();

        if(n.getMeta().isAllowedToChangeColour() && this.getSelectedColour() != null) {

            event.getNode().getMeta().colour(getSelectedColour());
            event.getNode().setValue(getSelectedColour().hashCode());

            n.getMeta().setAllowedToChangeColour(false);

            // TODO fix the comparision
            getGraph().getEdges(n.getId()).stream().filter(e -> e.getTo().getMeta().colour() == ColorList.NODE_INNER_DEFAULT).forEach(e -> e.getTo().getMeta().visible(false));

            path.pop();

            if(!path.isEmpty()) {
                Node node = path.peek();

                node.getMeta().visible(true);
                node.getMeta().setAllowedToChangeColour(true);
                getGraph().getEdges(node.getId()).forEach(e -> {
                    e.getTo().getMeta().visible(true);
                    e.getTo().getMeta().setAllowedToChangeColour(false);
                });
            }
        }

    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        setSelectedColour(event.getColor());
    }

}
