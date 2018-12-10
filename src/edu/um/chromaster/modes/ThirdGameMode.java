package edu.um.chromaster.modes;

import edu.um.chromaster.Game;
import edu.um.chromaster.event.Subscribe;
import edu.um.chromaster.event.events.GameEndEvent;
import edu.um.chromaster.event.events.NodeClickedEvent;
import edu.um.chromaster.event.events.SelectColourEvent;
import edu.um.chromaster.graph.Graph;
import edu.um.chromaster.graph.Node;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.Stack;

public class ThirdGameMode extends GameMode {

    private Stack<Node> path = new Stack<>();

    public ThirdGameMode(Graph graph, long time) {
        super(graph, time, false);
        Game.getInstance().getEventHandler().registerListener(this);
    }

    @Override
    public void start() {
        getGraph().getNodes().values().forEach(e -> {
            e.getMeta().setAllowedToChangeColour(false);
            e.getMeta().visible(false);
        });

        for(Node n : getGraph().getNodes().values()) {
            path.push(n);
        }
        Collections.shuffle(path);

        Node node = path.peek();

        node.getMeta().hint(Color.ROYALBLUE); // TODO
        node.getMeta().visible(true);
        node.getMeta().setAllowedToChangeColour(true);
        getGraph().getEdges(node.getId()).forEach(e -> {
            e.getTo().getMeta().visible(true);
            e.getTo().getMeta().setAllowedToChangeColour(false);
        });
    }

    @Override
    public boolean gameWon() {
        return isValidColoured(true);
    }

    @Subscribe
    public void onNodeClicked(NodeClickedEvent event) {

        Node n = event.getNode();

        if(n.getMeta().isAllowedToChangeColour() && this.getSelectedColour() != null) {


            n.getMeta().hint(null); // TODO
            // we don't care if the user doesn't know the rules
            //boolean neighbourHasSelectedColour = getGraph().getEdges(n.getId()).stream().noneMatch(e -> e.getTo().getValue() == getSelectedColour().hashCode());

            event.getNode().getMeta().colour(getSelectedColour());
            event.getNode().setValue(getSelectedColour().hashCode());
            n.getMeta().setAllowedToChangeColour(false);
            n.getMeta().highlight(false);

            path.pop();

            getGraph().getEdges(n.getId()).stream().filter(e -> e.getTo().getValue() == -1).forEach(e -> e.getTo().getMeta().visible(false));

            if (!path.isEmpty()) {
                Node node = path.peek();
                node.getMeta().hint(Color.ROYALBLUE); // TODO

                node.getMeta().visible(true);
                node.getMeta().setAllowedToChangeColour(true);
                getGraph().getEdges(node.getId()).forEach(e -> {
                    e.getTo().getMeta().visible(true);
                    e.getTo().getMeta().setAllowedToChangeColour(false);
                });
            } else {
                if (gameWon()) {
                    Game.getInstance().getEventHandler().trigger(new GameEndEvent("ez pz", true));
                } else {
                    Game.getInstance().getEventHandler().trigger(new GameEndEvent("u lost", false));
                }
                System.out.println("game end - third game mode");
            }

        }

    }

    @Subscribe
    public void onSelectColour(SelectColourEvent event) {
        setSelectedColour(event.getColor());
    }

}
